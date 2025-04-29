package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class ChatbotActivity extends AppCompatActivity {

    WebView chatbotWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatbotWebView = findViewById(R.id.chatbot_webview);
        chatbotWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = chatbotWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        chatbotWebView.loadUrl("https://mk090311.github.io/MyBuddy/");
    }
}
