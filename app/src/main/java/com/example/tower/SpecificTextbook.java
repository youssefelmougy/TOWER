package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.NumberFormat;

public class SpecificTextbook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_textbook);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String title = extras.getString("BOOK_TITLE");
        String author = extras.getString("BOOK_AUTHOR");
        long seller = extras.getLong("BOOK_SELLER");
        Double price = extras.getDouble("BOOK_PRICE");
        TextView specificTitle = (TextView)findViewById(R.id.specific_title);
        TextView specificAuthor = (TextView)findViewById(R.id.specific_author);
        TextView specificSeller = (TextView)findViewById(R.id.specific_seller);
        TextView specificPrice = (TextView)findViewById(R.id.specific_price);

        specificTitle.setText(title);
        specificAuthor.setText(author);
        specificSeller.setText("Seller: " + seller);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        specificPrice.setText(formatter.format(price));

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
                        startActivity(new Intent(getApplicationContext(), ProfileLogin.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    public void onClickReturn(View view) {
        Intent intent = new Intent(this, ProfileLogin.class);
        startActivity(intent);
    }
}
