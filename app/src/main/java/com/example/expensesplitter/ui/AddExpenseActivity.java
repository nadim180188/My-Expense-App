package com.example.expensesplitter.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.ExpenseEntity;
import com.example.expensesplitter.data.entity.ExpenseShareEntity;
import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        long groupId = getIntent().getLongExtra("GROUP_ID", -1);
        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAmount = findViewById(R.id.etAmount);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String amtStr = etAmount.getText().toString();
            if (title.isEmpty() || amtStr.isEmpty()) {
                Toast.makeText(this, "সব ঘর পূরণ করুন", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(amtStr);
            
            // এখানে আমরা ধরে নিচ্ছি আপনি নিজেই সব খরচ দিচ্ছেন, তাই paidByMemberId = 0 (বা যেকোনো ডামি আইডি)
            ExpenseEntity expense = new ExpenseEntity(groupId, title, amount, 0);
            
            // মেম্বার না থাকায় আমরা একটি ডামি শেয়ার তৈরি করছি (যাতে অ্যাপ ক্র্যাশ না করে)
            List<ExpenseShareEntity> shares = new ArrayList<>();
            shares.add(new ExpenseShareEntity(0, 0, amount));

            vm.addExpense(expense, shares);
            Toast.makeText(this, "খরচ সেভ হয়েছে", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
