package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;

public final class StringPrefField extends AbstractPrefField<String> {
    StringPrefField(SharedPreferences sharedPreferences, String str, String str2) {
        super(sharedPreferences, str, str2);
    }

    public String getOr(String str) {
        return this.sharedPreferences.getString(this.key, str);
    }

    /* access modifiers changed from: protected */
    public void putInternal(String str) {
        apply(edit().putString(this.key, str));
    }
}
