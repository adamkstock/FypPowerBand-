package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzc implements Creator<AuthAccountRequest> {
    static void zza(AuthAccountRequest authAccountRequest, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, authAccountRequest.mVersionCode);
        zzb.zza(parcel, 2, authAccountRequest.zzaeH, false);
        zzb.zza(parcel, 3, (T[]) authAccountRequest.zzaeI, i, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzai */
    public AuthAccountRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        IBinder iBinder = null;
        int i = 0;
        Scope[] scopeArr = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                iBinder = zza.zzq(parcel, zzao);
            } else if (zzbM != 3) {
                zza.zzb(parcel, zzao);
            } else {
                scopeArr = (Scope[]) zza.zzb(parcel, zzao, Scope.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new AuthAccountRequest(i, iBinder, scopeArr);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbB */
    public AuthAccountRequest[] newArray(int i) {
        return new AuthAccountRequest[i];
    }
}
