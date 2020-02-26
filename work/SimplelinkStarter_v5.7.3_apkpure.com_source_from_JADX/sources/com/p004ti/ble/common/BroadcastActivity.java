package com.p004ti.ble.common;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.bluetooth_le_controller.EddystoneBeaconDecoder;
import com.p004ti.device_selector.BTDeviceWithAdvData;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.util.bleUtility;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.common.BroadcastActivity */
public class BroadcastActivity extends AppCompatActivity {
    public static final String TAG = "BroadCastActivity";
    public String btAddr;
    public BTDeviceWithAdvData mDev;
    public BroadcastActivity mThis;
    ScanCallback scanCB = new ScanCallback() {
        public void onScanResult(int i, final ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            if (scanResult.getDevice().getAddress().equalsIgnoreCase(BroadcastActivity.this.btAddr)) {
                Log.d(BroadcastActivity.TAG, "Found correct device !");
                if (BroadcastActivity.this.mDev == null) {
                    BroadcastActivity.this.mDev = new BTDeviceWithAdvData(scanResult.getDevice());
                    BroadcastActivity.this.mDev.beaconDecoder = new EddystoneBeaconDecoder();
                }
                final ScanRecord scanRecord = scanResult.getScanRecord();
                if (BroadcastActivity.this.mDev.beaconDecoder.updateData(scanRecord.getServiceData())) {
                    BroadcastActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) BroadcastActivity.this.findViewById(R.id.ab_title)).setText("Eddystone beacon Decoder");
                            ((TextView) BroadcastActivity.this.findViewById(R.id.ab_service_data_title)).setText("Eddystone parameters");
                            ((TextView) BroadcastActivity.this.findViewById(R.id.ab_bt_addr_label)).setText(BroadcastActivity.this.mDev.btDevice.getAddress().toString());
                            TextView textView = (TextView) BroadcastActivity.this.findViewById(R.id.ab_rssi_label);
                            StringBuilder sb = new StringBuilder();
                            sb.append(scanResult.getRssi());
                            sb.append("dB");
                            textView.setText(sb.toString());
                            TableLayout tableLayout = (TableLayout) BroadcastActivity.this.findViewById(R.id.ab_adv_data_table);
                            tableLayout.removeAllViews();
                            TableLayout tableLayout2 = (TableLayout) BroadcastActivity.this.findViewById(R.id.srv_data_table);
                            tableLayout2.removeAllViews();
                            SparseArray manufacturerSpecificData = scanRecord.getManufacturerSpecificData();
                            for (int i = 0; i < manufacturerSpecificData.size(); i++) {
                                int keyAt = manufacturerSpecificData.keyAt(i);
                                byte[] bArr = (byte[]) manufacturerSpecificData.get(keyAt);
                                TableRow tableRow = (TableRow) ((LayoutInflater) BroadcastActivity.this.getApplicationContext().getSystemService("layout_inflater")).inflate(R.layout.adv_data_table_row, null);
                                ((TextView) tableRow.findViewById(R.id.advdtr_data_name)).setText((CharSequence) BluetoothGATTDefines.manufacturerIDStrings.get(Integer.valueOf(keyAt)));
                                ((TextView) tableRow.findViewById(R.id.advdtr_data_value)).setText(bleUtility.getStringFromByteVector(bArr));
                                tableLayout.addView(tableRow);
                            }
                            BroadcastActivity.this.mDev.beaconDecoder.updateTableWithParameters(tableLayout2, BroadcastActivity.this.getApplicationContext());
                        }
                    });
                }
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_broadcaster);
        this.btAddr = getIntent().getExtras().getString(TopLevel.BROADCAST_DEVICE_ADDR);
        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(this.scanCB);
        this.mThis = this;
    }
}
