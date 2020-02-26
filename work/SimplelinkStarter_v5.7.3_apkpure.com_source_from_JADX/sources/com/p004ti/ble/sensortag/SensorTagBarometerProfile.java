package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.Point3D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ti.ble.sensortag.SensorTagBarometerProfile */
public class SensorTagBarometerProfile extends GenericBluetoothProfile {
    private static final double PA_PER_METER = 12.0d;
    private BluetoothGattCharacteristic calibC;
    private boolean isCalibrated;
    private boolean isHeightCalibrated;

    public SensorTagBarometerProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new SensorTagBarometerTableRow(context);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_BAR_DATA.toString())) {
                this.dataC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_BAR_CONF.toString())) {
                this.configC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_BAR_PERI.toString())) {
                this.periodC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_BAR_CALI.toString())) {
                this.calibC = bluetoothGattCharacteristic;
            }
        }
        if (this.mBTDevice.getName().equals("CC2650 SensorTag") || this.mBTDevice.getName().equals("CC1350 SensorTag")) {
            this.isCalibrated = true;
        } else {
            this.isCalibrated = false;
        }
        this.isHeightCalibrated = false;
        this.tRow.sl1.autoScale = true;
        this.tRow.sl1.autoScaleBounceBack = true;
        this.tRow.sl1.setColor(0, 0, 0, 0, 2);
        this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "barometer");
        this.tRow.title.setText("Barometer Data");
        this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
        this.tRow.value.setText("0.0mBar, 0.0m");
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_BAR_SERV.toString()) == 0;
    }

    public void enableService() {
        if (!this.isCalibrated) {
            this.dev.writeCharacteristicSync(this.configC, 2);
            this.dev.readCharacteristicSync(this.calibC);
        } else {
            int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, 1);
            String str = " Error: ";
            String str2 = "SensorTagBarometerProfile";
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
        }
        this.isEnabled = true;
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic2 = this.calibC;
        if (bluetoothGattCharacteristic2 != null && bluetoothGattCharacteristic2.equals(bluetoothGattCharacteristic)) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (value.length == 16) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < 8; i += 2) {
                    arrayList.add(Integer.valueOf((Integer.valueOf(value[i + 1] & 255).intValue() << 8) + Integer.valueOf(value[i] & 255).intValue()));
                }
                for (int i2 = 8; i2 < 16; i2 += 2) {
                    arrayList.add(Integer.valueOf((Integer.valueOf(value[i2 + 1]).intValue() << 8) + Integer.valueOf(value[i2] & 255).intValue()));
                }
                String str = "SensorTagBarometerProfile";
                Log.d(str, "Barometer calibrated !!!!!");
                BarometerCalibrationCoefficients.INSTANCE.barometerCalibrationCoefficients = arrayList;
                this.isCalibrated = true;
                int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, 1);
                String str2 = " Error: ";
                if (!(writeCharacteristicAsync == 0 || this.configC == null)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Sensor config failed: ");
                    sb.append(this.configC.getUuid().toString());
                    sb.append(str2);
                    sb.append(writeCharacteristicAsync);
                    Log.d(str, sb.toString());
                }
                int characteristicNotificationSync = this.dev.setCharacteristicNotificationSync(this.dataC, true);
                if (!(characteristicNotificationSync == 0 || this.dataC == null)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Sensor notification enable failed: ");
                    sb2.append(this.configC.getUuid().toString());
                    sb2.append(str2);
                    sb2.append(characteristicNotificationSync);
                    Log.d(str, sb2.toString());
                }
            }
        }
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (bluetoothGattCharacteristic.equals(this.dataC)) {
            Point3D convert = Sensor.BAROMETER.convert(value);
            if (!this.isHeightCalibrated) {
                BarometerCalibrationCoefficients.INSTANCE.heightCalibration = convert.f71x;
                this.isHeightCalibrated = true;
            }
            double round = ((double) Math.round((-((convert.f71x - BarometerCalibrationCoefficients.INSTANCE.heightCalibration) / PA_PER_METER)) * 10.0d)) / 10.0d;
            if (!this.tRow.config) {
                this.tRow.value.setText(String.format("%.1f mBar %.1f meter", new Object[]{Double.valueOf(convert.f71x / 100.0d), Double.valueOf(round)}));
            }
            this.tRow.sl1.addValue(((float) convert.f71x) / 100.0f);
            this.tRow.sl1.addValue((float) round, 1);
        }
    }

    /* access modifiers changed from: protected */
    public void calibrationButtonTouched() {
        this.isHeightCalibrated = false;
    }

    public Map<String, String> getMQTTMap() {
        Point3D convert = Sensor.BAROMETER.convert(this.dataC.getValue());
        HashMap hashMap = new HashMap();
        hashMap.put("air_pressure", String.format("%.2f", new Object[]{Double.valueOf(convert.f71x / 100.0d)}));
        return hashMap;
    }
}
