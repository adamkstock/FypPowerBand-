package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.EmailSignInConfig;
import com.google.android.gms.auth.api.signin.FacebookSignInConfig;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class SignInConfiguration implements SafeParcelable {
    public static final Creator<SignInConfiguration> CREATOR = new zzh();
    private static int zzTr = 31;
    final int versionCode;
    private String zzTl;
    private final String zzTs;
    private EmailSignInConfig zzTt;
    private GoogleSignInConfig zzTu;
    private FacebookSignInConfig zzTv;
    private String zzTw;

    SignInConfiguration(int i, String str, String str2, EmailSignInConfig emailSignInConfig, GoogleSignInConfig googleSignInConfig, FacebookSignInConfig facebookSignInConfig, String str3) {
        this.versionCode = i;
        this.zzTs = zzx.zzcr(str);
        this.zzTl = str2;
        this.zzTt = emailSignInConfig;
        this.zzTu = googleSignInConfig;
        this.zzTv = facebookSignInConfig;
        this.zzTw = str3;
    }

    public int describeContents() {
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
        if (r3.zzTl.equals(r4.zzmb()) != false) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004e, code lost:
        if (r3.zzTw.equals(r4.zzmi()) != false) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0065, code lost:
        if (r3.zzTt.equals(r4.zzmf()) != false) goto L_0x0067;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x007c, code lost:
        if (r3.zzTv.equals(r4.zzmh()) != false) goto L_0x007e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0093, code lost:
        if (r3.zzTu.equals(r4.zzmg()) != false) goto L_0x0095;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.google.android.gms.auth.api.signin.internal.SignInConfiguration r4 = (com.google.android.gms.auth.api.signin.internal.SignInConfiguration) r4     // Catch:{ ClassCastException -> 0x0096 }
            java.lang.String r1 = r3.zzTs     // Catch:{ ClassCastException -> 0x0096 }
            java.lang.String r2 = r4.zzme()     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0096
            java.lang.String r1 = r3.zzTl     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0025
            java.lang.String r1 = r4.zzmb()     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0096
            goto L_0x0031
        L_0x0025:
            java.lang.String r1 = r3.zzTl     // Catch:{ ClassCastException -> 0x0096 }
            java.lang.String r2 = r4.zzmb()     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0096
        L_0x0031:
            java.lang.String r1 = r3.zzTw     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0044
            java.lang.String r1 = r4.zzmi()     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0096
            goto L_0x0050
        L_0x0044:
            java.lang.String r1 = r3.zzTw     // Catch:{ ClassCastException -> 0x0096 }
            java.lang.String r2 = r4.zzmi()     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0096
        L_0x0050:
            com.google.android.gms.auth.api.signin.EmailSignInConfig r1 = r3.zzTt     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 != 0) goto L_0x005b
            com.google.android.gms.auth.api.signin.EmailSignInConfig r1 = r4.zzmf()     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 != 0) goto L_0x0096
            goto L_0x0067
        L_0x005b:
            com.google.android.gms.auth.api.signin.EmailSignInConfig r1 = r3.zzTt     // Catch:{ ClassCastException -> 0x0096 }
            com.google.android.gms.auth.api.signin.EmailSignInConfig r2 = r4.zzmf()     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0096
        L_0x0067:
            com.google.android.gms.auth.api.signin.FacebookSignInConfig r1 = r3.zzTv     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 != 0) goto L_0x0072
            com.google.android.gms.auth.api.signin.FacebookSignInConfig r1 = r4.zzmh()     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 != 0) goto L_0x0096
            goto L_0x007e
        L_0x0072:
            com.google.android.gms.auth.api.signin.FacebookSignInConfig r1 = r3.zzTv     // Catch:{ ClassCastException -> 0x0096 }
            com.google.android.gms.auth.api.signin.FacebookSignInConfig r2 = r4.zzmh()     // Catch:{ ClassCastException -> 0x0096 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 == 0) goto L_0x0096
        L_0x007e:
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r1 = r3.zzTu     // Catch:{ ClassCastException -> 0x0096 }
            if (r1 != 0) goto L_0x0089
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r4 = r4.zzmg()     // Catch:{ ClassCastException -> 0x0096 }
            if (r4 != 0) goto L_0x0096
            goto L_0x0095
        L_0x0089:
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r1 = r3.zzTu     // Catch:{ ClassCastException -> 0x0096 }
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r4 = r4.zzmg()     // Catch:{ ClassCastException -> 0x0096 }
            boolean r4 = r1.equals(r4)     // Catch:{ ClassCastException -> 0x0096 }
            if (r4 == 0) goto L_0x0096
        L_0x0095:
            r0 = 1
        L_0x0096:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.internal.SignInConfiguration.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return new zzc().zzl(this.zzTs).zzl(this.zzTl).zzl(this.zzTw).zzl(this.zzTt).zzl(this.zzTu).zzl(this.zzTv).zzmd();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzh.zza(this, parcel, i);
    }

    public String zzmb() {
        return this.zzTl;
    }

    public String zzme() {
        return this.zzTs;
    }

    public EmailSignInConfig zzmf() {
        return this.zzTt;
    }

    public GoogleSignInConfig zzmg() {
        return this.zzTu;
    }

    public FacebookSignInConfig zzmh() {
        return this.zzTv;
    }

    public String zzmi() {
        return this.zzTw;
    }
}
