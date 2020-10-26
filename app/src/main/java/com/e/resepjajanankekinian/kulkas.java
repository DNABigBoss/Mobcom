package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.BahanAdapter;
import com.e.resepjajanankekinian.model.BahanData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.GetBahan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class kulkas extends AppCompatActivity {
    BahanAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kulkas);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        progressDialog = new ProgressDialog(kulkas.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetBahan service = ApiClient.getRetrofitInstance().create(GetBahan.class);
        Call<List<BahanData>> call = service.getAllBahan("Asc");

        call.enqueue(new Callback<List<BahanData>>() {
            @Override
            public void onResponse(@NonNull Call<List<BahanData>> call, @NonNull Response<List<BahanData>> response) {
                generateDataList(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<BahanData>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(kulkas.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });

        /* search */
        SearchView searchView = findViewById(R.id.search_bar_kulkas);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(kulkas.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.kulkas:
                        break;
                    case R.id.bookmark:
                        startActivity(new Intent(kulkas.this, BookmarkActivity.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(kulkas.this, profil.class));
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void generateDataList(List<BahanData> list) {
        RecyclerView recyclerView = findViewById(R.id.customRecyclerViewKulkas);
        adapter = new BahanAdapter(this, list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(kulkas.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        progressDialog.dismiss();
    }
}