package com.google.android.gms.internal;

import com.google.android.gms.internal.zzry;
import java.io.IOException;

public abstract class zzry<M extends zzry<M>> extends zzse {
    protected zzsa zzbik;

    /* access modifiers changed from: protected */
    public int zzB() {
        if (this.zzbik == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.zzbik.size(); i2++) {
            i += this.zzbik.zzlS(i2).zzB();
        }
        return i;
    }

    /* renamed from: zzFF */
    public M zzFG() throws CloneNotSupportedException {
        M m = (zzry) super.clone();
        zzsc.zza(this, (zzry) m);
        return m;
    }

    public final <T> T zza(zzrz<M, T> zzrz) {
        zzsa zzsa = this.zzbik;
        T t = null;
        if (zzsa == null) {
            return null;
        }
        zzsb zzlR = zzsa.zzlR(zzsh.zzlV(zzrz.tag));
        if (zzlR != null) {
            t = zzlR.zzb(zzrz);
        }
        return t;
    }

    public void zza(zzrx zzrx) throws IOException {
        if (this.zzbik != null) {
            for (int i = 0; i < this.zzbik.size(); i++) {
                this.zzbik.zzlS(i).zza(zzrx);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final boolean zza(zzrw zzrw, int i) throws IOException {
        int position = zzrw.getPosition();
        if (!zzrw.zzlA(i)) {
            return false;
        }
        int zzlV = zzsh.zzlV(i);
        zzsg zzsg = new zzsg(i, zzrw.zzx(position, zzrw.getPosition() - position));
        zzsb zzsb = null;
        zzsa zzsa = this.zzbik;
        if (zzsa == null) {
            this.zzbik = new zzsa();
        } else {
            zzsb = zzsa.zzlR(zzlV);
        }
        if (zzsb == null) {
            zzsb = new zzsb();
            this.zzbik.zza(zzlV, zzsb);
        }
        zzsb.zza(zzsg);
        return true;
    }
}
