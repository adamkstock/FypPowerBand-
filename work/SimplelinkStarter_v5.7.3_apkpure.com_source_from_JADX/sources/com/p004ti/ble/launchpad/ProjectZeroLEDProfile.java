package com.p004ti.ble.launchpad;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import java.util.HashMap;
import java.util.Map;

import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.launchpad.ProjectZeroLEDProfile */
public class ProjectZeroLEDProfile extends GenericBluetoothProfile {
    static final String PRZ_LED0_STATE_CHARACTERISIC_UUID = "f0001111-0451-4000-b000-000000000000";
    static final String PRZ_LED1_STATE_CHARACTERISIC_UUID = "f0001112-0451-4000-b000-000000000000";
    static final String PRZ_LED_SERVICE_UUID = "f0001110-0451-4000-b000-000000000000";
    BluetoothGattCharacteristic led0C;
    boolean led0On;
    BluetoothGattCharacteristic led1C;
    boolean led1On;
    ProjectZeroLEDTableRow pTr;

    public void deConfigureService() {
    }

    public void disableService() {
    }

    public void enableService() {
    }

    public ProjectZeroLEDProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.pTr = new ProjectZeroLEDTableRow(context);
        if (bluetoothDevice.getName().contains("Multi-Sensor") || bluetoothDevice.getName().contains("LPSTK:)")) {
            this.pTr.przLedIcon.setImageResource(R.mipmap.lpstk_led_service);
        }
        this.pTr.greenLed.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProjectZeroLEDProfile projectZeroLEDProfile = ProjectZeroLEDProfile.this;
                int i = 1;
                projectZeroLEDProfile.led1On = !projectZeroLEDProfile.led1On;
                BluetoothLEDevice foobar = ProjectZeroLEDProfile.this.dev;
                BluetoothGattCharacteristic bluetoothGattCharacteristic = ProjectZeroLEDProfile.this.led1C;
                if (!ProjectZeroLEDProfile.this.led1On) {
                    i = 0;
                }
                foobar.writeCharacteristicSync(bluetoothGattCharacteristic, (byte) i);
                ProjectZeroLEDProfile.this.setPictureForLeds();
            }
        });
        this.pTr.redLed.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProjectZeroLEDProfile projectZeroLEDProfile = ProjectZeroLEDProfile.this;
                int i = 1;
                projectZeroLEDProfile.led0On = !projectZeroLEDProfile.led0On;
                BluetoothLEDevice access$100 = ProjectZeroLEDProfile.this.dev;
                BluetoothGattCharacteristic bluetoothGattCharacteristic = ProjectZeroLEDProfile.this.led0C;
                if (!ProjectZeroLEDProfile.this.led0On) {
                    i = 0;
                }
                access$100.writeCharacteristicSync(bluetoothGattCharacteristic, (byte) i);
                ProjectZeroLEDProfile.this.setPictureForLeds();
            }
        });
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(PRZ_LED0_STATE_CHARACTERISIC_UUID)) {
                this.led0C = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(PRZ_LED1_STATE_CHARACTERISIC_UUID)) {
                this.led1C = bluetoothGattCharacteristic;
            }
        }
    }

    public void configureService() {
        this.dev.readCharacteristicAsync(this.led0C);
        this.dev.readCharacteristicAsync(this.led1C);
    }

    public TableRow getTableRow() {
        return this.pTr;
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareToIgnoreCase(PRZ_LED_SERVICE_UUID) == 0;
    }

    public void setPictureForLeds() {
        if (this.led0On) {
            this.pTr.redLed.setImageResource(R.mipmap.project_zero_red_led_on);
        } else {
            this.pTr.redLed.setImageResource(R.mipmap.project_zero_red_led_off);
        }
        if (this.led1On) {
            this.pTr.greenLed.setImageResource(R.mipmap.project_zero_green_led_on);
        } else {
            this.pTr.greenLed.setImageResource(R.mipmap.project_zero_green_led_off);
        }
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(this.led0C.getUuid().toString())) {
            if (bluetoothGattCharacteristic.getValue()[0] != 0) {
                this.led0On = true;
            } else {
                this.led0On = false;
            }
            setPictureForLeds();
        } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(this.led1C.getUuid().toString())) {
            if (bluetoothGattCharacteristic.getValue()[0] != 0) {
                this.led1On = true;
            } else {
                this.led1On = false;
            }
            setPictureForLeds();
        }
    }

    public Map<String, String> getMQTTMap() {
        HashMap hashMap = new HashMap();
        String str = "%d";
        String str2 = "prz_red_led";
        hashMap.put(str2, String.format(str, new Object[]{Integer.valueOf(this.led0On ? 1 : 0)}));
        String str3 = "prz_green_led";
        hashMap.put(str3, String.format(str, new Object[]{Integer.valueOf(this.led1On ? 1 : 0)}));
        return hashMap;
    }
}
