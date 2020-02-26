package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzc implements Creator<DocumentId> {
    static void zza(DocumentId documentId, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, documentId.zzQe, false);
        zzb.zzc(parcel, 1000, documentId.mVersionCode);
        zzb.zza(parcel, 2, documentId.zzQf, false);
        zzb.zza(parcel, 3, documentId.zzQg, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzaf */
    public DocumentId[] newArray(int i) {
        return new DocumentId[i];
    }

    /* renamed from: zzs */
    public DocumentId createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        int i = 0;
        String str2 = null;
        String str3 = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM == 2) {
                str2 = zza.zzp(parcel, zzao);
            } else if (zzbM == 3) {
                str3 = zza.zzp(parcel, zzao);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new DocumentId(i, str, str2, str3);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }
}
