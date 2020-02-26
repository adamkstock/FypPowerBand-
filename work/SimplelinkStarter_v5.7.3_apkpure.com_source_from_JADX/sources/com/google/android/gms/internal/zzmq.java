package com.google.android.gms.internal;

import android.content.res.Configuration;
import android.content.res.Resources;

public final class zzmq {
    public static boolean zzb(Resources resources) {
        boolean z = false;
        if (resources == null) {
            return false;
        }
        boolean z2 = (resources.getConfiguration().screenLayout & 15) > 3;
        if ((zzmx.zzqu() && z2) || zzc(resources)) {
            z = true;
        }
        return z;
    }

    private static boolean zzc(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        return zzmx.zzqw() && (configuration.screenLayout & 15) <= 3 && configuration.smallestScreenWidthDp >= 600;
    }
}
