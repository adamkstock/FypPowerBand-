package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;

public final class BooleanPrefField extends AbstractPrefField<Boolean> {
    BooleanPrefField(SharedPreferences sharedPreferences, String str, Boolean bool) {
        super(sharedPreferences, str, bool);
    }

    public Boolean getOr(Boolean bool) {
        return Boolean.valueOf(this.sharedPreferences.getBoolean(this.key, bool.booleanValue()));
    }

    /* access modifiers changed from: protected */
    public void putInternal(Boolean bool) {
        apply(edit().putBoolean(this.key, bool.booleanValue()));
    }
}
