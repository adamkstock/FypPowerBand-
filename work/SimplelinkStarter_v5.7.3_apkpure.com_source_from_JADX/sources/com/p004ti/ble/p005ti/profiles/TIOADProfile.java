package com.p004ti.ble.p005ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;
import com.p004ti.ble.common.GenericBluetoothProfile;

/* renamed from: com.ti.ble.ti.profiles.TIOADProfile */
public class TIOADProfile extends GenericBluetoothProfile {
    public static final String ACTION_PREPARE_FOR_OAD = "com.ti.ble.ti.profiles.ACTION_PREPARE_FOR_OAD";
    public static final String ACTION_RESTORE_AFTER_OAD = "com.ti.ble.ti.profiles.ACTION_RESTORE_AFTER_OAD";
    public static final String oadBlockRequest_UUID = "f000ffc2-0451-4000-b000-000000000000";
    public static final String oadImageNotify_UUID = "f000ffc1-0451-4000-b000-000000000000";
    public static final String oadImageStatus_UUID = "f000ffc4-0451-4000-b000-000000000000";
    public static final String oadService_UUID = "f000ffc0-0451-4000-b000-000000000000";
    private BroadcastReceiver brRecv;
    private boolean clickReceiverRegistered = false;
    private String fwRev;

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void disableService() {
    }

    public void enableService() {
    }

    public void periodWasUpdated(int i) {
    }

    public TIOADProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new TIOADProfileTableRow(context);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(oadImageNotify_UUID)) {
                this.dataC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(oadBlockRequest_UUID)) {
                this.configC = bluetoothGattCharacteristic;
            }
        }
        this.tRow.title.setText("TI OAD Service");
        this.tRow.sl1.setVisibility(4);
        this.tRow.value.setVisibility(4);
        this.tRow.setIcon(getIconPrefix(), bluetoothGattService.getUuid().toString(), "oad");
        this.brRecv = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (TIOADProfileTableRow.ACTION_VIEW_CLICKED.equals(intent.getAction())) {
                    Log.d("TIOADProfile", "SHOW OAD DIALOG !");
                    TIOADProfile.this.prepareForOAD();
                }
            }
        };
        this.context.registerReceiver(this.brRecv, makeIntentFilter());
        this.clickReceiverRegistered = true;
    }

    public void onResume() {
        super.onResume();
        if (!this.clickReceiverRegistered) {
            this.brRecv = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (TIOADProfileTableRow.ACTION_VIEW_CLICKED.equals(intent.getAction())) {
                        Log.d("TIOADProfile", "SHOW OAD DIALOG !");
                        TIOADProfile.this.prepareForOAD();
                    }
                }
            };
            this.context.registerReceiver(this.brRecv, makeIntentFilter());
            this.clickReceiverRegistered = true;
        }
    }

    public void onPause() {
        super.onPause();
        if (this.clickReceiverRegistered) {
            this.context.unregisterReceiver(this.brRecv);
            this.clickReceiverRegistered = false;
        }
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(oadService_UUID) == 0;
    }

    public void prepareForOAD() {
        this.context.sendBroadcast(new Intent(ACTION_PREPARE_FOR_OAD));
    }

    public void didUpdateFirmwareRevision(String str) {
        this.fwRev = str;
        TextView textView = this.tRow.value;
        StringBuilder sb = new StringBuilder();
        sb.append("Current FW :");
        sb.append(str);
        textView.setText(sb.toString());
    }

    private static IntentFilter makeIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TIOADProfileTableRow.ACTION_VIEW_CLICKED);
        return intentFilter;
    }
}
