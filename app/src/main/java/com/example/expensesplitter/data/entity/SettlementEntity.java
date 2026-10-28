package com.example.expensesplitter.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "settlements",
        foreignKeys = @ForeignKey(entity = GroupEntity.class,
                parentColumns = "id", childColumns = "groupId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("groupId"), @Index("memberId")})
public class SettlementEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long groupId;
    public long memberId;
    public double amount;
    public String type;   // "PAID" or "RECEIVED"
    public String note;
    public long date;

    public SettlementEntity(long groupId, long memberId, double amount, String type, String note) {
        this.groupId = groupId;
        this.memberId = memberId;
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.date = System.currentTimeMillis();
    }
}
