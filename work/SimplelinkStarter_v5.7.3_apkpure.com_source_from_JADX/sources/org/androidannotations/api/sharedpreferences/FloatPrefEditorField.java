package org.androidannotations.api.sharedpreferences;

import org.androidannotations.api.sharedpreferences.EditorHelper;

public final class FloatPrefEditorField<T extends EditorHelper<T>> extends AbstractPrefEditorField<T> {
    FloatPrefEditorField(T t, String str) {
        super(t, str);
    }

    public T put(float f) {
        this.editorHelper.getEditor().putFloat(this.key, f);
        return this.editorHelper;
    }
}
