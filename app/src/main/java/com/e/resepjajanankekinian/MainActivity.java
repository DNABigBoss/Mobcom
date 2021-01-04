package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.Adapter;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private RecyclerView recyclerView;
    SessionManager sessionManager;
    ProgressBar progressBar;
    private String userId;
    List<ResepData> resepDataBaru;
    TextView textViewIklan;
    ImageView imageViewIklan;
    String namaResepIklan;
    Integer idResepIklan;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        if(!sessionManager.isLogin()) {
            moveToLogin();
        }
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userName = user.get(SessionManager.NAME);
        userId = user.get(SessionManager.ID);

        TextView textViewPencarianSemuaBaru = findViewById(R.id.pencarianSemuaBaru);
        TextView textViewPencarianSemuaPopuler = findViewById(R.id.pencarianSemuaPopuler);
        TextView textViewUcapan = findViewById(R.id.textViewUcapan);
        Button button = findViewById(R.id.search_bar);
        Button buttonFav = findViewById(R.id.buttonBookmark);
        Button buttonTambahResep = findViewById(R.id.buttonTambahResep);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        progressBar = findViewById(R.id.progressbarmain);
        textViewIklan = findViewById(R.id.textViewIklan);
        imageViewIklan = findViewById(R.id.imageViewIklan);
        CardView cardViewIklan = findViewById(R.id.cardViewIklan);

        /*Create handle for the RetrofitInstance interface*/
        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<List<ResepData>> call = apiRequest.getResep(null, null, null, 10, "dilihat");
        Call<List<ResepData>> call1 = apiRequest.getResep(null, null, null, 10, "id");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLog("melihat pencarian", "watch");
                startActivity(new Intent(MainActivity.this, search.class));
            }
        });

        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLog("melihat bookmark", "watch");
                startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
            }
        });

        buttonTambahResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLog("menekan menu tambah resep", "click");
                startActivity(new Intent(MainActivity.this, tambah_resep.class));
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
        Recyler View Baru ditambahkan
         */
        call1.enqueue(new Callback<List<ResepData>>() {
            @Override
            public void onResponse(Call<List<ResepData>> call, final Response<List<ResepData>> response) {
                Log.d(TAG, "server contacted and has file");
                progressBar.setVisibility(View.GONE);
                recyclerView = findViewById(R.id.customRecyclerViewBaru);
                generateDataList(response.body(), recyclerView);
                resepDataBaru = response.body();
                memuatiklan();
            }

            @Override
            public void onFailure(Call<List<ResepData>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "error");
            }
        });


        /*
        Recyler View Populer
         */
        call.enqueue(new Callback<List<ResepData>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResepData>> call, @NonNull final Response<List<ResepData>> response) {
                Log.d(TAG, "server contacted and has file");
                progressBar.setVisibility(View.GONE);
                recyclerView = findViewById(R.id.customRecyclerView);
                generateDataList(response.body(), recyclerView);
            }

            @Override
            public void onFailure(@NonNull Call<List<ResepData>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "error");
            }
        });

        cardViewIklan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLog("melihat resep "+namaResepIklan, "watch");
                Intent intent = new Intent(MainActivity.this, resep.class);
                intent.putExtra("id", idResepIklan);
                startActivity(intent);
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
                String menu = String.valueOf(item.getTitle());
                switch (item.getItemId()){
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

    private void memuatiklan() {
        Collections.shuffle(resepDataBaru);
        for (ResepData resepData : resepDataBaru) {
            idResepIklan = resepData.getId();
            namaResepIklan = resepData.getNama();
            String txt = "Kuy Nyoba Bikin " + namaResepIklan + "!";
            textViewIklan.setText(txt);
            Picasso.Builder builder = new Picasso.Builder(MainActivity.this);
            builder.downloader(new OkHttp3Downloader(MainActivity.this));
            builder.build().load(resepData.getGambar())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(imageViewIklan);
            break;
        }
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

    private void pencarianSemua(final String id) {
        createLog("mencari resep "+id, "search");
        Intent intent = new Intent(MainActivity.this, search_resep_bahan.class);
        intent.putExtra("order", id);
        startActivity(intent);
    }

    /**generate data list method()
     */
    private void generateDataList(List<ResepData> ResepDataList, RecyclerView recyclerView){
        Adapter adapter = new Adapter(this, ResepDataList, userId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
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
        startActivity(new Intent(MainActivity.this, aClass));
        finish();
    }
}