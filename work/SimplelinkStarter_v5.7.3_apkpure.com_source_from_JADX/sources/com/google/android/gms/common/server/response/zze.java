package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zze implements Creator<SafeParcelResponse> {
    static void zza(SafeParcelResponse safeParcelResponse, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, safeParcelResponse.getVersionCode());
        zzb.zza(parcel, 2, safeParcelResponse.zzpV(), false);
        zzb.zza(parcel, 3, (Parcelable) safeParcelResponse.zzpW(), i, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzaz */
    public SafeParcelResponse createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        Parcel parcel2 = null;
        int i = 0;
        FieldMappingDictionary fieldMappingDictionary = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                parcel2 = zza.zzE(parcel, zzao);
            } else if (zzbM != 3) {
                zza.zzb(parcel, zzao);
            } else {
                fieldMappingDictionary = (FieldMappingDictionary) zza.zza(parcel, zzao, (Creator<T>) FieldMappingDictionary.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new SafeParcelResponse(i, parcel2, fieldMappingDictionary);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbX */
    public SafeParcelResponse[] newArray(int i) {
        return new SafeParcelResponse[i];
    }
}
