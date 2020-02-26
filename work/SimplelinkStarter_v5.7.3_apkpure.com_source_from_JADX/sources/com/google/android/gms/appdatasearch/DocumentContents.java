package com.google.android.gms.appdatasearch;

import android.accounts.Account;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class DocumentContents implements SafeParcelable {
    public static final zzb CREATOR = new zzb();
    public final Account account;
    final int mVersionCode;
    final DocumentSection[] zzPX;
    public final String zzPY;
    public final boolean zzPZ;

    public static class zza {
        private List<DocumentSection> zzQa;
        private String zzQb;
        private boolean zzQc;
        private Account zzQd;

        public zza zzK(boolean z) {
            this.zzQc = z;
            return this;
        }

        public zza zza(DocumentSection documentSection) {
            if (this.zzQa == null) {
                this.zzQa = new ArrayList();
            }
            this.zzQa.add(documentSection);
            return this;
        }

        public zza zzb(Account account) {
            this.zzQd = account;
            return this;
        }

        public zza zzbx(String str) {
            this.zzQb = str;
            return this;
        }

        public DocumentContents zzlo() {
            String str = this.zzQb;
            boolean z = this.zzQc;
            Account account = this.zzQd;
            List<DocumentSection> list = this.zzQa;
            return new DocumentContents(str, z, account, list != null ? (DocumentSection[]) list.toArray(new DocumentSection[list.size()]) : null);
        }
    }

    DocumentContents(int i, DocumentSection[] documentSectionArr, String str, boolean z, Account account2) {
        this.mVersionCode = i;
        this.zzPX = documentSectionArr;
        this.zzPY = str;
        this.zzPZ = z;
        this.account = account2;
    }

    DocumentContents(String str, boolean z, Account account2, DocumentSection... documentSectionArr) {
        this(1, documentSectionArr, str, z, account2);
        BitSet bitSet = new BitSet(zzh.zzls());
        for (DocumentSection documentSection : documentSectionArr) {
            int i = documentSection.zzQl;
            if (i != -1) {
                if (!bitSet.get(i)) {
                    bitSet.set(i);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Duplicate global search section type ");
                    sb.append(zzh.zzak(i));
                    throw new IllegalArgumentException(sb.toString());
                }
            }
        }
    }

    public int describeContents() {
        zzb zzb = CREATOR;
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzb zzb = CREATOR;
        zzb.zza(this, parcel, i);
    }

    public DocumentSection zzbw(String str) {
        zzx.zzcr(str);
        DocumentSection[] documentSectionArr = this.zzPX;
        if (documentSectionArr == null) {
            return null;
        }
        for (DocumentSection documentSection : documentSectionArr) {
            if (str.equals(documentSection.zzlp().name)) {
                return documentSection;
            }
        }
        return null;
    }

    public String zzln() {
        DocumentSection zzbw = zzbw("web_url");
        if (zzbw != null) {
            return zzbw.zzQj;
        }
        return null;
    }
}
