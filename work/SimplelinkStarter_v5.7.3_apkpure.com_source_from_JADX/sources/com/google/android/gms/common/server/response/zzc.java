package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.server.response.FieldMappingDictionary.Entry;
import java.util.ArrayList;

public class zzc implements Creator<FieldMappingDictionary> {
    static void zza(FieldMappingDictionary fieldMappingDictionary, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, fieldMappingDictionary.getVersionCode());
        zzb.zzc(parcel, 2, fieldMappingDictionary.zzpS(), false);
        zzb.zza(parcel, 3, fieldMappingDictionary.zzpT(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzax */
    public FieldMappingDictionary createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        ArrayList arrayList = null;
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                arrayList = zza.zzc(parcel, zzao, Entry.CREATOR);
            } else if (zzbM != 3) {
                zza.zzb(parcel, zzao);
            } else {
                str = zza.zzp(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new FieldMappingDictionary(i, arrayList, str);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbV */
    public FieldMappingDictionary[] newArray(int i) {
        return new FieldMappingDictionary[i];
    }
}
