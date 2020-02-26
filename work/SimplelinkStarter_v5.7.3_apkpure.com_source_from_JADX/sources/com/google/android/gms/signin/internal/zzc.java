package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;

public class zzc implements Creator<CheckServerAuthResult> {
    static void zza(CheckServerAuthResult checkServerAuthResult, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, checkServerAuthResult.mVersionCode);
        zzb.zza(parcel, 2, checkServerAuthResult.zzaVi);
        zzb.zzc(parcel, 3, checkServerAuthResult.zzaVj, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzgC */
    public CheckServerAuthResult createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        int i = 0;
        List list = null;
        boolean z = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                z = zza.zzc(parcel, zzao);
            } else if (zzbM != 3) {
                zza.zzb(parcel, zzao);
            } else {
                list = zza.zzc(parcel, zzao, Scope.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new CheckServerAuthResult(i, z, list);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzjp */
    public CheckServerAuthResult[] newArray(int i) {
        return new CheckServerAuthResult[i];
    }
}
