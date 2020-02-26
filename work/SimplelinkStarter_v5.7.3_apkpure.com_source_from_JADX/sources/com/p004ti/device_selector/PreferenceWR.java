package com.p004ti.device_selector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

/* renamed from: com.ti.device_selector.PreferenceWR */
public class PreferenceWR {
    public static final String PREFERENCEWR_NEEDS_REFRESH = "refresh";
    private String prefix;
    private SharedPreferences sharedPreferences;

    public PreferenceWR(String str, Context context) {
        this.prefix = str;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setBooleanPreference("Exists", true);
        StringBuilder sb = new StringBuilder();
        sb.append("Instantiated a new preference reader/writer with prefix : \"");
        sb.append(this.prefix);
        sb.append("_\"");
        Log.d("PreferenceWR", sb.toString());
    }

    public static boolean isKnown(String str, Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("_Exists");
        return defaultSharedPreferences.getBoolean(sb.toString(), false);
    }

    public String getStringPreference(String str) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefix);
        sb.append("_");
        sb.append(str);
        return sharedPreferences2.getString(sb.toString(), "");
    }

    public boolean setStringPreference(String str, String str2) {
        Editor edit = this.sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefix);
        sb.append("_");
        sb.append(str);
        edit.putString(sb.toString(), str2);
        return edit.commit();
    }

    public boolean getBooleanPreference(String str) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefix);
        sb.append("_");
        sb.append(str);
        return sharedPreferences2.getBoolean(sb.toString(), false);
    }

    public boolean setBooleanPreference(String str, boolean z) {
        Editor edit = this.sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefix);
        sb.append("_");
        sb.append(str);
        edit.putBoolean(sb.toString(), z);
        return edit.commit();
    }

    public int getIntegerPreference(String str) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefix);
        sb.append("_");
        sb.append(str);
        return sharedPreferences2.getInt(sb.toString(), -1);
    }

    public boolean setIntegerPreference(String str, int i) {
        Editor edit = this.sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefix);
        sb.append("_");
        sb.append(str);
        edit.putInt(sb.toString(), i);
        return edit.commit();
    }
}
