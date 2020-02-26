package com.google.android.gms.internal;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;

public final class zzsf {
    private static void zza(String str, Object obj, StringBuffer stringBuffer, StringBuffer stringBuffer2) throws IllegalAccessException, InvocationTargetException {
        String str2;
        Field[] fields;
        if (obj != null) {
            if (obj instanceof zzse) {
                int length = stringBuffer.length();
                if (str != null) {
                    stringBuffer2.append(stringBuffer);
                    stringBuffer2.append(zzfB(str));
                    stringBuffer2.append(" <\n");
                    stringBuffer.append("  ");
                }
                Class cls = obj.getClass();
                for (Field field : cls.getFields()) {
                    int modifiers = field.getModifiers();
                    String name = field.getName();
                    if (!"cachedSize".equals(name) && (modifiers & 1) == 1 && (modifiers & 8) != 8) {
                        String str3 = "_";
                        if (!name.startsWith(str3) && !name.endsWith(str3)) {
                            Class type = field.getType();
                            Object obj2 = field.get(obj);
                            if (!type.isArray() || type.getComponentType() == Byte.TYPE) {
                                zza(name, obj2, stringBuffer, stringBuffer2);
                            } else {
                                int length2 = obj2 == null ? 0 : Array.getLength(obj2);
                                for (int i = 0; i < length2; i++) {
                                    zza(name, Array.get(obj2, i), stringBuffer, stringBuffer2);
                                }
                            }
                        }
                    }
                }
                for (Method name2 : cls.getMethods()) {
                    String name3 = name2.getName();
                    if (name3.startsWith("set")) {
                        String substring = name3.substring(3);
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append("has");
                            sb.append(substring);
                            if (((Boolean) cls.getMethod(sb.toString(), new Class[0]).invoke(obj, new Object[0])).booleanValue()) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("get");
                                sb2.append(substring);
                                zza(substring, cls.getMethod(sb2.toString(), new Class[0]).invoke(obj, new Object[0]), stringBuffer, stringBuffer2);
                            }
                        } catch (NoSuchMethodException unused) {
                        }
                    }
                }
                if (str != null) {
                    stringBuffer.setLength(length);
                    stringBuffer2.append(stringBuffer);
                    str2 = ">\n";
                } else {
                    return;
                }
            } else {
                String zzfB = zzfB(str);
                stringBuffer2.append(stringBuffer);
                stringBuffer2.append(zzfB);
                stringBuffer2.append(": ");
                if (obj instanceof String) {
                    String zzfC = zzfC((String) obj);
                    String str4 = "\"";
                    stringBuffer2.append(str4);
                    stringBuffer2.append(zzfC);
                    stringBuffer2.append(str4);
                } else if (obj instanceof byte[]) {
                    zza((byte[]) obj, stringBuffer2);
                } else {
                    stringBuffer2.append(obj);
                }
                str2 = "\n";
            }
            stringBuffer2.append(str2);
        }
    }

    private static void zza(byte[] bArr, StringBuffer stringBuffer) {
        if (bArr == null) {
            stringBuffer.append("\"\"");
            return;
        }
        stringBuffer.append('\"');
        for (byte b : bArr) {
            byte b2 = b & 255;
            if (b2 == 92 || b2 == 34) {
                stringBuffer.append('\\');
            } else if (b2 < 32 || b2 >= Byte.MAX_VALUE) {
                stringBuffer.append(String.format("\\%03o", new Object[]{Integer.valueOf(b2)}));
            }
            stringBuffer.append((char) b2);
        }
        stringBuffer.append('\"');
    }

    private static String zzcz(String str) {
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt < ' ' || charAt > '~' || charAt == '\"' || charAt == '\'') {
                sb.append(String.format("\\u%04x", new Object[]{Integer.valueOf(charAt)}));
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    private static String zzfB(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (i != 0) {
                if (Character.isUpperCase(charAt)) {
                    stringBuffer.append('_');
                }
                stringBuffer.append(charAt);
            }
            charAt = Character.toLowerCase(charAt);
            stringBuffer.append(charAt);
        }
        return stringBuffer.toString();
    }

    private static String zzfC(String str) {
        if (!str.startsWith(HttpHost.DEFAULT_SCHEME_NAME) && str.length() > 200) {
            StringBuilder sb = new StringBuilder();
            sb.append(str.substring(0, HttpStatus.SC_OK));
            sb.append("[...]");
            str = sb.toString();
        }
        return zzcz(str);
    }

    public static <T extends zzse> String zzg(T t) {
        StringBuilder sb;
        String message;
        String str = "Error printing proto: ";
        if (t == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            zza(null, t, new StringBuffer(), stringBuffer);
            return stringBuffer.toString();
        } catch (IllegalAccessException e) {
            sb = new StringBuilder();
            sb.append(str);
            message = e.getMessage();
            sb.append(message);
            return sb.toString();
        } catch (InvocationTargetException e2) {
            sb = new StringBuilder();
            sb.append(str);
            message = e2.getMessage();
            sb.append(message);
            return sb.toString();
        }
    }
}
