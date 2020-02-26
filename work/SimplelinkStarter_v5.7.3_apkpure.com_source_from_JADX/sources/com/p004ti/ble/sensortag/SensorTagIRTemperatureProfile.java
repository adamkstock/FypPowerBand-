package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.p004ti.util.Point3D;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;

/* renamed from: com.ti.ble.sensortag.SensorTagIRTemperatureProfile */
public class SensorTagIRTemperatureProfile extends GenericBluetoothProfile {
    boolean sensorPresent = true;

    public SensorTagIRTemperatureProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
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
        this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "irtemperature");
        this.tRow.title.setText("IR Temperature Data");
        this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
        this.tRow.value.setText("0.0'C");
        this.tRow.periodMinVal = HttpStatus.SC_OK;
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (this.sensorPresent && bluetoothGattCharacteristic.equals(this.dataC)) {
            String str = "%.1f°F";
            String str2 = "%.1f°C";
            String str3 = "imperial";
            if (this.mBTDevice.getName().equals("CC2650 SensorTag") || this.mBTDevice.getName().equals("CC1350 SensorTag")) {
                Point3D convert = Sensor.IR_TEMPERATURE.convert(value);
                if (isEnabledByPrefs(str3)) {
                    this.tRow.value.setText(String.format(str, new Object[]{Double.valueOf((convert.f73z * 1.8d) + 32.0d)}));
                } else {
                    this.tRow.value.setText(String.format(str2, new Object[]{Double.valueOf(convert.f73z)}));
                }
                this.tRow.sl1.addValue((float) convert.f73z);
                return;
            }
            Point3D convert2 = Sensor.IR_TEMPERATURE.convert(value);
            if (!this.tRow.config) {
                if (isEnabledByPrefs(str3)) {
                    this.tRow.value.setText(String.format(str, new Object[]{Double.valueOf((convert2.f72y * 1.8d) + 32.0d)}));
                } else {
                    this.tRow.value.setText(String.format(str2, new Object[]{Double.valueOf(convert2.f72y)}));
                }
            }
            this.tRow.sl1.addValue((float) convert2.f72y);
        }
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super.didReadValueForCharacteristic(bluetoothGattCharacteristic);
        if (bluetoothGattCharacteristic.equals(this.configC)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Read config register : ");
            sb.append(bluetoothGattCharacteristic.getValue()[0]);
            String str = "SensorTagIRTemperatureProfile";
            Log.d(str, sb.toString());
            if (bluetoothGattCharacteristic.getValue()[0] == -1) {
                Log.d(str, "Sensor not present, mark it and show to user !");
                this.sensorPresent = false;
                this.tRow.value.setText("Sensor not present on HW !");
            }
        }
    }

    public void enableService() {
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, 1);
        if (!(writeCharacteristicAsync == 0 || this.configC == null)) {
            printError("Sensor enable failed: ", this.configC, writeCharacteristicAsync);
        }
        this.dev.readCharacteristicAsync(this.configC);
        this.dev.readCharacteristicAsync(this.periodC);
        this.isEnabled = true;
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_IRT_SERV.toString()) == 0;
    }

    public Map<String, String> getMQTTMap() {
        Point3D convert = Sensor.IR_TEMPERATURE.convert(this.dataC.getValue());
        HashMap hashMap = new HashMap();
        String str = "%.2f";
        String str2 = "object_temp";
        if (this.mBTDevice.getName().equals("CC2650 SensorTag") || this.mBTDevice.getName().equals("CC1350 SensorTag")) {
            hashMap.put(str2, String.format(str, new Object[]{Double.valueOf(convert.f73z)}));
        } else {
            hashMap.put(str2, String.format(str, new Object[]{Double.valueOf(convert.f72y)}));
        }
        return hashMap;
    }
}
