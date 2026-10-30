package com.example.expensesplitter.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.expensesplitter.data.dao.*;
import com.example.expensesplitter.data.entity.*;

@Database(entities = {
        GroupEntity.class, MemberEntity.class,
        ExpenseEntity.class, ExpenseShareEntity.class,
        SettlementEntity.class
}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract GroupDao groupDao();
    public abstract MemberDao memberDao();
    public abstract ExpenseDao expenseDao();
    public abstract SettlementDao settlementDao();

    public static AppDatabase get(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            ctx.getApplicationContext(),
                            AppDatabase.class, "expense_splitter.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
