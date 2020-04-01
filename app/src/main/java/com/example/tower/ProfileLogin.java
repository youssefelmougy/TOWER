package com.example.tower;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

//TODO Create Dynamic UI for adding Textbooks
//TODO use link https://www.raywenderlich.com/995-android-gridview-tutorial


public class ProfileLogin extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    GridView gridView;
    TextbookAdapter adapter;
    long studentID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_login);
        gridView = (GridView)findViewById(R.id.main_grid_view);
        studentID = MainActivity.id;

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
                adapter = new TextbookAdapter(context, textbooks,getLocalClassName());
                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClickAdd (View view) {
        Intent intent = new Intent(this, AddBook.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2) {
            String title = data.getStringExtra("BOOK_TITLE");
            String author = data.getStringExtra("BOOK_AUTHOR");
            double price = data.getDoubleExtra("BOOK_PRICE", 0);
            Textbook textbook = new Textbook(title, author, MainActivity.id, price);
            addBook(textbook);
            adapter.notifyDataSetChanged();
        }
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

