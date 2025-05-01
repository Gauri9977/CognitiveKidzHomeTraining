package com.example.cognitivekidshometraining;

import android.app.DatePickerDialog;
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

import java.util.Calendar;
import java.util.HashMap;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Button;


public class dr_consult extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView toolbarLeftImage;
    private ActionBarDrawerToggle toggle;

    private EditText etAppointmentDate, etAppointmentTime;
    private Spinner spinnerChildName, spinnerChildDisorder;

    // Map for storing child name and disorder mapping
    private final HashMap<String, String> childDisorderMap = new HashMap<String, String>() {{
        put("Vihan Jagtap", "Autism Spectrum Disorder");
        put("Shourya Avate", "IQ/DQ");
        put("Sanchi Kasbe", "ASD");
        put("Janvi Satpute", "ADHD");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_consult);

        // Toolbar setup
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_right_text);
        toolbarLeftImage = toolbar.findViewById(R.id.toolbar_left_image);

        toolbarTitle.setText("Consultation");
        toolbarLeftImage.setVisibility(View.GONE);

        // Drawer setup
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();

        DrawerLayout drawerLayout = findViewById(R.id.dr_consult_page);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        //the response from the child's side
        TextView tvResponseStatus = findViewById(R.id.tv_response_status);
        Button btnActionResponse = findViewById(R.id.btn_action_response);

        // Get SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String response = prefs.getString("appointment_response", "none"); // accepted or rescheduled
        String reason = prefs.getString("reschedule_reason", "");

        // Update UI based on response
        if (response.equals("accepted")) {
            tvResponseStatus.setText("Appointment Accepted by Child Side.");
            btnActionResponse.setVisibility(View.VISIBLE);
            btnActionResponse.setText("Confirm");
        } else if (response.equals("rescheduled")) {
            tvResponseStatus.setText("Appointment Rescheduled by Child Side.\nReason: " + reason);
            btnActionResponse.setVisibility(View.VISIBLE);
            btnActionResponse.setText("Reschedule Appointment");
        } else {
            tvResponseStatus.setText("Appointment response not received yet.");
            btnActionResponse.setVisibility(View.GONE);
        }


        // Initialize views
        spinnerChildName = findViewById(R.id.spinner_child_name);
        spinnerChildDisorder = findViewById(R.id.spinner_child_disorder);
        etAppointmentDate = findViewById(R.id.et_appointment_date);
        etAppointmentTime = findViewById(R.id.et_appointment_time);

        // Setup child name spinner
        ArrayAdapter<String> childAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Select Child", "Vihan Jagtap", "Shourya Avate", "Sanchi Kasbe", "Janvi Satpute"}
        );
        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChildName.setAdapter(childAdapter);

        // Disable disorder spinner initially
        spinnerChildDisorder.setEnabled(false);

        spinnerChildName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedChild = parent.getItemAtPosition(position).toString();

                if (childDisorderMap.containsKey(selectedChild)) {
                    String disorder = childDisorderMap.get(selectedChild);
                    ArrayAdapter<String> disorderAdapter = new ArrayAdapter<>(
                            dr_consult.this,
                            android.R.layout.simple_spinner_item,
                            new String[]{disorder}
                    );
                    disorderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerChildDisorder.setAdapter(disorderAdapter);
                } else {
                    spinnerChildDisorder.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: handle if needed
            }
        });

        // Setup calendar-style DatePicker
        etAppointmentDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    dr_consult.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etAppointmentDate.setText(formattedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Setup clock-style TimePicker
        etAppointmentTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    dr_consult.this,
                    (view, selectedHour, selectedMinute) -> {
                        String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        etAppointmentTime.setText(formattedTime);
                    },
                    hour, minute, true // Set 'false' if you want 12-hour with AM/PM
            );
            timePickerDialog.show();
        });
    }
}
