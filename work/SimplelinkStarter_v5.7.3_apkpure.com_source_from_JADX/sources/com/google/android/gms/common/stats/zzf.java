package com.google.android.gms.common.stats;

public abstract class zzf {
    public static int zzahY = 0;
    public static int zzahZ = 1;

    public abstract int getEventType();

    public abstract long getTimeMillis();

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTimeMillis());
        String str = "\t";
        sb.append(str);
        sb.append(getEventType());
        sb.append(str);
        sb.append(zzqd());
        sb.append(zzqg());
        return sb.toString();
    }

    public abstract long zzqd();

    public abstract String zzqg();
}
