package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.p000v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.common.internal.zzk;
import com.google.android.gms.common.internal.zzx;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.jmdns.impl.constants.DNSConstants;

public final class zzli extends GoogleApiClient {
    private final Context mContext;
    /* access modifiers changed from: private */
    public final int zzaaM;
    private final Looper zzaaO;
    private final GoogleApiAvailability zzaaP;
    final com.google.android.gms.common.api.Api.zza<? extends zzqw, zzqx> zzaaQ;
    final com.google.android.gms.common.internal.zzf zzabI;
    final Map<Api<?>, Integer> zzabJ;
    private final Condition zzabY;
    final zzk zzabZ;
    /* access modifiers changed from: private */
    public final Lock zzabt = new ReentrantLock();
    final Queue<zzf<?>> zzaca = new LinkedList();
    private volatile boolean zzacb;
    private long zzacc = 120000;
    private long zzacd = DNSConstants.CLOSE_TIMEOUT;
    private final zza zzace;
    zzd zzacf;
    final Map<com.google.android.gms.common.api.Api.zzc<?>, com.google.android.gms.common.api.Api.zzb> zzacg = new HashMap();
    final Map<com.google.android.gms.common.api.Api.zzc<?>, ConnectionResult> zzach = new HashMap();
    Set<Scope> zzaci = new HashSet();
    /* access modifiers changed from: private */
    public volatile zzlj zzacj;
    private ConnectionResult zzack = null;
    private final Set<zzlm<?>> zzacl = Collections.newSetFromMap(new WeakHashMap());
    final Set<zzf<?>> zzacm = Collections.newSetFromMap(new ConcurrentHashMap(16, 0.75f, 2));
    /* access modifiers changed from: private */
    public com.google.android.gms.common.api.zza zzacn;
    private final zze zzaco = new zze() {
        public void zzc(zzf<?> zzf) {
            zzli.this.zzacm.remove(zzf);
            if (zzf.zznF() != null && zzli.this.zzacn != null) {
                zzli.this.zzacn.remove(zzf.zznF().intValue());
            }
        }
    };
    private final ConnectionCallbacks zzacp = new ConnectionCallbacks() {
        public void onConnected(Bundle bundle) {
            zzli.this.zzabt.lock();
            try {
                zzli.this.zzacj.onConnected(bundle);
            } finally {
                zzli.this.zzabt.unlock();
            }
        }

        public void onConnectionSuspended(int i) {
            zzli.this.zzabt.lock();
            try {
                zzli.this.zzacj.onConnectionSuspended(i);
            } finally {
                zzli.this.zzabt.unlock();
            }
        }
    };
    private final com.google.android.gms.common.internal.zzk.zza zzacq = new com.google.android.gms.common.internal.zzk.zza() {
        public boolean isConnected() {
            return zzli.this.isConnected();
        }

        public Bundle zzmS() {
            return null;
        }
    };

