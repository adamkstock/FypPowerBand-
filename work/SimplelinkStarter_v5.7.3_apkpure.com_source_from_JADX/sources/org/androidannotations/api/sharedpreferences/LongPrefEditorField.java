package org.androidannotations.api.sharedpreferences;

import org.androidannotations.api.sharedpreferences.EditorHelper;

public final class LongPrefEditorField<T extends EditorHelper<T>> extends AbstractPrefEditorField<T> {
    LongPrefEditorField(T t, String str) {
        super(t, str);
    }

    public T put(long j) {
        this.editorHelper.getEditor().putLong(this.key, j);
        return this.editorHelper;
    }
}
