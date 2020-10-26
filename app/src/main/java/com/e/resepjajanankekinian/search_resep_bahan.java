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
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.SearchResepBahanAdapter;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.GetSearchResep;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class search_resep_bahan extends AppCompatActivity {
    private SearchResepBahanAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String bahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resep_bahan);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            bahan = extras.getString("bahan");
        } else {
            bahan = null; 
        }
        progressDialog = new ProgressDialog(search_resep_bahan.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();


        /*Create handle for the RetrofitInstance interface*/
        GetSearchResep getSearchResep = ApiClient.getRetrofitInstance().create(GetSearchResep.class);
        Call<List<ResepData>> call = getSearchResep.getResep(null, null, bahan, null);

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
                finish();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(search_resep_bahan.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.kulkas:
                        startActivity(new Intent(search_resep_bahan.this, kulkas.class));
                        finish();
                        break;
                    case R.id.bookmark:
                        startActivity(new Intent(search_resep_bahan.this, BookmarkActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(search_resep_bahan.this, profil.class));
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void generateDataList(List<ResepData> resepDataList) {
        recyclerView = findViewById(R.id.customRecyclerViewKulkas);
        adapter = new SearchResepBahanAdapter(this,resepDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(search_resep_bahan.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}