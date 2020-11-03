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
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;

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
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLogin()){
            finish();
            startActivity(new Intent(login.this, MainActivity.class));
        }

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
                        ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
                        Call<List<UserData>> call = apiRequest.getUser(null, null, txtLogin, txtPass);
                        call.enqueue(new Callback<List<UserData>>() {
                            @Override
                            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                                progressDialog.dismiss();
                                String code = String.valueOf(response.code());
                                switch (code) {
                                    case "200":
                                        openMain(response.body());
                                        break;
                                    case "400":
                                        Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "404":
                                        Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<List<UserData>> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
                        Call<List<UserData>> call = apiRequest.getUser(null, txtLogin, null, txtPass);
                        call.enqueue(new Callback<List<UserData>>() {
                            @Override
                            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                                progressDialog.dismiss();
                                String code = String.valueOf(response.code());
                                switch (code) {
                                    case "200":
                                        openMain(response.body());
                                        break;
                                    case "400":
                                        Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "404":
                                        Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                                        break;
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

    public void openMain(List<UserData> userDataList){
        String nama = userDataList.get(0).getNama();
        String email = userDataList.get(0).getEmail();
        Integer id = userDataList.get(0).getId();
        String foto = userDataList.get(0).getFoto();
        String idx = String.valueOf(id);
        sessionManager.createSession(nama, email, idx, foto);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}