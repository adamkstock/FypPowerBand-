package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.playlog.internal.LogEvent;
import com.google.android.gms.playlog.internal.PlayLoggerContext;
import com.google.android.gms.playlog.internal.zzd;
import com.google.android.gms.playlog.internal.zzf;

@Deprecated
public class zzqd {
    private final zzf zzaRE;
    private PlayLoggerContext zzaRF;

    public interface zza {
        void zzBr();

        void zzBs();

        void zzf(PendingIntent pendingIntent);
    }

    public zzqd(Context context, int i, String str, String str2, zza zza2, boolean z, String str3) {
        int i2;
        String packageName = context.getPackageName();
        try {
            i2 = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.wtf("PlayLogger", "This can't happen.", e);
            i2 = 0;
        }
        PlayLoggerContext playLoggerContext = new PlayLoggerContext(packageName, i2, i, str, str2, z);
        this.zzaRF = playLoggerContext;
        Looper mainLooper = context.getMainLooper();
        zzd zzd = new zzd(zza2);
        com.google.android.gms.common.internal.zzf zzf = new com.google.android.gms.common.internal.zzf(null, null, null, 49, null, packageName, str3, null);
        Context context2 = context;
        this.zzaRE = new zzf(context, mainLooper, zzd, zzf);
    }

    public void start() {
        this.zzaRE.start();
    }

    public void stop() {
        this.zzaRE.stop();
    }

    public void zza(long j, String str, byte[] bArr, String... strArr) {
        zzf zzf = this.zzaRE;
        PlayLoggerContext playLoggerContext = this.zzaRF;
        LogEvent logEvent = new LogEvent(j, 0, str, bArr, strArr);
        zzf.zzb(playLoggerContext, logEvent);
    }

    public void zzb(String str, byte[] bArr, String... strArr) {
        zza(System.currentTimeMillis(), str, bArr, strArr);
    }
}
