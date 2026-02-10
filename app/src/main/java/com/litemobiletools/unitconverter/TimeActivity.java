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

public class TimeActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    TextView tvPicosecond, tvNanosecond, tvMicrosecond, tvMillisecond, tvSecond, tvMinute, tvHour, tvDay, tvWeek, tvMonth, tvYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_time);

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);

        String[] units = {
                "Picosecond", "Nanosecond", "Microsecond", "Millisecond", "Second", "Minute", "Hour", "Day", "Week", "Month", "Year"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        unitDropdown.setAdapter(adapter);
        unitDropdown2.setAdapter(adapter);

        unitDropdown.setText("Hour", false);
        unitDropdown2.setText("Minute", false);

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
        tvPicosecond = findViewById(R.id.tvPicosecondValue);
        tvNanosecond = findViewById(R.id.tvNanosecondValue);
        tvMicrosecond = findViewById(R.id.tvMicrosecondValue);
        tvMillisecond = findViewById(R.id.tvMillisecondValue);
        tvSecond = findViewById(R.id.tvSecondValue);
        tvMinute = findViewById(R.id.tvMinuteValue);
        tvHour = findViewById(R.id.tvHourValue);
        tvDay = findViewById(R.id.tvDayValue);
        tvWeek = findViewById(R.id.tvWeekValue);
        tvMonth = findViewById(R.id.tvMonthValue);
        tvYear = findViewById(R.id.tvYearValue);
    }

    private double convert(double value, String from, String to) {
        double seconds;
        // Base unit: Second
        switch (from) {
            case "Picosecond":  seconds = value / 1_000_000_000_000.0; break;
            case "Nanosecond":  seconds = value / 1_000_000_000.0; break;
            case "Microsecond": seconds = value / 1_000_000.0; break;
            case "Millisecond": seconds = value / 1000.0; break;
            case "Minute":      seconds = value * 60.0; break;
            case "Hour":        seconds = value * 3600.0; break;
            case "Day":         seconds = value * 86400.0; break;
            case "Week":        seconds = value * 604800.0; break;
            case "Month":       seconds = value * 2592000.0; break; // 30 days
            case "Year":        seconds = value * 31536000.0; break; // 365 days
            case "Second":
            default:            seconds = value; break;
        }

        switch (to) {
            case "Picosecond":  return seconds * 1_000_000_000_000.0;
            case "Nanosecond":  return seconds * 1_000_000_000.0;
            case "Microsecond": return seconds * 1_000_000.0;
            case "Millisecond": return seconds * 1000.0;
            case "Minute":      return seconds / 60.0;
            case "Hour":        return seconds / 3600.0;
            case "Day":         return seconds / 86400.0;
            case "Week":        return seconds / 604800.0;
            case "Month":       return seconds / 2592000.0;
            case "Year":        return seconds / 31536000.0;
            case "Second":
            default:            return seconds;
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
        if (tvPicosecond != null) tvPicosecond.setText(format(convert(value, fromUnit, "Picosecond")));
        if (tvNanosecond != null) tvNanosecond.setText(format(convert(value, fromUnit, "Nanosecond")));
        if (tvMicrosecond != null) tvMicrosecond.setText(format(convert(value, fromUnit, "Microsecond")));
        if (tvMillisecond != null) tvMillisecond.setText(format(convert(value, fromUnit, "Millisecond")));
        if (tvSecond != null) tvSecond.setText(format(convert(value, fromUnit, "Second")));
        if (tvMinute != null) tvMinute.setText(format(convert(value, fromUnit, "Minute")));
        if (tvHour != null) tvHour.setText(format(convert(value, fromUnit, "Hour")));
        if (tvDay != null) tvDay.setText(format(convert(value, fromUnit, "Day")));
        if (tvWeek != null) tvWeek.setText(format(convert(value, fromUnit, "Week")));
        if (tvMonth != null) tvMonth.setText(format(convert(value, fromUnit, "Month")));
        if (tvYear != null) tvYear.setText(format(convert(value, fromUnit, "Year")));
    }

    private String format(double value) {
        if (value == 0) return "0";
        if (Math.abs(value) < 0.0001 || Math.abs(value) > 1_000_000)
            return String.format("%.4e", value);
        return String.format("%.4f", value);
    }
}
