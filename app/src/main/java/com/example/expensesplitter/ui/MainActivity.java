package com.example.expensesplitter.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private GroupViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ১. ViewModel Initialize
        vm = new ViewModelProvider(this).get(GroupViewModel.class);

        // ২. RecyclerView সেটআপ
        RecyclerView rv = findViewById(R.id.recyclerView);
        GroupAdapter adapter = new GroupAdapter(
            group -> {
                Intent i = new Intent(this, GroupDetailActivity.class);
                i.putExtra("GROUP_ID", group.id);
                i.putExtra("GROUP_NAME", group.name);
                startActivity(i);
            },
            group -> {
                new AlertDialog.Builder(this)
                    .setTitle("Delete Group")
                    .setMessage("আপনি কি নিশ্চিতভাবে এই গ্রুপটি ডিলিট করতে চান?")
                    .setPositiveButton("Yes", (d, w) -> {
                        vm.deleteGroup(group.id);
                        Toast.makeText(this, "গ্রুপ ডিলিট হয়েছে", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null).show();
            }
        );
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // ৩. গ্রুপ লিস্ট এবং গ্রাফ আপডেট
        vm.groupsWithTotal().observe(this, groups -> {
            adapter.submit(groups);
            
            // গ্রাফের জন্য আসল ডেটা হিসাব
            double totalPaid = 0;
            double totalDue = 0;
            for (com.example.expensesplitter.model.GroupWithExpense g : groups) {
                totalPaid += (g.totalExpense - g.dueAmount); // Paid = Total - Due
                totalDue += g.dueAmount;
            }
            PieChartView pieChart = findViewById(R.id.pieChart);
            pieChart.setData((float) totalPaid, (float) totalDue);
        });

        // ৪. + বাটনের কাজ (CreateGroupActivity ওপেন করবে)
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateGroupActivity.class));
        });
    }
}
