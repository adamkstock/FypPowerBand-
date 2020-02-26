package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.internal.zzqc;

public final class zzo {
    public static final int zzagk = 15;
    private static final String zzagl = null;
    private final String zzagm;
    private final String zzagn;

    public zzo(String str) {
        this(str, zzagl);
    }

    public zzo(String str, String str2) {
        zzx.zzb(str, (Object) "log tag cannot be null");
        zzx.zzb(str.length() <= 23, "tag \"%s\" is longer than the %d character maximum", str, Integer.valueOf(23));
        this.zzagm = str;
        if (str2 == null || str2.length() <= 0) {
            this.zzagn = zzagl;
        } else {
            this.zzagn = str2;
        }
    }

    private String zzcp(String str) {
        String str2 = this.zzagn;
        return str2 == null ? str : str2.concat(str);
    }

    public void zza(Context context, String str, String str2, Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < stackTrace.length && i < 2) {
            sb.append(stackTrace[i].toString());
            sb.append("\n");
            i++;
        }
        zzqc zzqc = new zzqc(context, 10);
        String str3 = "GMS_WTF";
        zzqc.zza(str3, null, str3, sb.toString());
        zzqc.send();
        if (zzbH(7)) {
            Log.e(str, zzcp(str2), th);
            Log.wtf(str, zzcp(str2), th);
        }
    }

    public void zza(String str, String str2, Throwable th) {
        if (zzbH(4)) {
            Log.i(str, zzcp(str2), th);
        }
    }

    public void zzb(String str, String str2, Throwable th) {
        if (zzbH(5)) {
            Log.w(str, zzcp(str2), th);
        }
    }

    public boolean zzbH(int i) {
        return Log.isLoggable(this.zzagm, i);
    }

    public void zzc(String str, String str2, Throwable th) {
        if (zzbH(6)) {
            Log.e(str, zzcp(str2), th);
        }
    }

    public void zzx(String str, String str2) {
        if (zzbH(3)) {
            Log.d(str, zzcp(str2));
        }
    }

    public void zzy(String str, String str2) {
        if (zzbH(5)) {
            Log.w(str, zzcp(str2));
        }
    }

    public void zzz(String str, String str2) {
        if (zzbH(6)) {
            Log.e(str, zzcp(str2));
        }
    }
}
