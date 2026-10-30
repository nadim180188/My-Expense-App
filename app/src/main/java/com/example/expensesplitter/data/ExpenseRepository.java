package com.example.expensesplitter.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.expensesplitter.data.dao.*;
import com.example.expensesplitter.data.entity.*;
import com.example.expensesplitter.model.CategorySum;
import com.example.expensesplitter.model.DateSum;
import com.example.expensesplitter.model.GroupWithExpense;
import java.util.List;

public class ExpenseRepository {
    public final GroupDao groups;
    public final MemberDao members;
    public final ExpenseDao expenses;
    public final SettlementDao settlements;
    public final CategoryDao categories;

    public ExpenseRepository(Context ctx) {
        AppDatabase db = AppDatabase.get(ctx);
        groups = db.groupDao();
        members = db.memberDao();
        expenses = db.expenseDao();
        settlements = db.settlementDao();
        categories = db.categoryDao();
    }

    public LiveData<List<GroupWithExpense>> allGroupsWithTotal() { return groups.getAllWithTotal(); }
    public void deleteGroupById(long groupId) { groups.deleteById(groupId); }
    public void deleteExpenseById(long expenseId) { expenses.deleteById(expenseId); }
    
    public void markExpensesAsSettled(List<Long> ids) { expenses.markAsSettled(ids); }
    public void markAllAsSettled(long gid) { expenses.markAllAsSettled(gid); }
    public List<ExpenseEntity> getExpensesByIds(List<Long> ids) { return expenses.getExpensesByIds(ids); }

    public LiveData<List<MemberEntity>> membersOf(long gid) { return members.ofGroup(gid); }
    public LiveData<List<ExpenseEntity>> expensesOf(long gid) { return expenses.ofGroup(gid); }
    public LiveData<List<ExpenseShareEntity>> sharesOf(long gid) { return expenses.sharesOfGroup(gid); }
    public LiveData<List<SettlementEntity>> settlementsOf(long gid) { return settlements.ofGroup(gid); }
    
    public LiveData<List<CategorySum>> getCategorySums(long gid) { return expenses.getCategorySums(gid); }
    public LiveData<List<DateSum>> getDateSums(long gid) { return expenses.getDateSums(gid); }

    public LiveData<List<CategoryEntity>> getCategories() { return categories.getAll(); }
    public void addCategory(CategoryEntity c) { categories.insert(c); }
    public void deleteCategoryById(long id) { categories.deleteById(id); }
}
