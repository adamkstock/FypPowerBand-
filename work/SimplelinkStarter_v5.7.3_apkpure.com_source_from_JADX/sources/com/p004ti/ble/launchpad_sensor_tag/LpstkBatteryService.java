package com.p004ti.ble.launchpad_sensor_tag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.launchpad_sensor_tag.LpstkBatteryService */
public class LpstkBatteryService extends GenericBluetoothProfile {
    public static final String LPSTK_BATTERY_LEVEL_CHARACTERISTIC_UUID = "f0002a19-0451-4000-b000-000000000000";
    public static final String LPSTK_BATTERY_SERVICE_UUID = "f000180f-0451-4000-b000-000000000000";
    public static final String TAG = LpstkBatteryService.class.getSimpleName();
    private BluetoothGattCharacteristic batteryLevel;

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public LpstkBatteryService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context, 1000, true);
        this.tRow.title.setText("Battery Level");
        this.tRow.icon.setImageResource(R.mipmap.lpstk_battery);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(LPSTK_BATTERY_LEVEL_CHARACTERISTIC_UUID)) {
                this.batteryLevel = bluetoothGattCharacteristic;
            }
        }
    }

    public void enableService() {
        if (this.batteryLevel != null) {
            this.dev.setCharacteristicNotificationSync(this.batteryLevel, true);
            this.dev.readCharacteristicSync(this.batteryLevel);
        }
    }

    public void disableService() {
        if (this.batteryLevel != null) {
            this.dev.setCharacteristicNotificationSync(this.batteryLevel, false);
        }
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.getUuid().toString().equals(this.batteryLevel.getUuid().toString())) {
            this.tRow.value.setText(String.format("%d%%", new Object[]{Byte.valueOf(this.batteryLevel.getValue()[0])}));
            this.tRow.sl1.addValue((float) this.batteryLevel.getValue()[0]);
        }
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.getUuid().toString().equals(this.batteryLevel.getUuid().toString())) {
            this.tRow.value.setText(String.format("%d%%", new Object[]{Byte.valueOf(this.batteryLevel.getValue()[0])}));
            this.tRow.sl1.addValue((float) this.batteryLevel.getValue()[0]);
        }
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(LPSTK_BATTERY_SERVICE_UUID) == 0;
    }
}
