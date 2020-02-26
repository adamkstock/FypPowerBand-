package com.google.android.gms.auth.api.credentials.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzi implements Creator<SaveRequest> {
    static void zza(SaveRequest saveRequest, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, (Parcelable) saveRequest.getCredential(), i, false);
        zzb.zzc(parcel, 1000, saveRequest.mVersionCode);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzK */
    public SaveRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        int i = 0;
        Credential credential = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                credential = (Credential) zza.zza(parcel, zzao, Credential.CREATOR);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new SaveRequest(i, credential);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzaB */
    public SaveRequest[] newArray(int i) {
        return new SaveRequest[i];
    }
}
