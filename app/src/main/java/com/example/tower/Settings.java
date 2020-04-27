package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.tower.MainActivity.loggedIn;
import static com.example.tower.ProfileLogin.fa;

public class Settings extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (loggedIn) {
            ImageButton signOutButton = findViewById(R.id.imageButton);
            ImageButton accountSettingsButton = findViewById(R.id.imageButton4);
            signOutButton.setVisibility(View.VISIBLE);
            accountSettingsButton.setVisibility(View.VISIBLE);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.settings);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        if(loggedIn == false) {
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                            overridePendingTransition(0, 0);
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), ProfileLogin.class));
                            overridePendingTransition(0,0);
                        }
                        return true;
                    case R.id.settings:
                        return true;
                }

                return false;
            }
        });

    }

    public void reportBug(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"towerapplicationteam@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "TOWER Bug Report");
        intent.putExtra(Intent.EXTRA_TEXT, "Change this text with the bug you would like to report.");
        startActivity(Intent.createChooser(intent, ""));
    }

    public void signOutButton(View view) {
        if (loggedIn == true) {
            FirebaseAuth.getInstance().signOut();
            loggedIn = false;
            MainActivity.id = 0;
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
        }

    }

    public void accountSettings(View view) {
        startActivity(new Intent(this, AccountSettings.class));
        overridePendingTransition(0,0);
    }

    public void openPolicy(View view) {
        startActivity(new Intent(this, PrivacyPolicy.class));
        overridePendingTransition(0,0);
    }

    public void openEULA(View view) {
        startActivity(new Intent(this, EULA.class));
        overridePendingTransition(0,0);
    }

}
