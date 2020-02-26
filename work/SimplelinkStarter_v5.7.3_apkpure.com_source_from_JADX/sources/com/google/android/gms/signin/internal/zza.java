package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<AuthAccountResult> {
    static void zza(AuthAccountResult authAccountResult, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, authAccountResult.mVersionCode);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzgB */
    public AuthAccountResult createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int i = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            if (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao) != 1) {
                com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
            } else {
                i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new AuthAccountResult(i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzjo */
    public AuthAccountResult[] newArray(int i) {
        return new AuthAccountResult[i];
    }
}
