package com.dylanlong.fridgeapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button btn_scan;
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(MainActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String barcode = result.getContents();
                    Log.d("MainActivity", barcode);

                    Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_LONG).show();

                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "https://world.openfoodfacts.org/api/v2/product/" + barcode + ".json";
                    Log.d("MainActivity", url);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("MainActivity", response);

                                    try {
                                        JSONObject food_data = new JSONObject(response);
                                        Intent intent = new Intent(MainActivity.this, CreateNewFoodItem.class);

                                        Bundle options = new Bundle();

                                        String brand_name = food_data.getJSONObject("product").getString("brands");
                                        String product_name = food_data.getJSONObject("product").getString("product_name");
                                        String quantity = food_data.getJSONObject("product").getString("quantity");

                                        String label = product_name + " - " + brand_name + " - " + quantity;
                                        options.putString("product_name", label);

                                        intent.putExtras(options);

                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
//                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG);

//                                    startActivity(intent, options);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "That didn't work", Toast.LENGTH_LONG);
                        }
                    });

                    queue.add(stringRequest);
                }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> {

            ScanOptions options = new ScanOptions();
            options.setOrientationLocked(true);
            options.setPrompt("Scan a barcode");
            options.setCaptureActivity(BarcodeScanner.class);
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
            barcodeLauncher.launch(options);

        });
    }
}