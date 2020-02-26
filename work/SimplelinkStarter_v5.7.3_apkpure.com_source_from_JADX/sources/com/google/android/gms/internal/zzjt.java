package com.google.android.gms.internal;

import android.accounts.Account;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.appdatasearch.DocumentContents;
import com.google.android.gms.appdatasearch.DocumentSection;
import com.google.android.gms.appdatasearch.RegisterSectionInfo.zza;
import com.google.android.gms.appdatasearch.UsageInfo;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.internal.zzox.zzb;
import com.google.android.gms.internal.zzox.zzc;
import com.google.android.gms.internal.zzox.zzd;
import java.util.ArrayList;

public class zzjt {
    private static DocumentSection zza(String str, zzc zzc) {
        return new DocumentSection(zzse.zzf(zzc), new zza(str).zzM(true).zzbB(str).zzbA("blob").zzlt());
    }

    public static UsageInfo zza(Action action, long j, String str, int i) {
        int i2;
        Bundle bundle = new Bundle();
        bundle.putAll(action.zzlx());
        Bundle bundle2 = bundle.getBundle("object");
        String str2 = "id";
        Uri parse = bundle2.containsKey(str2) ? Uri.parse(bundle2.getString(str2)) : null;
        String string = bundle2.getString("name");
        String string2 = bundle2.getString("type");
        Intent zza = zzju.zza(str, Uri.parse(bundle2.getString("url")));
        DocumentContents.zza zza2 = UsageInfo.zza(zza, string, parse, string2, null);
        String str3 = ".private:ssbContext";
        if (bundle.containsKey(str3)) {
            zza2.zza(DocumentSection.zzh(bundle.getByteArray(str3)));
            bundle.remove(str3);
        }
        String str4 = ".private:accountName";
        if (bundle.containsKey(str4)) {
            zza2.zzb(new Account(bundle.getString(str4), GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE));
            bundle.remove(str4);
        }
        String str5 = ".private:isContextOnly";
        boolean z = false;
        if (!bundle.containsKey(str5) || !bundle.getBoolean(str5)) {
            i2 = 0;
        } else {
            i2 = 4;
            bundle.remove(str5);
        }
        String str6 = ".private:isDeviceOnly";
        if (bundle.containsKey(str6)) {
            z = bundle.getBoolean(str6, false);
            bundle.remove(str6);
        }
        zza2.zza(zza(".private:action", zzf(bundle)));
        return new UsageInfo.zza().zza(UsageInfo.zza(str, zza)).zzw(j).zzan(i2).zza(zza2.zzlo()).zzO(z).zzao(i).zzlv();
    }

    static zzc zzf(Bundle bundle) {
        zzc zzc = new zzc();
        ArrayList arrayList = new ArrayList();
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            zzb zzb = new zzb();
            zzb.name = str;
            zzb.zzaCZ = new zzd();
            if (obj instanceof String) {
                zzb.zzaCZ.zzagS = (String) obj;
            } else if (obj instanceof Bundle) {
                zzb.zzaCZ.zzaDe = zzf((Bundle) obj);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unsupported value: ");
                sb.append(obj);
                Log.e("SearchIndex", sb.toString());
            }
            arrayList.add(zzb);
        }
        String str2 = "type";
        if (bundle.containsKey(str2)) {
            zzc.type = bundle.getString(str2);
        }
        zzc.zzaDa = (zzb[]) arrayList.toArray(new zzb[arrayList.size()]);
        return zzc;
    }
}
