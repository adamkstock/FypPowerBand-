package javax.jmdns.impl.constants;

import java.util.logging.Level;
import java.util.logging.Logger;

public enum DNSRecordClass {
    CLASS_UNKNOWN("?", 0),
    CLASS_IN("in", 1),
    CLASS_CS("cs", 2),
    CLASS_CH("ch", 3),
    CLASS_HS("hs", 4),
    CLASS_NONE("none", 254),
    CLASS_ANY("any", 255);
    
    public static final int CLASS_MASK = 32767;
    public static final int CLASS_UNIQUE = 32768;
    public static final boolean NOT_UNIQUE = false;
    public static final boolean UNIQUE = true;
    private static Logger logger;
    private final String _externalName;
    private final int _index;

    static {
        logger = Logger.getLogger(DNSRecordClass.class.getName());
    }

    private DNSRecordClass(String str, int i) {
        this._externalName = str;
        this._index = i;
    }

    public String externalName() {
        return this._externalName;
    }

    public int indexValue() {
        return this._index;
    }

    public boolean isUnique(int i) {
        return (this == CLASS_UNKNOWN || (i & 32768) == 0) ? false : true;
    }

    public static DNSRecordClass classForName(String str) {
        DNSRecordClass[] values;
        if (str != null) {
            String lowerCase = str.toLowerCase();
            for (DNSRecordClass dNSRecordClass : values()) {
                if (dNSRecordClass._externalName.equals(lowerCase)) {
                    return dNSRecordClass;
                }
            }
        }
        Logger logger2 = logger;
        Level level = Level.WARNING;
        StringBuilder sb = new StringBuilder();
        sb.append("Could not find record class for name: ");
        sb.append(str);
        logger2.log(level, sb.toString());
        return CLASS_UNKNOWN;
    }

    public static DNSRecordClass classForIndex(int i) {
        DNSRecordClass[] values;
        int i2 = i & CLASS_MASK;
        for (DNSRecordClass dNSRecordClass : values()) {
            if (dNSRecordClass._index == i2) {
                return dNSRecordClass;
            }
        }
        Logger logger2 = logger;
        Level level = Level.WARNING;
        StringBuilder sb = new StringBuilder();
        sb.append("Could not find record class for index: ");
        sb.append(i);
        logger2.log(level, sb.toString());
        return CLASS_UNKNOWN;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name());
        sb.append(" index ");
        sb.append(indexValue());
        return sb.toString();
    }
}
