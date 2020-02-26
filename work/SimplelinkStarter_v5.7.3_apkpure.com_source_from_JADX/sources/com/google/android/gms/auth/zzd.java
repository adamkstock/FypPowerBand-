package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;

public class zzd implements Creator<TokenData> {
    static void zza(TokenData tokenData, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, tokenData.mVersionCode);
        zzb.zza(parcel, 2, tokenData.getToken(), false);
        zzb.zza(parcel, 3, tokenData.zzlA(), false);
        zzb.zza(parcel, 4, tokenData.zzlB());
        zzb.zza(parcel, 5, tokenData.zzlC());
        zzb.zzb(parcel, 6, tokenData.zzlD(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzC */
    public TokenData createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        Long l = null;
        List list = null;
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case 1:
                    i = zza.zzg(parcel, zzao);
                    break;
                case 2:
                    str = zza.zzp(parcel, zzao);
                    break;
                case 3:
                    l = zza.zzj(parcel, zzao);
                    break;
                case 4:
                    z = zza.zzc(parcel, zzao);
                    break;
                case 5:
                    z2 = zza.zzc(parcel, zzao);
                    break;
                case 6:
                    list = zza.zzD(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            TokenData tokenData = new TokenData(i, str, l, z, z2, list);
            return tokenData;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzat */
    public TokenData[] newArray(int i) {
        return new TokenData[i];
    }
}
