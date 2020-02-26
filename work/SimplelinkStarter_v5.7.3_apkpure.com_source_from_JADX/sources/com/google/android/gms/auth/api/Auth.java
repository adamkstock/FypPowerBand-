package com.google.android.gms.auth.api;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.PasswordSpecification;
import com.google.android.gms.auth.api.credentials.internal.zzc;
import com.google.android.gms.auth.api.credentials.internal.zze;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.auth.api.signin.internal.zzb;
import com.google.android.gms.auth.api.signin.internal.zzg;
import com.google.android.gms.auth.api.signin.zzd;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.internal.zzjz;
import com.google.android.gms.internal.zzka;
import com.google.android.gms.internal.zzkb;
import com.google.android.gms.internal.zzke;
import com.google.android.gms.internal.zzkf;
import com.google.android.gms.internal.zzki;
import com.google.android.gms.internal.zzkm;

public final class Auth {
    public static final Api<AuthCredentialsOptions> CREDENTIALS_API = new Api<>("Auth.CREDENTIALS_API", zzRL, zzRF);
    public static final CredentialsApi CredentialsApi = new zzc();
    public static final Api<zza> PROXY_API = new Api<>("Auth.PROXY_API", zzRK, zzRE);
    public static final ProxyApi ProxyApi = new zzkm();
    public static final Api.zzc<zzki> zzRE = new Api.zzc<>();
    public static final Api.zzc<zze> zzRF = new Api.zzc<>();
    public static final Api.zzc<zzkb> zzRG = new Api.zzc<>();
    public static final Api.zzc<zzg> zzRH = new Api.zzc<>();
    public static final Api.zzc<zzb> zzRI = new Api.zzc<>();
    public static final Api.zzc<zzkf> zzRJ = new Api.zzc<>();
    private static final com.google.android.gms.common.api.Api.zza<zzki, zza> zzRK = new com.google.android.gms.common.api.Api.zza<zzki, zza>() {
        public zzki zza(Context context, Looper looper, zzf zzf, zza zza, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            zzki zzki = new zzki(context, looper, zzf, zza, connectionCallbacks, onConnectionFailedListener);
            return zzki;
        }
    };
    private static final com.google.android.gms.common.api.Api.zza<zze, AuthCredentialsOptions> zzRL = new com.google.android.gms.common.api.Api.zza<zze, AuthCredentialsOptions>() {
        public zze zza(Context context, Looper looper, zzf zzf, AuthCredentialsOptions authCredentialsOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            zze zze = new zze(context, looper, zzf, authCredentialsOptions, connectionCallbacks, onConnectionFailedListener);
            return zze;
        }
    };
    private static final com.google.android.gms.common.api.Api.zza<zzkb, NoOptions> zzRM = new com.google.android.gms.common.api.Api.zza<zzkb, NoOptions>() {
        /* renamed from: zzc */
        public zzkb zza(Context context, Looper looper, zzf zzf, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            zzkb zzkb = new zzkb(context, looper, zzf, connectionCallbacks, onConnectionFailedListener);
            return zzkb;
        }
    };
    private static final com.google.android.gms.common.api.Api.zza<zzkf, NoOptions> zzRN = new com.google.android.gms.common.api.Api.zza<zzkf, NoOptions>() {
        /* renamed from: zzd */
        public zzkf zza(Context context, Looper looper, zzf zzf, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            zzkf zzkf = new zzkf(context, looper, zzf, connectionCallbacks, onConnectionFailedListener);
            return zzkf;
        }
    };
    private static final com.google.android.gms.common.api.Api.zza<zzg, com.google.android.gms.auth.api.signin.zzg> zzRO = new com.google.android.gms.common.api.Api.zza<zzg, com.google.android.gms.auth.api.signin.zzg>() {
        public zzg zza(Context context, Looper looper, zzf zzf, com.google.android.gms.auth.api.signin.zzg zzg, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            zzg zzg2 = new zzg(context, looper, zzf, zzg, connectionCallbacks, onConnectionFailedListener);
            return zzg2;
        }
    };
    private static final com.google.android.gms.common.api.Api.zza<zzb, GoogleSignInConfig> zzRP = new com.google.android.gms.common.api.Api.zza<zzb, GoogleSignInConfig>() {
        public zzb zza(Context context, Looper looper, zzf zzf, GoogleSignInConfig googleSignInConfig, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            zzb zzb = new zzb(context, looper, zzf, googleSignInConfig, connectionCallbacks, onConnectionFailedListener);
            return zzb;
        }
    };
    public static final Api<com.google.android.gms.auth.api.signin.zzg> zzRQ = new Api<>("Auth.SIGN_IN_API", zzRO, zzRH);
    public static final Api<GoogleSignInConfig> zzRR = new Api<>("Auth.GOOGLE_SIGN_IN_API", zzRP, zzRI);
    public static final Api<NoOptions> zzRS = new Api<>("Auth.ACCOUNT_STATUS_API", zzRM, zzRG);
    public static final Api<NoOptions> zzRT = new Api<>("Auth.CONSENT_API", zzRN, zzRJ);
    public static final zzjz zzRU = new zzka();
    public static final com.google.android.gms.auth.api.signin.zzf zzRV = new com.google.android.gms.auth.api.signin.internal.zzf();
    public static final zzd zzRW = new com.google.android.gms.auth.api.signin.internal.zza();
    public static final com.google.android.gms.auth.api.consent.zza zzRX = new zzke();

    public static final class AuthCredentialsOptions implements Optional {
        private final String zzRY;
        private final PasswordSpecification zzRZ;

        public static class Builder {
            private PasswordSpecification zzRZ = PasswordSpecification.zzSt;
        }

        public Bundle zzly() {
            Bundle bundle = new Bundle();
            bundle.putString("consumer_package", this.zzRY);
            bundle.putParcelable("password_specification", this.zzRZ);
            return bundle;
        }
    }

    public static final class zza implements Optional {
        private final Bundle zzSa;

        public Bundle zzlE() {
            return new Bundle(this.zzSa);
        }
    }

    private Auth() {
    }
}
