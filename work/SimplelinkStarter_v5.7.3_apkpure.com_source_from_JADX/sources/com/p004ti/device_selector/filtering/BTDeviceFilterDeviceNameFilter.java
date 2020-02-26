package com.p004ti.device_selector.filtering;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.filtering.BTDeviceFilterDeviceNameFilter */
public class BTDeviceFilterDeviceNameFilter extends BTDeviceFilter {
    protected String PREF_FILTER_CONFIG_DEVICE_NAME;
    protected String PREF_FILTER_CONFIG_DEVICE_NAME_MATCH_PARTIAL;
    /* access modifiers changed from: private */
    public String deviceName;
    private boolean filterAdvName;
    /* access modifiers changed from: private */
    public boolean matchPartial;
    private View myView;

    public BTDeviceFilterDeviceNameFilter(Context context) {
        super(context);
        StringBuilder sb = new StringBuilder();
        String str = "preference_filter_config_";
        sb.append(str);
        sb.append(this.TAG);
        sb.append("_name");
        this.PREF_FILTER_CONFIG_DEVICE_NAME = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(this.TAG);
        sb2.append("_match_partial");
        this.PREF_FILTER_CONFIG_DEVICE_NAME_MATCH_PARTIAL = sb2.toString();
        readFilterConfig();
        this.myView = LayoutInflater.from(context).inflate(R.layout.filter_config_adv_name, null);
        Switch switchR = (Switch) this.myView.findViewById(R.id.trfcws_switch);
        switchR.setText("Device Name Filter Enable");
        switchR.setChecked(this.enabled);
        switchR.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                BTDeviceFilterDeviceNameFilter.this.mThis.enabled = z;
                BTDeviceFilterDeviceNameFilter.this.storeFilterConfig();
            }
        });
        ViewGroup viewGroup = (ViewGroup) this.myView;
        final TableRow tableRow = (TableRow) viewGroup.getChildAt(2);
        TextView textView = (TextView) tableRow.findViewById(R.id.trfcwu_filter_name);
        textView.setText("Device Name Setting");
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.e(BTDeviceFilterDeviceNameFilter.this.TAG, "Device name setting touched");
                Builder builder = new Builder(BTDeviceFilterDeviceNameFilter.this.mThis.con);
                builder.setTitle("Enter Device Name to filter");
                final EditText editText = new EditText(BTDeviceFilterDeviceNameFilter.this.mThis.con);
                editText.setInputType(1);
                editText.setText(BTDeviceFilterDeviceNameFilter.this.deviceName);
                editText.setHint("Device Name");
                TextInputLayout textInputLayout = new TextInputLayout(BTDeviceFilterDeviceNameFilter.this.mThis.con);
                textInputLayout.setPadding(20, 20, 20, 20);
                textInputLayout.addView(editText);
                builder.setView(textInputLayout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = BTDeviceFilterDeviceNameFilter.this.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Text entered: ");
                        sb.append(editText.getText().toString());
                        Log.d(str, sb.toString());
                        BTDeviceFilterDeviceNameFilter.this.deviceName = editText.getText().toString();
                        BTDeviceFilterDeviceNameFilter.this.storeFilterConfig();
                        ((TextView) tableRow.findViewById(R.id.trfcwu_filter_value)).setText(BTDeviceFilterDeviceNameFilter.this.deviceName);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(BTDeviceFilterDeviceNameFilter.this.TAG, "Dialog Canceled");
                    }
                });
                builder.show();
            }
        });
        ((TextView) tableRow.findViewById(R.id.trfcwu_filter_value)).setText(this.deviceName);
        Switch switchR2 = (Switch) viewGroup.getChildAt(3).findViewById(R.id.trfcws_switch);
        switchR2.setChecked(this.matchPartial);
        switchR2.setText("Match Partial Name");
        switchR2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                BTDeviceFilterDeviceNameFilter.this.matchPartial = z;
                BTDeviceFilterDeviceNameFilter.this.storeFilterConfig();
            }
        });
    }

    public boolean readFilterConfig() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.con);
        this.enabled = defaultSharedPreferences.getBoolean(this.PREF_FILTER_CONFIG_ENABLE, false);
        this.deviceName = defaultSharedPreferences.getString(this.PREF_FILTER_CONFIG_DEVICE_NAME, "None");
        this.matchPartial = defaultSharedPreferences.getBoolean(this.PREF_FILTER_CONFIG_DEVICE_NAME_MATCH_PARTIAL, false);
        return true;
    }

    public boolean storeFilterConfig() {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.con).edit();
        edit.putString(this.PREF_FILTER_CONFIG_DEVICE_NAME, this.deviceName);
        edit.putBoolean(this.PREF_FILTER_CONFIG_DEVICE_NAME_MATCH_PARTIAL, this.matchPartial);
        edit.putBoolean(this.PREF_FILTER_CONFIG_ENABLE, this.enabled);
        edit.commit();
        return true;
    }

    public View getConfigView() {
        return this.myView;
    }

    public boolean includeDevice(BluetoothLEDevice bluetoothLEDevice) {
        if (bluetoothLEDevice.f27d.getName() == null) {
            Log.e(this.TAG, "Device has no name !");
            return false;
        }
        if (this.enabled) {
            String str = "No match partial name: ";
            if (this.matchPartial) {
                if (bluetoothLEDevice.f27d.getName().contains(this.deviceName)) {
                    String str2 = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Matched partial name: ");
                    sb.append(bluetoothLEDevice.f27d.getName());
                    Log.e(str2, sb.toString());
                    return true;
                }
                String str3 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(bluetoothLEDevice.f27d.getName());
                Log.e(str3, sb2.toString());
                return false;
            } else if (bluetoothLEDevice.f27d.getName().contentEquals(this.deviceName)) {
                String str4 = this.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Matched entire name: ");
                sb3.append(bluetoothLEDevice.f27d.getName());
                Log.e(str4, sb3.toString());
                return true;
            } else {
                String str5 = this.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(bluetoothLEDevice.f27d.getName());
                Log.e(str5, sb4.toString());
            }
        }
        return false;
    }
}
