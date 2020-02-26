package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.WorkSource;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class zznc {
    private static final Method zzaip = zzqG();
    private static final Method zzaiq = zzqH();
    private static final Method zzair = zzqI();
    private static final Method zzais = zzqJ();
    private static final Method zzait = zzqK();

    public static int zza(WorkSource workSource) {
        Method method = zzair;
        if (method != null) {
            try {
                return ((Integer) method.invoke(workSource, new Object[0])).intValue();
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            }
        }
        return 0;
    }

    public static String zza(WorkSource workSource, int i) {
        Method method = zzait;
        if (method != null) {
            try {
                return (String) method.invoke(workSource, new Object[]{Integer.valueOf(i)});
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            }
        }
        return null;
    }

    public static void zza(WorkSource workSource, int i, String str) {
        String str2 = "Unable to assign blame through WorkSource";
        String str3 = "WorkSourceUtil";
        if (zzaiq != null) {
            if (str == null) {
                str = "";
            }
            try {
                zzaiq.invoke(workSource, new Object[]{Integer.valueOf(i), str});
            } catch (Exception e) {
                Log.wtf(str3, str2, e);
            }
            return;
        }
        Method method = zzaip;
        if (method != null) {
            try {
                method.invoke(workSource, new Object[]{Integer.valueOf(i)});
            } catch (Exception e2) {
                Log.wtf(str3, str2, e2);
            }
        }
    }

    public static boolean zzar(Context context) {
        return context.getPackageManager().checkPermission("android.permission.UPDATE_DEVICE_STATS", context.getPackageName()) == 0;
    }

    public static List<String> zzb(WorkSource workSource) {
        int zza = workSource == null ? 0 : zza(workSource);
        if (zza == 0) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < zza; i++) {
            String zza2 = zza(workSource, i);
            if (!zznb.zzcA(zza2)) {
                arrayList.add(zza2);
            }
        }
        return arrayList;
    }

    public static WorkSource zzf(int i, String str) {
        WorkSource workSource = new WorkSource();
        zza(workSource, i, str);
        return workSource;
    }

    public static WorkSource zzm(Context context, String str) {
        StringBuilder sb;
        String str2;
        String str3 = "WorkSourceUtil";
        if (context == null || context.getPackageManager() == null) {
            return null;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 0);
            if (applicationInfo != null) {
                return zzf(applicationInfo.uid, str);
            }
            sb = new StringBuilder();
            str2 = "Could not get applicationInfo from package: ";
            sb.append(str2);
            sb.append(str);
            Log.e(str3, sb.toString());
            return null;
        } catch (NameNotFoundException unused) {
            sb = new StringBuilder();
            str2 = "Could not find package: ";
        }
    }

    private static Method zzqG() {
        try {
            return WorkSource.class.getMethod("add", new Class[]{Integer.TYPE});
        } catch (Exception unused) {
            return null;
        }
    }

    private static Method zzqH() {
        if (zzmx.zzqA()) {
            try {
                return WorkSource.class.getMethod("add", new Class[]{Integer.TYPE, String.class});
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private static Method zzqI() {
        try {
            return WorkSource.class.getMethod("size", new Class[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    private static Method zzqJ() {
        try {
            return WorkSource.class.getMethod("get", new Class[]{Integer.TYPE});
        } catch (Exception unused) {
            return null;
        }
    }

    private static Method zzqK() {
        if (zzmx.zzqA()) {
            try {
                return WorkSource.class.getMethod("getName", new Class[]{Integer.TYPE});
            } catch (Exception unused) {
            }
        }
        return null;
    }
}
