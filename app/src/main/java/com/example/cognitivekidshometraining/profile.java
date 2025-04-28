package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
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

    private FirebaseAuth mAuth;
    private DatabaseReference childrenRef, usersRef;

    private static final String TAG = "ProfileActivity";
    int childAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        childrenRef = database.getReference("children");
        usersRef = database.getReference("users");

        // Bind Views
        etName = findViewById(R.id.etName);
        tvPhone = findViewById(R.id.tvPhone);
        tvRegNo = findViewById(R.id.tvRegNo);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvEmail = findViewById(R.id.tvEmail);
        tvCity = findViewById(R.id.tvCity);
        tvDoctor = findViewById(R.id.tvDoctor);
        tvPaediatrician = findViewById(R.id.tvPaediatrician);
        tvDisorder = findViewById(R.id.tvDisorder);

        // Load user data
        loadProfileData();
    }


    private void loadProfileData() {
        String userId = mAuth.getCurrentUser().getUid();
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    etName.setText(snapshot.child("username").getValue(String.class));
//                    tvPhone.setText(snapshot.child("phone").getValue(String.class));
                    tvBirthDate.setText("Birth Date: " + snapshot.child("dob").getValue(String.class));
//                    tvAge.setText("Age: " + childAge);
                    tvGender.setText("Gender: " + snapshot.child("gender").getValue(String.class));
                    tvEmail.setText("Email ID: " + snapshot.child("email").getValue(String.class));
//                    tvDisorder.setText(snapshot.child("disorder").getValue(String.class));

                    String dob = snapshot.child("dob").getValue(String.class); // Example: "27/04/2020"
                    String ageString = calculateAgeString(dob); // ageString will be like "5"

                    tvAge.setText("Age: " + ageString); // Set it to your TextView


                    //Static field
                    tvCity.setText("Address: " + snapshot.child("address").getValue(String.class));
                    tvDoctor.setText("Diagnosis: " + snapshot.child("diagnosis").getValue(String.class));
//                    tvPaediatrician.setText(snapshot.child("paediatrician").getValue(String.class));
                } else {
                    Log.e(TAG, "Snapshot does not exist for userId: " + userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error loading child profile: " + error.getMessage());
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




    private void loadUserNameByEmail(String targetEmail) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (targetEmail.equalsIgnoreCase(email)) {
                        String userName = userSnapshot.child("user_name").getValue(String.class);
                        if (userName != null) {
                            etName.setText(userName); // Set user_name in the etName field
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
