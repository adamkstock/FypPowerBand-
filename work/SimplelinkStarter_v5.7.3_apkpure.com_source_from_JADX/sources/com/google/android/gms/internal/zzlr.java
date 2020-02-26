package com.google.android.gms.internal;

import android.os.Binder;

public abstract class zzlr<T> {
    /* access modifiers changed from: private */
    public static zza zzadc = null;
    private static int zzadd = 0;
    private static String zzade = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    private static final Object zzpy = new Object();
    private T zzOX = null;
    protected final String zzue;
    protected final T zzuf;

    private interface zza {
        Long getLong(String str, Long l);

        String getString(String str, String str2);

        Boolean zzb(String str, Boolean bool);

        Float zzb(String str, Float f);

        Integer zzb(String str, Integer num);
    }

    protected zzlr(String str, T t) {
        this.zzue = str;
        this.zzuf = t;
    }

    public static boolean isInitialized() {
        return zzadc != null;
    }

    public static zzlr<Float> zza(String str, Float f) {
        return new zzlr<Float>(str, f) {
            /* access modifiers changed from: protected */
            /* renamed from: zzcc */
            public Float zzbY(String str) {
                return zzlr.zzadc.zzb(this.zzue, (Float) this.zzuf);
            }
        };
    }

    public static zzlr<Integer> zza(String str, Integer num) {
        return new zzlr<Integer>(str, num) {
            /* access modifiers changed from: protected */
            /* renamed from: zzcb */
            public Integer zzbY(String str) {
                return zzlr.zzadc.zzb(this.zzue, (Integer) this.zzuf);
            }
        };
    }

    public static zzlr<Long> zza(String str, Long l) {
        return new zzlr<Long>(str, l) {
            /* access modifiers changed from: protected */
            /* renamed from: zzca */
            public Long zzbY(String str) {
                return zzlr.zzadc.getLong(this.zzue, (Long) this.zzuf);
            }
        };
    }

    public static zzlr<Boolean> zzg(String str, boolean z) {
        return new zzlr<Boolean>(str, Boolean.valueOf(z)) {
            /* access modifiers changed from: protected */
            /* renamed from: zzbZ */
            public Boolean zzbY(String str) {
                return zzlr.zzadc.zzb(this.zzue, (Boolean) this.zzuf);
            }
        };
    }

    public static int zzoo() {
        return zzadd;
    }

    public static zzlr<String> zzu(String str, String str2) {
        return new zzlr<String>(str, str2) {
            /* access modifiers changed from: protected */
            /* renamed from: zzcd */
            public String zzbY(String str) {
                return zzlr.zzadc.getString(this.zzue, (String) this.zzuf);
            }
        };
    }

    public final T get() {
        T t = this.zzOX;
        return t != null ? t : zzbY(this.zzue);
    }

    /* access modifiers changed from: protected */
    public abstract T zzbY(String str);

    public final T zzop() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return get();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
