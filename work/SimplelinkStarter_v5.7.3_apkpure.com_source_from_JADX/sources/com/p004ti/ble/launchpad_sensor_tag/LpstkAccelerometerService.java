package com.p004ti.ble.launchpad_sensor_tag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.p004ti.util.bleUtility;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ti.ble.launchpad_sensor_tag.LpstkAccelerometerService */
public class LpstkAccelerometerService extends GenericBluetoothProfile {
    public static final String LPSTK_ACC_ENABLE_CHARACTERISTIC_UUID = "f000ffa1-0451-4000-b000-000000000000";
    public static final String LPSTK_ACC_RANGE_CHARACTERISTIC_UUID = "f000ffa2-0451-4000-b000-000000000000";
    public static final String LPSTK_ACC_SERVICE_UUID = "f000ffa0-0451-4000-b000-000000000000";
    public static final String LPSTK_ACC_X_DATA_CHARACTERISTIC_UUID = "f000ffa3-0451-4000-b000-000000000000";
    public static final String LPSTK_ACC_Y_DATA_CHARACTERISTIC_UUID = "f000ffa4-0451-4000-b000-000000000000";
    public static final String LPSTK_ACC_Z_DATA_CHARACTERISTIC_UUID = "f000ffa5-0451-4000-b000-000000000000";
    public static final String TAG = LpstkAccelerometerService.class.getSimpleName();
    public BluetoothGattCharacteristic accEnC;
    public BluetoothGattCharacteristic accRangeC;
    public BluetoothGattCharacteristic accXDatC;
    public BluetoothGattCharacteristic accYDatC;
    public BluetoothGattCharacteristic accZDatC;
    public float lastXVal = 0.0f;
    public float lastYVal = 0.0f;
    public float lastZVal = 0.0f;

