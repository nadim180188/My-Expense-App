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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_settlement);

        long groupId = getIntent().getLongExtra("GROUP_ID", -1);
        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);

        EditText etAmount = findViewById(R.id.etAmount);
        EditText etNote = findViewById(R.id.etNote);
        etDateTime = findViewById(R.id.etDateTime); // নিশ্চিত করুন XML-এ এই আইডি আছে
        Button btnSave = findViewById(R.id.btnSave);

        // প্রাথমিকভাবে আজকের তারিখ ও সময় দেখাবে
        updateDateTimeText(selectedDate);

        // ক্লিক করলে ডেট এবং টাইম পিকার খুলবে
        if (etDateTime != null) {
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
        }

        btnSave.setOnClickListener(v -> {
            String amtStr = etAmount.getText().toString();
            if (amtStr.isEmpty()) {
                Toast.makeText(this, "টাকার পরিমাণ দিন", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(amtStr);
            String note = etNote.getText().toString();

            // type = "PAID" মানে আপনি টাকা দিয়েছেন
            SettlementEntity settlement = new SettlementEntity(groupId, 0, amount, "PAID", note);
            settlement.date = selectedDate; // ★ সিলেক্ট করা তারিখ সেভ হবে
            
            vm.addSettlement(settlement);
            Toast.makeText(this, "শোধ সেভ হয়েছে", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateDateTimeText(long time) {
        if (etDateTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            etDateTime.setText(sdf.format(new Date(time)));
        }
    }
}
