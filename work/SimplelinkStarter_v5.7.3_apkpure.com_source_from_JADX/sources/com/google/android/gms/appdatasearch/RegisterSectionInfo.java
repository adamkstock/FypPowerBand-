package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class RegisterSectionInfo implements SafeParcelable {
    public static final zzi CREATOR = new zzi();
    final int mVersionCode;
    public final String name;
    public final int weight;
    public final String zzQF;
    public final boolean zzQG;
    public final boolean zzQH;
    public final String zzQI;
    public final Feature[] zzQJ;
    final int[] zzQK;
    public final String zzQL;

    public static final class zza {
        private final String mName;
        private String zzQM;
        private boolean zzQN;
        private int zzQO = 1;
        private boolean zzQP;
        private String zzQQ;
        private final List<Feature> zzQR = new ArrayList();
        private BitSet zzQS;
        private String zzQT;

        public zza(String str) {
            this.mName = str;
        }

        public zza zzM(boolean z) {
            this.zzQN = z;
            return this;
        }

        public zza zzN(boolean z) {
            this.zzQP = z;
            return this;
        }

        public zza zzal(int i) {
            if (this.zzQS == null) {
                this.zzQS = new BitSet();
            }
            this.zzQS.set(i);
            return this;
        }

        public zza zzbA(String str) {
            this.zzQM = str;
            return this;
        }

        public zza zzbB(String str) {
            this.zzQT = str;
            return this;
        }

        public RegisterSectionInfo zzlt() {
            int[] iArr;
            BitSet bitSet = this.zzQS;
            if (bitSet != null) {
                iArr = new int[bitSet.cardinality()];
                int i = 0;
                int nextSetBit = this.zzQS.nextSetBit(0);
                while (nextSetBit >= 0) {
                    int i2 = i + 1;
                    iArr[i] = nextSetBit;
                    nextSetBit = this.zzQS.nextSetBit(nextSetBit + 1);
                    i = i2;
                }
            } else {
                iArr = null;
            }
            int[] iArr2 = iArr;
            String str = this.mName;
            String str2 = this.zzQM;
            boolean z = this.zzQN;
            int i3 = this.zzQO;
            boolean z2 = this.zzQP;
            String str3 = this.zzQQ;
            List<Feature> list = this.zzQR;
            RegisterSectionInfo registerSectionInfo = new RegisterSectionInfo(str, str2, z, i3, z2, str3, (Feature[]) list.toArray(new Feature[list.size()]), iArr2, this.zzQT);
            return registerSectionInfo;
        }
    }

    RegisterSectionInfo(int i, String str, String str2, boolean z, int i2, boolean z2, String str3, Feature[] featureArr, int[] iArr, String str4) {
        this.mVersionCode = i;
        this.name = str;
        this.zzQF = str2;
        this.zzQG = z;
        this.weight = i2;
        this.zzQH = z2;
        this.zzQI = str3;
        this.zzQJ = featureArr;
        this.zzQK = iArr;
        this.zzQL = str4;
    }

    RegisterSectionInfo(String str, String str2, boolean z, int i, boolean z2, String str3, Feature[] featureArr, int[] iArr, String str4) {
        this(2, str, str2, z, i, z2, str3, featureArr, iArr, str4);
    }

    public int describeContents() {
        zzi zzi = CREATOR;
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzi zzi = CREATOR;
        zzi.zza(this, parcel, i);
    }
}
