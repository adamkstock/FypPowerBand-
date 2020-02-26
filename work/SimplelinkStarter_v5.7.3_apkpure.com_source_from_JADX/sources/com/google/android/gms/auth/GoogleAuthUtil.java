package com.google.android.gms.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import java.io.IOException;
import java.net.URISyntaxException;

public final class GoogleAuthUtil {
    public static final int CHANGE_TYPE_ACCOUNT_ADDED = 1;
    public static final int CHANGE_TYPE_ACCOUNT_REMOVED = 2;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_FROM = 3;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_TO = 4;
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    public static final String KEY_ANDROID_PACKAGE_NAME = "androidPackageName";
    public static final String KEY_CALLER_UID = "callerUid";
    public static final String KEY_REQUEST_ACTIONS = "request_visible_actions";
    @Deprecated
    public static final String KEY_REQUEST_VISIBLE_ACTIVITIES = "request_visible_actions";
    public static final String KEY_SUPPRESS_PROGRESS_SCREEN = "suppressProgressScreen";
    private static final ComponentName zzRw;
    private static final ComponentName zzRx;

    static {
        int i = VERSION.SDK_INT;
        int i2 = VERSION.SDK_INT;
        String str = "com.google.android.gms";
        zzRw = new ComponentName(str, "com.google.android.gms.auth.GetToken");
        zzRx = new ComponentName(str, "com.google.android.gms.recovery.RecoveryService");
    }

