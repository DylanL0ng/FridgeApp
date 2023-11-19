package com.dylanlong.fridgeapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FridgeRepository {
    FridgeDAO fridgeDAO;
    LiveData<List<FridgeItem>> allItems;


    public FridgeRepository(Application application) {
        ProductDatabase database = ProductDatabase.getInstance(application);
        fridgeDAO = database.fridgeDAO();
        allItems = fridgeDAO.getAllItems();
    }

    public void insert(FridgeItem fridgeItem)
    {
        // Insert new item into the storage
        new FridgeRepository.InsertItemAsync(fridgeDAO).execute(fridgeItem);
    }

    public void deleteAllItems() {
        new DeleteAllItemsAsync(fridgeDAO).execute();
    }

    public void delete(FridgeItem fridgeItem) {
        new DeleteItemAsync(fridgeDAO).execute(fridgeItem);
    }

    public void update(FridgeItem fridgeItem) {
        new UpdateItemAsync(fridgeDAO).execute(fridgeItem);
    }

    public LiveData<List<FridgeItem>> getAllItems() {
        return allItems;
    }

    public LiveData<FridgeItem> getFoodItem(int id) {
        return fridgeDAO.getFoodItem(id);
    }

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
