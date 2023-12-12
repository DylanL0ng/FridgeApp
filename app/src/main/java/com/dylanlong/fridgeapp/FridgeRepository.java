package com.dylanlong.fridgeapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Repository class that acts as a mediator between the ViewModel and the data sources.
 * It provides an abstraction layer for accessing data from different sources.
 */
public class FridgeRepository {
    private FridgeDAO fridgeDAO;
    private LiveData<List<FridgeItem>> allItems;

    /**
     * Constructor for the FridgeRepository.
     *
     * @param application The application context.
     */
    public FridgeRepository(Application application) {
        ProductDatabase database = ProductDatabase.getInstance(application);
        fridgeDAO = database.fridgeDAO();
        allItems = fridgeDAO.getAllItems();
    }

    /**
     * Inserts a new fridge item asynchronously.
     *
     * @param fridgeItem The fridge item to be inserted.
     */
    public void insert(FridgeItem fridgeItem) {
        new InsertItemAsync(fridgeDAO).execute(fridgeItem);
    }

    /**
     * Deletes all fridge items asynchronously.
     */
    public void deleteAllItems() {
        new DeleteAllItemsAsync(fridgeDAO).execute();
    }

    /**
     * Deletes a specific fridge item asynchronously.
     *
     * @param fridgeItem The fridge item to be deleted.
     */
    public void delete(FridgeItem fridgeItem) {
        new DeleteItemAsync(fridgeDAO).execute(fridgeItem);
    }

    /**
     * Updates a specific fridge item asynchronously.
     *
     * @param fridgeItem The fridge item to be updated.
     */
    public void update(FridgeItem fridgeItem) {
        new UpdateItemAsync(fridgeDAO).execute(fridgeItem);
    }

    /**
     * Retrieves all fridge items.
     *
     * @return A LiveData list of all fridge items.
     */
    public LiveData<List<FridgeItem>> getAllItems() {
        return allItems;
    }

    /**
     * Retrieves a specific fridge item based on its ID.
     *
     * @param id The ID of the fridge item.
     * @return A LiveData instance of the specific fridge item.
     */
    public LiveData<FridgeItem> getFoodItem(int id) {
        return fridgeDAO.getFoodItem(id);
    }

    /**
     * AsyncTask for inserting a fridge item asynchronously.
     */
    private static class InsertItemAsync extends AsyncTask<FridgeItem, Void, Void> {
        private FridgeDAO fridgeDAO;

        private InsertItemAsync(FridgeDAO fridgeDAO) {
            this.fridgeDAO = fridgeDAO;
        }

        @Override
        protected Void doInBackground(FridgeItem... fridgeItems) {
            fridgeDAO.insert(fridgeItems[0]);
            return null;
        }
    }

    /**
     * AsyncTask for updating a fridge item asynchronously.
     */
    private static class UpdateItemAsync extends AsyncTask<FridgeItem, Void, Void> {
        private FridgeDAO fridgeDAO;

        private UpdateItemAsync(FridgeDAO fridgeDAO) {
            this.fridgeDAO = fridgeDAO;
        }

        @Override
        protected Void doInBackground(FridgeItem... fridgeItems) {
            fridgeDAO.update(fridgeItems[0]);
            return null;
        }
    }

    /**
     * AsyncTask for deleting a fridge item asynchronously.
     */
    private static class DeleteItemAsync extends AsyncTask<FridgeItem, Void, Void> {
        private FridgeDAO fridgeDAO;

        private DeleteItemAsync(FridgeDAO fridgeDAO) {
            this.fridgeDAO = fridgeDAO;
        }

        @Override
        protected Void doInBackground(FridgeItem... fridgeItems) {
            fridgeDAO.delete(fridgeItems[0]);
            return null;
        }
    }

    /**
     * AsyncTask for deleting all fridge items asynchronously.
     */
    private static class DeleteAllItemsAsync extends AsyncTask<Void, Void, Void> {
        private FridgeDAO fridgeDAO;

        private DeleteAllItemsAsync(FridgeDAO fridgeDAO) {
            this.fridgeDAO = fridgeDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fridgeDAO.deleteAllItems();
            return null;
        }
    }
}