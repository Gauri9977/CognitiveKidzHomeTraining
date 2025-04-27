package com.example.cognitivekidshometraining;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dr_todo extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;

    Spinner spinnerDisorder, spinnerChildName, spinnerTherapist;

    Spinner spinnerTask1, spinnerTask2, spinnerTask3;
    EditText editTime1, editTime2, editTime3;
    Button btnAssignTodo;

    Map<String, List<String>> childNameMap = new HashMap<>();

    // Updated task categories
    String[] therapySessions = {"Therapy Session 1", "Therapy Session 2"};
    String[] activities = {"Learn Numbers", "Learn Words", "Learn Colours", "Learn Shapes", "Learn Sequence"};
    String[] otherActivities = {"Outdoor Games", "Mind Games", "Puzzles", "Meditation", "Mind Exercises"};

    String[] childNames = {"ABC", "PQR", "XYZ"};
    String[] disorders = {"Autism", "ADHD", "IQ/DQ", "Learning Disorder"};
    String[] therapists = {"Doctor A", "Doctor B", "Doctor C", "Doctor D"};

    CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6, checkbox7, checkbox8, checkbox9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_todo);

        spinnerChildName = findViewById(R.id.spinnerChildName);
        spinnerDisorder = findViewById(R.id.spinnerDisorder);

        // Child Name Spinner data

        // Disorder Spinner data
        String[] disorders = {"Autism", "ADHD", "IQ/DQ", "Learning disability"};

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Assign Activities");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();

        DrawerLayout drawer = findViewById(R.id.dr_todo_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        ArrayAdapter<String> childAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, childNames);
        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChildName.setAdapter(childAdapter);

        // Set adapter for Disorder Spinner
        ArrayAdapter<String> disorderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, disorders);
        disorderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisorder.setAdapter(disorderAdapter);

        // Optional: Add listeners
        spinnerChildName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedChild = parent.getItemAtPosition(position).toString();
                Toast.makeText(dr_todo.this, "Selected Child: " + selectedChild, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDisorder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDisorder = parent.getItemAtPosition(position).toString();
                Toast.makeText(dr_todo.this, "Selected Disorder: " + selectedDisorder, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);
        checkbox3 = findViewById(R.id.checkbox3);
        checkbox4 = findViewById(R.id.checkbox4);
        checkbox5 = findViewById(R.id.checkbox5);
        checkbox6 = findViewById(R.id.checkbox6);
        checkbox8 = findViewById(R.id.checkbox8);
        checkbox9 = findViewById(R.id.checkbox9);
        checkbox7 = findViewById(R.id.checkbox7);
        btnAssignTodo = findViewById(R.id.btnAssignTodo);


        btnAssignTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder schedule = new StringBuilder();

                if (checkbox1.isChecked()) schedule.append("- Activity 1: Learn Numbers\n");
                if (checkbox2.isChecked()) schedule.append("- Activity 2: Learn Words\n");
                if (checkbox3.isChecked()) schedule.append("- Activity 3: Learn Colours\n");
                if (checkbox4.isChecked()) schedule.append("- Activity 4: Learn Shapes\n");
                if (checkbox5.isChecked()) schedule.append("- Activity 5: Learn Sequence\n");
                if (checkbox9.isChecked()) schedule.append("- Mind Exercises\n");
                if (checkbox6.isChecked()) schedule.append("- Mind Games\n");
                if (checkbox7.isChecked()) schedule.append("- OutDoor Games\n");
                if (checkbox8.isChecked()) schedule.append("- Puzzles\n");

                if (schedule.length() == 0) {
                    Toast.makeText(dr_todo.this, "Please select at least one task", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save schedule using SharedPreferences
                SharedPreferences prefs = getSharedPreferences("ChildTodo", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("todoData", schedule.toString());
                editor.apply();

                Toast.makeText(dr_todo.this, "Schedule Assigned Successfully", Toast.LENGTH_SHORT).show();


            }
        });
    }
}
