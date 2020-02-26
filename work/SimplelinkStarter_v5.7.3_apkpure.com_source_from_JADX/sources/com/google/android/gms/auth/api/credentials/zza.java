package com.google.android.gms.auth.api.credentials;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;

public class zza implements Creator<Credential> {
    static void zza(Credential credential, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, credential.getId(), false);
        zzb.zzc(parcel, 1000, credential.mVersionCode);
        zzb.zza(parcel, 2, credential.getName(), false);
        zzb.zza(parcel, 3, (Parcelable) credential.getProfilePictureUri(), i, false);
        zzb.zzc(parcel, 4, credential.getIdTokens(), false);
        zzb.zza(parcel, 5, credential.getPassword(), false);
        zzb.zza(parcel, 6, credential.getAccountType(), false);
        zzb.zza(parcel, 7, credential.getGeneratedPassword(), false);
        zzb.zza(parcel, 8, credential.zzlI(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzE */
    public Credential createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String str = null;
        String str2 = null;
        Uri uri = null;
        List list = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        int i = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int zzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    case 1:
                        str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        break;
                    case 2:
                        str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        break;
                    case 3:
                        uri = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, Uri.CREATOR);
                        break;
                    case 4:
                        list = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao, IdToken.CREATOR);
                        break;
                    case 5:
                        str3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        break;
                    case 6:
                        str4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        break;
                    case 7:
                        str5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        break;
                    case 8:
                        str6 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        break;
                    default:
                        com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                        break;
                }
            } else {
                i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            Credential credential = new Credential(i, str, str2, uri, list, str3, str4, str5, str6);
            return credential;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzav */
    public Credential[] newArray(int i) {
        return new Credential[i];
    }
}
