package com.example.expensesplitter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.SettlementEntity;
import java.text.SimpleDateFormat;
import java.util.*;

public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.VH> {
    private final List<SettlementEntity> list = new ArrayList<>();
    private final OnSettlementClickListener listener;

    public interface OnSettlementClickListener {
        void onEdit(SettlementEntity s);
        void onDelete(SettlementEntity s);
    }

    public SettlementAdapter(OnSettlementClickListener listener) { this.listener = listener; }

    public void submit(List<SettlementEntity> data) {
        list.clear(); list.addAll(data); notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_settlement, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        SettlementEntity s = list.get(pos);
        h.amount.setText(String.format(Locale.US, "৳%.0f", s.amount));
        h.note.setText(s.note != null && !s.note.isEmpty() ? s.note : "No Note");
        
        Calendar cal = Calendar.getInstance(); 
        cal.setTimeInMillis(s.date);
        
        // তারিখ সেট
        h.dateDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        h.dateMonth.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime()));
        
        // ★ টাইম সেট
        h.time.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.getTime()));
        
        h.btnEdit.setOnClickListener(v -> listener.onEdit(s));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(s));

        // ★ স্লাইড-ইন অ্যানিমেশন
        h.itemView.setTranslationY(100f);
        h.itemView.setAlpha(0f);
        h.itemView.animate().translationY(0f).alpha(1f).setDuration(400).setStartDelay(pos * 50L).start();
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView amount, note, dateDay, dateMonth, time; 
        ImageView btnEdit, btnDelete;
        VH(View v) {
            super(v);
            amount = v.findViewById(R.id.tvAmount);
            note = v.findViewById(R.id.tvNote);
            dateDay = v.findViewById(R.id.tvDateDay);
            dateMonth = v.findViewById(R.id.tvDateMonth);
            time = v.findViewById(R.id.tvTime); // নতুন টাইম ভিউ
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
