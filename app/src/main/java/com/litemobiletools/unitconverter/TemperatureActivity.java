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

public class TemperatureActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    TextView tvMeter, tvKm, tvCm;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_temperature);

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);
        String[] units = {
                "Celsius",
                "Kelvin",
                "Fahrenheit"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        unitDropdown.setAdapter(adapter);
        unitDropdown2.setAdapter(adapter);

        unitDropdown.setText("Celsius", false);
        unitDropdown2.setText("Kelvin", false);

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
    }

    private double convert(double value, String from, String to) {

        // Step 1: Convert FROM source unit TO Celsius (base unit)
        double celsiusValue;

        switch (from) {
            case "Kelvin":
                celsiusValue = value - 273.15;
                break;
            case "Fahrenheit":
                celsiusValue = (value - 32) * 5 / 9;
                break;
            case "Celsius":
            default:
                celsiusValue = value;
                break;
        }

        // Step 2: Convert FROM Celsius TO target unit
        switch (to) {
            case "Kelvin":
                return celsiusValue + 273.15;
            case "Fahrenheit":
                return (celsiusValue * 9 / 5) + 32;
            case "Celsius":
            default:
                return celsiusValue;
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
        tvMeter.setText(format(convert(value, fromUnit, "Celsius")));
        tvKm.setText(format(convert(value, fromUnit, "Kelvin")));
        tvCm.setText(format(convert(value, fromUnit, "Fahrenheit")));
    }

    private String format(double value) {
        if (value == 0) return "0";
        if (Math.abs(value) < 0.0001 || Math.abs(value) > 1_000_000)
            return String.format("%.6e", value);
        return String.format("%.6f", value);
    }

}