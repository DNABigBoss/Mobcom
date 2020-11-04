package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.e.resepjajanankekinian.adapter.Adapter;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private Adapter adapter;
    private RecyclerView recyclerView;
    private Button button, buttonFav;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userName = user.get(sessionManager.NAME);

        TextView textViewPencarianSemuaBaru = findViewById(R.id.pencarianSemuaBaru);
        TextView textViewPencarianSemuaPopuler = findViewById(R.id.pencarianSemuaPopuler);
        TextView textViewUcapan = findViewById(R.id.textViewUcapan);
        button = findViewById(R.id.search_bar);
        buttonFav = findViewById(R.id.buttonFavorite);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<List<ResepData>> call = apiRequest.getResep(null, null, null, 10, "dilihat");
        Call<List<ResepData>> call1 = apiRequest.getResep(null, null, null, 10, "id");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, search.class));
            }
        });

        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
            }
        });

        /* Ketika mengklik lihat semua yang terpopuler */
        textViewPencarianSemuaPopuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencarianSemua("dilihat");
            }
        });

        /* Ketika mengklik lihat semua yang terbaru */
        textViewPencarianSemuaBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencarianSemua("id");
            }
        });

        /*
        Recyler View Populer
         */
        call.enqueue(new Callback<List<ResepData>>() {
            @Override
            public void onResponse(Call<List<ResepData>> call, final Response<List<ResepData>> response) {
                Log.d(TAG, "server contacted and has file");
                progressDialog.dismiss();
                recyclerView = findViewById(R.id.customRecyclerView);
                generateDataList(response.body(), recyclerView);
            }

            @Override
            public void onFailure(Call<List<ResepData>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "error");
            }
        });

        /*
        Recyler View Baru ditambahkan
         */
        call1.enqueue(new Callback<List<ResepData>>() {
            @Override
            public void onResponse(Call<List<ResepData>> call, final Response<List<ResepData>> response) {
                Log.d(TAG, "server contacted and has file");
                progressDialog.dismiss();
                recyclerView = findViewById(R.id.customRecyclerViewBaru);
                generateDataList(response.body(), recyclerView);
            }

            @Override
            public void onFailure(Call<List<ResepData>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "error");
            }
        });

        /*
        Text view ucapan
         */
        Locale locale = new java.util.Locale("id","ID","id-ID");
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("EEEE", locale);
        String date = dateFormat.format(Calendar.getInstance().getTime());
        textViewUcapan.setText("Selamat Hari "+ date + ", " + userName);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        break;
                    case R.id.kulkas:
                        startActivity(new Intent(MainActivity.this, kulkas.class));
                        finish();
                        break;
                    case R.id.bookmark:
                        startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this, profil.class));
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void pencarianSemua(String id) {
        Intent intent = new Intent(MainActivity.this, search_resep_bahan.class);
        intent.putExtra("order", id);
        startActivity(intent);
    }

    /**generate data list method()
     */
    private void generateDataList(List<ResepData> ResepDataList, RecyclerView recyclerView){
        adapter = new Adapter(this, ResepDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}