    final class zza extends Handler {
        zza(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                zzli.this.zzod();
            } else if (i == 2) {
                zzli.this.resume();
            } else if (i == 3) {
                ((zzb) message.obj).zzg(zzli.this);
            } else if (i != 4) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown message id: ");
                sb.append(message.what);
                Log.w("GoogleApiClientImpl", sb.toString());
            } else {
                throw ((RuntimeException) message.obj);
            }
        }
    }

    static abstract class zzb {
        private final zzlj zzacy;

        protected zzb(zzlj zzlj) {
            this.zzacy = zzlj;
        }

        public final void zzg(zzli zzli) {
            zzli.zzabt.lock();
            try {
                if (zzli.zzacj == this.zzacy) {
                    zznO();
                    zzli.zzabt.unlock();
                }
            } finally {
                zzli.zzabt.unlock();
            }
        }

        /* access modifiers changed from: protected */
        public abstract void zznO();
    }

    private static class zzc implements DeathRecipient, zze {
        private final WeakReference<com.google.android.gms.common.api.zza> zzacA;
        private final WeakReference<IBinder> zzacB;
        private final WeakReference<zzf<?>> zzacz;

        private zzc(zzf zzf, com.google.android.gms.common.api.zza zza, IBinder iBinder) {
            this.zzacA = new WeakReference<>(zza);
            this.zzacz = new WeakReference<>(zzf);
            this.zzacB = new WeakReference<>(iBinder);
        }

        private void zzoh() {
            zzf zzf = (zzf) this.zzacz.get();
            com.google.android.gms.common.api.zza zza = (com.google.android.gms.common.api.zza) this.zzacA.get();
            if (!(zza == null || zzf == null)) {
                zza.remove(zzf.zznF().intValue());
            }
            IBinder iBinder = (IBinder) this.zzacB.get();
            if (this.zzacB != null) {
                iBinder.unlinkToDeath(this, 0);
            }
        }

        public void binderDied() {
            zzoh();
        }

        public void zzc(zzf<?> zzf) {
            zzoh();
        }
    }

    static class zzd extends zzll {
        private WeakReference<zzli> zzacC;

        zzd(zzli zzli) {
            this.zzacC = new WeakReference<>(zzli);
        }

        public void zzoi() {
            zzli zzli = (zzli) this.zzacC.get();
            if (zzli != null) {
                zzli.resume();
            }
        }
    }

    interface zze {
        void zzc(zzf<?> zzf);
    }

    interface zzf<A extends com.google.android.gms.common.api.Api.zzb> {
        void cancel();

        boolean isReady();

        void zza(zze zze);

        void zzb(A a) throws DeadObjectException;

        Integer zznF();

        void zznJ();

        int zznK();

        com.google.android.gms.common.api.Api.zzc<A> zznx();

        void zzv(Status status);

        void zzw(Status status);
    }

    public zzli(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzf2, GoogleApiAvailability googleApiAvailability, com.google.android.gms.common.api.Api.zza<? extends zzqw, zzqx> zza2, Map<Api<?>, ApiOptions> map, ArrayList<ConnectionCallbacks> arrayList, ArrayList<OnConnectionFailedListener> arrayList2, int i) {
        Looper looper2 = looper;
        this.mContext = context;
        this.zzabZ = new zzk(looper2, this.zzacq);
        this.zzaaO = looper2;
        this.zzace = new zza(looper2);
        this.zzaaP = googleApiAvailability;
        this.zzaaM = i;
        this.zzabJ = new HashMap();
        this.zzabY = this.zzabt.newCondition();
        this.zzacj = new zzlh(this);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.zzabZ.registerConnectionCallbacks((ConnectionCallbacks) it.next());
        }
        Iterator it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            this.zzabZ.registerConnectionFailedListener((OnConnectionFailedListener) it2.next());
        }
        Map zzoM = zzf2.zzoM();
        for (Api api : map.keySet()) {
            Object obj = map.get(api);
            int i2 = 0;
            if (zzoM.get(api) != null) {
                i2 = ((com.google.android.gms.common.internal.zzf.zza) zzoM.get(api)).zzafk ? 1 : 2;
            }
            this.zzabJ.put(api, Integer.valueOf(i2));
            this.zzacg.put(api.zznx(), api.zzny() ? zza(api.zznw(), obj, context, looper, zzf2, this.zzacp, zza(api, i2)) : zza(api.zznv(), obj, context, looper, zzf2, this.zzacp, zza(api, i2)));
        }
        this.zzabI = zzf2;
        this.zzaaQ = zza2;
    }

    /* access modifiers changed from: private */
    public void resume() {
        this.zzabt.lock();
        try {
            if (zzoc()) {
                connect();
            }
        } finally {
            this.zzabt.unlock();
        }
    }

    private static <C extends com.google.android.gms.common.api.Api.zzb, O> C zza(com.google.android.gms.common.api.Api.zza<C, O> zza2, Object obj, Context context, Looper looper, com.google.android.gms.common.internal.zzf zzf2, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        return zza2.zza(context, looper, zzf2, obj, connectionCallbacks, onConnectionFailedListener);
    }

    private OnConnectionFailedListener zza(final Api<?> api, final int i) {
        return new OnConnectionFailedListener() {
            public void onConnectionFailed(ConnectionResult connectionResult) {
                zzli.this.zzabt.lock();
                try {
                    zzli.this.zzacj.zza(connectionResult, api, i);
                } finally {
                    zzli.this.zzabt.unlock();
                }
            }
        };
    }

    private static <C extends com.google.android.gms.common.api.Api.zzd, O> zzac zza(com.google.android.gms.common.api.Api.zze<C, O> zze2, Object obj, Context context, Looper looper, com.google.android.gms.common.internal.zzf zzf2, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        zzac zzac = new zzac(context, looper, zze2.zznA(), connectionCallbacks, onConnectionFailedListener, zzf2, zze2.zzn(obj));
        return zzac;
    }

    /* access modifiers changed from: private */
    public void zza(final GoogleApiClient googleApiClient, final zzlo zzlo, final boolean z) {
        zzlx.zzagw.zzb(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            /* renamed from: zzo */
            public void onResult(Status status) {
                if (status.isSuccess() && zzli.this.isConnected()) {
                    zzli.this.reconnect();
                }
                zzlo.zzb(status);
                if (z) {
                    googleApiClient.disconnect();
                }
            }
        });
    }

    private static void zza(zzf<?> zzf2, com.google.android.gms.common.api.zza zza2, IBinder iBinder) {
        if (zzf2.isReady()) {
            zzf2.zza(new zzc(zzf2, zza2, iBinder));
            return;
        }
        if (iBinder == null || !iBinder.isBinderAlive()) {
            zzf2.zza(null);
        } else {
            zzc zzc2 = new zzc(zzf2, zza2, iBinder);
            zzf2.zza(zzc2);
            try {
                iBinder.linkToDeath(zzc2, 0);
                return;
            } catch (RemoteException unused) {
            }
        }
        zzf2.cancel();
        zza2.remove(zzf2.zznF().intValue());
    }

    /* access modifiers changed from: private */
    public void zzod() {
        this.zzabt.lock();
        try {
            if (zzof()) {
                connect();
            }
        } finally {
            this.zzabt.unlock();
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:10|11|12|13|7|8) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0027 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.common.ConnectionResult blockingConnect() {
        /*
            r3 = this;
            android.os.Looper r0 = android.os.Looper.myLooper()
            android.os.Looper r1 = android.os.Looper.getMainLooper()
            if (r0 == r1) goto L_0x000c
            r0 = 1
            goto L_0x000d
        L_0x000c:
            r0 = 0
        L_0x000d:
            java.lang.String r1 = "blockingConnect must not be called on the UI thread"
            com.google.android.gms.common.internal.zzx.zza(r0, r1)
            java.util.concurrent.locks.Lock r0 = r3.zzabt
            r0.lock()
            r3.connect()     // Catch:{ all -> 0x0053 }
        L_0x001a:
            boolean r0 = r3.isConnecting()     // Catch:{ all -> 0x0053 }
            r1 = 0
            if (r0 == 0) goto L_0x003b
            java.util.concurrent.locks.Condition r0 = r3.zzabY     // Catch:{ InterruptedException -> 0x0027 }
            r0.await()     // Catch:{ InterruptedException -> 0x0027 }
            goto L_0x001a
        L_0x0027:
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0053 }
            r0.interrupt()     // Catch:{ all -> 0x0053 }
            com.google.android.gms.common.ConnectionResult r0 = new com.google.android.gms.common.ConnectionResult     // Catch:{ all -> 0x0053 }
            r2 = 15
            r0.<init>(r2, r1)     // Catch:{ all -> 0x0053 }
        L_0x0035:
            java.util.concurrent.locks.Lock r1 = r3.zzabt
            r1.unlock()
            return r0
        L_0x003b:
            boolean r0 = r3.isConnected()     // Catch:{ all -> 0x0053 }
            if (r0 == 0) goto L_0x0044
            com.google.android.gms.common.ConnectionResult r0 = com.google.android.gms.common.ConnectionResult.zzZY     // Catch:{ all -> 0x0053 }
            goto L_0x0035
        L_0x0044:
            com.google.android.gms.common.ConnectionResult r0 = r3.zzack     // Catch:{ all -> 0x0053 }
            if (r0 == 0) goto L_0x004b
            com.google.android.gms.common.ConnectionResult r0 = r3.zzack     // Catch:{ all -> 0x0053 }
            goto L_0x0035
        L_0x004b:
            com.google.android.gms.common.ConnectionResult r0 = new com.google.android.gms.common.ConnectionResult     // Catch:{ all -> 0x0053 }
            r2 = 13
            r0.<init>(r2, r1)     // Catch:{ all -> 0x0053 }
            goto L_0x0035
        L_0x0053:
            r0 = move-exception
            java.util.concurrent.locks.Lock r1 = r3.zzabt
            r1.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzli.blockingConnect():com.google.android.gms.common.ConnectionResult");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:16|17) */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        java.lang.Thread.currentThread().interrupt();
        r4 = new com.google.android.gms.common.ConnectionResult(15, null);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0043 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.common.ConnectionResult blockingConnect(long r4, java.util.concurrent.TimeUnit r6) {
        /*
            r3 = this;
            android.os.Looper r0 = android.os.Looper.myLooper()
            android.os.Looper r1 = android.os.Looper.getMainLooper()
            if (r0 == r1) goto L_0x000c
            r0 = 1
            goto L_0x000d
        L_0x000c:
            r0 = 0
        L_0x000d:
            java.lang.String r1 = "blockingConnect must not be called on the UI thread"
            com.google.android.gms.common.internal.zzx.zza(r0, r1)
            java.lang.String r0 = "TimeUnit must not be null"
            com.google.android.gms.common.internal.zzx.zzb(r6, r0)
            java.util.concurrent.locks.Lock r0 = r3.zzabt
            r0.lock()
            r3.connect()     // Catch:{ all -> 0x006a }
            long r4 = r6.toNanos(r4)     // Catch:{ all -> 0x006a }
        L_0x0023:
            boolean r6 = r3.isConnecting()     // Catch:{ all -> 0x006a }
            r0 = 0
            if (r6 == 0) goto L_0x0052
            java.util.concurrent.locks.Condition r6 = r3.zzabY     // Catch:{ InterruptedException -> 0x0043 }
            long r4 = r6.awaitNanos(r4)     // Catch:{ InterruptedException -> 0x0043 }
            r1 = 0
            int r6 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r6 > 0) goto L_0x0023
            com.google.android.gms.common.ConnectionResult r4 = new com.google.android.gms.common.ConnectionResult     // Catch:{ InterruptedException -> 0x0043 }
            r5 = 14
            r4.<init>(r5, r0)     // Catch:{ InterruptedException -> 0x0043 }
        L_0x003d:
            java.util.concurrent.locks.Lock r5 = r3.zzabt
            r5.unlock()
            return r4
        L_0x0043:
            java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x006a }
            r4.interrupt()     // Catch:{ all -> 0x006a }
            com.google.android.gms.common.ConnectionResult r4 = new com.google.android.gms.common.ConnectionResult     // Catch:{ all -> 0x006a }
            r5 = 15
            r4.<init>(r5, r0)     // Catch:{ all -> 0x006a }
            goto L_0x003d
        L_0x0052:
            boolean r4 = r3.isConnected()     // Catch:{ all -> 0x006a }
            if (r4 == 0) goto L_0x005b
            com.google.android.gms.common.ConnectionResult r4 = com.google.android.gms.common.ConnectionResult.zzZY     // Catch:{ all -> 0x006a }
            goto L_0x003d
        L_0x005b:
            com.google.android.gms.common.ConnectionResult r4 = r3.zzack     // Catch:{ all -> 0x006a }
            if (r4 == 0) goto L_0x0062
            com.google.android.gms.common.ConnectionResult r4 = r3.zzack     // Catch:{ all -> 0x006a }
            goto L_0x003d
        L_0x0062:
            com.google.android.gms.common.ConnectionResult r4 = new com.google.android.gms.common.ConnectionResult     // Catch:{ all -> 0x006a }
            r5 = 13
            r4.<init>(r5, r0)     // Catch:{ all -> 0x006a }
            goto L_0x003d
        L_0x006a:
            r4 = move-exception
            java.util.concurrent.locks.Lock r5 = r3.zzabt
            r5.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzli.blockingConnect(long, java.util.concurrent.TimeUnit):com.google.android.gms.common.ConnectionResult");
    }

    public PendingResult<Status> clearDefaultAccountAndReconnect() {
        zzx.zza(isConnected(), (Object) "GoogleApiClient is not connected yet.");
        final zzlo zzlo = new zzlo((GoogleApiClient) this);
        if (this.zzacg.containsKey(zzlx.zzRk)) {
            zza((GoogleApiClient) this, zzlo, false);
        } else {
            final AtomicReference atomicReference = new AtomicReference();
            C05645 r2 = new ConnectionCallbacks() {
                public void onConnected(Bundle bundle) {
                    zzli.this.zza((GoogleApiClient) atomicReference.get(), zzlo, true);
                }

                public void onConnectionSuspended(int i) {
                }
            };
            GoogleApiClient build = new Builder(this.mContext).addApi(zzlx.API).addConnectionCallbacks(r2).addOnConnectionFailedListener(new OnConnectionFailedListener() {
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    zzlo.zzb(new Status(8));
                }
            }).setHandler(this.zzace).build();
            atomicReference.set(build);
            build.connect();
        }
        return zzlo;
    }

    public void connect() {
        this.zzabt.lock();
        try {
            this.zzacj.connect();
        } finally {
            this.zzabt.unlock();
        }
    }

    public void disconnect() {
        this.zzabt.lock();
        try {
            zzof();
            this.zzacj.disconnect();
        } finally {
            this.zzabt.unlock();
        }
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).append("mState=").append(this.zzacj.getName());
        printWriter.append(" mResuming=").print(this.zzacb);
        printWriter.append(" mWorkQueue.size()=").print(this.zzaca.size());
        printWriter.append(" mUnconsumedRunners.size()=").println(this.zzacm.size());
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("  ");
        String sb2 = sb.toString();
        for (Api api : this.zzabJ.keySet()) {
            printWriter.append(str).append(api.getName()).println(":");
            ((com.google.android.gms.common.api.Api.zzb) this.zzacg.get(api.zznx())).dump(sb2, fileDescriptor, printWriter, strArr);
        }
    }

    public ConnectionResult getConnectionResult(Api<?> api) {
        ConnectionResult connectionResult;
        String str = "GoogleApiClientImpl";
        com.google.android.gms.common.api.Api.zzc zznx = api.zznx();
        this.zzabt.lock();
        try {
            if (!isConnected()) {
                if (!zzoc()) {
                    throw new IllegalStateException("Cannot invoke getConnectionResult unless GoogleApiClient is connected");
                }
            }
            if (this.zzacg.containsKey(zznx)) {
                if (((com.google.android.gms.common.api.Api.zzb) this.zzacg.get(zznx)).isConnected()) {
                    connectionResult = ConnectionResult.zzZY;
                } else if (this.zzach.containsKey(zznx)) {
                    connectionResult = (ConnectionResult) this.zzach.get(zznx);
                } else {
                    Log.i(str, zzog());
                    StringBuilder sb = new StringBuilder();
                    sb.append(api.getName());
                    sb.append(" requested in getConnectionResult");
                    sb.append(" is not connected but is not present in the failed connections map");
                    Log.wtf(str, sb.toString(), new Exception());
                    connectionResult = new ConnectionResult(8, null);
                }
                return connectionResult;
            }
            this.zzabt.unlock();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(api.getName());
            sb2.append(" was never registered with GoogleApiClient");
            throw new IllegalArgumentException(sb2.toString());
        } finally {
            this.zzabt.unlock();
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public Looper getLooper() {
        return this.zzaaO;
    }

    public int getSessionId() {
        return System.identityHashCode(this);
    }

    public boolean hasConnectedApi(Api<?> api) {
        com.google.android.gms.common.api.Api.zzb zzb2 = (com.google.android.gms.common.api.Api.zzb) this.zzacg.get(api.zznx());
        return zzb2 != null && zzb2.isConnected();
    }

    public boolean isConnected() {
        return this.zzacj instanceof zzlf;
    }

    public boolean isConnecting() {
        return this.zzacj instanceof zzlg;
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        return this.zzabZ.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        return this.zzabZ.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public void reconnect() {
        disconnect();
        connect();
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.zzabZ.registerConnectionCallbacks(connectionCallbacks);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.zzabZ.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public void stopAutoManage(final FragmentActivity fragmentActivity) {
        if (this.zzaaM >= 0) {
            zzlp zza2 = zzlp.zza(fragmentActivity);
            if (zza2 == null) {
                new Handler(this.mContext.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (!fragmentActivity.isFinishing() && !fragmentActivity.getSupportFragmentManager().isDestroyed()) {
                            zzlp.zzb(fragmentActivity).zzbp(zzli.this.zzaaM);
                        }
                    }
                });
            } else {
                zza2.zzbp(this.zzaaM);
            }
        } else {
            throw new IllegalStateException("Called stopAutoManage but automatic lifecycle management is not enabled.");
        }
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.zzabZ.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.zzabZ.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    public <C extends com.google.android.gms.common.api.Api.zzb> C zza(com.google.android.gms.common.api.Api.zzc<C> zzc2) {
        C c = (com.google.android.gms.common.api.Api.zzb) this.zzacg.get(zzc2);
        zzx.zzb(c, (Object) "Appropriate Api was not requested.");
        return c;
    }

    public <A extends com.google.android.gms.common.api.Api.zzb, R extends Result, T extends com.google.android.gms.internal.zzlb.zza<R, A>> T zza(T t) {
        zzx.zzb(t.zznx() != null, (Object) "This task can not be enqueued (it's probably a Batch or malformed)");
        zzx.zzb(this.zzacg.containsKey(t.zznx()), (Object) "GoogleApiClient is not configured to use the API required for this call.");
        this.zzabt.lock();
        try {
            return this.zzacj.zza(t);
        } finally {
            this.zzabt.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public void zza(zzb zzb2) {
        this.zzace.sendMessage(this.zzace.obtainMessage(3, zzb2));
    }

    /* access modifiers changed from: 0000 */
    public void zza(RuntimeException runtimeException) {
        this.zzace.sendMessage(this.zzace.obtainMessage(4, runtimeException));
    }

    public boolean zza(Api<?> api) {
        return this.zzacg.containsKey(api.zznx());
    }

    public <A extends com.google.android.gms.common.api.Api.zzb, T extends com.google.android.gms.internal.zzlb.zza<? extends Result, A>> T zzb(T t) {
        zzx.zzb(t.zznx() != null, (Object) "This task can not be executed (it's probably a Batch or malformed)");
        this.zzabt.lock();
        try {
            if (zzoc()) {
                this.zzaca.add(t);
                while (!this.zzaca.isEmpty()) {
                    zzf zzf2 = (zzf) this.zzaca.remove();
                    zzb(zzf2);
                    zzf2.zzv(Status.zzabd);
                }
            } else {
                t = this.zzacj.zzb(t);
            }
            return t;
        } finally {
            this.zzabt.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public <A extends com.google.android.gms.common.api.Api.zzb> void zzb(zzf<A> zzf2) {
        this.zzacm.add(zzf2);
        zzf2.zza(this.zzaco);
    }

    /* access modifiers changed from: 0000 */
    public void zzg(ConnectionResult connectionResult) {
        this.zzabt.lock();
        try {
            this.zzack = connectionResult;
            this.zzacj = new zzlh(this);
            this.zzacj.begin();
            this.zzabY.signalAll();
        } finally {
            this.zzabt.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public void zznY() {
        for (zzf zzf2 : this.zzacm) {
            zzf2.zza(null);
            if (zzf2.zznF() == null) {
                zzf2.cancel();
            } else {
                zzf2.zznJ();
                zza(zzf2, this.zzacn, zza(zzf2.zznx()).zznz());
            }
        }
        this.zzacm.clear();
        for (zzlm clear : this.zzacl) {
            clear.clear();
        }
        this.zzacl.clear();
    }

    /* access modifiers changed from: 0000 */
    public void zznZ() {
        for (com.google.android.gms.common.api.Api.zzb disconnect : this.zzacg.values()) {
            disconnect.disconnect();
        }
    }

    public <L> zzlm<L> zzo(L l) {
        zzx.zzb(l, (Object) "Listener must not be null");
        this.zzabt.lock();
        try {
            zzlm<L> zzlm = new zzlm<>(this.zzaaO, l);
            this.zzacl.add(zzlm);
            return zzlm;
        } finally {
            this.zzabt.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public void zzoa() {
        this.zzabt.lock();
        try {
            zzlg zzlg = new zzlg(this, this.zzabI, this.zzabJ, this.zzaaP, this.zzaaQ, this.zzabt, this.mContext);
            this.zzacj = zzlg;
            this.zzacj.begin();
            this.zzabY.signalAll();
        } finally {
            this.zzabt.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public void zzob() {
        this.zzabt.lock();
        try {
            zzof();
            this.zzacj = new zzlf(this);
            this.zzacj.begin();
            this.zzabY.signalAll();
        } finally {
            this.zzabt.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean zzoc() {
        return this.zzacb;
    }

    /* access modifiers changed from: 0000 */
    public void zzoe() {
        if (!zzoc()) {
            this.zzacb = true;
            if (this.zzacf == null) {
                this.zzacf = (zzd) zzll.zza(this.mContext.getApplicationContext(), new zzd(this), this.zzaaP);
            }
            zza zza2 = this.zzace;
            zza2.sendMessageDelayed(zza2.obtainMessage(1), this.zzacc);
            zza zza3 = this.zzace;
            zza3.sendMessageDelayed(zza3.obtainMessage(2), this.zzacd);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean zzof() {
        if (!zzoc()) {
            return false;
        }
        this.zzacb = false;
        this.zzace.removeMessages(2);
        this.zzace.removeMessages(1);
        zzd zzd2 = this.zzacf;
        if (zzd2 != null) {
            zzd2.unregister();
            this.zzacf = null;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public String zzog() {
        StringWriter stringWriter = new StringWriter();
        dump("", null, new PrintWriter(stringWriter), null);
        return stringWriter.toString();
    }
}
