package com.dylanlong.fridgeapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fridge")
public class FridgeItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;

    String expiry;
    String barcode;
    String name;


    @Override
    public String toString() {
        return barcode + " " + name;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FridgeItem(String barcode, String name, String expiry) {
        this.barcode = barcode;
        this.name = name;
        this.expiry = expiry;
    }
}
