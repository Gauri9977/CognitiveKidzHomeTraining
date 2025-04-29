package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class form extends AppCompatActivity {
    String name;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Button btnFillForm = findViewById(R.id.btnFillForm);

        Bundle extra = getIntent().getExtras();
        assert extra != null;
        if (!extra.isEmpty()){
            name = extra.getString("username");
            email = extra.getString("email");
            password = extra.getString("password");
        }

        btnFillForm.setOnClickListener(v -> {
            Intent i = new Intent(form.this, GoogleForm.class);
            i.putExtra("username", name);
            i.putExtra("email", email);
            i.putExtra("password", password);
            startActivity(i);
        });


    }
}
