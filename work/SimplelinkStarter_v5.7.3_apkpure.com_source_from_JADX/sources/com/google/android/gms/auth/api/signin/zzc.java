package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzc implements Creator<GoogleSignInAccount> {
    static void zza(GoogleSignInAccount googleSignInAccount, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, googleSignInAccount.versionCode);
        zzb.zza(parcel, 2, googleSignInAccount.getId(), false);
        zzb.zza(parcel, 3, googleSignInAccount.getIdToken(), false);
        zzb.zza(parcel, 4, googleSignInAccount.getEmail(), false);
        zzb.zza(parcel, 5, googleSignInAccount.getDisplayName(), false);
        zzb.zza(parcel, 6, (Parcelable) googleSignInAccount.zzlT(), i, false);
        zzb.zza(parcel, 7, googleSignInAccount.zzlU(), false);
        zzb.zza(parcel, 8, googleSignInAccount.zzlV());
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzQ */
    public GoogleSignInAccount createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzap = zza.zzap(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        Uri uri = null;
        String str5 = null;
        long j = 0;
        int i = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case 1:
                    i = zza.zzg(parcel2, zzao);
                    break;
                case 2:
                    str = zza.zzp(parcel2, zzao);
                    break;
                case 3:
                    str2 = zza.zzp(parcel2, zzao);
                    break;
                case 4:
                    str3 = zza.zzp(parcel2, zzao);
                    break;
                case 5:
                    str4 = zza.zzp(parcel2, zzao);
                    break;
                case 6:
                    uri = (Uri) zza.zza(parcel2, zzao, Uri.CREATOR);
                    break;
                case 7:
                    str5 = zza.zzp(parcel2, zzao);
                    break;
                case 8:
                    j = zza.zzi(parcel2, zzao);
                    break;
                default:
                    zza.zzb(parcel2, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            GoogleSignInAccount googleSignInAccount = new GoogleSignInAccount(i, str, str2, str3, str4, uri, str5, j);
            return googleSignInAccount;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel2);
    }

    /* renamed from: zzaH */
    public GoogleSignInAccount[] newArray(int i) {
        return new GoogleSignInAccount[i];
    }
}
