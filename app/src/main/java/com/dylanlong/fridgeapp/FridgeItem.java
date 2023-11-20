package com.dylanlong.fridgeapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "fridge")
public class FridgeItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;

    Date expiry;
    String barcode;
    String name;


    @Override
    public String toString() {
        return barcode + " " + name;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
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

    public FridgeItem(String barcode, String name, Date expiry) {
        this.barcode = barcode;
        this.name = name;
        this.expiry = expiry;
    }
}
