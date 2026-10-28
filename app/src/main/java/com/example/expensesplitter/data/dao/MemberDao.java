package com.example.expensesplitter.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.expensesplitter.data.entity.MemberEntity;
import java.util.List;

@Dao
public interface MemberDao {
    @Insert long insert(MemberEntity m);
    @Delete void delete(MemberEntity m);

    @Query("SELECT * FROM members WHERE groupId = :gid ORDER BY id ASC")
    LiveData<List<MemberEntity>> ofGroup(long gid);
}
