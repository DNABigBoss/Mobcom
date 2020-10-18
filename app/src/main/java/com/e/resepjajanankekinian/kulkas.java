package com.e.resepjajanankekinian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class kulkas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kulkas);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(kulkas.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.kulkas:
                        break;
                    case R.id.bookmark:
                        startActivity(new Intent(kulkas.this, BookmarkActivity.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(kulkas.this, profil.class));
                        finish();
                        break;
                }
                return true;
            }
        });
    }
}