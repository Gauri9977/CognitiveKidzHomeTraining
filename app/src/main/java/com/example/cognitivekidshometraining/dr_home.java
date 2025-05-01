package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class dr_home extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_home);

        LinearLayout todolayout= findViewById(R.id.todo_linear_layout1);
        LinearLayout academic_linear_layout=findViewById(R.id.academic_linear_layout1);
        LinearLayout activity_linear_layout=findViewById(R.id.activity_linear_layout1);
        LinearLayout consultation=findViewById(R.id.consultation_linear_layout1);
        LinearLayout child_details=findViewById(R.id.reward_linear_layout1);
        LinearLayout report=findViewById(R.id.weeklyreport_linear_layout1);
        LinearLayout dr_profile= findViewById(R.id.myactivity_home);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Doctor's Home");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();

        DrawerLayout drawer = findViewById(R.id.dr_home_screen_drawer);
        toggle = new ActionBarDrawerToggle(dr_home.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();

        consultation.setOnClickListener(v -> {
            Intent intent = new Intent(dr_home.this, dr_consult.class);
            startActivity(intent);

        });

        activity_linear_layout.setOnClickListener(v -> {
            Intent intent = new Intent(dr_home.this, dr_activities.class);
            startActivity(intent);
        });

        dr_profile.setOnClickListener(v -> {
            Intent intent= new Intent(dr_home.this, dr_profile.class);
            startActivity(intent);
        });

        todolayout.setOnClickListener(v -> {
            Intent intent= new Intent(dr_home.this, dr_todo.class);
            startActivity(intent);
        });

        child_details.setOnClickListener(v -> {
            Intent intent= new Intent(dr_home.this, dr_child_list.class);
            startActivity(intent);
        });

        academic_linear_layout.setOnClickListener(v -> {
            Toast.makeText(dr_home.this, "Academic Activities will be available soon. Stay tuned!", Toast.LENGTH_SHORT).show();
        });

        //Doctorside report
        report.setOnClickListener(v -> {
            Intent intent=new Intent(dr_home.this,ReportDoctorDashboardActivity.class);
            startActivity(intent);
        });
    }
}
