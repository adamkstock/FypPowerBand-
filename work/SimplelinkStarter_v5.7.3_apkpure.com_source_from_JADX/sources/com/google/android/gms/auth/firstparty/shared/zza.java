package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<FACLConfig> {
    static void zza(FACLConfig fACLConfig, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, fACLConfig.version);
        zzb.zza(parcel, 2, fACLConfig.zzTx);
        zzb.zza(parcel, 3, fACLConfig.zzTy, false);
        zzb.zza(parcel, 4, fACLConfig.zzTz);
        zzb.zza(parcel, 5, fACLConfig.zzTA);
        zzb.zza(parcel, 6, fACLConfig.zzTB);
        zzb.zza(parcel, 7, fACLConfig.zzTC);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzT */
    public FACLConfig createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String str = null;
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case 2:
                    z = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao);
                    break;
                case 3:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                case 4:
                    z2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao);
                    break;
                case 5:
                    z3 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao);
                    break;
                case 6:
                    z4 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao);
                    break;
                case 7:
                    z5 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            FACLConfig fACLConfig = new FACLConfig(i, z, str, z2, z3, z4, z5);
            return fACLConfig;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaK */
    public FACLConfig[] newArray(int i) {
        return new FACLConfig[i];
    }
}
