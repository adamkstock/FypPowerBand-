package com.p004ti.ble.sensortag;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/* renamed from: com.ti.ble.sensortag.SensorTagApplicationClass */
public class SensorTagApplicationClass extends Application {
    private static final int REQ_ENABLE_BT = 0;
    public static BluetoothManager mBluetoothManager;
    public Activity currentActivity;
    public boolean mBleSupported = true;
    public BluetoothAdapter mBtAdapter = null;
    public boolean mBtAdapterEnabled = false;
    private IntentFilter mFilter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction()) && SensorTagApplicationClass.this.mBtAdapter.getState() != 10) {
            }
        }
    };

    public void onCreate() {
        super.onCreate();
    }
}
