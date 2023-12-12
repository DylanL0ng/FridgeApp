package com.dylanlong.fridgeapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

/**
 * Activity for scanning barcodes using the camera.
 */
public class BarcodeScanner extends Activity implements CompoundBarcodeView.TorchListener {

    private CaptureManager capture;
    private CompoundBarcodeView barcodeScannerView;
    private FloatingActionButton cancelButton;

    /**
     * Initializes the activity and sets up the barcode scanner.
     *
     * @param savedInstanceState Saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        // Initialize UI elements
        barcodeScannerView = findViewById(R.id.scanner);
        cancelButton = findViewById(R.id.cancel_scanner);

        // Initialize barcode scanner
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setShowMissingCameraPermissionDialog(false);
        capture.decode();

        // Set up click listener for cancel button
        cancelButton.setOnClickListener(v -> finish());
    }

    /**
     * Resumes the barcode scanning when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    /**
     * Pauses the barcode scanning when the activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    /**
     * Destroys the barcode scanning resources when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    /**
     * Saves the instance state when needed.
     *
     * @param outState The Bundle in which to place the saved state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    /**
     * Handles the result of the request for camera permissions.
     *
     * @param requestCode The request code.
     * @param permissions The requested permissions.
     * @param grantResults The grant results.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Callback method for when the torch (flashlight) is turned on.
     */
    @Override
    public void onTorchOn() {
        // TODO: Handle torch on event if needed
    }

    /**
     * Callback method for when the torch (flashlight) is turned off.
     */
    @Override
    public void onTorchOff() {
        // TODO: Handle torch off event if needed
    }
}