package com.google.android.gms.common.server;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<FavaDiagnosticsEntity> {
    static void zza(FavaDiagnosticsEntity favaDiagnosticsEntity, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, favaDiagnosticsEntity.mVersionCode);
        zzb.zza(parcel, 2, favaDiagnosticsEntity.zzagM, false);
        zzb.zzc(parcel, 3, favaDiagnosticsEntity.zzagN);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzar */
    public FavaDiagnosticsEntity createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int i = 0;
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int zzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao);
            if (zzbM == 1) {
                i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
            } else if (zzbM != 3) {
                com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
            } else {
                i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new FavaDiagnosticsEntity(i, str, i2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbP */
    public FavaDiagnosticsEntity[] newArray(int i) {
        return new FavaDiagnosticsEntity[i];
    }
}
