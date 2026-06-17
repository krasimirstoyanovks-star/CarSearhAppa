package com.example.carsearhapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsearhapp.model.Vehicle;

import java.util.List;

public class GarageAdapter extends RecyclerView.Adapter<GarageAdapter.ViewHolder> {

    private List<Vehicle> garageList;
    private OnGarageItemClickListener listener;

    public interface OnGarageItemClickListener {
        void onGarageClick(Vehicle vehicle);
        void onRemoveClick(int position);
        void onServiceLogClick(Vehicle vehicle, int position);
    }

    public GarageAdapter(List<Vehicle> garageList, OnGarageItemClickListener listener) {
        this.garageList = garageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_garage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = garageList.get(position);
        String title = vehicle.getMake() + " " + vehicle.getModel();
        holder.tvTitle.setText(title);
        
        String vinText = "VIN: " + (vehicle.getVin() != null ? vehicle.getVin() : "");
        holder.tvVin.setText(vinText);
        
        holder.itemView.setOnClickListener(v -> listener.onGarageClick(vehicle));
        holder.btnRemove.setOnClickListener(v -> listener.onRemoveClick(holder.getAdapterPosition()));
        holder.btnService.setOnClickListener(v -> listener.onServiceLogClick(vehicle, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return garageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvVin;
        ImageButton btnRemove, btnService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGarageTitle);
            tvVin = itemView.findViewById(R.id.tvGarageVin);
            btnRemove = itemView.findViewById(R.id.btnRemoveGarage);
            btnService = itemView.findViewById(R.id.btnServiceLog);
        }
    }
}