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

import com.google.android.material.textfield.TextInputEditText;

public class VolumeActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    TextView tvCubicMeter, tvCubicKm, tvCubicCm, tvCubicMm, tvLiter, tvMl,
            tvUsGallon, tvUsQuart, tvUsPint, tvUsCup, tvUsFluidOunce, tvUsTableSpoon, tvUsTeaSpoon,
            tvImpGallon, tvImpQuart, tvImpPint, tvImpFluidOunce, tvImpTableSpoon, tvImpTeaSpoon,
            tvCubicMile, tvCubicYard, tvCubicFoot, tvCubicInch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_volume);

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);

        String[] units = {
                "Cubic Meter", "Cubic Kilometer", "Cubic Centimeter", "Cubic Millimeter",
                "Liter", "Milliliter", "US Gallon", "US Quart", "US Pint", "US Cup",
                "US Fluid Ounce", "US Table Spoon", "US Tea Spoon", "Imperial Gallon",
                "Imperial Quart", "Imperial Pint", "Imperial Fluid Ounce",
                "Imperial Table Spoon", "Imperial Tea Spoon", "Cubic Mile",
                "Cubic Yard", "Cubic Foot", "Cubic Inch"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        unitDropdown.setAdapter(adapter);
        unitDropdown2.setAdapter(adapter);

        unitDropdown.setText("Liter", false);
        unitDropdown2.setText("Milliliter", false);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { calculate(); }
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

        // Initialize all unit display TextViews
        tvCubicMeter = findViewById(R.id.tvCubicMeterValue);
        tvCubicKm = findViewById(R.id.tvCubicKmValue);
        tvCubicCm = findViewById(R.id.tvCubicCmValue);
        tvCubicMm = findViewById(R.id.tvCubicMmValue);
        tvLiter = findViewById(R.id.tvLiterValue);
        tvMl = findViewById(R.id.tvMlValue);
        tvUsGallon = findViewById(R.id.tvUsGallonValue);
        tvUsQuart = findViewById(R.id.tvUsQuartValue);
        tvUsPint = findViewById(R.id.tvUsPintValue);
        tvUsCup = findViewById(R.id.tvUsCupValue);
        tvUsFluidOunce = findViewById(R.id.tvUsFluidOunceValue);
        tvUsTableSpoon = findViewById(R.id.tvUsTableSpoonValue);
        tvUsTeaSpoon = findViewById(R.id.tvUsTeaSpoonValue);
        tvImpGallon = findViewById(R.id.tvImpGallonValue);
        tvImpQuart = findViewById(R.id.tvImpQuartValue);
        tvImpPint = findViewById(R.id.tvImpPintValue);
        tvImpFluidOunce = findViewById(R.id.tvImpFluidOunceValue);
        tvImpTableSpoon = findViewById(R.id.tvImpTableSpoonValue);
        tvImpTeaSpoon = findViewById(R.id.tvImpTeaSpoonValue);
        tvCubicMile = findViewById(R.id.tvCubicMileValue);
        tvCubicYard = findViewById(R.id.tvCubicYardValue);
        tvCubicFoot = findViewById(R.id.tvCubicFootValue);
        tvCubicInch = findViewById(R.id.tvCubicInchValue);
    }

    private double convert(double value, String from, String to) {
        double literValue;
        switch (from) {
            case "Cubic Meter":        literValue = value * 1000.0; break;
            case "Cubic Kilometer":    literValue = value * 1e12; break;
            case "Cubic Centimeter":   literValue = value / 1000.0; break;
            case "Cubic Millimeter":   literValue = value / 1_000_000.0; break;
            case "Milliliter":         literValue = value / 1000.0; break;
            case "US Gallon":          literValue = value * 3.785411784; break;
            case "US Quart":           literValue = value * 0.946352946; break;
            case "US Pint":            literValue = value * 0.473176473; break;
            case "US Cup":             literValue = value * 0.2365882365; break;
            case "US Fluid Ounce":     literValue = value * 0.0295735296; break;
            case "US Table Spoon":     literValue = value * 0.0147867648; break;
            case "US Tea Spoon":       literValue = value * 0.0049289216; break;
            case "Imperial Gallon":    literValue = value * 4.54609; break;
            case "Imperial Quart":     literValue = value * 1.1365225; break;
            case "Imperial Pint":      literValue = value * 0.56826125; break;
            case "Imperial Fluid Ounce": literValue = value * 0.0284130625; break;
            case "Imperial Table Spoon": literValue = value * 0.0177581714; break;
            case "Imperial Tea Spoon":   literValue = value * 0.0059193905; break;
            case "Cubic Mile":         literValue = value * 4.16818183e12; break;
            case "Cubic Yard":         literValue = value * 764.55485798; break;
            case "Cubic Foot":         literValue = value * 28.316846592; break;
            case "Cubic Inch":         literValue = value * 0.016387064; break;
            case "Liter":
            default:                   literValue = value; break;
        }

        switch (to) {
            case "Cubic Meter":        return literValue / 1000.0;
            case "Cubic Kilometer":    return literValue / 1e12;
            case "Cubic Centimeter":   return literValue * 1000.0;
            case "Cubic Millimeter":   return literValue * 1_000_000.0;
            case "Milliliter":         return literValue * 1000.0;
            case "US Gallon":          return literValue / 3.785411784;
            case "US Quart":           return literValue / 0.946352946;
            case "US Pint":            return literValue / 0.473176473;
            case "US Cup":             return literValue / 0.2365882365;
            case "US Fluid Ounce":     return literValue / 0.0295735296;
            case "US Table Spoon":     return literValue / 0.0147867648;
            case "US Tea Spoon":       return literValue / 0.0049289216;
            case "Imperial Gallon":    return literValue / 4.54609;
            case "Imperial Quart":     return literValue / 1.1365225;
            case "Imperial Pint":      return literValue / 0.56826125;
            case "Imperial Fluid Ounce": return literValue / 0.0284130625;
            case "Imperial Table Spoon": return literValue / 0.0177581714;
            case "Imperial Tea Spoon":   return literValue / 0.0059193905;
            case "Cubic Mile":         return literValue / 4.16818183e12;
            case "Cubic Yard":         return literValue / 764.55485798;
            case "Cubic Foot":         return literValue / 28.316846592;
            case "Cubic Inch":         return literValue / 0.016387064;
            case "Liter":
            default:                   return literValue;
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
        tvResult.setText(String.format("%.4f", result));

        updateAll(value, from);
    }

    private void updateAll(double value, String fromUnit) {
        if (tvCubicMeter != null) tvCubicMeter.setText(format(convert(value, fromUnit, "Cubic Meter")));
        if (tvCubicKm != null) tvCubicKm.setText(format(convert(value, fromUnit, "Cubic Kilometer")));
        if (tvCubicCm != null) tvCubicCm.setText(format(convert(value, fromUnit, "Cubic Centimeter")));
        if (tvCubicMm != null) tvCubicMm.setText(format(convert(value, fromUnit, "Cubic Millimeter")));
        if (tvLiter != null) tvLiter.setText(format(convert(value, fromUnit, "Liter")));
        if (tvMl != null) tvMl.setText(format(convert(value, fromUnit, "Milliliter")));
        if (tvUsGallon != null) tvUsGallon.setText(format(convert(value, fromUnit, "US Gallon")));
        if (tvUsQuart != null) tvUsQuart.setText(format(convert(value, fromUnit, "US Quart")));
        if (tvUsPint != null) tvUsPint.setText(format(convert(value, fromUnit, "US Pint")));
        if (tvUsCup != null) tvUsCup.setText(format(convert(value, fromUnit, "US Cup")));
        if (tvUsFluidOunce != null) tvUsFluidOunce.setText(format(convert(value, fromUnit, "US Fluid Ounce")));
        if (tvUsTableSpoon != null) tvUsTableSpoon.setText(format(convert(value, fromUnit, "US Table Spoon")));
        if (tvUsTeaSpoon != null) tvUsTeaSpoon.setText(format(convert(value, fromUnit, "US Tea Spoon")));
        if (tvImpGallon != null) tvImpGallon.setText(format(convert(value, fromUnit, "Imperial Gallon")));
        if (tvImpQuart != null) tvImpQuart.setText(format(convert(value, fromUnit, "Imperial Quart")));
        if (tvImpPint != null) tvImpPint.setText(format(convert(value, fromUnit, "Imperial Pint")));
        if (tvImpFluidOunce != null) tvImpFluidOunce.setText(format(convert(value, fromUnit, "Imperial Fluid Ounce")));
        if (tvImpTableSpoon != null) tvImpTableSpoon.setText(format(convert(value, fromUnit, "Imperial Table Spoon")));
        if (tvImpTeaSpoon != null) tvImpTeaSpoon.setText(format(convert(value, fromUnit, "Imperial Tea Spoon")));
        if (tvCubicMile != null) tvCubicMile.setText(format(convert(value, fromUnit, "Cubic Mile")));
        if (tvCubicYard != null) tvCubicYard.setText(format(convert(value, fromUnit, "Cubic Yard")));
        if (tvCubicFoot != null) tvCubicFoot.setText(format(convert(value, fromUnit, "Cubic Foot")));
        if (tvCubicInch != null) tvCubicInch.setText(format(convert(value, fromUnit, "Cubic Inch")));
    }

    private String format(double value) {
        if (value == 0) return "0";
        if (Math.abs(value) < 0.0001 || Math.abs(value) > 1_000_000)
            return String.format("%.4e", value);
        return String.format("%.4f", value);
    }
}
