package com.example.cognitivekidshometraining;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.database.*;
import java.util.*;

public class dr_consult extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView toolbarLeftImage;
    private EditText etAppointmentDate, etAppointmentTime, etDoctorNote;
    private Spinner spinnerChildName, spinnerChildDisorder, spinnerTherapist;
    private Button btnSendRequest;

    private DatabaseReference usersRef, consultationRef;

    private final List<String> childList = new ArrayList<>();
    private final HashMap<String, String> disorderMap = new HashMap<>();

    private final String[] therapists = {"Nitin Chavan", "Therapist 1", "Therapist 2", "Therapist 3", "Therapist 4"};
    private final String[] meetingLinks = {
            "https://meet.google.com/wam-vhkr-vyk", "https://meet.google.com/cnd-vydn-btm",
            "https://meet.google.com/ote-ccof-cdb", "https://meet.google.com/znj-rmvg-xha",
            "https://meet.google.com/jaa-voij-puu", "https://meet.google.com/qvi-vaqg-pia",
            "https://meet.google.com/ipo-ugae-ftv", "https://meet.google.com/zhi-jfyb-kdn",
            "https://meet.google.com/eix-feiu-gmt", "https://meet.google.com/urh-omnd-qye"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_consult);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_right_text);
        toolbarLeftImage = toolbar.findViewById(R.id.toolbar_left_image);

        toolbarTitle.setText("Consultation");
        toolbarLeftImage.setVisibility(View.GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();

        DrawerLayout drawerLayout = findViewById(R.id.dr_consult_page);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        consultationRef = FirebaseDatabase.getInstance().getReference("therapy_and_consultation");

        spinnerChildName = findViewById(R.id.spinner_child_name);
        spinnerChildDisorder = findViewById(R.id.spinner_child_disorder);
        etAppointmentDate = findViewById(R.id.et_appointment_date);
        etAppointmentTime = findViewById(R.id.et_appointment_time);
        spinnerTherapist = findViewById(R.id.spinner_therapist);
        etDoctorNote = findViewById(R.id.et_doctor_note);
        btnSendRequest = findViewById(R.id.btn_send_request);

        setupTherapistSpinner();
        setupDateTimePickers();
        loadChildrenFromFirebase();

        btnSendRequest.setOnClickListener(v -> sendConsultationRequest());
    }

    private void setupTherapistSpinner() {
        ArrayAdapter<String> therapistAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, therapists);
        therapistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTherapist.setAdapter(therapistAdapter);
    }

    private void setupDateTimePickers() {
        etAppointmentDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(dr_consult.this, (view, year, month, dayOfMonth) ->
                    etAppointmentDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        etAppointmentTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(dr_consult.this, (view, hourOfDay, minute) ->
                    etAppointmentTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });
    }

    private void loadChildrenFromFirebase() {
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                childList.clear();
                disorderMap.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot childData = userSnapshot.child("ChildData");
                    if (childData.exists()) {
                        String name = childData.child("child_name").getValue(String.class);
                        String disorder = childData.child("disability_type").getValue(String.class);
                        if (name != null && !name.trim().isEmpty()) {
                            childList.add(name);
                            disorderMap.put(name, disorder != null ? disorder : "Not specified");
                        }
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(dr_consult.this, android.R.layout.simple_spinner_item, childList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerChildName.setEnabled(true);
                spinnerChildName.setAdapter(adapter);

                spinnerChildName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String child = childList.get(position);
                        String disorder = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            disorder = disorderMap.getOrDefault(child, "Unknown");
                        }
                        ArrayAdapter<String> disorderAdapter = new ArrayAdapter<>(dr_consult.this, android.R.layout.simple_spinner_item, new String[]{disorder});
                        spinnerChildDisorder.setEnabled(true);
                        spinnerChildDisorder.setAdapter(disorderAdapter);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
        });
    }

    private void sendConsultationRequest() {
        String child = spinnerChildName.getSelectedItem().toString();
        String disorder = spinnerChildDisorder.getSelectedItem().toString();
        String therapist = spinnerTherapist.getSelectedItem().toString();
        String date = etAppointmentDate.getText().toString();
        String time = etAppointmentTime.getText().toString();
        String note = etDoctorNote.getText().toString();
        String meetingLink = meetingLinks[new Random().nextInt(meetingLinks.length)];

        if (child.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("therapist_name", therapist);
        data.put("assignedDate", date);
        data.put("assignedtime", time);
        data.put("disorder", disorder);
        data.put("doctorNote", note);
        data.put("meetingLink", meetingLink);

        String key = date.replace("/", "_") + "_" + time.replace(":", "-");
        consultationRef.child(child).child(key).setValue(data)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Consultation scheduled", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error scheduling consultation", Toast.LENGTH_SHORT).show());
    }
}
