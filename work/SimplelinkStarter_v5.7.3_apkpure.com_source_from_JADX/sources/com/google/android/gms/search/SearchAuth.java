package com.google.android.gms.search;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.internal.zzqs;
import com.google.android.gms.internal.zzqt;

public class SearchAuth {
    public static final Api<NoOptions> API = new Api<>("SearchAuth.API", zzaUP, zzRk);
    public static final SearchAuthApi SearchAuthApi = new zzqt();
    public static final zzc<zzqs> zzRk = new zzc<>();
    private static final zza<zzqs, NoOptions> zzaUP = new zza<zzqs, NoOptions>() {
        /* renamed from: zzs */
        public zzqs zza(Context context, Looper looper, zzf zzf, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzqs(context, connectionCallbacks, onConnectionFailedListener, zzf);
        }
    };

    public static class StatusCodes {
        public static final int AUTH_DISABLED = 10000;
        public static final int AUTH_THROTTLED = 10001;
        public static final int DEVELOPER_ERROR = 10;
        public static final int INTERNAL_ERROR = 8;
        public static final int SUCCESS = 0;
    }

    private SearchAuth() {
    }
}
