package javax.jmdns.impl.constants;

import android.support.p000v4.internal.view.SupportMenu;

public enum DNSOptionCode {
    Unknown(r2, SupportMenu.USER_MASK),
    LLQ(r2, 1),
    f88UL(r2, 2),
    NSID(r2, 3),
    Owner(r2, 4);
    
    private final String _externalName;
    private final int _index;

    private DNSOptionCode(String str, int i) {
        this._externalName = str;
        this._index = i;
    }

    public String externalName() {
        return this._externalName;
    }

    public int indexValue() {
        return this._index;
    }

    public static DNSOptionCode resultCodeForFlags(int i) {
        DNSOptionCode[] values;
        for (DNSOptionCode dNSOptionCode : values()) {
            if (dNSOptionCode._index == i) {
                return dNSOptionCode;
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
