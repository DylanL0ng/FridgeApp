package com.dylanlong.fridgeapp;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Adding annotation to our Dao class
@androidx.room.Dao
public interface FridgeDAO {

    @Insert
    void insert(FridgeItem model);

    @Update
    void update(FridgeItem model);

    @Delete
    void delete(FridgeItem model);


    @Query("DELETE FROM fridge")
    void deleteAllItems();

    @Query("SELECT * FROM fridge")
    LiveData<List<FridgeItem>> getAllItems();

    @Query("SELECT * FROM fridge WHERE id = :id")
    LiveData<FridgeItem> getFoodItem(int id);
}
