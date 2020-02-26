package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzd implements Creator<Status> {
    static void zza(Status status, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, status.getStatusCode());
        zzb.zzc(parcel, 1000, status.getVersionCode());
        zzb.zza(parcel, 2, status.getStatusMessage(), false);
        zzb.zza(parcel, 3, (Parcelable) status.zznH(), i, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzae */
    public Status createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        int i = 0;
        String str = null;
        PendingIntent pendingIntent = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                i2 = zza.zzg(parcel, zzao);
            } else if (zzbM == 2) {
                str = zza.zzp(parcel, zzao);
            } else if (zzbM == 3) {
                pendingIntent = (PendingIntent) zza.zza(parcel, zzao, PendingIntent.CREATOR);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new Status(i, i2, str, pendingIntent);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbm */
    public Status[] newArray(int i) {
        return new Status[i];
    }
}
