package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class daftar extends AppCompatActivity {

    private EditText nama;
    private EditText email;
    private EditText pass;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLogin()){
            finish();
            startActivity(new Intent(daftar.this, MainActivity.class));
        }

        TextView textView = findViewById(R.id.login);
        ImageView imageView = findViewById(R.id.x);
        nama = findViewById(R.id.daftarNama);
        email = findViewById(R.id.daftarEmail);
        pass = findViewById(R.id.daftarPass);
        Button buttonDaftar = findViewById(R.id.buttonDaftar);

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namaPendaftar = nama.getText().toString().trim();
                String emailPendaftar = email.getText().toString().trim();
                final String passPendaftar = pass.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (namaPendaftar != null && emailPendaftar != null && passPendaftar != null) {
                    if (emailPendaftar.matches(emailPattern)) {
                        if (passPendaftar.length() >= 8) {
                            progressDialog = new ProgressDialog(daftar.this);
                            progressDialog.setMessage("Loading....");
                            progressDialog.show();
                            ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
                            Call<ResponseBody> call = apiRequest.postUser(namaPendaftar, emailPendaftar, passPendaftar);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(daftar.this, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show();
                                    openSelesaiDaftar(namaPendaftar, passPendaftar);
                                    finish();
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
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
        finish();
    }

    public void openSelesaiDaftar(String name, String pass) {
        Intent intent = new Intent(this, selesai_daftar.class);
        intent.putExtra("username",name);
        intent.putExtra("pass", pass);
        startActivity(intent);
    }
}