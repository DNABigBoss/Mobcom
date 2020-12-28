package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.service.CircleTransform;
import com.e.resepjajanankekinian.service.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profil extends AppCompatActivity {
    SessionManager sessionManager;
    CircleTransform circleTransform = new CircleTransform();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        userId = user.get(SessionManager.ID);
        String userName = user.get(SessionManager.NAME);
        String userEmail = user.get(SessionManager.EMAIL);
        String userFoto = user.get(SessionManager.FOTO);

        TextView textViewName = findViewById(R.id.username);
        TextView textViewEmail = findViewById(R.id.email);
        ImageView imageView = findViewById(R.id.profile_image);

        textViewName.setText(userName);
        textViewEmail.setText(userEmail);

        Picasso.with(this).load(userFoto).placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.user).transform(circleTransform).into(imageView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String menu = String.valueOf(item.getTitle());
                switch (item.getItemId()){
                    case R.id.home:
                        movebottomnav(MainActivity.class, menu);
                        break;
                    case R.id.kulkas:
                        movebottomnav(kulkas.class, menu);
                        break;
                    case R.id.bookmark:
                        movebottomnav(BookmarkActivity.class, menu);
                        break;
                }
                return true;
            }
        });
        Button buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> responseBodyCall = createLog("menekan tombol edit profil", "click");
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                startActivity(new Intent(profil.this, edit_profil.class));
            }
        });

        Button button_logout = findViewById(R.id.buttonLogout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> responseBodyCall = createLog("menekan tombol logout", "click");
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(profil.this);
                alertDialogBuilder.setMessage("Apakah anda yakin ingin keluar?");
                alertDialogBuilder.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Call<ResponseBody> responseBodyCall = createLog("logout", "logout");
                                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                sessionManager.logout();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<ResponseBody> responseBodyCall = createLog("menekan batal logout", "click");
                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        Call<ResponseBody> responseBodyCall = createLog("menekan tombol kembali", "click");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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

    private Call<ResponseBody> createLog(String action, String type){
        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiRequest.postLog(userId, action, type);
        return responseBodyCall;
    }

    private void movebottomnav(final Class aClass, String menu) {
        Call<ResponseBody> responseBodyCall = createLog("menekan menu "+menu, "click");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                startActivity(new Intent(profil.this, aClass));
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                startActivity(new Intent(profil.this, aClass));
                finish();
            }
        });
    }
}