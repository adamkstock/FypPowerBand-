package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzd implements Creator<IdToken> {
    static void zza(IdToken idToken, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, idToken.getAccountType(), false);
        zzb.zzc(parcel, 1000, idToken.mVersionCode);
        zzb.zza(parcel, 2, idToken.getIdToken(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzH */
    public IdToken createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        int i = 0;
        String str2 = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM == 2) {
                str2 = zza.zzp(parcel, zzao);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new IdToken(i, str, str2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzay */
    public IdToken[] newArray(int i) {
        return new IdToken[i];
    }
}
