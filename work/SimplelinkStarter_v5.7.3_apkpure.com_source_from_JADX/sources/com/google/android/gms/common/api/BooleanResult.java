package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;

public class BooleanResult implements Result {
    private final Status zzSC;
    private final boolean zzaaE;

    public BooleanResult(Status status, boolean z) {
        this.zzSC = (Status) zzx.zzb(status, (Object) "Status must not be null");
        this.zzaaE = z;
    }

    public final boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BooleanResult)) {
            return false;
        }
        BooleanResult booleanResult = (BooleanResult) obj;
        if (!this.zzSC.equals(booleanResult.zzSC) || this.zzaaE != booleanResult.zzaaE) {
            z = false;
        }
        return z;
    }

    public Status getStatus() {
        return this.zzSC;
    }

    public boolean getValue() {
        return this.zzaaE;
    }

    public final int hashCode() {
        return ((527 + this.zzSC.hashCode()) * 31) + (this.zzaaE ? 1 : 0);
    }
}
