package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.adapter.BahanAdapter;
import com.e.resepjajanankekinian.adapter.PencarianAdapter;
import com.e.resepjajanankekinian.model.BahanData;
import com.e.resepjajanankekinian.model.BahanData2;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.model.ResepUserData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.layout.simple_spinner_dropdown_item;

public class tambah_resep extends AppCompatActivity {

    AutoCompleteTextView textIn;
    Button buttonAdd;
    LinearLayout container;
    TextView reList, info, tambah_gambar_tv;
    Bitmap bitmap;
    ImageView gambar_resep, tambah_gambar_icon;
    SessionManager sessionManager;
    ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    BahanAdapter adapter;

    private Button buttonView, buttonViewCara;
    private LinearLayout parentLayout, parentCara;
    private List<BahanData2> BahanData2;
    private int hint=0, hintcara=0;

    private static final String[] NUMBER = new String[] {
            "One", "Two", "Three", "Four", "Five",
            "Six", "Seven", "Eight", "Nine", "Ten"
    };

    List<EditText> allEds = new ArrayList<EditText>(); //bahan
    List<EditText> allEdsStep = new ArrayList<EditText>(); //step

    private ArrayList<String> bahan;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_resep);

        LinearLayout ubahFoto = findViewById(R.id.ubahFoto);
        gambar_resep = findViewById(R.id.gambar_resep);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.tambahResepToolbar);
        toolbar.setTitle("Tambah Resep Baru");
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

        // bahan
        buttonView=(Button)findViewById(R.id.buttonTambahBahan);
        parentLayout = (LinearLayout)findViewById(R.id.parentBahanBahan);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                createEditTextView();
            }
        });

        // cara
        buttonViewCara=(Button)findViewById(R.id.buttonTambahCara);
        parentCara = (LinearLayout)findViewById(R.id.parentCara);
        buttonViewCara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                createEditTextViewCara();
            }
        });


        //tambah foto
        ubahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        //upload data
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userID = user.get(SessionManager.ID);
        final Integer user_id = Integer.valueOf(userID);

        final EditText et_namaresep = findViewById(R.id.namaresep);
        final EditText et_porsi = findViewById(R.id.porsi);
        final EditText et_biaya = findViewById(R.id.biaya);
        final EditText et_waktu = findViewById(R.id.waktu_memasak);
        final Button buttontambahresep = findViewById(R.id.buttontambahresep);

