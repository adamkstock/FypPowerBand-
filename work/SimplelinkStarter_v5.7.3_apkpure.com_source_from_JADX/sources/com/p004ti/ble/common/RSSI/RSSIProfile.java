package com.p004ti.ble.common.RSSI;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.TrippleSparkLineView;
import com.ti.ble.simplelinkstarter.R;
import java.util.Map;

/* renamed from: com.ti.ble.common.RSSI.RSSIProfile */
public class RSSIProfile extends GenericBluetoothProfile {
    static final String TAG = RSSIProfile.class.getSimpleName();
    TextView name = ((TextView) this.tRow.findViewById(R.id.gctr_characteristic_title));

    /* renamed from: sl */
    TrippleSparkLineView f35sl = ((TrippleSparkLineView) this.tRow.findViewById(R.id.gctr_sparkline1));
    TextView value = ((TextView) this.tRow.findViewById(R.id.gctr_value));

    /* access modifiers changed from: protected */
    public void calibrationButtonTouched() {
    }

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void didUpdateFirmwareRevision(String str) {
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void disableService() {
    }

    public void enableService() {
    }

    public boolean isDataC(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return false;
    }

    public boolean isEnabledByPrefs(String str) {
        return false;
    }

    public void onOffWasUpdated(boolean z) {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void periodWasUpdated(int i) {
    }

    public void printError(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
    }

    public RSSIProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow.setIcon(getIconPrefix(), "", "rssi");
        this.f35sl.maxVal(Float.valueOf(0.0f));
        this.f35sl.minVal(Float.valueOf(-120.0f));
        TrippleSparkLineView trippleSparkLineView = this.f35sl;
        trippleSparkLineView.autoScale = false;
        trippleSparkLineView.autoScaleBounceBack = false;
        trippleSparkLineView.setColor(0, 0, 0, 0, 1);
        this.f35sl.setColor(0, 0, 0, 0, 2);
        this.name.setText("RSSI");
        this.value.setText("Unknown");
        this.tRow.getChildAt(0).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d(RSSIProfile.TAG, "Cell touched");
            }
        });
        ((ImageView) this.tRow.getChildAt(0).findViewById(R.id.gctr_disclosure_indicator)).setVisibility(4);
    }

    public void rssiUpdated(int i) {
        super.rssiUpdated(i);
        TextView textView = this.value;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(i);
        sb.append("dBm");
        textView.setText(sb.toString());
        this.f35sl.addValue((float) i);
    }

    public TableRow getTableRow() {
        return super.getTableRow();
    }

    public Map<String, String> getMQTTMap() {
        return super.getMQTTMap();
    }

    public void grayOutCell(boolean z) {
        super.grayOutCell(z);
    }
}
