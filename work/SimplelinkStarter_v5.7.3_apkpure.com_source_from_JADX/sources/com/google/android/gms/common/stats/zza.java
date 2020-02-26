package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zza implements Creator<ConnectionEvent> {
    static void zza(ConnectionEvent connectionEvent, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, connectionEvent.mVersionCode);
        zzb.zza(parcel, 2, connectionEvent.getTimeMillis());
        zzb.zza(parcel, 4, connectionEvent.zzpX(), false);
        zzb.zza(parcel, 5, connectionEvent.zzpY(), false);
        zzb.zza(parcel, 6, connectionEvent.zzpZ(), false);
        zzb.zza(parcel, 7, connectionEvent.zzqa(), false);
        zzb.zza(parcel, 8, connectionEvent.zzqb(), false);
        zzb.zza(parcel, 10, connectionEvent.zzqf());
        zzb.zza(parcel, 11, connectionEvent.zzqe());
        zzb.zzc(parcel, 12, connectionEvent.getEventType());
        zzb.zza(parcel, 13, connectionEvent.zzqc(), false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzaA */
    public ConnectionEvent createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        int i = 0;
        int i2 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel2, zzao);
                    break;
                case 2:
                    j = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel2, zzao);
                    break;
                case 4:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel2, zzao);
                    break;
                case 5:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel2, zzao);
                    break;
                case 6:
                    str3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel2, zzao);
                    break;
                case 7:
                    str4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel2, zzao);
                    break;
                case 8:
                    str5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel2, zzao);
                    break;
                case 10:
                    j2 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel2, zzao);
                    break;
                case 11:
                    j3 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel2, zzao);
                    break;
                case 12:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel2, zzao);
                    break;
                case 13:
                    str6 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel2, zzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel2, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            ConnectionEvent connectionEvent = new ConnectionEvent(i, j, i2, str, str2, str3, str4, str5, str6, j2, j3);
            return connectionEvent;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel2);
    }

    /* renamed from: zzbY */
    public ConnectionEvent[] newArray(int i) {
        return new ConnectionEvent[i];
    }
}
