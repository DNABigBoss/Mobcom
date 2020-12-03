package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class daftar extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    private EditText nama;
    @NotEmpty
    @Email
    private EditText email;
    @NotEmpty
    @Password(min = 6)
    private EditText pass;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    Validator validator;

    private static final String TAG = "daftar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLogin()){
            finish();
            startActivity(new Intent(daftar.this, MainActivity.class));
        }

        TextView imageView = findViewById(R.id.x);
        nama = findViewById(R.id.daftarNama);
        email = findViewById(R.id.daftarEmail);
        pass = findViewById(R.id.daftarPass);
        Button buttonDaftar = findViewById(R.id.buttonDaftar);

        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validator.validate();
                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    String message = "Error " + e.getMessage();
                    Log.e(TAG, message);
                }
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

    public void openSelesaiDaftar(String name, String pass) {
        Intent intent = new Intent(this, selesai_daftar.class);
        intent.putExtra("username",name);
        intent.putExtra("pass", pass);
        startActivity(intent);
    }

    @Override
    public void onValidationSucceeded() {
        final String namaPendaftar = nama.getText().toString().trim();
        String emailPendaftar = email.getText().toString().trim();
        final String passPendaftar = pass.getText().toString().trim();

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
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}