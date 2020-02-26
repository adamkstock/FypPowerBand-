package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.internal.zzqr.zza;

public class zzqs extends zzj<zzqr> {
    public zzqs(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, zzf zzf) {
        super(context, context.getMainLooper(), 73, zzf, connectionCallbacks, onConnectionFailedListener);
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzdL */
    public zzqr zzW(IBinder iBinder) {
        return zza.zzdK(iBinder);
    }

    /* access modifiers changed from: protected */
    public String zzfK() {
        return "com.google.android.gms.search.service.SEARCH_AUTH_START";
    }

    /* access modifiers changed from: protected */
    public String zzfL() {
        return "com.google.android.gms.search.internal.ISearchAuthService";
    }
}
