package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult.zza;
import com.google.android.gms.internal.zzlc;
import java.util.ArrayList;
import java.util.List;

public final class Batch extends zzlc<BatchResult> {
    /* access modifiers changed from: private */
    public boolean zzaaA;
    /* access modifiers changed from: private */
    public final PendingResult<?>[] zzaaB;
    /* access modifiers changed from: private */
    public int zzaay;
    /* access modifiers changed from: private */
    public boolean zzaaz;
    /* access modifiers changed from: private */
    public final Object zzpd;

    public static final class Builder {
        private GoogleApiClient zzVs;
        private List<PendingResult<?>> zzaaD = new ArrayList();

        public Builder(GoogleApiClient googleApiClient) {
            this.zzVs = googleApiClient;
        }

        public <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken<R> batchResultToken = new BatchResultToken<>(this.zzaaD.size());
            this.zzaaD.add(pendingResult);
            return batchResultToken;
        }

        public Batch build() {
            return new Batch(this.zzaaD, this.zzVs);
        }
    }

    private Batch(List<PendingResult<?>> list, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.zzpd = new Object();
        this.zzaay = list.size();
        this.zzaaB = new PendingResult[this.zzaay];
        for (int i = 0; i < list.size(); i++) {
            PendingResult<?> pendingResult = (PendingResult) list.get(i);
            this.zzaaB[i] = pendingResult;
            pendingResult.zza(new zza() {
                /* JADX WARNING: Code restructure failed: missing block: B:24:0x0067, code lost:
                    return;
                 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void zzt(com.google.android.gms.common.api.Status r5) {
                    /*
                        r4 = this;
                        com.google.android.gms.common.api.Batch r0 = com.google.android.gms.common.api.Batch.this
                        java.lang.Object r0 = r0.zzpd
                        monitor-enter(r0)
                        com.google.android.gms.common.api.Batch r1 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        boolean r1 = r1.isCanceled()     // Catch:{ all -> 0x0068 }
                        if (r1 == 0) goto L_0x0011
                        monitor-exit(r0)     // Catch:{ all -> 0x0068 }
                        return
                    L_0x0011:
                        boolean r1 = r5.isCanceled()     // Catch:{ all -> 0x0068 }
                        r2 = 1
                        if (r1 == 0) goto L_0x001e
                        com.google.android.gms.common.api.Batch r5 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        r5.zzaaA = r2     // Catch:{ all -> 0x0068 }
                        goto L_0x0029
                    L_0x001e:
                        boolean r5 = r5.isSuccess()     // Catch:{ all -> 0x0068 }
                        if (r5 != 0) goto L_0x0029
                        com.google.android.gms.common.api.Batch r5 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        r5.zzaaz = r2     // Catch:{ all -> 0x0068 }
                    L_0x0029:
                        com.google.android.gms.common.api.Batch r5 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        r5.zzaay = r5.zzaay - 1     // Catch:{ all -> 0x0068 }
                        com.google.android.gms.common.api.Batch r5 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        int r5 = r5.zzaay     // Catch:{ all -> 0x0068 }
                        if (r5 != 0) goto L_0x0066
                        com.google.android.gms.common.api.Batch r5 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        boolean r5 = r5.zzaaA     // Catch:{ all -> 0x0068 }
                        if (r5 == 0) goto L_0x0044
                        com.google.android.gms.common.api.Batch r5 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        com.google.android.gms.common.api.Batch.super.cancel()     // Catch:{ all -> 0x0068 }
                        goto L_0x0066
                    L_0x0044:
                        com.google.android.gms.common.api.Batch r5 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        boolean r5 = r5.zzaaz     // Catch:{ all -> 0x0068 }
                        if (r5 == 0) goto L_0x0054
                        com.google.android.gms.common.api.Status r5 = new com.google.android.gms.common.api.Status     // Catch:{ all -> 0x0068 }
                        r1 = 13
                        r5.<init>(r1)     // Catch:{ all -> 0x0068 }
                        goto L_0x0056
                    L_0x0054:
                        com.google.android.gms.common.api.Status r5 = com.google.android.gms.common.api.Status.zzabb     // Catch:{ all -> 0x0068 }
                    L_0x0056:
                        com.google.android.gms.common.api.Batch r1 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        com.google.android.gms.common.api.BatchResult r2 = new com.google.android.gms.common.api.BatchResult     // Catch:{ all -> 0x0068 }
                        com.google.android.gms.common.api.Batch r3 = com.google.android.gms.common.api.Batch.this     // Catch:{ all -> 0x0068 }
                        com.google.android.gms.common.api.PendingResult[] r3 = r3.zzaaB     // Catch:{ all -> 0x0068 }
                        r2.<init>(r5, r3)     // Catch:{ all -> 0x0068 }
                        r1.zzb(r2)     // Catch:{ all -> 0x0068 }
                    L_0x0066:
                        monitor-exit(r0)     // Catch:{ all -> 0x0068 }
                        return
                    L_0x0068:
                        r5 = move-exception
                        monitor-exit(r0)     // Catch:{ all -> 0x0068 }
                        throw r5
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.Batch.C03341.zzt(com.google.android.gms.common.api.Status):void");
                }
            });
        }
    }

    public void cancel() {
        super.cancel();
        for (PendingResult<?> cancel : this.zzaaB) {
            cancel.cancel();
        }
    }

    /* renamed from: createFailedResult */
    public BatchResult zzb(Status status) {
        return new BatchResult(status, this.zzaaB);
    }
}
