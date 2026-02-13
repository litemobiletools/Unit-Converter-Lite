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
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

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
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);
        recentConversionsContainer = findViewById(R.id.recent_conversions_container);
        String[] units = {
                "Meter",
                "Kilometer",
                "Centimeter",
                "Millimeter",
                "Micrometer",
                "Nanometer",
                "Mile",
                "Yard",
                "Foot",
                "Inch",
                "Light Year"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        unitDropdown.setAdapter(adapter);
        unitDropdown2.setAdapter(adapter);

        unitDropdown.setText("Meter", false);
        unitDropdown2.setText("Kilometer", false);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Already on home, do nothing or refresh
                Toast.makeText(MainActivity.this, "Already on home", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_convert) {
                // Start the conversion activity (e.g., LengthActivity or a generic conversion screen)
                String temp = unitDropdown.getText().toString();
                unitDropdown.setText(unitDropdown2.getText().toString(), false);
                unitDropdown2.setText(temp, false);
                calculate();

                return true;
            } else if (itemId == R.id.nav_favorite) {
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
            String shareBody = "Check out this awesome Unit Converter app: https://play.google.com/store/apps/details?id=" + getPackageName(); // Replace with your actual Play Store link
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // --- End Top Menu Methods ---

    private double convert(double value, String from, String to) {

        // Step 1: Convert FROM source unit TO meter (base unit)
        double meterValue;

        switch (from) {
            case "Kilometer":   meterValue = value * 1000; break;
            case "Centimeter":  meterValue = value / 100; break;
            case "Millimeter":  meterValue = value / 1000; break;
            case "Micrometer":  meterValue = value / 1_000_000; break;
            case "Nanometer":   meterValue = value / 1_000_000_000; break;
            case "Mile":        meterValue = value * 1609.344; break;
            case "Yard":        meterValue = value * 0.9144; break;
            case "Foot":        meterValue = value * 0.3048; break;
            case "Inch":        meterValue = value * 0.0254; break;
            case "Light Year":  meterValue = value * 9.4607e15; break;
            case "Meter":
            default:
                meterValue = value;
                break;
        }

        // Step 2: Convert FROM meter TO target unit
        switch (to) {
            case "Kilometer":   return meterValue / 1000;
            case "Centimeter":  return meterValue * 100;
            case "Millimeter":  return meterValue * 1000;
            case "Micrometer":  return meterValue * 1_000_000;
            case "Nanometer":   return meterValue * 1_000_000_000;
            case "Mile":        return meterValue / 1609.344;
            case "Yard":        return meterValue / 0.9144;
            case "Foot":        return meterValue / 0.3048;
            case "Inch":        return meterValue / 0.0254;
            case "Light Year":  return meterValue / 9.4607e15;
            case "Meter":
            default:
                return meterValue;
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
        String conversionString = String.format("%.4f %s → %.4f %s", value, from, result, to);

        tvResult.setText(String.format("%.4f %s", result, to));

        addRecentConversion(conversionString);
        saveRecentConversions();
        displayRecentConversions();
    }
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
    public void leanth(View view) {
        startActivity(new Intent(this, LengthActivity.class));
    }
    public void weight(View view) {
        startActivity(new Intent(this, WeightActivity.class));
    }
    public void temparature(View view) {
        startActivity(new Intent(this, TemperatureActivity.class));
    }
    public void area(View view) {
        startActivity(new Intent(this, AreaActivity.class));
    }
    public void volume(View view) {
        startActivity(new Intent(this, VolumeActivity.class));
    }
    public void time(View view) {
        startActivity(new Intent(this, TimeActivity.class));
    }
    public void speed(View view) {
        startActivity(new Intent(this, SpeedActivity.class));
    }
    public void currency(View view) {
        startActivity(new Intent(this, CurrencyActivity.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        // This refreshes the list every time you return to this screen
        loadRecentConversions();
    }
}