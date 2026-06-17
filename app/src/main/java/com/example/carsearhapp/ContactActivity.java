package com.example.carsearhapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ContactActivity extends AppCompatActivity {

    private EditText etName, etFeedback;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etName = findViewById(R.id.etName);
        etFeedback = findViewById(R.id.etFeedback);
        btnSend = findViewById(R.id.btnSendFeedback);

        btnSend.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String feedback = etFeedback.getText().toString().trim();

            if (name.isEmpty() || feedback.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            String successMsg = String.format(getString(R.string.feedback_success), name);
            Toast.makeText(this, successMsg, Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}