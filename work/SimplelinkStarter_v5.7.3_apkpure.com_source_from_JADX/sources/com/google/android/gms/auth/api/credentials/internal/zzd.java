package com.google.android.gms.auth.api.credentials.internal;

import android.content.Context;
import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlb.zza;

abstract class zzd<R extends Result> extends zza<R, zze> {
    zzd(GoogleApiClient googleApiClient) {
        super(Auth.zzRF, googleApiClient);
    }

    /* access modifiers changed from: protected */
    public abstract void zza(Context context, zzh zzh) throws DeadObjectException, RemoteException;

    /* access modifiers changed from: protected */
    public final void zza(zze zze) throws DeadObjectException, RemoteException {
        zza(zze.getContext(), (zzh) zze.zzpc());
    }
}
