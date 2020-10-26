package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e.resepjajanankekinian.model.UserData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class selesai_daftar extends AppCompatActivity {
    String username;
    String pass;
    ProgressBar progressBar;
    SessionManager sessionManager;
    private static final String TAG = "Selesai Daftar Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_daftar);
        sessionManager = new SessionManager(this);
        if (sessionManager.isLogin()){
            finish();
            startActivity(new Intent(selesai_daftar.this, MainActivity.class));
        }
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            username = extras.getString("username");
            pass = extras.getString("pass");
        }
        progressBar = findViewById(R.id.progressSelesai);

        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Call<List<UserData>> call = apiRequest.getUser(username, null, pass);
                                          call.enqueue(new Callback<List<UserData>>() {
                                              @Override
                                              public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                                                  String code = String.valueOf(response.code());
                                                  switch (code) {
                                                      case "200":
                                                          openMain(response.body());
                                                  }
                                              }

                                              @Override
                                              public void onFailure(Call<List<UserData>> call, Throwable t) {
                                                  Log.e(TAG, "error");
                                              }
                                          });
                                      }
                                  }, 3000);

    }

    private void openMain(List<UserData> userDataList) {
        String nama = userDataList.get(0).getNama();
        String email = userDataList.get(0).getEmail();
        Integer id = userDataList.get(0).getId();
        String idx = String.valueOf(id);
        sessionManager.createSession(nama, email, idx);
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}