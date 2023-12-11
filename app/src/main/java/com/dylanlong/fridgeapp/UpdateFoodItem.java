package com.dylanlong.fridgeapp;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateFoodItem extends AppCompatActivity {

    ProductDatabase productDatabase;
    Button cancelButton;
    Button updateButton;
    Button productExpiryDate;

    int productId;

    EditText itemNameInput;

    Long productExpiryTimestamp;
    String productBarcode;
    String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food_item);

        itemNameInput = findViewById(R.id.editProductName);
        productExpiryDate = findViewById(R.id.editExpiryDate);

        productDatabase = ProductDatabase.getInstance(this);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;

        productBarcode = bundle.getString("barcode");
        productName = bundle.getString("product_name");
        productExpiryTimestamp = bundle.getLong("product_expiry");
        productId = bundle.getInt("product_id");

        itemNameInput.setText(productName);

        productExpiryDate.setText(convertTimestampToDate(productExpiryTimestamp));

        if (
                productBarcode == null || productName == null ||
                productExpiryTimestamp == null
        )
        {
            finish();
            return;
        }

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


        cancelButton = findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(v -> finish());

        updateButton = findViewById(R.id.update_btn);
        updateButton.setOnClickListener(v -> {
            String name = String.valueOf(itemNameInput.getText());

            if (name.length() == 0)
            {
                Toast.makeText(UpdateFoodItem.this, "You have to fill in the name field", Toast.LENGTH_SHORT).show();
                return;
            }

            if (productExpiryTimestamp == null)
            {
                Toast.makeText(UpdateFoodItem.this, "You have to fill in the expiry field", Toast.LENGTH_SHORT).show();
                return;
            }

            FridgeItem newItem = new FridgeItem(productName, name, productExpiryTimestamp);

            newItem.setId(productId);

            updateFood(newItem);
            finish();
        });
    }

    private void updateFood(FridgeItem fridgeItem) {
        AsyncTask.execute(() -> productDatabase.fridgeDAO().update(fridgeItem));
    }

    public static String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        return dateFormat.format(date);
    }
}