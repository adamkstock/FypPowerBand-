package com.google.android.gms.auth.api.proxy;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<ProxyGrpcRequest> {
    static void zza(ProxyGrpcRequest proxyGrpcRequest, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, proxyGrpcRequest.hostname, false);
        zzb.zzc(parcel, 1000, proxyGrpcRequest.versionCode);
        zzb.zzc(parcel, 2, proxyGrpcRequest.port);
        zzb.zza(parcel, 3, proxyGrpcRequest.timeoutMillis);
        zzb.zza(parcel, 4, proxyGrpcRequest.body, false);
        zzb.zza(parcel, 5, proxyGrpcRequest.method, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzL */
    public ProxyGrpcRequest createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String str = null;
        byte[] bArr = null;
        String str2 = null;
        long j = 0;
        int i = 0;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int zzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao);
            if (zzbM == 1) {
                str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
            } else if (zzbM == 2) {
                i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            } else if (zzbM == 3) {
                j = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzao);
            } else if (zzbM == 4) {
                bArr = com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, zzao);
            } else if (zzbM == 5) {
                str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
            } else if (zzbM != 1000) {
                com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
            } else {
                i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            ProxyGrpcRequest proxyGrpcRequest = new ProxyGrpcRequest(i, str, i2, j, bArr, str2);
            return proxyGrpcRequest;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaC */
    public ProxyGrpcRequest[] newArray(int i) {
        return new ProxyGrpcRequest[i];
    }
}
