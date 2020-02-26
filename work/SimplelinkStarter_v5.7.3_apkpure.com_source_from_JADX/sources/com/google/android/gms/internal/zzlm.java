package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.internal.zzx;

public final class zzlm<L> {
    private volatile L mListener;
    private final zza zzacG;

    private final class zza extends Handler {
        public zza(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            boolean z = true;
            if (message.what != 1) {
                z = false;
            }
            zzx.zzaa(z);
            zzlm.this.zzb((zzb) message.obj);
        }
    }

    public interface zzb<L> {
        void zznN();

        void zzq(L l);
    }

    public zzlm(Looper looper, L l) {
        this.zzacG = new zza<>(looper);
        this.mListener = zzx.zzb(l, (Object) "Listener must not be null");
    }

    public void clear() {
        this.mListener = null;
    }

    public void zza(zzb<? super L> zzb2) {
        zzx.zzb(zzb2, (Object) "Notifier must not be null");
        this.zzacG.sendMessage(this.zzacG.obtainMessage(1, zzb2));
    }

    /* access modifiers changed from: 0000 */
    public void zzb(zzb<? super L> zzb2) {
        L l = this.mListener;
        if (l == null) {
            zzb2.zznN();
            return;
        }
        try {
            zzb2.zzq(l);
        } catch (RuntimeException e) {
            zzb2.zznN();
            throw e;
        }
    }
}
