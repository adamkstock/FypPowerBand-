package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

final class zzsg {
    final int tag;
    final byte[] zzbiw;

    zzsg(int i, byte[] bArr) {
        this.tag = i;
        this.zzbiw = bArr;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzsg)) {
            return false;
        }
        zzsg zzsg = (zzsg) obj;
        if (this.tag != zzsg.tag || !Arrays.equals(this.zzbiw, zzsg.zzbiw)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((527 + this.tag) * 31) + Arrays.hashCode(this.zzbiw);
    }

    /* access modifiers changed from: 0000 */
    public int zzB() {
        return zzrx.zzlO(this.tag) + 0 + this.zzbiw.length;
    }

    /* access modifiers changed from: 0000 */
    public void zza(zzrx zzrx) throws IOException {
        zzrx.zzlN(this.tag);
        zzrx.zzF(this.zzbiw);
    }
}
