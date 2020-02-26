package javax.jmdns.impl;

import java.net.InetAddress;
import javax.jmdns.JmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.NetworkTopologyListener;

public class NetworkTopologyEventImpl extends NetworkTopologyEvent implements Cloneable {
    private static final long serialVersionUID = 1445606146153550463L;
    private final InetAddress _inetAddress;

    public NetworkTopologyEventImpl(JmDNS jmDNS, InetAddress inetAddress) {
        super(jmDNS);
        this._inetAddress = inetAddress;
    }

    NetworkTopologyEventImpl(NetworkTopologyListener networkTopologyListener, InetAddress inetAddress) {
        super(networkTopologyListener);
        this._inetAddress = inetAddress;
    }

    public JmDNS getDNS() {
        if (getSource() instanceof JmDNS) {
            return (JmDNS) getSource();
        }
        return null;
    }

    public InetAddress getInetAddress() {
        return this._inetAddress;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[");
        sb2.append(getClass().getSimpleName());
        sb2.append("@");
        sb2.append(System.identityHashCode(this));
        sb2.append(" ");
        sb.append(sb2.toString());
        sb.append("\n\tinetAddress: '");
        sb.append(getInetAddress());
        sb.append("']");
        return sb.toString();
    }

    public NetworkTopologyEventImpl clone() throws CloneNotSupportedException {
        return new NetworkTopologyEventImpl(getDNS(), getInetAddress());
    }
}
