package com.example.cognitivekidshometraining;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class rewards extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;
    private LinearLayout badgeContainer, stickerContainer, trophyContainer, certificateContainer;
    private DatabaseReference db;
    private String childName;
    private FirebaseAuth auth;

    private final Map<String, String> activityToBadge = new HashMap<>();
    private final Map<String, String> activityToSticker = new HashMap<>();

    private final List<String> trophyNames = Arrays.asList(
            "trophy_week1", "trophy_week2", "trophy_week3", "trophy_week4", "trophy_week5",
            "trophy_week6", "trophy_week7", "trophy_week8", "trophy_week9", "trophy_week10"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("\uD83C\uDFC6 Rewards");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.reward_drawer_layout);
        toggle = new ActionBarDrawerToggle(rewards.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();


        badgeContainer = findViewById(R.id.badge_container);
        stickerContainer = findViewById(R.id.sticker_container);
        trophyContainer = findViewById(R.id.trophy_container);
        certificateContainer = findViewById(R.id.certificate_container);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        setupLocalRewardMappings();
        fetchChildNameAndLoad();
    }

    private void setupLocalRewardMappings() {
        activityToBadge.put("Counting Game", "badge_counting_game");
        activityToBadge.put("Bubble Pop Game", "badge_bubble_pop_game");
        activityToBadge.put("Reaction Time Tap Game", "badge_reaction_time_tap_game");
        activityToBadge.put("Memory Card Match", "badge_memory_card_match");
        activityToBadge.put("Word Builder", "badge_word_builder");
        activityToBadge.put("Learn Colors", "badge_learncolors");
        activityToBadge.put("Shape Sorter", "badge_shapesorter");
        activityToBadge.put("Sequencing Tasks Game", "badge_sequencing_tasks_game");
        activityToBadge.put("Color Sorting Game", "badge_color_sorting_game");
        activityToBadge.put("Number Sequence Game", "badge_number_sequence_game");

        activityToSticker.put("Counting Game", "sticker_counting_game");
        activityToSticker.put("Bubble Pop Game", "sticker_bubble_pop_game");
        activityToSticker.put("Reaction Time Tap Game", "sticker_reaction_time_tap_game");
        activityToSticker.put("Memory Card Match", "sticker_memory_card_match");
        activityToSticker.put("Word Builder", "sticker_word_builder");
        activityToSticker.put("Learn Colors", "sticker_learncolors");
        activityToSticker.put("Shape Sorter", "sticker_shapesorter");
        activityToSticker.put("Sequencing Tasks Game", "sticker_sequencing_tasks_game");
        activityToSticker.put("Color Sorting Game", "sticker_color_sorting_game");
        activityToSticker.put("Number Sequence Game", "sticker_number_sequence_game");
    }

    private void fetchChildNameAndLoad() {
        String uid = auth.getCurrentUser().getUid();
        db.child("Users").child(uid).child("ChildData").child("child_name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        childName = snapshot.getValue(String.class);
                        loadCompletedActivities();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void loadCompletedActivities() {
        db.child("activities_result").child(childName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Set<String> completed = new HashSet<>();
                        for (DataSnapshot entry : snapshot.getChildren()) {
                            String activityName = entry.child("activity_name").getValue(String.class);
                            if (activityName != null) {
                                completed.add(activityName);
                            }
                        }
                        loadLocalImages(completed);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void loadLocalImages(Set<String> completedActivities) {
        for (Map.Entry<String, String> entry : activityToSticker.entrySet()) {
            boolean unlocked = completedActivities.contains(entry.getKey());
            if (unlocked) {
                saveEarnedReward(entry.getKey(), "Sticker");
            }
            addRewardImage(stickerContainer, entry.getValue(), unlocked);
        }

        for (Map.Entry<String, String> entry : activityToBadge.entrySet()) {
            boolean unlocked = completedActivities.contains(entry.getKey());
            if (unlocked) {
                saveEarnedReward(entry.getKey(), "Badge");
            }
            addRewardImage(badgeContainer, entry.getValue(), unlocked);
        }

        for (String trophy : trophyNames) {
            addRewardImage(trophyContainer, trophy, false);
        }

        addRewardImage(certificateContainer, "certificate", false);
    }

    private void addRewardImage(LinearLayout container, String imageName, boolean unlocked) {
        LinearLayout frame = new LinearLayout(this);
        frame.setOrientation(LinearLayout.VERTICAL);
        frame.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(300, 340);
        frameParams.setMargins(20, 20, 20, 20);
        frame.setLayoutParams(frameParams);

        ImageView img = new ImageView(this);
        img.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        img.setImageResource(resId);

        if (!unlocked) {
            img.setColorFilter(Color.argb(150, 150, 150, 150), PorterDuff.Mode.SRC_ATOP);

            TextView lockedLabel = new TextView(this);
            lockedLabel.setText("Locked");
            lockedLabel.setTextColor(Color.RED);
            lockedLabel.setTextSize(14);
            lockedLabel.setGravity(Gravity.CENTER);
            frame.addView(lockedLabel);
        }

        frame.addView(img);
        container.addView(frame);
    }

    private void saveEarnedReward(String activityName, String rewardType) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        // Get the current datetime (to store the completion time)
        String datetimeCompleted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        DatabaseReference ref = db.child("rewards_history").child(childName).child(datetimeCompleted);
        ref.child("reward_type").setValue(rewardType);
        ref.child("activity_name").setValue(activityName);


    }
}
