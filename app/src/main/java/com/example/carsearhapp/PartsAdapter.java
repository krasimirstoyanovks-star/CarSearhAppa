package com.example.carsearhapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carsearhapp.model.Part;

import java.util.List;

public class PartsAdapter extends RecyclerView.Adapter<PartsAdapter.ViewHolder> {

    private List<Part> partsList;
    private OnPartClickListener listener;

    public interface OnPartClickListener {
        void onPartClick(Part part);
    }

    public PartsAdapter(List<Part> partsList, OnPartClickListener listener) {
        this.partsList = partsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_part, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Part part = partsList.get(position);
        holder.tvPartName.setText(part.getName());
        holder.tvPartCategory.setText(part.getCategory());
        holder.tvPartPrice.setText(part.getPrice());
        holder.tvPartAvailability.setText(part.getAvailability());

        String imageUrl = part.getImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = "https://img.freepik.com/free-photo/car-spare-parts-white-background_23-2149029141.jpg";
        }

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivPartThumb);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPartClick(part);
            }
        });
    }

    public void updateList(List<Part> newList) {
        this.partsList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return partsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPartName, tvPartCategory, tvPartPrice, tvPartAvailability;
        ImageView ivPartThumb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPartName = itemView.findViewById(R.id.tvPartName);
            tvPartCategory = itemView.findViewById(R.id.tvPartCategory);
            tvPartPrice = itemView.findViewById(R.id.tvPartPrice);
            tvPartAvailability = itemView.findViewById(R.id.tvPartAvailability);
            ivPartThumb = itemView.findViewById(R.id.ivPartThumb);
        }
    }
}