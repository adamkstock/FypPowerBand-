package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public final class zzk implements Callback {
    private final Handler mHandler;
    private final zza zzafP;
    private final ArrayList<ConnectionCallbacks> zzafQ = new ArrayList<>();
    final ArrayList<ConnectionCallbacks> zzafR = new ArrayList<>();
    private final ArrayList<OnConnectionFailedListener> zzafS = new ArrayList<>();
    private volatile boolean zzafT = false;
    private final AtomicInteger zzafU = new AtomicInteger(0);
    private boolean zzafV = false;
    private final Object zzpd = new Object();

    public interface zza {
        boolean isConnected();

        Bundle zzmS();
    }

    public zzk(Looper looper, zza zza2) {
        this.zzafP = zza2;
        this.mHandler = new Handler(looper, this);
    }

    public boolean handleMessage(Message message) {
        if (message.what == 1) {
            ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) message.obj;
            synchronized (this.zzpd) {
                if (this.zzafT && this.zzafP.isConnected() && this.zzafQ.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(this.zzafP.zzmS());
                }
            }
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Don't know how to handle message: ");
        sb.append(message.what);
        Log.wtf("GmsClientEvents", sb.toString(), new Exception());
        return false;
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        boolean contains;
        zzx.zzw(connectionCallbacks);
        synchronized (this.zzpd) {
            contains = this.zzafQ.contains(connectionCallbacks);
        }
        return contains;
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        boolean contains;
        zzx.zzw(onConnectionFailedListener);
        synchronized (this.zzpd) {
            contains = this.zzafS.contains(onConnectionFailedListener);
        }
        return contains;
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzx.zzw(connectionCallbacks);
        synchronized (this.zzpd) {
            if (this.zzafQ.contains(connectionCallbacks)) {
                StringBuilder sb = new StringBuilder();
                sb.append("registerConnectionCallbacks(): listener ");
                sb.append(connectionCallbacks);
                sb.append(" is already registered");
                Log.w("GmsClientEvents", sb.toString());
            } else {
                this.zzafQ.add(connectionCallbacks);
            }
        }
        if (this.zzafP.isConnected()) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(1, connectionCallbacks));
        }
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzw(onConnectionFailedListener);
        synchronized (this.zzpd) {
            if (this.zzafS.contains(onConnectionFailedListener)) {
                StringBuilder sb = new StringBuilder();
                sb.append("registerConnectionFailedListener(): listener ");
                sb.append(onConnectionFailedListener);
                sb.append(" is already registered");
                Log.w("GmsClientEvents", sb.toString());
            } else {
                this.zzafS.add(onConnectionFailedListener);
            }
        }
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzx.zzw(connectionCallbacks);
        synchronized (this.zzpd) {
            if (!this.zzafQ.remove(connectionCallbacks)) {
                StringBuilder sb = new StringBuilder();
                sb.append("unregisterConnectionCallbacks(): listener ");
                sb.append(connectionCallbacks);
                sb.append(" not found");
                Log.w("GmsClientEvents", sb.toString());
            } else if (this.zzafV) {
                this.zzafR.add(connectionCallbacks);
            }
        }
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzw(onConnectionFailedListener);
        synchronized (this.zzpd) {
            if (!this.zzafS.remove(onConnectionFailedListener)) {
                StringBuilder sb = new StringBuilder();
                sb.append("unregisterConnectionFailedListener(): listener ");
                sb.append(onConnectionFailedListener);
                sb.append(" not found");
                Log.w("GmsClientEvents", sb.toString());
            }
        }
    }

    public void zzbG(int i) {
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), (Object) "onUnintentionalDisconnection must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.zzpd) {
            this.zzafV = true;
            ArrayList arrayList = new ArrayList(this.zzafQ);
            int i2 = this.zzafU.get();
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) it.next();
                if (!this.zzafT) {
                    break;
                } else if (this.zzafU.get() != i2) {
                    break;
                } else if (this.zzafQ.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnectionSuspended(i);
                }
            }
            this.zzafR.clear();
            this.zzafV = false;
        }
    }

    public void zzh(Bundle bundle) {
        boolean z = true;
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), (Object) "onConnectionSuccess must only be called on the Handler thread");
        synchronized (this.zzpd) {
            zzx.zzZ(!this.zzafV);
            this.mHandler.removeMessages(1);
            this.zzafV = true;
            if (this.zzafR.size() != 0) {
                z = false;
            }
            zzx.zzZ(z);
            ArrayList arrayList = new ArrayList(this.zzafQ);
            int i = this.zzafU.get();
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) it.next();
                if (!this.zzafT || !this.zzafP.isConnected()) {
                    break;
                } else if (this.zzafU.get() != i) {
                    break;
                } else if (!this.zzafR.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(bundle);
                }
            }
            this.zzafR.clear();
            this.zzafV = false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0054, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void zzi(com.google.android.gms.common.ConnectionResult r6) {
        /*
            r5 = this;
            android.os.Looper r0 = android.os.Looper.myLooper()
            android.os.Handler r1 = r5.mHandler
            android.os.Looper r1 = r1.getLooper()
            r2 = 1
            if (r0 != r1) goto L_0x000f
            r0 = 1
            goto L_0x0010
        L_0x000f:
            r0 = 0
        L_0x0010:
            java.lang.String r1 = "onConnectionFailure must only be called on the Handler thread"
            com.google.android.gms.common.internal.zzx.zza(r0, r1)
            android.os.Handler r0 = r5.mHandler
            r0.removeMessages(r2)
            java.lang.Object r0 = r5.zzpd
            monitor-enter(r0)
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x0057 }
            java.util.ArrayList<com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener> r2 = r5.zzafS     // Catch:{ all -> 0x0057 }
            r1.<init>(r2)     // Catch:{ all -> 0x0057 }
            java.util.concurrent.atomic.AtomicInteger r2 = r5.zzafU     // Catch:{ all -> 0x0057 }
            int r2 = r2.get()     // Catch:{ all -> 0x0057 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0057 }
        L_0x002e:
            boolean r3 = r1.hasNext()     // Catch:{ all -> 0x0057 }
            if (r3 == 0) goto L_0x0055
            java.lang.Object r3 = r1.next()     // Catch:{ all -> 0x0057 }
            com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener r3 = (com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener) r3     // Catch:{ all -> 0x0057 }
            boolean r4 = r5.zzafT     // Catch:{ all -> 0x0057 }
            if (r4 == 0) goto L_0x0053
            java.util.concurrent.atomic.AtomicInteger r4 = r5.zzafU     // Catch:{ all -> 0x0057 }
            int r4 = r4.get()     // Catch:{ all -> 0x0057 }
            if (r4 == r2) goto L_0x0047
            goto L_0x0053
        L_0x0047:
            java.util.ArrayList<com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener> r4 = r5.zzafS     // Catch:{ all -> 0x0057 }
            boolean r4 = r4.contains(r3)     // Catch:{ all -> 0x0057 }
            if (r4 == 0) goto L_0x002e
            r3.onConnectionFailed(r6)     // Catch:{ all -> 0x0057 }
            goto L_0x002e
        L_0x0053:
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            return
        L_0x0055:
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            return
        L_0x0057:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzk.zzi(com.google.android.gms.common.ConnectionResult):void");
    }

    public void zzpk() {
        this.zzafT = false;
        this.zzafU.incrementAndGet();
    }

    public void zzpl() {
        this.zzafT = true;
    }
}
