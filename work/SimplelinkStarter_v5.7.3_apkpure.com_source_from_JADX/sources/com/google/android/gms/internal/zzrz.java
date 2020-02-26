package com.google.android.gms.internal;

import com.google.android.gms.internal.zzry;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class zzrz<M extends zzry<M>, T> {
    public final int tag;
    protected final int type;
    protected final Class<T> zzbil;
    protected final boolean zzbim;

    private zzrz(int i, Class<T> cls, int i2, boolean z) {
        this.type = i;
        this.zzbil = cls;
        this.tag = i2;
        this.zzbim = z;
    }

    private T zzF(List<zzsg> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            zzsg zzsg = (zzsg) list.get(i);
            if (zzsg.zzbiw.length != 0) {
                zza(zzsg, (List<Object>) arrayList);
            }
        }
        int size = arrayList.size();
        if (size == 0) {
            return null;
        }
        Class<T> cls = this.zzbil;
        T cast = cls.cast(Array.newInstance(cls.getComponentType(), size));
        for (int i2 = 0; i2 < size; i2++) {
            Array.set(cast, i2, arrayList.get(i2));
        }
        return cast;
    }

    private T zzG(List<zzsg> list) {
        if (list.isEmpty()) {
            return null;
        }
        return this.zzbil.cast(zzF(zzrw.zzB(((zzsg) list.get(list.size() - 1)).zzbiw)));
    }

    public static <M extends zzry<M>, T extends zzse> zzrz<M, T> zza(int i, Class<T> cls, long j) {
        return new zzrz<>(i, cls, (int) j, false);
    }

    /* access modifiers changed from: 0000 */
    public final T zzE(List<zzsg> list) {
        if (list == null) {
            return null;
        }
        return this.zzbim ? zzF(list) : zzG(list);
    }

    /* access modifiers changed from: protected */
    public Object zzF(zzrw zzrw) {
        String str = "Error creating instance of class ";
        Class<T> componentType = this.zzbim ? this.zzbil.getComponentType() : this.zzbil;
        try {
            int i = this.type;
            if (i == 10) {
                zzse zzse = (zzse) componentType.newInstance();
                zzrw.zza(zzse, zzsh.zzlV(this.tag));
                return zzse;
            } else if (i == 11) {
                zzse zzse2 = (zzse) componentType.newInstance();
                zzrw.zza(zzse2);
                return zzse2;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown type ");
                sb.append(this.type);
                throw new IllegalArgumentException(sb.toString());
            }
        } catch (InstantiationException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(componentType);
            throw new IllegalArgumentException(sb2.toString(), e);
        } catch (IllegalAccessException e2) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(componentType);
            throw new IllegalArgumentException(sb3.toString(), e2);
        } catch (IOException e3) {
            throw new IllegalArgumentException("Error reading extension field", e3);
        }
    }

    /* access modifiers changed from: 0000 */
    public int zzX(Object obj) {
        return this.zzbim ? zzY(obj) : zzZ(obj);
    }

    /* access modifiers changed from: protected */
    public int zzY(Object obj) {
        int length = Array.getLength(obj);
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (Array.get(obj, i2) != null) {
                i += zzZ(Array.get(obj, i2));
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public int zzZ(Object obj) {
        int zzlV = zzsh.zzlV(this.tag);
        int i = this.type;
        if (i == 10) {
            return zzrx.zzb(zzlV, (zzse) obj);
        }
        if (i == 11) {
            return zzrx.zzc(zzlV, (zzse) obj);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown type ");
        sb.append(this.type);
        throw new IllegalArgumentException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public void zza(zzsg zzsg, List<Object> list) {
        list.add(zzF(zzrw.zzB(zzsg.zzbiw)));
    }

    /* access modifiers changed from: 0000 */
    public void zza(Object obj, zzrx zzrx) throws IOException {
        if (this.zzbim) {
            zzc(obj, zzrx);
        } else {
            zzb(obj, zzrx);
        }
    }

    /* access modifiers changed from: protected */
    public void zzb(Object obj, zzrx zzrx) {
        try {
            zzrx.zzlN(this.tag);
            int i = this.type;
            if (i == 10) {
                zzse zzse = (zzse) obj;
                int zzlV = zzsh.zzlV(this.tag);
                zzrx.zzb(zzse);
                zzrx.zzC(zzlV, 4);
            } else if (i == 11) {
                zzrx.zzc((zzse) obj);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown type ");
                sb.append(this.type);
                throw new IllegalArgumentException(sb.toString());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /* access modifiers changed from: protected */
    public void zzc(Object obj, zzrx zzrx) {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null) {
                zzb(obj2, zzrx);
            }
        }
    }
}
