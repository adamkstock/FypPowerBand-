package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;

public final class WakeLockEvent extends zzf implements SafeParcelable {
    public static final Creator<WakeLockEvent> CREATOR = new zzh();
    private final long mTimeout;
    final int mVersionCode;
    private final long zzahn;
    private int zzaho;
    private final long zzahv;
    private long zzahx;
    private final String zzaia;
    private final int zzaib;
    private final List<String> zzaic;
    private final String zzaid;
    private int zzaie;
    private final String zzaif;
    private final String zzaig;
    private final float zzaih;

    WakeLockEvent(int i, long j, int i2, String str, int i3, List<String> list, String str2, long j2, int i4, String str3, String str4, float f, long j3) {
        this.mVersionCode = i;
        this.zzahn = j;
        this.zzaho = i2;
        this.zzaia = str;
        this.zzaif = str3;
        this.zzaib = i3;
        this.zzahx = -1;
        this.zzaic = list;
        this.zzaid = str2;
        this.zzahv = j2;
        this.zzaie = i4;
        this.zzaig = str4;
        this.zzaih = f;
        this.mTimeout = j3;
    }

    public WakeLockEvent(long j, int i, String str, int i2, List<String> list, String str2, long j2, int i3, String str3, String str4, float f, long j3) {
        this(1, j, i, str, i2, list, str2, j2, i3, str3, str4, f, j3);
    }

    public int describeContents() {
        return 0;
    }

    public int getEventType() {
        return this.zzaho;
    }

    public long getTimeMillis() {
        return this.zzahn;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzh.zza(this, parcel, i);
    }

    public String zzqc() {
        return this.zzaid;
    }

    public long zzqd() {
        return this.zzahx;
    }

    public long zzqf() {
        return this.zzahv;
    }

    public String zzqg() {
        StringBuilder sb = new StringBuilder();
        String str = "\t";
        sb.append(str);
        sb.append(zzqj());
        sb.append(str);
        sb.append(zzql());
        sb.append(str);
        String str2 = "";
        sb.append(zzqm() == null ? str2 : TextUtils.join(",", zzqm()));
        sb.append(str);
        sb.append(zzqn());
        sb.append(str);
        sb.append(zzqk() == null ? str2 : zzqk());
        sb.append(str);
        if (zzqo() != null) {
            str2 = zzqo();
        }
        sb.append(str2);
        sb.append(str);
        sb.append(zzqp());
        return sb.toString();
    }

    public String zzqj() {
        return this.zzaia;
    }

    public String zzqk() {
        return this.zzaif;
    }

    public int zzql() {
        return this.zzaib;
    }

    public List<String> zzqm() {
        return this.zzaic;
    }

    public int zzqn() {
        return this.zzaie;
    }

    public String zzqo() {
        return this.zzaig;
    }

    public float zzqp() {
        return this.zzaih;
    }

    public long zzqq() {
        return this.mTimeout;
    }
}
