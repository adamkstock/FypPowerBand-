package com.google.android.gms.auth.api.credentials.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.auth.api.Auth.AuthCredentialsOptions;
import com.google.android.gms.auth.api.credentials.internal.zzh.zza;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;

public final class zze extends zzj<zzh> {
    private final AuthCredentialsOptions zzSJ;

    public zze(Context context, Looper looper, zzf zzf, AuthCredentialsOptions authCredentialsOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 68, zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzSJ = authCredentialsOptions;
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzar */
    public zzh zzW(IBinder iBinder) {
        return zza.zzat(iBinder);
    }

    /* access modifiers changed from: protected */
    public String zzfK() {
        return "com.google.android.gms.auth.api.credentials.service.START";
    }

    /* access modifiers changed from: protected */
    public String zzfL() {
        return "com.google.android.gms.auth.api.credentials.internal.ICredentialsService";
    }

    /* access modifiers changed from: protected */
    public Bundle zzly() {
        AuthCredentialsOptions authCredentialsOptions = this.zzSJ;
        return authCredentialsOptions == null ? new Bundle() : authCredentialsOptions.zzly();
    }
}
