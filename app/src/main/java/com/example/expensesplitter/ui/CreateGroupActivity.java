package com.example.expensesplitter.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.GroupEntity;
import com.example.expensesplitter.data.entity.MemberEntity;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateGroupActivity extends AppCompatActivity {
    private String selectedIcon = "🛍️";
    private long startDate = System.currentTimeMillis();
    private long endDate = System.currentTimeMillis();
    private EditText etDateRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);
        EditText etGroupName = findViewById(R.id.etGroupName);
        etDateRange = findViewById(R.id.etDateRange);
        MaterialButton btnSave = findViewById(R.id.btnSaveGroup);
        LinearLayout iconContainer = findViewById(R.id.iconContainer);
        AutoCompleteTextView spinnerCurrency = findViewById(R.id.spinnerCurrency);

        // ১. কারেন্সি ড্রপডাউন সেটআপ
        String[] currencies = {"৳ BDT", "$ USD", "€ EUR", "₹ INR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, currencies);
        spinnerCurrency.setAdapter(adapter);

        // ২. আইকন সিলেকশন লজিক
        String[] icons = {"🛍️", "✈️", "🏠", "🎉", "💼", "🍔", "🚗", "🎬"};
        for (String icon : icons) {
            TextView tv = new TextView(this);
            tv.setText(icon);
            tv.setTextSize(28);
            tv.setPadding(24, 24, 24, 24);
            tv.setOnClickListener(v -> {
                selectedIcon = icon;
                Toast.makeText(this, "Selected: " + icon, Toast.LENGTH_SHORT).show();
            });
            iconContainer.addView(tv);
        }

        // ৩. ডেট পিকার
        updateDateText();
        etDateRange.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                startDate = cal.getTimeInMillis();
                endDate = startDate + (86400000L * 7); // উদাহরণস্বরূপ ৭ দিন পরের তারিখ
                updateDateText();
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // ৪. সেভ বাটন
        btnSave.setOnClickListener(v -> {
            String name = etGroupName.getText().toString();
            String currency = spinnerCurrency.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(this, "গ্রুপের নাম দিন", Toast.LENGTH_SHORT).show();
                return;
            }
            
            GroupEntity group = new GroupEntity(name, "Custom", selectedIcon, currency, startDate, endDate);
            vm.addCustomGroup(group); // নিচে ViewModel-এ এই মেথড যোগ করতে হবে
            finish();
        });
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        etDateRange.setText(sdf.format(new Date(startDate)) + " to " + sdf.format(new Date(endDate)));
    }
}
