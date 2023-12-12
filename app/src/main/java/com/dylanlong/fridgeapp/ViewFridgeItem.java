package com.dylanlong.fridgeapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * AndroidViewModel class that serves as an intermediary between the UI and the data layer.
 * It provides access to the data and business logic for displaying and managing fridge items.
 */
public class ViewFridgeItem extends AndroidViewModel {

    private FridgeRepository fridgeRepository;
    private LiveData<List<FridgeItem>> allItems;

    /**
     * Constructor for the ViewFridgeItem AndroidViewModel.
     *
     * @param application The application context.
     */
    public ViewFridgeItem(@NonNull Application application) {
        super(application);
        fridgeRepository = new FridgeRepository(application);
        allItems = fridgeRepository.getAllItems();
    }

    /**
     * Inserts a new fridge item into the repository.
     *
     * @param fridgeItem The fridge item to be inserted.
     */
    public void insert(FridgeItem fridgeItem) { fridgeRepository.insert(fridgeItem); }

    /**
     * Updates an existing fridge item in the repository.
     *
     * @param fridgeItem The fridge item to be updated.
     */
    public void update(FridgeItem fridgeItem) { fridgeRepository.update(fridgeItem); }

    /**
     * Deletes a specific fridge item from the repository.
     *
     * @param fridgeItem The fridge item to be deleted.
     */
    public void delete(FridgeItem fridgeItem) { fridgeRepository.delete(fridgeItem); }

    /**
     * Deletes all fridge items from the repository.
     */
    public void deleteAllItems() { fridgeRepository.deleteAllItems(); }

    /**
     * Retrieves a LiveData list of all fridge items from the repository.
     *
     * @return A LiveData list of all fridge items.
     */
    public LiveData<List<FridgeItem>> getAllItems() { return allItems; }

    /**
     * Retrieves a LiveData instance of a specific fridge item based on its ID from the repository.
     *
     * @param id The ID of the fridge item.
     * @return A LiveData instance of the specific fridge item.
     */
    public LiveData<FridgeItem> getItemById(int id) { return fridgeRepository.getFoodItem(id); }
}
