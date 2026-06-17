package com.example.carsearhapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carsearhapp.model.Part;
import com.example.carsearhapp.model.Vehicle;
import com.example.carsearhapp.network.ApiService;
import com.example.carsearhapp.network.RetrofitClient;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryClickListener, PartsAdapter.OnPartClickListener, GarageAdapter.OnGarageItemClickListener {

    private EditText etVin;
    private Button btnSearch, btnFindParts, btnSaveGarage, btnGoToLogin;
    private TextView tvResult, tvProfileStatus;
    private ProgressBar progressBar;
    private MaterialCardView cardResult, cardProfilePrompt;
    private ImageView ivVehicleLogo;
    private LinearLayout layoutParts;
    private Spinner spinnerCategories;
    private RecyclerView rvHistory, rvParts, rvGarage;
    private HistoryAdapter historyAdapter;
    private PartsAdapter partsAdapter;
    private GarageAdapter garageAdapter;
    private List<String> historyList = new ArrayList<>();
    private List<Vehicle> garageList = new ArrayList<>();
    private List<Part> allPartsList = new ArrayList<>();
    private Vehicle currentVehicle;
    private ApiService apiService;

    private static final String PREFS_NAME = "CarSearchPrefs";
    private static final String KEY_HISTORY = "search_history";
    private static final String KEY_GARAGE_BASE = "my_garage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etVin = findViewById(R.id.etVin);
        btnSearch = findViewById(R.id.btnSearch);
        btnFindParts = findViewById(R.id.btnFindParts);
        btnSaveGarage = findViewById(R.id.btnSaveGarage);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);
        cardResult = findViewById(R.id.cardResult);
        cardProfilePrompt = findViewById(R.id.cardProfilePrompt);
        tvProfileStatus = findViewById(R.id.tvProfileStatus);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        ivVehicleLogo = findViewById(R.id.ivVehicleLogo);
        layoutParts = findViewById(R.id.layoutParts);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        rvHistory = findViewById(R.id.rvHistory);
        rvParts = findViewById(R.id.rvParts);
        rvGarage = findViewById(R.id.rvGarage);

        setupRecyclerViews(); 
        loadHistory();
        loadGarage();
        setupSpinner();
        updateProfilePrompt();

        btnGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        btnSearch.setOnClickListener(v -> {
            String vin = etVin.getText().toString().trim().toUpperCase();
            if (vin.length() < 10) {
                etVin.setError("VIN номерът е твърде кратък");
                return;
            }
            searchVehicle(vin);
        });

        btnFindParts.setOnClickListener(v -> {
            if (currentVehicle != null) {
                findParts(currentVehicle);
            }
        });

        btnSaveGarage.setOnClickListener(v -> {
            if (currentVehicle != null) {
                saveToGarage(currentVehicle);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGarage();
        updateProfilePrompt();
        checkReminders();
    }

    private String getGarageKey() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains("logged_user")) {
            String userJson = prefs.getString("logged_user", "");
            com.example.carsearhapp.model.User user = new com.google.gson.Gson().fromJson(userJson, com.example.carsearhapp.model.User.class);
            return KEY_GARAGE_BASE + "_" + user.getId();
        }
        return KEY_GARAGE_BASE + "_guest";
    }

    private void updateProfilePrompt() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains("logged_user")) {
            String userJson = prefs.getString("logged_user", "");
            com.example.carsearhapp.model.User user = new com.google.gson.Gson().fromJson(userJson, com.example.carsearhapp.model.User.class);
            
            tvProfileStatus.setText(String.format(getString(R.string.welcome_user), user.getFullName()));
            tvProfileStatus.append("\nИмате " + garageList.size() + " автомобила в гаража.");
            btnGoToLogin.setVisibility(View.GONE);
            
            cardProfilePrompt.setCardBackgroundColor(getResources().getColor(R.color.car_primary));
            tvProfileStatus.setTextColor(getResources().getColor(R.color.white));
            
            View icon = cardProfilePrompt.findViewById(R.id.ivProfileIcon);
            if (icon instanceof ImageView) {
                ((ImageView)icon).setColorFilter(getResources().getColor(R.color.white));
            }
        } else {
            tvProfileStatus.setText(R.string.profile_prompt);
            btnGoToLogin.setVisibility(View.VISIBLE);
            
            cardProfilePrompt.setCardBackgroundColor(getResources().getColor(R.color.white));
            tvProfileStatus.setTextColor(getResources().getColor(R.color.car_primary));
            
            View icon = cardProfilePrompt.findViewById(R.id.ivProfileIcon);
            if (icon instanceof ImageView) {
                ((ImageView)icon).setColorFilter(getResources().getColor(R.color.car_primary));
            }
        }
        invalidateOptionsMenu();
    }

    private void checkReminders() {
        if (garageList == null || garageList.isEmpty()) return;
        
        int issuesCount = 0;
        for (Vehicle v : garageList) {
            if (v.getInsuranceExpiry() != null || v.getTechnicalInspectionExpiry() != null) {
                issuesCount++;
            }
        }
        
        if (issuesCount > 0) {
            Toast.makeText(this, "Имате " + issuesCount + " автомобила, изискващи внимание в гаража.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.parts_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterParts(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterParts(String category) {
        if (allPartsList.isEmpty()) return;

        if (category.equals(getString(R.string.parts_categories_all)) || category.equals("Всички части")) {
            partsAdapter.updateList(allPartsList);
        } else {
            List<Part> filteredList = new ArrayList<>();
            for (Part part : allPartsList) {
                if (part.getCategory().equalsIgnoreCase(category)) {
                    filteredList.add(part);
                }
            }
            partsAdapter.updateList(filteredList);
        }
    }

    private void setupRecyclerViews() {
        historyAdapter = new HistoryAdapter(historyList, this);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(historyAdapter);
        rvHistory.setNestedScrollingEnabled(false);

        partsAdapter = new PartsAdapter(new ArrayList<>(), this);
        rvParts.setLayoutManager(new LinearLayoutManager(this));
        rvParts.setAdapter(partsAdapter);
        rvParts.setNestedScrollingEnabled(false);

        garageAdapter = new GarageAdapter(garageList, this);
        rvGarage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvGarage.setAdapter(garageAdapter);
        rvGarage.setNestedScrollingEnabled(true);
    }

    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedHistory = prefs.getString(KEY_HISTORY, "");
        if (savedHistory.isEmpty()) {
            historyList.clear();
        } else {
            historyList.clear();
            historyList.addAll(Arrays.asList(savedHistory.split(",")));
        }
        if (historyAdapter != null) historyAdapter.notifyDataSetChanged();
    }

    private void saveHistory(String vin) {
        if (historyList.contains(vin)) {
            historyList.remove(vin);
        }
        historyList.add(0, vin);
        if (historyList.size() > 5) {
            historyList.remove(5);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < historyList.size(); i++) {
            sb.append(historyList.get(i));
            if (i < historyList.size() - 1) {
                sb.append(",");
            }
        }

        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_HISTORY, sb.toString())
                .apply();
        
        if (historyAdapter != null) historyAdapter.notifyDataSetChanged();
    }

    private void loadGarage() {
        loadGarageLocally();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains("logged_user")) {
            String userJson = prefs.getString("logged_user", "");
            com.example.carsearhapp.model.User user = new com.google.gson.Gson().fromJson(userJson, com.example.carsearhapp.model.User.class);
            
            apiService.getGarage(user.getId()).enqueue(new Callback<List<Vehicle>>() {
                @Override
                public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        List<Vehicle> serverList = response.body();
                        for (Vehicle sv : serverList) {
                            boolean exists = false;
                            for (Vehicle lv : garageList) {
                                if (lv.getVin().equals(sv.getVin())) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                garageList.add(sv);
                            }
                        }
                        if (garageAdapter != null) garageAdapter.notifyDataSetChanged();
                        updateProfilePrompt();
                    }
                }

                @Override
                public void onFailure(Call<List<Vehicle>> call, Throwable t) {}
            });
        }
    }

    private void loadGarageLocally() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(getGarageKey(), "");
        garageList.clear();
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Vehicle>>() {}.getType();
            garageList.addAll(gson.fromJson(json, type));
        }
        if (garageAdapter != null) garageAdapter.notifyDataSetChanged();
    }

    private void saveToGarage(Vehicle vehicle) {
        if (vehicle == null) return;
        saveLocally(vehicle);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains("logged_user")) {
            String userJson = prefs.getString("logged_user", "");
            com.example.carsearhapp.model.User user = new com.google.gson.Gson().fromJson(userJson, com.example.carsearhapp.model.User.class);
            
            apiService.addToGarage(user.getId(), vehicle.getVin()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {}
                @Override
                public void onFailure(Call<Void> call, Throwable t) {}
            });
        }
    }

    private void saveLocally(Vehicle vehicle) {
        for (Vehicle v : garageList) {
            if (v.getVin().equals(vehicle.getVin())) {
                Toast.makeText(this, "Вече е в гаража", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        garageList.add(0, vehicle);
        updateGaragePrefs();
        if (garageAdapter != null) garageAdapter.notifyDataSetChanged();
        updateProfilePrompt();
        Toast.makeText(this, getString(R.string.msg_saved_garage), Toast.LENGTH_SHORT).show();
    }

    private void updateGaragePrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = new Gson().toJson(garageList);
        prefs.edit().putString(getGarageKey(), json).apply();
    }

    private void searchVehicle(String vin) {
        progressBar.setVisibility(View.VISIBLE);
        cardResult.setVisibility(View.GONE);
        layoutParts.setVisibility(View.GONE);
        btnSearch.setEnabled(false);

        apiService.getVehicle(vin).enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                progressBar.setVisibility(View.GONE);
                btnSearch.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    currentVehicle = response.body();
                    if (currentVehicle.getVin() == null || currentVehicle.getVin().isEmpty()) {
                        currentVehicle.setVin(vin);
                    }
                    displayVehicle(currentVehicle);
                    saveHistory(vin);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.error_not_found), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSearch.setEnabled(true);
                Toast.makeText(MainActivity.this, getString(R.string.error_network) + ": " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findParts(Vehicle vehicle) {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getParts(vehicle.getMake(), vehicle.getModel(), vehicle.getYear()).enqueue(new Callback<List<Part>>() {
            @Override
            public void onResponse(Call<List<Part>> call, Response<List<Part>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    allPartsList.clear();
                    allPartsList.addAll(response.body());
                    spinnerCategories.setSelection(0);
                    partsAdapter.updateList(allPartsList);
                    layoutParts.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "Няма намерени части", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Part>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Грешка при търсене на части", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayVehicle(Vehicle vehicle) {
        String result = getString(R.string.label_make) + vehicle.getMake() + "\n" +
                getString(R.string.label_model) + vehicle.getModel() + "\n" +
                getString(R.string.label_year) + vehicle.getYear() + "\n" +
                getString(R.string.label_engine) + vehicle.getEngine() + "\n" +
                getString(R.string.label_fuel) + vehicle.getFuel();

        tvResult.setText(result);
        loadVehicleLogo(vehicle.getMake());
        cardResult.setVisibility(View.VISIBLE);
    }

    private void loadVehicleLogo(String make) {
        String logoUrl;
        switch (make.toLowerCase()) {
            case "bmw": logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/BMW.svg/2048px-BMW.svg.png"; break;
            case "audi": logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Audi-Logo_2016.svg/2560px-Audi-Logo_2016.svg.png"; break;
            case "volkswagen": logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Volkswagen_logo_2019.svg/2048px-Volkswagen_logo_2019.svg.png"; break;
            case "renault": logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Renault_2021_Textless_Logo.svg/2048px-Renault_2021_Textless_Logo.svg.png"; break;
            default: logoUrl = "https://cdn-icons-png.flaticon.com/512/741/741407.png"; break;
        }
        Glide.with(this)
                .load(logoUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivVehicleLogo);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_history) {
            clearHistory();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.action_contact) {
            startActivity(new Intent(this, ContactActivity.class));
            return true;
        } else if (id == R.id.action_nearby_services) {
            openNearbyServices("car service");
            return true;
        } else if (id == R.id.action_gas_stations) {
            openNearbyServices("gas station");
            return true;
        } else if (id == R.id.action_ev_stations) {
            openNearbyServices("electric vehicle charging station");
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = prefs.contains("logged_user");
        
        if (menu.findItem(R.id.action_logout) != null) {
            menu.findItem(R.id.action_logout).setVisible(isLoggedIn);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void logout() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().remove("logged_user").apply();
        
        currentVehicle = null;
        etVin.setText("");
        cardResult.setVisibility(View.GONE);
        layoutParts.setVisibility(View.GONE);
        allPartsList.clear();
        partsAdapter.updateList(new ArrayList<>());

        garageList.clear();
        if (garageAdapter != null) {
            garageAdapter.notifyDataSetChanged();
        }

        updateProfilePrompt();
        loadGarage();
        Toast.makeText(this, "Излязохте успешно", Toast.LENGTH_SHORT).show();
    }

    private void openNearbyServices(String query) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/" + query));
            startActivity(browserIntent);
        }
    }

    private void clearHistory() {
        historyList.clear();
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().remove(KEY_HISTORY).apply();
        if (historyAdapter != null) historyAdapter.notifyDataSetChanged();

        garageList.clear();
        updateGaragePrefs();
        if (garageAdapter != null) garageAdapter.notifyDataSetChanged();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains("logged_user")) {
            String userJson = prefs.getString("logged_user", "");
            com.example.carsearhapp.model.User user = new com.google.gson.Gson().fromJson(userJson, com.example.carsearhapp.model.User.class);
            
            apiService.clearGarage(user.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {}
                @Override
                public void onFailure(Call<Void> call, Throwable t) {}
            });
        }

        Toast.makeText(this, "Всички данни са изчистени", Toast.LENGTH_SHORT).show();
    }

    private void showAboutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.action_about)
                .setMessage(R.string.about_message)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onHistoryClick(String vin) {
        etVin.setText(vin);
        searchVehicle(vin);
    }

    @Override
    public void onPartClick(Part part) {
        Intent intent = new Intent(this, PartDetailActivity.class);
        intent.putExtra("part", part);
        startActivity(intent);
    }

    @Override
    public void onGarageClick(Vehicle vehicle) {
        etVin.setText(vehicle.getVin());
        searchVehicle(vehicle.getVin());
    }

    @Override
    public void onServiceLogClick(Vehicle vehicle, int position) {
        Intent intent = new Intent(this, ServiceLogActivity.class);
        intent.putExtra("vehicle", vehicle);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onRemoveClick(int position) {
        if (position < 0 || position >= garageList.size()) return;
        
        Vehicle vehicle = garageList.get(position);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        if (prefs.contains("logged_user")) {
            String userJson = prefs.getString("logged_user", "");
            com.example.carsearhapp.model.User user = new com.google.gson.Gson().fromJson(userJson, com.example.carsearhapp.model.User.class);
            
            apiService.removeFromGarage(user.getId(), vehicle.getVin()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        garageList.remove(position);
                        garageAdapter.notifyItemRemoved(position);
                        updateGaragePrefs();
                        updateProfilePrompt();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Грешка при триене от сървъра", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            garageList.remove(position);
            garageAdapter.notifyItemRemoved(position);
            updateGaragePrefs();
            updateProfilePrompt();
        }
    }
}