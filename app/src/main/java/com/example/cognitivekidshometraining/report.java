package com.example.cognitivekidshometraining;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class report extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Weekly Report");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.report_drawer);
        toggle = new ActionBarDrawerToggle(report.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();

        Button reportButton1 = findViewById(R.id.reportButton1);
        Button reportButton2 = findViewById(R.id.reportButton2);

        // Set click listeners for each report button
        reportButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReport("Week 1");
            }
        });

        reportButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReport("Week 2");
            }
        });
    }

    // Method to open the detailed report in Report1 class
    private void openReport(String week) {
        Intent intent = new Intent(report.this, report1.class); // Navigate to Report1 activity
        intent.putExtra("REPORT_WEEK", week);
        startActivity(intent);
    }
}