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

public class CurrencyActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    // TextView declarations
    private TextView tvUsd, tvEur, tvGbp, tvJpy, tvAud, tvCad, tvChf, tvCny;
    private TextView tvInr, tvSgd, tvHkd, tvKrw, tvThb, tvMyr, tvIdr, tvPhp;
    private TextView tvSek, tvNok, tvTry, tvRub, tvAed, tvSar, tvIls, tvKwd;
    private TextView tvMxn, tvBrl, tvZar, tvEgp, tvNzd, tvPkr, tvBdt, tvVnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_currency);

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);

        String[] units = {
                "USD - US Dollar",
                "EUR - Euro",
                "GBP - British Pound",
                "JPY - Japanese Yen",
                "AUD - Australian Dollar",
                "CAD - Canadian Dollar",
                "CHF - Swiss Franc",
                "CNY - Chinese Yuan",
                "INR - Indian Rupee",
                "SGD - Singapore Dollar",
                "HKD - Hong Kong Dollar",
                "KRW - South Korean Won",
                "THB - Thai Baht",
                "MYR - Malaysian Ringgit",
                "IDR - Indonesian Rupiah",
                "PHP - Philippine Peso",
                "SEK - Swedish Krona",
                "NOK - Norwegian Krone",
                "TRY - Turkish Lira",
                "RUB - Russian Ruble",
                "AED - UAE Dirham",
                "SAR - Saudi Riyal",
                "ILS - Israeli Shekel",
                "KWD - Kuwaiti Dinar",
                "MXN - Mexican Peso",
                "BRL - Brazilian Real",
                "ZAR - S. African Rand",
                "EGP - Egyptian Pound",
                "NZD - NZ Dollar",
                "PKR - Pakistani Rupee",
                "BDT - Bangladesh Taka",
                "VND - Vietnam Dong"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        unitDropdown.setAdapter(adapter);
        unitDropdown2.setAdapter(adapter);

        unitDropdown.setText("USD - US Dollar", false);
        unitDropdown2.setText("USD - US Dollar", false);

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
        // Major
        tvUsd = findViewById(R.id.tvUsdValue);
        tvEur = findViewById(R.id.tvEurValue);
        tvGbp = findViewById(R.id.tvGbpValue);
        tvJpy = findViewById(R.id.tvJpyValue);
        tvAud = findViewById(R.id.tvAudValue);
        tvCad = findViewById(R.id.tvCadValue);
        tvChf = findViewById(R.id.tvChfValue);
        tvCny = findViewById(R.id.tvCnyValue);

        // Asia & Pacific
        tvInr = findViewById(R.id.tvInrValue);
        tvSgd = findViewById(R.id.tvSgdValue);
        tvHkd = findViewById(R.id.tvHkdValue);
        tvKrw = findViewById(R.id.tvKrwValue);
        tvThb = findViewById(R.id.tvThbValue);
        tvMyr = findViewById(R.id.tvMyrValue);
        tvIdr = findViewById(R.id.tvIdrValue);
        tvPhp = findViewById(R.id.tvPhpValue);

        // Europe & Middle East
        tvSek = findViewById(R.id.tvSekValue);
        tvNok = findViewById(R.id.tvNokValue);
        tvTry = findViewById(R.id.tvTryValue);
        tvRub = findViewById(R.id.tvRubValue);
        tvAed = findViewById(R.id.tvAedValue);
        tvSar = findViewById(R.id.tvSarValue);
        tvIls = findViewById(R.id.tvIlsValue);
        tvKwd = findViewById(R.id.tvKwdValue);

        // Americas & Africa & Others
        tvMxn = findViewById(R.id.tvMxnValue);
        tvBrl = findViewById(R.id.tvBrlValue);
        tvZar = findViewById(R.id.tvZarValue);
        tvEgp = findViewById(R.id.tvEgpValue);
        tvNzd = findViewById(R.id.tvNzdValue);
        tvPkr = findViewById(R.id.tvPkrValue);
        tvBdt = findViewById(R.id.tvBdtValue);
        tvVnd = findViewById(R.id.tvVndValue);
    }

    private double convert(double value, String fromUnit, String toUnit) {
        // Get rate relative to 1 USD
        double fromRate = getRate(fromUnit);
        double toRate = getRate(toUnit);

        // Formula: (Value / FromRate) * ToRate
        return (value / fromRate) * toRate;
    }

    private double getRate(String unit) {
        switch (unit) {
            case "USD - US Dollar": return 1.0;
            case "EUR - Euro": return 0.92;
            case "GBP - British Pound": return 0.79;
            case "JPY - Japanese Yen": return 150.12;
            case "AUD - Australian Dollar": return 1.53;
            case "CAD - Canadian Dollar": return 1.35;
            case "CHF - Swiss Franc": return 0.88;
            case "CNY - Chinese Yuan": return 7.19;
            case "INR - Indian Rupee": return 82.95;
            case "SGD - Singapore Dollar": return 1.34;
            case "HKD - Hong Kong Dollar": return 7.82;
            case "KRW - South Korean Won": return 1332.50;
            case "THB - Thai Baht": return 35.85;
            case "MYR - Malaysian Ringgit": return 4.77;
            case "IDR - Indonesian Rupiah": return 15645.0;
            case "PHP - Philippine Peso": return 56.05;
            case "SEK - Swedish Krona": return 10.42;
            case "NOK - Norwegian Krone": return 10.55;
            case "TRY - Turkish Lira": return 31.02;
            case "RUB - Russian Ruble": return 92.50;
            case "AED - UAE Dirham": return 3.67;
            case "SAR - Saudi Riyal": return 3.75;
            case "ILS - Israeli Shekel": return 3.62;
            case "KWD - Kuwaiti Dinar": return 0.31;
            case "MXN - Mexican Peso": return 17.05;
            case "BRL - Brazilian Real": return 4.97;
            case "ZAR - S. African Rand": return 19.15;
            case "EGP - Egyptian Pound": return 30.90;
            case "NZD - NZ Dollar": return 1.62;
            case "PKR - Pakistani Rupee": return 279.50;
            case "BDT - Bangladesh Taka": return 110.0;
            case "VND - Vietnam Dong": return 24650.0;
            default: return 1.0;
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
        // Major
        if (tvUsd != null) tvUsd.setText(format(convert(value, fromUnit, "USD - US Dollar")));
        if (tvEur != null) tvEur.setText(format(convert(value, fromUnit, "EUR - Euro")));
        if (tvGbp != null) tvGbp.setText(format(convert(value, fromUnit, "GBP - British Pound")));
        if (tvJpy != null) tvJpy.setText(format(convert(value, fromUnit, "JPY - Japanese Yen")));
        if (tvAud != null) tvAud.setText(format(convert(value, fromUnit, "AUD - Australian Dollar")));
        if (tvCad != null) tvCad.setText(format(convert(value, fromUnit, "CAD - Canadian Dollar")));
        if (tvChf != null) tvChf.setText(format(convert(value, fromUnit, "CHF - Swiss Franc")));
        if (tvCny != null) tvCny.setText(format(convert(value, fromUnit, "CNY - Chinese Yuan")));

        // Asia & Pacific
        if (tvInr != null) tvInr.setText(format(convert(value, fromUnit, "INR - Indian Rupee")));
        if (tvSgd != null) tvSgd.setText(format(convert(value, fromUnit, "SGD - Singapore Dollar")));
        if (tvHkd != null) tvHkd.setText(format(convert(value, fromUnit, "HKD - Hong Kong Dollar")));
        if (tvKrw != null) tvKrw.setText(format(convert(value, fromUnit, "KRW - South Korean Won")));
        if (tvThb != null) tvThb.setText(format(convert(value, fromUnit, "THB - Thai Baht")));
        if (tvMyr != null) tvMyr.setText(format(convert(value, fromUnit, "MYR - Malaysian Ringgit")));
        if (tvIdr != null) tvIdr.setText(format(convert(value, fromUnit, "IDR - Indonesian Rupiah")));
        if (tvPhp != null) tvPhp.setText(format(convert(value, fromUnit, "PHP - Philippine Peso")));

        // Europe & Middle East
        if (tvSek != null) tvSek.setText(format(convert(value, fromUnit, "SEK - Swedish Krona")));
        if (tvNok != null) tvNok.setText(format(convert(value, fromUnit, "NOK - Norwegian Krone")));
        if (tvTry != null) tvTry.setText(format(convert(value, fromUnit, "TRY - Turkish Lira")));
        if (tvRub != null) tvRub.setText(format(convert(value, fromUnit, "RUB - Russian Ruble")));
        if (tvAed != null) tvAed.setText(format(convert(value, fromUnit, "AED - UAE Dirham")));
        if (tvSar != null) tvSar.setText(format(convert(value, fromUnit, "SAR - Saudi Riyal")));
        if (tvIls != null) tvIls.setText(format(convert(value, fromUnit, "ILS - Israeli Shekel")));
        if (tvKwd != null) tvKwd.setText(format(convert(value, fromUnit, "KWD - Kuwaiti Dinar")));

        // Others
        if (tvMxn != null) tvMxn.setText(format(convert(value, fromUnit, "MXN - Mexican Peso")));
        if (tvBrl != null) tvBrl.setText(format(convert(value, fromUnit, "BRL - Brazilian Real")));
        if (tvZar != null) tvZar.setText(format(convert(value, fromUnit, "ZAR - S. African Rand")));
        if (tvEgp != null) tvEgp.setText(format(convert(value, fromUnit, "EGP - Egyptian Pound")));
        if (tvNzd != null) tvNzd.setText(format(convert(value, fromUnit, "NZD - NZ Dollar")));
        if (tvPkr != null) tvPkr.setText(format(convert(value, fromUnit, "PKR - Pakistani Rupee")));
        if (tvBdt != null) tvBdt.setText(format(convert(value, fromUnit, "BDT - Bangladesh Taka")));
        if (tvVnd != null) tvVnd.setText(format(convert(value, fromUnit, "VND - Vietnam Dong")));
    }

    private String format(double value) {
        if (value == 0) return "0";
        if (Math.abs(value) < 0.0001 || Math.abs(value) > 1_000_000)
            return String.format("%.4e", value);
        return String.format("%.4f", value);
    }
}
