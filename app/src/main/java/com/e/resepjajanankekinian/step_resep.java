package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.Adapter;
import com.e.resepjajanankekinian.adapter.StepResepAdapter;
import com.e.resepjajanankekinian.model.StepResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class step_resep extends AppCompatActivity {
    StepResepAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    Integer id;
    List<StepResepData.DatumStep> datumSteps;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_resep);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getInt("id");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<StepResepData> call = apiRequest.getStepResep(id);

        call.enqueue(new Callback<StepResepData>() {
            @Override
            public void onResponse(Call<StepResepData> call, Response<StepResepData> response) {
                progressDialog.dismiss();
                StepResepData resource = response.body();
                datumSteps = resource.getStep();
                generateDataList(datumSteps);
            }

            @Override
            public void onFailure(Call<StepResepData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(step_resep.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });


        //View
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Step Resep");
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

    }

    public void onPause() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    private void generateDataList(List<StepResepData.DatumStep> stepResepData) {
        recyclerView = findViewById(R.id.customRecyclerViewStep);
        adapter = new StepResepAdapter(this, stepResepData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(step_resep.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}