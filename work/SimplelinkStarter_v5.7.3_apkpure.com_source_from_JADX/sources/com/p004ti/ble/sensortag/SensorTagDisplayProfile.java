package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableRow;
import com.p004ti.ble.common.GenericBluetoothProfile;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.ti.ble.sensortag.SensorTagDisplayProfile */
public class SensorTagDisplayProfile extends GenericBluetoothProfile {
    public static final String TI_SENSORTAG_TWO_DISPLAY_CONTROL_UUID = "f000ad02-0451-4000-b000-000000000000";
    public static final String TI_SENSORTAG_TWO_DISPLAY_DATA_UUID = "f000ad01-0451-4000-b000-000000000000";
    public static final String TI_SENSORTAG_TWO_DISPLAY_SERVICE_UUID = "f000ad00-0451-4000-b000-000000000000";
    SensorTagDisplayTableRow cRow;
    Timer displayClock;

    /* renamed from: com.ti.ble.sensortag.SensorTagDisplayProfile$clockTask */
    private class clockTask extends TimerTask {
        private clockTask() {
        }

        public void run() {
            Date date = new Date();
            final String format = String.format("%02d:%02d:%02d        ", new Object[]{Integer.valueOf(date.getHours()), Integer.valueOf(date.getMinutes()), Integer.valueOf(date.getSeconds())});
            byte[] bArr = new byte[format.length()];
            for (int i = 0; i < format.length(); i++) {
                bArr[i] = (byte) format.charAt(i);
            }
            if (SensorTagDisplayProfile.this.dataC != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        SensorTagDisplayProfile.this.cRow.displayText.setText(format);
                    }
                });
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public SensorTagDisplayProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.cRow = new SensorTagDisplayTableRow(context);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(TI_SENSORTAG_TWO_DISPLAY_DATA_UUID)) {
                this.dataC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(TI_SENSORTAG_TWO_DISPLAY_CONTROL_UUID)) {
                this.configC = bluetoothGattCharacteristic;
            }
        }
        this.cRow.displayClock.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    if (SensorTagDisplayProfile.this.displayClock != null) {
                        SensorTagDisplayProfile.this.displayClock.cancel();
                    }
                    SensorTagDisplayProfile.this.displayClock = new Timer();
                    SensorTagDisplayProfile.this.displayClock.schedule(new clockTask(), 1000, 1000);
                } else if (SensorTagDisplayProfile.this.displayClock != null) {
                    SensorTagDisplayProfile.this.displayClock.cancel();
                }
            }
        });
        this.cRow.displayInvert.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (SensorTagDisplayProfile.this.configC != null && SensorTagDisplayProfile.this.dev.writeCharacteristicAsync(SensorTagDisplayProfile.this.configC, 5) != 0) {
                    Log.d("SensorTagDisplayProfile", "Error writing config characteristic !");
                }
            }
        });
        this.cRow.displayText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                StringBuilder sb = new StringBuilder();
                sb.append("New Display Text:");
                sb.append(charSequence);
                String str = "SensorTagDisplayProfile";
                Log.d(str, sb.toString());
                byte[] bArr = new byte[charSequence.length()];
                for (int i4 = 0; i4 < charSequence.length(); i4++) {
                    bArr[i4] = (byte) charSequence.charAt(i4);
                }
                if (SensorTagDisplayProfile.this.dataC != null && SensorTagDisplayProfile.this.dev.writeCharacteristicAsync(SensorTagDisplayProfile.this.dataC, bArr) != 0) {
                    Log.d(str, "Error writing data characteristic !");
                }
            }
        });
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(TI_SENSORTAG_TWO_DISPLAY_SERVICE_UUID) == 0;
    }

    public void enableService() {
        if (this.cRow.displayClock.isChecked()) {
            Timer timer = this.displayClock;
            if (timer != null) {
                timer.cancel();
            }
            this.displayClock = new Timer();
            this.displayClock.schedule(new clockTask(), 1000, 1000);
        }
    }

    public void disableService() {
        Timer timer = this.displayClock;
        if (timer != null) {
            timer.cancel();
        }
    }

    public TableRow getTableRow() {
        return this.cRow;
    }
}
