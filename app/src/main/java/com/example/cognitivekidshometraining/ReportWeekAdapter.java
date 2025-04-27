package com.example.cognitivekidshometraining;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class ReportWeekAdapter extends RecyclerView.Adapter<ReportWeekAdapter.WeekViewHolder> {

    private Context context;
    private List<String> weekDates;
    private List<Integer> reportIndexes;

    public ReportWeekAdapter(Context context, Map<String, Integer> weekMap) {
        this.context = context;
        this.weekDates = new ArrayList<>(weekMap.keySet());
        this.reportIndexes = new ArrayList<>(weekMap.values());
    }

    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_week_item, parent, false);
        return new WeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        String date = weekDates.get(position);
        int reportIndex = reportIndexes.get(position);

        holder.weekNumberText.setText("Week of " + date);

        holder.viewReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportActivity.class);
            intent.putExtra("report_index", reportIndex);
            context.startActivity(intent);
        });
        Log.d("WeekAdapter", "Binding view for week: " + weekDates.get(position));
    }

    @Override
    public int getItemCount() {
        return weekDates.size();
    }

    public static class WeekViewHolder extends RecyclerView.ViewHolder {
        TextView weekNumberText;
        Button viewReportButton;

        public WeekViewHolder(@NonNull View itemView) {
            super(itemView);
            weekNumberText = itemView.findViewById(R.id.weekNumberText);
            viewReportButton = itemView.findViewById(R.id.viewReportButton);
        }
    }
}
