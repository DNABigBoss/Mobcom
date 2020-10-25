package com.e.resepjajanankekinian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.Adapter;
import com.e.resepjajanankekinian.adapter.DiskusiAdapter;
import com.e.resepjajanankekinian.model.DiskusiData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;

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
    private DiskusiAdapter adapter;
    private RecyclerView recyclerView;
    SessionManager sessionManager;
    ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userName = user.get(sessionManager.NAME);
        String userEmail = user.get(sessionManager.EMAIL);
        String userID = user.get(sessionManager.ID);
        final Integer user_id = Integer.valueOf(userID);

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

        Call<List<DiskusiData>> call = apiRequest.getDiskusi(null, resep_id);

        call.enqueue(new Callback<List<DiskusiData>>() {
            @Override
            public void onResponse(Call<List<DiskusiData>> call, Response<List<DiskusiData>> response) {
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
            public void onFailure(Call<List<DiskusiData>> call, Throwable t) {
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
                String komen = editTextDiskusi.getText().toString().trim();
                if (komen != null && komen.length() != 0) {
                    progressDialog = new ProgressDialog(comment.this);
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = dateFormat.format(Calendar.getInstance().getTime());

                    Call<ResponseBody> call1 = apiRequest.postDiskusi(komen, user_id, resep_id, 0, date);
                    call1.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            Toast.makeText(comment.this, "Berhasil menambahkan komen", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(comment.this, comment.class);
                            intent.putExtra("resep_id", resep_id);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
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
                finish();
            }
        });
    }

    private void generateDataList(List<DiskusiData> diskusiDataList) {
        recyclerView = findViewById(R.id.customRecyclerViewDiskusi);
        adapter = new DiskusiAdapter(this, diskusiDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(comment.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}