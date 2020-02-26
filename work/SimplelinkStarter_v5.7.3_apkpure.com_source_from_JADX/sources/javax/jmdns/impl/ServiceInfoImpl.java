package javax.jmdns.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceInfo.Fields;
import javax.jmdns.impl.DNSRecord.Pointer;
import javax.jmdns.impl.DNSRecord.Service;
import javax.jmdns.impl.DNSRecord.Text;
import javax.jmdns.impl.DNSStatefulObject.DefaultImplementation;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.constants.DNSState;
import javax.jmdns.impl.tasks.DNSTask;
import org.apache.http.HttpHost;
import org.apache.http.cookie.ClientCookie;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

public class ServiceInfoImpl extends ServiceInfo implements DNSListener, DNSStatefulObject {
    private static Logger logger = Logger.getLogger(ServiceInfoImpl.class.getName());
    private String _application;
    private Delegate _delegate;
    private String _domain;
    private final Set<Inet4Address> _ipv4Addresses;
    private final Set<Inet6Address> _ipv6Addresses;
    private transient String _key;
    private String _name;
    private boolean _needTextAnnouncing;
    private boolean _persistent;
    private int _port;
    private int _priority;
    private Map<String, byte[]> _props;
    private String _protocol;
    private String _server;
    private final ServiceInfoState _state;
    private String _subtype;
    private byte[] _text;
    private int _weight;

