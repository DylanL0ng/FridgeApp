package com.dylanlong.fridgeapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * Student Name: Dylan Hussain
 * Student Number: C21331063
 *
 *
 * MainActivity represents the main
 * screen of the application, displaying
 * fridge items and providing options
 * to scan barcodes and manage the items.
 */
public class MainActivity extends AppCompatActivity {

    private Button scanForBarcodeButton;
    private ProductDatabase productDatabase;

    private ViewFridgeItem viewFridge;

    private FridgeItemListAdapter fridgeListAdapter;
    private RecyclerView fridgeRecyclerView;

    private TextView messagePrompt;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the app's mode to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // Initialise the database and recycler view
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        scanForBarcodeButton = findViewById(R.id.btn_scan);
        messagePrompt = findViewById(R.id.messagePrompt);
        fridgeRecyclerView = findViewById(R.id.rvFridgeItems);
        fridgeRecyclerView.setVisibility(View.VISIBLE);

        productDatabase = ProductDatabase.getInstance(this);

        viewFridge = new ViewModelProvider(this).get(ViewFridgeItem.class);

        fridgeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialise list adapter
        fridgeListAdapter = new FridgeItemListAdapter();
        fridgeRecyclerView.setAdapter(fridgeListAdapter);

        // Create an observer, which will trigger
        // when an item is added or removed.
        // The observer will fire the onChanged
        // method which will update the list
        viewFridge.getAllItems().observe(this, fridgeItems -> {
            fridgeListAdapter.setItems(fridgeItems);
            messagePrompt.setVisibility(fridgeItems.size() == 0 ? View.VISIBLE : View.GONE);
        });

        // Set on click handler which will
        // start an edit activity when an
        // item is clicked on.
        fridgeListAdapter.setOnItemClickListener(fridgeItem -> {
            Intent intent = new Intent(MainActivity.this, UpdateFoodItem.class);
            Bundle options = new Bundle();

            options.putInt("product_id", fridgeItem.getId());
            options.putLong("product_expiry", fridgeItem.getExpiry());
            options.putString("product_name", fridgeItem.getName());

            options.putString("barcode", fridgeItem.getBarcode());

            intent.putExtras(options);

            startActivity(intent);
        });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(fridgeRecyclerView);

        // Setup click listener on new food button
        scanForBarcodeButton.setOnClickListener(v -> {
            // When clicked it will set up the
            // barcode scanner and launch the
            // activity

            ScanOptions options = new ScanOptions();
            options.setOrientationLocked(true);
            options.setPrompt("Scan a barcode");
            options.setCaptureActivity(BarcodeScanner.class);
            options.setDesiredBarcodeFormats(ScanOptions.EAN_13);
            barcodeLauncher.launch(options);
        });

        if (fridgeListAdapter.getItemCount() == 0)
        {
            showSnackbar("You have no items in storage, add them now!");
        }
    }

    /**
     * Result launcher for the barcode scanner,
     * this will fire when the barcode scanner
     * has detected a valid barcode.
     */
    public ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                // Check if there's any result; if not
                // it's likely due to permission errors
                if (result.getContents() == null) {
                    // Handle permission errors
                    Intent originalIntent = result.getOriginalIntent();

                    if (originalIntent == null)
                        showSnackbar("Cancelled due to unknown reasons!");
                    else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION))
                        showSnackbar("Cancelled due to missing camera permission!");

                    return;
                }

                // Get the barcode result and store it
                String barcode = result.getContents();

                // Create an intent to input the expiry
                // date and food label then bundle the
                // barcode into the intent and start the
                // activity
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

    /**
     * Registers a callback for detecting
     * swiping on the screen. when an item
     * is swiped on, it will trigger the
     * adapter to remove the item from the
     * list and the database.
     */
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            showSnackbar("Item deleted!");

            FridgeItem deletedItem = fridgeListAdapter.removeItem(viewHolder.getAdapterPosition());
            deleteItem(deletedItem);
        }
    };

    /**
     * Deletes a fridge item from the database
     * in a background thread.
     *
     * @param fridgeItem The fridge item to be deleted.
     */
    private void deleteItem(FridgeItem fridgeItem) {
        AsyncTask.execute(() -> productDatabase.fridgeDAO().delete(fridgeItem));
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
