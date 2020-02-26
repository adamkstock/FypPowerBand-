package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public final class PasswordSpecification implements SafeParcelable {
    public static final zze CREATOR = new zze();
    public static final PasswordSpecification zzSt = new zza().zzg(12, 16).zzbD("abcdefghijkmnopqrstxyzABCDEFGHJKLMNPQRSTXY3456789").zzf("abcdefghijkmnopqrstxyz", 1).zzf("ABCDEFGHJKLMNPQRSTXY", 1).zzf("3456789", 1).zzlK();
    public static final PasswordSpecification zzSu = new zza().zzg(12, 16).zzbD("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890").zzf("abcdefghijklmnopqrstuvwxyz", 1).zzf("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1).zzf("1234567890", 1).zzlK();
    final int mVersionCode;
    private final int[] zzSA = zzlJ();
    final String zzSv;
    final List<String> zzSw;
    final List<Integer> zzSx;
    final int zzSy;
    final int zzSz;
    private final Random zzts = new SecureRandom();

    public static class zza {
        private final TreeSet<Character> zzSB = new TreeSet<>();
        private final List<String> zzSw = new ArrayList();
        private final List<Integer> zzSx = new ArrayList();
        private int zzSy = 12;
        private int zzSz = 16;

        private void zzlL() {
            int i = 0;
            for (Integer intValue : this.zzSx) {
                i += intValue.intValue();
            }
            if (i > this.zzSz) {
                throw new zzb("required character count cannot be greater than the max password size");
            }
        }

        private void zzlM() {
            boolean[] zArr = new boolean[95];
            for (String charArray : this.zzSw) {
                char[] charArray2 = charArray.toCharArray();
                int length = charArray2.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        char c = charArray2[i];
                        int i2 = c - ' ';
                        if (!zArr[i2]) {
                            zArr[i2] = true;
                            i++;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("character ");
                            sb.append(c);
                            sb.append(" occurs in more than one required character set");
                            throw new zzb(sb.toString());
                        }
                    }
                }
            }
        }

        private TreeSet<Character> zzr(String str, String str2) {
            if (!TextUtils.isEmpty(str)) {
                TreeSet<Character> treeSet = new TreeSet<>();
                char[] charArray = str.toCharArray();
                int length = charArray.length;
                int i = 0;
                while (i < length) {
                    char c = charArray[i];
                    if (!PasswordSpecification.zzb(c, 32, 126)) {
                        treeSet.add(Character.valueOf(c));
                        i++;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(" must only contain ASCII printable characters");
                        throw new zzb(sb.toString());
                    }
                }
                return treeSet;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(" cannot be null or empty");
            throw new zzb(sb2.toString());
        }

        public zza zzbD(String str) {
            this.zzSB.addAll(zzr(str, "allowedChars"));
            return this;
        }

        public zza zzf(String str, int i) {
            if (i >= 1) {
                this.zzSw.add(PasswordSpecification.zzb(zzr(str, "requiredChars")));
                this.zzSx.add(Integer.valueOf(i));
                return this;
            }
            throw new zzb("count must be at least 1");
        }

        public zza zzg(int i, int i2) {
            if (i < 1) {
                throw new zzb("minimumSize must be at least 1");
            } else if (i <= i2) {
                this.zzSy = i;
                this.zzSz = i2;
                return this;
            } else {
                throw new zzb("maximumSize must be greater than or equal to minimumSize");
            }
        }

        public PasswordSpecification zzlK() {
            if (!this.zzSB.isEmpty()) {
                zzlL();
                zzlM();
                PasswordSpecification passwordSpecification = new PasswordSpecification(1, PasswordSpecification.zzb(this.zzSB), this.zzSw, this.zzSx, this.zzSy, this.zzSz);
                return passwordSpecification;
            }
            throw new zzb("no allowed characters specified");
        }
    }

    public static class zzb extends Error {
        public zzb(String str) {
            super(str);
        }
    }

    PasswordSpecification(int i, String str, List<String> list, List<Integer> list2, int i2, int i3) {
        this.mVersionCode = i;
        this.zzSv = str;
        this.zzSw = Collections.unmodifiableList(list);
        this.zzSx = Collections.unmodifiableList(list2);
        this.zzSy = i2;
        this.zzSz = i3;
    }

    private int zza(char c) {
        return c - ' ';
    }

    /* access modifiers changed from: private */
    public static String zzb(Collection<Character> collection) {
        char[] cArr = new char[collection.size()];
        int i = 0;
        for (Character charValue : collection) {
            int i2 = i + 1;
            cArr[i] = charValue.charValue();
            i = i2;
        }
        return new String(cArr);
    }

    /* access modifiers changed from: private */
    public static boolean zzb(int i, int i2, int i3) {
        return i < i2 || i > i3;
    }

    private int[] zzlJ() {
        int[] iArr = new int[95];
        Arrays.fill(iArr, -1);
        int i = 0;
        for (String charArray : this.zzSw) {
            for (char zza2 : charArray.toCharArray()) {
                iArr[zza(zza2)] = i;
            }
            i++;
        }
        return iArr;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }
}
