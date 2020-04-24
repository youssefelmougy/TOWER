package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConditionDescription extends AppCompatActivity {
    String originatingClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_description);
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.getStringExtra("ORIGINAL_CLASS") != null) {
                originatingClass = intent.getStringExtra("ORIGINAL_CLASS");
            }

        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if(originatingClass.equals("MainActivity")) {
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
        else if (originatingClass.equals("Profile") || originatingClass.equals("ProfileLogin")){
            bottomNavigationView.setSelectedItemId(R.id.profile);
        }

        else if(originatingClass.equals("GoogleSuggestions")) {
            bottomNavigationView.setSelectedItemId(R.id.profile);
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.search);
        }
        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        if(MainActivity.loggedIn){
                            startActivity(new Intent(getApplicationContext(), ProfileLogin.class));
                            overridePendingTransition(0,0);
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                            overridePendingTransition(0,0);
                        }
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
        overridePendingTransition(0,0);
    }
}
