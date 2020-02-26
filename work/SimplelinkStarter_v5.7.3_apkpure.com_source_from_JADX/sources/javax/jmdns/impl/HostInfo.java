package javax.jmdns.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.NetworkTopologyDiscovery.Factory;
import javax.jmdns.impl.DNSRecord.Address;
import javax.jmdns.impl.DNSRecord.IPv4Address;
import javax.jmdns.impl.DNSRecord.IPv6Address;
import javax.jmdns.impl.DNSRecord.Pointer;
import javax.jmdns.impl.DNSStatefulObject.DefaultImplementation;
import javax.jmdns.impl.NameRegister.NameType;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.constants.DNSState;
import javax.jmdns.impl.tasks.DNSTask;

public class HostInfo implements DNSStatefulObject {
    private static Logger logger = Logger.getLogger(HostInfo.class.getName());
    protected InetAddress _address;
    protected NetworkInterface _interfaze;
    protected String _name;
    private final HostInfoState _state;

    /* renamed from: javax.jmdns.impl.HostInfo$1 */
    static /* synthetic */ class C10801 {
        static final /* synthetic */ int[] $SwitchMap$javax$jmdns$impl$constants$DNSRecordType = new int[DNSRecordType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
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
                javax.jmdns.impl.constants.DNSRecordType r1 = javax.jmdns.impl.constants.DNSRecordType.TYPE_A6     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x002a }
                javax.jmdns.impl.constants.DNSRecordType r1 = javax.jmdns.impl.constants.DNSRecordType.TYPE_AAAA     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.HostInfo.C10801.<clinit>():void");
        }
    }

    private static final class HostInfoState extends DefaultImplementation {
        private static final long serialVersionUID = -8191476803620402088L;

        public HostInfoState(JmDNSImpl jmDNSImpl) {
            setDns(jmDNSImpl);
        }
    }

    public static HostInfo newHostInfo(InetAddress inetAddress, JmDNSImpl jmDNSImpl, String str) {
        InetAddress inetAddress2;
        String str2 = str != null ? str : "";
        if (inetAddress == null) {
            try {
                String property = System.getProperty("net.mdns.interface");
                if (property != null) {
                    inetAddress2 = InetAddress.getByName(property);
                } else {
                    inetAddress2 = InetAddress.getLocalHost();
                    if (inetAddress2.isLoopbackAddress()) {
                        InetAddress[] inetAddresses = Factory.getInstance().getInetAddresses();
                        if (inetAddresses.length > 0) {
                            inetAddress2 = inetAddresses[0];
                        }
                    }
                }
                if (inetAddress2.isLoopbackAddress()) {
                    logger.warning("Could not find any address beside the loopback.");
                }
            } catch (IOException e) {
                Logger logger2 = logger;
                Level level = Level.WARNING;
                StringBuilder sb = new StringBuilder();
                sb.append("Could not intialize the host network interface on ");
                sb.append(inetAddress);
                sb.append("because of an error: ");
                sb.append(e.getMessage());
                logger2.log(level, sb.toString(), e);
                inetAddress2 = loopbackAddress();
                if (str == null || str.length() <= 0) {
                    str = "computer";
                }
            }
        } else {
            inetAddress2 = inetAddress;
        }
        if (str2.length() == 0) {
            str2 = inetAddress2.getHostName();
        }
        if (str2.contains("in-addr.arpa") || str2.equals(inetAddress2.getHostAddress())) {
            if (str == null || str.length() <= 0) {
                str = inetAddress2.getHostAddress();
            }
            str2 = str;
        }
        int indexOf = str2.indexOf(".local");
        if (indexOf > 0) {
            str2 = str2.substring(0, indexOf);
        }
        String replace = str2.replace('.', '-');
        StringBuilder sb2 = new StringBuilder();
        sb2.append(replace);
        sb2.append(".local.");
        return new HostInfo(inetAddress2, sb2.toString(), jmDNSImpl);
    }

    private static InetAddress loopbackAddress() {
        try {
            return InetAddress.getByName(null);
        } catch (UnknownHostException unused) {
            return null;
        }
    }

    private HostInfo(InetAddress inetAddress, String str, JmDNSImpl jmDNSImpl) {
        this._state = new HostInfoState(jmDNSImpl);
        this._address = inetAddress;
        this._name = str;
        if (inetAddress != null) {
            try {
                this._interfaze = NetworkInterface.getByInetAddress(inetAddress);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "LocalHostInfo() exception ", e);
            }
        }
    }

    public String getName() {
        return this._name;
    }

    public InetAddress getInetAddress() {
        return this._address;
    }

    /* access modifiers changed from: 0000 */
    public Inet4Address getInet4Address() {
        if (getInetAddress() instanceof Inet4Address) {
            return (Inet4Address) this._address;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public Inet6Address getInet6Address() {
        if (getInetAddress() instanceof Inet6Address) {
            return (Inet6Address) this._address;
        }
        return null;
    }

    public NetworkInterface getInterface() {
        return this._interfaze;
    }

    public boolean conflictWithRecord(Address address) {
        Address dNSAddressRecord = getDNSAddressRecord(address.getRecordType(), address.isUnique(), DNSConstants.DNS_TTL);
        if (dNSAddressRecord == null || !dNSAddressRecord.sameType(address) || !dNSAddressRecord.sameName(address) || dNSAddressRecord.sameValue(address)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public synchronized String incrementHostName() {
        this._name = NameRegister.Factory.getRegistry().incrementName(getInetAddress(), this._name, NameType.HOST);
        return this._name;
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldIgnorePacket(DatagramPacket datagramPacket) {
        boolean z = false;
        if (getInetAddress() == null) {
            return false;
        }
        InetAddress address = datagramPacket.getAddress();
        if (address == null) {
            return false;
        }
        if ((getInetAddress().isLinkLocalAddress() || getInetAddress().isMCLinkLocal()) && !address.isLinkLocalAddress()) {
            z = true;
        }
        if (!address.isLoopbackAddress() || getInetAddress().isLoopbackAddress()) {
            return z;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public Address getDNSAddressRecord(DNSRecordType dNSRecordType, boolean z, int i) {
        int i2 = C10801.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType[dNSRecordType.ordinal()];
        if (i2 == 1) {
            return getDNS4AddressRecord(z, i);
        }
        if (i2 == 2 || i2 == 3) {
            return getDNS6AddressRecord(z, i);
        }
        return null;
    }

    private Address getDNS4AddressRecord(boolean z, int i) {
        if (!(getInetAddress() instanceof Inet4Address)) {
            return null;
        }
        IPv4Address iPv4Address = new IPv4Address(getName(), DNSRecordClass.CLASS_IN, z, i, getInetAddress());
        return iPv4Address;
    }

    private Address getDNS6AddressRecord(boolean z, int i) {
        if (!(getInetAddress() instanceof Inet6Address)) {
            return null;
        }
        IPv6Address iPv6Address = new IPv6Address(getName(), DNSRecordClass.CLASS_IN, z, i, getInetAddress());
        return iPv6Address;
    }

    /* access modifiers changed from: 0000 */
    public Pointer getDNSReverseAddressRecord(DNSRecordType dNSRecordType, boolean z, int i) {
        int i2 = C10801.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType[dNSRecordType.ordinal()];
        if (i2 == 1) {
            return getDNS4ReverseAddressRecord(z, i);
        }
        if (i2 == 2 || i2 == 3) {
            return getDNS6ReverseAddressRecord(z, i);
        }
        return null;
    }

    private Pointer getDNS4ReverseAddressRecord(boolean z, int i) {
        if (!(getInetAddress() instanceof Inet4Address)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getInetAddress().getHostAddress());
        sb.append(".in-addr.arpa.");
        Pointer pointer = new Pointer(sb.toString(), DNSRecordClass.CLASS_IN, z, i, getName());
        return pointer;
    }

    private Pointer getDNS6ReverseAddressRecord(boolean z, int i) {
        if (!(getInetAddress() instanceof Inet6Address)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getInetAddress().getHostAddress());
        sb.append(".ip6.arpa.");
        Pointer pointer = new Pointer(sb.toString(), DNSRecordClass.CLASS_IN, z, i, getName());
        return pointer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("local host info[");
        sb.append(getName() != null ? getName() : "no name");
        String str = ", ";
        sb.append(str);
        sb.append(getInterface() != null ? getInterface().getDisplayName() : "???");
        sb.append(":");
        sb.append(getInetAddress() != null ? getInetAddress().getHostAddress() : "no address");
        sb.append(str);
        sb.append(this._state);
        sb.append("]");
        return sb.toString();
    }

    public Collection<DNSRecord> answers(DNSRecordClass dNSRecordClass, boolean z, int i) {
        ArrayList arrayList = new ArrayList();
        Address dNS4AddressRecord = getDNS4AddressRecord(z, i);
        if (dNS4AddressRecord != null && dNS4AddressRecord.matchRecordClass(dNSRecordClass)) {
            arrayList.add(dNS4AddressRecord);
        }
        Address dNS6AddressRecord = getDNS6AddressRecord(z, i);
        if (dNS6AddressRecord != null && dNS6AddressRecord.matchRecordClass(dNSRecordClass)) {
            arrayList.add(dNS6AddressRecord);
        }
        return arrayList;
    }

    public JmDNSImpl getDns() {
        return this._state.getDns();
    }

    public boolean advanceState(DNSTask dNSTask) {
        return this._state.advanceState(dNSTask);
    }

    public void removeAssociationWithTask(DNSTask dNSTask) {
        this._state.removeAssociationWithTask(dNSTask);
    }

    public boolean revertState() {
        return this._state.revertState();
    }

    public void associateWithTask(DNSTask dNSTask, DNSState dNSState) {
        this._state.associateWithTask(dNSTask, dNSState);
    }

    public boolean isAssociatedWithTask(DNSTask dNSTask, DNSState dNSState) {
        return this._state.isAssociatedWithTask(dNSTask, dNSState);
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
        if (this._address == null) {
            return true;
        }
        return this._state.waitForCanceled(j);
    }
}
