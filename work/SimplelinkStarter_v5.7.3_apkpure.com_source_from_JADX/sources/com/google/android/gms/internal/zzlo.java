package com.google.android.gms.internal;

import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

public class zzlo extends zzlc<Status> {
    @Deprecated
    public zzlo(Looper looper) {
        super(looper);
    }

    public zzlo(GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzd */
    public Status zzb(Status status) {
        return status;
    }
}
