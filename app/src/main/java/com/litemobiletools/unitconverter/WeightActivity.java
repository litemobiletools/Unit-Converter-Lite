package com.litemobiletools.unitconverter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class WeightActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    TextView tvMeter, tvKm, tvCm, tvMm, tvMicro, tvNano,
            tvMile, tvYard, tvFoot, tvInch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weight);

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);
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
    }

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

}