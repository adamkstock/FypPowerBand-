package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.ArrayList;

public class zze implements Creator<GoogleSignInConfig> {
    static void zza(GoogleSignInConfig googleSignInConfig, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, googleSignInConfig.versionCode);
        zzb.zzc(parcel, 2, googleSignInConfig.zzlS(), false);
        zzb.zza(parcel, 3, (Parcelable) googleSignInConfig.getAccount(), i, false);
        zzb.zza(parcel, 4, googleSignInConfig.zzlY());
        zzb.zza(parcel, 5, googleSignInConfig.zzlZ());
        zzb.zza(parcel, 6, googleSignInConfig.zzma());
        zzb.zza(parcel, 7, googleSignInConfig.zzmb(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzR */
    public GoogleSignInConfig createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        ArrayList arrayList = null;
        Account account = null;
        String str = null;
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case 1:
                    i = zza.zzg(parcel, zzao);
                    break;
                case 2:
                    arrayList = zza.zzc(parcel, zzao, Scope.CREATOR);
                    break;
                case 3:
                    account = (Account) zza.zza(parcel, zzao, Account.CREATOR);
                    break;
                case 4:
                    z = zza.zzc(parcel, zzao);
                    break;
                case 5:
                    z2 = zza.zzc(parcel, zzao);
                    break;
                case 6:
                    z3 = zza.zzc(parcel, zzao);
                    break;
                case 7:
                    str = zza.zzp(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            GoogleSignInConfig googleSignInConfig = new GoogleSignInConfig(i, arrayList, account, z, z2, z3, str);
            return googleSignInConfig;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaI */
    public GoogleSignInConfig[] newArray(int i) {
        return new GoogleSignInConfig[i];
    }
}
