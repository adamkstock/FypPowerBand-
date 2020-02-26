package org.eclipse.paho.client.mqttv3.internal;

import java.io.IOException;
import java.io.OutputStream;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttDisconnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttOutputStream;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class CommsSender implements Runnable {
    private static final String CLASS_NAME;
    static /* synthetic */ Class class$0;
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    private ClientComms clientComms = null;
    private ClientState clientState = null;
    private Object lifecycle = new Object();
    private MqttOutputStream out;
    private boolean running = false;
    private Thread sendThread = null;
    private CommsTokenStore tokenStore = null;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.CommsSender");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
    }

    public CommsSender(ClientComms clientComms2, ClientState clientState2, CommsTokenStore commsTokenStore, OutputStream outputStream) {
        this.out = new MqttOutputStream(clientState2, outputStream);
        this.clientComms = clientComms2;
        this.clientState = clientState2;
        this.tokenStore = commsTokenStore;
        log.setResourceName(clientComms2.getClient().getClientId());
    }

    public void start(String str) {
        synchronized (this.lifecycle) {
            if (!this.running) {
                this.running = true;
                this.sendThread = new Thread(this, str);
                this.sendThread.start();
            }
        }
    }

    public void stop() {
        synchronized (this.lifecycle) {
            log.fine(CLASS_NAME, "stop", "800");
            if (this.running) {
                this.running = false;
                if (!Thread.currentThread().equals(this.sendThread)) {
                    try {
                        this.clientState.notifyQueueLock();
                        this.sendThread.join();
                    } catch (InterruptedException unused) {
                    }
                }
            }
            this.sendThread = null;
            log.fine(CLASS_NAME, "stop", "801");
        }
    }

    public void run() {
        MqttWireMessage mqttWireMessage = null;
        while (this.running && this.out != null) {
            try {
                mqttWireMessage = this.clientState.get();
                if (mqttWireMessage != null) {
                    log.fine(CLASS_NAME, "run", "802", new Object[]{mqttWireMessage.getKey(), mqttWireMessage});
                    if (mqttWireMessage instanceof MqttAck) {
                        this.out.write(mqttWireMessage);
                        this.out.flush();
                    } else {
                        MqttToken token = this.tokenStore.getToken(mqttWireMessage);
                        if (token != null) {
                            synchronized (token) {
                                this.out.write(mqttWireMessage);
                                try {
                                    this.out.flush();
                                } catch (IOException e) {
                                    if (!(mqttWireMessage instanceof MqttDisconnect)) {
                                        throw e;
                                    }
                                }
                                this.clientState.notifySent(mqttWireMessage);
                            }
                        } else {
                            continue;
                        }
                    }
                } else {
                    log.fine(CLASS_NAME, "run", "803");
                    this.running = false;
                }
            } catch (MqttException e2) {
                handleRunException(mqttWireMessage, e2);
            } catch (Exception e3) {
                handleRunException(mqttWireMessage, e3);
            }
        }
        log.fine(CLASS_NAME, "run", "805");
    }

    private void handleRunException(MqttWireMessage mqttWireMessage, Exception exc) {
        MqttException mqttException;
        log.fine(CLASS_NAME, "handleRunException", "804", null, exc);
        if (!(exc instanceof MqttException)) {
            mqttException = new MqttException(32109, exc);
        } else {
            mqttException = (MqttException) exc;
        }
        this.running = false;
        this.clientComms.shutdownConnection(null, mqttException);
    }
}
