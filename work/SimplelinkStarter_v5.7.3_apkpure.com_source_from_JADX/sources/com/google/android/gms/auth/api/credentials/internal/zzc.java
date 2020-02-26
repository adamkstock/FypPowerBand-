package com.google.android.gms.auth.api.credentials.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlb.zzb;

public final class zzc implements CredentialsApi {

    private static class zza extends zza {
        private zzb<Status> zzSI;

        zza(zzb<Status> zzb) {
            this.zzSI = zzb;
        }

        public void zzg(Status status) {
            this.zzSI.zzp(status);
        }
    }

    public PendingResult<Status> delete(GoogleApiClient googleApiClient, final Credential credential) {
        return googleApiClient.zzb(new zzd<Status>(googleApiClient) {
            /* access modifiers changed from: protected */
            public void zza(Context context, zzh zzh) throws RemoteException {
                zzh.zza((zzg) new zza(this), new DeleteRequest(credential));
            }

            /* access modifiers changed from: protected */
            /* renamed from: zzd */
            public Status zzb(Status status) {
                return status;
            }
        });
    }

    public PendingResult<Status> disableAutoSignIn(GoogleApiClient googleApiClient) {
        return googleApiClient.zzb(new zzd<Status>(googleApiClient) {
            /* access modifiers changed from: protected */
            public void zza(Context context, zzh zzh) throws RemoteException {
                zzh.zza(new zza(this));
            }

            /* access modifiers changed from: protected */
            /* renamed from: zzd */
            public Status zzb(Status status) {
                return status;
            }
        });
    }

    public PendingResult<CredentialRequestResult> request(GoogleApiClient googleApiClient, final CredentialRequest credentialRequest) {
        return googleApiClient.zza(new zzd<CredentialRequestResult>(googleApiClient) {
            /* access modifiers changed from: protected */
            public void zza(Context context, zzh zzh) throws RemoteException {
                zzh.zza((zzg) new zza() {
                    public void zza(Status status, Credential credential) {
                        C03281.this.zzb((Result) new zzb(status, credential));
                    }

                    public void zzg(Status status) {
                        C03281.this.zzb((Result) zzb.zzh(status));
                    }
                }, credentialRequest);
            }

            /* access modifiers changed from: protected */
            /* renamed from: zzi */
            public CredentialRequestResult zzb(Status status) {
                return zzb.zzh(status);
            }
        });
    }

    public PendingResult<Status> save(GoogleApiClient googleApiClient, final Credential credential) {
        return googleApiClient.zzb(new zzd<Status>(googleApiClient) {
            /* access modifiers changed from: protected */
            public void zza(Context context, zzh zzh) throws RemoteException {
                zzh.zza((zzg) new zza(this), new SaveRequest(credential));
            }

            /* access modifiers changed from: protected */
            /* renamed from: zzd */
            public Status zzb(Status status) {
                return status;
            }
        });
    }
}
