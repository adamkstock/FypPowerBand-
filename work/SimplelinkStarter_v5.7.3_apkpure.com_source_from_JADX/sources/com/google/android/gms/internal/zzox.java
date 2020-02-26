package com.google.android.gms.internal;

import java.io.IOException;

public interface zzox {

    public static final class zza extends zzry<zza> {
        public C1141zza[] zzaCU;

        /* renamed from: com.google.android.gms.internal.zzox$zza$zza reason: collision with other inner class name */
        public static final class C1141zza extends zzry<C1141zza> {
            private static volatile C1141zza[] zzaCV;
            public int viewId;
            public String zzaCW;
            public String zzaCX;

            public C1141zza() {
                zzwe();
            }

            public static C1141zza[] zzwd() {
                if (zzaCV == null) {
                    synchronized (zzsc.zzbiu) {
                        if (zzaCV == null) {
                            zzaCV = new C1141zza[0];
                        }
                    }
                }
                return zzaCV;
            }

            public boolean equals(Object obj) {
                boolean z = true;
                if (obj == this) {
                    return true;
                }
                if (!(obj instanceof C1141zza)) {
                    return false;
                }
                C1141zza zza = (C1141zza) obj;
                String str = this.zzaCW;
                if (str == null) {
                    if (zza.zzaCW != null) {
                        return false;
                    }
                } else if (!str.equals(zza.zzaCW)) {
                    return false;
                }
                String str2 = this.zzaCX;
                if (str2 == null) {
                    if (zza.zzaCX != null) {
                        return false;
                    }
                } else if (!str2.equals(zza.zzaCX)) {
                    return false;
                }
                if (this.viewId != zza.viewId) {
                    return false;
                }
                if (this.zzbik != null && !this.zzbik.isEmpty()) {
                    return this.zzbik.equals(zza.zzbik);
                }
                if (zza.zzbik != null && !zza.zzbik.isEmpty()) {
                    z = false;
                }
                return z;
            }

            public int hashCode() {
                int hashCode = (527 + getClass().getName().hashCode()) * 31;
                String str = this.zzaCW;
                int i = 0;
                int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
                String str2 = this.zzaCX;
                int hashCode3 = (((hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31) + this.viewId) * 31;
                if (this.zzbik != null && !this.zzbik.isEmpty()) {
                    i = this.zzbik.hashCode();
                }
                return hashCode3 + i;
            }

            /* access modifiers changed from: protected */
            public int zzB() {
                int zzB = super.zzB();
                String str = "";
                if (!this.zzaCW.equals(str)) {
                    zzB += zzrx.zzn(1, this.zzaCW);
                }
                if (!this.zzaCX.equals(str)) {
                    zzB += zzrx.zzn(2, this.zzaCX);
                }
                int i = this.viewId;
                return i != 0 ? zzB + zzrx.zzA(3, i) : zzB;
            }

            public void zza(zzrx zzrx) throws IOException {
                String str = "";
                if (!this.zzaCW.equals(str)) {
                    zzrx.zzb(1, this.zzaCW);
                }
                if (!this.zzaCX.equals(str)) {
                    zzrx.zzb(2, this.zzaCX);
                }
                int i = this.viewId;
                if (i != 0) {
                    zzrx.zzy(3, i);
                }
                super.zza(zzrx);
            }

            /* renamed from: zzq */
            public C1141zza zzb(zzrw zzrw) throws IOException {
                while (true) {
                    int zzFo = zzrw.zzFo();
                    if (zzFo == 0) {
                        return this;
                    }
                    if (zzFo == 10) {
                        this.zzaCW = zzrw.readString();
                    } else if (zzFo == 18) {
                        this.zzaCX = zzrw.readString();
                    } else if (zzFo == 24) {
                        this.viewId = zzrw.zzFr();
                    } else if (!zza(zzrw, zzFo)) {
                        return this;
                    }
                }
            }

            public C1141zza zzwe() {
                String str = "";
                this.zzaCW = str;
                this.zzaCX = str;
                this.viewId = 0;
                this.zzbik = null;
                this.zzbiv = -1;
                return this;
            }
        }

