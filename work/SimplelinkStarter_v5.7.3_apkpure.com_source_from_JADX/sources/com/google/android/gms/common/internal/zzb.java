package com.google.android.gms.common.internal;

import android.os.Looper;
import android.util.Log;

public final class zzb {
    public static void zzZ(boolean z) {
        if (!z) {
            throw new IllegalStateException();
        }
    }

    public static void zza(boolean z, Object obj) {
        if (!z) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }

    public static void zzci(String str) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            StringBuilder sb = new StringBuilder();
            sb.append("checkMainThread: current thread ");
            sb.append(Thread.currentThread());
            sb.append(" IS NOT the main thread ");
            sb.append(Looper.getMainLooper().getThread());
            sb.append("!");
            Log.e("Asserts", sb.toString());
            throw new IllegalStateException(str);
        }
    }

    public static void zzcj(String str) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            StringBuilder sb = new StringBuilder();
            sb.append("checkNotMainThread: current thread ");
            sb.append(Thread.currentThread());
            sb.append(" IS the main thread ");
            sb.append(Looper.getMainLooper().getThread());
            sb.append("!");
            Log.e("Asserts", sb.toString());
            throw new IllegalStateException(str);
        }
    }

    public static void zzs(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("null reference");
        }
    }
}
