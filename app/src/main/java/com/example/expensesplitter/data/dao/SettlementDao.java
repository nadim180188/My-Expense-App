package com.example.expensesplitter.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.expensesplitter.data.entity.SettlementEntity;
import java.util.List;

@Dao
public interface SettlementDao {
    @Insert long insert(SettlementEntity s);
    @Delete void delete(SettlementEntity s);

    @Query("SELECT * FROM settlements WHERE groupId = :gid ORDER BY date DESC")
    LiveData<List<SettlementEntity>> ofGroup(long gid);
}
