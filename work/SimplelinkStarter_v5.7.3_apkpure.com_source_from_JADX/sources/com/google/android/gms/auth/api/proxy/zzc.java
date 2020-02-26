package com.google.android.gms.auth.api.proxy;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzc implements Creator<ProxyResponse> {
    static void zza(ProxyResponse proxyResponse, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, proxyResponse.googlePlayServicesStatusCode);
        zzb.zzc(parcel, 1000, proxyResponse.versionCode);
        zzb.zza(parcel, 2, (Parcelable) proxyResponse.recoveryAction, i, false);
        zzb.zzc(parcel, 3, proxyResponse.statusCode);
        zzb.zza(parcel, 4, proxyResponse.zzSK, false);
        zzb.zza(parcel, 5, proxyResponse.body, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzN */
    public ProxyResponse createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        PendingIntent pendingIntent = null;
        Bundle bundle = null;
        byte[] bArr = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i2 = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                pendingIntent = (PendingIntent) zza.zza(parcel, zzao, PendingIntent.CREATOR);
            } else if (zzbM == 3) {
                i3 = zza.zzg(parcel, zzao);
            } else if (zzbM == 4) {
                bundle = zza.zzr(parcel, zzao);
            } else if (zzbM == 5) {
                bArr = zza.zzs(parcel, zzao);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            ProxyResponse proxyResponse = new ProxyResponse(i, i2, pendingIntent, i3, bundle, bArr);
            return proxyResponse;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaE */
    public ProxyResponse[] newArray(int i) {
        return new ProxyResponse[i];
    }
}
