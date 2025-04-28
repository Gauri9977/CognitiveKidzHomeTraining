package com.example.cognitivekidshometraining;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GoogleForm extends AppCompatActivity {

    EditText name, dob, diagnosis, behavior, parentName, relationship,
            contactInfo, address, caregiverDetails;

    RadioGroup genderGroup, severityGroup, communicationGroup, activitiesGroup, relationshipStatusGroup;
    CheckBox autism, adhd, learning, intellectual;
    Button submitButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_form);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Mapping Views
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        diagnosis = findViewById(R.id.diagnosis);
        behavior = findViewById(R.id.behavior);
        parentName = findViewById(R.id.parentName);
        relationship = findViewById(R.id.relationship);
        contactInfo = findViewById(R.id.contactInfo);
        address = findViewById(R.id.address);
        caregiverDetails = findViewById(R.id.caregiverDetails);

        genderGroup = findViewById(R.id.genderGroup);
        severityGroup = findViewById(R.id.severityGroup);
        communicationGroup = findViewById(R.id.communicationGroup);
        activitiesGroup = findViewById(R.id.activitiesGroup);
        relationshipStatusGroup = findViewById(R.id.relationshipStatusGroup);

        autism = findViewById(R.id.autism);
        adhd = findViewById(R.id.adhd);
        learning = findViewById(R.id.learning);
        intellectual = findViewById(R.id.intellectual);

        submitButton = findViewById(R.id.submitButton);

        // Retrieve data passed from the registration activity
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        // Optionally display these values (e.g., in Toast or Log)
        Toast.makeText(this, "Username: " + username + "\nEmail: " + email
                + "\nPassword: " + password, Toast.LENGTH_LONG).show();

        // Set click listener for Submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectFormData(username, email, password);
            }
        });
    }

    private void collectFormData(String username, String email, String password) {
        // Text Fields
        String childName = name.getText().toString().trim();
        String birthDate = dob.getText().toString().trim();
        String diag = diagnosis.getText().toString().trim();
        String behaviorIssues = behavior.getText().toString().trim();
        String pName = parentName.getText().toString().trim();
        String relation = relationship.getText().toString().trim();
        String contact = contactInfo.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String caregiver = caregiverDetails.getText().toString().trim();

        // Validation for required fields
        if (childName.isEmpty() || birthDate.isEmpty() || diag.isEmpty() ||
                behaviorIssues.isEmpty() || pName.isEmpty() || relation.isEmpty() ||
                contact.isEmpty() || addr.isEmpty() || caregiver.isEmpty()) {
            Toast.makeText(GoogleForm.this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;  // Stop execution if validation fails
        }

        // Gender
        String gender = getSelectedRadioText(genderGroup);
        if (gender.equals("Not selected")) {
            Toast.makeText(this, "Please select gender.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Severity
        String severity = getSelectedRadioText(severityGroup);
        if (severity.equals("Not selected")) {
            Toast.makeText(this, "Please select severity.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Communication
        String communication = getSelectedRadioText(communicationGroup);
        if (communication.equals("Not selected")) {
            Toast.makeText(this, "Please select communication level.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Activities
        String activityHelp = getSelectedRadioText(activitiesGroup);
        if (activityHelp.equals("Not selected")) {
            Toast.makeText(this, "Please select activity help level.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Relationship Status
        String relStatus = getSelectedRadioText(relationshipStatusGroup);  // Fixed here
        if (relStatus.equals("Not selected")) {
            Toast.makeText(this, "Please select relationship status.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Checkboxes
        StringBuilder disabilityTypes = new StringBuilder();
        if (autism.isChecked()) disabilityTypes.append("Autism, ");
        if (adhd.isChecked()) disabilityTypes.append("ADHD/ADD, ");
        if (learning.isChecked()) disabilityTypes.append("Learning Disability, ");
        if (intellectual.isChecked()) disabilityTypes.append("Intellectual Disability");

        saveUserToDatabase(username, email, password, childName, birthDate, diag, behaviorIssues, pName, relation, contact, addr, caregiver, gender, severity, communication, activityHelp, disabilityTypes.toString(), relStatus);

        // Firebase user registration (if not already done)
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, authTask -> {
//                    if (authTask.isSuccessful()) {
//                        saveUserToDatabase(username, email, password, childName, birthDate, diag, behaviorIssues, pName, relation, contact, addr, caregiver, gender, severity, communication, activityHelp, disabilityTypes.toString(), relStatus);
//                    } else {
//                        Toast.makeText(this, "Registration Failed: " + authTask.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
    }

    private void saveUserToDatabase(String username, String email, String password, String childName, String birthDate, String diag, String behaviorIssues, String pName, String relation, String contact, String addr, String caregiver, String gender, String severity, String communication, String activityHelp, String disabilityTypes, String relStatus) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid(); // Get logged-in user UID

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        String createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

        Map<String, Object> user = new HashMap<>();
        user.put("user_id", uid); // save UID instead of number
        user.put("username", username);
        user.put("email", email);
        user.put("password", password);
        user.put("child_name", childName);
        user.put("dob", birthDate);
        user.put("gender", gender);
        user.put("diagnosis", diag);
        user.put("disability_type", disabilityTypes);
        user.put("severity", severity);
        user.put("behavioral_issues", behaviorIssues);
        user.put("communication", communication);
        user.put("activities", activityHelp);
        user.put("parent_name", pName);
        user.put("relationship", relation);
        user.put("contact", contact);
        user.put("relationship_status", relStatus);
        user.put("address", addr);
        user.put("caregiver_details", caregiver);
        user.put("role", "Child");
        user.put("created_at", createdAt);

        usersRef.child(uid).setValue(user) // Save under UID
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(GoogleForm.this, "Child Details Submitted Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(GoogleForm.this, login.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(GoogleForm.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


//    private void saveUserToDatabase(String username, String email, String password, String childName, String birthDate, String diag, String behaviorIssues, String pName, String relation, String contact, String addr, String caregiver, String gender, String severity, String communication, String activityHelp, String disabilityTypes, String relStatus) {
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
//
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                long maxId = 0;
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    Object userIdObj = child.child("user_id").getValue();
//                    if (userIdObj != null) {
//                        try {
//                            long userId = Long.parseLong(userIdObj.toString());
//                            if (userId > maxId) {
//                                maxId = userId;
//                            }
//                        } catch (NumberFormatException ignored) {}
//                    }
//                }
//
//                long newUserId = maxId + 1;
//                String createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());
//
//                Map<String, Object> user = new HashMap<>();
//                user.put("user_id", newUserId);
//                user.put("username", username);
//                user.put("email", email);
//                user.put("password", password);
//                user.put("child_name", childName);
//                user.put("dob", birthDate);
//                user.put("gender", gender);
//                user.put("diagnosis", diag);
//                user.put("disability_type", disabilityTypes);
//                user.put("severity", severity);
//                user.put("behavioral_issues", behaviorIssues);
//                user.put("communication", communication);
//                user.put("activities", activityHelp);
//                user.put("parent_name", pName);
//                user.put("relationship", relation);
//                user.put("contact", contact);
//                user.put("relationship_status", relStatus);  // Save relationship status
//                user.put("address", addr);
//                user.put("caregiver_details", caregiver);
//                user.put("role", "Child");
//                user.put("created_at", createdAt);
//
//                usersRef.child(String.valueOf(newUserId)).setValue(user)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(GoogleForm.this, "Child Details Submitted Successfully", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(GoogleForm.this, login.class); // Change to desired activity
//                                startActivity(i);
//                                finish();
//                            } else {
//                                Toast.makeText(GoogleForm.this, "Database Error", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(GoogleForm.this, "Failed to read data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // Helper method to get selected radio button text
    private String getSelectedRadioText(RadioGroup group) {
        int selectedId = group.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            return selectedButton.getText().toString();
        }
        return "Not selected";
    }
}
