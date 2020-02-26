package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzz implements Creator<ResolveAccountResponse> {
    static void zza(ResolveAccountResponse resolveAccountResponse, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, resolveAccountResponse.mVersionCode);
        zzb.zza(parcel, 2, resolveAccountResponse.zzaeH, false);
        zzb.zza(parcel, 3, (Parcelable) resolveAccountResponse.zzpr(), i, false);
        zzb.zza(parcel, 4, resolveAccountResponse.zzps());
        zzb.zza(parcel, 5, resolveAccountResponse.zzpt());
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzam */
    public ResolveAccountResponse createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        IBinder iBinder = null;
        ConnectionResult connectionResult = null;
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                iBinder = zza.zzq(parcel, zzao);
            } else if (zzbM == 3) {
                connectionResult = (ConnectionResult) zza.zza(parcel, zzao, ConnectionResult.CREATOR);
            } else if (zzbM == 4) {
                z = zza.zzc(parcel, zzao);
            } else if (zzbM != 5) {
                zza.zzb(parcel, zzao);
            } else {
                z2 = zza.zzc(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            ResolveAccountResponse resolveAccountResponse = new ResolveAccountResponse(i, iBinder, connectionResult, z, z2);
            return resolveAccountResponse;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbK */
    public ResolveAccountResponse[] newArray(int i) {
        return new ResolveAccountResponse[i];
    }
}
