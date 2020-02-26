package com.p004ti.ble.btsig;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.TableRow;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEManager;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.device_selector.TopLevel;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.protocol.HTTP;

/* renamed from: com.ti.ble.btsig.DeviceInformationServiceProfile */
public class DeviceInformationServiceProfile extends GenericBluetoothProfile {
    public static final String ACTION_FW_REV_UPDATED = "com.ti.ble.btsig.ACTION_FW_REV_UPDATED";
    public static final String EXTRA_FW_REV_STRING = "com.ti.ble.btsig.EXTRA_FW_REV_STRING";
    public static final String dISFirmwareREV_UUID = "00002a26-0000-1000-8000-00805f9b34fb";
    public static final String dISHardwareREV_UUID = "00002a27-0000-1000-8000-00805f9b34fb";
    public static final String dISManifacturerNAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb";
    public static final String dISModelNR_UUID = "00002a24-0000-1000-8000-00805f9b34fb";
    public static final String dISSerialNR_UUID = "00002a25-0000-1000-8000-00805f9b34fb";
    public static final String dISService_UUID = "0000180a-0000-1000-8000-00805f9b34fb";
    public static final String dISSoftwareREV_UUID = "00002a28-0000-1000-8000-00805f9b34fb";
    public static final String dISSystemID_UUID = "00002a23-0000-1000-8000-00805f9b34fb";
    BluetoothGattCharacteristic ManifacturerNAMEc;
    BluetoothLEDevice dev;
    BluetoothGattCharacteristic firmwareREVc;
    BluetoothGattCharacteristic hardwareREVc;
    BluetoothLEManager man;
    BluetoothGattCharacteristic modelNRc;
    BluetoothGattCharacteristic serialNRc;
    BluetoothGattCharacteristic softwareREVc;
    BluetoothGattCharacteristic systemIDc;
    DeviceInformationServiceTableRow tRow;

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void disableService() {
    }

