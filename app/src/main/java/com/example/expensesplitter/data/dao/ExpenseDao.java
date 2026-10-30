package com.example.expensesplitter.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.expensesplitter.data.entity.ExpenseEntity;
import com.example.expensesplitter.data.entity.ExpenseShareEntity;
import com.example.expensesplitter.model.CategorySum;
import com.example.expensesplitter.model.DateSum;
import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert long insertExpense(ExpenseEntity e);
    @Insert void insertShares(List<ExpenseShareEntity> s);

    @Update void updateExpense(ExpenseEntity e);
    
    @Query("DELETE FROM expense_shares WHERE expenseId = :expenseId")
    void deleteShares(long expenseId);

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    void deleteById(long expenseId);

    @Transaction
    default long insertWithShares(ExpenseEntity e, List<ExpenseShareEntity> shares) {
        long id = insertExpense(e);
        for (ExpenseShareEntity s : shares) s.expenseId = id;
        insertShares(shares);
        return id;
    }

    @Transaction
    default long updateExpenseWithShares(ExpenseEntity e, List<ExpenseShareEntity> shares) {
        updateExpense(e);
        deleteShares(e.id);
        for (ExpenseShareEntity s : shares) s.expenseId = e.id;
        insertShares(shares);
        return e.id;
    }

    @Query("SELECT * FROM expenses WHERE groupId = :gid ORDER BY createdAt DESC")
    LiveData<List<ExpenseEntity>> ofGroup(long gid);

    @Query("SELECT * FROM expense_shares WHERE expenseId IN (SELECT id FROM expenses WHERE groupId = :gid)")
    LiveData<List<ExpenseShareEntity>> sharesOfGroup(long gid);

    // ★ ক্যাটাগরি সামারি (পাই চার্টের জন্য)
    @Query("SELECT category, SUM(amount) as totalAmount FROM expenses WHERE groupId = :gid GROUP BY category")
    LiveData<List<CategorySum>> getCategorySums(long gid);

    // ★ তারিখ অনুযায়ী সামারি (বার চার্টের জন্য)
    @Query("SELECT strftime('%d', datetime(createdAt/1000, 'unixepoch')) as date, SUM(amount) as totalAmount " +
           "FROM expenses WHERE groupId = :gid GROUP BY date ORDER BY date ASC")
    LiveData<List<DateSum>> getDateSums(long gid);
}
