package javax.jmdns.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceInfo.Fields;
import javax.jmdns.impl.DNSOutgoing.MessageOutputStream;
import javax.jmdns.impl.NameRegister.Factory;
import javax.jmdns.impl.NameRegister.NameType;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import org.apache.http.protocol.HTTP;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public abstract class DNSRecord extends DNSEntry {
    public static final byte[] EMPTY_TXT = {0};
    private static Logger logger = Logger.getLogger(DNSRecord.class.getName());
    private long _created = System.currentTimeMillis();
    private InetAddress _source;
    private int _ttl;

    public static abstract class Address extends DNSRecord {
        private static Logger logger1 = Logger.getLogger(Address.class.getName());
        InetAddress _addr;

        /* access modifiers changed from: 0000 */
        public DNSOutgoing addAnswer(JmDNSImpl jmDNSImpl, DNSIncoming dNSIncoming, InetAddress inetAddress, int i, DNSOutgoing dNSOutgoing) throws IOException {
            return dNSOutgoing;
        }

        public boolean isSingleValued() {
            return false;
        }

        protected Address(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z, int i, InetAddress inetAddress) {
            super(str, dNSRecordType, dNSRecordClass, z, i);
            this._addr = inetAddress;
        }

        protected Address(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z, int i, byte[] bArr) {
            super(str, dNSRecordType, dNSRecordClass, z, i);
            try {
                this._addr = InetAddress.getByAddress(bArr);
            } catch (UnknownHostException e) {
                logger1.log(Level.WARNING, "Address() exception ", e);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean same(DNSRecord dNSRecord) {
            boolean z = false;
            if (!(dNSRecord instanceof Address)) {
                return false;
            }
            if (sameName(dNSRecord) && sameValue(dNSRecord)) {
                z = true;
            }
            return z;
        }

        /* access modifiers changed from: 0000 */
        public boolean sameName(DNSRecord dNSRecord) {
            return getName().equalsIgnoreCase(dNSRecord.getName());
        }

        /* access modifiers changed from: 0000 */
        public boolean sameValue(DNSRecord dNSRecord) {
            if (!(dNSRecord instanceof Address)) {
                return false;
            }
            Address address = (Address) dNSRecord;
            if (getAddress() != null || address.getAddress() == null) {
                return getAddress().equals(address.getAddress());
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public InetAddress getAddress() {
            return this._addr;
        }

        /* access modifiers changed from: protected */
        public void toByteArray(DataOutputStream dataOutputStream) throws IOException {
            DNSRecord.super.toByteArray(dataOutputStream);
            byte[] address = getAddress().getAddress();
            for (byte writeByte : address) {
                dataOutputStream.writeByte(writeByte);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean handleQuery(JmDNSImpl jmDNSImpl, long j) {
            if (jmDNSImpl.getLocalHost().conflictWithRecord(this)) {
                Address dNSAddressRecord = jmDNSImpl.getLocalHost().getDNSAddressRecord(getRecordType(), isUnique(), DNSConstants.DNS_TTL);
                if (dNSAddressRecord != null) {
                    int compareTo = compareTo(dNSAddressRecord);
                    if (compareTo == 0) {
                        logger1.finer("handleQuery() Ignoring an identical address query");
                        return false;
                    }
                    logger1.finer("handleQuery() Conflicting query detected.");
                    if (jmDNSImpl.isProbing() && compareTo > 0) {
                        jmDNSImpl.getLocalHost().incrementHostName();
                        jmDNSImpl.getCache().clear();
                        for (ServiceInfo serviceInfo : jmDNSImpl.getServices().values()) {
                            ((ServiceInfoImpl) serviceInfo).revertState();
                        }
                    }
                    jmDNSImpl.revertState();
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleResponse(JmDNSImpl jmDNSImpl) {
            if (!jmDNSImpl.getLocalHost().conflictWithRecord(this)) {
                return false;
            }
            logger1.finer("handleResponse() Denial detected");
            if (jmDNSImpl.isProbing()) {
                jmDNSImpl.getLocalHost().incrementHostName();
                jmDNSImpl.getCache().clear();
                for (ServiceInfo serviceInfo : jmDNSImpl.getServices().values()) {
                    ((ServiceInfoImpl) serviceInfo).revertState();
                }
            }
            jmDNSImpl.revertState();
            return true;
        }

        public ServiceInfo getServiceInfo(boolean z) {
            ServiceInfoImpl serviceInfoImpl = new ServiceInfoImpl(getQualifiedNameMap(), 0, 0, 0, z, (byte[]) null);
            return serviceInfoImpl;
        }

        public ServiceEvent getServiceEvent(JmDNSImpl jmDNSImpl) {
            ServiceInfo serviceInfo = getServiceInfo(false);
            ((ServiceInfoImpl) serviceInfo).setDns(jmDNSImpl);
            return new ServiceEventImpl(jmDNSImpl, serviceInfo.getType(), serviceInfo.getName(), serviceInfo);
        }

        /* access modifiers changed from: protected */
        public void toString(StringBuilder sb) {
            DNSRecord.super.toString(sb);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" address: '");
            sb2.append(getAddress() != null ? getAddress().getHostAddress() : "null");
            sb2.append("'");
            sb.append(sb2.toString());
        }
    }

    public static class HostInformation extends DNSRecord {
        String _cpu;
        String _os;

        /* access modifiers changed from: 0000 */
        public DNSOutgoing addAnswer(JmDNSImpl jmDNSImpl, DNSIncoming dNSIncoming, InetAddress inetAddress, int i, DNSOutgoing dNSOutgoing) throws IOException {
            return dNSOutgoing;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleQuery(JmDNSImpl jmDNSImpl, long j) {
            return false;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleResponse(JmDNSImpl jmDNSImpl) {
            return false;
        }

        public boolean isSingleValued() {
            return true;
        }

        public HostInformation(String str, DNSRecordClass dNSRecordClass, boolean z, int i, String str2, String str3) {
            super(str, DNSRecordType.TYPE_HINFO, dNSRecordClass, z, i);
            this._cpu = str2;
            this._os = str3;
        }

        /* access modifiers changed from: 0000 */
        public boolean sameValue(DNSRecord dNSRecord) {
            boolean z = false;
            if (!(dNSRecord instanceof HostInformation)) {
                return false;
            }
            HostInformation hostInformation = (HostInformation) dNSRecord;
            if (this._cpu == null && hostInformation._cpu != null) {
                return false;
            }
            if (this._os == null && hostInformation._os != null) {
                return false;
            }
            if (this._cpu.equals(hostInformation._cpu) && this._os.equals(hostInformation._os)) {
                z = true;
            }
            return z;
        }

        /* access modifiers changed from: 0000 */
        public void write(MessageOutputStream messageOutputStream) {
            StringBuilder sb = new StringBuilder();
            sb.append(this._cpu);
            sb.append(" ");
            sb.append(this._os);
            String sb2 = sb.toString();
            messageOutputStream.writeUTF(sb2, 0, sb2.length());
        }

        public ServiceInfo getServiceInfo(boolean z) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("cpu", this._cpu);
            hashMap.put("os", this._os);
            ServiceInfoImpl serviceInfoImpl = new ServiceInfoImpl(getQualifiedNameMap(), 0, 0, 0, z, (Map<String, ?>) hashMap);
            return serviceInfoImpl;
        }

        public ServiceEvent getServiceEvent(JmDNSImpl jmDNSImpl) {
            ServiceInfo serviceInfo = getServiceInfo(false);
            ((ServiceInfoImpl) serviceInfo).setDns(jmDNSImpl);
            return new ServiceEventImpl(jmDNSImpl, serviceInfo.getType(), serviceInfo.getName(), serviceInfo);
        }

        /* access modifiers changed from: protected */
        public void toString(StringBuilder sb) {
            DNSRecord.super.toString(sb);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" cpu: '");
            sb2.append(this._cpu);
            sb2.append("' os: '");
            sb2.append(this._os);
            sb2.append("'");
            sb.append(sb2.toString());
        }
    }

    public static class IPv4Address extends Address {
        IPv4Address(String str, DNSRecordClass dNSRecordClass, boolean z, int i, InetAddress inetAddress) {
            super(str, DNSRecordType.TYPE_A, dNSRecordClass, z, i, inetAddress);
        }

        IPv4Address(String str, DNSRecordClass dNSRecordClass, boolean z, int i, byte[] bArr) {
            super(str, DNSRecordType.TYPE_A, dNSRecordClass, z, i, bArr);
        }

        /* access modifiers changed from: 0000 */
        public void write(MessageOutputStream messageOutputStream) {
            if (this._addr != null) {
                byte[] address = this._addr.getAddress();
                if (!(this._addr instanceof Inet4Address)) {
                    byte[] bArr = new byte[4];
                    System.arraycopy(address, 12, bArr, 0, 4);
                    address = bArr;
                }
                messageOutputStream.writeBytes(address, 0, address.length);
            }
        }

        public ServiceInfo getServiceInfo(boolean z) {
            ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) super.getServiceInfo(z);
            serviceInfoImpl.addAddress((Inet4Address) this._addr);
            return serviceInfoImpl;
        }
    }

    public static class IPv6Address extends Address {
        IPv6Address(String str, DNSRecordClass dNSRecordClass, boolean z, int i, InetAddress inetAddress) {
            super(str, DNSRecordType.TYPE_AAAA, dNSRecordClass, z, i, inetAddress);
        }

        IPv6Address(String str, DNSRecordClass dNSRecordClass, boolean z, int i, byte[] bArr) {
            super(str, DNSRecordType.TYPE_AAAA, dNSRecordClass, z, i, bArr);
        }

        /* access modifiers changed from: 0000 */
        public void write(MessageOutputStream messageOutputStream) {
            if (this._addr != null) {
                byte[] address = this._addr.getAddress();
                if (this._addr instanceof Inet4Address) {
                    byte[] bArr = new byte[16];
                    for (int i = 0; i < 16; i++) {
                        if (i < 11) {
                            bArr[i] = address[i - 12];
                        } else {
                            bArr[i] = 0;
                        }
                    }
                    address = bArr;
                }
                messageOutputStream.writeBytes(address, 0, address.length);
            }
        }

        public ServiceInfo getServiceInfo(boolean z) {
            ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) super.getServiceInfo(z);
            serviceInfoImpl.addAddress((Inet6Address) this._addr);
            return serviceInfoImpl;
        }
    }

    public static class Pointer extends DNSRecord {
        private final String _alias;

        /* access modifiers changed from: 0000 */
        public DNSOutgoing addAnswer(JmDNSImpl jmDNSImpl, DNSIncoming dNSIncoming, InetAddress inetAddress, int i, DNSOutgoing dNSOutgoing) throws IOException {
            return dNSOutgoing;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleQuery(JmDNSImpl jmDNSImpl, long j) {
            return false;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleResponse(JmDNSImpl jmDNSImpl) {
            return false;
        }

        public boolean isSingleValued() {
            return false;
        }

        public Pointer(String str, DNSRecordClass dNSRecordClass, boolean z, int i, String str2) {
            super(str, DNSRecordType.TYPE_PTR, dNSRecordClass, z, i);
            this._alias = str2;
        }

        public boolean isSameEntry(DNSEntry dNSEntry) {
            return DNSRecord.super.isSameEntry(dNSEntry) && (dNSEntry instanceof Pointer) && sameValue((Pointer) dNSEntry);
        }

        /* access modifiers changed from: 0000 */
        public void write(MessageOutputStream messageOutputStream) {
            messageOutputStream.writeName(this._alias);
        }

        /* access modifiers changed from: 0000 */
        public boolean sameValue(DNSRecord dNSRecord) {
            if (!(dNSRecord instanceof Pointer)) {
                return false;
            }
            Pointer pointer = (Pointer) dNSRecord;
            if (this._alias != null || pointer._alias == null) {
                return this._alias.equals(pointer._alias);
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public String getAlias() {
            return this._alias;
        }

        public ServiceInfo getServiceInfo(boolean z) {
            if (isServicesDiscoveryMetaQuery()) {
                ServiceInfoImpl serviceInfoImpl = new ServiceInfoImpl(ServiceInfoImpl.decodeQualifiedNameMapForType(getAlias()), 0, 0, 0, z, (byte[]) null);
                return serviceInfoImpl;
            } else if (isReverseLookup()) {
                ServiceInfoImpl serviceInfoImpl2 = new ServiceInfoImpl(getQualifiedNameMap(), 0, 0, 0, z, (byte[]) null);
                return serviceInfoImpl2;
            } else if (isDomainDiscoveryQuery()) {
                ServiceInfoImpl serviceInfoImpl3 = new ServiceInfoImpl(getQualifiedNameMap(), 0, 0, 0, z, (byte[]) null);
                return serviceInfoImpl3;
            } else {
                Map decodeQualifiedNameMapForType = ServiceInfoImpl.decodeQualifiedNameMapForType(getAlias());
                decodeQualifiedNameMapForType.put(Fields.Subtype, getQualifiedNameMap().get(Fields.Subtype));
                ServiceInfoImpl serviceInfoImpl4 = new ServiceInfoImpl(decodeQualifiedNameMapForType, 0, 0, 0, z, getAlias());
                return serviceInfoImpl4;
            }
        }

        public ServiceEvent getServiceEvent(JmDNSImpl jmDNSImpl) {
            ServiceInfo serviceInfo = getServiceInfo(false);
            ((ServiceInfoImpl) serviceInfo).setDns(jmDNSImpl);
            String type = serviceInfo.getType();
            return new ServiceEventImpl(jmDNSImpl, type, JmDNSImpl.toUnqualifiedName(type, getAlias()), serviceInfo);
        }

        /* access modifiers changed from: protected */
        public void toString(StringBuilder sb) {
            DNSRecord.super.toString(sb);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" alias: '");
            String str = this._alias;
            sb2.append(str != null ? str.toString() : "null");
            sb2.append("'");
            sb.append(sb2.toString());
        }
    }

    public static class Service extends DNSRecord {
        private static Logger logger1 = Logger.getLogger(Service.class.getName());
        private final int _port;
        private final int _priority;
        private final String _server;
        private final int _weight;

        public boolean isSingleValued() {
            return true;
        }

        public Service(String str, DNSRecordClass dNSRecordClass, boolean z, int i, int i2, int i3, int i4, String str2) {
            super(str, DNSRecordType.TYPE_SRV, dNSRecordClass, z, i);
            this._priority = i2;
            this._weight = i3;
            this._port = i4;
            this._server = str2;
        }

        /* access modifiers changed from: 0000 */
        public void write(MessageOutputStream messageOutputStream) {
            messageOutputStream.writeShort(this._priority);
            messageOutputStream.writeShort(this._weight);
            messageOutputStream.writeShort(this._port);
            if (DNSIncoming.USE_DOMAIN_NAME_FORMAT_FOR_SRV_TARGET) {
                messageOutputStream.writeName(this._server);
                return;
            }
            String str = this._server;
            messageOutputStream.writeUTF(str, 0, str.length());
            messageOutputStream.writeByte(0);
        }

        /* access modifiers changed from: protected */
        public void toByteArray(DataOutputStream dataOutputStream) throws IOException {
            DNSRecord.super.toByteArray(dataOutputStream);
            dataOutputStream.writeShort(this._priority);
            dataOutputStream.writeShort(this._weight);
            dataOutputStream.writeShort(this._port);
            try {
                dataOutputStream.write(this._server.getBytes(HTTP.UTF_8));
            } catch (UnsupportedEncodingException unused) {
            }
        }

        /* access modifiers changed from: 0000 */
        public String getServer() {
            return this._server;
        }

        public int getPriority() {
            return this._priority;
        }

        public int getWeight() {
            return this._weight;
        }

        public int getPort() {
            return this._port;
        }

        /* access modifiers changed from: 0000 */
        public boolean sameValue(DNSRecord dNSRecord) {
            boolean z = false;
            if (!(dNSRecord instanceof Service)) {
                return false;
            }
            Service service = (Service) dNSRecord;
            if (this._priority == service._priority && this._weight == service._weight && this._port == service._port && this._server.equals(service._server)) {
                z = true;
            }
            return z;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleQuery(JmDNSImpl jmDNSImpl, long j) {
            ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) jmDNSImpl.getServices().get(getKey());
            if (serviceInfoImpl != null && ((serviceInfoImpl.isAnnouncing() || serviceInfoImpl.isAnnounced()) && (this._port != serviceInfoImpl.getPort() || !this._server.equalsIgnoreCase(jmDNSImpl.getLocalHost().getName())))) {
                Logger logger = logger1;
                StringBuilder sb = new StringBuilder();
                sb.append("handleQuery() Conflicting probe detected from: ");
                sb.append(getRecordSource());
                logger.finer(sb.toString());
                Service service = new Service(serviceInfoImpl.getQualifiedName(), DNSRecordClass.CLASS_IN, true, DNSConstants.DNS_TTL, serviceInfoImpl.getPriority(), serviceInfoImpl.getWeight(), serviceInfoImpl.getPort(), jmDNSImpl.getLocalHost().getName());
                try {
                    if (jmDNSImpl.getInetAddress().equals(getRecordSource())) {
                        Logger logger2 = logger1;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Got conflicting probe from ourselves\nincoming: ");
                        sb2.append(toString());
                        sb2.append("\n");
                        sb2.append("local   : ");
                        sb2.append(service.toString());
                        logger2.warning(sb2.toString());
                    }
                } catch (IOException e) {
                    logger1.log(Level.WARNING, "IOException", e);
                }
                int compareTo = compareTo(service);
                if (compareTo == 0) {
                    logger1.finer("handleQuery() Ignoring a identical service query");
                    return false;
                } else if (serviceInfoImpl.isProbing() && compareTo > 0) {
                    String lowerCase = serviceInfoImpl.getQualifiedName().toLowerCase();
                    serviceInfoImpl.setName(Factory.getRegistry().incrementName(jmDNSImpl.getLocalHost().getInetAddress(), serviceInfoImpl.getName(), NameType.SERVICE));
                    jmDNSImpl.getServices().remove(lowerCase);
                    jmDNSImpl.getServices().put(serviceInfoImpl.getQualifiedName().toLowerCase(), serviceInfoImpl);
                    Logger logger3 = logger1;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("handleQuery() Lost tie break: new unique name chosen:");
                    sb3.append(serviceInfoImpl.getName());
                    logger3.finer(sb3.toString());
                    serviceInfoImpl.revertState();
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleResponse(JmDNSImpl jmDNSImpl) {
            ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) jmDNSImpl.getServices().get(getKey());
            if (serviceInfoImpl == null || (this._port == serviceInfoImpl.getPort() && this._server.equalsIgnoreCase(jmDNSImpl.getLocalHost().getName()))) {
                return false;
            }
            logger1.finer("handleResponse() Denial detected");
            if (serviceInfoImpl.isProbing()) {
                String lowerCase = serviceInfoImpl.getQualifiedName().toLowerCase();
                serviceInfoImpl.setName(Factory.getRegistry().incrementName(jmDNSImpl.getLocalHost().getInetAddress(), serviceInfoImpl.getName(), NameType.SERVICE));
                jmDNSImpl.getServices().remove(lowerCase);
                jmDNSImpl.getServices().put(serviceInfoImpl.getQualifiedName().toLowerCase(), serviceInfoImpl);
                Logger logger = logger1;
                StringBuilder sb = new StringBuilder();
                sb.append("handleResponse() New unique name chose:");
                sb.append(serviceInfoImpl.getName());
                logger.finer(sb.toString());
            }
            serviceInfoImpl.revertState();
            return true;
        }

        /* access modifiers changed from: 0000 */
        public DNSOutgoing addAnswer(JmDNSImpl jmDNSImpl, DNSIncoming dNSIncoming, InetAddress inetAddress, int i, DNSOutgoing dNSOutgoing) throws IOException {
            ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) jmDNSImpl.getServices().get(getKey());
            if (serviceInfoImpl != null) {
                if ((this._port == serviceInfoImpl.getPort()) != this._server.equals(jmDNSImpl.getLocalHost().getName())) {
                    Service service = new Service(serviceInfoImpl.getQualifiedName(), DNSRecordClass.CLASS_IN, true, DNSConstants.DNS_TTL, serviceInfoImpl.getPriority(), serviceInfoImpl.getWeight(), serviceInfoImpl.getPort(), jmDNSImpl.getLocalHost().getName());
                    return jmDNSImpl.addAnswer(dNSIncoming, inetAddress, i, dNSOutgoing, service);
                }
            }
            return dNSOutgoing;
        }

        public ServiceInfo getServiceInfo(boolean z) {
            ServiceInfoImpl serviceInfoImpl = new ServiceInfoImpl(getQualifiedNameMap(), this._port, this._weight, this._priority, z, (byte[]) null);
            return serviceInfoImpl;
        }

        public ServiceEvent getServiceEvent(JmDNSImpl jmDNSImpl) {
            ServiceInfo serviceInfo = getServiceInfo(false);
            ((ServiceInfoImpl) serviceInfo).setDns(jmDNSImpl);
            return new ServiceEventImpl(jmDNSImpl, serviceInfo.getType(), serviceInfo.getName(), serviceInfo);
        }

        /* access modifiers changed from: protected */
        public void toString(StringBuilder sb) {
            DNSRecord.super.toString(sb);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" server: '");
            sb2.append(this._server);
            sb2.append(":");
            sb2.append(this._port);
            sb2.append("'");
            sb.append(sb2.toString());
        }
    }

    public static class Text extends DNSRecord {
        private final byte[] _text;

        /* access modifiers changed from: 0000 */
        public DNSOutgoing addAnswer(JmDNSImpl jmDNSImpl, DNSIncoming dNSIncoming, InetAddress inetAddress, int i, DNSOutgoing dNSOutgoing) throws IOException {
            return dNSOutgoing;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleQuery(JmDNSImpl jmDNSImpl, long j) {
            return false;
        }

        /* access modifiers changed from: 0000 */
        public boolean handleResponse(JmDNSImpl jmDNSImpl) {
            return false;
        }

        public boolean isSingleValued() {
            return true;
        }

        public Text(String str, DNSRecordClass dNSRecordClass, boolean z, int i, byte[] bArr) {
            super(str, DNSRecordType.TYPE_TXT, dNSRecordClass, z, i);
            if (bArr == null || bArr.length <= 0) {
                bArr = EMPTY_TXT;
            }
            this._text = bArr;
        }

        /* access modifiers changed from: 0000 */
        public byte[] getText() {
            return this._text;
        }

        /* access modifiers changed from: 0000 */
        public void write(MessageOutputStream messageOutputStream) {
            byte[] bArr = this._text;
            messageOutputStream.writeBytes(bArr, 0, bArr.length);
        }

        /* access modifiers changed from: 0000 */
        public boolean sameValue(DNSRecord dNSRecord) {
            if (!(dNSRecord instanceof Text)) {
                return false;
            }
            Text text = (Text) dNSRecord;
            if (this._text == null && text._text != null) {
                return false;
            }
            int length = text._text.length;
            byte[] bArr = this._text;
            if (length != bArr.length) {
                return false;
            }
            int length2 = bArr.length;
            while (true) {
                int i = length2 - 1;
                if (length2 <= 0) {
                    return true;
                }
                if (text._text[i] != this._text[i]) {
                    return false;
                }
                length2 = i;
            }
        }

        public ServiceInfo getServiceInfo(boolean z) {
            ServiceInfoImpl serviceInfoImpl = new ServiceInfoImpl(getQualifiedNameMap(), 0, 0, 0, z, this._text);
            return serviceInfoImpl;
        }

        public ServiceEvent getServiceEvent(JmDNSImpl jmDNSImpl) {
            ServiceInfo serviceInfo = getServiceInfo(false);
            ((ServiceInfoImpl) serviceInfo).setDns(jmDNSImpl);
            return new ServiceEventImpl(jmDNSImpl, serviceInfo.getType(), serviceInfo.getName(), serviceInfo);
        }

        /* access modifiers changed from: protected */
        public void toString(StringBuilder sb) {
            String str;
            DNSRecord.super.toString(sb);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" text: '");
            byte[] bArr = this._text;
            if (bArr.length > 20) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(new String(this._text, 0, 17));
                sb3.append("...");
                str = sb3.toString();
            } else {
                str = new String(bArr);
            }
            sb2.append(str);
            sb2.append("'");
            sb.append(sb2.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract DNSOutgoing addAnswer(JmDNSImpl jmDNSImpl, DNSIncoming dNSIncoming, InetAddress inetAddress, int i, DNSOutgoing dNSOutgoing) throws IOException;

    public abstract ServiceEvent getServiceEvent(JmDNSImpl jmDNSImpl);

    public abstract ServiceInfo getServiceInfo(boolean z);

    /* access modifiers changed from: 0000 */
    public abstract boolean handleQuery(JmDNSImpl jmDNSImpl, long j);

    /* access modifiers changed from: 0000 */
    public abstract boolean handleResponse(JmDNSImpl jmDNSImpl);

    public abstract boolean isSingleValued();

    /* access modifiers changed from: 0000 */
    public abstract boolean sameValue(DNSRecord dNSRecord);

    /* access modifiers changed from: 0000 */
    public abstract void write(MessageOutputStream messageOutputStream);

    DNSRecord(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z, int i) {
        super(str, dNSRecordType, dNSRecordClass, z);
        this._ttl = i;
    }

    public boolean equals(Object obj) {
        return (obj instanceof DNSRecord) && super.equals(obj) && sameValue((DNSRecord) obj);
    }

    /* access modifiers changed from: 0000 */
    public boolean sameType(DNSRecord dNSRecord) {
        return getRecordType() == dNSRecord.getRecordType();
    }

    /* access modifiers changed from: 0000 */
    public boolean suppressedBy(DNSIncoming dNSIncoming) {
        try {
            for (DNSRecord suppressedBy : dNSIncoming.getAllAnswers()) {
                if (suppressedBy(suppressedBy)) {
                    return true;
                }
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger logger2 = logger;
            Level level = Level.WARNING;
            StringBuilder sb = new StringBuilder();
            sb.append("suppressedBy() message ");
            sb.append(dNSIncoming);
            sb.append(" exception ");
            logger2.log(level, sb.toString(), e);
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean suppressedBy(DNSRecord dNSRecord) {
        return equals(dNSRecord) && dNSRecord._ttl > this._ttl / 2;
    }

    /* access modifiers changed from: 0000 */
    public long getExpirationTime(int i) {
        return this._created + (((long) (i * this._ttl)) * 10);
    }

    /* access modifiers changed from: 0000 */
    public int getRemainingTTL(long j) {
        return (int) Math.max(0, (getExpirationTime(100) - j) / 1000);
    }

    public boolean isExpired(long j) {
        return getExpirationTime(100) <= j;
    }

    public boolean isStale(long j) {
        return getExpirationTime(50) <= j;
    }

    /* access modifiers changed from: 0000 */
    public void resetTTL(DNSRecord dNSRecord) {
        this._created = dNSRecord._created;
        this._ttl = dNSRecord._ttl;
    }

    /* access modifiers changed from: 0000 */
    public void setWillExpireSoon(long j) {
        this._created = j;
        this._ttl = 1;
    }

    public ServiceInfo getServiceInfo() {
        return getServiceInfo(false);
    }

    public void setRecordSource(InetAddress inetAddress) {
        this._source = inetAddress;
    }

    public InetAddress getRecordSource() {
        return this._source;
    }

    /* access modifiers changed from: protected */
    public void toString(StringBuilder sb) {
        super.toString(sb);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" ttl: '");
        sb2.append(getRemainingTTL(System.currentTimeMillis()));
        sb2.append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        sb2.append(this._ttl);
        sb2.append("'");
        sb.append(sb2.toString());
    }

    public void setTTL(int i) {
        this._ttl = i;
    }

    public int getTTL() {
        return this._ttl;
    }
}
