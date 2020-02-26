package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzad implements Creator<ValidateAccountRequest> {
    static void zza(ValidateAccountRequest validateAccountRequest, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, validateAccountRequest.mVersionCode);
        zzb.zzc(parcel, 2, validateAccountRequest.zzpu());
        zzb.zza(parcel, 3, validateAccountRequest.zzaeH, false);
        zzb.zza(parcel, 4, (T[]) validateAccountRequest.zzpv(), i, false);
        zzb.zza(parcel, 5, validateAccountRequest.zzpw(), false);
        zzb.zza(parcel, 6, validateAccountRequest.getCallingPackage(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzan */
    public ValidateAccountRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        IBinder iBinder = null;
        Scope[] scopeArr = null;
        Bundle bundle = null;
        String str = null;
        int i = 0;
        int i2 = 0;
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
                    iBinder = zza.zzq(parcel, zzao);
                    break;
                case 4:
                    scopeArr = (Scope[]) zza.zzb(parcel, zzao, Scope.CREATOR);
                    break;
                case 5:
                    bundle = zza.zzr(parcel, zzao);
                    break;
                case 6:
                    str = zza.zzp(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            ValidateAccountRequest validateAccountRequest = new ValidateAccountRequest(i, i2, iBinder, scopeArr, bundle, str);
            return validateAccountRequest;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbL */
    public ValidateAccountRequest[] newArray(int i) {
        return new ValidateAccountRequest[i];
    }
}
