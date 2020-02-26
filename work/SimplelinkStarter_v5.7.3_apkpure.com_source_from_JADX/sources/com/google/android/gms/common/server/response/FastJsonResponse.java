package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.internal.zzmk;
import com.google.android.gms.internal.zzmu;
import com.google.android.gms.internal.zzmv;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class FastJsonResponse {

    public static class Field<I, O> implements SafeParcelable {
        public static final zza CREATOR = new zza();
        private final int mVersionCode;
        protected final int zzagU;
        protected final boolean zzagV;
        protected final int zzagW;
        protected final boolean zzagX;
        protected final String zzagY;
        protected final int zzagZ;
        protected final Class<? extends FastJsonResponse> zzaha;
        protected final String zzahb;
        private FieldMappingDictionary zzahc;
        /* access modifiers changed from: private */
        public zza<I, O> zzahd;

        Field(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, ConverterWrapper converterWrapper) {
            this.mVersionCode = i;
            this.zzagU = i2;
            this.zzagV = z;
            this.zzagW = i3;
            this.zzagX = z2;
            this.zzagY = str;
            this.zzagZ = i4;
            zza<I, O> zza = null;
            if (str2 == null) {
                this.zzaha = null;
                this.zzahb = null;
            } else {
                this.zzaha = SafeParcelResponse.class;
                this.zzahb = str2;
            }
            if (converterWrapper != null) {
                zza = converterWrapper.zzpz();
            }
            this.zzahd = zza;
        }

        protected Field(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends FastJsonResponse> cls, zza<I, O> zza) {
            this.mVersionCode = 1;
            this.zzagU = i;
            this.zzagV = z;
            this.zzagW = i2;
            this.zzagX = z2;
            this.zzagY = str;
            this.zzagZ = i3;
            this.zzaha = cls;
            this.zzahb = cls == null ? null : cls.getCanonicalName();
            this.zzahd = zza;
        }

        public static Field zza(String str, int i, zza<?, ?> zza, boolean z) {
            Field field = new Field(zza.zzpB(), z, zza.zzpC(), false, str, i, null, zza);
            return field;
        }

        public static <T extends FastJsonResponse> Field<T, T> zza(String str, int i, Class<T> cls) {
            Field field = new Field(11, false, 11, false, str, i, cls, null);
            return field;
        }

        public static <T extends FastJsonResponse> Field<ArrayList<T>, ArrayList<T>> zzb(String str, int i, Class<T> cls) {
            Field field = new Field(11, true, 11, true, str, i, cls, null);
            return field;
        }

        public static Field<Integer, Integer> zzj(String str, int i) {
            Field field = new Field(0, false, 0, false, str, i, null, null);
            return field;
        }

        public static Field<Double, Double> zzk(String str, int i) {
            Field field = new Field(4, false, 4, false, str, i, null, null);
            return field;
        }

        public static Field<Boolean, Boolean> zzl(String str, int i) {
            Field field = new Field(6, false, 6, false, str, i, null, null);
            return field;
        }

        public static Field<String, String> zzm(String str, int i) {
            Field field = new Field(7, false, 7, false, str, i, null, null);
            return field;
        }

        public static Field<ArrayList<String>, ArrayList<String>> zzn(String str, int i) {
            Field field = new Field(7, true, 7, true, str, i, null, null);
            return field;
        }

        public I convertBack(O o) {
            return this.zzahd.convertBack(o);
        }

        public int describeContents() {
            zza zza = CREATOR;
            return 0;
        }

        public int getVersionCode() {
            return this.mVersionCode;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Field\n");
            sb.append("            versionCode=");
            sb.append(this.mVersionCode);
            sb.append(10);
            sb.append("                 typeIn=");
            sb.append(this.zzagU);
            sb.append(10);
            sb.append("            typeInArray=");
            sb.append(this.zzagV);
            sb.append(10);
            sb.append("                typeOut=");
            sb.append(this.zzagW);
            sb.append(10);
            sb.append("           typeOutArray=");
            sb.append(this.zzagX);
            sb.append(10);
            sb.append("        outputFieldName=");
            sb.append(this.zzagY);
            sb.append(10);
            sb.append("      safeParcelFieldId=");
            sb.append(this.zzagZ);
            sb.append(10);
            sb.append("       concreteTypeName=");
            sb.append(zzpM());
            sb.append(10);
            if (zzpL() != null) {
                sb.append("     concreteType.class=");
                sb.append(zzpL().getCanonicalName());
                sb.append(10);
            }
            sb.append("          converterName=");
            zza<I, O> zza = this.zzahd;
            sb.append(zza == null ? "null" : zza.getClass().getCanonicalName());
            sb.append(10);
            return sb.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            zza zza = CREATOR;
            zza.zza(this, parcel, i);
        }

        public void zza(FieldMappingDictionary fieldMappingDictionary) {
            this.zzahc = fieldMappingDictionary;
        }

        public int zzpB() {
            return this.zzagU;
        }

        public int zzpC() {
            return this.zzagW;
        }

        public Field<I, O> zzpG() {
            Field field = new Field(this.mVersionCode, this.zzagU, this.zzagV, this.zzagW, this.zzagX, this.zzagY, this.zzagZ, this.zzahb, zzpO());
            return field;
        }

        public boolean zzpH() {
            return this.zzagV;
        }

        public boolean zzpI() {
            return this.zzagX;
        }

        public String zzpJ() {
            return this.zzagY;
        }

        public int zzpK() {
            return this.zzagZ;
        }

        public Class<? extends FastJsonResponse> zzpL() {
            return this.zzaha;
        }

        /* access modifiers changed from: 0000 */
        public String zzpM() {
            String str = this.zzahb;
            if (str == null) {
                return null;
            }
            return str;
        }

        public boolean zzpN() {
            return this.zzahd != null;
        }

        /* access modifiers changed from: 0000 */
        public ConverterWrapper zzpO() {
            zza<I, O> zza = this.zzahd;
            if (zza == null) {
                return null;
            }
            return ConverterWrapper.zza(zza);
        }

        public Map<String, Field<?, ?>> zzpP() {
            zzx.zzw(this.zzahb);
            zzx.zzw(this.zzahc);
            return this.zzahc.zzcw(this.zzahb);
        }
    }

    public interface zza<I, O> {
        I convertBack(O o);

        int zzpB();

        int zzpC();
    }

    private void zza(StringBuilder sb, Field field, Object obj) {
        String str;
        if (field.zzpB() == 11) {
            str = ((FastJsonResponse) field.zzpL().cast(obj)).toString();
        } else if (field.zzpB() == 7) {
            str = "\"";
            sb.append(str);
            sb.append(zzmu.zzcz((String) obj));
        } else {
            sb.append(obj);
            return;
        }
        sb.append(str);
    }

    private void zza(StringBuilder sb, Field field, ArrayList<Object> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                zza(sb, field, obj);
            }
        }
        sb.append("]");
    }

    public String toString() {
        String str;
        Map zzpD = zzpD();
        StringBuilder sb = new StringBuilder(100);
        for (String str2 : zzpD.keySet()) {
            Field field = (Field) zzpD.get(str2);
            if (zza(field)) {
                Object zza2 = zza(field, zzb(field));
                sb.append(sb.length() == 0 ? "{" : ",");
                String str3 = "\"";
                sb.append(str3);
                sb.append(str2);
                sb.append("\":");
                if (zza2 == null) {
                    sb.append("null");
                } else {
                    switch (field.zzpC()) {
                        case 8:
                            sb.append(str3);
                            str = zzmk.zzi((byte[]) zza2);
                            break;
                        case 9:
                            sb.append(str3);
                            str = zzmk.zzj((byte[]) zza2);
                            break;
                        case 10:
                            zzmv.zza(sb, (HashMap) zza2);
                            continue;
                        default:
                            if (!field.zzpH()) {
                                zza(sb, field, zza2);
                                break;
                            } else {
                                zza(sb, field, (ArrayList) zza2);
                                continue;
                            }
                    }
                    sb.append(str);
                    sb.append(str3);
                }
            }
        }
        sb.append(sb.length() > 0 ? "}" : "{}");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public <O, I> I zza(Field<I, O> field, Object obj) {
        return field.zzahd != null ? field.convertBack(obj) : obj;
    }

    /* access modifiers changed from: protected */
    public boolean zza(Field field) {
        if (field.zzpC() != 11) {
            return zzct(field.zzpJ());
        }
        boolean zzpI = field.zzpI();
        String zzpJ = field.zzpJ();
        return zzpI ? zzcv(zzpJ) : zzcu(zzpJ);
    }

    /* access modifiers changed from: protected */
    public Object zzb(Field field) {
        String zzpJ = field.zzpJ();
        if (field.zzpL() == null) {
            return zzcs(field.zzpJ());
        }
        zzx.zza(zzcs(field.zzpJ()) == null, "Concrete field shouldn't be value object: %s", field.zzpJ());
        HashMap zzpF = field.zzpI() ? zzpF() : zzpE();
        if (zzpF != null) {
            return zzpF.get(zzpJ);
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("get");
            sb.append(Character.toUpperCase(zzpJ.charAt(0)));
            sb.append(zzpJ.substring(1));
            return getClass().getMethod(sb.toString(), new Class[0]).invoke(this, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public abstract Object zzcs(String str);

    /* access modifiers changed from: protected */
    public abstract boolean zzct(String str);

    /* access modifiers changed from: protected */
    public boolean zzcu(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    /* access modifiers changed from: protected */
    public boolean zzcv(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    public abstract Map<String, Field<?, ?>> zzpD();

    public HashMap<String, Object> zzpE() {
        return null;
    }

    public HashMap<String, Object> zzpF() {
        return null;
    }
}
