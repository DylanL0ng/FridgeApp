package com.dylanlong.fridgeapp;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

/**
 * Activity for creating a new food item.
 */
public class CreateNewFoodItem extends AppCompatActivity {

    // Database instance for storing products and fridge items
    ProductDatabase productDatabase;

    // UI elements
    Button cancelButton;
    Button addItemButton;
    Button productExpiryDate;
    EditText productName;

    // Expiry timestamp and barcode for the new food item
    Long expiryTimestamp;
    String barcode;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_food_item);

        // Initialise UI elements and database
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        productName = findViewById(R.id.editProductName);
        productExpiryDate = findViewById(R.id.editExpiryDate);
        productDatabase = ProductDatabase.getInstance(this);

        // Set the app's mode to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // Retrieve data from the previous activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            barcode = bundle.getString("barcode");
            if (bundle.containsKey("product_name")) {
                String initialFoodLabel = bundle.getString("product_name");
                productName.setText(initialFoodLabel);
            }
        }

        // Set up a click listener for the expiry
        // date button to show a date picker dialog
        productExpiryDate.setOnClickListener(v -> {
            Calendar calender = Calendar.getInstance();
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH);
            int day = calender.get(Calendar.DATE);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, day1) -> {
                calender.set(Calendar.YEAR, year1);
                calender.set(Calendar.MONTH, month1);
                calender.set(Calendar.DATE, day1);

                expiryTimestamp = calender.getTimeInMillis();

                String newLabel = String.format(Locale.getDefault(), "%02d/%02d/%04d", day1, month1, year1);

                productExpiryDate.setText(newLabel);
            }, year, month, day);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        // Set up click listeners for cancel and
        // add item buttons
        cancelButton = findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(v -> finish());

        addItemButton = findViewById(R.id.addItem_btn);
        addItemButton.setOnClickListener(v -> {
            String name = String.valueOf(productName.getText());

            // Validate input fields
            if (name.length() == 0) {
                showSnackbar("You have to fill in the name field!");
                return;
            }

            if (expiryTimestamp == null) {
                showSnackbar("You have to fill in the expiry field!");
                return;
            }

            // Create a new fridge item
            FridgeItem newItem = new FridgeItem(barcode, name, expiryTimestamp);

            // Insert the new fridge item and observe
            // the product data
            insertFood(newItem);
            LiveData<Product> productLiveData = productDatabase.productDAO().getFoodItem(barcode);
            Observer<Product> observer = new Observer<Product>() {
                @Override
                public void onChanged(Product product) {
                    productLiveData.removeObserver(this);

                    // Handle product data changes
                    if (product == null) {
                        // Product does not exist, insert a new
                        // product
                        Product newProduct = new Product(barcode, name);
                        insertProduct(newProduct);
                        return;
                    }

                    if (!product.getName().equals(name)) {
                        // Show a dialog for updating the database
                        // if the name is different
                        new AlertDialog.Builder(CreateNewFoodItem.this)
                                .setTitle("Update Database")
                                .setMessage("This item is already saved under a different name, do you want to update the database?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    Product updated = new Product(barcode, name);
                                    updateProduct(updated);
                                    finish();
                                })
                                .setNegativeButton("No", (dialog, which) -> finish())
                                .show();
                    } else {
                        // Finish the activity if the product
                        // name is the same
                        finish();
                    }
                }
            };

            // Observe product data changes
            productLiveData.observe(this, observer);
        });
    }

    /**
     * Update the product in the database.
     *
     * @param product The product to be updated.
     */
    private void updateProduct(Product product) {
        AsyncTask.execute(() -> productDatabase.productDAO().update(product));
    }

    /**
     * Insert a new fridge item into the database.
     *
     * @param fridgeItem The fridge item to be inserted.
     */
    private void insertFood(FridgeItem fridgeItem) {
        AsyncTask.execute(() -> productDatabase.fridgeDAO().insert(fridgeItem));
    }

    /**
     * Insert a new product into the database.
     *
     * @param product The product to be inserted.
     */
    private void insertProduct(Product product) {
        AsyncTask.execute(() -> productDatabase.productDAO().insert(product));
    }

    /**
     * Displays a Snackbar with the specified
     * message if the coordinatorLayout is not
     * null.
     *
     * @param message The message to be displayed in the Snackbar.
     */
    private void showSnackbar(String message) {
        if (coordinatorLayout != null) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}