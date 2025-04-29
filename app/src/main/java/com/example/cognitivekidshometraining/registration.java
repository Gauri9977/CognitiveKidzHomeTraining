package com.example.cognitivekidshometraining;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cognitivekidshometraining.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;  // Correct import
import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.*;

public class registration extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;
    private static final String PREFS_NAME = "CognitivePrefs";
    private static final String CONSENT_ACCEPTED_KEY = "consentAccepted";

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference usersRef;// Firebase Auth Instance
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.textcolour)));
        }

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.emaiL);
        passwordEditText = findViewById(R.id.pass);
        Button signUpButton = findViewById(R.id.sign);

        signUpButton.setOnClickListener(v -> {
            if (checkConsentAccepted()) {
                validateAndProceed();
            } else {
                showConsentDialogAndThenProceed();
            }
        });
    }

    private boolean checkConsentAccepted() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(CONSENT_ACCEPTED_KEY, false);
    }

    private void showConsentDialogAndThenProceed() {

        String consentMessage = "By proceeding, I confirm that:\n\n" +
                "• I understand this app is designed to help children with cognitive developmental disabilities through interactive, personalized exercises.\n\n" +
                "• My child will use the app regularly under my supervision, completing cognitive activities supported by adaptive learning tools.\n\n" +
                "• To ensure healthy usage, I will limit screen time & ensure regular breaks, and encourage physical activity.\n\n" +
                "• My child’s data will be securely stored and used only to personalize and improve his learning experience.\n\n" +
                "• I acknowledge the app’s goal is to enhance cognitive skills, boost motivation and support personalized learning.\n\n" +
                "☑️ I consent to my child’s use of the Cognitive Kidz Home Training App under these conditions.";

        new AlertDialog.Builder(this)
                .setTitle("Parental Consent Form")
                .setMessage(consentMessage)
                .setCancelable(false)
                .setPositiveButton("I Agree", (d, which) -> {
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    prefs.edit().putBoolean(CONSENT_ACCEPTED_KEY, true).apply();
                    validateAndProceed();
                })
                .setNegativeButton("Decline", (d, which) -> {
                    Toast.makeText(this, "Consent is required to use this app", Toast.LENGTH_LONG).show();
                    finish();
                })
                .show();
    }

    private void validateAndProceed() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Step 1: Validate that all fields (name, email, and password) are filled
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all the information", Toast.LENGTH_SHORT).show();
            return;
        }

        // Step 2: Validate the password format (should be a valid date in ddMMyyyy format)
        if (!isValidDate(password)) {
            Toast.makeText(this, "Password should be a valid date (e.g., ddMMyyyy)", Toast.LENGTH_SHORT).show();
            return;
        }

//        saveUserToFirebase(name, email, password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        // Save Username to database
                        mDatabase.child("Users").child(userId).child("username").setValue(name);
                        mDatabase.child("Users").child(userId).child("email").setValue(email);
                        mDatabase.child("Users").child(userId).child("password").setValue(password);

                        // Move to Form Activity
                        Intent i = new Intent(registration.this, form.class);
                        i.putExtra("username", name);
                        i.putExtra("email", email);
                        i.putExtra("password", password);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Step 3: Check if the email is already registered with Firebase
        // Firebase user registration (if not already done)

//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, authTask -> {
//                    if (authTask.isSuccessful()) {
//                        proceedToFormPage(name, email, password);
//                    } else {
//                        Toast.makeText(this, "Email already registered. Try logging in.", Toast.LENGTH_SHORT).show();
//                    }
//                });

    }


    private void saveUserToFirebase(String username, String email, String password) {
        usersRef.orderByChild("email").equalTo(email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            Toast.makeText(registration.this, "Email already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            User user = new User(username, email, password);

//                            String userId = usersRef.push().getKey(); // Create a unique ID
//                            usersRef.child(userId).setValue(user)
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            usersRef.child(uid).setValue(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(registration.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(registration.this, form.class);
                                        i.putExtra("username", username);
                                        i.putExtra("email", email);
                                        i.putExtra("password", password);
                                        startActivity(i);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(registration.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(registration.this, "Failed to check email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Method to navigate to the next page (form activity) if the email is new
    private void proceedToFormPage(String name, String email, String password) {
        // Create an Intent to navigate to the 'form' activity
        Intent i = new Intent(registration.this, form.class);

        // Pass the user details (name, email, password) as extras to the next activity
        i.putExtra("username", name);
        i.putExtra("email", email);
        i.putExtra("password", password);
        Toast.makeText(this, "User is registered successfully.", Toast.LENGTH_SHORT).show();

        // Start the 'form' activity and finish the current registration activity
        startActivity(i);
        finish();
    }


    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
