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
import java.util.*;

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
        
        // ক্যাটাগরি সেট করা
        String category = e.category != null ? e.category : "অন্যান্য";
        h.category.setText(category + " • ");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(e.createdAt);
        
        // তারিখ ও সময় সেট
        h.dateDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        h.dateMonth.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime()));
        h.time.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.getTime()));
        h.amount.setText(String.format(Locale.US, "৳%.0f", e.amount));

        h.btnEdit.setOnClickListener(v -> editListener.onEdit(e));
        h.btnDelete.setOnClickListener(v -> deleteListener.onDelete(e));

        // ★ Fade + Slide Left অ্যানিমেশন
        h.itemView.setTranslationX(200f); // ডান দিক থেকে শুরু করবে
        h.itemView.setAlpha(0f);          // অদৃশ্য থাকবে
        h.itemView.animate()
                .translationX(0f)         // নিজের জায়গায় আসবে
                .alpha(1f)                // দৃশ্যমান হবে
                .setDuration(400)
                .setStartDelay(pos * 60L) // একটার পর একটা আসবে
                .start();
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, category, amount, dateDay, dateMonth, time;
        ImageView btnEdit, btnDelete;
        
        VH(View v) {
            super(v);
            title = v.findViewById(R.id.tvTitle);
            category = v.findViewById(R.id.tvCategory);
            amount = v.findViewById(R.id.tvAmount);
            dateDay = v.findViewById(R.id.tvDateDay);
            dateMonth = v.findViewById(R.id.tvDateMonth);
            time = v.findViewById(R.id.tvTime);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
