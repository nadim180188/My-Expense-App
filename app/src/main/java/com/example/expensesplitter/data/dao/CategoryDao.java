package com.example.expensesplitter.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.expensesplitter.data.entity.CategoryEntity;
import java.util.List;

@Dao
public interface CategoryDao {
    @Insert long insert(CategoryEntity category);
    @Query("DELETE FROM categories WHERE id = :id") void deleteById(long id);
    @Query("SELECT * FROM categories ORDER BY id ASC") LiveData<List<CategoryEntity>> getAll();
}
