// Java logic for child-side consultation response
// Implements: Firebase query, response to consultation, and UI updates

package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class consultation extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView tvMeetingDetails;
    private RadioGroup radioGroup;
    private RadioButton rbAccept, rbReschedule;
    private CheckBox cbDay, cbWeek;
    private EditText etReason;
    private Button btnSubmit;
    private DrawerLayout drawer;

    private DatabaseReference database;
    private FirebaseAuth auth;
    private String childName;
    private String latestDateTimeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        // Setup UI
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_right_text);
        toolbarTitle.setText("Consultation");
        drawer = findViewById(R.id.consult_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        // Init views
        tvMeetingDetails = findViewById(R.id.tv_child_info);
        radioGroup = findViewById(R.id.radio_group);
        rbAccept = findViewById(R.id.rb_accept);
        rbReschedule = findViewById(R.id.rb_reschedule);
        cbDay = findViewById(R.id.cb_day);
        cbWeek = findViewById(R.id.cb_week);
        etReason = findViewById(R.id.et_reason);
        btnSubmit = findViewById(R.id.btn_submit_response);

        // Firebase init
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        fetchPendingConsultation();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            boolean reschedule = checkedId == R.id.rb_reschedule;
            cbDay.setVisibility(reschedule ? View.VISIBLE : View.GONE);
            cbWeek.setVisibility(reschedule ? View.VISIBLE : View.GONE);
            etReason.setVisibility(reschedule ? View.VISIBLE : View.GONE);
        });

        btnSubmit.setOnClickListener(v -> handleSubmitResponse());
    }

    private void fetchPendingConsultation() {
        String uid = auth.getCurrentUser().getUid();
        database.child("Users").child(uid).child("ChildData").child("child_name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childName = snapshot.getValue(String.class);
                if (childName != null) {
                    DatabaseReference ref = database.child("therapy_and_consultation").child(childName);
                    ref.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    latestDateTimeKey = ds.getKey();
                                    String therapist = ds.child("therapist_name").getValue(String.class);
                                    String date = ds.child("assignedDate").getValue(String.class);
                                    String time = ds.child("assignedtime").getValue(String.class);
                                    String link = ds.child("meetingLink").getValue(String.class);

                                    String info = "\uD83D\uDCC5 Date: " + date + "\n" +
                                            "\uD83D\uDD52 Time: " + time + "\n" +
                                            "👨‍⚕️ Therapist: " + therapist + "\n" +
                                            "🔗 Link: " + link;
                                    tvMeetingDetails.setText(info);
                                }
                            } else {
                                tvMeetingDetails.setText("No upcoming consultations found.");
                                btnSubmit.setEnabled(false);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            tvMeetingDetails.setText("Error fetching data.");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(consultation.this, "User fetch failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSubmitResponse() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1 || childName == null || latestDateTimeKey == null) {
            Toast.makeText(this, "Incomplete data or selection", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        if (selectedId == R.id.rb_accept) {
            updates.put("status", "accepted");
            updates.put("reschedule_reason", "");
            Toast.makeText(this, "Appointment accepted.", Toast.LENGTH_SHORT).show();
        } else {
            String reason = etReason.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(this, "Enter a reason for rescheduling.", Toast.LENGTH_SHORT).show();
                return;
            }
            updates.put("status", "rescheduled");
            updates.put("reschedule_reason", reason);
            Toast.makeText(this, "Rescheduling appointment.", Toast.LENGTH_SHORT).show();
        }

        database.child("therapy_and_consultation").child(childName).child(latestDateTimeKey)
                .updateChildren(updates)
                .addOnSuccessListener(unused -> finish())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to submit.", Toast.LENGTH_SHORT).show());
    }
}
