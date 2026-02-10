package com.litemobiletools.unitconverter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);
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







//        AutoCompleteTextView unitDropdown = findViewById(R.id.unitDropdown);
//        // Load array
//        String[] units2 = getResources().getStringArray(R.array.length_units);
//        // Adapter
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
//                this,
//                R.layout.dropdown_item,   // custom layout
//                units
//        );
//        unitDropdown.setAdapter(adapter);
//        // Show dropdown on click
//        unitDropdown.setOnClickListener(v -> unitDropdown.showDropDown());
//        // Handle selection
//        unitDropdown.setOnItemClickListener((parent, view, position, id) -> {
//            String selectedUnit = parent.getItemAtPosition(position).toString();
//            Toast.makeText(this, "Selected: " + selectedUnit, Toast.LENGTH_SHORT).show();
//        });
    }
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
//        String input = etValue.getText().toString().trim();
//        if (input.isEmpty()) {
//            tvResult.setText("0");
//            return;
//        }
//
//        double value = Double.parseDouble(input);
//        String from = unitDropdown.getText().toString();
//        String to = unitDropdown2.getText().toString();
//
//        double result = convert(value, from, to);
//
//        tvResult.setText(String.format("%.4f %s", result, to));

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
}