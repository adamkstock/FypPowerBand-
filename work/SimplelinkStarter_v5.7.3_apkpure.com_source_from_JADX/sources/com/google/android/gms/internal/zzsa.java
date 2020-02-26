package com.google.android.gms.internal;

public final class zzsa implements Cloneable {
    private static final zzsb zzbin = new zzsb();
    private int mSize;
    private boolean zzbio;
    private int[] zzbip;
    private zzsb[] zzbiq;

    zzsa() {
        this(10);
    }

    zzsa(int i) {
        this.zzbio = false;
        int idealIntArraySize = idealIntArraySize(i);
        this.zzbip = new int[idealIntArraySize];
        this.zzbiq = new zzsb[idealIntArraySize];
        this.mSize = 0;
    }

    /* renamed from: gc */
    private void m8gc() {
        int i = this.mSize;
        int[] iArr = this.zzbip;
        zzsb[] zzsbArr = this.zzbiq;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            zzsb zzsb = zzsbArr[i3];
            if (zzsb != zzbin) {
                if (i3 != i2) {
                    iArr[i2] = iArr[i3];
                    zzsbArr[i2] = zzsb;
                    zzsbArr[i3] = null;
                }
                i2++;
            }
        }
        this.zzbio = false;
        this.mSize = i2;
    }

    private int idealByteArraySize(int i) {
        for (int i2 = 4; i2 < 32; i2++) {
            int i3 = (1 << i2) - 12;
            if (i <= i3) {
                return i3;
            }
        }
        return i;
    }

    private int idealIntArraySize(int i) {
        return idealByteArraySize(i * 4) / 4;
    }

    private boolean zza(int[] iArr, int[] iArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private boolean zza(zzsb[] zzsbArr, zzsb[] zzsbArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (!zzsbArr[i2].equals(zzsbArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    private int zzlT(int i) {
        int i2 = this.mSize - 1;
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) >>> 1;
            int i5 = this.zzbip[i4];
            if (i5 < i) {
                i3 = i4 + 1;
            } else if (i5 <= i) {
                return i4;
            } else {
                i2 = i4 - 1;
            }
        }
        return ~i3;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzsa)) {
            return false;
        }
        zzsa zzsa = (zzsa) obj;
        if (size() != zzsa.size()) {
            return false;
        }
        if (!zza(this.zzbip, zzsa.zzbip, this.mSize) || !zza(this.zzbiq, zzsa.zzbiq, this.mSize)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (this.zzbio) {
            m8gc();
        }
        int i = 17;
        for (int i2 = 0; i2 < this.mSize; i2++) {
            i = (((i * 31) + this.zzbip[i2]) * 31) + this.zzbiq[i2].hashCode();
        }
        return i;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /* access modifiers changed from: 0000 */
    public int size() {
        if (this.zzbio) {
            m8gc();
        }
        return this.mSize;
    }

    /* renamed from: zzFH */
    public final zzsa clone() {
        int size = size();
        zzsa zzsa = new zzsa(size);
        System.arraycopy(this.zzbip, 0, zzsa.zzbip, 0, size);
        for (int i = 0; i < size; i++) {
            zzsb[] zzsbArr = this.zzbiq;
            if (zzsbArr[i] != null) {
                zzsa.zzbiq[i] = zzsbArr[i].clone();
            }
        }
        zzsa.mSize = size;
        return zzsa;
    }

    /* access modifiers changed from: 0000 */
    public void zza(int i, zzsb zzsb) {
        int zzlT = zzlT(i);
        if (zzlT >= 0) {
            this.zzbiq[zzlT] = zzsb;
        } else {
            int i2 = ~zzlT;
            if (i2 < this.mSize) {
                zzsb[] zzsbArr = this.zzbiq;
                if (zzsbArr[i2] == zzbin) {
                    this.zzbip[i2] = i;
                    zzsbArr[i2] = zzsb;
                    return;
                }
            }
            if (this.zzbio && this.mSize >= this.zzbip.length) {
                m8gc();
                i2 = ~zzlT(i);
            }
            int i3 = this.mSize;
            if (i3 >= this.zzbip.length) {
                int idealIntArraySize = idealIntArraySize(i3 + 1);
                int[] iArr = new int[idealIntArraySize];
                zzsb[] zzsbArr2 = new zzsb[idealIntArraySize];
                int[] iArr2 = this.zzbip;
                System.arraycopy(iArr2, 0, iArr, 0, iArr2.length);
                zzsb[] zzsbArr3 = this.zzbiq;
                System.arraycopy(zzsbArr3, 0, zzsbArr2, 0, zzsbArr3.length);
                this.zzbip = iArr;
                this.zzbiq = zzsbArr2;
            }
            int i4 = this.mSize;
            if (i4 - i2 != 0) {
                int[] iArr3 = this.zzbip;
                int i5 = i2 + 1;
                System.arraycopy(iArr3, i2, iArr3, i5, i4 - i2);
                zzsb[] zzsbArr4 = this.zzbiq;
                System.arraycopy(zzsbArr4, i2, zzsbArr4, i5, this.mSize - i2);
            }
            this.zzbip[i2] = i;
            this.zzbiq[i2] = zzsb;
            this.mSize++;
        }
    }

    /* access modifiers changed from: 0000 */
    public zzsb zzlR(int i) {
        int zzlT = zzlT(i);
        if (zzlT >= 0) {
            zzsb[] zzsbArr = this.zzbiq;
            if (zzsbArr[zzlT] != zzbin) {
                return zzsbArr[zzlT];
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public zzsb zzlS(int i) {
        if (this.zzbio) {
            m8gc();
        }
        return this.zzbiq[i];
    }
}
