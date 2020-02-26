package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<EmailSignInConfig> {
    static void zza(EmailSignInConfig emailSignInConfig, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, emailSignInConfig.versionCode);
        zzb.zza(parcel, 2, (Parcelable) emailSignInConfig.zzlO(), i, false);
        zzb.zza(parcel, 3, emailSignInConfig.zzlQ(), false);
        zzb.zza(parcel, 4, (Parcelable) emailSignInConfig.zzlP(), i, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzO */
    public EmailSignInConfig createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        Uri uri = null;
        int i = 0;
        String str = null;
        Uri uri2 = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int zzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao);
            if (zzbM == 1) {
                i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                uri = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, Uri.CREATOR);
            } else if (zzbM == 3) {
                str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
            } else if (zzbM != 4) {
                com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
            } else {
                uri2 = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, Uri.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new EmailSignInConfig(i, uri, str, uri2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaF */
    public EmailSignInConfig[] newArray(int i) {
        return new EmailSignInConfig[i];
    }
}
