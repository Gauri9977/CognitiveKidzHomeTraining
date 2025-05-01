package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class webview extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the WebView
        webView = new WebView(this);
        setContentView(webView);

        // Get the URL from the intent
        String url = getIntent().getStringExtra("url");

        if (url != null && !url.isEmpty()) {
            // Set WebViewClient to ensure links open within the WebView
            webView.setWebViewClient(new WebViewClient());
            // Enable JavaScript for dynamic content
            webView.getSettings().setJavaScriptEnabled(true);

            // Load the URL in WebView
            webView.loadUrl(url);
        } else {
            // Handle error if URL is invalid or missing
            Toast.makeText(this, "Invalid or missing URL", Toast.LENGTH_SHORT).show();
        }
    }
}
