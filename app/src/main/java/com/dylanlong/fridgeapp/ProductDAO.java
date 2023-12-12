package com.dylanlong.fridgeapp;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Data Access Object (DAO) interface for accessing and managing product details in the database.
 *
 * The interface is annotated with Room's Dao annotation, indicating that it provides methods
 * to interact with the "product_details" table in the database.
 */
@androidx.room.Dao
public interface ProductDAO {

    /**
     * Inserts a new product into the database.
     *
     * @param model The product to be inserted.
     */
    @Insert
    void insert(Product model);

    /**
     * Updates an existing product in the database.
     *
     * @param model The product to be updated.
     */
    @Update
    void update(Product model);

    /**
     * Deletes a product from the database.
     *
     * @param model The product to be deleted.
     */
    @Delete
    void delete(Product model);

    /**
     * Retrieves a specific product based on its product code.
     *
     * @param code The product code used for identification.
     * @return A LiveData instance of the specific product.
     */
    @Query("SELECT * FROM product_details WHERE productCode = :code")
    LiveData<Product> getFoodItem(String code);
}