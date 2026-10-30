package com.example.expensesplitter.ui;

import android.os.Bundle;
import android.widget.EditText; // ★ এই ইমপোর্টটি মিসিং ছিল
import android.widget.TextView;
import android.widget.Toast;
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
    private SettlementAdapter settlementAdapter;
    private BalanceAdapter balanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_group_detail);
            
            groupId = getIntent().getLongExtra("GROUP_ID", -1);
            setTitle(getIntent().getStringExtra("GROUP_NAME"));

            GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);
            
            TextView tvTotal = findViewById(R.id.tvTotal);
            TextView tvPaid = findViewById(R.id.tvPaid);
            TextView tvDue = findViewById(R.id.tvDue);
            CategoryPieChartView categoryChart = findViewById(R.id.categoryPieChart);
            BarChartView barChart = findViewById(R.id.barChart);

            rv = findViewById(R.id.recyclerView);
            rv.setLayoutManager(new LinearLayoutManager(this));
            
            expenseAdapter = new ExpenseAdapter(
                expense -> {
                    android.content.Intent intent = new android.content.Intent(this, AddExpenseActivity.class);
                    intent.putExtra("GROUP_ID", groupId);
                    intent.putExtra("EXPENSE_ID", expense.id);
                    intent.putExtra("EXPENSE_TITLE", expense.title);
                    intent.putExtra("EXPENSE_AMOUNT", expense.amount);
                    intent.putExtra("EXPENSE_DATE", expense.createdAt);
                    startActivity(intent);
                },
                expense -> {
                    new android.app.AlertDialog.Builder(this)
                        .setTitle("Delete Expense").setMessage("ডিলিট করতে চান?")
                        .setPositiveButton("Yes", (d, w) -> vm.deleteExpense(expense.id))
                        .setNegativeButton("No", null).show();
                }
            );
            
            settlementAdapter = new SettlementAdapter(new SettlementAdapter.OnSettlementClickListener() {
                @Override
                public void onEdit(SettlementEntity s) {
                    android.content.Intent intent = new android.content.Intent(GroupDetailActivity.this, AddSettlementActivity.class);
                    intent.putExtra("GROUP_ID", groupId);
                    intent.putExtra("SETTLEMENT_ID", s.id);
                    intent.putExtra("SETTLEMENT_AMOUNT", s.amount);
                    intent.putExtra("SETTLEMENT_NOTE", s.note);
                    intent.putExtra("SETTLEMENT_DATE", s.date);
                    startActivity(intent);
                }
                @Override
                public void onDelete(SettlementEntity s) {
                    new android.app.AlertDialog.Builder(GroupDetailActivity.this)
                        .setTitle("Delete").setMessage("ডিলিট করতে চান?")
                        .setPositiveButton("Yes", (d, w) -> vm.deleteSettlement(s.id))
                        .setNegativeButton("No", null).show();
                }
            });
            
            balanceAdapter = new BalanceAdapter();
            rv.setAdapter(expenseAdapter);

            // ★ সার্চ বার লজিক
            EditText etSearchExpense = findViewById(R.id.etSearchExpense);
            if (etSearchExpense != null) {
                etSearchExpense.addTextChangedListener(new android.text.TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        expenseAdapter.getFilter().filter(s);
                    }
                    @Override public void afterTextChanged(android.text.Editable s) {}
                });
            }

            TabLayout tabLayout = findViewById(R.id.tabLayout);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) rv.setAdapter(expenseAdapter);
                    else if (tab.getPosition() == 1) rv.setAdapter(settlementAdapter);
                    else rv.setAdapter(balanceAdapter);
                }
                @Override public void onTabUnselected(TabLayout.Tab tab) {}
                @Override public void onTabReselected(TabLayout.Tab tab) {}
            });

            AtomicReference<List<MemberEntity>> members = new AtomicReference<>(Collections.emptyList());
            AtomicReference<List<ExpenseEntity>> expenses = new AtomicReference<>(Collections.emptyList());
            AtomicReference<List<ExpenseShareEntity>> shares = new AtomicReference<>(Collections.emptyList());
            AtomicReference<List<SettlementEntity>> settlements = new AtomicReference<>(Collections.emptyList());

            Runnable recompute = () -> {
                List<com.example.expensesplitter.model.MemberBalance> balances = BalanceCalculator.calculate(
                        members.get(), shares.get(), expenses.get(), settlements.get());
                balanceAdapter.submit(balances);
                
                double totalExp = 0, totalPaid = 0;
                for (ExpenseEntity e : expenses.get()) totalExp += e.amount;
                for (SettlementEntity s : settlements.get()) if ("PAID".equals(s.type)) totalPaid += s.amount;
                
                double totalDue = totalExp - totalPaid;
                tvTotal.setText("৳" + totalExp);
                tvPaid.setText("৳" + totalPaid);
                tvDue.setText("৳" + totalDue);
            };

            vm.members(groupId).observe(this, m -> { members.set(m); recompute.run(); });
            vm.expenses(groupId).observe(this, e -> { 
                expenses.set(e); 
                expenseAdapter.submit(e); 
                recompute.run(); 
            });
            vm.shares(groupId).observe(this, s -> { shares.set(s); recompute.run(); });
            vm.settlements(groupId).observe(this, s -> { 
                settlements.set(s); 
                settlementAdapter.submit(s); 
                recompute.run(); 
            });

            vm.getCategorySums(groupId).observe(this, categorySums -> {
                if (categoryChart != null) {
                    categoryChart.setData(categorySums);
                }
            });

            vm.getDateSums(groupId).observe(this, dateSums -> {
                if (barChart != null && dateSums != null) {
                    List<Float> values = new ArrayList<>();
                    List<String> labels = new ArrayList<>();
                    int size = Math.min(dateSums.size(), 7);
                    for (int i = 0; i < size; i++) {
                        values.add((float) dateSums.get(i).totalAmount);
                        labels.add(dateSums.get(i).date); 
                    }
                    barChart.setData(values, labels);
                }
            });

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

            findViewById(R.id.btnSharePdf).setOnClickListener(v -> {
                try {
                    PdfGenerator.generateAndShare(this, getIntent().getStringExtra("GROUP_NAME"), expenses.get(), settlements.get());
                } catch (Exception e) {
                    Toast.makeText(this, "PDF Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            
        } catch (Exception e) {
            Toast.makeText(this, "Activity Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
