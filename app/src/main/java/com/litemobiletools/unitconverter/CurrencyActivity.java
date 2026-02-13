package com.litemobiletools.unitconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CurrencyActivity extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, unitDropdown2;
    TextInputEditText etValue;
    TextView tvResult;
    ImageButton btnSwap;

    // TextView declarations
    // In your Activity or Fragment class (outside any method)
    private TextView
            // Major Global Currencies
            tvUsd, tvEur, tvGbp, tvJpy, tvAud, tvCad, tvChf,

    // Asia & Pacific
    tvCny, tvHkd, tvInr, tvIdr, tvKrw, tvMyr, tvNzd, tvPhp, tvSgd, tvThb, tvVnd,tvPkr,

    // Europe & Middle East
    tvAed, tvIls, tvKwd, tvSar, tvTry,

    // Americas & Africa
    tvBrl, tvEgp, tvMxn, tvZar,

    // Other Currencies (all remaining)
    tvAfn, tvAll, tvAmd, tvAng, tvAoa, tvArs, tvAwg, tvAzn, tvBam, tvBbd,
            tvBdt, tvBgn, tvBhd, tvBif, tvBmd, tvBnd, tvBob, tvBsd, tvBtn, tvBwp,
            tvByn, tvBzd, tvCdf, tvClf, tvClp, tvCnh, tvCop, tvCrc, tvCup, tvCve,
            tvCzk, tvDjf, tvDkk, tvDop, tvDzd, tvErn, tvEtb, tvFjd, tvFkp, tvFok,
            tvGel, tvGgp, tvGhs, tvGip, tvGmd, tvGnf, tvGtq, tvGyd, tvHnl, tvHrk,
            tvHtg, tvHuf, tvIqd, tvIrr, tvIsk, tvJep, tvJmd, tvJod, tvKes, tvKgs,
            tvKhr, tvKid, tvKmf, tvKyd, tvKzt, tvLak, tvLbp, tvLkr, tvLrd, tvLsl,
            tvLyd, tvMad, tvMdl, tvMga, tvMkd, tvMmk, tvMnt, tvMop, tvMru, tvMur,
            tvMvr, tvMwk, tvMzn, tvNad, tvNgn, tvNio, tvNok, tvNpr, tvOmr, tvPab,
            tvPen, tvPgk, tvPln, tvPyg, tvQar, tvRon, tvRsd, tvRub, tvRwf, tvSbd,
            tvScr, tvSdg, tvSek, tvShp, tvSle, tvSll, tvSos, tvSrd, tvSsp, tvStn,
            tvSyp, tvSzl, tvTjs, tvTmt, tvTnd, tvTop, tvTtd, tvTvd, tvTwd, tvTzs,
            tvUah, tvUgx, tvUyu, tvUzs, tvVes, tvVuv, tvWst, tvXaf, tvXcd, tvXcg,
            tvXdr, tvXof, tvXpf, tvYer, tvZmw, tvZwg, tvZwl;

    private MaterialToolbar toolbar; // Declare MaterialToolbar


    private Map<String, Double> liveRates = new HashMap<>();
    private SharedPreferences prefs;

    TextView txtUpdated;
    public ImageButton updateIDmanual;

    LinearLayout recentConversionsContainer;
    List<String> recentConversions = new ArrayList<>();
    private static final String PREFS_NAME = "UnitConverterPrefs";
    private static final String RECENT_CONVERSIONS_KEY = "recentConversions";
    private static final int MAX_RECENT_CONVERSIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_currency);
        //load data api
        prefs = getSharedPreferences("rates_cache", MODE_PRIVATE);
        loadCachedRates();

        updateIDmanual = findViewById(R.id.updateIDmanual);

        long last = prefs.getLong("last_fetch",0);
        if(System.currentTimeMillis() - last > 86400000){
            fetchRatesFromServer();
            prefs.edit().putLong("last_fetch",System.currentTimeMillis()).apply();

            updateIDmanual.setVisibility(View.GONE);
        }

        updateIDmanual.setOnClickListener(v -> {
            fetchRatesFromServer();
            prefs.edit().putLong("last_fetch",System.currentTimeMillis()).apply();
        });


        //load data api
        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Handle navigation icon click (e.g., the back arrow)
        toolbar.setNavigationOnClickListener(v -> {
            //onBackPressed(); // Go back to the previous activity
            // Or finish() to close the current activity:
            finish();
        });

        unitDropdown = findViewById(R.id.unitDropdown);
        unitDropdown2 = findViewById(R.id.unitDropdown2);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        btnSwap = findViewById(R.id.btnSwap);

        recentConversionsContainer = findViewById(R.id.recent_conversions_container);

        String[] units = {
                "USD - US Dollar",
                "AED - UAE Dirham",
                "AFN - Afghan Afghani",
                "ALL - Albanian Lek",
                "AMD - Armenian Dram",
                "ANG - Netherlands Antillean Guilder",
                "AOA - Angolan Kwanza",
                "ARS - Argentine Peso",
                "AUD - Australian Dollar",
                "AWG - Aruban Florin",
                "AZN - Azerbaijani Manat",
                "BAM - Bosnia-Herzegovina Convertible Mark",
                "BBD - Barbadian Dollar",
                "BDT - Bangladeshi Taka",
                "BGN - Bulgarian Lev",
                "BHD - Bahraini Dinar",
                "BIF - Burundian Franc",
                "BMD - Bermudian Dollar",
                "BND - Brunei Dollar",
                "BOB - Bolivian Boliviano",
                "BRL - Brazilian Real",
                "BSD - Bahamian Dollar",
                "BTN - Bhutanese Ngultrum",
                "BWP - Botswana Pula",
                "BYN - Belarusian Ruble",
                "BZD - Belize Dollar",
                "CAD - Canadian Dollar",
                "CDF - Congolese Franc",
                "CHF - Swiss Franc",
                "CLF - Unidad de Fomento",
                "CLP - Chilean Peso",
                "CNH - Chinese Yuan (Offshore)",
                "CNY - Chinese Yuan",
                "COP - Colombian Peso",
                "CRC - Costa Rican Colón",
                "CUP - Cuban Peso",
                "CVE - Cape Verdean Escudo",
                "CZK - Czech Koruna",
                "DJF - Djiboutian Franc",
                "DKK - Danish Krone",
                "DOP - Dominican Peso",
                "DZD - Algerian Dinar",
                "EGP - Egyptian Pound",
                "ERN - Eritrean Nakfa",
                "ETB - Ethiopian Birr",
                "EUR - Euro",
                "FJD - Fijian Dollar",
                "FKP - Falkland Islands Pound",
                "FOK - Faroese Króna",
                "GBP - British Pound Sterling",
                "GEL - Georgian Lari",
                "GGP - Guernsey Pound",
                "GHS - Ghanaian Cedi",
                "GIP - Gibraltar Pound",
                "GMD - Gambian Dalasi",
                "GNF - Guinean Franc",
                "GTQ - Guatemalan Quetzal",
                "GYD - Guyanaese Dollar",
                "HKD - Hong Kong Dollar",
                "HNL - Honduran Lempira",
                "HRK - Croatian Kuna",
                "HTG - Haitian Gourde",
                "HUF - Hungarian Forint",
                "IDR - Indonesian Rupiah",
                "ILS - Israeli New Shekel",
                "IMP - Isle of Man Pound",
                "INR - Indian Rupee",
                "IQD - Iraqi Dinar",
                "IRR - Iranian Rial",
                "ISK - Icelandic Króna",
                "JEP - Jersey Pound",
                "JMD - Jamaican Dollar",
                "JOD - Jordanian Dinar",
                "JPY - Japanese Yen",
                "KES - Kenyan Shilling",
                "KGS - Kyrgyzstani Som",
                "KHR - Cambodian Riel",
                "KID - Kiribati Dollar",
                "KMF - Comorian Franc",
                "KRW - South Korean Won",
                "KWD - Kuwaiti Dinar",
                "KYD - Cayman Islands Dollar",
                "KZT - Kazakhstani Tenge",
                "LAK - Laotian Kip",
                "LBP - Lebanese Pound",
                "LKR - Sri Lankan Rupee",
                "LRD - Liberian Dollar",
                "LSL - Lesotho Loti",
                "LYD - Libyan Dinar",
                "MAD - Moroccan Dirham",
                "MDL - Moldovan Leu",
                "MGA - Malagasy Ariary",
                "MKD - Macedonian Denar",
                "MMK - Myanmar Kyat",
                "MNT - Mongolian Tugrik",
                "MOP - Macanese Pataca",
                "MRU - Mauritanian Ouguiya",
                "MUR - Mauritian Rupee",
                "MVR - Maldivian Rufiyaa",
                "MWK - Malawian Kwacha",
                "MXN - Mexican Peso",
                "MYR - Malaysian Ringgit",
                "MZN - Mozambican Metical",
                "NAD - Namibian Dollar",
                "NGN - Nigerian Naira",
                "NIO - Nicaraguan Córdoba",
                "NOK - Norwegian Krone",
                "NPR - Nepalese Rupee",
                "NZD - New Zealand Dollar",
                "OMR - Omani Rial",
                "PAB - Panamanian Balboa",
                "PEN - Peruvian Sol",
                "PGK - Papua New Guinean Kina",
                "PHP - Philippine Peso",
                "PKR - Pakistani Rupee",
                "PLN - Polish Złoty",
                "PYG - Paraguayan Guarani",
                "QAR - Qatari Riyal",
                "RON - Romanian Leu",
                "RSD - Serbian Dinar",
                "RUB - Russian Ruble",
                "RWF - Rwandan Franc",
                "SAR - Saudi Riyal",
                "SBD - Solomon Islands Dollar",
                "SCR - Seychellois Rupee",
                "SDG - Sudanese Pound",
                "SEK - Swedish Krona",
                "SGD - Singapore Dollar",
                "SHP - Saint Helena Pound",
                "SLE - Sierra Leonean Leone (new)",
                "SLL - Sierra Leonean Leone (old)",
                "SOS - Somali Shilling",
                "SRD - Surinamese Dollar",
                "SSP - South Sudanese Pound",
                "STN - São Tomé and Príncipe Dobra",
                "SYP - Syrian Pound",
                "SZL - Swazi Lilangeni",
                "THB - Thai Baht",
                "TJS - Tajikistani Somoni",
                "TMT - Turkmenistani Manat",
                "TND - Tunisian Dinar",
                "TOP - Tongan Paʻanga",
                "TRY - Turkish Lira",
                "TTD - Trinidad and Tobago Dollar",
                "TVD - Tuvaluan Dollar",
                "TWD - New Taiwan Dollar",
                "TZS - Tanzanian Shilling",
                "UAH - Ukrainian Hryvnia",
                "UGX - Ugandan Shilling",
                "UYU - Uruguayan Peso",
                "UZS - Uzbekistan Som",
                "VES - Venezuelan Bolívar Soberano",
                "VND - Vietnamese Dong",
                "VUV - Vanuatu Vatu",
                "WST - Samoan Tālā",
                "XAF - CFA Franc BEAC",
                "XCD - East Caribbean Dollar",
                "XCG - Caribbean Guilder",
                "XDR - Special Drawing Rights",
                "XOF - CFA Franc BCEAO",
                "XPF - CFP Franc",
                "YER - Yemeni Rial",
                "ZAR - South African Rand",
                "ZMW - Zambian Kwacha",
                "ZWG - Zimbabwe Gold",
                "ZWL - Zimbabwean Dollar"
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
        // Major Global Currencies
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
        tvNzd = findViewById(R.id.tvNzdValue);
        tvVnd = findViewById(R.id.tvVndValue);

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
        tvNzd = findViewById(R.id.tvNzdValue);   // already in Asia, but kept here if you want duplicate reference
        tvPkr = findViewById(R.id.tvPkrValue);
        tvBdt = findViewById(R.id.tvBdtValue);
        tvNgn = findViewById(R.id.tvNgnValue);
        tvKes = findViewById(R.id.tvKesValue);

// Other Currencies (remaining ~140 entries)
        tvAfn = findViewById(R.id.tvAfnValue);
        tvAll = findViewById(R.id.tvAllValue);
        tvAmd = findViewById(R.id.tvAmdValue);
        tvAng = findViewById(R.id.tvAngValue);
        tvAoa = findViewById(R.id.tvAoaValue);
        tvArs = findViewById(R.id.tvArsValue);
        tvAwg = findViewById(R.id.tvAwgValue);
        tvAzn = findViewById(R.id.tvAznValue);
        tvBam = findViewById(R.id.tvBamValue);
        tvBbd = findViewById(R.id.tvBbdValue);
        tvBgn = findViewById(R.id.tvBgnValue);
        tvBhd = findViewById(R.id.tvBhdValue);
        tvBif = findViewById(R.id.tvBifValue);
        tvBmd = findViewById(R.id.tvBmdValue);
        tvBnd = findViewById(R.id.tvBndValue);
        tvBob = findViewById(R.id.tvBobValue);
        tvBsd = findViewById(R.id.tvBsdValue);
        tvBtn = findViewById(R.id.tvBtnValue);
        tvBwp = findViewById(R.id.tvBwpValue);
        tvByn = findViewById(R.id.tvBynValue);
        tvBzd = findViewById(R.id.tvBzdValue);
        tvCdf = findViewById(R.id.tvCdfValue);
        tvClf = findViewById(R.id.tvClfValue);
        tvClp = findViewById(R.id.tvClpValue);
        tvCnh = findViewById(R.id.tvCnhValue);
        tvCop = findViewById(R.id.tvCopValue);
        tvCrc = findViewById(R.id.tvCrcValue);
        tvCup = findViewById(R.id.tvCupValue);
        tvCve = findViewById(R.id.tvCveValue);
        tvCzk = findViewById(R.id.tvCzkValue);
        tvDjf = findViewById(R.id.tvDjfValue);
        tvDkk = findViewById(R.id.tvDkkValue);
        tvDop = findViewById(R.id.tvDopValue);
        tvDzd = findViewById(R.id.tvDzdValue);
        tvErn = findViewById(R.id.tvErnValue);
        tvEtb = findViewById(R.id.tvEtbValue);
        tvFjd = findViewById(R.id.tvFjdValue);
        tvFkp = findViewById(R.id.tvFkpValue);
        tvFok = findViewById(R.id.tvFokValue);
        tvGel = findViewById(R.id.tvGelValue);
        tvGgp = findViewById(R.id.tvGgpValue);
        tvGhs = findViewById(R.id.tvGhsValue);
        tvGip = findViewById(R.id.tvGipValue);
        tvGmd = findViewById(R.id.tvGmdValue);
        tvGnf = findViewById(R.id.tvGnfValue);
        tvGtq = findViewById(R.id.tvGtqValue);
        tvGyd = findViewById(R.id.tvGydValue);
        tvHnl = findViewById(R.id.tvHnlValue);
        tvHrk = findViewById(R.id.tvHrkValue);
        tvHtg = findViewById(R.id.tvHtgValue);
        tvHuf = findViewById(R.id.tvHufValue);
        tvIqd = findViewById(R.id.tvIqdValue);
        tvIrr = findViewById(R.id.tvIrrValue);
        tvIsk = findViewById(R.id.tvIskValue);
        tvJep = findViewById(R.id.tvJepValue);
        tvJmd = findViewById(R.id.tvJmdValue);
        tvJod = findViewById(R.id.tvJodValue);
        tvKes = findViewById(R.id.tvKesValue);   // already above, duplicate if needed
        tvKgs = findViewById(R.id.tvKgsValue);
        tvKhr = findViewById(R.id.tvKhrValue);
        tvKid = findViewById(R.id.tvKidValue);
        tvKmf = findViewById(R.id.tvKmfValue);
        tvKyd = findViewById(R.id.tvKydValue);
        tvKzt = findViewById(R.id.tvKztValue);
        tvLak = findViewById(R.id.tvLakValue);
        tvLbp = findViewById(R.id.tvLbpValue);
        tvLkr = findViewById(R.id.tvLkrValue);
        tvLrd = findViewById(R.id.tvLrdValue);
        tvLsl = findViewById(R.id.tvLslValue);
        tvLyd = findViewById(R.id.tvLydValue);
        tvMad = findViewById(R.id.tvMadValue);
        tvMdl = findViewById(R.id.tvMdlValue);
        tvMga = findViewById(R.id.tvMgaValue);
        tvMkd = findViewById(R.id.tvMkdValue);
        tvMmk = findViewById(R.id.tvMmkValue);
        tvMnt = findViewById(R.id.tvMntValue);
        tvMop = findViewById(R.id.tvMopValue);
        tvMru = findViewById(R.id.tvMruValue);
        tvMur = findViewById(R.id.tvMurValue);
        tvMvr = findViewById(R.id.tvMvrValue);
        tvMwk = findViewById(R.id.tvMwkValue);
        tvMzn = findViewById(R.id.tvMznValue);
        tvNad = findViewById(R.id.tvNadValue);
        tvNio = findViewById(R.id.tvNioValue);
        tvNpr = findViewById(R.id.tvNprValue);
        tvOmr = findViewById(R.id.tvOmrValue);
        tvPab = findViewById(R.id.tvPabValue);
        tvPen = findViewById(R.id.tvPenValue);
        tvPgk = findViewById(R.id.tvPgkValue);
        tvPln = findViewById(R.id.tvPlnValue);
        tvPyg = findViewById(R.id.tvPygValue);
        tvQar = findViewById(R.id.tvQarValue);
        tvRon = findViewById(R.id.tvRonValue);
        tvRsd = findViewById(R.id.tvRsdValue);
        tvRwf = findViewById(R.id.tvRwfValue);
        tvSbd = findViewById(R.id.tvSbdValue);
        tvScr = findViewById(R.id.tvScrValue);
        tvSdg = findViewById(R.id.tvSdgValue);
        tvShp = findViewById(R.id.tvShpValue);
        tvSle = findViewById(R.id.tvSleValue);
        tvSll = findViewById(R.id.tvSllValue);
        tvSos = findViewById(R.id.tvSosValue);
        tvSrd = findViewById(R.id.tvSrdValue);
        tvSsp = findViewById(R.id.tvSspValue);
        tvStn = findViewById(R.id.tvStnValue);
        tvSyp = findViewById(R.id.tvSypValue);
        tvSzl = findViewById(R.id.tvSzlValue);
        tvTjs = findViewById(R.id.tvTjsValue);
        tvTmt = findViewById(R.id.tvTmtValue);
        tvTnd = findViewById(R.id.tvTndValue);
        tvTop = findViewById(R.id.tvTopValue);
        tvTtd = findViewById(R.id.tvTtdValue);
        tvTvd = findViewById(R.id.tvTvdValue);
        tvTwd = findViewById(R.id.tvTwdValue);
        tvTzs = findViewById(R.id.tvTzsValue);
        tvUah = findViewById(R.id.tvUahValue);
        tvUgx = findViewById(R.id.tvUgxValue);
        tvUyu = findViewById(R.id.tvUyuValue);
        tvUzs = findViewById(R.id.tvUzsValue);
        tvVes = findViewById(R.id.tvVesValue);
        tvVuv = findViewById(R.id.tvVuvValue);
        tvWst = findViewById(R.id.tvWstValue);
        tvXaf = findViewById(R.id.tvXafValue);
        tvXcd = findViewById(R.id.tvXcdValue);
        tvXcg = findViewById(R.id.tvXcgValue);
        tvXdr = findViewById(R.id.tvXdrValue);
        tvXof = findViewById(R.id.tvXofValue);
        tvXpf = findViewById(R.id.tvXpfValue);
        tvYer = findViewById(R.id.tvYerValue);
        tvZmw = findViewById(R.id.tvZmwValue);
        tvZwg = findViewById(R.id.tvZwgValue);
        tvZwl = findViewById(R.id.tvZwlValue);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Already on home, do nothing or refresh
                finish();
                return true;
            } else if (itemId == R.id.nav_convert) {
                // Start the conversion activity (e.g., LengthActivity or a generic conversion screen)
                String temp = unitDropdown.getText().toString();
                unitDropdown.setText(unitDropdown2.getText().toString(), false);
                unitDropdown2.setText(temp, false);
                calculate();

                return true;
            } else if (itemId == R.id.nav_favorite) {
                // Start Favorites Activity
                startActivity(new Intent(this, InfoActivity.class));
                return true;
            }
            return false;
        });

        loadRecentConversions();
    }
    // --- Top Menu Methods ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            Toast.makeText(this, "Share clicked!", Toast.LENGTH_SHORT).show();
            // Implement your share logic here
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = "Check out this amazing Currency Converter app: https://play.google.com/store/apps/details?id=" + getPackageName(); // Replace with your actual Play Store link
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // --- End Top Menu Methods ---

    private double convert(double value, String fromUnit, String toUnit) {
        // Get rate relative to 1 USD
        double fromRate = getRate(fromUnit);
        double toRate = getRate(toUnit);

        // Formula: (Value / FromRate) * ToRate
        return (value / fromRate) * toRate;
    }

    private double getRate333(String unit) {
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
        //sharedpreface
        String conversionString = String.format("%.4f %s → %.4f %s", value, from, result, to);
        addRecentConversion(conversionString);
        saveRecentConversions();
        displayRecentConversions();
    }

    private void updateAll(double value, String fromUnit) {

        // Major Global Currencies
        if (tvUsd != null) tvUsd.setText(format(convert(value, fromUnit, "USD - US Dollar")));
        if (tvEur != null) tvEur.setText(format(convert(value, fromUnit, "EUR - Euro")));
        if (tvGbp != null) tvGbp.setText(format(convert(value, fromUnit, "GBP - British Pound Sterling")));
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
        if (tvNzd != null) tvNzd.setText(format(convert(value, fromUnit, "NZD - New Zealand Dollar")));
        if (tvVnd != null) tvVnd.setText(format(convert(value, fromUnit, "VND - Vietnamese Dong")));

        // Europe & Middle East
        if (tvSek != null) tvSek.setText(format(convert(value, fromUnit, "SEK - Swedish Krona")));
        if (tvNok != null) tvNok.setText(format(convert(value, fromUnit, "NOK - Norwegian Krone")));
        if (tvTry != null) tvTry.setText(format(convert(value, fromUnit, "TRY - Turkish Lira")));
        if (tvRub != null) tvRub.setText(format(convert(value, fromUnit, "RUB - Russian Ruble")));
        if (tvAed != null) tvAed.setText(format(convert(value, fromUnit, "AED - UAE Dirham")));
        if (tvSar != null) tvSar.setText(format(convert(value, fromUnit, "SAR - Saudi Riyal")));
        if (tvIls != null) tvIls.setText(format(convert(value, fromUnit, "ILS - Israeli New Shekel")));
        if (tvKwd != null) tvKwd.setText(format(convert(value, fromUnit, "KWD - Kuwaiti Dinar")));

        // Americas & Africa & Others (expanded)
        if (tvMxn != null) tvMxn.setText(format(convert(value, fromUnit, "MXN - Mexican Peso")));
        if (tvBrl != null) tvBrl.setText(format(convert(value, fromUnit, "BRL - Brazilian Real")));
        if (tvZar != null) tvZar.setText(format(convert(value, fromUnit, "ZAR - South African Rand")));
        if (tvEgp != null) tvEgp.setText(format(convert(value, fromUnit, "EGP - Egyptian Pound")));
        if (tvPkr != null) tvPkr.setText(format(convert(value, fromUnit, "PKR - Pakistani Rupee")));
        if (tvBdt != null) tvBdt.setText(format(convert(value, fromUnit, "BDT - Bangladeshi Taka")));
        if (tvNgn != null) tvNgn.setText(format(convert(value, fromUnit, "NGN - Nigerian Naira")));
        if (tvKes != null) tvKes.setText(format(convert(value, fromUnit, "KES - Kenyan Shilling")));

        // Remaining currencies (all others)
        if (tvAfn != null) tvAfn.setText(format(convert(value, fromUnit, "AFN - Afghan Afghani")));
        if (tvAll != null) tvAll.setText(format(convert(value, fromUnit, "ALL - Albanian Lek")));
        if (tvAmd != null) tvAmd.setText(format(convert(value, fromUnit, "AMD - Armenian Dram")));
        if (tvAng != null) tvAng.setText(format(convert(value, fromUnit, "ANG - Netherlands Antillean Guilder")));
        if (tvAoa != null) tvAoa.setText(format(convert(value, fromUnit, "AOA - Angolan Kwanza")));
        if (tvArs != null) tvArs.setText(format(convert(value, fromUnit, "ARS - Argentine Peso")));
        if (tvAwg != null) tvAwg.setText(format(convert(value, fromUnit, "AWG - Aruban Florin")));
        if (tvAzn != null) tvAzn.setText(format(convert(value, fromUnit, "AZN - Azerbaijani Manat")));
        if (tvBam != null) tvBam.setText(format(convert(value, fromUnit, "BAM - Bosnia-Herzegovina Convertible Mark")));
        if (tvBbd != null) tvBbd.setText(format(convert(value, fromUnit, "BBD - Barbadian Dollar")));
        if (tvBgn != null) tvBgn.setText(format(convert(value, fromUnit, "BGN - Bulgarian Lev")));
        if (tvBhd != null) tvBhd.setText(format(convert(value, fromUnit, "BHD - Bahraini Dinar")));
        if (tvBif != null) tvBif.setText(format(convert(value, fromUnit, "BIF - Burundian Franc")));
        if (tvBmd != null) tvBmd.setText(format(convert(value, fromUnit, "BMD - Bermudian Dollar")));
        if (tvBnd != null) tvBnd.setText(format(convert(value, fromUnit, "BND - Brunei Dollar")));
        if (tvBob != null) tvBob.setText(format(convert(value, fromUnit, "BOB - Bolivian Boliviano")));
        if (tvBsd != null) tvBsd.setText(format(convert(value, fromUnit, "BSD - Bahamian Dollar")));
        if (tvBtn != null) tvBtn.setText(format(convert(value, fromUnit, "BTN - Bhutanese Ngultrum")));
        if (tvBwp != null) tvBwp.setText(format(convert(value, fromUnit, "BWP - Botswana Pula")));
        if (tvByn != null) tvByn.setText(format(convert(value, fromUnit, "BYN - Belarusian Ruble")));
        if (tvBzd != null) tvBzd.setText(format(convert(value, fromUnit, "BZD - Belize Dollar")));
        if (tvCdf != null) tvCdf.setText(format(convert(value, fromUnit, "CDF - Congolese Franc")));
        if (tvClf != null) tvClf.setText(format(convert(value, fromUnit, "CLF - Unidad de Fomento")));
        if (tvClp != null) tvClp.setText(format(convert(value, fromUnit, "CLP - Chilean Peso")));
        if (tvCnh != null) tvCnh.setText(format(convert(value, fromUnit, "CNH - Chinese Yuan (Offshore)")));
        if (tvCop != null) tvCop.setText(format(convert(value, fromUnit, "COP - Colombian Peso")));
        if (tvCrc != null) tvCrc.setText(format(convert(value, fromUnit, "CRC - Costa Rican Colón")));
        if (tvCup != null) tvCup.setText(format(convert(value, fromUnit, "CUP - Cuban Peso")));
        if (tvCve != null) tvCve.setText(format(convert(value, fromUnit, "CVE - Cape Verdean Escudo")));
        if (tvCzk != null) tvCzk.setText(format(convert(value, fromUnit, "CZK - Czech Koruna")));
        if (tvDjf != null) tvDjf.setText(format(convert(value, fromUnit, "DJF - Djiboutian Franc")));
        if (tvDkk != null) tvDkk.setText(format(convert(value, fromUnit, "DKK - Danish Krone")));
        if (tvDop != null) tvDop.setText(format(convert(value, fromUnit, "DOP - Dominican Peso")));
        if (tvDzd != null) tvDzd.setText(format(convert(value, fromUnit, "DZD - Algerian Dinar")));
        if (tvErn != null) tvErn.setText(format(convert(value, fromUnit, "ERN - Eritrean Nakfa")));
        if (tvEtb != null) tvEtb.setText(format(convert(value, fromUnit, "ETB - Ethiopian Birr")));
        if (tvFjd != null) tvFjd.setText(format(convert(value, fromUnit, "FJD - Fijian Dollar")));
        if (tvFkp != null) tvFkp.setText(format(convert(value, fromUnit, "FKP - Falkland Islands Pound")));
        if (tvFok != null) tvFok.setText(format(convert(value, fromUnit, "FOK - Faroese Króna")));
        if (tvGel != null) tvGel.setText(format(convert(value, fromUnit, "GEL - Georgian Lari")));
        if (tvGgp != null) tvGgp.setText(format(convert(value, fromUnit, "GGP - Guernsey Pound")));
        if (tvGhs != null) tvGhs.setText(format(convert(value, fromUnit, "GHS - Ghanaian Cedi")));
        if (tvGip != null) tvGip.setText(format(convert(value, fromUnit, "GIP - Gibraltar Pound")));
        if (tvGmd != null) tvGmd.setText(format(convert(value, fromUnit, "GMD - Gambian Dalasi")));
        if (tvGnf != null) tvGnf.setText(format(convert(value, fromUnit, "GNF - Guinean Franc")));
        if (tvGtq != null) tvGtq.setText(format(convert(value, fromUnit, "GTQ - Guatemalan Quetzal")));
        if (tvGyd != null) tvGyd.setText(format(convert(value, fromUnit, "GYD - Guyanaese Dollar")));
        if (tvHnl != null) tvHnl.setText(format(convert(value, fromUnit, "HNL - Honduran Lempira")));
        if (tvHrk != null) tvHrk.setText(format(convert(value, fromUnit, "HRK - Croatian Kuna")));
        if (tvHtg != null) tvHtg.setText(format(convert(value, fromUnit, "HTG - Haitian Gourde")));
        if (tvHuf != null) tvHuf.setText(format(convert(value, fromUnit, "HUF - Hungarian Forint")));
        if (tvIqd != null) tvIqd.setText(format(convert(value, fromUnit, "IQD - Iraqi Dinar")));
        if (tvIrr != null) tvIrr.setText(format(convert(value, fromUnit, "IRR - Iranian Rial")));
        if (tvIsk != null) tvIsk.setText(format(convert(value, fromUnit, "ISK - Icelandic Króna")));
        if (tvJep != null) tvJep.setText(format(convert(value, fromUnit, "JEP - Jersey Pound")));
        if (tvJmd != null) tvJmd.setText(format(convert(value, fromUnit, "JMD - Jamaican Dollar")));
        if (tvJod != null) tvJod.setText(format(convert(value, fromUnit, "JOD - Jordanian Dinar")));
        if (tvKgs != null) tvKgs.setText(format(convert(value, fromUnit, "KGS - Kyrgyzstani Som")));
        if (tvKhr != null) tvKhr.setText(format(convert(value, fromUnit, "KHR - Cambodian Riel")));
        if (tvKid != null) tvKid.setText(format(convert(value, fromUnit, "KID - Kiribati Dollar")));
        if (tvKmf != null) tvKmf.setText(format(convert(value, fromUnit, "KMF - Comorian Franc")));
        if (tvKyd != null) tvKyd.setText(format(convert(value, fromUnit, "KYD - Cayman Islands Dollar")));
        if (tvKzt != null) tvKzt.setText(format(convert(value, fromUnit, "KZT - Kazakhstani Tenge")));
        if (tvLak != null) tvLak.setText(format(convert(value, fromUnit, "LAK - Laotian Kip")));
        if (tvLbp != null) tvLbp.setText(format(convert(value, fromUnit, "LBP - Lebanese Pound")));
        if (tvLkr != null) tvLkr.setText(format(convert(value, fromUnit, "LKR - Sri Lankan Rupee")));
        if (tvLrd != null) tvLrd.setText(format(convert(value, fromUnit, "LRD - Liberian Dollar")));
        if (tvLsl != null) tvLsl.setText(format(convert(value, fromUnit, "LSL - Lesotho Loti")));
        if (tvLyd != null) tvLyd.setText(format(convert(value, fromUnit, "LYD - Libyan Dinar")));
        if (tvMad != null) tvMad.setText(format(convert(value, fromUnit, "MAD - Moroccan Dirham")));
        if (tvMdl != null) tvMdl.setText(format(convert(value, fromUnit, "MDL - Moldovan Leu")));
        if (tvMga != null) tvMga.setText(format(convert(value, fromUnit, "MGA - Malagasy Ariary")));
        if (tvMkd != null) tvMkd.setText(format(convert(value, fromUnit, "MKD - Macedonian Denar")));
        if (tvMmk != null) tvMmk.setText(format(convert(value, fromUnit, "MMK - Myanmar Kyat")));
        if (tvMnt != null) tvMnt.setText(format(convert(value, fromUnit, "MNT - Mongolian Tugrik")));
        if (tvMop != null) tvMop.setText(format(convert(value, fromUnit, "MOP - Macanese Pataca")));
        if (tvMru != null) tvMru.setText(format(convert(value, fromUnit, "MRU - Mauritanian Ouguiya")));
        if (tvMur != null) tvMur.setText(format(convert(value, fromUnit, "MUR - Mauritian Rupee")));
        if (tvMvr != null) tvMvr.setText(format(convert(value, fromUnit, "MVR - Maldivian Rufiyaa")));
        if (tvMwk != null) tvMwk.setText(format(convert(value, fromUnit, "MWK - Malawian Kwacha")));
        if (tvMzn != null) tvMzn.setText(format(convert(value, fromUnit, "MZN - Mozambican Metical")));
        if (tvNad != null) tvNad.setText(format(convert(value, fromUnit, "NAD - Namibian Dollar")));
        if (tvNio != null) tvNio.setText(format(convert(value, fromUnit, "NIO - Nicaraguan Córdoba")));
        if (tvNpr != null) tvNpr.setText(format(convert(value, fromUnit, "NPR - Nepalese Rupee")));
        if (tvOmr != null) tvOmr.setText(format(convert(value, fromUnit, "OMR - Omani Rial")));
        if (tvPab != null) tvPab.setText(format(convert(value, fromUnit, "PAB - Panamanian Balboa")));
        if (tvPen != null) tvPen.setText(format(convert(value, fromUnit, "PEN - Peruvian Sol")));
        if (tvPgk != null) tvPgk.setText(format(convert(value, fromUnit, "PGK - Papua New Guinean Kina")));
        if (tvPln != null) tvPln.setText(format(convert(value, fromUnit, "PLN - Polish Złoty")));
        if (tvPyg != null) tvPyg.setText(format(convert(value, fromUnit, "PYG - Paraguayan Guarani")));
        if (tvQar != null) tvQar.setText(format(convert(value, fromUnit, "QAR - Qatari Riyal")));
        if (tvRon != null) tvRon.setText(format(convert(value, fromUnit, "RON - Romanian Leu")));
        if (tvRsd != null) tvRsd.setText(format(convert(value, fromUnit, "RSD - Serbian Dinar")));
        if (tvRwf != null) tvRwf.setText(format(convert(value, fromUnit, "RWF - Rwandan Franc")));
        if (tvSbd != null) tvSbd.setText(format(convert(value, fromUnit, "SBD - Solomon Islands Dollar")));
        if (tvScr != null) tvScr.setText(format(convert(value, fromUnit, "SCR - Seychellois Rupee")));
        if (tvSdg != null) tvSdg.setText(format(convert(value, fromUnit, "SDG - Sudanese Pound")));
        if (tvShp != null) tvShp.setText(format(convert(value, fromUnit, "SHP - Saint Helena Pound")));
        if (tvSle != null) tvSle.setText(format(convert(value, fromUnit, "SLE - Sierra Leonean Leone (new)")));
        if (tvSll != null) tvSll.setText(format(convert(value, fromUnit, "SLL - Sierra Leonean Leone (old)")));
        if (tvSos != null) tvSos.setText(format(convert(value, fromUnit, "SOS - Somali Shilling")));
        if (tvSrd != null) tvSrd.setText(format(convert(value, fromUnit, "SRD - Surinamese Dollar")));
        if (tvSsp != null) tvSsp.setText(format(convert(value, fromUnit, "SSP - South Sudanese Pound")));
        if (tvStn != null) tvStn.setText(format(convert(value, fromUnit, "STN - São Tomé and Príncipe Dobra")));
        if (tvSyp != null) tvSyp.setText(format(convert(value, fromUnit, "SYP - Syrian Pound")));
        if (tvSzl != null) tvSzl.setText(format(convert(value, fromUnit, "SZL - Swazi Lilangeni")));
        if (tvTjs != null) tvTjs.setText(format(convert(value, fromUnit, "TJS - Tajikistani Somoni")));
        if (tvTmt != null) tvTmt.setText(format(convert(value, fromUnit, "TMT - Turkmenistani Manat")));
        if (tvTnd != null) tvTnd.setText(format(convert(value, fromUnit, "TND - Tunisian Dinar")));
        if (tvTop != null) tvTop.setText(format(convert(value, fromUnit, "TOP - Tongan Paʻanga")));
        if (tvTtd != null) tvTtd.setText(format(convert(value, fromUnit, "TTD - Trinidad and Tobago Dollar")));
        if (tvTvd != null) tvTvd.setText(format(convert(value, fromUnit, "TVD - Tuvaluan Dollar")));
        if (tvTwd != null) tvTwd.setText(format(convert(value, fromUnit, "TWD - New Taiwan Dollar")));
        if (tvTzs != null) tvTzs.setText(format(convert(value, fromUnit, "TZS - Tanzanian Shilling")));
        if (tvUah != null) tvUah.setText(format(convert(value, fromUnit, "UAH - Ukrainian Hryvnia")));
        if (tvUgx != null) tvUgx.setText(format(convert(value, fromUnit, "UGX - Ugandan Shilling")));
        if (tvUyu != null) tvUyu.setText(format(convert(value, fromUnit, "UYU - Uruguayan Peso")));
        if (tvUzs != null) tvUzs.setText(format(convert(value, fromUnit, "UZS - Uzbekistan Som")));
        if (tvVes != null) tvVes.setText(format(convert(value, fromUnit, "VES - Venezuelan Bolívar Soberano")));
        if (tvVuv != null) tvVuv.setText(format(convert(value, fromUnit, "VUV - Vanuatu Vatu")));
        if (tvWst != null) tvWst.setText(format(convert(value, fromUnit, "WST - Samoan Tālā")));
        if (tvXaf != null) tvXaf.setText(format(convert(value, fromUnit, "XAF - CFA Franc BEAC")));
        if (tvXcd != null) tvXcd.setText(format(convert(value, fromUnit, "XCD - East Caribbean Dollar")));
        if (tvXcg != null) tvXcg.setText(format(convert(value, fromUnit, "XCG - Caribbean Guilder")));
        if (tvXdr != null) tvXdr.setText(format(convert(value, fromUnit, "XDR - Special Drawing Rights")));
        if (tvXof != null) tvXof.setText(format(convert(value, fromUnit, "XOF - CFA Franc BCEAO")));
        if (tvXpf != null) tvXpf.setText(format(convert(value, fromUnit, "XPF - CFP Franc")));
        if (tvYer != null) tvYer.setText(format(convert(value, fromUnit, "YER - Yemeni Rial")));
        if (tvZmw != null) tvZmw.setText(format(convert(value, fromUnit, "ZMW - Zambian Kwacha")));
        if (tvZwg != null) tvZwg.setText(format(convert(value, fromUnit, "ZWG - Zimbabwe Gold")));
        if (tvZwl != null) tvZwl.setText(format(convert(value, fromUnit, "ZWL - Zimbabwean Dollar")));
    }

    private String format(double value) {
        if (value == 0) return "0";
        if (Math.abs(value) < 0.0001 || Math.abs(value) > 1_000_000)
            return String.format("%.4e", value);
        return String.format("%.4f", value);
    }
    //load data api=======================================
    private void loadCachedRates() {
        String json = prefs.getString("rates_json", null);

        if(json == null) return;

        try {
            JSONObject obj = new JSONObject(json);
            JSONObject rates = obj.getJSONObject("rates");

            Iterator<String> keys = rates.keys();

            while(keys.hasNext()){
                String key = keys.next();
                liveRates.put(key, rates.getDouble(key));
            }

            //load update text----------------
            txtUpdated = findViewById(R.id.txtUpdated);
            String updated = obj.getString("updated");
            //formate
            SimpleDateFormat serverFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat displayFormat =
                    new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
            Date date = serverFormat.parse(updated);   // ✅ parse using server format
            String showDate = displayFormat.format(date); // ✅ format for display
            txtUpdated.setText("Live Updated: " + showDate);
            //end-----------------------------

            if(isUpdatedToday(updated)){
                runOnUiThread(() -> {
                    updateIDmanual = findViewById(R.id.updateIDmanual);
                    updateIDmanual.setVisibility(View.GONE);
                });
            }

        } catch(Exception e){
            e.printStackTrace();
            txtUpdated.setText("Internet connect is failed. please try again.");
        }
    }
    private void fetchRatesFromServer() {

        new Thread(() -> {

            try {
                URL url = new URL("https://larapress.org/en/exchange");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder json = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
                    json.append(line);
                }

                String result = json.toString();

                // Save cache
                prefs.edit().putString("rates_json", result).apply();

                runOnUiThread(() -> {
                    // Reload into memory
                    loadCachedRates();
                    updateIDmanual.setVisibility(View.GONE);
                    Toast.makeText(this,
                            "Updated ✔",
                            Toast.LENGTH_SHORT).show();
                });

            } catch(Exception e){
                e.printStackTrace();

                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "Update Failed",
                            Toast.LENGTH_SHORT).show();
                });
            }

        }).start();
    }
    private String getCode(String unit){
        return unit.substring(0,3); // "USD - US Dollar" → USD
    }
    private double getRate(String unit) {

        String code = getCode(unit);

        // 1️⃣ Try LIVE cache
        if(liveRates.containsKey(code)){
            return liveRates.get(code);
        }

        // 2️⃣ Fallback to your offline switch
        switch (unit) {
            case "USD - US Dollar":                  return 1.0;
            case "AED - UAE Dirham":                 return 3.6725;
            case "AFN - Afghan Afghani":             return 64.7258;
            case "ALL - Albanian Lek":               return 81.1441;
            case "AMD - Armenian Dram":              return 377.5668;
            case "ANG - Netherlands Antillean Guilder": return 1.79;
            case "AOA - Angolan Kwanza":             return 920.9253;
            case "ARS - Argentine Peso":             return 1452.25;
            case "AUD - Australian Dollar":          return 1.4084;
            case "AWG - Aruban Florin":              return 1.79;
            case "AZN - Azerbaijani Manat":          return 1.7007;
            case "BAM - Bosnia-Herzegovina Convertible Mark": return 1.6474;
            case "BBD - Barbadian Dollar":           return 2.0;
            case "BDT - Bangladeshi Taka":           return 122.3587;
            case "BGN - Bulgarian Lev":              return 1.5974;
            case "BHD - Bahraini Dinar":             return 0.376;
            case "BIF - Burundian Franc":            return 2967.7764;
            case "BMD - Bermudian Dollar":           return 1.0;
            case "BND - Brunei Dollar":              return 1.262;
            case "BOB - Bolivian Boliviano":         return 6.9323;
            case "BRL - Brazilian Real":             return 5.1814;
            case "BSD - Bahamian Dollar":            return 1.0;
            case "BTN - Bhutanese Ngultrum":         return 90.6165;
            case "BWP - Botswana Pula":              return 13.3848;
            case "BYN - Belarusian Ruble":           return 2.8626;
            case "BZD - Belize Dollar":              return 2.0;
            case "CAD - Canadian Dollar":            return 1.3598;
            case "CDF - Congolese Franc":            return 2265.2062;
            case "CHF - Swiss Franc":                return 0.7695;
            case "CLF - Unidad de Fomento":          return 0.0216;     // (inflation-adjusted, Chile)
            case "CLP - Chilean Peso":               return 853.9375;
            case "CNH - Chinese Yuan (Offshore)":    return 6.9008;
            case "CNY - Chinese Yuan":               return 6.9096;
            case "COP - Colombian Peso":             return 3674.4267;
            case "CRC - Costa Rican Colón":          return 492.0536;
            case "CUP - Cuban Peso":                 return 24.0;
            case "CVE - Cape Verdean Escudo":        return 92.8781;
            case "CZK - Czech Koruna":               return 20.4268;
            case "DJF - Djiboutian Franc":           return 177.721;
            case "DKK - Danish Krone":               return 6.2862;
            case "DOP - Dominican Peso":             return 62.7807;
            case "DZD - Algerian Dinar":             return 129.5715;
            case "EGP - Egyptian Pound":             return 46.8453;
            case "ERN - Eritrean Nakfa":             return 15.0;
            case "ETB - Ethiopian Birr":             return 155.1402;
            case "EUR - Euro":                       return 0.8423;
            case "FJD - Fijian Dollar":              return 2.1867;
            case "FKP - Falkland Islands Pound":     return 0.7339;
            case "FOK - Faroese Króna":              return 6.2863;
            case "GBP - British Pound Sterling":     return 0.7339;
            case "GEL - Georgian Lari":              return 2.6825;
            case "GGP - Guernsey Pound":             return 0.7339;
            case "GHS - Ghanaian Cedi":              return 11.0136;
            case "GIP - Gibraltar Pound":            return 0.7339;
            case "GMD - Gambian Dalasi":             return 74.1073;
            case "GNF - Guinean Franc":              return 8759.1179;
            case "GTQ - Guatemalan Quetzal":         return 7.6779;
            case "GYD - Guyanaese Dollar":           return 209.2431;
            case "HKD - Hong Kong Dollar":           return 7.8165;
            case "HNL - Honduran Lempira":           return 26.458;
            case "HRK - Croatian Kuna":              return 6.3464;     // (note: HRK replaced by EUR, but kept if in your data)
            case "HTG - Haitian Gourde":             return 130.9764;
            case "HUF - Hungarian Forint":           return 319.465;
            case "IDR - Indonesian Rupiah":          return 16814.482;
            case "ILS - Israeli New Shekel":         return 3.0726;
            case "IMP - Isle of Man Pound":          return 0.7339;
            case "INR - Indian Rupee":               return 90.6206;
            case "IQD - Iraqi Dinar":                return 1309.7168;
            case "IRR - Iranian Rial":               return 1231096.6366;
            case "ISK - Icelandic Króna":            return 122.2694;
            case "JEP - Jersey Pound":               return 0.7339;
            case "JMD - Jamaican Dollar":            return 156.4587;
            case "JOD - Jordanian Dinar":            return 0.709;
            case "JPY - Japanese Yen":               return 152.9512;
            case "KES - Kenyan Shilling":            return 128.979;
            case "KGS - Kyrgyzstani Som":            return 87.4241;
            case "KHR - Cambodian Riel":             return 4019.2111;
            case "KID - Kiribati Dollar":            return 1.4083;     // (often uses AUD)
            case "KMF - Comorian Franc":             return 414.393;
            case "KRW - South Korean Won":           return 1439.0452;
            case "KWD - Kuwaiti Dinar":              return 0.3066;
            case "KYD - Cayman Islands Dollar":      return 0.8333;
            case "KZT - Kazakhstani Tenge":          return 494.3144;
            case "LAK - Laotian Kip":                return 21609.5757;
            case "LBP - Lebanese Pound":             return 89500.0;
            case "LKR - Sri Lankan Rupee":           return 309.1793;
            case "LRD - Liberian Dollar":            return 186.7283;
            case "LSL - Lesotho Loti":               return 15.9397;
            case "LYD - Libyan Dinar":               return 6.3043;
            case "MAD - Moroccan Dirham":            return 9.1337;
            case "MDL - Moldovan Leu":               return 16.9068;
            case "MGA - Malagasy Ariary":            return 4417.8538;
            case "MKD - Macedonian Denar":           return 51.7888;
            case "MMK - Myanmar Kyat":               return 2102.2795;
            case "MNT - Mongolian Tugrik":           return 3593.5827;
            case "MOP - Macanese Pataca":            return 8.0511;
            case "MRU - Mauritanian Ouguiya":        return 39.8681;
            case "MUR - Mauritian Rupee":            return 45.7114;
            case "MVR - Maldivian Rufiyaa":          return 15.4464;
            case "MWK - Malawian Kwacha":            return 1741.1385;
            case "MXN - Mexican Peso":               return 17.212;
            case "MYR - Malaysian Ringgit":          return 3.9029;
            case "MZN - Mozambican Metical":         return 63.6864;
            case "NAD - Namibian Dollar":            return 15.9397;
            case "NGN - Nigerian Naira":             return 1353.0691;
            case "NIO - Nicaraguan Córdoba":         return 36.8325;
            case "NOK - Norwegian Krone":            return 9.5141;
            case "NPR - Nepalese Rupee":             return 144.9864;
            case "NZD - New Zealand Dollar":         return 1.6556;
            case "OMR - Omani Rial":                 return 0.3845;
            case "PAB - Panamanian Balboa":          return 1.0;
            case "PEN - Peruvian Sol":               return 3.3565;
            case "PGK - Papua New Guinean Kina":     return 4.2968;
            case "PHP - Philippine Peso":            return 58.0665;
            case "PKR - Pakistani Rupee":            return 279.72;
            case "PLN - Polish Złoty":               return 3.55;
            case "PYG - Paraguayan Guarani":         return 6621.0704;
            case "QAR - Qatari Riyal":               return 3.64;
            case "RON - Romanian Leu":               return 4.2878;
            case "RSD - Serbian Dinar":              return 98.85;
            case "RUB - Russian Ruble":              return 77.2401;
            case "RWF - Rwandan Franc":              return 1459.9532;
            case "SAR - Saudi Riyal":                return 3.75;
            case "SBD - Solomon Islands Dollar":     return 7.925;
            case "SCR - Seychellois Rupee":          return 13.9055;
            case "SDG - Sudanese Pound":             return 511.6948;
            case "SEK - Swedish Krona":              return 8.9196;
            case "SGD - Singapore Dollar":           return 1.2621;
            case "SHP - Saint Helena Pound":         return 0.7339;
            case "SLE - Sierra Leonean Leone (new)": return 24.6191;
            case "SLL - Sierra Leonean Leone (old)": return 24619.1096;
            case "SOS - Somali Shilling":            return 570.7851;
            case "SRD - Surinamese Dollar":          return 37.9272;
            case "SSP - South Sudanese Pound":       return 4573.4968;
            case "STN - São Tomé and Príncipe Dobra": return 20.6368;
            case "SYP - Syrian Pound":               return 113.013;
            case "SZL - Swazi Lilangeni":            return 15.9397;
            case "THB - Thai Baht":                  return 31.0331;
            case "TJS - Tajikistani Somoni":         return 9.3492;
            case "TMT - Turkmenistani Manat":        return 3.4995;
            case "TND - Tunisian Dinar":             return 2.8469;
            case "TOP - Tongan Paʻanga":             return 2.3605;
            case "TRY - Turkish Lira":               return 43.715;
            case "TTD - Trinidad and Tobago Dollar": return 6.7652;
            case "TVD - Tuvaluan Dollar":            return 1.4083;     // (often uses AUD)
            case "TWD - New Taiwan Dollar":          return 31.4036;
            case "TZS - Tanzanian Shilling":         return 2569.5916;
            case "UAH - Ukrainian Hryvnia":          return 43.0026;
            case "UGX - Ugandan Shilling":           return 3526.6969;
            case "UYU - Uruguayan Peso":             return 38.3636;
            case "UZS - Uzbekistan Som":             return 12270.8154;
            case "VES - Venezuelan Bolívar Soberano": return 393.2216;
            case "VND - Vietnamese Dong":            return 25909.333;
            case "VUV - Vanuatu Vatu":               return 119.1519;
            case "WST - Samoan Tālā":                return 2.6727;
            case "XAF - CFA Franc BEAC":             return 552.5239;
            case "XCD - East Caribbean Dollar":      return 2.7;
            case "XCG - Caribbean Guilder":          return 1.79;
            case "XDR - Special Drawing Rights":     return 0.7247;
            case "XOF - CFA Franc BCEAO":            return 552.5239;
            case "XPF - CFP Franc":                  return 100.5154;
            case "YER - Yemeni Rial":                return 238.4218;
            case "ZAR - South African Rand":         return 15.9401;
            case "ZMW - Zambian Kwacha":             return 18.7706;
            case "ZWG - Zimbabwe Gold":              return 25.5643;
            case "ZWL - Zimbabwean Dollar":          return 25.5643;

            default:                                 return 1.0;
        }
    }

    private boolean isUpdatedToday(String updated) {
        try {
            SimpleDateFormat serverFormat2 =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date updatedDate = serverFormat2.parse(updated);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(updatedDate);
            Calendar cal2 = Calendar.getInstance();
            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } catch (Exception e) {
            return false;
        }
    }

    // sharedprefacen
    private void addRecentConversion(String conversion) {
        // Add to the beginning of the list
        recentConversions.add(0, conversion);
        // Keep only the latest MAX_RECENT_CONVERSIONS
        while (recentConversions.size() > MAX_RECENT_CONVERSIONS) {
            recentConversions.remove(recentConversions.size() - 1);
        }
    }

    private void saveRecentConversions() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Convert the list to a comma-separated string
        String conversionsString = TextUtils.join("||", recentConversions);
        editor.putString(RECENT_CONVERSIONS_KEY, conversionsString);
        editor.apply();
    }

    private void loadRecentConversions() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String conversionsString = prefs.getString(RECENT_CONVERSIONS_KEY, "");
        if (!conversionsString.isEmpty()) {
            recentConversions = new ArrayList<>(Arrays.asList(conversionsString.split("\\|\\|")));
        } else {
            recentConversions = new ArrayList<>();
        }
        displayRecentConversions();
    }

    private void displayRecentConversions() {
        recentConversionsContainer.removeAllViews(); // Clear existing views
        if (recentConversions.isEmpty()) {
            TextView noConversions = new TextView(this);
            noConversions.setText("No recent conversions.");
            noConversions.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small);
            recentConversionsContainer.addView(noConversions);
        } else {
            for (int i = 0; i < recentConversions.size(); i++) {
                String conversion = recentConversions.get(i);
                TextView tv = new TextView(this);
                tv.setText(conversion);
                tv.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small);
                tv.setPadding(0, 8, 0, 8);
                recentConversionsContainer.addView(tv);

                if (i < recentConversions.size() - 1) {
                    View divider = new View(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    layoutParams.setMargins(0, 6, 0, 6);
                    divider.setLayoutParams(layoutParams);
                    divider.setBackgroundColor(getColor(R.color.colorOnSurface)); // Assuming you have this color or define it
                    recentConversionsContainer.addView(divider);
                }
            }
        }
    }




}
