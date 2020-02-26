package com.google.android.gms.auth;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;

public class zzb implements Creator<AccountChangeEventsRequest> {
    static void zza(AccountChangeEventsRequest accountChangeEventsRequest, Parcel parcel, int i) {
        int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, accountChangeEventsRequest.mVersion);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, accountChangeEventsRequest.zzRu);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, accountChangeEventsRequest.zzRs, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable) accountChangeEventsRequest.zzQd, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzA */
    public AccountChangeEventsRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        int i = 0;
        String str = null;
        Account account = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                i2 = zza.zzg(parcel, zzao);
            } else if (zzbM == 3) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM != 4) {
                zza.zzb(parcel, zzao);
            } else {
                account = (Account) zza.zza(parcel, zzao, Account.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new AccountChangeEventsRequest(i, i2, str, account);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzar */
    public AccountChangeEventsRequest[] newArray(int i) {
        return new AccountChangeEventsRequest[i];
    }
}
