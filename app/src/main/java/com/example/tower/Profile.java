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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);

        mAuth = FirebaseAuth.getInstance();
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
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();


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
                            final Student student = dataSnapshot.getValue(Student.class);
                            String email = student.getEmail();
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Profile.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        long id = student.getId();
                                        MainActivity.id = id;
                                        MainActivity.loggedIn = true;
                                        login(id);
                                    }
                                    else {
                                        result.setText("Invalid User ID or Password");
                                    }
                                }
                            });
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

        EditText idInput = findViewById(R.id.add_book_title);
        EditText nameInput = findViewById(R.id.add_book_author);
        EditText emailInput = findViewById(R.id.add_book_isbn);
        EditText passInput = findViewById(R.id.add_book_description);
        EditText passRepeat = findViewById(R.id.add_book_price);

        final Long id = Long.parseLong(idInput.getText().toString());
        final String fullName = nameInput.getText().toString();
        final String email = emailInput.getText().toString();
        final String password = passInput.getText().toString();
        final String passwordRepeat = passRepeat.getText().toString();

        if(!password.equals(passwordRepeat)) {
            makeToast("Please enter the same password");
        }

        //TODO IMPLEMENT ACTUAL LOGIN
        //TODO Use this https://firebase.googleblog.com/2017/02/email-verification-in-firebase-auth.html
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("MikeX", "success?");
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        ref.child("" + id).child("id").setValue(id);
                        ref.child("" + id).child("name").setValue(fullName);
                        ref.child("" + id).child("email").setValue(email);
                        ref.child("" + id).child("password").setValue(password);
                        ref.child("" + id).child("uID").setValue(user.getUid());
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Profile.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    MainActivity.loggedIn = true;
                                    MainActivity.id = id;
                                    login(id);
                                }
                                else {
                                    startActivity(new Intent(Profile.this, Profile.class));
                                }
                            }
                        });



                    }
                    else {
                        Log.d("MikeX", "" + task.getException());
                        Toast.makeText(Profile.this, "Invalid Email Address or Password.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    public void login(long id) {
        Intent intent = new Intent(this, ProfileLogin.class);
        intent.putExtra("STUDENT_ID", id);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
