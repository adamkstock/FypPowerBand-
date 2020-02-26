package com.google.android.gms.playlog.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzb;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzse;
import com.google.android.gms.playlog.internal.zza.C1144zza;
import com.google.android.gms.playlog.internal.zzb.zza;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class zzf extends zzj<zza> {
    private final String zzQe;
    private final zzd zzaRZ;
    private final zzb zzaSa = new zzb();
    private boolean zzaSb = true;
    private final Object zzpd = new Object();

    public zzf(Context context, Looper looper, zzd zzd, com.google.android.gms.common.internal.zzf zzf) {
        super(context, looper, 24, zzf, zzd, zzd);
        this.zzQe = context.getPackageName();
        this.zzaRZ = (zzd) zzx.zzw(zzd);
        this.zzaRZ.zza(this);
    }

    private void zzBv() {
        zzb.zzZ(!this.zzaSb);
        if (!this.zzaSa.isEmpty()) {
            PlayLoggerContext playLoggerContext = null;
            try {
                ArrayList arrayList = new ArrayList();
                Iterator it = this.zzaSa.zzBt().iterator();
                while (it.hasNext()) {
                    zza zza = (zza) it.next();
                    if (zza.zzaRO != null) {
                        ((zza) zzpc()).zza(this.zzQe, zza.zzaRM, zzse.zzf(zza.zzaRO));
                    } else {
                        if (!zza.zzaRM.equals(playLoggerContext)) {
                            if (!arrayList.isEmpty()) {
                                ((zza) zzpc()).zza(this.zzQe, playLoggerContext, (List<LogEvent>) arrayList);
                                arrayList.clear();
                            }
                            playLoggerContext = zza.zzaRM;
                        }
                        arrayList.add(zza.zzaRN);
                    }
                }
                if (!arrayList.isEmpty()) {
                    ((zza) zzpc()).zza(this.zzQe, playLoggerContext, (List<LogEvent>) arrayList);
                }
                this.zzaSa.clear();
            } catch (RemoteException unused) {
                Log.e("PlayLoggerImpl", "Couldn't send cached log events to AndroidLog service.  Retaining in memory cache.");
            }
        }
    }

    private void zzc(PlayLoggerContext playLoggerContext, LogEvent logEvent) {
        this.zzaSa.zza(playLoggerContext, logEvent);
    }

    private void zzd(PlayLoggerContext playLoggerContext, LogEvent logEvent) {
        String str;
        String str2 = "PlayLoggerImpl";
        try {
            zzBv();
            ((zza) zzpc()).zza(this.zzQe, playLoggerContext, logEvent);
            return;
        } catch (RemoteException unused) {
            str = "Couldn't send log event.  Will try caching.";
        } catch (IllegalStateException unused2) {
            str = "Service was disconnected.  Will try caching.";
        }
        Log.e(str2, str);
        zzc(playLoggerContext, logEvent);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void start() {
        /*
            r3 = this;
            java.lang.Object r0 = r3.zzpd
            monitor-enter(r0)
            boolean r1 = r3.isConnecting()     // Catch:{ all -> 0x001d }
            if (r1 != 0) goto L_0x001b
            boolean r1 = r3.isConnected()     // Catch:{ all -> 0x001d }
            if (r1 == 0) goto L_0x0010
            goto L_0x001b
        L_0x0010:
            com.google.android.gms.playlog.internal.zzd r1 = r3.zzaRZ     // Catch:{ all -> 0x001d }
            r2 = 1
            r1.zzao(r2)     // Catch:{ all -> 0x001d }
            r3.zzoZ()     // Catch:{ all -> 0x001d }
            monitor-exit(r0)     // Catch:{ all -> 0x001d }
            return
        L_0x001b:
            monitor-exit(r0)     // Catch:{ all -> 0x001d }
            return
        L_0x001d:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.playlog.internal.zzf.start():void");
    }

    public void stop() {
        synchronized (this.zzpd) {
            this.zzaRZ.zzao(false);
            disconnect();
        }
    }

    /* access modifiers changed from: 0000 */
    public void zzap(boolean z) {
        synchronized (this.zzpd) {
            boolean z2 = this.zzaSb;
            this.zzaSb = z;
            if (z2 && !this.zzaSb) {
                zzBv();
            }
        }
    }

    public void zzb(PlayLoggerContext playLoggerContext, LogEvent logEvent) {
        synchronized (this.zzpd) {
            if (this.zzaSb) {
                zzc(playLoggerContext, logEvent);
            } else {
                zzd(playLoggerContext, logEvent);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzdA */
    public zza zzW(IBinder iBinder) {
        return C1144zza.zzdz(iBinder);
    }

    /* access modifiers changed from: protected */
    public String zzfK() {
        return "com.google.android.gms.playlog.service.START";
    }

    /* access modifiers changed from: protected */
    public String zzfL() {
        return "com.google.android.gms.playlog.internal.IPlayLogService";
    }
}
