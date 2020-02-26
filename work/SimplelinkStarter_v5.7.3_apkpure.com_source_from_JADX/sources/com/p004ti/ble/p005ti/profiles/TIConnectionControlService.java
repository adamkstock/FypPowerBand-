package com.p004ti.ble.p005ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.p000v4.view.MotionEventCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.ToggleButton;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.ti.profiles.TIConnectionControlService */
public class TIConnectionControlService extends GenericBluetoothProfile {
    public static final String connectionControlService_UUID = "f000ccc0-0451-4000-b000-000000000000";
    public static final String currentConnectionParams_UUID = "f000ccc1-0451-4000-b000-000000000000";
    public static final String disconnectRequest_UUID = "f000ccc3-0451-4000-b000-000000000000";
    public static final String requestConnectionParams_UUID = "f000ccc2-0451-4000-b000-000000000000";
    public TIConnectionControlServiceTableRow cRow;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic curConParamsChar;
    private BluetoothGattCharacteristic disconnectReqChar;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic reqConParamsChar;

    public void deConfigureService() {
    }

    public void disableService() {
    }

    public void enableService() {
    }

    public void periodWasUpdated(int i) {
    }

    public TIConnectionControlService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.cRow = new TIConnectionControlServiceTableRow(context);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals("f000ccc1-0451-4000-b000-000000000000")) {
                this.curConParamsChar = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals("f000ccc2-0451-4000-b000-000000000000")) {
                this.reqConParamsChar = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals("f000ccc3-0451-4000-b000-000000000000")) {
                this.disconnectReqChar = bluetoothGattCharacteristic;
            }
        }
        this.cRow.setIcon(getIconPrefix(), bluetoothGattService.getUuid().toString(), "connection_control");
        ((SeekBar) this.cRow.findViewById(R.id.cctr_set_connection_interval_seekbar)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TIConnectionControlService.this.cRow.setConIntText(i + 6);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                TIConnectionControlService.this.cRow.invalidateConnectionParameters();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = (int) (((float) (seekBar.getProgress() + 6)) / 1.25f);
                int i = progress + 10;
                int i2 = progress * 4;
                TIConnectionControlService.this.dev.writeCharacteristicAsync(TIConnectionControlService.this.reqConParamsChar, new byte[]{(byte) (progress & 255), (byte) ((progress & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (i & 255), (byte) ((i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), 0, 0, (byte) (i2 & 255), (byte) ((i2 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8)});
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        TIConnectionControlService.this.dev.readCharacteristicAsync(TIConnectionControlService.this.curConParamsChar);
                    }
                }).start();
            }
        });
        final ToggleButton toggleButton = (ToggleButton) this.cRow.findViewById(R.id.cctr_high_priority);
        final ToggleButton toggleButton2 = (ToggleButton) this.cRow.findViewById(R.id.cctr_balanced_priority);
        final ToggleButton toggleButton3 = (ToggleButton) this.cRow.findViewById(R.id.cctr_low_pwr_priority);
        toggleButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d("TIConnectionControlService", "High Priority touched !");
                TIConnectionControlService.this.dev.setCurrentConnectionPriority(1);
                toggleButton.setChecked(true);
                toggleButton2.setChecked(false);
                toggleButton3.setChecked(false);
                TIConnectionControlService.this.cRow.invalidateConnectionParameters();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        TIConnectionControlService.this.dev.readCharacteristicAsync(TIConnectionControlService.this.curConParamsChar);
                    }
                }).start();
            }
        });
        toggleButton2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d("TIConnectionControlService", "Balanced Priority touched !");
                TIConnectionControlService.this.dev.setCurrentConnectionPriority(0);
                toggleButton.setChecked(false);
                toggleButton2.setChecked(true);
                toggleButton3.setChecked(false);
                TIConnectionControlService.this.cRow.invalidateConnectionParameters();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        TIConnectionControlService.this.dev.readCharacteristicAsync(TIConnectionControlService.this.curConParamsChar);
                    }
                }).start();
            }
        });
        toggleButton3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d("TIConnectionControlService", "Low Power Priority touched !");
                TIConnectionControlService.this.dev.setCurrentConnectionPriority(2);
                toggleButton.setChecked(false);
                toggleButton2.setChecked(false);
                toggleButton3.setChecked(true);
                TIConnectionControlService.this.cRow.invalidateConnectionParameters();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        TIConnectionControlService.this.dev.readCharacteristicAsync(TIConnectionControlService.this.curConParamsChar);
                    }
                }).start();
            }
        });
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo("f000ccc0-0451-4000-b000-000000000000") == 0;
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void configureService() {
        this.dev.readCharacteristicAsync(this.curConParamsChar);
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.equals(this.curConParamsChar)) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (value.length > 5) {
                byte b = ((value[1] & 255) << 8) | (value[0] & 255);
                byte b2 = ((value[3] & 255) << 8) | (value[2] & 255);
                byte b3 = (value[4] & 255) | ((value[5] & 255) << 8);
                StringBuilder sb = new StringBuilder();
                sb.append("Got new connection parameters : ");
                sb.append(b);
                String str = ", ";
                sb.append(str);
                sb.append(b2);
                sb.append(str);
                sb.append(b3);
                Log.d("TIConnectionControlService", sb.toString());
                this.cRow.setConnectionParameters(b, b2, b3, this.dev.getCurrentConnectionPriority());
            }
        }
    }

    public TableRow getTableRow() {
        return this.cRow;
    }

    public static float[] getValuesFromBytes(byte[] bArr) {
        if (bArr.length <= 5) {
            return new float[3];
        }
        return new float[]{((float) (((bArr[1] & 255) << 8) | (bArr[0] & 255))) * 1.25f, ((float) (((bArr[3] & 255) << 8) | (bArr[2] & 255))) * 1.25f, ((float) ((bArr[4] & 255) | ((bArr[5] & 255) << 8))) * 1.25f};
    }
}
