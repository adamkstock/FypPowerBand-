package com.p004ti.wifi.utils;

/* renamed from: com.ti.wifi.utils.DeviceVersion */
public enum DeviceVersion {
    UNKNOWN,
    R1,
    R2;

    /* renamed from: com.ti.wifi.utils.DeviceVersion$1 */
    static /* synthetic */ class C10291 {
        static final /* synthetic */ int[] $SwitchMap$com$ti$wifi$utils$DeviceVersion = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                com.ti.wifi.utils.DeviceVersion[] r0 = com.p004ti.wifi.utils.DeviceVersion.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$ti$wifi$utils$DeviceVersion = r0
                int[] r0 = $SwitchMap$com$ti$wifi$utils$DeviceVersion     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.ti.wifi.utils.DeviceVersion r1 = com.p004ti.wifi.utils.DeviceVersion.R1     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$DeviceVersion     // Catch:{ NoSuchFieldError -> 0x001f }
                com.ti.wifi.utils.DeviceVersion r1 = com.p004ti.wifi.utils.DeviceVersion.R2     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$DeviceVersion     // Catch:{ NoSuchFieldError -> 0x002a }
                com.ti.wifi.utils.DeviceVersion r1 = com.p004ti.wifi.utils.DeviceVersion.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.utils.DeviceVersion.C10291.<clinit>():void");
        }
    }

    public static String getStringFor(DeviceVersion deviceVersion) {
        int i = C10291.$SwitchMap$com$ti$wifi$utils$DeviceVersion[deviceVersion.ordinal()];
        if (i == 1) {
            return "R1.0";
        }
        if (i != 2) {
            return i != 3 ? "" : "UNKNOWN";
        }
        return "R2.0";
    }
}
