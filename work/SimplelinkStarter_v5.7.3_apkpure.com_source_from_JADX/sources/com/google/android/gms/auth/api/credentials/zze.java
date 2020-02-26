package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;

public class zze implements Creator<PasswordSpecification> {
    static void zza(PasswordSpecification passwordSpecification, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, passwordSpecification.zzSv, false);
        zzb.zzc(parcel, 1000, passwordSpecification.mVersionCode);
        zzb.zzb(parcel, 2, passwordSpecification.zzSw, false);
        zzb.zza(parcel, 3, passwordSpecification.zzSx, false);
        zzb.zzc(parcel, 4, passwordSpecification.zzSy);
        zzb.zzc(parcel, 5, passwordSpecification.zzSz);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzI */
    public PasswordSpecification createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        List list = null;
        List list2 = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM == 2) {
                list = zza.zzD(parcel, zzao);
            } else if (zzbM == 3) {
                list2 = zza.zzC(parcel, zzao);
            } else if (zzbM == 4) {
                i2 = zza.zzg(parcel, zzao);
            } else if (zzbM == 5) {
                i3 = zza.zzg(parcel, zzao);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            PasswordSpecification passwordSpecification = new PasswordSpecification(i, str, list, list2, i2, i3);
            return passwordSpecification;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaz */
    public PasswordSpecification[] newArray(int i) {
        return new PasswordSpecification[i];
    }
}
