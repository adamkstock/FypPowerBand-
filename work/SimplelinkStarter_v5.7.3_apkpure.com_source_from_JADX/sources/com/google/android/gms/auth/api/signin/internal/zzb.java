package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.auth.api.signin.internal.zze.zza;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;

public class zzb extends zzj<zze> {
    private final GoogleSignInConfig zzTn;

    public zzb(Context context, Looper looper, zzf zzf, GoogleSignInConfig googleSignInConfig, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 91, zzf, connectionCallbacks, onConnectionFailedListener);
        if (googleSignInConfig == null) {
            googleSignInConfig = GoogleSignInConfig.zzTh;
        }
        this.zzTn = googleSignInConfig;
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
