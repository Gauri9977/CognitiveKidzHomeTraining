package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ChangeUsernameActivity extends AppCompatActivity {

    private EditText oldUsernameInput, newUsernameInput;
    private Button updateButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_change_username);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Change Username");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        oldUsernameInput = findViewById(R.id.edit_old_username);
        newUsernameInput = findViewById(R.id.edit_new_username);
        updateButton = findViewById(R.id.btn_update_username);

        // Firebase setup
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Button click listener
        updateButton.setOnClickListener(v -> {
            String oldUsername = oldUsernameInput.getText().toString().trim();
            String newUsername = newUsernameInput.getText().toString().trim();

            if (oldUsername.isEmpty() || newUsername.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            } else {
                updateUsername(oldUsername, newUsername);
            }
        });
    }

    private void updateUsername(String oldUsername, String newUsername) {
        String email = mAuth.getCurrentUser().getEmail();
        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            String currentUsername = userSnap.child("user_name").getValue(String.class);
                            if (currentUsername != null && currentUsername.equals(oldUsername)) {
                                userSnap.getRef().child("user_name").setValue(newUsername);
                                Toast.makeText(ChangeUsernameActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangeUsernameActivity.this, "Old username doesn't match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChangeUsernameActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
