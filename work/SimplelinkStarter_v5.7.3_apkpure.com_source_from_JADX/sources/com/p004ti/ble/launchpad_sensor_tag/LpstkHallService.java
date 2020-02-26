package com.p004ti.ble.launchpad_sensor_tag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.GenericCharacteristicTableRow;

/* renamed from: com.ti.ble.launchpad_sensor_tag.LpstkHallService */
public class LpstkHallService extends GenericBluetoothProfile {
    public static final String LPSTK_HALL_CONFIG_CHARACTERISTIC_UUID = "f000aa32-0451-4000-b000-000000000000";
    public static final String LPSTK_HALL_DATA_CHARACTERISTIC_UUID = "f000aa31-0451-4000-b000-000000000000";
    public static final String LPSTK_HALL_PERIOD_CHARACTERISTIC_UUID = "f000aa33-0451-4000-b000-000000000000";
    public static final String LPSTK_HALL_SERVICE_UUID = "f000aa30-0451-4000-b000-000000000000";
    public static final String TAG = LpstkHallService.class.getSimpleName();

    public LpstkHallService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context, 1000, true);
        this.tRow.title.setText("Hall Sensor Data");
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(LPSTK_HALL_SERVICE_UUID) == 0;
    }
}
