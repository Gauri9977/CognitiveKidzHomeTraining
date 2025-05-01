package com.example.cognitivekidshometraining;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class dr_activities extends AppCompatActivity {

    //Spinner spinner1, spinner2;
    Button assignButton;
    EditText etAppointmentTime;
    Spinner childNameSpinner;
    Spinner therapistSpinner;
    Spinner activityTypeSpinner;
    EditText notesEditText;
    TextView assignedTherapiesTextView;
    TextView assignedListTextView;
    TextView appointmentDetailsTextView; // To display appointment info
    String selectedTime;
    String selectedChild;
    String selectedTherapist;
    String selectedActivityType;
    String notes;
    List<String> assignedTherapiesList = new ArrayList<>();
    //String[] mainActivities = {"Numbers", "Words", "Colours", "Shapes", "Sequence"};
    HashMap<String, String[]> subActivityMap;
    String[] activityTypes = {"Speech Therapy", "Occupational Therapy", "Physical Therapy", "Behavioral Therapy"};
    String[] therapistsList = {"Dr. Nitin Chavan", "Therapist A", "Therapist B", "Therapist C", "Therapist D"};
    String[] childNamesList = {"Sanchi Kasbe", "Shourya Avate", "Vihan Jagtap", "janvi Satpute"};
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_activities);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Assign Activities");
        toolbar_left_image.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();
        DrawerLayout drawer = findViewById(R.id.dr_activity_page);
        toggle = new ActionBarDrawerToggle(dr_activities.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(v -> {});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();


        etAppointmentTime = findViewById(R.id.etAppointmentTime);
        childNameSpinner = findViewById(R.id.childNameSpinner1);
        therapistSpinner = findViewById(R.id.therapistSpinner1);
        activityTypeSpinner = findViewById(R.id.activityTypeSpinner1);
        notesEditText = findViewById(R.id.notesEditText1);
        assignButton = findViewById(R.id.assignButton);
        assignedTherapiesTextView = findViewById(R.id.assignedTherapiesTextView);
        assignedListTextView = findViewById(R.id.assignedListTextView);
        appointmentDetailsTextView = findViewById(R.id.appointmentDetailsTextView); // Initialize

        // Retrieve appointment details from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppointmentDetails", MODE_PRIVATE);
        String appointmentChildName = sharedPreferences.getString("appointment_child_name", "");
        String appointmentTime = sharedPreferences.getString("appointment_time", "");
        String appointmentDoctorName = sharedPreferences.getString("appointment_doctor_name", "");

        if (!appointmentChildName.isEmpty() && !appointmentTime.isEmpty() && !appointmentDoctorName.isEmpty()) {
            String appointmentInfo = String.format("Appointment Details:\nTime: %s\nChild: %s\nDoctor: %s",
                    appointmentTime, appointmentChildName, appointmentDoctorName);
            appointmentDetailsTextView.setText(appointmentInfo);
        } else {
            appointmentDetailsTextView.setText("No appointment scheduled.");
        }

        // Initialize with default values
        selectedTime = "Select Time";
        selectedChild = "";
        selectedTherapist = "";
        selectedActivityType = "";
        notes = "";
        etAppointmentTime.setText(selectedTime);

        // Time picker
        etAppointmentTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        etAppointmentTime.setText(selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            ).show();
        });

        // Child Name Dropdown
        ArrayAdapter<String> childAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, childNamesList);
        childNameSpinner.setAdapter(childAdapter);

        childNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedChild = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedChild = "";
            }
        });

        // Therapist Dropdown
        ArrayAdapter<String> therapistAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, therapistsList);
        therapistSpinner.setAdapter(therapistAdapter);

        therapistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTherapist = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTherapist = "";
            }
        });

        // Activity Type Dropdown
        ArrayAdapter<String> activityTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activityTypes);
        activityTypeSpinner.setAdapter(activityTypeAdapter);

        activityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedActivityType = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedActivityType = "";
            }
        });

        // Assign Button Click Listener
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes = notesEditText.getText().toString().trim();
                if (!selectedTime.equals("Select Time") && !selectedChild.isEmpty() && !selectedTherapist.isEmpty() && !selectedActivityType.isEmpty()) {
                    String assignment = String.format("%-10s   %-15s   %-20s",
                            selectedTime, selectedChild, selectedTherapist);
                    assignedTherapiesList.add(assignment);
                    displayAssignedTherapies();
                    // Optionally clear fields after assignment
                    etAppointmentTime.setText("Select Time");
                    childNameSpinner.setSelection(0);
                    therapistSpinner.setSelection(0);
                    activityTypeSpinner.setSelection(0);
                    notesEditText.setText("");
                } else {
                    Toast.makeText(dr_activities.this, "Please select Time, Child, Therapist, and Activity Type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void displayAssignedTherapies() {
        StringBuilder sb = new StringBuilder();
        if (assignedTherapiesList.isEmpty()) {
            assignedListTextView.setText("No therapies assigned yet.");
        } else {
            sb.append(String.format("%-10s %-15s %-20s\n", "Time", "Child Name", "Therapist"));
            sb.append("---------------------------------------------\n");
            for (String assignment : assignedTherapiesList) {
                sb.append(assignment).append("\n");
            }
            assignedListTextView.setText(sb.toString());
        }
    }
}