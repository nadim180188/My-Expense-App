package com.example.expensesplitter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.ExpenseEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.VH> {
    private final List<ExpenseEntity> list = new ArrayList<>();
    private final OnExpenseEditListener editListener;
    private final OnExpenseDeleteListener deleteListener;

    public interface OnExpenseEditListener { void onEdit(ExpenseEntity expense); }
    public interface OnExpenseDeleteListener { void onDelete(ExpenseEntity expense); }

    public ExpenseAdapter(OnExpenseEditListener editListener, OnExpenseDeleteListener deleteListener) {
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    public void submit(List<ExpenseEntity> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_expense, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ExpenseEntity e = list.get(pos);
        h.title.setText(e.title);
        h.paidBy.setText("Paid by You");
        h.amount.setText(String.format("৳%.0f", e.amount));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(e.createdAt);
        h.dateDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        h.dateMonth.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime()));

        // ক্লিক লিসেনার (এগুলো onBindViewHolder-এর ভেতরে আছে)
        h.btnEdit.setOnClickListener(v -> editListener.onEdit(e));
        h.btnDelete.setOnClickListener(v -> deleteListener.onDelete(e));
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, paidBy, amount, dateDay, dateMonth;
        ImageView btnEdit, btnDelete;
        
        VH(View v) {
            super(v);
            title = v.findViewById(R.id.tvTitle);
            paidBy = v.findViewById(R.id.tvPaidBy);
            amount = v.findViewById(R.id.tvAmount);
            dateDay = v.findViewById(R.id.tvDateDay);
            dateMonth = v.findViewById(R.id.tvDateMonth);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
