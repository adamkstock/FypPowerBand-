package com.p004ti.device_selector.filtering;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.ti.device_selector.filtering.BTDeviceFilterGlobal */
public class BTDeviceFilterGlobal {
    private static BTDeviceFilterGlobal myInstance;
    protected String PREF_GLOBAL_FILTER_ENABLE = "preference_filter_global_enable";
    protected String TAG = BTDeviceFilterGlobal.class.getSimpleName();
    public Context con;
    private ArrayList<BluetoothLEDevice> filteredInDevices;
    private ArrayList<BluetoothLEDevice> filteredOutDevices;
    private ArrayList<BTDeviceFilter> filters;
    public boolean globalEnable = false;
    private BTDeviceFilterGlobal mThis;

    public BTDeviceFilterGlobal(Context context) {
        this.con = context;
        this.filters = new ArrayList<>();
        this.filteredInDevices = new ArrayList<>();
        this.filteredOutDevices = new ArrayList<>();
        addFilterToBank(new BTDeviceFilterRSSIFilter(context));
        addFilterToBank(new BTDeviceFilterDeviceNameFilter(context));
        this.globalEnable = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(this.PREF_GLOBAL_FILTER_ENABLE, false);
        this.mThis = this;
    }

    public static BTDeviceFilterGlobal getInstance(Context context) {
        if (myInstance == null) {
            myInstance = new BTDeviceFilterGlobal(context);
        }
        return myInstance;
    }

    public void enableGlobalFilterBank() {
        this.globalEnable = true;
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mThis.con).edit();
        edit.putBoolean(this.PREF_GLOBAL_FILTER_ENABLE, this.globalEnable);
        edit.commit();
        Log.e(this.TAG, "Global Filter enable ON");
    }

    public void disableGlobalFilterBank() {
        this.globalEnable = false;
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mThis.con).edit();
        edit.putBoolean(this.PREF_GLOBAL_FILTER_ENABLE, this.globalEnable);
        edit.commit();
        Log.e(this.TAG, "Global Filter enable OFF");
    }

    public void addFilterToBank(BTDeviceFilter bTDeviceFilter) {
        if (bTDeviceFilter != null) {
            this.filters.add(bTDeviceFilter);
        }
    }

    public ArrayList<BTDeviceFilter> getFilterBank() {
        return this.mThis.filters;
    }

    public int inDeviceCount() {
        return this.filteredInDevices.size();
    }

    public int outDeviceCount() {
        return this.filteredOutDevices.size();
    }

    public int totalDeviceCount() {
        return this.filteredOutDevices.size() + this.filteredInDevices.size();
    }

    public void resetDeviceList() {
        this.filteredInDevices = new ArrayList<>();
        this.filteredOutDevices = new ArrayList<>();
    }

    public void reloadFilterConfigs() {
        Iterator it = this.filters.iterator();
        while (it.hasNext()) {
            ((BTDeviceFilter) it.next()).readFilterConfig();
        }
    }

    public boolean filterDevice(BluetoothLEDevice bluetoothLEDevice) {
        boolean z = false;
        this.globalEnable = PreferenceManager.getDefaultSharedPreferences(this.mThis.con).getBoolean(this.PREF_GLOBAL_FILTER_ENABLE, false);
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("filterDevice: Running device ");
        sb.append(bluetoothLEDevice.f27d.getAddress());
        sb.append(" (");
        sb.append(bluetoothLEDevice.f27d.getName());
        sb.append(") Through filtering");
        Log.e(str, sb.toString());
        if (!this.globalEnable) {
            return true;
        }
        Iterator it = this.filters.iterator();
        while (it.hasNext()) {
            BTDeviceFilter bTDeviceFilter = (BTDeviceFilter) it.next();
            if (bTDeviceFilter.enabled && !bTDeviceFilter.includeDevice(bluetoothLEDevice)) {
                Iterator it2 = this.filteredOutDevices.iterator();
                boolean z2 = false;
                while (it2.hasNext()) {
                    if (((BluetoothLEDevice) it2.next()).f27d.getAddress().equalsIgnoreCase(bluetoothLEDevice.f27d.getAddress())) {
                        z2 = true;
                    }
                }
                if (!z2) {
                    this.filteredOutDevices.add(bluetoothLEDevice);
                }
                return false;
            }
        }
        Iterator it3 = this.filteredInDevices.iterator();
        while (it3.hasNext()) {
            if (((BluetoothLEDevice) it3.next()).f27d.getAddress().equalsIgnoreCase(bluetoothLEDevice.f27d.getAddress())) {
                z = true;
            }
        }
        if (!z) {
            this.filteredInDevices.add(bluetoothLEDevice);
        }
        return true;
    }
}
