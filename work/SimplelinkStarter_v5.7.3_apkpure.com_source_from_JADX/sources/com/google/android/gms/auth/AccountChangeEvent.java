package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;

public class AccountChangeEvent implements SafeParcelable {
    public static final Creator<AccountChangeEvent> CREATOR = new zza();
    final int mVersion;
    final long zzRr;
    final String zzRs;
    final int zzRt;
    final int zzRu;
    final String zzRv;

    AccountChangeEvent(int i, long j, String str, int i2, int i3, String str2) {
        this.mVersion = i;
        this.zzRr = j;
        this.zzRs = (String) zzx.zzw(str);
        this.zzRt = i2;
        this.zzRu = i3;
        this.zzRv = str2;
    }

    public AccountChangeEvent(long j, String str, int i, int i2, String str2) {
        this.mVersion = 1;
        this.zzRr = j;
        this.zzRs = (String) zzx.zzw(str);
        this.zzRt = i;
        this.zzRu = i2;
        this.zzRv = str2;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AccountChangeEvent)) {
            return false;
        }
        AccountChangeEvent accountChangeEvent = (AccountChangeEvent) obj;
        if (!(this.mVersion == accountChangeEvent.mVersion && this.zzRr == accountChangeEvent.zzRr && zzw.equal(this.zzRs, accountChangeEvent.zzRs) && this.zzRt == accountChangeEvent.zzRt && this.zzRu == accountChangeEvent.zzRu && zzw.equal(this.zzRv, accountChangeEvent.zzRv))) {
            z = false;
        }
        return z;
    }

    public String getAccountName() {
        return this.zzRs;
    }

    public String getChangeData() {
        return this.zzRv;
    }

    public int getChangeType() {
        return this.zzRt;
    }

    public int getEventIndex() {
        return this.zzRu;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.mVersion), Long.valueOf(this.zzRr), this.zzRs, Integer.valueOf(this.zzRt), Integer.valueOf(this.zzRu), this.zzRv);
    }

    public String toString() {
        int i = this.zzRt;
        String str = i != 1 ? i != 2 ? i != 3 ? i != 4 ? "UNKNOWN" : "RENAMED_TO" : "RENAMED_FROM" : "REMOVED" : "ADDED";
        StringBuilder sb = new StringBuilder();
        sb.append("AccountChangeEvent {accountName = ");
        sb.append(this.zzRs);
        sb.append(", changeType = ");
        sb.append(str);
        sb.append(", changeData = ");
        sb.append(this.zzRv);
        sb.append(", eventIndex = ");
        sb.append(this.zzRu);
        sb.append("}");
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }
}
