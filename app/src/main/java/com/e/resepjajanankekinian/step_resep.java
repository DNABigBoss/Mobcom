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
import com.e.resepjajanankekinian.service.SessionManager;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
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
    SessionManager sessionManager;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_resep);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userId = user.get(SessionManager.ID);

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
        Toolbar toolbar = findViewById(R.id.toolbarStep);
        toolbar.setTitle("Step Resep");
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
        adapter = new StepResepAdapter(this, stepResepData, userId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(step_resep.this, LinearLayoutManager.HORIZONTAL, false);
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
}