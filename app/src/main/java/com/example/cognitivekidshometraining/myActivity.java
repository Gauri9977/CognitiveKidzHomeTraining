package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class myActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_left_image;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String childName;

    private LinearLayout activityContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("\uD83D\uDCDA My Past Activities");
        toolbar_left_image.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        activityContainer = findViewById(R.id.activity_list_container);  // Add this ID in XML

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.main);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        loadChildName();
    }

    private void loadChildName() {
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(uid).child("ChildData").child("child_name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        childName = snapshot.getValue(String.class);
                        if (childName != null) {
                            loadTherapySessions();
                            loadActivityResults();
                            loadEarnedRewards();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("myActivity", "Error loading child name: " + error.getMessage());
                    }
                });
    }

    private void loadTherapySessions() {
        mDatabase.child("therapy_and_consultation").child(childName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot session : snapshot.getChildren()) {
                            String status = session.child("status").getValue(String.class);
                            String date = session.child("assignedDate").getValue(String.class);
                            String time = session.child("assignedtime").getValue(String.class);
                            addCard("Therapy Session", "Date: " + date, "Time: " + time, "Status: " + status);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void loadActivityResults() {
        mDatabase.child("activities_result").child(childName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot result : snapshot.getChildren()) {
                            String name = result.child("activity_name").getValue(String.class);
                            Long timeTaken = result.child("time_taken").getValue(Long.class);
                            Long score = result.child("score").getValue(Long.class);
                            addCard(name, "Time Taken: " + timeTaken + " min", "Score: " + score, "Completed");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void loadEarnedRewards() {
        mDatabase.child("rewards_history").child(childName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot entry : snapshot.getChildren()) {
                            String dateTime = entry.getKey();
                            String activityName = entry.child("activity_name").getValue(String.class);
                            String rewardType = entry.hasChild("reward_type") ?
                                    entry.child("reward_type").getValue(String.class) : null;
                            Long score = entry.child("score").getValue(Long.class);
                            Long timeTaken = entry.child("time_taken").getValue(Long.class);

                            String line1 = "Date: " + dateTime;
                            String line2 = (score != null || timeTaken != null)
                                    ? "Score: " + score + ", Time: " + timeTaken + " min"
                                    : "Reward Type: " + rewardType;

                            addCard("Reward from " + activityName, line1, line2, "Unlocked");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("myActivity", "Failed to load rewards_history: " + error.getMessage());
                    }
                });
    }


    private void addCard(String title, String line1, String line2, String status) {
        View card = getLayoutInflater().inflate(R.layout.card_item_layout, activityContainer, false);
        ((TextView) card.findViewById(R.id.card_title)).setText(title);
        ((TextView) card.findViewById(R.id.card_line1)).setText(line1);
        ((TextView) card.findViewById(R.id.card_line2)).setText(line2);
        ((TextView) card.findViewById(R.id.card_status)).setText(status);
        activityContainer.addView(card);
    }
}
