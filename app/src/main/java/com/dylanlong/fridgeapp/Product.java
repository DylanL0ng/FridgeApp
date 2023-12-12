package com.dylanlong.fridgeapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a product with its details, such as a product code and name.
 *
 * The class is annotated with Room's Entity annotation, indicating that instances of this class
 * will be stored in the "product_details" table in the database.
 */
@Entity(tableName = "product_details")
public class Product {

    /**
     * The product code, serving as the primary key for the database.
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "productCode")
    private String productCode;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * Constructs a new Product instance with the given product code and name.
     *
     * @param productCode The product code.
     * @param name        The name of the product.
     */
    public Product(String productCode, String name) {
        this.productCode = productCode;
        this.name = name;
    }

    /**
     * Gets the product code.
     *
     * @return The product code.
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the product code.
     *
     * @param productCode The product code to set.
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * Gets the name of the product.
     *
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the product, combining the product code and name.
     *
     * @return A string representation of the product.
     */
    @NonNull
    @Override
    public String toString() {
        return productCode + " " + name;
    }
}