package com.example.cognitivekidshometraining;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dr_todo extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;
    private Spinner spinnerChildName;
    private TextView disorder_tv, selectedActivities_tv;
    private LinearLayout onlineActivitiesLayout;
    private LinearLayout offlineActivitiesLayout;
    private Button saveActivitiesBtn;

    private final List<String> childNames = new ArrayList<>();
    private final HashMap<String, String> childDisorderMap = new HashMap<>();
    private final HashMap<String, String> selectedActivities = new HashMap<>();
    private final HashMap<String, String> offlineActivityCategoryMap = new HashMap<>();

    private DatabaseReference usersRef;
    private DatabaseReference onlineActivitiesRef;
    private DatabaseReference offlineActivitiesRef;
    private DatabaseReference assignedActivitiesRef;

    private String currentDisorderText = "";
    private String selectedChildName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_todo);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("\uD83D\uDCDD Assign Activities");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();

        DrawerLayout drawer = findViewById(R.id.dr_todo_page);
        toggle = new ActionBarDrawerToggle(dr_todo.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();

        spinnerChildName = findViewById(R.id.spinnerChildName);
        disorder_tv = findViewById(R.id.disorder_tv);
        selectedActivities_tv = findViewById(R.id.selectedActivities_tv);
        onlineActivitiesLayout = findViewById(R.id.onlineActivitiesLayout);
        offlineActivitiesLayout = findViewById(R.id.offlineActivitiesLayout);
        saveActivitiesBtn = findViewById(R.id.saveActivitiesBtn);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        onlineActivitiesRef = FirebaseDatabase.getInstance().getReference("OnlineActivities");
        offlineActivitiesRef = FirebaseDatabase.getInstance().getReference("OfflineActivities");
        assignedActivitiesRef = FirebaseDatabase.getInstance().getReference("AssignedActivities");

        loadChildrenFromFirebase();
        loadOnlineActivities();
        loadOfflineActivities();

        saveActivitiesBtn.setOnClickListener(view -> saveSelectedActivities());
    }

    private void loadChildrenFromFirebase() {
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot childDataSnapshot = userSnapshot.child("ChildData");
                    if (childDataSnapshot.exists()) {
                        String name = childDataSnapshot.child("child_name").getValue(String.class);
                        String disorder = childDataSnapshot.child("disability_type").getValue(String.class);

                        if (name != null && !name.trim().isEmpty()) {
                            childNames.add(name);
                            childDisorderMap.put(name, disorder != null ? disorder : "Not specified");
                        }
                    }
                }

                if (childNames.isEmpty()) {
                    Toast.makeText(dr_todo.this, "No children found in Users table", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(dr_todo.this, android.R.layout.simple_spinner_dropdown_item, childNames);
                spinnerChildName.setAdapter(adapter);

                spinnerChildName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedChildName = parent.getItemAtPosition(position).toString();
                        String disorder = childDisorderMap.get(selectedChildName);
                        currentDisorderText = "Disorder: " + (disorder != null ? disorder : "Not specified");
                        updateDisorderAndSelectionText();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        currentDisorderText = "Disorder:";
                        updateDisorderAndSelectionText();
                    }
                });

            } else {
                Toast.makeText(dr_todo.this, "Failed to load child data from Users", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadOnlineActivities() {
        onlineActivitiesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                    String activityName = activitySnapshot.child("activity_name").getValue(String.class);
                    if (activityName != null && !activityName.trim().isEmpty()) {
                        addCheckboxToLayout(onlineActivitiesLayout, activityName);
                    }
                }
            } else {
                Toast.makeText(dr_todo.this, "Failed to load online activities", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOfflineActivities() {
        offlineActivitiesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();

                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String category = categorySnapshot.getKey();
                    TextView categoryLabel = new TextView(this);
                    categoryLabel.setText(category);
                    categoryLabel.setTextSize(17);
                    categoryLabel.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                    categoryLabel.setPadding(0, 12, 0, 6);
                    offlineActivitiesLayout.addView(categoryLabel);

                    for (DataSnapshot activitySnapshot : categorySnapshot.getChildren()) {
                        String activityName = activitySnapshot.child("activity_name").getValue(String.class);
                        if (activityName != null && !activityName.trim().isEmpty()) {
                            offlineActivityCategoryMap.put(activityName, category);
                            addCheckboxToLayout(offlineActivitiesLayout, activityName);
                        }
                    }
                }
            } else {
                Toast.makeText(dr_todo.this, "Failed to load offline activities", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCheckboxToLayout(LinearLayout layout, String activityName) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(activityName);
        checkBox.setTextSize(16);
        checkBox.setTextColor(getResources().getColor(android.R.color.black));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedActivities.put(activityName, "selected");
            } else {
                selectedActivities.remove(activityName);
            }
            updateDisorderAndSelectionText();
        });

        layout.addView(checkBox);
    }

    private void updateDisorderAndSelectionText() {
        disorder_tv.setText(currentDisorderText);
        StringBuilder selectedText = new StringBuilder("Selected Activities:\n");
        if (selectedActivities.isEmpty()) {
            selectedText.append("- None");
        } else {
            for (String activity : selectedActivities.keySet()) {
                selectedText.append("- ").append(activity).append("\n");
            }
        }
        selectedActivities_tv.setText(selectedText.toString());
    }

    private boolean isActivityInLayout(LinearLayout layout, String activityName) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox cb = (CheckBox) view;
                if (cb.getText().toString().equals(activityName)) return true;
            }
        }
        return false;
    }

    private void saveSelectedActivities() {
        if (selectedChildName.isEmpty()) {
            Toast.makeText(this, "Please select a child first", Toast.LENGTH_SHORT).show();
            return;
        }

        String assignedTime = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        List<HashMap<String, String>> onlineActivities = new ArrayList<>();
        HashMap<String, List<HashMap<String, String>>> offlineByCategory = new HashMap<>();

        for (String activityName : selectedActivities.keySet()) {
            HashMap<String, String> activityEntry = new HashMap<>();
            activityEntry.put("activity_name", activityName);

            if (isActivityInLayout(onlineActivitiesLayout, activityName)) {
                onlineActivities.add(activityEntry);
            } else {
                String category = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    category = offlineActivityCategoryMap.getOrDefault(activityName, "Uncategorized");
                }

                if (!offlineByCategory.containsKey(category)) {
                    offlineByCategory.put(category, new ArrayList<>());
                }
                offlineByCategory.get(category).add(activityEntry);
            }
        }

        HashMap<String, Object> childData = new HashMap<>();
        if (!onlineActivities.isEmpty()) childData.put("online", onlineActivities);
        if (!offlineByCategory.isEmpty()) childData.put("offline", offlineByCategory);

        assignedActivitiesRef.child(selectedChildName).child(assignedTime).setValue(childData)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Activities saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save activities", Toast.LENGTH_SHORT).show());
    }
}
