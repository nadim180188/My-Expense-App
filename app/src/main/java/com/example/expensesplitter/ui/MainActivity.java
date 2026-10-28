package com.example.expensesplitter.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
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
        // ... (আগের কোড)
        vm.groupsWithTotal().observe(this, adapter::submit);

        // গ্রাফ অ্যানিমেশন শুরু
        PieChartView pieChart = findViewById(R.id.pieChart);
        pieChart.animateChart();

// ... (আগের কোড)

        vm = new ViewModelProvider(this).get(GroupViewModel.class);
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
                        vm.deleteGroup(group.id); // নিচে ViewModel-এ মেথড যোগ করব
                        Toast.makeText(this, "গ্রুপ ডিলিট হয়েছে", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null).show();
            }
        );
                FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateGroupActivity.class));
        });
        
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        vm.groupsWithTotal().observe(this, adapter::submit);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            EditText et = new EditText(this);
            et.setHint("Group Name");
            new AlertDialog.Builder(this)
                    .setTitle("New Group")
                    .setView(et)
                    .setPositiveButton("Create", (d, w) -> vm.addGroup(et.getText().toString()))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
