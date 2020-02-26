package com.p004ti.device_selector;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build.VERSION;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import com.p004ti.ble.sensortag.Sensor;
import com.ti.ble.simplelinkstarter.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* renamed from: com.ti.device_selector.PreferencesListener */
public class PreferencesListener implements OnSharedPreferenceChangeListener {
    private static final int MAX_NOTIFICATIONS = 4;
    private Context context;
    private PreferenceFragment preferenceFragment;
    private SharedPreferences sharedPreferences;

    public PreferencesListener(Context context2, SharedPreferences sharedPreferences2, PreferenceFragment preferenceFragment2) {
        this.context = context2;
        this.sharedPreferences = sharedPreferences2;
        this.preferenceFragment = preferenceFragment2;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences2, String str) {
        if (VERSION.SDK_INT <= 18) {
            if (!(getSensorFromPrefKey(str) == null) && sharedPreferences2.getBoolean(str, true) && enabledSensors().size() > 4) {
                ((CheckBoxPreference) this.preferenceFragment.findPreference(str)).setChecked(false);
                alertNotifyLimitaion();
            }
        }
    }

    private void alertNotifyLimitaion() {
        Builder builder = new Builder(this.context);
        builder.setTitle("Notifications limit");
        builder.setMessage("Android 4.3 BLE allows a maximum of 4 simultaneous notifications.\n");
        builder.setIcon(R.mipmap.bluetooth);
        builder.setNeutralButton(17039370, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    private Sensor getSensorFromPrefKey(String str) {
        try {
            return Sensor.valueOf(str.substring(5, str.length() - 3).toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | NullPointerException unused) {
            return null;
        }
    }

    private List<Sensor> enabledSensors() {
        Sensor[] values;
        ArrayList arrayList = new ArrayList();
        for (Sensor sensor : Sensor.values()) {
            if (isEnabledByPrefs(sensor)) {
                arrayList.add(sensor);
            }
        }
        return arrayList;
    }

    private boolean isEnabledByPrefs(Sensor sensor) {
        StringBuilder sb = new StringBuilder();
        sb.append("pref_");
        sb.append(sensor.name().toLowerCase(Locale.ENGLISH));
        sb.append("_on");
        String sb2 = sb.toString();
        if (this.sharedPreferences.contains(sb2)) {
            return this.sharedPreferences.getBoolean(sb2, true);
        }
        return false;
    }
}
