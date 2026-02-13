package com.litemobiletools.unitconverter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeightActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    TextView tvMeter, tvKm, tvCm, tvMm, tvMicro, tvNano,
            tvMile, tvYard, tvFoot, tvInch;
    private MaterialToolbar toolbar; // Declare MaterialToolbar

    LinearLayout recentConversionsContainer;
    List<String> recentConversions = new ArrayList<>();
    private static final String PREFS_NAME = "UnitConverterPrefs";
    private static final String RECENT_CONVERSIONS_KEY = "recentConversions";
    private static final int MAX_RECENT_CONVERSIONS = 10;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weight);

        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Handle navigation icon click (e.g., the back arrow)
        toolbar.setNavigationOnClickListener(v -> {
            //onBackPressed(); // Go back to the previous activity
            // Or finish() to close the current activity:
            finish();
        });

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);

        recentConversionsContainer = findViewById(R.id.recent_conversions_container);
        String[] units = {
                "Kilogram",
                "Gram",
                "Milligram",
                "Metric Ton",
                "Long Ton",
                "Short Ton",
                "Pound",
                "Ounce",
                "Carrat",
                "Atomic Mass Unit",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        unitDropdown.setAdapter(adapter);
        unitDropdown2.setAdapter(adapter);

        unitDropdown.setText("Kilogram", false);
        unitDropdown2.setText("Gram", false);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        };

        etValue.addTextChangedListener(watcher);
        unitDropdown.setOnItemClickListener((a, b, c, d) -> calculate());
        unitDropdown2.setOnItemClickListener((a, b, c, d) -> calculate());

        btnSwap.setOnClickListener(v -> {
            String temp = unitDropdown.getText().toString();
            unitDropdown.setText(unitDropdown2.getText().toString(), false);
            unitDropdown2.setText(temp, false);
            calculate();
        });

        unitDropdown.setOnClickListener(v -> unitDropdown.showDropDown());
        unitDropdown2.setOnClickListener(v -> unitDropdown2.showDropDown());
        unitDropdown.setShowSoftInputOnFocus(false);
        unitDropdown2.setShowSoftInputOnFocus(false);


        tvMeter = findViewById(R.id.tvMeterValue);
        tvKm = findViewById(R.id.tvKmValue);
        tvCm = findViewById(R.id.tvCmValue);
        tvMm = findViewById(R.id.tvMmValue);
        tvMicro = findViewById(R.id.tvMicroValue);
        tvNano = findViewById(R.id.tvNanoValue);
        tvMile = findViewById(R.id.tvMileValue);
        tvYard = findViewById(R.id.tvYardValue);
        tvFoot = findViewById(R.id.tvFootValue);
        tvInch = findViewById(R.id.tvInchValue);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Already on home, do nothing or refresh
                finish();
                return true;
            } else if (itemId == R.id.nav_convert) {
                // Start the conversion activity (e.g., LengthActivity or a generic conversion screen)
                String temp = unitDropdown.getText().toString();
                unitDropdown.setText(unitDropdown2.getText().toString(), false);
                unitDropdown2.setText(temp, false);
                calculate();

                return true;
            } else if (itemId == R.id.nav_favorite) {
                // Start Favorites Activity
                startActivity(new Intent(this, InfoActivity.class));
                return true;
            }
            return false;
        });
        loadRecentConversions();
    }
    // --- Top Menu Methods ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            Toast.makeText(this, "Share clicked!", Toast.LENGTH_SHORT).show();
            // Implement your share logic here
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = "Check out this amazing Weight Converter app: https://play.google.com/store/apps/details?id=" + getPackageName(); // Replace with your actual Play Store link
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // --- End Top Menu Methods ---

    private double convert(double value, String from, String to) {

        // Step 1: Convert FROM source unit TO kilogram (base unit)
        double kgValue;

        switch (from) {
            case "Gram":
                kgValue = value / 1000;
                break;
            case "Milligram":
                kgValue = value / 1_000_000;
                break;
            case "Metric Ton":
                kgValue = value * 1000;
                break;
            case "Long Ton":
                kgValue = value * 1016.0469088;
                break;
            case "Short Ton":
                kgValue = value * 907.18474;
                break;
            case "Pound":
                kgValue = value * 0.45359237;
                break;
            case "Ounce":
                kgValue = value * 0.0283495231;
                break;
            case "Carrat":
                kgValue = value * 0.0002;
                break;
            case "Atomic Mass Unit":
                kgValue = value * 1.66053906660e-27;
                break;
            case "Kilogram":
            default:
                kgValue = value;
                break;
        }

        // Step 2: Convert FROM kilogram TO target unit
        switch (to) {
            case "Gram":
                return kgValue * 1000;
            case "Milligram":
                return kgValue * 1_000_000;
            case "Metric Ton":
                return kgValue / 1000;
            case "Long Ton":
                return kgValue / 1016.0469088;
            case "Short Ton":
                return kgValue / 907.18474;
            case "Pound":
                return kgValue / 0.45359237;
            case "Ounce":
                return kgValue / 0.0283495231;
            case "Carrat":
                return kgValue / 0.0002;
            case "Atomic Mass Unit":
                return kgValue / 1.66053906660e-27;
            case "Kilogram":
            default:
                return kgValue;
        }
    }


    private void calculate() {
        if (etValue.getText() == null) return;

        String input = etValue.getText().toString().trim();
        if (input.isEmpty()) {
            tvResult.setText("0");
            return;
        }

        String from = unitDropdown.getText().toString();
        String to = unitDropdown2.getText().toString();

        if (from.isEmpty() || to.isEmpty()) return;

        double value = Double.parseDouble(input);
        double result = convert(value, from, to);

        tvResult.setText(String.format("%.4f %s", result, to));

        // Update all rows
        updateAll(value, from);
        //sharedpreface
        String conversionString = String.format("%.4f %s → %.4f %s", value, from, result, to);
        addRecentConversion(conversionString);
        saveRecentConversions();
        displayRecentConversions();
    }


    private void updateAll(double value, String fromUnit) {

        tvMeter.setText(format(convert(value, fromUnit, "Kilogram")));
        tvKm.setText(format(convert(value, fromUnit, "Gram")));
        tvCm.setText(format(convert(value, fromUnit, "Milligram")));
        tvMm.setText(format(convert(value, fromUnit, "Metric Ton")));
        tvMicro.setText(format(convert(value, fromUnit, "Long Ton")));
        tvNano.setText(format(convert(value, fromUnit, "Short Ton")));
        tvMile.setText(format(convert(value, fromUnit, "Pound")));
        tvYard.setText(format(convert(value, fromUnit, "Ounce")));
        tvFoot.setText(format(convert(value, fromUnit, "Carrat")));
        tvInch.setText(format(convert(value, fromUnit, "Atomic Mass Unit")));
    }

    private String format(double value) {
        if (value == 0) return "0";
        if (Math.abs(value) < 0.0001 || Math.abs(value) > 1_000_000)
            return String.format("%.6e", value);
        return String.format("%.6f", value);
    }
    // sharedprefacen
    private void addRecentConversion(String conversion) {
        // Add to the beginning of the list
        recentConversions.add(0, conversion);
        // Keep only the latest MAX_RECENT_CONVERSIONS
        while (recentConversions.size() > MAX_RECENT_CONVERSIONS) {
            recentConversions.remove(recentConversions.size() - 1);
        }
    }

    private void saveRecentConversions() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Convert the list to a comma-separated string
        String conversionsString = TextUtils.join("||", recentConversions);
        editor.putString(RECENT_CONVERSIONS_KEY, conversionsString);
        editor.apply();
    }

    private void loadRecentConversions() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String conversionsString = prefs.getString(RECENT_CONVERSIONS_KEY, "");
        if (!conversionsString.isEmpty()) {
            recentConversions = new ArrayList<>(Arrays.asList(conversionsString.split("\\|\\|")));
        } else {
            recentConversions = new ArrayList<>();
        }
        displayRecentConversions();
    }

    private void displayRecentConversions() {
        recentConversionsContainer.removeAllViews(); // Clear existing views
        if (recentConversions.isEmpty()) {
            TextView noConversions = new TextView(this);
            noConversions.setText("No recent conversions.");
            noConversions.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small);
            recentConversionsContainer.addView(noConversions);
        } else {
            for (int i = 0; i < recentConversions.size(); i++) {
                String conversion = recentConversions.get(i);
                TextView tv = new TextView(this);
                tv.setText(conversion);
                tv.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small);
                tv.setPadding(0, 8, 0, 8);
                recentConversionsContainer.addView(tv);

                if (i < recentConversions.size() - 1) {
                    View divider = new View(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    layoutParams.setMargins(0, 6, 0, 6);
                    divider.setLayoutParams(layoutParams);
                    divider.setBackgroundColor(getColor(R.color.colorOnSurface)); // Assuming you have this color or define it
                    recentConversionsContainer.addView(divider);
                }
            }
        }
    }

}