    public DeviceInformationServiceProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new DeviceInformationServiceTableRow(context);
        this.man = BluetoothLEManager.getInstance(context);
        try {
            this.dev = this.man.deviceForBluetoothDev(bluetoothDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(dISSystemID_UUID)) {
                this.systemIDc = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(dISModelNR_UUID)) {
                this.modelNRc = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(dISSerialNR_UUID)) {
                this.serialNRc = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(dISFirmwareREV_UUID)) {
                this.firmwareREVc = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(dISHardwareREV_UUID)) {
                this.hardwareREVc = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(dISSoftwareREV_UUID)) {
                this.softwareREVc = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(dISManifacturerNAME_UUID)) {
                this.ManifacturerNAMEc = bluetoothGattCharacteristic;
            }
        }
        this.tRow.setIcon(getIconPrefix(), bluetoothGattService.getUuid().toString(), "deviceinfo");
        this.tRow.title.setText("Device Information Service");
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(dISService_UUID) == 0;
    }

    public void enableService() {
        this.dev.readCharacteristicSync(this.systemIDc);
        this.dev.readCharacteristicSync(this.modelNRc);
        this.dev.readCharacteristicSync(this.serialNRc);
        this.dev.readCharacteristicSync(this.firmwareREVc);
        this.dev.readCharacteristicSync(this.hardwareREVc);
        this.dev.readCharacteristicSync(this.softwareREVc);
        this.dev.readCharacteristicSync(this.ManifacturerNAMEc);
    }

    private String getValueSafe(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value = bluetoothGattCharacteristic.getValue();
        String str = HTTP.UTF_8;
        if (value == null) {
            value = "N/A".getBytes(Charset.forName(str));
        }
        try {
            return new String(value, str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:50:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void didReadValueForCharacteristic(android.bluetooth.BluetoothGattCharacteristic r10) {
        /*
            r9 = this;
            android.bluetooth.BluetoothGattCharacteristic r0 = r9.systemIDc
            if (r0 == 0) goto L_0x004a
            if (r10 == 0) goto L_0x004a
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L_0x004a
            java.lang.String r0 = "System ID: "
            byte[] r1 = r10.getValue()     // Catch:{ Exception -> 0x003d }
            int r2 = r1.length     // Catch:{ Exception -> 0x003d }
            r3 = 0
            r4 = r0
            r0 = 0
        L_0x0016:
            if (r0 >= r2) goto L_0x0043
            byte r5 = r1[r0]     // Catch:{ Exception -> 0x003b }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x003b }
            r6.<init>()     // Catch:{ Exception -> 0x003b }
            r6.append(r4)     // Catch:{ Exception -> 0x003b }
            java.lang.String r7 = "%02x:"
            r8 = 1
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x003b }
            java.lang.Byte r5 = java.lang.Byte.valueOf(r5)     // Catch:{ Exception -> 0x003b }
            r8[r3] = r5     // Catch:{ Exception -> 0x003b }
            java.lang.String r5 = java.lang.String.format(r7, r8)     // Catch:{ Exception -> 0x003b }
            r6.append(r5)     // Catch:{ Exception -> 0x003b }
            java.lang.String r4 = r6.toString()     // Catch:{ Exception -> 0x003b }
            int r0 = r0 + 1
            goto L_0x0016
        L_0x003b:
            r0 = move-exception
            goto L_0x0040
        L_0x003d:
            r1 = move-exception
            r4 = r0
            r0 = r1
        L_0x0040:
            r0.printStackTrace()
        L_0x0043:
            com.ti.ble.btsig.DeviceInformationServiceTableRow r0 = r9.tRow
            android.widget.TextView r0 = r0.SystemIDLabel
            r0.setText(r4)
        L_0x004a:
            android.bluetooth.BluetoothGattCharacteristic r0 = r9.modelNRc
            if (r0 == 0) goto L_0x0070
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L_0x0070
            com.ti.ble.btsig.DeviceInformationServiceTableRow r0 = r9.tRow
            android.widget.TextView r0 = r0.ModelNRLabel
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Model NR: "
            r1.append(r2)
            java.lang.String r2 = r9.getValueSafe(r10)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.setText(r1)
        L_0x0070:
            android.bluetooth.BluetoothGattCharacteristic r0 = r9.serialNRc
            if (r0 == 0) goto L_0x0096
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L_0x0096
            com.ti.ble.btsig.DeviceInformationServiceTableRow r0 = r9.tRow
            android.widget.TextView r0 = r0.SerialNRLabel
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Serial NR: "
            r1.append(r2)
            java.lang.String r2 = r9.getValueSafe(r10)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.setText(r1)
        L_0x0096:
            android.bluetooth.BluetoothGattCharacteristic r0 = r9.firmwareREVc
            if (r0 == 0) goto L_0x00cd
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L_0x00cd
            java.lang.String r0 = r9.getValueSafe(r10)
            com.ti.ble.btsig.DeviceInformationServiceTableRow r1 = r9.tRow
            android.widget.TextView r1 = r1.FirmwareREVLabel
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Firmware Revision: "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            r1.setText(r2)
            android.content.Intent r1 = new android.content.Intent
            java.lang.String r2 = "com.ti.ble.btsig.ACTION_FW_REV_UPDATED"
            r1.<init>(r2)
            java.lang.String r2 = "com.ti.ble.btsig.EXTRA_FW_REV_STRING"
            r1.putExtra(r2, r0)
            android.content.Context r0 = r9.context
            r0.sendBroadcast(r1)
        L_0x00cd:
            android.bluetooth.BluetoothGattCharacteristic r0 = r9.hardwareREVc
            if (r0 == 0) goto L_0x00f3
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L_0x00f3
            com.ti.ble.btsig.DeviceInformationServiceTableRow r0 = r9.tRow
            android.widget.TextView r0 = r0.HardwareREVLabel
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Hardware Revision: "
            r1.append(r2)
            java.lang.String r2 = r9.getValueSafe(r10)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.setText(r1)
        L_0x00f3:
            android.bluetooth.BluetoothGattCharacteristic r0 = r9.softwareREVc
            if (r0 == 0) goto L_0x0119
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L_0x0119
            com.ti.ble.btsig.DeviceInformationServiceTableRow r0 = r9.tRow
            android.widget.TextView r0 = r0.SoftwareREVLabel
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Software Revision: "
            r1.append(r2)
            java.lang.String r2 = r9.getValueSafe(r10)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.setText(r1)
        L_0x0119:
            android.bluetooth.BluetoothGattCharacteristic r0 = r9.ManifacturerNAMEc
            if (r0 == 0) goto L_0x013f
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L_0x013f
            com.ti.ble.btsig.DeviceInformationServiceTableRow r0 = r9.tRow
            android.widget.TextView r0 = r0.ManifacturerNAMELabel
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Manufacturer Name: "
            r1.append(r2)
            java.lang.String r10 = r9.getValueSafe(r10)
            r1.append(r10)
            java.lang.String r10 = r1.toString()
            r0.setText(r10)
        L_0x013f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.ble.btsig.DeviceInformationServiceProfile.didReadValueForCharacteristic(android.bluetooth.BluetoothGattCharacteristic):void");
    }

    public String getIconPrefix() {
        String str = "cc2650_launchpad_";
        String str2 = "sensortag2_";
        if (this.mBTDevice.getName() == null || this.mBTDevice.getName().equals("CC2650 SensorTag") || this.mBTDevice.getName().equals("CC1350 SensorTag")) {
            return str2;
        }
        if (this.mBTDevice.getName().equals("Multi-Sensor") || this.mBTDevice.getName().equals("LPSTK:)")) {
            return "lpstk_";
        }
        if (this.mBTDevice.getName().equals(TopLevel.Sensor_Tag)) {
            return "sensortag_";
        }
        if (this.mBTDevice.getName().equals("CC2650 RC") || this.mBTDevice.getName().equals("HID AdvRemote")) {
            return "cc2650_rc_";
        }
        if (this.mBTDevice.getName().equals("CC2650 LaunchPad") || this.mBTDevice.getName().equals("CC1350 LaunchPad") || this.mBTDevice.getName().equals("Throughput Periph")) {
            return str;
        }
        if (this.mBTDevice.getName().equalsIgnoreCase("ProjectZero") || this.mBTDevice.getName().equalsIgnoreCase("Project Zero")) {
            return "prz_";
        }
        return this.mBTDevice.getName().contains("DMM") ? "dmm_" : "";
    }

    public TableRow getTableRow() {
        return this.tRow;
    }
}
