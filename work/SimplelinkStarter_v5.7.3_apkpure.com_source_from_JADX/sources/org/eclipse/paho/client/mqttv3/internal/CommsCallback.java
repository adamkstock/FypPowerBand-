package org.eclipse.paho.client.mqttv3.internal;

import java.util.Vector;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubComp;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class CommsCallback implements Runnable {
    private static final String CLASS_NAME;
    private static final int INBOUND_QUEUE_SIZE = 10;
    static /* synthetic */ Class class$0;
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    private Thread callbackThread;
    private ClientComms clientComms;
    private ClientState clientState;
    private Vector completeQueue;
    private Object lifecycle = new Object();
    private Vector messageQueue;
    private MqttCallback mqttCallback;
    private boolean quiescing = false;
    public boolean running = false;
    private Object spaceAvailable = new Object();
    private Object workAvailable = new Object();

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.CommsCallback");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
    }

    CommsCallback(ClientComms clientComms2) {
        this.clientComms = clientComms2;
        this.messageQueue = new Vector(10);
        this.completeQueue = new Vector(10);
        log.setResourceName(clientComms2.getClient().getClientId());
    }

    public void setClientState(ClientState clientState2) {
        this.clientState = clientState2;
    }

    public void start(String str) {
        synchronized (this.lifecycle) {
            if (!this.running) {
                this.messageQueue.clear();
                this.completeQueue.clear();
                this.running = true;
                this.quiescing = false;
                this.callbackThread = new Thread(this, str);
                this.callbackThread.start();
            }
        }
    }

    public void stop() {
        synchronized (this.lifecycle) {
            if (this.running) {
                log.fine(CLASS_NAME, "stop", "700");
                this.running = false;
                if (!Thread.currentThread().equals(this.callbackThread)) {
                    try {
                        synchronized (this.workAvailable) {
                            log.fine(CLASS_NAME, "stop", "701");
                            this.workAvailable.notifyAll();
                        }
                        this.callbackThread.join();
                    } catch (InterruptedException unused) {
                    }
                }
            }
            this.callbackThread = null;
            log.fine(CLASS_NAME, "stop", "703");
        }
    }

    public void setCallback(MqttCallback mqttCallback2) {
        this.mqttCallback = mqttCallback2;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0035 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r9 = this;
        L_0x0000:
            boolean r0 = r9.running
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            r0 = 0
            r1 = 0
            java.lang.Object r2 = r9.workAvailable     // Catch:{ InterruptedException -> 0x0035 }
            monitor-enter(r2)     // Catch:{ InterruptedException -> 0x0035 }
            boolean r3 = r9.running     // Catch:{ all -> 0x0030 }
            if (r3 == 0) goto L_0x002e
            java.util.Vector r3 = r9.messageQueue     // Catch:{ all -> 0x0030 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0030 }
            if (r3 == 0) goto L_0x002e
            java.util.Vector r3 = r9.completeQueue     // Catch:{ all -> 0x0030 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0030 }
            if (r3 == 0) goto L_0x002e
            org.eclipse.paho.client.mqttv3.logging.Logger r3 = log     // Catch:{ all -> 0x0030 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ all -> 0x0030 }
            java.lang.String r5 = "run"
            java.lang.String r6 = "704"
            r3.fine(r4, r5, r6)     // Catch:{ all -> 0x0030 }
            java.lang.Object r3 = r9.workAvailable     // Catch:{ all -> 0x0030 }
            r3.wait()     // Catch:{ all -> 0x0030 }
        L_0x002e:
            monitor-exit(r2)     // Catch:{ all -> 0x0030 }
            goto L_0x0035
        L_0x0030:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0030 }
            throw r3     // Catch:{ InterruptedException -> 0x0035 }
        L_0x0033:
            r2 = move-exception
            goto L_0x008a
        L_0x0035:
            boolean r2 = r9.running     // Catch:{ all -> 0x0033 }
            if (r2 == 0) goto L_0x0080
            java.util.Vector r2 = r9.completeQueue     // Catch:{ all -> 0x0033 }
            monitor-enter(r2)     // Catch:{ all -> 0x0033 }
            java.util.Vector r3 = r9.completeQueue     // Catch:{ all -> 0x007d }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x007d }
            if (r3 != 0) goto L_0x0052
            java.util.Vector r3 = r9.completeQueue     // Catch:{ all -> 0x007d }
            java.lang.Object r3 = r3.elementAt(r1)     // Catch:{ all -> 0x007d }
            org.eclipse.paho.client.mqttv3.MqttToken r3 = (org.eclipse.paho.client.mqttv3.MqttToken) r3     // Catch:{ all -> 0x007d }
            java.util.Vector r4 = r9.completeQueue     // Catch:{ all -> 0x007d }
            r4.removeElementAt(r1)     // Catch:{ all -> 0x007d }
            goto L_0x0053
        L_0x0052:
            r3 = r0
        L_0x0053:
            monitor-exit(r2)     // Catch:{ all -> 0x007d }
            if (r3 == 0) goto L_0x0059
            r9.handleActionComplete(r3)     // Catch:{ all -> 0x0033 }
        L_0x0059:
            java.util.Vector r2 = r9.messageQueue     // Catch:{ all -> 0x0033 }
            monitor-enter(r2)     // Catch:{ all -> 0x0033 }
            java.util.Vector r3 = r9.messageQueue     // Catch:{ all -> 0x007a }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x007a }
            if (r3 != 0) goto L_0x0072
            java.util.Vector r3 = r9.messageQueue     // Catch:{ all -> 0x007a }
            java.lang.Object r3 = r3.elementAt(r1)     // Catch:{ all -> 0x007a }
            org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish r3 = (org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish) r3     // Catch:{ all -> 0x007a }
            java.util.Vector r4 = r9.messageQueue     // Catch:{ all -> 0x007a }
            r4.removeElementAt(r1)     // Catch:{ all -> 0x007a }
            goto L_0x0073
        L_0x0072:
            r3 = r0
        L_0x0073:
            monitor-exit(r2)     // Catch:{ all -> 0x007a }
            if (r3 == 0) goto L_0x0080
            r9.handleMessage(r3)     // Catch:{ all -> 0x0033 }
            goto L_0x0080
        L_0x007a:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x007a }
            throw r3     // Catch:{ all -> 0x0033 }
        L_0x007d:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x007d }
            throw r3     // Catch:{ all -> 0x0033 }
        L_0x0080:
            boolean r2 = r9.quiescing     // Catch:{ all -> 0x0033 }
            if (r2 == 0) goto L_0x00a3
            org.eclipse.paho.client.mqttv3.internal.ClientState r2 = r9.clientState     // Catch:{ all -> 0x0033 }
            r2.checkQuiesceLock()     // Catch:{ all -> 0x0033 }
            goto L_0x00a3
        L_0x008a:
            org.eclipse.paho.client.mqttv3.logging.Logger r3 = log     // Catch:{ all -> 0x00bc }
            java.lang.String r4 = CLASS_NAME     // Catch:{ all -> 0x00bc }
            java.lang.String r5 = "run"
            java.lang.String r6 = "714"
            r7 = 0
            r8 = r2
            r3.fine(r4, r5, r6, r7, r8)     // Catch:{ all -> 0x00bc }
            r9.running = r1     // Catch:{ all -> 0x00bc }
            org.eclipse.paho.client.mqttv3.internal.ClientComms r1 = r9.clientComms     // Catch:{ all -> 0x00bc }
            org.eclipse.paho.client.mqttv3.MqttException r3 = new org.eclipse.paho.client.mqttv3.MqttException     // Catch:{ all -> 0x00bc }
            r3.<init>(r2)     // Catch:{ all -> 0x00bc }
            r1.shutdownConnection(r0, r3)     // Catch:{ all -> 0x00bc }
        L_0x00a3:
            java.lang.Object r0 = r9.spaceAvailable
            monitor-enter(r0)
            org.eclipse.paho.client.mqttv3.logging.Logger r1 = log     // Catch:{ all -> 0x00b9 }
            java.lang.String r2 = CLASS_NAME     // Catch:{ all -> 0x00b9 }
            java.lang.String r3 = "run"
            java.lang.String r4 = "706"
            r1.fine(r2, r3, r4)     // Catch:{ all -> 0x00b9 }
            java.lang.Object r1 = r9.spaceAvailable     // Catch:{ all -> 0x00b9 }
            r1.notifyAll()     // Catch:{ all -> 0x00b9 }
            monitor-exit(r0)     // Catch:{ all -> 0x00b9 }
            goto L_0x0000
        L_0x00b9:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00b9 }
            throw r1
        L_0x00bc:
            r0 = move-exception
            java.lang.Object r1 = r9.spaceAvailable
            monitor-enter(r1)
            org.eclipse.paho.client.mqttv3.logging.Logger r2 = log     // Catch:{ all -> 0x00d2 }
            java.lang.String r3 = CLASS_NAME     // Catch:{ all -> 0x00d2 }
            java.lang.String r4 = "run"
            java.lang.String r5 = "706"
            r2.fine(r3, r4, r5)     // Catch:{ all -> 0x00d2 }
            java.lang.Object r2 = r9.spaceAvailable     // Catch:{ all -> 0x00d2 }
            r2.notifyAll()     // Catch:{ all -> 0x00d2 }
            monitor-exit(r1)     // Catch:{ all -> 0x00d2 }
            throw r0
        L_0x00d2:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x00d2 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.CommsCallback.run():void");
    }

    private void handleActionComplete(MqttToken mqttToken) throws MqttException {
        synchronized (mqttToken) {
            log.fine(CLASS_NAME, "handleActionComplete", "705", new Object[]{mqttToken.internalTok.getKey()});
            mqttToken.internalTok.notifyComplete();
            if (!mqttToken.internalTok.isNotified()) {
                if (this.mqttCallback != null && (mqttToken instanceof MqttDeliveryToken) && mqttToken.isComplete()) {
                    this.mqttCallback.deliveryComplete((MqttDeliveryToken) mqttToken);
                }
                fireActionEvent(mqttToken);
            }
            if (mqttToken.isComplete() && ((mqttToken instanceof MqttDeliveryToken) || (mqttToken.getActionCallback() instanceof IMqttActionListener))) {
                mqttToken.internalTok.setNotified(true);
            }
            if (mqttToken.isComplete()) {
                this.clientState.notifyComplete(mqttToken);
            }
        }
    }

    public void connectionLost(MqttException mqttException) {
        String str = "connectionLost";
        try {
            if (this.mqttCallback != null && mqttException != null) {
                log.fine(CLASS_NAME, str, "708", new Object[]{mqttException});
                this.mqttCallback.connectionLost(mqttException);
            }
        } catch (Throwable th) {
            log.fine(CLASS_NAME, str, "720", new Object[]{th});
        }
    }

    public void fireActionEvent(MqttToken mqttToken) {
        if (mqttToken != null) {
            IMqttActionListener actionCallback = mqttToken.getActionCallback();
            if (actionCallback != null) {
                String str = "716";
                String str2 = "fireActionEvent";
                if (mqttToken.getException() == null) {
                    log.fine(CLASS_NAME, str2, str, new Object[]{mqttToken.internalTok.getKey()});
                    actionCallback.onSuccess(mqttToken);
                    return;
                }
                log.fine(CLASS_NAME, str2, str, new Object[]{mqttToken.internalTok.getKey()});
                actionCallback.onFailure(mqttToken, mqttToken.getException());
            }
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0007 */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0007 A[LOOP:0: B:4:0x0007->B:31:0x0007, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void messageArrived(org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish r6) {
        /*
            r5 = this;
            org.eclipse.paho.client.mqttv3.MqttCallback r0 = r5.mqttCallback
            if (r0 == 0) goto L_0x0052
            java.lang.Object r0 = r5.spaceAvailable
            monitor-enter(r0)
        L_0x0007:
            boolean r1 = r5.running     // Catch:{ all -> 0x004f }
            if (r1 == 0) goto L_0x002d
            boolean r1 = r5.quiescing     // Catch:{ all -> 0x004f }
            if (r1 != 0) goto L_0x002d
            java.util.Vector r1 = r5.messageQueue     // Catch:{ all -> 0x004f }
            int r1 = r1.size()     // Catch:{ all -> 0x004f }
            r2 = 10
            if (r1 >= r2) goto L_0x001a
            goto L_0x002d
        L_0x001a:
            org.eclipse.paho.client.mqttv3.logging.Logger r1 = log     // Catch:{ InterruptedException -> 0x0007 }
            java.lang.String r2 = CLASS_NAME     // Catch:{ InterruptedException -> 0x0007 }
            java.lang.String r3 = "messageArrived"
            java.lang.String r4 = "709"
            r1.fine(r2, r3, r4)     // Catch:{ InterruptedException -> 0x0007 }
            java.lang.Object r1 = r5.spaceAvailable     // Catch:{ InterruptedException -> 0x0007 }
            r2 = 200(0xc8, double:9.9E-322)
            r1.wait(r2)     // Catch:{ InterruptedException -> 0x0007 }
            goto L_0x0007
        L_0x002d:
            monitor-exit(r0)     // Catch:{ all -> 0x004f }
            boolean r0 = r5.quiescing
            if (r0 != 0) goto L_0x0052
            java.util.Vector r0 = r5.messageQueue
            r0.addElement(r6)
            java.lang.Object r6 = r5.workAvailable
            monitor-enter(r6)
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log     // Catch:{ all -> 0x004c }
            java.lang.String r1 = CLASS_NAME     // Catch:{ all -> 0x004c }
            java.lang.String r2 = "messageArrived"
            java.lang.String r3 = "710"
            r0.fine(r1, r2, r3)     // Catch:{ all -> 0x004c }
            java.lang.Object r0 = r5.workAvailable     // Catch:{ all -> 0x004c }
            r0.notifyAll()     // Catch:{ all -> 0x004c }
            monitor-exit(r6)     // Catch:{ all -> 0x004c }
            goto L_0x0052
        L_0x004c:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x004c }
            throw r0
        L_0x004f:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004f }
            throw r6
        L_0x0052:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.CommsCallback.messageArrived(org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish):void");
    }

    public void quiesce() {
        this.quiescing = true;
        synchronized (this.spaceAvailable) {
            log.fine(CLASS_NAME, "quiesce", "711");
            this.spaceAvailable.notifyAll();
        }
    }

    public boolean isQuiesced() {
        return this.quiescing && this.completeQueue.size() == 0 && this.messageQueue.size() == 0;
    }

    private void handleMessage(MqttPublish mqttPublish) throws MqttException, Exception {
        if (this.mqttCallback != null) {
            String topicName = mqttPublish.getTopicName();
            log.fine(CLASS_NAME, "handleMessage", "713", new Object[]{new Integer(mqttPublish.getMessageId()), topicName});
            this.mqttCallback.messageArrived(topicName, mqttPublish.getMessage());
            if (mqttPublish.getMessage().getQos() == 1) {
                this.clientComms.internalSend(new MqttPubAck(mqttPublish), new MqttToken(this.clientComms.getClient().getClientId()));
            } else if (mqttPublish.getMessage().getQos() == 2) {
                this.clientComms.deliveryComplete(mqttPublish);
                MqttPubComp mqttPubComp = new MqttPubComp(mqttPublish);
                ClientComms clientComms2 = this.clientComms;
                clientComms2.internalSend(mqttPubComp, new MqttToken(clientComms2.getClient().getClientId()));
            }
        }
    }

    public void asyncOperationComplete(MqttToken mqttToken) {
        if (this.running) {
            this.completeQueue.addElement(mqttToken);
            synchronized (this.workAvailable) {
                log.fine(CLASS_NAME, "asyncOperationComplete", "715", new Object[]{mqttToken.internalTok.getKey()});
                this.workAvailable.notifyAll();
            }
            return;
        }
        try {
            handleActionComplete(mqttToken);
        } catch (Throwable th) {
            log.fine(CLASS_NAME, "asyncOperationComplete", "719", null, th);
            this.clientComms.shutdownConnection(null, new MqttException(th));
        }
    }

    /* access modifiers changed from: protected */
    public Thread getThread() {
        return this.callbackThread;
    }
}
