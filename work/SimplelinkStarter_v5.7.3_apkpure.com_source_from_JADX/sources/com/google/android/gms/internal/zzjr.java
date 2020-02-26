package com.google.android.gms.internal;

import android.os.ParcelFileDescriptor;
import com.google.android.gms.appdatasearch.GetRecentContextCall.Response;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzjq.zza;
import com.google.android.gms.internal.zzlb.zzb;

public abstract class zzjr<T> extends zza {
    protected zzb<T> zzRb;

    public zzjr(zzb<T> zzb) {
        this.zzRb = zzb;
    }

    public void zza(Response response) {
    }

    public void zza(Status status, ParcelFileDescriptor parcelFileDescriptor) {
    }

    public void zza(Status status, boolean z) {
    }

    public void zzc(Status status) {
    }
}
