package com.google.android.gms.auth.api.signin;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import java.util.ArrayList;

public class zzb implements Creator<FacebookSignInConfig> {
    static void zza(FacebookSignInConfig facebookSignInConfig, Parcel parcel, int i) {
        int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, facebookSignInConfig.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) facebookSignInConfig.zzlR(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 3, facebookSignInConfig.zzlS(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzP */
    public FacebookSignInConfig createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        Intent intent = null;
        int i = 0;
        ArrayList arrayList = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                intent = (Intent) zza.zza(parcel, zzao, Intent.CREATOR);
            } else if (zzbM != 3) {
                zza.zzb(parcel, zzao);
            } else {
                arrayList = zza.zzD(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new FacebookSignInConfig(i, intent, arrayList);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaG */
    public FacebookSignInConfig[] newArray(int i) {
        return new FacebookSignInConfig[i];
    }
}
