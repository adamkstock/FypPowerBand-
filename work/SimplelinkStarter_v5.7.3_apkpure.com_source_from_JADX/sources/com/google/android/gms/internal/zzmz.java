package com.google.android.gms.internal;

public final class zzmz {
    private static int zza(StackTraceElement[] stackTraceElementArr, StackTraceElement[] stackTraceElementArr2) {
        int length = stackTraceElementArr2.length;
        int length2 = stackTraceElementArr.length;
        int i = 0;
        while (true) {
            length2--;
            if (length2 < 0) {
                break;
            }
            length--;
            if (length < 0 || !stackTraceElementArr2[length].equals(stackTraceElementArr[length2])) {
                break;
            }
            i++;
        }
        return i;
    }

    public static String zzqF() {
        String str;
        StringBuilder sb = new StringBuilder();
        Throwable th = new Throwable();
        StackTraceElement[] stackTrace = th.getStackTrace();
        sb.append("Async stack trace:");
        int length = stackTrace.length;
        int i = 0;
        while (true) {
            str = "\n\tat ";
            if (i >= length) {
                break;
            }
            StackTraceElement stackTraceElement = stackTrace[i];
            sb.append(str);
            sb.append(stackTraceElement);
            i++;
        }
        Throwable cause = th.getCause();
        while (cause != null) {
            sb.append("\nCaused by: ");
            sb.append(cause);
            StackTraceElement[] stackTrace2 = cause.getStackTrace();
            int zza = zza(stackTrace2, stackTrace);
            for (int i2 = 0; i2 < stackTrace2.length - zza; i2++) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(stackTrace2[i2]);
                sb.append(sb2.toString());
            }
            if (zza > 0) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("\n\t... ");
                sb3.append(zza);
                sb3.append(" more");
                sb.append(sb3.toString());
            }
            cause = cause.getCause();
            stackTrace = stackTrace2;
        }
        return sb.toString();
    }
}
