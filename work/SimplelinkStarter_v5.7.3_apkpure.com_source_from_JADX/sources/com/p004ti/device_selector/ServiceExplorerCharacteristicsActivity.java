package com.p004ti.device_selector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
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
import com.p004ti.ble.common.GattInfo;
import com.ti.ble.simplelinkstarter.R;
import java.util.UUID;

/* renamed from: com.ti.device_selector.ServiceExplorerCharacteristicsActivity */
public class ServiceExplorerCharacteristicsActivity extends AppCompatActivity {
    public static final String BLUETOOTH_DEVICE_EXTRA = "com.ti.ble.service.explorer.service.activity.BLUETOOTH_DEVICE";
    public static final String BLUETOOTH_SERVICE_EXTRA = "com.ti.ble.service.explorer.service.activity.BLUETOOTH_SERVICE";
    static final String TAG = ServiceExplorerCharacteristicsActivity.class.getSimpleName();
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

        public void didReadRSSI(int i) {
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

        public void didReadDescriptor(BluetoothLEDevice bluetoothLEDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
            for (int i = 0; i < ServiceExplorerCharacteristicsActivity.this.servicesTable.getChildCount(); i++) {
                ((ServiceExplorerCharacteristicsActivityTableRow) ServiceExplorerCharacteristicsActivity.this.servicesTable.getChildAt(i)).refreshDescription();
            }
        }

        public void didUpdateCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            ServiceExplorerCharacteristicsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    for (int i = 0; i < ServiceExplorerCharacteristicsActivity.this.servicesTable.getChildCount(); i++) {
                        ((ServiceExplorerCharacteristicsActivityTableRow) ServiceExplorerCharacteristicsActivity.this.servicesTable.getChildAt(i)).refreshValue();
                    }
                }
            });
        }

        public void didReadCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            ServiceExplorerCharacteristicsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    for (int i = 0; i < ServiceExplorerCharacteristicsActivity.this.servicesTable.getChildCount(); i++) {
                        ((ServiceExplorerCharacteristicsActivityTableRow) ServiceExplorerCharacteristicsActivity.this.servicesTable.getChildAt(i)).refreshValue();
                    }
                }
            });
        }
    };
    OnClickListener characteristicWasTouched = new OnClickListener() {
        public void onClick(View view) {
            final ServiceExplorerCharacteristicsActivityTableRow serviceExplorerCharacteristicsActivityTableRow = (ServiceExplorerCharacteristicsActivityTableRow) view;
            String str = ServiceExplorerCharacteristicsActivity.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Cell with Characteristic : ");
            sb.append(serviceExplorerCharacteristicsActivityTableRow.myCharacteristic.getUuid().toString());
            sb.append(" Touched !");
            Log.d(str, sb.toString());
            ServiceExplorerCharacteristicActionDialog.newInstance(ServiceExplorerCharacteristicsActivity.this.mThis, serviceExplorerCharacteristicsActivityTableRow.myCharacteristic, new ServiceExplorerCharacteristicAction() {
                public void shallReadCharacteristic() {
                    ServiceExplorerCharacteristicsActivity.this.mBTLEDev.readCharacteristicSync(serviceExplorerCharacteristicsActivityTableRow.myCharacteristic);
                }

                public void shallWriteCharacteristic(byte[] bArr) {
                    ServiceExplorerCharacteristicsActivity.this.mBTLEDev.writeCharacteristicSync(serviceExplorerCharacteristicsActivityTableRow.myCharacteristic, bArr);
                }

                public void shallSetNotification() {
                    if (serviceExplorerCharacteristicsActivityTableRow.myCharacteristic.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG).getValue()[0] == 1) {
                        ServiceExplorerCharacteristicsActivity.this.mBTLEDev.setCharacteristicNotificationSync(serviceExplorerCharacteristicsActivityTableRow.myCharacteristic, false);
                    } else {
                        ServiceExplorerCharacteristicsActivity.this.mBTLEDev.setCharacteristicNotificationSync(serviceExplorerCharacteristicsActivityTableRow.myCharacteristic, true);
                    }
                }

                public void shallSetIndication() {
                    if (serviceExplorerCharacteristicsActivityTableRow.myCharacteristic.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG).getValue()[0] == 2) {
                        ServiceExplorerCharacteristicsActivity.this.mBTLEDev.setCharacteristicIndicationSync(serviceExplorerCharacteristicsActivityTableRow.myCharacteristic, false);
                    } else {
                        ServiceExplorerCharacteristicsActivity.this.mBTLEDev.setCharacteristicIndicationSync(serviceExplorerCharacteristicsActivityTableRow.myCharacteristic, true);
                    }
                }
            }).show(ServiceExplorerCharacteristicsActivity.this.mThis.getFragmentManager(), "characteristicActionDialog");
        }
    };
    BluetoothLEDevice mBTLEDev;
    BluetoothDevice mBluetoothDevice;
    BluetoothGattService mServ;
    ServiceExplorerCharacteristicsActivity mThis;
    TableLayout servicesTable;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mThis = this;
        setContentView((int) R.layout.activity_service_explorer_services);
        this.servicesTable = (TableLayout) findViewById(R.id.ases_services_table);
        Intent intent = getIntent();
        this.mBluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("com.ti.ble.service.explorer.service.activity.BLUETOOTH_DEVICE");
        String stringExtra = intent.getStringExtra(ServiceExplorerServicesActivity.BLUETOOTH_SERVICE_UUID_EXTRA);
        this.mBTLEDev = BluetoothLEManager.getInstance(getApplicationContext()).deviceForBluetoothDev(this.mBluetoothDevice);
        this.mServ = this.mBTLEDev.f28g.getService(UUID.fromString(stringExtra));
        this.mBTLEDev.myCB = this.bluetoothCB;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(GattInfo.getTitle(this.mServ.getUuid()));
        sb.append(" characteristics");
        setTitle(sb.toString());
        int i = 0;
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mServ.getCharacteristics()) {
            BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString("00002901-0000-1000-8000-00805f9b34fb"));
            int i2 = i + 1;
            ServiceExplorerCharacteristicsActivityTableRow serviceExplorerCharacteristicsActivityTableRow = new ServiceExplorerCharacteristicsActivityTableRow(this, bluetoothGattCharacteristic, descriptor, i);
            serviceExplorerCharacteristicsActivityTableRow.setOnClickListener(this.characteristicWasTouched);
            this.servicesTable.addView(serviceExplorerCharacteristicsActivityTableRow);
            if (descriptor != null) {
                this.mBTLEDev.readDescriptorSync(descriptor);
            }
            if ((bluetoothGattCharacteristic.getProperties() & 2) == 2) {
                this.mBTLEDev.readCharacteristicSync(bluetoothGattCharacteristic);
            }
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
}
