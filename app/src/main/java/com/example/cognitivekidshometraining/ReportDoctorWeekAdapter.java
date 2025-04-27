package com.example.cognitivekidshometraining;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportDoctorWeekAdapter extends RecyclerView.Adapter<ReportDoctorWeekAdapter.DoctorWeekViewHolder> {

    private final Context context;
    private final List<String> weekDates;
    private final List<Integer> reportIndexes;
    private final List<ReportWeeklyReport> reports;

    public ReportDoctorWeekAdapter(Context context, Map<String, Integer> weekMap, List<ReportWeeklyReport> reports) {
        this.context = context;
        this.weekDates = new ArrayList<>(weekMap.keySet());
        this.reportIndexes = new ArrayList<>(weekMap.values());
        this.reports = reports;
    }

    @NonNull
    @Override
    public DoctorWeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_week_item, parent, false);
        return new DoctorWeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorWeekViewHolder holder, int position) {
        String date = weekDates.get(position);
        int reportIndex = reportIndexes.get(position);

        holder.weekNumberText.setText("Week of " + date);

        holder.viewReportButton.setText("Edit/View Report");
        holder.viewReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDoctorReportActivity.class);
            intent.putExtra("report_index", reportIndex);
            intent.putExtra("report_date", date);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return weekDates.size();
    }

    public static class DoctorWeekViewHolder extends RecyclerView.ViewHolder {
        TextView weekNumberText;
        Button viewReportButton;

        public DoctorWeekViewHolder(@NonNull View itemView) {
            super(itemView);
            weekNumberText = itemView.findViewById(R.id.weekNumberText);
            viewReportButton = itemView.findViewById(R.id.viewReportButton);
        }
    }
}
