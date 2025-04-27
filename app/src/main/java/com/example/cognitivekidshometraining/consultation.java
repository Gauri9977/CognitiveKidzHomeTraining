package com.example.cognitivekidshometraining;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
    RadioButton rbAccept, rbReschedule, rbPostpone;
    EditText etReason;
    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Consultation");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.consult_drawer);
        toggle = new ActionBarDrawerToggle(consultation.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();


        tvChildInfo = findViewById(R.id.tv_child_info);
        radioGroup = findViewById(R.id.radio_group);
        etReason = findViewById(R.id.et_reason);
        btnSubmit = findViewById(R.id.btn_submit_response);

        // Fetch stored data
        SharedPreferences sharedPreferences = getSharedPreferences("AppointmentData", MODE_PRIVATE);
        String name = sharedPreferences.getString("childName", "Unknown");
        String disorder = sharedPreferences.getString("childDisorder", "N/A");
        String date = sharedPreferences.getString("date", "N/A");
        String time = sharedPreferences.getString("time", "N/A");

        tvChildInfo.setText("Appointment for " + name + "\nDisorder: " + disorder + "\nDate: " + date + "\nTime: " + time);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            etReason.setVisibility((checkedId == R.id.rb_reschedule || checkedId == R.id.rb_postpone)
                    ? View.VISIBLE : View.GONE);
        });

        btnSubmit.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                return;
            }

            String response = ((RadioButton) findViewById(selectedId)).getText().toString();
            String reason = etReason.getText().toString();

            if ((selectedId == R.id.rb_reschedule || selectedId == R.id.rb_postpone) && reason.isEmpty()) {
                Toast.makeText(this, "Please enter a reason", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Response: " + response + "\n" + (reason.isEmpty() ? "" : "Reason: " + reason), Toast.LENGTH_LONG).show();
        });
    }
}