package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlb.zza;

public class zzlf implements zzlj {
    private final zzli zzabr;

    public zzlf(zzli zzli) {
        this.zzabr = zzli;
    }

    private <A extends zzb> void zza(zzf<A> zzf) throws DeadObjectException {
        this.zzabr.zzb(zzf);
        zzb zza = this.zzabr.zza(zzf.zznx());
        if (zza.isConnected() || !this.zzabr.zzach.containsKey(zzf.zznx())) {
            zzf.zzb(zza);
        } else {
            zzf.zzv(new Status(17));
        }
    }

    public void begin() {
        while (!this.zzabr.zzaca.isEmpty()) {
            try {
                zza((zzf) this.zzabr.zzaca.remove());
            } catch (DeadObjectException e) {
                Log.w("GACConnected", "Service died while flushing queue", e);
            }
        }
    }

    public void connect() {
    }

    public void disconnect() {
        this.zzabr.zzach.clear();
        this.zzabr.zznY();
        this.zzabr.zzg(null);
        this.zzabr.zzabZ.zzpk();
    }

    public String getName() {
        return "CONNECTED";
    }

    public void onConnected(Bundle bundle) {
    }

    public void onConnectionSuspended(int i) {
        if (i == 1) {
            this.zzabr.zzoe();
        }
        for (zzf zzw : this.zzabr.zzacm) {
            zzw.zzw(new Status(8, "The connection to Google Play services was lost"));
        }
        this.zzabr.zzg(null);
        this.zzabr.zzabZ.zzbG(i);
        this.zzabr.zzabZ.zzpk();
        if (i == 2) {
            this.zzabr.connect();
        }
    }

    public <A extends zzb, R extends Result, T extends zza<R, A>> T zza(T t) {
        return zzb(t);
    }

    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
    }

    public <A extends zzb, T extends zza<? extends Result, A>> T zzb(T t) {
        try {
            zza((zzf<A>) t);
        } catch (DeadObjectException unused) {
            this.zzabr.zza((zzb) new zzb(this) {
                public void zznO() {
                    zzlf.this.onConnectionSuspended(1);
                }
            });
        }
        return t;
    }
}
