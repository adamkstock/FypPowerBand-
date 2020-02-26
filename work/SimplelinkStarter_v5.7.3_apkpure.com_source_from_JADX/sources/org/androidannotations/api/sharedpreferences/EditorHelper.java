package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.androidannotations.api.sharedpreferences.EditorHelper;

public abstract class EditorHelper<T extends EditorHelper<T>> {
    private final Editor editor;

    private T cast() {
        return this;
    }

    public EditorHelper(SharedPreferences sharedPreferences) {
        this.editor = sharedPreferences.edit();
    }

    /* access modifiers changed from: protected */
    public Editor getEditor() {
        return this.editor;
    }

    public final T clear() {
        this.editor.clear();
        return cast();
    }

    public final void apply() {
        SharedPreferencesCompat.apply(this.editor);
    }

    /* access modifiers changed from: protected */
    public IntPrefEditorField<T> intField(String str) {
        return new IntPrefEditorField<>(cast(), str);
    }

    /* access modifiers changed from: protected */
    public StringPrefEditorField<T> stringField(String str) {
        return new StringPrefEditorField<>(cast(), str);
    }

    /* access modifiers changed from: protected */
    public StringSetPrefEditorField<T> stringSetField(String str) {
        return new StringSetPrefEditorField<>(cast(), str);
    }

    /* access modifiers changed from: protected */
    public BooleanPrefEditorField<T> booleanField(String str) {
        return new BooleanPrefEditorField<>(cast(), str);
    }

    /* access modifiers changed from: protected */
    public FloatPrefEditorField<T> floatField(String str) {
        return new FloatPrefEditorField<>(cast(), str);
    }

    /* access modifiers changed from: protected */
    public LongPrefEditorField<T> longField(String str) {
        return new LongPrefEditorField<>(cast(), str);
    }
}
