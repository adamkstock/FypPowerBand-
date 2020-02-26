package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.p004ti.util.Point3D;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ti.ble.sensortag.SensorTagLuxometerProfile */
public class SensorTagLuxometerProfile extends GenericBluetoothProfile {
    private float lastValue = 0.0f;

    public SensorTagLuxometerProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context, 1000, true);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_OPT_DATA.toString())) {
                this.dataC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_OPT_CONF.toString())) {
                this.configC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_OPT_PERI.toString())) {
                this.periodC = bluetoothGattCharacteristic;
            }
        }
        this.tRow.sl1.autoScale = true;
        this.tRow.sl1.autoScaleBounceBack = true;
        this.tRow.sl1.setColor(0, 0, 0, 0, 1);
        this.tRow.sl1.setColor(0, 0, 0, 0, 2);
        this.tRow.sl1.setColor(255, 0, 150, 125);
        this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "lightsensor");
        this.tRow.title.setText("Light Sensor Data");
        this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
        this.tRow.value.setText("0.0 Lux");
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_OPT_SERV.toString()) == 0;
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (bluetoothGattCharacteristic.equals(this.dataC)) {
            String str = "%.1f Lux";
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
            Point3D convert = Sensor.LUXOMETER.convert(value);
            if (!this.tRow.config) {
                this.tRow.value.setText(String.format(str, new Object[]{Double.valueOf(convert.f71x)}));
            }
            this.tRow.sl1.addValue((float) convert.f71x);
            this.lastValue = (float) convert.f71x;
        }
    }

    public Map<String, String> getMQTTMap() {
        Sensor.LUXOMETER.convert(this.dataC.getValue());
        HashMap hashMap = new HashMap();
        hashMap.put("light", String.format("%.2f", new Object[]{Float.valueOf(this.lastValue)}));
        return hashMap;
    }
}
