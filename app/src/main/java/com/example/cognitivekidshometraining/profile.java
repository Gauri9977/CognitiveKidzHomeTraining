package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class profile extends AppCompatActivity {

    private EditText etName;
    private TextView tvPhone, tvRegNo, tvBirthDate, tvAge, tvGender, tvEmail,
            tvCity, tvDoctor, tvPaediatrician, tvDisorder;

    // Firebase child data fields
    private TextView tvChildName, tvContact, tvRelation, tvCaregiver, tvRelationStatus,
            tvCommunication, tvDisability, tvSeverity, tvActivity, tvBehavior;

    private FirebaseAuth mAuth;
    private DatabaseReference childrenRef, usersRef;
    private DatabaseReference mDatabase;

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Firebase Init
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        childrenRef = database.getReference("children");
        usersRef = database.getReference("Users");

        // Bind Views in Professional Order
        etName = findViewById(R.id.etName);
        tvPhone = findViewById(R.id.tvPhone);
        tvRegNo = findViewById(R.id.tvRegNo);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvEmail = findViewById(R.id.tvEmail);
        tvCity = findViewById(R.id.tvCity);
        //tvDoctor = findViewById(R.id.tvDoctor);
        //tvPaediatrician = findViewById(R.id.tvPaediatrician);
        //tvDisorder = findViewById(R.id.tvDisorder);

        // Additional Firebase Bindings (Grouped Logically)
        tvChildName = findViewById(R.id.tvChildName);
        tvContact = findViewById(R.id.tvContact);
        tvRelation = findViewById(R.id.tvRelation);
        tvCaregiver = findViewById(R.id.tvCaregiver);
        //tvRelationStatus = findViewById(R.id.tvRelationStatus);
        tvCommunication = findViewById(R.id.tvCommunication);
        tvDisability = findViewById(R.id.tvDisability);
        tvSeverity = findViewById(R.id.tvSeverity);
        //tvActivity = findViewById(R.id.tvActivity);
        //tvBehavior = findViewById(R.id.tvBehavior);

        // Load User Data
        loadUserData();
    }

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        String userEmail = mAuth.getCurrentUser().getEmail();
        tvEmail.setText("Email: " + userEmail);

        mDatabase.child("Users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            etName.setText(snapshot.child("username").getValue(String.class));
                            tvBirthDate.setText("Birth Date: " + snapshot.child("ChildData").child("dob").getValue(String.class));
                            tvGender.setText("Gender: " + snapshot.child("ChildData").child("gender").getValue(String.class));

                            String dob = snapshot.child("ChildData").child("dob").getValue(String.class);
                            tvAge.setText("Age: " + calculateAgeString(dob));

                            tvCity.setText("Address: " + snapshot.child("ChildData").child("address").getValue(String.class));
                            //tvDoctor.setText("Diagnosis: " + snapshot.child("ChildData").child("diagnosis").getValue(String.class));

                            // Additional Fields
                            tvChildName.setText("Name: " + snapshot.child("ChildData").child("child_name").getValue(String.class));
                            tvContact.setText("Contact: " + snapshot.child("ChildData").child("contact").getValue(String.class));
                            tvCaregiver.setText("Caregiver: " + snapshot.child("ChildData").child("caregiver_details").getValue(String.class));
                            tvRelation.setText("Relation: " + snapshot.child("ChildData").child("relationship").getValue(String.class));
                            //tvRelationStatus.setText("Relationship Status: " + snapshot.child("ChildData").child("relationship_status").getValue(String.class));
                            tvCommunication.setText("Communication: " + snapshot.child("ChildData").child("communication").getValue(String.class));
                            tvDisability.setText("Disorder: " + snapshot.child("ChildData").child("disability_type").getValue(String.class));
                            tvSeverity.setText("Severity: " + snapshot.child("ChildData").child("severity").getValue(String.class));
                            //tvActivity.setText("Daily Activities: " + snapshot.child("ChildData").child("activities").getValue(String.class));
                            //tvBehavior.setText("Behavioral Issues: " + snapshot.child("ChildData").child("behavioral_issues").getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(profile.this, "Failed to load data", Toast.LENGTH_SHORT).show();
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
            return String.valueOf(age);
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
    }

    private void loadUserNameByEmail(String targetEmail) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (targetEmail.equalsIgnoreCase(email)) {
                        String userName = userSnapshot.child("user_name").getValue(String.class);
                        if (userName != null) {
                            etName.setText(userName);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error loading user_name: " + error.getMessage());
            }
        });
    }
}