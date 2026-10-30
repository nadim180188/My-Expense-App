package com.example.expensesplitter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.model.GroupWithExpense;
import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.VH> implements Filterable {
    private final List<GroupWithExpense> list = new ArrayList<>();
    private final List<GroupWithExpense> listFull = new ArrayList<>(); // সার্চের জন্য মূল লিস্ট
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
        listFull.clear();
        listFull.addAll(data);
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
        h.expense.setText(String.format("বাকি : ৳%.0f", g.dueAmount));
        
        if (g.icon != null) h.icon.setText(g.icon);
        else h.icon.setText("🛍️");

        h.itemView.setOnClickListener(v -> clickListener.onClick(g));
        h.btnDelete.setOnClickListener(v -> deleteListener.onDelete(g));

        // ★ স্লাইড-ইন অ্যানিমেশন (নিচে দেওয়া হবে ধাপ ২-তে, আপাতত হোমের জন্য সাধারণ ফেড)
        h.itemView.setAlpha(0f);
        h.itemView.animate().alpha(1f).setDuration(300).setStartDelay(pos * 50L).start();
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, expense, icon;
        ImageView btnDelete;
        VH(View v) {
            super(v);
            name = v.findViewById(R.id.tvGroupName);
            expense = v.findViewById(R.id.tvTotalExpense);
            icon = v.findViewById(R.id.tvGroupIcon);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    // ★ সার্চ ফিল্টার লজিক
    @Override
    public Filter getFilter() {
        return groupFilter;
    }

    private final Filter groupFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<GroupWithExpense> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (GroupWithExpense item : listFull) {
                    if (item.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
