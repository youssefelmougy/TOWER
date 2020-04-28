package com.example.tower;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.joanzapata.pdfview.PDFView;

public class EULA extends AppCompatActivity {
    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_u_l);

        String pdf = "eula.pdf";
        pdfView = findViewById(R.id.pdfView);

        pdfView.fromAsset(pdf).load();


    }

    public void goBack(View view) {
        onBackPressed();
        overridePendingTransition(0,0);
    }
}
