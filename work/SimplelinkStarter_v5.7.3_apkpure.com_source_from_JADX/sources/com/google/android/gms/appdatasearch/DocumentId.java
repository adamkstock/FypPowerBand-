package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DocumentId implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    final int mVersionCode;
    final String zzQe;
    final String zzQf;
    final String zzQg;

    DocumentId(int i, String str, String str2, String str3) {
        this.mVersionCode = i;
        this.zzQe = str;
        this.zzQf = str2;
        this.zzQg = str3;
    }

    public DocumentId(String str, String str2, String str3) {
        this(1, str, str2, str3);
    }

    public int describeContents() {
        zzc zzc = CREATOR;
        return 0;
    }

    public String toString() {
        return String.format("DocumentId[packageName=%s, corpusName=%s, uri=%s]", new Object[]{this.zzQe, this.zzQf, this.zzQg});
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc zzc = CREATOR;
        zzc.zza(this, parcel, i);
    }
}
