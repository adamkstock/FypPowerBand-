package org.androidannotations.api.sharedpreferences;

import org.androidannotations.api.sharedpreferences.EditorHelper;

public final class BooleanPrefEditorField<T extends EditorHelper<T>> extends AbstractPrefEditorField<T> {
    BooleanPrefEditorField(T t, String str) {
        super(t, str);
    }

    public T put(boolean z) {
        this.editorHelper.getEditor().putBoolean(this.key, z);
        return this.editorHelper;
    }
}
