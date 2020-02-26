package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public abstract class AbstractPrefField<T> {
    protected final T defaultValue;
    protected final String key;
    protected final SharedPreferences sharedPreferences;

    public abstract T getOr(T t);

    /* access modifiers changed from: protected */
    public abstract void putInternal(T t);

    public AbstractPrefField(SharedPreferences sharedPreferences2, String str, T t) {
        this.sharedPreferences = sharedPreferences2;
        this.key = str;
        this.defaultValue = t;
    }

    public final boolean exists() {
        return this.sharedPreferences.contains(this.key);
    }

    public String key() {
        return this.key;
    }

    public final T get() {
        return getOr(this.defaultValue);
    }

    public final void put(T t) {
        if (t == null) {
            t = this.defaultValue;
        }
        putInternal(t);
    }

    public final void remove() {
        apply(edit().remove(this.key));
    }

    /* access modifiers changed from: protected */
    public Editor edit() {
        return this.sharedPreferences.edit();
    }

    /* access modifiers changed from: protected */
    public final void apply(Editor editor) {
        SharedPreferencesCompat.apply(editor);
    }
}
