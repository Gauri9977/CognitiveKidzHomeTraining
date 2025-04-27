package com.example.cognitivekidshometraining;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    TextView aboutTitle, aboutDescription, version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        aboutTitle = findViewById(R.id.aboutTitle);
        aboutDescription = findViewById(R.id.aboutDescription);
        version = findViewById(R.id.appVersion);
    }
}
