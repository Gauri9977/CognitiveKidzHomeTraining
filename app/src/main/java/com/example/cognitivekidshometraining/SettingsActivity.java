package com.example.cognitivekidshometraining;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        Button btnChangeUsername = findViewById(R.id.btnChangeUsername);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        Button btnFAQs = findViewById(R.id.btnFAQs);
        Button btnContact = findViewById(R.id.btnContact);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        btnChangeUsername.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ChangeUsernameActivity.class));
        });

        btnChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
        });

        btnFAQs.setOnClickListener(v -> openFAQs());
        btnContact.setOnClickListener(v -> contactUs());
        btnLogout.setOnClickListener(v -> logout());
        btnDeleteAccount.setOnClickListener(v -> confirmDeleteAccount());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back arrow click
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeUsername() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter new username");

        new AlertDialog.Builder(this)
                .setTitle("Change Username")
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newUsername = input.getText().toString().trim();
                    if (!newUsername.isEmpty()) {
                        updateUsernameInDatabase(newUsername);
                    } else {
                        Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateUsernameInDatabase(String newUsername) {
        String email = mAuth.getCurrentUser().getEmail();
        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            userSnapshot.getRef().child("user_name").setValue(newUsername);
                            Toast.makeText(SettingsActivity.this, "Username updated", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SettingsActivity.this, "Update failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changePassword() {
        EditText input = new EditText(this);
        input.setHint("Enter new password");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newPass = input.getText().toString().trim();
                    if (newPass.length() < 6) {
                        Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        user.updatePassword(newPass).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openFAQs() {
        startActivity(new Intent(SettingsActivity.this, FAQsActivity.class));
    }

    private void contactUs() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:support@cognitivekidz.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Cognitive Kidz Support Request");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi Cognitive Kidz Team,\n\nI need help regarding...");
        startActivity(Intent.createChooser(intent, "Contact Support"));
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, login.class));
        finish();
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This cannot be undone.")
                .setPositiveButton("Yes, Delete", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        userSnapshot.getRef().removeValue();
                    }
                    user.delete().addOnCompleteListener(task -> {
                        Toast.makeText(SettingsActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SettingsActivity.this, login.class));
                        finish();
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SettingsActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
