package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.e.resepjajanankekinian.model.ResepUserData;
import com.e.resepjajanankekinian.model.UserData;
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

public class user_resep extends AppCompatActivity {
    private static final String TAG = "Resep User Activity";
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    Integer resep_id;
    Integer user_id;
    ApiRequest apiRequest;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_resep);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userId = user.get(SessionManager.ID);
        user_id = Integer.valueOf(userId);

        final TextView namajajanan = findViewById(R.id.namajajananuserresep);
        final TextView hargajajanan = findViewById(R.id.hargauserresep);
        final ImageView imagejajanan = findViewById(R.id.imagejajananuserresep);
        final TextView dilihat = findViewById(R.id.dilihatuserresep);
        final TextView favorit = findViewById(R.id.favorituserresep);
        final TextView penjelasanbahan = findViewById(R.id.penjelasanbahanbahanuserresep);
        final TextView stepmembuat = findViewById(R.id.penjelasancaramembuatuserresep);
        final TextView porsi = findViewById(R.id.porsiuserresep);
        final TextView resepby = findViewById(R.id.id_usersuserresep);

        final Toolbar toolbar = findViewById(R.id.toolbaruserresep);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            resep_id = extras.getInt("id");
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<List<ResepUserData>> call = apiRequest.getUserresep(resep_id, null, null, null, null, null);

        call.enqueue(new Callback<List<ResepUserData>>() {
            @Override
            public void onResponse(Call<List<ResepUserData>> call, Response<List<ResepUserData>> response) {
                Log.d(TAG, "server contacted and has file");
                List<ResepUserData> resource = response.body();
                for(ResepUserData resepUserData : resource) {
                    List<ResepUserData.DatumBahan> datumBahans = resepUserData.getBahan();
                    List<ResepUserData.DatumStep> datumSteps = resepUserData.getStep();

                    toolbar.setTitle("Resep "+ resepUserData.getnama_resep());
                    namajajanan.setText(resepUserData.getnama_resep());
                    hargajajanan.setText(String.format("Rp. %s",resepUserData.getHarga()));
                    dilihat.setText(String.valueOf(resepUserData.getDilihat()));
                    favorit.setText(String.valueOf(resepUserData.getFavorit()));
                    if (resepUserData.getPorsi() != null) {
                        porsi.setText(resepUserData.getPorsi());
                    }
                    Picasso.Builder builder = new Picasso.Builder(user_resep.this);
                    builder.downloader(new OkHttp3Downloader(user_resep.this));
                    builder.build().load(resepUserData.getGambar())
                            .placeholder((R.drawable.ic_launcher_background))
                            .error(R.drawable.ic_launcher_background)
                            .into(imagejajanan);
                    String resep_by = resepUserData.getId_users();
                    if (resep_by == null) resepby.setText("Admin");
                    else {
                        Call<List<UserData>> userDataCall = apiRequest.getUser(Integer.valueOf(resep_by), null, null,null);
                        userDataCall.enqueue(new Callback<List<UserData>>() {
                            @Override
                            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                                List<UserData> userData = response.body();
                                for (UserData userData1: userData) {
                                    resepby.setText(userData1.getNama());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<UserData>> call, Throwable t) {

                            }
                        });
                    }

                    String bahanbahan = "";
                    for (ResepUserData.DatumBahan datumBahan : datumBahans) {
                        bahanbahan = bahanbahan + datumBahan.getTakaran() + " " + datumBahan.getNama_bahan() + "\n";
                    }
                    penjelasanbahan.setText(bahanbahan);

                    String step = "";
                    for (ResepUserData.DatumStep datumStep : datumSteps) {
                        String nomor_step = String.valueOf(datumStep.getNomor_step());
                        step = step + nomor_step + ". " + datumStep.getIntruksi() + "\n\n";
                    }
                    stepmembuat.setText(step);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ResepUserData>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(user_resep.this, "Gagal Memuat Resep", Toast.LENGTH_SHORT).show();
            }
        });

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