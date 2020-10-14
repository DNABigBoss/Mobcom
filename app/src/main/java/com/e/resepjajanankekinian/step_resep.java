package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.model.StepResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.GetResep;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class step_resep extends AppCompatActivity {
    ProgressDialog progressDialog;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_resep);

        final TextView nomorstep = findViewById(R.id.nomorstepresep);
        final TextView stepresep = findViewById(R.id.stepresep);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getInt("id");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetResep service = ApiClient.getRetrofitInstance().create(GetResep.class);
        Call<StepResepData> call = service.getResep(id);

        call.enqueue(new Callback<StepResepData>() {
            @Override
            public void onResponse(Call<StepResepData> call, Response<StepResepData> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                StepResepData resource = response.body();
                assert resource != null;
                List<StepResepData.DatumStep> datumSteps = resource.getStep();
                for (StepResepData.DatumStep datumStep: datumSteps) {
                    String nomor_step = String.valueOf(datumStep.getNomor_step());
                    nomorstep.setText(nomor_step);
                    stepresep.setText(datumStep.getIntruksi());
                }
            }

            @Override
            public void onFailure(Call<StepResepData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(step_resep.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });

        //View
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
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
}