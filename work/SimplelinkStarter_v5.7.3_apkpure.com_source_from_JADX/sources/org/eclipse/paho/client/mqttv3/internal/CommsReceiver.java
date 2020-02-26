package org.eclipse.paho.client.mqttv3.internal;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttInputStream;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class CommsReceiver implements Runnable {
    private static final String CLASS_NAME;
    static /* synthetic */ Class class$0;
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    private ClientComms clientComms = null;
    private ClientState clientState = null;

    /* renamed from: in */
    private MqttInputStream f98in;
    private Object lifecycle = new Object();
    private Thread recThread = null;
    private volatile boolean receiving;
    private boolean running = false;
    private CommsTokenStore tokenStore = null;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.CommsReceiver");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
    }

    public CommsReceiver(ClientComms clientComms2, ClientState clientState2, CommsTokenStore commsTokenStore, InputStream inputStream) {
        this.f98in = new MqttInputStream(clientState2, inputStream);
        this.clientComms = clientComms2;
        this.clientState = clientState2;
        this.tokenStore = commsTokenStore;
        log.setResourceName(clientComms2.getClient().getClientId());
    }

    public void start(String str) {
        log.fine(CLASS_NAME, "start", "855");
        synchronized (this.lifecycle) {
            if (!this.running) {
                this.running = true;
                this.recThread = new Thread(this, str);
                this.recThread.start();
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(2:5|(2:7|8))|9|10) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0028 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stop() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.lifecycle
            monitor-enter(r0)
            org.eclipse.paho.client.mqttv3.logging.Logger r1 = log     // Catch:{ all -> 0x0038 }
            java.lang.String r2 = CLASS_NAME     // Catch:{ all -> 0x0038 }
            java.lang.String r3 = "stop"
            java.lang.String r4 = "850"
            r1.fine(r2, r3, r4)     // Catch:{ all -> 0x0038 }
            boolean r1 = r5.running     // Catch:{ all -> 0x0038 }
            if (r1 == 0) goto L_0x0028
            r1 = 0
            r5.running = r1     // Catch:{ all -> 0x0038 }
            r5.receiving = r1     // Catch:{ all -> 0x0038 }
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0038 }
            java.lang.Thread r2 = r5.recThread     // Catch:{ all -> 0x0038 }
            boolean r1 = r1.equals(r2)     // Catch:{ all -> 0x0038 }
            if (r1 != 0) goto L_0x0028
            java.lang.Thread r1 = r5.recThread     // Catch:{ InterruptedException -> 0x0028 }
            r1.join()     // Catch:{ InterruptedException -> 0x0028 }
        L_0x0028:
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            r0 = 0
            r5.recThread = r0
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log
            java.lang.String r1 = CLASS_NAME
            java.lang.String r2 = "stop"
            java.lang.String r3 = "851"
            r0.fine(r1, r2, r3)
            return
        L_0x0038:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.CommsReceiver.stop():void");
    }

    public void run() {
        MqttToken mqttToken = null;
        while (this.running && this.f98in != null) {
            try {
                log.fine(CLASS_NAME, "run", "852");
                this.receiving = this.f98in.available() > 0;
                MqttWireMessage readMqttWireMessage = this.f98in.readMqttWireMessage();
                this.receiving = false;
                if (readMqttWireMessage instanceof MqttAck) {
                    mqttToken = this.tokenStore.getToken(readMqttWireMessage);
                    if (mqttToken != null) {
                        synchronized (mqttToken) {
                            this.clientState.notifyReceivedAck((MqttAck) readMqttWireMessage);
                        }
                    } else {
                        throw new MqttException(6);
                    }
                } else {
                    this.clientState.notifyReceivedMsg(readMqttWireMessage);
                }
            } catch (MqttException e) {
                log.fine(CLASS_NAME, "run", "856", null, e);
                this.running = false;
                this.clientComms.shutdownConnection(mqttToken, e);
            } catch (IOException e2) {
                try {
                    log.fine(CLASS_NAME, "run", "853");
                    this.running = false;
                    if (!this.clientComms.isDisconnecting()) {
                        this.clientComms.shutdownConnection(mqttToken, new MqttException(32109, e2));
                    }
                } catch (Throwable th) {
                    this.receiving = false;
                    throw th;
                }
            }
            this.receiving = false;
        }
        log.fine(CLASS_NAME, "run", "854");
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean isReceiving() {
        return this.receiving;
    }
}
