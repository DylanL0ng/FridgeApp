package com.dylanlong.fridgeapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Adapter for the RecyclerView that displays a list of fridge items.
 */
public class FridgeItemListAdapter extends RecyclerView.Adapter<FridgeItemListAdapter.ItemViewHolder> {
    private List<FridgeItem> items = new ArrayList<>();
    private OnItemClickListener listener;

    /**
     * Removes an item from the list at the specified position.
     *
     * @param position The position of the item to be removed.
     * @return The removed {@link FridgeItem} or null if the position is out of bounds.
     */
    public FridgeItem removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            FridgeItem removedItem = items.get(position);
            items.remove(position);
            return removedItem;
        }
        return null;
    }

    /**
     * Creates a new {@link ItemViewHolder} instance by inflating the item view.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The type of the view.
     * @return A new {@link ItemViewHolder} instance.
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    /**
     * Binds data to the views of the {@link ItemViewHolder} at the specified position.
     *
     * @param holder   The {@link ItemViewHolder} instance.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FridgeItem currentItem = items.get(position);

        // Set product name and color
        holder.productName.setText(currentItem.getName());
        holder.productName.setTextColor(Color.LTGRAY);

        // Calculate days until expiry and format the string
        long msDiff = Math.abs(Calendar.getInstance().getTimeInMillis() - currentItem.getExpiry());
        long daysUntil = TimeUnit.MILLISECONDS.toDays(msDiff);

        String daysUntilString = daysUntil + " ";
        if (daysUntil == 1)
            daysUntilString += "day";
        else
            daysUntilString += "days";

        // Set text color based on days until expiry
        if (daysUntil <= 0)
            daysUntilString = "Expired";

        if (daysUntil <= 1)
            holder.productExpiry.setTextColor(Color.RED);
        else if (daysUntil <= 3)
            holder.productExpiry.setTextColor(Color.YELLOW);
        else
            holder.productExpiry.setTextColor(Color.LTGRAY);

        // Set product expiry text
        holder.productExpiry.setText(daysUntilString);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
    }

    /**
     * Gets the total number of items in the list.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Sets the list of items to be displayed in the RecyclerView.
     *
     * @param items The list of {@link FridgeItem}s.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<FridgeItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Interface definition for a callback to be invoked when an item in the RecyclerView is clicked.
     */
    public interface OnItemClickListener {
        void onItemClick(FridgeItem fridgeItem);
    }

    /**
     * Sets the click listener for item clicks.
     *
     * @param listener The {@link OnItemClickListener} to set.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * ViewHolder class for holding references to the views of each item in the RecyclerView.
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productExpiry;

        /**
         * Constructor for the ItemViewHolder.
         *
         * @param itemView The view for a single item in the RecyclerView.
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.foodName);
            productExpiry = itemView.findViewById(R.id.expiryDate);
        }
    }
}