package com.example.expensesplitter.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.ExpenseEntity;
import com.example.expensesplitter.data.entity.ExpenseShareEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private long selectedDate = System.currentTimeMillis();
    private EditText etDateTime;
    private long expenseId = -1;
    private AutoCompleteTextView spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        long groupId = getIntent().getLongExtra("GROUP_ID", -1);
        expenseId = getIntent().getLongExtra("EXPENSE_ID", -1);
        
        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAmount = findViewById(R.id.etAmount);
        etDateTime = findViewById(R.id.etDateTime);
        Button btnSave = findViewById(R.id.btnSave);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // ★ ক্যাটাগরি ড্রপডাউন সেটআপ
        String[] categories = {"বাজার", "খাবার", "যাতায়াত", "বিল", "অন্যান্য"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        spinnerCategory.setAdapter(catAdapter);

        // এডিট মোড চেক (যদি আগের কোনো খরচ এডিট করতে আসেন)
        if (expenseId != -1) {
            String title = getIntent().getStringExtra("EXPENSE_TITLE");
            double amt = getIntent().getDoubleExtra("EXPENSE_AMOUNT", 0);
            selectedDate = getIntent().getLongExtra("EXPENSE_DATE", System.currentTimeMillis());
            etTitle.setText(title);
            etAmount.setText(String.valueOf(amt));
        }

        updateDateTimeText(selectedDate);

        // ডেট এবং টাইম পিকার
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

        // সেভ বাটনের কাজ
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String amtStr = etAmount.getText().toString();
            String category = spinnerCategory.getText().toString();
            
            if (title.isEmpty() || amtStr.isEmpty()) {
                Toast.makeText(this, "সব ঘর পূরণ করুন", Toast.LENGTH_SHORT).show();
                return;
            }
            if (category.isEmpty()) {
                category = "অন্যান্য";
            }
            
            double amount = Double.parseDouble(amtStr);
            
            // ★ নতুন কনস্ট্রাক্টর অনুযায়ী ক্যাটাগরি পাস করা হলো
            ExpenseEntity expense = new ExpenseEntity(groupId, title, amount, 0, category);
            expense.createdAt = selectedDate;
            
            List<ExpenseShareEntity> shares = new ArrayList<>();
            shares.add(new ExpenseShareEntity(0, 0, amount));

            if (expenseId != -1) {
                expense.id = expenseId;
                vm.updateExpense(expense, shares);
            } else {
                vm.addExpense(expense, shares);
            }
            finish();
        });
    }

    private void updateDateTimeText(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        etDateTime.setText(sdf.format(new Date(time)));
    }
}
