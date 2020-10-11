package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.Adapter;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.GetService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class test extends AppCompatActivity {
    private Adapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        progressDialog = new ProgressDialog(test.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetService service = ApiClient.getRetrofitInstance().create(GetService.class);
        Call<List<ResepData>> call = service.getAllResep();
        call.enqueue(new Callback<List<ResepData>>() {
            @Override
            public void onResponse(Call<List<ResepData>> call, Response<List<ResepData>> response) {
                progressDialog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<ResepData>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(test.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**generate data list method()
     */
    private void generateDataList(List<ResepData> ResepDataList){
        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new Adapter(this, ResepDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(test.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}