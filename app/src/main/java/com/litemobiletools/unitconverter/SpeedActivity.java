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

public class SpeedActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    // TextView declarations
    private TextView tvMps, tvKph, tvMph, tvMphr, tvMpmin, tvKpmin, tvKps;
    private TextView tvCph, tvCpmin, tvCps, tvMmph, tvMmpmin, tvMmps;
    private TextView tvFph, tvFpmin, tvFps, tvYph, tvYpmin, tvYps, tvMipmin, tvMips;
    private TextView tvKnot, tvKnotUK, tvLight, tvCosmic1, tvCosmic2, tvCosmic3, tvEarth;
    private TextView tvWater, tvSea, tvMach20, tvMachSI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_speed);

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);

        String[] units = {
                "Meter per second (m/s)",
                "Kilometer per hour (km/h)",
                "Mile per hour (mi/h)",
                "Meter per hour (m/h)",
                "Meter per minute (m/min)",
                "Kilometer per minute (km/min)",
                "Kilometer per second (km/s)",

                "Centimeter per hour (cm/h)",
                "Centimeter per minute (cm/min)",
                "Centimeter per second (cm/s)",

                "Millimeter per hour (mm/h)",
                "Millimeter per minute (mm/min)",
                "Millimeter per second (mm/s)",

                "Foot per hour (ft/h)",
                "Foot per minute (ft/min)",
                "Foot per second (ft/s)",

                "Yard per hour (yd/h)",
                "Yard per minute (yd/min)",
                "Yard per second (yd/s)",

                "Mile per minute (mi/min)",
                "Mile per second (mi/s)",

                "Knot (kt)",
                "Knot (UK)",

                "Velocity of light (vacuum)",
                "Cosmic velocity – First",
                "Cosmic velocity – Second",
                "Cosmic velocity – Third",
                "Earth’s velocity",

                "Speed of sound in pure water",
                "Speed of sound in sea water",
                "Mach (20°C, 1 atm)",
                "Mach (SI standard)"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        unitDropdown.setAdapter(adapter);
        unitDropdown2.setAdapter(adapter);

        unitDropdown.setText("Meter per second (m/s)", false);
        unitDropdown2.setText("Kilometer per hour (km/h)", false);

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
        // Metric
        tvMps = findViewById(R.id.tvMpsValue);
        tvKph = findViewById(R.id.tvKphValue);
        tvMph = findViewById(R.id.tvMphValue);
        tvMphr = findViewById(R.id.tvMphrValue);
        tvMpmin = findViewById(R.id.tvMpminValue);
        tvKpmin = findViewById(R.id.tvKpminValue);
        tvKps = findViewById(R.id.tvKpsValue);

        // Centimeters & Millimeters
        tvCph = findViewById(R.id.tvCphValue);
        tvCpmin = findViewById(R.id.tvCpminValue);
        tvCps = findViewById(R.id.tvCpsValue);
        tvMmph = findViewById(R.id.tvMmphValue);
        tvMmpmin = findViewById(R.id.tvMmpminValue);
        tvMmps = findViewById(R.id.tvMmpsValue);

        // Imperial
        tvFph = findViewById(R.id.tvFphValue);
        tvFpmin = findViewById(R.id.tvFpminValue);
        tvFps = findViewById(R.id.tvFpsValue);
        tvYph = findViewById(R.id.tvYphValue);
        tvYpmin = findViewById(R.id.tvYpminValue);
        tvYps = findViewById(R.id.tvYpsValue);
        tvMipmin = findViewById(R.id.tvMipminValue);
        tvMips = findViewById(R.id.tvMipsValue);

        // Special & Cosmic
        tvKnot = findViewById(R.id.tvKnotValue);
        tvKnotUK = findViewById(R.id.tvKnotUKValue);
        tvLight = findViewById(R.id.tvLightValue);
        tvCosmic1 = findViewById(R.id.tvCosmic1Value);
        tvCosmic2 = findViewById(R.id.tvCosmic2Value);
        tvCosmic3 = findViewById(R.id.tvCosmic3Value);
        tvEarth = findViewById(R.id.tvEarthValue);

        // Sound
        tvWater = findViewById(R.id.tvWaterValue);
        tvSea = findViewById(R.id.tvSeaValue);
        tvMach20 = findViewById(R.id.tvMach20Value);
        tvMachSI = findViewById(R.id.tvMachSIValue);
    }

    private double convert(double value, String from, String to) {
        double mps; // base unit: meter per second (m/s)

        // ---------- FROM unit → m/s ----------
        switch (from) {

            case "Meter per second (m/s)": mps = value; break;

            case "Kilometer per hour (km/h)": mps = value / 3.6; break;
            case "Mile per hour (mi/h)": mps = value * 0.44704; break;

            case "Meter per hour (m/h)": mps = value / 3600.0; break;
            case "Meter per minute (m/min)": mps = value / 60.0; break;

            case "Kilometer per minute (km/min)": mps = value * 1000.0 / 60.0; break;
            case "Kilometer per second (km/s)": mps = value * 1000.0; break;

            case "Centimeter per hour (cm/h)": mps = value / 360000.0; break;
            case "Centimeter per minute (cm/min)": mps = value / 6000.0; break;
            case "Centimeter per second (cm/s)": mps = value / 100.0; break;

            case "Millimeter per hour (mm/h)": mps = value / 3600000.0; break;
            case "Millimeter per minute (mm/min)": mps = value / 60000.0; break;
            case "Millimeter per second (mm/s)": mps = value / 1000.0; break;

            case "Foot per hour (ft/h)": mps = value * 0.3048 / 3600.0; break;
            case "Foot per minute (ft/min)": mps = value * 0.3048 / 60.0; break;
            case "Foot per second (ft/s)": mps = value * 0.3048; break;

            case "Yard per hour (yd/h)": mps = value * 0.9144 / 3600.0; break;
            case "Yard per minute (yd/min)": mps = value * 0.9144 / 60.0; break;
            case "Yard per second (yd/s)": mps = value * 0.9144; break;

            case "Mile per minute (mi/min)": mps = value * 1609.344 / 60.0; break;
            case "Mile per second (mi/s)": mps = value * 1609.344; break;

            case "Knot (kt)": mps = value * 0.514444; break;
            case "Knot (UK)": mps = value * 0.514773; break;

            case "Velocity of light (vacuum)": mps = value * 299792458.0; break;

            case "Cosmic velocity – First": mps = value * 7900.0; break;
            case "Cosmic velocity – Second": mps = value * 11200.0; break;
            case "Cosmic velocity – Third": mps = value * 16700.0; break;

            case "Earth’s velocity": mps = value * 29780.0; break;

            case "Speed of sound in pure water": mps = value * 1482.0; break;
            case "Speed of sound in sea water": mps = value * 1531.0; break;

            case "Mach (20°C, 1 atm)": mps = value * 343.0; break;
            case "Mach (SI standard)": mps = value * 340.29; break;

            default: mps = value; break;
        }

        // ---------- m/s → TO unit ----------
        switch (to) {

            case "Meter per second (m/s)": return mps;

            case "Kilometer per hour (km/h)": return mps * 3.6;
            case "Mile per hour (mi/h)": return mps / 0.44704;

            case "Meter per hour (m/h)": return mps * 3600.0;
            case "Meter per minute (m/min)": return mps * 60.0;

            case "Kilometer per minute (km/min)": return mps * 60.0 / 1000.0;
            case "Kilometer per second (km/s)": return mps / 1000.0;

            case "Centimeter per hour (cm/h)": return mps * 360000.0;
            case "Centimeter per minute (cm/min)": return mps * 6000.0;
            case "Centimeter per second (cm/s)": return mps * 100.0;

            case "Millimeter per hour (mm/h)": return mps * 3600000.0;
            case "Millimeter per minute (mm/min)": return mps * 60000.0;
            case "Millimeter per second (mm/s)": return mps * 1000.0;

            case "Foot per hour (ft/h)": return mps * 3600.0 / 0.3048;
            case "Foot per minute (ft/min)": return mps * 60.0 / 0.3048;
            case "Foot per second (ft/s)": return mps / 0.3048;

            case "Yard per hour (yd/h)": return mps * 3600.0 / 0.9144;
            case "Yard per minute (yd/min)": return mps * 60.0 / 0.9144;
            case "Yard per second (yd/s)": return mps / 0.9144;

            case "Mile per minute (mi/min)": return mps * 60.0 / 1609.344;
            case "Mile per second (mi/s)": return mps / 1609.344;

            case "Knot (kt)": return mps / 0.514444;
            case "Knot (UK)": return mps / 0.514773;

            case "Velocity of light (vacuum)": return mps / 299792458.0;

            case "Cosmic velocity – First": return mps / 7900.0;
            case "Cosmic velocity – Second": return mps / 11200.0;
            case "Cosmic velocity – Third": return mps / 16700.0;

            case "Earth’s velocity": return mps / 29780.0;

            case "Speed of sound in pure water": return mps / 1482.0;
            case "Speed of sound in sea water": return mps / 1531.0;

            case "Mach (20°C, 1 atm)": return mps / 343.0;
            case "Mach (SI standard)": return mps / 340.29;

            default: return mps;
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
        // Metric & General
        if (tvMps != null) tvMps.setText(format(convert(value, fromUnit, "Meter per second (m/s)")));
        if (tvKph != null) tvKph.setText(format(convert(value, fromUnit, "Kilometer per hour (km/h)")));
        if (tvMph != null) tvMph.setText(format(convert(value, fromUnit, "Mile per hour (mi/h)")));
        if (tvMphr != null) tvMphr.setText(format(convert(value, fromUnit, "Meter per hour (m/h)")));
        if (tvMpmin != null) tvMpmin.setText(format(convert(value, fromUnit, "Meter per minute (m/min)")));
        if (tvKpmin != null) tvKpmin.setText(format(convert(value, fromUnit, "Kilometer per minute (km/min)")));
        if (tvKps != null) tvKps.setText(format(convert(value, fromUnit, "Kilometer per second (km/s)")));

        // Centimeters & Millimeters
        if (tvCph != null) tvCph.setText(format(convert(value, fromUnit, "Centimeter per hour (cm/h)")));
        if (tvCpmin != null) tvCpmin.setText(format(convert(value, fromUnit, "Centimeter per minute (cm/min)")));
        if (tvCps != null) tvCps.setText(format(convert(value, fromUnit, "Centimeter per second (cm/s)")));
        if (tvMmph != null) tvMmph.setText(format(convert(value, fromUnit, "Millimeter per hour (mm/h)")));
        if (tvMmpmin != null) tvMmpmin.setText(format(convert(value, fromUnit, "Millimeter per minute (mm/min)")));
        if (tvMmps != null) tvMmps.setText(format(convert(value, fromUnit, "Millimeter per second (mm/s)")));

        // Imperial (Feet, Yards, Miles)
        if (tvFph != null) tvFph.setText(format(convert(value, fromUnit, "Foot per hour (ft/h)")));
        if (tvFpmin != null) tvFpmin.setText(format(convert(value, fromUnit, "Foot per minute (ft/min)")));
        if (tvFps != null) tvFps.setText(format(convert(value, fromUnit, "Foot per second (ft/s)")));
        if (tvYph != null) tvYph.setText(format(convert(value, fromUnit, "Yard per hour (yd/h)")));
        if (tvYpmin != null) tvYpmin.setText(format(convert(value, fromUnit, "Yard per minute (yd/min)")));
        if (tvYps != null) tvYps.setText(format(convert(value, fromUnit, "Yard per second (yd/s)")));
        if (tvMipmin != null) tvMipmin.setText(format(convert(value, fromUnit, "Mile per minute (mi/min)")));
        if (tvMips != null) tvMips.setText(format(convert(value, fromUnit, "Mile per second (mi/s)")));

        // Nautical & Cosmic
        if (tvKnot != null) tvKnot.setText(format(convert(value, fromUnit, "Knot (kt)")));
        if (tvKnotUK != null) tvKnotUK.setText(format(convert(value, fromUnit, "Knot (UK)")));
        if (tvLight != null) tvLight.setText(format(convert(value, fromUnit, "Velocity of light (vacuum)")));
        if (tvCosmic1 != null) tvCosmic1.setText(format(convert(value, fromUnit, "Cosmic velocity – First")));
        if (tvCosmic2 != null) tvCosmic2.setText(format(convert(value, fromUnit, "Cosmic velocity – Second")));
        if (tvCosmic3 != null) tvCosmic3.setText(format(convert(value, fromUnit, "Cosmic velocity – Third")));
        if (tvEarth != null) tvEarth.setText(format(convert(value, fromUnit, "Earth’s velocity")));

        // Sound
        if (tvWater != null) tvWater.setText(format(convert(value, fromUnit, "Speed of sound in pure water")));
        if (tvSea != null) tvSea.setText(format(convert(value, fromUnit, "Speed of sound in sea water")));
        if (tvMach20 != null) tvMach20.setText(format(convert(value, fromUnit, "Mach (20°C, 1 atm)")));
        if (tvMachSI != null) tvMachSI.setText(format(convert(value, fromUnit, "Mach (SI standard)")));
    }

    private String format(double value) {
        if (value == 0) return "0";
        if (Math.abs(value) < 0.0001 || Math.abs(value) > 1_000_000)
            return String.format("%.4e", value);
        return String.format("%.4f", value);
    }
}
