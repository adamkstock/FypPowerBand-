package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzi implements Creator<GetServiceRequest> {
    static void zza(GetServiceRequest getServiceRequest, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, getServiceRequest.version);
        zzb.zzc(parcel, 2, getServiceRequest.zzafq);
        zzb.zzc(parcel, 3, getServiceRequest.zzafr);
        zzb.zza(parcel, 4, getServiceRequest.zzafs, false);
        zzb.zza(parcel, 5, getServiceRequest.zzaft, false);
        zzb.zza(parcel, 6, (T[]) getServiceRequest.zzafu, i, false);
        zzb.zza(parcel, 7, getServiceRequest.zzafv, false);
        zzb.zza(parcel, 8, (Parcelable) getServiceRequest.zzafw, i, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzak */
    public GetServiceRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        IBinder iBinder = null;
        Scope[] scopeArr = null;
        Bundle bundle = null;
        Account account = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case 1:
                    i = zza.zzg(parcel, zzao);
                    break;
                case 2:
                    i2 = zza.zzg(parcel, zzao);
                    break;
                case 3:
                    i3 = zza.zzg(parcel, zzao);
                    break;
                case 4:
                    str = zza.zzp(parcel, zzao);
                    break;
                case 5:
                    iBinder = zza.zzq(parcel, zzao);
                    break;
                case 6:
                    scopeArr = (Scope[]) zza.zzb(parcel, zzao, Scope.CREATOR);
                    break;
                case 7:
                    bundle = zza.zzr(parcel, zzao);
                    break;
                case 8:
                    account = (Account) zza.zza(parcel, zzao, Account.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            GetServiceRequest getServiceRequest = new GetServiceRequest(i, i2, i3, str, iBinder, scopeArr, bundle, account);
            return getServiceRequest;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbD */
    public GetServiceRequest[] newArray(int i) {
        return new GetServiceRequest[i];
    }
}
