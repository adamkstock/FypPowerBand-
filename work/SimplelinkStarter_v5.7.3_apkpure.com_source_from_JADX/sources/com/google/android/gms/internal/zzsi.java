package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;
import javax.jmdns.impl.constants.DNSConstants;

public interface zzsi {

    public static final class zza extends zzry<zza> {
        public String[] zzbiF;
        public String[] zzbiG;
        public int[] zzbiH;
        public long[] zzbiI;

        public zza() {
            zzFS();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza zza = (zza) obj;
            if (!zzsc.equals((Object[]) this.zzbiF, (Object[]) zza.zzbiF) || !zzsc.equals((Object[]) this.zzbiG, (Object[]) zza.zzbiG) || !zzsc.equals(this.zzbiH, zza.zzbiH) || !zzsc.equals(this.zzbiI, zza.zzbiI)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zza.zzbik);
            }
            if (zza.zzbik != null && !zza.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return ((((((((((527 + getClass().getName().hashCode()) * 31) + zzsc.hashCode((Object[]) this.zzbiF)) * 31) + zzsc.hashCode((Object[]) this.zzbiG)) * 31) + zzsc.hashCode(this.zzbiH)) * 31) + zzsc.hashCode(this.zzbiI)) * 31) + ((this.zzbik == null || this.zzbik.isEmpty()) ? 0 : this.zzbik.hashCode());
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int[] iArr;
            int zzB = super.zzB();
            String[] strArr = this.zzbiF;
            int i = 0;
            if (strArr != null && strArr.length > 0) {
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    String[] strArr2 = this.zzbiF;
                    if (i2 >= strArr2.length) {
                        break;
                    }
                    String str = strArr2[i2];
                    if (str != null) {
                        i4++;
                        i3 += zzrx.zzfA(str);
                    }
                    i2++;
                }
                zzB = zzB + i3 + (i4 * 1);
            }
            String[] strArr3 = this.zzbiG;
            if (strArr3 != null && strArr3.length > 0) {
                int i5 = 0;
                int i6 = 0;
                int i7 = 0;
                while (true) {
                    String[] strArr4 = this.zzbiG;
                    if (i5 >= strArr4.length) {
                        break;
                    }
                    String str2 = strArr4[i5];
                    if (str2 != null) {
                        i7++;
                        i6 += zzrx.zzfA(str2);
                    }
                    i5++;
                }
                zzB = zzB + i6 + (i7 * 1);
            }
            int[] iArr2 = this.zzbiH;
            if (iArr2 != null && iArr2.length > 0) {
                int i8 = 0;
                int i9 = 0;
                while (true) {
                    iArr = this.zzbiH;
                    if (i8 >= iArr.length) {
                        break;
                    }
                    i9 += zzrx.zzlJ(iArr[i8]);
                    i8++;
                }
                zzB = zzB + i9 + (iArr.length * 1);
            }
            long[] jArr = this.zzbiI;
            if (jArr == null || jArr.length <= 0) {
                return zzB;
            }
            int i10 = 0;
            while (true) {
                long[] jArr2 = this.zzbiI;
                if (i >= jArr2.length) {
                    return zzB + i10 + (jArr2.length * 1);
                }
                i10 += zzrx.zzaa(jArr2[i]);
                i++;
            }
        }

