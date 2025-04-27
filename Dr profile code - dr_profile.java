package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

public class dr_profile extends AppCompatActivity {

    TextView tvDrName, tvDrMobile, tvDrEmail, tvDrSpeciality, tvDrDegree,
            tvDrExperience, tvDrHospital, tvDrCity, tvDrAge, tvDrGender, tvDrDOB,
            tvDrLicense, tvDrTimings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize TextViews
        tvDrName = findViewById(R.id.tv_dr_name);
        tvDrMobile = findViewById(R.id.tv_dr_mobile);
        tvDrEmail = findViewById(R.id.tv_dr_email);
        tvDrSpeciality = findViewById(R.id.tv_dr_speciality);
        tvDrDegree = findViewById(R.id.tv_dr_degree);
        tvDrExperience = findViewById(R.id.tv_dr_experience);
        tvDrHospital = findViewById(R.id.tv_dr_hospital);
        tvDrCity = findViewById(R.id.tv_dr_city);
        tvDrAge = findViewById(R.id.tv_dr_age);
        tvDrGender = findViewById(R.id.tv_dr_gender);
        tvDrDOB = findViewById(R.id.tv_dr_dob);
        tvDrLicense = findViewById(R.id.tv_dr_license);
        tvDrTimings = findViewById(R.id.tv_dr_timings);

        // Set hardcoded doctor data
        tvDrName.setText("Dr. Nitin Chavan");
        tvDrMobile.setText("+91-9876543210");
        tvDrEmail.setText("nchavan00@gmail.com");
        tvDrSpeciality.setText("Autism and Child Development");
        tvDrDegree.setText("MBBS, MD-Pediatric Neurology");
        tvDrExperience.setText("12+ years");
        tvDrHospital.setText("Sense Rehab, Pandharpur");
        tvDrCity.setText("Pandharpur");
        tvDrAge.setText("47 years");
        tvDrGender.setText("Male");
        tvDrDOB.setText("25/11/1978");
        tvDrLicense.setText("Active (Maharashtra Medical Council)");
        tvDrTimings.setText("10:00 AM - 06:00 PM");
    }
}