    private GoogleAuthUtil() {
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0064 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void clearToken(android.content.Context r4, java.lang.String r5) throws com.google.android.gms.auth.GooglePlayServicesAvailabilityException, com.google.android.gms.auth.GoogleAuthException, java.io.IOException {
        /*
            android.content.Context r0 = r4.getApplicationContext()
            java.lang.String r1 = "Calling this from your main thread can lead to deadlock"
            com.google.android.gms.common.internal.zzx.zzcj(r1)
            zzaa(r0)
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
            android.content.pm.ApplicationInfo r4 = r4.getApplicationInfo()
            java.lang.String r4 = r4.packageName
            java.lang.String r2 = "clientPackageName"
            r1.putString(r2, r4)
            java.lang.String r2 = KEY_ANDROID_PACKAGE_NAME
            boolean r2 = r1.containsKey(r2)
            if (r2 != 0) goto L_0x0029
            java.lang.String r2 = KEY_ANDROID_PACKAGE_NAME
            r1.putString(r2, r4)
        L_0x0029:
            com.google.android.gms.common.zza r4 = new com.google.android.gms.common.zza
            r4.<init>()
            com.google.android.gms.common.internal.zzl r0 = com.google.android.gms.common.internal.zzl.zzal(r0)
            android.content.ComponentName r2 = zzRw
            java.lang.String r3 = "GoogleAuthUtil"
            boolean r2 = r0.zza(r2, r4, r3)
            if (r2 == 0) goto L_0x0080
            android.os.IBinder r2 = r4.zzno()     // Catch:{ RemoteException -> 0x006c, InterruptedException -> 0x0064 }
            com.google.android.gms.internal.zzau r2 = com.google.android.gms.internal.zzau.zza.zza(r2)     // Catch:{ RemoteException -> 0x006c, InterruptedException -> 0x0064 }
            android.os.Bundle r5 = r2.zza(r5, r1)     // Catch:{ RemoteException -> 0x006c, InterruptedException -> 0x0064 }
            java.lang.String r1 = "Error"
            java.lang.String r1 = r5.getString(r1)     // Catch:{ RemoteException -> 0x006c, InterruptedException -> 0x0064 }
            java.lang.String r2 = "booleanResult"
            boolean r5 = r5.getBoolean(r2)     // Catch:{ RemoteException -> 0x006c, InterruptedException -> 0x0064 }
            if (r5 == 0) goto L_0x005c
            android.content.ComponentName r5 = zzRw
            r0.zzb(r5, r4, r3)
            return
        L_0x005c:
            com.google.android.gms.auth.GoogleAuthException r5 = new com.google.android.gms.auth.GoogleAuthException     // Catch:{ RemoteException -> 0x006c, InterruptedException -> 0x0064 }
            r5.<init>(r1)     // Catch:{ RemoteException -> 0x006c, InterruptedException -> 0x0064 }
            throw r5     // Catch:{ RemoteException -> 0x006c, InterruptedException -> 0x0064 }
        L_0x0062:
            r5 = move-exception
            goto L_0x007a
        L_0x0064:
            com.google.android.gms.auth.GoogleAuthException r5 = new com.google.android.gms.auth.GoogleAuthException     // Catch:{ all -> 0x0062 }
            java.lang.String r1 = "Interrupted"
            r5.<init>(r1)     // Catch:{ all -> 0x0062 }
            throw r5     // Catch:{ all -> 0x0062 }
        L_0x006c:
            r5 = move-exception
            java.lang.String r1 = "GMS remote exception "
            android.util.Log.i(r3, r1, r5)     // Catch:{ all -> 0x0062 }
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x0062 }
            java.lang.String r1 = "remote exception"
            r5.<init>(r1)     // Catch:{ all -> 0x0062 }
            throw r5     // Catch:{ all -> 0x0062 }
        L_0x007a:
            android.content.ComponentName r1 = zzRw
            r0.zzb(r1, r4, r3)
            throw r5
        L_0x0080:
            java.io.IOException r4 = new java.io.IOException
            java.lang.String r5 = "Could not bind to service with the given context."
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.GoogleAuthUtil.clearToken(android.content.Context, java.lang.String):void");
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0049 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.google.android.gms.auth.AccountChangeEvent> getAccountChangeEvents(android.content.Context r4, int r5, java.lang.String r6) throws com.google.android.gms.auth.GoogleAuthException, java.io.IOException {
        /*
            java.lang.String r0 = "accountName must be provided"
            com.google.android.gms.common.internal.zzx.zzh(r6, r0)
            java.lang.String r0 = "Calling this from your main thread can lead to deadlock"
            com.google.android.gms.common.internal.zzx.zzcj(r0)
            android.content.Context r4 = r4.getApplicationContext()
            zzaa(r4)
            com.google.android.gms.common.zza r0 = new com.google.android.gms.common.zza
            r0.<init>()
            com.google.android.gms.common.internal.zzl r4 = com.google.android.gms.common.internal.zzl.zzal(r4)
            android.content.ComponentName r1 = zzRw
            java.lang.String r2 = "GoogleAuthUtil"
            boolean r1 = r4.zza(r1, r0, r2)
            if (r1 == 0) goto L_0x0065
            android.os.IBinder r1 = r0.zzno()     // Catch:{ RemoteException -> 0x0051, InterruptedException -> 0x0049 }
            com.google.android.gms.internal.zzau r1 = com.google.android.gms.internal.zzau.zza.zza(r1)     // Catch:{ RemoteException -> 0x0051, InterruptedException -> 0x0049 }
            com.google.android.gms.auth.AccountChangeEventsRequest r3 = new com.google.android.gms.auth.AccountChangeEventsRequest     // Catch:{ RemoteException -> 0x0051, InterruptedException -> 0x0049 }
            r3.<init>()     // Catch:{ RemoteException -> 0x0051, InterruptedException -> 0x0049 }
            com.google.android.gms.auth.AccountChangeEventsRequest r6 = r3.setAccountName(r6)     // Catch:{ RemoteException -> 0x0051, InterruptedException -> 0x0049 }
            com.google.android.gms.auth.AccountChangeEventsRequest r5 = r6.setEventIndex(r5)     // Catch:{ RemoteException -> 0x0051, InterruptedException -> 0x0049 }
            com.google.android.gms.auth.AccountChangeEventsResponse r5 = r1.zza(r5)     // Catch:{ RemoteException -> 0x0051, InterruptedException -> 0x0049 }
            java.util.List r5 = r5.getEvents()     // Catch:{ RemoteException -> 0x0051, InterruptedException -> 0x0049 }
            android.content.ComponentName r6 = zzRw
            r4.zzb(r6, r0, r2)
            return r5
        L_0x0047:
            r5 = move-exception
            goto L_0x005f
        L_0x0049:
            com.google.android.gms.auth.GoogleAuthException r5 = new com.google.android.gms.auth.GoogleAuthException     // Catch:{ all -> 0x0047 }
            java.lang.String r6 = "Interrupted"
            r5.<init>(r6)     // Catch:{ all -> 0x0047 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x0051:
            r5 = move-exception
            java.lang.String r6 = "GMS remote exception "
            android.util.Log.i(r2, r6, r5)     // Catch:{ all -> 0x0047 }
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x0047 }
            java.lang.String r6 = "remote exception"
            r5.<init>(r6)     // Catch:{ all -> 0x0047 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x005f:
            android.content.ComponentName r6 = zzRw
            r4.zzb(r6, r0, r2)
            throw r5
        L_0x0065:
            java.io.IOException r4 = new java.io.IOException
            java.lang.String r5 = "Could not bind to service with the given context."
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.GoogleAuthUtil.getAccountChangeEvents(android.content.Context, int, java.lang.String):java.util.List");
    }

    public static String getAccountId(Context context, String str) throws GoogleAuthException, IOException {
        zzx.zzh(str, "accountName must be provided");
        zzx.zzcj("Calling this from your main thread can lead to deadlock");
        zzaa(context.getApplicationContext());
        return getToken(context, str, "^^_account_id_^^", new Bundle());
    }

    public static String getToken(Context context, Account account, String str) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, account, str, new Bundle());
    }

    public static String getToken(Context context, Account account, String str, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return zza(context, account, str, bundle).getToken();
    }

    @Deprecated
    public static String getToken(Context context, String str, String str2) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2);
    }

    @Deprecated
    public static String getToken(Context context, String str, String str2, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2, bundle);
    }

    public static String getTokenWithNotification(Context context, Account account, String str, Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        return zzb(context, account, str, bundle).getToken();
    }

    public static String getTokenWithNotification(Context context, Account account, String str, Bundle bundle, Intent intent) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        zzi(intent);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putParcelable("callback_intent", intent);
        bundle.putBoolean("handle_notification", true);
        return zzc(context, account, str, bundle).getToken();
    }

    public static String getTokenWithNotification(Context context, Account account, String str, Bundle bundle, String str2, Bundle bundle2) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        if (!TextUtils.isEmpty(str2)) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            if (bundle2 == null) {
                bundle2 = new Bundle();
            }
            ContentResolver.validateSyncExtrasBundle(bundle2);
            bundle.putString("authority", str2);
            bundle.putBundle("sync_extras", bundle2);
            bundle.putBoolean("handle_notification", true);
            return zzc(context, account, str, bundle).getToken();
        }
        throw new IllegalArgumentException("Authority cannot be empty or null.");
    }

    @Deprecated
    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        return getTokenWithNotification(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2, bundle);
    }

    @Deprecated
    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle, Intent intent) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        return getTokenWithNotification(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2, bundle, intent);
    }

    @Deprecated
    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle, String str3, Bundle bundle2) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        return getTokenWithNotification(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2, bundle, str3, bundle2);
    }

    @Deprecated
    public static void invalidateToken(Context context, String str) {
        AccountManager.get(context).invalidateAuthToken(GOOGLE_ACCOUNT_TYPE, str);
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00ad */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.auth.TokenData zza(android.content.Context r3, android.accounts.Account r4, java.lang.String r5, android.os.Bundle r6) throws java.io.IOException, com.google.android.gms.auth.UserRecoverableAuthException, com.google.android.gms.auth.GoogleAuthException {
        /*
            android.content.Context r0 = r3.getApplicationContext()
            java.lang.String r1 = "Calling this from your main thread can lead to deadlock"
            com.google.android.gms.common.internal.zzx.zzcj(r1)
            zzaa(r0)
            if (r6 != 0) goto L_0x0014
            android.os.Bundle r6 = new android.os.Bundle
            r6.<init>()
            goto L_0x001a
        L_0x0014:
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>(r6)
            r6 = r1
        L_0x001a:
            android.content.pm.ApplicationInfo r3 = r3.getApplicationInfo()
            java.lang.String r3 = r3.packageName
            java.lang.String r1 = "clientPackageName"
            r6.putString(r1, r3)
            java.lang.String r1 = KEY_ANDROID_PACKAGE_NAME
            java.lang.String r1 = r6.getString(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x0036
            java.lang.String r1 = KEY_ANDROID_PACKAGE_NAME
            r6.putString(r1, r3)
        L_0x0036:
            long r1 = android.os.SystemClock.elapsedRealtime()
            java.lang.String r3 = "service_connection_start_time_millis"
            r6.putLong(r3, r1)
            com.google.android.gms.common.zza r3 = new com.google.android.gms.common.zza
            r3.<init>()
            com.google.android.gms.common.internal.zzl r0 = com.google.android.gms.common.internal.zzl.zzal(r0)
            android.content.ComponentName r1 = zzRw
            java.lang.String r2 = "GoogleAuthUtil"
            boolean r1 = r0.zza(r1, r3, r2)
            if (r1 == 0) goto L_0x00c9
            android.os.IBinder r1 = r3.zzno()     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            com.google.android.gms.internal.zzau r1 = com.google.android.gms.internal.zzau.zza.zza(r1)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            android.os.Bundle r4 = r1.zza(r4, r5, r6)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            if (r4 == 0) goto L_0x009e
            java.lang.String r5 = "tokenDetails"
            com.google.android.gms.auth.TokenData r5 = com.google.android.gms.auth.TokenData.zza(r4, r5)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            if (r5 == 0) goto L_0x006e
            android.content.ComponentName r4 = zzRw
            r0.zzb(r4, r3, r2)
            return r5
        L_0x006e:
            java.lang.String r5 = "Error"
            java.lang.String r5 = r4.getString(r5)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            java.lang.String r6 = "userRecoveryIntent"
            android.os.Parcelable r4 = r4.getParcelable(r6)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            android.content.Intent r4 = (android.content.Intent) r4     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            com.google.android.gms.auth.firstparty.shared.zzd r6 = com.google.android.gms.auth.firstparty.shared.zzd.zzbE(r5)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            boolean r1 = com.google.android.gms.auth.firstparty.shared.zzd.zza(r6)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            if (r1 != 0) goto L_0x0098
            boolean r4 = com.google.android.gms.auth.firstparty.shared.zzd.zzc(r6)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            if (r4 == 0) goto L_0x0092
            java.io.IOException r4 = new java.io.IOException     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            r4.<init>(r5)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            throw r4     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
        L_0x0092:
            com.google.android.gms.auth.GoogleAuthException r4 = new com.google.android.gms.auth.GoogleAuthException     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            r4.<init>(r5)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            throw r4     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
        L_0x0098:
            com.google.android.gms.auth.UserRecoverableAuthException r6 = new com.google.android.gms.auth.UserRecoverableAuthException     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            r6.<init>(r5, r4)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            throw r6     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
        L_0x009e:
            java.lang.String r4 = "Binder call returned null."
            android.util.Log.w(r2, r4)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            com.google.android.gms.auth.GoogleAuthException r4 = new com.google.android.gms.auth.GoogleAuthException     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            java.lang.String r5 = "ServiceUnavailable"
            r4.<init>(r5)     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
            throw r4     // Catch:{ RemoteException -> 0x00b5, InterruptedException -> 0x00ad }
        L_0x00ab:
            r4 = move-exception
            goto L_0x00c3
        L_0x00ad:
            com.google.android.gms.auth.GoogleAuthException r4 = new com.google.android.gms.auth.GoogleAuthException     // Catch:{ all -> 0x00ab }
            java.lang.String r5 = "Interrupted"
            r4.<init>(r5)     // Catch:{ all -> 0x00ab }
            throw r4     // Catch:{ all -> 0x00ab }
        L_0x00b5:
            r4 = move-exception
            java.lang.String r5 = "GMS remote exception "
            android.util.Log.i(r2, r5, r4)     // Catch:{ all -> 0x00ab }
            java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x00ab }
            java.lang.String r5 = "remote exception"
            r4.<init>(r5)     // Catch:{ all -> 0x00ab }
            throw r4     // Catch:{ all -> 0x00ab }
        L_0x00c3:
            android.content.ComponentName r5 = zzRw
            r0.zzb(r5, r3, r2)
            throw r4
        L_0x00c9:
            java.io.IOException r3 = new java.io.IOException
            java.lang.String r4 = "Could not bind to service with the given context."
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.GoogleAuthUtil.zza(android.content.Context, android.accounts.Account, java.lang.String, android.os.Bundle):com.google.android.gms.auth.TokenData");
    }

    private static void zzaa(Context context) throws GoogleAuthException {
        try {
            GooglePlayServicesUtil.zzaa(context);
        } catch (GooglePlayServicesRepairableException e) {
            throw new GooglePlayServicesAvailabilityException(e.getConnectionStatusCode(), e.getMessage(), e.getIntent());
        } catch (GooglePlayServicesNotAvailableException e2) {
            throw new GoogleAuthException(e2.getMessage());
        }
    }

    public static TokenData zzb(Context context, Account account, String str, Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean("handle_notification", true);
        return zzc(context, account, str, bundle);
    }

    private static TokenData zzc(Context context, Account account, String str, Bundle bundle) throws IOException, GoogleAuthException {
        String str2 = "User intervention required. Notification has been pushed.";
        if (bundle == null) {
            bundle = new Bundle();
        }
        try {
            TokenData zza = zza(context, account, str, bundle);
            GooglePlayServicesUtil.zzac(context);
            return zza;
        } catch (GooglePlayServicesAvailabilityException e) {
            GooglePlayServicesUtil.showErrorNotification(e.getConnectionStatusCode(), context);
            throw new UserRecoverableNotifiedException(str2);
        } catch (UserRecoverableAuthException unused) {
            GooglePlayServicesUtil.zzac(context);
            throw new UserRecoverableNotifiedException(str2);
        }
    }

    private static void zzi(Intent intent) {
        if (intent != null) {
            try {
                Intent.parseUri(intent.toUri(1), 1);
            } catch (URISyntaxException unused) {
                throw new IllegalArgumentException("Parameter callback contains invalid data. It must be serializable using toUri() and parseUri().");
            }
        } else {
            throw new IllegalArgumentException("Callback cannot be null.");
        }
    }
}
