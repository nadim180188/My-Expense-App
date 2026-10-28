package com.example.expensesplitter.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "groups")
public class GroupEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public long createdAt;

    public GroupEntity(String name) {
        this.name = name;
        this.createdAt = System.currentTimeMillis();
    }
}
