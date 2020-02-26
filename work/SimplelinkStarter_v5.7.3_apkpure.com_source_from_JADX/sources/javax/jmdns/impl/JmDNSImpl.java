package javax.jmdns.impl;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import javax.jmdns.JmDNS.Delegate;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceInfo.Fields;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;
import javax.jmdns.impl.DNSRecord.Pointer;
import javax.jmdns.impl.DNSTaskStarter.Factory;
import javax.jmdns.impl.ListenerStatus.ServiceListenerStatus;
import javax.jmdns.impl.ListenerStatus.ServiceTypeListenerStatus;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.constants.DNSState;
import javax.jmdns.impl.tasks.DNSTask;
import javax.jmdns.impl.util.NamedThreadFactory;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.ClientCookie;

public class JmDNSImpl extends JmDNS implements DNSStatefulObject, DNSTaskStarter {
    private static final Random _random = new Random();
    private static Logger logger = Logger.getLogger(JmDNSImpl.class.getName());
    private final DNSCache _cache;
    private volatile Delegate _delegate;
    private final ExecutorService _executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("JmDNS"));
    private volatile InetAddress _group;
    private Thread _incomingListener;
    private final ReentrantLock _ioLock = new ReentrantLock();
    private long _lastThrottleIncrement;
    private final List<DNSListener> _listeners;
    private HostInfo _localHost;
    private final String _name;
    private DNSIncoming _plannedAnswer;
    private final Object _recoverLock = new Object();
    private final ConcurrentMap<String, ServiceCollector> _serviceCollectors;
    private final ConcurrentMap<String, List<ServiceListenerStatus>> _serviceListeners;
    private final ConcurrentMap<String, ServiceTypeEntry> _serviceTypes;
    private final ConcurrentMap<String, ServiceInfo> _services;
    protected Thread _shutdown;
    private volatile MulticastSocket _socket;
    private int _throttle;
    private final Set<ServiceTypeListenerStatus> _typeListeners;

    /* renamed from: javax.jmdns.impl.JmDNSImpl$7 */
    static /* synthetic */ class C10877 {
        static final /* synthetic */ int[] $SwitchMap$javax$jmdns$impl$JmDNSImpl$Operation = new int[Operation.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                javax.jmdns.impl.JmDNSImpl$Operation[] r0 = javax.jmdns.impl.JmDNSImpl.Operation.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$javax$jmdns$impl$JmDNSImpl$Operation = r0
                int[] r0 = $SwitchMap$javax$jmdns$impl$JmDNSImpl$Operation     // Catch:{ NoSuchFieldError -> 0x0014 }
                javax.jmdns.impl.JmDNSImpl$Operation r1 = javax.jmdns.impl.JmDNSImpl.Operation.Add     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$javax$jmdns$impl$JmDNSImpl$Operation     // Catch:{ NoSuchFieldError -> 0x001f }
                javax.jmdns.impl.JmDNSImpl$Operation r1 = javax.jmdns.impl.JmDNSImpl.Operation.Remove     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.JmDNSImpl.C10877.<clinit>():void");
        }
    }

    public enum Operation {
        Remove,
        Update,
        Add,
        RegisterServiceType,
        Noop
    }

    private static class ServiceCollector implements ServiceListener {
        private final ConcurrentMap<String, ServiceEvent> _events = new ConcurrentHashMap();
        private final ConcurrentMap<String, ServiceInfo> _infos = new ConcurrentHashMap();
        private volatile boolean _needToWaitForInfos;
        private final String _type;

        public ServiceCollector(String str) {
            this._type = str;
            this._needToWaitForInfos = true;
        }

        public void serviceAdded(ServiceEvent serviceEvent) {
            synchronized (this) {
                ServiceInfo info = serviceEvent.getInfo();
                if (info == null || !info.hasData()) {
                    ServiceInfoImpl resolveServiceInfo = ((JmDNSImpl) serviceEvent.getDNS()).resolveServiceInfo(serviceEvent.getType(), serviceEvent.getName(), info != null ? info.getSubtype() : "", true);
                    if (resolveServiceInfo != null) {
                        this._infos.put(serviceEvent.getName(), resolveServiceInfo);
                    } else {
                        this._events.put(serviceEvent.getName(), serviceEvent);
                    }
                } else {
                    this._infos.put(serviceEvent.getName(), info);
                }
            }
        }

        public void serviceRemoved(ServiceEvent serviceEvent) {
            synchronized (this) {
                this._infos.remove(serviceEvent.getName());
                this._events.remove(serviceEvent.getName());
            }
        }

        public void serviceResolved(ServiceEvent serviceEvent) {
            synchronized (this) {
                this._infos.put(serviceEvent.getName(), serviceEvent.getInfo());
                this._events.remove(serviceEvent.getName());
            }
        }

        public ServiceInfo[] list(long j) {
            if (this._infos.isEmpty() || !this._events.isEmpty() || this._needToWaitForInfos) {
                long j2 = j / 200;
                if (j2 < 1) {
                    j2 = 1;
                }
                for (int i = 0; ((long) i) < j2; i++) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException unused) {
                    }
                    if (this._events.isEmpty() && !this._infos.isEmpty() && !this._needToWaitForInfos) {
                        break;
                    }
                }
            }
            this._needToWaitForInfos = false;
            return (ServiceInfo[]) this._infos.values().toArray(new ServiceInfo[this._infos.size()]);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("\n\tType: ");
            stringBuffer.append(this._type);
            String str = ": ";
            if (this._infos.isEmpty()) {
                stringBuffer.append("\n\tNo services collected.");
            } else {
                stringBuffer.append("\n\tServices");
                for (String str2 : this._infos.keySet()) {
                    stringBuffer.append("\n\t\tService: ");
                    stringBuffer.append(str2);
                    stringBuffer.append(str);
                    stringBuffer.append(this._infos.get(str2));
                }
            }
            if (this._events.isEmpty()) {
                stringBuffer.append("\n\tNo event queued.");
            } else {
                stringBuffer.append("\n\tEvents");
                for (String str3 : this._events.keySet()) {
                    stringBuffer.append("\n\t\tEvent: ");
                    stringBuffer.append(str3);
                    stringBuffer.append(str);
                    stringBuffer.append(this._events.get(str3));
                }
            }
            return stringBuffer.toString();
        }
    }

    public static class ServiceTypeEntry extends AbstractMap<String, String> implements Cloneable {
        private final Set<Entry<String, String>> _entrySet = new HashSet();
        private final String _type;

        private static class SubTypeEntry implements Entry<String, String>, Serializable, Cloneable {
            private static final long serialVersionUID = 9188503522395855322L;
            private final String _key;
            private final String _value;

            public SubTypeEntry clone() {
                return this;
            }

            public SubTypeEntry(String str) {
                if (str == null) {
                    str = "";
                }
                this._value = str;
                this._key = this._value.toLowerCase();
            }

            public String getKey() {
                return this._key;
            }

            public String getValue() {
                return this._value;
            }

            public String setValue(String str) {
                throw new UnsupportedOperationException();
            }

            public boolean equals(Object obj) {
                boolean z = false;
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry entry = (Entry) obj;
                if (getKey().equals(entry.getKey()) && getValue().equals(entry.getValue())) {
                    z = true;
                }
                return z;
            }

            public int hashCode() {
                String str = this._key;
                int i = 0;
                int hashCode = str == null ? 0 : str.hashCode();
                String str2 = this._value;
                if (str2 != null) {
                    i = str2.hashCode();
                }
                return hashCode ^ i;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append(this._key);
                sb.append("=");
                sb.append(this._value);
                return sb.toString();
            }
        }

        public ServiceTypeEntry(String str) {
            this._type = str;
        }

        public String getType() {
            return this._type;
        }

        public Set<Entry<String, String>> entrySet() {
            return this._entrySet;
        }

        public boolean contains(String str) {
            return str != null && containsKey(str.toLowerCase());
        }

        public boolean add(String str) {
            if (str == null || contains(str)) {
                return false;
            }
            this._entrySet.add(new SubTypeEntry(str));
            return true;
        }

        public Iterator<String> iterator() {
            return keySet().iterator();
        }

        public ServiceTypeEntry clone() {
            ServiceTypeEntry serviceTypeEntry = new ServiceTypeEntry(getType());
            for (Entry value : entrySet()) {
                serviceTypeEntry.add((String) value.getValue());
            }
            return serviceTypeEntry;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(HttpStatus.SC_OK);
            if (isEmpty()) {
                sb.append("empty");
            } else {
                for (String append : values()) {
                    sb.append(append);
                    sb.append(", ");
                }
                sb.setLength(sb.length() - 2);
            }
            return sb.toString();
        }
    }

    protected class Shutdown implements Runnable {
        protected Shutdown() {
        }

        public void run() {
            try {
                JmDNSImpl.this._shutdown = null;
                JmDNSImpl.this.close();
            } catch (Throwable th) {
                PrintStream printStream = System.err;
                StringBuilder sb = new StringBuilder();
                sb.append("Error while shuting down. ");
                sb.append(th);
                printStream.println(sb.toString());
            }
        }
    }

    public JmDNSImpl getDns() {
        return this;
    }

    public static void main(String[] strArr) {
        String str;
        try {
            Properties properties = new Properties();
            properties.load(JmDNSImpl.class.getResourceAsStream("/META-INF/maven/javax.jmdns/jmdns/pom.properties"));
            str = properties.getProperty(ClientCookie.VERSION_ATTR);
        } catch (Exception unused) {
            str = "RUNNING.IN.IDE.FULL";
        }
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("JmDNS version \"");
        sb.append(str);
        String str2 = "\"";
        sb.append(str2);
        printStream.println(sb.toString());
        System.out.println(" ");
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Running on java version \"");
        sb2.append(System.getProperty("java.version"));
        sb2.append(str2);
        sb2.append(" (build ");
        sb2.append(System.getProperty("java.runtime.version"));
        sb2.append(")");
        sb2.append(" from ");
        sb2.append(System.getProperty("java.vendor"));
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Operating environment \"");
        sb3.append(System.getProperty("os.name"));
        sb3.append(str2);
        sb3.append(" version ");
        sb3.append(System.getProperty("os.version"));
        sb3.append(" on ");
        sb3.append(System.getProperty("os.arch"));
        printStream3.println(sb3.toString());
        System.out.println("For more information on JmDNS please visit https://sourceforge.net/projects/jmdns/");
    }

    public JmDNSImpl(InetAddress inetAddress, String str) throws IOException {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("JmDNS instance created");
        }
        this._cache = new DNSCache(100);
        this._listeners = Collections.synchronizedList(new ArrayList());
        this._serviceListeners = new ConcurrentHashMap();
        this._typeListeners = Collections.synchronizedSet(new HashSet());
        this._serviceCollectors = new ConcurrentHashMap();
        this._services = new ConcurrentHashMap(20);
        this._serviceTypes = new ConcurrentHashMap(20);
        this._localHost = HostInfo.newHostInfo(inetAddress, this, str);
        if (str == null) {
            str = this._localHost.getName();
        }
        this._name = str;
        openMulticastSocket(getLocalHost());
        start(getServices().values());
        startReaper();
    }

    private void start(Collection<? extends ServiceInfo> collection) {
        if (this._incomingListener == null) {
            this._incomingListener = new SocketListener(this);
            this._incomingListener.start();
        }
        startProber();
        for (ServiceInfo serviceInfoImpl : collection) {
            try {
                registerService(new ServiceInfoImpl(serviceInfoImpl));
            } catch (Exception e) {
                logger.log(Level.WARNING, "start() Registration exception ", e);
            }
        }
    }

    private void openMulticastSocket(HostInfo hostInfo) throws IOException {
        if (this._group == null) {
            if (hostInfo.getInetAddress() instanceof Inet6Address) {
                this._group = InetAddress.getByName(DNSConstants.MDNS_GROUP_IPV6);
            } else {
                this._group = InetAddress.getByName(DNSConstants.MDNS_GROUP);
            }
        }
        if (this._socket != null) {
            closeMulticastSocket();
        }
        this._socket = new MulticastSocket(DNSConstants.MDNS_PORT);
        if (!(hostInfo == null || hostInfo.getInterface() == null)) {
            try {
                this._socket.setNetworkInterface(hostInfo.getInterface());
            } catch (SocketException e) {
                if (logger.isLoggable(Level.FINE)) {
                    Logger logger2 = logger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("openMulticastSocket() Set network interface exception: ");
                    sb.append(e.getMessage());
                    logger2.fine(sb.toString());
                }
            }
        }
        this._socket.setTimeToLive(255);
        this._socket.joinGroup(this._group);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(9:5|6|7|9|10|(3:31|28|11)|32|34|37) */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005e, code lost:
        logger.log(java.util.logging.Level.WARNING, "closeMulticastSocket() Close socket exception ", r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001e, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x0057 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0020 */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0032 A[SYNTHETIC, Splitter:B:16:0x0032] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void closeMulticastSocket() {
        /*
            r5 = this;
            java.util.logging.Logger r0 = logger
            java.util.logging.Level r1 = java.util.logging.Level.FINER
            boolean r0 = r0.isLoggable(r1)
            if (r0 == 0) goto L_0x0011
            java.util.logging.Logger r0 = logger
            java.lang.String r1 = "closeMulticastSocket()"
            r0.finer(r1)
        L_0x0011:
            java.net.MulticastSocket r0 = r5._socket
            if (r0 == 0) goto L_0x0069
            r0 = 0
            java.net.MulticastSocket r1 = r5._socket     // Catch:{ SocketException -> 0x0020 }
            java.net.InetAddress r2 = r5._group     // Catch:{ SocketException -> 0x0020 }
            r1.leaveGroup(r2)     // Catch:{ SocketException -> 0x0020 }
            goto L_0x0020
        L_0x001e:
            r1 = move-exception
            goto L_0x005e
        L_0x0020:
            java.net.MulticastSocket r1 = r5._socket     // Catch:{ Exception -> 0x001e }
            r1.close()     // Catch:{ Exception -> 0x001e }
        L_0x0025:
            java.lang.Thread r1 = r5._incomingListener     // Catch:{ Exception -> 0x001e }
            if (r1 == 0) goto L_0x005b
            java.lang.Thread r1 = r5._incomingListener     // Catch:{ Exception -> 0x001e }
            boolean r1 = r1.isAlive()     // Catch:{ Exception -> 0x001e }
            if (r1 == 0) goto L_0x005b
            monitor-enter(r5)     // Catch:{ Exception -> 0x001e }
            java.lang.Thread r1 = r5._incomingListener     // Catch:{ InterruptedException -> 0x0057 }
            if (r1 == 0) goto L_0x0057
            java.lang.Thread r1 = r5._incomingListener     // Catch:{ InterruptedException -> 0x0057 }
            boolean r1 = r1.isAlive()     // Catch:{ InterruptedException -> 0x0057 }
            if (r1 == 0) goto L_0x0057
            java.util.logging.Logger r1 = logger     // Catch:{ InterruptedException -> 0x0057 }
            java.util.logging.Level r2 = java.util.logging.Level.FINER     // Catch:{ InterruptedException -> 0x0057 }
            boolean r1 = r1.isLoggable(r2)     // Catch:{ InterruptedException -> 0x0057 }
            if (r1 == 0) goto L_0x004f
            java.util.logging.Logger r1 = logger     // Catch:{ InterruptedException -> 0x0057 }
            java.lang.String r2 = "closeMulticastSocket(): waiting for jmDNS monitor"
            r1.finer(r2)     // Catch:{ InterruptedException -> 0x0057 }
        L_0x004f:
            r1 = 1000(0x3e8, double:4.94E-321)
            r5.wait(r1)     // Catch:{ InterruptedException -> 0x0057 }
            goto L_0x0057
        L_0x0055:
            r1 = move-exception
            goto L_0x0059
        L_0x0057:
            monitor-exit(r5)     // Catch:{ all -> 0x0055 }
            goto L_0x0025
        L_0x0059:
            monitor-exit(r5)     // Catch:{ all -> 0x0055 }
            throw r1     // Catch:{ Exception -> 0x001e }
        L_0x005b:
            r5._incomingListener = r0     // Catch:{ Exception -> 0x001e }
            goto L_0x0067
        L_0x005e:
            java.util.logging.Logger r2 = logger
            java.util.logging.Level r3 = java.util.logging.Level.WARNING
            java.lang.String r4 = "closeMulticastSocket() Close socket exception "
            r2.log(r3, r4, r1)
        L_0x0067:
            r5._socket = r0
        L_0x0069:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.JmDNSImpl.closeMulticastSocket():void");
    }

    public boolean advanceState(DNSTask dNSTask) {
        return this._localHost.advanceState(dNSTask);
    }

    public boolean revertState() {
        return this._localHost.revertState();
    }

    public boolean cancelState() {
        return this._localHost.cancelState();
    }

    public boolean closeState() {
        return this._localHost.closeState();
    }

    public boolean recoverState() {
        return this._localHost.recoverState();
    }

    public void associateWithTask(DNSTask dNSTask, DNSState dNSState) {
        this._localHost.associateWithTask(dNSTask, dNSState);
    }

    public void removeAssociationWithTask(DNSTask dNSTask) {
        this._localHost.removeAssociationWithTask(dNSTask);
    }

    public boolean isAssociatedWithTask(DNSTask dNSTask, DNSState dNSState) {
        return this._localHost.isAssociatedWithTask(dNSTask, dNSState);
    }

    public boolean isProbing() {
        return this._localHost.isProbing();
    }

    public boolean isAnnouncing() {
        return this._localHost.isAnnouncing();
    }

    public boolean isAnnounced() {
        return this._localHost.isAnnounced();
    }

    public boolean isCanceling() {
        return this._localHost.isCanceling();
    }

    public boolean isCanceled() {
        return this._localHost.isCanceled();
    }

    public boolean isClosing() {
        return this._localHost.isClosing();
    }

    public boolean isClosed() {
        return this._localHost.isClosed();
    }

    public boolean waitForAnnounced(long j) {
        return this._localHost.waitForAnnounced(j);
    }

    public boolean waitForCanceled(long j) {
        return this._localHost.waitForCanceled(j);
    }

    public DNSCache getCache() {
        return this._cache;
    }

    public String getName() {
        return this._name;
    }

    public String getHostName() {
        return this._localHost.getName();
    }

    public HostInfo getLocalHost() {
        return this._localHost;
    }

    public InetAddress getInetAddress() throws IOException {
        return this._localHost.getInetAddress();
    }

    @Deprecated
    public InetAddress getInterface() throws IOException {
        return this._socket.getInterface();
    }

    public ServiceInfo getServiceInfo(String str, String str2) {
        return getServiceInfo(str, str2, false, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public ServiceInfo getServiceInfo(String str, String str2, long j) {
        return getServiceInfo(str, str2, false, j);
    }

    public ServiceInfo getServiceInfo(String str, String str2, boolean z) {
        return getServiceInfo(str, str2, z, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public ServiceInfo getServiceInfo(String str, String str2, boolean z, long j) {
        ServiceInfoImpl resolveServiceInfo = resolveServiceInfo(str, str2, "", z);
        waitForInfoData(resolveServiceInfo, j);
        if (resolveServiceInfo.hasData()) {
            return resolveServiceInfo;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public ServiceInfoImpl resolveServiceInfo(String str, String str2, String str3, boolean z) {
        cleanCache();
        String lowerCase = str.toLowerCase();
        registerServiceType(str);
        if (this._serviceCollectors.putIfAbsent(lowerCase, new ServiceCollector(str)) == null) {
            addServiceListener(lowerCase, (ServiceListener) this._serviceCollectors.get(lowerCase), true);
        }
        ServiceInfoImpl serviceInfoFromCache = getServiceInfoFromCache(str, str2, str3, z);
        startServiceInfoResolver(serviceInfoFromCache);
        return serviceInfoFromCache;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00bb A[EDGE_INSN: B:44:0x00bb->B:21:0x00bb ?: BREAK  
    EDGE_INSN: B:44:0x00bb->B:21:0x00bb ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public javax.jmdns.impl.ServiceInfoImpl getServiceInfoFromCache(java.lang.String r14, java.lang.String r15, java.lang.String r16, boolean r17) {
        /*
            r13 = this;
            r9 = r17
            javax.jmdns.impl.ServiceInfoImpl r10 = new javax.jmdns.impl.ServiceInfoImpl
            r11 = 0
            r12 = r11
            byte[] r12 = (byte[]) r12
            r4 = 0
            r5 = 0
            r6 = 0
            r0 = r10
            r1 = r14
            r2 = r15
            r3 = r16
            r7 = r17
            r8 = r12
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            javax.jmdns.impl.DNSCache r0 = r13.getCache()
            javax.jmdns.impl.DNSRecord$Pointer r7 = new javax.jmdns.impl.DNSRecord$Pointer
            javax.jmdns.impl.constants.DNSRecordClass r3 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_ANY
            java.lang.String r6 = r10.getQualifiedName()
            r1 = r7
            r2 = r14
            r1.<init>(r2, r3, r4, r5, r6)
            javax.jmdns.impl.DNSEntry r0 = r0.getDNSEntry(r7)
            boolean r1 = r0 instanceof javax.jmdns.impl.DNSRecord
            if (r1 == 0) goto L_0x012f
            javax.jmdns.impl.DNSRecord r0 = (javax.jmdns.impl.DNSRecord) r0
            javax.jmdns.ServiceInfo r0 = r0.getServiceInfo(r9)
            javax.jmdns.impl.ServiceInfoImpl r0 = (javax.jmdns.impl.ServiceInfoImpl) r0
            if (r0 == 0) goto L_0x012f
            java.util.Map r1 = r0.getQualifiedNameMap()
            javax.jmdns.impl.DNSCache r2 = r13.getCache()
            java.lang.String r3 = r10.getQualifiedName()
            javax.jmdns.impl.constants.DNSRecordType r4 = javax.jmdns.impl.constants.DNSRecordType.TYPE_SRV
            javax.jmdns.impl.constants.DNSRecordClass r5 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_ANY
            javax.jmdns.impl.DNSEntry r2 = r2.getDNSEntry(r3, r4, r5)
            boolean r3 = r2 instanceof javax.jmdns.impl.DNSRecord
            if (r3 == 0) goto L_0x0077
            javax.jmdns.impl.DNSRecord r2 = (javax.jmdns.impl.DNSRecord) r2
            javax.jmdns.ServiceInfo r7 = r2.getServiceInfo(r9)
            if (r7 == 0) goto L_0x0077
            javax.jmdns.impl.ServiceInfoImpl r8 = new javax.jmdns.impl.ServiceInfoImpl
            int r2 = r7.getPort()
            int r3 = r7.getWeight()
            int r4 = r7.getPriority()
            r0 = r8
            r5 = r17
            r6 = r12
            r0.<init>(r1, r2, r3, r4, r5, r6)
            byte[] r11 = r7.getTextBytes()
            java.lang.String r0 = r7.getServer()
            goto L_0x007b
        L_0x0077:
            java.lang.String r1 = ""
            r8 = r0
            r0 = r1
        L_0x007b:
            javax.jmdns.impl.DNSCache r1 = r13.getCache()
            javax.jmdns.impl.constants.DNSRecordType r2 = javax.jmdns.impl.constants.DNSRecordType.TYPE_A
            javax.jmdns.impl.constants.DNSRecordClass r3 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_ANY
            java.util.Collection r1 = r1.getDNSEntryList(r0, r2, r3)
            java.util.Iterator r1 = r1.iterator()
        L_0x008b:
            boolean r2 = r1.hasNext()
            r3 = 0
            if (r2 == 0) goto L_0x00bb
            java.lang.Object r2 = r1.next()
            javax.jmdns.impl.DNSEntry r2 = (javax.jmdns.impl.DNSEntry) r2
            boolean r4 = r2 instanceof javax.jmdns.impl.DNSRecord
            if (r4 == 0) goto L_0x008b
            javax.jmdns.impl.DNSRecord r2 = (javax.jmdns.impl.DNSRecord) r2
            javax.jmdns.ServiceInfo r2 = r2.getServiceInfo(r9)
            if (r2 == 0) goto L_0x008b
            java.net.Inet4Address[] r4 = r2.getInet4Addresses()
            int r5 = r4.length
        L_0x00a9:
            if (r3 >= r5) goto L_0x00b3
            r6 = r4[r3]
            r8.addAddress(r6)
            int r3 = r3 + 1
            goto L_0x00a9
        L_0x00b3:
            byte[] r2 = r2.getTextBytes()
            r8._setText(r2)
            goto L_0x008b
        L_0x00bb:
            javax.jmdns.impl.DNSCache r1 = r13.getCache()
            javax.jmdns.impl.constants.DNSRecordType r2 = javax.jmdns.impl.constants.DNSRecordType.TYPE_AAAA
            javax.jmdns.impl.constants.DNSRecordClass r4 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_ANY
            java.util.Collection r0 = r1.getDNSEntryList(r0, r2, r4)
            java.util.Iterator r0 = r0.iterator()
        L_0x00cb:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x00fb
            java.lang.Object r1 = r0.next()
            javax.jmdns.impl.DNSEntry r1 = (javax.jmdns.impl.DNSEntry) r1
            boolean r2 = r1 instanceof javax.jmdns.impl.DNSRecord
            if (r2 == 0) goto L_0x00cb
            javax.jmdns.impl.DNSRecord r1 = (javax.jmdns.impl.DNSRecord) r1
            javax.jmdns.ServiceInfo r1 = r1.getServiceInfo(r9)
            if (r1 == 0) goto L_0x00cb
            java.net.Inet6Address[] r2 = r1.getInet6Addresses()
            int r4 = r2.length
            r5 = 0
        L_0x00e9:
            if (r5 >= r4) goto L_0x00f3
            r6 = r2[r5]
            r8.addAddress(r6)
            int r5 = r5 + 1
            goto L_0x00e9
        L_0x00f3:
            byte[] r1 = r1.getTextBytes()
            r8._setText(r1)
            goto L_0x00cb
        L_0x00fb:
            javax.jmdns.impl.DNSCache r0 = r13.getCache()
            java.lang.String r1 = r8.getQualifiedName()
            javax.jmdns.impl.constants.DNSRecordType r2 = javax.jmdns.impl.constants.DNSRecordType.TYPE_TXT
            javax.jmdns.impl.constants.DNSRecordClass r3 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_ANY
            javax.jmdns.impl.DNSEntry r0 = r0.getDNSEntry(r1, r2, r3)
            boolean r1 = r0 instanceof javax.jmdns.impl.DNSRecord
            if (r1 == 0) goto L_0x011e
            javax.jmdns.impl.DNSRecord r0 = (javax.jmdns.impl.DNSRecord) r0
            javax.jmdns.ServiceInfo r0 = r0.getServiceInfo(r9)
            if (r0 == 0) goto L_0x011e
            byte[] r0 = r0.getTextBytes()
            r8._setText(r0)
        L_0x011e:
            byte[] r0 = r8.getTextBytes()
            int r0 = r0.length
            if (r0 != 0) goto L_0x0128
            r8._setText(r11)
        L_0x0128:
            boolean r0 = r8.hasData()
            if (r0 == 0) goto L_0x012f
            goto L_0x0130
        L_0x012f:
            r8 = r10
        L_0x0130:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.JmDNSImpl.getServiceInfoFromCache(java.lang.String, java.lang.String, java.lang.String, boolean):javax.jmdns.impl.ServiceInfoImpl");
    }

    private void waitForInfoData(ServiceInfo serviceInfo, long j) {
        synchronized (serviceInfo) {
            long j2 = j / 200;
            if (j2 < 1) {
                j2 = 1;
            }
            for (int i = 0; ((long) i) < j2 && !serviceInfo.hasData(); i++) {
                try {
                    serviceInfo.wait(200);
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    public void requestServiceInfo(String str, String str2) {
        requestServiceInfo(str, str2, false, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public void requestServiceInfo(String str, String str2, boolean z) {
        requestServiceInfo(str, str2, z, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public void requestServiceInfo(String str, String str2, long j) {
        requestServiceInfo(str, str2, false, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public void requestServiceInfo(String str, String str2, boolean z, long j) {
        waitForInfoData(resolveServiceInfo(str, str2, "", z), j);
    }

    /* access modifiers changed from: 0000 */
    public void handleServiceResolved(final ServiceEvent serviceEvent) {
        ArrayList<ServiceListenerStatus> arrayList;
        List list = (List) this._serviceListeners.get(serviceEvent.getType().toLowerCase());
        if (list != null && !list.isEmpty() && serviceEvent.getInfo() != null && serviceEvent.getInfo().hasData()) {
            synchronized (list) {
                arrayList = new ArrayList<>(list);
            }
            for (final ServiceListenerStatus serviceListenerStatus : arrayList) {
                this._executor.submit(new Runnable() {
                    public void run() {
                        serviceListenerStatus.serviceResolved(serviceEvent);
                    }
                });
            }
        }
    }

    public void addServiceTypeListener(ServiceTypeListener serviceTypeListener) throws IOException {
        ServiceTypeListenerStatus serviceTypeListenerStatus = new ServiceTypeListenerStatus(serviceTypeListener, false);
        this._typeListeners.add(serviceTypeListenerStatus);
        for (String serviceEventImpl : this._serviceTypes.keySet()) {
            serviceTypeListenerStatus.serviceTypeAdded(new ServiceEventImpl(this, serviceEventImpl, "", null));
        }
        startTypeResolver();
    }

    public void removeServiceTypeListener(ServiceTypeListener serviceTypeListener) {
        this._typeListeners.remove(new ServiceTypeListenerStatus(serviceTypeListener, false));
    }

    public void addServiceListener(String str, ServiceListener serviceListener) {
        addServiceListener(str, serviceListener, false);
    }

    private void addServiceListener(String str, ServiceListener serviceListener, boolean z) {
        ServiceListenerStatus serviceListenerStatus = new ServiceListenerStatus(serviceListener, z);
        String lowerCase = str.toLowerCase();
        List list = (List) this._serviceListeners.get(lowerCase);
        if (list == null) {
            if (this._serviceListeners.putIfAbsent(lowerCase, new LinkedList()) == null && this._serviceCollectors.putIfAbsent(lowerCase, new ServiceCollector(str)) == null) {
                addServiceListener(lowerCase, (ServiceListener) this._serviceCollectors.get(lowerCase), true);
            }
            list = (List) this._serviceListeners.get(lowerCase);
        }
        if (list != null) {
            synchronized (list) {
                if (!list.contains(serviceListener)) {
                    list.add(serviceListenerStatus);
                }
            }
        }
        ArrayList<ServiceEvent> arrayList = new ArrayList<>();
        for (DNSEntry dNSEntry : getCache().allValues()) {
            DNSRecord dNSRecord = (DNSRecord) dNSEntry;
            if (dNSRecord.getRecordType() == DNSRecordType.TYPE_SRV && dNSRecord.getKey().endsWith(lowerCase)) {
                arrayList.add(new ServiceEventImpl(this, dNSRecord.getType(), toUnqualifiedName(dNSRecord.getType(), dNSRecord.getName()), dNSRecord.getServiceInfo()));
            }
        }
        for (ServiceEvent serviceAdded : arrayList) {
            serviceListenerStatus.serviceAdded(serviceAdded);
        }
        startServiceResolver(str);
    }

    public void removeServiceListener(String str, ServiceListener serviceListener) {
        String lowerCase = str.toLowerCase();
        List list = (List) this._serviceListeners.get(lowerCase);
        if (list != null) {
            synchronized (list) {
                list.remove(new ServiceListenerStatus(serviceListener, false));
                if (list.isEmpty()) {
                    this._serviceListeners.remove(lowerCase, list);
                }
            }
        }
    }

    public void registerService(ServiceInfo serviceInfo) throws IOException {
        if (isClosing() || isClosed()) {
            throw new IllegalStateException("This DNS is closed.");
        }
        ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) serviceInfo;
        if (serviceInfoImpl.getDns() != null) {
            if (serviceInfoImpl.getDns() != this) {
                throw new IllegalStateException("A service information can only be registered with a single instamce of JmDNS.");
            } else if (this._services.get(serviceInfoImpl.getKey()) != null) {
                throw new IllegalStateException("A service information can only be registered once.");
            }
        }
        serviceInfoImpl.setDns(this);
        registerServiceType(serviceInfoImpl.getTypeWithSubtype());
        serviceInfoImpl.recoverState();
        serviceInfoImpl.setServer(this._localHost.getName());
        serviceInfoImpl.addAddress(this._localHost.getInet4Address());
        serviceInfoImpl.addAddress(this._localHost.getInet6Address());
        waitForAnnounced(DNSConstants.SERVICE_INFO_TIMEOUT);
        makeServiceNameUnique(serviceInfoImpl);
        while (this._services.putIfAbsent(serviceInfoImpl.getKey(), serviceInfoImpl) != null) {
            makeServiceNameUnique(serviceInfoImpl);
        }
        startProber();
        serviceInfoImpl.waitForAnnounced(DNSConstants.SERVICE_INFO_TIMEOUT);
        if (logger.isLoggable(Level.FINE)) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("registerService() JmDNS registered service as ");
            sb.append(serviceInfoImpl);
            logger2.fine(sb.toString());
        }
    }

    public void unregisterService(ServiceInfo serviceInfo) {
        ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) this._services.get(serviceInfo.getKey());
        if (serviceInfoImpl != null) {
            serviceInfoImpl.cancelState();
            startCanceler();
            serviceInfoImpl.waitForCanceled(DNSConstants.CLOSE_TIMEOUT);
            this._services.remove(serviceInfoImpl.getKey(), serviceInfoImpl);
            if (logger.isLoggable(Level.FINE)) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("unregisterService() JmDNS ");
                sb.append(getName());
                sb.append(" unregistered service as ");
                sb.append(serviceInfoImpl);
                logger2.fine(sb.toString());
                return;
            }
            return;
        }
        Logger logger3 = logger;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getName());
        sb2.append(" removing unregistered service info: ");
        sb2.append(serviceInfo.getKey());
        logger3.warning(sb2.toString());
    }

    public void unregisterAllServices() {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("unregisterAllServices()");
        }
        for (String str : this._services.keySet()) {
            ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) this._services.get(str);
            if (serviceInfoImpl != null) {
                if (logger.isLoggable(Level.FINER)) {
                    Logger logger2 = logger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cancelling service info: ");
                    sb.append(serviceInfoImpl);
                    logger2.finer(sb.toString());
                }
                serviceInfoImpl.cancelState();
            }
        }
        startCanceler();
        for (String str2 : this._services.keySet()) {
            ServiceInfoImpl serviceInfoImpl2 = (ServiceInfoImpl) this._services.get(str2);
            if (serviceInfoImpl2 != null) {
                if (logger.isLoggable(Level.FINER)) {
                    Logger logger3 = logger;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Wait for service info cancel: ");
                    sb2.append(serviceInfoImpl2);
                    logger3.finer(sb2.toString());
                }
                serviceInfoImpl2.waitForCanceled(DNSConstants.CLOSE_TIMEOUT);
                this._services.remove(str2, serviceInfoImpl2);
            }
        }
    }

    public boolean registerServiceType(String str) {
        String str2;
        String str3;
        boolean z;
        String str4;
        Map decodeQualifiedNameMapForType = ServiceInfoImpl.decodeQualifiedNameMapForType(str);
        String str5 = (String) decodeQualifiedNameMapForType.get(Fields.Domain);
        String str6 = (String) decodeQualifiedNameMapForType.get(Fields.Protocol);
        String str7 = (String) decodeQualifiedNameMapForType.get(Fields.Application);
        String str8 = (String) decodeQualifiedNameMapForType.get(Fields.Subtype);
        StringBuilder sb = new StringBuilder();
        if (str7.length() > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("_");
            sb2.append(str7);
            sb2.append(".");
            str2 = sb2.toString();
        } else {
            str2 = "";
        }
        sb.append(str2);
        if (str6.length() > 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("_");
            sb3.append(str6);
            sb3.append(".");
            str3 = sb3.toString();
        } else {
            str3 = "";
        }
        sb.append(str3);
        sb.append(str5);
        sb.append(".");
        String sb4 = sb.toString();
        String lowerCase = sb4.toLowerCase();
        if (logger.isLoggable(Level.FINE)) {
            Logger logger2 = logger;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(getName());
            sb5.append(".registering service type: ");
            sb5.append(str);
            sb5.append(" as: ");
            sb5.append(sb4);
            if (str8.length() > 0) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(" subtype: ");
                sb6.append(str8);
                str4 = sb6.toString();
            } else {
                str4 = "";
            }
            sb5.append(str4);
            logger2.fine(sb5.toString());
        }
        if (this._serviceTypes.containsKey(lowerCase) || str7.toLowerCase().equals("dns-sd") || str5.toLowerCase().endsWith("in-addr.arpa") || str5.toLowerCase().endsWith("ip6.arpa")) {
            z = false;
        } else {
            z = this._serviceTypes.putIfAbsent(lowerCase, new ServiceTypeEntry(sb4)) == null;
            if (z) {
                Set<ServiceTypeListenerStatus> set = this._typeListeners;
                ServiceTypeListenerStatus[] serviceTypeListenerStatusArr = (ServiceTypeListenerStatus[]) set.toArray(new ServiceTypeListenerStatus[set.size()]);
                final ServiceEventImpl serviceEventImpl = new ServiceEventImpl(this, sb4, "", null);
                for (final ServiceTypeListenerStatus serviceTypeListenerStatus : serviceTypeListenerStatusArr) {
                    this._executor.submit(new Runnable() {
                        public void run() {
                            serviceTypeListenerStatus.serviceTypeAdded(serviceEventImpl);
                        }
                    });
                }
            }
        }
        if (str8.length() > 0) {
            ServiceTypeEntry serviceTypeEntry = (ServiceTypeEntry) this._serviceTypes.get(lowerCase);
            if (serviceTypeEntry != null && !serviceTypeEntry.contains(str8)) {
                synchronized (serviceTypeEntry) {
                    if (!serviceTypeEntry.contains(str8)) {
                        serviceTypeEntry.add(str8);
                        ServiceTypeListenerStatus[] serviceTypeListenerStatusArr2 = (ServiceTypeListenerStatus[]) this._typeListeners.toArray(new ServiceTypeListenerStatus[this._typeListeners.size()]);
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("_");
                        sb7.append(str8);
                        sb7.append("._sub.");
                        sb7.append(sb4);
                        final ServiceEventImpl serviceEventImpl2 = new ServiceEventImpl(this, sb7.toString(), "", null);
                        for (final ServiceTypeListenerStatus serviceTypeListenerStatus2 : serviceTypeListenerStatusArr2) {
                            this._executor.submit(new Runnable() {
                                public void run() {
                                    serviceTypeListenerStatus2.subTypeForServiceTypeAdded(serviceEventImpl2);
                                }
                            });
                        }
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005d, code lost:
        if (logger.isLoggable(java.util.logging.Level.FINER) == false) goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x005f, code lost:
        r3 = logger;
        r4 = new java.lang.StringBuilder();
        r4.append("makeServiceNameUnique() JmDNS.makeServiceNameUnique srv collision:");
        r4.append(r5);
        r4.append(" s.server=");
        r4.append(r7.getServer());
        r4.append(" ");
        r4.append(r10._localHost.getName());
        r4.append(" equals:");
        r4.append(r7.getServer().equals(r10._localHost.getName()));
        r3.finer(r4.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x00a5, code lost:
        r11.setName(javax.jmdns.impl.NameRegister.Factory.getRegistry().incrementName(r10._localHost.getInetAddress(), r11.getName(), javax.jmdns.impl.NameRegister.NameType.SERVICE));
        r3 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean makeServiceNameUnique(javax.jmdns.impl.ServiceInfoImpl r11) {
        /*
            r10 = this;
            java.lang.String r0 = r11.getKey()
            long r1 = java.lang.System.currentTimeMillis()
        L_0x0008:
            r3 = 0
            javax.jmdns.impl.DNSCache r4 = r10.getCache()
            java.lang.String r5 = r11.getKey()
            java.util.Collection r4 = r4.getDNSEntryList(r5)
            java.util.Iterator r4 = r4.iterator()
        L_0x0019:
            boolean r5 = r4.hasNext()
            r6 = 1
            if (r5 == 0) goto L_0x00bd
            java.lang.Object r5 = r4.next()
            javax.jmdns.impl.DNSEntry r5 = (javax.jmdns.impl.DNSEntry) r5
            javax.jmdns.impl.constants.DNSRecordType r7 = javax.jmdns.impl.constants.DNSRecordType.TYPE_SRV
            javax.jmdns.impl.constants.DNSRecordType r8 = r5.getRecordType()
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x0019
            boolean r7 = r5.isExpired(r1)
            if (r7 != 0) goto L_0x0019
            r7 = r5
            javax.jmdns.impl.DNSRecord$Service r7 = (javax.jmdns.impl.DNSRecord.Service) r7
            int r8 = r7.getPort()
            int r9 = r11.getPort()
            if (r8 != r9) goto L_0x0055
            java.lang.String r8 = r7.getServer()
            javax.jmdns.impl.HostInfo r9 = r10._localHost
            java.lang.String r9 = r9.getName()
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L_0x0019
        L_0x0055:
            java.util.logging.Logger r3 = logger
            java.util.logging.Level r4 = java.util.logging.Level.FINER
            boolean r3 = r3.isLoggable(r4)
            if (r3 == 0) goto L_0x00a5
            java.util.logging.Logger r3 = logger
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r8 = "makeServiceNameUnique() JmDNS.makeServiceNameUnique srv collision:"
            r4.append(r8)
            r4.append(r5)
            java.lang.String r5 = " s.server="
            r4.append(r5)
            java.lang.String r5 = r7.getServer()
            r4.append(r5)
            java.lang.String r5 = " "
            r4.append(r5)
            javax.jmdns.impl.HostInfo r5 = r10._localHost
            java.lang.String r5 = r5.getName()
            r4.append(r5)
            java.lang.String r5 = " equals:"
            r4.append(r5)
            java.lang.String r5 = r7.getServer()
            javax.jmdns.impl.HostInfo r7 = r10._localHost
            java.lang.String r7 = r7.getName()
            boolean r5 = r5.equals(r7)
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r3.finer(r4)
        L_0x00a5:
            javax.jmdns.impl.NameRegister r3 = javax.jmdns.impl.NameRegister.Factory.getRegistry()
            javax.jmdns.impl.HostInfo r4 = r10._localHost
            java.net.InetAddress r4 = r4.getInetAddress()
            java.lang.String r5 = r11.getName()
            javax.jmdns.impl.NameRegister$NameType r7 = javax.jmdns.impl.NameRegister.NameType.SERVICE
            java.lang.String r3 = r3.incrementName(r4, r5, r7)
            r11.setName(r3)
            r3 = 1
        L_0x00bd:
            java.util.concurrent.ConcurrentMap<java.lang.String, javax.jmdns.ServiceInfo> r4 = r10._services
            java.lang.String r5 = r11.getKey()
            java.lang.Object r4 = r4.get(r5)
            javax.jmdns.ServiceInfo r4 = (javax.jmdns.ServiceInfo) r4
            if (r4 == 0) goto L_0x00e5
            if (r4 == r11) goto L_0x00e5
            javax.jmdns.impl.NameRegister r3 = javax.jmdns.impl.NameRegister.Factory.getRegistry()
            javax.jmdns.impl.HostInfo r4 = r10._localHost
            java.net.InetAddress r4 = r4.getInetAddress()
            java.lang.String r5 = r11.getName()
            javax.jmdns.impl.NameRegister$NameType r7 = javax.jmdns.impl.NameRegister.NameType.SERVICE
            java.lang.String r3 = r3.incrementName(r4, r5, r7)
            r11.setName(r3)
            r3 = 1
        L_0x00e5:
            if (r3 != 0) goto L_0x0008
            java.lang.String r11 = r11.getKey()
            boolean r11 = r0.equals(r11)
            r11 = r11 ^ r6
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.JmDNSImpl.makeServiceNameUnique(javax.jmdns.impl.ServiceInfoImpl):boolean");
    }

    public void addListener(DNSListener dNSListener, DNSQuestion dNSQuestion) {
        long currentTimeMillis = System.currentTimeMillis();
        this._listeners.add(dNSListener);
        if (dNSQuestion != null) {
            for (DNSEntry dNSEntry : getCache().getDNSEntryList(dNSQuestion.getName().toLowerCase())) {
                if (dNSQuestion.answeredBy(dNSEntry) && !dNSEntry.isExpired(currentTimeMillis)) {
                    dNSListener.updateRecord(getCache(), currentTimeMillis, dNSEntry);
                }
            }
        }
    }

    public void removeListener(DNSListener dNSListener) {
        this._listeners.remove(dNSListener);
    }

    public void renewServiceCollector(DNSRecord dNSRecord) {
        ServiceInfo serviceInfo = dNSRecord.getServiceInfo();
        if (this._serviceCollectors.containsKey(serviceInfo.getType().toLowerCase())) {
            startServiceResolver(serviceInfo.getType());
        }
    }

    public void updateRecord(long j, DNSRecord dNSRecord, Operation operation) {
        ArrayList<DNSListener> arrayList;
        List<ServiceListenerStatus> list;
        synchronized (this._listeners) {
            arrayList = new ArrayList<>(this._listeners);
        }
        for (DNSListener updateRecord : arrayList) {
            updateRecord.updateRecord(getCache(), j, dNSRecord);
        }
        if (DNSRecordType.TYPE_PTR.equals(dNSRecord.getRecordType())) {
            final ServiceEvent serviceEvent = dNSRecord.getServiceEvent(this);
            if (serviceEvent.getInfo() == null || !serviceEvent.getInfo().hasData()) {
                ServiceInfoImpl serviceInfoFromCache = getServiceInfoFromCache(serviceEvent.getType(), serviceEvent.getName(), "", false);
                if (serviceInfoFromCache.hasData()) {
                    serviceEvent = new ServiceEventImpl(this, serviceEvent.getType(), serviceEvent.getName(), serviceInfoFromCache);
                }
            }
            List list2 = (List) this._serviceListeners.get(serviceEvent.getType().toLowerCase());
            if (list2 != null) {
                synchronized (list2) {
                    list = new ArrayList<>(list2);
                }
            } else {
                list = Collections.emptyList();
            }
            if (logger.isLoggable(Level.FINEST)) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append(getName());
                sb.append(".updating record for event: ");
                sb.append(serviceEvent);
                sb.append(" list ");
                sb.append(list);
                sb.append(" operation: ");
                sb.append(operation);
                logger2.finest(sb.toString());
            }
            if (!list.isEmpty()) {
                int i = C10877.$SwitchMap$javax$jmdns$impl$JmDNSImpl$Operation[operation.ordinal()];
                if (i == 1) {
                    for (final ServiceListenerStatus serviceListenerStatus : list) {
                        if (serviceListenerStatus.isSynchronous()) {
                            serviceListenerStatus.serviceAdded(serviceEvent);
                        } else {
                            this._executor.submit(new Runnable() {
                                public void run() {
                                    serviceListenerStatus.serviceAdded(serviceEvent);
                                }
                            });
                        }
                    }
                } else if (i == 2) {
                    for (final ServiceListenerStatus serviceListenerStatus2 : list) {
                        if (serviceListenerStatus2.isSynchronous()) {
                            serviceListenerStatus2.serviceRemoved(serviceEvent);
                        } else {
                            this._executor.submit(new Runnable() {
                                public void run() {
                                    serviceListenerStatus2.serviceRemoved(serviceEvent);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void handleRecord(DNSRecord dNSRecord, long j) {
        Operation operation = Operation.Noop;
        boolean isExpired = dNSRecord.isExpired(j);
        if (logger.isLoggable(Level.FINE)) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append(getName());
            sb.append(" handle response: ");
            sb.append(dNSRecord);
            logger2.fine(sb.toString());
        }
        if (!dNSRecord.isServicesDiscoveryMetaQuery() && !dNSRecord.isDomainDiscoveryQuery()) {
            boolean isUnique = dNSRecord.isUnique();
            DNSRecord dNSRecord2 = (DNSRecord) getCache().getDNSEntry(dNSRecord);
            if (logger.isLoggable(Level.FINE)) {
                Logger logger3 = logger;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getName());
                sb2.append(" handle response cached record: ");
                sb2.append(dNSRecord2);
                logger3.fine(sb2.toString());
            }
            if (isUnique) {
                for (DNSEntry dNSEntry : getCache().getDNSEntryList(dNSRecord.getKey())) {
                    if (dNSRecord.getRecordType().equals(dNSEntry.getRecordType()) && dNSRecord.getRecordClass().equals(dNSEntry.getRecordClass()) && dNSEntry != dNSRecord2) {
                        ((DNSRecord) dNSEntry).setWillExpireSoon(j);
                    }
                }
            }
            if (dNSRecord2 != null) {
                if (isExpired) {
                    if (dNSRecord.getTTL() == 0) {
                        operation = Operation.Noop;
                        dNSRecord2.setWillExpireSoon(j);
                    } else {
                        operation = Operation.Remove;
                        getCache().removeDNSEntry(dNSRecord2);
                    }
                } else if (dNSRecord.sameValue(dNSRecord2) && (dNSRecord.sameSubtype(dNSRecord2) || dNSRecord.getSubtype().length() <= 0)) {
                    dNSRecord2.resetTTL(dNSRecord);
                    dNSRecord = dNSRecord2;
                } else if (dNSRecord.isSingleValued()) {
                    operation = Operation.Update;
                    getCache().replaceDNSEntry(dNSRecord, dNSRecord2);
                } else {
                    operation = Operation.Add;
                    getCache().addDNSEntry(dNSRecord);
                }
            } else if (!isExpired) {
                operation = Operation.Add;
                getCache().addDNSEntry(dNSRecord);
            }
        }
        if (dNSRecord.getRecordType() == DNSRecordType.TYPE_PTR) {
            if (dNSRecord.isServicesDiscoveryMetaQuery()) {
                if (!isExpired) {
                    registerServiceType(((Pointer) dNSRecord).getAlias());
                }
                return;
            } else if ((registerServiceType(dNSRecord.getName()) || false) && operation == Operation.Noop) {
                operation = Operation.RegisterServiceType;
            }
        }
        if (operation != Operation.Noop) {
            updateRecord(j, dNSRecord, operation);
        }
    }

    /* access modifiers changed from: 0000 */
    public void handleResponse(DNSIncoming dNSIncoming) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = false;
        boolean z2 = false;
        for (DNSRecord dNSRecord : dNSIncoming.getAllAnswers()) {
            handleRecord(dNSRecord, currentTimeMillis);
            if (DNSRecordType.TYPE_A.equals(dNSRecord.getRecordType()) || DNSRecordType.TYPE_AAAA.equals(dNSRecord.getRecordType())) {
                z |= dNSRecord.handleResponse(this);
            } else {
                z2 |= dNSRecord.handleResponse(this);
            }
        }
        if (z || z2) {
            startProber();
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: 0000 */
    public void handleQuery(DNSIncoming dNSIncoming, InetAddress inetAddress, int i) throws IOException {
        if (logger.isLoggable(Level.FINE)) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append(getName());
            sb.append(".handle query: ");
            sb.append(dNSIncoming);
            logger2.fine(sb.toString());
        }
        boolean z = false;
        long currentTimeMillis = System.currentTimeMillis() + 120;
        for (DNSRecord handleQuery : dNSIncoming.getAllAnswers()) {
            z |= handleQuery.handleQuery(this, currentTimeMillis);
        }
        ioLock();
        try {
            if (this._plannedAnswer != null) {
                this._plannedAnswer.append(dNSIncoming);
            } else {
                DNSIncoming clone = dNSIncoming.clone();
                if (dNSIncoming.isTruncated()) {
                    this._plannedAnswer = clone;
                }
                startResponder(clone, i);
            }
            ioUnlock();
            long currentTimeMillis2 = System.currentTimeMillis();
            for (DNSRecord handleRecord : dNSIncoming.getAnswers()) {
                handleRecord(handleRecord, currentTimeMillis2);
            }
            if (z) {
                startProber();
            }
        } catch (Throwable th) {
            ioUnlock();
            throw th;
        }
    }

    public void respondToQuery(DNSIncoming dNSIncoming) {
        ioLock();
        try {
            if (this._plannedAnswer == dNSIncoming) {
                this._plannedAnswer = null;
            }
        } finally {
            ioUnlock();
        }
    }

    public DNSOutgoing addAnswer(DNSIncoming dNSIncoming, InetAddress inetAddress, int i, DNSOutgoing dNSOutgoing, DNSRecord dNSRecord) throws IOException {
        if (dNSOutgoing == null) {
            dNSOutgoing = new DNSOutgoing(33792, false, dNSIncoming.getSenderUDPPayload());
        }
        try {
            dNSOutgoing.addAnswer(dNSIncoming, dNSRecord);
            return dNSOutgoing;
        } catch (IOException unused) {
            dNSOutgoing.setFlags(dNSOutgoing.getFlags() | 512);
            dNSOutgoing.setId(dNSIncoming.getId());
            send(dNSOutgoing);
            DNSOutgoing dNSOutgoing2 = new DNSOutgoing(33792, false, dNSIncoming.getSenderUDPPayload());
            dNSOutgoing2.addAnswer(dNSIncoming, dNSRecord);
            return dNSOutgoing2;
        }
    }

    public void send(DNSOutgoing dNSOutgoing) throws IOException {
        String str = "send(";
        if (!dNSOutgoing.isEmpty()) {
            byte[] data = dNSOutgoing.data();
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, this._group, DNSConstants.MDNS_PORT);
            if (logger.isLoggable(Level.FINEST)) {
                try {
                    DNSIncoming dNSIncoming = new DNSIncoming(datagramPacket);
                    if (logger.isLoggable(Level.FINEST)) {
                        Logger logger2 = logger;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(getName());
                        sb.append(") JmDNS out:");
                        sb.append(dNSIncoming.print(true));
                        logger2.finest(sb.toString());
                    }
                } catch (IOException e) {
                    Logger logger3 = logger;
                    String cls = getClass().toString();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(getName());
                    sb2.append(") - JmDNS can not parse what it sends!!!");
                    logger3.throwing(cls, sb2.toString(), e);
                }
            }
            MulticastSocket multicastSocket = this._socket;
            if (multicastSocket != null && !multicastSocket.isClosed()) {
                multicastSocket.send(datagramPacket);
            }
        }
    }

    public void purgeTimer() {
        Factory.getInstance().getStarter(getDns()).purgeTimer();
    }

    public void purgeStateTimer() {
        Factory.getInstance().getStarter(getDns()).purgeStateTimer();
    }

    public void cancelTimer() {
        Factory.getInstance().getStarter(getDns()).cancelTimer();
    }

    public void cancelStateTimer() {
        Factory.getInstance().getStarter(getDns()).cancelStateTimer();
    }

    public void startProber() {
        Factory.getInstance().getStarter(getDns()).startProber();
    }

    public void startAnnouncer() {
        Factory.getInstance().getStarter(getDns()).startAnnouncer();
    }

    public void startRenewer() {
        Factory.getInstance().getStarter(getDns()).startRenewer();
    }

    public void startCanceler() {
        Factory.getInstance().getStarter(getDns()).startCanceler();
    }

    public void startReaper() {
        Factory.getInstance().getStarter(getDns()).startReaper();
    }

    public void startServiceInfoResolver(ServiceInfoImpl serviceInfoImpl) {
        Factory.getInstance().getStarter(getDns()).startServiceInfoResolver(serviceInfoImpl);
    }

    public void startTypeResolver() {
        Factory.getInstance().getStarter(getDns()).startTypeResolver();
    }

    public void startServiceResolver(String str) {
        Factory.getInstance().getStarter(getDns()).startServiceResolver(str);
    }

    public void startResponder(DNSIncoming dNSIncoming, int i) {
        Factory.getInstance().getStarter(getDns()).startResponder(dNSIncoming, i);
    }

    public void recover() {
        Logger logger2 = logger;
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("recover()");
        logger2.finer(sb.toString());
        if (!isClosing() && !isClosed() && !isCanceling() && !isCanceled()) {
            synchronized (this._recoverLock) {
                if (cancelState()) {
                    Logger logger3 = logger;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(getName());
                    sb2.append("recover() thread ");
                    sb2.append(Thread.currentThread().getName());
                    logger3.finer(sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(getName());
                    sb3.append(".recover()");
                    new Thread(sb3.toString()) {
                        public void run() {
                            JmDNSImpl.this.__recover();
                        }
                    }.start();
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void __recover() {
        if (logger.isLoggable(Level.FINER)) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append(getName());
            sb.append("recover() Cleanning up");
            logger2.finer(sb.toString());
        }
        logger.warning("RECOVERING");
        purgeTimer();
        ArrayList<ServiceInfo> arrayList = new ArrayList<>(getServices().values());
        unregisterAllServices();
        disposeServiceCollectors();
        waitForCanceled(DNSConstants.CLOSE_TIMEOUT);
        purgeStateTimer();
        closeMulticastSocket();
        getCache().clear();
        if (logger.isLoggable(Level.FINER)) {
            Logger logger3 = logger;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getName());
            sb2.append("recover() All is clean");
            logger3.finer(sb2.toString());
        }
        if (isCanceled()) {
            for (ServiceInfo serviceInfo : arrayList) {
                ((ServiceInfoImpl) serviceInfo).recoverState();
            }
            recoverState();
            try {
                openMulticastSocket(getLocalHost());
                start(arrayList);
            } catch (Exception e) {
                Logger logger4 = logger;
                Level level = Level.WARNING;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(getName());
                sb3.append("recover() Start services exception ");
                logger4.log(level, sb3.toString(), e);
            }
            Logger logger5 = logger;
            Level level2 = Level.WARNING;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(getName());
            sb4.append("recover() We are back!");
            logger5.log(level2, sb4.toString());
            return;
        }
        Logger logger6 = logger;
        Level level3 = Level.WARNING;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(getName());
        sb5.append("recover() Could not recover we are Down!");
        logger6.log(level3, sb5.toString());
        if (getDelegate() != null) {
            getDelegate().cannotRecoverFromIOError(getDns(), arrayList);
        }
    }

    public void cleanCache() {
        long currentTimeMillis = System.currentTimeMillis();
        for (DNSEntry dNSEntry : getCache().allValues()) {
            try {
                DNSRecord dNSRecord = (DNSRecord) dNSEntry;
                if (dNSRecord.isExpired(currentTimeMillis)) {
                    updateRecord(currentTimeMillis, dNSRecord, Operation.Remove);
                    getCache().removeDNSEntry(dNSRecord);
                } else if (dNSRecord.isStale(currentTimeMillis)) {
                    renewServiceCollector(dNSRecord);
                }
            } catch (Exception e) {
                Logger logger2 = logger;
                Level level = Level.SEVERE;
                StringBuilder sb = new StringBuilder();
                sb.append(getName());
                sb.append(".Error while reaping records: ");
                sb.append(dNSEntry);
                logger2.log(level, sb.toString(), e);
                logger.severe(toString());
            }
        }
    }

    public void close() {
        if (!isClosing()) {
            if (logger.isLoggable(Level.FINER)) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("Cancelling JmDNS: ");
                sb.append(this);
                logger2.finer(sb.toString());
            }
            if (closeState()) {
                logger.finer("Canceling the timer");
                cancelTimer();
                unregisterAllServices();
                disposeServiceCollectors();
                if (logger.isLoggable(Level.FINER)) {
                    Logger logger3 = logger;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Wait for JmDNS cancel: ");
                    sb2.append(this);
                    logger3.finer(sb2.toString());
                }
                waitForCanceled(DNSConstants.CLOSE_TIMEOUT);
                logger.finer("Canceling the state timer");
                cancelStateTimer();
                this._executor.shutdown();
                closeMulticastSocket();
                if (this._shutdown != null) {
                    Runtime.getRuntime().removeShutdownHook(this._shutdown);
                }
                Factory.getInstance().disposeStarter(getDns());
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("JmDNS closed.");
                }
            }
            advanceState(null);
        }
    }

    @Deprecated
    public void printServices() {
        System.err.println(toString());
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder(2048);
        String str2 = "\n";
        sb.append(str2);
        sb.append("\t---- Local Host -----");
        sb.append("\n\t");
        sb.append(this._localHost);
        sb.append("\n\t---- Services -----");
        Iterator it = this._services.keySet().iterator();
        while (true) {
            str = ": ";
            if (!it.hasNext()) {
                break;
            }
            String str3 = (String) it.next();
            sb.append("\n\t\tService: ");
            sb.append(str3);
            sb.append(str);
            sb.append(this._services.get(str3));
        }
        sb.append(str2);
        sb.append("\t---- Types ----");
        for (String str4 : this._serviceTypes.keySet()) {
            ServiceTypeEntry serviceTypeEntry = (ServiceTypeEntry) this._serviceTypes.get(str4);
            sb.append("\n\t\tType: ");
            sb.append(serviceTypeEntry.getType());
            sb.append(str);
            sb.append(serviceTypeEntry.isEmpty() ? "no subtypes" : serviceTypeEntry);
        }
        sb.append(str2);
        sb.append(this._cache.toString());
        sb.append(str2);
        sb.append("\t---- Service Collectors ----");
        for (String str5 : this._serviceCollectors.keySet()) {
            sb.append("\n\t\tService Collector: ");
            sb.append(str5);
            sb.append(str);
            sb.append(this._serviceCollectors.get(str5));
        }
        sb.append(str2);
        sb.append("\t---- Service Listeners ----");
        for (String str6 : this._serviceListeners.keySet()) {
            sb.append("\n\t\tService Listener: ");
            sb.append(str6);
            sb.append(str);
            sb.append(this._serviceListeners.get(str6));
        }
        return sb.toString();
    }

    public ServiceInfo[] list(String str) {
        return list(str, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public ServiceInfo[] list(String str, long j) {
        ServiceCollector serviceCollector;
        ServiceInfo[] serviceInfoArr;
        cleanCache();
        String lowerCase = str.toLowerCase();
        if (isCanceling() || isCanceled()) {
            System.out.println("JmDNS Cancelling.");
            return new ServiceInfo[0];
        }
        ServiceCollector serviceCollector2 = (ServiceCollector) this._serviceCollectors.get(lowerCase);
        if (serviceCollector2 == null) {
            boolean z = this._serviceCollectors.putIfAbsent(lowerCase, new ServiceCollector(str)) == null;
            serviceCollector = (ServiceCollector) this._serviceCollectors.get(lowerCase);
            if (z) {
                addServiceListener(str, serviceCollector, true);
            }
        } else {
            serviceCollector = serviceCollector2;
        }
        if (logger.isLoggable(Level.FINER)) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append(getName());
            sb.append("-collector: ");
            sb.append(serviceCollector);
            logger2.finer(sb.toString());
        }
        if (serviceCollector != null) {
            serviceInfoArr = serviceCollector.list(j);
        } else {
            serviceInfoArr = new ServiceInfo[0];
        }
        return serviceInfoArr;
    }

    public Map<String, ServiceInfo[]> listBySubtype(String str) {
        return listBySubtype(str, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public Map<String, ServiceInfo[]> listBySubtype(String str, long j) {
        ServiceInfo[] list;
        HashMap hashMap = new HashMap(5);
        for (ServiceInfo serviceInfo : list(str, j)) {
            String lowerCase = serviceInfo.getSubtype().toLowerCase();
            if (!hashMap.containsKey(lowerCase)) {
                hashMap.put(lowerCase, new ArrayList(10));
            }
            ((List) hashMap.get(lowerCase)).add(serviceInfo);
        }
        HashMap hashMap2 = new HashMap(hashMap.size());
        for (String str2 : hashMap.keySet()) {
            List list2 = (List) hashMap.get(str2);
            hashMap2.put(str2, list2.toArray(new ServiceInfo[list2.size()]));
        }
        return hashMap2;
    }

    private void disposeServiceCollectors() {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("disposeServiceCollectors()");
        }
        for (String str : this._serviceCollectors.keySet()) {
            ServiceCollector serviceCollector = (ServiceCollector) this._serviceCollectors.get(str);
            if (serviceCollector != null) {
                removeServiceListener(str, serviceCollector);
                this._serviceCollectors.remove(str, serviceCollector);
            }
        }
    }

    static String toUnqualifiedName(String str, String str2) {
        String lowerCase = str.toLowerCase();
        String lowerCase2 = str2.toLowerCase();
        return (!lowerCase2.endsWith(lowerCase) || lowerCase2.equals(lowerCase)) ? str2 : str2.substring(0, (str2.length() - str.length()) - 1);
    }

    public Map<String, ServiceInfo> getServices() {
        return this._services;
    }

    public void setLastThrottleIncrement(long j) {
        this._lastThrottleIncrement = j;
    }

    public long getLastThrottleIncrement() {
        return this._lastThrottleIncrement;
    }

    public void setThrottle(int i) {
        this._throttle = i;
    }

    public int getThrottle() {
        return this._throttle;
    }

    public static Random getRandom() {
        return _random;
    }

    public void ioLock() {
        this._ioLock.lock();
    }

    public void ioUnlock() {
        this._ioLock.unlock();
    }

    public void setPlannedAnswer(DNSIncoming dNSIncoming) {
        this._plannedAnswer = dNSIncoming;
    }

    public DNSIncoming getPlannedAnswer() {
        return this._plannedAnswer;
    }

    /* access modifiers changed from: 0000 */
    public void setLocalHost(HostInfo hostInfo) {
        this._localHost = hostInfo;
    }

    public Map<String, ServiceTypeEntry> getServiceTypes() {
        return this._serviceTypes;
    }

    public MulticastSocket getSocket() {
        return this._socket;
    }

    public InetAddress getGroup() {
        return this._group;
    }

    public Delegate getDelegate() {
        return this._delegate;
    }

    public Delegate setDelegate(Delegate delegate) {
        Delegate delegate2 = this._delegate;
        this._delegate = delegate;
        return delegate2;
    }
}
