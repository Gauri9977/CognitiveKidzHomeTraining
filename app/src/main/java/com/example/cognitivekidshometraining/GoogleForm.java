package com.example.cognitivekidshometraining;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class GoogleForm extends AppCompatActivity {

    EditText name, dob, diagnosis, behavior, parentName, relationship,
            contactInfo, address, caregiverDetails;

    RadioGroup genderGroup, severityGroup, communicationGroup, activitiesGroup, relationshipStatusGroup;
    CheckBox autism, adhd, learning, intellectual;
    Button submitButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_form);

        // Mapping Views
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        diagnosis = findViewById(R.id.diagnosis);
        behavior = findViewById(R.id.behavior);
        parentName = findViewById(R.id.parentName);
        relationship = findViewById(R.id.relationship);
        contactInfo = findViewById(R.id.contactInfo);
        //address = findViewById(R.id.address);
        //caregiverDetails = findViewById(R.id.caregiverDetails);

        genderGroup = findViewById(R.id.genderGroup);
        severityGroup = findViewById(R.id.severityGroup);
        communicationGroup = findViewById(R.id.communicationGroup);
        activitiesGroup = findViewById(R.id.activitiesGroup);
        relationshipStatusGroup = findViewById(R.id.relationshipStatusGroup);

        autism = findViewById(R.id.autism);
        adhd = findViewById(R.id.adhd);
        learning = findViewById(R.id.learning);
        intellectual = findViewById(R.id.intellectual);

        //submitButton = findViewById(R.id.submitButton);

        // Set click listener for Submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectFormData();
            }
        });
    }

    private void collectFormData() {
        if (isEmpty(name) || isEmpty(dob) || isEmpty(diagnosis) || isEmpty(behavior) ||
                isEmpty(parentName) || isEmpty(relationship) || isEmpty(contactInfo) ||
                isEmpty(address) || isEmpty(caregiverDetails)) {
            Toast.makeText(this, "Please fill all text fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!atLeastOneChecked(autism, adhd, learning, intellectual)) {
            Toast.makeText(this, "Select at least one disability type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!radioGroupSelected(genderGroup) || !radioGroupSelected(severityGroup) ||
                !radioGroupSelected(communicationGroup) || !radioGroupSelected(activitiesGroup) ||
                !radioGroupSelected(relationshipStatusGroup)) {
            Toast.makeText(this, "Please complete all multiple choice selections", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed if all fields are valid (rest of your form logic...)
        Toast.makeText(this, "Form submitted successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, home.class));
        finish();
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private boolean radioGroupSelected(RadioGroup group) {
        return group.getCheckedRadioButtonId() != -1;
    }

    private boolean atLeastOneChecked(CheckBox... boxes) {
        for (CheckBox box : boxes) {
            if (box.isChecked()) return true;
        }
        return false;
    }

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