        public zza() {
            zzwc();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza zza = (zza) obj;
            if (!zzsc.equals((Object[]) this.zzaCU, (Object[]) zza.zzaCU)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zza.zzbik);
            }
            if (zza.zzbik != null && !zza.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return ((((527 + getClass().getName().hashCode()) * 31) + zzsc.hashCode((Object[]) this.zzaCU)) * 31) + ((this.zzbik == null || this.zzbik.isEmpty()) ? 0 : this.zzbik.hashCode());
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int zzB = super.zzB();
            C1141zza[] zzaArr = this.zzaCU;
            if (zzaArr != null && zzaArr.length > 0) {
                int i = 0;
                while (true) {
                    C1141zza[] zzaArr2 = this.zzaCU;
                    if (i >= zzaArr2.length) {
                        break;
                    }
                    C1141zza zza = zzaArr2[i];
                    if (zza != null) {
                        zzB += zzrx.zzc(1, (zzse) zza);
                    }
                    i++;
                }
            }
            return zzB;
        }

        public void zza(zzrx zzrx) throws IOException {
            C1141zza[] zzaArr = this.zzaCU;
            if (zzaArr != null && zzaArr.length > 0) {
                int i = 0;
                while (true) {
                    C1141zza[] zzaArr2 = this.zzaCU;
                    if (i >= zzaArr2.length) {
                        break;
                    }
                    C1141zza zza = zzaArr2[i];
                    if (zza != null) {
                        zzrx.zza(1, (zzse) zza);
                    }
                    i++;
                }
            }
            super.zza(zzrx);
        }

        /* renamed from: zzp */
        public zza zzb(zzrw zzrw) throws IOException {
            while (true) {
                int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo == 10) {
                    int zzc = zzsh.zzc(zzrw, 10);
                    C1141zza[] zzaArr = this.zzaCU;
                    int length = zzaArr == null ? 0 : zzaArr.length;
                    C1141zza[] zzaArr2 = new C1141zza[(zzc + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzaCU, 0, zzaArr2, 0, length);
                    }
                    while (length < zzaArr2.length - 1) {
                        zzaArr2[length] = new C1141zza();
                        zzrw.zza(zzaArr2[length]);
                        zzrw.zzFo();
                        length++;
                    }
                    zzaArr2[length] = new C1141zza();
                    zzrw.zza(zzaArr2[length]);
                    this.zzaCU = zzaArr2;
                } else if (!zza(zzrw, zzFo)) {
                    return this;
                }
            }
        }

        public zza zzwc() {
            this.zzaCU = C1141zza.zzwd();
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
    }

    public static final class zzb extends zzry<zzb> {
        private static volatile zzb[] zzaCY;
        public String name;
        public zzd zzaCZ;

        public zzb() {
            zzwg();
        }

