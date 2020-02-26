package com.google.android.gms.internal;

import android.os.SystemClock;

public final class zzmp implements zzmn {
    private static zzmp zzaik;

    public static synchronized zzmn zzqt() {
        zzmp zzmp;
        synchronized (zzmp.class) {
            if (zzaik == null) {
                zzaik = new zzmp();
            }
            zzmp = zzaik;
        }
        return zzmp;
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    public long nanoTime() {
        return System.nanoTime();
    }
}
