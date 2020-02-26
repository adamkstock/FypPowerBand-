package com.p004ti.ble.dmm;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.ti.ble.simplelinkstarter.R;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

/* renamed from: com.ti.ble.dmm.TIDMMProvisioningService */
public class TIDMMProvisioningService extends GenericBluetoothProfile {
    public static final String DMM_PROV_ROW_CLICKED = "TIDMMProvisioningRow.ACTION_VIEW_CLICKED";
    public static final String TAG = TIDMMProvisioningService.class.getSimpleName();
    public static final String TIDMM_PROVISIONING_DEFAULT_NETWORK_KEY_CHAR = "f0001196-0451-4000-b000-000000000000";
    public static final String TIDMM_PROVISIONING_NETWORK_CHANNEL_MASK_CHAR = "f0001194-0451-4000-b000-000000000000";
    public static final String TIDMM_PROVISIONING_NETWORK_PAN_ID_CHAR = "f0001191-0451-4000-b000-000000000000";
    public static final String TIDMM_PROVISIONING_NETWORK_PROVISIONING_STATE_CHAR = "f0001198-0451-4000-b000-000000000000";
    public static final String TIDMM_PROVISIONING_SERVICE_UUID = "f0001190-0451-4000-b000-000000000000";
    public static final String TIDMM_PROVISIONING_START_NETWORK_PROVISIONING_CHAR = "f0001197-0451-4000-b000-000000000000";
    public BroadcastReceiver cellClicker = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        }
    };
    TIDMMProvisioningService mThis = this;

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void disableService() {
    }

    public void enableService() {
    }

    public void periodWasUpdated(int i) {
    }

    public TIDMMProvisioningService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context, 1000, true);
        this.tRow.getChildAt(0).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TIDMMProvisioningService.this.mThis.context, TIDMMProvisioningActivity.class);
                intent.putExtra(TIDMMProvisioningActivity.DMM_BLUETOOTH_DEVICE_EXTRA, TIDMMProvisioningService.this.mBTDevice);
                intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
                TIDMMProvisioningService.this.mThis.context.startActivity(intent);
                TIDMMProvisioningService.this.mThis.context.sendBroadcast(intent);
            }
        });
        this.tRow.icon.setImageResource(R.mipmap.dmm_prov);
        this.tRow.title.setText("DMM Provisioning Service");
        this.tRow.sl1.setVisibility(4);
        this.tRow.value.setVisibility(4);
    }

    public void onResume() {
        super.onResume();
        this.mThis.context.registerReceiver(this.cellClicker, makeTILampBroadcastFilter());
    }

    public void onPause() {
        this.context.unregisterReceiver(this.cellClicker);
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(TIDMM_PROVISIONING_SERVICE_UUID) == 0;
    }

    private static IntentFilter makeTILampBroadcastFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DMM_PROV_ROW_CLICKED);
        return intentFilter;
    }
}
