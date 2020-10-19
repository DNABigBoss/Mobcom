package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.model.UserData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.GetUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private EditText editText;
    private EditText pass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.daftar);
        button = findViewById(R.id.login);
        editText = findViewById(R.id.editTextLogin);
        pass = findViewById(R.id.loginPass);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaftar();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtLogin = editText.getText().toString().trim();
                String txtPass = pass.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(txtLogin != null && txtPass != null) {
                    progressDialog = new ProgressDialog(login.this);
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();
                    if(txtLogin.matches(emailPattern)) {
                        GetUser getUser = ApiClient.getRetrofitInstance().create(GetUser.class);
                        Call<List<UserData>> call = getUser.getUser(null, txtLogin, txtPass);
                        call.enqueue(new Callback<List<UserData>>() {
                            @Override
                            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                                progressDialog.dismiss();
                                String code = String.valueOf(response.code());
                                switch (code) {
                                    case "200":
                                        openMain();
                                    case "400":
                                        Toast.makeText(login.this, code, Toast.LENGTH_SHORT).show();
                                    case "404":
                                        Toast.makeText(login.this, code, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<UserData>> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        GetUser getUser = ApiClient.getRetrofitInstance().create(GetUser.class);
                        Call<List<UserData>> call = getUser.getUser(txtLogin, null, txtPass);
                        call.enqueue(new Callback<List<UserData>>() {
                            @Override
                            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                                progressDialog.dismiss();
                                String code = String.valueOf(response.code());
                                switch (code) {
                                    case "200":
                                        openMain();
                                    case "400":
                                        Toast.makeText(login.this, code, Toast.LENGTH_SHORT).show();
                                    case "404":
                                        Toast.makeText(login.this, code, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<UserData>> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(login.this, "Tidak boleh dikosongkan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openDaftar(){
        Intent intent = new Intent(this, daftar.class);
        startActivity(intent);
    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}