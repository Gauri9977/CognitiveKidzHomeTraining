package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private DatabaseReference usersRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.textcolour)));
        }

        emailEditText = findViewById(R.id.emaiLs);
        passwordEditText = findViewById(R.id.passs);
        Button loginButton = findViewById(R.id.sign);
        Button registerButton = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();

        // Firebase Database reference
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Add dd/mm/yyyy formatting to password field
        passwordEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String cleanC = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }

                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%04d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s",
                            clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    passwordEditText.setText(current);
                    passwordEditText.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Register button click
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(login.this, registration.class));
            finish();
        });

        // Login button click
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().replaceAll("/", "");

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(login.this, "Please fill in all the information", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        if (email.equals("nchavan00@gmail.com")) {
                            Intent intent=new Intent(login.this, dr_home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            startActivity(new Intent(login.this, home.class));
                        }

                        // optional, so user cannot go back
                    } else {
                        // Login failed
                        Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

//    private void loginUser(String email, String password) {
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean matchFound = false;
//
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    String dbEmail = userSnapshot.child("email").getValue(String.class);
//                    String dbPassword = userSnapshot.child("password").getValue(String.class);
//
//                    if (email.equalsIgnoreCase(dbEmail) && password.equals(dbPassword)) {
//                        matchFound = true;
//
//                        Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//
//                        if (email.equals("nchavan00@gmail.com")) {
//                            Intent intent=new Intent(login.this, dr_home.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                        else {
//                            startActivity(new Intent(login.this, home.class));
//                        }
//                        finish();
//                        break;
//                    }
//                }
//
//                if (!matchFound) {
//                    Toast.makeText(login.this, "Invalid email or password", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
