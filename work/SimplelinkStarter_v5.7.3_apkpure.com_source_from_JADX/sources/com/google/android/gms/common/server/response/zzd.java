package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.server.response.FieldMappingDictionary.Entry;
import com.google.android.gms.common.server.response.FieldMappingDictionary.FieldMapPair;
import java.util.ArrayList;

public class zzd implements Creator<Entry> {
    static void zza(Entry entry, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, entry.versionCode);
        zzb.zza(parcel, 2, entry.className, false);
        zzb.zzc(parcel, 3, entry.zzahh, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzay */
    public Entry createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        int i = 0;
        ArrayList arrayList = null;
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
                arrayList = zza.zzc(parcel, zzao, FieldMapPair.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new Entry(i, str, arrayList);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbW */
    public Entry[] newArray(int i) {
        return new Entry[i];
    }
}
