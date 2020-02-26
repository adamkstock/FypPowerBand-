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
import org.apache.http.HttpStatus;

/* renamed from: com.ti.ble.sensortag.SensorTagAmbientTemperatureProfile */
public class SensorTagAmbientTemperatureProfile extends GenericBluetoothProfile {
    private float lastValue = 0.0f;

    public SensorTagAmbientTemperatureProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context, 1000, true);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_IRT_DATA.toString())) {
                this.dataC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_IRT_CONF.toString())) {
                this.configC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_IRT_PERI.toString())) {
                this.periodC = bluetoothGattCharacteristic;
            }
        }
        this.tRow.sl1.autoScale = true;
        this.tRow.sl1.autoScaleBounceBack = true;
        this.tRow.sl1.setColor(0, 0, 0, 0, 1);
        this.tRow.sl1.setColor(0, 0, 0, 0, 2);
        this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "temperature");
        this.tRow.title.setText("Ambient Temperature Data");
        this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
        this.tRow.value.setText("0.0'C");
        this.tRow.periodMinVal = HttpStatus.SC_OK;
    }

    public void configureService() {
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, 1);
        String str = " Error: ";
        String str2 = "SensorTagAmbientTemperatureProfile";
        if (!(writeCharacteristicAsync == 0 || this.configC == null)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Sensor config failed: ");
            sb.append(this.configC.getUuid().toString());
            sb.append(str);
            sb.append(writeCharacteristicAsync);
            Log.d(str2, sb.toString());
        }
        int characteristicNotificationSync = this.dev.setCharacteristicNotificationSync(this.dataC, true);
        if (!(characteristicNotificationSync == 0 || this.dataC == null)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Sensor notification enable failed: ");
            sb2.append(this.configC.getUuid().toString());
            sb2.append(str);
            sb2.append(characteristicNotificationSync);
            Log.d(str2, sb2.toString());
        }
        this.tRow.periodMinVal = HttpStatus.SC_OK;
        this.isConfigured = true;
    }

    public void deConfigureService() {
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, 0);
        String str = " Error: ";
        String str2 = "SensorTagAmbientTemperatureProfile";
        if (!(writeCharacteristicAsync == 0 || this.configC == null)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Sensor config failed: ");
            sb.append(this.configC.getUuid().toString());
            sb.append(str);
            sb.append(writeCharacteristicAsync);
            Log.d(str2, sb.toString());
        }
        int characteristicNotificationSync = this.dev.setCharacteristicNotificationSync(this.dataC, false);
        if (!(characteristicNotificationSync == 0 || this.dataC == null)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Sensor notification enable failed: ");
            sb2.append(this.configC.getUuid().toString());
            sb2.append(str);
            sb2.append(characteristicNotificationSync);
            Log.d(str2, sb2.toString());
        }
        this.isConfigured = false;
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value = bluetoothGattCharacteristic.getValue();
        String str = "%.1f°C";
        if ((this.mBTDevice.getName().contains("Multi-Sensor") || this.mBTDevice.getName().contains("LPSTK:)")) && bluetoothGattCharacteristic.equals(this.dataC)) {
            int i = ByteBuffer.wrap(value).getInt();
            byte[] bArr = new byte[4];
            ByteBuffer.wrap(bArr).putInt(((i >> 24) & 255) | ((i & 255) << 24) | ((65280 & i) << 8) | ((16711680 & i) >> 8));
            float f = ByteBuffer.wrap(bArr).getFloat();
            this.tRow.value.setText(String.format(str, new Object[]{Float.valueOf(f)}));
            this.tRow.sl1.addValue(f);
            this.lastValue = f;
        } else if (bluetoothGattCharacteristic.equals(this.dataC)) {
            Point3D convert = Sensor.TEMPHUM.convert(value);
            if (!this.tRow.config) {
                if (isEnabledByPrefs("imperial")) {
                    this.tRow.value.setText(String.format("%.1f°F", new Object[]{Double.valueOf((convert.f71x * 1.8d) + 32.0d)}));
                } else {
                    this.tRow.value.setText(String.format(str, new Object[]{Double.valueOf(convert.f71x)}));
                }
            }
            this.tRow.sl1.addValue((float) convert.f71x);
            this.lastValue = (float) convert.f71x;
        }
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_IRT_SERV.toString()) == 0;
    }

    public Map<String, String> getMQTTMap() {
        Sensor.TEMPHUM.convert(this.dataC.getValue());
        HashMap hashMap = new HashMap();
        hashMap.put("ambient_temp", String.format("%.2f", new Object[]{Float.valueOf(this.lastValue)}));
        return hashMap;
    }
}
