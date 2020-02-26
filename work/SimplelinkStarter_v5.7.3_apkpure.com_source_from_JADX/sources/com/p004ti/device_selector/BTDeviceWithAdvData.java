package com.p004ti.device_selector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import com.p004ti.ble.bluetooth_le_controller.EddystoneBeaconDecoder;

/* renamed from: com.ti.device_selector.BTDeviceWithAdvData */
public class BTDeviceWithAdvData {
    public EddystoneBeaconDecoder beaconDecoder = new EddystoneBeaconDecoder();
    public BluetoothDevice btDevice;
    public boolean isBroadcaster;
    public int lastRSSI;
    public ScanRecord lastScanRecord;
    public boolean needsBroadcastScreen;

    public BTDeviceWithAdvData(BluetoothDevice bluetoothDevice) {
        this.btDevice = bluetoothDevice;
    }
}
