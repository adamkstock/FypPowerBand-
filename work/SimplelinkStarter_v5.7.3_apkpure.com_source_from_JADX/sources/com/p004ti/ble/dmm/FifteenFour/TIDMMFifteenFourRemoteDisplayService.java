package com.p004ti.ble.dmm.FifteenFour;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.bleUtility;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.dmm.FifteenFour.TIDMMFifteenFourRemoteDisplayService */
public class TIDMMFifteenFourRemoteDisplayService extends GenericBluetoothProfile {
    public static final String TAG = TIDMMFifteenFourRemoteDisplayService.class.getSimpleName();
    public static final String TIDMM_FIFTEEN_FOUR_REMOTE_DISPLAY_CONCENTRATIOR_LED = "f0001183-0451-4000-b000-000000000000";
    public static final String TIDMM_FIFTEEN_FOUR_REMOTE_DISPLAY_NODE_DATA = "f0001181-0451-4000-b000-000000000000";
    public static final String TIDMM_FIFTEEN_FOUR_REMOTE_DISPLAY_NODE_REPORT_INT = "f0001182-0451-4000-b000-000000000000";
    public static final String TIDMM_FIFTEEN_FOUR_REMOTE_DISPLAY_SERVICE = "f0001180-0451-4000-b000-000000000000";
    public TIDMMFifteenFourRemoteDisplayTableRow fRow;
    private BluetoothGattCharacteristic nodeData;
    private BluetoothGattCharacteristic nodeReportInterval;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic remoteDisplayConcentratorLED;

    public void deConfigureService() {
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public TIDMMFifteenFourRemoteDisplayService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIDMM_FIFTEEN_FOUR_REMOTE_DISPLAY_NODE_DATA)) {
                this.nodeData = bluetoothGattCharacteristic;
            } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIDMM_FIFTEEN_FOUR_REMOTE_DISPLAY_NODE_REPORT_INT)) {
                this.nodeReportInterval = bluetoothGattCharacteristic;
            } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIDMM_FIFTEEN_FOUR_REMOTE_DISPLAY_CONCENTRATIOR_LED)) {
                this.remoteDisplayConcentratorLED = bluetoothGattCharacteristic;
            }
        }
        this.fRow = new TIDMMFifteenFourRemoteDisplayTableRow(context);
        this.fRow.title.setText("15.4 Remote Display Service");
        this.fRow.icon.setImageResource(R.mipmap.dmm_rd);
        this.fRow.toggleCollectorLEDButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TIDMMFifteenFourRemoteDisplayService.this.dev.writeCharacteristicSync(TIDMMFifteenFourRemoteDisplayService.this.remoteDisplayConcentratorLED, 1);
            }
        });
    }

    public void configureService() {
        this.dev.readCharacteristicSync(this.nodeData);
        if ((this.nodeReportInterval.getProperties() & 2) == 2) {
            this.dev.readCharacteristicSync(this.nodeReportInterval);
        }
        if ((this.remoteDisplayConcentratorLED.getProperties() & 2) == 2) {
            this.dev.readCharacteristicSync(this.remoteDisplayConcentratorLED);
        }
    }

    public void enableService() {
        this.dev.setCharacteristicNotificationSync(this.nodeData, true);
    }

    public void disableService() {
        this.dev.setCharacteristicNotificationSync(this.nodeData, false);
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(this.nodeData.getUuid().toString())) {
            this.fRow.nodeDataLabel.setText(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
        } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(this.nodeReportInterval.getUuid().toString())) {
            this.fRow.nodeDataLabel.setText(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
        }
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(this.nodeData.getUuid().toString())) {
            this.fRow.nodeDataLabel.setText(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
        }
    }

    public TableRow getTableRow() {
        return this.fRow;
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(TIDMM_FIFTEEN_FOUR_REMOTE_DISPLAY_SERVICE) == 0;
    }
}
