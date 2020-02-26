package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzd implements Creator<DocumentSection> {
    static void zza(DocumentSection documentSection, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, documentSection.zzQj, false);
        zzb.zzc(parcel, 1000, documentSection.mVersionCode);
        zzb.zza(parcel, 3, (Parcelable) documentSection.zzQk, i, false);
        zzb.zzc(parcel, 4, documentSection.zzQl);
        zzb.zza(parcel, 5, documentSection.zzQm, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzag */
    public DocumentSection[] newArray(int i) {
        return new DocumentSection[i];
    }

    /* renamed from: zzt */
    public DocumentSection createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        RegisterSectionInfo registerSectionInfo = null;
        byte[] bArr = null;
        int i = 0;
        int i2 = -1;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM == 1000) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 3) {
                registerSectionInfo = (RegisterSectionInfo) zza.zza(parcel, zzao, (Creator<T>) RegisterSectionInfo.CREATOR);
            } else if (zzbM == 4) {
                i2 = zza.zzg(parcel, zzao);
            } else if (zzbM != 5) {
                zza.zzb(parcel, zzao);
            } else {
                bArr = zza.zzs(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            DocumentSection documentSection = new DocumentSection(i, str, registerSectionInfo, i2, bArr);
            return documentSection;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }
}
