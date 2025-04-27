package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordInput, newPasswordInput;
    private Button updateButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar_change_password);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Change Password");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        oldPasswordInput = findViewById(R.id.edit_old_password);   // UI only, not validated
        newPasswordInput = findViewById(R.id.edit_new_password);
        updateButton = findViewById(R.id.btn_update_password);
        mAuth = FirebaseAuth.getInstance();

        updateButton.setOnClickListener(v -> {
            String newPass = newPasswordInput.getText().toString().trim();

            if (!isValidDate(newPass)) {
                Toast.makeText(this, "Password must be a valid date (e.g. dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.updatePassword(newPass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Update failed. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // Helper method to check valid date format (dd/MM/yyyy)
    private boolean isValidDate(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
