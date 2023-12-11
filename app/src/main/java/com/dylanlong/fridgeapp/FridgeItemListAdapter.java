package com.dylanlong.fridgeapp;

import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FridgeItemListAdapter extends RecyclerView.Adapter<FridgeItemListAdapter.ItemViewHolder> {
    private List<FridgeItem> items = new ArrayList<>();
    private OnItemClickListener listener;

    public FridgeItem removeItem(int position) {
        FridgeItem removedItem = items.get(position);
        items.remove(position);
        return removedItem;
    }

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

        long msDiff = Math.abs(Calendar.getInstance().getTimeInMillis() - currentItem.getExpiry());
        long daysUntil = TimeUnit.MILLISECONDS.toDays(msDiff);

        String daysUntilString = daysUntil + " ";
        if (daysUntil == 1)
            daysUntilString += "day";
        else
            daysUntilString += "days";

        if (daysUntil <= 0)
            daysUntilString = "Expired";

        holder.productExpiry.setText(daysUntilString);
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