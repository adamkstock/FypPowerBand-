package javax.jmdns.impl.tasks.resolver;

import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.tasks.DNSTask;

public abstract class DNSResolverTask extends DNSTask {
    private static Logger logger = Logger.getLogger(DNSResolverTask.class.getName());
    protected int _count = 0;

    /* access modifiers changed from: protected */
    public abstract DNSOutgoing addAnswers(DNSOutgoing dNSOutgoing) throws IOException;

    /* access modifiers changed from: protected */
    public abstract DNSOutgoing addQuestions(DNSOutgoing dNSOutgoing) throws IOException;

    /* access modifiers changed from: protected */
    public abstract String description();

    public DNSResolverTask(JmDNSImpl jmDNSImpl) {
        super(jmDNSImpl);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" count: ");
        sb.append(this._count);
        return sb.toString();
    }

    public void start(Timer timer) {
        if (!getDns().isCanceling() && !getDns().isCanceled()) {
            timer.schedule(this, 225, 225);
        }
    }

    public void run() {
        try {
            if (!getDns().isCanceling()) {
                if (!getDns().isCanceled()) {
                    int i = this._count;
                    this._count = i + 1;
                    if (i < 3) {
                        if (logger.isLoggable(Level.FINER)) {
                            Logger logger2 = logger;
                            StringBuilder sb = new StringBuilder();
                            sb.append(getName());
                            sb.append(".run() JmDNS ");
                            sb.append(description());
                            logger2.finer(sb.toString());
                        }
                        DNSOutgoing addQuestions = addQuestions(new DNSOutgoing(0));
                        if (getDns().isAnnounced()) {
                            addQuestions = addAnswers(addQuestions);
                        }
                        if (!addQuestions.isEmpty()) {
                            getDns().send(addQuestions);
                            return;
                        }
                        return;
                    }
                    cancel();
                    return;
                }
            }
            cancel();
        } catch (Throwable th) {
            Logger logger3 = logger;
            Level level = Level.WARNING;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getName());
            sb2.append(".run() exception ");
            logger3.log(level, sb2.toString(), th);
            getDns().recover();
        }
    }
}
