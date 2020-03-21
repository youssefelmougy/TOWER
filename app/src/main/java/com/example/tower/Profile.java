package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }

                return false;
            }
        });
    }

    //TODO Create Activity for Once You're Logged In

    public void onClickLogin(View view) {
        Button btn = findViewById(R.id.login_button);
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
                if(dataSnapshot.exists()){
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
                            }
                            else {
                                result.setTextColor(Color.RED);
                                result.setText("Incorrect Password!!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    result.setTextColor(Color.BLACK);
                    result.setText("The username " + user + " does not exist.");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void login(long id) {
        Intent intent = new Intent(this, ProfileLogin.class);
        intent.putExtra("STUDENT_ID", id);
        startActivity(intent);
    }

    public void makeToast (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }





}
