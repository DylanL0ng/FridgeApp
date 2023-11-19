package com.dylanlong.fridgeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FridgeItemListAdapter extends RecyclerView.Adapter<FridgeItemListAdapter.ItemViewHolder> {
    private List<FridgeItem> items = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FridgeItem currentItem = items.get(position);

        holder.productName.setText(currentItem.getName());
        holder.productExpiry.setText(currentItem.getExpiry());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<FridgeItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(FridgeItem fridgeItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productExpiry;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.foodName);
            productExpiry = itemView.findViewById(R.id.expiryDate);
        }
    }
}