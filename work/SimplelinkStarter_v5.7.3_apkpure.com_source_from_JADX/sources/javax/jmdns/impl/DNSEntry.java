package javax.jmdns.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.jmdns.ServiceInfo.Fields;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import org.apache.http.HttpStatus;

public abstract class DNSEntry {
    private final DNSRecordClass _dnsClass;
    private final String _key;
    private final String _name;
    final Map<Fields, String> _qualifiedNameMap = ServiceInfoImpl.decodeQualifiedNameMapForType(getName());
    private final DNSRecordType _recordType;
    private final String _type;
    private final boolean _unique;

    public abstract boolean isExpired(long j);

    public abstract boolean isStale(long j);

    /* access modifiers changed from: protected */
    public void toString(StringBuilder sb) {
    }

    DNSEntry(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
        String str2;
        String str3;
        this._name = str;
        this._recordType = dNSRecordType;
        this._dnsClass = dNSRecordClass;
        this._unique = z;
        String str4 = (String) this._qualifiedNameMap.get(Fields.Domain);
        String str5 = (String) this._qualifiedNameMap.get(Fields.Protocol);
        String str6 = (String) this._qualifiedNameMap.get(Fields.Application);
        String lowerCase = ((String) this._qualifiedNameMap.get(Fields.Instance)).toLowerCase();
        StringBuilder sb = new StringBuilder();
        String str7 = "_";
        String str8 = "";
        String str9 = ".";
        if (str6.length() > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str7);
            sb2.append(str6);
            sb2.append(str9);
            str2 = sb2.toString();
        } else {
            str2 = str8;
        }
        sb.append(str2);
        if (str5.length() > 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str7);
            sb3.append(str5);
            sb3.append(str9);
            str3 = sb3.toString();
        } else {
            str3 = str8;
        }
        sb.append(str3);
        sb.append(str4);
        sb.append(str9);
        this._type = sb.toString();
        StringBuilder sb4 = new StringBuilder();
        if (lowerCase.length() > 0) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(lowerCase);
            sb5.append(str9);
            str8 = sb5.toString();
        }
        sb4.append(str8);
        sb4.append(this._type);
        this._key = sb4.toString().toLowerCase();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DNSEntry)) {
            return false;
        }
        DNSEntry dNSEntry = (DNSEntry) obj;
        if (!getKey().equals(dNSEntry.getKey()) || !getRecordType().equals(dNSEntry.getRecordType()) || getRecordClass() != dNSEntry.getRecordClass()) {
            return false;
        }
        return true;
    }

    public boolean isSameEntry(DNSEntry dNSEntry) {
        return getKey().equals(dNSEntry.getKey()) && matchRecordType(dNSEntry.getRecordType()) && matchRecordClass(dNSEntry.getRecordClass());
    }

    public boolean sameSubtype(DNSEntry dNSEntry) {
        return getSubtype().equals(dNSEntry.getSubtype());
    }

    public boolean matchRecordClass(DNSRecordClass dNSRecordClass) {
        return DNSRecordClass.CLASS_ANY == dNSRecordClass || DNSRecordClass.CLASS_ANY == getRecordClass() || getRecordClass().equals(dNSRecordClass);
    }

    public boolean matchRecordType(DNSRecordType dNSRecordType) {
        return getRecordType().equals(dNSRecordType);
    }

    public String getSubtype() {
        String str = (String) getQualifiedNameMap().get(Fields.Subtype);
        return str != null ? str : "";
    }

    public String getName() {
        String str = this._name;
        return str != null ? str : "";
    }

    public String getType() {
        String str = this._type;
        return str != null ? str : "";
    }

    public String getKey() {
        String str = this._key;
        return str != null ? str : "";
    }

    public DNSRecordType getRecordType() {
        DNSRecordType dNSRecordType = this._recordType;
        return dNSRecordType != null ? dNSRecordType : DNSRecordType.TYPE_IGNORE;
    }

    public DNSRecordClass getRecordClass() {
        DNSRecordClass dNSRecordClass = this._dnsClass;
        return dNSRecordClass != null ? dNSRecordClass : DNSRecordClass.CLASS_UNKNOWN;
    }

    public boolean isUnique() {
        return this._unique;
    }

    public Map<Fields, String> getQualifiedNameMap() {
        return Collections.unmodifiableMap(this._qualifiedNameMap);
    }

    public boolean isServicesDiscoveryMetaQuery() {
        return ((String) this._qualifiedNameMap.get(Fields.Application)).equals("dns-sd") && ((String) this._qualifiedNameMap.get(Fields.Instance)).equals("_services");
    }

    public boolean isDomainDiscoveryQuery() {
        if (!((String) this._qualifiedNameMap.get(Fields.Application)).equals("dns-sd")) {
            return false;
        }
        String str = (String) this._qualifiedNameMap.get(Fields.Instance);
        if ("b".equals(str) || "db".equals(str) || "r".equals(str) || "dr".equals(str) || "lb".equals(str)) {
            return true;
        }
        return false;
    }

    public boolean isReverseLookup() {
        return isV4ReverseLookup() || isV6ReverseLookup();
    }

    public boolean isV4ReverseLookup() {
        return ((String) this._qualifiedNameMap.get(Fields.Domain)).endsWith("in-addr.arpa");
    }

    public boolean isV6ReverseLookup() {
        return ((String) this._qualifiedNameMap.get(Fields.Domain)).endsWith("ip6.arpa");
    }

    public boolean isSameRecordClass(DNSEntry dNSEntry) {
        return dNSEntry != null && dNSEntry.getRecordClass() == getRecordClass();
    }

    public boolean isSameType(DNSEntry dNSEntry) {
        return dNSEntry != null && dNSEntry.getRecordType() == getRecordType();
    }

    /* access modifiers changed from: protected */
    public void toByteArray(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(getName().getBytes("UTF8"));
        dataOutputStream.writeShort(getRecordType().indexValue());
        dataOutputStream.writeShort(getRecordClass().indexValue());
    }

    /* access modifiers changed from: protected */
    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            toByteArray(dataOutputStream);
            dataOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException unused) {
            throw new InternalError();
        }
    }

    public int compareTo(DNSEntry dNSEntry) {
        byte[] byteArray = toByteArray();
        byte[] byteArray2 = dNSEntry.toByteArray();
        int min = Math.min(byteArray.length, byteArray2.length);
        for (int i = 0; i < min; i++) {
            if (byteArray[i] > byteArray2[i]) {
                return 1;
            }
            if (byteArray[i] < byteArray2[i]) {
                return -1;
            }
        }
        return byteArray.length - byteArray2.length;
    }

    public int hashCode() {
        return getKey().hashCode() + getRecordType().indexValue() + getRecordClass().indexValue();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(HttpStatus.SC_OK);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[");
        sb2.append(getClass().getSimpleName());
        sb2.append("@");
        sb2.append(System.identityHashCode(this));
        sb.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" type: ");
        sb3.append(getRecordType());
        sb.append(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append(", class: ");
        sb4.append(getRecordClass());
        sb.append(sb4.toString());
        sb.append(this._unique ? "-unique," : ",");
        StringBuilder sb5 = new StringBuilder();
        sb5.append(" name: ");
        sb5.append(this._name);
        sb.append(sb5.toString());
        toString(sb);
        sb.append("]");
        return sb.toString();
    }
}
