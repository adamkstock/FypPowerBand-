package com.google.android.gms.common;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;
import android.util.Log;

public class zzd {
    private static final zzd zzaas = new zzd();

    private zzd() {
    }

    private boolean zza(PackageInfo packageInfo, boolean z) {
        String str = "GoogleSignatureVerifier";
        if (packageInfo.signatures.length != 1) {
            Log.w(str, "Package has more than one signature.");
            return false;
        }
        zzb zzb = new zzb(packageInfo.signatures[0].toByteArray());
        if ((z ? zzc.zznp() : zzc.zznq()).contains(zzb)) {
            return true;
        }
        if (Log.isLoggable(str, 2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Signature not valid.  Found: \n");
            sb.append(Base64.encodeToString(zzb.getBytes(), 0));
            Log.v(str, sb.toString());
        }
        return false;
    }

    public static zzd zznu() {
        return zzaas;
    }

    /* access modifiers changed from: 0000 */
    public zza zza(PackageInfo packageInfo, zza... zzaArr) {
        String str = "GoogleSignatureVerifier";
        if (packageInfo.signatures.length != 1) {
            Log.w(str, "Package has more than one signature.");
            return null;
        }
        zzb zzb = new zzb(packageInfo.signatures[0].toByteArray());
        for (int i = 0; i < zzaArr.length; i++) {
            if (zzaArr[i].equals(zzb)) {
                return zzaArr[i];
            }
        }
        if (Log.isLoggable(str, 2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Signature not valid.  Found: \n");
            sb.append(Base64.encodeToString(zzb.getBytes(), 0));
            Log.v(str, sb.toString());
        }
        return null;
    }

    public boolean zza(PackageManager packageManager, PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (GooglePlayServicesUtil.zzc(packageManager)) {
            return zza(packageInfo, true);
        }
        boolean zza = zza(packageInfo, false);
        if (!zza && zza(packageInfo, true)) {
            Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        }
        return zza;
    }

    public boolean zzb(PackageManager packageManager, String str) {
        try {
            return zza(packageManager, packageManager.getPackageInfo(str, 64));
        } catch (NameNotFoundException unused) {
            String str2 = "GoogleSignatureVerifier";
            if (Log.isLoggable(str2, 3)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Package manager can't find package ");
                sb.append(str);
                sb.append(", defaulting to false");
                Log.d(str2, sb.toString());
            }
            return false;
        }
    }
}
