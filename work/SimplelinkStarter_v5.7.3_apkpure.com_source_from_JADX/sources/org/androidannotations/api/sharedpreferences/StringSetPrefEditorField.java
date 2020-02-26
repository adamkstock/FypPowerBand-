package org.androidannotations.api.sharedpreferences;

import java.util.Set;
import org.androidannotations.api.sharedpreferences.EditorHelper;

public final class StringSetPrefEditorField<T extends EditorHelper<T>> extends AbstractPrefEditorField<T> {
    StringSetPrefEditorField(T t, String str) {
        super(t, str);
    }

    public T put(Set<String> set) {
        SharedPreferencesCompat.putStringSet(this.editorHelper.getEditor(), this.key, set);
        return this.editorHelper;
    }
}
