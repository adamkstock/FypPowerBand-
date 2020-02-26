package com.google.android.gms.search;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class GoogleNowAuthState implements SafeParcelable {
    public static final Creator<GoogleNowAuthState> CREATOR = new zza();
    final int mVersionCode;
    String zzaUM;
    String zzaUN;
    long zzaUO;

    GoogleNowAuthState(int i, String str, String str2, long j) {
        this.mVersionCode = i;
        this.zzaUM = str;
        this.zzaUN = str2;
        this.zzaUO = j;
    }

    public int describeContents() {
        return 0;
    }

    public String getAccessToken() {
        return this.zzaUN;
    }

    public String getAuthCode() {
        return this.zzaUM;
    }

    public long getNextAllowedTimeMillis() {
        return this.zzaUO;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("mAuthCode = ");
        sb.append(this.zzaUM);
        sb.append("\nmAccessToken = ");
        sb.append(this.zzaUN);
        sb.append("\nmNextAllowedTimeMillis = ");
        sb.append(this.zzaUO);
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }
}
