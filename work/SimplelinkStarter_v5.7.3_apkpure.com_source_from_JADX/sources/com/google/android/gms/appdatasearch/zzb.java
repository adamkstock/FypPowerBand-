package com.google.android.gms.appdatasearch;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;

public class zzb implements Creator<DocumentContents> {
    static void zza(DocumentContents documentContents, Parcel parcel, int i) {
        int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, (T[]) documentContents.zzPX, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, documentContents.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, documentContents.zzPY, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, documentContents.zzPZ);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable) documentContents.account, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzae */
    public DocumentContents[] newArray(int i) {
        return new DocumentContents[i];
    }

    /* renamed from: zzr */
    public DocumentContents createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        DocumentSection[] documentSectionArr = null;
        String str = null;
        Account account = null;
        int i = 0;
        boolean z = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                documentSectionArr = (DocumentSection[]) zza.zzb(parcel, zzao, DocumentSection.CREATOR);
            } else if (zzbM == 2) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM == 3) {
                z = zza.zzc(parcel, zzao);
            } else if (zzbM == 4) {
                account = (Account) zza.zza(parcel, zzao, Account.CREATOR);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            DocumentContents documentContents = new DocumentContents(i, documentSectionArr, str, z, account);
            return documentContents;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }
}
