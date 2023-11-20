package com.dylanlong.fridgeapp;

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
import java.util.Date;
import java.util.List;

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
        int daysUntil = getDaysUntilTimestamp(currentItem.getExpiry());
        String daysUntilString = daysUntil + " ";
        if (daysUntil == 1)
            daysUntilString += "day";
        else
            daysUntilString += "days";

        if (daysUntil < 0)
            daysUntilString = "Expired";

        holder.productExpiry.setText(daysUntilString);
    }

    private static int getDaysUntilTimestamp(Date timestamp) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O)
            return 1;

        Instant now = Instant.now();
        Instant target = timestamp.toInstant();

        LocalDate nowDate = now.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate targetDate = target.atZone(ZoneId.systemDefault()).toLocalDate();

        // Calculate the difference in days
        Duration duration = Duration.between(nowDate.atStartOfDay(), targetDate.atStartOfDay());
        return (int) duration.toDays();
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