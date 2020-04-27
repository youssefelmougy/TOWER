package com.example.tower;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.joanzapata.pdfview.PDFView;

public class PrivacyPolicy extends AppCompatActivity {
    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        String pdf = "privacy_policy.pdf";
        pdfView = findViewById(R.id.pdfView);

        pdfView.fromAsset(pdf).load();


    }

    public void goBack(View view) {
        onBackPressed();
        overridePendingTransition(0,0);
    }


}
