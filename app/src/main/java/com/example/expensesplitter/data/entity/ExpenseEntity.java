package com.example.expensesplitter.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses",
        foreignKeys = @ForeignKey(entity = GroupEntity.class,
                parentColumns = "id", childColumns = "groupId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("groupId")})
public class ExpenseEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long groupId;
    public String title;
    public double amount;
    public long paidByMemberId;
    public long createdAt;
    public String category;
    
    // ★ নতুন যোগ করা হলো: খরচটি সেটেল (শোধ) হয়েছে কিনা
    public boolean isSettled = false; 

    public ExpenseEntity(long groupId, String title, double amount, long paidByMemberId, String category) {
        this.groupId = groupId;
        this.title = title;
        this.amount = amount;
        this.paidByMemberId = paidByMemberId;
        this.category = category;
        this.createdAt = System.currentTimeMillis();
    }
}
