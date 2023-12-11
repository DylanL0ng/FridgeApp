package com.dylanlong.fridgeapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.arch.persistence.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraint_layout);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // Initialise the database and recycler view
        fridgeRecyclerView = findViewById(R.id.rvFridgeItems);

        productDatabase = ProductDatabase.getInstance(this);
        viewFridge = new ViewModelProvider(this).get((ViewFridgeItem.class));

        fridgeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initalise list adapter
        fridgeListAdapter = new FridgeItemListAdapter();
        fridgeRecyclerView.setAdapter(fridgeListAdapter);

        // Create an observer, when an item is a added or removed
        // observer will fire the onChanged method which will update
        // the list
        viewFridge.getAllItems().observe(this, new Observer<List<FridgeItem>>() {
            @Override
            public void onChanged(List<FridgeItem> fridgeItems) {
                fridgeListAdapter.setItems(fridgeItems);
            }
        });

        // Set on click handler, will be used later on down the road
        fridgeListAdapter.setOnItemClickListener(new FridgeItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FridgeItem fridgeItem) {
                // TODO: Showcase an option to delete
                Log.d("MainActivity", fridgeItem.getId() + "");
//                productDatabase.fridgeDAO().delete(fridgeItem);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(fridgeRecyclerView);

        // Initialise barcode button
        scanForBarcodeButton = findViewById(R.id.btn_scan);
        scanForBarcodeButton.setOnClickListener(v -> {
            // When clicked it will setup the barcode scanner
            // and launch the activity
//            Toast.makeText(MainActivity.this, "TEST", Toast.LENGTH_LONG).show();

            ScanOptions options = new ScanOptions();
            options.setOrientationLocked(true);
            options.setPrompt("Scan a barcode");
            options.setCaptureActivity(BarcodeScanner.class);
            options.setDesiredBarcodeFormats(ScanOptions.EAN_13);
            barcodeLauncher.launch(options);
        });

//        if (checkSelfPermission(Manifest.permission.Ca))
    }

    // Barcode result launcher, when the result of a barcode is given it will
    // run this method and this method handles the result logic
    public final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                // Check if theres any result, if not its likely due to permission errors
                if(result.getContents() == null)
                {
                    // Handle permission errors
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(MainActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }

                    return;
                }

                // Get the barcode result and log it
                String barcode = result.getContents();

                Log.d("MainActivity", barcode);
                Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_LONG).show();

                // Create an intent to input the expiry date and food label
                // then bundle the barcode into the intent and start the activity

//                Intent intent = new Intent(MainActivity.this, CreateNewFoodItem.class);
//                Bundle options = new Bundle();
//                options.putString("barcode", barcode);
//
//                intent.putExtras(options);
//                startActivity(intent);

                // TODO: Work on below code, it works mostly but has minor bugs that causes crashes
                // Also, would like to clean up the code for the final project.

                // Query the database to check if the product scanned already exists
                // if so we want to use that saved products label instead of calling
                // the api for auto completion

                Log.d("MainActivity", "Select DAO");
                productDatabase.productDAO().getFoodItem(barcode).observe(this, new Observer<Product>() {
                    @Override
                    public void onChanged(Product product) {
                        Intent intent = new Intent(MainActivity.this, CreateNewFoodItem.class);
                        Bundle options = new Bundle();

                        if (product != null)
                        {
                            options.putString("product_name", product.getName());
                        }

                        options.putString("barcode", barcode);

                        intent.putExtras(options);
                        startActivity(intent);
                    }
                });

//                if (foodItem == null)
//                {
//                        Log.d("YourActivity", "Food item not found");
//                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//                        String url = "https://world.openfoodfacts.org/api/v2/product/" + barcode + ".json";
//                        Log.d("MainActivity", url);
//
//                        // Request a string response from the provided URL.
//                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                                new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        Log.d("MainActivity", response);
//                                        Log.d("MainActivity", "TESTING");
//
//                                        Intent intent = new Intent(MainActivity.this, CreateNewFoodItem.class);
//                                        Bundle options = new Bundle();
//
//                                        try {
//                                            JSONObject food_data = new JSONObject(response);
//
//                                            // Check if food was found in API
//                                            if (food_data.getInt("status") == 0)
//                                            {
//                                                Log.d("MainActivity", "Food not found in API");
//                                            }
//                                            else
//                                            {
//                                                String brand_name = food_data.getJSONObject("product").getString("brands");
//                                                String product_name = food_data.getJSONObject("product").getString("product_name");
//                                                String quantity = food_data.getJSONObject("product").getString("quantity");
//
//                                                String label = product_name + " - " + brand_name + " - " + quantity;
//                                                options.putString("product_name", label);
//                                            }
//
//                                            options.putString("barcode", barcode);
//
//                                            intent.putExtras(options);
//                                                startActivity(intent);
////                                            startActivityForResult(intent, CREATE_NEW_FOOD_REQUEST);
//                                        } catch (JSONException e) {
//                                            throw new RuntimeException(e);
//                                        }
//                                    }
//                                }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                try {
//                                    String responseBody = new String( error.networkResponse.data, "utf-8" );
//                                    JSONObject jsonObject = new JSONObject( responseBody );
//                                    // Check if API doesn't have barcode
//                                    if (jsonObject.getInt("status") == 0)
//                                    {
//                                        Intent intent = new Intent(MainActivity.this, CreateNewFoodItem.class);
//                                        startActivity(intent);
//                                    }
//                                } catch ( JSONException e ) {
//                                    throw new RuntimeException(e);
//                                } catch (UnsupportedEncodingException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                        });
//
//                        queue.add(stringRequest);

//                    Intent intent = new Intent(MainActivity.this, CreateNewFoodItem.class);
//                    Bundle options = new Bundle();
//                    options.putString("barcode", barcode);
//
//                    intent.putExtras(options);
//                    startActivity(intent);
//                }
//                else
//                {
                    // If item is already labeled in database, use this label
//                    Log.d("MainActivity", "Product found in DB");

//                    Intent intent = new Intent(MainActivity.this, CreateNewFoodItem.class);
//                    Bundle options = new Bundle();
//
//                    options.putString("barcode", foodItem.getProductCode());
//                    options.putString("product_name", foodItem.getName());
//
//                    intent.putExtras(options);
//                    startActivity(intent);
//                }
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