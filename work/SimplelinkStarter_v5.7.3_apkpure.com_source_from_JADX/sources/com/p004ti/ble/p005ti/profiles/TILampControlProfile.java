package com.p004ti.ble.p005ti.profiles;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import com.p004ti.ble.common.GenericBluetoothProfile;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.ti.ble.ti.profiles.TILampControlProfile */
public class TILampControlProfile extends GenericBluetoothProfile {
    private static final String lightBlue_UUID = "0000ffb3-0000-1000-8000-00805f9b34fb";
    private static final String lightCompound_UUID = "0000ffb5-0000-1000-8000-00805f9b34fb";
    private static final String lightGreen_UUID = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static final String lightRed_UUID = "0000ffb1-0000-1000-8000-00805f9b34fb";
    private static final String lightService_UUID = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String lightWhite_UUID = "0000ffb4-0000-1000-8000-00805f9b34fb";

    /* renamed from: B */
    int f56B = 10;

    /* renamed from: G */
    int f57G = 10;

    /* renamed from: R */
    int f58R = 10;

    /* renamed from: W */
    int f59W = 10;
    TILampControlProfileTableRow cRow;
    BluetoothGattCharacteristic compoundCharacteristic;
    BroadcastReceiver lightControlReceiver;
    Timer updateLampTimer;

    /* renamed from: com.ti.ble.ti.profiles.TILampControlProfile$updateCompoundTask */
    private class updateCompoundTask extends TimerTask {
        byte oldB;
        byte oldG;
        byte oldR;
        byte oldW;

        private updateCompoundTask() {
        }

        public void run() {
            if (TILampControlProfile.this.compoundCharacteristic != null) {
                byte[] bArr = {(byte) TILampControlProfile.this.f58R, (byte) TILampControlProfile.this.f57G, (byte) TILampControlProfile.this.f56B, (byte) TILampControlProfile.this.f59W};
                if (bArr[0] != this.oldR || bArr[1] != this.oldG || bArr[2] != this.oldB || bArr[3] != this.oldW) {
                    if (TILampControlProfile.this.dev.writeCharacteristicAsync(TILampControlProfile.this.compoundCharacteristic, bArr) != 0) {
                        Log.d("TILampControlProfile", "Error writing compound color characteristic !");
                    }
                    this.oldR = bArr[0];
                    this.oldG = bArr[1];
                    this.oldB = bArr[2];
                    this.oldW = bArr[3];
                }
            }
        }
    }

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void periodWasUpdated(int i) {
    }

    public TILampControlProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.cRow = new TILampControlProfileTableRow(context);
        this.cRow.setColorButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TILampControlDialogFragment.newInstance().show(((Activity) TILampControlProfile.this.context).getFragmentManager(), "LampSetting");
            }
        });
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(lightCompound_UUID)) {
                this.compoundCharacteristic = bluetoothGattCharacteristic;
            }
        }
        this.tRow.setIcon(getIconPrefix(), bluetoothGattService.getUuid().toString());
        this.lightControlReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(TILampControlDialogFragment.ACTION_LAMP_HSI_COLOR_CHANGED)) {
                    TILampControlProfile.this.setLightHSV(intent.getDoubleExtra(TILampControlDialogFragment.EXTRA_LAMP_HSI_COLOR_H, 0.0d), intent.getDoubleExtra(TILampControlDialogFragment.EXTRA_LAMP_HSI_COLOR_S, 0.0d), intent.getDoubleExtra(TILampControlDialogFragment.EXTRA_LAMP_HSI_COLOR_I, 0.0d));
                }
            }
        };
        this.context.registerReceiver(this.lightControlReceiver, makeTILampBroadcastFilter());
    }

    public void setLightHSV(double d, double d2, double d3) {
        double d4 = ((d % 360.0d) * 3.14159d) / 180.0d;
        double d5 = 0.0d;
        double d6 = d2 > 0.0d ? d2 < 1.0d ? d2 : 1.0d : 0.0d;
        if (d3 > 0.0d) {
            d5 = d3 < 1.0d ? d3 : 1.0d;
        }
        if (d4 < 2.09439d) {
            double d7 = ((d6 * 255.0d) * d5) / 3.0d;
            double cos = Math.cos(d4) / Math.cos(1.047196667d - d4);
            this.f58R = (int) ((cos + 1.0d) * d7);
            this.f57G = (int) (d7 * ((1.0d - cos) + 1.0d));
            this.f56B = 0;
            this.f59W = (int) ((1.0d - d6) * 255.0d * d5);
        } else if (d4 < 4.188787d) {
            double d8 = d4 - 2.09439d;
            double d9 = ((d6 * 255.0d) * d5) / 3.0d;
            double cos2 = Math.cos(d8) / Math.cos(1.047196667d - d8);
            this.f57G = (int) ((cos2 + 1.0d) * d9);
            this.f56B = (int) (d9 * ((1.0d - cos2) + 1.0d));
            this.f58R = 0;
            this.f59W = (int) ((1.0d - d6) * 255.0d * d5);
        } else {
            double d10 = d4 - 4.188787d;
            double d11 = ((d6 * 255.0d) * d5) / 3.0d;
            double cos3 = Math.cos(d10) / Math.cos(1.047196667d - d10);
            this.f56B = (int) ((cos3 + 1.0d) * d11);
            this.f58R = (int) (d11 * ((1.0d - cos3) + 1.0d));
            this.f57G = 0;
            this.f59W = (int) ((1.0d - d6) * 255.0d * d5);
        }
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(lightService_UUID) == 0;
    }

    public void onPause() {
        super.onPause();
        this.context.unregisterReceiver(this.lightControlReceiver);
    }

    public void onResume() {
        super.onResume();
        this.context.registerReceiver(this.lightControlReceiver, makeTILampBroadcastFilter());
    }

    public void enableService() {
        this.updateLampTimer = new Timer();
        this.updateLampTimer.schedule(new updateCompoundTask(), 1000, 100);
    }

    public void disableService() {
        this.updateLampTimer.cancel();
        this.updateLampTimer = null;
    }

    private static IntentFilter makeTILampBroadcastFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TILampControlDialogFragment.ACTION_LAMP_HSI_COLOR_CHANGED);
        return intentFilter;
    }

    public TableRow getTableRow() {
        return this.cRow;
    }
}
