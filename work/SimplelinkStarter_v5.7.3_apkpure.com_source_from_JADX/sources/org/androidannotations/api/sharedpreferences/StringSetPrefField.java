package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Set;

public final class StringSetPrefField extends AbstractPrefField<Set<String>> {
    StringSetPrefField(SharedPreferences sharedPreferences, String str, Set<String> set) {
        super(sharedPreferences, str, set);
    }

    public Set<String> getOr(Set<String> set) {
        return SharedPreferencesCompat.getStringSet(this.sharedPreferences, this.key, set);
    }

    /* access modifiers changed from: protected */
    public void putInternal(Set<String> set) {
        Editor edit = this.sharedPreferences.edit();
        SharedPreferencesCompat.putStringSet(edit, this.key, set);
        apply(edit);
    }
}
