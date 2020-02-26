package javax.jmdns.impl;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.NetworkTopologyDiscovery;

public class NetworkTopologyDiscoveryImpl implements NetworkTopologyDiscovery {
    private static final Logger logger = Logger.getLogger(NetworkTopologyDiscoveryImpl.class.getName());
    private final Method _isUp;
    private final Method _supportsMulticast;

    public void lockInetAddress(InetAddress inetAddress) {
    }

    public void unlockInetAddress(InetAddress inetAddress) {
    }

    public NetworkTopologyDiscoveryImpl() {
        Method method;
        Method method2 = null;
        try {
            method = NetworkInterface.class.getMethod("isUp", null);
        } catch (Exception unused) {
            method = null;
        }
        this._isUp = method;
        try {
            method2 = NetworkInterface.class.getMethod("supportsMulticast", null);
        } catch (Exception unused2) {
        }
        this._supportsMulticast = method2;
    }

    public InetAddress[] getInetAddresses() {
        HashSet hashSet = new HashSet();
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (logger.isLoggable(Level.FINEST)) {
                        Logger logger2 = logger;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Found NetworkInterface/InetAddress: ");
                        sb.append(networkInterface);
                        sb.append(" -- ");
                        sb.append(inetAddress);
                        logger2.finest(sb.toString());
                    }
                    if (useInetAddress(networkInterface, inetAddress)) {
                        hashSet.add(inetAddress);
                    }
                }
            }
        } catch (SocketException e) {
            Logger logger3 = logger;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Error while fetching network interfaces addresses: ");
            sb2.append(e);
            logger3.warning(sb2.toString());
        }
        return (InetAddress[]) hashSet.toArray(new InetAddress[hashSet.size()]);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|(3:5|6|(1:8))|9|10|(3:12|13|(1:15))|16|18) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0018 */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x001c A[SYNTHETIC, Splitter:B:12:0x001c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean useInetAddress(java.net.NetworkInterface r4, java.net.InetAddress r5) {
        /*
            r3 = this;
            r5 = 0
            java.lang.reflect.Method r0 = r3._isUp     // Catch:{ Exception -> 0x002f }
            r1 = 0
            if (r0 == 0) goto L_0x0018
            java.lang.reflect.Method r0 = r3._isUp     // Catch:{ Exception -> 0x0018 }
            r2 = r1
            java.lang.Object[] r2 = (java.lang.Object[]) r2     // Catch:{ Exception -> 0x0018 }
            java.lang.Object r0 = r0.invoke(r4, r2)     // Catch:{ Exception -> 0x0018 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Exception -> 0x0018 }
            boolean r0 = r0.booleanValue()     // Catch:{ Exception -> 0x0018 }
            if (r0 != 0) goto L_0x0018
            return r5
        L_0x0018:
            java.lang.reflect.Method r0 = r3._supportsMulticast     // Catch:{ Exception -> 0x002f }
            if (r0 == 0) goto L_0x002d
            java.lang.reflect.Method r0 = r3._supportsMulticast     // Catch:{ Exception -> 0x002d }
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ Exception -> 0x002d }
            java.lang.Object r4 = r0.invoke(r4, r1)     // Catch:{ Exception -> 0x002d }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ Exception -> 0x002d }
            boolean r4 = r4.booleanValue()     // Catch:{ Exception -> 0x002d }
            if (r4 != 0) goto L_0x002d
            return r5
        L_0x002d:
            r4 = 1
            return r4
        L_0x002f:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.NetworkTopologyDiscoveryImpl.useInetAddress(java.net.NetworkInterface, java.net.InetAddress):boolean");
    }
}
