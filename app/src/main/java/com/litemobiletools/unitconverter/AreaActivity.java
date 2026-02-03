package com.litemobiletools.unitconverter;

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

public class AreaActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    TextView tvMeter, tvKm, tvCm, tvMm, tvMicro, tvNano,
            tvMile, tvYard, tvFoot, tvInch, tvLy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_area);
        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);
        String[] units = {
                "Square Meter",
                "Square Kilometer",
                "Square Centimeter",
                "Square Millimeter",
                "Square Micrometer",
                "Hectare",
                "Square Mile",
                "Square Yard",
                "Square Foot",
                "Square Inch",
                "Acre"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        unitDropdown.setAdapter(adapter);
        unitDropdown2.setAdapter(adapter);

        unitDropdown.setText("Square Meter", false);
        unitDropdown2.setText("Square Kilometer", false);

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
        tvLy = findViewById(R.id.tvLyValue);
    }

    private double convert(double value, String from, String to) {

        // Step 1: Convert FROM source unit TO square meter (base unit)
        double squareMeterValue;

        switch (from) {
            case "Square Kilometer":   squareMeterValue = value * 1_000_000; break;
            case "Square Centimeter":  squareMeterValue = value / 10_000; break;
            case "Square Millimeter":  squareMeterValue = value / 1_000_000; break;
            case "Square Micrometer":  squareMeterValue = value / 1_000_000_000_000L; break;
            case "Hectare":            squareMeterValue = value * 10_000; break;
            case "Square Mile":        squareMeterValue = value * 2_589_988.110336; break;
            case "Square Yard":        squareMeterValue = value * 0.83612736; break;
            case "Square Foot":        squareMeterValue = value * 0.09290304; break;
            case "Square Inch":        squareMeterValue = value * 0.00064516; break;
            case "Acre":               squareMeterValue = value * 4046.8564224; break;
            case "Square Meter":
            default:
                squareMeterValue = value;
                break;
        }

        // Step 2: Convert FROM square meter TO target unit
        switch (to) {
            case "Square Kilometer":   return squareMeterValue / 1_000_000;
            case "Square Centimeter":  return squareMeterValue * 10_000;
            case "Square Millimeter":  return squareMeterValue * 1_000_000;
            case "Square Micrometer":  return squareMeterValue * 1_000_000_000_000L;
            case "Hectare":            return squareMeterValue / 10_000;
            case "Square Mile":        return squareMeterValue / 2_589_988.110336;
            case "Square Yard":        return squareMeterValue / 0.83612736;
            case "Square Foot":        return squareMeterValue / 0.09290304;
            case "Square Inch":        return squareMeterValue / 0.00064516;
            case "Acre":               return squareMeterValue / 4046.8564224;
            case "Square Meter":
            default:
                return squareMeterValue;
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
        tvMeter.setText(format(convert(value, fromUnit, "Square Meter")));
        tvKm.setText(format(convert(value, fromUnit, "Square Kilometer")));
        tvCm.setText(format(convert(value, fromUnit, "Square Centimeter")));
        tvMm.setText(format(convert(value, fromUnit, "Square Millimeter")));
        tvMicro.setText(format(convert(value, fromUnit, "Square Micrometer")));
        tvNano.setText(format(convert(value, fromUnit, "Hectare")));
        tvMile.setText(format(convert(value, fromUnit, "Square Mile")));
        tvYard.setText(format(convert(value, fromUnit, "Square Yard")));
        tvFoot.setText(format(convert(value, fromUnit, "Square Foot")));
        tvInch.setText(format(convert(value, fromUnit, "Square Inch")));
        tvLy.setText(format(convert(value, fromUnit, "Acre")));
    }
    private String format(double value) {
        if (value == 0) return "0";
        if (Math.abs(value) < 0.0001 || Math.abs(value) > 1_000_000)
            return String.format("%.6e", value);
        return String.format("%.6f", value);
    }
}