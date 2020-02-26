package com.google.android.gms.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller.SessionInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri.Builder;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserManager;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentActivity;
import android.support.p000v4.app.NotificationCompat;
import android.support.p000v4.app.NotificationCompatExtras;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.gms.C0310R;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzml;
import com.google.android.gms.internal.zzmx;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GooglePlayServicesUtil {
    public static final String GMS_ERROR_DIALOG = "GooglePlayServicesErrorDialog";
    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = zzns();
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    public static boolean zzaal = false;
    public static boolean zzaam = false;
    private static int zzaan = -1;
    private static String zzaao = null;
    private static Integer zzaap = null;
    static final AtomicBoolean zzaaq = new AtomicBoolean();
    private static final AtomicBoolean zzaar = new AtomicBoolean();
    private static final Object zzpy = new Object();

    private static class zza extends Handler {
        private final Context zzqZ;

        zza(Context context) {
            super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
            this.zzqZ = context.getApplicationContext();
        }

        public void handleMessage(Message message) {
            if (message.what != 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("Don't know how to handle this message: ");
                sb.append(message.what);
                Log.w("GooglePlayServicesUtil", sb.toString());
                return;
            }
            int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.zzqZ);
            if (GooglePlayServicesUtil.isUserRecoverableError(isGooglePlayServicesAvailable)) {
                GooglePlayServicesUtil.zza(isGooglePlayServicesAvailable, this.zzqZ);
            }
        }
    }

    private GooglePlayServicesUtil() {
    }

    @Deprecated
    public static Dialog getErrorDialog(int i, Activity activity, int i2) {
        return getErrorDialog(i, activity, i2, null);
    }

    @Deprecated
    public static Dialog getErrorDialog(int i, Activity activity, int i2, OnCancelListener onCancelListener) {
        return zza(i, activity, null, i2, onCancelListener);
    }

    @Deprecated
    public static PendingIntent getErrorPendingIntent(int i, Context context, int i2) {
        return GoogleApiAvailability.getInstance().getErrorResolutionPendingIntent(context, i, i2);
    }

    @Deprecated
    public static String getErrorString(int i) {
        return ConnectionResult.getStatusString(i);
    }

    @Deprecated
    public static String getOpenSourceSoftwareLicenseInfo(Context context) {
        InputStream openInputStream;
        try {
            openInputStream = context.getContentResolver().openInputStream(new Builder().scheme("android.resource").authority("com.google.android.gms").appendPath("raw").appendPath("oss_notice").build());
            String next = new Scanner(openInputStream).useDelimiter("\\A").next();
            if (openInputStream != null) {
                openInputStream.close();
            }
            return next;
        } catch (NoSuchElementException unused) {
            if (openInputStream != null) {
                openInputStream.close();
            }
            return null;
        } catch (Exception unused2) {
            return null;
        } catch (Throwable th) {
            if (openInputStream != null) {
                openInputStream.close();
            }
            throw th;
        }
    }

    public static Context getRemoteContext(Context context) {
        try {
            return context.createPackageContext("com.google.android.gms", 3);
        } catch (NameNotFoundException unused) {
            return null;
        }
    }

    public static Resources getRemoteResource(Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.google.android.gms");
        } catch (NameNotFoundException unused) {
            return null;
        }
    }

    @Deprecated
    public static int isGooglePlayServicesAvailable(Context context) {
        String str = "GooglePlayServicesUtil";
        if (zzd.zzaeK) {
            return 0;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            context.getResources().getString(C0310R.string.common_google_play_services_unknown_issue);
        } catch (Throwable unused) {
            Log.e(str, "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
        }
        String str2 = "com.google.android.gms";
        if (!str2.equals(context.getPackageName())) {
            zzad(context);
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str2, 64);
            zzd zznu = zzd.zznu();
            String str3 = "Google Play services signature invalid.";
            if (!zzml.zzcb(packageInfo.versionCode) && !zzml.zzan(context)) {
                try {
                    zza zza2 = zznu.zza(packageManager.getPackageInfo(GOOGLE_PLAY_STORE_PACKAGE, 8256), zzbz.zzaak);
                    if (zza2 == null) {
                        Log.w(str, "Google Play Store signature invalid.");
                        return 9;
                    }
                    if (zznu.zza(packageInfo, zza2) == null) {
                        Log.w(str, str3);
                        return 9;
                    }
                } catch (NameNotFoundException unused2) {
                    Log.w(str, "Google Play Store is neither installed nor updating.");
                    return 9;
                }
            } else if (zznu.zza(packageInfo, zzbz.zzaak) == null) {
                Log.w(str, str3);
                return 9;
            }
            if (zzml.zzca(packageInfo.versionCode) < zzml.zzca(GOOGLE_PLAY_SERVICES_VERSION_CODE)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Google Play services out of date.  Requires ");
                sb.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
                sb.append(" but found ");
                sb.append(packageInfo.versionCode);
                Log.w(str, sb.toString());
                return 2;
            }
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if (applicationInfo == null) {
                try {
                    applicationInfo = packageManager.getApplicationInfo(str2, 0);
                } catch (NameNotFoundException e) {
                    Log.wtf(str, "Google Play services missing when getting application info.", e);
                    return 1;
                }
            }
            return !applicationInfo.enabled ? 3 : 0;
        } catch (NameNotFoundException unused3) {
            Log.w(str, "Google Play services is missing.");
            return 1;
        }
    }

    @Deprecated
    public static boolean isUserRecoverableError(int i) {
        return i == 1 || i == 2 || i == 3 || i == 9;
    }

    @Deprecated
    public static boolean showErrorDialogFragment(int i, Activity activity, int i2) {
        return showErrorDialogFragment(i, activity, i2, null);
    }

    @Deprecated
    public static boolean showErrorDialogFragment(int i, Activity activity, int i2, OnCancelListener onCancelListener) {
        return showErrorDialogFragment(i, activity, null, i2, onCancelListener);
    }

    public static boolean showErrorDialogFragment(int i, Activity activity, Fragment fragment, int i2, OnCancelListener onCancelListener) {
        Dialog zza2 = zza(i, activity, fragment, i2, onCancelListener);
        if (zza2 == null) {
            return false;
        }
        zza(activity, onCancelListener, GMS_ERROR_DIALOG, zza2);
        return true;
    }

    @Deprecated
    public static void showErrorNotification(int i, Context context) {
        if (zzml.zzan(context) && i == 2) {
            i = 42;
        }
        if (zzd(context, i) || zzf(context, i)) {
            zzae(context);
        } else {
            zza(i, context);
        }
    }

    private static Dialog zza(int i, Activity activity, Fragment fragment, int i2, OnCancelListener onCancelListener) {
        AlertDialog.Builder builder = null;
        if (i == 0) {
            return null;
        }
        if (zzml.zzan(activity) && i == 2) {
            i = 42;
        }
        if (zzmx.zzqx()) {
            TypedValue typedValue = new TypedValue();
            activity.getTheme().resolveAttribute(16843529, typedValue, true);
            if ("Theme.Dialog.Alert".equals(activity.getResources().getResourceEntryName(typedValue.resourceId))) {
                builder = new AlertDialog.Builder(activity, 5);
            }
        }
        if (builder == null) {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setMessage(zzg.zzc(activity, i, zzaf(activity)));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        Intent zza2 = GoogleApiAvailability.getInstance().zza(activity, i, "d");
        zzh zzh = fragment == null ? new zzh(activity, zza2, i2) : new zzh(fragment, zza2, i2);
        String zzh2 = zzg.zzh(activity, i);
        if (zzh2 != null) {
            builder.setPositiveButton(zzh2, zzh);
        }
        String zzg = zzg.zzg(activity, i);
        if (zzg != null) {
            builder.setTitle(zzg);
        }
        return builder.create();
    }

    /* access modifiers changed from: private */
    public static void zza(int i, Context context) {
        zza(i, context, null);
    }

    private static void zza(int i, Context context, String str) {
        Notification notification;
        int i2;
        Resources resources = context.getResources();
        String zzaf = zzaf(context);
        String zzi = zzg.zzi(context, i);
        if (zzi == null) {
            zzi = resources.getString(C0310R.string.common_google_play_services_notification_ticker);
        }
        String zzd = zzg.zzd(context, i, zzaf);
        PendingIntent zza2 = GoogleApiAvailability.getInstance().zza(context, i, 0, "n");
        if (zzml.zzan(context)) {
            zzx.zzZ(zzmx.zzqy());
            Notification.Builder autoCancel = new Notification.Builder(context).setSmallIcon(C0310R.C0311drawable.common_ic_googleplayservices).setPriority(2).setAutoCancel(true);
            BigTextStyle bigTextStyle = new BigTextStyle();
            StringBuilder sb = new StringBuilder();
            sb.append(zzi);
            sb.append(" ");
            sb.append(zzd);
            notification = autoCancel.setStyle(bigTextStyle.bigText(sb.toString())).addAction(C0310R.C0311drawable.common_full_open_on_phone, resources.getString(C0310R.string.common_open_on_phone), zza2).build();
        } else {
            String string = resources.getString(C0310R.string.common_google_play_services_notification_ticker);
            if (zzmx.zzqu()) {
                Notification.Builder autoCancel2 = new Notification.Builder(context).setSmallIcon(17301642).setContentTitle(zzi).setContentText(zzd).setContentIntent(zza2).setTicker(string).setAutoCancel(true);
                if (zzmx.zzqC()) {
                    autoCancel2.setLocalOnly(true);
                }
                if (zzmx.zzqy()) {
                    autoCancel2.setStyle(new BigTextStyle().bigText(zzd));
                    notification = autoCancel2.build();
                } else {
                    notification = autoCancel2.getNotification();
                }
                if (VERSION.SDK_INT == 19) {
                    notification.extras.putBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY, true);
                }
            } else {
                notification = new NotificationCompat.Builder(context).setSmallIcon(17301642).setTicker(string).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(zza2).setContentTitle(zzi).setContentText(zzd).build();
            }
        }
        if (zzbk(i)) {
            i2 = 10436;
            zzaaq.set(false);
        } else {
            i2 = 39789;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (str != null) {
            notificationManager.notify(str, i2, notification);
        } else {
            notificationManager.notify(i2, notification);
        }
    }

    public static void zza(Activity activity, OnCancelListener onCancelListener, String str, Dialog dialog) {
        boolean z;
        try {
            z = activity instanceof FragmentActivity;
        } catch (NoClassDefFoundError unused) {
            z = false;
        }
        if (z) {
            SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show(((FragmentActivity) activity).getSupportFragmentManager(), str);
        } else if (zzmx.zzqu()) {
            ErrorDialogFragment.newInstance(dialog, onCancelListener).show(activity.getFragmentManager(), str);
        } else {
            throw new RuntimeException("This Activity does not support Fragments.");
        }
    }

    @Deprecated
    public static void zzaa(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        int isGooglePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (isGooglePlayServicesAvailable != 0) {
            Intent zza2 = GoogleApiAvailability.getInstance().zza(context, isGooglePlayServicesAvailable, "e");
            StringBuilder sb = new StringBuilder();
            sb.append("GooglePlayServices not available due to error ");
            sb.append(isGooglePlayServicesAvailable);
            Log.e("GooglePlayServicesUtil", sb.toString());
            if (zza2 == null) {
                throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
            }
            throw new GooglePlayServicesRepairableException(isGooglePlayServicesAvailable, "Google Play Services not available", zza2);
        }
    }

    @Deprecated
    public static void zzac(Context context) {
        if (!zzaaq.getAndSet(true)) {
            try {
                ((NotificationManager) context.getSystemService("notification")).cancel(10436);
            } catch (SecurityException unused) {
            }
        }
    }

    private static void zzad(Context context) {
        Integer num;
        if (!zzaar.get()) {
            synchronized (zzpy) {
                if (zzaao == null) {
                    zzaao = context.getPackageName();
                    try {
                        Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                        if (bundle != null) {
                            zzaap = Integer.valueOf(bundle.getInt("com.google.android.gms.version"));
                        } else {
                            zzaap = null;
                        }
                    } catch (NameNotFoundException e) {
                        Log.wtf("GooglePlayServicesUtil", "This should never happen.", e);
                    }
                } else if (!zzaao.equals(context.getPackageName())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("isGooglePlayServicesAvailable should only be called with Context from your application's package. A previous call used package '");
                    sb.append(zzaao);
                    sb.append("' and this call used package '");
                    sb.append(context.getPackageName());
                    sb.append("'.");
                    throw new IllegalArgumentException(sb.toString());
                }
                num = zzaap;
            }
            if (num == null) {
                throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
            } else if (num.intValue() != GOOGLE_PLAY_SERVICES_VERSION_CODE) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected ");
                sb2.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
                sb2.append(" but");
                sb2.append(" found ");
                sb2.append(num);
                sb2.append(".  You must have the");
                sb2.append(" following declaration within the <application> element: ");
                sb2.append("    <meta-data android:name=\"");
                sb2.append("com.google.android.gms.version");
                sb2.append("\" android:value=\"@integer/google_play_services_version\" />");
                throw new IllegalStateException(sb2.toString());
            }
        }
    }

    private static void zzae(Context context) {
        zza zza2 = new zza(context);
        zza2.sendMessageDelayed(zza2.obtainMessage(1), 120000);
    }

    public static String zzaf(Context context) {
        ApplicationInfo applicationInfo;
        String str = context.getApplicationInfo().name;
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException unused) {
            applicationInfo = null;
        }
        return applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo).toString() : packageName;
    }

    public static boolean zzag(Context context) {
        return zzmx.zzqD() && context.getPackageManager().hasSystemFeature("com.google.sidewinder");
    }

    public static boolean zzah(Context context) {
        if (zzmx.zzqA()) {
            Bundle applicationRestrictions = ((UserManager) context.getSystemService("user")).getApplicationRestrictions(context.getPackageName());
            if (applicationRestrictions != null) {
                if ("true".equals(applicationRestrictions.getString("restricted_profile"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean zzb(Context context, int i, String str) {
        if (zzmx.zzqB()) {
            try {
                ((AppOpsManager) context.getSystemService("appops")).checkPackage(i, str);
                return true;
            } catch (SecurityException unused) {
                return false;
            }
        } else {
            String[] packagesForUid = context.getPackageManager().getPackagesForUid(i);
            if (!(str == null || packagesForUid == null)) {
                for (String equals : packagesForUid) {
                    if (str.equals(equals)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:12|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        zzaan = 0;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x002a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean zzb(android.content.pm.PackageManager r6) {
        /*
            java.lang.Object r0 = zzpy
            monitor-enter(r0)
            int r1 = zzaan     // Catch:{ all -> 0x0034 }
            r2 = -1
            r3 = 1
            r4 = 0
            if (r1 != r2) goto L_0x002c
            java.lang.String r1 = "com.google.android.gms"
            r2 = 64
            android.content.pm.PackageInfo r6 = r6.getPackageInfo(r1, r2)     // Catch:{ NameNotFoundException -> 0x002a }
            com.google.android.gms.common.zzd r1 = com.google.android.gms.common.zzd.zznu()     // Catch:{ NameNotFoundException -> 0x002a }
            com.google.android.gms.common.zzc$zza[] r2 = new com.google.android.gms.common.zzc.zza[r3]     // Catch:{ NameNotFoundException -> 0x002a }
            com.google.android.gms.common.zzc$zza[] r5 = com.google.android.gms.common.zzc.zzaad     // Catch:{ NameNotFoundException -> 0x002a }
            r5 = r5[r3]     // Catch:{ NameNotFoundException -> 0x002a }
            r2[r4] = r5     // Catch:{ NameNotFoundException -> 0x002a }
            com.google.android.gms.common.zzc$zza r6 = r1.zza(r6, r2)     // Catch:{ NameNotFoundException -> 0x002a }
            if (r6 == 0) goto L_0x0027
            zzaan = r3     // Catch:{ NameNotFoundException -> 0x002a }
            goto L_0x002c
        L_0x0027:
            zzaan = r4     // Catch:{ NameNotFoundException -> 0x002a }
            goto L_0x002c
        L_0x002a:
            zzaan = r4     // Catch:{ all -> 0x0034 }
        L_0x002c:
            int r6 = zzaan     // Catch:{ all -> 0x0034 }
            if (r6 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r3 = 0
        L_0x0032:
            monitor-exit(r0)     // Catch:{ all -> 0x0034 }
            return r3
        L_0x0034:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0034 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtil.zzb(android.content.pm.PackageManager):boolean");
    }

    @Deprecated
    public static boolean zzb(PackageManager packageManager, String str) {
        return zzd.zznu().zzb(packageManager, str);
    }

    @Deprecated
    public static Intent zzbj(int i) {
        return GoogleApiAvailability.getInstance().zza(null, i, null);
    }

    private static boolean zzbk(int i) {
        return i == 1 || i == 2 || i == 3 || i == 18 || i == 42;
    }

    public static boolean zzc(PackageManager packageManager) {
        return zzb(packageManager) || !zznt();
    }

    @Deprecated
    public static boolean zzd(Context context, int i) {
        if (i == 18) {
            return true;
        }
        if (i == 1) {
            return zzj(context, "com.google.android.gms");
        }
        return false;
    }

    public static boolean zze(Context context, int i) {
        String str = "com.google.android.gms";
        return zzb(context, i, str) && zzb(context.getPackageManager(), str);
    }

    @Deprecated
    public static boolean zzf(Context context, int i) {
        if (i == 9) {
            return zzj(context, GOOGLE_PLAY_STORE_PACKAGE);
        }
        return false;
    }

    static boolean zzj(Context context, String str) {
        if (zzmx.zzqD()) {
            for (SessionInfo appPackageName : context.getPackageManager().getPackageInstaller().getAllSessions()) {
                if (str.equals(appPackageName.getAppPackageName())) {
                    return true;
                }
            }
        }
        if (zzah(context)) {
            return false;
        }
        try {
            return context.getPackageManager().getApplicationInfo(str, 8192).enabled;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }

    private static int zzns() {
        return 8115000;
    }

    public static boolean zznt() {
        if (zzaal) {
            return zzaam;
        }
        return "user".equals(Build.TYPE);
    }
}
