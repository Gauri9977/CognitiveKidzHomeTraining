package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form); // Set the layout for this activity

        // Find the "Fill Form" button by its ID
        Button btnFillForm = findViewById(R.id.btnFillForm);

        // When the button is clicked, navigate to the new activity where the form will be loaded
        btnFillForm.setOnClickListener(v -> {
            // Create an intent to navigate to the new activity
            Intent intent = new Intent(form.this, FormWebViewActivity.class);
            startActivity(intent); // Start the new activity
        });
    }
}
