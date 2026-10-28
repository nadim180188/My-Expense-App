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
        h.amount.setText("৳" + s.amount);
        h.note.setText(s.note != null ? s.note : "No Note");
        Calendar cal = Calendar.getInstance(); cal.setTimeInMillis(s.date);
        h.dateDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        
        h.btnEdit.setOnClickListener(v -> listener.onEdit(s));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(s));
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView amount, note, dateDay; ImageView btnEdit, btnDelete;
        VH(View v) {
            super(v);
            amount = v.findViewById(R.id.tvAmount);
            note = v.findViewById(R.id.tvNote);
            dateDay = v.findViewById(R.id.tvDateDay);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
