package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportChildDashboard extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;
    RecyclerView weekRecyclerView;
    ReportWeekAdapter weekAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("\uD83D\uDCC8 Weekly Report");
        toolbar_left_image.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();
        DrawerLayout drawer = findViewById(R.id.weekly_report_drawer);
        toggle = new ActionBarDrawerToggle(ReportChildDashboard.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

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
