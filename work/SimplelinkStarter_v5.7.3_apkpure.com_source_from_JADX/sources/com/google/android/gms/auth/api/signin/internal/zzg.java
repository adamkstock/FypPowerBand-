package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.internal.zze.zza;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzx;

public class zzg extends zzj<zze> {
    private final com.google.android.gms.auth.api.signin.zzg zzTq;

    public zzg(Context context, Looper looper, zzf zzf, com.google.android.gms.auth.api.signin.zzg zzg, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 87, zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzTq = (com.google.android.gms.auth.api.signin.zzg) zzx.zzw(zzg);
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzax */
    public zze zzW(IBinder iBinder) {
        return zza.zzaz(iBinder);
    }

    /* access modifiers changed from: protected */
    public String zzfK() {
        return "com.google.android.gms.auth.api.signin.service.START";
    }

    /* access modifiers changed from: protected */
    public String zzfL() {
        return "com.google.android.gms.auth.api.signin.internal.ISignInService";
    }
}
