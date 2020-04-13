package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;

public class SpecificTextbook extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String originatingClass;
    String uniqueID;
    Boolean isSeller = false;
    String title;
    String description;
    String searchQuery;
    String imageUrl;
    String isbn13;
    long seller;
    LinearLayout linearLayout;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_textbook);

        linearLayout = findViewById(R.id.linear_layout);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        context = this;

        originatingClass = extras.getString("CLASS_FROM");
        uniqueID = extras.getString("UNIQUE_ID");
        title = extras.getString("BOOK_TITLE");
        final String author = extras.getString("BOOK_AUTHOR");
        seller = extras.getLong("BOOK_SELLER");
        Double price = extras.getDouble("BOOK_PRICE");
        imageUrl = extras.getString("IMAGE_URL");
        description = extras.getString("BOOK_DESCRIPTION");
        isbn13 = extras.getString("BOOK_ISBN");
        if(extras.getString("SEARCH_QUERY") != null) {
            searchQuery = extras.getString("SEARCH_QUERY");
        }

        TextView specificTitle = (TextView)findViewById(R.id.specific_title);
        TextView specificAuthor = (TextView)findViewById(R.id.specific_author);
        TextView specificSeller = (TextView)findViewById(R.id.specific_seller);
        TextView specificPrice = (TextView)findViewById(R.id.specific_price);
        TextView specificDescription = (TextView)findViewById(R.id.specific_description);
        TextView isbnText = findViewById(R.id.isbn_textview);

        Button removeOrContact = (Button) findViewById(R.id.removeOrContact);
        ImageView bookCover = (ImageView) findViewById(R.id.book_cover);

        if(seller == MainActivity.id) {
            isSeller = true;
        }

        if(isSeller) {
            removeOrContact.setText("DELETE THIS LISTING");
        }

        if(originatingClass.equals("GoogleSuggestions")) {
            linearLayout.removeView(removeOrContact);
            linearLayout.removeView(specificPrice);
            linearLayout.removeView(specificSeller);
            Button button = new Button(this);
            button.setText("This Looks Like My Book");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            button.setLayoutParams(params);
            params.gravity=Gravity.CENTER;
            linearLayout.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddBookFinal.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("BOOK_TITLE", title);
                    bundle.putString("BOOK_AUTHOR", author);
                    bundle.putString("BOOK_ISBN", isbn13);
                    bundle.putString("BOOK_DESCRIPTION", description);
                    bundle.putString("IMAGE_URL", imageUrl);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            Picasso.get().load(imageUrl).fit().into(bookCover);
        }

        Picasso.get().load(imageUrl).placeholder(R.drawable.textbook).fit().into(bookCover);
        specificTitle.setText(title);
        specificAuthor.setText(author);
        specificSeller.setText("Seller: " + seller);
        specificDescription.setText(description);
        isbnText.setText("ISBN-13: " + isbn13);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        specificPrice.setText(formatter.format(price));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        if(originatingClass.equals("MainActivity")) {
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
        else if (originatingClass.equals("Profile")){
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
                }

                return false;
            }
        });
    }

    public void onClickReturn(View view) {
        Intent intent = goBack();
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    public void onClickContactDelete(View view) {
        DatabaseReference ref = database.getReference().child("textbooks/" + uniqueID);
        if(isSeller) {
            ref.removeValue();
            Intent intent = goBack();
            startActivity(intent);
            Toast.makeText(this, "Deleted " + title, Toast.LENGTH_SHORT).show();
        }
        else if(!MainActivity.loggedIn) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
        else {
            DatabaseReference newRef = database.getReference().child("students/" + seller);
            newRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String email = dataSnapshot.child("email").getValue().toString();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "TOWER Offer: " + title);
                    intent.putExtra(Intent.EXTRA_TEXT, "Hi, I'm interested in your book " + title);
                    startActivity(Intent.createChooser(intent, ""));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        finish();
    }

    public Intent goBack() {
        Intent intent;
        if(originatingClass.equals("MainActivity")) {
            intent = new Intent(this, MainActivity.class);
        }
        else if(originatingClass.equals("Search")) {
            intent = new Intent(this, Search.class);
            if(searchQuery != null) {
                intent.putExtra("SEARCH_QUERY", searchQuery);
            }
        }
        else if(originatingClass.equals("GoogleSuggestions")) {
            intent = new Intent(this, GoogleSuggestions.class);
            if(searchQuery != null) {
                intent.putExtra("BOOK_TITLE", searchQuery);
            }
        }
        else{
            intent = new Intent(this, ProfileLogin.class);
        }
        return intent;
    }
}
