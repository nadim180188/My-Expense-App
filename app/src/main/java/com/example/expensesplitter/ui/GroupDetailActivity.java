package com.example.expensesplitter.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.*;
import com.example.expensesplitter.model.BalanceCalculator;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class GroupDetailActivity extends AppCompatActivity {
    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        
        groupId = getIntent().getLongExtra("GROUP_ID", -1);
        setTitle(getIntent().getStringExtra("GROUP_NAME"));

        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);
        
        RecyclerView rv = findViewById(R.id.viewPager); 
        rv.setLayoutManager(new LinearLayoutManager(this));
        BalanceAdapter adapter = new BalanceAdapter();
        rv.setAdapter(adapter);

        AtomicReference<List<MemberEntity>> members = new AtomicReference<>(Collections.emptyList());
        AtomicReference<List<ExpenseEntity>> expenses = new AtomicReference<>(Collections.emptyList());
        AtomicReference<List<ExpenseShareEntity>> shares = new AtomicReference<>(Collections.emptyList());
        AtomicReference<List<SettlementEntity>> settlements = new AtomicReference<>(Collections.emptyList());

        Runnable recompute = () -> adapter.submit(BalanceCalculator.calculate(
                members.get(), shares.get(), expenses.get(), settlements.get()));

        vm.members(groupId).observe(this, m -> { members.set(m); recompute.run(); });
        vm.expenses(groupId).observe(this, e -> { expenses.set(e); recompute.run(); });
        vm.shares(groupId).observe(this, s -> { shares.set(s); recompute.run(); });
        vm.settlements(groupId).observe(this, s -> { settlements.set(s); recompute.run(); });
    }
}
