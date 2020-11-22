package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.BookmarkAdapter;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private CardView imageBookmark;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private BookmarkAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String id = user.get(SessionManager.ID);
        Integer idx = Integer.valueOf(id);
        final TextView textViewKosong = findViewById(R.id.TextViewKosongBookmark);
        recyclerView = findViewById(R.id.customRecyclerViewBookmark);

        /*
        * Prosess dulu
         */
        progressDialog = new ProgressDialog(BookmarkActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*
        Ambil datanya
         */
        ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<List<ResepData>> call = apiRequest.getBookmark(idx);

        call.enqueue(new Callback<List<ResepData>>() {
            @Override
            public void onResponse(Call<List<ResepData>> call, Response<List<ResepData>> response) {
                progressDialog.dismiss();
                List<ResepData> resepDataList = response.body();
                String code = String.valueOf(response.code());
                switch (code) {
                    case "200":
                        generateDataList(resepDataList);
                        break;
                    case "404":
                        recyclerView.setVisibility(View.GONE);
                        textViewKosong.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<ResepData>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BookmarkActivity.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bookmark);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(BookmarkActivity.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.kulkas:
                        startActivity(new Intent(BookmarkActivity.this, kulkas.class));
                        finish();
                        break;
                    case R.id.bookmark:
                        break;
                    case R.id.profile:
                        startActivity(new Intent(BookmarkActivity.this, profil.class));
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    public void goBack(){
        Intent intent = new Intent(BookmarkActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void generateDataList(List<ResepData> ResepDataList){
        recyclerView = findViewById(R.id.customRecyclerViewBookmark);
        adapter = new BookmarkAdapter(this, ResepDataList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}