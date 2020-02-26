package com.p004ti.device_selector.filtering;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.filtering.BTDeviceFilterRSSIFilter */
public class BTDeviceFilterRSSIFilter extends BTDeviceFilter {
    protected String PREF_FILTER_CONFIG_RSSI_ABOVE_LEVEL;
    protected String PREF_FILTER_CONFIG_RSSI_LEVEL;
    /* access modifiers changed from: private */
    public boolean above;
    /* access modifiers changed from: private */
    public int level;
    private View myView;

    public BTDeviceFilterRSSIFilter(Context context) {
        super(context);
        StringBuilder sb = new StringBuilder();
        String str = "preference_filter_config_";
        sb.append(str);
        sb.append(this.TAG);
        sb.append("_level");
        this.PREF_FILTER_CONFIG_RSSI_LEVEL = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(this.TAG);
        sb2.append("_above");
        this.PREF_FILTER_CONFIG_RSSI_ABOVE_LEVEL = sb2.toString();
        readFilterConfig();
        this.myView = LayoutInflater.from(context).inflate(R.layout.filter_config_rssi, null);
        Switch switchR = (Switch) this.myView.findViewById(R.id.trfcws_switch);
        switchR.setText("RSSI Filter Enable");
        switchR.setChecked(this.enabled);
        switchR.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                BTDeviceFilterRSSIFilter.this.mThis.enabled = z;
                BTDeviceFilterRSSIFilter.this.storeFilterConfig();
            }
        });
        Switch switchR2 = (Switch) ((ViewGroup) this.myView).getChildAt(2).findViewById(R.id.trfcws_switch);
        switchR2.setChecked(this.above);
        switchR2.setText("Filter in above level");
        switchR2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                BTDeviceFilterRSSIFilter.this.above = z;
                BTDeviceFilterRSSIFilter.this.storeFilterConfig();
            }
        });
        final TextView textView = (TextView) this.myView.findViewById(R.id.trfcwsat_filter_value);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.level);
        sb3.append("dBm");
        textView.setText(sb3.toString());
        SeekBar seekBar = (SeekBar) this.myView.findViewById(R.id.trfcwsat_value_seek_bar);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                BTDeviceFilterRSSIFilter.this.level = i - 100;
                TextView textView = textView;
                StringBuilder sb = new StringBuilder();
                sb.append(BTDeviceFilterRSSIFilter.this.level);
                sb.append("dBm");
                textView.setText(sb.toString());
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                BTDeviceFilterRSSIFilter.this.level = seekBar.getProgress() - 100;
                BTDeviceFilterRSSIFilter.this.storeFilterConfig();
            }
        });
        seekBar.setProgress(this.level + 100);
    }

    public boolean readFilterConfig() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.con);
        this.enabled = defaultSharedPreferences.getBoolean(this.PREF_FILTER_CONFIG_ENABLE, false);
        this.level = defaultSharedPreferences.getInt(this.PREF_FILTER_CONFIG_RSSI_LEVEL, 0);
        this.above = defaultSharedPreferences.getBoolean(this.PREF_FILTER_CONFIG_RSSI_ABOVE_LEVEL, false);
        return true;
    }

    public boolean storeFilterConfig() {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.con).edit();
        edit.putInt(this.PREF_FILTER_CONFIG_RSSI_LEVEL, this.level);
        edit.putBoolean(this.PREF_FILTER_CONFIG_RSSI_ABOVE_LEVEL, this.above);
        edit.putBoolean(this.PREF_FILTER_CONFIG_ENABLE, this.enabled);
        edit.commit();
        return true;
    }

    public View getConfigView() {
        return this.myView;
    }

    public boolean includeDevice(BluetoothLEDevice bluetoothLEDevice) {
        if (!this.enabled) {
            return false;
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Filtering device: ");
        sb.append(bluetoothLEDevice.f27d.getName());
        sb.append(" (");
        sb.append(bluetoothLEDevice.f27d.getAddress());
        sb.append(") RSSI: ");
        sb.append(bluetoothLEDevice.f30sR.getRssi());
        sb.append("dBm");
        Log.e(str, sb.toString());
        if (this.above) {
            if (bluetoothLEDevice.f30sR.getRssi() >= this.level) {
                return true;
            }
            return false;
        } else if (bluetoothLEDevice.f30sR.getRssi() <= this.level) {
            return true;
        } else {
            return false;
        }
    }
}
