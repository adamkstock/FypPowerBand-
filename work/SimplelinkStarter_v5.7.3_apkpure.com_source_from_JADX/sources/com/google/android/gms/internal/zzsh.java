package com.google.android.gms.internal;

import java.io.IOException;

public final class zzsh {
    public static final double[] zzbiA = new double[0];
    public static final boolean[] zzbiB = new boolean[0];
    public static final String[] zzbiC = new String[0];
    public static final byte[][] zzbiD = new byte[0][];
    public static final byte[] zzbiE = new byte[0];
    public static final int[] zzbix = new int[0];
    public static final long[] zzbiy = new long[0];
    public static final float[] zzbiz = new float[0];

    static int zzD(int i, int i2) {
        return (i << 3) | i2;
    }

    public static boolean zzb(zzrw zzrw, int i) throws IOException {
        return zzrw.zzlA(i);
    }

    public static final int zzc(zzrw zzrw, int i) throws IOException {
        int position = zzrw.getPosition();
        zzrw.zzlA(i);
        int i2 = 1;
        while (zzrw.zzFo() == i) {
            zzrw.zzlA(i);
            i2++;
        }
        zzrw.zzlE(position);
        return i2;
    }

    static int zzlU(int i) {
        return i & 7;
    }

    public static int zzlV(int i) {
        return i >>> 3;
    }
}
