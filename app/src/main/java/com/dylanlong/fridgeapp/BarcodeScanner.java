package com.dylanlong.fridgeapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class BarcodeScanner extends Activity implements CompoundBarcodeView.TorchListener {
    private CaptureManager capture;
    private CompoundBarcodeView barcodeScannerView;

//    private ViewfinderView viewFinderView;

    private FloatingActionButton cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        barcodeScannerView = findViewById(R.id.scanner);
        cancelButton = findViewById(R.id.cancel_scanner);

//        viewFinderView = barcodeScannerView.getViewFinder();

        // https://stackoverflow.com/questions/32579557/how-to-update-an-imported-module-in-android-studio
        // https://github.com/journeyapps/zxing-android-embedded/blob/master/zxing-android-embedded/src/com/journeyapps/barcodescanner/ViewfinderView.java#L79

        /* TODO: Import the barcode scanner package as a manual
        // package so that we may update it and set the alpha of
        // the mask to 100%
        */

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setShowMissingCameraPermissionDialog(false);
        capture.decode();

        cancelButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }
}