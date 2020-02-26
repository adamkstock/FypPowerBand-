package com.google.android.gms.appdatasearch;

import java.util.HashMap;
import java.util.Map;

public class zzh {
    private static final String[] zzQD = {"text1", "text2", "icon", "intent_action", "intent_data", "intent_data_id", "intent_extra_data", "suggest_large_icon", "intent_activity"};
    private static final Map<String, Integer> zzQE = new HashMap(zzQD.length);

    static {
        int i = 0;
        while (true) {
            String[] strArr = zzQD;
            if (i < strArr.length) {
                zzQE.put(strArr[i], Integer.valueOf(i));
                i++;
            } else {
                return;
            }
        }
    }

    public static String zzak(int i) {
        if (i >= 0) {
            String[] strArr = zzQD;
            if (i < strArr.length) {
                return strArr[i];
            }
        }
        return null;
    }

    public static int zzbz(String str) {
        Integer num = (Integer) zzQE.get(str);
        if (num != null) {
            return num.intValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(str);
        sb.append("] is not a valid global search section name");
        throw new IllegalArgumentException(sb.toString());
    }

    public static int zzls() {
        return zzQD.length;
    }
}
