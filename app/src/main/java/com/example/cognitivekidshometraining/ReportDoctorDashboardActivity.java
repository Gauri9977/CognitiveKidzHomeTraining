package com.example.cognitivekidshometraining;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDoctorDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReportDoctorWeekAdapter doctorWeekAdapter;
    private List<ReportWeeklyReport> reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity_doctor_dashboard); // This should be the layout that contains recyclerView

        // Bind RecyclerView from XML layout
        recyclerView = findViewById(R.id.recyclerView); // Correctly bind the RecyclerView

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the reports from StaticDatabase
        reports = ReportStaticDatabase.getReports();

        // Create a Map of week dates and corresponding report indexes
        Map<String, Integer> weekMap = generateWeekMap(reports);

        // Initialize the adapter with Context, weekMap, and reports list
        doctorWeekAdapter = new ReportDoctorWeekAdapter(this, weekMap, reports);
        recyclerView.setAdapter(doctorWeekAdapter);
    }

    // This function creates a Map<String, Integer> where each week date points to its respective report index
    private Map<String, Integer> generateWeekMap(List<ReportWeeklyReport> reports) {
        // Implement logic to generate the week map based on your requirement
        Map<String, Integer> weekMap = new HashMap<>();
        for (int i = 0; i < reports.size(); i++) {
            String date = reports.get(i).getDate();
            weekMap.put(date, i);  // Assuming date is unique and can be used as a key
        }
        return weekMap;
    }
}
