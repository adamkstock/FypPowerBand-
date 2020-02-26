package com.p004ti.ble.bluetooth_le_controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanSettings.Builder;
import android.content.Context;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.ContextCompat;
import android.util.Log;
import com.p004ti.ble.bluetooth_le_controller.Exceptions.BluetoothLEBluetoothEnableTimeoutException;
import com.p004ti.ble.bluetooth_le_controller.Exceptions.BluetoothLEPermissionException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.ti.ble.bluetooth_le_controller.BluetoothLEManager */
public class BluetoothLEManager {
    public static final int BT_ENABLE_TIMEOUT = 5;
    public static final int SCAN_PERMISSIONS_CODE = 1;
    public static final int SCAN_TERMINATE_TIMEOUT = 10;
    static final String TAG = BluetoothLEManager.class.getSimpleName();
    /* access modifiers changed from: private */
    public static BluetoothLEManager mThis;
    BluetoothAdapter adapter;

    /* renamed from: c */
    Context f32c;
    List<BluetoothLEDevice> deviceList;

    /* renamed from: m */
    BluetoothManager f33m;
    public BluetoothLEManagerCB managerCB;
    Runnable scanRoutine = new Runnable() {
        public void run() {
            Log.d(BluetoothLEManager.TAG, "scanRoutine started");
            BluetoothAdapter adapter = BluetoothLEManager.mThis.f33m.getAdapter();
            ScanSettings build = new Builder().build();
            ArrayList arrayList = new ArrayList(2);
            if (!adapter.isEnabled()) {
                adapter.enable();
                int i = 50;
                while (!adapter.isEnabled()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException unused) {
                        Log.d(BluetoothLEManager.TAG, "scanRoutine: Interrupted while sleeping when waiting for bluetooth adapter enable !");
                    }
                    i--;
                    if (i < 0) {
                        return;
                    }
                }
            }
            BluetoothLeScanner bluetoothLeScanner = adapter.getBluetoothLeScanner();
            C06121 r4 = new ScanCallback() {
                public void onScanResult(int i, ScanResult scanResult) {
                    super.onScanResult(i, scanResult);
                    if (!BluetoothLEManager.this.deviceInList(scanResult.getDevice())) {
                        BluetoothLEDevice bluetoothLEDevice = new BluetoothLEDevice(scanResult.getDevice(), BluetoothLEManager.this.f32c);
                        bluetoothLEDevice.f29m = BluetoothLEManager.this.f33m;
                        bluetoothLEDevice.f30sR = scanResult;
                        BluetoothLEManager.this.deviceList.add(bluetoothLEDevice);
                        String str = BluetoothLEManager.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(scanResult.getDevice().getAddress().toString());
                        sb.append(" - Added to device list");
                        Log.d(str, sb.toString());
                        BluetoothLEManager.mThis.managerCB.deviceFound(bluetoothLEDevice);
                        return;
                    }
                    for (int i2 = 0; i2 < BluetoothLEManager.this.deviceList.size(); i2++) {
                        BluetoothLEDevice bluetoothLEDevice2 = (BluetoothLEDevice) BluetoothLEManager.this.deviceList.get(i2);
                        if (bluetoothLEDevice2.f27d.getAddress().equalsIgnoreCase(scanResult.getDevice().getAddress())) {
                            bluetoothLEDevice2.f30sR = scanResult;
                            BluetoothLEManager.mThis.managerCB.deviceFound(bluetoothLEDevice2);
                        }
                    }
                }

                public void onBatchScanResults(List<ScanResult> list) {
                    super.onBatchScanResults(list);
                    Log.d(BluetoothLEManager.TAG, "onBatchScanResults");
                }

                public void onScanFailed(int i) {
                    super.onScanFailed(i);
                    Log.d(BluetoothLEManager.TAG, "onScanFailed");
                }
            };
            bluetoothLeScanner.startScan(arrayList, build, r4);
            while (!BluetoothLEManager.this.stopScan) {
                try {
                    Thread.sleep(500, 0);
                } catch (InterruptedException unused2) {
                    Log.d(BluetoothLEManager.TAG, "Interrupted");
                }
            }
            if (adapter.isEnabled()) {
                bluetoothLeScanner.stopScan(r4);
            }
            Log.d(BluetoothLEManager.TAG, "scanRoutine stopped");
        }
    };
    Thread scanThread;
    boolean stopScan = false;

    /* renamed from: com.ti.ble.bluetooth_le_controller.BluetoothLEManager$BluetoothLEManagerCB */
    public interface BluetoothLEManagerCB {
        void deviceFound(BluetoothLEDevice bluetoothLEDevice);
    }

    public BluetoothLEManager(Context context) {
        this.f32c = context;
        this.deviceList = new ArrayList();
        this.f33m = (BluetoothManager) this.f32c.getSystemService("bluetooth");
        this.adapter = this.f33m.getAdapter();
    }

    public static BluetoothLEManager getInstance(Context context) {
        if (mThis == null) {
            mThis = new BluetoothLEManager(context);
        }
        return mThis;
    }

    public int checkPermission() {
        return ContextCompat.checkSelfPermission(this.f32c, "android.permission.ACCESS_FINE_LOCATION");
    }

    public void prepareForScanForDevices(Activity activity) {
        BluetoothAdapter adapter2 = this.f33m.getAdapter();
        if (adapter2 != null) {
            if (!adapter2.isEnabled()) {
                adapter2.enable();
                int i = 50;
                while (!adapter2.isEnabled()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException unused) {
                        Log.d(TAG, "prepareForScanForDevices: Interrupted while sleeping when waiting for bluetooth adapter enable !");
                    }
                    i--;
                    if (i < 0) {
                        return;
                    }
                }
            }
            String str = "android.permission.ACCESS_FINE_LOCATION";
            if (ContextCompat.checkSelfPermission(activity, str) != 0 && !ActivityCompat.shouldShowRequestPermissionRationale(activity, str)) {
                ActivityCompat.requestPermissions(activity, new String[]{str}, 1);
            }
        }
    }

    public void scanForDevices() {
        BluetoothAdapter adapter2 = this.f33m.getAdapter();
        if (adapter2 != null) {
            if (!adapter2.isEnabled()) {
                adapter2.enable();
                int i = 50;
                while (!adapter2.isEnabled()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException unused) {
                        Log.d(TAG, "prepareForScanForDevices: Interrupted while sleeping when waiting for bluetooth adapter enable !");
                    }
                    i--;
                    if (i < 0) {
                        throw new BluetoothLEBluetoothEnableTimeoutException("Timed out after waiting for 5 seconds for bluetooth enable");
                    }
                }
            }
            if (ContextCompat.checkSelfPermission(this.f32c, "android.permission.ACCESS_FINE_LOCATION") == 0) {
                scanForDevices(0);
                return;
            }
            throw new BluetoothLEPermissionException("Permission denied");
        }
    }

    public void scanForDevices(int i) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("scanForDevices from : ");
            sb.append(stackTrace[3].getClassName());
            sb.append(" ");
            sb.append(stackTrace[3].getMethodName());
            Log.d(str, sb.toString());
        }
        if (i != -1) {
            Thread thread = this.scanThread;
            if (thread == null) {
                this.scanThread = new Thread(this.scanRoutine);
                this.scanThread.start();
            } else if (thread.getState() == State.TERMINATED) {
                this.stopScan = false;
                this.scanThread = new Thread(this.scanRoutine);
                this.scanThread.start();
            } else {
                stopScan();
                int i2 = 10;
                while (this.scanThread.getState() != State.TERMINATED) {
                    try {
                        Thread.sleep(50, 0);
                    } catch (InterruptedException unused) {
                    }
                    i2--;
                    if (i2 < 0) {
                        Log.d(TAG, "Timeout while waiting for scanThread to die ...");
                        return;
                    }
                }
                Log.d(TAG, "Scan thread stopped, restarting");
                this.deviceList = new ArrayList();
                this.stopScan = false;
                this.scanThread = new Thread(this.scanRoutine);
                this.scanThread.start();
            }
            return;
        }
        throw new BluetoothLEPermissionException("Permission denied");
    }

    public void restartBluetooth(Context context, final Activity activity) {
        final BluetoothAdapter adapter2 = this.f33m.getAdapter();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Restarting Bluetooth");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(1);
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (adapter2.isEnabled()) {
                        adapter2.disable();
                        for (final int i = 0; i < 10; i++) {
                            Thread.sleep(200, 0);
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    progressDialog.setProgress(i * 5);
                                }
                            });
                        }
                    }
                    adapter2.enable();
                    for (final int i2 = 0; i2 < 10; i2++) {
                        Thread.sleep(250, 0);
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.setProgress((i2 * 5) + 50);
                            }
                        });
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                        }
                    });
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopScan() {
        this.stopScan = true;
    }

    public BluetoothLEDevice deviceForBluetoothDev(BluetoothDevice bluetoothDevice) {
        for (int i = 0; i < this.deviceList.size(); i++) {
            BluetoothLEDevice bluetoothLEDevice = (BluetoothLEDevice) this.deviceList.get(i);
            if (bluetoothLEDevice.f27d.getAddress().toString().equalsIgnoreCase(bluetoothDevice.getAddress().toString())) {
                return bluetoothLEDevice;
            }
        }
        BluetoothLEDevice bluetoothLEDevice2 = new BluetoothLEDevice(bluetoothDevice, this.f32c);
        ScanResult scanResult = new ScanResult(bluetoothDevice, null, 0, 0);
        bluetoothLEDevice2.f30sR = scanResult;
        this.deviceList.add(bluetoothLEDevice2);
        Log.d(TAG, "Did not find deviceForBluetoothDev, but added a new device instead to the list");
        return bluetoothLEDevice2;
    }

    public boolean deviceInList(BluetoothDevice bluetoothDevice) {
        for (int i = 0; i < this.deviceList.size(); i++) {
            if (((BluetoothLEDevice) this.deviceList.get(i)).f27d.getAddress().equalsIgnoreCase(bluetoothDevice.getAddress())) {
                return true;
            }
        }
        return false;
    }
}
