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

    //below method is use to add data to database.
    @Insert
    void insert(FridgeItem model);

    //below method is use to update the data in our database.
    @Update
    void update(FridgeItem model);

    //below line is use to delete a specific course in our database.
    @Delete
    void delete(FridgeItem model);


    //deleting all habits
    @Query("DELETE FROM fridge")
    void deleteAllItems();

    @Query("SELECT * FROM fridge")
    LiveData<List<FridgeItem>> getAllItems();

    @Query("SELECT * FROM fridge WHERE id = :id")
    LiveData<FridgeItem> getFoodItem(int id);
}
