package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.PostUsers;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class daftar extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private EditText nama;
    private EditText email;
    private EditText pass;
    private Button buttonDaftar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        textView = findViewById(R.id.login);
        imageView = findViewById(R.id.x);
        nama = findViewById(R.id.daftarNama);
        email = findViewById(R.id.daftarEmail);
        pass = findViewById(R.id.daftarPass);
        buttonDaftar = findViewById(R.id.buttonDaftar);

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaPendaftar = nama.getText().toString().trim();
                String emailPendaftar = email.getText().toString().trim();
                String passPendaftar = pass.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (namaPendaftar != null && emailPendaftar != null && passPendaftar != null) {
                    if (emailPendaftar.matches(emailPattern)) {
                        if (passPendaftar.length() >= 8) {
                            progressDialog = new ProgressDialog(daftar.this);
                            progressDialog.setMessage("Loading....");
                            progressDialog.show();
                            PostUsers postUsers = ApiClient.getRetrofitInstance().create(PostUsers.class);
                            Call<ResponseBody> call = postUsers.postUser(namaPendaftar, emailPendaftar, passPendaftar);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(daftar.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                    openLogin();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(daftar.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(daftar.this, "Password minimal 8 karakter", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(daftar.this, "Masukkan email yang valid", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(daftar.this, "Tidak boleh dikosongkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
                finish();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
                finish();
            }
        });
    }

    public void openLogin(){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
}