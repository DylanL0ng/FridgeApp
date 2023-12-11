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

public class CreateNewFoodItem extends AppCompatActivity {

    EditText productName;
    Button productExpiryDate;
    Long expiryTimestamp;
    Button cancelButton;

    Button addItemButton;

    String initialFoodLabel = "";
    String barcode;

    ProductDatabase productDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_food_item);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        Bundle bundle = getIntent().getExtras();;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        productDatabase = ProductDatabase.getInstance(this);

        productName = findViewById(R.id.editProductName);

        barcode = bundle.getString("barcode");

        if (bundle.containsKey("product_name"))
        {
            initialFoodLabel = bundle.getString("product_name");
            productName.setText(initialFoodLabel);
        }

        productExpiryDate = findViewById(R.id.editExpiryDate);
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
                    productExpiryDate.setText(day + "/" + month + "/" + year);
                }
            }, year, month, day);

            datePickerDialog.show();
        });

        cancelButton = findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(v -> {
            finish();
        });

        addItemButton = findViewById(R.id.addItem_btn);
        addItemButton.setOnClickListener(v -> {
            String name = String.valueOf(productName.getText());
            String expiry_str = String.valueOf(productExpiryDate.getText());

//            Date expiry = null;
//            try {
//                expiry = sdf.parse(expiry_str);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }

            FridgeItem newItem = new FridgeItem(barcode, name, expiryTimestamp);
            insertFood(newItem);

            // TODO: Add product to Database, so that we can check for
            // pre-existing names to auto-fill the input
            final int[] count = {0};
            productDatabase.productDAO().getFoodItem(barcode).observe(this, new Observer<Product>() {
                @Override
                public void onChanged(Product product) {
                    if (count[0] > 0) return;
                    count[0] += 1;

                    Log.d("CreateNewFoodItem", "checking for item");
                    if (product == null)
                    {
                        Product newProduct = new Product(barcode, name);
                        insertProduct(newProduct);
                        return;
                    }

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
                }
            });
        });
    }

    private void updateProduct(Product product) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                productDatabase.productDAO().update(product);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreateNewFoodItem.this, product.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void insertFood(FridgeItem fridgeItem) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                productDatabase.fridgeDAO().insert(fridgeItem);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreateNewFoodItem.this, fridgeItem.toString(), Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to another activity or update UI
                    }
                });
            }
        });
    }

    private void insertProduct(Product product) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                productDatabase.productDAO().insert(product);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreateNewFoodItem.this, product.toString(), Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to another activity or update UI
                    }
                });
            }
        });
    }
}