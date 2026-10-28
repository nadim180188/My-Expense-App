package com.example.expensesplitter.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.model.MemberBalance;
import java.util.*;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.VH> {
    private final List<MemberBalance> list = new ArrayList<>();

    public void submit(List<MemberBalance> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_balance, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        MemberBalance b = list.get(pos);
        h.name.setText(b.name);
        h.share.setText(String.format("মোট খরচ: ৳%.2f", b.totalShare));
        h.settled.setText(String.format("শোধ: ৳%.2f", b.totalSettled));
        h.remaining.setText(String.format("বাকি: ৳%.2f", b.remaining));
        h.remaining.setTextColor(b.remaining > 0.01 ? Color.parseColor("#C62828") : Color.parseColor("#2E7D32"));
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, share, settled, remaining;
        VH(View v) {
            super(v);
            name = v.findViewById(R.id.tvName);
            share = v.findViewById(R.id.tvShare);
            settled = v.findViewById(R.id.tvSettled);
            remaining = v.findViewById(R.id.tvRemaining);
        }
    }
}
