package com.p004ti.ble.common;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TableRow;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEManager;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.util.GenericCharacteristicTableRow;
import java.util.Map;

/* renamed from: com.ti.ble.common.GenericBluetoothProfile */
public class GenericBluetoothProfile {
    protected static final int GATT_TIMEOUT = 250;
    /* access modifiers changed from: protected */
    public BluetoothGattCharacteristic configC;
    /* access modifiers changed from: protected */
    public Context context;
    /* access modifiers changed from: protected */
    public BluetoothGattCharacteristic dataC;
    /* access modifiers changed from: protected */
    public BluetoothLEDevice dev;
    private final BroadcastReceiver guiReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GenericBluetoothProfile.this.tRow.uuidLabel.getText().toString().compareTo(intent.getStringExtra(GenericCharacteristicTableRow.EXTRA_SERVICE_UUID)) == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("Matched UUID :");
                sb.append(GenericBluetoothProfile.this.tRow.uuidLabel.getText());
                Log.d("GenericBluetoothProfile", sb.toString());
                String stringExtra = intent.getStringExtra(GenericServiceConfigurationDialogFragment.VALUE_GENERIC_SERVICE_CONFIGURATION_PERIOD);
                boolean booleanExtra = intent.getBooleanExtra(GenericServiceConfigurationDialogFragment.VALUE_GENERIC_SERVICE_CONFIGURATION_SENSOR_STATE, true);
                if (action.compareTo(GenericServiceConfigurationDialogFragment.ACTION_GENERIC_SERVICE_CONFIGURATION_WAS_UPDATED) == 0) {
                    GenericBluetoothProfile.this.onOffWasUpdated(booleanExtra);
                    GenericBluetoothProfile.this.periodWasUpdated(Integer.parseInt(stringExtra.replaceAll("[^0-9.]", "")));
                }
            }
        }
    };
    public boolean isConfigured;
    public boolean isEnabled;
    protected boolean isRegistered;
    /* access modifiers changed from: protected */
    public BluetoothDevice mBTDevice;
    protected BluetoothGattService mBTService;
    protected BluetoothLEManager man;
    protected BluetoothGattCharacteristic periodC;
    protected BroadcastReceiver serviceSettings;
    /* access modifiers changed from: protected */
    public GenericCharacteristicTableRow tRow;

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void calibrationButtonTouched() {
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void didUpdateFirmwareRevision(String str) {
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public Map<String, String> getMQTTMap() {
        return null;
    }

    public void rssiUpdated(int i) {
    }

    public GenericBluetoothProfile(Context context2, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        this.mBTDevice = bluetoothDevice;
        this.mBTService = bluetoothGattService;
        this.tRow = new GenericCharacteristicTableRow(context2, 1000, true);
        this.dataC = null;
        this.periodC = null;
        this.configC = null;
        this.context = context2;
        this.isRegistered = false;
        this.man = BluetoothLEManager.getInstance(context2);
        try {
            this.dev = this.man.deviceForBluetoothDev(bluetoothDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public boolean isDataC(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic2 = this.dataC;
        if (bluetoothGattCharacteristic2 != null && bluetoothGattCharacteristic.equals(bluetoothGattCharacteristic2)) {
            return true;
        }
        return false;
    }

    public void configureService() {
        int characteristicNotificationSync = this.dev.setCharacteristicNotificationSync(this.dataC, true);
        if (characteristicNotificationSync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.dataC;
            if (bluetoothGattCharacteristic != null) {
                printError("Sensor notification enable failed: ", bluetoothGattCharacteristic, characteristicNotificationSync);
            }
        }
        this.isConfigured = true;
    }

    public void deConfigureService() {
        int characteristicNotificationSync = this.dev.setCharacteristicNotificationSync(this.dataC, false);
        if (characteristicNotificationSync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.dataC;
            if (bluetoothGattCharacteristic != null) {
                printError("Sensor notification disable failed: ", bluetoothGattCharacteristic, characteristicNotificationSync);
            }
        }
        this.isConfigured = false;
    }

    public void enableService() {
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, 1);
        if (writeCharacteristicAsync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.configC;
            if (bluetoothGattCharacteristic != null) {
                printError("Sensor enable failed: ", bluetoothGattCharacteristic, writeCharacteristicAsync);
            }
        }
        this.dev.readCharacteristicAsync(this.periodC);
        this.isEnabled = true;
    }

    public void disableService() {
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.configC, 0);
        if (writeCharacteristicAsync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.configC;
            if (bluetoothGattCharacteristic != null) {
                printError("Sensor disable failed: ", bluetoothGattCharacteristic, writeCharacteristicAsync);
            }
        }
        this.isConfigured = false;
    }

    public TableRow getTableRow() {
        return this.tRow;
    }

    public String getIconPrefix() {
        if (this.mBTDevice.getName() != null) {
            if (this.mBTDevice.getName().equals("CC2650 SensorTag") || this.mBTDevice.getName().equals("CC1350 SensorTag")) {
                return "sensortag2_";
            }
            if (this.mBTDevice.getName().equals(TopLevel.Sensor_Tag)) {
                return "sensortag_";
            }
            if (this.mBTDevice.getName().equals("Multi-Sensor") || this.mBTDevice.getName().equals("LPSTK:)")) {
                return "lpstk_";
            }
            if (this.mBTDevice.getName().equals("CC2650 RC")) {
                return "cc2650_rc_";
            }
            if (this.mBTDevice.getName().equals("CC2650 LaunchPad") || this.mBTDevice.getName().equals("CC1350 LaunchPad")) {
                return "cc2650_launchpad_";
            }
            if (this.mBTDevice.getName().equalsIgnoreCase("ProjectZero") || this.mBTDevice.getName().equalsIgnoreCase("Project Zero")) {
                return "prz_";
            }
            if (this.mBTDevice.getName().contains("DMM")) {
                return "dmm_";
            }
        }
        return "";
    }

    public boolean isEnabledByPrefs(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("pref_");
        sb.append(str);
        return PreferenceManager.getDefaultSharedPreferences(this.context).getBoolean(sb.toString(), Boolean.valueOf(true).booleanValue());
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
        this.tRow.period = i;
        String str2 = "Sensor period failed: ";
        if (this.dev.writeCharacteristicSync(this.periodC, b) != 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(this.periodC.getUuid().toString());
            Log.d(str, sb2.toString());
        }
        int writeCharacteristicAsync = this.dev.writeCharacteristicAsync(this.periodC, b);
        if (writeCharacteristicAsync != 0) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.periodC;
            if (bluetoothGattCharacteristic != null) {
                printError(str2, bluetoothGattCharacteristic, writeCharacteristicAsync);
            }
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
            this.tRow.grayedOut(false);
            this.tRow.servOn = true;
            return;
        }
        deConfigureService();
        disableService();
        this.tRow.grayedOut(true);
        this.tRow.servOn = false;
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

    public void printError(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        String str2 = "GenericBluetoothProfile";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" Error: ");
            sb.append(i);
            Log.d(str2, sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
