package com.google.android.gms.appdatasearch;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.internal.zzjs;
import com.google.android.gms.internal.zzju;

public final class zza {
    public static final zzc<zzjs> zzPT = new zzc<>();
    private static final com.google.android.gms.common.api.Api.zza<zzjs, NoOptions> zzPU = new com.google.android.gms.common.api.Api.zza<zzjs, NoOptions>() {
        public zzjs zza(Context context, Looper looper, zzf zzf, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            zzjs zzjs = new zzjs(context, looper, zzf, connectionCallbacks, onConnectionFailedListener);
            return zzjs;
        }
    };
    public static final Api<NoOptions> zzPV = new Api<>("AppDataSearch.LIGHTWEIGHT_API", zzPU, zzPT);
    public static final zzk zzPW = new zzju();
}
