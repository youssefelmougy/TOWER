package com.example.tower;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.os.Handler;

public class Helper {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Context context;
    RequestQueue mQueue;
    public Helper(Context context) {
        this.context = context;
        this.mQueue = Volley.newRequestQueue(context);
    }

    public void deleteAllBooks() {
        DatabaseReference reference = database.getReference().child("textbooks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void floodDatabase(int size) {
        InputStream is = context.getResources().openRawResource(R.raw.popularbooks);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> titles = new ArrayList<>();

        if (is != null) {
            try{
                while(reader.readLine() != null) {
                    titles.add(reader.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < size; i++) {
            String title = titles.get(i);
            jsonParse("https://www.googleapis.com/books/v1/volumes?q="+title+"&maxResults=40");
        }
       /* for (String title: titles) {
            jsonParse("https://www.googleapis.com/books/v1/volumes?q="+title+"&maxResults=1");
        }*/

    }

    private void jsonParse(String apiLink) {
        String url = apiLink;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    int success = 0;
                    Textbook textbook = new Textbook();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            if(success == 1) {
                                Log.d("mikew", "it ran");
                                addBook(textbook);
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
                            textbook = new Textbook(title, authors, isbn13, imageUrl, description);
                            Log.d("MikeX", textbook.getAuthor());
                            success++;
                        } catch (JSONException e) {
                            continue;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void addBook(final Textbook textbook) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseReference reference = database.getReference().child("textbooks");
                String key = reference.push().getKey();

                textbook.setSeller(999999999);
                textbook.setCondition("Good");
                textbook.setPrice(6.99);

                reference.child(key).child("title").setValue(textbook.getTitle());
                reference.child(key).child("author").setValue(textbook.getAuthor());
                reference.child(key).child("price").setValue(textbook.getPrice());
                reference.child(key).child("seller").setValue(textbook.getSeller());
                reference.child(key).child("description").setValue(textbook.getDescription());
                reference.child(key).child("imageUrl").setValue(textbook.getImageUrl());
                reference.child(key).child("isbn13").setValue(textbook.getIsbn13());
                reference.child(key).child("uniqueID").setValue(key);
                reference.child(key).child("condition").setValue(textbook.getCondition());
                //handler.postDelayed(this, 10000);

            }
        }, 10000);

    }
}
