// Java: child_activity.java
package com.example.cognitivekidshometraining;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class child_activity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView toolbarLeftImage;
    private ActionBarDrawerToggle toggle;

    private LinearLayout contentContainer;
    private boolean isSubscribed = false; // Replace with real subscription check

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_right_text);
        toolbarLeftImage = toolbar.findViewById(R.id.toolbar_left_image);
        toolbarTitle.setText("Academic");
        toolbarLeftImage.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.dr_child_list);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        contentContainer = findViewById(R.id.academicExpandableContainer);

        setupExpandableSubjects();
    }

    private void setupExpandableSubjects() {
        int[] subjectIds = new int[]{R.id.subject_3_5, R.id.subject_6_10, R.id.subject_11_15, R.id.subject_15_18};

        for (int id : subjectIds) {
            LinearLayout subjectLayout = findViewById(id);
            subjectLayout.setOnClickListener(view -> {
                if (!isSubscribed) {
                    showLockedDialog();
                } else if (id == R.id.subject_3_5) {
                    View content = subjectLayout.findViewById(R.id.subject_content_3_5);
                    content.setVisibility(content.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
                // Add additional checks for other subjects if needed
                else if (id == R.id.subject_6_10) {
                    // Handle visibility toggle for subject 6-10
                    View content = subjectLayout.findViewById(R.id.subject_content_6_10);
                    content.setVisibility(content.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                } else if (id == R.id.subject_11_15) {
                    // Handle visibility toggle for subject 11-15
                    View content = subjectLayout.findViewById(R.id.subject_content_11_15);
                    content.setVisibility(content.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                } else if (id == R.id.subject_15_18) {
                    // Handle visibility toggle for subject 15-18
                    View content = subjectLayout.findViewById(R.id.subject_content_15_18);
                    content.setVisibility(content.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

    private void showLockedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Subscription Required")
                .setMessage("Subscribe to unlock this content.")
                .setPositiveButton("Subscribe Now", (dialog, which) -> {
                    // Launch your in-app purchase flow here
                })
                .setNegativeButton("Maybe Later", null)
                .show();
    }
}
