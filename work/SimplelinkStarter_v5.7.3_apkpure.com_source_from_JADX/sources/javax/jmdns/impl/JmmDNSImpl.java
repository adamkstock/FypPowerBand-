package javax.jmdns.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import javax.jmdns.JmmDNS;
import javax.jmdns.NetworkTopologyDiscovery;
import javax.jmdns.NetworkTopologyDiscovery.Factory;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.NetworkTopologyListener;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;
import javax.jmdns.impl.ServiceInfoImpl.Delegate;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.util.NamedThreadFactory;

public class JmmDNSImpl implements JmmDNS, NetworkTopologyListener, Delegate {
    private static Logger logger = Logger.getLogger(JmmDNSImpl.class.getName());
    private final AtomicBoolean _closed;
    private final AtomicBoolean _isClosing;
    private final ExecutorService _jmDNSExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("JmmDNS"));
    private final ConcurrentMap<InetAddress, JmDNS> _knownMDNS = new ConcurrentHashMap();
    private final ExecutorService _listenerExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory("JmmDNS Listeners"));
    private final Set<NetworkTopologyListener> _networkListeners = Collections.synchronizedSet(new HashSet());
    private final ConcurrentMap<String, List<ServiceListener>> _serviceListeners = new ConcurrentHashMap();
    private final Set<String> _serviceTypes = Collections.synchronizedSet(new HashSet());
    private final ConcurrentMap<String, ServiceInfo> _services = new ConcurrentHashMap(20);
    private final Timer _timer = new Timer("Multihomed mDNS.Timer", true);
    private final Set<ServiceTypeListener> _typeListeners = Collections.synchronizedSet(new HashSet());

    static class NetworkChecker extends TimerTask {
        private static Logger logger1 = Logger.getLogger(NetworkChecker.class.getName());
        private Set<InetAddress> _knownAddresses = Collections.synchronizedSet(new HashSet());
        private final NetworkTopologyListener _mmDNS;
        private final NetworkTopologyDiscovery _topology;

        public NetworkChecker(NetworkTopologyListener networkTopologyListener, NetworkTopologyDiscovery networkTopologyDiscovery) {
            this._mmDNS = networkTopologyListener;
            this._topology = networkTopologyDiscovery;
        }

        public void start(Timer timer) {
            run();
            timer.schedule(this, 10000, 10000);
        }

        public void run() {
            try {
                InetAddress[] inetAddresses = this._topology.getInetAddresses();
                HashSet hashSet = new HashSet(inetAddresses.length);
                for (InetAddress inetAddress : inetAddresses) {
                    hashSet.add(inetAddress);
                    if (!this._knownAddresses.contains(inetAddress)) {
                        this._mmDNS.inetAddressAdded(new NetworkTopologyEventImpl(this._mmDNS, inetAddress));
                    }
                }
                for (InetAddress inetAddress2 : this._knownAddresses) {
                    if (!hashSet.contains(inetAddress2)) {
                        this._mmDNS.inetAddressRemoved(new NetworkTopologyEventImpl(this._mmDNS, inetAddress2));
                    }
                }
                this._knownAddresses = hashSet;
            } catch (Exception e) {
                Logger logger = logger1;
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected unhandled exception: ");
                sb.append(e);
                logger.warning(sb.toString());
            }
        }
    }

    public JmmDNSImpl() {
        new NetworkChecker(this, Factory.getInstance()).start(this._timer);
        this._isClosing = new AtomicBoolean(false);
        this._closed = new AtomicBoolean(false);
    }

    /* JADX INFO: finally extract failed */
    public void close() throws IOException {
        JmDNS[] dns;
        if (this._isClosing.compareAndSet(false, true)) {
            if (logger.isLoggable(Level.FINER)) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("Cancelling JmmDNS: ");
                sb.append(this);
                logger2.finer(sb.toString());
            }
            this._timer.cancel();
            this._listenerExecutor.shutdown();
            this._jmDNSExecutor.shutdown();
            ExecutorService newCachedThreadPool = Executors.newCachedThreadPool(new NamedThreadFactory("JmmDNS.close"));
            try {
                for (final JmDNS jmDNS : getDNS()) {
                    newCachedThreadPool.submit(new Runnable() {
                        public void run() {
                            try {
                                jmDNS.close();
                            } catch (IOException unused) {
                            }
                        }
                    });
                }
                newCachedThreadPool.shutdown();
                try {
                    newCachedThreadPool.awaitTermination(DNSConstants.CLOSE_TIMEOUT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Exception ", e);
                }
                this._knownMDNS.clear();
                this._services.clear();
                this._serviceListeners.clear();
                this._typeListeners.clear();
                this._serviceTypes.clear();
                this._closed.set(true);
                JmmDNS.Factory.close();
            } catch (Throwable th) {
                newCachedThreadPool.shutdown();
                throw th;
            }
        }
    }

    public String[] getNames() {
        HashSet hashSet = new HashSet();
        for (JmDNS name : getDNS()) {
            hashSet.add(name.getName());
        }
        return (String[]) hashSet.toArray(new String[hashSet.size()]);
    }

    public String[] getHostNames() {
        HashSet hashSet = new HashSet();
        for (JmDNS hostName : getDNS()) {
            hashSet.add(hostName.getHostName());
        }
        return (String[]) hashSet.toArray(new String[hashSet.size()]);
    }

    public InetAddress[] getInetAddresses() throws IOException {
        HashSet hashSet = new HashSet();
        for (JmDNS inetAddress : getDNS()) {
            hashSet.add(inetAddress.getInetAddress());
        }
        return (InetAddress[]) hashSet.toArray(new InetAddress[hashSet.size()]);
    }

    public JmDNS[] getDNS() {
        JmDNS[] jmDNSArr;
        synchronized (this._knownMDNS) {
            jmDNSArr = (JmDNS[]) this._knownMDNS.values().toArray(new JmDNS[this._knownMDNS.size()]);
        }
        return jmDNSArr;
    }

    @Deprecated
    public InetAddress[] getInterfaces() throws IOException {
        HashSet hashSet = new HashSet();
        for (JmDNS jmDNS : getDNS()) {
            hashSet.add(jmDNS.getInterface());
        }
        return (InetAddress[]) hashSet.toArray(new InetAddress[hashSet.size()]);
    }

    public ServiceInfo[] getServiceInfos(String str, String str2) {
        return getServiceInfos(str, str2, false, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public ServiceInfo[] getServiceInfos(String str, String str2, long j) {
        return getServiceInfos(str, str2, false, j);
    }

    public ServiceInfo[] getServiceInfos(String str, String str2, boolean z) {
        return getServiceInfos(str, str2, z, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public ServiceInfo[] getServiceInfos(String str, String str2, boolean z, long j) {
        List<Future> emptyList;
        String str3 = "Interrupted ";
        JmDNS[] dns = getDNS();
        HashSet hashSet = new HashSet(dns.length);
        if (dns.length > 0) {
            ArrayList arrayList = new ArrayList(dns.length);
            for (final JmDNS jmDNS : dns) {
                final String str4 = str;
                final String str5 = str2;
                final boolean z2 = z;
                final long j2 = j;
                C10892 r6 = new Callable<ServiceInfo>() {
                    public ServiceInfo call() throws Exception {
                        return jmDNS.getServiceInfo(str4, str5, z2, j2);
                    }
                };
                arrayList.add(r6);
            }
            ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(arrayList.size(), new NamedThreadFactory("JmmDNS.getServiceInfos"));
            try {
                emptyList = Collections.emptyList();
                emptyList = newFixedThreadPool.invokeAll(arrayList, j + 100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.log(Level.FINE, str3, e);
                Thread.currentThread().interrupt();
            } catch (Throwable th) {
                newFixedThreadPool.shutdown();
                throw th;
            }
            for (Future future : emptyList) {
                if (!future.isCancelled()) {
                    try {
                        ServiceInfo serviceInfo = (ServiceInfo) future.get();
                        if (serviceInfo != null) {
                            hashSet.add(serviceInfo);
                        }
                    } catch (InterruptedException e2) {
                        logger.log(Level.FINE, str3, e2);
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e3) {
                        logger.log(Level.WARNING, "Exception ", e3);
                    }
                }
            }
            newFixedThreadPool.shutdown();
        }
        return (ServiceInfo[]) hashSet.toArray(new ServiceInfo[hashSet.size()]);
    }

    public void requestServiceInfo(String str, String str2) {
        requestServiceInfo(str, str2, false, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public void requestServiceInfo(String str, String str2, boolean z) {
        requestServiceInfo(str, str2, z, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public void requestServiceInfo(String str, String str2, long j) {
        requestServiceInfo(str, str2, false, j);
    }

    public void requestServiceInfo(String str, String str2, boolean z, long j) {
        JmDNS[] dns;
        for (final JmDNS jmDNS : getDNS()) {
            ExecutorService executorService = this._jmDNSExecutor;
            final String str3 = str;
            final String str4 = str2;
            final boolean z2 = z;
            final long j2 = j;
            C10903 r3 = new Runnable() {
                public void run() {
                    jmDNS.requestServiceInfo(str3, str4, z2, j2);
                }
            };
            executorService.submit(r3);
        }
    }

    public void addServiceTypeListener(ServiceTypeListener serviceTypeListener) throws IOException {
        this._typeListeners.add(serviceTypeListener);
        for (JmDNS addServiceTypeListener : getDNS()) {
            addServiceTypeListener.addServiceTypeListener(serviceTypeListener);
        }
    }

    public void removeServiceTypeListener(ServiceTypeListener serviceTypeListener) {
        this._typeListeners.remove(serviceTypeListener);
        for (JmDNS removeServiceTypeListener : getDNS()) {
            removeServiceTypeListener.removeServiceTypeListener(serviceTypeListener);
        }
    }

    public void addServiceListener(String str, ServiceListener serviceListener) {
        String lowerCase = str.toLowerCase();
        List list = (List) this._serviceListeners.get(lowerCase);
        if (list == null) {
            this._serviceListeners.putIfAbsent(lowerCase, new LinkedList());
            list = (List) this._serviceListeners.get(lowerCase);
        }
        if (list != null) {
            synchronized (list) {
                if (!list.contains(serviceListener)) {
                    list.add(serviceListener);
                }
            }
        }
        for (JmDNS addServiceListener : getDNS()) {
            addServiceListener.addServiceListener(str, serviceListener);
        }
    }

    public void removeServiceListener(String str, ServiceListener serviceListener) {
        String lowerCase = str.toLowerCase();
        List list = (List) this._serviceListeners.get(lowerCase);
        if (list != null) {
            synchronized (list) {
                list.remove(serviceListener);
                if (list.isEmpty()) {
                    this._serviceListeners.remove(lowerCase, list);
                }
            }
        }
        for (JmDNS removeServiceListener : getDNS()) {
            removeServiceListener.removeServiceListener(str, serviceListener);
        }
    }

    public void textValueUpdated(ServiceInfo serviceInfo, byte[] bArr) {
        JmDNS[] dns = getDNS();
        synchronized (this._services) {
            for (JmDNS jmDNS : dns) {
                ServiceInfo serviceInfo2 = (ServiceInfo) ((JmDNSImpl) jmDNS).getServices().get(serviceInfo.getQualifiedName());
                if (serviceInfo2 != null) {
                    serviceInfo2.setText(bArr);
                } else {
                    logger.warning("We have a mDNS that does not know about the service info being updated.");
                }
            }
        }
    }

    public void registerService(ServiceInfo serviceInfo) throws IOException {
        JmDNS[] dns = getDNS();
        synchronized (this._services) {
            for (JmDNS registerService : dns) {
                registerService.registerService(serviceInfo.clone());
            }
            ((ServiceInfoImpl) serviceInfo).setDelegate(this);
            this._services.put(serviceInfo.getQualifiedName(), serviceInfo);
        }
    }

    public void unregisterService(ServiceInfo serviceInfo) {
        JmDNS[] dns = getDNS();
        synchronized (this._services) {
            this._services.remove(serviceInfo.getQualifiedName());
            for (JmDNS unregisterService : dns) {
                unregisterService.unregisterService(serviceInfo);
            }
            ((ServiceInfoImpl) serviceInfo).setDelegate(null);
        }
    }

    public void unregisterAllServices() {
        JmDNS[] dns = getDNS();
        synchronized (this._services) {
            this._services.clear();
            for (JmDNS unregisterAllServices : dns) {
                unregisterAllServices.unregisterAllServices();
            }
        }
    }

    public void registerServiceType(String str) {
        this._serviceTypes.add(str);
        for (JmDNS registerServiceType : getDNS()) {
            registerServiceType.registerServiceType(str);
        }
    }

    public ServiceInfo[] list(String str) {
        return list(str, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public ServiceInfo[] list(String str, long j) {
        List<Future> emptyList;
        String str2 = "Interrupted ";
        JmDNS[] dns = getDNS();
        HashSet hashSet = new HashSet(dns.length * 5);
        if (dns.length > 0) {
            ArrayList arrayList = new ArrayList(dns.length);
            for (final JmDNS jmDNS : dns) {
                final String str3 = str;
                final long j2 = j;
                C10914 r6 = new Callable<List<ServiceInfo>>() {
                    public List<ServiceInfo> call() throws Exception {
                        return Arrays.asList(jmDNS.list(str3, j2));
                    }
                };
                arrayList.add(r6);
            }
            ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(arrayList.size(), new NamedThreadFactory("JmmDNS.list"));
            try {
                emptyList = Collections.emptyList();
                emptyList = newFixedThreadPool.invokeAll(arrayList, 100 + j, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.log(Level.FINE, str2, e);
                Thread.currentThread().interrupt();
            } catch (Throwable th) {
                newFixedThreadPool.shutdown();
                throw th;
            }
            for (Future future : emptyList) {
                if (!future.isCancelled()) {
                    try {
                        hashSet.addAll((Collection) future.get());
                    } catch (InterruptedException e2) {
                        logger.log(Level.FINE, str2, e2);
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e3) {
                        logger.log(Level.WARNING, "Exception ", e3);
                    }
                }
            }
            newFixedThreadPool.shutdown();
        }
        return (ServiceInfo[]) hashSet.toArray(new ServiceInfo[hashSet.size()]);
    }

    public Map<String, ServiceInfo[]> listBySubtype(String str) {
        return listBySubtype(str, DNSConstants.SERVICE_INFO_TIMEOUT);
    }

    public Map<String, ServiceInfo[]> listBySubtype(String str, long j) {
        ServiceInfo[] list;
        HashMap hashMap = new HashMap(5);
        for (ServiceInfo serviceInfo : list(str, j)) {
            String subtype = serviceInfo.getSubtype();
            if (!hashMap.containsKey(subtype)) {
                hashMap.put(subtype, new ArrayList(10));
            }
            ((List) hashMap.get(subtype)).add(serviceInfo);
        }
        HashMap hashMap2 = new HashMap(hashMap.size());
        for (String str2 : hashMap.keySet()) {
            List list2 = (List) hashMap.get(str2);
            hashMap2.put(str2, list2.toArray(new ServiceInfo[list2.size()]));
        }
        return hashMap2;
    }

    public void addNetworkTopologyListener(NetworkTopologyListener networkTopologyListener) {
        this._networkListeners.add(networkTopologyListener);
    }

    public void removeNetworkTopologyListener(NetworkTopologyListener networkTopologyListener) {
        this._networkListeners.remove(networkTopologyListener);
    }

    public NetworkTopologyListener[] networkListeners() {
        Set<NetworkTopologyListener> set = this._networkListeners;
        return (NetworkTopologyListener[]) set.toArray(new NetworkTopologyListener[set.size()]);
    }

    public void inetAddressAdded(NetworkTopologyEvent networkTopologyEvent) {
        NetworkTopologyListener[] networkListeners;
        InetAddress inetAddress = networkTopologyEvent.getInetAddress();
        try {
            if (!this._knownMDNS.containsKey(inetAddress)) {
                synchronized (this._knownMDNS) {
                    if (!this._knownMDNS.containsKey(inetAddress)) {
                        JmDNS create = JmDNS.create(inetAddress);
                        if (this._knownMDNS.putIfAbsent(inetAddress, create) == null) {
                            final Set<String> set = this._serviceTypes;
                            final Collection values = this._services.values();
                            final Set<ServiceTypeListener> set2 = this._typeListeners;
                            final ConcurrentMap<String, List<ServiceListener>> concurrentMap = this._serviceListeners;
                            ExecutorService executorService = this._jmDNSExecutor;
                            final JmDNS jmDNS = create;
                            C10925 r2 = new Runnable() {
                                public void run() {
                                    for (String registerServiceType : set) {
                                        jmDNS.registerServiceType(registerServiceType);
                                    }
                                    for (ServiceInfo clone : values) {
                                        try {
                                            jmDNS.registerService(clone.clone());
                                        } catch (IOException unused) {
                                        }
                                    }
                                    for (ServiceTypeListener addServiceTypeListener : set2) {
                                        try {
                                            jmDNS.addServiceTypeListener(addServiceTypeListener);
                                        } catch (IOException unused2) {
                                        }
                                    }
                                    for (String str : concurrentMap.keySet()) {
                                        List<ServiceListener> list = (List) concurrentMap.get(str);
                                        synchronized (list) {
                                            for (ServiceListener addServiceListener : list) {
                                                jmDNS.addServiceListener(str, addServiceListener);
                                            }
                                        }
                                    }
                                }
                            };
                            executorService.submit(r2);
                            final NetworkTopologyEventImpl networkTopologyEventImpl = new NetworkTopologyEventImpl(create, inetAddress);
                            for (final NetworkTopologyListener networkTopologyListener : networkListeners()) {
                                this._listenerExecutor.submit(new Runnable() {
                                    public void run() {
                                        networkTopologyListener.inetAddressAdded(networkTopologyEventImpl);
                                    }
                                });
                            }
                        } else {
                            create.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected unhandled exception: ");
            sb.append(e);
            logger2.warning(sb.toString());
        }
    }

    public void inetAddressRemoved(NetworkTopologyEvent networkTopologyEvent) {
        NetworkTopologyListener[] networkListeners;
        InetAddress inetAddress = networkTopologyEvent.getInetAddress();
        try {
            if (this._knownMDNS.containsKey(inetAddress)) {
                synchronized (this._knownMDNS) {
                    if (this._knownMDNS.containsKey(inetAddress)) {
                        JmDNS jmDNS = (JmDNS) this._knownMDNS.remove(inetAddress);
                        jmDNS.close();
                        final NetworkTopologyEventImpl networkTopologyEventImpl = new NetworkTopologyEventImpl(jmDNS, inetAddress);
                        for (final NetworkTopologyListener networkTopologyListener : networkListeners()) {
                            this._listenerExecutor.submit(new Runnable() {
                                public void run() {
                                    networkTopologyListener.inetAddressRemoved(networkTopologyEventImpl);
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected unhandled exception: ");
            sb.append(e);
            logger2.warning(sb.toString());
        }
    }
}
