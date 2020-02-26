package com.p004ti.device_selector;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import com.p004ti.ble.sensortag.Sensor;
import com.ti.ble.simplelinkstarter.R;
import java.util.Locale;

/* renamed from: com.ti.device_selector.PreferencesFragment */
public class PreferencesFragment extends PreferenceFragment {
    private static final String TAG = "PreferencesFragment";
    private PreferencesListener preferencesListener;

    public void onCreate(Bundle bundle) {
        Log.i(TAG, "created");
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.preferencesListener = new PreferencesListener(getActivity(), defaultSharedPreferences, this);
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this.preferencesListener);
    }

    public boolean isEnabledByPrefs(Sensor sensor) {
        StringBuilder sb = new StringBuilder();
        sb.append("pref_");
        sb.append(sensor.name().toLowerCase(Locale.ENGLISH));
        sb.append("_on");
        String sb2 = sb.toString();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!defaultSharedPreferences.contains(sb2)) {
            return false;
        }
        return defaultSharedPreferences.getBoolean(sb2, true);
    }
}
