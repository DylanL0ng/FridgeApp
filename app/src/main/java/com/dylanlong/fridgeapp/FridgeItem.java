package com.dylanlong.fridgeapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a fridge item.
 */
@Entity(tableName = "fridge")
public class FridgeItem {

    // Auto-generated primary key
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    // Expiry timestamp of the fridge item
    private Long expiry;

    // Barcode associated with the fridge item
    private String barcode;

    // Name of the fridge item
    private String name;

    /**
     * Default constructor for the FridgeItem class.
     *
     * @param barcode Barcode associated with the fridge item.
     * @param name    Name of the fridge item.
     * @param expiry  Expiry timestamp of the fridge item.
     */
    public FridgeItem(String barcode, String name, Long expiry) {
        this.barcode = barcode;
        this.name = name;
        this.expiry = expiry;
    }

    /**
     * Getter for the expiry timestamp.
     *
     * @return The expiry timestamp of the fridge item.
     */
    public Long getExpiry() {
        return expiry;
    }

    /**
     * Setter for the expiry timestamp.
     *
     * @param expiry The expiry timestamp to set.
     */
    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }

    /**
     * Getter for the barcode.
     *
     * @return The barcode associated with the fridge item.
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Setter for the barcode.
     *
     * @param barcode The barcode to set.
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * Getter for the name.
     *
     * @return The name of the fridge item.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the ID.
     *
     * @return The ID of the fridge item.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the ID.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Overrides the toString() method to provide a custom string representation of the object.
     *
     * @return A string representation of the FridgeItem object.
     */
    @Override
    public String toString() {
        return barcode + " " + name;
    }
}