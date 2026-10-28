package com.example.expensesplitter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.GroupEntity;
import java.util.*;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.VH> {
    private final List<GroupEntity> list = new ArrayList<>();
    private final OnGroupClickListener listener;

    public interface OnGroupClickListener { void onClick(GroupEntity group); }

    public GroupAdapter(OnGroupClickListener listener) { this.listener = listener; }

    public void submit(List<GroupEntity> data) {
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
        GroupEntity g = list.get(pos);
        h.name.setText(g.name);
        h.itemView.setOnClickListener(v -> listener.onClick(g));
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name;
        VH(View v) { super(v); name = v.findViewById(R.id.tvGroupName); }
    }
}
