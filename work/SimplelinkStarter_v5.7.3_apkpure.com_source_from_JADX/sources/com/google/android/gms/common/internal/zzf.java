package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.view.View;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzqx;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class zzf {
    private final Account zzQd;
    private final String zzRq;
    private final Set<Scope> zzaaF;
    private final int zzaaG;
    private final View zzaaH;
    private final String zzaaI;
    private final zzqx zzaaT;
    private final Set<Scope> zzafh;
    private final Map<Api<?>, zza> zzafi;
    private Integer zzafj;

    public static final class zza {
        public final Set<Scope> zzTm;
        public final boolean zzafk;

        public zza(Set<Scope> set, boolean z) {
            zzx.zzw(set);
            this.zzTm = Collections.unmodifiableSet(set);
            this.zzafk = z;
        }
    }

    public zzf(Account account, Set<Scope> set, Map<Api<?>, zza> map, int i, View view, String str, String str2, zzqx zzqx) {
        this.zzQd = account;
        this.zzaaF = set == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(set);
        if (map == null) {
            map = Collections.EMPTY_MAP;
        }
        this.zzafi = map;
        this.zzaaH = view;
        this.zzaaG = i;
        this.zzRq = str;
        this.zzaaI = str2;
        this.zzaaT = zzqx;
        HashSet hashSet = new HashSet(this.zzaaF);
        for (zza zza2 : this.zzafi.values()) {
            hashSet.addAll(zza2.zzTm);
        }
        this.zzafh = Collections.unmodifiableSet(hashSet);
    }

    public static zzf zzak(Context context) {
        return new Builder(context).zznB();
    }

    public Account getAccount() {
        return this.zzQd;
    }

    @Deprecated
    public String getAccountName() {
        Account account = this.zzQd;
        if (account != null) {
            return account.name;
        }
        return null;
    }

    public void zza(Integer num) {
        this.zzafj = num;
    }

    public Set<Scope> zzb(Api<?> api) {
        zza zza2 = (zza) this.zzafi.get(api);
        if (zza2 == null || zza2.zzTm.isEmpty()) {
            return this.zzaaF;
        }
        HashSet hashSet = new HashSet(this.zzaaF);
        hashSet.addAll(zza2.zzTm);
        return hashSet;
    }

    public Account zzoI() {
        Account account = this.zzQd;
        return account != null ? account : new Account("<<default account>>", GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
    }

    public int zzoJ() {
        return this.zzaaG;
    }

    public Set<Scope> zzoK() {
        return this.zzaaF;
    }

    public Set<Scope> zzoL() {
        return this.zzafh;
    }

    public Map<Api<?>, zza> zzoM() {
        return this.zzafi;
    }

    public String zzoN() {
        return this.zzRq;
    }

    public String zzoO() {
        return this.zzaaI;
    }

    public View zzoP() {
        return this.zzaaH;
    }

    public zzqx zzoQ() {
        return this.zzaaT;
    }

    public Integer zzoR() {
        return this.zzafj;
    }
}
