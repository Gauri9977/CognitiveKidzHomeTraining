package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class FormWebViewActivity extends AppCompatActivity {

    // URL of the Google Form
    //private static final String FORM_URL = "https://cognoappproject.github.io/Counting_Game/";
    private static final String FORM_URL = "https://docs.google.com/forms/d/e/1FAIpQLSd-v155MHUa5mxQUi78x8ZkfNqpmwMaNmJJhAPSvUPJZcMqGQ/viewform?usp=sf_link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_webview); // Set layout for the new activity

        // Find the WebView by its ID
        WebView webView = findViewById(R.id.webView);

        // Set WebView settings (Enable JavaScript)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript for the form to work properly

        // Set WebViewClient to open links within the WebView itself
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Check if the URL contains a string indicating the form submission is complete
                if (url.contains("thankyou") || url.contains("response")) {
                    // Form has been submitted, redirect to HomeActivity
                    Intent intent = new Intent(FormWebViewActivity.this, home.class);
                    startActivity(intent);
                    finish();  // Close the current activity to prevent the user from going back to the form
                }
            }
        });

        // Load the Google Form URL into the WebView
        webView.loadUrl(FORM_URL);
    }

    // Handle the back button press to go back within the WebView if possible
    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webView);
        if (webView.canGoBack()) {
            webView.goBack(); // Go back to the previous page in the WebView
        } else {
            super.onBackPressed(); // Exit the activity if WebView cannot go back
        }
    }
}
