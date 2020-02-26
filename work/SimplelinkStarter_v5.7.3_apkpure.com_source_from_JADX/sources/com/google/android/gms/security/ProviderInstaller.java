package com.google.android.gms.security;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import java.lang.reflect.Method;

public class ProviderInstaller {
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static Method zzaUV = null;
    /* access modifiers changed from: private */
    public static final GoogleApiAvailability zzacJ = GoogleApiAvailability.getInstance();
    private static final Object zzpy = new Object();

    public interface ProviderInstallListener {
        void onProviderInstallFailed(int i, Intent intent);

        void onProviderInstalled();
    }

    public static void installIfNeeded(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        zzx.zzb(context, (Object) "Context must not be null");
        zzacJ.zzab(context);
        Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
        if (remoteContext != null) {
            synchronized (zzpy) {
                try {
                    if (zzaUV == null) {
                        zzaM(remoteContext);
                    }
                    zzaUV.invoke(null, new Object[]{remoteContext});
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to install provider: ");
                    sb.append(e.getMessage());
                    Log.e("ProviderInstaller", sb.toString());
                    throw new GooglePlayServicesNotAvailableException(8);
                } catch (Throwable th) {
                    throw th;
                }
            }
            return;
        }
        Log.e("ProviderInstaller", "Failed to get remote context");
        throw new GooglePlayServicesNotAvailableException(8);
    }

    public static void installIfNeededAsync(final Context context, final ProviderInstallListener providerInstallListener) {
        zzx.zzb(context, (Object) "Context must not be null");
        zzx.zzb(providerInstallListener, (Object) "Listener must not be null");
        zzx.zzci("Must be called on the UI thread");
        new AsyncTask<Void, Void, Integer>() {
            /* access modifiers changed from: protected */
            /* renamed from: zzc */
            public Integer doInBackground(Void... voidArr) {
                int connectionStatusCode;
                try {
                    ProviderInstaller.installIfNeeded(context);
                    connectionStatusCode = 0;
                } catch (GooglePlayServicesRepairableException e) {
                    connectionStatusCode = e.getConnectionStatusCode();
                } catch (GooglePlayServicesNotAvailableException e2) {
                    connectionStatusCode = e2.errorCode;
                }
                return Integer.valueOf(connectionStatusCode);
            }

            /* access modifiers changed from: protected */
            /* renamed from: zze */
            public void onPostExecute(Integer num) {
                if (num.intValue() == 0) {
                    providerInstallListener.onProviderInstalled();
                    return;
                }
                providerInstallListener.onProviderInstallFailed(num.intValue(), ProviderInstaller.zzacJ.zza(context, num.intValue(), "pi"));
            }
        }.execute(new Void[0]);
    }

    private static void zzaM(Context context) throws ClassNotFoundException, NoSuchMethodException {
        zzaUV = context.getClassLoader().loadClass("com.google.android.gms.common.security.ProviderInstallerImpl").getMethod("insertProvider", new Class[]{Context.class});
    }
}
