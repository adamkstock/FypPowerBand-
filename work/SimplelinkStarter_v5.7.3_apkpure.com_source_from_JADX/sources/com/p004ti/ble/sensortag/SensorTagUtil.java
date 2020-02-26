package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;

/* renamed from: com.ti.ble.sensortag.SensorTagUtil */
public class SensorTagUtil {
    public static boolean isSensorTag2(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice != null) {
            String name = bluetoothDevice.getName();
            if (name.compareTo("SensorTag2") == 0 || name.compareTo("SensorTag2.0") == 0 || name.compareTo("CC2650 SensorTag") == 0 || name.compareTo("CC2650 SensorTag LED") == 0) {
                return true;
            }
        }
        return false;
    }
}
