package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.internal.zzmd.zza;

public class zzmb extends zzj<zzmd> {
    public zzmb(Context context, Looper looper, zzf zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 39, zzf, connectionCallbacks, onConnectionFailedListener);
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzaO */
    public zzmd zzW(IBinder iBinder) {
        return zza.zzaQ(iBinder);
    }

    public String zzfK() {
        return "com.google.android.gms.common.service.START";
    }

    /* access modifiers changed from: protected */
    public String zzfL() {
        return "com.google.android.gms.common.internal.service.ICommonService";
    }
}
