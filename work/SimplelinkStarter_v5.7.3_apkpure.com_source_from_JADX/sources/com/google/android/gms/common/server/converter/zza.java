package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<ConverterWrapper> {
    static void zza(ConverterWrapper converterWrapper, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, converterWrapper.getVersionCode());
        zzb.zza(parcel, 2, (Parcelable) converterWrapper.zzpy(), i, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzas */
    public ConverterWrapper createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int i = 0;
        StringToIntConverter stringToIntConverter = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int zzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao);
            if (zzbM == 1) {
                i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            } else if (zzbM != 2) {
                com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
            } else {
                stringToIntConverter = (StringToIntConverter) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, (Creator<T>) StringToIntConverter.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new ConverterWrapper(i, stringToIntConverter);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbQ */
    public ConverterWrapper[] newArray(int i) {
        return new ConverterWrapper[i];
    }
}
