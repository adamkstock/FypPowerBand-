package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;

public abstract class zzg<T> {
    private final String zzapA;
    private T zzapB;

    public static class zza extends Exception {
        public zza(String str) {
            super(str);
        }

        public zza(String str, Throwable th) {
            super(str, th);
        }
    }

    protected zzg(String str) {
        this.zzapA = str;
    }

    /* access modifiers changed from: protected */
    public final T zzas(Context context) throws zza {
        if (this.zzapB == null) {
            zzx.zzw(context);
            Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
            if (remoteContext != null) {
                try {
                    this.zzapB = zzd((IBinder) remoteContext.getClassLoader().loadClass(this.zzapA).newInstance());
                } catch (ClassNotFoundException e) {
                    throw new zza("Could not load creator class.", e);
                } catch (InstantiationException e2) {
                    throw new zza("Could not instantiate creator.", e2);
                } catch (IllegalAccessException e3) {
                    throw new zza("Could not access creator.", e3);
                }
            } else {
                throw new zza("Could not get remote context.");
            }
        }
        return this.zzapB;
    }

    /* access modifiers changed from: protected */
    public abstract T zzd(IBinder iBinder);
}