    /* renamed from: javax.jmdns.impl.ServiceInfoImpl$1 */
    static /* synthetic */ class C10961 {
        static final /* synthetic */ int[] $SwitchMap$javax$jmdns$impl$constants$DNSRecordType = new int[DNSRecordType.values().length];

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
                javax.jmdns.impl.constants.DNSRecordType[] r0 = javax.jmdns.impl.constants.DNSRecordType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$javax$jmdns$impl$constants$DNSRecordType = r0
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x0014 }
                javax.jmdns.impl.constants.DNSRecordType r1 = javax.jmdns.impl.constants.DNSRecordType.TYPE_A     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x001f }
                javax.jmdns.impl.constants.DNSRecordType r1 = javax.jmdns.impl.constants.DNSRecordType.TYPE_AAAA     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x002a }
                javax.jmdns.impl.constants.DNSRecordType r1 = javax.jmdns.impl.constants.DNSRecordType.TYPE_SRV     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x0035 }
                javax.jmdns.impl.constants.DNSRecordType r1 = javax.jmdns.impl.constants.DNSRecordType.TYPE_TXT     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x0040 }
                javax.jmdns.impl.constants.DNSRecordType r1 = javax.jmdns.impl.constants.DNSRecordType.TYPE_PTR     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.ServiceInfoImpl.C10961.<clinit>():void");
        }
    }

    public interface Delegate {
        void textValueUpdated(ServiceInfo serviceInfo, byte[] bArr);
    }

    private static final class ServiceInfoState extends DefaultImplementation {
        private static final long serialVersionUID = 1104131034952196820L;
        private final ServiceInfoImpl _info;

        public ServiceInfoState(ServiceInfoImpl serviceInfoImpl) {
            this._info = serviceInfoImpl;
        }

        /* access modifiers changed from: protected */
        public void setTask(DNSTask dNSTask) {
            super.setTask(dNSTask);
            if (this._task == null && this._info.needTextAnnouncing()) {
                lock();
                try {
                    if (this._task == null && this._info.needTextAnnouncing()) {
                        if (this._state.isAnnounced()) {
                            setState(DNSState.ANNOUNCING_1);
                            if (getDns() != null) {
                                getDns().startAnnouncer();
                            }
                        }
                        this._info.setNeedTextAnnouncing(false);
                    }
                } finally {
                    unlock();
                }
            }
        }

        public void setDns(JmDNSImpl jmDNSImpl) {
            super.setDns(jmDNSImpl);
        }
    }

    public ServiceInfoImpl(String str, String str2, String str3, int i, int i2, int i3, boolean z, String str4) {
        this(decodeQualifiedNameMap(str, str2, str3), i, i2, i3, z, (byte[]) null);
        this._server = str4;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256);
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream(100);
            writeUTF(byteArrayOutputStream2, str4);
            byte[] byteArray = byteArrayOutputStream2.toByteArray();
            if (byteArray.length <= 255) {
                byteArrayOutputStream.write((byte) byteArray.length);
                byteArrayOutputStream.write(byteArray, 0, byteArray.length);
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                if (byteArray2 == null || byteArray2.length <= 0) {
                    byteArray2 = DNSRecord.EMPTY_TXT;
                }
                this._text = byteArray2;
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot have individual values larger that 255 chars. Offending value: ");
            sb.append(str4);
            throw new IOException(sb.toString());
        } catch (IOException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("unexpected exception: ");
            sb2.append(e);
            throw new RuntimeException(sb2.toString());
        }
    }

    public ServiceInfoImpl(String str, String str2, String str3, int i, int i2, int i3, boolean z, Map<String, ?> map) {
        this(decodeQualifiedNameMap(str, str2, str3), i, i2, i3, z, textFromProperties(map));
    }

    public ServiceInfoImpl(String str, String str2, String str3, int i, int i2, int i3, boolean z, byte[] bArr) {
        this(decodeQualifiedNameMap(str, str2, str3), i, i2, i3, z, bArr);
    }

    public ServiceInfoImpl(Map<Fields, String> map, int i, int i2, int i3, boolean z, Map<String, ?> map2) {
        this(map, i, i2, i3, z, textFromProperties(map2));
    }

    ServiceInfoImpl(Map<Fields, String> map, int i, int i2, int i3, boolean z, String str) {
        this(map, i, i2, i3, z, (byte[]) null);
        this._server = str;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(str.length());
            writeUTF(byteArrayOutputStream, str);
            this._text = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("unexpected exception: ");
            sb.append(e);
            throw new RuntimeException(sb.toString());
        }
    }

    ServiceInfoImpl(Map<Fields, String> map, int i, int i2, int i3, boolean z, byte[] bArr) {
        Map checkQualifiedNameMap = checkQualifiedNameMap(map);
        this._domain = (String) checkQualifiedNameMap.get(Fields.Domain);
        this._protocol = (String) checkQualifiedNameMap.get(Fields.Protocol);
        this._application = (String) checkQualifiedNameMap.get(Fields.Application);
        this._name = (String) checkQualifiedNameMap.get(Fields.Instance);
        this._subtype = (String) checkQualifiedNameMap.get(Fields.Subtype);
        this._port = i;
        this._weight = i2;
        this._priority = i3;
        this._text = bArr;
        setNeedTextAnnouncing(false);
        this._state = new ServiceInfoState(this);
        this._persistent = z;
        this._ipv4Addresses = Collections.synchronizedSet(new LinkedHashSet());
        this._ipv6Addresses = Collections.synchronizedSet(new LinkedHashSet());
    }

    ServiceInfoImpl(ServiceInfo serviceInfo) {
        this._ipv4Addresses = Collections.synchronizedSet(new LinkedHashSet());
        this._ipv6Addresses = Collections.synchronizedSet(new LinkedHashSet());
        if (serviceInfo != null) {
            this._domain = serviceInfo.getDomain();
            this._protocol = serviceInfo.getProtocol();
            this._application = serviceInfo.getApplication();
            this._name = serviceInfo.getName();
            this._subtype = serviceInfo.getSubtype();
            this._port = serviceInfo.getPort();
            this._weight = serviceInfo.getWeight();
            this._priority = serviceInfo.getPriority();
            this._text = serviceInfo.getTextBytes();
            this._persistent = serviceInfo.isPersistent();
            for (Inet6Address add : serviceInfo.getInet6Addresses()) {
                this._ipv6Addresses.add(add);
            }
            for (Inet4Address add2 : serviceInfo.getInet4Addresses()) {
                this._ipv4Addresses.add(add2);
            }
        }
        this._state = new ServiceInfoState(this);
    }

    public static Map<Fields, String> decodeQualifiedNameMap(String str, String str2, String str3) {
        Map decodeQualifiedNameMapForType = decodeQualifiedNameMapForType(str);
        decodeQualifiedNameMapForType.put(Fields.Instance, str2);
        decodeQualifiedNameMapForType.put(Fields.Subtype, str3);
        return checkQualifiedNameMap(decodeQualifiedNameMapForType);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map<javax.jmdns.ServiceInfo.Fields, java.lang.String> decodeQualifiedNameMapForType(java.lang.String r10) {
        /*
            java.lang.String r0 = r10.toLowerCase()
            java.lang.String r1 = "in-addr.arpa"
            boolean r2 = r0.contains(r1)
            java.lang.String r3 = "ip6.arpa"
            r4 = 0
            java.lang.String r5 = ""
            if (r2 != 0) goto L_0x00e4
            boolean r2 = r0.contains(r3)
            if (r2 == 0) goto L_0x0019
            goto L_0x00e4
        L_0x0019:
            java.lang.String r1 = "_"
            boolean r2 = r0.contains(r1)
            r3 = 46
            java.lang.String r6 = "."
            if (r2 != 0) goto L_0x0041
            boolean r2 = r0.contains(r6)
            if (r2 == 0) goto L_0x0041
            int r0 = r0.indexOf(r3)
            java.lang.String r1 = r10.substring(r4, r0)
            java.lang.String r1 = removeSeparators(r1)
            java.lang.String r10 = r10.substring(r0)
            java.lang.String r10 = removeSeparators(r10)
            goto L_0x00ff
        L_0x0041:
            boolean r2 = r0.startsWith(r1)
            java.lang.String r7 = "._"
            if (r2 == 0) goto L_0x0051
            java.lang.String r2 = "_services"
            boolean r2 = r0.startsWith(r2)
            if (r2 == 0) goto L_0x006e
        L_0x0051:
            int r2 = r0.indexOf(r7)
            if (r2 <= 0) goto L_0x006e
            java.lang.String r8 = r10.substring(r4, r2)
            int r2 = r2 + 1
            int r9 = r0.length()
            if (r2 >= r9) goto L_0x006c
            java.lang.String r9 = r0.substring(r2)
            java.lang.String r10 = r10.substring(r2)
            goto L_0x0070
        L_0x006c:
            r9 = r0
            goto L_0x0070
        L_0x006e:
            r9 = r0
            r8 = r5
        L_0x0070:
            int r2 = r9.lastIndexOf(r7)
            if (r2 <= 0) goto L_0x0081
            int r2 = r2 + 2
            int r3 = r9.indexOf(r3, r2)
            java.lang.String r2 = r10.substring(r2, r3)
            goto L_0x0082
        L_0x0081:
            r2 = r5
        L_0x0082:
            int r3 = r2.length()
            if (r3 <= 0) goto L_0x00c7
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r1)
            java.lang.String r1 = r2.toLowerCase()
            r0.append(r1)
            r0.append(r6)
            java.lang.String r0 = r0.toString()
            int r0 = r9.indexOf(r0)
            int r1 = r2.length()
            int r1 = r1 + r0
            int r1 = r1 + 2
            int r3 = r9.length()
            boolean r6 = r9.endsWith(r6)
            int r3 = r3 - r6
            if (r3 <= r1) goto L_0x00b9
            java.lang.String r1 = r10.substring(r1, r3)
            goto L_0x00ba
        L_0x00b9:
            r1 = r5
        L_0x00ba:
            if (r0 <= 0) goto L_0x00c4
            int r0 = r0 + -1
            java.lang.String r0 = r10.substring(r4, r0)
            r10 = r1
            goto L_0x00c8
        L_0x00c4:
            r10 = r1
            r0 = r5
            goto L_0x00c8
        L_0x00c7:
            r10 = r5
        L_0x00c8:
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r3 = "._sub"
            int r1 = r1.indexOf(r3)
            if (r1 <= 0) goto L_0x00e2
            int r3 = r1 + 5
            java.lang.String r1 = r0.substring(r4, r1)
            java.lang.String r5 = removeSeparators(r1)
            java.lang.String r0 = r0.substring(r3)
        L_0x00e2:
            r1 = r8
            goto L_0x0101
        L_0x00e4:
            boolean r2 = r0.contains(r1)
            if (r2 == 0) goto L_0x00ef
            int r0 = r0.indexOf(r1)
            goto L_0x00f3
        L_0x00ef:
            int r0 = r0.indexOf(r3)
        L_0x00f3:
            java.lang.String r1 = r10.substring(r4, r0)
            java.lang.String r1 = removeSeparators(r1)
            java.lang.String r10 = r10.substring(r0)
        L_0x00ff:
            r0 = r5
            r2 = r0
        L_0x0101:
            java.util.HashMap r3 = new java.util.HashMap
            r4 = 5
            r3.<init>(r4)
            javax.jmdns.ServiceInfo$Fields r4 = javax.jmdns.ServiceInfo.Fields.Domain
            java.lang.String r10 = removeSeparators(r10)
            r3.put(r4, r10)
            javax.jmdns.ServiceInfo$Fields r10 = javax.jmdns.ServiceInfo.Fields.Protocol
            r3.put(r10, r2)
            javax.jmdns.ServiceInfo$Fields r10 = javax.jmdns.ServiceInfo.Fields.Application
            java.lang.String r0 = removeSeparators(r0)
            r3.put(r10, r0)
            javax.jmdns.ServiceInfo$Fields r10 = javax.jmdns.ServiceInfo.Fields.Instance
            r3.put(r10, r1)
            javax.jmdns.ServiceInfo$Fields r10 = javax.jmdns.ServiceInfo.Fields.Subtype
            r3.put(r10, r5)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.ServiceInfoImpl.decodeQualifiedNameMapForType(java.lang.String):java.util.Map");
    }

    protected static Map<Fields, String> checkQualifiedNameMap(Map<Fields, String> map) {
        HashMap hashMap = new HashMap(5);
        String str = "local";
        String str2 = map.containsKey(Fields.Domain) ? (String) map.get(Fields.Domain) : str;
        if (str2 == null || str2.length() == 0) {
            str2 = str;
        }
        hashMap.put(Fields.Domain, removeSeparators(str2));
        String str3 = "tcp";
        String str4 = map.containsKey(Fields.Protocol) ? (String) map.get(Fields.Protocol) : str3;
        if (str4 == null || str4.length() == 0) {
            str4 = str3;
        }
        hashMap.put(Fields.Protocol, removeSeparators(str4));
        String str5 = "";
        String str6 = map.containsKey(Fields.Application) ? (String) map.get(Fields.Application) : str5;
        if (str6 == null || str6.length() == 0) {
            str6 = str5;
        }
        hashMap.put(Fields.Application, removeSeparators(str6));
        String str7 = map.containsKey(Fields.Instance) ? (String) map.get(Fields.Instance) : str5;
        if (str7 == null || str7.length() == 0) {
            str7 = str5;
        }
        hashMap.put(Fields.Instance, removeSeparators(str7));
        String str8 = map.containsKey(Fields.Subtype) ? (String) map.get(Fields.Subtype) : str5;
        if (str8 == null || str8.length() == 0) {
            str8 = str5;
        }
        hashMap.put(Fields.Subtype, removeSeparators(str8));
        return hashMap;
    }

    private static String removeSeparators(String str) {
        if (str == null) {
            return "";
        }
        String trim = str.trim();
        String str2 = ".";
        if (trim.startsWith(str2)) {
            trim = trim.substring(1);
        }
        if (trim.startsWith("_")) {
            trim = trim.substring(1);
        }
        if (trim.endsWith(str2)) {
            trim = trim.substring(0, trim.length() - 1);
        }
        return trim;
    }

    public String getType() {
        String str;
        String domain = getDomain();
        String protocol = getProtocol();
        String application = getApplication();
        StringBuilder sb = new StringBuilder();
        String str2 = "_";
        String str3 = "";
        String str4 = ".";
        if (application.length() > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(application);
            sb2.append(str4);
            str = sb2.toString();
        } else {
            str = str3;
        }
        sb.append(str);
        if (protocol.length() > 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(protocol);
            sb3.append(str4);
            str3 = sb3.toString();
        }
        sb.append(str3);
        sb.append(domain);
        sb.append(str4);
        return sb.toString();
    }

    public String getTypeWithSubtype() {
        String str;
        String subtype = getSubtype();
        StringBuilder sb = new StringBuilder();
        if (subtype.length() > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("_");
            sb2.append(subtype.toLowerCase());
            sb2.append("._sub.");
            str = sb2.toString();
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(getType());
        return sb.toString();
    }

    public String getName() {
        String str = this._name;
        return str != null ? str : "";
    }

    public String getKey() {
        if (this._key == null) {
            this._key = getQualifiedName().toLowerCase();
        }
        return this._key;
    }

    /* access modifiers changed from: 0000 */
    public void setName(String str) {
        this._name = str;
        this._key = null;
    }

    public String getQualifiedName() {
        String str;
        String str2;
        String domain = getDomain();
        String protocol = getProtocol();
        String application = getApplication();
        String name = getName();
        StringBuilder sb = new StringBuilder();
        String str3 = "";
        String str4 = ".";
        if (name.length() > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(name);
            sb2.append(str4);
            str = sb2.toString();
        } else {
            str = str3;
        }
        sb.append(str);
        String str5 = "_";
        if (application.length() > 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str5);
            sb3.append(application);
            sb3.append(str4);
            str2 = sb3.toString();
        } else {
            str2 = str3;
        }
        sb.append(str2);
        if (protocol.length() > 0) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str5);
            sb4.append(protocol);
            sb4.append(str4);
            str3 = sb4.toString();
        }
        sb.append(str3);
        sb.append(domain);
        sb.append(str4);
        return sb.toString();
    }

    public String getServer() {
        String str = this._server;
        return str != null ? str : "";
    }

    /* access modifiers changed from: 0000 */
    public void setServer(String str) {
        this._server = str;
    }

    @Deprecated
    public String getHostAddress() {
        String[] hostAddresses = getHostAddresses();
        return hostAddresses.length > 0 ? hostAddresses[0] : "";
    }

    public String[] getHostAddresses() {
        Inet4Address[] inet4Addresses = getInet4Addresses();
        Inet6Address[] inet6Addresses = getInet6Addresses();
        String[] strArr = new String[(inet4Addresses.length + inet6Addresses.length)];
        for (int i = 0; i < inet4Addresses.length; i++) {
            strArr[i] = inet4Addresses[i].getHostAddress();
        }
        for (int i2 = 0; i2 < inet6Addresses.length; i2++) {
            int length = inet4Addresses.length + i2;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(inet6Addresses[i2].getHostAddress());
            sb.append("]");
            strArr[length] = sb.toString();
        }
        return strArr;
    }

    /* access modifiers changed from: 0000 */
    public void addAddress(Inet4Address inet4Address) {
        this._ipv4Addresses.add(inet4Address);
    }

    /* access modifiers changed from: 0000 */
    public void addAddress(Inet6Address inet6Address) {
        this._ipv6Addresses.add(inet6Address);
    }

    @Deprecated
    public InetAddress getAddress() {
        return getInetAddress();
    }

    @Deprecated
    public InetAddress getInetAddress() {
        InetAddress[] inetAddresses = getInetAddresses();
        if (inetAddresses.length > 0) {
            return inetAddresses[0];
        }
        return null;
    }

    @Deprecated
    public Inet4Address getInet4Address() {
        Inet4Address[] inet4Addresses = getInet4Addresses();
        if (inet4Addresses.length > 0) {
            return inet4Addresses[0];
        }
        return null;
    }

    @Deprecated
    public Inet6Address getInet6Address() {
        Inet6Address[] inet6Addresses = getInet6Addresses();
        if (inet6Addresses.length > 0) {
            return inet6Addresses[0];
        }
        return null;
    }

    public InetAddress[] getInetAddresses() {
        ArrayList arrayList = new ArrayList(this._ipv4Addresses.size() + this._ipv6Addresses.size());
        arrayList.addAll(this._ipv4Addresses);
        arrayList.addAll(this._ipv6Addresses);
        return (InetAddress[]) arrayList.toArray(new InetAddress[arrayList.size()]);
    }

    public Inet4Address[] getInet4Addresses() {
        Set<Inet4Address> set = this._ipv4Addresses;
        return (Inet4Address[]) set.toArray(new Inet4Address[set.size()]);
    }

    public Inet6Address[] getInet6Addresses() {
        Set<Inet6Address> set = this._ipv6Addresses;
        return (Inet6Address[]) set.toArray(new Inet6Address[set.size()]);
    }

    public int getPort() {
        return this._port;
    }

    public int getPriority() {
        return this._priority;
    }

    public int getWeight() {
        return this._weight;
    }

    public byte[] getTextBytes() {
        byte[] bArr = this._text;
        return (bArr == null || bArr.length <= 0) ? DNSRecord.EMPTY_TXT : bArr;
    }

    @Deprecated
    public String getTextString() {
        Map properties = getProperties();
        Iterator it = properties.keySet().iterator();
        if (!it.hasNext()) {
            return "";
        }
        String str = (String) it.next();
        byte[] bArr = (byte[]) properties.get(str);
        if (bArr == null || bArr.length <= 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("=");
        sb.append(new String(bArr));
        return sb.toString();
    }

    @Deprecated
    public String getURL() {
        return getURL(HttpHost.DEFAULT_SCHEME_NAME);
    }

    public String[] getURLs() {
        return getURLs(HttpHost.DEFAULT_SCHEME_NAME);
    }

    @Deprecated
    public String getURL(String str) {
        String[] uRLs = getURLs(str);
        if (uRLs.length > 0) {
            return uRLs[0];
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("://null:");
        sb.append(getPort());
        return sb.toString();
    }

    public String[] getURLs(String str) {
        InetAddress[] inetAddresses = getInetAddresses();
        ArrayList arrayList = new ArrayList(inetAddresses.length);
        for (InetAddress inetAddress : inetAddresses) {
            String hostAddress = inetAddress.getHostAddress();
            if (inetAddress instanceof Inet6Address) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append(hostAddress);
                sb.append("]");
                hostAddress = sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            String str2 = "://";
            sb2.append(str2);
            sb2.append(hostAddress);
            sb2.append(":");
            sb2.append(getPort());
            String sb3 = sb2.toString();
            String propertyString = getPropertyString(ClientCookie.PATH_ATTR);
            if (propertyString != null) {
                if (propertyString.indexOf(str2) >= 0) {
                    sb3 = propertyString;
                } else {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(sb3);
                    String str3 = MqttTopic.TOPIC_LEVEL_SEPARATOR;
                    if (!propertyString.startsWith(str3)) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str3);
                        sb5.append(propertyString);
                        propertyString = sb5.toString();
                    }
                    sb4.append(propertyString);
                    sb3 = sb4.toString();
                }
            }
            arrayList.add(sb3);
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public synchronized byte[] getPropertyBytes(String str) {
        return (byte[]) getProperties().get(str);
    }

    public synchronized String getPropertyString(String str) {
        byte[] bArr = (byte[]) getProperties().get(str);
        if (bArr == null) {
            return null;
        }
        if (bArr == NO_VALUE) {
            return "true";
        }
        return readUTF(bArr, 0, bArr.length);
    }

    public Enumeration<String> getPropertyNames() {
        Map properties = getProperties();
        return new Vector(properties != null ? properties.keySet() : Collections.emptySet()).elements();
    }

    public String getApplication() {
        String str = this._application;
        return str != null ? str : "";
    }

    public String getDomain() {
        String str = this._domain;
        return str != null ? str : "local";
    }

    public String getProtocol() {
        String str = this._protocol;
        return str != null ? str : "tcp";
    }

    public String getSubtype() {
        String str = this._subtype;
        return str != null ? str : "";
    }

    public Map<Fields, String> getQualifiedNameMap() {
        HashMap hashMap = new HashMap(5);
        hashMap.put(Fields.Domain, getDomain());
        hashMap.put(Fields.Protocol, getProtocol());
        hashMap.put(Fields.Application, getApplication());
        hashMap.put(Fields.Instance, getName());
        hashMap.put(Fields.Subtype, getSubtype());
        return hashMap;
    }

    static void writeUTF(OutputStream outputStream, String str) throws IOException {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt >= 1 && charAt <= 127) {
                outputStream.write(charAt);
            } else if (charAt > 2047) {
                outputStream.write(((charAt >> 12) & 15) | 224);
                outputStream.write(((charAt >> 6) & 63) | 128);
                outputStream.write(((charAt >> 0) & 63) | 128);
            } else {
                outputStream.write(((charAt >> 6) & 31) | 192);
                outputStream.write(((charAt >> 0) & 63) | 128);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public String readUTF(byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        int i5 = i + i2;
        while (i < i5) {
            int i6 = i + 1;
            byte b2 = bArr[i] & 255;
            switch (b2 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    break;
                case 12:
                case 13:
                    if (i6 < i2) {
                        i3 = (b2 & 31) << 6;
                        i4 = i6 + 1;
                        b = bArr[i6] & 63;
                        break;
                    } else {
                        return null;
                    }
                case 14:
                    if (i6 + 2 >= i2) {
                        return null;
                    }
                    int i7 = i6 + 1;
                    byte b3 = ((b2 & 15) << MqttWireMessage.MESSAGE_TYPE_PINGREQ) | ((bArr[i6] & 63) << 6);
                    i6 = i7 + 1;
                    b2 = b3 | (bArr[i7] & 63);
                    continue;
                default:
                    i4 = i6 + 1;
                    if (i4 < i2) {
                        i3 = (b2 & 63) << 4;
                        b = bArr[i6] & 15;
                        break;
                    } else {
                        return null;
                    }
            }
            b2 = i3 | b;
            i6 = i4;
            stringBuffer.append((char) b2);
            i = i6;
        }
        return stringBuffer.toString();
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006a, code lost:
        r0.clear();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.Map<java.lang.String, byte[]> getProperties() {
        /*
            r9 = this;
            monitor-enter(r9)
            java.util.Map<java.lang.String, byte[]> r0 = r9._props     // Catch:{ all -> 0x0087 }
            if (r0 != 0) goto L_0x007a
            byte[] r0 = r9.getTextBytes()     // Catch:{ all -> 0x0087 }
            if (r0 == 0) goto L_0x007a
            java.util.Hashtable r0 = new java.util.Hashtable     // Catch:{ all -> 0x0087 }
            r0.<init>()     // Catch:{ all -> 0x0087 }
            r1 = 0
            r2 = 0
        L_0x0012:
            byte[] r3 = r9.getTextBytes()     // Catch:{ Exception -> 0x006e }
            int r3 = r3.length     // Catch:{ Exception -> 0x006e }
            if (r2 >= r3) goto L_0x0078
            byte[] r3 = r9.getTextBytes()     // Catch:{ Exception -> 0x006e }
            int r4 = r2 + 1
            byte r2 = r3[r2]     // Catch:{ Exception -> 0x006e }
            r2 = r2 & 255(0xff, float:3.57E-43)
            if (r2 == 0) goto L_0x006a
            int r3 = r4 + r2
            byte[] r5 = r9.getTextBytes()     // Catch:{ Exception -> 0x006e }
            int r5 = r5.length     // Catch:{ Exception -> 0x006e }
            if (r3 <= r5) goto L_0x002f
            goto L_0x006a
        L_0x002f:
            r5 = 0
        L_0x0030:
            if (r5 >= r2) goto L_0x0041
            byte[] r6 = r9.getTextBytes()     // Catch:{ Exception -> 0x006e }
            int r7 = r4 + r5
            byte r6 = r6[r7]     // Catch:{ Exception -> 0x006e }
            r7 = 61
            if (r6 == r7) goto L_0x0041
            int r5 = r5 + 1
            goto L_0x0030
        L_0x0041:
            byte[] r6 = r9.getTextBytes()     // Catch:{ Exception -> 0x006e }
            java.lang.String r6 = r9.readUTF(r6, r4, r5)     // Catch:{ Exception -> 0x006e }
            if (r6 != 0) goto L_0x004f
            r0.clear()     // Catch:{ Exception -> 0x006e }
            goto L_0x0078
        L_0x004f:
            if (r5 != r2) goto L_0x0058
            byte[] r2 = NO_VALUE     // Catch:{ Exception -> 0x006e }
            r0.put(r6, r2)     // Catch:{ Exception -> 0x006e }
            r2 = r4
            goto L_0x0012
        L_0x0058:
            int r5 = r5 + 1
            int r2 = r2 - r5
            byte[] r7 = new byte[r2]     // Catch:{ Exception -> 0x006e }
            byte[] r8 = r9.getTextBytes()     // Catch:{ Exception -> 0x006e }
            int r4 = r4 + r5
            java.lang.System.arraycopy(r8, r4, r7, r1, r2)     // Catch:{ Exception -> 0x006e }
            r0.put(r6, r7)     // Catch:{ Exception -> 0x006e }
            r2 = r3
            goto L_0x0012
        L_0x006a:
            r0.clear()     // Catch:{ Exception -> 0x006e }
            goto L_0x0078
        L_0x006e:
            r1 = move-exception
            java.util.logging.Logger r2 = logger     // Catch:{ all -> 0x0087 }
            java.util.logging.Level r3 = java.util.logging.Level.WARNING     // Catch:{ all -> 0x0087 }
            java.lang.String r4 = "Malformed TXT Field "
            r2.log(r3, r4, r1)     // Catch:{ all -> 0x0087 }
        L_0x0078:
            r9._props = r0     // Catch:{ all -> 0x0087 }
        L_0x007a:
            java.util.Map<java.lang.String, byte[]> r0 = r9._props     // Catch:{ all -> 0x0087 }
            if (r0 == 0) goto L_0x0081
            java.util.Map<java.lang.String, byte[]> r0 = r9._props     // Catch:{ all -> 0x0087 }
            goto L_0x0085
        L_0x0081:
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch:{ all -> 0x0087 }
        L_0x0085:
            monitor-exit(r9)
            return r0
        L_0x0087:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.ServiceInfoImpl.getProperties():java.util.Map");
    }

    /* JADX WARNING: Removed duplicated region for block: B:54:0x012b  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x013c A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateRecord(javax.jmdns.impl.DNSCache r5, long r6, javax.jmdns.impl.DNSEntry r8) {
        /*
            r4 = this;
            boolean r0 = r8 instanceof javax.jmdns.impl.DNSRecord
            if (r0 == 0) goto L_0x0144
            boolean r0 = r8.isExpired(r6)
            if (r0 != 0) goto L_0x0144
            int[] r0 = javax.jmdns.impl.ServiceInfoImpl.C10961.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType
            javax.jmdns.impl.constants.DNSRecordType r1 = r8.getRecordType()
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = 0
            r2 = 1
            if (r0 == r2) goto L_0x0100
            r3 = 2
            if (r0 == r3) goto L_0x00e4
            r3 = 3
            if (r0 == r3) goto L_0x005f
            r5 = 4
            if (r0 == r5) goto L_0x0044
            r5 = 5
            if (r0 == r5) goto L_0x0028
            goto L_0x011c
        L_0x0028:
            java.lang.String r5 = r4.getSubtype()
            int r5 = r5.length()
            if (r5 != 0) goto L_0x011c
            java.lang.String r5 = r8.getSubtype()
            int r5 = r5.length()
            if (r5 == 0) goto L_0x011c
            java.lang.String r5 = r8.getSubtype()
            r4._subtype = r5
            goto L_0x011d
        L_0x0044:
            java.lang.String r5 = r8.getName()
            java.lang.String r6 = r4.getQualifiedName()
            boolean r5 = r5.equalsIgnoreCase(r6)
            if (r5 == 0) goto L_0x011c
            javax.jmdns.impl.DNSRecord$Text r8 = (javax.jmdns.impl.DNSRecord.Text) r8
            byte[] r5 = r8.getText()
            r4._text = r5
            r5 = 0
            r4._props = r5
            goto L_0x011d
        L_0x005f:
            java.lang.String r0 = r8.getName()
            java.lang.String r3 = r4.getQualifiedName()
            boolean r0 = r0.equalsIgnoreCase(r3)
            if (r0 == 0) goto L_0x011c
            javax.jmdns.impl.DNSRecord$Service r8 = (javax.jmdns.impl.DNSRecord.Service) r8
            java.lang.String r0 = r4._server
            if (r0 == 0) goto L_0x0080
            java.lang.String r3 = r8.getServer()
            boolean r0 = r0.equalsIgnoreCase(r3)
            if (r0 != 0) goto L_0x007e
            goto L_0x0080
        L_0x007e:
            r0 = 0
            goto L_0x0081
        L_0x0080:
            r0 = 1
        L_0x0081:
            java.lang.String r3 = r8.getServer()
            r4._server = r3
            int r3 = r8.getPort()
            r4._port = r3
            int r3 = r8.getWeight()
            r4._weight = r3
            int r8 = r8.getPriority()
            r4._priority = r8
            if (r0 == 0) goto L_0x00e1
            java.util.Set<java.net.Inet4Address> r8 = r4._ipv4Addresses
            r8.clear()
            java.util.Set<java.net.Inet6Address> r8 = r4._ipv6Addresses
            r8.clear()
            java.lang.String r8 = r4._server
            javax.jmdns.impl.constants.DNSRecordType r0 = javax.jmdns.impl.constants.DNSRecordType.TYPE_A
            javax.jmdns.impl.constants.DNSRecordClass r2 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_IN
            java.util.Collection r8 = r5.getDNSEntryList(r8, r0, r2)
            java.util.Iterator r8 = r8.iterator()
        L_0x00b3:
            boolean r0 = r8.hasNext()
            if (r0 == 0) goto L_0x00c3
            java.lang.Object r0 = r8.next()
            javax.jmdns.impl.DNSEntry r0 = (javax.jmdns.impl.DNSEntry) r0
            r4.updateRecord(r5, r6, r0)
            goto L_0x00b3
        L_0x00c3:
            java.lang.String r8 = r4._server
            javax.jmdns.impl.constants.DNSRecordType r0 = javax.jmdns.impl.constants.DNSRecordType.TYPE_AAAA
            javax.jmdns.impl.constants.DNSRecordClass r2 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_IN
            java.util.Collection r8 = r5.getDNSEntryList(r8, r0, r2)
            java.util.Iterator r8 = r8.iterator()
        L_0x00d1:
            boolean r0 = r8.hasNext()
            if (r0 == 0) goto L_0x00e2
            java.lang.Object r0 = r8.next()
            javax.jmdns.impl.DNSEntry r0 = (javax.jmdns.impl.DNSEntry) r0
            r4.updateRecord(r5, r6, r0)
            goto L_0x00d1
        L_0x00e1:
            r1 = 1
        L_0x00e2:
            r2 = r1
            goto L_0x011d
        L_0x00e4:
            java.lang.String r5 = r8.getName()
            java.lang.String r6 = r4.getServer()
            boolean r5 = r5.equalsIgnoreCase(r6)
            if (r5 == 0) goto L_0x011c
            java.util.Set<java.net.Inet6Address> r5 = r4._ipv6Addresses
            javax.jmdns.impl.DNSRecord$Address r8 = (javax.jmdns.impl.DNSRecord.Address) r8
            java.net.InetAddress r6 = r8.getAddress()
            java.net.Inet6Address r6 = (java.net.Inet6Address) r6
            r5.add(r6)
            goto L_0x011d
        L_0x0100:
            java.lang.String r5 = r8.getName()
            java.lang.String r6 = r4.getServer()
            boolean r5 = r5.equalsIgnoreCase(r6)
            if (r5 == 0) goto L_0x011c
            java.util.Set<java.net.Inet4Address> r5 = r4._ipv4Addresses
            javax.jmdns.impl.DNSRecord$Address r8 = (javax.jmdns.impl.DNSRecord.Address) r8
            java.net.InetAddress r6 = r8.getAddress()
            java.net.Inet4Address r6 = (java.net.Inet4Address) r6
            r5.add(r6)
            goto L_0x011d
        L_0x011c:
            r2 = 0
        L_0x011d:
            if (r2 == 0) goto L_0x013b
            boolean r5 = r4.hasData()
            if (r5 == 0) goto L_0x013b
            javax.jmdns.impl.JmDNSImpl r5 = r4.getDns()
            if (r5 == 0) goto L_0x013b
            javax.jmdns.impl.ServiceEventImpl r6 = new javax.jmdns.impl.ServiceEventImpl
            java.lang.String r7 = r4.getType()
            java.lang.String r8 = r4.getName()
            r6.<init>(r5, r7, r8, r4)
            r5.handleServiceResolved(r6)
        L_0x013b:
            monitor-enter(r4)
            r4.notifyAll()     // Catch:{ all -> 0x0141 }
            monitor-exit(r4)     // Catch:{ all -> 0x0141 }
            goto L_0x0144
        L_0x0141:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0141 }
            throw r5
        L_0x0144:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.ServiceInfoImpl.updateRecord(javax.jmdns.impl.DNSCache, long, javax.jmdns.impl.DNSEntry):void");
    }

    public synchronized boolean hasData() {
        return getServer() != null && hasInetAddress() && getTextBytes() != null && getTextBytes().length > 0;
    }

    private final boolean hasInetAddress() {
        return this._ipv4Addresses.size() > 0 || this._ipv6Addresses.size() > 0;
    }

    public boolean advanceState(DNSTask dNSTask) {
        return this._state.advanceState(dNSTask);
    }

    public boolean revertState() {
        return this._state.revertState();
    }

    public boolean cancelState() {
        return this._state.cancelState();
    }

    public boolean closeState() {
        return this._state.closeState();
    }

    public boolean recoverState() {
        return this._state.recoverState();
    }

    public void removeAssociationWithTask(DNSTask dNSTask) {
        this._state.removeAssociationWithTask(dNSTask);
    }

    public void associateWithTask(DNSTask dNSTask, DNSState dNSState) {
        this._state.associateWithTask(dNSTask, dNSState);
    }

    public boolean isAssociatedWithTask(DNSTask dNSTask, DNSState dNSState) {
        return this._state.isAssociatedWithTask(dNSTask, dNSState);
    }

    public boolean isProbing() {
        return this._state.isProbing();
    }

    public boolean isAnnouncing() {
        return this._state.isAnnouncing();
    }

    public boolean isAnnounced() {
        return this._state.isAnnounced();
    }

    public boolean isCanceling() {
        return this._state.isCanceling();
    }

    public boolean isCanceled() {
        return this._state.isCanceled();
    }

    public boolean isClosing() {
        return this._state.isClosing();
    }

    public boolean isClosed() {
        return this._state.isClosed();
    }

    public boolean waitForAnnounced(long j) {
        return this._state.waitForAnnounced(j);
    }

    public boolean waitForCanceled(long j) {
        return this._state.waitForCanceled(j);
    }

    public int hashCode() {
        return getQualifiedName().hashCode();
    }

    public boolean equals(Object obj) {
        return (obj instanceof ServiceInfoImpl) && getQualifiedName().equals(((ServiceInfoImpl) obj).getQualifiedName());
    }

    public String getNiceTextString() {
        StringBuffer stringBuffer = new StringBuffer();
        int length = getTextBytes().length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (i >= 200) {
                stringBuffer.append("...");
                break;
            } else {
                byte b = getTextBytes()[i] & 255;
                if (b < 32 || b > Byte.MAX_VALUE) {
                    stringBuffer.append("\\0");
                    stringBuffer.append(Integer.toString(b, 8));
                } else {
                    stringBuffer.append((char) b);
                }
                i++;
            }
        }
        return stringBuffer.toString();
    }

    public ServiceInfoImpl clone() {
        ServiceInfoImpl serviceInfoImpl = new ServiceInfoImpl(getQualifiedNameMap(), this._port, this._weight, this._priority, this._persistent, this._text);
        for (Inet6Address add : getInet6Addresses()) {
            serviceInfoImpl._ipv6Addresses.add(add);
        }
        for (Inet4Address add2 : getInet4Addresses()) {
            serviceInfoImpl._ipv4Addresses.add(add2);
        }
        return serviceInfoImpl;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[");
        sb2.append(getClass().getSimpleName());
        sb2.append("@");
        sb2.append(System.identityHashCode(this));
        sb2.append(" ");
        sb.append(sb2.toString());
        sb.append("name: '");
        StringBuilder sb3 = new StringBuilder();
        String str2 = "";
        if (getName().length() > 0) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(getName());
            sb4.append(".");
            str = sb4.toString();
        } else {
            str = str2;
        }
        sb3.append(str);
        sb3.append(getTypeWithSubtype());
        sb.append(sb3.toString());
        sb.append("' address: '");
        InetAddress[] inetAddresses = getInetAddresses();
        if (inetAddresses.length > 0) {
            for (InetAddress append : inetAddresses) {
                sb.append(append);
                sb.append(':');
                sb.append(getPort());
                sb.append(' ');
            }
        } else {
            sb.append("(null):");
            sb.append(getPort());
        }
        sb.append("' status: '");
        sb.append(this._state.toString());
        sb.append(isPersistent() ? "' is persistent," : "',");
        sb.append(" has ");
        if (!hasData()) {
            str2 = "NO ";
        }
        sb.append(str2);
        sb.append("data");
        if (getTextBytes().length > 0) {
            Map properties = getProperties();
            if (!properties.isEmpty()) {
                String str3 = "\n";
                sb.append(str3);
                for (String str4 : properties.keySet()) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("\t");
                    sb5.append(str4);
                    sb5.append(": ");
                    sb5.append(new String((byte[]) properties.get(str4)));
                    sb5.append(str3);
                    sb.append(sb5.toString());
                }
            } else {
                sb.append(" empty");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public Collection<DNSRecord> answers(DNSRecordClass dNSRecordClass, boolean z, int i, HostInfo hostInfo) {
        DNSRecordClass dNSRecordClass2 = dNSRecordClass;
        ArrayList arrayList = new ArrayList();
        if (dNSRecordClass2 == DNSRecordClass.CLASS_ANY || dNSRecordClass2 == DNSRecordClass.CLASS_IN) {
            if (getSubtype().length() > 0) {
                Pointer pointer = new Pointer(getTypeWithSubtype(), DNSRecordClass.CLASS_IN, false, i, getQualifiedName());
                arrayList.add(pointer);
            }
            Pointer pointer2 = new Pointer(getType(), DNSRecordClass.CLASS_IN, false, i, getQualifiedName());
            arrayList.add(pointer2);
            String qualifiedName = getQualifiedName();
            DNSRecordClass dNSRecordClass3 = DNSRecordClass.CLASS_IN;
            int i2 = this._priority;
            int i3 = this._weight;
            Service service = new Service(qualifiedName, dNSRecordClass3, z, i, i2, i3, this._port, hostInfo.getName());
            arrayList.add(service);
            Text text = new Text(getQualifiedName(), DNSRecordClass.CLASS_IN, z, i, getTextBytes());
            arrayList.add(text);
        }
        return arrayList;
    }

    public void setText(byte[] bArr) throws IllegalStateException {
        synchronized (this) {
            this._text = bArr;
            this._props = null;
            setNeedTextAnnouncing(true);
        }
    }

    public void setText(Map<String, ?> map) throws IllegalStateException {
        setText(textFromProperties(map));
    }

    /* access modifiers changed from: 0000 */
    public void _setText(byte[] bArr) {
        this._text = bArr;
        this._props = null;
    }

    private static byte[] textFromProperties(Map<String, ?> map) {
        String str;
        byte[] bArr = null;
        if (map != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256);
                for (String str2 : map.keySet()) {
                    Object obj = map.get(str2);
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream(100);
                    writeUTF(byteArrayOutputStream2, str2);
                    if (obj != null) {
                        if (obj instanceof String) {
                            byteArrayOutputStream2.write(61);
                            writeUTF(byteArrayOutputStream2, (String) obj);
                        } else if (obj instanceof byte[]) {
                            byte[] bArr2 = (byte[]) obj;
                            if (bArr2.length > 0) {
                                byteArrayOutputStream2.write(61);
                                byteArrayOutputStream2.write(bArr2, 0, bArr2.length);
                            } else {
                                obj = null;
                            }
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("invalid property value: ");
                            sb.append(obj);
                            throw new IllegalArgumentException(sb.toString());
                        }
                    }
                    byte[] byteArray = byteArrayOutputStream2.toByteArray();
                    if (byteArray.length > 255) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Cannot have individual values larger that 255 chars. Offending value: ");
                        sb2.append(str2);
                        if (obj != null) {
                            str = "";
                        } else {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("=");
                            sb3.append(obj);
                            str = sb3.toString();
                        }
                        sb2.append(str);
                        throw new IOException(sb2.toString());
                    }
                    byteArrayOutputStream.write((byte) byteArray.length);
                    byteArrayOutputStream.write(byteArray, 0, byteArray.length);
                }
                bArr = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("unexpected exception: ");
                sb4.append(e);
                throw new RuntimeException(sb4.toString());
            }
        }
        return (bArr == null || bArr.length <= 0) ? DNSRecord.EMPTY_TXT : bArr;
    }

    public void setDns(JmDNSImpl jmDNSImpl) {
        this._state.setDns(jmDNSImpl);
    }

    public JmDNSImpl getDns() {
        return this._state.getDns();
    }

    public boolean isPersistent() {
        return this._persistent;
    }

    public void setNeedTextAnnouncing(boolean z) {
        this._needTextAnnouncing = z;
        if (this._needTextAnnouncing) {
            this._state.setTask(null);
        }
    }

    public boolean needTextAnnouncing() {
        return this._needTextAnnouncing;
    }

    /* access modifiers changed from: 0000 */
    public Delegate getDelegate() {
        return this._delegate;
    }

    /* access modifiers changed from: 0000 */
    public void setDelegate(Delegate delegate) {
        this._delegate = delegate;
    }
}
