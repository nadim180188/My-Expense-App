package com.example.expensesplitter.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.SettlementEntity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddSettlementActivity extends AppCompatActivity {
    private long selectedDate = System.currentTimeMillis();
    private EditText etDateTime;
    private long settlementId = -1; // ★ এডিট করার জন্য নতুন ভেরিয়েবল

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_settlement);

        long groupId = getIntent().getLongExtra("GROUP_ID", -1);
        settlementId = getIntent().getLongExtra("SETTLEMENT_ID", -1); // ★ এডিট মোড চেক

        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);

        EditText etAmount = findViewById(R.id.etAmount);
        EditText etNote = findViewById(R.id.etNote);
        etDateTime = findViewById(R.id.etDateTime);
        Button btnSave = findViewById(R.id.btnSave);

        // এডিট মোড হলে আগের ডেটা দেখাবে
        if (settlementId != -1) {
            double amount = getIntent().getDoubleExtra("SETTLEMENT_AMOUNT", 0);
            String note = getIntent().getStringExtra("SETTLEMENT_NOTE");
            selectedDate = getIntent().getLongExtra("SETTLEMENT_DATE", System.currentTimeMillis());
            etAmount.setText(String.valueOf(amount));
            etNote.setText(note);
        }

        updateDateTimeText(selectedDate);

        etDateTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(selectedDate);
            
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                cal.set(year, month, dayOfMonth);
                
                new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);
                    selectedDate = cal.getTimeInMillis();
                    updateDateTimeText(selectedDate);
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
                
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSave.setOnClickListener(v -> {
            String amtStr = etAmount.getText().toString();
            if (amtStr.isEmpty()) {
                Toast.makeText(this, "টাকার পরিমাণ দিন", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(amtStr);
            String note = etNote.getText().toString();

            SettlementEntity settlement = new SettlementEntity(groupId, 0, amount, "PAID", note);
            settlement.date = selectedDate;
            
            if (settlementId != -1) {
                settlement.id = settlementId; // ★ পুরনো আইডি সেট
                vm.updateSettlement(settlement); // ★ আপডেট মেথড
            } else {
                vm.addSettlement(settlement); // ★ নতুন সেভ
            }
            finish();
        });
    }

    private void updateDateTimeText(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        etDateTime.setText(sdf.format(new Date(time)));
    }
}
