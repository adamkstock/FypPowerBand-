package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzi implements Creator<RegisterSectionInfo> {
    static void zza(RegisterSectionInfo registerSectionInfo, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, registerSectionInfo.name, false);
        zzb.zzc(parcel, 1000, registerSectionInfo.mVersionCode);
        zzb.zza(parcel, 2, registerSectionInfo.zzQF, false);
        zzb.zza(parcel, 3, registerSectionInfo.zzQG);
        zzb.zzc(parcel, 4, registerSectionInfo.weight);
        zzb.zza(parcel, 5, registerSectionInfo.zzQH);
        zzb.zza(parcel, 6, registerSectionInfo.zzQI, false);
        zzb.zza(parcel, 7, (T[]) registerSectionInfo.zzQJ, i, false);
        zzb.zza(parcel, 8, registerSectionInfo.zzQK, false);
        zzb.zza(parcel, 11, registerSectionInfo.zzQL, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzam */
    public RegisterSectionInfo[] newArray(int i) {
        return new RegisterSectionInfo[i];
    }

    /* renamed from: zzx */
    public RegisterSectionInfo createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzap = zza.zzap(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        Feature[] featureArr = null;
        int[] iArr = null;
        String str4 = null;
        int i = 0;
        boolean z = false;
        int i2 = 1;
        boolean z2 = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 11) {
                str4 = zza.zzp(parcel2, zzao);
            } else if (zzbM != 1000) {
                switch (zzbM) {
                    case 1:
                        str = zza.zzp(parcel2, zzao);
                        break;
                    case 2:
                        str2 = zza.zzp(parcel2, zzao);
                        break;
                    case 3:
                        z = zza.zzc(parcel2, zzao);
                        break;
                    case 4:
                        i2 = zza.zzg(parcel2, zzao);
                        break;
                    case 5:
                        z2 = zza.zzc(parcel2, zzao);
                        break;
                    case 6:
                        str3 = zza.zzp(parcel2, zzao);
                        break;
                    case 7:
                        featureArr = (Feature[]) zza.zzb(parcel2, zzao, Feature.CREATOR);
                        break;
                    case 8:
                        iArr = zza.zzv(parcel2, zzao);
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
            RegisterSectionInfo registerSectionInfo = new RegisterSectionInfo(i, str, str2, z, i2, z2, str3, featureArr, iArr, str4);
            return registerSectionInfo;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel2);
    }
}
