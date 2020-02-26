package com.p004ti.util;

import java.util.Formatter;

/* renamed from: com.ti.util.Conversion */
public class Conversion {
    public static short buildUint16(byte b, byte b2) {
        return (short) ((b << 8) + (b2 & 255));
    }

    public static byte hiUint16(short s) {
        return (byte) (s >> 8);
    }

    private static boolean isAsciiPrintable(char c) {
        return c >= ' ' && c < 127;
    }

    public static byte loUint16(short s) {
        return (byte) (s & 255);
    }

    public static String BytetohexString(byte[] bArr, int i) {
        StringBuilder sb = new StringBuilder(bArr.length * 3);
        Formatter formatter = new Formatter(sb);
        for (int i2 = 0; i2 < i; i2++) {
            if (i2 < i - 1) {
                formatter.format("%02X:", new Object[]{Byte.valueOf(bArr[i2])});
            } else {
                formatter.format("%02X", new Object[]{Byte.valueOf(bArr[i2])});
            }
        }
        formatter.close();
        return sb.toString();
    }

    static String BytetohexString(byte[] bArr, boolean z) {
        StringBuilder sb = new StringBuilder(bArr.length * 3);
        Formatter formatter = new Formatter(sb);
        String str = "%02X:";
        String str2 = "%02X";
        if (!z) {
            for (int i = 0; i < bArr.length; i++) {
                if (i < bArr.length - 1) {
                    formatter.format(str, new Object[]{Byte.valueOf(bArr[i])});
                } else {
                    formatter.format(str2, new Object[]{Byte.valueOf(bArr[i])});
                }
            }
        } else {
            for (int length = bArr.length - 1; length >= 0; length--) {
                if (length > 0) {
                    formatter.format(str, new Object[]{Byte.valueOf(bArr[length])});
                } else {
                    formatter.format(str2, new Object[]{Byte.valueOf(bArr[length])});
                }
            }
        }
        formatter.close();
        return sb.toString();
    }

    public static int hexStringtoByte(String str, byte[] bArr) {
        if (str == null) {
            return 0;
        }
        int i = 0;
        boolean z = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if ((str.charAt(i2) >= '0' && str.charAt(i2) <= '9') || ((str.charAt(i2) >= 'a' && str.charAt(i2) <= 'f') || (str.charAt(i2) >= 'A' && str.charAt(i2) <= 'F'))) {
                if (z) {
                    bArr[i] = (byte) (bArr[i] + ((byte) Character.digit(str.charAt(i2), 16)));
                    i++;
                } else {
                    bArr[i] = (byte) (Character.digit(str.charAt(i2), 16) << 4);
                }
                z = !z;
            }
        }
        return i;
    }

    public static boolean isAsciiPrintable(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!isAsciiPrintable(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
