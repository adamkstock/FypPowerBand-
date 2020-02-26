package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Set;

public class AuthAccountRequest implements SafeParcelable {
    public static final Creator<AuthAccountRequest> CREATOR = new zzc();
    final int mVersionCode;
    final IBinder zzaeH;
    final Scope[] zzaeI;

    AuthAccountRequest(int i, IBinder iBinder, Scope[] scopeArr) {
        this.mVersionCode = i;
        this.zzaeH = iBinder;
        this.zzaeI = scopeArr;
    }

    public AuthAccountRequest(zzp zzp, Set<Scope> set) {
        this(1, zzp.asBinder(), (Scope[]) set.toArray(new Scope[set.size()]));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
