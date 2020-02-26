package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;

public class zzh implements Creator<WakeLockEvent> {
    static void zza(WakeLockEvent wakeLockEvent, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, wakeLockEvent.mVersionCode);
        zzb.zza(parcel, 2, wakeLockEvent.getTimeMillis());
        zzb.zza(parcel, 4, wakeLockEvent.zzqj(), false);
        zzb.zzc(parcel, 5, wakeLockEvent.zzql());
        zzb.zzb(parcel, 6, wakeLockEvent.zzqm(), false);
        zzb.zza(parcel, 8, wakeLockEvent.zzqf());
        zzb.zza(parcel, 10, wakeLockEvent.zzqk(), false);
        zzb.zzc(parcel, 11, wakeLockEvent.getEventType());
        zzb.zza(parcel, 12, wakeLockEvent.zzqc(), false);
        zzb.zza(parcel, 13, wakeLockEvent.zzqo(), false);
        zzb.zzc(parcel, 14, wakeLockEvent.zzqn());
        zzb.zza(parcel, 15, wakeLockEvent.zzqp());
        zzb.zza(parcel, 16, wakeLockEvent.zzqq());
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzaB */
    public WakeLockEvent createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzap = zza.zzap(parcel);
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        String str = null;
        List list = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        float f = 0.0f;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case 1:
                    i = zza.zzg(parcel2, zzao);
                    break;
                case 2:
                    j = zza.zzi(parcel2, zzao);
                    break;
                case 4:
                    str = zza.zzp(parcel2, zzao);
                    break;
                case 5:
                    i3 = zza.zzg(parcel2, zzao);
                    break;
                case 6:
                    list = zza.zzD(parcel2, zzao);
                    break;
                case 8:
                    j2 = zza.zzi(parcel2, zzao);
                    break;
                case 10:
                    str3 = zza.zzp(parcel2, zzao);
                    break;
                case 11:
                    i2 = zza.zzg(parcel2, zzao);
                    break;
                case 12:
                    str2 = zza.zzp(parcel2, zzao);
                    break;
                case 13:
                    str4 = zza.zzp(parcel2, zzao);
                    break;
                case 14:
                    i4 = zza.zzg(parcel2, zzao);
                    break;
                case 15:
                    f = zza.zzl(parcel2, zzao);
                    break;
                case 16:
                    j3 = zza.zzi(parcel2, zzao);
                    break;
                default:
                    zza.zzb(parcel2, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            WakeLockEvent wakeLockEvent = new WakeLockEvent(i, j, i2, str, i3, list, str2, j2, i4, str3, str4, f, j3);
            return wakeLockEvent;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel2);
    }

    /* renamed from: zzbZ */
    public WakeLockEvent[] newArray(int i) {
        return new WakeLockEvent[i];
    }
}
