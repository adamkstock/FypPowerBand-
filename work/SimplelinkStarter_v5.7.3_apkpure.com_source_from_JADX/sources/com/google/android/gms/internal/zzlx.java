package com.google.android.gms.internal;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;

public final class zzlx {
    public static final Api<NoOptions> API = new Api<>("Common.API", zzRl, zzRk);
    public static final zzc<zzmb> zzRk = new zzc<>();
    private static final zza<zzmb, NoOptions> zzRl = new zza<zzmb, NoOptions>() {
        /* renamed from: zze */
        public zzmb zza(Context context, Looper looper, zzf zzf, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            zzmb zzmb = new zzmb(context, looper, zzf, connectionCallbacks, onConnectionFailedListener);
            return zzmb;
        }
    };
    public static final zzly zzagw = new zzlz();
}
