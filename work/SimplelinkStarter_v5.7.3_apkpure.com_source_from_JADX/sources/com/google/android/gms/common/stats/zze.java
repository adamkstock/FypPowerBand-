package com.google.android.gms.common.stats;

import android.os.SystemClock;
import android.support.p000v4.util.SimpleArrayMap;
import android.util.Log;

public class zze {
    private final long zzahV;
    private final int zzahW;
    private final SimpleArrayMap<String, Long> zzahX;

    public zze() {
        this.zzahV = 60000;
        this.zzahW = 10;
        this.zzahX = new SimpleArrayMap<>(10);
    }

    public zze(int i, long j) {
        this.zzahV = j;
        this.zzahW = i;
        this.zzahX = new SimpleArrayMap<>();
    }

    private void zzb(long j, long j2) {
        for (int size = this.zzahX.size() - 1; size >= 0; size--) {
            if (j2 - ((Long) this.zzahX.valueAt(size)).longValue() > j) {
                this.zzahX.removeAt(size);
            }
        }
    }

    public Long zzcx(String str) {
        Long l;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.zzahV;
        synchronized (this) {
            while (this.zzahX.size() >= this.zzahW) {
                zzb(j, elapsedRealtime);
                j /= 2;
                StringBuilder sb = new StringBuilder();
                sb.append("The max capacity ");
                sb.append(this.zzahW);
                sb.append(" is not enough. Current durationThreshold is: ");
                sb.append(j);
                Log.w("ConnectionTracker", sb.toString());
            }
            l = (Long) this.zzahX.put(str, Long.valueOf(elapsedRealtime));
        }
        return l;
    }

    public boolean zzcy(String str) {
        boolean z;
        synchronized (this) {
            z = this.zzahX.remove(str) != null;
        }
        return z;
    }
}
