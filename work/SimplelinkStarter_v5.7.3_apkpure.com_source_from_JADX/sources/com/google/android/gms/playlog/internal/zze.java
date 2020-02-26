package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zze implements Creator<PlayLoggerContext> {
    static void zza(PlayLoggerContext playLoggerContext, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, playLoggerContext.versionCode);
        zzb.zza(parcel, 2, playLoggerContext.packageName, false);
        zzb.zzc(parcel, 3, playLoggerContext.zzaRR);
        zzb.zzc(parcel, 4, playLoggerContext.zzaRS);
        zzb.zza(parcel, 5, playLoggerContext.zzaRT, false);
        zzb.zza(parcel, 6, playLoggerContext.zzaRU, false);
        zzb.zza(parcel, 7, playLoggerContext.zzaRV);
        zzb.zza(parcel, 8, playLoggerContext.zzaRW, false);
        zzb.zza(parcel, 9, playLoggerContext.zzaRX);
        zzb.zzc(parcel, 10, playLoggerContext.zzaRY);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzgj */
    public PlayLoggerContext createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzap = zza.zzap(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        boolean z = true;
        boolean z2 = false;
        int i4 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case 1:
                    i = zza.zzg(parcel2, zzao);
                    break;
                case 2:
                    str = zza.zzp(parcel2, zzao);
                    break;
                case 3:
                    i2 = zza.zzg(parcel2, zzao);
                    break;
                case 4:
                    i3 = zza.zzg(parcel2, zzao);
                    break;
                case 5:
                    str2 = zza.zzp(parcel2, zzao);
                    break;
                case 6:
                    str3 = zza.zzp(parcel2, zzao);
                    break;
                case 7:
                    z = zza.zzc(parcel2, zzao);
                    break;
                case 8:
                    str4 = zza.zzp(parcel2, zzao);
                    break;
                case 9:
                    z2 = zza.zzc(parcel2, zzao);
                    break;
                case 10:
                    i4 = zza.zzg(parcel2, zzao);
                    break;
                default:
                    zza.zzb(parcel2, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            PlayLoggerContext playLoggerContext = new PlayLoggerContext(i, str, i2, i3, str2, str3, z, str4, z2, i4);
            return playLoggerContext;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel2);
    }

    /* renamed from: zziV */
    public PlayLoggerContext[] newArray(int i) {
        return new PlayLoggerContext[i];
    }
}
