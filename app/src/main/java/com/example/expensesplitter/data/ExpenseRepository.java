package com.example.expensesplitter.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.expensesplitter.data.dao.*;
import com.example.expensesplitter.data.entity.*;
import com.example.expensesplitter.model.GroupWithExpense;
import java.util.List;

public class ExpenseRepository {
    public final GroupDao groups;
    public final MemberDao members;
    public final ExpenseDao expenses;
    public final SettlementDao settlements;

    public ExpenseRepository(Context ctx) {
        AppDatabase db = AppDatabase.get(ctx);
        groups = db.groupDao();
        members = db.memberDao();
        expenses = db.expenseDao();
        settlements = db.settlementDao();
    }

    // হোম স্ক্রিনের জন্য টোটাল সহ গ্রুপ লিস্ট
    public LiveData<List<GroupWithExpense>> allGroupsWithTotal() {
        return groups.getAllWithTotal();
    }

    // ডিলিট করার মেথড
    public void deleteGroupById(long groupId) {
        groups.deleteById(groupId);
    }

    public LiveData<List<MemberEntity>> membersOf(long gid) { return members.ofGroup(gid); }
    public LiveData<List<ExpenseEntity>> expensesOf(long gid) { return expenses.ofGroup(gid); }
    public LiveData<List<ExpenseShareEntity>> sharesOf(long gid) { return expenses.sharesOfGroup(gid); }
    public LiveData<List<SettlementEntity>> settlementsOf(long gid) { return settlements.ofGroup(gid); }
}
