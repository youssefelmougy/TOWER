package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;

public class AddBookFinal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    EditText titleET;
    EditText authorET;
    EditText descriptionET;
    EditText isbnET;
    EditText priceET;
    String imageUrl;
    Spinner spinner;
    String condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_final);


        String title = "";
        String author = "";
        String description = "";
        String isbn13 = "";
        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            title = bundle.getString("BOOK_TITLE");
            author = bundle.getString("BOOK_AUTHOR");
            description = bundle.getString("BOOK_DESCRIPTION");
            isbn13 = bundle.getString("BOOK_ISBN");

            imageUrl = bundle.getString("IMAGE_URL");
        }

        titleET = findViewById(R.id.add_book_title);
        authorET = findViewById(R.id.add_book_author);
        descriptionET = findViewById(R.id.add_book_description);
        isbnET = findViewById(R.id.add_book_isbn);
        priceET = findViewById(R.id.add_book_price);
        spinner = (Spinner) findViewById(R.id.spinner);

        titleET.setText(title);
        authorET.setText(author);
        descriptionET.setText(description);
        isbnET.setText(isbn13);

        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(this, R.array.condition_options, android.R.layout.simple_spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(conditionAdapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(this);



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

    public void onClickSubmit(View view) {
        DatabaseReference reference = database.getReference().child("textbooks");
        String key = reference.push().getKey();
        String title = titleET.getText().toString();
        String author = authorET.getText().toString();
        String description = descriptionET.getText().toString();
        String isbn = isbnET.getText().toString();
        Double price = Double.parseDouble(priceET.getText().toString());
        Textbook textbook = new Textbook(title, author, MainActivity.id, price, key, imageUrl, isbn, description, condition);

        reference.child(key).child("title").setValue(textbook.getTitle());
        reference.child(key).child("author").setValue(textbook.getAuthor());
        reference.child(key).child("price").setValue(textbook.getPrice());
        reference.child(key).child("seller").setValue(MainActivity.id);
        reference.child(key).child("description").setValue(textbook.getDescription());
        reference.child(key).child("imageUrl").setValue(textbook.getImageUrl());
        reference.child(key).child("isbn13").setValue(textbook.getIsbn13());
        reference.child(key).child("uniqueID").setValue(key);
        reference.child(key).child("condition").setValue(textbook.getCondition());

        Intent intent = new Intent(this, ProfileLogin.class);
        intent.putExtra("ADDED_BOOK_MESSAGE", "Added " + textbook.getTitle());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String condition = parent.getItemAtPosition(position).toString();
        this.condition = condition;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
