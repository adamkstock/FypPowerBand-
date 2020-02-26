package com.google.android.gms.search;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<GoogleNowAuthState> {
    static void zza(GoogleNowAuthState googleNowAuthState, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, googleNowAuthState.getAuthCode(), false);
        zzb.zzc(parcel, 1000, googleNowAuthState.mVersionCode);
        zzb.zza(parcel, 2, googleNowAuthState.getAccessToken(), false);
        zzb.zza(parcel, 3, googleNowAuthState.getNextAllowedTimeMillis());
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzgA */
    public GoogleNowAuthState createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String str = null;
        String str2 = null;
        long j = 0;
        int i = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int zzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao);
            if (zzbM == 1) {
                str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
            } else if (zzbM == 2) {
                str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
            } else if (zzbM == 3) {
                j = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzao);
            } else if (zzbM != 1000) {
                com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
            } else {
                i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            GoogleNowAuthState googleNowAuthState = new GoogleNowAuthState(i, str, str2, j);
            return googleNowAuthState;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzjn */
    public GoogleNowAuthState[] newArray(int i) {
        return new GoogleNowAuthState[i];
    }
}
