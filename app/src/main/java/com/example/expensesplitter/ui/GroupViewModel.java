package com.example.expensesplitter.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.example.expensesplitter.data.ExpenseRepository;
import com.example.expensesplitter.data.entity.*;
import com.example.expensesplitter.model.GroupWithExpense;
import com.example.expensesplitter.model.CategorySum;
import com.example.expensesplitter.model.DateSum;
import java.util.List;

public class GroupViewModel extends AndroidViewModel {
    private final ExpenseRepository repo;

    public GroupViewModel(@NonNull Application app) {
        super(app);
        repo = new ExpenseRepository(app);
    }

    public void updateExpense(ExpenseEntity e, List<ExpenseShareEntity> shares) {
        new Thread(() -> repo.expenses.updateExpenseWithShares(e, shares)).start();
    }

    public LiveData<List<GroupWithExpense>> groupsWithTotal() { return repo.allGroupsWithTotal(); }
    public void deleteGroup(long groupId) { new Thread(() -> repo.deleteGroupById(groupId)).start(); }

    public void addCustomGroup(GroupEntity group) {
        new Thread(() -> {
            long gid = repo.groups.insert(group);
            repo.members.insert(new MemberEntity(gid, "আমি")); 
        }).start();
    }

    public LiveData<List<MemberEntity>> members(long gid) { return repo.membersOf(gid); }
    public LiveData<List<ExpenseEntity>> expenses(long gid) { return repo.expensesOf(gid); }
    public LiveData<List<ExpenseShareEntity>> shares(long gid) { return repo.sharesOf(gid); }
    public LiveData<List<SettlementEntity>> settlements(long gid) { return repo.settlementsOf(gid); }
    public LiveData<List<CategorySum>> getCategorySums(long gid) { return repo.getCategorySums(gid); }
    public LiveData<List<DateSum>> getDateSums(long gid) { return repo.getDateSums(gid); }

    public void addMember(long gid, String name) { new Thread(() -> repo.members.insert(new MemberEntity(gid, name))).start(); }
    public void addExpense(ExpenseEntity e, List<ExpenseShareEntity> shares) { new Thread(() -> repo.expenses.insertWithShares(e, shares)).start(); }
    public void addSettlement(SettlementEntity s) { new Thread(() -> repo.settlements.insert(s)).start(); }
    public void deleteExpense(long expenseId) { new Thread(() -> repo.deleteExpenseById(expenseId)).start(); }
    public void deleteSettlement(long id) { new Thread(() -> repo.settlements.deleteById(id)).start(); }
    public void updateSettlement(SettlementEntity s) { new Thread(() -> repo.settlements.update(s)).start(); }

    // ★ টোটাল সেটেলমেন্ট লজিক (সব খরচ সেটেল + একটি Settlement তৈরি)
    public void settleAllExpenses(long groupId, double dueAmount) {
        new Thread(() -> {
            repo.expenses.markAllAsSettled(groupId);
            if (dueAmount > 0) {
                SettlementEntity s = new SettlementEntity(groupId, 0, dueAmount, "PAID", "Settled All");
                repo.settlements.insert(s);
            }
        }).start();
    }

    // ★ সিলেক্টেড সেটেলমেন্ট লজিক (শুধু সিলেক্ট করা খরচ সেটেল + Settlement তৈরি)
    public void settleSelectedExpenses(long groupId, List<Long> expenseIds) {
        new Thread(() -> {
            List<ExpenseEntity> selected = repo.expenses.getExpensesByIds(expenseIds);
            double totalAmount = 0;
            for (ExpenseEntity e : selected) totalAmount += e.amount;

            repo.expenses.markAsSettled(expenseIds);
            
            if (totalAmount > 0) {
                SettlementEntity s = new SettlementEntity(groupId, 0, totalAmount, "PAID", "Settled Selected (" + selected.size() + " items)");
                repo.settlements.insert(s);
            }
        }).start();
    }

    public LiveData<List<CategoryEntity>> getCategories() { return repo.getCategories(); }
    public void addCategory(CategoryEntity c) { new Thread(() -> repo.addCategory(c)).start(); }
    public void deleteCategory(long id) { new Thread(() -> repo.deleteCategoryById(id)).start(); }
}
