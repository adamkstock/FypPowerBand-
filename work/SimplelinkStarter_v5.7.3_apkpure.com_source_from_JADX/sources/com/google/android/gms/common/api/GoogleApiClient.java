package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.p000v4.app.FragmentActivity;
import android.view.View;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Api.ApiOptions.NotRequiredOptions;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzli;
import com.google.android.gms.internal.zzlm;
import com.google.android.gms.internal.zzlp;
import com.google.android.gms.internal.zzme;
import com.google.android.gms.internal.zzqu;
import com.google.android.gms.internal.zzqw;
import com.google.android.gms.internal.zzqx;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class GoogleApiClient {

    public static final class Builder {
        private final Context mContext;
        private Account zzQd;
        private String zzRq;
        private final Set<Scope> zzaaF;
        private int zzaaG;
        private View zzaaH;
        private String zzaaI;
        private final Map<Api<?>, com.google.android.gms.common.internal.zzf.zza> zzaaJ;
        private final Map<Api<?>, ApiOptions> zzaaK;
        /* access modifiers changed from: private */
        public FragmentActivity zzaaL;
        private int zzaaM;
        private OnConnectionFailedListener zzaaN;
        private Looper zzaaO;
        private GoogleApiAvailability zzaaP;
        private com.google.android.gms.common.api.Api.zza<? extends zzqw, zzqx> zzaaQ;
        private final ArrayList<ConnectionCallbacks> zzaaR;
        private final ArrayList<OnConnectionFailedListener> zzaaS;
        private zzqx zzaaT;

        public Builder(Context context) {
            this.zzaaF = new HashSet();
            this.zzaaJ = new zzme();
            this.zzaaK = new zzme();
            this.zzaaM = -1;
            this.zzaaP = GoogleApiAvailability.getInstance();
            this.zzaaQ = zzqu.zzRl;
            this.zzaaR = new ArrayList<>();
            this.zzaaS = new ArrayList<>();
            this.mContext = context;
            this.zzaaO = context.getMainLooper();
            this.zzRq = context.getPackageName();
            this.zzaaI = context.getClass().getName();
        }

        public Builder(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            this(context);
            zzx.zzb(connectionCallbacks, (Object) "Must provide a connected listener");
            this.zzaaR.add(connectionCallbacks);
            zzx.zzb(onConnectionFailedListener, (Object) "Must provide a connection failed listener");
            this.zzaaS.add(onConnectionFailedListener);
        }

        private <O extends ApiOptions> void zza(Api<O> api, O o, int i, Scope... scopeArr) {
            boolean z = true;
            if (i != 1) {
                if (i == 2) {
                    z = false;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid resolution mode: '");
                    sb.append(i);
                    sb.append("', use a constant from GoogleApiClient.ResolutionMode");
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            HashSet hashSet = new HashSet(api.zznv().zzm(o));
            for (Scope add : scopeArr) {
                hashSet.add(add);
            }
            this.zzaaJ.put(api, new com.google.android.gms.common.internal.zzf.zza(hashSet, z));
        }

        /* access modifiers changed from: private */
        public void zza(zzlp zzlp, GoogleApiClient googleApiClient) {
            zzlp.zza(this.zzaaM, googleApiClient, this.zzaaN);
        }

        private GoogleApiClient zznC() {
            final zzli zzli = new zzli(this.mContext.getApplicationContext(), this.zzaaO, zznB(), this.zzaaP, this.zzaaQ, this.zzaaK, this.zzaaR, this.zzaaS, this.zzaaM);
            zzlp zza = zzlp.zza(this.zzaaL);
            if (zza == null) {
                new Handler(this.mContext.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (!Builder.this.zzaaL.isFinishing() && !Builder.this.zzaaL.getSupportFragmentManager().isDestroyed()) {
                            Builder builder = Builder.this;
                            builder.zza(zzlp.zzb(builder.zzaaL), zzli);
                        }
                    }
                });
            } else {
                zza(zza, zzli);
            }
            return zzli;
        }

        public Builder addApi(Api<? extends NotRequiredOptions> api) {
            zzx.zzb(api, (Object) "Api must not be null");
            this.zzaaK.put(api, null);
            this.zzaaF.addAll(api.zznv().zzm(null));
            return this;
        }

        public <O extends HasOptions> Builder addApi(Api<O> api, O o) {
            zzx.zzb(api, (Object) "Api must not be null");
            zzx.zzb(o, (Object) "Null options are not permitted for this Api");
            this.zzaaK.put(api, o);
            this.zzaaF.addAll(api.zznv().zzm(o));
            return this;
        }

        public <O extends HasOptions> Builder addApiIfAvailable(Api<O> api, O o, Scope... scopeArr) {
            zzx.zzb(api, (Object) "Api must not be null");
            zzx.zzb(o, (Object) "Null options are not permitted for this Api");
            this.zzaaK.put(api, o);
            zza(api, o, 1, scopeArr);
            return this;
        }

        public Builder addApiIfAvailable(Api<? extends NotRequiredOptions> api, Scope... scopeArr) {
            zzx.zzb(api, (Object) "Api must not be null");
            this.zzaaK.put(api, null);
            zza(api, null, 1, scopeArr);
            return this;
        }

        public Builder addConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
            zzx.zzb(connectionCallbacks, (Object) "Listener must not be null");
            this.zzaaR.add(connectionCallbacks);
            return this;
        }

        public Builder addOnConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
            zzx.zzb(onConnectionFailedListener, (Object) "Listener must not be null");
            this.zzaaS.add(onConnectionFailedListener);
            return this;
        }

        public Builder addScope(Scope scope) {
            zzx.zzb(scope, (Object) "Scope must not be null");
            this.zzaaF.add(scope);
            return this;
        }

        public GoogleApiClient build() {
            zzx.zzb(!this.zzaaK.isEmpty(), (Object) "must call addApi() to add at least one API");
            if (this.zzaaM >= 0) {
                return zznC();
            }
            zzli zzli = new zzli(this.mContext, this.zzaaO, zznB(), this.zzaaP, this.zzaaQ, this.zzaaK, this.zzaaR, this.zzaaS, -1);
            return zzli;
        }

        public Builder enableAutoManage(FragmentActivity fragmentActivity, int i, OnConnectionFailedListener onConnectionFailedListener) {
            zzx.zzb(i >= 0, (Object) "clientId must be non-negative");
            this.zzaaM = i;
            this.zzaaL = (FragmentActivity) zzx.zzb(fragmentActivity, (Object) "Null activity is not permitted.");
            this.zzaaN = onConnectionFailedListener;
            return this;
        }

        public Builder enableAutoManage(FragmentActivity fragmentActivity, OnConnectionFailedListener onConnectionFailedListener) {
            return enableAutoManage(fragmentActivity, 0, onConnectionFailedListener);
        }

        public Builder requestServerAuthCode(String str, ServerAuthCodeCallbacks serverAuthCodeCallbacks) {
            this.zzaaT = new com.google.android.gms.internal.zzqx.zza().zza(str, serverAuthCodeCallbacks).zzCi();
            return this;
        }

        public Builder setAccountName(String str) {
            this.zzQd = str == null ? null : new Account(str, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            return this;
        }

        public Builder setGravityForPopups(int i) {
            this.zzaaG = i;
            return this;
        }

        public Builder setHandler(Handler handler) {
            zzx.zzb(handler, (Object) "Handler must not be null");
            this.zzaaO = handler.getLooper();
            return this;
        }

        public Builder setViewForPopups(View view) {
            zzx.zzb(view, (Object) "View must not be null");
            this.zzaaH = view;
            return this;
        }

        public Builder useDefaultAccount() {
            return setAccountName("<<default account>>");
        }

        public zzf zznB() {
            if (this.zzaaK.containsKey(zzqu.API)) {
                zzx.zza(this.zzaaT == null, (Object) "SignIn.API can't be used in conjunction with requestServerAuthCode.");
                this.zzaaT = (zzqx) this.zzaaK.get(zzqu.API);
            }
            Account account = this.zzQd;
            Set<Scope> set = this.zzaaF;
            Map<Api<?>, com.google.android.gms.common.internal.zzf.zza> map = this.zzaaJ;
            int i = this.zzaaG;
            View view = this.zzaaH;
            String str = this.zzRq;
            String str2 = this.zzaaI;
            zzqx zzqx = this.zzaaT;
            if (zzqx == null) {
                zzqx = zzqx.zzaUZ;
            }
            zzf zzf = new zzf(account, set, map, i, view, str, str2, zzqx);
            return zzf;
        }
    }

    public interface ConnectionCallbacks {
        public static final int CAUSE_NETWORK_LOST = 2;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;

        void onConnected(Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public interface OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    public interface ServerAuthCodeCallbacks {

        public static class CheckResult {
            private Set<Scope> zzTm;
            private boolean zzaaV;

            private CheckResult(boolean z, Set<Scope> set) {
                this.zzaaV = z;
                this.zzTm = set;
            }

            public static CheckResult newAuthNotRequiredResult() {
                return new CheckResult(false, null);
            }

            public static CheckResult newAuthRequiredResult(Set<Scope> set) {
                zzx.zzb(set != null && !set.isEmpty(), (Object) "A non-empty scope set is required if further auth is needed.");
                return new CheckResult(true, set);
            }

            public boolean zznD() {
                return this.zzaaV;
            }

            public Set<Scope> zznE() {
                return this.zzTm;
            }
        }

        CheckResult onCheckServerAuthorization(String str, Set<Scope> set);

        boolean onUploadServerAuthCode(String str, String str2);
    }

    public interface zza {
        void zza(ConnectionResult connectionResult);

        void zzb(ConnectionResult connectionResult);
    }

    public abstract ConnectionResult blockingConnect();

    public abstract ConnectionResult blockingConnect(long j, TimeUnit timeUnit);

    public abstract PendingResult<Status> clearDefaultAccountAndReconnect();

    public abstract void connect();

    public abstract void disconnect();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract ConnectionResult getConnectionResult(Api<?> api);

    public Context getContext() {
        throw new UnsupportedOperationException();
    }

    public Looper getLooper() {
        throw new UnsupportedOperationException();
    }

    public int getSessionId() {
        throw new UnsupportedOperationException();
    }

    public abstract boolean hasConnectedApi(Api<?> api);

    public abstract boolean isConnected();

    public abstract boolean isConnecting();

    public abstract boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks);

    public abstract boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener);

    public abstract void reconnect();

    public abstract void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks);

    public abstract void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    public abstract void stopAutoManage(FragmentActivity fragmentActivity);

    public abstract void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks);

    public abstract void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    public <C extends zzb> C zza(zzc<C> zzc) {
        throw new UnsupportedOperationException();
    }

    public <A extends zzb, R extends Result, T extends com.google.android.gms.internal.zzlb.zza<R, A>> T zza(T t) {
        throw new UnsupportedOperationException();
    }

    public boolean zza(Api<?> api) {
        throw new UnsupportedOperationException();
    }

    public <A extends zzb, T extends com.google.android.gms.internal.zzlb.zza<? extends Result, A>> T zzb(T t) {
        throw new UnsupportedOperationException();
    }

    public <L> zzlm<L> zzo(L l) {
        throw new UnsupportedOperationException();
    }
}
