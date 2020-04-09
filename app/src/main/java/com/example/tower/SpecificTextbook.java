package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    String searchQuery;
    String imageUrl;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_textbook);

        linearLayout = findViewById(R.id.linear_layout);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        originatingClass = extras.getString("CLASS_FROM");
        uniqueID = extras.getString("UNIQUE_ID");
        title = extras.getString("BOOK_TITLE");
        String author = extras.getString("BOOK_AUTHOR");
        long seller = extras.getLong("BOOK_SELLER");
        Double price = extras.getDouble("BOOK_PRICE");
        if(extras.getString("SEARCH_QUERY") != null) {
            searchQuery = extras.getString("SEARCH_QUERY");
            imageUrl = extras.getString("IMAGE_URL");
        }

        TextView specificTitle = (TextView)findViewById(R.id.specific_title);
        TextView specificAuthor = (TextView)findViewById(R.id.specific_author);
        TextView specificSeller = (TextView)findViewById(R.id.specific_seller);
        TextView specificPrice = (TextView)findViewById(R.id.specific_price);
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
            Picasso.get().load(imageUrl).fit().into(bookCover);
        }


        specificTitle.setText(title);
        specificAuthor.setText(author);
        specificSeller.setText("Seller: " + seller);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        specificPrice.setText(formatter.format(price));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if(originatingClass.equals("MainActivity")) {
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
        else if (originatingClass.equals("Profile")){
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
                        startActivity(new Intent(getApplicationContext(), ProfileLogin.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    public void onClickReturn(View view) {
        Intent intent = goBack();
        startActivity(intent);
    }

    public void onClickContactDelete(View view) {
        DatabaseReference ref = database.getReference().child("textbooks/" + uniqueID);
        if(isSeller) {
            ref.removeValue();
            Intent intent = goBack();
            startActivity(intent);
            Toast.makeText(this, "Deleted " + title, Toast.LENGTH_SHORT).show();
        }
    }

    public Intent goBack() {
        Intent intent;
        if(originatingClass.equals("MainActivity")) {
            intent = new Intent(this, MainActivity.class);
        }
        else if(originatingClass.equals("Search")) {
            intent = new Intent(this, Search.class);
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
