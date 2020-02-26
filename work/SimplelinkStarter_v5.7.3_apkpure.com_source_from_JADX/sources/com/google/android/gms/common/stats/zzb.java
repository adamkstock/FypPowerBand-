package com.google.android.gms.common.stats;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Debug;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.stats.zzc.zza;
import com.google.android.gms.internal.zzmm;
import com.google.android.gms.internal.zzmy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class zzb {
    private static final Object zzafW = new Object();
    private static Integer zzahE;
    private static zzb zzahy;
    private final List<String> zzahA;
    private final List<String> zzahB;
    private final List<String> zzahC;
    private zze zzahD;
    private zze zzahF;
    private final List<String> zzahz;

    private zzb() {
        if (getLogLevel() == zzd.LOG_LEVEL_OFF) {
            this.zzahz = Collections.EMPTY_LIST;
            this.zzahA = Collections.EMPTY_LIST;
            this.zzahB = Collections.EMPTY_LIST;
            this.zzahC = Collections.EMPTY_LIST;
            return;
        }
        String str = (String) zza.zzahI.get();
        String str2 = ",";
        this.zzahz = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(str2));
        String str3 = (String) zza.zzahJ.get();
        this.zzahA = str3 == null ? Collections.EMPTY_LIST : Arrays.asList(str3.split(str2));
        String str4 = (String) zza.zzahK.get();
        this.zzahB = str4 == null ? Collections.EMPTY_LIST : Arrays.asList(str4.split(str2));
        String str5 = (String) zza.zzahL.get();
        this.zzahC = str5 == null ? Collections.EMPTY_LIST : Arrays.asList(str5.split(str2));
        this.zzahD = new zze(1024, ((Long) zza.zzahM.get()).longValue());
        this.zzahF = new zze(1024, ((Long) zza.zzahM.get()).longValue());
    }

    private static int getLogLevel() {
        if (zzahE == null) {
            try {
                zzahE = Integer.valueOf(zzmm.zzjA() ? ((Integer) zza.zzahH.get()).intValue() : zzd.LOG_LEVEL_OFF);
            } catch (SecurityException unused) {
                zzahE = Integer.valueOf(zzd.LOG_LEVEL_OFF);
            }
        }
        return zzahE.intValue();
    }

    private void zza(Context context, String str, int i, String str2, String str3, String str4, String str5) {
        int i2 = i;
        long currentTimeMillis = System.currentTimeMillis();
        String zzl = ((getLogLevel() & zzd.zzahR) == 0 || i2 == 13) ? null : zzmy.zzl(3, 5);
        long j = 0;
        if ((getLogLevel() & zzd.zzahT) != 0) {
            j = Debug.getNativeHeapAllocatedSize();
        }
        long j2 = j;
        context.startService(new Intent().setComponent(zzd.zzahN).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", (i2 == 1 || i2 == 4 || i2 == 14) ? new ConnectionEvent(currentTimeMillis, i, null, null, null, null, zzl, str, SystemClock.elapsedRealtime(), j2) : new ConnectionEvent(currentTimeMillis, i, str2, str3, str4, str5, zzl, str, SystemClock.elapsedRealtime(), j2)));
    }

    private void zza(Context context, String str, String str2, Intent intent, int i) {
        String str3;
        String str4;
        String str5;
        if (zzqi() && this.zzahD != null) {
            if (i != 4 && i != 1) {
                ServiceInfo zzd = zzd(context, intent);
                if (zzd == null) {
                    Log.w("ConnectionTracker", String.format("Client %s made an invalid request %s", new Object[]{str2, intent.toUri(0)}));
                    return;
                }
                String str6 = zzd.processName;
                String str7 = zzd.name;
                String zzaq = zzmy.zzaq(context);
                if (zzb(zzaq, str2, str6, str7)) {
                    this.zzahD.zzcx(str);
                    str4 = str6;
                    str5 = zzaq;
                    str3 = str7;
                } else {
                    return;
                }
            } else if (this.zzahD.zzcy(str)) {
                str5 = null;
                str4 = null;
                str3 = null;
            } else {
                return;
            }
            zza(context, str, i, str5, str2, str4, str3);
        }
    }

    private String zzb(ServiceConnection serviceConnection) {
        return String.valueOf((((long) Process.myPid()) << 32) | ((long) System.identityHashCode(serviceConnection)));
    }

    private boolean zzb(String str, String str2, String str3, String str4) {
        return !this.zzahz.contains(str) && !this.zzahA.contains(str2) && !this.zzahB.contains(str3) && !this.zzahC.contains(str4) && (!str3.equals(str) || (zzd.zzahS & getLogLevel()) == 0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0014, code lost:
        if ("com.google.android.gms".equals(r4.getPackageName()) != false) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean zzc(android.content.Context r3, android.content.Intent r4) {
        /*
            r2 = this;
            android.content.ComponentName r4 = r4.getComponent()
            if (r4 == 0) goto L_0x0020
            boolean r0 = com.google.android.gms.common.internal.zzd.zzaeK
            if (r0 == 0) goto L_0x0017
            java.lang.String r0 = r4.getPackageName()
            java.lang.String r1 = "com.google.android.gms"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0017
            goto L_0x0020
        L_0x0017:
            java.lang.String r4 = r4.getPackageName()
            boolean r3 = com.google.android.gms.internal.zzmm.zzl(r3, r4)
            return r3
        L_0x0020:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.stats.zzb.zzc(android.content.Context, android.content.Intent):boolean");
    }

    private static ServiceInfo zzd(Context context, Intent intent) {
        String format;
        List queryIntentServices = context.getPackageManager().queryIntentServices(intent, 128);
        String str = "ConnectionTracker";
        if (queryIntentServices == null || queryIntentServices.size() == 0) {
            format = String.format("There are no handler of this intent: %s\n Stack trace: %s", new Object[]{intent.toUri(0), zzmy.zzl(3, 20)});
        } else {
            if (queryIntentServices.size() > 1) {
                Log.w(str, String.format("Multiple handlers found for this intent: %s\n Stack trace: %s", new Object[]{intent.toUri(0), zzmy.zzl(3, 20)}));
                Iterator it = queryIntentServices.iterator();
                if (it.hasNext()) {
                    format = ((ResolveInfo) it.next()).serviceInfo.name;
                }
            }
            return ((ResolveInfo) queryIntentServices.get(0)).serviceInfo;
        }
        Log.w(str, format);
        return null;
    }

    public static zzb zzqh() {
        synchronized (zzafW) {
            if (zzahy == null) {
                zzahy = new zzb();
            }
        }
        return zzahy;
    }

    private boolean zzqi() {
        return zzd.zzaeK && getLogLevel() != zzd.LOG_LEVEL_OFF;
    }

    public void zza(Context context, ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
        zza(context, zzb(serviceConnection), (String) null, (Intent) null, 1);
    }

    public void zza(Context context, ServiceConnection serviceConnection, String str, Intent intent) {
        zza(context, zzb(serviceConnection), str, intent, 3);
    }

    public boolean zza(Context context, Intent intent, ServiceConnection serviceConnection, int i) {
        return zza(context, context.getClass().getName(), intent, serviceConnection, i);
    }

    public boolean zza(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i) {
        if (zzc(context, intent)) {
            Log.w("ConnectionTracker", "Attempted to bind to a service in a STOPPED package.");
            return false;
        }
        boolean bindService = context.bindService(intent, serviceConnection, i);
        if (bindService) {
            zza(context, zzb(serviceConnection), str, intent, 2);
        }
        return bindService;
    }

    public void zzb(Context context, ServiceConnection serviceConnection) {
        zza(context, zzb(serviceConnection), (String) null, (Intent) null, 4);
    }
}
