package com.example.expensesplitter.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "members",
        foreignKeys = @ForeignKey(entity = GroupEntity.class,
                parentColumns = "id", childColumns = "groupId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("groupId")})
public class MemberEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long groupId;
    public String name;

    public MemberEntity(long groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }
}
