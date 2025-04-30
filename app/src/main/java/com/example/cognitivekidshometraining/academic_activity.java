package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class academic_activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fetchChildNameAndCheckActivities();
    }

    private void fetchChildNameAndCheckActivities() {
        String uid = mAuth.getCurrentUser().getUid();

        mDatabase.child("Users").child(uid).child("ChildData").child("child_name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        childName = snapshot.getValue(String.class);
                        if (childName != null) {
                            checkIfActivityAssigned("Counting Game", R.id.Counting_Game, R.id.Counting_Game_locked_overlay, "https://cognoappproject.github.io/Counting_Game/");
                            checkIfActivityAssigned("Learn Colors", R.id.LearnColors, R.id.LearnColors_locked_overlay, "https://cognoappproject.github.io/LearnColors/");
                            checkIfActivityAssigned("Shape Sorter", R.id.ShapeSorter, R.id.ShapeSorter_locked_overlay, "https://cognoappproject.github.io/ShapeSorter/");
                            checkIfActivityAssigned("Sequencing Tasks Game", R.id.Sequencing_Tasks_Game, R.id.Sequencing_Tasks_Game_locked_overlay, "https://cognoappproject.github.io/Sequencing-Tasks-Game/");
                            checkIfActivityAssigned("Color Sorting Game", R.id.Color_Sorting_Game, R.id.Color_Sorting_Game_locked_overlay, "https://cognoappproject.github.io/Color-Sorting-Game/");
                            checkIfActivityAssigned("Number Sequence Game", R.id.Number_Sequence_Game, R.id.Number_Sequence_Game_locked_overlay, "https://cognoappproject.github.io/Number-Sequence-Game/");
                            checkIfActivityAssigned("Word Builder", R.id.WordBuilder, R.id.WordBuilder_locked_overlay, "https://cognoappproject.github.io/WordBuilder/");
                            checkIfActivityAssigned("Reaction Time Tap Game", R.id.Reaction_Time_Tap_Game, R.id.Reaction_Time_Tap_Game_locked_overlay, "https://cognoappproject.github.io/Reaction-Time-Tap-Game/");
                            checkIfActivityAssigned("Bubble Pop Game", R.id.Bubble_Pop_Game, R.id.Bubble_Pop_Game_locked_overlay, "https://cognoappproject.github.io/Bubble-Pop-Game/");
                            checkIfActivityAssigned("Memory Card Match", R.id.Memory_Card_Match, R.id.Memory_Card_Match_locked_overlay, "https://cognoappproject.github.io/Memory-Card-Match/");
                        } else {
                            Toast.makeText(academic_activity.this, "Child name not found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("AcademicActivity", "Failed to load child name: " + error.getMessage());
                    }
                });
    }

    private void checkIfActivityAssigned(String activityName, int layoutId, int lockOverlayId, String url) {
        DatabaseReference actRef = mDatabase.child("AssignedActivities").child(childName);

        actRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAssigned = false;

                for (DataSnapshot dateSnap : snapshot.getChildren()) {
                    DataSnapshot onlineSnap = dateSnap.child("online");
                    if (onlineSnap.exists()) {
                        for (DataSnapshot activitySnap : onlineSnap.getChildren()) {
                            String name = activitySnap.child("activity_name").getValue(String.class);
                            Log.d("CheckActivity", "Comparing: " + activityName + " with " + name);
                            if (name != null && activityName.equalsIgnoreCase(name.trim())) {
                                isAssigned = true;
                                break;
                            }
                        }
                    }
                    if (isAssigned) break;
                }

                LinearLayout layout = findViewById(layoutId);
                View lockOverlay = findViewById(lockOverlayId);

                if (layout == null || lockOverlay == null) {
                    Log.e("AcademicActivity", "Missing view for: " + activityName);
                    return;
                }

                if (isAssigned) {
                    lockOverlay.setVisibility(View.GONE);
                    layout.setEnabled(true);
                    layout.setAlpha(1.0f);
                    layout.setOnClickListener(v -> openWebLink(url));
                } else {
                    lockOverlay.setVisibility(View.VISIBLE);
                    layout.setEnabled(false);
                    layout.setAlpha(0.5f);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AcademicActivity", "Error checking activity: " + error.getMessage());
            }
        });
    }

    private void openWebLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
