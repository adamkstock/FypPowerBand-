package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;

public final class FloatPrefField extends AbstractPrefField<Float> {
    FloatPrefField(SharedPreferences sharedPreferences, String str, Float f) {
        super(sharedPreferences, str, f);
    }

    public Float getOr(Float f) {
        try {
            return Float.valueOf(this.sharedPreferences.getFloat(this.key, f.floatValue()));
        } catch (ClassCastException e) {
            try {
                SharedPreferences sharedPreferences = this.sharedPreferences;
                String str = this.key;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(f);
                return Float.valueOf(Float.parseFloat(sharedPreferences.getString(str, sb.toString())));
            } catch (Exception unused) {
                throw e;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void putInternal(Float f) {
        apply(edit().putFloat(this.key, f.floatValue()));
    }
}
