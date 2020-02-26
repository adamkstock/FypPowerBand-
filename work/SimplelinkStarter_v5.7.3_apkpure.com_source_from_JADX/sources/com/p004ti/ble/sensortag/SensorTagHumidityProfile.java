package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.p004ti.util.Point3D;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ti.ble.sensortag.SensorTagHumidityProfile */
public class SensorTagHumidityProfile extends GenericBluetoothProfile {
    private float lastValue = 0.0f;

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public SensorTagHumidityProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context, 1000, true);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_HUM_DATA.toString())) {
                this.dataC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_HUM_CONF.toString())) {
                this.configC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_HUM_PERI.toString())) {
                this.periodC = bluetoothGattCharacteristic;
            }
        }
        this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "humidity");
        this.tRow.title.setText("Humidity Data");
        this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
        this.tRow.value.setText("0.0%rH");
        this.tRow.sl1.maxVal(Float.valueOf(100.0f));
        this.tRow.sl1.minVal(Float.valueOf(0.0f));
        this.tRow.sl1.setColor(0, 0, 0, 0, 1);
        this.tRow.sl1.setColor(0, 0, 0, 0, 2);
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        if (bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_HUM_SERV.toString()) != 0) {
            return false;
        }
        Log.d("Test", "Match !");
        return true;
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        Point3D point3D;
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (bluetoothGattCharacteristic.equals(this.dataC)) {
            String str = "%.1f %%rH";
            if (this.mBTDevice.getName().contains("Multi-Sensor") || this.mBTDevice.getName().contains("LPSTK:)")) {
                int i = ByteBuffer.wrap(value).getInt();
                byte[] bArr = new byte[4];
                ByteBuffer.wrap(bArr).putInt(((i >> 24) & 255) | ((i & 255) << 24) | ((65280 & i) << 8) | ((16711680 & i) >> 8));
                float f = ByteBuffer.wrap(bArr).getFloat();
                this.tRow.value.setText(String.format(str, new Object[]{Float.valueOf(f)}));
                this.tRow.sl1.addValue(f);
                this.lastValue = f;
                return;
            }
            if (SensorTagUtil.isSensorTag2(this.mBTDevice)) {
                point3D = Sensor.HUMIDITY2.convert(value);
            } else {
                point3D = Sensor.HUMIDITY.convert(value);
            }
            if (!this.tRow.config) {
                this.tRow.value.setText(String.format(str, new Object[]{Double.valueOf(point3D.f71x)}));
            }
            this.tRow.sl1.addValue((float) point3D.f71x);
            this.lastValue = (float) point3D.f71x;
        }
    }

    public Map<String, String> getMQTTMap() {
        if (SensorTagUtil.isSensorTag2(this.mBTDevice)) {
            Sensor.HUMIDITY2.convert(this.dataC.getValue());
        } else {
            Sensor.HUMIDITY.convert(this.dataC.getValue());
        }
        HashMap hashMap = new HashMap();
        hashMap.put("humidity", String.format("%.2f", new Object[]{Float.valueOf(this.lastValue)}));
        return hashMap;
    }
}
