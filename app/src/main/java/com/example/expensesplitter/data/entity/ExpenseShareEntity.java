package com.example.expensesplitter.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_shares",
        foreignKeys = @ForeignKey(entity = ExpenseEntity.class,
                parentColumns = "id", childColumns = "expenseId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("expenseId"), @Index("memberId")})
public class ExpenseShareEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long expenseId;
    public long memberId;
    public double shareAmount;

    public ExpenseShareEntity(long expenseId, long memberId, double shareAmount) {
        this.expenseId = expenseId;
        this.memberId = memberId;
        this.shareAmount = shareAmount;
    }
}
