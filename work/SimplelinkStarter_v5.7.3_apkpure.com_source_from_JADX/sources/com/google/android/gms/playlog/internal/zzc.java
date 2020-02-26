package com.google.android.gms.playlog.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzc implements Creator<LogEvent> {
    static void zza(LogEvent logEvent, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, logEvent.versionCode);
        zzb.zza(parcel, 2, logEvent.zzaRG);
        zzb.zza(parcel, 3, logEvent.tag, false);
        zzb.zza(parcel, 4, logEvent.zzaRI, false);
        zzb.zza(parcel, 5, logEvent.zzaRJ, false);
        zzb.zza(parcel, 6, logEvent.zzaRH);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzgi */
    public LogEvent createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        long j = 0;
        long j2 = 0;
        String str = null;
        byte[] bArr = null;
        Bundle bundle = null;
        int i = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case 1:
                    i = zza.zzg(parcel, zzao);
                    break;
                case 2:
                    j = zza.zzi(parcel, zzao);
                    break;
                case 3:
                    str = zza.zzp(parcel, zzao);
                    break;
                case 4:
                    bArr = zza.zzs(parcel, zzao);
                    break;
                case 5:
                    bundle = zza.zzr(parcel, zzao);
                    break;
                case 6:
                    j2 = zza.zzi(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            LogEvent logEvent = new LogEvent(i, j, j2, str, bArr, bundle);
            return logEvent;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zziU */
    public LogEvent[] newArray(int i) {
        return new LogEvent[i];
    }
}
