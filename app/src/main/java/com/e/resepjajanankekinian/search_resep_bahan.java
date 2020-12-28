package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.SearchResepBahanAdapter;
import com.e.resepjajanankekinian.model.ResepData;
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

public class search_resep_bahan extends AppCompatActivity {
    private SearchResepBahanAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String bahan = null;
    String order = null;
    SessionManager sessionManager;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resep_bahan);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userId = user.get(SessionManager.ID);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.kulkas);
        
        SearchView searchView = findViewById(R.id.search_bar_search);
        searchView.setIconified(false);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.getString("bahan") != null) {
                bahan = extras.getString("bahan");
            }
            if (extras.getString("order") != null) order = extras.getString("order");
        }
        progressDialog = new ProgressDialog(search_resep_bahan.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();


        /*Create handle for the RetrofitInstance interface*/
        ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<List<ResepData>> call = apiRequest.getResep(null, null, bahan, null, order);

        call.enqueue(new Callback<List<ResepData>>() {
            @Override
            public void onResponse(Call<List<ResepData>> call, Response<List<ResepData>> response) {
                progressDialog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<ResepData>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(search_resep_bahan.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });

        //View
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Search Resep");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLog("menekan tombol kembali", "click");
                finish();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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

    private void generateDataList(List<ResepData> resepDataList) {
        recyclerView = findViewById(R.id.customRecyclerViewKulkas);
        adapter = new SearchResepBahanAdapter(this,resepDataList, userId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(search_resep_bahan.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
        startActivity(new Intent(search_resep_bahan.this, aClass));
        finish();
    }
}