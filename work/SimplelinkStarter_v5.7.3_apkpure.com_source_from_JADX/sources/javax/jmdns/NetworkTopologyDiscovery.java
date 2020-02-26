package javax.jmdns;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.atomic.AtomicReference;
import javax.jmdns.impl.NetworkTopologyDiscoveryImpl;

public interface NetworkTopologyDiscovery {

    public static final class Factory {
        private static final AtomicReference<ClassDelegate> _databaseClassDelegate = new AtomicReference<>();
        private static volatile NetworkTopologyDiscovery _instance;

        public interface ClassDelegate {
            NetworkTopologyDiscovery newNetworkTopologyDiscovery();
        }

        private Factory() {
        }

        public static void setClassDelegate(ClassDelegate classDelegate) {
            _databaseClassDelegate.set(classDelegate);
        }

        public static ClassDelegate classDelegate() {
            return (ClassDelegate) _databaseClassDelegate.get();
        }

        protected static NetworkTopologyDiscovery newNetworkTopologyDiscovery() {
            ClassDelegate classDelegate = (ClassDelegate) _databaseClassDelegate.get();
            NetworkTopologyDiscovery newNetworkTopologyDiscovery = classDelegate != null ? classDelegate.newNetworkTopologyDiscovery() : null;
            return newNetworkTopologyDiscovery != null ? newNetworkTopologyDiscovery : new NetworkTopologyDiscoveryImpl();
        }

        public static NetworkTopologyDiscovery getInstance() {
            if (_instance == null) {
                synchronized (Factory.class) {
                    if (_instance == null) {
                        _instance = newNetworkTopologyDiscovery();
                    }
                }
            }
            return _instance;
        }
    }

    InetAddress[] getInetAddresses();

    void lockInetAddress(InetAddress inetAddress);

    void unlockInetAddress(InetAddress inetAddress);

    boolean useInetAddress(NetworkInterface networkInterface, InetAddress inetAddress);
}
