package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class zzj<T extends IInterface> implements com.google.android.gms.common.api.Api.zzb, com.google.android.gms.common.internal.zzk.zza {
    public static final String[] zzafI = {"service_esmobile", "service_googleme"};
    private final Context mContext;
    final Handler mHandler;
    private final Account zzQd;
    /* access modifiers changed from: private */
    public final Set<Scope> zzTm;
    private final Looper zzaaO;
    private final GoogleApiAvailability zzaaP;
    private final zzf zzabI;
    private T zzafA;
    /* access modifiers changed from: private */
    public final ArrayList<zzc<?>> zzafB;
    private zze zzafC;
    private int zzafD;
    /* access modifiers changed from: private */
    public final ConnectionCallbacks zzafE;
    /* access modifiers changed from: private */
    public final OnConnectionFailedListener zzafF;
    private final int zzafG;
    protected AtomicInteger zzafH;
    private final zzl zzafx;
    /* access modifiers changed from: private */
    public zzs zzafy;
    /* access modifiers changed from: private */
    public com.google.android.gms.common.api.GoogleApiClient.zza zzafz;
    private final Object zzpd;

    private abstract class zza extends zzc<Boolean> {
        public final int statusCode;
        public final Bundle zzafJ;

        protected zza(int i, Bundle bundle) {
            super(Boolean.valueOf(true));
            this.statusCode = i;
            this.zzafJ = bundle;
        }

        /* access modifiers changed from: protected */
        /* renamed from: zzc */
        public void zzt(Boolean bool) {
            ConnectionResult connectionResult;
            PendingIntent pendingIntent = null;
            if (bool == null) {
                zzj.this.zzb(1, null);
                return;
            }
            int i = this.statusCode;
            if (i == 0) {
                if (!zzpf()) {
                    zzj.this.zzb(1, null);
                    connectionResult = new ConnectionResult(8, null);
                }
            } else if (i != 10) {
                zzj.this.zzb(1, null);
                Bundle bundle = this.zzafJ;
                if (bundle != null) {
                    pendingIntent = (PendingIntent) bundle.getParcelable("pendingIntent");
                }
                connectionResult = new ConnectionResult(this.statusCode, pendingIntent);
            } else {
                zzj.this.zzb(1, null);
                throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
            }
            zzh(connectionResult);
        }

        /* access modifiers changed from: protected */
        public abstract void zzh(ConnectionResult connectionResult);

        /* access modifiers changed from: protected */
        public abstract boolean zzpf();

        /* access modifiers changed from: protected */
        public void zzpg() {
        }
    }

    final class zzb extends Handler {
        public zzb(Looper looper) {
            super(looper);
        }

        private void zza(Message message) {
            zzc zzc = (zzc) message.obj;
            zzc.zzpg();
            zzc.unregister();
        }

        private boolean zzb(Message message) {
            return message.what == 2 || message.what == 1 || message.what == 5 || message.what == 6;
        }

        public void handleMessage(Message message) {
            if (zzj.this.zzafH.get() != message.arg1) {
                if (zzb(message)) {
                    zza(message);
                }
            } else if ((message.what == 1 || message.what == 5 || message.what == 6) && !zzj.this.isConnecting()) {
                zza(message);
            } else if (message.what == 3) {
                ConnectionResult connectionResult = new ConnectionResult(message.arg2, null);
                zzj.this.zzafz.zza(connectionResult);
                zzj.this.onConnectionFailed(connectionResult);
            } else if (message.what == 4) {
                zzj.this.zzb(4, null);
                if (zzj.this.zzafE != null) {
                    zzj.this.zzafE.onConnectionSuspended(message.arg2);
                }
                zzj.this.onConnectionSuspended(message.arg2);
                zzj.this.zza(4, 1, null);
            } else if (message.what == 2 && !zzj.this.isConnected()) {
                zza(message);
            } else if (zzb(message)) {
                ((zzc) message.obj).zzph();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Don't know how to handle message: ");
                sb.append(message.what);
                Log.wtf("GmsClient", sb.toString(), new Exception());
            }
        }
    }

    protected abstract class zzc<TListener> {
        private TListener mListener;
        private boolean zzafL = false;

        public zzc(TListener tlistener) {
            this.mListener = tlistener;
        }

        public void unregister() {
            zzpi();
            synchronized (zzj.this.zzafB) {
                zzj.this.zzafB.remove(this);
            }
        }

        /* access modifiers changed from: protected */
        public abstract void zzpg();

        public void zzph() {
            TListener tlistener;
            synchronized (this) {
                tlistener = this.mListener;
                if (this.zzafL) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Callback proxy ");
                    sb.append(this);
                    sb.append(" being reused. This is not safe.");
                    Log.w("GmsClient", sb.toString());
                }
            }
            if (tlistener != null) {
                try {
                    zzt(tlistener);
                } catch (RuntimeException e) {
                    zzpg();
                    throw e;
                }
            } else {
                zzpg();
            }
            synchronized (this) {
                this.zzafL = true;
            }
            unregister();
        }

        public void zzpi() {
            synchronized (this) {
                this.mListener = null;
            }
        }

        /* access modifiers changed from: protected */
        public abstract void zzt(TListener tlistener);
    }

    public static final class zzd extends com.google.android.gms.common.internal.zzr.zza {
        private zzj zzafM;
        private final int zzafN;

        public zzd(zzj zzj, int i) {
            this.zzafM = zzj;
            this.zzafN = i;
        }

        private void zzpj() {
            this.zzafM = null;
        }

        public void zza(int i, IBinder iBinder, Bundle bundle) {
            zzx.zzb(this.zzafM, (Object) "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzafM.zza(i, iBinder, bundle, this.zzafN);
            zzpj();
        }

        public void zzb(int i, Bundle bundle) {
            zzx.zzb(this.zzafM, (Object) "onAccountValidationComplete can be called only once per call to validateAccount");
            this.zzafM.zza(i, bundle, this.zzafN);
            zzpj();
        }
    }

    public final class zze implements ServiceConnection {
        private final int zzafN;

        public zze(int i) {
            this.zzafN = i;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            zzx.zzb(iBinder, (Object) "Expecting a valid IBinder");
            zzj.this.zzafy = com.google.android.gms.common.internal.zzs.zza.zzaK(iBinder);
            zzj.this.zzbF(this.zzafN);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            zzj.this.mHandler.sendMessage(zzj.this.mHandler.obtainMessage(4, this.zzafN, 1));
        }
    }

    protected class zzf implements com.google.android.gms.common.api.GoogleApiClient.zza {
        public zzf() {
        }

        public void zza(ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                zzj zzj = zzj.this;
                zzj.zza((zzp) null, zzj.zzTm);
            } else if (zzj.this.zzafF != null) {
                zzj.this.zzafF.onConnectionFailed(connectionResult);
            }
        }

        public void zzb(ConnectionResult connectionResult) {
            throw new IllegalStateException("Legacy GmsClient received onReportAccountValidation callback.");
        }
    }

    protected final class zzg extends zza {
        public final IBinder zzafO;

        public zzg(int i, IBinder iBinder, Bundle bundle) {
            super(i, bundle);
            this.zzafO = iBinder;
        }

        /* access modifiers changed from: protected */
        public void zzh(ConnectionResult connectionResult) {
            if (zzj.this.zzafF != null) {
                zzj.this.zzafF.onConnectionFailed(connectionResult);
            }
            zzj.this.onConnectionFailed(connectionResult);
        }

        /* access modifiers changed from: protected */
        public boolean zzpf() {
            String str = "GmsClient";
            try {
                String interfaceDescriptor = this.zzafO.getInterfaceDescriptor();
                if (!zzj.this.zzfL().equals(interfaceDescriptor)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("service descriptor mismatch: ");
                    sb.append(zzj.this.zzfL());
                    sb.append(" vs. ");
                    sb.append(interfaceDescriptor);
                    Log.e(str, sb.toString());
                    return false;
                }
                IInterface zzW = zzj.this.zzW(this.zzafO);
                if (zzW == null || !zzj.this.zza(2, 3, zzW)) {
                    return false;
                }
                Bundle zzmS = zzj.this.zzmS();
                if (zzj.this.zzafE != null) {
                    zzj.this.zzafE.onConnected(zzmS);
                }
                return true;
            } catch (RemoteException unused) {
                Log.w(str, "service probably died");
                return false;
            }
        }
    }

    protected final class zzh extends zza {
        public zzh() {
            super(0, null);
        }

        /* access modifiers changed from: protected */
        public void zzh(ConnectionResult connectionResult) {
            zzj.this.zzafz.zza(connectionResult);
            zzj.this.onConnectionFailed(connectionResult);
        }

        /* access modifiers changed from: protected */
        public boolean zzpf() {
            zzj.this.zzafz.zza(ConnectionResult.zzZY);
            return true;
        }
    }

    protected final class zzi extends zza {
        public zzi(int i, Bundle bundle) {
            super(i, bundle);
        }

        /* access modifiers changed from: protected */
        public void zzh(ConnectionResult connectionResult) {
            zzj.this.zzafz.zzb(connectionResult);
            zzj.this.onConnectionFailed(connectionResult);
        }

        /* access modifiers changed from: protected */
        public boolean zzpf() {
            zzj.this.zzafz.zzb(ConnectionResult.zzZY);
            return true;
        }
    }

    protected zzj(Context context, Looper looper, int i, zzf zzf2, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, zzl.zzal(context), GoogleApiAvailability.getInstance(), i, zzf2, (ConnectionCallbacks) zzx.zzw(connectionCallbacks), (OnConnectionFailedListener) zzx.zzw(onConnectionFailedListener));
    }

    protected zzj(Context context, Looper looper, zzl zzl, GoogleApiAvailability googleApiAvailability, int i, zzf zzf2, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this.zzpd = new Object();
        this.zzafB = new ArrayList<>();
        this.zzafD = 1;
        this.zzafH = new AtomicInteger(0);
        this.mContext = (Context) zzx.zzb(context, (Object) "Context must not be null");
        this.zzaaO = (Looper) zzx.zzb(looper, (Object) "Looper must not be null");
        this.zzafx = (zzl) zzx.zzb(zzl, (Object) "Supervisor must not be null");
        this.zzaaP = (GoogleApiAvailability) zzx.zzb(googleApiAvailability, (Object) "API availability must not be null");
        this.mHandler = new zzb(looper);
        this.zzafG = i;
        this.zzabI = (zzf) zzx.zzw(zzf2);
        this.zzQd = zzf2.getAccount();
        this.zzTm = zza(zzf2.zzoL());
        this.zzafE = connectionCallbacks;
        this.zzafF = onConnectionFailedListener;
    }

    private Set<Scope> zza(Set<Scope> set) {
        Set<Scope> zzb2 = zzb(set);
        if (zzb2 == null) {
            return zzb2;
        }
        for (Scope contains : zzb2) {
            if (!set.contains(contains)) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return zzb2;
    }

    /* access modifiers changed from: private */
    public boolean zza(int i, int i2, T t) {
        synchronized (this.zzpd) {
            if (this.zzafD != i) {
                return false;
            }
            zzb(i2, t);
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void zzb(int i, T t) {
        boolean z = false;
        if ((i == 3) == (t != null)) {
            z = true;
        }
        zzx.zzaa(z);
        synchronized (this.zzpd) {
            this.zzafD = i;
            this.zzafA = t;
            zzc(i, t);
            if (i == 1) {
                zzoY();
            } else if (i == 2) {
                zzoX();
            } else if (i == 3) {
                zzoW();
            }
        }
    }

    private void zzoX() {
        String str = "GmsClient";
        if (this.zzafC != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Calling connect() while still connected, missing disconnect() for ");
            sb.append(zzfK());
            Log.e(str, sb.toString());
            this.zzafx.zzb(zzfK(), (ServiceConnection) this.zzafC, zzoV());
            this.zzafH.incrementAndGet();
        }
        this.zzafC = new zze<>(this.zzafH.get());
        if (!this.zzafx.zza(zzfK(), (ServiceConnection) this.zzafC, zzoV())) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("unable to connect to service: ");
            sb2.append(zzfK());
            Log.e(str, sb2.toString());
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(3, this.zzafH.get(), 9));
        }
    }

    private void zzoY() {
        if (this.zzafC != null) {
            this.zzafx.zzb(zzfK(), (ServiceConnection) this.zzafC, zzoV());
            this.zzafC = null;
        }
    }

    public void disconnect() {
        this.zzafH.incrementAndGet();
        synchronized (this.zzafB) {
            int size = this.zzafB.size();
            for (int i = 0; i < size; i++) {
                ((zzc) this.zzafB.get(i)).zzpi();
            }
            this.zzafB.clear();
        }
        zzb(1, null);
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int i;
        T t;
        synchronized (this.zzpd) {
            i = this.zzafD;
            t = this.zzafA;
        }
        printWriter.append(str).append("mConnectState=");
        String str2 = i != 1 ? i != 2 ? i != 3 ? i != 4 ? "UNKNOWN" : "DISCONNECTING" : "CONNECTED" : "CONNECTING" : "DISCONNECTED";
        printWriter.print(str2);
        printWriter.append(" mService=");
        if (t == null) {
            printWriter.println("null");
        } else {
            printWriter.append(zzfL()).append("@").println(Integer.toHexString(System.identityHashCode(t.asBinder())));
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.zzaaO;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.zzpd) {
            z = this.zzafD == 3;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.zzpd) {
            z = this.zzafD == 2;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    /* access modifiers changed from: protected */
    public void onConnectionSuspended(int i) {
    }

    /* access modifiers changed from: protected */
    public abstract T zzW(IBinder iBinder);

    /* access modifiers changed from: protected */
    public void zza(int i, Bundle bundle, int i2) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(5, i2, -1, new zzi(i, bundle)));
    }

    /* access modifiers changed from: protected */
    public void zza(int i, IBinder iBinder, Bundle bundle, int i2) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(1, i2, -1, new zzg(i, iBinder, bundle)));
    }

    public void zza(com.google.android.gms.common.api.GoogleApiClient.zza zza2) {
        this.zzafz = (com.google.android.gms.common.api.GoogleApiClient.zza) zzx.zzb(zza2, (Object) "Connection progress callbacks cannot be null.");
        zzb(2, null);
    }

    public void zza(zzp zzp) {
        String str = "GmsClient";
        Bundle zzpd2 = zzpd();
        Set<Scope> set = this.zzTm;
        try {
            this.zzafy.zza((zzr) new zzd(this, this.zzafH.get()), new ValidateAccountRequest(zzp, (Scope[]) set.toArray(new Scope[set.size()]), this.mContext.getPackageName(), zzpd2));
        } catch (DeadObjectException unused) {
            Log.w(str, "service died");
            zzbE(1);
        } catch (RemoteException e) {
            Log.w(str, "Remote exception occurred", e);
        }
    }

    public void zza(zzp zzp, Set<Scope> set) {
        String str = "GmsClient";
        try {
            GetServiceRequest zzg2 = new GetServiceRequest(this.zzafG).zzcl(this.mContext.getPackageName()).zzg(zzly());
            if (set != null) {
                zzg2.zzd(set);
            }
            if (zzlN()) {
                zzg2.zzc(zzoI()).zzc(zzp);
            } else if (zzpe()) {
                zzg2.zzc(this.zzQd);
            }
            this.zzafy.zza((zzr) new zzd(this, this.zzafH.get()), zzg2);
        } catch (DeadObjectException unused) {
            Log.w(str, "service died");
            zzbE(1);
        } catch (RemoteException e) {
            Log.w(str, "Remote exception occurred", e);
        }
    }

    /* access modifiers changed from: protected */
    public Set<Scope> zzb(Set<Scope> set) {
        return set;
    }

    public void zzbE(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(4, this.zzafH.get(), i));
    }

    /* access modifiers changed from: protected */
    public void zzbF(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(6, i, -1, new zzh()));
    }

    /* access modifiers changed from: protected */
    public void zzc(int i, T t) {
    }

    /* access modifiers changed from: protected */
    public abstract String zzfK();

    /* access modifiers changed from: protected */
    public abstract String zzfL();

    public boolean zzlN() {
        return false;
    }

    /* access modifiers changed from: protected */
    public Bundle zzly() {
        return new Bundle();
    }

    public Bundle zzmS() {
        return null;
    }

    public IBinder zznz() {
        zzs zzs = this.zzafy;
        if (zzs == null) {
            return null;
        }
        return zzs.asBinder();
    }

    public final Account zzoI() {
        Account account = this.zzQd;
        return account != null ? account : new Account("<<default account>>", GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
    }

    /* access modifiers changed from: protected */
    public final String zzoV() {
        return this.zzabI.zzoO();
    }

    /* access modifiers changed from: protected */
    public void zzoW() {
    }

    public void zzoZ() {
        int isGooglePlayServicesAvailable = this.zzaaP.isGooglePlayServicesAvailable(this.mContext);
        if (isGooglePlayServicesAvailable != 0) {
            zzb(1, null);
            this.zzafz = new zzf();
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(3, this.zzafH.get(), isGooglePlayServicesAvailable));
            return;
        }
        zza((com.google.android.gms.common.api.GoogleApiClient.zza) new zzf());
    }

    /* access modifiers changed from: protected */
    public final zzf zzpa() {
        return this.zzabI;
    }

    /* access modifiers changed from: protected */
    public final void zzpb() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public final T zzpc() throws DeadObjectException {
        T t;
        synchronized (this.zzpd) {
            if (this.zzafD != 4) {
                zzpb();
                zzx.zza(this.zzafA != null, (Object) "Client is connected but service is null");
                t = this.zzafA;
            } else {
                throw new DeadObjectException();
            }
        }
        return t;
    }

    /* access modifiers changed from: protected */
    public Bundle zzpd() {
        return null;
    }

    public boolean zzpe() {
        return false;
    }
}
