package com.example.carsearhapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsearhapp.model.ServiceRecord;

import java.util.List;

public class ServiceLogAdapter extends RecyclerView.Adapter<ServiceLogAdapter.ViewHolder> {

    private List<ServiceRecord> records;

    public ServiceLogAdapter(List<ServiceRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRecord record = records.get(position);
        holder.tvDate.setText(record.getDate());
        holder.tvMileage.setText(record.getMileage() + " км");
        holder.tvDescription.setText(record.getDescription());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvMileage, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvServiceDate);
            tvMileage = itemView.findViewById(R.id.tvServiceMileage);
            tvDescription = itemView.findViewById(R.id.tvServiceDescription);
        }
    }
}