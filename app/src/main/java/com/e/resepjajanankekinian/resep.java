package com.e.resepjajanankekinian;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.resepjajanankekinian.adapter.Adapter;
import com.e.resepjajanankekinian.adapter.StepResepAdapter;
import com.e.resepjajanankekinian.model.StepResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.GetResep;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class resep extends AppCompatActivity {
    private StepResepAdapter adapter;
    ProgressDialog progressDialog;
    Integer id;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep);

        final TextView namajajanan = findViewById(R.id.namajajanan);
        final ImageView imagejajanan = findViewById(R.id.imagejajanan);
        final TextView dilihat = findViewById(R.id.dilihat);
        final TextView favorit = findViewById(R.id.favorit);
        final TextView penjelasanbahan = findViewById(R.id.penjelasanbahanbahan);
        final TextView stepmembuat = findViewById(R.id.penjelasancaramembuat);

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
                List<StepResepData.DatumInfo> datumInfos = resource.getInfo();
                List<StepResepData.DatumBahan> datumBahans = resource.getBahan();
                List<StepResepData.DatumStep> datumSteps = resource.getStep();

                for (StepResepData.DatumInfo datumInfo : datumInfos) {
                    namajajanan.setText(datumInfo.getNama());
                    dilihat.setText(String.valueOf(datumInfo.getDilihat()));
                    favorit.setText(String.valueOf(datumInfo.getFavorit()));
                    Picasso.Builder builder = new Picasso.Builder(resep.this);
                    builder.downloader(new OkHttp3Downloader(resep.this));
                    builder.build().load(datumInfo.getGambar())
                            .placeholder((R.drawable.ic_launcher_background))
                            .error(R.drawable.ic_launcher_background)
                            .into(imagejajanan);
                }

                String bahanbahan = "";
                for (StepResepData.DatumBahan datumBahan : datumBahans) {
                    bahanbahan = bahanbahan + datumBahan.getTakaran() + " " + datumBahan.getNama() + "\n";
                }
                penjelasanbahan.setText(bahanbahan);

                String step = "";
                for (StepResepData.DatumStep datumStep: datumSteps) {
                    String nomor_step = String.valueOf(datumStep.getNomor_step());
                    step = step + nomor_step + ". " + datumStep.getIntruksi() + "\n\n";
                }
                stepmembuat.setText(step);
            }

            @Override
            public void onFailure(Call<StepResepData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(resep.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });

        //View
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Resep");
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

    /**generate data list method()

    private void generateDataList(List<StepResepData> ResepDataList){

    }
     */

}