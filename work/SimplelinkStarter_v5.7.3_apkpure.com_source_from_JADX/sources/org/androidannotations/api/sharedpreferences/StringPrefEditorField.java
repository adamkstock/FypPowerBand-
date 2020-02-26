package org.androidannotations.api.sharedpreferences;

import org.androidannotations.api.sharedpreferences.EditorHelper;

public final class StringPrefEditorField<T extends EditorHelper<T>> extends AbstractPrefEditorField<T> {
    StringPrefEditorField(T t, String str) {
        super(t, str);
    }

    public T put(String str) {
        this.editorHelper.getEditor().putString(this.key, str);
        return this.editorHelper;
    }
}
