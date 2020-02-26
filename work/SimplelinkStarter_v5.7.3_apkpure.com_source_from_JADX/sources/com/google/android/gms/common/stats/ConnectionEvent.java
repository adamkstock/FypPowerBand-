package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class ConnectionEvent extends zzf implements SafeParcelable {
    public static final Creator<ConnectionEvent> CREATOR = new zza();
    final int mVersionCode;
    private final long zzahn;
    private int zzaho;
    private final String zzahp;
    private final String zzahq;
    private final String zzahr;
    private final String zzahs;
    private final String zzaht;
    private final String zzahu;
    private final long zzahv;
    private final long zzahw;
    private long zzahx;

    ConnectionEvent(int i, long j, int i2, String str, String str2, String str3, String str4, String str5, String str6, long j2, long j3) {
        this.mVersionCode = i;
        this.zzahn = j;
        this.zzaho = i2;
        this.zzahp = str;
        this.zzahq = str2;
        this.zzahr = str3;
        this.zzahs = str4;
        this.zzahx = -1;
        this.zzaht = str5;
        this.zzahu = str6;
        this.zzahv = j2;
        this.zzahw = j3;
    }

    public ConnectionEvent(long j, int i, String str, String str2, String str3, String str4, String str5, String str6, long j2, long j3) {
        this(1, j, i, str, str2, str3, str4, str5, str6, j2, j3);
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
        zza.zza(this, parcel, i);
    }

    public String zzpX() {
        return this.zzahp;
    }

    public String zzpY() {
        return this.zzahq;
    }

    public String zzpZ() {
        return this.zzahr;
    }

    public String zzqa() {
        return this.zzahs;
    }

    public String zzqb() {
        return this.zzaht;
    }

    public String zzqc() {
        return this.zzahu;
    }

    public long zzqd() {
        return this.zzahx;
    }

    public long zzqe() {
        return this.zzahw;
    }

    public long zzqf() {
        return this.zzahv;
    }

    public String zzqg() {
        StringBuilder sb = new StringBuilder();
        String str = "\t";
        sb.append(str);
        sb.append(zzpX());
        String str2 = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        sb.append(str2);
        sb.append(zzpY());
        sb.append(str);
        sb.append(zzpZ());
        sb.append(str2);
        sb.append(zzqa());
        sb.append(str);
        String str3 = this.zzaht;
        if (str3 == null) {
            str3 = "";
        }
        sb.append(str3);
        sb.append(str);
        sb.append(zzqe());
        return sb.toString();
    }
}
