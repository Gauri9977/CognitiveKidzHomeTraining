package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class profile extends AppCompatActivity {

    private EditText etName;
    private TextView tvPhone, tvRegNo, tvBirthDate, tvAge, tvGender, tvEmail,
            tvCity, tvDoctor, tvPaediatrician, tvDisorder;

    private FirebaseAuth mAuth;
    private DatabaseReference childrenRef, usersRef;

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //childrenRef = database.getReference("children");
        //usersRef = database.getReference("users");

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
        childrenRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    etName.setText(snapshot.child("name").getValue(String.class));
                    tvPhone.setText(snapshot.child("phone").getValue(String.class));
                    tvBirthDate.setText(snapshot.child("birthDate").getValue(String.class));
                    tvAge.setText(snapshot.child("age").getValue(String.class));
                    tvGender.setText(snapshot.child("gender").getValue(String.class));
                    tvEmail.setText(snapshot.child("email").getValue(String.class));
                    tvDisorder.setText(snapshot.child("disorder").getValue(String.class));

                    //Static fields
                    tvRegNo.setText(snapshot.child("regNo").getValue(String.class));
                    tvCity.setText(snapshot.child("city").getValue(String.class));
                    tvDoctor.setText(snapshot.child("doctor").getValue(String.class));
                    tvPaediatrician.setText(snapshot.child("paediatrician").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error loading child profile: " + error.getMessage());
            }
        });
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
