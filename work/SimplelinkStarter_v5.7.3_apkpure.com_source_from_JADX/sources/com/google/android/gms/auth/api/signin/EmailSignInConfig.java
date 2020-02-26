package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Patterns;
import com.google.android.gms.auth.api.signin.internal.zzc;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public class EmailSignInConfig implements SafeParcelable {
    public static final Creator<EmailSignInConfig> CREATOR = new zza();
    final int versionCode;
    private final Uri zzSU;
    private String zzSV;
    private Uri zzSW;

    EmailSignInConfig(int i, Uri uri, String str, Uri uri2) {
        String str2 = "Server widget url cannot be null in order to use email/password sign in.";
        zzx.zzb(uri, (Object) str2);
        zzx.zzh(uri.toString(), str2);
        zzx.zzb(Patterns.WEB_URL.matcher(uri.toString()).matches(), (Object) "Invalid server widget url");
        this.versionCode = i;
        this.zzSU = uri;
        this.zzSV = str;
        this.zzSW = uri2;
    }

    public int describeContents() {
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0027, code lost:
        if (r3.zzSW.equals(r4.zzlP()) != false) goto L_0x0029;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        if (r3.zzSV.equals(r4.zzlQ()) != false) goto L_0x0048;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.google.android.gms.auth.api.signin.EmailSignInConfig r4 = (com.google.android.gms.auth.api.signin.EmailSignInConfig) r4     // Catch:{ ClassCastException -> 0x0049 }
            android.net.Uri r1 = r3.zzSU     // Catch:{ ClassCastException -> 0x0049 }
            android.net.Uri r2 = r4.zzlO()     // Catch:{ ClassCastException -> 0x0049 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0049 }
            if (r1 == 0) goto L_0x0049
            android.net.Uri r1 = r3.zzSW     // Catch:{ ClassCastException -> 0x0049 }
            if (r1 != 0) goto L_0x001d
            android.net.Uri r1 = r4.zzlP()     // Catch:{ ClassCastException -> 0x0049 }
            if (r1 != 0) goto L_0x0049
            goto L_0x0029
        L_0x001d:
            android.net.Uri r1 = r3.zzSW     // Catch:{ ClassCastException -> 0x0049 }
            android.net.Uri r2 = r4.zzlP()     // Catch:{ ClassCastException -> 0x0049 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0049 }
            if (r1 == 0) goto L_0x0049
        L_0x0029:
            java.lang.String r1 = r3.zzSV     // Catch:{ ClassCastException -> 0x0049 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0049 }
            if (r1 == 0) goto L_0x003c
            java.lang.String r4 = r4.zzlQ()     // Catch:{ ClassCastException -> 0x0049 }
            boolean r4 = android.text.TextUtils.isEmpty(r4)     // Catch:{ ClassCastException -> 0x0049 }
            if (r4 == 0) goto L_0x0049
            goto L_0x0048
        L_0x003c:
            java.lang.String r1 = r3.zzSV     // Catch:{ ClassCastException -> 0x0049 }
            java.lang.String r4 = r4.zzlQ()     // Catch:{ ClassCastException -> 0x0049 }
            boolean r4 = r1.equals(r4)     // Catch:{ ClassCastException -> 0x0049 }
            if (r4 == 0) goto L_0x0049
        L_0x0048:
            r0 = 1
        L_0x0049:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.EmailSignInConfig.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return new zzc().zzl(this.zzSU).zzl(this.zzSW).zzl(this.zzSV).zzmd();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }

    public Uri zzlO() {
        return this.zzSU;
    }

    public Uri zzlP() {
        return this.zzSW;
    }

    public String zzlQ() {
        return this.zzSV;
    }
}
