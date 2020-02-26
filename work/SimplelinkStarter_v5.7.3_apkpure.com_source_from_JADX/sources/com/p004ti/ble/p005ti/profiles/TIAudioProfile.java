package com.p004ti.ble.p005ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.p004ti.ble.audio.AdvancedRemoteBLEAudioDefines;
import com.p004ti.ble.audio.AudioActivity;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.ti.profiles.TIAudioProfile */
public class TIAudioProfile extends GenericBluetoothProfile implements OnClickListener {
    public final Context context;

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

    public TIAudioProfile(Context context2, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context2, bluetoothDevice, bluetoothGattService);
        this.tRow = new GenericCharacteristicTableRow(context2, 1000, true);
        this.tRow.sl1.setVisibility(4);
        if (this.mBTDevice.getName().equals(TopLevel.Sensor_Tag)) {
            this.tRow.icon.setImageResource(R.mipmap.sensortag2_audio);
        } else {
            this.tRow.icon.setImageResource(R.mipmap.cc2650_rc_audio);
        }
        this.tRow.title.setText("Audio Service");
        this.tRow.value.setText("");
        this.tRow.getChildAt(0).setOnClickListener(this);
        this.context = context2;
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(AdvancedRemoteBLEAudioDefines.AudioServiceUUID) == 0;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onClick(View view) {
        Log.d("TIAudioProfile", "Cell was clicked !");
        this.dev.disconnectDevice();
        Intent intent = new Intent(this.context, AudioActivity.class);
        intent.putExtra(AudioActivity.BT_ADDRESS_EXTRA, this.mBTDevice.getAddress());
        this.context.startActivity(intent);
    }
}