        public zza zzFS() {
            this.zzbiF = zzsh.zzbiC;
            this.zzbiG = zzsh.zzbiC;
            this.zzbiH = zzsh.zzbix;
            this.zzbiI = zzsh.zzbiy;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        /* renamed from: zzG */
        public zza zzb(zzrw zzrw) throws IOException {
            int i;
            while (true) {
                int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo == 10) {
                    int zzc = zzsh.zzc(zzrw, 10);
                    String[] strArr = this.zzbiF;
                    int length = strArr == null ? 0 : strArr.length;
                    String[] strArr2 = new String[(zzc + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzbiF, 0, strArr2, 0, length);
                    }
                    while (length < strArr2.length - 1) {
                        strArr2[length] = zzrw.readString();
                        zzrw.zzFo();
                        length++;
                    }
                    strArr2[length] = zzrw.readString();
                    this.zzbiF = strArr2;
                } else if (zzFo == 18) {
                    int zzc2 = zzsh.zzc(zzrw, 18);
                    String[] strArr3 = this.zzbiG;
                    int length2 = strArr3 == null ? 0 : strArr3.length;
                    String[] strArr4 = new String[(zzc2 + length2)];
                    if (length2 != 0) {
                        System.arraycopy(this.zzbiG, 0, strArr4, 0, length2);
                    }
                    while (length2 < strArr4.length - 1) {
                        strArr4[length2] = zzrw.readString();
                        zzrw.zzFo();
                        length2++;
                    }
                    strArr4[length2] = zzrw.readString();
                    this.zzbiG = strArr4;
                } else if (zzFo != 24) {
                    if (zzFo == 26) {
                        i = zzrw.zzlC(zzrw.zzFv());
                        int position = zzrw.getPosition();
                        int i2 = 0;
                        while (zzrw.zzFA() > 0) {
                            zzrw.zzFr();
                            i2++;
                        }
                        zzrw.zzlE(position);
                        int[] iArr = this.zzbiH;
                        int length3 = iArr == null ? 0 : iArr.length;
                        int[] iArr2 = new int[(i2 + length3)];
                        if (length3 != 0) {
                            System.arraycopy(this.zzbiH, 0, iArr2, 0, length3);
                        }
                        while (length3 < iArr2.length) {
                            iArr2[length3] = zzrw.zzFr();
                            length3++;
                        }
                        this.zzbiH = iArr2;
                    } else if (zzFo == 32) {
                        int zzc3 = zzsh.zzc(zzrw, 32);
                        long[] jArr = this.zzbiI;
                        int length4 = jArr == null ? 0 : jArr.length;
                        long[] jArr2 = new long[(zzc3 + length4)];
                        if (length4 != 0) {
                            System.arraycopy(this.zzbiI, 0, jArr2, 0, length4);
                        }
                        while (length4 < jArr2.length - 1) {
                            jArr2[length4] = zzrw.zzFq();
                            zzrw.zzFo();
                            length4++;
                        }
                        jArr2[length4] = zzrw.zzFq();
                        this.zzbiI = jArr2;
                    } else if (zzFo == 34) {
                        i = zzrw.zzlC(zzrw.zzFv());
                        int position2 = zzrw.getPosition();
                        int i3 = 0;
                        while (zzrw.zzFA() > 0) {
                            zzrw.zzFq();
                            i3++;
                        }
                        zzrw.zzlE(position2);
                        long[] jArr3 = this.zzbiI;
                        int length5 = jArr3 == null ? 0 : jArr3.length;
                        long[] jArr4 = new long[(i3 + length5)];
                        if (length5 != 0) {
                            System.arraycopy(this.zzbiI, 0, jArr4, 0, length5);
                        }
                        while (length5 < jArr4.length) {
                            jArr4[length5] = zzrw.zzFq();
                            length5++;
                        }
                        this.zzbiI = jArr4;
                    } else if (!zza(zzrw, zzFo)) {
                        return this;
                    }
                    zzrw.zzlD(i);
                } else {
                    int zzc4 = zzsh.zzc(zzrw, 24);
                    int[] iArr3 = this.zzbiH;
                    int length6 = iArr3 == null ? 0 : iArr3.length;
                    int[] iArr4 = new int[(zzc4 + length6)];
                    if (length6 != 0) {
                        System.arraycopy(this.zzbiH, 0, iArr4, 0, length6);
                    }
                    while (length6 < iArr4.length - 1) {
                        iArr4[length6] = zzrw.zzFr();
                        zzrw.zzFo();
                        length6++;
                    }
                    iArr4[length6] = zzrw.zzFr();
                    this.zzbiH = iArr4;
                }
            }
        }

