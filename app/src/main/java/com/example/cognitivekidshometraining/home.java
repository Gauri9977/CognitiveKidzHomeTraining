package com.example.cognitivekidshometraining;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class home extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout todolayout= findViewById(R.id.todo_linear_layout);
        LinearLayout activity_linear_layout=findViewById(R.id.activity_linear_layout);
        LinearLayout academic_linear_layout=findViewById(R.id.academic_linear_layout);
        LinearLayout consultation=findViewById(R.id.consultation_linear_layout);
        LinearLayout rewards=findViewById(R.id.reward_linear_layout);
        LinearLayout report=findViewById(R.id.weeklyreport_linear_layout);
        LinearLayout chatbot=findViewById(R.id.chatbot_linear_layout);
        LinearLayout myactivity=findViewById(R.id.myactivity_home);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Home");
        toolbar_left_image.setVisibility(View.GONE);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.home_screen_drawer);
        toggle = new ActionBarDrawerToggle(home.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();

        //daily agenda
        todolayout.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, todo.class);
            startActivity(intent);
        });
        //activity
        activity_linear_layout.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, academic_activity.class);
            startActivity(intent);
        });
        //academic activity
        academic_linear_layout.setOnClickListener(v -> {
            //Toast.makeText(home.this, "Academic Activities will be available soon. Stay tuned!", Toast.LENGTH_SHORT).show();

            // Uncomment below lines once feature is ready
            Intent intent = new Intent(home.this, child_activity.class);
            startActivity(intent);
        });
        //consultation
        consultation.setOnClickListener(v ->{
            Intent intent=new Intent(home.this, consultation.class);
            startActivity(intent);
        });
        //rewards
        rewards.setOnClickListener(v ->{
            Intent intent=new Intent(home.this, rewards.class);
            startActivity(intent);
        });
        //report
        report.setOnClickListener(v -> {
            Intent intent=new Intent(home.this,ReportChildDashboard.class);
            startActivity(intent);
        });
        //chatbot
        chatbot.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, ChatbotActivity.class);
            startActivity(intent);
        });

        //myactivity
        myactivity.setOnClickListener(v ->{
            Intent intent=new Intent(home.this, myActivity.class);
            startActivity(intent);
        });
    }
}