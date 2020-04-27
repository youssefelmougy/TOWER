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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoogleSuggestions extends AppCompatActivity {

    private RequestQueue mQueue;
    private GridView gridView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String rawTitle;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_suggestions);

        context = this;
        mQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        rawTitle = intent.getStringExtra("BOOK_TITLE");
        gridView = findViewById(R.id.api_grid);

        rawTitle = rawTitle.trim();
        String title = rawTitle.toLowerCase();
        String[] titleArr = title.split(" ");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < titleArr.length; i++) {
            if(i == titleArr.length-1) {
                sb.append(titleArr[i]);
                break;
            }
            sb.append(titleArr[i] + "+");
        }
        String formattedTitle = sb.toString();
        String apiLink = "https://www.googleapis.com/books/v1/volumes?q="+formattedTitle+"&maxResults=40";
        jsonParse(apiLink);

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

    public void notBookButton(View view) {
        Intent intent = new Intent(this, AddBookFinal.class);
        startActivity(intent);
    }

    private void jsonParse(String apiLink) {
        String url = apiLink;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Textbook> textbooks = new ArrayList<>();
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    int success = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {
                            if(success == 20) {
                                break;
                            }
                            JSONObject items = jsonArray.getJSONObject(i);
                            JSONObject volumeInfo = items.getJSONObject("volumeInfo");
                            if(!volumeInfo.has("title") && !volumeInfo.has("authors")) continue;
                            String title = volumeInfo.getString("title");
                            JSONArray authorsArr = volumeInfo.getJSONArray("authors");
                            String authors = "";
                            String description = volumeInfo.getString("description");
                            for(int j = 0; j < authorsArr.length(); j++) {
                                String author = authorsArr.getString(j);
                                if(j == authorsArr.length()-1){
                                    authors += author;
                                    continue;
                                }
                                authors += author + ", ";
                            }
                            if(!volumeInfo.has("industryIdentifiers")) continue;
                            String isbn13 = "";
                            JSONArray industryIdentifiersArr = volumeInfo.getJSONArray("industryIdentifiers");
                            for(int j = 0; j < industryIdentifiersArr.length(); j++) {
                                JSONObject industryIdentifiers = industryIdentifiersArr.getJSONObject(j);
                                if(industryIdentifiers.getString("type").equals("ISBN_13")) {
                                    isbn13 = industryIdentifiers.getString("identifier");
                                }
                            }
                            if (isbn13.equals("")) continue;
                            if(!volumeInfo.has("imageLinks")) continue;
                            JSONObject linkObject = volumeInfo.getJSONObject("imageLinks");
                            String imageUrl = linkObject.getString("thumbnail");
                            imageUrl = imageUrl.substring(0,4)+"s" + imageUrl.substring(4)+".jpg";
                            Textbook textbook = new Textbook(title, authors, isbn13, imageUrl, description);
                            textbooks.add(textbook);
                            success++;
                        } catch (JSONException e) {
                            continue;
                        }

                    }

                } catch (JSONException e) {
                    Log.d("MikeC", "Not found?");
                    e.printStackTrace();
                }
                gridView.setAdapter(new TextbookAdapter(context, textbooks, getLocalClassName(),rawTitle));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MikeC", "onErrorResponse fuck");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void goBack(View view) {
        onBackPressed();
        overridePendingTransition(0,0);
    }


}
