package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzj implements Creator<UsageInfo> {
    static void zza(UsageInfo usageInfo, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, (Parcelable) usageInfo.zzQU, i, false);
        zzb.zzc(parcel, 1000, usageInfo.mVersionCode);
        zzb.zza(parcel, 2, usageInfo.zzQV);
        zzb.zzc(parcel, 3, usageInfo.zzQW);
        zzb.zza(parcel, 4, usageInfo.zzub, false);
        zzb.zza(parcel, 5, (Parcelable) usageInfo.zzQX, i, false);
        zzb.zza(parcel, 6, usageInfo.zzQY);
        zzb.zzc(parcel, 7, usageInfo.zzQZ);
        zzb.zzc(parcel, 8, usageInfo.zzRa);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzap */
    public UsageInfo[] newArray(int i) {
        return new UsageInfo[i];
    }

    /* renamed from: zzy */
    public UsageInfo createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzap = zza.zzap(parcel);
        DocumentId documentId = null;
        String str = null;
        DocumentContents documentContents = null;
        long j = 0;
        int i = 0;
        int i2 = 0;
        boolean z = false;
        int i3 = -1;
        int i4 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    case 1:
                        documentId = (DocumentId) zza.zza(parcel2, zzao, (Creator<T>) DocumentId.CREATOR);
                        break;
                    case 2:
                        j = zza.zzi(parcel2, zzao);
                        break;
                    case 3:
                        i2 = zza.zzg(parcel2, zzao);
                        break;
                    case 4:
                        str = zza.zzp(parcel2, zzao);
                        break;
                    case 5:
                        documentContents = (DocumentContents) zza.zza(parcel2, zzao, (Creator<T>) DocumentContents.CREATOR);
                        break;
                    case 6:
                        z = zza.zzc(parcel2, zzao);
                        break;
                    case 7:
                        i3 = zza.zzg(parcel2, zzao);
                        break;
                    case 8:
                        i4 = zza.zzg(parcel2, zzao);
                        break;
                    default:
                        zza.zzb(parcel2, zzao);
                        break;
                }
            } else {
                i = zza.zzg(parcel2, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            UsageInfo usageInfo = new UsageInfo(i, documentId, j, i2, str, documentContents, z, i3, i4);
            return usageInfo;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel2);
    }
}
