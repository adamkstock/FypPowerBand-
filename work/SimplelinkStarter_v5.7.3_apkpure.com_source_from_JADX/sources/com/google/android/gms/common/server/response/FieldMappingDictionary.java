package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FieldMappingDictionary implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    private final int mVersionCode;
    private final HashMap<String, Map<String, Field<?, ?>>> zzahe;
    private final ArrayList<Entry> zzahf;
    private final String zzahg;

    public static class Entry implements SafeParcelable {
        public static final zzd CREATOR = new zzd();
        final String className;
        final int versionCode;
        final ArrayList<FieldMapPair> zzahh;

        Entry(int i, String str, ArrayList<FieldMapPair> arrayList) {
            this.versionCode = i;
            this.className = str;
            this.zzahh = arrayList;
        }

        Entry(String str, Map<String, Field<?, ?>> map) {
            this.versionCode = 1;
            this.className = str;
            this.zzahh = zzF(map);
        }

        private static ArrayList<FieldMapPair> zzF(Map<String, Field<?, ?>> map) {
            if (map == null) {
                return null;
            }
            ArrayList<FieldMapPair> arrayList = new ArrayList<>();
            for (String str : map.keySet()) {
                arrayList.add(new FieldMapPair(str, (Field) map.get(str)));
            }
            return arrayList;
        }

        public int describeContents() {
            zzd zzd = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            zzd zzd = CREATOR;
            zzd.zza(this, parcel, i);
        }

        /* access modifiers changed from: 0000 */
        public HashMap<String, Field<?, ?>> zzpU() {
            HashMap<String, Field<?, ?>> hashMap = new HashMap<>();
            int size = this.zzahh.size();
            for (int i = 0; i < size; i++) {
                FieldMapPair fieldMapPair = (FieldMapPair) this.zzahh.get(i);
                hashMap.put(fieldMapPair.key, fieldMapPair.zzahi);
            }
            return hashMap;
        }
    }

    public static class FieldMapPair implements SafeParcelable {
        public static final zzb CREATOR = new zzb();
        final String key;
        final int versionCode;
        final Field<?, ?> zzahi;

        FieldMapPair(int i, String str, Field<?, ?> field) {
            this.versionCode = i;
            this.key = str;
            this.zzahi = field;
        }

        FieldMapPair(String str, Field<?, ?> field) {
            this.versionCode = 1;
            this.key = str;
            this.zzahi = field;
        }

        public int describeContents() {
            zzb zzb = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            zzb zzb = CREATOR;
            zzb.zza(this, parcel, i);
        }
    }

    FieldMappingDictionary(int i, ArrayList<Entry> arrayList, String str) {
        this.mVersionCode = i;
        this.zzahf = null;
        this.zzahe = zzc(arrayList);
        this.zzahg = (String) zzx.zzw(str);
        zzpQ();
    }

    public FieldMappingDictionary(Class<? extends FastJsonResponse> cls) {
        this.mVersionCode = 1;
        this.zzahf = null;
        this.zzahe = new HashMap<>();
        this.zzahg = cls.getCanonicalName();
    }

    private static HashMap<String, Map<String, Field<?, ?>>> zzc(ArrayList<Entry> arrayList) {
        HashMap<String, Map<String, Field<?, ?>>> hashMap = new HashMap<>();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Entry entry = (Entry) arrayList.get(i);
            hashMap.put(entry.className, entry.zzpU());
        }
        return hashMap;
    }

    public int describeContents() {
        zzc zzc = CREATOR;
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getVersionCode() {
        return this.mVersionCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String str : this.zzahe.keySet()) {
            sb.append(str);
            sb.append(":\n");
            Map map = (Map) this.zzahe.get(str);
            for (String str2 : map.keySet()) {
                sb.append("  ");
                sb.append(str2);
                sb.append(": ");
                sb.append(map.get(str2));
            }
        }
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc zzc = CREATOR;
        zzc.zza(this, parcel, i);
    }

    public void zza(Class<? extends FastJsonResponse> cls, Map<String, Field<?, ?>> map) {
        this.zzahe.put(cls.getCanonicalName(), map);
    }

    public boolean zzb(Class<? extends FastJsonResponse> cls) {
        return this.zzahe.containsKey(cls.getCanonicalName());
    }

    public Map<String, Field<?, ?>> zzcw(String str) {
        return (Map) this.zzahe.get(str);
    }

    public void zzpQ() {
        for (String str : this.zzahe.keySet()) {
            Map map = (Map) this.zzahe.get(str);
            for (String str2 : map.keySet()) {
                ((Field) map.get(str2)).zza(this);
            }
        }
    }

    public void zzpR() {
        for (String str : this.zzahe.keySet()) {
            Map map = (Map) this.zzahe.get(str);
            HashMap hashMap = new HashMap();
            for (String str2 : map.keySet()) {
                hashMap.put(str2, ((Field) map.get(str2)).zzpG());
            }
            this.zzahe.put(str, hashMap);
        }
    }

    /* access modifiers changed from: 0000 */
    public ArrayList<Entry> zzpS() {
        ArrayList<Entry> arrayList = new ArrayList<>();
        for (String str : this.zzahe.keySet()) {
            arrayList.add(new Entry(str, (Map) this.zzahe.get(str)));
        }
        return arrayList;
    }

    public String zzpT() {
        return this.zzahg;
    }
}
