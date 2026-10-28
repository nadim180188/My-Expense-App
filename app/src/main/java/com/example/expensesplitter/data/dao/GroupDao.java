package com.example.expensesplitter.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.expensesplitter.data.entity.GroupEntity;
import com.example.expensesplitter.model.GroupWithExpense;
import java.util.List;

@Dao
public interface GroupDao {
    @Insert long insert(GroupEntity g);
    
    @Query("DELETE FROM groups WHERE id = :groupId")
    void deleteById(long groupId);

    // মেথডটি যেন শুধু একবার থাকে
    @Query("SELECT g.id, g.name, g.icon, g.createdAt, IFNULL(SUM(e.amount), 0) as totalExpense " +
           "FROM groups g LEFT JOIN expenses e ON g.id = e.groupId " +
           "GROUP BY g.id ORDER BY g.createdAt DESC")
    LiveData<List<GroupWithExpense>> getAllWithTotal();
}
