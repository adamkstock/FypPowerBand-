package com.p004ti.device_selector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice.BluetoothLEDeviceCB;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEManager;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.ServiceExplorerServicesActivity */
public class ServiceExplorerServicesActivity extends AppCompatActivity {
    public static final String BLUETOOTH_DEVICE_EXTRA = "com.ti.ble.service.explorer.service.activity.BLUETOOTH_DEVICE";
    public static final String BLUETOOTH_SERVICE_UUID_EXTRA = "com.ti.ble.service.explorer.service.activity.BLUETOOTH_SERVICE_UUID";
    static final String TAG = ServiceExplorerServicesActivity.class.getSimpleName();
    BluetoothLEDeviceCB bluetoothCB = new BluetoothLEDeviceCB() {
        public void deviceConnectTimedOut(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceDidDisconnect(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceDiscoveryTimedOut(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceFailed(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceReady(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void didReadCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void didReadDescriptor(BluetoothLEDevice bluetoothLEDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
        }

        public void didReadRSSI(int i) {
        }

        public void didUpdateCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void didUpdateCharacteristicIndication(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void didUpdateCharacteristicNotification(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void didWriteCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void mtuValueChanged(int i) {
        }

        public void waitingForConnect(BluetoothLEDevice bluetoothLEDevice, int i, int i2) {
        }

        public void waitingForDiscovery(BluetoothLEDevice bluetoothLEDevice, int i, int i2) {
        }
    };
    BluetoothLEDevice mBTLEDev;
    BluetoothDevice mBluetoothDevice;
    ServiceExplorerServicesActivity mThis;
    TableLayout servicesTable;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mThis = this;
        setContentView((int) R.layout.activity_service_explorer_services);
        this.servicesTable = (TableLayout) findViewById(R.id.ases_services_table);
        Intent intent = getIntent();
        String str = "com.ti.ble.service.explorer.service.activity.BLUETOOTH_DEVICE";
        if (bundle != null) {
            this.mBluetoothDevice = (BluetoothDevice) bundle.getParcelable(str);
            Log.d(TAG, "Restored saved state");
        } else {
            this.mBluetoothDevice = (BluetoothDevice) intent.getParcelableExtra(str);
            Log.d(TAG, "Read from intent");
        }
        this.mBTLEDev = BluetoothLEManager.getInstance(getApplicationContext()).deviceForBluetoothDev(this.mBluetoothDevice);
        this.mBTLEDev.myCB = this.bluetoothCB;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mBluetoothDevice.getName());
        sb.append(" Services");
        setTitle(sb.toString());
        int i = 0;
        for (final BluetoothGattService bluetoothGattService : this.mBTLEDev.f28g.getServices()) {
            int i2 = i + 1;
            ServiceExplorerServicesActivityTableRow serviceExplorerServicesActivityTableRow = new ServiceExplorerServicesActivityTableRow((Context) this, bluetoothGattService, i);
            serviceExplorerServicesActivityTableRow.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(ServiceExplorerServicesActivity.this.mThis, ServiceExplorerCharacteristicsActivity.class);
                    intent.putExtra(ServiceExplorerServicesActivity.BLUETOOTH_SERVICE_UUID_EXTRA, bluetoothGattService.getUuid().toString());
                    intent.putExtra("com.ti.ble.service.explorer.service.activity.BLUETOOTH_DEVICE", ServiceExplorerServicesActivity.this.mBluetoothDevice);
                    ServiceExplorerServicesActivity.this.mThis.startActivity(intent);
                }
            });
            this.servicesTable.addView(serviceExplorerServicesActivityTableRow);
            i = i2;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable("com.ti.ble.service.explorer.service.activity.BLUETOOTH_DEVICE", this.mBluetoothDevice);
        super.onSaveInstanceState(bundle);
    }
}
