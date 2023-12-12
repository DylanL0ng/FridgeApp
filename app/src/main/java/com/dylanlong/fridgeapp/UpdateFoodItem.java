package com.dylanlong.fridgeapp;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity class for updating an existing food item in the database.
 * It allows users to modify the name and expiry date of the selected food item.
 */
public class UpdateFoodItem extends AppCompatActivity {

    private ProductDatabase productDatabase;
    private Button cancelButton;
    private Button updateButton;
    private Button productExpiryDate;

    private int productId;

    private EditText itemNameInput;

    private Long productExpiryTimestamp;
    private String productBarcode;
    private String productName;

    private CoordinatorLayout coordinatorLayout;

    /**
     * Called when the activity is starting. This is where most initialisation
     * should go: calling setContentView(int) to inflate the activity's UI,
     * using findViewById(int) to programmatically interact with widgets in the UI,
     * and initialising the activity's state.
     *
     * @param savedInstanceState If the activity is being re-initialised after
     *                           previously being shut down, then this Bundle
     *                           contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food_item);

        // Initialise UI elements and database
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        itemNameInput = findViewById(R.id.editProductName);
        productExpiryDate = findViewById(R.id.editExpiryDate);
        cancelButton = findViewById(R.id.cancel_btn);
        updateButton = findViewById(R.id.update_btn);

        productDatabase = ProductDatabase.getInstance(this);

        // Set the app's mode to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // Retrieve data from the previous activity
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;

        // Set initial values for UI elements
        productBarcode = bundle.getString("barcode");
        productName = bundle.getString("product_name");
        productExpiryTimestamp = bundle.getLong("product_expiry");
        productId = bundle.getInt("product_id");

        itemNameInput.setText(productName);
        productExpiryDate.setText(convertTimestampToDate(productExpiryTimestamp));

        // Finish the activity if essential data is missing
        if (
                productBarcode == null || productName == null ||
                        productExpiryTimestamp == null
        ) {
            finish();
            return;
        }

        // Set up a click listener for the expiry date button to show a date picker dialog
        productExpiryDate.setOnClickListener(v -> {
            Calendar calender = Calendar.getInstance();
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH);
            int day = calender.get(Calendar.DATE);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, day1) -> {
                calender.set(Calendar.YEAR, year1);
                calender.set(Calendar.MONTH, month1);
                calender.set(Calendar.DATE, day1);

                productExpiryTimestamp = calender.getTimeInMillis();

                String newLabel = String.format(Locale.getDefault(), "%02d/%02d/%04d", day1, month1, year1);

                productExpiryDate.setText(newLabel);
            }, year, month, day);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });

        // Set up click listeners for cancel and update buttons
        cancelButton.setOnClickListener(v -> finish());

        updateButton.setOnClickListener(v -> {
            String name = String.valueOf(itemNameInput.getText());

            // Validate input fields and show Snackbar for any errors
            if (name.length() == 0) {
                showSnackbar("You have to fill in the name field!");
                return;
            }

            if (productExpiryTimestamp == null) {
                showSnackbar("You have to fill in the expiry field!");
                return;
            }

            // Create a new fridge item, update database, and show a success Snackbar
            FridgeItem newItem = new FridgeItem(productName, name, productExpiryTimestamp);

            newItem.setId(productId);

            showSnackbar("Food item updated!");

            updateFood(newItem);
            finish();
        });
    }

    /**
     * Updates the specified food item in the database asynchronously.
     *
     * @param fridgeItem The updated FridgeItem object.
     */
    private void updateFood(FridgeItem fridgeItem) {
        AsyncTask.execute(() -> productDatabase.fridgeDAO().update(fridgeItem));
    }

    /**
     * Converts a timestamp to a formatted date string.
     *
     * @param timestamp The timestamp to be converted.
     * @return The formatted date string (dd/MM/yyyy).
     */
    public static String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        return dateFormat.format(date);
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