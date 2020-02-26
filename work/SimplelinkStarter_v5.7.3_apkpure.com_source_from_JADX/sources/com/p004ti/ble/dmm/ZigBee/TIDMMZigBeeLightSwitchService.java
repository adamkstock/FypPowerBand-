package com.p004ti.ble.dmm.ZigBee;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.bleUtility;

/* renamed from: com.ti.ble.dmm.ZigBee.TIDMMZigBeeLightSwitchService */
public class TIDMMZigBeeLightSwitchService extends GenericBluetoothProfile {
    public static final String TIDMM_ZIGBEE_LIGHT_BATTERY_LEVEL_CHAR = "f00011a4-0451-4000-b000-000000000000";
    public static final String TIDMM_ZIGBEE_LIGHT_SWITCH_ON_OFF_CHAR = "f00011a1-0451-4000-b000-000000000000";
    public static final String TIDMM_ZIGBEE_LIGHT_SWITCH_SERVICE = "f00011a0-0451-4000-b000-000000000000";
    public static final String TIDMM_ZIGBEE_LIGHT_TARGET_ADDRESS_CHAR = "f00011a3-0451-4000-b000-000000000000";
    public static final String TIDMM_ZIGBEE_LIGHT_TARGET_ADDRESS_TYPE_CHAR = "f00011a2-0451-4000-b000-000000000000";
    public static final String TIDMM_ZIGBEE_LIGHT_TARGET_ENDPOINT_CHAR = "f00011a5-0451-4000-b000-000000000000";
    private BluetoothGattCharacteristic batteryLevel;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic switchOnOffChar;
    private BluetoothGattCharacteristic targetAddress;
    private BluetoothGattCharacteristic targetAddressType;
    private BluetoothGattCharacteristic targetEndPoint;
    private TIDMMZigBeeLightSwitchTableRow zRow;

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void disableService() {
    }

    public TIDMMZigBeeLightSwitchService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().compareToIgnoreCase(TIDMM_ZIGBEE_LIGHT_SWITCH_ON_OFF_CHAR) == 0) {
                this.switchOnOffChar = bluetoothGattCharacteristic;
            } else if (bluetoothGattCharacteristic.getUuid().toString().compareToIgnoreCase(TIDMM_ZIGBEE_LIGHT_TARGET_ADDRESS_TYPE_CHAR) == 0) {
                this.targetAddressType = bluetoothGattCharacteristic;
            } else if (bluetoothGattCharacteristic.getUuid().toString().compareToIgnoreCase(TIDMM_ZIGBEE_LIGHT_TARGET_ADDRESS_CHAR) == 0) {
                this.targetAddress = bluetoothGattCharacteristic;
            } else if (bluetoothGattCharacteristic.getUuid().toString().compareToIgnoreCase(TIDMM_ZIGBEE_LIGHT_BATTERY_LEVEL_CHAR) == 0) {
                this.batteryLevel = bluetoothGattCharacteristic;
            } else if (bluetoothGattCharacteristic.getUuid().toString().compareToIgnoreCase(TIDMM_ZIGBEE_LIGHT_TARGET_ENDPOINT_CHAR) == 0) {
                this.targetEndPoint = bluetoothGattCharacteristic;
            }
        }
        this.zRow = new TIDMMZigBeeLightSwitchTableRow(context);
        this.zRow.title.setText("TI DMM ZigBee Light Switch");
        this.zRow.lightOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    TIDMMZigBeeLightSwitchService.this.dev.writeCharacteristicSync(TIDMMZigBeeLightSwitchService.this.switchOnOffChar, 1);
                } else {
                    TIDMMZigBeeLightSwitchService.this.dev.writeCharacteristicSync(TIDMMZigBeeLightSwitchService.this.switchOnOffChar, 0);
                }
            }
        });
    }

    public void enableService() {
        this.dev.readCharacteristicSync(this.switchOnOffChar);
        this.dev.readCharacteristicSync(this.targetAddressType);
        this.dev.readCharacteristicSync(this.targetAddress);
        this.dev.readCharacteristicSync(this.targetEndPoint);
        this.dev.readCharacteristicSync(this.batteryLevel);
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (this.switchOnOffChar.getUuid().toString().equalsIgnoreCase(bluetoothGattCharacteristic.getUuid().toString())) {
            if (bluetoothGattCharacteristic.getValue()[0] == 0) {
                this.zRow.lightOnOff.setChecked(false);
            } else {
                this.zRow.lightOnOff.setChecked(true);
            }
        } else if (this.targetAddressType.getUuid().toString().equalsIgnoreCase(bluetoothGattCharacteristic.getUuid().toString())) {
            this.zRow.targetAddressTypeLabel.setText(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
        } else if (this.targetAddress.getUuid().toString().equalsIgnoreCase(bluetoothGattCharacteristic.getUuid().toString())) {
            this.zRow.targetAddressLabel.setText(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
        } else if (this.batteryLevel.getUuid().toString().equalsIgnoreCase(bluetoothGattCharacteristic.getUuid().toString())) {
            TextView textView = this.zRow.switchBatteryLevelLabel;
            StringBuilder sb = new StringBuilder();
            sb.append(bluetoothGattCharacteristic.getValue()[0]);
            sb.append("%");
            textView.setText(sb.toString());
        } else if (this.targetEndPoint.getUuid().toString().equalsIgnoreCase(bluetoothGattCharacteristic.getUuid().toString())) {
            this.zRow.targetEndpoint.setText(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
        }
    }

    public TableRow getTableRow() {
        return this.zRow;
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(TIDMM_ZIGBEE_LIGHT_SWITCH_SERVICE) == 0;
    }
}
