package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import com.p004ti.ble.common.GenericBluetoothProfile;

/* renamed from: com.ti.ble.sensortag.SensorTagTestProfile */
public class SensorTagTestProfile extends GenericBluetoothProfile {
    BluetoothGattCharacteristic testResult;

    public void deConfigureService() {
    }

    public void disableService() {
    }

    public void enableService() {
    }

    public SensorTagTestProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.testResult = bluetoothGattService.getCharacteristic(SensorTagGatt.UUID_TST_DATA);
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_TST_SERV.toString()) == 0;
    }

    public void configureService() {
        if (this.testResult != null) {
            this.dev.readCharacteristicSync(this.testResult);
        }
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super.didReadValueForCharacteristic(bluetoothGattCharacteristic);
    }
}