        public void zza(zzrx zzrx) throws IOException {
            String[] strArr = this.zzbiF;
            int i = 0;
            if (strArr != null && strArr.length > 0) {
                int i2 = 0;
                while (true) {
                    String[] strArr2 = this.zzbiF;
                    if (i2 >= strArr2.length) {
                        break;
                    }
                    String str = strArr2[i2];
                    if (str != null) {
                        zzrx.zzb(1, str);
                    }
                    i2++;
                }
            }
            String[] strArr3 = this.zzbiG;
            if (strArr3 != null && strArr3.length > 0) {
                int i3 = 0;
                while (true) {
                    String[] strArr4 = this.zzbiG;
                    if (i3 >= strArr4.length) {
                        break;
                    }
                    String str2 = strArr4[i3];
                    if (str2 != null) {
                        zzrx.zzb(2, str2);
                    }
                    i3++;
                }
            }
            int[] iArr = this.zzbiH;
            if (iArr != null && iArr.length > 0) {
                int i4 = 0;
                while (true) {
                    int[] iArr2 = this.zzbiH;
                    if (i4 >= iArr2.length) {
                        break;
                    }
                    zzrx.zzy(3, iArr2[i4]);
                    i4++;
                }
            }
            long[] jArr = this.zzbiI;
            if (jArr != null && jArr.length > 0) {
                while (true) {
                    long[] jArr2 = this.zzbiI;
                    if (i >= jArr2.length) {
                        break;
                    }
                    zzrx.zzb(4, jArr2[i]);
                    i++;
                }
            }
            super.zza(zzrx);
        }
    }

    public static final class zzb extends zzry<zzb> {
        public String version;
        public int zzbiJ;
        public String zzbiK;

        public zzb() {
            zzFT();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzb)) {
                return false;
            }
            zzb zzb = (zzb) obj;
            if (this.zzbiJ != zzb.zzbiJ) {
                return false;
            }
            String str = this.zzbiK;
            if (str == null) {
                if (zzb.zzbiK != null) {
                    return false;
                }
            } else if (!str.equals(zzb.zzbiK)) {
                return false;
            }
            String str2 = this.version;
            if (str2 == null) {
                if (zzb.version != null) {
                    return false;
                }
            } else if (!str2.equals(zzb.version)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzb.zzbik);
            }
            if (zzb.zzbik != null && !zzb.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int hashCode = (((527 + getClass().getName().hashCode()) * 31) + this.zzbiJ) * 31;
            String str = this.zzbiK;
            int i = 0;
            int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            String str2 = this.version;
            int hashCode3 = (hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31;
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                i = this.zzbik.hashCode();
            }
            return hashCode3 + i;
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int zzB = super.zzB();
            int i = this.zzbiJ;
            if (i != 0) {
                zzB += zzrx.zzA(1, i);
            }
            String str = "";
            if (!this.zzbiK.equals(str)) {
                zzB += zzrx.zzn(2, this.zzbiK);
            }
            return !this.version.equals(str) ? zzB + zzrx.zzn(3, this.version) : zzB;
        }

        public zzb zzFT() {
            this.zzbiJ = 0;
            String str = "";
            this.zzbiK = str;
            this.version = str;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        /* renamed from: zzH */
        public zzb zzb(zzrw zzrw) throws IOException {
            while (true) {
                int zzFo = zzrw.zzFo();
                if (zzFo != 0) {
                    if (zzFo == 8) {
                        int zzFr = zzrw.zzFr();
                        switch (zzFr) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                            case 26:
                                this.zzbiJ = zzFr;
                                break;
                        }
                    } else if (zzFo == 18) {
                        this.zzbiK = zzrw.readString();
                    } else if (zzFo == 26) {
                        this.version = zzrw.readString();
                    } else if (!zza(zzrw, zzFo)) {
                        return this;
                    }
                } else {
                    return this;
                }
            }
        }

        public void zza(zzrx zzrx) throws IOException {
            int i = this.zzbiJ;
            if (i != 0) {
                zzrx.zzy(1, i);
            }
            String str = "";
            if (!this.zzbiK.equals(str)) {
                zzrx.zzb(2, this.zzbiK);
            }
            if (!this.version.equals(str)) {
                zzrx.zzb(3, this.version);
            }
            super.zza(zzrx);
        }
    }

    public static final class zzc extends zzry<zzc> {
        public byte[] zzbiL;
        public byte[][] zzbiM;
        public boolean zzbiN;

        public zzc() {
            zzFU();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzc)) {
                return false;
            }
            zzc zzc = (zzc) obj;
            if (!Arrays.equals(this.zzbiL, zzc.zzbiL) || !zzsc.zza(this.zzbiM, zzc.zzbiM) || this.zzbiN != zzc.zzbiN) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzc.zzbik);
            }
            if (zzc.zzbik != null && !zzc.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return ((((((((527 + getClass().getName().hashCode()) * 31) + Arrays.hashCode(this.zzbiL)) * 31) + zzsc.zza(this.zzbiM)) * 31) + (this.zzbiN ? 1231 : 1237)) * 31) + ((this.zzbik == null || this.zzbik.isEmpty()) ? 0 : this.zzbik.hashCode());
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int zzB = super.zzB();
            if (!Arrays.equals(this.zzbiL, zzsh.zzbiE)) {
                zzB += zzrx.zzb(1, this.zzbiL);
            }
            byte[][] bArr = this.zzbiM;
            if (bArr != null && bArr.length > 0) {
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    byte[][] bArr2 = this.zzbiM;
                    if (i >= bArr2.length) {
                        break;
                    }
                    byte[] bArr3 = bArr2[i];
                    if (bArr3 != null) {
                        i3++;
                        i2 += zzrx.zzE(bArr3);
                    }
                    i++;
                }
                zzB = zzB + i2 + (i3 * 1);
            }
            boolean z = this.zzbiN;
            return z ? zzB + zzrx.zzc(3, z) : zzB;
        }

        public zzc zzFU() {
            this.zzbiL = zzsh.zzbiE;
            this.zzbiM = zzsh.zzbiD;
            this.zzbiN = false;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        /* renamed from: zzI */
        public zzc zzb(zzrw zzrw) throws IOException {
            while (true) {
                int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo == 10) {
                    this.zzbiL = zzrw.readBytes();
                } else if (zzFo == 18) {
                    int zzc = zzsh.zzc(zzrw, 18);
                    byte[][] bArr = this.zzbiM;
                    int length = bArr == null ? 0 : bArr.length;
                    byte[][] bArr2 = new byte[(zzc + length)][];
                    if (length != 0) {
                        System.arraycopy(this.zzbiM, 0, bArr2, 0, length);
                    }
                    while (length < bArr2.length - 1) {
                        bArr2[length] = zzrw.readBytes();
                        zzrw.zzFo();
                        length++;
                    }
                    bArr2[length] = zzrw.readBytes();
                    this.zzbiM = bArr2;
                } else if (zzFo == 24) {
                    this.zzbiN = zzrw.zzFs();
                } else if (!zza(zzrw, zzFo)) {
                    return this;
                }
            }
        }

        public void zza(zzrx zzrx) throws IOException {
            if (!Arrays.equals(this.zzbiL, zzsh.zzbiE)) {
                zzrx.zza(1, this.zzbiL);
            }
            byte[][] bArr = this.zzbiM;
            if (bArr != null && bArr.length > 0) {
                int i = 0;
                while (true) {
                    byte[][] bArr2 = this.zzbiM;
                    if (i >= bArr2.length) {
                        break;
                    }
                    byte[] bArr3 = bArr2[i];
                    if (bArr3 != null) {
                        zzrx.zza(2, bArr3);
                    }
                    i++;
                }
            }
            boolean z = this.zzbiN;
            if (z) {
                zzrx.zzb(3, z);
            }
            super.zza(zzrx);
        }
    }

    public static final class zzd extends zzry<zzd> {
        public String tag;
        public long zzbiO;
        public long zzbiP;
        public int zzbiQ;
        public int zzbiR;
        public boolean zzbiS;
        public zze[] zzbiT;
        public zzb zzbiU;
        public byte[] zzbiV;
        public byte[] zzbiW;
        public byte[] zzbiX;
        public zza zzbiY;
        public String zzbiZ;
        public long zzbja;
        public zzc zzbjb;
        public byte[] zzbjc;
        public int zzbjd;
        public int[] zzbje;

        public zzd() {
            zzFV();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzd)) {
                return false;
            }
            zzd zzd = (zzd) obj;
            if (this.zzbiO != zzd.zzbiO || this.zzbiP != zzd.zzbiP) {
                return false;
            }
            String str = this.tag;
            if (str == null) {
                if (zzd.tag != null) {
                    return false;
                }
            } else if (!str.equals(zzd.tag)) {
                return false;
            }
            if (this.zzbiQ != zzd.zzbiQ || this.zzbiR != zzd.zzbiR || this.zzbiS != zzd.zzbiS || !zzsc.equals((Object[]) this.zzbiT, (Object[]) zzd.zzbiT)) {
                return false;
            }
            zzb zzb = this.zzbiU;
            if (zzb == null) {
                if (zzd.zzbiU != null) {
                    return false;
                }
            } else if (!zzb.equals(zzd.zzbiU)) {
                return false;
            }
            if (!Arrays.equals(this.zzbiV, zzd.zzbiV) || !Arrays.equals(this.zzbiW, zzd.zzbiW) || !Arrays.equals(this.zzbiX, zzd.zzbiX)) {
                return false;
            }
            zza zza = this.zzbiY;
            if (zza == null) {
                if (zzd.zzbiY != null) {
                    return false;
                }
            } else if (!zza.equals(zzd.zzbiY)) {
                return false;
            }
            String str2 = this.zzbiZ;
            if (str2 == null) {
                if (zzd.zzbiZ != null) {
                    return false;
                }
            } else if (!str2.equals(zzd.zzbiZ)) {
                return false;
            }
            if (this.zzbja != zzd.zzbja) {
                return false;
            }
            zzc zzc = this.zzbjb;
            if (zzc == null) {
                if (zzd.zzbjb != null) {
                    return false;
                }
            } else if (!zzc.equals(zzd.zzbjb)) {
                return false;
            }
            if (!Arrays.equals(this.zzbjc, zzd.zzbjc) || this.zzbjd != zzd.zzbjd || !zzsc.equals(this.zzbje, zzd.zzbje)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzd.zzbik);
            }
            if (zzd.zzbik != null && !zzd.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int hashCode = (527 + getClass().getName().hashCode()) * 31;
            long j = this.zzbiO;
            int i = (hashCode + ((int) (j ^ (j >>> 32)))) * 31;
            long j2 = this.zzbiP;
            int i2 = (i + ((int) (j2 ^ (j2 >>> 32)))) * 31;
            String str = this.tag;
            int i3 = 0;
            int hashCode2 = (((((((((i2 + (str == null ? 0 : str.hashCode())) * 31) + this.zzbiQ) * 31) + this.zzbiR) * 31) + (this.zzbiS ? 1231 : 1237)) * 31) + zzsc.hashCode((Object[]) this.zzbiT)) * 31;
            zzb zzb = this.zzbiU;
            int hashCode3 = (((((((hashCode2 + (zzb == null ? 0 : zzb.hashCode())) * 31) + Arrays.hashCode(this.zzbiV)) * 31) + Arrays.hashCode(this.zzbiW)) * 31) + Arrays.hashCode(this.zzbiX)) * 31;
            zza zza = this.zzbiY;
            int hashCode4 = (hashCode3 + (zza == null ? 0 : zza.hashCode())) * 31;
            String str2 = this.zzbiZ;
            int hashCode5 = (hashCode4 + (str2 == null ? 0 : str2.hashCode())) * 31;
            long j3 = this.zzbja;
            int i4 = (hashCode5 + ((int) (j3 ^ (j3 >>> 32)))) * 31;
            zzc zzc = this.zzbjb;
            int hashCode6 = (((((((i4 + (zzc == null ? 0 : zzc.hashCode())) * 31) + Arrays.hashCode(this.zzbjc)) * 31) + this.zzbjd) * 31) + zzsc.hashCode(this.zzbje)) * 31;
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                i3 = this.zzbik.hashCode();
            }
            return hashCode6 + i3;
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int zzB = super.zzB();
            long j = this.zzbiO;
            if (j != 0) {
                zzB += zzrx.zzd(1, j);
            }
            String str = "";
            if (!this.tag.equals(str)) {
                zzB += zzrx.zzn(2, this.tag);
            }
            zze[] zzeArr = this.zzbiT;
            int i = 0;
            if (zzeArr != null && zzeArr.length > 0) {
                int i2 = zzB;
                int i3 = 0;
                while (true) {
                    zze[] zzeArr2 = this.zzbiT;
                    if (i3 >= zzeArr2.length) {
                        break;
                    }
                    zze zze = zzeArr2[i3];
                    if (zze != null) {
                        i2 += zzrx.zzc(3, (zzse) zze);
                    }
                    i3++;
                }
                zzB = i2;
            }
            if (!Arrays.equals(this.zzbiV, zzsh.zzbiE)) {
                zzB += zzrx.zzb(6, this.zzbiV);
            }
            zza zza = this.zzbiY;
            if (zza != null) {
                zzB += zzrx.zzc(7, (zzse) zza);
            }
            if (!Arrays.equals(this.zzbiW, zzsh.zzbiE)) {
                zzB += zzrx.zzb(8, this.zzbiW);
            }
            zzb zzb = this.zzbiU;
            if (zzb != null) {
                zzB += zzrx.zzc(9, (zzse) zzb);
            }
            boolean z = this.zzbiS;
            if (z) {
                zzB += zzrx.zzc(10, z);
            }
            int i4 = this.zzbiQ;
            if (i4 != 0) {
                zzB += zzrx.zzA(11, i4);
            }
            int i5 = this.zzbiR;
            if (i5 != 0) {
                zzB += zzrx.zzA(12, i5);
            }
            if (!Arrays.equals(this.zzbiX, zzsh.zzbiE)) {
                zzB += zzrx.zzb(13, this.zzbiX);
            }
            if (!this.zzbiZ.equals(str)) {
                zzB += zzrx.zzn(14, this.zzbiZ);
            }
            long j2 = this.zzbja;
            if (j2 != 180000) {
                zzB += zzrx.zze(15, j2);
            }
            zzc zzc = this.zzbjb;
            if (zzc != null) {
                zzB += zzrx.zzc(16, (zzse) zzc);
            }
            long j3 = this.zzbiP;
            if (j3 != 0) {
                zzB += zzrx.zzd(17, j3);
            }
            if (!Arrays.equals(this.zzbjc, zzsh.zzbiE)) {
                zzB += zzrx.zzb(18, this.zzbjc);
            }
            int i6 = this.zzbjd;
            if (i6 != 0) {
                zzB += zzrx.zzA(19, i6);
            }
            int[] iArr = this.zzbje;
            if (iArr == null || iArr.length <= 0) {
                return zzB;
            }
            int i7 = 0;
            while (true) {
                int[] iArr2 = this.zzbje;
                if (i >= iArr2.length) {
                    return zzB + i7 + (iArr2.length * 2);
                }
                i7 += zzrx.zzlJ(iArr2[i]);
                i++;
            }
        }

        public zzd zzFV() {
            this.zzbiO = 0;
            this.zzbiP = 0;
            String str = "";
            this.tag = str;
            this.zzbiQ = 0;
            this.zzbiR = 0;
            this.zzbiS = false;
            this.zzbiT = zze.zzFW();
            this.zzbiU = null;
            this.zzbiV = zzsh.zzbiE;
            this.zzbiW = zzsh.zzbiE;
            this.zzbiX = zzsh.zzbiE;
            this.zzbiY = null;
            this.zzbiZ = str;
            this.zzbja = 180000;
            this.zzbjb = null;
            this.zzbjc = zzsh.zzbiE;
            this.zzbjd = 0;
            this.zzbje = zzsh.zzbix;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        /* renamed from: zzJ */
        public zzd zzb(zzrw zzrw) throws IOException {
            zzse zzse;
            while (true) {
                int zzFo = zzrw.zzFo();
                switch (zzFo) {
                    case 0:
                        return this;
                    case 8:
                        this.zzbiO = zzrw.zzFq();
                        continue;
                    case 18:
                        this.tag = zzrw.readString();
                        continue;
                    case 26:
                        int zzc = zzsh.zzc(zzrw, 26);
                        zze[] zzeArr = this.zzbiT;
                        int length = zzeArr == null ? 0 : zzeArr.length;
                        zze[] zzeArr2 = new zze[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zzbiT, 0, zzeArr2, 0, length);
                        }
                        while (length < zzeArr2.length - 1) {
                            zzeArr2[length] = new zze();
                            zzrw.zza(zzeArr2[length]);
                            zzrw.zzFo();
                            length++;
                        }
                        zzeArr2[length] = new zze();
                        zzrw.zza(zzeArr2[length]);
                        this.zzbiT = zzeArr2;
                        continue;
                    case 50:
                        this.zzbiV = zzrw.readBytes();
                        continue;
                    case 58:
                        if (this.zzbiY == null) {
                            this.zzbiY = new zza();
                        }
                        zzse = this.zzbiY;
                        break;
                    case 66:
                        this.zzbiW = zzrw.readBytes();
                        continue;
                    case 74:
                        if (this.zzbiU == null) {
                            this.zzbiU = new zzb();
                        }
                        zzse = this.zzbiU;
                        break;
                    case 80:
                        this.zzbiS = zzrw.zzFs();
                        continue;
                    case 88:
                        this.zzbiQ = zzrw.zzFr();
                        continue;
                    case 96:
                        this.zzbiR = zzrw.zzFr();
                        continue;
                    case 106:
                        this.zzbiX = zzrw.readBytes();
                        continue;
                    case 114:
                        this.zzbiZ = zzrw.readString();
                        continue;
                    case DNSConstants.KNOWN_ANSWER_TTL /*120*/:
                        this.zzbja = zzrw.zzFu();
                        continue;
                    case 130:
                        if (this.zzbjb == null) {
                            this.zzbjb = new zzc();
                        }
                        zzse = this.zzbjb;
                        break;
                    case 136:
                        this.zzbiP = zzrw.zzFq();
                        continue;
                    case 146:
                        this.zzbjc = zzrw.readBytes();
                        continue;
                    case 152:
                        int zzFr = zzrw.zzFr();
                        if (zzFr == 0 || zzFr == 1 || zzFr == 2) {
                            this.zzbjd = zzFr;
                            break;
                        } else {
                            continue;
                        }
                    case 160:
                        int zzc2 = zzsh.zzc(zzrw, 160);
                        int[] iArr = this.zzbje;
                        int length2 = iArr == null ? 0 : iArr.length;
                        int[] iArr2 = new int[(zzc2 + length2)];
                        if (length2 != 0) {
                            System.arraycopy(this.zzbje, 0, iArr2, 0, length2);
                        }
                        while (length2 < iArr2.length - 1) {
                            iArr2[length2] = zzrw.zzFr();
                            zzrw.zzFo();
                            length2++;
                        }
                        iArr2[length2] = zzrw.zzFr();
                        this.zzbje = iArr2;
                        continue;
                    case 162:
                        int zzlC = zzrw.zzlC(zzrw.zzFv());
                        int position = zzrw.getPosition();
                        int i = 0;
                        while (zzrw.zzFA() > 0) {
                            zzrw.zzFr();
                            i++;
                        }
                        zzrw.zzlE(position);
                        int[] iArr3 = this.zzbje;
                        int length3 = iArr3 == null ? 0 : iArr3.length;
                        int[] iArr4 = new int[(i + length3)];
                        if (length3 != 0) {
                            System.arraycopy(this.zzbje, 0, iArr4, 0, length3);
                        }
                        while (length3 < iArr4.length) {
                            iArr4[length3] = zzrw.zzFr();
                            length3++;
                        }
                        this.zzbje = iArr4;
                        zzrw.zzlD(zzlC);
                        continue;
                    default:
                        if (!zza(zzrw, zzFo)) {
                            return this;
                        }
                        continue;
                }
                zzrw.zza(zzse);
            }
        }

        public void zza(zzrx zzrx) throws IOException {
            long j = this.zzbiO;
            if (j != 0) {
                zzrx.zzb(1, j);
            }
            String str = "";
            if (!this.tag.equals(str)) {
                zzrx.zzb(2, this.tag);
            }
            zze[] zzeArr = this.zzbiT;
            int i = 0;
            if (zzeArr != null && zzeArr.length > 0) {
                int i2 = 0;
                while (true) {
                    zze[] zzeArr2 = this.zzbiT;
                    if (i2 >= zzeArr2.length) {
                        break;
                    }
                    zze zze = zzeArr2[i2];
                    if (zze != null) {
                        zzrx.zza(3, (zzse) zze);
                    }
                    i2++;
                }
            }
            if (!Arrays.equals(this.zzbiV, zzsh.zzbiE)) {
                zzrx.zza(6, this.zzbiV);
            }
            zza zza = this.zzbiY;
            if (zza != null) {
                zzrx.zza(7, (zzse) zza);
            }
            if (!Arrays.equals(this.zzbiW, zzsh.zzbiE)) {
                zzrx.zza(8, this.zzbiW);
            }
            zzb zzb = this.zzbiU;
            if (zzb != null) {
                zzrx.zza(9, (zzse) zzb);
            }
            boolean z = this.zzbiS;
            if (z) {
                zzrx.zzb(10, z);
            }
            int i3 = this.zzbiQ;
            if (i3 != 0) {
                zzrx.zzy(11, i3);
            }
            int i4 = this.zzbiR;
            if (i4 != 0) {
                zzrx.zzy(12, i4);
            }
            if (!Arrays.equals(this.zzbiX, zzsh.zzbiE)) {
                zzrx.zza(13, this.zzbiX);
            }
            if (!this.zzbiZ.equals(str)) {
                zzrx.zzb(14, this.zzbiZ);
            }
            long j2 = this.zzbja;
            if (j2 != 180000) {
                zzrx.zzc(15, j2);
            }
            zzc zzc = this.zzbjb;
            if (zzc != null) {
                zzrx.zza(16, (zzse) zzc);
            }
            long j3 = this.zzbiP;
            if (j3 != 0) {
                zzrx.zzb(17, j3);
            }
            if (!Arrays.equals(this.zzbjc, zzsh.zzbiE)) {
                zzrx.zza(18, this.zzbjc);
            }
            int i5 = this.zzbjd;
            if (i5 != 0) {
                zzrx.zzy(19, i5);
            }
            int[] iArr = this.zzbje;
            if (iArr != null && iArr.length > 0) {
                while (true) {
                    int[] iArr2 = this.zzbje;
                    if (i >= iArr2.length) {
                        break;
                    }
                    zzrx.zzy(20, iArr2[i]);
                    i++;
                }
            }
            super.zza(zzrx);
        }
    }

    public static final class zze extends zzry<zze> {
        private static volatile zze[] zzbjf;
        public String key;
        public String value;

        public zze() {
            zzFX();
        }

        public static zze[] zzFW() {
            if (zzbjf == null) {
                synchronized (zzsc.zzbiu) {
                    if (zzbjf == null) {
                        zzbjf = new zze[0];
                    }
                }
            }
            return zzbjf;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zze)) {
                return false;
            }
            zze zze = (zze) obj;
            String str = this.key;
            if (str == null) {
                if (zze.key != null) {
                    return false;
                }
            } else if (!str.equals(zze.key)) {
                return false;
            }
            String str2 = this.value;
            if (str2 == null) {
                if (zze.value != null) {
                    return false;
                }
            } else if (!str2.equals(zze.value)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zze.zzbik);
            }
            if (zze.zzbik != null && !zze.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int hashCode = (527 + getClass().getName().hashCode()) * 31;
            String str = this.key;
            int i = 0;
            int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            String str2 = this.value;
            int hashCode3 = (hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31;
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                i = this.zzbik.hashCode();
            }
            return hashCode3 + i;
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int zzB = super.zzB();
            String str = "";
            if (!this.key.equals(str)) {
                zzB += zzrx.zzn(1, this.key);
            }
            return !this.value.equals(str) ? zzB + zzrx.zzn(2, this.value) : zzB;
        }

        public zze zzFX() {
            String str = "";
            this.key = str;
            this.value = str;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        /* renamed from: zzK */
        public zze zzb(zzrw zzrw) throws IOException {
            while (true) {
                int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo == 10) {
                    this.key = zzrw.readString();
                } else if (zzFo == 18) {
                    this.value = zzrw.readString();
                } else if (!zza(zzrw, zzFo)) {
                    return this;
                }
            }
        }

        public void zza(zzrx zzrx) throws IOException {
            String str = "";
            if (!this.key.equals(str)) {
                zzrx.zzb(1, this.key);
            }
            if (!this.value.equals(str)) {
                zzrx.zzb(2, this.value);
            }
            super.zza(zzrx);
        }
    }
}
