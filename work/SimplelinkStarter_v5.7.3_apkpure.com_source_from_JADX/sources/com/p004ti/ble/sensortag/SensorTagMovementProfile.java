package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Html;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableRow;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.ble.common.GenericServiceConfigurationDialogFragment;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.p004ti.util.Point3D;
import com.p004ti.util.TrippleSparkLineView;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ti.ble.sensortag.SensorTagMovementProfile */
public class SensorTagMovementProfile extends GenericBluetoothProfile {
    private final BroadcastReceiver guiReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SensorTagMovementProfile.this.myRow.uuidLabel.getText().toString().compareTo(intent.getStringExtra(GenericCharacteristicTableRow.EXTRA_SERVICE_UUID)) == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("Matched UUID :");
                sb.append(SensorTagMovementProfile.this.tRow.uuidLabel.getText());
                Log.d("GenericBluetoothProfile", sb.toString());
                String stringExtra = intent.getStringExtra(GenericServiceConfigurationDialogFragment.VALUE_GENERIC_SERVICE_CONFIGURATION_PERIOD);
                boolean booleanExtra = intent.getBooleanExtra(GenericServiceConfigurationDialogFragment.VALUE_GENERIC_SERVICE_CONFIGURATION_SENSOR_STATE, true);
                if (action.compareTo(GenericServiceConfigurationDialogFragment.ACTION_GENERIC_SERVICE_CONFIGURATION_WAS_UPDATED) == 0) {
                    SensorTagMovementProfile.this.onOffWasUpdated(booleanExtra);
                    SensorTagMovementProfile.this.periodWasUpdated(Integer.parseInt(stringExtra.replaceAll("[^0-9.]", "")));
                }
            }
        }
    };
    public SensorTagMovementTableRow myRow;

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public SensorTagMovementProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.myRow = new SensorTagMovementTableRow(context);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_MOV_DATA.toString())) {
                this.dataC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_MOV_CONF.toString())) {
                this.configC = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_MOV_PERI.toString())) {
                this.periodC = bluetoothGattCharacteristic;
            }
        }
        this.myRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "motion");
        this.myRow.title.setText("Motion Data");
        this.myRow.uuidLabel.setText(this.dataC.getUuid().toString());
        this.myRow.accValue.setText("X:0.00G, Y:0.00G, Z:0.00G");
        this.myRow.gyroValue.setText("X:0.00'/s, Y:0.00'/s, Z:0.00'/s");
        this.myRow.magValue.setText("X:0.00mT, Y:0.00mT, Z:0.00mT");
        TrippleSparkLineView trippleSparkLineView = this.myRow.sl1;
        TrippleSparkLineView trippleSparkLineView2 = this.myRow.sl2;
        this.myRow.sl3.autoScale = true;
        trippleSparkLineView2.autoScale = true;
        trippleSparkLineView.autoScale = true;
        TrippleSparkLineView trippleSparkLineView3 = this.myRow.sl1;
        TrippleSparkLineView trippleSparkLineView4 = this.myRow.sl2;
        this.myRow.sl3.autoScaleBounceBack = true;
        trippleSparkLineView4.autoScaleBounceBack = true;
        trippleSparkLineView3.autoScaleBounceBack = true;
        this.myRow.WOS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                byte[] bArr = {Byte.MAX_VALUE, 0};
                if (z) {
                    bArr[0] = -1;
                }
                int writeCharacteristicAsync = SensorTagMovementProfile.this.dev.writeCharacteristicAsync(SensorTagMovementProfile.this.configC, bArr);
                if (writeCharacteristicAsync != 0 && SensorTagMovementProfile.this.configC != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Sensor config failed: ");
                    sb.append(SensorTagMovementProfile.this.configC.getUuid().toString());
                    sb.append(" Error: ");
                    sb.append(writeCharacteristicAsync);
                    Log.d("SensorTagMovementProfile", sb.toString());
                }
            }
        });
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_MOV_SERV.toString()) == 0;
    }

    public void enableService() {
        byte[] bArr = {Byte.MAX_VALUE, 0};
        if (this.myRow.WOS.isChecked()) {
            bArr[0] = -1;
        }
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, bArr);
        String str = " Error: ";
        String str2 = "SensorTagMovementProfile";
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
        periodWasUpdated(this.myRow.period);
        this.isEnabled = true;
        this.myRow.servOn = true;
    }

    public void disableService() {
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, new byte[]{0, 0});
        String str = " Error: ";
        String str2 = "SensorTagMovementProfile";
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
            sb2.append("Sensor notification disable failed: ");
            sb2.append(this.configC.getUuid().toString());
            sb2.append(str);
            sb2.append(characteristicNotificationSync);
            Log.d(str2, sb2.toString());
        }
        this.isEnabled = false;
        this.myRow.servOn = false;
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (bluetoothGattCharacteristic.equals(this.dataC)) {
            Point3D convert = Sensor.MOVEMENT_ACC.convert(value);
            if (!this.myRow.config) {
                this.myRow.accValue.setText(Html.fromHtml(String.format("<font color=#FF0000>X:%.2fG</font>, <font color=#00967D>Y:%.2fG</font>, <font color=#00000>Z:%.2fG</font>", new Object[]{Double.valueOf(convert.f71x), Double.valueOf(convert.f72y), Double.valueOf(convert.f73z)})));
            }
            this.myRow.sl1.addValue((float) convert.f71x, 0);
            this.myRow.sl1.addValue((float) convert.f72y, 1);
            this.myRow.sl1.addValue((float) convert.f73z, 2);
            Point3D convert2 = Sensor.MOVEMENT_GYRO.convert(value);
            this.myRow.gyroValue.setText(Html.fromHtml(String.format("<font color=#FF0000>X:%.2f°/s</font>, <font color=#00967D>Y:%.2f°/s</font>, <font color=#00000>Z:%.2f°/s</font>", new Object[]{Double.valueOf(convert2.f71x), Double.valueOf(convert2.f72y), Double.valueOf(convert2.f73z)})));
            this.myRow.sl2.addValue((float) convert2.f71x, 0);
            this.myRow.sl2.addValue((float) convert2.f72y, 1);
            this.myRow.sl2.addValue((float) convert2.f73z, 2);
            Point3D convert3 = Sensor.MOVEMENT_MAG.convert(value);
            this.myRow.magValue.setText(Html.fromHtml(String.format("<font color=#FF0000>X:%.2fuT</font>, <font color=#00967D>Y:%.2fuT</font>, <font color=#00000>Z:%.2fuT</font>", new Object[]{Double.valueOf(convert3.f71x), Double.valueOf(convert3.f72y), Double.valueOf(convert3.f73z)})));
            this.myRow.sl3.addValue((float) convert3.f71x, 0);
            this.myRow.sl3.addValue((float) convert3.f72y, 1);
            this.myRow.sl3.addValue((float) convert3.f73z, 1);
        }
    }

    public void onOffWasUpdated(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Config characteristic set to :");
        sb.append(z);
        Log.d("GenericBluetoothProfile", sb.toString());
        if (z) {
            configureService();
            enableService();
            this.myRow.servOn = true;
            return;
        }
        deConfigureService();
        disableService();
        this.myRow.servOn = false;
    }

    public void periodWasUpdated(int i) {
        if (i > 2450) {
            i = 2450;
        }
        if (i < 100) {
            i = 100;
        }
        byte b = (byte) (i / 10);
        StringBuilder sb = new StringBuilder();
        sb.append("Period characteristic set to :");
        sb.append(i);
        String str = "GenericBluetoothProfile";
        Log.d(str, sb.toString());
        this.myRow.period = i;
        String str2 = "Sensor period failed: ";
        if (this.dev.writeCharacteristicSync(this.periodC, b) != 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(this.periodC.getUuid().toString());
            Log.d(str, sb2.toString());
        }
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.periodC, b);
        if (writeCharacteristicAsync != 0 && this.periodC != null) {
            printError(str2, this.periodC, writeCharacteristicAsync);
        }
    }

    public void grayOutCell(boolean z) {
        if (z) {
            this.tRow.setAlpha(0.4f);
        } else {
            this.tRow.setAlpha(1.0f);
        }
    }

    private static IntentFilter makeFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GenericCharacteristicTableRow.ACTION_PERIOD_UPDATE);
        intentFilter.addAction(GenericCharacteristicTableRow.ACTION_ONOFF_UPDATE);
        intentFilter.addAction(GenericCharacteristicTableRow.ACTION_CALIBRATE);
        intentFilter.addAction(GenericServiceConfigurationDialogFragment.ACTION_GENERIC_SERVICE_CONFIGURATION_WAS_UPDATED);
        return intentFilter;
    }

    public void onResume() {
        if (!this.isRegistered) {
            this.context.registerReceiver(this.guiReceiver, makeFilter());
            this.isRegistered = true;
        }
    }

    public void onPause() {
        if (this.isRegistered) {
            this.context.unregisterReceiver(this.guiReceiver);
            this.isRegistered = false;
        }
    }

    public Map<String, String> getMQTTMap() {
        Point3D convert = Sensor.MOVEMENT_ACC.convert(this.dataC.getValue());
        HashMap hashMap = new HashMap();
        String str = "%.2f";
        String str2 = "acc_x";
        hashMap.put(str2, String.format(str, new Object[]{Double.valueOf(convert.f71x)}));
        String str3 = "acc_y";
        hashMap.put(str3, String.format(str, new Object[]{Double.valueOf(convert.f72y)}));
        hashMap.put("acc_z", String.format(str, new Object[]{Double.valueOf(convert.f73z)}));
        Point3D convert2 = Sensor.MOVEMENT_GYRO.convert(this.dataC.getValue());
        String str4 = "gyro_x";
        hashMap.put(str4, String.format(str, new Object[]{Double.valueOf(convert2.f71x)}));
        String str5 = "gyro_y";
        hashMap.put(str5, String.format(str, new Object[]{Double.valueOf(convert2.f72y)}));
        hashMap.put("gyro_z", String.format(str, new Object[]{Double.valueOf(convert2.f73z)}));
        Point3D convert3 = Sensor.MOVEMENT_MAG.convert(this.dataC.getValue());
        String str6 = "compass_x";
        hashMap.put(str6, String.format(str, new Object[]{Double.valueOf(convert3.f71x)}));
        String str7 = "compass_y";
        hashMap.put(str7, String.format(str, new Object[]{Double.valueOf(convert3.f72y)}));
        hashMap.put("compass_z", String.format(str, new Object[]{Double.valueOf(convert3.f73z)}));
        return hashMap;
    }

    public TableRow getTableRow() {
        return this.myRow;
    }
}
