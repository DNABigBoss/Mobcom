package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.PencarianAdapter;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.GetService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class search extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<ResepData> ResepDatas;
    private PencarianAdapter adapter;
    private GetService GetService;
    ProgressBar progressBar;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBar = findViewById(R.id.progress);

        SearchView searchView = findViewById(R.id.search);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // ketikan di searchview
            @Override
            public boolean onQueryTextSubmit(String query) { // ketika sdh selesai / enter
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // selesai mencari satu huruf satu huruf (lagi ngetik)
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        fetchUsers(); //without keyword
    }


    public void fetchUsers(){
        GetService = ApiClient.getRetrofitInstance().create(GetService.class);
        Call<List<ResepData>> call = GetService.getAllResep();

        call.enqueue(new Callback<List<ResepData>>() {
            @Override
            public void onResponse(Call<List<ResepData>> call, Response<List<ResepData>> response) {
                progressBar.setVisibility(View.GONE);
                ResepDatas = response.body();
                generateDataList(ResepDatas);
            }

            @Override
            public void onFailure(Call<List<ResepData>> call, Throwable t) {
                progressBar.setVisibility((View.GONE));
                Toast.makeText(search.this, "Error on :" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<ResepData> Userlist){
        recyclerView = findViewById(R.id.recycler);
        adapter = new PencarianAdapter(Userlist, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(search.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}