package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;

public class zzb implements Creator<FACLData> {
    static void zza(FACLData fACLData, Parcel parcel, int i) {
        int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fACLData.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) fACLData.zzTD, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, fACLData.zzTE, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, fACLData.zzTF);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, fACLData.zzTG, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzU */
    public FACLData createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        FACLConfig fACLConfig = null;
        String str = null;
        String str2 = null;
        int i = 0;
        boolean z = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                fACLConfig = (FACLConfig) zza.zza(parcel, zzao, (Creator<T>) FACLConfig.CREATOR);
            } else if (zzbM == 3) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM == 4) {
                z = zza.zzc(parcel, zzao);
            } else if (zzbM != 5) {
                zza.zzb(parcel, zzao);
            } else {
                str2 = zza.zzp(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            FACLData fACLData = new FACLData(i, fACLConfig, str, z, str2);
            return fACLData;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaL */
    public FACLData[] newArray(int i) {
        return new FACLData[i];
    }
}
