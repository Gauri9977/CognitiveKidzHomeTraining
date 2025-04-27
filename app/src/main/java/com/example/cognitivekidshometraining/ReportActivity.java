package com.example.cognitivekidshometraining;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    RecyclerView reportRecyclerView;
    ReportAdapter reportAdapter;
    List<ReportWeeklyReport> reportList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_main); // Use the correct layout here

        int index = getIntent().getIntExtra("report_index", -1);
        if (index == -1) {
            // Show a message or close the activity if index is invalid
            finish();
            return;
        }

        // Get the selected report from the StaticDatabase using the index
        List<ReportWeeklyReport> allReports = ReportStaticDatabase.getReports();
        if (index >= 0 && index < allReports.size()) {
            ReportWeeklyReport report = allReports.get(index);

            // Add the report to a list (or you can directly use the report fields)
            reportList = new ArrayList<>();
            reportList.add(report);

            // Set up RecyclerView and Adapter
            reportRecyclerView = findViewById(R.id.reportRecyclerView); // Use correct ID
            reportAdapter = new ReportAdapter(reportList);  // Using the existing ReportAdapter
            reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reportRecyclerView.setAdapter(reportAdapter);
            Log.d("ReportActivity", "onCreate: Report Activity started");
            Log.d("ReportActivity", "Fetching reports from StaticDatabase...");
        } else {
            // Handle the case where the index is invalid or out of bounds
            finish(); // Optionally, show a message if needed
        }
    }
}
