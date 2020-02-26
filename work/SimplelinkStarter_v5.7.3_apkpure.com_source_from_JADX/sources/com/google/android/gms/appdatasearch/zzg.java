package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.appdatasearch.GetRecentContextCall.Response;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;

public class zzg implements Creator<Response> {
    static void zza(Response response, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1000, response.mVersionCode);
        zzb.zza(parcel, 1, (Parcelable) response.zzQA, i, false);
        zzb.zzc(parcel, 2, response.zzQB, false);
        zzb.zza(parcel, 3, response.zzQC, false);
        zzb.zzI(parcel, zzaq);
    }

    /* renamed from: zzaj */
    public Response[] newArray(int i) {
        return new Response[i];
    }

    /* renamed from: zzw */
    public Response createFromParcel(Parcel parcel) {
        int zzap = zza.zzap(parcel);
        Status status = null;
        int i = 0;
        List list = null;
        String[] strArr = null;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            int zzbM = zza.zzbM(zzao);
            if (zzbM == 1) {
                status = (Status) zza.zza(parcel, zzao, Status.CREATOR);
            } else if (zzbM == 2) {
                list = zza.zzc(parcel, zzao, UsageInfo.CREATOR);
            } else if (zzbM == 3) {
                strArr = zza.zzB(parcel, zzao);
            } else if (zzbM != 1000) {
                zza.zzb(parcel, zzao);
            } else {
                i = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new Response(i, status, list, strArr);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Overread allowed size end=");
        sb.append(zzap);
        throw new C1121zza(sb.toString(), parcel);
    }
}
