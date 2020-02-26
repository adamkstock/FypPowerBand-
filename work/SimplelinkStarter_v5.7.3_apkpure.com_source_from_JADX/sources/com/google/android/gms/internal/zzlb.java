package com.google.android.gms.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzx;
import java.util.concurrent.atomic.AtomicReference;

public class zzlb {

    public static abstract class zza<R extends Result, A extends com.google.android.gms.common.api.Api.zzb> extends zzlc<R> implements zzb<R>, zzf<A> {
        private final zzc<A> zzZM;
        private AtomicReference<zze> zzabg = new AtomicReference<>();

        protected zza(zzc<A> zzc, GoogleApiClient googleApiClient) {
            super(((GoogleApiClient) zzx.zzb(googleApiClient, (Object) "GoogleApiClient must not be null")).getLooper());
            this.zzZM = (zzc) zzx.zzw(zzc);
        }

        private void zza(RemoteException remoteException) {
            zzv(new Status(8, remoteException.getLocalizedMessage(), null));
        }

        /* access modifiers changed from: protected */
        public abstract void zza(A a) throws RemoteException;

        public void zza(zze zze) {
            this.zzabg.set(zze);
        }

        public final void zzb(A a) throws DeadObjectException {
            try {
                zza(a);
            } catch (DeadObjectException e) {
                zza((RemoteException) e);
                throw e;
            } catch (RemoteException e2) {
                zza(e2);
            }
        }

        public void zznJ() {
            setResultCallback(null);
        }

        public int zznK() {
            return 0;
        }

        /* access modifiers changed from: protected */
        public void zznL() {
            zze zze = (zze) this.zzabg.getAndSet(null);
            if (zze != null) {
                zze.zzc(this);
            }
        }

        public final zzc<A> zznx() {
            return this.zzZM;
        }

        public /* synthetic */ void zzp(Object obj) {
            super.zzb((Result) obj);
        }

        public final void zzv(Status status) {
            zzx.zzb(!status.isSuccess(), (Object) "Failed result must not be success");
            zzb(zzb(status));
        }
    }

    public interface zzb<R> {
        void zzp(R r);

        void zzv(Status status);
    }
}
