package com.dylanlong.fridgeapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class CreateNewFoodItem extends AppCompatActivity {

    EditText productName;
    Button productExpiryDate;
    Button cancelButton;

    Button addItemButton;

    String initialFoodLabel = "";
    String barcode;

    ProductDatabase productDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_food_item);

        Bundle bundle = getIntent().getExtras();;

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
            String expiry = String.valueOf(productExpiryDate.getText());

            FridgeItem newItem = new FridgeItem(barcode, name, expiry);
            insertFood(newItem);

            // TODO: Add product to Database, so that we can check for
            // pre-existing names to auto-fill the input

            //            Product newProduct = new Product(barcode, name);
            //            insertProduct(newProduct);

            finish();
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