    public LpstkAccelerometerService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context, 1000, true);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(LPSTK_ACC_ENABLE_CHARACTERISTIC_UUID)) {
                this.accEnC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(LPSTK_ACC_RANGE_CHARACTERISTIC_UUID)) {
                this.accRangeC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(LPSTK_ACC_X_DATA_CHARACTERISTIC_UUID)) {
                this.accXDatC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(LPSTK_ACC_Y_DATA_CHARACTERISTIC_UUID)) {
                this.accYDatC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(LPSTK_ACC_Z_DATA_CHARACTERISTIC_UUID)) {
                this.accZDatC = bluetoothGattCharacteristic;
            }
        }
        this.tRow.sl1.autoScale = true;
        this.tRow.sl1.autoScaleBounceBack = true;
        GenericCharacteristicTableRow genericCharacteristicTableRow = this.tRow;
        String iconPrefix = getIconPrefix();
        String str = LPSTK_ACC_SERVICE_UUID;
        genericCharacteristicTableRow.setIcon(iconPrefix, str, "motion");
        this.tRow.title.setText("Accelerometer Data");
        this.tRow.uuidLabel.setText(str);
        this.tRow.value.setText("X:0.00G, Y:0.00G, Z:0.00G");
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(LPSTK_ACC_SERVICE_UUID) == 0;
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        String str = "%x    %x(%d)";
        if (bluetoothGattCharacteristic.equals(this.accXDatC)) {
            this.lastXVal = ((float) bleUtility.BUILD_INT16(this.accXDatC.getValue()[1], this.accXDatC.getValue()[0])) / 1024.0f;
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("X:");
            sb.append(String.format(str, new Object[]{Integer.valueOf(bleUtility.BUILD_UINT16(this.accXDatC.getValue()[1], this.accXDatC.getValue()[0])), Integer.valueOf(bleUtility.BUILD_INT16(this.accXDatC.getValue()[1], this.accXDatC.getValue()[0])), Integer.valueOf(bleUtility.BUILD_INT16(this.accXDatC.getValue()[1], this.accXDatC.getValue()[0]))}));
            Log.d(str2, sb.toString());
        } else if (bluetoothGattCharacteristic.equals(this.accYDatC)) {
            this.lastYVal = ((float) bleUtility.BUILD_INT16(this.accYDatC.getValue()[1], this.accYDatC.getValue()[0])) / 1024.0f;
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Y:");
            sb2.append(String.format(str, new Object[]{Integer.valueOf(bleUtility.BUILD_UINT16(this.accYDatC.getValue()[1], this.accYDatC.getValue()[0])), Integer.valueOf(bleUtility.BUILD_INT16(this.accYDatC.getValue()[1], this.accYDatC.getValue()[0])), Integer.valueOf(bleUtility.BUILD_INT16(this.accYDatC.getValue()[1], this.accYDatC.getValue()[0]))}));
            Log.d(str3, sb2.toString());
        } else if (bluetoothGattCharacteristic.equals(this.accZDatC)) {
            this.lastZVal = ((float) bleUtility.BUILD_INT16(this.accZDatC.getValue()[1], this.accZDatC.getValue()[0])) / 1024.0f;
            String str4 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Z:");
            sb3.append(String.format(str, new Object[]{Integer.valueOf(bleUtility.BUILD_UINT16(this.accZDatC.getValue()[1], this.accZDatC.getValue()[0])), Integer.valueOf(bleUtility.BUILD_INT16(this.accZDatC.getValue()[1], this.accZDatC.getValue()[0])), Integer.valueOf(bleUtility.BUILD_INT16(this.accZDatC.getValue()[1], this.accZDatC.getValue()[0]))}));
            Log.d(str4, sb3.toString());
        }
        if (!this.tRow.config) {
            this.tRow.value.setText(Html.fromHtml(String.format("<font color='red'>X:%.2fG</font>, <font color='#009B7D'>Y:%.2fG</font>, <font color='black'>Z:%.2fG</font>", new Object[]{Float.valueOf(this.lastXVal), Float.valueOf(this.lastYVal), Float.valueOf(this.lastZVal)})));
        }
        this.tRow.sl1.addValue(this.lastXVal);
        this.tRow.sl1.addValue(this.lastYVal, 1);
        this.tRow.sl1.addValue(this.lastZVal, 2);
    }

    public Map<String, String> getMQTTMap() {
        HashMap hashMap = new HashMap();
        String str = "%.2f";
        String str2 = "acc_x";
        hashMap.put(str2, String.format(str, new Object[]{Float.valueOf(this.lastXVal)}));
        String str3 = "acc_y";
        hashMap.put(str3, String.format(str, new Object[]{Float.valueOf(this.lastYVal)}));
        String str4 = "acc_z";
        hashMap.put(str4, String.format(str, new Object[]{Float.valueOf(this.lastZVal)}));
        return hashMap;
    }

    public void onResume() {
        super.onResume();
    }

    public void configureService() {
        int characteristicNotificationSync = this.dev.setCharacteristicNotificationSync(this.accXDatC, true);
        String str = "Sensor notification enable failed: ";
        if (characteristicNotificationSync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.accXDatC;
            if (bluetoothGattCharacteristic != null) {
                printError(str, bluetoothGattCharacteristic, characteristicNotificationSync);
            }
        }
        int characteristicNotificationSync2 = this.dev.setCharacteristicNotificationSync(this.accYDatC, true);
        if (characteristicNotificationSync2 != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic2 = this.accYDatC;
            if (bluetoothGattCharacteristic2 != null) {
                printError(str, bluetoothGattCharacteristic2, characteristicNotificationSync2);
            }
        }
        int characteristicNotificationSync3 = this.dev.setCharacteristicNotificationSync(this.accZDatC, true);
        if (characteristicNotificationSync3 != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic3 = this.accZDatC;
            if (bluetoothGattCharacteristic3 != null) {
                printError(str, bluetoothGattCharacteristic3, characteristicNotificationSync3);
            }
        }
        this.isConfigured = true;
    }

    public void deConfigureService() {
        int characteristicNotificationSync = this.dev.setCharacteristicNotificationSync(this.accXDatC, false);
        String str = "Sensor notification enable failed: ";
        if (characteristicNotificationSync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.accXDatC;
            if (bluetoothGattCharacteristic != null) {
                printError(str, bluetoothGattCharacteristic, characteristicNotificationSync);
            }
        }
        int characteristicNotificationSync2 = this.dev.setCharacteristicNotificationSync(this.accYDatC, false);
        if (characteristicNotificationSync2 != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic2 = this.accYDatC;
            if (bluetoothGattCharacteristic2 != null) {
                printError(str, bluetoothGattCharacteristic2, characteristicNotificationSync2);
            }
        }
        int characteristicNotificationSync3 = this.dev.setCharacteristicNotificationSync(this.accZDatC, false);
        if (characteristicNotificationSync3 != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic3 = this.accZDatC;
            if (bluetoothGattCharacteristic3 != null) {
                printError(str, bluetoothGattCharacteristic3, characteristicNotificationSync3);
            }
        }
        this.isConfigured = false;
    }

    public void enableService() {
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.accEnC, 1);
        if (writeCharacteristicAsync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.accEnC;
            if (bluetoothGattCharacteristic != null) {
                printError("Sensor enable failed: ", bluetoothGattCharacteristic, writeCharacteristicAsync);
            }
        }
        this.isEnabled = true;
    }

    public void disableService() {
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.accEnC, 0);
        if (writeCharacteristicAsync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.accEnC;
            if (bluetoothGattCharacteristic != null) {
                printError("Sensor enable failed: ", bluetoothGattCharacteristic, writeCharacteristicAsync);
            }
        }
        this.isEnabled = false;
    }
}
