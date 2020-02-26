package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;

public final class IntPrefField extends AbstractPrefField<Integer> {
    IntPrefField(SharedPreferences sharedPreferences, String str, Integer num) {
        super(sharedPreferences, str, num);
    }

    public Integer getOr(Integer num) {
        try {
            return Integer.valueOf(this.sharedPreferences.getInt(this.key, num.intValue()));
        } catch (ClassCastException e) {
            try {
                SharedPreferences sharedPreferences = this.sharedPreferences;
                String str = this.key;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(num);
                return Integer.valueOf(Integer.parseInt(sharedPreferences.getString(str, sb.toString())));
            } catch (Exception unused) {
                throw e;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void putInternal(Integer num) {
        apply(edit().putInt(this.key, num.intValue()));
    }
}
