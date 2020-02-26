package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class zzsb implements Cloneable {
    private zzrz<?, ?> zzbir;
    private Object zzbis;
    private List<zzsg> zzbit = new ArrayList();

    zzsb() {
    }

    private byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zzB()];
        zza(zzrx.zzC(bArr));
        return bArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzsb)) {
            return false;
        }
        zzsb zzsb = (zzsb) obj;
        if (this.zzbis == null || zzsb.zzbis == null) {
            List<zzsg> list = this.zzbit;
            if (list != null) {
                List<zzsg> list2 = zzsb.zzbit;
                if (list2 != null) {
                    return list.equals(list2);
                }
            }
            try {
                return Arrays.equals(toByteArray(), zzsb.toByteArray());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            zzrz<?, ?> zzrz = this.zzbir;
            if (zzrz != zzsb.zzbir) {
                return false;
            }
            if (!zzrz.zzbil.isArray()) {
                return this.zzbis.equals(zzsb.zzbis);
            }
            Object obj2 = this.zzbis;
            return obj2 instanceof byte[] ? Arrays.equals((byte[]) obj2, (byte[]) zzsb.zzbis) : obj2 instanceof int[] ? Arrays.equals((int[]) obj2, (int[]) zzsb.zzbis) : obj2 instanceof long[] ? Arrays.equals((long[]) obj2, (long[]) zzsb.zzbis) : obj2 instanceof float[] ? Arrays.equals((float[]) obj2, (float[]) zzsb.zzbis) : obj2 instanceof double[] ? Arrays.equals((double[]) obj2, (double[]) zzsb.zzbis) : obj2 instanceof boolean[] ? Arrays.equals((boolean[]) obj2, (boolean[]) zzsb.zzbis) : Arrays.deepEquals((Object[]) obj2, (Object[]) zzsb.zzbis);
        }
    }

    public int hashCode() {
        try {
            return 527 + Arrays.hashCode(toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /* access modifiers changed from: 0000 */
    public int zzB() {
        Object obj = this.zzbis;
        if (obj != null) {
            return this.zzbir.zzX(obj);
        }
        int i = 0;
        for (zzsg zzB : this.zzbit) {
            i += zzB.zzB();
        }
        return i;
    }

    /* renamed from: zzFI */
    public final zzsb clone() {
        Object clone;
        zzsb zzsb = new zzsb();
        try {
            zzsb.zzbir = this.zzbir;
            if (this.zzbit == null) {
                zzsb.zzbit = null;
            } else {
                zzsb.zzbit.addAll(this.zzbit);
            }
            if (this.zzbis != null) {
                if (this.zzbis instanceof zzse) {
                    clone = ((zzse) this.zzbis).clone();
                } else if (this.zzbis instanceof byte[]) {
                    clone = ((byte[]) this.zzbis).clone();
                } else {
                    int i = 0;
                    if (this.zzbis instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) this.zzbis;
                        byte[][] bArr2 = new byte[bArr.length][];
                        zzsb.zzbis = bArr2;
                        while (i < bArr.length) {
                            bArr2[i] = (byte[]) bArr[i].clone();
                            i++;
                        }
                    } else if (this.zzbis instanceof boolean[]) {
                        clone = ((boolean[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof int[]) {
                        clone = ((int[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof long[]) {
                        clone = ((long[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof float[]) {
                        clone = ((float[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof double[]) {
                        clone = ((double[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof zzse[]) {
                        zzse[] zzseArr = (zzse[]) this.zzbis;
                        zzse[] zzseArr2 = new zzse[zzseArr.length];
                        zzsb.zzbis = zzseArr2;
                        while (i < zzseArr.length) {
                            zzseArr2[i] = zzseArr[i].clone();
                            i++;
                        }
                    }
                }
                zzsb.zzbis = clone;
            }
            return zzsb;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void zza(zzrx zzrx) throws IOException {
        Object obj = this.zzbis;
        if (obj != null) {
            this.zzbir.zza(obj, zzrx);
            return;
        }
        for (zzsg zza : this.zzbit) {
            zza.zza(zzrx);
        }
    }

    /* access modifiers changed from: 0000 */
    public void zza(zzsg zzsg) {
        this.zzbit.add(zzsg);
    }

    /* access modifiers changed from: 0000 */
    public <T> T zzb(zzrz<?, T> zzrz) {
        if (this.zzbis == null) {
            this.zzbir = zzrz;
            this.zzbis = zzrz.zzE(this.zzbit);
            this.zzbit = null;
        } else if (this.zzbir != zzrz) {
            throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
        }
        return this.zzbis;
    }
}
