package com.litemobiletools.unitconverter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        AutoCompleteTextView unitDropdown = findViewById(R.id.unitDropdown);

        // Load array
                String[] units = getResources().getStringArray(R.array.length_units);

        // Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
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
}