        public static zzb[] zzwf() {
            if (zzaCY == null) {
                synchronized (zzsc.zzbiu) {
                    if (zzaCY == null) {
                        zzaCY = new zzb[0];
                    }
                }
            }
            return zzaCY;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzb)) {
                return false;
            }
            zzb zzb = (zzb) obj;
            String str = this.name;
            if (str == null) {
                if (zzb.name != null) {
                    return false;
                }
            } else if (!str.equals(zzb.name)) {
                return false;
            }
            zzd zzd = this.zzaCZ;
            if (zzd == null) {
                if (zzb.zzaCZ != null) {
                    return false;
                }
            } else if (!zzd.equals(zzb.zzaCZ)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzb.zzbik);
            }
            if (zzb.zzbik != null && !zzb.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int hashCode = (527 + getClass().getName().hashCode()) * 31;
            String str = this.name;
            int i = 0;
            int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            zzd zzd = this.zzaCZ;
            int hashCode3 = (hashCode2 + (zzd == null ? 0 : zzd.hashCode())) * 31;
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                i = this.zzbik.hashCode();
            }
            return hashCode3 + i;
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int zzB = super.zzB();
            if (!this.name.equals("")) {
                zzB += zzrx.zzn(1, this.name);
            }
            zzd zzd = this.zzaCZ;
            return zzd != null ? zzB + zzrx.zzc(2, (zzse) zzd) : zzB;
        }

        public void zza(zzrx zzrx) throws IOException {
            if (!this.name.equals("")) {
                zzrx.zzb(1, this.name);
            }
            zzd zzd = this.zzaCZ;
            if (zzd != null) {
                zzrx.zza(2, (zzse) zzd);
            }
            super.zza(zzrx);
        }

        /* renamed from: zzr */
        public zzb zzb(zzrw zzrw) throws IOException {
            while (true) {
                int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo == 10) {
                    this.name = zzrw.readString();
                } else if (zzFo == 18) {
                    if (this.zzaCZ == null) {
                        this.zzaCZ = new zzd();
                    }
                    zzrw.zza(this.zzaCZ);
                } else if (!zza(zzrw, zzFo)) {
                    return this;
                }
            }
        }

        public zzb zzwg() {
            this.name = "";
            this.zzaCZ = null;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
    }

    public static final class zzc extends zzry<zzc> {
        public String type;
        public zzb[] zzaDa;

        public zzc() {
            zzwh();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzc)) {
                return false;
            }
            zzc zzc = (zzc) obj;
            String str = this.type;
            if (str == null) {
                if (zzc.type != null) {
                    return false;
                }
            } else if (!str.equals(zzc.type)) {
                return false;
            }
            if (!zzsc.equals((Object[]) this.zzaDa, (Object[]) zzc.zzaDa)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzc.zzbik);
            }
            if (zzc.zzbik != null && !zzc.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int hashCode = (527 + getClass().getName().hashCode()) * 31;
            String str = this.type;
            int i = 0;
            int hashCode2 = (((hashCode + (str == null ? 0 : str.hashCode())) * 31) + zzsc.hashCode((Object[]) this.zzaDa)) * 31;
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                i = this.zzbik.hashCode();
            }
            return hashCode2 + i;
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int zzB = super.zzB();
            if (!this.type.equals("")) {
                zzB += zzrx.zzn(1, this.type);
            }
            zzb[] zzbArr = this.zzaDa;
            if (zzbArr != null && zzbArr.length > 0) {
                int i = 0;
                while (true) {
                    zzb[] zzbArr2 = this.zzaDa;
                    if (i >= zzbArr2.length) {
                        break;
                    }
                    zzb zzb = zzbArr2[i];
                    if (zzb != null) {
                        zzB += zzrx.zzc(2, (zzse) zzb);
                    }
                    i++;
                }
            }
            return zzB;
        }

        public void zza(zzrx zzrx) throws IOException {
            if (!this.type.equals("")) {
                zzrx.zzb(1, this.type);
            }
            zzb[] zzbArr = this.zzaDa;
            if (zzbArr != null && zzbArr.length > 0) {
                int i = 0;
                while (true) {
                    zzb[] zzbArr2 = this.zzaDa;
                    if (i >= zzbArr2.length) {
                        break;
                    }
                    zzb zzb = zzbArr2[i];
                    if (zzb != null) {
                        zzrx.zza(2, (zzse) zzb);
                    }
                    i++;
                }
            }
            super.zza(zzrx);
        }

        /* renamed from: zzs */
        public zzc zzb(zzrw zzrw) throws IOException {
            while (true) {
                int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo == 10) {
                    this.type = zzrw.readString();
                } else if (zzFo == 18) {
                    int zzc = zzsh.zzc(zzrw, 18);
                    zzb[] zzbArr = this.zzaDa;
                    int length = zzbArr == null ? 0 : zzbArr.length;
                    zzb[] zzbArr2 = new zzb[(zzc + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzaDa, 0, zzbArr2, 0, length);
                    }
                    while (length < zzbArr2.length - 1) {
                        zzbArr2[length] = new zzb();
                        zzrw.zza(zzbArr2[length]);
                        zzrw.zzFo();
                        length++;
                    }
                    zzbArr2[length] = new zzb();
                    zzrw.zza(zzbArr2[length]);
                    this.zzaDa = zzbArr2;
                } else if (!zza(zzrw, zzFo)) {
                    return this;
                }
            }
        }

        public zzc zzwh() {
            this.type = "";
            this.zzaDa = zzb.zzwf();
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
    }

    public static final class zzd extends zzry<zzd> {
        public boolean zzaDb;
        public long zzaDc;
        public double zzaDd;
        public zzc zzaDe;
        public String zzagS;

        public zzd() {
            zzwi();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzd)) {
                return false;
            }
            zzd zzd = (zzd) obj;
            if (this.zzaDb != zzd.zzaDb) {
                return false;
            }
            String str = this.zzagS;
            if (str == null) {
                if (zzd.zzagS != null) {
                    return false;
                }
            } else if (!str.equals(zzd.zzagS)) {
                return false;
            }
            if (this.zzaDc != zzd.zzaDc || Double.doubleToLongBits(this.zzaDd) != Double.doubleToLongBits(zzd.zzaDd)) {
                return false;
            }
            zzc zzc = this.zzaDe;
            if (zzc == null) {
                if (zzd.zzaDe != null) {
                    return false;
                }
            } else if (!zzc.equals(zzd.zzaDe)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzd.zzbik);
            }
            if (zzd.zzbik != null && !zzd.zzbik.isEmpty()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int hashCode = (((527 + getClass().getName().hashCode()) * 31) + (this.zzaDb ? 1231 : 1237)) * 31;
            String str = this.zzagS;
            int i = 0;
            int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            long j = this.zzaDc;
            int i2 = hashCode2 + ((int) (j ^ (j >>> 32)));
            long doubleToLongBits = Double.doubleToLongBits(this.zzaDd);
            int i3 = ((i2 * 31) + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)))) * 31;
            zzc zzc = this.zzaDe;
            int hashCode3 = (i3 + (zzc == null ? 0 : zzc.hashCode())) * 31;
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                i = this.zzbik.hashCode();
            }
            return hashCode3 + i;
        }

        /* access modifiers changed from: protected */
        public int zzB() {
            int zzB = super.zzB();
            boolean z = this.zzaDb;
            if (z) {
                zzB += zzrx.zzc(1, z);
            }
            if (!this.zzagS.equals("")) {
                zzB += zzrx.zzn(2, this.zzagS);
            }
            long j = this.zzaDc;
            if (j != 0) {
                zzB += zzrx.zzd(3, j);
            }
            if (Double.doubleToLongBits(this.zzaDd) != Double.doubleToLongBits(0.0d)) {
                zzB += zzrx.zzb(4, this.zzaDd);
            }
            zzc zzc = this.zzaDe;
            return zzc != null ? zzB + zzrx.zzc(5, (zzse) zzc) : zzB;
        }

        public void zza(zzrx zzrx) throws IOException {
            boolean z = this.zzaDb;
            if (z) {
                zzrx.zzb(1, z);
            }
            if (!this.zzagS.equals("")) {
                zzrx.zzb(2, this.zzagS);
            }
            long j = this.zzaDc;
            if (j != 0) {
                zzrx.zzb(3, j);
            }
            if (Double.doubleToLongBits(this.zzaDd) != Double.doubleToLongBits(0.0d)) {
                zzrx.zza(4, this.zzaDd);
            }
            zzc zzc = this.zzaDe;
            if (zzc != null) {
                zzrx.zza(5, (zzse) zzc);
            }
            super.zza(zzrx);
        }

        /* renamed from: zzt */
        public zzd zzb(zzrw zzrw) throws IOException {
            while (true) {
                int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo == 8) {
                    this.zzaDb = zzrw.zzFs();
                } else if (zzFo == 18) {
                    this.zzagS = zzrw.readString();
                } else if (zzFo == 24) {
                    this.zzaDc = zzrw.zzFq();
                } else if (zzFo == 33) {
                    this.zzaDd = zzrw.readDouble();
                } else if (zzFo == 42) {
                    if (this.zzaDe == null) {
                        this.zzaDe = new zzc();
                    }
                    zzrw.zza(this.zzaDe);
                } else if (!zza(zzrw, zzFo)) {
                    return this;
                }
            }
        }

        public zzd zzwi() {
            this.zzaDb = false;
            this.zzagS = "";
            this.zzaDc = 0;
            this.zzaDd = 0.0d;
            this.zzaDe = null;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
    }
}
