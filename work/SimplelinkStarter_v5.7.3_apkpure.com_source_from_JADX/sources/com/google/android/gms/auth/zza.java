package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<AccountChangeEvent> {
    static void zza(AccountChangeEvent accountChangeEvent, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, accountChangeEvent.mVersion);
        zzb.zza(parcel, 2, accountChangeEvent.zzRr);
        zzb.zza(parcel, 3, accountChangeEvent.zzRs, false);
        zzb.zzc(parcel, 4, accountChangeEvent.zzRt);
        zzb.zzc(parcel, 5, accountChangeEvent.zzRu);
        zzb.zza(parcel, 6, accountChangeEvent.zzRv, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzaq */
    public AccountChangeEvent[] newArray(int i) {
        return new AccountChangeEvent[i];
    }

    /* renamed from: zzz */
    public AccountChangeEvent createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String str = null;
        String str2 = null;
        long j = 0;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case 2:
                    j = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzao);
                    break;
                case 3:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                case 4:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case 5:
                    i3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case 6:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            AccountChangeEvent accountChangeEvent = new AccountChangeEvent(i, j, str, i2, i3, str2);
            return accountChangeEvent;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }
}
