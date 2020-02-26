package com.google.android.gms.signin.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ServerAuthCodeCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.ServerAuthCodeCallbacks.CheckResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.AuthAccountRequest;
import com.google.android.gms.common.internal.BinderWrapper;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzqw;
import com.google.android.gms.internal.zzqx;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class zzi extends zzj<zzf> implements zzqw {
    private final boolean zzaVl;
    private final ExecutorService zzaVm;
    private final zzqx zzaaT;
    private final zzf zzabI;
    private Integer zzafj;

    private static class zza extends com.google.android.gms.signin.internal.zzd.zza {
        private final ExecutorService zzaVm;
        private final zzqx zzaaT;

        public zza(zzqx zzqx, ExecutorService executorService) {
            this.zzaaT = zzqx;
            this.zzaVm = executorService;
        }

        /* access modifiers changed from: private */
        public ServerAuthCodeCallbacks zzCg() throws RemoteException {
            return this.zzaaT.zzCg();
        }

        public void zza(final String str, final String str2, final zzf zzf) throws RemoteException {
            this.zzaVm.submit(new Runnable() {
                public void run() {
                    try {
                        zzf.zzaq(zza.this.zzCg().onUploadServerAuthCode(str, str2));
                    } catch (RemoteException e) {
                        Log.e("SignInClientImpl", "RemoteException thrown when processing uploadServerAuthCode callback", e);
                    }
                }
            });
        }

        public void zza(final String str, final List<Scope> list, final zzf zzf) throws RemoteException {
            this.zzaVm.submit(new Runnable() {
                public void run() {
                    try {
                        CheckResult onCheckServerAuthorization = zza.this.zzCg().onCheckServerAuthorization(str, Collections.unmodifiableSet(new HashSet(list)));
                        zzf.zza(new CheckServerAuthResult(onCheckServerAuthorization.zznD(), onCheckServerAuthorization.zznE()));
                    } catch (RemoteException e) {
                        Log.e("SignInClientImpl", "RemoteException thrown when processing checkServerAuthorization callback", e);
                    }
                }
            });
        }
    }

    public zzi(Context context, Looper looper, boolean z, zzf zzf, zzqx zzqx, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, ExecutorService executorService) {
        super(context, looper, 44, zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzaVl = z;
        this.zzabI = zzf;
        this.zzaaT = zzqx;
        this.zzafj = zzf.zzoR();
        this.zzaVm = executorService;
    }

    public static Bundle zza(zzqx zzqx, Integer num, ExecutorService executorService) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", zzqx.zzCf());
        bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", zzqx.zzlY());
        bundle.putString("com.google.android.gms.signin.internal.serverClientId", zzqx.zzmb());
        if (zzqx.zzCg() != null) {
            bundle.putParcelable("com.google.android.gms.signin.internal.signInCallbacks", new BinderWrapper(new zza(zzqx, executorService).asBinder()));
        }
        if (num != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", num.intValue());
        }
        bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", zzqx.zzCh());
        bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", zzqx.zzma());
        return bundle;
    }

    public void connect() {
        zza((com.google.android.gms.common.api.GoogleApiClient.zza) new zzf());
    }

    public void zzCe() {
        try {
            ((zzf) zzpc()).zzjq(this.zzafj.intValue());
        } catch (RemoteException unused) {
            Log.w("SignInClientImpl", "Remote service probably died when clearAccountFromSessionStore is called");
        }
    }

    public void zza(zzp zzp, Set<Scope> set, zze zze) {
        zzx.zzb(zze, (Object) "Expecting a valid ISignInCallbacks");
        try {
            ((zzf) zzpc()).zza(new AuthAccountRequest(zzp, set), zze);
        } catch (RemoteException e) {
            String str = "SignInClientImpl";
            Log.w(str, "Remote service probably died when authAccount is called");
            try {
                zze.zza(new ConnectionResult(8, null), new AuthAccountResult());
            } catch (RemoteException unused) {
                Log.wtf(str, "ISignInCallbacks#onAuthAccount should be executed from the same process, unexpected RemoteException.", e);
            }
        }
    }

    public void zza(zzp zzp, boolean z) {
        try {
            ((zzf) zzpc()).zza(zzp, this.zzafj.intValue(), z);
        } catch (RemoteException unused) {
            Log.w("SignInClientImpl", "Remote service probably died when saveDefaultAccount is called");
        }
    }

    public void zza(zzt zzt) {
        zzx.zzb(zzt, (Object) "Expecting a valid IResolveAccountCallbacks");
        try {
            ((zzf) zzpc()).zza(new ResolveAccountRequest(this.zzabI.zzoI(), this.zzafj.intValue()), zzt);
        } catch (RemoteException e) {
            String str = "SignInClientImpl";
            Log.w(str, "Remote service probably died when resolveAccount is called");
            try {
                zzt.zzb(new ResolveAccountResponse(8));
            } catch (RemoteException unused) {
                Log.wtf(str, "IResolveAccountCallbacks#onAccountResolutionComplete should be executed from the same process, unexpected RemoteException.", e);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzdO */
    public zzf zzW(IBinder iBinder) {
        return com.google.android.gms.signin.internal.zzf.zza.zzdN(iBinder);
    }

    /* access modifiers changed from: protected */
    public String zzfK() {
        return "com.google.android.gms.signin.service.START";
    }

    /* access modifiers changed from: protected */
    public String zzfL() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    public boolean zzlN() {
        return this.zzaVl;
    }

    /* access modifiers changed from: protected */
    public Bundle zzly() {
        Bundle zza2 = zza(this.zzaaT, this.zzabI.zzoR(), this.zzaVm);
        if (!getContext().getPackageName().equals(this.zzabI.zzoN())) {
            zza2.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzabI.zzoN());
        }
        return zza2;
    }
}
