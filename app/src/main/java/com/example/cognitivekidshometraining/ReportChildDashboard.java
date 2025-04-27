package com.example.cognitivekidshometraining;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportChildDashboard extends AppCompatActivity {

    RecyclerView weekRecyclerView;
    ReportWeekAdapter weekAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity_main);

        weekRecyclerView = findViewById(R.id.weekRecyclerView);

        List<ReportWeeklyReport> allReports = ReportStaticDatabase.getReports();

        // Use LinkedHashMap to maintain order and remove duplicate weeks
        Map<String, Integer> weekMap = new LinkedHashMap<>();
        int index = 0;
        for (ReportWeeklyReport report : allReports) {
            if (!weekMap.containsKey(report.date)) {
                weekMap.put(report.date, index); // Store index of first appearance
            }
            index++;
        }

        weekAdapter = new ReportWeekAdapter(this, weekMap);
        weekRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weekRecyclerView.setAdapter(weekAdapter);
    }
}
