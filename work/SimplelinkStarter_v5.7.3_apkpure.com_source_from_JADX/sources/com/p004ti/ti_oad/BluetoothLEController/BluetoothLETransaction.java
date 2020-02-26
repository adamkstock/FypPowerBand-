package com.p004ti.ti_oad.BluetoothLEController;

import android.bluetooth.BluetoothGattCharacteristic;
import java.util.Date;

/* renamed from: com.ti.ti_oad.BluetoothLEController.BluetoothLETransaction */
public class BluetoothLETransaction {
    public static final String TAG = BluetoothLETransaction.class.getSimpleName();
    public BluetoothGattCharacteristic characteristic;
    public byte[] dat;
    public BluetoothLEDevice dev;
    public boolean transactionFinished;
    public Date transactionStartDate;
    public BluetoothLETransactionType transactionType;

    /* renamed from: com.ti.ti_oad.BluetoothLEController.BluetoothLETransaction$BluetoothLETransactionType */
    public enum BluetoothLETransactionType {
        READ_ASYNC,
        READ_SYNC,
        WRITE_ASYNC,
        WRITE_SYNC,
        ENABLE_NOTIFICATION_ASYNC,
        ENABLE_NOTIFICATION_SYNC,
        DISABLE_NOTIFICATION_ASYNC,
        DISABLE_NOTIFICATION_SYNC,
        ENABLE_INDICATION_ASYNC,
        ENABLE_INDICATION_SYNC,
        DISABLE_INDICATION_ASYNC,
        DISABLE_INDICATION_SYNC
    }

    public BluetoothLETransaction(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothLETransactionType bluetoothLETransactionType, byte[] bArr) {
        this.dev = bluetoothLEDevice;
        this.characteristic = bluetoothGattCharacteristic;
        this.transactionType = bluetoothLETransactionType;
        this.dat = bArr;
    }
}
