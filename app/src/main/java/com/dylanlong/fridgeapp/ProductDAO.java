package com.dylanlong.fridgeapp;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

//Adding annotation to our Dao class
@androidx.room.Dao
public interface ProductDAO {

    //below method is use to add data to database.
    @Insert
    void insert(Product model);

    //below method is use to update the data in our database.
    @Update
    void update(Product model);

    //below line is use to delete a specific course in our database.
    @Delete
    void delete(Product model);

    @Query("SELECT * FROM product_details WHERE productCode = :code")
    LiveData<Product> getFoodItem(String code);
}
