package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.EmailSignInConfig;
import com.google.android.gms.auth.api.signin.FacebookSignInConfig;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzh implements Creator<SignInConfiguration> {
    static void zza(SignInConfiguration signInConfiguration, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, signInConfiguration.versionCode);
        zzb.zza(parcel, 2, signInConfiguration.zzme(), false);
        zzb.zza(parcel, 3, signInConfiguration.zzmb(), false);
        zzb.zza(parcel, 4, (Parcelable) signInConfiguration.zzmf(), i, false);
        zzb.zza(parcel, 5, (Parcelable) signInConfiguration.zzmg(), i, false);
        zzb.zza(parcel, 6, (Parcelable) signInConfiguration.zzmh(), i, false);
        zzb.zza(parcel, 7, signInConfiguration.zzmi(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzS */
    public SignInConfiguration createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        String str2 = null;
        EmailSignInConfig emailSignInConfig = null;
        GoogleSignInConfig googleSignInConfig = null;
        FacebookSignInConfig facebookSignInConfig = null;
        String str3 = null;
        int i = 0;
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
                    str2 = zza.zzp(parcel, zzao);
                    break;
                case 4:
                    emailSignInConfig = (EmailSignInConfig) zza.zza(parcel, zzao, EmailSignInConfig.CREATOR);
                    break;
                case 5:
                    googleSignInConfig = (GoogleSignInConfig) zza.zza(parcel, zzao, GoogleSignInConfig.CREATOR);
                    break;
                case 6:
                    facebookSignInConfig = (FacebookSignInConfig) zza.zza(parcel, zzao, FacebookSignInConfig.CREATOR);
                    break;
                case 7:
                    str3 = zza.zzp(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            SignInConfiguration signInConfiguration = new SignInConfiguration(i, str, str2, emailSignInConfig, googleSignInConfig, facebookSignInConfig, str3);
            return signInConfiguration;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaJ */
    public SignInConfiguration[] newArray(int i) {
        return new SignInConfiguration[i];
    }
}
