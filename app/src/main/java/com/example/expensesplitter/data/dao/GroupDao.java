package com.example.expensesplitter.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.expensesplitter.data.entity.GroupEntity;
import java.util.List;

@Dao
public interface GroupDao {
    @Insert long insert(GroupEntity g);
    @Delete void delete(GroupEntity g);

    @Query("SELECT * FROM groups ORDER BY createdAt DESC")
    LiveData<List<GroupEntity>> getAll();
}
