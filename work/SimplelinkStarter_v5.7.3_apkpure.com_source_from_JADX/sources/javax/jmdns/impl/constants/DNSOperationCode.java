package javax.jmdns.impl.constants;

public enum DNSOperationCode {
    Query(r1, 0),
    IQuery("Inverse Query", 1),
    Status(r3, 2),
    Unassigned(r3, 3),
    Notify(r3, 4),
    Update(r3, 5);
    
    static final int OpCode_MASK = 30720;
    private final String _externalName;
    private final int _index;

    private DNSOperationCode(String str, int i) {
        this._externalName = str;
        this._index = i;
    }

    public String externalName() {
        return this._externalName;
    }

    public int indexValue() {
        return this._index;
    }

    public static DNSOperationCode operationCodeForFlags(int i) {
        DNSOperationCode[] values;
        int i2 = (i & 30720) >> 11;
        for (DNSOperationCode dNSOperationCode : values()) {
            if (dNSOperationCode._index == i2) {
                return dNSOperationCode;
            }
        }
        return Unassigned;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name());
        sb.append(" index ");
        sb.append(indexValue());
        return sb.toString();
    }
}
