package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zza.C1121zza;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.google.android.gms.internal.zzmk;
import com.google.android.gms.internal.zzmu;
import com.google.android.gms.internal.zzmv;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SafeParcelResponse extends FastJsonResponse implements SafeParcelable {
    public static final zze CREATOR = new zze();
    private final String mClassName;
    private final int mVersionCode;
    private final FieldMappingDictionary zzahc;
    private final Parcel zzahj;
    private final int zzahk;
    private int zzahl;
    private int zzahm;

    SafeParcelResponse(int i, Parcel parcel, FieldMappingDictionary fieldMappingDictionary) {
        this.mVersionCode = i;
        this.zzahj = (Parcel) zzx.zzw(parcel);
        this.zzahk = 2;
        this.zzahc = fieldMappingDictionary;
        FieldMappingDictionary fieldMappingDictionary2 = this.zzahc;
        this.mClassName = fieldMappingDictionary2 == null ? null : fieldMappingDictionary2.zzpT();
        this.zzahl = 2;
    }

    private SafeParcelResponse(SafeParcelable safeParcelable, FieldMappingDictionary fieldMappingDictionary, String str) {
        this.mVersionCode = 1;
        this.zzahj = Parcel.obtain();
        safeParcelable.writeToParcel(this.zzahj, 0);
        this.zzahk = 1;
        this.zzahc = (FieldMappingDictionary) zzx.zzw(fieldMappingDictionary);
        this.mClassName = (String) zzx.zzw(str);
        this.zzahl = 2;
    }

    private static HashMap<Integer, Entry<String, Field<?, ?>>> zzG(Map<String, Field<?, ?>> map) {
        HashMap<Integer, Entry<String, Field<?, ?>>> hashMap = new HashMap<>();
        for (Entry entry : map.entrySet()) {
            hashMap.put(Integer.valueOf(((Field) entry.getValue()).zzpK()), entry);
        }
        return hashMap;
    }

    public static <T extends FastJsonResponse & SafeParcelable> SafeParcelResponse zza(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        return new SafeParcelResponse((SafeParcelable) t, zzb(t), canonicalName);
    }

    private static void zza(FieldMappingDictionary fieldMappingDictionary, FastJsonResponse fastJsonResponse) {
        Class cls = fastJsonResponse.getClass();
        if (!fieldMappingDictionary.zzb(cls)) {
            Map zzpD = fastJsonResponse.zzpD();
            fieldMappingDictionary.zza(cls, zzpD);
            for (String str : zzpD.keySet()) {
                Field field = (Field) zzpD.get(str);
                Class zzpL = field.zzpL();
                if (zzpL != null) {
                    try {
                        zza(fieldMappingDictionary, (FastJsonResponse) zzpL.newInstance());
                    } catch (InstantiationException e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Could not instantiate an object of type ");
                        sb.append(field.zzpL().getCanonicalName());
                        throw new IllegalStateException(sb.toString(), e);
                    } catch (IllegalAccessException e2) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Could not access object of type ");
                        sb2.append(field.zzpL().getCanonicalName());
                        throw new IllegalStateException(sb2.toString(), e2);
                    }
                }
            }
        }
    }

    private void zza(StringBuilder sb, int i, Object obj) {
        String str;
        String str2 = "\"";
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append(obj);
                return;
            case 7:
                sb.append(str2);
                str = zzmu.zzcz(obj.toString());
                break;
            case 8:
                sb.append(str2);
                str = zzmk.zzi((byte[]) obj);
                break;
            case 9:
                sb.append(str2);
                str = zzmk.zzj((byte[]) obj);
                break;
            case 10:
                zzmv.zza(sb, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown type = ");
                sb2.append(i);
                throw new IllegalArgumentException(sb2.toString());
        }
        sb.append(str);
        sb.append(str2);
    }

    private void zza(StringBuilder sb, Field<?, ?> field, Parcel parcel, int i) {
        Object obj;
        switch (field.zzpC()) {
            case 0:
                obj = Integer.valueOf(zza.zzg(parcel, i));
                break;
            case 1:
                obj = zza.zzk(parcel, i);
                break;
            case 2:
                obj = Long.valueOf(zza.zzi(parcel, i));
                break;
            case 3:
                obj = Float.valueOf(zza.zzl(parcel, i));
                break;
            case 4:
                obj = Double.valueOf(zza.zzn(parcel, i));
                break;
            case 5:
                obj = zza.zzo(parcel, i);
                break;
            case 6:
                obj = Boolean.valueOf(zza.zzc(parcel, i));
                break;
            case 7:
                obj = zza.zzp(parcel, i);
                break;
            case 8:
            case 9:
                obj = zza.zzs(parcel, i);
                break;
            case 10:
                obj = zzi(zza.zzr(parcel, i));
                break;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown field out type = ");
                sb2.append(field.zzpC());
                throw new IllegalArgumentException(sb2.toString());
        }
        zzb(sb, field, zza(field, obj));
    }

    private void zza(StringBuilder sb, String str, Field<?, ?> field, Parcel parcel, int i) {
        sb.append("\"");
        sb.append(str);
        sb.append("\":");
        if (field.zzpN()) {
            zza(sb, field, parcel, i);
        } else {
            zzb(sb, field, parcel, i);
        }
    }

    private void zza(StringBuilder sb, Map<String, Field<?, ?>> map, Parcel parcel) {
        HashMap zzG = zzG(map);
        sb.append('{');
        int zzap = zza.zzap(parcel);
        boolean z = false;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            Entry entry = (Entry) zzG.get(Integer.valueOf(zza.zzbM(zzao)));
            if (entry != null) {
                if (z) {
                    sb.append(",");
                }
                zza(sb, (String) entry.getKey(), (Field) entry.getValue(), parcel, zzao);
                z = true;
            }
        }
        if (parcel.dataPosition() == zzap) {
            sb.append('}');
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Overread allowed size end=");
        sb2.append(zzap);
        throw new C1121zza(sb2.toString(), parcel);
    }

    private static FieldMappingDictionary zzb(FastJsonResponse fastJsonResponse) {
        FieldMappingDictionary fieldMappingDictionary = new FieldMappingDictionary(fastJsonResponse.getClass());
        zza(fieldMappingDictionary, fastJsonResponse);
        fieldMappingDictionary.zzpR();
        fieldMappingDictionary.zzpQ();
        return fieldMappingDictionary;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0117, code lost:
        r6.append(r7);
        r6.append(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0147, code lost:
        r6.append(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zzb(java.lang.StringBuilder r6, com.google.android.gms.common.server.response.FastJsonResponse.Field<?, ?> r7, android.os.Parcel r8, int r9) {
        /*
            r5 = this;
            boolean r0 = r7.zzpI()
            java.lang.String r1 = ","
            r2 = 0
            if (r0 == 0) goto L_0x0089
            java.lang.String r0 = "["
            r6.append(r0)
            int r0 = r7.zzpC()
            switch(r0) {
                case 0: goto L_0x007b;
                case 1: goto L_0x0073;
                case 2: goto L_0x006b;
                case 3: goto L_0x0063;
                case 4: goto L_0x005b;
                case 5: goto L_0x0053;
                case 6: goto L_0x004b;
                case 7: goto L_0x0043;
                case 8: goto L_0x003b;
                case 9: goto L_0x003b;
                case 10: goto L_0x003b;
                case 11: goto L_0x001d;
                default: goto L_0x0015;
            }
        L_0x0015:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "Unknown field type out."
            r6.<init>(r7)
            throw r6
        L_0x001d:
            android.os.Parcel[] r8 = com.google.android.gms.common.internal.safeparcel.zza.zzF(r8, r9)
            int r9 = r8.length
            r0 = 0
        L_0x0023:
            if (r0 >= r9) goto L_0x0082
            if (r0 <= 0) goto L_0x002a
            r6.append(r1)
        L_0x002a:
            r3 = r8[r0]
            r3.setDataPosition(r2)
            java.util.Map r3 = r7.zzpP()
            r4 = r8[r0]
            r5.zza(r6, r3, r4)
            int r0 = r0 + 1
            goto L_0x0023
        L_0x003b:
            java.lang.UnsupportedOperationException r6 = new java.lang.UnsupportedOperationException
            java.lang.String r7 = "List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported"
            r6.<init>(r7)
            throw r6
        L_0x0043:
            java.lang.String[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzB(r8, r9)
            com.google.android.gms.internal.zzmj.zza(r6, r7)
            goto L_0x0082
        L_0x004b:
            boolean[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzu(r8, r9)
            com.google.android.gms.internal.zzmj.zza(r6, r7)
            goto L_0x0082
        L_0x0053:
            java.math.BigDecimal[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzA(r8, r9)
            com.google.android.gms.internal.zzmj.zza(r6, (T[]) r7)
            goto L_0x0082
        L_0x005b:
            double[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzz(r8, r9)
            com.google.android.gms.internal.zzmj.zza(r6, r7)
            goto L_0x0082
        L_0x0063:
            float[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzy(r8, r9)
            com.google.android.gms.internal.zzmj.zza(r6, r7)
            goto L_0x0082
        L_0x006b:
            long[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzw(r8, r9)
            com.google.android.gms.internal.zzmj.zza(r6, r7)
            goto L_0x0082
        L_0x0073:
            java.math.BigInteger[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzx(r8, r9)
            com.google.android.gms.internal.zzmj.zza(r6, (T[]) r7)
            goto L_0x0082
        L_0x007b:
            int[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzv(r8, r9)
            com.google.android.gms.internal.zzmj.zza(r6, r7)
        L_0x0082:
            java.lang.String r7 = "]"
        L_0x0084:
            r6.append(r7)
            goto L_0x0152
        L_0x0089:
            int r0 = r7.zzpC()
            java.lang.String r3 = "\""
            switch(r0) {
                case 0: goto L_0x014b;
                case 1: goto L_0x0143;
                case 2: goto L_0x013b;
                case 3: goto L_0x0133;
                case 4: goto L_0x012b;
                case 5: goto L_0x0126;
                case 6: goto L_0x011e;
                case 7: goto L_0x010c;
                case 8: goto L_0x0100;
                case 9: goto L_0x00f4;
                case 10: goto L_0x00aa;
                case 11: goto L_0x009a;
                default: goto L_0x0092;
            }
        L_0x0092:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "Unknown field type out"
            r6.<init>(r7)
            throw r6
        L_0x009a:
            android.os.Parcel r8 = com.google.android.gms.common.internal.safeparcel.zza.zzE(r8, r9)
            r8.setDataPosition(r2)
            java.util.Map r7 = r7.zzpP()
            r5.zza(r6, r7, r8)
            goto L_0x0152
        L_0x00aa:
            android.os.Bundle r7 = com.google.android.gms.common.internal.safeparcel.zza.zzr(r8, r9)
            java.util.Set r8 = r7.keySet()
            r8.size()
            java.lang.String r9 = "{"
            r6.append(r9)
            java.util.Iterator r8 = r8.iterator()
            r9 = 1
        L_0x00bf:
            boolean r0 = r8.hasNext()
            if (r0 == 0) goto L_0x00f1
            java.lang.Object r0 = r8.next()
            java.lang.String r0 = (java.lang.String) r0
            if (r9 != 0) goto L_0x00d0
            r6.append(r1)
        L_0x00d0:
            r6.append(r3)
            r6.append(r0)
            r6.append(r3)
            java.lang.String r9 = ":"
            r6.append(r9)
            r6.append(r3)
            java.lang.String r9 = r7.getString(r0)
            java.lang.String r9 = com.google.android.gms.internal.zzmu.zzcz(r9)
            r6.append(r9)
            r6.append(r3)
            r9 = 0
            goto L_0x00bf
        L_0x00f1:
            java.lang.String r7 = "}"
            goto L_0x0084
        L_0x00f4:
            byte[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzs(r8, r9)
            r6.append(r3)
            java.lang.String r7 = com.google.android.gms.internal.zzmk.zzj(r7)
            goto L_0x0117
        L_0x0100:
            byte[] r7 = com.google.android.gms.common.internal.safeparcel.zza.zzs(r8, r9)
            r6.append(r3)
            java.lang.String r7 = com.google.android.gms.internal.zzmk.zzi(r7)
            goto L_0x0117
        L_0x010c:
            java.lang.String r7 = com.google.android.gms.common.internal.safeparcel.zza.zzp(r8, r9)
            r6.append(r3)
            java.lang.String r7 = com.google.android.gms.internal.zzmu.zzcz(r7)
        L_0x0117:
            r6.append(r7)
            r6.append(r3)
            goto L_0x0152
        L_0x011e:
            boolean r7 = com.google.android.gms.common.internal.safeparcel.zza.zzc(r8, r9)
            r6.append(r7)
            goto L_0x0152
        L_0x0126:
            java.math.BigDecimal r7 = com.google.android.gms.common.internal.safeparcel.zza.zzo(r8, r9)
            goto L_0x0147
        L_0x012b:
            double r7 = com.google.android.gms.common.internal.safeparcel.zza.zzn(r8, r9)
            r6.append(r7)
            goto L_0x0152
        L_0x0133:
            float r7 = com.google.android.gms.common.internal.safeparcel.zza.zzl(r8, r9)
            r6.append(r7)
            goto L_0x0152
        L_0x013b:
            long r7 = com.google.android.gms.common.internal.safeparcel.zza.zzi(r8, r9)
            r6.append(r7)
            goto L_0x0152
        L_0x0143:
            java.math.BigInteger r7 = com.google.android.gms.common.internal.safeparcel.zza.zzk(r8, r9)
        L_0x0147:
            r6.append(r7)
            goto L_0x0152
        L_0x014b:
            int r7 = com.google.android.gms.common.internal.safeparcel.zza.zzg(r8, r9)
            r6.append(r7)
        L_0x0152:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.SafeParcelResponse.zzb(java.lang.StringBuilder, com.google.android.gms.common.server.response.FastJsonResponse$Field, android.os.Parcel, int):void");
    }

    private void zzb(StringBuilder sb, Field<?, ?> field, Object obj) {
        if (field.zzpH()) {
            zzb(sb, field, (ArrayList) obj);
        } else {
            zza(sb, field.zzpB(), obj);
        }
    }

    private void zzb(StringBuilder sb, Field<?, ?> field, ArrayList<?> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(",");
            }
            zza(sb, field.zzpB(), arrayList.get(i));
        }
        sb.append("]");
    }

    public static HashMap<String, String> zzi(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    public int describeContents() {
        zze zze = CREATOR;
        return 0;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public String toString() {
        zzx.zzb(this.zzahc, (Object) "Cannot convert to JSON on client side.");
        Parcel zzpV = zzpV();
        zzpV.setDataPosition(0);
        StringBuilder sb = new StringBuilder(100);
        zza(sb, this.zzahc.zzcw(this.mClassName), zzpV);
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze zze = CREATOR;
        zze.zza(this, parcel, i);
    }

    /* access modifiers changed from: protected */
    public Object zzcs(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    /* access modifiers changed from: protected */
    public boolean zzct(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public Map<String, Field<?, ?>> zzpD() {
        FieldMappingDictionary fieldMappingDictionary = this.zzahc;
        if (fieldMappingDictionary == null) {
            return null;
        }
        return fieldMappingDictionary.zzcw(this.mClassName);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0006, code lost:
        if (r0 != 1) goto L_0x001a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.os.Parcel zzpV() {
        /*
            r3 = this;
            int r0 = r3.zzahl
            r1 = 2
            if (r0 == 0) goto L_0x0009
            r2 = 1
            if (r0 == r2) goto L_0x0011
            goto L_0x001a
        L_0x0009:
            android.os.Parcel r0 = r3.zzahj
            int r0 = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(r0)
            r3.zzahm = r0
        L_0x0011:
            android.os.Parcel r0 = r3.zzahj
            int r2 = r3.zzahm
            com.google.android.gms.common.internal.safeparcel.zzb.zzI(r0, r2)
            r3.zzahl = r1
        L_0x001a:
            android.os.Parcel r0 = r3.zzahj
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.SafeParcelResponse.zzpV():android.os.Parcel");
    }

    /* access modifiers changed from: 0000 */
    public FieldMappingDictionary zzpW() {
        int i = this.zzahk;
        if (i == 0) {
            return null;
        }
        if (i == 1) {
            return this.zzahc;
        }
        if (i == 2) {
            return this.zzahc;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid creation type: ");
        sb.append(this.zzahk);
        throw new IllegalStateException(sb.toString());
    }
}
