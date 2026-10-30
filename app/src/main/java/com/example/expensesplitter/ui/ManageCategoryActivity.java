package com.example.expensesplitter.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.CategoryEntity;

public class ManageCategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);
        setTitle("Manage Categories");

        GroupViewModel vm = new ViewModelProvider(this).get(GroupViewModel.class);
        EditText etNew = findViewById(R.id.etNewCategory);
        Button btnAdd = findViewById(R.id.btnAddCategory);
        RecyclerView rv = findViewById(R.id.rvCategories);

        CategoryAdapter adapter = new CategoryAdapter(category -> vm.deleteCategory(category.id));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        vm.getCategories().observe(this, adapter::submit);

        btnAdd.setOnClickListener(v -> {
            String name = etNew.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "নাম দিন", Toast.LENGTH_SHORT).show();
                return;
            }
            vm.addCategory(new CategoryEntity(name));
            etNew.setText("");
        });
    }
}
