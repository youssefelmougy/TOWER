package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    public static Boolean loggedIn = false;
    public static long id = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Helper helper = new Helper(this);
        //helper.deleteAllBooks();
        //helper.floodDatabase(500);
        DatabaseReference reference = database.getReference().child("textbooks");
        GridView gridView = findViewById(R.id.main_grid_view);
        displayTextbooks(this, gridView);


        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
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
                }

                return false;
            }
        });

    }


    public void displayTextbooks(final Context context, final GridView gridView) {
        DatabaseReference reference = database.getReference().child("textbooks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Textbook> textbooks = new ArrayList<>();
                Stack<Textbook> textbookStack = new Stack<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Textbook textbook = snapshot.getValue(Textbook.class);
                    textbookStack.push(textbook);
                }
                int max = 0;
               for (int i = 0; i < textbookStack.size(); i++) {
                   if (max == 20) break;
                   textbooks.add(textbookStack.pop());
                   max++;
               }

                gridView.setAdapter(new TextbookAdapter(context, textbooks, getLocalClassName()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
