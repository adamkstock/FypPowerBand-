package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class Scope implements SafeParcelable {
    public static final Creator<Scope> CREATOR = new zzc();
    final int mVersionCode;
    private final String zzaba;

    Scope(int i, String str) {
        zzx.zzh(str, "scopeUri must not be null or empty");
        this.mVersionCode = i;
        this.zzaba = str;
    }

    public Scope(String str) {
        this(1, str);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Scope)) {
            return false;
        }
        return this.zzaba.equals(((Scope) obj).zzaba);
    }

    public int hashCode() {
        return this.zzaba.hashCode();
    }

    public String toString() {
        return this.zzaba;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }

    public String zznG() {
        return this.zzaba;
    }
}
