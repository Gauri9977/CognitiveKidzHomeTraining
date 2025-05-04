package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class dr_activities extends AppCompatActivity {

    private LinearLayout tableContainer;
    private DatabaseReference dbRef;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_activities);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_title.setText("⏰ Consultation Schedule");

        DrawerLayout drawer = findViewById(R.id.dr_activity_page);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();

        tableContainer = findViewById(R.id.table_container);
        dbRef = FirebaseDatabase.getInstance().getReference("therapy_and_consultation");

        loadConsultationSchedule();
    }

    private void loadConsultationSchedule() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(dr_activities.this, "No consultation data found.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Map<String, Map<String, String>>> dateWiseMap = new TreeMap<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String childName = childSnapshot.getKey();
                    for (DataSnapshot entry : childSnapshot.getChildren()) {
                        String dateTimeKey = entry.getKey();
                        Map<String, String> entryData = (Map<String, String>) entry.getValue();

                        String date = entryData.get("assignedDate");
                        if (!dateWiseMap.containsKey(date)) {
                            dateWiseMap.put(date, new HashMap<>());
                        }
                        dateWiseMap.get(date).put(childName + "/" + dateTimeKey, entryData);
                    }
                }

                renderTables(dateWiseMap);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(dr_activities.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderTables(Map<String, Map<String, Map<String, String>>> dateWiseMap) {
        tableContainer.removeAllViews();

        for (String date : dateWiseMap.keySet()) {
            TextView dateHeader = new TextView(this);
            dateHeader.setText("📅 " + date);
            dateHeader.setTextSize(20);
            dateHeader.setTypeface(null, Typeface.BOLD);
            dateHeader.setTextColor(getResources().getColor(R.color.textcolour));
            dateHeader.setPadding(0, 24, 0, 16);
            tableContainer.addView(dateHeader);

            TableLayout table = new TableLayout(this);
            table.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            table.setStretchAllColumns(true);
            table.setBackgroundResource(R.drawable.table_border);

            // Header row
            TableRow header = new TableRow(this);
            header.setBackgroundColor(Color.parseColor("#003366")); // dark blue

            String[] headers = {"Time", "Child", "Therapist", "Status", "Reason", "Meeting Link"};
            for (String h : headers) {
                TextView tv = new TextView(this);
                tv.setText(h);
                tv.setTextSize(16);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(16, 12, 16, 12);
                tv.setTextColor(Color.BLUE);
                tv.setBackgroundResource(R.drawable.table_cell_border);
                header.addView(tv);
            }
            table.addView(header);

            // Data rows
            int rowIndex = 0;
            for (String key : dateWiseMap.get(date).keySet()) {
                Map<String, String> entry = dateWiseMap.get(date).get(key);

                TableRow row = new TableRow(this);
                int rowColor = (rowIndex++ % 2 == 0)
                        ? getResources().getColor(R.color.row_even)
                        : getResources().getColor(R.color.row_odd);
                row.setBackgroundColor(rowColor);

                String[] fields = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fields = new String[]{
                            entry.get("assignedtime"),
                            key.split("/")[0],
                            entry.get("therapist_name"),
                            entry.getOrDefault("status", "-"),
                            entry.getOrDefault("reschedule_reason", "-"),
                            "Open"
                    };
                }

                for (int i = 0; i < fields.length; i++) {
                    TextView tv = new TextView(this);
                    tv.setText(fields[i]);
                    tv.setTextSize(14);
                    tv.setTextColor(Color.BLACK); // black for values
                    tv.setPadding(16, 12, 16, 12);
                    tv.setBackgroundResource(R.drawable.table_cell_border);

                    if (i == 5) {
                        // Get the meeting link from entry (make sure it's valid)
                        final String originalMeetingLink = entry.get("meetingLink");

                        // Ensure the meeting link is not null and not empty
                        if (originalMeetingLink != null && !originalMeetingLink.isEmpty()) {
                            // Prepare the link to open by adding "https://" if not already present
                            final String meetingLink;
                            if (!originalMeetingLink.startsWith("http://") && !originalMeetingLink.startsWith("https://")) {
                                meetingLink = "https://" + originalMeetingLink; // Modify the link
                            } else {
                                meetingLink = originalMeetingLink; // Keep the link as it is
                            }

                            tv.setTextColor(Color.BLUE);  // Set text color for meeting link
                            tv.setTypeface(null, Typeface.BOLD);  // Make the text bold

                            // Set the click listener for the meeting link
                            tv.setOnClickListener(view -> {
                                // Create an Intent to open the URL in a browser
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meetingLink));
                                startActivity(intent);
                            });
                        } else {
                            // If no meeting link is available, display a message
                            tv.setTextColor(Color.GRAY);
                            tv.setText("No meeting link available");
                        }
                    }


                    row.addView(tv);
                }

                table.addView(row);
            }

            tableContainer.addView(table);
        }
    }
}
