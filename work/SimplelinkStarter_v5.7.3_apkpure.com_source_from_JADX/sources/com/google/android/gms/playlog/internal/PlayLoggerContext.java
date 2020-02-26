package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;

public class PlayLoggerContext implements SafeParcelable {
    public static final zze CREATOR = new zze();
    public final String packageName;
    public final int versionCode;
    public final int zzaRR;
    public final int zzaRS;
    public final String zzaRT;
    public final String zzaRU;
    public final boolean zzaRV;
    public final String zzaRW;
    public final boolean zzaRX;
    public final int zzaRY;

    public PlayLoggerContext(int i, String str, int i2, int i3, String str2, String str3, boolean z, String str4, boolean z2, int i4) {
        this.versionCode = i;
        this.packageName = str;
        this.zzaRR = i2;
        this.zzaRS = i3;
        this.zzaRT = str2;
        this.zzaRU = str3;
        this.zzaRV = z;
        this.zzaRW = str4;
        this.zzaRX = z2;
        this.zzaRY = i4;
    }

    @Deprecated
    public PlayLoggerContext(String str, int i, int i2, String str2, String str3, boolean z) {
        this.versionCode = 1;
        this.packageName = (String) zzx.zzw(str);
        this.zzaRR = i;
        this.zzaRS = i2;
        this.zzaRW = null;
        this.zzaRT = str2;
        this.zzaRU = str3;
        this.zzaRV = z;
        this.zzaRX = false;
        this.zzaRY = 0;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PlayLoggerContext)) {
            return false;
        }
        PlayLoggerContext playLoggerContext = (PlayLoggerContext) obj;
        if (!(this.versionCode == playLoggerContext.versionCode && this.packageName.equals(playLoggerContext.packageName) && this.zzaRR == playLoggerContext.zzaRR && this.zzaRS == playLoggerContext.zzaRS && zzw.equal(this.zzaRW, playLoggerContext.zzaRW) && zzw.equal(this.zzaRT, playLoggerContext.zzaRT) && zzw.equal(this.zzaRU, playLoggerContext.zzaRU) && this.zzaRV == playLoggerContext.zzaRV && this.zzaRX == playLoggerContext.zzaRX && this.zzaRY == playLoggerContext.zzaRY)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.versionCode), this.packageName, Integer.valueOf(this.zzaRR), Integer.valueOf(this.zzaRS), this.zzaRW, this.zzaRT, this.zzaRU, Boolean.valueOf(this.zzaRV), Boolean.valueOf(this.zzaRX), Integer.valueOf(this.zzaRY));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PlayLoggerContext[");
        sb.append("versionCode=");
        sb.append(this.versionCode);
        sb.append(',');
        sb.append("package=");
        sb.append(this.packageName);
        sb.append(',');
        sb.append("packageVersionCode=");
        sb.append(this.zzaRR);
        sb.append(',');
        sb.append("logSource=");
        sb.append(this.zzaRS);
        sb.append(',');
        sb.append("logSourceName=");
        sb.append(this.zzaRW);
        sb.append(',');
        sb.append("uploadAccount=");
        sb.append(this.zzaRT);
        sb.append(',');
        sb.append("loggingId=");
        sb.append(this.zzaRU);
        sb.append(',');
        sb.append("logAndroidId=");
        sb.append(this.zzaRV);
        sb.append(',');
        sb.append("isAnonymous=");
        sb.append(this.zzaRX);
        sb.append(',');
        sb.append("qosTier=");
        sb.append(this.zzaRY);
        sb.append("]");
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }
}
