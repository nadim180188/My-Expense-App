package com.example.expensesplitter.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private GroupViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ★ ১. সময় অনুযায়ী গ্রিটিংস সেট করা
        updateGreeting();

        // ২. ViewModel Initialize
        vm = new ViewModelProvider(this).get(GroupViewModel.class);

        // ৩. RecyclerView সেটআপ
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

        // ৪. গ্রুপ লিস্ট এবং গ্রাফ আপডেট
        vm.groupsWithTotal().observe(this, groups -> {
            adapter.submit(groups);
            
            double totalPaid = 0;
            double totalDue = 0;
            for (com.example.expensesplitter.model.GroupWithExpense g : groups) {
                totalPaid += (g.totalExpense - g.dueAmount); 
                totalDue += g.dueAmount;
            }
            PieChartView pieChart = findViewById(R.id.pieChart);
            pieChart.setData((float) totalPaid, (float) totalDue);
        });

        // ৫. + বাটনের কাজ
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateGroupActivity.class));
        });
    }

    // ★ গ্রিটিংস আপডেট করার মেথড
    private void updateGreeting() {
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greeting = "Hello, Good Morning ☀️";
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            greeting = "Hello, Good Afternoon 🌤️";
        } else if (timeOfDay >= 17 && timeOfDay < 21) {
            greeting = "Hello, Good Evening 🌆";
        } else {
            greeting = "Hello, Good Night 🌙";
        }

        tvGreeting.setText(greeting);
    }
}
