package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class Feature implements SafeParcelable {
    public static final zze CREATOR = new zze();

    /* renamed from: id */
    public final int f24id;
    final int mVersionCode;
    final Bundle zzQn;

    Feature(int i, int i2, Bundle bundle) {
        this.mVersionCode = i;
        this.f24id = i2;
        this.zzQn = bundle;
    }

    public int describeContents() {
        zze zze = CREATOR;
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze zze = CREATOR;
        zze.zza(this, parcel, i);
    }
}
