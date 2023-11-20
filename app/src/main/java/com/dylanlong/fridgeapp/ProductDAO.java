package com.dylanlong.fridgeapp;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@androidx.room.Dao
public interface ProductDAO {

    @Insert
    void insert(Product model);

    @Update
    void update(Product model);

    @Delete
    void delete(Product model);

    @Query("SELECT * FROM product_details WHERE productCode = :code")
    LiveData<Product> getFoodItem(String code);
}
