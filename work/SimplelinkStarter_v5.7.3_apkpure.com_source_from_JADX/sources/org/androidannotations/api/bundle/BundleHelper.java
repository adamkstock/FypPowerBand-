package org.androidannotations.api.bundle;

import android.os.Bundle;
import android.os.Parcelable;
import java.lang.reflect.Array;

public final class BundleHelper {
    private BundleHelper() {
    }

    public static <T extends Parcelable> T[] getParcelableArray(Bundle bundle, String str, Class<T[]> cls) {
        Parcelable[] parcelableArray = bundle.getParcelableArray(str);
        if (parcelableArray == null) {
            return null;
        }
        Object newInstance = Array.newInstance(cls.getComponentType(), parcelableArray.length);
        System.arraycopy(parcelableArray, 0, newInstance, 0, parcelableArray.length);
        return (Parcelable[]) newInstance;
    }
}
