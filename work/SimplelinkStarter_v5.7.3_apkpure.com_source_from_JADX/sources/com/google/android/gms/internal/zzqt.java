package com.google.android.gms.internal;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.search.GoogleNowAuthState;
import com.google.android.gms.search.SearchAuth;
import com.google.android.gms.search.SearchAuthApi;
import com.google.android.gms.search.SearchAuthApi.GoogleNowAuthResult;

public class zzqt implements SearchAuthApi {

    static abstract class zza extends com.google.android.gms.internal.zzqq.zza {
        zza() {
        }

        public void zza(Status status, GoogleNowAuthState googleNowAuthState) {
            throw new UnsupportedOperationException();
        }

        public void zzbb(Status status) {
            throw new UnsupportedOperationException();
        }
    }

    static class zzb extends com.google.android.gms.internal.zzlb.zza<Status, zzqs> {
        private final GoogleApiClient zzVs;
        private final String zzaUN;
        /* access modifiers changed from: private */
        public final boolean zzaUQ = Log.isLoggable("SearchAuth", 3);

        protected zzb(GoogleApiClient googleApiClient, String str) {
            super(SearchAuth.zzRk, googleApiClient);
            this.zzVs = googleApiClient;
            this.zzaUN = str;
        }

        /* access modifiers changed from: protected */
        public void zza(zzqs zzqs) throws RemoteException {
            if (this.zzaUQ) {
                Log.d("SearchAuth", "ClearTokenImpl started");
            }
            String packageName = this.zzVs.getContext().getPackageName();
            ((zzqr) zzqs.zzpc()).zzb(new zza() {
                public void zzbb(Status status) {
                    if (zzb.this.zzaUQ) {
                        Log.d("SearchAuth", "ClearTokenImpl success");
                    }
                    zzb.this.zzb(status);
                }
            }, packageName, this.zzaUN);
        }

        /* access modifiers changed from: protected */
        /* renamed from: zzd */
        public Status zzb(Status status) {
            if (this.zzaUQ) {
                StringBuilder sb = new StringBuilder();
                sb.append("ClearTokenImpl received failure: ");
                sb.append(status.getStatusMessage());
                Log.d("SearchAuth", sb.toString());
            }
            return status;
        }
    }

    static class zzc extends com.google.android.gms.internal.zzlb.zza<GoogleNowAuthResult, zzqs> {
        private final GoogleApiClient zzVs;
        /* access modifiers changed from: private */
        public final boolean zzaUQ = Log.isLoggable("SearchAuth", 3);
        private final String zzaUS;

        protected zzc(GoogleApiClient googleApiClient, String str) {
            super(SearchAuth.zzRk, googleApiClient);
            this.zzVs = googleApiClient;
            this.zzaUS = str;
        }

        /* access modifiers changed from: protected */
        public void zza(zzqs zzqs) throws RemoteException {
            if (this.zzaUQ) {
                Log.d("SearchAuth", "GetGoogleNowAuthImpl started");
            }
            String packageName = this.zzVs.getContext().getPackageName();
            ((zzqr) zzqs.zzpc()).zza(new zza() {
                public void zza(Status status, GoogleNowAuthState googleNowAuthState) {
                    if (zzc.this.zzaUQ) {
                        Log.d("SearchAuth", "GetGoogleNowAuthImpl success");
                    }
                    zzc.this.zzb(new zzd(status, googleNowAuthState));
                }
            }, packageName, this.zzaUS);
        }

        /* access modifiers changed from: protected */
        /* renamed from: zzbc */
        public GoogleNowAuthResult zzb(Status status) {
            if (this.zzaUQ) {
                StringBuilder sb = new StringBuilder();
                sb.append("GetGoogleNowAuthImpl received failure: ");
                sb.append(status.getStatusMessage());
                Log.d("SearchAuth", sb.toString());
            }
            return new zzd(status, null);
        }
    }

    static class zzd implements GoogleNowAuthResult {
        private final Status zzSC;
        private final GoogleNowAuthState zzaUU;

        zzd(Status status, GoogleNowAuthState googleNowAuthState) {
            this.zzSC = status;
            this.zzaUU = googleNowAuthState;
        }

        public GoogleNowAuthState getGoogleNowAuthState() {
            return this.zzaUU;
        }

        public Status getStatus() {
            return this.zzSC;
        }
    }

    public PendingResult<Status> clearToken(GoogleApiClient googleApiClient, String str) {
        return googleApiClient.zza(new zzb(googleApiClient, str));
    }

    public PendingResult<GoogleNowAuthResult> getGoogleNowAuth(GoogleApiClient googleApiClient, String str) {
        return googleApiClient.zza(new zzc(googleApiClient, str));
    }
}
