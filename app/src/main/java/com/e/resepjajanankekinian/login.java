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

import com.e.resepjajanankekinian.model.UserData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty @Email private EditText editText;
    @NotEmpty private EditText pass;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    Validator validator;
    private static final String TAG = "login";
    ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLogin()){
            finish();
            startActivity(new Intent(login.this, MainActivity.class));
        }

        TextView textView = findViewById(R.id.daftar);
        Button button = findViewById(R.id.login);
        editText = findViewById(R.id.editTextLogin);
        pass = findViewById(R.id.loginPass);

        validator = new Validator(this);
        validator.setValidationListener(this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaftar();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onValidationSucceeded() {
        progressDialog = new ProgressDialog(login.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        String txtLogin = editText.getText().toString().trim();
        String txtPass = pass.getText().toString().trim();

        Call<List<UserData>> call = apiRequest.getUser(null, null, txtLogin, txtPass);
        call.enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserData>> call, @NonNull Response<List<UserData>> response) {
                progressDialog.dismiss();
                String code = String.valueOf(response.code());
                if ("200".equals(code)) {
                    openMain(response.body());
                } else {
                    Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserData>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(login.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
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