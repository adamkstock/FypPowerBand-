package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class zzlc<R extends Result> extends PendingResult<R> {
    private boolean zzL;
    private volatile R zzaaX;
    private final Object zzabh = new Object();
    protected final zza<R> zzabi;
    private final ArrayList<com.google.android.gms.common.api.PendingResult.zza> zzabj = new ArrayList<>();
    private ResultCallback<? super R> zzabk;
    private volatile boolean zzabl;
    private boolean zzabm;
    private zzq zzabn;
    private Integer zzabo;
    private volatile zzlq<R> zzabp;
    private final CountDownLatch zzoS = new CountDownLatch(1);

    public static class zza<R extends Result> extends Handler {
        public zza() {
            this(Looper.getMainLooper());
        }

        public zza(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Pair pair = (Pair) message.obj;
                zzb((ResultCallback) pair.first, (Result) pair.second);
            } else if (i != 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Don't know how to handle message: ");
                sb.append(message.what);
                Log.wtf("BasePendingResult", sb.toString(), new Exception());
            } else {
                ((zzlc) message.obj).zzw(Status.zzabe);
            }
        }

        public void zza(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }

        public void zza(zzlc<R> zzlc, long j) {
            sendMessageDelayed(obtainMessage(2, zzlc), j);
        }

        /* access modifiers changed from: protected */
        public void zzb(ResultCallback<? super R> resultCallback, R r) {
            try {
                resultCallback.onResult(r);
            } catch (RuntimeException e) {
                zzlc.zzd(r);
                throw e;
            }
        }

        public void zznM() {
            removeMessages(2);
        }
    }

    @Deprecated
    protected zzlc(Looper looper) {
        this.zzabi = new zza<>(looper);
    }

    protected zzlc(GoogleApiClient googleApiClient) {
        this.zzabi = new zza<>(googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
    }

    private R get() {
        R r;
        synchronized (this.zzabh) {
            zzx.zza(!this.zzabl, (Object) "Result has already been consumed.");
            zzx.zza(isReady(), (Object) "Result is not ready.");
            r = this.zzaaX;
            this.zzaaX = null;
            this.zzabk = null;
            this.zzabl = true;
        }
        zznL();
        return r;
    }

    private void zzc(R r) {
        this.zzaaX = r;
        this.zzabn = null;
        this.zzoS.countDown();
        Status status = this.zzaaX.getStatus();
        if (this.zzabk != null) {
            this.zzabi.zznM();
            if (!this.zzL) {
                this.zzabi.zza(this.zzabk, get());
            }
        }
        Iterator it = this.zzabj.iterator();
        while (it.hasNext()) {
            ((com.google.android.gms.common.api.PendingResult.zza) it.next()).zzt(status);
        }
        this.zzabj.clear();
    }

    public static void zzd(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to release ");
                sb.append(result);
                Log.w("BasePendingResult", sb.toString(), e);
            }
        }
    }

    public final R await() {
        boolean z = false;
        zzx.zza(Looper.myLooper() != Looper.getMainLooper(), (Object) "await must not be called on the UI thread");
        zzx.zza(!this.zzabl, (Object) "Result has already been consumed");
        if (this.zzabp == null) {
            z = true;
        }
        zzx.zza(z, (Object) "Cannot await if then() has been called.");
        try {
            this.zzoS.await();
        } catch (InterruptedException unused) {
            zzw(Status.zzabc);
        }
        zzx.zza(isReady(), (Object) "Result is not ready.");
        return get();
    }

    public final R await(long j, TimeUnit timeUnit) {
        boolean z = false;
        zzx.zza(j <= 0 || Looper.myLooper() != Looper.getMainLooper(), (Object) "await must not be called on the UI thread when time is greater than zero.");
        zzx.zza(!this.zzabl, (Object) "Result has already been consumed.");
        if (this.zzabp == null) {
            z = true;
        }
        zzx.zza(z, (Object) "Cannot await if then() has been called.");
        try {
            if (!this.zzoS.await(j, timeUnit)) {
                zzw(Status.zzabe);
            }
        } catch (InterruptedException unused) {
            zzw(Status.zzabc);
        }
        zzx.zza(isReady(), (Object) "Result is not ready.");
        return get();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:8|(2:10|11)|12|13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002c, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
            r2 = this;
            java.lang.Object r0 = r2.zzabh
            monitor-enter(r0)
            boolean r1 = r2.zzL     // Catch:{ all -> 0x002d }
            if (r1 != 0) goto L_0x002b
            boolean r1 = r2.zzabl     // Catch:{ all -> 0x002d }
            if (r1 == 0) goto L_0x000c
            goto L_0x002b
        L_0x000c:
            com.google.android.gms.common.internal.zzq r1 = r2.zzabn     // Catch:{ all -> 0x002d }
            if (r1 == 0) goto L_0x0015
            com.google.android.gms.common.internal.zzq r1 = r2.zzabn     // Catch:{ RemoteException -> 0x0015 }
            r1.cancel()     // Catch:{ RemoteException -> 0x0015 }
        L_0x0015:
            R r1 = r2.zzaaX     // Catch:{ all -> 0x002d }
            zzd(r1)     // Catch:{ all -> 0x002d }
            r1 = 0
            r2.zzabk = r1     // Catch:{ all -> 0x002d }
            r1 = 1
            r2.zzL = r1     // Catch:{ all -> 0x002d }
            com.google.android.gms.common.api.Status r1 = com.google.android.gms.common.api.Status.zzabf     // Catch:{ all -> 0x002d }
            com.google.android.gms.common.api.Result r1 = r2.zzb(r1)     // Catch:{ all -> 0x002d }
            r2.zzc(r1)     // Catch:{ all -> 0x002d }
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return
        L_0x002d:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzlc.cancel():void");
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this.zzabh) {
            z = this.zzL;
        }
        return z;
    }

    public final boolean isReady() {
        return this.zzoS.getCount() == 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r4) {
        /*
            r3 = this;
            boolean r0 = r3.zzabl
            r1 = 1
            r0 = r0 ^ r1
            java.lang.String r2 = "Result has already been consumed."
            com.google.android.gms.common.internal.zzx.zza(r0, r2)
            java.lang.Object r0 = r3.zzabh
            monitor-enter(r0)
            com.google.android.gms.internal.zzlq<R> r2 = r3.zzabp     // Catch:{ all -> 0x0033 }
            if (r2 != 0) goto L_0x0011
            goto L_0x0012
        L_0x0011:
            r1 = 0
        L_0x0012:
            java.lang.String r2 = "Cannot set callbacks if then() has been called."
            com.google.android.gms.common.internal.zzx.zza(r1, r2)     // Catch:{ all -> 0x0033 }
            boolean r1 = r3.isCanceled()     // Catch:{ all -> 0x0033 }
            if (r1 == 0) goto L_0x001f
            monitor-exit(r0)     // Catch:{ all -> 0x0033 }
            return
        L_0x001f:
            boolean r1 = r3.isReady()     // Catch:{ all -> 0x0033 }
            if (r1 == 0) goto L_0x002f
            com.google.android.gms.internal.zzlc$zza<R> r1 = r3.zzabi     // Catch:{ all -> 0x0033 }
            com.google.android.gms.common.api.Result r2 = r3.get()     // Catch:{ all -> 0x0033 }
            r1.zza(r4, r2)     // Catch:{ all -> 0x0033 }
            goto L_0x0031
        L_0x002f:
            r3.zzabk = r4     // Catch:{ all -> 0x0033 }
        L_0x0031:
            monitor-exit(r0)     // Catch:{ all -> 0x0033 }
            return
        L_0x0033:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0033 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzlc.setResultCallback(com.google.android.gms.common.api.ResultCallback):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r4, long r5, java.util.concurrent.TimeUnit r7) {
        /*
            r3 = this;
            boolean r0 = r3.zzabl
            r1 = 1
            r0 = r0 ^ r1
            java.lang.String r2 = "Result has already been consumed."
            com.google.android.gms.common.internal.zzx.zza(r0, r2)
            java.lang.Object r0 = r3.zzabh
            monitor-enter(r0)
            com.google.android.gms.internal.zzlq<R> r2 = r3.zzabp     // Catch:{ all -> 0x003c }
            if (r2 != 0) goto L_0x0011
            goto L_0x0012
        L_0x0011:
            r1 = 0
        L_0x0012:
            java.lang.String r2 = "Cannot set callbacks if then() has been called."
            com.google.android.gms.common.internal.zzx.zza(r1, r2)     // Catch:{ all -> 0x003c }
            boolean r1 = r3.isCanceled()     // Catch:{ all -> 0x003c }
            if (r1 == 0) goto L_0x001f
            monitor-exit(r0)     // Catch:{ all -> 0x003c }
            return
        L_0x001f:
            boolean r1 = r3.isReady()     // Catch:{ all -> 0x003c }
            if (r1 == 0) goto L_0x002f
            com.google.android.gms.internal.zzlc$zza<R> r5 = r3.zzabi     // Catch:{ all -> 0x003c }
            com.google.android.gms.common.api.Result r6 = r3.get()     // Catch:{ all -> 0x003c }
            r5.zza(r4, r6)     // Catch:{ all -> 0x003c }
            goto L_0x003a
        L_0x002f:
            r3.zzabk = r4     // Catch:{ all -> 0x003c }
            com.google.android.gms.internal.zzlc$zza<R> r4 = r3.zzabi     // Catch:{ all -> 0x003c }
            long r5 = r7.toMillis(r5)     // Catch:{ all -> 0x003c }
            r4.zza(r3, r5)     // Catch:{ all -> 0x003c }
        L_0x003a:
            monitor-exit(r0)     // Catch:{ all -> 0x003c }
            return
        L_0x003c:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003c }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzlc.setResultCallback(com.google.android.gms.common.api.ResultCallback, long, java.util.concurrent.TimeUnit):void");
    }

    public final void zza(com.google.android.gms.common.api.PendingResult.zza zza2) {
        boolean z = true;
        zzx.zza(!this.zzabl, (Object) "Result has already been consumed.");
        if (zza2 == null) {
            z = false;
        }
        zzx.zzb(z, (Object) "Callback cannot be null.");
        synchronized (this.zzabh) {
            if (isReady()) {
                zza2.zzt(this.zzaaX.getStatus());
            } else {
                this.zzabj.add(zza2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void zza(zzq zzq) {
        synchronized (this.zzabh) {
            this.zzabn = zzq;
        }
    }

    /* access modifiers changed from: protected */
    public abstract R zzb(Status status);

    public final void zzb(R r) {
        synchronized (this.zzabh) {
            if (!this.zzabm) {
                if (!this.zzL) {
                    boolean z = true;
                    zzx.zza(!isReady(), (Object) "Results have already been set");
                    if (this.zzabl) {
                        z = false;
                    }
                    zzx.zza(z, (Object) "Result has already been consumed");
                    zzc(r);
                    return;
                }
            }
            zzd(r);
        }
    }

    public Integer zznF() {
        return this.zzabo;
    }

    /* access modifiers changed from: protected */
    public void zznL() {
    }

    public final void zzw(Status status) {
        synchronized (this.zzabh) {
            if (!isReady()) {
                zzb((R) zzb(status));
                this.zzabm = true;
            }
        }
    }
}
