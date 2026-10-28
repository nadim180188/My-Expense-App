package com.example.expensesplitter.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "groups")
public class GroupEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String type;      // ক্যাটাগরি (যেমন: Trip)
    public String icon;      // ইমোজি (যেমন: ✈️)
    public String currency;  // কারেন্সি (যেমন: ৳ BDT)
    public long startDate;
    public long endDate;
    public long createdAt;

    public GroupEntity(String name, String type, String icon, String currency, long startDate, long endDate) {
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.currency = currency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = System.currentTimeMillis();
    }
}
