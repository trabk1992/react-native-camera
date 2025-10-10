package org.reactnative.camera.tasks;

import android.util.Log;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;

public class QrCodeScannerTask {

    private BarcodeScanner scanner;
    private QrCodeScannerDelegate mDelegate;
    private final String TAG = "QrCodeScannerTask";

    public QrCodeScannerTask() {
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build();

        scanner = BarcodeScanning.getClient(options);
    }

    public void setDelegate(QrCodeScannerDelegate delegate) {
        mDelegate = delegate;
    }

    public void processFrame(byte[] imageData, int width, int height, int rotation) {

        try {
            InputImage image = InputImage.fromByteArray(
                    imageData,
                    width,
                    height,
                    rotation,
                    InputImage.IMAGE_FORMAT_NV21
            );

            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        if (!barcodes.isEmpty()) {
                            ArrayList<String> qrData = new ArrayList<>();
                            for (Barcode barcode : barcodes) {
                                qrData.add(barcode.getDisplayValue());
                            }
                            mDelegate.sendQrCodeData(qrData);
                        }
                        mDelegate.onQrCodeScanningTaskCompleted();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "error scan qr: " + e.getMessage());
                        mDelegate.onQrCodeScanningTaskCompleted();
                    });
        } catch (Exception e) {
            Log.e(TAG, "error InputImage: " + e.getMessage());
            mDelegate.onQrCodeScanningTaskCompleted();
        }
    }
}
