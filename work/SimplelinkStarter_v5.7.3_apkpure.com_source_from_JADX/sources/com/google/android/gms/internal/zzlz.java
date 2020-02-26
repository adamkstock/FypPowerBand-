package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlb.zzb;

public final class zzlz implements zzly {

    private static class zza extends zzlw {
        private final zzb<Status> zzagy;

        public zza(zzb<Status> zzb) {
            this.zzagy = zzb;
        }

        public void zzbN(int i) throws RemoteException {
            this.zzagy.zzp(new Status(i));
        }
    }

    public PendingResult<Status> zzb(GoogleApiClient googleApiClient) {
        return googleApiClient.zzb(new zza(googleApiClient) {
            /* access modifiers changed from: protected */
            public void zza(zzmb zzmb) throws RemoteException {
                ((zzmd) zzmb.zzpc()).zza(new zza(this));
            }
        });
    }
}
