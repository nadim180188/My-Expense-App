package com.example.expensesplitter.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.*;
import com.example.expensesplitter.model.BalanceCalculator;
import com.google.android.material.tabs.TabLayout;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class GroupDetailActivity extends AppCompatActivity {
    private long groupId;
    private RecyclerView rv;
    private ExpenseAdapter expenseAdapter;
    private BalanceAdapter balanceAdapter;

            // ... (আগের কোড)
        
        // শেয়ার পিডিএফ বাটন
        findViewById(R.id.btnSharePdf).setOnClickListener(v -> {
            // পিডিএফ বানানোর জন্য এক্সপেন্স লিস্টটি নিতে হবে
            PdfGenerator.generateAndShare(this, getIntent().getStringExtra("GROUP_NAME"), expenses.get());
        });    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        
        groupId = getIntent().getLongExtra("GROUP_ID", -1);
        setTitle(getIntent().getStringExtra("GROUP_NAME"));

        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);
        
        rv = findViewById(R.id.recyclerView); // XML এর আইডি
        rv.setLayoutManager(new LinearLayoutManager(this));
        
        expenseAdapter = new ExpenseAdapter();
        balanceAdapter = new BalanceAdapter();

        rv.setAdapter(expenseAdapter); // ডিফল্ট Expense

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    rv.setAdapter(expenseAdapter);
                } else {
                    rv.setAdapter(balanceAdapter);
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        AtomicReference<List<MemberEntity>> members = new AtomicReference<>(Collections.emptyList());
        AtomicReference<List<ExpenseEntity>> expenses = new AtomicReference<>(Collections.emptyList());
        AtomicReference<List<ExpenseShareEntity>> shares = new AtomicReference<>(Collections.emptyList());
        AtomicReference<List<SettlementEntity>> settlements = new AtomicReference<>(Collections.emptyList());

        Runnable recompute = () -> balanceAdapter.submit(BalanceCalculator.calculate(
                members.get(), shares.get(), expenses.get(), settlements.get()));

        vm.members(groupId).observe(this, m -> { members.set(m); recompute.run(); });
        vm.expenses(groupId).observe(this, e -> { 
            expenses.set(e); 
            expenseAdapter.submit(e); 
            recompute.run(); 
        });
        vm.shares(groupId).observe(this, s -> { shares.set(s); recompute.run(); });
        vm.settlements(groupId).observe(this, s -> { settlements.set(s); recompute.run(); });

        // + বাটনের কাজ
        findViewById(R.id.fabAdd).setOnClickListener(v -> {
            String[] options = {"খরচ যোগ করুন (Expense)", "টাকা শোধ করুন (Settlement)"};
            new android.app.AlertDialog.Builder(this)
                    .setTitle("কী যোগ করতে চান?")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            android.content.Intent intent = new android.content.Intent(this, AddExpenseActivity.class);
                            intent.putExtra("GROUP_ID", groupId);
                            startActivity(intent);
                        } else {
                            android.content.Intent intent = new android.content.Intent(this, AddSettlementActivity.class);
                            intent.putExtra("GROUP_ID", groupId);
                            startActivity(intent);
                        }
                    }).show();
        });
    }
}
