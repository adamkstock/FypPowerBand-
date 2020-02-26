package javax.jmdns.impl.tasks.state;

import java.io.IOException;
import java.util.Timer;
import java.util.logging.Logger;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.DNSRecord.Service;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.ServiceInfoImpl;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.constants.DNSState;

public class Prober extends DNSStateTask {
    static Logger logger = Logger.getLogger(Prober.class.getName());

    public String getTaskDescription() {
        return "probing";
    }

    public Prober(JmDNSImpl jmDNSImpl) {
        super(jmDNSImpl, defaultTTL());
        setTaskState(DNSState.PROBING_1);
        associate(DNSState.PROBING_1);
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prober(");
        sb.append(getDns() != null ? getDns().getName() : "");
        sb.append(")");
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" state: ");
        sb.append(getTaskState());
        return sb.toString();
    }

    public void start(Timer timer) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - getDns().getLastThrottleIncrement() < DNSConstants.CLOSE_TIMEOUT) {
            getDns().setThrottle(getDns().getThrottle() + 1);
        } else {
            getDns().setThrottle(1);
        }
        getDns().setLastThrottleIncrement(currentTimeMillis);
        if (getDns().isAnnounced() && getDns().getThrottle() < 10) {
            timer.schedule(this, (long) JmDNSImpl.getRandom().nextInt(251), 250);
        } else if (!getDns().isCanceling() && !getDns().isCanceled()) {
            timer.schedule(this, 1000, 1000);
        }
    }

    public boolean cancel() {
        removeAssociation();
        return super.cancel();
    }

    /* access modifiers changed from: protected */
    public boolean checkRunCondition() {
        return !getDns().isCanceling() && !getDns().isCanceled();
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing createOugoing() {
        return new DNSOutgoing(0);
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing buildOutgoingForDNS(DNSOutgoing dNSOutgoing) throws IOException {
        dNSOutgoing.addQuestion(DNSQuestion.newQuestion(getDns().getLocalHost().getName(), DNSRecordType.TYPE_ANY, DNSRecordClass.CLASS_IN, false));
        for (DNSRecord addAuthoritativeAnswer : getDns().getLocalHost().answers(DNSRecordClass.CLASS_ANY, false, getTTL())) {
            dNSOutgoing = addAuthoritativeAnswer(dNSOutgoing, addAuthoritativeAnswer);
        }
        return dNSOutgoing;
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing buildOutgoingForInfo(ServiceInfoImpl serviceInfoImpl, DNSOutgoing dNSOutgoing) throws IOException {
        DNSOutgoing addQuestion = addQuestion(dNSOutgoing, DNSQuestion.newQuestion(serviceInfoImpl.getQualifiedName(), DNSRecordType.TYPE_ANY, DNSRecordClass.CLASS_IN, false));
        Service service = new Service(serviceInfoImpl.getQualifiedName(), DNSRecordClass.CLASS_IN, false, getTTL(), serviceInfoImpl.getPriority(), serviceInfoImpl.getWeight(), serviceInfoImpl.getPort(), getDns().getLocalHost().getName());
        return addAuthoritativeAnswer(addQuestion, service);
    }

    /* access modifiers changed from: protected */
    public void recoverTask(Throwable th) {
        getDns().recover();
    }

    /* access modifiers changed from: protected */
    public void advanceTask() {
        setTaskState(getTaskState().advance());
        if (!getTaskState().isProbing()) {
            cancel();
            getDns().startAnnouncer();
        }
    }
}
