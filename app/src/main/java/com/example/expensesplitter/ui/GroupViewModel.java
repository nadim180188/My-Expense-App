package com.example.expensesplitter.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.example.expensesplitter.data.ExpenseRepository;
import com.example.expensesplitter.data.entity.*;
import com.example.expensesplitter.model.GroupWithExpense;
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

    public LiveData<List<GroupWithExpense>> groupsWithTotal() { 
        return repo.allGroupsWithTotal(); 
    }

    public void deleteGroup(long groupId) {
        new Thread(() -> repo.deleteGroupById(groupId)).start();
    }

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

    public void addMember(long gid, String name) {
        new Thread(() -> repo.members.insert(new MemberEntity(gid, name))).start();
    }
    
    public void addExpense(ExpenseEntity e, List<ExpenseShareEntity> shares) {
        new Thread(() -> repo.expenses.insertWithShares(e, shares)).start();
    }
    
    public void addSettlement(SettlementEntity s) {
        new Thread(() -> repo.settlements.insert(s)).start();
    }

    // ★ নতুন যোগ করা মেথড
    public void deleteExpense(long expenseId) {
        new Thread(() -> repo.deleteExpenseById(expenseId)).start();
    }
}
