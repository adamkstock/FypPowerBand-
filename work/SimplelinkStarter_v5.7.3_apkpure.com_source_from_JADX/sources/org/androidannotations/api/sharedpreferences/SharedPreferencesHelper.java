package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;
import java.util.Set;

public abstract class SharedPreferencesHelper {
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences2) {
        this.sharedPreferences = sharedPreferences2;
    }

    public final SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    public final void clear() {
        SharedPreferencesCompat.apply(this.sharedPreferences.edit().clear());
    }

    /* access modifiers changed from: protected */
    public IntPrefField intField(String str, int i) {
        return new IntPrefField(this.sharedPreferences, str, Integer.valueOf(i));
    }

    /* access modifiers changed from: protected */
    public StringPrefField stringField(String str, String str2) {
        return new StringPrefField(this.sharedPreferences, str, str2);
    }

    /* access modifiers changed from: protected */
    public StringSetPrefField stringSetField(String str, Set<String> set) {
        return new StringSetPrefField(this.sharedPreferences, str, set);
    }

    /* access modifiers changed from: protected */
    public BooleanPrefField booleanField(String str, boolean z) {
        return new BooleanPrefField(this.sharedPreferences, str, Boolean.valueOf(z));
    }

    /* access modifiers changed from: protected */
    public FloatPrefField floatField(String str, float f) {
        return new FloatPrefField(this.sharedPreferences, str, Float.valueOf(f));
    }

    /* access modifiers changed from: protected */
    public LongPrefField longField(String str, long j) {
        return new LongPrefField(this.sharedPreferences, str, Long.valueOf(j));
    }
}
