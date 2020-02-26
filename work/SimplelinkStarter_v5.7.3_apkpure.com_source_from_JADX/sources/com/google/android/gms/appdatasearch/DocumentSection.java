package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.appdatasearch.RegisterSectionInfo.zza;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public class DocumentSection implements SafeParcelable {
    public static final zzd CREATOR = new zzd();
    public static final int zzQh = Integer.parseInt("-1");
    private static final RegisterSectionInfo zzQi = new zza("SsbContext").zzM(true).zzbA("blob").zzlt();
    final int mVersionCode;
    public final String zzQj;
    final RegisterSectionInfo zzQk;
    public final int zzQl;
    public final byte[] zzQm;

    DocumentSection(int i, String str, RegisterSectionInfo registerSectionInfo, int i2, byte[] bArr) {
        boolean z = i2 == zzQh || zzh.zzak(i2) != null;
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid section type ");
        sb.append(i2);
        zzx.zzb(z, (Object) sb.toString());
        this.mVersionCode = i;
        this.zzQj = str;
        this.zzQk = registerSectionInfo;
        this.zzQl = i2;
        this.zzQm = bArr;
        String zzlq = zzlq();
        if (zzlq != null) {
            throw new IllegalArgumentException(zzlq);
        }
    }

    public DocumentSection(String str, RegisterSectionInfo registerSectionInfo) {
        this(1, str, registerSectionInfo, zzQh, null);
    }

    public DocumentSection(String str, RegisterSectionInfo registerSectionInfo, String str2) {
        this(1, str, registerSectionInfo, zzh.zzbz(str2), null);
    }

    public DocumentSection(byte[] bArr, RegisterSectionInfo registerSectionInfo) {
        this(1, null, registerSectionInfo, zzQh, bArr);
    }

    public static DocumentSection zzh(byte[] bArr) {
        return new DocumentSection(bArr, zzQi);
    }

    public int describeContents() {
        zzd zzd = CREATOR;
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzd zzd = CREATOR;
        zzd.zza(this, parcel, i);
    }

    public RegisterSectionInfo zzlp() {
        return this.zzQk;
    }

    public String zzlq() {
        int i = this.zzQl;
        if (i != zzQh && zzh.zzak(i) == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid section type ");
            sb.append(this.zzQl);
            return sb.toString();
        } else if (this.zzQj == null || this.zzQm == null) {
            return null;
        } else {
            return "Both content and blobContent set";
        }
    }
}
