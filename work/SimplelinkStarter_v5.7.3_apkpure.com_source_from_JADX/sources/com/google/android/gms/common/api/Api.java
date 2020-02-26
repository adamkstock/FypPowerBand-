package com.google.android.gms.common.api;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class Api<O extends ApiOptions> {
    private final String mName;
    private final zzc<?> zzZM;
    private final zza<?, O> zzaav;
    private final zze<?, O> zzaaw = null;
    private final zzf<?> zzaax;

    public interface ApiOptions {

        public interface HasOptions extends ApiOptions {
        }

        public static final class NoOptions implements NotRequiredOptions {
            private NoOptions() {
            }
        }

        public interface NotRequiredOptions extends ApiOptions {
        }

        public interface Optional extends HasOptions, NotRequiredOptions {
        }
    }

    public static abstract class zza<T extends zzb, O> {
        public int getPriority() {
            return ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        }

        public abstract T zza(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzf, O o, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener);

        public List<Scope> zzm(O o) {
            return Collections.emptyList();
        }
    }

    public interface zzb {
        void disconnect();

        void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

        boolean isConnected();

        void zza(com.google.android.gms.common.api.GoogleApiClient.zza zza);

        void zza(zzp zzp);

        void zza(zzp zzp, Set<Scope> set);

        boolean zzlN();

        IBinder zznz();
    }

    public static final class zzc<C extends zzb> {
    }

    public interface zzd<T extends IInterface> {
        T zzW(IBinder iBinder);

        void zza(int i, T t);

        String zzfK();

        String zzfL();
    }

    public interface zze<T extends zzd, O> {
        T zzn(O o);

        int zznA();
    }

    public static final class zzf<C extends zzd> {
    }

    public <C extends zzb> Api(String str, zza<C, O> zza2, zzc<C> zzc2) {
        zzx.zzb(zza2, (Object) "Cannot construct an Api with a null ClientBuilder");
        zzx.zzb(zzc2, (Object) "Cannot construct an Api with a null ClientKey");
        this.mName = str;
        this.zzaav = zza2;
        this.zzZM = zzc2;
        this.zzaax = null;
    }

    public String getName() {
        return this.mName;
    }

    public zza<?, O> zznv() {
        zzx.zza(this.zzaav != null, (Object) "This API was constructed with a SimpleClientBuilder. Use getSimpleClientBuilder");
        return this.zzaav;
    }

    public zze<?, O> zznw() {
        zzx.zza(this.zzaaw != null, (Object) "This API was constructed with a ClientBuilder. Use getClientBuilder");
        return this.zzaaw;
    }

    public zzc<?> zznx() {
        zzx.zza(this.zzZM != null, (Object) "This API was constructed with a SimpleClientKey. Use getSimpleClientKey");
        return this.zzZM;
    }

    public boolean zzny() {
        return this.zzaax != null;
    }
}
