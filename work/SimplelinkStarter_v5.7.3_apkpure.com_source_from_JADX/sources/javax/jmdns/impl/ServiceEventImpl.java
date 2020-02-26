package javax.jmdns.impl;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;

public class ServiceEventImpl extends ServiceEvent {
    private static final long serialVersionUID = 7107973622016897488L;
    private final ServiceInfo _info;
    private final String _name;
    private final String _type;

    public ServiceEventImpl(JmDNSImpl jmDNSImpl, String str, String str2, ServiceInfo serviceInfo) {
        super(jmDNSImpl);
        this._type = str;
        this._name = str2;
        this._info = serviceInfo;
    }

    public JmDNS getDNS() {
        return (JmDNS) getSource();
    }

    public String getType() {
        return this._type;
    }

    public String getName() {
        return this._name;
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
        sb.append("\n\tname: '");
        sb.append(getName());
        sb.append("' type: '");
        sb.append(getType());
        sb.append("' info: '");
        sb.append(getInfo());
        sb.append("']");
        return sb.toString();
    }

    public ServiceInfo getInfo() {
        return this._info;
    }

    public ServiceEventImpl clone() {
        return new ServiceEventImpl((JmDNSImpl) getDNS(), getType(), getName(), new ServiceInfoImpl(getInfo()));
    }
}
