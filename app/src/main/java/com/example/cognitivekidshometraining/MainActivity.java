package com.example.cognitivekidshometraining;

import static com.example.cognitivekidshometraining.R.id.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.*;
import android.os.Handler;
import android.util.Log;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.LogRecord;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.textcolour)));

        int delayMillis = 2000;
        android.os.Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ensure the Intent and class are correct
                try {
                    Intent intent = new Intent(MainActivity.this, login.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e("MainActivity", "Error starting register activity: " + e.getMessage());
                    // Handle the exception (e.g., show a toast, log error, etc.)
                }
            }
        }, delayMillis);

    }
}
