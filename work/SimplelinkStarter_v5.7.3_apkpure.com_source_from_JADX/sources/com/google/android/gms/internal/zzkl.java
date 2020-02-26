package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlb.zza;

abstract class zzkl extends zza<ProxyResult, zzki> {
    public zzkl(GoogleApiClient googleApiClient) {
        super(Auth.zzRE, googleApiClient);
    }

    /* access modifiers changed from: protected */
    public abstract void zza(Context context, zzkk zzkk) throws RemoteException;

    /* access modifiers changed from: protected */
    public final void zza(zzki zzki) throws RemoteException {
        zza(zzki.getContext(), (zzkk) zzki.zzpc());
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzj */
    public ProxyResult zzb(Status status) {
        return new zzkn(status);
    }
}
