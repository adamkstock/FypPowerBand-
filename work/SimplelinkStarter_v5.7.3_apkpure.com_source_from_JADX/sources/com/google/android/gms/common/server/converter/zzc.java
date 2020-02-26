package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.server.converter.StringToIntConverter.Entry;

public class zzc implements Creator<Entry> {
    static void zza(Entry entry, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, entry.versionCode);
        zzb.zza(parcel, 2, entry.zzagS, false);
        zzb.zzc(parcel, 3, entry.zzagT);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzau */
    public Entry createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        int i = 0;
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM != 3) {
                zza.zzb(parcel, zzao);
            } else {
                i2 = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new Entry(i, str, i2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbS */
    public Entry[] newArray(int i) {
        return new Entry[i];
    }
}
