package javax.jmdns.impl;

import java.util.EventListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;

public class ListenerStatus<T extends EventListener> {
    public static final boolean ASYNCHONEOUS = false;
    public static final boolean SYNCHONEOUS = true;
    private final T _listener;
    private final boolean _synch;

    public static class ServiceListenerStatus extends ListenerStatus<ServiceListener> {
        private static Logger logger = Logger.getLogger(ServiceListenerStatus.class.getName());
        private final ConcurrentMap<String, ServiceInfo> _addedServices = new ConcurrentHashMap(32);

        public ServiceListenerStatus(ServiceListener serviceListener, boolean z) {
            super(serviceListener, z);
        }

        /* access modifiers changed from: 0000 */
        public void serviceAdded(ServiceEvent serviceEvent) {
            StringBuilder sb = new StringBuilder();
            sb.append(serviceEvent.getName());
            sb.append(".");
            sb.append(serviceEvent.getType());
            if (this._addedServices.putIfAbsent(sb.toString(), serviceEvent.getInfo().clone()) == null) {
                ((ServiceListener) getListener()).serviceAdded(serviceEvent);
                ServiceInfo info = serviceEvent.getInfo();
                if (info != null && info.hasData()) {
                    ((ServiceListener) getListener()).serviceResolved(serviceEvent);
                    return;
                }
                return;
            }
            Logger logger2 = logger;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Service Added called for a service already added: ");
            sb2.append(serviceEvent);
            logger2.finer(sb2.toString());
        }

        /* access modifiers changed from: 0000 */
        public void serviceRemoved(ServiceEvent serviceEvent) {
            StringBuilder sb = new StringBuilder();
            sb.append(serviceEvent.getName());
            sb.append(".");
            sb.append(serviceEvent.getType());
            String sb2 = sb.toString();
            ConcurrentMap<String, ServiceInfo> concurrentMap = this._addedServices;
            if (concurrentMap.remove(sb2, concurrentMap.get(sb2))) {
                ((ServiceListener) getListener()).serviceRemoved(serviceEvent);
                return;
            }
            Logger logger2 = logger;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Service Removed called for a service already removed: ");
            sb3.append(serviceEvent);
            logger2.finer(sb3.toString());
        }

        /* access modifiers changed from: 0000 */
        public synchronized void serviceResolved(ServiceEvent serviceEvent) {
            ServiceInfo info = serviceEvent.getInfo();
            if (info == null || !info.hasData()) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("Service Resolved called for an unresolved event: ");
                sb.append(serviceEvent);
                logger2.warning(sb.toString());
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(serviceEvent.getName());
                sb2.append(".");
                sb2.append(serviceEvent.getType());
                String sb3 = sb2.toString();
                ServiceInfo serviceInfo = (ServiceInfo) this._addedServices.get(sb3);
                if (_sameInfo(info, serviceInfo)) {
                    Logger logger3 = logger;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Service Resolved called for a service already resolved: ");
                    sb4.append(serviceEvent);
                    logger3.finer(sb4.toString());
                } else if (serviceInfo == null) {
                    if (this._addedServices.putIfAbsent(sb3, info.clone()) == null) {
                        ((ServiceListener) getListener()).serviceResolved(serviceEvent);
                    }
                } else if (this._addedServices.replace(sb3, serviceInfo, info.clone())) {
                    ((ServiceListener) getListener()).serviceResolved(serviceEvent);
                }
            }
        }

        private static final boolean _sameInfo(ServiceInfo serviceInfo, ServiceInfo serviceInfo2) {
            if (serviceInfo == null || serviceInfo2 == null || !serviceInfo.equals(serviceInfo2)) {
                return false;
            }
            byte[] textBytes = serviceInfo.getTextBytes();
            byte[] textBytes2 = serviceInfo2.getTextBytes();
            if (textBytes.length != textBytes2.length) {
                return false;
            }
            for (int i = 0; i < textBytes.length; i++) {
                if (textBytes[i] != textBytes2[i]) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(2048);
            sb.append("[Status for ");
            sb.append(((ServiceListener) getListener()).toString());
            if (this._addedServices.isEmpty()) {
                sb.append(" no type event ");
            } else {
                sb.append(" (");
                for (String str : this._addedServices.keySet()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(", ");
                    sb.append(sb2.toString());
                }
                sb.append(") ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static class ServiceTypeListenerStatus extends ListenerStatus<ServiceTypeListener> {
        private static Logger logger = Logger.getLogger(ServiceTypeListenerStatus.class.getName());
        private final ConcurrentMap<String, String> _addedTypes = new ConcurrentHashMap(32);

        public ServiceTypeListenerStatus(ServiceTypeListener serviceTypeListener, boolean z) {
            super(serviceTypeListener, z);
        }

        /* access modifiers changed from: 0000 */
        public void serviceTypeAdded(ServiceEvent serviceEvent) {
            if (this._addedTypes.putIfAbsent(serviceEvent.getType(), serviceEvent.getType()) == null) {
                ((ServiceTypeListener) getListener()).serviceTypeAdded(serviceEvent);
                return;
            }
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Service Type Added called for a service type already added: ");
            sb.append(serviceEvent);
            logger2.finest(sb.toString());
        }

        /* access modifiers changed from: 0000 */
        public void subTypeForServiceTypeAdded(ServiceEvent serviceEvent) {
            if (this._addedTypes.putIfAbsent(serviceEvent.getType(), serviceEvent.getType()) == null) {
                ((ServiceTypeListener) getListener()).subTypeForServiceTypeAdded(serviceEvent);
                return;
            }
            Logger logger2 = logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Service Sub Type Added called for a service sub type already added: ");
            sb.append(serviceEvent);
            logger2.finest(sb.toString());
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(2048);
            sb.append("[Status for ");
            sb.append(((ServiceTypeListener) getListener()).toString());
            if (this._addedTypes.isEmpty()) {
                sb.append(" no type event ");
            } else {
                sb.append(" (");
                for (String str : this._addedTypes.keySet()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(", ");
                    sb.append(sb2.toString());
                }
                sb.append(") ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public ListenerStatus(T t, boolean z) {
        this._listener = t;
        this._synch = z;
    }

    public T getListener() {
        return this._listener;
    }

    public boolean isSynchronous() {
        return this._synch;
    }

    public int hashCode() {
        return getListener().hashCode();
    }

    public boolean equals(Object obj) {
        return (obj instanceof ListenerStatus) && getListener().equals(((ListenerStatus) obj).getListener());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Status for ");
        sb.append(getListener().toString());
        sb.append("]");
        return sb.toString();
    }
}
