package com.litemobiletools.unitconverter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
                "Millimeter"
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







        AutoCompleteTextView unitDropdown = findViewById(R.id.unitDropdown);
        // Load array
        String[] units2 = getResources().getStringArray(R.array.length_units);
        // Adapter
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,   // custom layout
                units
        );
        unitDropdown.setAdapter(adapter);
        // Show dropdown on click
        unitDropdown.setOnClickListener(v -> unitDropdown.showDropDown());
        // Handle selection
        unitDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedUnit = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Selected: " + selectedUnit, Toast.LENGTH_SHORT).show();
        });
    }
    private double convert(double value, String from, String to) {

        // Convert to base (meter)
        double meterValue;

        switch (from) {
            case "Kilometer": meterValue = value * 1000; break;
            case "Centimeter": meterValue = value / 100; break;
            case "Millimeter": meterValue = value / 1000; break;
            default: meterValue = value;
        }

        // Convert from meter to target
        switch (to) {
            case "Kilometer": return meterValue / 1000;
            case "Centimeter": return meterValue * 100;
            case "Millimeter": return meterValue * 1000;
            default: return meterValue;
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


}