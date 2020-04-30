package com.example.tower;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//TODO Create Dynamic UI for adding Textbooks
//TODO use link https://www.raywenderlich.com/995-android-gridview-tutorial


public class ProfileLogin extends AppCompatActivity {
    long studentID = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    GridView gridView;
    TextbookAdapter adapter;
    ConstraintLayout mainLayout;
    TextView firstBookAdd;
    ImageView firstArrow;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_login);
        MainActivity.count = 1;
        fa = this;
        gridView = findViewById(R.id.main_grid_view);
        studentID = MainActivity.id;
        mainLayout = findViewById(R.id.profile_login_layout);
        firstBookAdd = findViewById(R.id.add_first_book_text);
        firstArrow = findViewById(R.id.imageView);

        displayTextbooks(this, gridView);


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
    //TODO Check if this is legit
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            final String uID = user.getUid();
            DatabaseReference reference1 = database.getReference().child("students");
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        if (uID == null) break;
                        if (snapshot.child("uID").getValue() == null) break;
                        if (snapshot.child("uID").getValue().toString().equals(uID)) {
                            String id = snapshot.child("id").getValue().toString();
                            studentID = Long.parseLong(id);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        DatabaseReference ref1 = database.getReference("/students/" + studentID);

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                TextView tv = findViewById(R.id.suggestionsTitle);
                String name = student.getName();
                name = name.split(" ")[0];
                tv.setText("Welcome Back, " + name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                        textbooks.add(textbook);
                    }
                }
                adapter = new TextbookAdapter(context, textbooks,getLocalClassName());
                if (textbooks.size() == 0) {
                    firstBookAdd.setVisibility(View.VISIBLE);
                    firstArrow.setVisibility(View.VISIBLE);
                }
                gridView.setAdapter(adapter);

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


    public void addBook(Textbook textbook) {
        DatabaseReference reference = database.getReference().child("textbooks");
        String key = reference.push().getKey();
        reference.child(key).child("title").setValue(textbook.getTitle());
        reference.child(key).child("author").setValue(textbook.getAuthor());
        reference.child(key).child("price").setValue(textbook.getPrice());
        reference.child(key).child("seller").setValue(MainActivity.id);
        reference.child(key).child("uniqueID").setValue(key);

    }



}

