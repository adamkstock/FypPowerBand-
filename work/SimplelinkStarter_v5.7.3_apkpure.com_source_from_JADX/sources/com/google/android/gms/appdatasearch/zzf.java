package com.google.android.gms.appdatasearch;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.appdatasearch.GetRecentContextCall.Request;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzf implements Creator<Request> {
    static void zza(Request request, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, (Parcelable) request.zzQq, i, false);
        zzb.zzc(parcel, 1000, request.mVersionCode);
        zzb.zza(parcel, 2, request.zzQr);
        zzb.zza(parcel, 3, request.zzQs);
        zzb.zza(parcel, 4, request.zzQt);
        zzb.zza(parcel, 5, request.zzQu, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzai */
    public Request[] newArray(int i) {
        return new Request[i];
    }

    /* renamed from: zzv */
    public Request createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        Account account = null;
        String str = null;
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                account = (Account) zza.zza(parcel, zzao, Account.CREATOR);
            } else if (zzbM == 2) {
                z = zza.zzc(parcel, zzao);
            } else if (zzbM == 3) {
                z2 = zza.zzc(parcel, zzao);
            } else if (zzbM == 4) {
                z3 = zza.zzc(parcel, zzao);
            } else if (zzbM == 5) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            Request request = new Request(i, account, z, z2, z3, str);
            return request;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }
}
