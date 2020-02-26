package javax.jmdns.impl.tasks.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSStatefulObject;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.ServiceInfoImpl;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSState;
import javax.jmdns.impl.tasks.DNSTask;

public abstract class DNSStateTask extends DNSTask {
    private static int _defaultTTL = DNSConstants.DNS_TTL;
    static Logger logger1 = Logger.getLogger(DNSStateTask.class.getName());
    private DNSState _taskState = null;
    private final int _ttl;

    /* access modifiers changed from: protected */
    public abstract void advanceTask();

    /* access modifiers changed from: protected */
    public abstract DNSOutgoing buildOutgoingForDNS(DNSOutgoing dNSOutgoing) throws IOException;

    /* access modifiers changed from: protected */
    public abstract DNSOutgoing buildOutgoingForInfo(ServiceInfoImpl serviceInfoImpl, DNSOutgoing dNSOutgoing) throws IOException;

    /* access modifiers changed from: protected */
    public abstract boolean checkRunCondition();

    /* access modifiers changed from: protected */
    public abstract DNSOutgoing createOugoing();

    public abstract String getTaskDescription();

    /* access modifiers changed from: protected */
    public abstract void recoverTask(Throwable th);

    public static int defaultTTL() {
        return _defaultTTL;
    }

    public static void setDefaultTTL(int i) {
        _defaultTTL = i;
    }

    public DNSStateTask(JmDNSImpl jmDNSImpl, int i) {
        super(jmDNSImpl);
        this._ttl = i;
    }

    public int getTTL() {
        return this._ttl;
    }

    /* access modifiers changed from: protected */
    public void associate(DNSState dNSState) {
        synchronized (getDns()) {
            getDns().associateWithTask(this, dNSState);
        }
        for (ServiceInfo serviceInfo : getDns().getServices().values()) {
            ((ServiceInfoImpl) serviceInfo).associateWithTask(this, dNSState);
        }
    }

    /* access modifiers changed from: protected */
    public void removeAssociation() {
        synchronized (getDns()) {
            getDns().removeAssociationWithTask(this);
        }
        for (ServiceInfo serviceInfo : getDns().getServices().values()) {
            ((ServiceInfoImpl) serviceInfo).removeAssociationWithTask(this);
        }
    }

    public void run() {
        DNSOutgoing createOugoing = createOugoing();
        try {
            if (!checkRunCondition()) {
                cancel();
                return;
            }
            ArrayList arrayList = new ArrayList();
            synchronized (getDns()) {
                if (getDns().isAssociatedWithTask(this, getTaskState())) {
                    Logger logger = logger1;
                    StringBuilder sb = new StringBuilder();
                    sb.append(getName());
                    sb.append(".run() JmDNS ");
                    sb.append(getTaskDescription());
                    sb.append(" ");
                    sb.append(getDns().getName());
                    logger.finer(sb.toString());
                    arrayList.add(getDns());
                    createOugoing = buildOutgoingForDNS(createOugoing);
                }
            }
            for (ServiceInfo serviceInfo : getDns().getServices().values()) {
                ServiceInfoImpl serviceInfoImpl = (ServiceInfoImpl) serviceInfo;
                synchronized (serviceInfoImpl) {
                    if (serviceInfoImpl.isAssociatedWithTask(this, getTaskState())) {
                        Logger logger2 = logger1;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(getName());
                        sb2.append(".run() JmDNS ");
                        sb2.append(getTaskDescription());
                        sb2.append(" ");
                        sb2.append(serviceInfoImpl.getQualifiedName());
                        logger2.fine(sb2.toString());
                        arrayList.add(serviceInfoImpl);
                        createOugoing = buildOutgoingForInfo(serviceInfoImpl, createOugoing);
                    }
                }
            }
            if (!createOugoing.isEmpty()) {
                Logger logger3 = logger1;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(getName());
                sb3.append(".run() JmDNS ");
                sb3.append(getTaskDescription());
                sb3.append(" #");
                sb3.append(getTaskState());
                logger3.finer(sb3.toString());
                getDns().send(createOugoing);
                advanceObjectsState(arrayList);
                advanceTask();
                return;
            }
            advanceObjectsState(arrayList);
            cancel();
        } catch (Throwable th) {
            Logger logger4 = logger1;
            Level level = Level.WARNING;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(getName());
            sb4.append(".run() exception ");
            logger4.log(level, sb4.toString(), th);
            recoverTask(th);
        }
    }

    /* access modifiers changed from: protected */
    public void advanceObjectsState(List<DNSStatefulObject> list) {
        if (list != null) {
            for (DNSStatefulObject dNSStatefulObject : list) {
                synchronized (dNSStatefulObject) {
                    dNSStatefulObject.advanceState(this);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public DNSState getTaskState() {
        return this._taskState;
    }

    /* access modifiers changed from: protected */
    public void setTaskState(DNSState dNSState) {
        this._taskState = dNSState;
    }
}
