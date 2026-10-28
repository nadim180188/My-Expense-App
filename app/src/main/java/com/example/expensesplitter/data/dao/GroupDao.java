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

    @Query("SELECT g.id, g.name, g.icon, g.createdAt, " +
           "(IFNULL((SELECT SUM(amount) FROM expenses WHERE groupId = g.id), 0) - " +
           "IFNULL((SELECT SUM(amount) FROM settlements WHERE groupId = g.id AND type = 'PAID'), 0)) as dueAmount, " +
           "IFNULL((SELECT SUM(amount) FROM expenses WHERE groupId = g.id), 0) as totalExpense " +
           "FROM groups g ORDER BY g.createdAt DESC")
    LiveData<List<GroupWithExpense>> getAllWithTotal();
}
