package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zze implements Creator<Feature> {
    static void zza(Feature feature, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, feature.f24id);
        zzb.zzc(parcel, 1000, feature.mVersionCode);
        zzb.zza(parcel, 2, feature.zzQn, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzah */
    public Feature[] newArray(int i) {
        return new Feature[i];
    }

    /* renamed from: zzu */
    public Feature createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        int i = 0;
        Bundle bundle = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i2 = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                bundle = zza.zzr(parcel, zzao);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new Feature(i, i2, bundle);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }
}
