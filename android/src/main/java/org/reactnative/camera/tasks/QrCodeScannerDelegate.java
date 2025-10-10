package org.reactnative.camera.tasks;

import java.util.ArrayList;

public interface QrCodeScannerDelegate {
    void onQrCodeScanningTaskCompleted();
    void sendQrCodeData(ArrayList<String> data);
}
