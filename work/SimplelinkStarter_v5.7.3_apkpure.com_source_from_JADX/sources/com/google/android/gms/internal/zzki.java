package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.Auth.zza;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;

public final class zzki extends zzj<zzkk> {
    private final Bundle zzSa;

    public zzki(Context context, Looper looper, zzf zzf, zza zza, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 16, zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzSa = zza == null ? new Bundle() : zza.zzlE();
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzau */
    public zzkk zzW(IBinder iBinder) {
        return zzkk.zza.zzaw(iBinder);
    }

    /* access modifiers changed from: protected */
    public String zzfK() {
        return "com.google.android.gms.auth.service.START";
    }

    /* access modifiers changed from: protected */
    public String zzfL() {
        return "com.google.android.gms.auth.api.internal.IAuthService";
    }

    public boolean zzlN() {
        zzf zzpa = zzpa();
        return !TextUtils.isEmpty(zzpa.getAccountName()) && !zzpa.zzb(Auth.PROXY_API).isEmpty();
    }

    /* access modifiers changed from: protected */
    public Bundle zzly() {
        return this.zzSa;
    }
}
