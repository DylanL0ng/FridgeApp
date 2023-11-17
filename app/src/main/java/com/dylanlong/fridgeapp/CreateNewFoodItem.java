package com.dylanlong.fridgeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class CreateNewFoodItem extends AppCompatActivity {

    EditText productName;
    Button productExpiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_food_item);

        Bundle bundle = getIntent().getExtras();;

        productName = findViewById(R.id.editProductName);
        productName.setText(bundle.getString("product_name"));

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
    }
}