package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.GoogleApiClient.ServerAuthCodeCallbacks;
import com.google.android.gms.common.internal.zzx;

public final class zzqx implements Optional {
    public static final zzqx zzaUZ = new zza().zzCi();
    private final boolean zzTi;
    private final boolean zzTk;
    private final String zzTl;
    private final boolean zzaVa;
    private final ServerAuthCodeCallbacks zzaVb;
    private final boolean zzaVc;

    public static final class zza {
        private String zzaSe;
        private boolean zzaVd;
        private boolean zzaVe;
        private ServerAuthCodeCallbacks zzaVf;
        private boolean zzaVg;
        private boolean zzaVh;

        private String zzet(String str) {
            zzx.zzw(str);
            String str2 = this.zzaSe;
            zzx.zzb(str2 == null || str2.equals(str), (Object) "two different server client ids provided");
            return str;
        }

        public zzqx zzCi() {
            zzqx zzqx = new zzqx(this.zzaVd, this.zzaVe, this.zzaSe, this.zzaVf, this.zzaVg, this.zzaVh);
            return zzqx;
        }

        public zza zza(String str, ServerAuthCodeCallbacks serverAuthCodeCallbacks) {
            this.zzaVd = true;
            this.zzaVe = true;
            this.zzaSe = zzet(str);
            this.zzaVf = (ServerAuthCodeCallbacks) zzx.zzw(serverAuthCodeCallbacks);
            return this;
        }
    }

    private zzqx(boolean z, boolean z2, String str, ServerAuthCodeCallbacks serverAuthCodeCallbacks, boolean z3, boolean z4) {
        this.zzaVa = z;
        this.zzTi = z2;
        this.zzTl = str;
        this.zzaVb = serverAuthCodeCallbacks;
        this.zzaVc = z3;
        this.zzTk = z4;
    }

    public boolean zzCf() {
        return this.zzaVa;
    }

    public ServerAuthCodeCallbacks zzCg() {
        return this.zzaVb;
    }

    public boolean zzCh() {
        return this.zzaVc;
    }

    public boolean zzlY() {
        return this.zzTi;
    }

    public boolean zzma() {
        return this.zzTk;
    }

    public String zzmb() {
        return this.zzTl;
    }
}
