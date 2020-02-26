package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class zzw {

    public static final class zza {
        private final Object zzJm;
        private final List<String> zzago;

        private zza(Object obj) {
            this.zzJm = zzx.zzw(obj);
            this.zzago = new ArrayList();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(100);
            sb.append(this.zzJm.getClass().getSimpleName());
            sb.append('{');
            int size = this.zzago.size();
            for (int i = 0; i < size; i++) {
                sb.append((String) this.zzago.get(i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append('}');
            return sb.toString();
        }

        public zza zzg(String str, Object obj) {
            List<String> list = this.zzago;
            StringBuilder sb = new StringBuilder();
            sb.append((String) zzx.zzw(str));
            sb.append("=");
            sb.append(String.valueOf(obj));
            list.add(sb.toString());
            return this;
        }
    }

    public static boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static zza zzv(Object obj) {
        return new zza(obj);
    }
}
