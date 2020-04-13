package com.example.tower;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static java.lang.Math.abs;

public class Search extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    GridView gridView;
    EditText searchBox;
    String searchQuery = "";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;
        gridView = (GridView) findViewById(R.id.search_grid_view);
        searchBox  = findViewById(R.id.search_box);

        if(getIntent().getStringExtra("SEARCH_QUERY") != null) {
            searchQuery = getIntent().getStringExtra("SEARCH_QUERY");
            findTextbooks(mContext, gridView, searchQuery);
            searchBox.setText(searchQuery);
        }

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Search Selected
        bottomNavigationView.setSelectedItemId(R.id.search);

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
                        return true;
                    case R.id.profile:
                        if (!MainActivity.loggedIn) {
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                            overridePendingTransition(0, 0);
                        } else {
                            startActivity(new Intent(getApplicationContext(), ProfileLogin.class));
                            overridePendingTransition(0, 0);
                        }
                        return true;
                }

                return false;
            }
        });

    }

    public void onClickSubmit(View view)
    {
        searchBox = findViewById(R.id.search_box);
        searchQuery = searchBox.getText().toString();
        if(!searchQuery.equals("")) {
            findTextbooks(mContext, gridView, searchQuery);
        }

    }

    public void findTextbooks(final Context context, final GridView gridView, final String searchQuery) {
        DatabaseReference reference = database.getReference().child("textbooks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Textbook> textbooks = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Textbook textbook = snapshot.getValue(Textbook.class);
                    textbooks.add(textbook);
                }

                ArrayList<Textbook> orderedList = orderBySimilarityMK2(textbooks, searchQuery);
                TextbookAdapter adapter = new TextbookAdapter(context, orderedList, getLocalClassName(), searchQuery);
                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Textbook> orderBySimilarityMK2(ArrayList<Textbook> originalList, String searchQuery)
    {
        ArrayList<Textbook> orderedList = new ArrayList<Textbook>();
        HashMap<Integer, ArrayList<Textbook>> map = new HashMap<Integer, ArrayList<Textbook>>();
        int LLCS; //Length of Longest Common Substring.

        for (Textbook i : originalList)
        {
            String combinedInfo = i.getTitle()+i.getAuthor()+i.getIsbn13();
            LLCS = longestCommonSubstring
                    (
                            combinedInfo.replaceAll("\\s","").toLowerCase().toCharArray(),
                            searchQuery.replaceAll("\\s","").toLowerCase().toCharArray()
                    );

            if(map.get(LLCS) == null)
            {
                map.put(LLCS, new ArrayList<Textbook>());
            }
            map.get(LLCS).add(i);
        }

        for(int i = searchQuery.length(); i >= 0; i--)
        {
            if(map.get(i) != null)
            {
                orderedList.addAll(map.get(i));
            }
        }

        return orderedList;
    }


    public static int longestCommonSubstring(char[] A, char[] B)
    {
        int[][] LCS = new int[A.length + 1][B.length + 1];
        // if A is null then LCS of A, B =0
        for (int i = 0; i <= B.length; i++)
        {
            LCS[0][i] = 0;
        }

        // if B is null then LCS of A, B =0
        for (int i = 0; i <= A.length; i++)
        {
            LCS[i][0] = 0;
        }

        //fill the rest of the matrix
        for (int i = 1; i <= A.length; i++) {
            for (int j = 1; j <= B.length; j++) {
                if (A[i - 1] == B[j - 1]) {
                    LCS[i][j] = LCS[i - 1][j - 1] + 1;
                } else {
                    LCS[i][j] = 0;
                }
            }
        }
        int result = -1;
        for (int i = 0; i <= A.length; i++) {
            for (int j = 0; j <= B.length; j++) {
                if (result < LCS[i][j])
                {
                    result = LCS[i][j];
                }
            }
        }
        return result;

    }
}