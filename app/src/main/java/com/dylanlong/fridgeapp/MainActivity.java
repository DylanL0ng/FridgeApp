package com.dylanlong.fridgeapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.arch.persistence.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button scanForBarcodeButton;
    ProductDatabase productDatabase;

    ViewFridgeItem viewFridge;

    FridgeItemListAdapter fridgeListAdapter;
    RecyclerView fridgeRecyclerView;

    ConstraintLayout constraintLayout;

    TextView messagePrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraint_layout);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // Initialise the database and recycler view
        fridgeRecyclerView = findViewById(R.id.rvFridgeItems);

        fridgeRecyclerView.setVisibility(View.VISIBLE);

        messagePrompt = findViewById(R.id.messagePrompt);

        productDatabase = ProductDatabase.getInstance(this);

        viewFridge = new ViewModelProvider(this).get((ViewFridgeItem.class));

        fridgeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialise list adapter
        fridgeListAdapter = new FridgeItemListAdapter();
        fridgeRecyclerView.setAdapter(fridgeListAdapter);

        // Create an observer, when an item is a added or removed
        // observer will fire the onChanged method which will update
        // the list
        viewFridge.getAllItems().observe(this, new Observer<List<FridgeItem>>() {
            @Override
            public void onChanged(List<FridgeItem> fridgeItems) {
                fridgeListAdapter.setItems(fridgeItems);
                messagePrompt.setVisibility(fridgeItems.size() == 0 ? View.VISIBLE: View.GONE);
            }
        });

        // Set on click handler, will be used later on down the road
        fridgeListAdapter.setOnItemClickListener(new FridgeItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FridgeItem fridgeItem) {
                Intent intent = new Intent(MainActivity.this, UpdateFoodItem.class);
                Bundle options = new Bundle();

                options.putInt("product_id", fridgeItem.getId());
                options.putLong("product_expiry", fridgeItem.getExpiry());
                options.putString("product_name", fridgeItem.getName());

                options.putString("barcode", fridgeItem.getBarcode());

                intent.putExtras(options);

                startActivity(intent);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(fridgeRecyclerView);

        // Initialise barcode button
        scanForBarcodeButton = findViewById(R.id.btn_scan);
        scanForBarcodeButton.setOnClickListener(v -> {
            // When clicked it will setup the barcode scanner
            // and launch the activity

            ScanOptions options = new ScanOptions();
            options.setOrientationLocked(true);
            options.setPrompt("Scan a barcode");
            options.setCaptureActivity(BarcodeScanner.class);
            options.setDesiredBarcodeFormats(ScanOptions.EAN_13);
            barcodeLauncher.launch(options);
        });

    }

    // Barcode result launcher, when the result of a barcode is given it will
    // run this method and this method handles the result logic
    public ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                // Check if theres any result, if not its likely due to permission errors
                if(result.getContents() == null)
                {
                    // Handle permission errors
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null)
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION))
                        Toast.makeText(MainActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();

                    return;
                }

                // Get the barcode result and log it
                String barcode = result.getContents();

                // Create an intent to input the expiry date and food label
                // then bundle the barcode into the intent and start the activity
                LiveData<Product> productLiveData = productDatabase.productDAO().getFoodItem(barcode);
                Observer<Product> observer = new Observer<Product>() {
                    @Override
                    public void onChanged(Product product) {
                        Intent intent = new Intent(MainActivity.this, CreateNewFoodItem.class);
                        Bundle options = new Bundle();

                        if (product != null)
                            options.putString("product_name", product.getName());

                        options.putString("barcode", barcode);

                        intent.putExtras(options);
                        startActivity(intent);

                        productLiveData.removeObserver(this);
                    }
                };

                productLiveData.observe(this, observer);
            });
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Snackbar snackbar = Snackbar.make(constraintLayout, "Item deleted!", Snackbar.LENGTH_LONG);
            snackbar.show();

            FridgeItem deletedItem = fridgeListAdapter.removeItem(viewHolder.getAdapterPosition());
            deleteItem(deletedItem);
        }
    };

    private void deleteItem(FridgeItem fridgeItem) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                productDatabase.fridgeDAO().delete(fridgeItem);
            }
        });
    }
}