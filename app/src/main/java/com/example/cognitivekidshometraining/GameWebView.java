package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GameWebView extends AppCompatActivity {

    private WebView webView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private String childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize WebView
        webView = new WebView(this);
        setContentView(webView);

        // Initialize Firebase Database reference and Authentication
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Enable JavaScript for the WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient()); // Keeps links opening within the WebView
        webView.setWebChromeClient(new WebChromeClient()); // Better support for console, alerts, etc.

        // Load the URL passed from the previous activity
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);

        // Fetch child name passed via the Intent
        childName = getIntent().getStringExtra("child_name");

        // Check if the child name was passed
        if (childName == null) {
            Toast.makeText(GameWebView.this, "Child name not found!", Toast.LENGTH_SHORT).show();
        }

        // Add JavaScript interface to communicate with the app
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
    }

    // JavaScript Interface to handle results from the game
    public class WebAppInterface {

        @JavascriptInterface
        public void submitResult(String activityName, int score, int timeTaken) {
            if (childName == null) {
                runOnUiThread(() -> Toast.makeText(GameWebView.this, "Missing child name. Cannot submit result.", Toast.LENGTH_SHORT).show());
                return;
            }

            // Get the current datetime (to store the completion time)
            String datetimeCompleted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            // Log the result for debugging
            Log.d("GameWebView", "Child: " + childName + ", Activity: " + activityName + ", Score: " + score + ", Time Taken: " + timeTaken);

            // Store the results in Firebase under the activities_result table
            DatabaseReference resultRef = mDatabase.child("activities_result").child(childName).child(datetimeCompleted);

            // Store the activity details under the datetime key
            resultRef.child("activity_name").setValue(activityName);
            resultRef.child("score").setValue(score);
            resultRef.child("time_taken").setValue(timeTaken);

            // Optionally, show a toast to confirm submission
            runOnUiThread(() -> Toast.makeText(GameWebView.this, "Results Submitted to Firebase!", Toast.LENGTH_SHORT).show());
        }
    }
}
