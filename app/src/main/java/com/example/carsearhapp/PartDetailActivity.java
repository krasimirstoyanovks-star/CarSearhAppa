package com.example.carsearhapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.carsearhapp.model.Part;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class PartDetailActivity extends AppCompatActivity {

    private Part currentPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_detail);

        currentPart = (Part) getIntent().getSerializableExtra("part");
        if (currentPart == null) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(currentPart.getName());

        ImageView ivPart = findViewById(R.id.ivPartDetail);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        Button btnOrder = findViewById(R.id.btnOrder);

        tvPrice.setText(currentPart.getPrice());
        tvCategory.setText(currentPart.getCategory());

        TextView tvDescription = findViewById(R.id.tvDetailDescription);
        if (currentPart.getDescription() != null && !currentPart.getDescription().isEmpty()) {
            tvDescription.setText(currentPart.getDescription());
        }

        String imageUrl = currentPart.getImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = "https://img.freepik.com/free-photo/car-spare-parts-white-background_23-2149029141.jpg";
        }
        Glide.with(this).load(imageUrl).into(ivPart);

        // КОГАТО НАТИСНЕМ БУТОНА:
        btnOrder.setOnClickListener(v -> {
            String paymentUrl = currentPart.getPaymentUrl();
            
            if (paymentUrl != null && !paymentUrl.isEmpty()) {
                Toast.makeText(this, "Отваряне на защитено плащане за " + currentPart.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(paymentUrl));
                startActivity(intent);
            } else {
                Toast.makeText(this, "За тази част все още няма активен линк за плащане.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}