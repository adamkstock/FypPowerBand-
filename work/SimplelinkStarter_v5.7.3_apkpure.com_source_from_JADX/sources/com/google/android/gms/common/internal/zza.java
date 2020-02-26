package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class zza extends com.google.android.gms.common.internal.zzp.zza {
    private Context mContext;
    private Account zzQd;
    int zzaeG;

    public static Account zzb(zzp zzp) {
        if (zzp != null) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return zzp.getAccount();
            } catch (RemoteException unused) {
                Log.w("AccountAccessor", "Remote account accessor probably died");
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zza)) {
            return false;
        }
        return this.zzQd.equals(((zza) obj).zzQd);
    }

    public Account getAccount() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == this.zzaeG) {
            return this.zzQd;
        }
        if (GooglePlayServicesUtil.zze(this.mContext, callingUid)) {
            this.zzaeG = callingUid;
            return this.zzQd;
        }
        throw new SecurityException("Caller is not GooglePlayServices");
    }
}
