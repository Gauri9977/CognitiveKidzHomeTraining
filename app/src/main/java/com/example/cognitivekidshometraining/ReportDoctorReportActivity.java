package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportDoctorReportActivity extends AppCompatActivity {

    TextView reportDateText, childName, age, disorder, doctorName,
            therapiesCompleted, therapiesMissed, activitiesCompleted,
            activitiesMissed, testsResults, clinicalObservation;
    EditText editDoctorNote;
    Button publishNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity_doctor_report);

        int index = getIntent().getIntExtra("report_index", -1);
        String reportDate = getIntent().getStringExtra("report_date");

        if (index == -1 || reportDate == null) {
            finish();
            return;
        }

        // View binding
        reportDateText = findViewById(R.id.dateText);
        childName = findViewById(R.id.childName);
        age = findViewById(R.id.age);
        disorder = findViewById(R.id.disorder);
        doctorName = findViewById(R.id.doctorName);
        therapiesCompleted = findViewById(R.id.therapiesCompleted);
        therapiesMissed = findViewById(R.id.therapiesMissed);
        activitiesCompleted = findViewById(R.id.activitiesCompleted);
        activitiesMissed = findViewById(R.id.activitiesMissed);
        testsResults = findViewById(R.id.testsResults);
        clinicalObservation = findViewById(R.id.clinicalObservation);
        editDoctorNote = findViewById(R.id.editDoctorNote);
        publishNoteButton = findViewById(R.id.publishNoteButton);

        // Get report
        List<ReportWeeklyReport> allReports = ReportStaticDatabase.getReports();
        ReportWeeklyReport report = allReports.get(index);

        reportDateText.setText("Date: " + report.date);
        childName.setText("Name: " + report.childName);
        age.setText("Age: " + report.age);
        disorder.setText("Disorder: " + report.disorder);
        doctorName.setText("Doctor: " + report.doctorName);
        therapiesCompleted.setText("Therapies Completed: " + report.therapiesCompleted);
        therapiesMissed.setText("Therapies Missed: " + report.therapiesMissed);
        activitiesCompleted.setText("Activities Completed: " + report.activitiesCompleted);
        activitiesMissed.setText("Activities Missed: " + report.activitiesMissed);
        testsResults.setText(report.testsAndResults);
        clinicalObservation.setText(report.clinicalObservation);

        // Determine if today is the report date
        String today = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        boolean isToday = today.equals(report.date);

        if (isToday) {
            editDoctorNote.setVisibility(View.VISIBLE);
            publishNoteButton.setVisibility(View.VISIBLE);
            editDoctorNote.setText(report.doctorNote);
        } else {
            editDoctorNote.setVisibility(View.GONE);
            publishNoteButton.setVisibility(View.GONE);
        }

        publishNoteButton.setOnClickListener(v -> {
            String updatedNote = editDoctorNote.getText().toString().trim();
            if (!updatedNote.isEmpty()) {
                report.doctorNote = updatedNote;
                Toast.makeText(this, "Doctor's note published", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
