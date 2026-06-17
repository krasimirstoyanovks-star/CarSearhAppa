package com.example.carsearhapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsearhapp.model.ServiceRecord;
import com.example.carsearhapp.model.Vehicle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ServiceLogActivity extends AppCompatActivity {

    private TextView tvCarTitle, tvInsuranceDate, tvInspectionDate, tvOilChangeDate, tvAvgConsumption;
    private RecyclerView rvHistory;
    private Vehicle currentVehicle;
    private int vehiclePosition;
    private List<Vehicle> garageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_log);

        currentVehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
        vehiclePosition = getIntent().getIntExtra("position", -1);

        if (currentVehicle == null || vehiclePosition == -1) {
            finish();
            return;
        }

        loadGarage();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvCarTitle = findViewById(R.id.tvServiceCarTitle);
        tvInsuranceDate = findViewById(R.id.tvInsuranceDate);
        tvInspectionDate = findViewById(R.id.tvInspectionDate);
        tvOilChangeDate = findViewById(R.id.tvOilChangeDate);
        tvAvgConsumption = findViewById(R.id.tvAvgConsumption);
        rvHistory = findViewById(R.id.rvServiceHistory);
        Button btnAddRecord = findViewById(R.id.btnAddServiceRecord);

        updateUI();

        tvInsuranceDate.setOnClickListener(v -> showDatePicker(1));
        tvInspectionDate.setOnClickListener(v -> showDatePicker(2));
        tvOilChangeDate.setOnClickListener(v -> showOilChangeDialog());
        btnAddRecord.setOnClickListener(v -> showAddRecordDialog());
        findViewById(R.id.btnCalculateFuel).setOnClickListener(v -> showFuelCalculator());
    }

    private void showFuelCalculator() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_fuel_calc, null);
        EditText etDistance = view.findViewById(R.id.etDistance);
        EditText etFuel = view.findViewById(R.id.etFuelAmount);
        TextView tvResult = view.findViewById(R.id.tvCalcResult);

        android.text.TextWatcher watcher = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculate(etDistance, etFuel, tvResult);
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        };

        etDistance.addTextChangedListener(watcher);
        etFuel.addTextChangedListener(watcher);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Запази", (dialog, which) -> {
                    String result = tvResult.getText().toString();
                    String dist = etDistance.getText().toString();
                    String fuel = etFuel.getText().toString();
                    
                    if (!result.equals("0.0 л/100км")) {
                        currentVehicle.setAverageConsumption(result);
                        currentVehicle.setLastDistance(dist);
                        currentVehicle.setLastFuelAmount(fuel);
                        saveAndRefresh();
                    }
                })
                .setNegativeButton("Затвори", null)
                .show();
    }

    private void calculate(EditText etDistance, EditText etFuel, TextView tvResult) {
        try {
            double distance = Double.parseDouble(etDistance.getText().toString());
            double fuel = Double.parseDouble(etFuel.getText().toString());
            if (distance > 0) {
                double consumption = (fuel / distance) * 100;
                tvResult.setText(String.format("%.1f л/100км", consumption));
            }
        } catch (Exception e) {
            tvResult.setText("0.0 л/100км");
        }
    }

    private void updateUI() {
        tvCarTitle.setText(currentVehicle.getMake() + " " + currentVehicle.getModel());
        
        String insurance = currentVehicle.getInsuranceExpiry() != null ? currentVehicle.getInsuranceExpiry() : getString(R.string.not_set);
        tvInsuranceDate.setText(String.format(getString(R.string.insurance_expiry_label), insurance));

        String inspection = currentVehicle.getTechnicalInspectionExpiry() != null ? currentVehicle.getTechnicalInspectionExpiry() : getString(R.string.not_set);
        tvInspectionDate.setText(String.format(getString(R.string.inspection_expiry_label), inspection));

        String oilDate = currentVehicle.getNextOilChange() != null ? currentVehicle.getNextOilChange() : getString(R.string.not_set);
        String oilMileage = currentVehicle.getNextOilChangeMileage() != null ? currentVehicle.getNextOilChangeMileage() : getString(R.string.not_set);
        tvOilChangeDate.setText(String.format(getString(R.string.oil_change_label), oilDate, oilMileage));

        String consumption = currentVehicle.getAverageConsumption() != null ? currentVehicle.getAverageConsumption() : getString(R.string.not_set);
        String lastFuel = currentVehicle.getLastFuelAmount() != null ? currentVehicle.getLastFuelAmount() : "0";
        String lastDist = currentVehicle.getLastDistance() != null ? currentVehicle.getLastDistance() : "0";
        
        tvAvgConsumption.setText(String.format(getString(R.string.consumption_label), consumption, lastFuel, lastDist));

        ServiceLogAdapter adapter = new ServiceLogAdapter(currentVehicle.getServiceHistory());
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);
    }

    private void showDatePicker(int type) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "." + (month + 1) + "." + year;
            if (type == 1) currentVehicle.setInsuranceExpiry(date);
            else if (type == 2) currentVehicle.setTechnicalInspectionExpiry(date);
            saveAndRefresh();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showOilChangeDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_oil_change, null);
        TextView tvOilDate = view.findViewById(R.id.tvOilDate);
        EditText etOilMileage = view.findViewById(R.id.etOilMileage);
        
        final Calendar selectedCalendar = Calendar.getInstance();

        tvOilDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                selectedCalendar.set(year, month, dayOfMonth);
                String date = dayOfMonth + "." + (month + 1) + "." + year;
                tvOilDate.setText("Дата на смяна: " + date);
            }, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Запази", (dialog, which) -> {
                    String mileageStr = etOilMileage.getText().toString().trim();
                    if (!mileageStr.isEmpty()) {
                        // 1. Изчисляваме следващата дата (+1 година)
                        selectedCalendar.add(Calendar.YEAR, 1);
                        String nextDate = selectedCalendar.get(Calendar.DAY_OF_MONTH) + "." + 
                                         (selectedCalendar.get(Calendar.MONTH) + 1) + "." + 
                                         selectedCalendar.get(Calendar.YEAR);
                        
                        // 2. Изчисляваме следващите километри (+10 000 км)
                        int nextMileage = Integer.parseInt(mileageStr) + 10000;
                        
                        currentVehicle.setNextOilChange(nextDate);
                        currentVehicle.setNextOilChangeMileage(String.valueOf(nextMileage));
                        saveAndRefresh();
                    }
                })
                .setNegativeButton("Отказ", null)
                .show();
    }

    private void showAddRecordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_service, null);
        EditText etDesc = view.findViewById(R.id.etServiceDesc);
        EditText etMileage = view.findViewById(R.id.etServiceMileage);

        new AlertDialog.Builder(this)
                .setTitle("Добави сервизен запис")
                .setView(view)
                .setPositiveButton("Запази", (dialog, which) -> {
                    String desc = etDesc.getText().toString().trim();
                    String mileageStr = etMileage.getText().toString().trim();

                    if (!desc.isEmpty() && !mileageStr.isEmpty()) {
                        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." +
                                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "." +
                                Calendar.getInstance().get(Calendar.YEAR);
                        
                        ServiceRecord record = new ServiceRecord(date, desc, Integer.parseInt(mileageStr));
                        currentVehicle.getServiceHistory().add(0, record);
                        saveAndRefresh();
                    }
                })
                .setNegativeButton("Отказ", null)
                .show();
    }

    private void loadGarage() {
        SharedPreferences prefs = getSharedPreferences("CarSearchPrefs", MODE_PRIVATE);
        String json = prefs.getString("my_garage", "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<Vehicle>>() {}.getType();
            garageList = new Gson().fromJson(json, type);
        }
    }

    private void saveAndRefresh() {
        garageList.set(vehiclePosition, currentVehicle);
        SharedPreferences prefs = getSharedPreferences("CarSearchPrefs", MODE_PRIVATE);
        String json = new Gson().toJson(garageList);
        prefs.edit().putString("my_garage", json).apply();
        updateUI();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}