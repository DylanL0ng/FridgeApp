package com.dylanlong.fridgeapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateNewFoodItem extends AppCompatActivity {
    ProductDatabase productDatabase;
    Button cancelButton;
    Button addItemButton;
    Button productExpiryDate;

    EditText productName;

    Long expiryTimestamp;
    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CreateNewFoodItem", "Open new intent");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_food_item);

        productName = findViewById(R.id.editProductName);
        productExpiryDate = findViewById(R.id.editExpiryDate);
        productDatabase = ProductDatabase.getInstance(this);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            barcode = bundle.getString("barcode");

            if (bundle.containsKey("product_name"))
            {
                String initialFoodLabel = bundle.getString("product_name");
                productName.setText(initialFoodLabel);
            }
        }

        productExpiryDate.setOnClickListener(v -> {
            Calendar calender = Calendar.getInstance();
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH);
            int day = calender.get(Calendar.DATE);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calender.set(Calendar.YEAR, year);
                    calender.set(Calendar.MONTH, month);
                    calender.set(Calendar.DATE, day);

                    expiryTimestamp = calender.getTimeInMillis();

                    String newLabel = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

                    productExpiryDate.setText(newLabel);
                }
            }, year, month, day);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });


        cancelButton = findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(v -> {
            finish();
        });

        addItemButton = findViewById(R.id.addItem_btn);
        addItemButton.setOnClickListener(v -> {
            String name = String.valueOf(productName.getText());

            if (name.length() == 0)
            {
                Toast.makeText(CreateNewFoodItem.this, "You have to fill in the name field", Toast.LENGTH_SHORT).show();
                return;
            }

            if (expiryTimestamp == null)
            {
                Toast.makeText(CreateNewFoodItem.this, "You have to fill in the expiry field", Toast.LENGTH_SHORT).show();
                return;
            }

            FridgeItem newItem = new FridgeItem(barcode, name, expiryTimestamp);
            insertFood(newItem);

            final int[] count = {0};
            productDatabase.productDAO().getFoodItem(barcode).observe(this, new Observer<Product>() {
                @Override
                public void onChanged(Product product) {
                    if (count[0] > 0) return;
                    count[0] += 1;

                    if (product == null)
                    {
                        Product newProduct = new Product(barcode, name);
                        insertProduct(newProduct);
                        return;
                    }

                    if (!product.getName().equals(name))
                    {
                        new AlertDialog.Builder(CreateNewFoodItem.this)
                                .setTitle("Update Database")
                                .setMessage("This item is already saved under a different name, do you want to update the database?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Product updated = new Product(barcode, name);
                                        updateProduct(updated);
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .show();
                        return;
                    }

                    finish();
                }
            });
        });
    }

    private void updateProduct(Product product) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                productDatabase.productDAO().update(product);
            }
        });
    }
    private void insertFood(FridgeItem fridgeItem) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                productDatabase.fridgeDAO().insert(fridgeItem);
            }
        });
    }

    private void insertProduct(Product product) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                productDatabase.productDAO().insert(product);
            }
        });
    }
}