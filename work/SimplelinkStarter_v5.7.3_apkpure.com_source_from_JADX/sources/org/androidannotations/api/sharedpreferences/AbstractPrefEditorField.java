package org.androidannotations.api.sharedpreferences;

import org.androidannotations.api.sharedpreferences.EditorHelper;

public abstract class AbstractPrefEditorField<T extends EditorHelper<T>> {
    protected final T editorHelper;
    protected final String key;

    public AbstractPrefEditorField(T t, String str) {
        this.editorHelper = t;
        this.key = str;
    }

    public final T remove() {
        this.editorHelper.getEditor().remove(this.key);
        return this.editorHelper;
    }
}
