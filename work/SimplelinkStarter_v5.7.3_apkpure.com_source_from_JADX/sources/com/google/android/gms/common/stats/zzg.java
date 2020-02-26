package com.google.android.gms.common.stats;

import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.text.TextUtils;

public class zzg {
    public static String zza(WakeLock wakeLock, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf((((long) Process.myPid()) << 32) | ((long) System.identityHashCode(wakeLock))));
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        sb.append(str);
        return sb.toString();
    }
}
