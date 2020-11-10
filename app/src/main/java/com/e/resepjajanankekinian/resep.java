package com.e.resepjajanankekinian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.e.resepjajanankekinian.model.StepResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class resep extends AppCompatActivity {
    private static final String TAG = "Resep Activity";
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    Integer resep_id;
    Integer user_id;
    ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String ids = user.get(SessionManager.ID);
        user_id = Integer.valueOf(ids);

        final TextView namajajanan = findViewById(R.id.namajajanan);
        final TextView hargajajanan = findViewById(R.id.harga);
        final ImageView imagejajanan = findViewById(R.id.imagejajanan);
        final TextView dilihat = findViewById(R.id.dilihat);
        final TextView favorit = findViewById(R.id.favorit);
        final TextView penjelasanbahan = findViewById(R.id.penjelasanbahanbahan);
        final TextView stepmembuat = findViewById(R.id.penjelasancaramembuat);
        final ToggleButton buttonFav = findViewById(R.id.tgbFav);
        final TextView diskusicount = findViewById(R.id.resepDiskusiCount);
        final RelativeLayout resepdiskusi = findViewById(R.id.resepDiskusi);

        RelativeLayout mulaimemasak = findViewById(R.id.buttonMulaimasak);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            resep_id = extras.getInt("id");
        }

        /* Ketika memulai memasak */
        mulaimemasak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(resep.this, step_resep.class);
                intent.putExtra("id", resep_id);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<StepResepData> call = apiRequest.getStepResep(resep_id);

        call.enqueue(new Callback<StepResepData>() {
            @Override
            public void onResponse(Call<StepResepData> call, Response<StepResepData> response) {
                Log.d(TAG, "server contacted and has file");
                StepResepData resource = response.body();
                List<StepResepData.DatumInfo> datumInfos = resource.getInfo();
                List<StepResepData.DatumBahan> datumBahans = resource.getBahan();
                List<StepResepData.DatumStep> datumSteps = resource.getStep();
                List<StepResepData.DatumDiskusi> datumDiskusis = resource.getDiskusi();
                List<StepResepData.DatumBookmark> datumBookmarks = resource.getBookmark();

                Integer dilihat_count = 0;
                for (StepResepData.DatumInfo datumInfo : datumInfos) {
                    dilihat_count = datumInfo.getDilihat();
                    namajajanan.setText(datumInfo.getNama());
                    hargajajanan.setText(String.format("Rp. %s",datumInfo.getHarga()));
                    dilihat.setText(String.valueOf(dilihat_count));
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
                for (StepResepData.DatumStep datumStep : datumSteps) {
                    String nomor_step = String.valueOf(datumStep.getNomor_step());
                    step = step + nomor_step + ". " + datumStep.getIntruksi() + "\n\n";
                }
                stepmembuat.setText(step);

                buttonFav.setChecked(false);
                for (StepResepData.DatumBookmark datumBookmark : datumBookmarks) {
                    if (datumBookmark.getUser_id().equals(user_id)) buttonFav.setChecked(true);
                }

                Integer countDiskusi = datumDiskusis.size();
                diskusicount.setText(String.valueOf(countDiskusi));

                /*
                Membuat fungsi perhitungan view
                 */
                Call<ResponseBody> call2 = apiRequest.putView(resep_id, dilihat_count + 1);
                call2.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "error");
                    }
                });
            }

            @Override
            public void onFailure(Call<StepResepData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(resep.this, "Gagal Memuat Resep", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        Click Favorit
         */
        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(resep.this);
                progressDialog.setMessage("Loading....");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (buttonFav.isChecked()) {
                            Call<ResponseBody> call1 = apiRequest.postBookmark(user_id, resep_id);
                            call1.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(resep.this, "Berhasil menjadi favorit", Toast.LENGTH_SHORT).show();
                                    buttonFav.setChecked(true);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(resep.this, "Gagal menjadi Favorit", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Call<ResponseBody> call1 = apiRequest.deleteBookmark(user_id, resep_id);
                            call1.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    String code = String.valueOf(response.code());
                                    if ("202".equals(code)) {
                                        progressDialog.dismiss();
                                        Toast.makeText(resep.this, "Fav Dihapus", Toast.LENGTH_SHORT).show();
                                        buttonFav.setChecked(false);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(resep.this, "Gagal menghapus Favorit", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(resep.this, "Gagal menghapus Favorit", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }, 1000);

            }
        });

        // Click untuk diskusi
        resepdiskusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(resep.this, comment.class);
                intent.putExtra("resep_id", resep_id);
                startActivity(intent);
            }
        });

        //View
        Toolbar toolbar = findViewById(R.id.toolbar);
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

}