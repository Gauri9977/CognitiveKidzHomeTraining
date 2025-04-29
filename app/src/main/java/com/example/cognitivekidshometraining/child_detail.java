package com.example.cognitivekidshometraining;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class child_detail extends AppCompatActivity {
    TextView tvName, mobile_no_tv, dob_tv, age_tv, gender_tv, city_tv, parent_tv, care_tv, diagnosis_tv;
    private DatabaseReference usersRef;
    private String uid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvName = findViewById(R.id.tvName);
        mobile_no_tv = findViewById(R.id.mobile_no_tv);
        dob_tv = findViewById(R.id.dob_tv);
        age_tv = findViewById(R.id.age_tv);
        gender_tv = findViewById(R.id.gender_tv);
        city_tv = findViewById(R.id.city_tv);
        parent_tv = findViewById(R.id.parent_tv);
        care_tv = findViewById(R.id.care_tv);
        diagnosis_tv = findViewById(R.id.diagnosis_tv);

        uid = getIntent().getStringExtra("uid");
        if (uid == null) {
            Toast.makeText(this, "Invalid child", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        loadChildData();

    }

    @SuppressLint("SetTextI18n")
    private void loadChildData() {
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();

                String name = snapshot.child("ChildData/child_name").getValue(String.class);
                String mobile = snapshot.child("ChildData/contact").getValue(String.class);
                String dob = snapshot.child("ChildData/dob").getValue(String.class);
                String ageString = calculateAgeString(dob);
                String gender = snapshot.child("ChildData/gender").getValue(String.class);
                String address = snapshot.child("ChildData/address").getValue(String.class);
                String parent = snapshot.child("ChildData/parent_name").getValue(String.class);
                String care = snapshot.child("ChildData/caregiver_details").getValue(String.class);
                String treatment = snapshot.child("ChildData/diagnosis").getValue(String.class);

                tvName.setText(name != null ? name : "N/A");
                mobile_no_tv.setText(mobile != null ? mobile : "N/A");
                dob_tv.setText("Date of birth: " + (dob != null ? dob : "N/A"));
                age_tv.setText("Age: " + (ageString != null ? ageString : "N/A"));
                gender_tv.setText("Gender: " + (gender != null ? gender : "N/A"));
                city_tv.setText("Address: " + (address != null ? address : "N/A"));
                parent_tv.setText("Parent Name: " + (parent != null ? parent : "N/A"));
                care_tv.setText("Care Giver: " + (care != null ? care : "N/A"));
                diagnosis_tv.setText("Treating For: " + (treatment != null ? treatment : "N/A"));

            } else {
                Toast.makeText(this, "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String calculateAgeString(String birthDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date birthDate = sdf.parse(birthDateString);
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);

            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return String.valueOf(age); // Convert age to String
        } catch (ParseException e) {
            e.printStackTrace();
            return "0"; // Return "0" as String if error
        }
    }

}