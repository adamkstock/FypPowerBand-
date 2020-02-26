package com.google.android.gms.auth.api.consent;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.firstparty.shared.ScopeDetail;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;

public class zzb implements Creator<GetConsentIntentRequest> {
    static void zza(GetConsentIntentRequest getConsentIntentRequest, Parcel parcel, int i) {
        int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, getConsentIntentRequest.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, getConsentIntentRequest.getCallingPackage(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, getConsentIntentRequest.getCallingUid());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, getConsentIntentRequest.zzlF(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, (Parcelable) getConsentIntentRequest.getAccount(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, (T[]) getConsentIntentRequest.zzSe, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, getConsentIntentRequest.zzlG());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 8, getConsentIntentRequest.zzlH());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzD */
    public GetConsentIntentRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        String str2 = null;
        Account account = null;
        ScopeDetail[] scopeDetailArr = null;
        int i = 0;
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
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
                    i2 = zza.zzg(parcel, zzao);
                    break;
                case 4:
                    str2 = zza.zzp(parcel, zzao);
                    break;
                case 5:
                    account = (Account) zza.zza(parcel, zzao, Account.CREATOR);
                    break;
                case 6:
                    scopeDetailArr = (ScopeDetail[]) zza.zzb(parcel, zzao, ScopeDetail.CREATOR);
                    break;
                case 7:
                    z = zza.zzc(parcel, zzao);
                    break;
                case 8:
                    i3 = zza.zzg(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            GetConsentIntentRequest getConsentIntentRequest = new GetConsentIntentRequest(i, str, i2, str2, account, scopeDetailArr, z, i3);
            return getConsentIntentRequest;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzau */
    public GetConsentIntentRequest[] newArray(int i) {
        return new GetConsentIntentRequest[i];
    }
}
