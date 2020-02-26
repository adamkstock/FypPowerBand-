package javax.jmdns.impl;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import javax.jmdns.impl.tasks.RecordReaper;
import javax.jmdns.impl.tasks.Responder;
import javax.jmdns.impl.tasks.resolver.ServiceInfoResolver;
import javax.jmdns.impl.tasks.resolver.ServiceResolver;
import javax.jmdns.impl.tasks.resolver.TypeResolver;
import javax.jmdns.impl.tasks.state.Announcer;
import javax.jmdns.impl.tasks.state.Canceler;
import javax.jmdns.impl.tasks.state.Prober;
import javax.jmdns.impl.tasks.state.Renewer;

public interface DNSTaskStarter {

    public static final class DNSTaskStarterImpl implements DNSTaskStarter {
        private final JmDNSImpl _jmDNSImpl;
        private final Timer _stateTimer;
        private final Timer _timer;

        public static class StarterTimer extends Timer {
            private volatile boolean _cancelled = false;

            public StarterTimer() {
            }

            public StarterTimer(boolean z) {
                super(z);
            }

            public StarterTimer(String str, boolean z) {
                super(str, z);
            }

            public StarterTimer(String str) {
                super(str);
            }

            public synchronized void cancel() {
                if (!this._cancelled) {
                    this._cancelled = true;
                    super.cancel();
                }
            }

            public synchronized void schedule(TimerTask timerTask, long j) {
                if (!this._cancelled) {
                    super.schedule(timerTask, j);
                }
            }

            public synchronized void schedule(TimerTask timerTask, Date date) {
                if (!this._cancelled) {
                    super.schedule(timerTask, date);
                }
            }

            public synchronized void schedule(TimerTask timerTask, long j, long j2) {
                if (!this._cancelled) {
                    super.schedule(timerTask, j, j2);
                }
            }

            public synchronized void schedule(TimerTask timerTask, Date date, long j) {
                if (!this._cancelled) {
                    super.schedule(timerTask, date, j);
                }
            }

            public synchronized void scheduleAtFixedRate(TimerTask timerTask, long j, long j2) {
                if (!this._cancelled) {
                    super.scheduleAtFixedRate(timerTask, j, j2);
                }
            }

            public synchronized void scheduleAtFixedRate(TimerTask timerTask, Date date, long j) {
                if (!this._cancelled) {
                    super.scheduleAtFixedRate(timerTask, date, j);
                }
            }
        }

        public DNSTaskStarterImpl(JmDNSImpl jmDNSImpl) {
            this._jmDNSImpl = jmDNSImpl;
            StringBuilder sb = new StringBuilder();
            String str = "JmDNS(";
            sb.append(str);
            sb.append(this._jmDNSImpl.getName());
            sb.append(").Timer");
            this._timer = new StarterTimer(sb.toString(), true);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(this._jmDNSImpl.getName());
            sb2.append(").State.Timer");
            this._stateTimer = new StarterTimer(sb2.toString(), true);
        }

        public void purgeTimer() {
            this._timer.purge();
        }

        public void purgeStateTimer() {
            this._stateTimer.purge();
        }

        public void cancelTimer() {
            this._timer.cancel();
        }

        public void cancelStateTimer() {
            this._stateTimer.cancel();
        }

        public void startProber() {
            new Prober(this._jmDNSImpl).start(this._stateTimer);
        }

        public void startAnnouncer() {
            new Announcer(this._jmDNSImpl).start(this._stateTimer);
        }

        public void startRenewer() {
            new Renewer(this._jmDNSImpl).start(this._stateTimer);
        }

        public void startCanceler() {
            new Canceler(this._jmDNSImpl).start(this._stateTimer);
        }

        public void startReaper() {
            new RecordReaper(this._jmDNSImpl).start(this._timer);
        }

        public void startServiceInfoResolver(ServiceInfoImpl serviceInfoImpl) {
            new ServiceInfoResolver(this._jmDNSImpl, serviceInfoImpl).start(this._timer);
        }

        public void startTypeResolver() {
            new TypeResolver(this._jmDNSImpl).start(this._timer);
        }

        public void startServiceResolver(String str) {
            new ServiceResolver(this._jmDNSImpl, str).start(this._timer);
        }

        public void startResponder(DNSIncoming dNSIncoming, int i) {
            new Responder(this._jmDNSImpl, dNSIncoming, i).start(this._timer);
        }
    }

    public static final class Factory {
        private static final AtomicReference<ClassDelegate> _databaseClassDelegate = new AtomicReference<>();
        private static volatile Factory _instance;
        private final ConcurrentMap<JmDNSImpl, DNSTaskStarter> _instances = new ConcurrentHashMap(20);

        public interface ClassDelegate {
            DNSTaskStarter newDNSTaskStarter(JmDNSImpl jmDNSImpl);
        }

        private Factory() {
        }

        public static void setClassDelegate(ClassDelegate classDelegate) {
            _databaseClassDelegate.set(classDelegate);
        }

        public static ClassDelegate classDelegate() {
            return (ClassDelegate) _databaseClassDelegate.get();
        }

        protected static DNSTaskStarter newDNSTaskStarter(JmDNSImpl jmDNSImpl) {
            ClassDelegate classDelegate = (ClassDelegate) _databaseClassDelegate.get();
            DNSTaskStarter newDNSTaskStarter = classDelegate != null ? classDelegate.newDNSTaskStarter(jmDNSImpl) : null;
            return newDNSTaskStarter != null ? newDNSTaskStarter : new DNSTaskStarterImpl(jmDNSImpl);
        }

        public static Factory getInstance() {
            if (_instance == null) {
                synchronized (Factory.class) {
                    if (_instance == null) {
                        _instance = new Factory();
                    }
                }
            }
            return _instance;
        }

        public DNSTaskStarter getStarter(JmDNSImpl jmDNSImpl) {
            DNSTaskStarter dNSTaskStarter = (DNSTaskStarter) this._instances.get(jmDNSImpl);
            if (dNSTaskStarter != null) {
                return dNSTaskStarter;
            }
            this._instances.putIfAbsent(jmDNSImpl, newDNSTaskStarter(jmDNSImpl));
            return (DNSTaskStarter) this._instances.get(jmDNSImpl);
        }

        public void disposeStarter(JmDNSImpl jmDNSImpl) {
            this._instances.remove(jmDNSImpl);
        }
    }

    void cancelStateTimer();

    void cancelTimer();

    void purgeStateTimer();

    void purgeTimer();

    void startAnnouncer();

    void startCanceler();

    void startProber();

    void startReaper();

    void startRenewer();

    void startResponder(DNSIncoming dNSIncoming, int i);

    void startServiceInfoResolver(ServiceInfoImpl serviceInfoImpl);

    void startServiceResolver(String str);

    void startTypeResolver();
}
