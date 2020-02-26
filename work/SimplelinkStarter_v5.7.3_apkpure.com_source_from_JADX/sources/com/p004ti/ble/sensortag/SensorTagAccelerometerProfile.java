package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import com.p004ti.ble.common.GattInfo;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.p004ti.util.Point3D;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* renamed from: com.ti.ble.sensortag.SensorTagAccelerometerProfile */
public class SensorTagAccelerometerProfile extends GenericBluetoothProfile {
    public SensorTagAccelerometerProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context, 1000, true);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_ACC_DATA.toString())) {
                this.dataC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_ACC_CONF.toString())) {
                this.configC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_ACC_PERI.toString())) {
                this.periodC = bluetoothGattCharacteristic;
            }
        }
        this.tRow.sl1.autoScale = true;
        this.tRow.sl1.autoScaleBounceBack = true;
        this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString());
        this.tRow.title.setText(GattInfo.uuidToName(UUID.fromString(this.dataC.getUuid().toString())));
        this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
        this.tRow.value.setText("X:0.00G, Y:0.00G, Z:0.00G");
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_ACC_SERV.toString()) == 0;
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.equals(this.dataC)) {
            Point3D convert = Sensor.ACCELEROMETER.convert(this.dataC.getValue());
            if (!this.tRow.config) {
                this.tRow.value.setText(String.format("X:%.2fG, Y:%.2fG, Z:%.2fG", new Object[]{Double.valueOf(convert.f71x), Double.valueOf(convert.f72y), Double.valueOf(convert.f73z)}));
            }
            this.tRow.sl1.addValue((float) convert.f71x);
            this.tRow.sl1.addValue((float) convert.f72y, 1);
            this.tRow.sl1.addValue((float) convert.f73z, 2);
        }
    }

    public Map<String, String> getMQTTMap() {
        Point3D convert = Sensor.ACCELEROMETER.convert(this.dataC.getValue());
        HashMap hashMap = new HashMap();
        String str = "%.2f";
        String str2 = "acc_x";
        hashMap.put(str2, String.format(str, new Object[]{Double.valueOf(convert.f71x)}));
        String str3 = "acc_y";
        hashMap.put(str3, String.format(str, new Object[]{Double.valueOf(convert.f72y)}));
        hashMap.put("acc_z", String.format(str, new Object[]{Double.valueOf(convert.f73z)}));
        return hashMap;
    }
}
