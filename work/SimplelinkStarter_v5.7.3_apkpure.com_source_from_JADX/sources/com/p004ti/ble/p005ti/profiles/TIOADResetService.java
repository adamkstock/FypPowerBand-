package com.p004ti.ble.p005ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEManager;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.ti.ble.simplelinkstarter.R;
import java.util.UUID;

/* renamed from: com.ti.ble.ti.profiles.TIOADResetService */
public class TIOADResetService extends GenericBluetoothProfile {
    public static final String TAG = "TIOADResetService";
    public static final String oadRestartService_UUID = "f000ffd0-0451-4000-b000-000000000000";
    public static final String oadStartResetCharacteristic_UUID = "f000ffd1-0451-4000-b000-000000000000";
    protected BluetoothLEDevice mBTLEDev;
    public BluetoothGattCharacteristic resetC;
    public BluetoothGattService resetS;
    public TIOADResetServiceTableRow tRow;

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void disableService() {
    }

    public void enableService() {
    }

    public TIOADResetService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.mBTLEDev = BluetoothLEManager.getInstance(context).deviceForBluetoothDev(bluetoothDevice);
        if (bluetoothGattService.getUuid().toString().equalsIgnoreCase(oadRestartService_UUID)) {
            Log.d(TAG, "Found TI OAD Reset Service !");
            this.resetS = bluetoothGattService;
            this.resetC = bluetoothGattService.getCharacteristic(UUID.fromString(oadStartResetCharacteristic_UUID));
            if (this.resetC == null) {
                return;
            }
        }
        this.tRow = new TIOADResetServiceTableRow(context);
        ((TextView) this.tRow.findViewById(R.id.trgtb_characteristic_title)).setText("OAD Reset Service");
        ((ImageView) this.tRow.findViewById(R.id.trgtb_icon)).setImageResource(R.mipmap.cc2652_launchpad_oad_reset);
        Button button = (Button) this.tRow.findViewById(R.id.trgtb_button_one);
        button.setText("Reset Target");
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TIOADResetService.this.mBTLEDev.writeCharacteristicSync(TIOADResetService.this.resetC, 0);
                Log.d(TIOADResetService.TAG, "Sent Reset To Target");
            }
        });
        ((Button) this.tRow.findViewById(R.id.trgtb_button_two)).setText("Start OAD");
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TIOADResetService.this.mBTLEDev.writeCharacteristicSync(TIOADResetService.this.resetC, 1);
                Log.d(TIOADResetService.TAG, "Sent Start OAD to Target");
            }
        });
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(oadRestartService_UUID) == 0;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public boolean isDataC(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return super.isDataC(bluetoothGattCharacteristic);
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        Log.d(TAG, "Write to reset characteristic success");
        super.didWriteValueForCharacteristic(bluetoothGattCharacteristic);
    }

    public TableRow getTableRow() {
        return this.tRow;
    }

    public String getIconPrefix() {
        return super.getIconPrefix();
    }
}
