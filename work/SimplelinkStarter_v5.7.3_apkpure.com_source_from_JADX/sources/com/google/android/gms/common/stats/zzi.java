package com.google.android.gms.common.stats;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.stats.zzc.zzb;
import com.google.android.gms.internal.zzmm;
import com.google.android.gms.internal.zzmr;
import java.util.List;

public class zzi {
    private static String TAG = "WakeLockTracker";
    private static Integer zzahE;
    private static zzi zzaii = new zzi();

    private static int getLogLevel() {
        try {
            return zzmm.zzjA() ? ((Integer) zzb.zzahH.get()).intValue() : zzd.LOG_LEVEL_OFF;
        } catch (SecurityException unused) {
            return zzd.LOG_LEVEL_OFF;
        }
    }

    private static boolean zzam(Context context) {
        if (zzahE == null) {
            zzahE = Integer.valueOf(getLogLevel());
        }
        return zzahE.intValue() != zzd.LOG_LEVEL_OFF;
    }

    public static zzi zzqr() {
        return zzaii;
    }

    public void zza(Context context, String str, int i, String str2, String str3, int i2, List<String> list) {
        zza(context, str, i, str2, str3, i2, list, 0);
    }

    public void zza(Context context, String str, int i, String str2, String str3, int i2, List<String> list, long j) {
        int i3 = i;
        if (zzam(context)) {
            if (TextUtils.isEmpty(str)) {
                String str4 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("missing wakeLock key. ");
                sb.append(str);
                Log.e(str4, sb.toString());
                return;
            }
            String str5 = str;
            long currentTimeMillis = System.currentTimeMillis();
            if (7 == i3 || 8 == i3 || 10 == i3 || 11 == i3) {
                WakeLockEvent wakeLockEvent = r1;
                WakeLockEvent wakeLockEvent2 = new WakeLockEvent(currentTimeMillis, i, str2, i2, list, str, SystemClock.elapsedRealtime(), zzmr.zzao(context), str3, context.getPackageName(), zzmr.zzap(context), j);
                try {
                    context.startService(new Intent().setComponent(zzd.zzahN).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", wakeLockEvent));
                } catch (Exception e) {
                    Log.wtf(TAG, e);
                }
            }
        }
    }
}
