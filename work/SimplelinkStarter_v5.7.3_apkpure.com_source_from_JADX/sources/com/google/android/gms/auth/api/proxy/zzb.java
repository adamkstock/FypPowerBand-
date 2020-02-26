package com.google.android.gms.auth.api.proxy;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;

public class zzb implements Creator<ProxyRequest> {
    static void zza(ProxyRequest proxyRequest, Parcel parcel, int i) {
        int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, proxyRequest.url, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, proxyRequest.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, proxyRequest.httpMethod);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, proxyRequest.timeoutMillis);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, proxyRequest.body, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, proxyRequest.zzSK, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzM */
    public ProxyRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String str = null;
        byte[] bArr = null;
        Bundle bundle = null;
        long j = 0;
        int i = 0;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM == 2) {
                i2 = zza.zzg(parcel, zzao);
            } else if (zzbM == 3) {
                j = zza.zzi(parcel, zzao);
            } else if (zzbM == 4) {
                bArr = zza.zzs(parcel, zzao);
            } else if (zzbM == 5) {
                bundle = zza.zzr(parcel, zzao);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            ProxyRequest proxyRequest = new ProxyRequest(i, str, i2, j, bArr, bundle);
            return proxyRequest;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaD */
    public ProxyRequest[] newArray(int i) {
        return new ProxyRequest[i];
    }
}
