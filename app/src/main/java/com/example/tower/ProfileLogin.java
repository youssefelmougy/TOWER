package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//TODO Create Dynamic UI for adding Textbooks
//TODO use link https://www.raywenderlich.com/995-android-gridview-tutorial


public class ProfileLogin extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    long studentID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_login);

        studentID = MainActivity.id;

        GridView gridView = (GridView)findViewById(R.id.main_grid_view);
        displayTextbooks(this, gridView);

        DatabaseReference ref1 = database.getReference("/students/" + studentID);

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                TextView tv = findViewById(R.id.profile_login_title);
                String name = student.getName();
                name = name.split(" ")[0];
                tv.setText("Welcome Back, " + name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("Mike", "" + studentID);

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

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null) {
            Intent intent = getIntent();
            String message = "";
            if (intent.getStringExtra("ADDED_BOOK_MESSAGE") != null) {
                message = intent.getStringExtra("ADDED_BOOK_MESSAGE");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void displayTextbooks(final Context context, final GridView gridView) {
        DatabaseReference reference = database.getReference().child("textbooks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Textbook> textbooks = new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Textbook textbook = snapshot.getValue(Textbook.class);
                    if(textbook.getSeller() == MainActivity.id) {
                        Log.d("MikeC", "Reached!!");
                        textbooks.add(textbook);
                    }
                    Log.d("MikeC", "" + textbooks.size());
                }
                gridView.setAdapter(new TextbookAdapter(context, textbooks));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClickAdd (View view) {
        Intent intent = new Intent(this, AddBook.class);
        startActivity(intent);
    }

}

