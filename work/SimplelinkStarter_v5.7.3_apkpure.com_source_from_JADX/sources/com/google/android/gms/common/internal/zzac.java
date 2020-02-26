package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.zzd;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class zzac<T extends IInterface> extends zzj<T> {
    private final zzd<T> zzagt;

    public zzac(Context context, Looper looper, int i, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, zzf zzf, zzd zzd) {
        super(context, looper, i, zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzagt = zzd;
    }

    /* access modifiers changed from: protected */
    public T zzW(IBinder iBinder) {
        return this.zzagt.zzW(iBinder);
    }

    /* access modifiers changed from: protected */
    public void zzc(int i, T t) {
        this.zzagt.zza(i, t);
    }

    /* access modifiers changed from: protected */
    public String zzfK() {
        return this.zzagt.zzfK();
    }

    /* access modifiers changed from: protected */
    public String zzfL() {
        return this.zzagt.zzfL();
    }
}
