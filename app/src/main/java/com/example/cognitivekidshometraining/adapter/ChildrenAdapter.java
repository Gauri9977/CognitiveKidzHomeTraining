package com.example.cognitivekidshometraining.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cognitivekidshometraining.Model.Child;
import com.example.cognitivekidshometraining.R;
import com.example.cognitivekidshometraining.child_detail;

import java.util.List;

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ChildViewHolder> {

    private final List<Child> childrenList;
    private final Context context;

    public interface OnViewClickListener {
        void onViewClick(Child child);
    }

    private final OnViewClickListener listener;

    public ChildrenAdapter(Context context, List<Child> childrenList, OnViewClickListener listener) {
        this.context = context;
        this.childrenList = childrenList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.childlist_itemview, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        Child child = childrenList.get(position);
        holder.nameText.setText(child.name);
        holder.disorderText.setText("Disorder: " + child.disorder);

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, child_detail.class);
            intent.putExtra("uid", child.uid);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, disorderText;
        Button viewButton;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.childName1);
            disorderText = itemView.findViewById(R.id.childDisorder1);
            viewButton = itemView.findViewById(R.id.viewDetails1);
        }
    }
}

