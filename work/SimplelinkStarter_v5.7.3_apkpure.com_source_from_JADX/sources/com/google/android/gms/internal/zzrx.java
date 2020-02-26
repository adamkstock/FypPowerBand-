package com.google.android.gms.internal;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

public final class zzrx {
    private final ByteBuffer zzbij;

    public static class zza extends IOException {
        zza(int i, int i2) {
            StringBuilder sb = new StringBuilder();
            sb.append("CodedOutputStream was writing to a flat byte array and ran out of space (pos ");
            sb.append(i);
            sb.append(" limit ");
            sb.append(i2);
            sb.append(").");
            super(sb.toString());
        }
    }

    private zzrx(ByteBuffer byteBuffer) {
        this.zzbij = byteBuffer;
        this.zzbij.order(ByteOrder.LITTLE_ENDIAN);
    }

    private zzrx(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    public static int zzA(int i, int i2) {
        return zzlM(i) + zzlJ(i2);
    }

    public static int zzB(int i, int i2) {
        return zzlM(i) + zzlK(i2);
    }

    public static zzrx zzC(byte[] bArr) {
        return zzb(bArr, 0, bArr.length);
    }

    public static int zzE(byte[] bArr) {
        return zzlO(bArr.length) + bArr.length;
    }

    private static int zza(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = 0;
        while (i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt < 2048) {
                i2 += (127 - charAt) >>> 31;
            } else {
                i2 += 2;
                if (55296 <= charAt && charAt <= 57343) {
                    if (Character.codePointAt(charSequence, i) >= 65536) {
                        i++;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unpaired surrogate at index ");
                        sb.append(i);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            }
            i++;
        }
        return i2;
    }

    private static int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int length = charSequence.length();
        int i4 = i2 + i;
        int i5 = 0;
        while (i5 < length) {
            int i6 = i5 + i;
            if (i6 >= i4) {
                break;
            }
            char charAt = charSequence.charAt(i5);
            if (charAt >= 128) {
                break;
            }
            bArr[i6] = (byte) charAt;
            i5++;
        }
        if (i5 == length) {
            return i + length;
        }
        int i7 = i + i5;
        while (i5 < length) {
            char charAt2 = charSequence.charAt(i5);
            if (charAt2 < 128 && i7 < i4) {
                i3 = i7 + 1;
                bArr[i7] = (byte) charAt2;
            } else if (charAt2 < 2048 && i7 <= i4 - 2) {
                int i8 = i7 + 1;
                bArr[i7] = (byte) ((charAt2 >>> 6) | 960);
                i7 = i8 + 1;
                bArr[i8] = (byte) ((charAt2 & '?') | 128);
                i5++;
            } else if ((charAt2 < 55296 || 57343 < charAt2) && i7 <= i4 - 3) {
                int i9 = i7 + 1;
                bArr[i7] = (byte) ((charAt2 >>> 12) | 480);
                int i10 = i9 + 1;
                bArr[i9] = (byte) (((charAt2 >>> 6) & 63) | 128);
                i3 = i10 + 1;
                bArr[i10] = (byte) ((charAt2 & '?') | 128);
            } else {
                String str = "Unpaired surrogate at index ";
                if (i7 <= i4 - 4) {
                    int i11 = i5 + 1;
                    if (i11 != charSequence.length()) {
                        char charAt3 = charSequence.charAt(i11);
                        if (Character.isSurrogatePair(charAt2, charAt3)) {
                            int codePoint = Character.toCodePoint(charAt2, charAt3);
                            int i12 = i7 + 1;
                            bArr[i7] = (byte) ((codePoint >>> 18) | 240);
                            int i13 = i12 + 1;
                            bArr[i12] = (byte) (((codePoint >>> 12) & 63) | 128);
                            int i14 = i13 + 1;
                            bArr[i13] = (byte) (((codePoint >>> 6) & 63) | 128);
                            i7 = i14 + 1;
                            bArr[i14] = (byte) ((codePoint & 63) | 128);
                            i5 = i11;
                            i5++;
                        } else {
                            i5 = i11;
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(i5 - 1);
                    throw new IllegalArgumentException(sb.toString());
                }
                if (55296 <= charAt2 && charAt2 <= 57343) {
                    int i15 = i5 + 1;
                    if (i15 == charSequence.length() || !Character.isSurrogatePair(charAt2, charSequence.charAt(i15))) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(i5);
                        throw new IllegalArgumentException(sb2.toString());
                    }
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Failed writing ");
                sb3.append(charAt2);
                sb3.append(" at index ");
                sb3.append(i7);
                throw new ArrayIndexOutOfBoundsException(sb3.toString());
            }
            i7 = i3;
            i5++;
        }
        return i7;
    }

    private static void zza(CharSequence charSequence, ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        } else if (byteBuffer.hasArray()) {
            try {
                byteBuffer.position(zza(charSequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
            } catch (ArrayIndexOutOfBoundsException e) {
                BufferOverflowException bufferOverflowException = new BufferOverflowException();
                bufferOverflowException.initCause(e);
                throw bufferOverflowException;
            }
        } else {
            zzb(charSequence, byteBuffer);
        }
    }

    public static int zzaa(long j) {
        return zzad(j);
    }

    public static int zzab(long j) {
        return zzad(zzaf(j));
    }

    public static int zzad(long j) {
        if ((-128 & j) == 0) {
            return 1;
        }
        if ((-16384 & j) == 0) {
            return 2;
        }
        if ((-2097152 & j) == 0) {
            return 3;
        }
        if ((-268435456 & j) == 0) {
            return 4;
        }
        if ((-34359738368L & j) == 0) {
            return 5;
        }
        if ((-4398046511104L & j) == 0) {
            return 6;
        }
        if ((-562949953421312L & j) == 0) {
            return 7;
        }
        if ((-72057594037927936L & j) == 0) {
            return 8;
        }
        return (j & Long.MIN_VALUE) == 0 ? 9 : 10;
    }

    public static long zzaf(long j) {
        return (j >> 63) ^ (j << 1);
    }

    public static int zzav(boolean z) {
        return 1;
    }

    public static int zzb(int i, double d) {
        return zzlM(i) + zzk(d);
    }

    public static int zzb(int i, zzse zzse) {
        return (zzlM(i) * 2) + zzd(zzse);
    }

    public static int zzb(int i, byte[] bArr) {
        return zzlM(i) + zzE(bArr);
    }

    public static zzrx zzb(byte[] bArr, int i, int i2) {
        return new zzrx(bArr, i, i2);
    }

    private static void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        int i;
        int length = charSequence.length();
        int i2 = 0;
        while (i2 < length) {
            char charAt = charSequence.charAt(i2);
            if (charAt >= 128) {
                if (charAt < 2048) {
                    i = (charAt >>> 6) | 960;
                } else if (charAt < 55296 || 57343 < charAt) {
                    byteBuffer.put((byte) ((charAt >>> 12) | 480));
                    i = ((charAt >>> 6) & 63) | 128;
                } else {
                    int i3 = i2 + 1;
                    if (i3 != charSequence.length()) {
                        char charAt2 = charSequence.charAt(i3);
                        if (Character.isSurrogatePair(charAt, charAt2)) {
                            int codePoint = Character.toCodePoint(charAt, charAt2);
                            byteBuffer.put((byte) ((codePoint >>> 18) | 240));
                            byteBuffer.put((byte) (((codePoint >>> 12) & 63) | 128));
                            byteBuffer.put((byte) (((codePoint >>> 6) & 63) | 128));
                            byteBuffer.put((byte) ((codePoint & 63) | 128));
                            i2 = i3;
                            i2++;
                        } else {
                            i2 = i3;
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unpaired surrogate at index ");
                    sb.append(i2 - 1);
                    throw new IllegalArgumentException(sb.toString());
                }
                byteBuffer.put((byte) i);
                charAt = (charAt & '?') | 128;
            }
            byteBuffer.put((byte) charAt);
            i2++;
        }
    }

    public static int zzc(int i, float f) {
        return zzlM(i) + zzj(f);
    }

    public static int zzc(int i, zzse zzse) {
        return zzlM(i) + zze(zzse);
    }

    public static int zzc(int i, boolean z) {
        return zzlM(i) + zzav(z);
    }

    private static int zzc(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && charSequence.charAt(i) < 128) {
            i++;
        }
        int i2 = length;
        while (true) {
            if (i >= length) {
                break;
            }
            char charAt = charSequence.charAt(i);
            if (charAt >= 2048) {
                i2 += zza(charSequence, i);
                break;
            }
            i2 += (127 - charAt) >>> 31;
            i++;
        }
        if (i2 >= length) {
            return i2;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("UTF-8 length does not fit in int: ");
        sb.append(((long) i2) + 4294967296L);
        throw new IllegalArgumentException(sb.toString());
    }

    public static int zzd(int i, long j) {
        return zzlM(i) + zzaa(j);
    }

    public static int zzd(zzse zzse) {
        return zzse.zzFR();
    }

    public static int zze(int i, long j) {
        return zzlM(i) + zzab(j);
    }

    public static int zze(zzse zzse) {
        int zzFR = zzse.zzFR();
        return zzlO(zzFR) + zzFR;
    }

    public static int zzfA(String str) {
        int zzc = zzc((CharSequence) str);
        return zzlO(zzc) + zzc;
    }

    public static int zzj(float f) {
        return 4;
    }

    public static int zzk(double d) {
        return 8;
    }

    public static int zzlJ(int i) {
        if (i >= 0) {
            return zzlO(i);
        }
        return 10;
    }

    public static int zzlK(int i) {
        return zzlO(zzlQ(i));
    }

    public static int zzlM(int i) {
        return zzlO(zzsh.zzD(i, 0));
    }

    public static int zzlO(int i) {
        if ((i & -128) == 0) {
            return 1;
        }
        if ((i & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & i) == 0) {
            return 3;
        }
        return (i & -268435456) == 0 ? 4 : 5;
    }

    public static int zzlQ(int i) {
        return (i >> 31) ^ (i << 1);
    }

    public static int zzn(int i, String str) {
        return zzlM(i) + zzfA(str);
    }

    public void zzC(int i, int i2) throws IOException {
        zzlN(zzsh.zzD(i, i2));
    }

    public void zzD(byte[] bArr) throws IOException {
        zzlN(bArr.length);
        zzF(bArr);
    }

    public void zzF(byte[] bArr) throws IOException {
        zzc(bArr, 0, bArr.length);
    }

    public int zzFD() {
        return this.zzbij.remaining();
    }

    public void zzFE() {
        if (zzFD() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public void zzY(long j) throws IOException {
        zzac(j);
    }

    public void zzZ(long j) throws IOException {
        zzac(zzaf(j));
    }

    public void zza(int i, double d) throws IOException {
        zzC(i, 1);
        zzj(d);
    }

    public void zza(int i, zzse zzse) throws IOException {
        zzC(i, 2);
        zzc(zzse);
    }

    public void zza(int i, byte[] bArr) throws IOException {
        zzC(i, 2);
        zzD(bArr);
    }

    public void zzac(long j) throws IOException {
        while ((-128 & j) != 0) {
            zzlL((((int) j) & 127) | 128);
            j >>>= 7;
        }
        zzlL((int) j);
    }

    public void zzae(long j) throws IOException {
        if (this.zzbij.remaining() >= 8) {
            this.zzbij.putLong(j);
            return;
        }
        throw new zza(this.zzbij.position(), this.zzbij.limit());
    }

    public void zzau(boolean z) throws IOException {
        zzlL(z ? 1 : 0);
    }

    public void zzb(byte b) throws IOException {
        if (this.zzbij.hasRemaining()) {
            this.zzbij.put(b);
            return;
        }
        throw new zza(this.zzbij.position(), this.zzbij.limit());
    }

    public void zzb(int i, float f) throws IOException {
        zzC(i, 5);
        zzi(f);
    }

    public void zzb(int i, long j) throws IOException {
        zzC(i, 0);
        zzY(j);
    }

    public void zzb(int i, String str) throws IOException {
        zzC(i, 2);
        zzfz(str);
    }

    public void zzb(int i, boolean z) throws IOException {
        zzC(i, 0);
        zzau(z);
    }

    public void zzb(zzse zzse) throws IOException {
        zzse.zza(this);
    }

    public void zzc(int i, long j) throws IOException {
        zzC(i, 0);
        zzZ(j);
    }

    public void zzc(zzse zzse) throws IOException {
        zzlN(zzse.zzFQ());
        zzse.zza(this);
    }

    public void zzc(byte[] bArr, int i, int i2) throws IOException {
        if (this.zzbij.remaining() >= i2) {
            this.zzbij.put(bArr, i, i2);
            return;
        }
        throw new zza(this.zzbij.position(), this.zzbij.limit());
    }

    public void zzfz(String str) throws IOException {
        try {
            int zzlO = zzlO(str.length());
            if (zzlO == zzlO(str.length() * 3)) {
                int position = this.zzbij.position();
                if (this.zzbij.remaining() >= zzlO) {
                    this.zzbij.position(position + zzlO);
                    zza((CharSequence) str, this.zzbij);
                    int position2 = this.zzbij.position();
                    this.zzbij.position(position);
                    zzlN((position2 - position) - zzlO);
                    this.zzbij.position(position2);
                    return;
                }
                throw new zza(position + zzlO, this.zzbij.limit());
            }
            zzlN(zzc((CharSequence) str));
            zza((CharSequence) str, this.zzbij);
        } catch (BufferOverflowException e) {
            zza zza2 = new zza(this.zzbij.position(), this.zzbij.limit());
            zza2.initCause(e);
            throw zza2;
        }
    }

    public void zzi(float f) throws IOException {
        zzlP(Float.floatToIntBits(f));
    }

    public void zzj(double d) throws IOException {
        zzae(Double.doubleToLongBits(d));
    }

    public void zzlH(int i) throws IOException {
        if (i >= 0) {
            zzlN(i);
        } else {
            zzac((long) i);
        }
    }

    public void zzlI(int i) throws IOException {
        zzlN(zzlQ(i));
    }

    public void zzlL(int i) throws IOException {
        zzb((byte) i);
    }

    public void zzlN(int i) throws IOException {
        while ((i & -128) != 0) {
            zzlL((i & 127) | 128);
            i >>>= 7;
        }
        zzlL(i);
    }

    public void zzlP(int i) throws IOException {
        if (this.zzbij.remaining() >= 4) {
            this.zzbij.putInt(i);
            return;
        }
        throw new zza(this.zzbij.position(), this.zzbij.limit());
    }

    public void zzy(int i, int i2) throws IOException {
        zzC(i, 0);
        zzlH(i2);
    }

    public void zzz(int i, int i2) throws IOException {
        zzC(i, 0);
        zzlI(i2);
    }
}
