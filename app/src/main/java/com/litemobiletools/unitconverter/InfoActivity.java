package com.litemobiletools.unitconverter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.litemobiletools.unitconverter.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private TextView tvAppVersion;
    private Button btnShareApp;
    private TextView tvPrivacyPolicyLink;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);


        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);

        // Enable the Up button (back arrow)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Handle navigation icon click (back arrow)
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize views
        tvAppVersion = findViewById(R.id.tv_app_version);
        btnShareApp = findViewById(R.id.btn_share_app);
        tvPrivacyPolicyLink = findViewById(R.id.tv_privacy_policy_link);

        // Set app version dynamically (optional, good practice)
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvAppVersion.setText("Version: " + versionName);
        } catch (Exception e) {
            e.printStackTrace();
            tvAppVersion.setText("Version: N/A");
        }

        // Set up Share App button click listener
        btnShareApp.setOnClickListener(v -> shareApp());

        // Set up Privacy Policy link click listener
        tvPrivacyPolicyLink.setOnClickListener(v -> openPrivacyPolicyLink());
    }

    // Handle menu item clicks if you reused top_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle the back arrow click
            return true;
        }
        // Add other menu item handling if you have them in top_menu
        // For example, if you have action_settings in top_menu:
        /*
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(this, "Settings clicked from Info!", Toast.LENGTH_SHORT).show();
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Check out this awesome Unit Converter app: https://play.google.com/store/apps/details?id=" + getPackageName(); // Replace with your actual Play Store link
        String shareSubject = "Unit Converter App";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share Unit Converter"));
    }

    private void openPrivacyPolicyLink() {
        String privacyPolicyUrl = getString(R.string.privacy_policy_url);
        if (!privacyPolicyUrl.isEmpty()) { // Check if URL is actually set
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not open link. No browser found.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Privacy Policy link is not configured.", Toast.LENGTH_SHORT).show();
        }
    }
}