package com.example.expensesplitter.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.SettlementEntity;

public class AddSettlementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_settlement);

        long groupId = getIntent().getLongExtra("GROUP_ID", -1);
        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);

        EditText etAmount = findViewById(R.id.etAmount);
        EditText etNote = findViewById(R.id.etNote);
        Button btnSave = findViewById(R.id.btnSave);

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
            vm.addSettlement(settlement);
            Toast.makeText(this, "শোধ সেভ হয়েছে", Toast.LENGTH_SHORT).show();
            finish();
            // AddExpenseActivity-তে নিচের মেথডটি যোগ করুন এবং etDateTime-এ ক্লিক করলে এটি কল করুন
private void showDateTimePicker() {
    final java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTimeInMillis(selectedDate);
    
    new android.app.DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
        cal.set(year, month, dayOfMonth);
        
        new android.app.TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
            cal.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(java.util.Calendar.MINUTE, minute);
            selectedDate = cal.getTimeInMillis();
            updateDateTimeText(selectedDate);
        }, cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE), false).show();
        
    }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
}
        });
    }
}
