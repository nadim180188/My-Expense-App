package com.example.expensesplitter.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
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

        vm = new ViewModelProvider(this).get(GroupViewModel.class);
        RecyclerView rv = findViewById(R.id.recyclerView);
        GroupAdapter adapter = new GroupAdapter(group -> {
            Intent i = new Intent(this, GroupDetailActivity.class);
            i.putExtra("GROUP_ID", group.id);
            i.putExtra("GROUP_NAME", group.name);
            startActivity(i);
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        vm.groups().observe(this, adapter::submit);

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
