package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class feedback extends AppCompatActivity {

    private EditText experienceInput, suggestionInput;
    private RatingBar ratingBar;
    private Button submitButton;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView toolbarLeftImage;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initializeViews();
        setupToolbar();
        setupDrawer();
        handleSubmit();
    }

    private void initializeViews() {
        experienceInput = findViewById(R.id.experience_input);
        suggestionInput = findViewById(R.id.suggestion_input);
        ratingBar = findViewById(R.id.rating_bar);
        submitButton = findViewById(R.id.submit_button);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_right_text);
        toolbarLeftImage = toolbar.findViewById(R.id.toolbar_left_image);
        drawerLayout = findViewById(R.id.feedback_drawer);
    }

    private void setupToolbar() {
        toolbarTitle.setText("Feedback");
        toolbarLeftImage.setVisibility(View.GONE);
    }

    private void setupDrawer() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
    }

    private void handleSubmit() {
        submitButton.setOnClickListener(view -> {
            String experience = experienceInput.getText().toString().trim();
            String suggestion = suggestionInput.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (experience.isEmpty()) {
                Toast.makeText(this, "Please enter your experience.", Toast.LENGTH_SHORT).show();
            } else if (suggestion.isEmpty()) {
                Toast.makeText(this, "Please provide your suggestion.", Toast.LENGTH_SHORT).show();
            } else if (rating == 0) {
                Toast.makeText(this, "Please rate the app.", Toast.LENGTH_SHORT).show();
            } else {
                String message;
                if (rating == 5.0f) message = "Excellent! 5 Stars!";
                else if (rating >= 4.0f) message = "Great! Thanks for your feedback.";
                else if (rating >= 3.0f) message = "Good! We will try to improve.";
                else message = "Thank you for your input.";

                String rateScore = Float.toString(rating);

                showThankYouDialog(message);
                submitFeedback(experience, suggestion, rateScore);
            }
        });
    }

    private void submitFeedback(String experience, String suggestion, String rateScore) {
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, String> feedbackData = new HashMap<>();
        feedbackData.put("experience", experience);
        feedbackData.put("suggestion", suggestion);
        feedbackData.put("rating", rateScore);

        mDatabase.child("Users").child(userId).child("feedback").setValue(feedbackData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        Toast.makeText(feedback.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
//                        finish(); // close the feedback screen
                    } else {
//                        Toast.makeText(feedback.this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showThankYouDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Thank You!")
                .setMessage(message + "\n\nYour feedback has been submitted.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialogInterface, id) -> {
                    dialogInterface.dismiss();
                    finish();
                })
                .create();

        dialog.show();
    }
}
