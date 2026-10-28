package com.example.expensesplitter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.model.GroupWithExpense;
import java.util.*;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.VH> {
    private final List<GroupWithExpense> list = new ArrayList<>();
    private final OnGroupClickListener clickListener;
    private final OnGroupDeleteListener deleteListener;

    public interface OnGroupClickListener { void onClick(GroupWithExpense group); }
    public interface OnGroupDeleteListener { void onDelete(GroupWithExpense group); }

    public GroupAdapter(OnGroupClickListener clickListener, OnGroupDeleteListener deleteListener) {
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    public void submit(List<GroupWithExpense> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_group, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        GroupWithExpense g = list.get(pos);
        h.name.setText(g.name);
        h.expense.setText(String.format("Total Expense : ৳%.0f", g.totalExpense));
        h.itemView.setOnClickListener(v -> clickListener.onClick(g));
        
        // ডিলিট বাটনের কাজ
        h.btnDelete.setOnClickListener(v -> deleteListener.onDelete(g));
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, expense;
        ImageView btnDelete;
        VH(View v) {
            super(v);
            name = v.findViewById(R.id.tvGroupName);
            expense = v.findViewById(R.id.tvTotalExpense);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
