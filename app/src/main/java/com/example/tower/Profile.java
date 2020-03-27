package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);

        final TabAdapter tabAdapter = new TabAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

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
                        return true;
                }

                return false;
            }
        });
    }

    public void onClickLogin(View view) {
        final TextView result = findViewById(R.id.login_result);
        EditText userField = findViewById(R.id.username_login);
        EditText passField = findViewById(R.id.password_login);
        final String user = userField.getText().toString();
        final String password = passField.getText().toString();

        final DatabaseReference myRef = database.getReference().child("students");
        Query query = myRef.orderByKey().equalTo(user);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DatabaseReference newRef = myRef.child(user);

                    newRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Student student = dataSnapshot.getValue(Student.class);
                            String passActual = student.getPassword();
                            if (password.equals(passActual)) {
                                result.setTextColor(Color.GREEN);
                                result.setText("Correct Username and Password!!!");
                                MainActivity.loggedIn = true;
                                MainActivity.id = student.getId();
                                login(student.getId());
                            } else {
                                result.setTextColor(Color.RED);
                                result.setText("Incorrect Password!!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    result.setTextColor(Color.BLACK);
                    result.setText("The username " + user + " does not exist.");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onClickSignup(View view) {
        final DatabaseReference ref = database.getReference().child("students");

        EditText idInput = findViewById(R.id.signup_id);
        EditText nameInput = findViewById(R.id.signup_name);
        EditText emailInput = findViewById(R.id.signup_email);
        EditText passInput = findViewById(R.id.signup_password);
        EditText passRepeat = findViewById(R.id.signup_repeat_password);

        Long id = Long.parseLong(idInput.getText().toString());
        String fullName = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passInput.getText().toString();
        String passwordRepeat = passRepeat.getText().toString();

        if(!password.equals(passwordRepeat)) {
            makeToast("Please enter the same password");
        }

        else {
            ref.child("" + id).child("id").setValue(id);
            ref.child("" + id).child("name").setValue(fullName);
            ref.child("" + id).child("email").setValue(email);
            ref.child("" + id).child("password").setValue(password);
            MainActivity.loggedIn = true;
            MainActivity.id = id;
            login(id);
        }


    }

    public void login(long id) {
        Intent intent = new Intent(this, ProfileLogin.class);
        intent.putExtra("STUDENT_ID", id);
        startActivity(intent);
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
