package com.p004ti.ble.bluetooth_le_controller;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import java.util.Date;

/* renamed from: com.ti.ble.bluetooth_le_controller.BluetoothLETransaction */
public class BluetoothLETransaction {
    public static final String TAG = BluetoothLETransaction.class.getSimpleName();
    public BluetoothGattCharacteristic characteristic;
    public byte[] dat;
    public BluetoothGattDescriptor descriptor;
    public BluetoothLEDevice dev;
    public boolean transactionFinished;
    public Date transactionStartDate;
    public BluetoothLETransactionType transactionType;

    /* renamed from: com.ti.ble.bluetooth_le_controller.BluetoothLETransaction$BluetoothLETransactionType */
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
        DISABLE_INDICATION_SYNC,
        READ_DESC_SYNC
    }

    public BluetoothLETransaction(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothLETransactionType bluetoothLETransactionType, byte[] bArr) {
        this.dev = bluetoothLEDevice;
        this.characteristic = bluetoothGattCharacteristic;
        this.transactionType = bluetoothLETransactionType;
        this.dat = bArr;
    }

    public BluetoothLETransaction(BluetoothLEDevice bluetoothLEDevice, BluetoothGattDescriptor bluetoothGattDescriptor, BluetoothLETransactionType bluetoothLETransactionType, byte[] bArr) {
        this.dev = bluetoothLEDevice;
        this.descriptor = bluetoothGattDescriptor;
        this.transactionType = bluetoothLETransactionType;
        this.dat = bArr;
    }
}
