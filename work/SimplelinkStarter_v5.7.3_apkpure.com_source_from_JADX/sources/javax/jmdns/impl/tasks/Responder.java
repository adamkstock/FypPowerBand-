package javax.jmdns.impl.tasks;

import java.util.HashSet;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.impl.DNSIncoming;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.constants.DNSConstants;

public class Responder extends DNSTask {
    static Logger logger = Logger.getLogger(Responder.class.getName());
    private final DNSIncoming _in;
    private final boolean _unicast;

    public Responder(JmDNSImpl jmDNSImpl, DNSIncoming dNSIncoming, int i) {
        super(jmDNSImpl);
        this._in = dNSIncoming;
        this._unicast = i != DNSConstants.MDNS_PORT;
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append("Responder(");
        sb.append(getDns() != null ? getDns().getName() : "");
        sb.append(")");
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" incomming: ");
        sb.append(this._in);
        return sb.toString();
    }

    public void start(Timer timer) {
        boolean z = true;
        for (DNSQuestion dNSQuestion : this._in.getQuestions()) {
            if (logger.isLoggable(Level.FINEST)) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append(getName());
                sb.append("start() question=");
                sb.append(dNSQuestion);
                logger2.finest(sb.toString());
            }
            z = dNSQuestion.iAmTheOnlyOne(getDns());
            if (!z) {
                break;
            }
        }
        int i = 0;
        int nextInt = (!z || this._in.isTruncated()) ? (JmDNSImpl.getRandom().nextInt(96) + 20) - this._in.elapseSinceArrival() : 0;
        if (nextInt >= 0) {
            i = nextInt;
        }
        if (logger.isLoggable(Level.FINEST)) {
            Logger logger3 = logger;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getName());
            sb2.append("start() Responder chosen delay=");
            sb2.append(i);
            logger3.finest(sb2.toString());
        }
        if (!getDns().isCanceling() && !getDns().isCanceled()) {
            timer.schedule(this, (long) i);
        }
    }

    public void run() {
        getDns().respondToQuery(this._in);
        HashSet<DNSQuestion> hashSet = new HashSet<>();
        HashSet<DNSRecord> hashSet2 = new HashSet<>();
        if (getDns().isAnnounced()) {
            try {
                for (DNSQuestion dNSQuestion : this._in.getQuestions()) {
                    if (logger.isLoggable(Level.FINER)) {
                        Logger logger2 = logger;
                        StringBuilder sb = new StringBuilder();
                        sb.append(getName());
                        sb.append("run() JmDNS responding to: ");
                        sb.append(dNSQuestion);
                        logger2.finer(sb.toString());
                    }
                    if (this._unicast) {
                        hashSet.add(dNSQuestion);
                    }
                    dNSQuestion.addAnswers(getDns(), hashSet2);
                }
                long currentTimeMillis = System.currentTimeMillis();
                for (DNSRecord dNSRecord : this._in.getAnswers()) {
                    if (dNSRecord.isStale(currentTimeMillis)) {
                        hashSet2.remove(dNSRecord);
                        if (logger.isLoggable(Level.FINER)) {
                            Logger logger3 = logger;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(getName());
                            sb2.append("JmDNS Responder Known Answer Removed");
                            logger3.finer(sb2.toString());
                        }
                    }
                }
                if (!hashSet2.isEmpty()) {
                    if (logger.isLoggable(Level.FINER)) {
                        Logger logger4 = logger;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(getName());
                        sb3.append("run() JmDNS responding");
                        logger4.finer(sb3.toString());
                    }
                    DNSOutgoing dNSOutgoing = new DNSOutgoing(33792, !this._unicast, this._in.getSenderUDPPayload());
                    dNSOutgoing.setId(this._in.getId());
                    for (DNSQuestion dNSQuestion2 : hashSet) {
                        if (dNSQuestion2 != null) {
                            dNSOutgoing = addQuestion(dNSOutgoing, dNSQuestion2);
                        }
                    }
                    for (DNSRecord dNSRecord2 : hashSet2) {
                        if (dNSRecord2 != null) {
                            dNSOutgoing = addAnswer(dNSOutgoing, this._in, dNSRecord2);
                        }
                    }
                    if (!dNSOutgoing.isEmpty()) {
                        getDns().send(dNSOutgoing);
                    }
                }
            } catch (Throwable th) {
                Logger logger5 = logger;
                Level level = Level.WARNING;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(getName());
                sb4.append("run() exception ");
                logger5.log(level, sb4.toString(), th);
                getDns().close();
            }
        }
    }
}
