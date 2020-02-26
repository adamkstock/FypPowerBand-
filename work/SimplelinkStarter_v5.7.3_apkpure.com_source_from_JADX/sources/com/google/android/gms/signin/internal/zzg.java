package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzg implements Creator<RecordConsentRequest> {
    static void zza(RecordConsentRequest recordConsentRequest, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, recordConsentRequest.mVersionCode);
        zzb.zza(parcel, 2, (Parcelable) recordConsentRequest.getAccount(), i, false);
        zzb.zza(parcel, 3, (T[]) recordConsentRequest.zzCj(), i, false);
        zzb.zza(parcel, 4, recordConsentRequest.zzmb(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzgD */
    public RecordConsentRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        Account account = null;
        int i = 0;
        Scope[] scopeArr = null;
        String str = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                account = (Account) zza.zza(parcel, zzao, Account.CREATOR);
            } else if (zzbM == 3) {
                scopeArr = (Scope[]) zza.zzb(parcel, zzao, Scope.CREATOR);
            } else if (zzbM != 4) {
                zza.zzb(parcel, zzao);
            } else {
                str = zza.zzp(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new RecordConsentRequest(i, account, scopeArr, str);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzjr */
    public RecordConsentRequest[] newArray(int i) {
        return new RecordConsentRequest[i];
    }
}
