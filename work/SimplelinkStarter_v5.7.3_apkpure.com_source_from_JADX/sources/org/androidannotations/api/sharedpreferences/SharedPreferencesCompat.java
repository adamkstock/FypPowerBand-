package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public abstract class SharedPreferencesCompat {
    private static final Method APPLY_METHOD = findMethod(Editor.class, "apply", new Class[0]);
    private static final Method GET_STRING_SET_METHOD = findMethod(SharedPreferences.class, "getStringSet", String.class, Set.class);
    private static final Method PUT_STRING_SET_METHOD = findMethod(Editor.class, "putStringSet", String.class, Set.class);

    private SharedPreferencesCompat() {
    }

    public static void apply(Editor editor) {
        try {
            invoke(APPLY_METHOD, editor, new Object[0]);
        } catch (NoSuchMethodException unused) {
            editor.commit();
        }
    }

    public static Set<String> getStringSet(SharedPreferences sharedPreferences, String str, Set<String> set) {
        try {
            return (Set) invoke(GET_STRING_SET_METHOD, sharedPreferences, str, set);
        } catch (NoSuchMethodException unused) {
            String string = sharedPreferences.getString(str, null);
            if (string == null) {
                return set;
            }
            return SetXmlSerializer.deserialize(string);
        }
    }

    public static void putStringSet(Editor editor, String str, Set<String> set) {
        try {
            invoke(PUT_STRING_SET_METHOD, editor, str, set);
        } catch (NoSuchMethodException unused) {
            editor.putString(str, SetXmlSerializer.serialize(set));
        }
    }

    private static Method findMethod(Class<?> cls, String str, Class<?>... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    public static <T> T invoke(Method method, Object obj, Object... objArr) throws NoSuchMethodException {
        if (method != null) {
            try {
                return method.invoke(obj, objArr);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException unused) {
                throw new NoSuchMethodException(method.getName());
            }
        } else {
            throw new NoSuchMethodException();
        }
    }
}
