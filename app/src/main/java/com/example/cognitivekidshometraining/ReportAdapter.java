package com.example.cognitivekidshometraining;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final List<ReportWeeklyReport> reports;

    public ReportAdapter(List<ReportWeeklyReport> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_card, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportWeeklyReport report = reports.get(position);

        // Set all text values
        holder.dateText.setText("Date: " + report.date);
        holder.childName.setText("Name: " + report.childName);
        holder.age.setText("Age: " + report.age);
        holder.disorder.setText("Disorder: " + report.disorder);
        holder.doctorName.setText("Doctor: " + report.doctorName);

        holder.therapiesCompleted.setText("Therapies Completed: " + report.therapiesCompleted);
        holder.therapiesMissed.setText("Therapies Missed: " + report.therapiesMissed);
        holder.activitiesCompleted.setText("Activities Completed: " + report.activitiesCompleted);
        holder.activitiesMissed.setText("Activities Missed: " + report.activitiesMissed);

        holder.testsResults.setText("Test Results: "+report.testsAndResults);
        holder.clinicalObservation.setText("Clinical Observations: "+report.clinicalObservation);

        // Handling doctor's note visibility
        if (report.doctorNote != null && !report.doctorNote.isEmpty()) {
            holder.doctorNote.setText("Doctor's Note: "+report.doctorNote);
        } else {
            holder.doctorNote.setText("Doctor's note is unavailable.");
        }
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, childName, age, disorder, doctorName,
                therapiesCompleted, therapiesMissed,
                activitiesCompleted, activitiesMissed,
                testsResults, clinicalObservation, doctorNote;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            childName = itemView.findViewById(R.id.childName);
            age = itemView.findViewById(R.id.age);
            disorder = itemView.findViewById(R.id.disorder);
            doctorName = itemView.findViewById(R.id.doctorName);
            therapiesCompleted = itemView.findViewById(R.id.therapiesCompleted);
            therapiesMissed = itemView.findViewById(R.id.therapiesMissed);
            activitiesCompleted = itemView.findViewById(R.id.activitiesCompleted);
            activitiesMissed = itemView.findViewById(R.id.activitiesMissed);
            testsResults = itemView.findViewById(R.id.testsResults);
            clinicalObservation = itemView.findViewById(R.id.clinicalObservation);
            doctorNote = itemView.findViewById(R.id.doctorNote);
        }
    }
}
