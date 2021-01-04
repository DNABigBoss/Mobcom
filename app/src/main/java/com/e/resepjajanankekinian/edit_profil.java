package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.model.UserData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.CircleTransform;
import com.e.resepjajanankekinian.service.SessionManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class edit_profil extends AppCompatActivity implements Validator.ValidationListener {

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    @NotEmpty EditText editTextName;
    @NotEmpty @Email EditText editTextEmail;
    @NotEmpty EditText editTextPass;
    String userId;
    String pass;
    String userName;
    String userEmail;
    Bitmap bitmap;
    ImageView profile_image;
    ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
    CircleTransform circleTransform = new CircleTransform();
    private static final int STORAGE_PERMISSION_CODE = 101;
    String foto;
    ProgressBar progressBar;
    String uploadgambar;
    Validator validator;
    private static final String TAG = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userName = user.get(SessionManager.NAME);
        userEmail = user.get(SessionManager.EMAIL);
        userId = user.get(SessionManager.ID);
        String userFoto = user.get(SessionManager.FOTO);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        Button buttonSave = findViewById(R.id.buttonSaveEditProfile);
        TextView ubahFoto = findViewById(R.id.ubahFoto);
        profile_image = findViewById(R.id.profile_image);
        progressBar = findViewById(R.id.progressbareditprofile);

        editTextName.setText(userName);
        editTextEmail.setText(userEmail);

        Picasso.with(this).load(userFoto).placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.user).transform(circleTransform).into(profile_image);

        editTextName.setFocusableInTouchMode(true);
        editTextEmail.setFocusableInTouchMode(true);

        validator = new Validator(this);
        validator.setValidationListener(this);

        InputMethodManager imm = (InputMethodManager) getSystemService(edit_profil.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextName, InputMethodManager.SHOW_IMPLICIT);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLog("menekan tombol simpan edit profil", "click");
                LayoutInflater layoutInflater = LayoutInflater.from(edit_profil.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.popup_input_dialog, null);
                editTextPass = popupInputDialogView.findViewById(R.id.editPass);
                AlertDialog.Builder builder = new AlertDialog.Builder(edit_profil.this);
                builder.setCancelable(true);
                builder.setTitle("Edit Profil");
                builder.setMessage("Masukkan Password");
                builder.setView(popupInputDialogView);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            validator.validate();
                        } catch (Exception e) {
                            // This will catch any exception, because they are all descended from Exception
                            String message = "Error " + e.getMessage();
                            Log.e(TAG, message);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        ubahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLog("menekan tombol edit foto profil", "click");
                checkPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        STORAGE_PERMISSION_CODE);
            }
        });

        //View
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
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

    private void saveEdit(final String pass) {
        progressDialog = new ProgressDialog(edit_profil.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final Integer id = Integer.valueOf(userId);
        if (bitmap != null) uploadgambar = getStringImage(bitmap);

        Call<List<UserData>> responseBodyCall = apiRequest.getUser(id, null, null, pass);
        responseBodyCall.enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                progressDialog.dismiss();
                String code = String.valueOf(response.code());
                if ("200".equals(code)) {
                    Call<ResponseBody> callx = apiRequest.putUser(id, name, email, pass, null, uploadgambar);
                    callx.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            createLog("mengupdate profil", "update");
                            openProfil(id, pass);
                        }
                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            Toast.makeText(edit_profil.this, "Gagal mengupdate profil", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(edit_profil.this, "Password Salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(edit_profil.this, "Password Salah", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            foto = getStringImage(bitmap);
            profile_image.setImageBitmap(bitmap);
        }
    }

    private void uploadPicture(final String userId, String photo, final String pass) {
        progressDialog = new ProgressDialog(edit_profil.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        final Integer id = Integer.valueOf(userId);

        Call<ResponseBody> call = apiRequest.putFoto(id, pass, photo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                progressDialog.dismiss();
                Toast.makeText(edit_profil.this, "Berhasil mengupdate foto", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(edit_profil.this, "Gagal mengupdate profil", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openProfil(Integer id, String pass) {
        progressDialog = new ProgressDialog(edit_profil.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        Call<List<UserData>> call = apiRequest.getUser(id, null, null, pass);
        call.enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserData>> call, @NonNull Response<List<UserData>> response) {
                progressDialog.dismiss();
                List<UserData> userDataList = response.body();
                String nama = userDataList.get(0).getNama();
                String email = userDataList.get(0).getEmail();
                Integer id = userDataList.get(0).getId();
                String foto = userDataList.get(0).getFoto();
                String idx = String.valueOf(id);
                sessionManager.createSession(nama, email, idx, foto);
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(edit_profil.this, profil.class));
                finish();
                Toast.makeText(edit_profil.this, "Berhasil mengupdate profil", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<List<UserData>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
        } else {
            chooseFile();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseFile();
            } else {
                Toast.makeText(this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void checkPass() {
        LayoutInflater layoutInflater = LayoutInflater.from(edit_profil.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.popup_input_dialog, null);
        final EditText editTextPass = popupInputDialogView.findViewById(R.id.editPass);
        AlertDialog.Builder builder = new AlertDialog.Builder(edit_profil.this);
        builder.setCancelable(true);
        builder.setTitle("Edit Profil");
        builder.setMessage("Masukkan Password");
        builder.setView(popupInputDialogView);
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pass = editTextPass.getText().toString().trim();
                chooseFile();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    @Override
    public void onValidationSucceeded() {
        String pass = editTextPass.getText().toString().trim();
        saveEdit(pass);
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
}