package com.p004ti.device_selector.filtering;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;

/* renamed from: com.ti.device_selector.filtering.BTDeviceFilter */
public abstract class BTDeviceFilter {
    protected String PREF_FILTER_CONFIG_ENABLE;
    protected String TAG = BTDeviceFilter.class.getSimpleName();
    protected Context con;
    protected boolean enabled;
    protected BTDeviceFilter mThis;

    public abstract View getConfigView();

    public abstract boolean includeDevice(BluetoothLEDevice bluetoothLEDevice);

    public BTDeviceFilter(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("preference_filter_config_");
        sb.append(this.TAG);
        sb.append("_enable");
        this.PREF_FILTER_CONFIG_ENABLE = sb.toString();
        this.con = context;
        this.mThis = this;
    }

    public boolean readFilterConfig() {
        this.enabled = PreferenceManager.getDefaultSharedPreferences(this.con).getBoolean(this.PREF_FILTER_CONFIG_ENABLE, false);
        return true;
    }

    public boolean storeFilterConfig() {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.con).edit();
        edit.putBoolean(this.PREF_FILTER_CONFIG_ENABLE, this.enabled);
        return edit.commit();
    }

    public void setFilterState(boolean z) {
        this.enabled = z;
        storeFilterConfig();
    }
}
