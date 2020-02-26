package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.ArrayList;

public class zzc implements Creator<AccountChangeEventsResponse> {
    static void zza(AccountChangeEventsResponse accountChangeEventsResponse, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, accountChangeEventsResponse.mVersion);
        zzb.zzc(parcel, 2, accountChangeEventsResponse.zzoQ, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzB */
    public AccountChangeEventsResponse createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        int i = 0;
        ArrayList arrayList = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM != 2) {
                zza.zzb(parcel, zzao);
            } else {
                arrayList = zza.zzc(parcel, zzao, AccountChangeEvent.CREATOR);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new AccountChangeEventsResponse(i, arrayList);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzas */
    public AccountChangeEventsResponse[] newArray(int i) {
        return new AccountChangeEventsResponse[i];
    }
}
