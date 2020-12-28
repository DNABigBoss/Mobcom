package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.DiskusiAdapter;
import com.e.resepjajanankekinian.model.DiskusiData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;
import com.e.resepjajanankekinian.service.CircleTransform;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class comment extends AppCompatActivity {
    Integer resep_id;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    SessionManager sessionManager;
    CircleTransform circleTransform = new CircleTransform();
    ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userId = user.get(SessionManager.ID);
        final Integer user_id = Integer.valueOf(userId);
        String userFoto = user.get(SessionManager.FOTO);

        final EditText editTextDiskusi = findViewById(R.id.editTextDiskusi);
        final ImageView imageViewSend = findViewById(R.id.send);
        final TextView textViewKosong = findViewById(R.id.TextViewKosong);
        recyclerView = findViewById(R.id.customRecyclerViewDiskusi);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            resep_id = extras.getInt("resep_id");
        }
        progressDialog = new ProgressDialog(comment.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        ImageView imageView = findViewById(R.id.avatar1);
        Picasso.with(this).load(userFoto).placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.user).transform(circleTransform).into(imageView);

        Call<List<DiskusiData>> call = apiRequest.getDiskusi(null, resep_id);

        call.enqueue(new Callback<List<DiskusiData>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<DiskusiData>> call, @NonNull Response<List<DiskusiData>> response) {
                progressDialog.dismiss();
                String code = String.valueOf(response.code());
                switch (code) {
                    case "200":
                        generateDataList(response.body());
                        break;
                    case "404":
                        recyclerView.setVisibility(View.GONE);
                        textViewKosong.setVisibility(View.VISIBLE);
                        textViewKosong.setText("Ayo mulai berdiskusi!");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DiskusiData>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(comment.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        Tambah komen
         */
        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> responseBodyCall = createLog("menekan tombol tambah komen", "click");
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                String komen = editTextDiskusi.getText().toString().trim();
                if (komen != null && komen.length() != 0) {
                    progressDialog = new ProgressDialog(comment.this);
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();

                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = dateFormat.format(Calendar.getInstance().getTime());

                    Call<ResponseBody> call1 = apiRequest.postDiskusi(komen, user_id, resep_id, 0, date);
                    call1.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            Call<ResponseBody> responseBodyCall = createLog("menambah komen", "add");
                            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                            Toast.makeText(comment.this, "Berhasil menambahkan komen", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(comment.this, comment.class);
                            intent.putExtra("resep_id", resep_id);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(comment.this, "Gagal menambahkan komen", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(comment.this, "Komen tidak bisa kosong", Toast.LENGTH_SHORT).show();
            }
        });

        //View
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Diskusi");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> responseBodyCall = createLog("menekan tombol kembali", "click");
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                finish();
            }
        });
    }

    private void generateDataList(List<DiskusiData> diskusiDataList) {
        recyclerView = findViewById(R.id.customRecyclerViewDiskusi);
        DiskusiAdapter adapter = new DiskusiAdapter(this, diskusiDataList, userId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(comment.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private Call<ResponseBody> createLog(String action, String type){
        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiRequest.postLog(userId, action, type);
        return responseBodyCall;
    }
}