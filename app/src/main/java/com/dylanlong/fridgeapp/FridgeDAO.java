package com.dylanlong.fridgeapp;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) for interacting with the "fridge" table in the database.
 */
@androidx.room.Dao
public interface FridgeDAO {

    /**
     * Inserts a new {@link FridgeItem} into the "fridge" table.
     *
     * @param model The {@link FridgeItem} to be inserted.
     */
    @Insert
    void insert(FridgeItem model);

    /**
     * Updates an existing {@link FridgeItem} in the "fridge" table.
     *
     * @param model The {@link FridgeItem} to be updated.
     */
    @Update
    void update(FridgeItem model);

    /**
     * Deletes an existing {@link FridgeItem} from the "fridge" table.
     *
     * @param model The {@link FridgeItem} to be deleted.
     */
    @Delete
    void delete(FridgeItem model);

    /**
     * Deletes all items from the "fridge" table.
     */
    @Query("DELETE FROM fridge")
    void deleteAllItems();

    /**
     * Retrieves all {@link FridgeItem}s from the "fridge" table.
     *
     * @return A {@link LiveData} list of all {@link FridgeItem}s.
     */
    @Query("SELECT * FROM fridge")
    LiveData<List<FridgeItem>> getAllItems();

    /**
     * Retrieves a specific {@link FridgeItem} from the "fridge" table based on its ID.
     *
     * @param id The ID of the {@link FridgeItem} to retrieve.
     * @return A {@link LiveData} instance of the requested {@link FridgeItem}.
     */
    @Query("SELECT * FROM fridge WHERE id = :id")
    LiveData<FridgeItem> getFoodItem(int id);
}