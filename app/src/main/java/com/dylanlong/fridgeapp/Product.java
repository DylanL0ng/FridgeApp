package com.dylanlong.fridgeapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_details")
public class Product {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "productCode")
    private String productCode;
    private String name;

    @Override
    public String toString() {
        return productCode + " " + name;
    }


    public Product(String productCode, String name)
    {
        this.productCode = productCode;
        this.name = name;
    }
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
