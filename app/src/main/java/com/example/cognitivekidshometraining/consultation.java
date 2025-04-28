package com.example.cognitivekidshometraining;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class consultation extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;

    TextView tvChildInfo;
    RadioGroup radioGroup;
    RadioButton rbAccept, rbReschedule;
    CheckBox cbDay, cbWeek;
    EditText etReason;
    Button btnSubmit;

    SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "ConsultPrefs";
    public static final String STATUS_KEY = "appointment_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        // Toolbar setup
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Consultation");
        toolbar_left_image.setVisibility(View.GONE);

        DrawerLayout drawer = findViewById(R.id.consult_drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Fragment drawer
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        // Bind views
        tvChildInfo = findViewById(R.id.tv_child_info);
        radioGroup = findViewById(R.id.radio_group);
        rbAccept = findViewById(R.id.rb_accept);
        rbReschedule = findViewById(R.id.rb_reschedule);
        cbDay = findViewById(R.id.cb_day);
        cbWeek = findViewById(R.id.cb_week);
        etReason = findViewById(R.id.et_reason);
        btnSubmit = findViewById(R.id.btn_submit_response);

        // Set child info
        SharedPreferences sp = getSharedPreferences("AppointmentData", MODE_PRIVATE);
        String name = sp.getString("childName", "Unknown");
        String disorder = sp.getString("childDisorder", "N/A");
        String date = sp.getString("date", "N/A");
        String time = sp.getString("time", "N/A");
        tvChildInfo.setText("Appointment for " + name + "\nDisorder: " + disorder + "\nDate: " + date + "\nTime: " + time);

        // Radio button logic
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_reschedule) {
                cbDay.setVisibility(View.VISIBLE);
                cbWeek.setVisibility(View.VISIBLE);
                etReason.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.rb_accept) {
                cbDay.setVisibility(View.GONE);
                cbWeek.setVisibility(View.GONE);
                etReason.setVisibility(View.GONE);
            }
        });

        // Submit button
        btnSubmit.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedId == R.id.rb_accept) {
                // Save to SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(STATUS_KEY, "Confirmed");
                editor.apply();
                Toast.makeText(this, "Appointment Confirmed", Toast.LENGTH_SHORT).show();
            } else if (selectedId == R.id.rb_reschedule) {
                if (!cbDay.isChecked() && !cbWeek.isChecked()) {
                    Toast.makeText(this, "Please choose a reschedule option", Toast.LENGTH_SHORT).show();
                    return;
                }

                String reason = etReason.getText().toString().trim();
                if (reason.isEmpty()) {
                    Toast.makeText(this, "Please enter reason for rescheduling", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Rescheduling with reason: " + reason, Toast.LENGTH_LONG).show();
            }
        });
    }
}