//        Call<List<BahanData2>> call = apiRequest.getBahan2();
//        call.enqueue(new Callback<List<com.e.resepjajanankekinian.model.BahanData2>>() {
//            @Override
//            public void onResponse(Call<List<com.e.resepjajanankekinian.model.BahanData2>> call, Response<List<com.e.resepjajanankekinian.model.BahanData2>> response) {
//                BahanData2 = response.body();
//                for(com.e.resepjajanankekinian.model.BahanData2 bahanData2 : BahanData2) {
//                 bahan.add(bahanData2.getNama());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<com.e.resepjajanankekinian.model.BahanData2>> call, Throwable t) {
//
//            }
//        });

        /*
        Tambah data
         */
        buttontambahresep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaresep = " ", porsi = " ", biaya = " ", waktu_memasak = " ", uploadgambar = " ";

                namaresep = et_namaresep.getText().toString().trim();
                porsi = et_porsi.getText().toString().trim();
                biaya = et_biaya.getText().toString().trim();
                waktu_memasak = et_waktu.getText().toString().trim();
                uploadgambar = getStringImage(bitmap);


                if (namaresep != null && namaresep.length() != 0) {
                    progressDialog = new ProgressDialog(tambah_resep.this);
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();

                    Call<ResepUserData> call = apiRequest.postResepUsers(user_id, namaresep, waktu_memasak, porsi, biaya, 0, 0, uploadgambar);
                    call.enqueue(new Callback<ResepUserData>() {
                        @Override
                        public void onResponse(@NonNull Call<ResepUserData> call, @NonNull Response<ResepUserData> response) {

                            // data bahan bahan

                            ResepUserData resepUserData = response.body();
                            Integer resep_user_id = resepUserData.getId();

                            String[] strings = new String[allEds.size()];

                            for( int i=0; i < allEds.size(); i++){
                                strings[i] = allEds.get(i).getText().toString();
                                Call<ResponseBody> call2 = apiRequest.postUsersBahan(i+1, strings[i], resep_user_id );
                                call2.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call2, @NonNull Response<ResponseBody> response) {
                                        //Toast.makeText(tambah_resep.this, "Berhasil menambahkan bahan resep", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ResponseBody> call2, @NonNull Throwable t) {
                                        //Toast.makeText(tambah_resep.this, "Gagal menambahkan bahan resep", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            //data bahan abis

                            //data step cara
                            String[] strings2 = new String[allEdsStep.size()];

                            for( int i=0; i < allEdsStep.size(); i++){
                                strings2[i] = allEdsStep.get(i).getText().toString();
                                Call<ResponseBody> call3 = apiRequest.postUsersStep(i+1, strings2[i], resep_user_id);
                                call3.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call3, @NonNull Response<ResponseBody> response) {
                                        //progressDialog.dismiss();
                                        //Toast.makeText(tambah_resep.this, "Berhasil menambahkan step resep", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ResponseBody> call3, @NonNull Throwable t) {
                                        //progressDialog.dismiss();
                                        //Toast.makeText(tambah_resep.this, "Gagal menambahkan step resep", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            //data stepcara abis
                            createLog("Menambah Resep", "add");
                            progressDialog.dismiss();
                            Toast.makeText(tambah_resep.this, "Berhasil menambahkan resep", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(tambah_resep.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResepUserData> call, @NonNull Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(tambah_resep.this, "Gagal menambahkan resep", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(tambah_resep.this, "Resep tidak bisa kosong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                gambar_resep.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            tambah_gambar_icon = findViewById(R.id.tambah_gambar_icon);
            tambah_gambar_tv = findViewById(R.id.tambah_gambar_tv);
            tambah_gambar_icon.setVisibility(View.GONE);
            tambah_gambar_tv.setVisibility(View.GONE);

        }
    }


    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }


    //tambah tambah data bahan
    protected void createEditTextView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(8,8,8, 0);
        EditText edittTxt = new EditText(this);
        allEds.add(edittTxt);
        hint++;
        edittTxt.setHint("Bahan "+hint);
        edittTxt.setLayoutParams(params);
        edittTxt.setEms(10);
        edittTxt.setPadding(16,16,30,16);
        edittTxt.setTextSize(0x10);
        edittTxt.setBackgroundResource(R.drawable.input_bg);
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        edittTxt.setId(hint);


        //String[] items = new String[]{"1", "2", "three"};

//        ApiRequest apiRequest2 = ApiClient.getRetrofitInstance().create(ApiRequest.class);
//        Call<List<BahanData2>> call4 = apiRequest2.getBahan2();
//        call4.enqueue(new Callback<List<BahanData2>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<BahanData2>> call4, @NonNull Response<List<BahanData2>> response) {
//                List<BahanData2> bahanData = response.body().
//                resource.getClass();
//                Spinner spinner = new Spinner(tambah_resep.this);
//                spinner.setAdapter(resource);
//                parentLayout.addView(spinner);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<BahanData2>> call4, @NonNull Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(tambah_resep.this, "Gagal Memuat", Toast.LENGTH_SHORT).show();
//            }
//        });

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        parentLayout.addView(edittTxt);
        //fetchBahan();
    }

    // tambah data cara
    protected void createEditTextViewCara() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(8,8,8, 0);
        EditText edittTxt = new EditText(this);
        allEdsStep.add(edittTxt);
        hintcara++;
        edittTxt.setHint("Cara "+hintcara);
        edittTxt.setLayoutParams(params);
        edittTxt.setEms(10);
        edittTxt.setPadding(16,16,30,16);
        edittTxt.setTextSize(0x10);
        edittTxt.setBackgroundResource(R.drawable.input_bg);
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        edittTxt.setId(hintcara);
        parentCara.addView(edittTxt);
    }

//    public void fetchBahan(){
//        apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
//        Call<List<BahanData>> call = apiRequest.getAllBahan("asc");
//
//        call.enqueue(new Callback<List<BahanData>>() {
//            @Override
//            public void onResponse(Call<List<BahanData>> call, Response<List<BahanData>> response) {
//                //progressBar.setVisibility(View.GONE);
//                BahanData = response.body();
//                generateDataList(BahanData);
//            }
//
//            @Override
//            public void onFailure(Call<List<BahanData>> call, Throwable t) {
//                //progressBar.setVisibility((View.GONE));
//                Toast.makeText(tambah_resep.this, "Error on :" + t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void generateDataList(List<BahanData> Userlist){
//        List<BahanData> adapter = new ArrayList<>(Userlist);
//        System.out.println(adapter);
//        Spinner spinner = new Spinner(tambah_resep.this);
//        //spinner.setAdapter(adapter);
//        parentLayout.addView(spinner);
//    }

    private void createLog(String action, String type){
        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiRequest.postLog(userID, action, type);
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