package com.example.expensesplitter.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;

    public CategoryEntity(String name) {
        this.name = name;
    }
}
