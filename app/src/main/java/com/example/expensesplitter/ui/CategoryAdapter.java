package com.example.expensesplitter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensesplitter.R;
import com.example.expensesplitter.data.entity.CategoryEntity;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {
    private final List<CategoryEntity> list = new ArrayList<>();
    private final OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener { void onDelete(CategoryEntity category); }
    public CategoryAdapter(OnDeleteClickListener listener) { this.deleteListener = listener; }

    public void submit(List<CategoryEntity> data) {
        list.clear(); list.addAll(data); notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_category, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        CategoryEntity c = list.get(pos);
        h.name.setText(c.name);
        h.btnDelete.setOnClickListener(v -> deleteListener.onDelete(c));
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name; ImageView btnDelete;
        VH(View v) {
            super(v);
            name = v.findViewById(R.id.tvCategoryName);
            btnDelete = v.findViewById(R.id.btnDeleteCat);
        }
    }
}
