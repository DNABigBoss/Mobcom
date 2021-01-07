package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.BookmarkAdapter;
import com.e.resepjajanankekinian.adapter.ListUsersResepAdapter;
import com.e.resepjajanankekinian.model.ResepUserData;
import com.e.resepjajanankekinian.model.ResepUserData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class list_resep extends AppCompatActivity {

    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resep);
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userId = user.get(SessionManager.ID);
        Integer idx = Integer.valueOf(userId);
        final TextView textViewKosong = findViewById(R.id.TextViewKosonglistresep);
        recyclerView = findViewById(R.id.customRecyclerViewlistresep);

        /*
         * Prosess dulu
         */
        progressDialog = new ProgressDialog(list_resep.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*
        Ambil datanya
         */
        ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<List<ResepUserData>> call = apiRequest.getUserresep(null, null,null, null, null, idx);

        call.enqueue(new Callback<List<ResepUserData>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResepUserData>> call, @NonNull Response<List<ResepUserData>> response) {
                progressDialog.dismiss();
                List<ResepUserData> ResepUserDataList = response.body();
                String code = String.valueOf(response.code());
                switch (code) {
                    case "200":
                        generateDataList(ResepUserDataList);
                        break;
                    case "404":
                        recyclerView.setVisibility(View.GONE);
                        textViewKosong.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ResepUserData>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(list_resep.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String menu = String.valueOf(item.getTitle());
                switch (item.getItemId()){
                    case R.id.home:
                        movebottomnav(MainActivity.class, menu);
                        break;
                    case R.id.kulkas:
                        movebottomnav(kulkas.class, menu);
                        break;
                    case R.id.bookmark:
                        movebottomnav(BookmarkActivity.class, menu);
                        break;
                    case R.id.profile:
                        movebottomnav(profil.class, menu);
                        break;
                }
                return true;
            }
        });
    }

    private void generateDataList(List<ResepUserData> ResepUserDataList){
        recyclerView = findViewById(R.id.customRecyclerViewlistresep);
        ListUsersResepAdapter adapter = new ListUsersResepAdapter(this, ResepUserDataList, userId);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        createLog("menekan tombol kembali", "click");
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void createLog(String action, String type){
        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiRequest.postLog(userId, action, type);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void movebottomnav(final Class aClass, String menu) {
        createLog("menekan menu " + menu, "click");
        startActivity(new Intent(list_resep.this, aClass));
        finish();
    }
}