package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.google.android.gms.common.server.response.FieldMappingDictionary.FieldMapPair;

public class zzb implements Creator<FieldMapPair> {
    static void zza(FieldMapPair fieldMapPair, Parcel parcel, int i) {
        int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fieldMapPair.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, fieldMapPair.key, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) fieldMapPair.zzahi, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzaw */
    public FieldMapPair createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        int i = 0;
        Field field = null;
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
                field = (Field) zza.zza(parcel, zzao, (Creator<T>) Field.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new FieldMapPair(i, str, field);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbU */
    public FieldMapPair[] newArray(int i) {
        return new FieldMapPair[i];
    }
}
