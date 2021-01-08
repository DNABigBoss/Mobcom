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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

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

public class tambah_resep extends AppCompatActivity implements Validator.ValidationListener {

    TextView reList, info, tambah_gambar_tv;
    Bitmap bitmap;
    ImageView gambar_resep, tambah_gambar_icon;
    SessionManager sessionManager;
    ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    Validator validator;

    private Button buttonView, buttonViewCara;
    private LinearLayout parentLayout, parentCara;
    private List<BahanData2> BahanData2;
    private int hint=0, hinttakaran=0, hintcara=0;

    private static final String[] NUMBER = new String[] {
            "One", "Two", "Three", "Four", "Five",
            "Six", "Seven", "Eight", "Nine", "Ten"
    };

    List<EditText> allEds = new ArrayList<EditText>(); //bahan
    @NotEmpty List<EditText> allEdsTakaran = new ArrayList<EditText>(); //takaran bahan
    @NotEmpty List<EditText> allEdsStep = new ArrayList<EditText>(); //step

    String userID;
    Integer user_id;

    @NotEmpty
    private EditText et_namaresep;
    @NotEmpty
    private EditText et_porsi;
    @NotEmpty
    private EditText et_biaya;
    @NotEmpty
    private EditText et_waktu;

    private static final String TAG = "Tambah Resep";

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
                createEditTextViewTakaran();
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
        user_id = Integer.valueOf(userID);

        et_namaresep = findViewById(R.id.namaresep);
        et_porsi = findViewById(R.id.porsi);
        et_biaya = findViewById(R.id.biaya);
        et_waktu = findViewById(R.id.waktu_memasak);
        final Button buttontambahresep = findViewById(R.id.buttontambahresep);


        validator = new Validator(this);
        validator.setValidationListener(this);

        /*
        Tambah data
         */
        buttontambahresep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validator.validate();
                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    String message = "Error " + e.getMessage();
                    Log.e(TAG, message);
                }
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
        edittTxt.setTextColor(getResources().getColor(R.color.colorBlack));
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        edittTxt.setId(hint);

        parentLayout.addView(edittTxt);
    }

    //takaran
    protected void createEditTextViewTakaran() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(8,8,8, 24);
        EditText edittTxt2 = new EditText(this);
        allEdsTakaran.add(edittTxt2);
        hinttakaran++;
        edittTxt2.setHint("Takaran Bahan "+ hinttakaran + " cth: 1 kg");
        edittTxt2.setLayoutParams(params);
        edittTxt2.setEms(10);
        edittTxt2.setPadding(16,16,30,16);
        edittTxt2.setTextSize(0x10);
        edittTxt2.setBackgroundResource(R.drawable.input_bg);
        edittTxt2.setTextColor(getResources().getColor(R.color.colorBlack));
        edittTxt2.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt2.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt2.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        edittTxt2.setId(hinttakaran);

        parentLayout.addView(edittTxt2);
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
        edittTxt.setTextColor(getResources().getColor(R.color.colorBlack));
        edittTxt.setBackgroundResource(R.drawable.input_bg);
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        edittTxt.setId(hintcara);
        parentCara.addView(edittTxt);
    }


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

    @Override
    public void onValidationSucceeded() {
        if (allEds.isEmpty()) {
            Toast.makeText(tambah_resep.this, "Bahan Tidak Bisa Kosong", Toast.LENGTH_SHORT).show();
        } else if (allEdsStep.isEmpty()) {
            Toast.makeText(tambah_resep.this, "Step Tidak Bisa Kosong", Toast.LENGTH_SHORT).show();
        }  else {
            String namaresep = " ", porsi = " ", biaya = " ", waktu_memasak = " ", uploadgambar = " ";

            namaresep = et_namaresep.getText().toString().trim();
            porsi = et_porsi.getText().toString().trim();
            biaya = et_biaya.getText().toString().trim();
            waktu_memasak = et_waktu.getText().toString().trim();
            if (bitmap != null) uploadgambar = getStringImage(bitmap);

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
                    String[] stringsTakaran = new String[allEdsTakaran.size()];

                    for( int i=0; i < allEds.size(); i++){
                        strings[i] = allEds.get(i).getText().toString();
                        stringsTakaran[i] = allEdsTakaran.get(i).getText().toString();
                        Call<ResponseBody> call2 = apiRequest.postUsersBahan(strings[i], stringsTakaran[i] , resep_user_id );
                        call2.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call2, @NonNull Response<ResponseBody> response) {
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call2, @NonNull Throwable t) {
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
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call3, @NonNull Throwable t) {
                            }
                        });
                    }

                    //data stepcara abis
                    createLog("Menambah Resep", "add");
                    progressDialog.dismiss();
                    Toast.makeText(tambah_resep.this, "Berhasil menambahkan resep", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(tambah_resep.this, list_resep.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<ResepUserData> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(tambah_resep.this, "Gagal menambahkan resep", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        createLog("menekan tombol kembali", "click");
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}