package javax.jmdns.impl.constants;

import android.support.p000v4.internal.view.SupportMenu;

public enum DNSResultCode {
    Unknown(r1, SupportMenu.USER_MASK),
    NoError("No Error", 0),
    FormErr("Format Error", 1),
    ServFail("Server Failure", 2),
    NXDomain("Non-Existent Domain", 3),
    NotImp("Not Implemented", 4),
    Refused("Query Refused", 5),
    YXDomain("Name Exists when it should not", 6),
    YXRRSet("RR Set Exists when it should not", 7),
    NXRRSet("RR Set that should exist does not", 8),
    NotAuth("Server Not Authoritative for zone", 9),
    NotZone("NotZone Name not contained in zone", 10);
    
    static final int ExtendedRCode_MASK = 255;
    static final int RCode_MASK = 15;
    private final String _externalName;
    private final int _index;

    private DNSResultCode(String str, int i) {
        this._externalName = str;
        this._index = i;
    }

    public String externalName() {
        return this._externalName;
    }

    public int indexValue() {
        return this._index;
    }

    public static DNSResultCode resultCodeForFlags(int i) {
        DNSResultCode[] values;
        int i2 = i & 15;
        for (DNSResultCode dNSResultCode : values()) {
            if (dNSResultCode._index == i2) {
                return dNSResultCode;
            }
        }
        return Unknown;
    }

    public static DNSResultCode resultCodeForFlags(int i, int i2) {
        DNSResultCode[] values;
        int i3 = (i & 15) | ((i2 >> 28) & 255);
        for (DNSResultCode dNSResultCode : values()) {
            if (dNSResultCode._index == i3) {
                return dNSResultCode;
            }
        }
        return Unknown;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name());
        sb.append(" index ");
        sb.append(indexValue());
        return sb.toString();
    }
}
