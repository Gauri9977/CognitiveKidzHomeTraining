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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class registration extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private static final String PREFS_NAME = "CognitivePrefs";
    private static final String CONSENT_ACCEPTED_KEY = "consentAccepted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.textcolour)));
        }

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.emaiL);
        passwordEditText = findViewById(R.id.pass);
        Button signUpButton = findViewById(R.id.sign);

        // 🔧 Force reset the consent flag for testing (REMOVE this after testing)
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().remove(CONSENT_ACCEPTED_KEY).apply();

        signUpButton.setOnClickListener(v -> {
            if (checkConsentAccepted()) {
                registerUser();  // Only register if consent was already accepted
            } else {
                showConsentDialogAndThenRegister(); // Show consent and register only if accepted
            }
        });
    }

    private boolean checkConsentAccepted() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean accepted = prefs.getBoolean(CONSENT_ACCEPTED_KEY, false);
        Log.d("CONSENT", "Consent already accepted: " + accepted);
        return accepted;
    }

    private void showConsentDialogAndThenRegister() {
        String consentMessage = "By proceeding, I confirm that:\n\n" +
                "• I understand this app is designed to help children with cognitive developmental disabilities through interactive, personalized exercises.\n\n" +
                "• My child will use the app regularly under my supervision, completing cognitive activities supported by adaptive learning tools.\n\n" +
                "• To ensure healthy usage, I will limit screen time & ensure regular breaks, and encourage physical activity.\n\n" +
                "• My child’s data will be securely stored and used only to personalize and improve his learning experience.\n\n" +
                "• I acknowledge the app’s goal is to enhance cognitive skills, boost motivation and support personalized learning.\n\n" +
                "☑️ I consent to my child’s use of the Cognitive Kidz Home Training App under these conditions.";

        Toast.makeText(this, "Showing consent dialog", Toast.LENGTH_SHORT).show();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Parental Consent Form")
                .setMessage(consentMessage)
                .setCancelable(false)
                .setPositiveButton("I Agree", (d, which) -> {
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    prefs.edit().putBoolean(CONSENT_ACCEPTED_KEY, true).apply();
                    Toast.makeText(this, "Consent accepted", Toast.LENGTH_SHORT).show();
                    registerUser();  // Register after consent accepted
                })
                .setNegativeButton("Decline", (d, which) -> {
                    Toast.makeText(this, "Consent is required to use this app", Toast.LENGTH_LONG).show();
                    finish(); // Close the app
                })
                .create();

        dialog.show();
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all the information", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDate(password)) {
            Toast.makeText(this, "Password should be a valid date (e.g., ddMMyyyy)", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean emailExists = !task.getResult().getSignInMethods().isEmpty();
                        if (emailExists) {
                            Toast.makeText(this, "Email already registered. Try logging in.", Toast.LENGTH_SHORT).show();
                        } else {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this, authTask -> {
                                        if (authTask.isSuccessful()) {
                                            saveUserToDatabase(name, email, password);
                                        } else {
                                            Toast.makeText(this, "Registration Failed: " + authTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Error checking email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void saveUserToDatabase(String name, String email, String password) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long maxId = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Object userIdObj = child.child("user_id").getValue();
                    if (userIdObj != null) {
                        try {
                            long userId = Long.parseLong(userIdObj.toString());
                            if (userId > maxId) {
                                maxId = userId;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }

                long newUserId = maxId + 1;
                String createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

                Map<String, Object> user = new HashMap<>();
                user.put("user_id", newUserId);
                user.put("username", name);
                user.put("email", email);
                user.put("password", password);
                user.put("role", "Child");
                user.put("created_at", createdAt);

                usersRef.child(String.valueOf(newUserId)).setValue(user)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(registration.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(registration.this, form.class));
                                finish();
                            } else {
                                Toast.makeText(registration.this, "Database Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(registration.this, "Failed to read data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
