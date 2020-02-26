package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;

public final class LongPrefField extends AbstractPrefField<Long> {
    LongPrefField(SharedPreferences sharedPreferences, String str, Long l) {
        super(sharedPreferences, str, l);
    }

    public Long getOr(Long l) {
        try {
            return Long.valueOf(this.sharedPreferences.getLong(this.key, l.longValue()));
        } catch (ClassCastException e) {
            try {
                SharedPreferences sharedPreferences = this.sharedPreferences;
                String str = this.key;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(l);
                return Long.valueOf(Long.parseLong(sharedPreferences.getString(str, sb.toString())));
            } catch (Exception unused) {
                throw e;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void putInternal(Long l) {
        apply(edit().putLong(this.key, l.longValue()));
    }
}
