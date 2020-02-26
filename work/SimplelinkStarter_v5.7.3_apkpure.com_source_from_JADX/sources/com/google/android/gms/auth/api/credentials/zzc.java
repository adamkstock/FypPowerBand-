package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzc implements Creator<CredentialRequest> {
    static void zza(CredentialRequest credentialRequest, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, credentialRequest.getSupportsPasswordLogin());
        zzb.zzc(parcel, 1000, credentialRequest.mVersionCode);
        zzb.zza(parcel, 2, credentialRequest.getAccountTypes(), false);
        zzb.zza(parcel, 3, (Parcelable) credentialRequest.getCredentialPickerConfig(), i, false);
        zzb.zza(parcel, 4, (Parcelable) credentialRequest.getCredentialHintPickerConfig(), i, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzG */
    public CredentialRequest createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        String[] strArr = null;
        CredentialPickerConfig credentialPickerConfig = null;
        CredentialPickerConfig credentialPickerConfig2 = null;
        int i = 0;
        boolean z = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                z = zza.zzc(parcel, zzao);
            } else if (zzbM == 2) {
                strArr = zza.zzB(parcel, zzao);
            } else if (zzbM == 3) {
                credentialPickerConfig = (CredentialPickerConfig) zza.zza(parcel, zzao, CredentialPickerConfig.CREATOR);
            } else if (zzbM == 4) {
                credentialPickerConfig2 = (CredentialPickerConfig) zza.zza(parcel, zzao, CredentialPickerConfig.CREATOR);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            CredentialRequest credentialRequest = new CredentialRequest(i, z, strArr, credentialPickerConfig, credentialPickerConfig2);
            return credentialRequest;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzax */
    public CredentialRequest[] newArray(int i) {
        return new CredentialRequest[i];
    }
}
