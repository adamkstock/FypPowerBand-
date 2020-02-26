package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzlc;
import com.google.android.gms.internal.zzln;
import com.google.android.gms.internal.zzlo;

public final class PendingResults {

    private static final class zza<R extends Result> extends zzlc<R> {
        private final R zzaaW;

        public zza(R r) {
            super(Looper.getMainLooper());
            this.zzaaW = r;
        }

        /* access modifiers changed from: protected */
        public R zzb(Status status) {
            if (status.getStatusCode() == this.zzaaW.getStatus().getStatusCode()) {
                return this.zzaaW;
            }
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    private static final class zzb<R extends Result> extends zzlc<R> {
        private final R zzaaX;

        public zzb(GoogleApiClient googleApiClient, R r) {
            super(googleApiClient);
            this.zzaaX = r;
        }

        /* access modifiers changed from: protected */
        public R zzb(Status status) {
            return this.zzaaX;
        }
    }

    private static final class zzc<R extends Result> extends zzlc<R> {
        public zzc(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        /* access modifiers changed from: protected */
        public R zzb(Status status) {
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    private PendingResults() {
    }

    public static PendingResult<Status> canceledPendingResult() {
        zzlo zzlo = new zzlo(Looper.getMainLooper());
        zzlo.cancel();
        return zzlo;
    }

    public static <R extends Result> PendingResult<R> canceledPendingResult(R r) {
        zzx.zzb(r, (Object) "Result must not be null");
        zzx.zzb(r.getStatus().getStatusCode() == 16, (Object) "Status code must be CommonStatusCodes.CANCELED");
        zza zza2 = new zza(r);
        zza2.cancel();
        return zza2;
    }

    public static <R extends Result> OptionalPendingResult<R> immediatePendingResult(R r) {
        zzx.zzb(r, (Object) "Result must not be null");
        zzc zzc2 = new zzc(null);
        zzc2.zzb(r);
        return new zzln(zzc2);
    }

    public static PendingResult<Status> immediatePendingResult(Status status) {
        zzx.zzb(status, (Object) "Result must not be null");
        zzlo zzlo = new zzlo(Looper.getMainLooper());
        zzlo.zzb(status);
        return zzlo;
    }

    public static <R extends Result> PendingResult<R> zza(R r, GoogleApiClient googleApiClient) {
        zzx.zzb(r, (Object) "Result must not be null");
        zzx.zzb(!r.getStatus().isSuccess(), (Object) "Status code must not be SUCCESS");
        zzb zzb2 = new zzb(googleApiClient, r);
        zzb2.zzb(r);
        return zzb2;
    }

    public static PendingResult<Status> zza(Status status, GoogleApiClient googleApiClient) {
        zzx.zzb(status, (Object) "Result must not be null");
        zzlo zzlo = new zzlo(googleApiClient);
        zzlo.zzb(status);
        return zzlo;
    }
}
