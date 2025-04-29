package com.example.cognitivekidshometraining;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dr_consult extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;

    EditText etName, etDisorder, etDate, etTime;
    Button btnBook;
    Spinner spinnerChildName;

    private DatabaseReference usersRef;
    private Map<String, String> childUidMap = new HashMap<>();

    String selectedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_consult);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        fetchChildren();

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Consultation");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();

        DrawerLayout drawer = findViewById(R.id.dr_consult_page);
        toggle = new ActionBarDrawerToggle(dr_consult.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();

        etName = findViewById(R.id.et_child_name);
        etDisorder = findViewById(R.id.et_child_disorder);
        etDate = findViewById(R.id.et_appointment_date);
        etTime = findViewById(R.id.et_appointment_time);
        btnBook = findViewById(R.id.btn_book_appointment);
        spinnerChildName = findViewById(R.id.spinnerChildName);

        etDisorder.setEnabled(false);

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view, year, month, day) ->
                            etDate.setText(day + "/" + (month + 1) + "/" + year),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        etTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this,
                    (view, hour, minute) ->
                            etTime.setText(String.format("%02d:%02d", hour, minute)),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), true).show();
        });

        btnBook.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String disorder = etDisorder.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (selectedName.isEmpty() || disorder.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Store appointment details using SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AppointmentData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("childName", selectedName);
            editor.putString("childDisorder", disorder);
            editor.putString("date", date);
            editor.putString("time", time);
            editor.apply();

            // Just show toast, do NOT go to consultation screen
            Toast.makeText(this, "Appointment request sent to " + selectedName, Toast.LENGTH_LONG).show();
        });
    }

    private void fetchChildren() {
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<String> childNames = new ArrayList<>();

                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    String uid = userSnapshot.getKey();
                    String name = userSnapshot.child("ChildData/child_name").getValue(String.class);

                    if (name != null) {
                        childNames.add(name);
                        childUidMap.put(name, uid); // Store mapping
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, childNames);
                spinnerChildName.setAdapter(adapter);

                spinnerChildName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedName = parent.getItemAtPosition(position).toString();
                        String uid = childUidMap.get(selectedName);
                        fetchDisorderForChild(uid);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

            } else {
                Toast.makeText(this, "Failed to load children", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDisorderForChild(String uid) {
        usersRef.child(uid).child("ChildData").child("disability_type")
                .get().addOnSuccessListener(snapshot -> {
                    String disorder = snapshot.getValue(String.class);
                    if (disorder != null) {
                        etDisorder.setText(disorder);
                    } else {
                        etDisorder.setText("Disorder: Not specified");
                    }
                }).addOnFailureListener(e -> {
                    etDisorder.setText("Error fetching disorder");
                });
    }
}