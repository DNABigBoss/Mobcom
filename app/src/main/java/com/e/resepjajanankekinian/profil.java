package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.e.resepjajanankekinian.service.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class profil extends AppCompatActivity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userName = user.get(sessionManager.NAME);
        String userEmail = user.get(sessionManager.EMAIL);

        TextView textViewName = findViewById(R.id.username);
        TextView textViewEmail = findViewById(R.id.email);

        textViewName.setText(userName);
        textViewEmail.setText(userEmail);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(profil.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.kulkas:
                        startActivity(new Intent(profil.this, kulkas.class));
                        finish();
                        break;
                    case R.id.bookmark:
                        startActivity(new Intent(profil.this, BookmarkActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        break;
                }
                return true;
            }
        });
        Button button_logout = (Button) findViewById(R.id.buttonLogout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logut();
            }
        });
    }
}