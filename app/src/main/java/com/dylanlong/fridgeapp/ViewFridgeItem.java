package com.dylanlong.fridgeapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewFridgeItem extends AndroidViewModel {
    private FridgeRepository fridgeRepository;

    private LiveData<List<FridgeItem>> allItems;

    public ViewFridgeItem(@NonNull Application application) {
        super(application);
        fridgeRepository = new FridgeRepository(application);
        allItems = fridgeRepository.getAllItems();
    }
    public void insert(FridgeItem fridgeItem){
        fridgeRepository.insert(fridgeItem);
    }
    public void update(FridgeItem fridgeItem){
        fridgeRepository.update(fridgeItem);
    }
    public void delete(FridgeItem fridgeItem){
        fridgeRepository.delete(fridgeItem);
    }
    public void deleteAllItems(){
        fridgeRepository.deleteAllItems();
    }

    public LiveData<List<FridgeItem>> getAllItems(){
        return allItems;
    }

    public LiveData<FridgeItem> getItemById(int id) { return fridgeRepository.getFoodItem(id); }
}
