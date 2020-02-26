package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.internal.zzc;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GoogleSignInConfig implements Optional, SafeParcelable {
    public static final Creator<GoogleSignInConfig> CREATOR = new zze();
    public static final Scope zzTe = new Scope(Scopes.PROFILE);
    public static final Scope zzTf = new Scope("email");
    public static final Scope zzTg = new Scope("openid");
    public static final GoogleSignInConfig zzTh = new zza().zzmc();
    final int versionCode;
    private Account zzQd;
    private final ArrayList<Scope> zzSX;
    private boolean zzTi;
    private final boolean zzTj;
    private final boolean zzTk;
    private String zzTl;

    public static final class zza {
        private Account zzQd;
        private boolean zzTi;
        private boolean zzTj;
        private boolean zzTk;
        private String zzTl;
        private Set<Scope> zzTm = new HashSet(Arrays.asList(new Scope[]{GoogleSignInConfig.zzTg}));

        public GoogleSignInConfig zzmc() {
            GoogleSignInConfig googleSignInConfig = new GoogleSignInConfig((Set) this.zzTm, this.zzQd, this.zzTi, this.zzTj, this.zzTk, this.zzTl);
            return googleSignInConfig;
        }
    }

    GoogleSignInConfig(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str) {
        this.versionCode = i;
        this.zzSX = arrayList;
        this.zzQd = account;
        this.zzTi = z;
        this.zzTj = z2;
        this.zzTk = z3;
        this.zzTl = str;
    }

    private GoogleSignInConfig(Set<Scope> set, Account account, boolean z, boolean z2, boolean z3, String str) {
        this(1, new ArrayList<>(set), account, z, z2, z3, str);
    }

    public int describeContents() {
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0038, code lost:
        if (r3.zzQd.equals(r4.getAccount()) != false) goto L_0x003a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0057, code lost:
        if (r3.zzTl.equals(r4.zzmb()) != false) goto L_0x0059;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r4 = (com.google.android.gms.auth.api.signin.GoogleSignInConfig) r4     // Catch:{ ClassCastException -> 0x0072 }
            java.util.ArrayList<com.google.android.gms.common.api.Scope> r1 = r3.zzSX     // Catch:{ ClassCastException -> 0x0072 }
            int r1 = r1.size()     // Catch:{ ClassCastException -> 0x0072 }
            java.util.ArrayList r2 = r4.zzlS()     // Catch:{ ClassCastException -> 0x0072 }
            int r2 = r2.size()     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 != r2) goto L_0x0072
            java.util.ArrayList<com.google.android.gms.common.api.Scope> r1 = r3.zzSX     // Catch:{ ClassCastException -> 0x0072 }
            java.util.ArrayList r2 = r4.zzlS()     // Catch:{ ClassCastException -> 0x0072 }
            boolean r1 = r1.containsAll(r2)     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 != 0) goto L_0x0023
            goto L_0x0072
        L_0x0023:
            android.accounts.Account r1 = r3.zzQd     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 != 0) goto L_0x002e
            android.accounts.Account r1 = r4.getAccount()     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 != 0) goto L_0x0072
            goto L_0x003a
        L_0x002e:
            android.accounts.Account r1 = r3.zzQd     // Catch:{ ClassCastException -> 0x0072 }
            android.accounts.Account r2 = r4.getAccount()     // Catch:{ ClassCastException -> 0x0072 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 == 0) goto L_0x0072
        L_0x003a:
            java.lang.String r1 = r3.zzTl     // Catch:{ ClassCastException -> 0x0072 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 == 0) goto L_0x004d
            java.lang.String r1 = r4.zzmb()     // Catch:{ ClassCastException -> 0x0072 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 == 0) goto L_0x0072
            goto L_0x0059
        L_0x004d:
            java.lang.String r1 = r3.zzTl     // Catch:{ ClassCastException -> 0x0072 }
            java.lang.String r2 = r4.zzmb()     // Catch:{ ClassCastException -> 0x0072 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 == 0) goto L_0x0072
        L_0x0059:
            boolean r1 = r3.zzTk     // Catch:{ ClassCastException -> 0x0072 }
            boolean r2 = r4.zzma()     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 != r2) goto L_0x0072
            boolean r1 = r3.zzTi     // Catch:{ ClassCastException -> 0x0072 }
            boolean r2 = r4.zzlY()     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 != r2) goto L_0x0072
            boolean r1 = r3.zzTj     // Catch:{ ClassCastException -> 0x0072 }
            boolean r4 = r4.zzlZ()     // Catch:{ ClassCastException -> 0x0072 }
            if (r1 != r4) goto L_0x0072
            r0 = 1
        L_0x0072:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.GoogleSignInConfig.equals(java.lang.Object):boolean");
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public int hashCode() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.zzSX.iterator();
        while (it.hasNext()) {
            arrayList.add(((Scope) it.next()).zznG());
        }
        Collections.sort(arrayList);
        return new zzc().zzl(arrayList).zzl(this.zzQd).zzl(this.zzTl).zzP(this.zzTk).zzP(this.zzTi).zzP(this.zzTj).zzmd();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }

    public ArrayList<Scope> zzlS() {
        return new ArrayList<>(this.zzSX);
    }

    public boolean zzlY() {
        return this.zzTi;
    }

    public boolean zzlZ() {
        return this.zzTj;
    }

    public boolean zzma() {
        return this.zzTk;
    }

    public String zzmb() {
        return this.zzTl;
    }
}
