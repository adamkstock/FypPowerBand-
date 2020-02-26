package com.p004ti.wifi.utils;

/* renamed from: com.ti.wifi.utils.SecurityType */
public enum SecurityType {
    OPEN,
    WPA1,
    WEP,
    WPA2,
    UNKNOWN;

    /* renamed from: com.ti.wifi.utils.SecurityType$1 */
    static /* synthetic */ class C10391 {
        static final /* synthetic */ int[] $SwitchMap$com$ti$wifi$utils$SecurityType = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                com.ti.wifi.utils.SecurityType[] r0 = com.p004ti.wifi.utils.SecurityType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$ti$wifi$utils$SecurityType = r0
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.ti.wifi.utils.SecurityType r1 = com.p004ti.wifi.utils.SecurityType.OPEN     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x001f }
                com.ti.wifi.utils.SecurityType r1 = com.p004ti.wifi.utils.SecurityType.WEP     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x002a }
                com.ti.wifi.utils.SecurityType r1 = com.p004ti.wifi.utils.SecurityType.WPA1     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.ti.wifi.utils.SecurityType r1 = com.p004ti.wifi.utils.SecurityType.WPA2     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.ti.wifi.utils.SecurityType r1 = com.p004ti.wifi.utils.SecurityType.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.utils.SecurityType.C10391.<clinit>():void");
        }
    }

    public static int getIntValue(SecurityType securityType) {
        int i = C10391.$SwitchMap$com$ti$wifi$utils$SecurityType[securityType.ordinal()];
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 2;
        }
        if (i != 4) {
            return i != 5 ? 0 : 4;
        }
        return 3;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.p004ti.wifi.utils.SecurityType parseString(java.lang.String r5) {
        /*
            com.ti.wifi.utils.SecurityType r0 = UNKNOWN
            int r1 = r5.hashCode()
            r2 = 3
            r3 = 2
            r4 = 1
            switch(r1) {
                case 48: goto L_0x002b;
                case 49: goto L_0x0021;
                case 50: goto L_0x0017;
                case 51: goto L_0x000d;
                default: goto L_0x000c;
            }
        L_0x000c:
            goto L_0x0035
        L_0x000d:
            java.lang.String r1 = "3"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x0035
            r5 = 3
            goto L_0x0036
        L_0x0017:
            java.lang.String r1 = "2"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x0035
            r5 = 2
            goto L_0x0036
        L_0x0021:
            java.lang.String r1 = "1"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x0035
            r5 = 1
            goto L_0x0036
        L_0x002b:
            java.lang.String r1 = "0"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x0035
            r5 = 0
            goto L_0x0036
        L_0x0035:
            r5 = -1
        L_0x0036:
            if (r5 == 0) goto L_0x0048
            if (r5 == r4) goto L_0x0045
            if (r5 == r3) goto L_0x0042
            if (r5 == r2) goto L_0x003f
            goto L_0x004a
        L_0x003f:
            com.ti.wifi.utils.SecurityType r0 = WPA2
            goto L_0x004a
        L_0x0042:
            com.ti.wifi.utils.SecurityType r0 = WPA1
            goto L_0x004a
        L_0x0045:
            com.ti.wifi.utils.SecurityType r0 = WEP
            goto L_0x004a
        L_0x0048:
            com.ti.wifi.utils.SecurityType r0 = OPEN
        L_0x004a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.utils.SecurityType.parseString(java.lang.String):com.ti.wifi.utils.SecurityType");
    }
}
