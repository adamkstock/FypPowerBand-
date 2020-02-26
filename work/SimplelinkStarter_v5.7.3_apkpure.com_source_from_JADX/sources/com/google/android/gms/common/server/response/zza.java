package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;

public class zza implements Creator<Field> {
    static void zza(Field field, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, field.getVersionCode());
        zzb.zzc(parcel, 2, field.zzpB());
        zzb.zza(parcel, 3, field.zzpH());
        zzb.zzc(parcel, 4, field.zzpC());
        zzb.zza(parcel, 5, field.zzpI());
        zzb.zza(parcel, 6, field.zzpJ(), false);
        zzb.zzc(parcel, 7, field.zzpK());
        zzb.zza(parcel, 8, field.zzpM(), false);
        zzb.zza(parcel, 9, (Parcelable) field.zzpO(), i, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzav */
    public Field createFromParcel(Parcel parcel) {
        int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String str = null;
        String str2 = null;
        ConverterWrapper converterWrapper = null;
        int i = 0;
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        boolean z2 = false;
        int i4 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case 2:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case 3:
                    z = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao);
                    break;
                case 4:
                    i3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case 5:
                    z2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao);
                    break;
                case 6:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                case 7:
                    i4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    break;
                case 8:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    break;
                case 9:
                    converterWrapper = (ConverterWrapper) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, (Creator<T>) ConverterWrapper.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            Field field = new Field(i, i2, z, i3, z2, str, i4, str2, converterWrapper);
            return field;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }

    /* renamed from: zzbT */
    public Field[] newArray(int i) {
        return new Field[i];
    }
}
