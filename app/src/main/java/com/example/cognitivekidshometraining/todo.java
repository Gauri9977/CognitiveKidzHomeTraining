package com.example.cognitivekidshometraining;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.*;

public class todo extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;

    private static final String TAG = "TodoActivity";
    private static final String ONLINE_ACTIVITIES = "OnlineActivities";
    private static final String USERS = "Users";
    private static final String ASSIGNED_ACTIVITIES = "AssignedActivities";
    private static final String ONLINE = "online";
    private static final String OFFLINE = "offline";
    private static final String CHILD_DATA = "ChildData";
    private static final String CHILD_NAME = "child_name";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private HashMap<String, String> activityUrls = new HashMap<>();
    private ListView todoListView;
    private TextView assignedDateTextView, footerMessage;
    private String childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("\uD83D\uDCDD To-Do List");
        toolbar_left_image.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();
        DrawerLayout drawer = findViewById(R.id.todo_drawer);
        toggle = new ActionBarDrawerToggle(todo.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        todoListView = findViewById(R.id.todo_listview);
        assignedDateTextView = findViewById(R.id.assigned_date);
        footerMessage = findViewById(R.id.footer_message);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initializeActivityUrls();

        todoListView.setOnItemClickListener((parent, view, position, id) -> {
            ActivityItem clicked = (ActivityItem) parent.getItemAtPosition(position);
            if (!clicked.isHeader) {
                handleActivityClick(clicked.text);
            }
        });

        loadAssignedActivities();
    }

    private void initializeActivityUrls() {
        mDatabase.child(ONLINE_ACTIVITIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                    String name = activitySnapshot.child("activity_name").getValue(String.class);
                    String url = activitySnapshot.child("url").getValue(String.class);
                    if (name != null && url != null) {
                        activityUrls.put(name, url);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error loading activity URLs: " + error.getMessage());
            }
        });
    }

    private void loadAssignedActivities() {
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase.child(USERS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childName = snapshot.child(CHILD_DATA).child(CHILD_NAME).getValue(String.class);
                if (childName != null) {
                    fetchAllActivities(childName);
                } else {
                    Toast.makeText(todo.this, "No child info found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "User fetch failed: " + error.getMessage());
            }
        });
    }

    private void fetchAllActivities(String childName) {
        DatabaseReference activitiesRef = mDatabase.child(ASSIGNED_ACTIVITIES).child(childName);
        activitiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ActivityItem> displayList = new ArrayList<>();
                StringBuilder allDates = new StringBuilder();

                for (DataSnapshot dateSnap : snapshot.getChildren()) {
                    String dateKey = dateSnap.getKey();
                    allDates.append(dateKey).append("\n");

                    displayList.add(new ActivityItem(true, "📅 " + dateKey));

                    if (dateSnap.child(ONLINE).exists()) {
                        for (DataSnapshot act : dateSnap.child(ONLINE).getChildren()) {
                            String name = act.child("activity_name").getValue(String.class);
                            if (name != null)
                                displayList.add(new ActivityItem(false, "\uD83D\uDCF1 Online: " + name));
                        }
                    }

                    if (dateSnap.child(OFFLINE).exists()) {
                        for (DataSnapshot category : dateSnap.child(OFFLINE).getChildren()) {
                            String cat = category.getKey();
                            for (DataSnapshot act : category.getChildren()) {
                                String name = act.child("activity_name").getValue(String.class);
                                if (name != null)
                                    displayList.add(new ActivityItem(false, "\uD83C\uDFCB Offline (" + cat + "): " + name));
                            }
                        }
                    }
                }

                if (!displayList.isEmpty()) {
                    assignedDateTextView.setText("Assigned Dates:\n" + allDates.toString().trim());
                    footerMessage.setVisibility(View.GONE);
                    todoListView.setAdapter(new ActivityAdapter(todo.this, displayList));
                } else {
                    showCompletionMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Activity fetch failed: " + error.getMessage());
            }
        });
    }

    private void showCompletionMessage() {
        assignedDateTextView.setText("\uD83C\uDF1F All Activities Completed!");
        footerMessage.setVisibility(View.VISIBLE);
        todoListView.setAdapter(null);
    }

    private void handleActivityClick(String activityName) {
        for (String key : activityUrls.keySet()) {
            if (activityName.contains(key)) {
                String url = activityUrls.get(key);
                openWebLink(url, key);  // pass actual activity name
                return;
            }
        }
        Toast.makeText(this, "This activity is offline or no link found.", Toast.LENGTH_SHORT).show();
    }

    private void openWebLink(String url, String activityName) {
        Intent intent = new Intent(todo.this, GameWebView.class);
        intent.putExtra("url", url);
        intent.putExtra("child_name", childName); // ✅ Actual child name
        intent.putExtra("activity_name", activityName); // ✅ Optional tracking
        startActivity(intent);
    }

    // Helper class
    private static class ActivityItem {
        public boolean isHeader;
        public String text;

        public ActivityItem(boolean isHeader, String text) {
            this.isHeader = isHeader;
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    // Custom Adapter
    private static class ActivityAdapter extends ArrayAdapter<ActivityItem> {

        private final Context context;

        public ActivityAdapter(Context context, List<ActivityItem> items) {
            super(context, android.R.layout.simple_list_item_1, items);
            this.context = context;
        }

        @Override
        public boolean isEnabled(int position) {
            return !getItem(position).isHeader;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ActivityItem item = getItem(position);
            TextView view = new TextView(context);

            view.setPadding(32, 16, 32, 16);
            view.setTextSize(item.isHeader ? 18 : 16);
            view.setText(item.text);
            view.setTypeface(null, item.isHeader ? Typeface.BOLD : Typeface.NORMAL);
            view.setBackgroundColor(item.isHeader ? Color.parseColor("#E0F7FA") : Color.TRANSPARENT);

            return view;
        }
    }
}
