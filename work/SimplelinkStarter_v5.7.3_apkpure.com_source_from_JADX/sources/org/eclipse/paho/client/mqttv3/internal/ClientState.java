package org.eclipse.paho.client.mqttv3.internal;

import java.io.EOFException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnack;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPingReq;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPingResp;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubComp;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubRec;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubRel;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class ClientState {
    private static final String CLASS_NAME;
    private static final int DEFAULT_MAX_INFLIGHT = 10;
    private static final int MAX_MSG_ID = 65535;
    private static final int MIN_MSG_ID = 1;
    private static final String PERSISTENCE_CONFIRMED_PREFIX = "sc-";
    private static final String PERSISTENCE_RECEIVED_PREFIX = "r-";
    private static final String PERSISTENCE_SENT_PREFIX = "s-";
    static /* synthetic */ Class class$0;
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    private int actualInFlight = 0;
    private CommsCallback callback = null;
    private boolean cleanSession;
    private ClientComms clientComms = null;
    private boolean connected = false;
    private int inFlightPubRels = 0;
    private Hashtable inUseMsgIds;
    private Hashtable inboundQoS2 = null;
    private long keepAlive;
    private long lastInboundActivity = 0;
    private long lastOutboundActivity = 0;
    private long lastPing = 0;
    private int maxInflight = 10;
    private int nextMsgId = 0;
    private Hashtable outboundQoS1 = null;
    private Hashtable outboundQoS2 = null;
    private volatile Vector pendingFlows;
    private volatile Vector pendingMessages;
    private MqttClientPersistence persistence;
    private MqttWireMessage pingCommand;
    private int pingOutstanding = 0;
    private Object pingOutstandingLock = new Object();
    private MqttPingSender pingSender = null;
    private Object queueLock = new Object();
    private Object quiesceLock = new Object();
    private boolean quiescing = false;
    private CommsTokenStore tokenStore;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.ClientState");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
    }

    protected ClientState(MqttClientPersistence mqttClientPersistence, CommsTokenStore commsTokenStore, CommsCallback commsCallback, ClientComms clientComms2, MqttPingSender mqttPingSender) throws MqttException {
        log.setResourceName(clientComms2.getClient().getClientId());
        log.finer(CLASS_NAME, "<Init>", "");
        this.inUseMsgIds = new Hashtable();
        this.pendingMessages = new Vector(this.maxInflight);
        this.pendingFlows = new Vector();
        this.outboundQoS2 = new Hashtable();
        this.outboundQoS1 = new Hashtable();
        this.inboundQoS2 = new Hashtable();
        this.pingCommand = new MqttPingReq();
        this.inFlightPubRels = 0;
        this.actualInFlight = 0;
        this.persistence = mqttClientPersistence;
        this.callback = commsCallback;
        this.tokenStore = commsTokenStore;
        this.clientComms = clientComms2;
        this.pingSender = mqttPingSender;
        restoreState();
    }

    /* access modifiers changed from: protected */
    public void setKeepAliveSecs(long j) {
        this.keepAlive = j * 1000;
    }

    /* access modifiers changed from: protected */
    public long getKeepAlive() {
        return this.keepAlive;
    }

    /* access modifiers changed from: protected */
    public void setCleanSession(boolean z) {
        this.cleanSession = z;
    }

    private String getSendPersistenceKey(MqttWireMessage mqttWireMessage) {
        StringBuffer stringBuffer = new StringBuffer(PERSISTENCE_SENT_PREFIX);
        stringBuffer.append(mqttWireMessage.getMessageId());
        return stringBuffer.toString();
    }

    private String getSendConfirmPersistenceKey(MqttWireMessage mqttWireMessage) {
        StringBuffer stringBuffer = new StringBuffer(PERSISTENCE_CONFIRMED_PREFIX);
        stringBuffer.append(mqttWireMessage.getMessageId());
        return stringBuffer.toString();
    }

    private String getReceivedPersistenceKey(MqttWireMessage mqttWireMessage) {
        StringBuffer stringBuffer = new StringBuffer(PERSISTENCE_RECEIVED_PREFIX);
        stringBuffer.append(mqttWireMessage.getMessageId());
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    public void clearState() throws MqttException {
        log.fine(CLASS_NAME, "clearState", ">");
        this.persistence.clear();
        this.inUseMsgIds.clear();
        this.pendingMessages.clear();
        this.pendingFlows.clear();
        this.outboundQoS2.clear();
        this.outboundQoS1.clear();
        this.inboundQoS2.clear();
        this.tokenStore.clear();
    }

    private MqttWireMessage restoreMessage(String str, MqttPersistable mqttPersistable) throws MqttException {
        MqttWireMessage mqttWireMessage;
        try {
            mqttWireMessage = MqttWireMessage.createWireMessage(mqttPersistable);
        } catch (MqttException e) {
            log.fine(CLASS_NAME, "restoreMessage", "602", new Object[]{str}, e);
            if (e.getCause() instanceof EOFException) {
                if (str != null) {
                    this.persistence.remove(str);
                }
                mqttWireMessage = null;
            } else {
                throw e;
            }
        }
        log.fine(CLASS_NAME, "restoreMessage", "601", new Object[]{str, mqttWireMessage});
        return mqttWireMessage;
    }

    private void insertInOrder(Vector vector, MqttWireMessage mqttWireMessage) {
        int messageId = mqttWireMessage.getMessageId();
        for (int i = 0; i < vector.size(); i++) {
            if (((MqttWireMessage) vector.elementAt(i)).getMessageId() > messageId) {
                vector.insertElementAt(mqttWireMessage, i);
                return;
            }
        }
        vector.addElement(mqttWireMessage);
    }

    private Vector reOrder(Vector vector) {
        Vector vector2 = new Vector();
        if (vector.size() == 0) {
            return vector2;
        }
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i < vector.size()) {
            int messageId = ((MqttWireMessage) vector.elementAt(i)).getMessageId();
            int i5 = messageId - i2;
            if (i5 > i3) {
                i4 = i;
                i3 = i5;
            }
            i++;
            i2 = messageId;
        }
        int i6 = (65535 - i2) + ((MqttWireMessage) vector.elementAt(0)).getMessageId() > i3 ? 0 : i4;
        for (int i7 = i6; i7 < vector.size(); i7++) {
            vector2.addElement(vector.elementAt(i7));
        }
        for (int i8 = 0; i8 < i6; i8++) {
            vector2.addElement(vector.elementAt(i8));
        }
        return vector2;
    }

    /* access modifiers changed from: protected */
    public void restoreState() throws MqttException {
        Enumeration keys = this.persistence.keys();
        int i = this.nextMsgId;
        Vector vector = new Vector();
        String str = "restoreState";
        log.fine(CLASS_NAME, str, "600");
        while (keys.hasMoreElements()) {
            String str2 = (String) keys.nextElement();
            MqttWireMessage restoreMessage = restoreMessage(str2, this.persistence.get(str2));
            if (restoreMessage != null) {
                if (str2.startsWith(PERSISTENCE_RECEIVED_PREFIX)) {
                    log.fine(CLASS_NAME, str, "604", new Object[]{str2, restoreMessage});
                    this.inboundQoS2.put(new Integer(restoreMessage.getMessageId()), restoreMessage);
                } else if (str2.startsWith(PERSISTENCE_SENT_PREFIX)) {
                    MqttPublish mqttPublish = (MqttPublish) restoreMessage;
                    i = Math.max(mqttPublish.getMessageId(), i);
                    if (this.persistence.containsKey(getSendConfirmPersistenceKey(mqttPublish))) {
                        MqttPubRel mqttPubRel = (MqttPubRel) restoreMessage(str2, this.persistence.get(getSendConfirmPersistenceKey(mqttPublish)));
                        if (mqttPubRel != null) {
                            log.fine(CLASS_NAME, str, "605", new Object[]{str2, restoreMessage});
                            this.outboundQoS2.put(new Integer(mqttPubRel.getMessageId()), mqttPubRel);
                        } else {
                            log.fine(CLASS_NAME, str, "606", new Object[]{str2, restoreMessage});
                        }
                    } else {
                        mqttPublish.setDuplicate(true);
                        if (mqttPublish.getMessage().getQos() == 2) {
                            log.fine(CLASS_NAME, str, "607", new Object[]{str2, restoreMessage});
                            this.outboundQoS2.put(new Integer(mqttPublish.getMessageId()), mqttPublish);
                        } else {
                            log.fine(CLASS_NAME, str, "608", new Object[]{str2, restoreMessage});
                            this.outboundQoS1.put(new Integer(mqttPublish.getMessageId()), mqttPublish);
                        }
                    }
                    this.tokenStore.restoreToken(mqttPublish).internalTok.setClient(this.clientComms.getClient());
                    this.inUseMsgIds.put(new Integer(mqttPublish.getMessageId()), new Integer(mqttPublish.getMessageId()));
                } else if (str2.startsWith(PERSISTENCE_CONFIRMED_PREFIX)) {
                    if (!this.persistence.containsKey(getSendPersistenceKey((MqttPubRel) restoreMessage))) {
                        vector.addElement(str2);
                    }
                }
            }
        }
        Enumeration elements = vector.elements();
        while (elements.hasMoreElements()) {
            String str3 = (String) elements.nextElement();
            log.fine(CLASS_NAME, str, "609", new Object[]{str3});
            this.persistence.remove(str3);
        }
        this.nextMsgId = i;
    }

    private void restoreInflightMessages() {
        String str;
        this.pendingMessages = new Vector(this.maxInflight);
        this.pendingFlows = new Vector();
        Enumeration keys = this.outboundQoS2.keys();
        while (true) {
            str = "restoreInflightMessages";
            if (!keys.hasMoreElements()) {
                break;
            }
            Object nextElement = keys.nextElement();
            MqttWireMessage mqttWireMessage = (MqttWireMessage) this.outboundQoS2.get(nextElement);
            if (mqttWireMessage instanceof MqttPublish) {
                log.fine(CLASS_NAME, str, "610", new Object[]{nextElement});
                mqttWireMessage.setDuplicate(true);
                insertInOrder(this.pendingMessages, (MqttPublish) mqttWireMessage);
            } else if (mqttWireMessage instanceof MqttPubRel) {
                log.fine(CLASS_NAME, str, "611", new Object[]{nextElement});
                insertInOrder(this.pendingFlows, (MqttPubRel) mqttWireMessage);
            }
        }
        Enumeration keys2 = this.outboundQoS1.keys();
        while (keys2.hasMoreElements()) {
            Object nextElement2 = keys2.nextElement();
            MqttPublish mqttPublish = (MqttPublish) this.outboundQoS1.get(nextElement2);
            mqttPublish.setDuplicate(true);
            log.fine(CLASS_NAME, str, "612", new Object[]{nextElement2});
            insertInOrder(this.pendingMessages, mqttPublish);
        }
        this.pendingFlows = reOrder(this.pendingFlows);
        this.pendingMessages = reOrder(this.pendingMessages);
    }

    public void send(MqttWireMessage mqttWireMessage, MqttToken mqttToken) throws MqttException {
        if (mqttWireMessage.isMessageIdRequired() && mqttWireMessage.getMessageId() == 0) {
            mqttWireMessage.setMessageId(getNextMessageId());
        }
        if (mqttToken != null) {
            try {
                mqttToken.internalTok.setMessageID(mqttWireMessage.getMessageId());
            } catch (Exception unused) {
            }
        }
        if (mqttWireMessage instanceof MqttPublish) {
            synchronized (this.queueLock) {
                if (this.actualInFlight < this.maxInflight) {
                    MqttMessage message = ((MqttPublish) mqttWireMessage).getMessage();
                    log.fine(CLASS_NAME, MqttServiceConstants.SEND_ACTION, "628", new Object[]{new Integer(mqttWireMessage.getMessageId()), new Integer(message.getQos()), mqttWireMessage});
                    int qos = message.getQos();
                    if (qos == 1) {
                        this.outboundQoS1.put(new Integer(mqttWireMessage.getMessageId()), mqttWireMessage);
                        this.persistence.put(getSendPersistenceKey(mqttWireMessage), (MqttPublish) mqttWireMessage);
                    } else if (qos == 2) {
                        this.outboundQoS2.put(new Integer(mqttWireMessage.getMessageId()), mqttWireMessage);
                        this.persistence.put(getSendPersistenceKey(mqttWireMessage), (MqttPublish) mqttWireMessage);
                    }
                    this.tokenStore.saveToken(mqttToken, mqttWireMessage);
                    this.pendingMessages.addElement(mqttWireMessage);
                    this.queueLock.notifyAll();
                } else {
                    log.fine(CLASS_NAME, MqttServiceConstants.SEND_ACTION, "613", new Object[]{new Integer(this.actualInFlight)});
                    throw new MqttException(32202);
                }
            }
            return;
        }
        log.fine(CLASS_NAME, MqttServiceConstants.SEND_ACTION, "615", new Object[]{new Integer(mqttWireMessage.getMessageId()), mqttWireMessage});
        if (mqttWireMessage instanceof MqttConnect) {
            synchronized (this.queueLock) {
                this.tokenStore.saveToken(mqttToken, mqttWireMessage);
                this.pendingFlows.insertElementAt(mqttWireMessage, 0);
                this.queueLock.notifyAll();
            }
            return;
        }
        if (mqttWireMessage instanceof MqttPingReq) {
            this.pingCommand = mqttWireMessage;
        } else if (mqttWireMessage instanceof MqttPubRel) {
            this.outboundQoS2.put(new Integer(mqttWireMessage.getMessageId()), mqttWireMessage);
            this.persistence.put(getSendConfirmPersistenceKey(mqttWireMessage), (MqttPubRel) mqttWireMessage);
        } else if (mqttWireMessage instanceof MqttPubComp) {
            this.persistence.remove(getReceivedPersistenceKey(mqttWireMessage));
        }
        synchronized (this.queueLock) {
            if (!(mqttWireMessage instanceof MqttAck)) {
                this.tokenStore.saveToken(mqttToken, mqttWireMessage);
            }
            this.pendingFlows.addElement(mqttWireMessage);
            this.queueLock.notifyAll();
        }
    }

    /* access modifiers changed from: protected */
    public void undo(MqttPublish mqttPublish) throws MqttPersistenceException {
        synchronized (this.queueLock) {
            log.fine(CLASS_NAME, "undo", "618", new Object[]{new Integer(mqttPublish.getMessageId()), new Integer(mqttPublish.getMessage().getQos())});
            if (mqttPublish.getMessage().getQos() == 1) {
                this.outboundQoS1.remove(new Integer(mqttPublish.getMessageId()));
            } else {
                this.outboundQoS2.remove(new Integer(mqttPublish.getMessageId()));
            }
            this.pendingMessages.removeElement(mqttPublish);
            this.persistence.remove(getSendPersistenceKey(mqttPublish));
            this.tokenStore.removeToken((MqttWireMessage) mqttPublish);
            checkQuiesceLock();
        }
    }

    /* JADX INFO: used method not loaded: org.eclipse.paho.client.mqttv3.internal.CommsTokenStore.saveToken(org.eclipse.paho.client.mqttv3.MqttToken, org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage):null, types can be incorrect */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0028, code lost:
        if (r1.keepAlive <= 0) goto L_0x0186;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        r5 = java.lang.System.currentTimeMillis();
        r2 = r1.pingOutstandingLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0039, code lost:
        if (r1.pingOutstanding <= 0) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003b, code lost:
        r15 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0046, code lost:
        if ((r5 - r1.lastInboundActivity) >= (r1.keepAlive + ((long) 100))) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0048, code lost:
        r13 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004a, code lost:
        log.severe(CLASS_NAME, "checkForActivity", "619", new java.lang.Object[]{new java.lang.Long(r1.keepAlive), new java.lang.Long(r1.lastOutboundActivity), new java.lang.Long(r1.lastInboundActivity), new java.lang.Long(r15), new java.lang.Long(r1.lastPing)});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x008a, code lost:
        throw org.eclipse.paho.client.mqttv3.internal.ExceptionHelper.createMqttException(32000);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x008b, code lost:
        r13 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x008e, code lost:
        if (r1.pingOutstanding != 0) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x009c, code lost:
        if ((r13 - r1.lastOutboundActivity) >= (r1.keepAlive * 2)) goto L_0x009f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x009f, code lost:
        log.severe(CLASS_NAME, "checkForActivity", "642", new java.lang.Object[]{new java.lang.Long(r1.keepAlive), new java.lang.Long(r1.lastOutboundActivity), new java.lang.Long(r1.lastInboundActivity), new java.lang.Long(r13), new java.lang.Long(r1.lastPing)});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00e0, code lost:
        throw org.eclipse.paho.client.mqttv3.internal.ExceptionHelper.createMqttException(32002);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00e3, code lost:
        if (r1.pingOutstanding != 0) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00ef, code lost:
        if ((r13 - r1.lastInboundActivity) >= (r1.keepAlive - ((long) 100))) goto L_0x00fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00fb, code lost:
        if ((r13 - r1.lastOutboundActivity) < (r1.keepAlive - ((long) 100))) goto L_0x014c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00fd, code lost:
        log.fine(CLASS_NAME, "checkForActivity", "620", new java.lang.Object[]{new java.lang.Long(r1.keepAlive), new java.lang.Long(r1.lastOutboundActivity), new java.lang.Long(r1.lastInboundActivity)});
        r0 = new org.eclipse.paho.client.mqttv3.MqttToken(r1.clientComms.getClient().getClientId());
        r1.tokenStore.saveToken(r0, r1.pingCommand);
        r1.pendingFlows.insertElementAt(r1.pingCommand, 0);
        r4 = getKeepAlive();
        notifyQueueLock();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x014c, code lost:
        log.fine(CLASS_NAME, "checkForActivity", "634", null);
        r4 = java.lang.Math.max(1, getKeepAlive() - (r13 - r1.lastOutboundActivity));
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0168, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0169, code lost:
        log.fine(CLASS_NAME, "checkForActivity", "624", new java.lang.Object[]{new java.lang.Long(r4)});
        r1.pingSender.schedule(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0186, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0188, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001b, code lost:
        getKeepAlive();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0020, code lost:
        if (r1.connected == false) goto L_0x0186;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.eclipse.paho.client.mqttv3.MqttToken checkForActivity() throws org.eclipse.paho.client.mqttv3.MqttException {
        /*
            r17 = this;
            r1 = r17
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log
            java.lang.String r2 = CLASS_NAME
            r3 = 0
            java.lang.Object[] r4 = new java.lang.Object[r3]
            java.lang.String r5 = "checkForActivity"
            java.lang.String r6 = "616"
            r0.fine(r2, r5, r6, r4)
            java.lang.Object r2 = r1.quiesceLock
            monitor-enter(r2)
            boolean r0 = r1.quiescing     // Catch:{ all -> 0x0189 }
            r4 = 0
            if (r0 == 0) goto L_0x001a
            monitor-exit(r2)     // Catch:{ all -> 0x0189 }
            return r4
        L_0x001a:
            monitor-exit(r2)     // Catch:{ all -> 0x0189 }
            r17.getKeepAlive()
            boolean r0 = r1.connected
            if (r0 == 0) goto L_0x0186
            long r5 = r1.keepAlive
            r7 = 0
            int r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r0 <= 0) goto L_0x0186
            long r5 = java.lang.System.currentTimeMillis()
            r0 = 100
            java.lang.Object r2 = r1.pingOutstandingLock
            monitor-enter(r2)
            int r7 = r1.pingOutstanding     // Catch:{ all -> 0x0183 }
            r9 = 5
            r10 = 2
            r11 = 3
            r12 = 1
            if (r7 <= 0) goto L_0x008b
            long r13 = r1.lastInboundActivity     // Catch:{ all -> 0x0183 }
            long r13 = r5 - r13
            r15 = r5
            long r4 = r1.keepAlive     // Catch:{ all -> 0x0183 }
            long r7 = (long) r0     // Catch:{ all -> 0x0183 }
            long r4 = r4 + r7
            int r7 = (r13 > r4 ? 1 : (r13 == r4 ? 0 : -1))
            if (r7 >= 0) goto L_0x004a
            r13 = r15
            goto L_0x008c
        L_0x004a:
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log     // Catch:{ all -> 0x0183 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ all -> 0x0183 }
            java.lang.String r5 = "checkForActivity"
            java.lang.String r7 = "619"
            java.lang.Object[] r8 = new java.lang.Object[r9]     // Catch:{ all -> 0x0183 }
            java.lang.Long r9 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r13 = r1.keepAlive     // Catch:{ all -> 0x0183 }
            r9.<init>(r13)     // Catch:{ all -> 0x0183 }
            r8[r3] = r9     // Catch:{ all -> 0x0183 }
            java.lang.Long r3 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r13 = r1.lastOutboundActivity     // Catch:{ all -> 0x0183 }
            r3.<init>(r13)     // Catch:{ all -> 0x0183 }
            r8[r12] = r3     // Catch:{ all -> 0x0183 }
            java.lang.Long r3 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r12 = r1.lastInboundActivity     // Catch:{ all -> 0x0183 }
            r3.<init>(r12)     // Catch:{ all -> 0x0183 }
            r8[r10] = r3     // Catch:{ all -> 0x0183 }
            java.lang.Long r3 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            r13 = r15
            r3.<init>(r13)     // Catch:{ all -> 0x0183 }
            r8[r11] = r3     // Catch:{ all -> 0x0183 }
            java.lang.Long r3 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r9 = r1.lastPing     // Catch:{ all -> 0x0183 }
            r3.<init>(r9)     // Catch:{ all -> 0x0183 }
            r6 = 4
            r8[r6] = r3     // Catch:{ all -> 0x0183 }
            r0.severe(r4, r5, r7, r8)     // Catch:{ all -> 0x0183 }
            r0 = 32000(0x7d00, float:4.4842E-41)
            org.eclipse.paho.client.mqttv3.MqttException r0 = org.eclipse.paho.client.mqttv3.internal.ExceptionHelper.createMqttException(r0)     // Catch:{ all -> 0x0183 }
            throw r0     // Catch:{ all -> 0x0183 }
        L_0x008b:
            r13 = r5
        L_0x008c:
            int r4 = r1.pingOutstanding     // Catch:{ all -> 0x0183 }
            if (r4 != 0) goto L_0x00e1
            long r4 = r1.lastOutboundActivity     // Catch:{ all -> 0x0183 }
            long r4 = r13 - r4
            r7 = 2
            long r10 = r1.keepAlive     // Catch:{ all -> 0x0183 }
            long r10 = r10 * r7
            int r7 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r7 >= 0) goto L_0x009f
            goto L_0x00e1
        L_0x009f:
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log     // Catch:{ all -> 0x0183 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ all -> 0x0183 }
            java.lang.String r5 = "checkForActivity"
            java.lang.String r7 = "642"
            java.lang.Object[] r8 = new java.lang.Object[r9]     // Catch:{ all -> 0x0183 }
            java.lang.Long r9 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r10 = r1.keepAlive     // Catch:{ all -> 0x0183 }
            r9.<init>(r10)     // Catch:{ all -> 0x0183 }
            r8[r3] = r9     // Catch:{ all -> 0x0183 }
            java.lang.Long r3 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r9 = r1.lastOutboundActivity     // Catch:{ all -> 0x0183 }
            r3.<init>(r9)     // Catch:{ all -> 0x0183 }
            r8[r12] = r3     // Catch:{ all -> 0x0183 }
            java.lang.Long r3 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r9 = r1.lastInboundActivity     // Catch:{ all -> 0x0183 }
            r3.<init>(r9)     // Catch:{ all -> 0x0183 }
            r9 = 2
            r8[r9] = r3     // Catch:{ all -> 0x0183 }
            java.lang.Long r3 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            r3.<init>(r13)     // Catch:{ all -> 0x0183 }
            r9 = 3
            r8[r9] = r3     // Catch:{ all -> 0x0183 }
            java.lang.Long r3 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r9 = r1.lastPing     // Catch:{ all -> 0x0183 }
            r3.<init>(r9)     // Catch:{ all -> 0x0183 }
            r6 = 4
            r8[r6] = r3     // Catch:{ all -> 0x0183 }
            r0.severe(r4, r5, r7, r8)     // Catch:{ all -> 0x0183 }
            r0 = 32002(0x7d02, float:4.4844E-41)
            org.eclipse.paho.client.mqttv3.MqttException r0 = org.eclipse.paho.client.mqttv3.internal.ExceptionHelper.createMqttException(r0)     // Catch:{ all -> 0x0183 }
            throw r0     // Catch:{ all -> 0x0183 }
        L_0x00e1:
            int r4 = r1.pingOutstanding     // Catch:{ all -> 0x0183 }
            if (r4 != 0) goto L_0x00f1
            long r4 = r1.lastInboundActivity     // Catch:{ all -> 0x0183 }
            long r5 = r13 - r4
            long r7 = r1.keepAlive     // Catch:{ all -> 0x0183 }
            long r9 = (long) r0     // Catch:{ all -> 0x0183 }
            long r7 = r7 - r9
            int r4 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r4 >= 0) goto L_0x00fd
        L_0x00f1:
            long r4 = r1.lastOutboundActivity     // Catch:{ all -> 0x0183 }
            long r5 = r13 - r4
            long r7 = r1.keepAlive     // Catch:{ all -> 0x0183 }
            long r9 = (long) r0     // Catch:{ all -> 0x0183 }
            long r7 = r7 - r9
            int r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r0 < 0) goto L_0x014c
        L_0x00fd:
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log     // Catch:{ all -> 0x0183 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ all -> 0x0183 }
            java.lang.String r5 = "checkForActivity"
            java.lang.String r6 = "620"
            r7 = 3
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0183 }
            java.lang.Long r8 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r9 = r1.keepAlive     // Catch:{ all -> 0x0183 }
            r8.<init>(r9)     // Catch:{ all -> 0x0183 }
            r7[r3] = r8     // Catch:{ all -> 0x0183 }
            java.lang.Long r8 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r9 = r1.lastOutboundActivity     // Catch:{ all -> 0x0183 }
            r8.<init>(r9)     // Catch:{ all -> 0x0183 }
            r7[r12] = r8     // Catch:{ all -> 0x0183 }
            java.lang.Long r8 = new java.lang.Long     // Catch:{ all -> 0x0183 }
            long r9 = r1.lastInboundActivity     // Catch:{ all -> 0x0183 }
            r8.<init>(r9)     // Catch:{ all -> 0x0183 }
            r9 = 2
            r7[r9] = r8     // Catch:{ all -> 0x0183 }
            r0.fine(r4, r5, r6, r7)     // Catch:{ all -> 0x0183 }
            org.eclipse.paho.client.mqttv3.MqttToken r0 = new org.eclipse.paho.client.mqttv3.MqttToken     // Catch:{ all -> 0x0183 }
            org.eclipse.paho.client.mqttv3.internal.ClientComms r4 = r1.clientComms     // Catch:{ all -> 0x0183 }
            org.eclipse.paho.client.mqttv3.IMqttAsyncClient r4 = r4.getClient()     // Catch:{ all -> 0x0183 }
            java.lang.String r4 = r4.getClientId()     // Catch:{ all -> 0x0183 }
            r0.<init>(r4)     // Catch:{ all -> 0x0183 }
            org.eclipse.paho.client.mqttv3.internal.CommsTokenStore r4 = r1.tokenStore     // Catch:{ all -> 0x0183 }
            org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage r5 = r1.pingCommand     // Catch:{ all -> 0x0183 }
            r4.saveToken(r0, r5)     // Catch:{ all -> 0x0183 }
            java.util.Vector r4 = r1.pendingFlows     // Catch:{ all -> 0x0183 }
            org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage r5 = r1.pingCommand     // Catch:{ all -> 0x0183 }
            r4.insertElementAt(r5, r3)     // Catch:{ all -> 0x0183 }
            long r4 = r17.getKeepAlive()     // Catch:{ all -> 0x0183 }
            r17.notifyQueueLock()     // Catch:{ all -> 0x0183 }
            goto L_0x0168
        L_0x014c:
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log     // Catch:{ all -> 0x0183 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ all -> 0x0183 }
            java.lang.String r5 = "checkForActivity"
            java.lang.String r6 = "634"
            r7 = 0
            r0.fine(r4, r5, r6, r7)     // Catch:{ all -> 0x0183 }
            r4 = 1
            long r8 = r17.getKeepAlive()     // Catch:{ all -> 0x0183 }
            long r10 = r1.lastOutboundActivity     // Catch:{ all -> 0x0183 }
            long r10 = r13 - r10
            long r8 = r8 - r10
            long r4 = java.lang.Math.max(r4, r8)     // Catch:{ all -> 0x0183 }
            r0 = r7
        L_0x0168:
            monitor-exit(r2)     // Catch:{ all -> 0x0183 }
            org.eclipse.paho.client.mqttv3.logging.Logger r2 = log
            java.lang.String r6 = CLASS_NAME
            java.lang.Object[] r7 = new java.lang.Object[r12]
            java.lang.Long r8 = new java.lang.Long
            r8.<init>(r4)
            r7[r3] = r8
            java.lang.String r3 = "checkForActivity"
            java.lang.String r8 = "624"
            r2.fine(r6, r3, r8, r7)
            org.eclipse.paho.client.mqttv3.MqttPingSender r2 = r1.pingSender
            r2.schedule(r4)
            goto L_0x0188
        L_0x0183:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0183 }
            throw r0
        L_0x0186:
            r7 = r4
            r0 = r7
        L_0x0188:
            return r0
        L_0x0189:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0189 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.ClientState.checkForActivity():org.eclipse.paho.client.mqttv3.MqttToken");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:7|(2:15|16)|17|18|(3:27|(4:29|(1:31)|32|45)(2:33|(1:46)(2:35|(2:37|47)(2:38|48)))|42)(3:24|25|26)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0042 */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x009d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage get() throws org.eclipse.paho.client.mqttv3.MqttException {
        /*
            r11 = this;
            java.lang.Object r0 = r11.queueLock
            monitor-enter(r0)
            r1 = 0
            r2 = r1
        L_0x0005:
            if (r2 == 0) goto L_0x0009
            monitor-exit(r0)     // Catch:{ all -> 0x00e2 }
            return r2
        L_0x0009:
            java.util.Vector r3 = r11.pendingMessages     // Catch:{ all -> 0x00e2 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x00e2 }
            if (r3 == 0) goto L_0x0019
            java.util.Vector r3 = r11.pendingFlows     // Catch:{ all -> 0x00e2 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x00e2 }
            if (r3 != 0) goto L_0x0027
        L_0x0019:
            java.util.Vector r3 = r11.pendingFlows     // Catch:{ all -> 0x00e2 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x00e2 }
            if (r3 == 0) goto L_0x0042
            int r3 = r11.actualInFlight     // Catch:{ all -> 0x00e2 }
            int r4 = r11.maxInflight     // Catch:{ all -> 0x00e2 }
            if (r3 < r4) goto L_0x0042
        L_0x0027:
            org.eclipse.paho.client.mqttv3.logging.Logger r3 = log     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.String r5 = "get"
            java.lang.String r6 = "644"
            r3.fine(r4, r5, r6)     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.Object r3 = r11.queueLock     // Catch:{ InterruptedException -> 0x0042 }
            r3.wait()     // Catch:{ InterruptedException -> 0x0042 }
            org.eclipse.paho.client.mqttv3.logging.Logger r3 = log     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ InterruptedException -> 0x0042 }
            java.lang.String r5 = "get"
            java.lang.String r6 = "647"
            r3.fine(r4, r5, r6)     // Catch:{ InterruptedException -> 0x0042 }
        L_0x0042:
            boolean r3 = r11.connected     // Catch:{ all -> 0x00e2 }
            r4 = 0
            if (r3 != 0) goto L_0x0068
            java.util.Vector r3 = r11.pendingFlows     // Catch:{ all -> 0x00e2 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x00e2 }
            if (r3 != 0) goto L_0x005b
            java.util.Vector r3 = r11.pendingFlows     // Catch:{ all -> 0x00e2 }
            java.lang.Object r3 = r3.elementAt(r4)     // Catch:{ all -> 0x00e2 }
            org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage r3 = (org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage) r3     // Catch:{ all -> 0x00e2 }
            boolean r3 = r3 instanceof org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect     // Catch:{ all -> 0x00e2 }
            if (r3 != 0) goto L_0x0068
        L_0x005b:
            org.eclipse.paho.client.mqttv3.logging.Logger r2 = log     // Catch:{ all -> 0x00e2 }
            java.lang.String r3 = CLASS_NAME     // Catch:{ all -> 0x00e2 }
            java.lang.String r4 = "get"
            java.lang.String r5 = "621"
            r2.fine(r3, r4, r5)     // Catch:{ all -> 0x00e2 }
            monitor-exit(r0)     // Catch:{ all -> 0x00e2 }
            return r1
        L_0x0068:
            java.util.Vector r3 = r11.pendingFlows     // Catch:{ all -> 0x00e2 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x00e2 }
            r5 = 1
            if (r3 != 0) goto L_0x009d
            java.util.Vector r2 = r11.pendingFlows     // Catch:{ all -> 0x00e2 }
            java.lang.Object r2 = r2.remove(r4)     // Catch:{ all -> 0x00e2 }
            org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage r2 = (org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage) r2     // Catch:{ all -> 0x00e2 }
            boolean r3 = r2 instanceof org.eclipse.paho.client.mqttv3.internal.wire.MqttPubRel     // Catch:{ all -> 0x00e2 }
            if (r3 == 0) goto L_0x0098
            int r3 = r11.inFlightPubRels     // Catch:{ all -> 0x00e2 }
            int r3 = r3 + r5
            r11.inFlightPubRels = r3     // Catch:{ all -> 0x00e2 }
            org.eclipse.paho.client.mqttv3.logging.Logger r3 = log     // Catch:{ all -> 0x00e2 }
            java.lang.String r6 = CLASS_NAME     // Catch:{ all -> 0x00e2 }
            java.lang.String r7 = "get"
            java.lang.String r8 = "617"
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x00e2 }
            java.lang.Integer r9 = new java.lang.Integer     // Catch:{ all -> 0x00e2 }
            int r10 = r11.inFlightPubRels     // Catch:{ all -> 0x00e2 }
            r9.<init>(r10)     // Catch:{ all -> 0x00e2 }
            r5[r4] = r9     // Catch:{ all -> 0x00e2 }
            r3.fine(r6, r7, r8, r5)     // Catch:{ all -> 0x00e2 }
        L_0x0098:
            r11.checkQuiesceLock()     // Catch:{ all -> 0x00e2 }
            goto L_0x0005
        L_0x009d:
            java.util.Vector r3 = r11.pendingMessages     // Catch:{ all -> 0x00e2 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x00e2 }
            if (r3 != 0) goto L_0x0005
            int r3 = r11.actualInFlight     // Catch:{ all -> 0x00e2 }
            int r6 = r11.maxInflight     // Catch:{ all -> 0x00e2 }
            if (r3 >= r6) goto L_0x00d5
            java.util.Vector r2 = r11.pendingMessages     // Catch:{ all -> 0x00e2 }
            java.lang.Object r2 = r2.elementAt(r4)     // Catch:{ all -> 0x00e2 }
            org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage r2 = (org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage) r2     // Catch:{ all -> 0x00e2 }
            java.util.Vector r3 = r11.pendingMessages     // Catch:{ all -> 0x00e2 }
            r3.removeElementAt(r4)     // Catch:{ all -> 0x00e2 }
            int r3 = r11.actualInFlight     // Catch:{ all -> 0x00e2 }
            int r3 = r3 + r5
            r11.actualInFlight = r3     // Catch:{ all -> 0x00e2 }
            org.eclipse.paho.client.mqttv3.logging.Logger r3 = log     // Catch:{ all -> 0x00e2 }
            java.lang.String r6 = CLASS_NAME     // Catch:{ all -> 0x00e2 }
            java.lang.String r7 = "get"
            java.lang.String r8 = "623"
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x00e2 }
            java.lang.Integer r9 = new java.lang.Integer     // Catch:{ all -> 0x00e2 }
            int r10 = r11.actualInFlight     // Catch:{ all -> 0x00e2 }
            r9.<init>(r10)     // Catch:{ all -> 0x00e2 }
            r5[r4] = r9     // Catch:{ all -> 0x00e2 }
            r3.fine(r6, r7, r8, r5)     // Catch:{ all -> 0x00e2 }
            goto L_0x0005
        L_0x00d5:
            org.eclipse.paho.client.mqttv3.logging.Logger r3 = log     // Catch:{ all -> 0x00e2 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ all -> 0x00e2 }
            java.lang.String r5 = "get"
            java.lang.String r6 = "622"
            r3.fine(r4, r5, r6)     // Catch:{ all -> 0x00e2 }
            goto L_0x0005
        L_0x00e2:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00e2 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.ClientState.get():org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage");
    }

    public void setKeepAliveInterval(long j) {
        this.keepAlive = j;
    }

    public void notifySentBytes(int i) {
        if (i > 0) {
            this.lastOutboundActivity = System.currentTimeMillis();
        }
        log.fine(CLASS_NAME, "notifySentBytes", "631", new Object[]{new Integer(i)});
    }

    /* access modifiers changed from: protected */
    public void notifySent(MqttWireMessage mqttWireMessage) {
        this.lastOutboundActivity = System.currentTimeMillis();
        log.fine(CLASS_NAME, "notifySent", "625", new Object[]{mqttWireMessage.getKey()});
        MqttToken token = this.tokenStore.getToken(mqttWireMessage);
        token.internalTok.notifySent();
        if (mqttWireMessage instanceof MqttPingReq) {
            synchronized (this.pingOutstandingLock) {
                long currentTimeMillis = System.currentTimeMillis();
                synchronized (this.pingOutstandingLock) {
                    this.lastPing = currentTimeMillis;
                    this.pingOutstanding++;
                }
                log.fine(CLASS_NAME, "notifySent", "635", new Object[]{new Integer(this.pingOutstanding)});
            }
        } else if ((mqttWireMessage instanceof MqttPublish) && ((MqttPublish) mqttWireMessage).getMessage().getQos() == 0) {
            token.internalTok.markComplete(null, null);
            this.callback.asyncOperationComplete(token);
            decrementInFlight();
            releaseMessageId(mqttWireMessage.getMessageId());
            this.tokenStore.removeToken(mqttWireMessage);
            checkQuiesceLock();
        }
    }

    private void decrementInFlight() {
        synchronized (this.queueLock) {
            this.actualInFlight--;
            log.fine(CLASS_NAME, "decrementInFlight", "646", new Object[]{new Integer(this.actualInFlight)});
            if (!checkQuiesceLock()) {
                this.queueLock.notifyAll();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkQuiesceLock() {
        int count = this.tokenStore.count();
        if (!this.quiescing || count != 0 || this.pendingFlows.size() != 0 || !this.callback.isQuiesced()) {
            return false;
        }
        log.fine(CLASS_NAME, "checkQuiesceLock", "626", new Object[]{new Boolean(this.quiescing), new Integer(this.actualInFlight), new Integer(this.pendingFlows.size()), new Integer(this.inFlightPubRels), Boolean.valueOf(this.callback.isQuiesced()), new Integer(count)});
        synchronized (this.quiesceLock) {
            this.quiesceLock.notifyAll();
        }
        return true;
    }

    public void notifyReceivedBytes(int i) {
        if (i > 0) {
            this.lastInboundActivity = System.currentTimeMillis();
        }
        log.fine(CLASS_NAME, "notifyReceivedBytes", "630", new Object[]{new Integer(i)});
    }

    /* access modifiers changed from: protected */
    public void notifyReceivedAck(MqttAck mqttAck) throws MqttException {
        this.lastInboundActivity = System.currentTimeMillis();
        log.fine(CLASS_NAME, "notifyReceivedAck", "627", new Object[]{new Integer(mqttAck.getMessageId()), mqttAck});
        MqttToken token = this.tokenStore.getToken((MqttWireMessage) mqttAck);
        if (mqttAck instanceof MqttPubRec) {
            send(new MqttPubRel((MqttPubRec) mqttAck), token);
        } else if ((mqttAck instanceof MqttPubAck) || (mqttAck instanceof MqttPubComp)) {
            notifyResult(mqttAck, token, null);
        } else if (mqttAck instanceof MqttPingResp) {
            synchronized (this.pingOutstandingLock) {
                this.pingOutstanding = Math.max(0, this.pingOutstanding - 1);
                notifyResult(mqttAck, token, null);
                if (this.pingOutstanding == 0) {
                    this.tokenStore.removeToken((MqttWireMessage) mqttAck);
                }
            }
            log.fine(CLASS_NAME, "notifyReceivedAck", "636", new Object[]{new Integer(this.pingOutstanding)});
        } else if (mqttAck instanceof MqttConnack) {
            MqttConnack mqttConnack = (MqttConnack) mqttAck;
            int returnCode = mqttConnack.getReturnCode();
            if (returnCode == 0) {
                synchronized (this.queueLock) {
                    if (this.cleanSession) {
                        clearState();
                        this.tokenStore.saveToken(token, (MqttWireMessage) mqttAck);
                    }
                    this.inFlightPubRels = 0;
                    this.actualInFlight = 0;
                    restoreInflightMessages();
                    connected();
                }
                this.clientComms.connectComplete(mqttConnack, null);
                notifyResult(mqttAck, token, null);
                this.tokenStore.removeToken((MqttWireMessage) mqttAck);
                synchronized (this.queueLock) {
                    this.queueLock.notifyAll();
                }
            } else {
                throw ExceptionHelper.createMqttException(returnCode);
            }
        } else {
            notifyResult(mqttAck, token, null);
            releaseMessageId(mqttAck.getMessageId());
            this.tokenStore.removeToken((MqttWireMessage) mqttAck);
        }
        checkQuiesceLock();
    }

    /* access modifiers changed from: protected */
    public void notifyReceivedMsg(MqttWireMessage mqttWireMessage) throws MqttException {
        this.lastInboundActivity = System.currentTimeMillis();
        log.fine(CLASS_NAME, "notifyReceivedMsg", "651", new Object[]{new Integer(mqttWireMessage.getMessageId()), mqttWireMessage});
        if (this.quiescing) {
            return;
        }
        if (mqttWireMessage instanceof MqttPublish) {
            MqttPublish mqttPublish = (MqttPublish) mqttWireMessage;
            int qos = mqttPublish.getMessage().getQos();
            if (qos == 0 || qos == 1) {
                CommsCallback commsCallback = this.callback;
                if (commsCallback != null) {
                    commsCallback.messageArrived(mqttPublish);
                }
            } else if (qos == 2) {
                this.persistence.put(getReceivedPersistenceKey(mqttWireMessage), mqttPublish);
                this.inboundQoS2.put(new Integer(mqttPublish.getMessageId()), mqttPublish);
                send(new MqttPubRec(mqttPublish), null);
            }
        } else if (mqttWireMessage instanceof MqttPubRel) {
            MqttPublish mqttPublish2 = (MqttPublish) this.inboundQoS2.get(new Integer(mqttWireMessage.getMessageId()));
            if (mqttPublish2 != null) {
                CommsCallback commsCallback2 = this.callback;
                if (commsCallback2 != null) {
                    commsCallback2.messageArrived(mqttPublish2);
                    return;
                }
                return;
            }
            send(new MqttPubComp(mqttWireMessage.getMessageId()), null);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyComplete(MqttToken mqttToken) throws MqttException {
        MqttWireMessage wireMessage = mqttToken.internalTok.getWireMessage();
        if (wireMessage != null && (wireMessage instanceof MqttAck)) {
            String str = "notifyComplete";
            log.fine(CLASS_NAME, str, "629", new Object[]{new Integer(wireMessage.getMessageId()), mqttToken, wireMessage});
            MqttAck mqttAck = (MqttAck) wireMessage;
            if (mqttAck instanceof MqttPubAck) {
                this.persistence.remove(getSendPersistenceKey(wireMessage));
                this.outboundQoS1.remove(new Integer(mqttAck.getMessageId()));
                decrementInFlight();
                releaseMessageId(wireMessage.getMessageId());
                this.tokenStore.removeToken(wireMessage);
                log.fine(CLASS_NAME, str, "650", new Object[]{new Integer(mqttAck.getMessageId())});
            } else if (mqttAck instanceof MqttPubComp) {
                this.persistence.remove(getSendPersistenceKey(wireMessage));
                this.persistence.remove(getSendConfirmPersistenceKey(wireMessage));
                this.outboundQoS2.remove(new Integer(mqttAck.getMessageId()));
                this.inFlightPubRels--;
                decrementInFlight();
                releaseMessageId(wireMessage.getMessageId());
                this.tokenStore.removeToken(wireMessage);
                log.fine(CLASS_NAME, str, "645", new Object[]{new Integer(mqttAck.getMessageId()), new Integer(this.inFlightPubRels)});
            }
            checkQuiesceLock();
        }
    }

    /* access modifiers changed from: protected */
    public void notifyResult(MqttWireMessage mqttWireMessage, MqttToken mqttToken, MqttException mqttException) {
        mqttToken.internalTok.markComplete(mqttWireMessage, mqttException);
        String str = "notifyResult";
        if (mqttWireMessage != null && (mqttWireMessage instanceof MqttAck) && !(mqttWireMessage instanceof MqttPubRec)) {
            log.fine(CLASS_NAME, str, "648", new Object[]{mqttToken.internalTok.getKey(), mqttWireMessage, mqttException});
            this.callback.asyncOperationComplete(mqttToken);
        }
        if (mqttWireMessage == null) {
            log.fine(CLASS_NAME, str, "649", new Object[]{mqttToken.internalTok.getKey(), mqttException});
            this.callback.asyncOperationComplete(mqttToken);
        }
    }

    public void connected() {
        log.fine(CLASS_NAME, "connected", "631");
        this.connected = true;
        this.pingSender.start();
    }

    public Vector resolveOldTokens(MqttException mqttException) {
        log.fine(CLASS_NAME, "resolveOldTokens", "632", new Object[]{mqttException});
        if (mqttException == null) {
            mqttException = new MqttException(32102);
        }
        Vector outstandingTokens = this.tokenStore.getOutstandingTokens();
        Enumeration elements = outstandingTokens.elements();
        while (elements.hasMoreElements()) {
            MqttToken mqttToken = (MqttToken) elements.nextElement();
            synchronized (mqttToken) {
                if (!mqttToken.isComplete() && !mqttToken.internalTok.isCompletePending() && mqttToken.getException() == null) {
                    mqttToken.internalTok.setException(mqttException);
                }
            }
            if (!(mqttToken instanceof MqttDeliveryToken)) {
                this.tokenStore.removeToken(mqttToken.internalTok.getKey());
            }
        }
        return outstandingTokens;
    }

    public void disconnected(MqttException mqttException) {
        log.fine(CLASS_NAME, "disconnected", "633", new Object[]{mqttException});
        this.connected = false;
        try {
            if (this.cleanSession) {
                clearState();
            }
            this.pendingMessages.clear();
            this.pendingFlows.clear();
            synchronized (this.pingOutstandingLock) {
                this.pingOutstanding = 0;
            }
        } catch (MqttException unused) {
        }
    }

    private synchronized void releaseMessageId(int i) {
        this.inUseMsgIds.remove(new Integer(i));
    }

    private synchronized int getNextMessageId() throws MqttException {
        int i = this.nextMsgId;
        int i2 = 0;
        do {
            this.nextMsgId++;
            if (this.nextMsgId > 65535) {
                this.nextMsgId = 1;
            }
            if (this.nextMsgId == i) {
                i2++;
                if (i2 == 2) {
                    throw ExceptionHelper.createMqttException(32001);
                }
            }
        } while (this.inUseMsgIds.containsKey(new Integer(this.nextMsgId)));
        Integer num = new Integer(this.nextMsgId);
        this.inUseMsgIds.put(num, num);
        return this.nextMsgId;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x0083 */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0087 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void quiesce(long r12) {
        /*
            r11 = this;
            r0 = 0
            int r2 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r2 <= 0) goto L_0x00aa
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log
            java.lang.String r1 = CLASS_NAME
            r2 = 1
            java.lang.Object[] r3 = new java.lang.Object[r2]
            java.lang.Long r4 = new java.lang.Long
            r4.<init>(r12)
            r5 = 0
            r3[r5] = r4
            java.lang.String r4 = "quiesce"
            java.lang.String r6 = "637"
            r0.fine(r1, r4, r6, r3)
            java.lang.Object r0 = r11.queueLock
            monitor-enter(r0)
            r11.quiescing = r2     // Catch:{ all -> 0x00a7 }
            monitor-exit(r0)     // Catch:{ all -> 0x00a7 }
            org.eclipse.paho.client.mqttv3.internal.CommsCallback r0 = r11.callback
            r0.quiesce()
            r11.notifyQueueLock()
            java.lang.Object r1 = r11.quiesceLock
            monitor-enter(r1)
            org.eclipse.paho.client.mqttv3.internal.CommsTokenStore r0 = r11.tokenStore     // Catch:{ InterruptedException -> 0x0083 }
            int r0 = r0.count()     // Catch:{ InterruptedException -> 0x0083 }
            if (r0 > 0) goto L_0x0045
            java.util.Vector r3 = r11.pendingFlows     // Catch:{ InterruptedException -> 0x0083 }
            int r3 = r3.size()     // Catch:{ InterruptedException -> 0x0083 }
            if (r3 > 0) goto L_0x0045
            org.eclipse.paho.client.mqttv3.internal.CommsCallback r3 = r11.callback     // Catch:{ InterruptedException -> 0x0083 }
            boolean r3 = r3.isQuiesced()     // Catch:{ InterruptedException -> 0x0083 }
            if (r3 != 0) goto L_0x0083
        L_0x0045:
            org.eclipse.paho.client.mqttv3.logging.Logger r3 = log     // Catch:{ InterruptedException -> 0x0083 }
            java.lang.String r4 = CLASS_NAME     // Catch:{ InterruptedException -> 0x0083 }
            java.lang.String r6 = "quiesce"
            java.lang.String r7 = "639"
            r8 = 4
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ InterruptedException -> 0x0083 }
            java.lang.Integer r9 = new java.lang.Integer     // Catch:{ InterruptedException -> 0x0083 }
            int r10 = r11.actualInFlight     // Catch:{ InterruptedException -> 0x0083 }
            r9.<init>(r10)     // Catch:{ InterruptedException -> 0x0083 }
            r8[r5] = r9     // Catch:{ InterruptedException -> 0x0083 }
            java.lang.Integer r9 = new java.lang.Integer     // Catch:{ InterruptedException -> 0x0083 }
            java.util.Vector r10 = r11.pendingFlows     // Catch:{ InterruptedException -> 0x0083 }
            int r10 = r10.size()     // Catch:{ InterruptedException -> 0x0083 }
            r9.<init>(r10)     // Catch:{ InterruptedException -> 0x0083 }
            r8[r2] = r9     // Catch:{ InterruptedException -> 0x0083 }
            r2 = 2
            java.lang.Integer r9 = new java.lang.Integer     // Catch:{ InterruptedException -> 0x0083 }
            int r10 = r11.inFlightPubRels     // Catch:{ InterruptedException -> 0x0083 }
            r9.<init>(r10)     // Catch:{ InterruptedException -> 0x0083 }
            r8[r2] = r9     // Catch:{ InterruptedException -> 0x0083 }
            r2 = 3
            java.lang.Integer r9 = new java.lang.Integer     // Catch:{ InterruptedException -> 0x0083 }
            r9.<init>(r0)     // Catch:{ InterruptedException -> 0x0083 }
            r8[r2] = r9     // Catch:{ InterruptedException -> 0x0083 }
            r3.fine(r4, r6, r7, r8)     // Catch:{ InterruptedException -> 0x0083 }
            java.lang.Object r0 = r11.quiesceLock     // Catch:{ InterruptedException -> 0x0083 }
            r0.wait(r12)     // Catch:{ InterruptedException -> 0x0083 }
            goto L_0x0083
        L_0x0081:
            r12 = move-exception
            goto L_0x00a5
        L_0x0083:
            monitor-exit(r1)     // Catch:{ all -> 0x0081 }
            java.lang.Object r12 = r11.queueLock
            monitor-enter(r12)
            java.util.Vector r13 = r11.pendingMessages     // Catch:{ all -> 0x00a2 }
            r13.clear()     // Catch:{ all -> 0x00a2 }
            java.util.Vector r13 = r11.pendingFlows     // Catch:{ all -> 0x00a2 }
            r13.clear()     // Catch:{ all -> 0x00a2 }
            r11.quiescing = r5     // Catch:{ all -> 0x00a2 }
            r11.actualInFlight = r5     // Catch:{ all -> 0x00a2 }
            monitor-exit(r12)     // Catch:{ all -> 0x00a2 }
            org.eclipse.paho.client.mqttv3.logging.Logger r12 = log
            java.lang.String r13 = CLASS_NAME
            java.lang.String r0 = "quiesce"
            java.lang.String r1 = "640"
            r12.fine(r13, r0, r1)
            goto L_0x00aa
        L_0x00a2:
            r13 = move-exception
            monitor-exit(r12)     // Catch:{ all -> 0x00a2 }
            throw r13
        L_0x00a5:
            monitor-exit(r1)     // Catch:{ all -> 0x0081 }
            throw r12
        L_0x00a7:
            r12 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00a7 }
            throw r12
        L_0x00aa:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.ClientState.quiesce(long):void");
    }

    public void notifyQueueLock() {
        synchronized (this.queueLock) {
            log.fine(CLASS_NAME, "notifyQueueLock", "638");
            this.queueLock.notifyAll();
        }
    }

    /* access modifiers changed from: protected */
    public void deliveryComplete(MqttPublish mqttPublish) throws MqttPersistenceException {
        log.fine(CLASS_NAME, "deliveryComplete", "641", new Object[]{new Integer(mqttPublish.getMessageId())});
        this.persistence.remove(getReceivedPersistenceKey(mqttPublish));
        this.inboundQoS2.remove(new Integer(mqttPublish.getMessageId()));
    }

    /* access modifiers changed from: protected */
    public void close() {
        this.inUseMsgIds.clear();
        this.pendingMessages.clear();
        this.pendingFlows.clear();
        this.outboundQoS2.clear();
        this.outboundQoS1.clear();
        this.inboundQoS2.clear();
        this.tokenStore.clear();
        this.inUseMsgIds = null;
        this.pendingMessages = null;
        this.pendingFlows = null;
        this.outboundQoS2 = null;
        this.outboundQoS1 = null;
        this.inboundQoS2 = null;
        this.tokenStore = null;
        this.callback = null;
        this.clientComms = null;
        this.persistence = null;
        this.pingCommand = null;
    }

    public Properties getDebug() {
        Properties properties = new Properties();
        properties.put("In use msgids", this.inUseMsgIds);
        properties.put("pendingMessages", this.pendingMessages);
        properties.put("pendingFlows", this.pendingFlows);
        properties.put("maxInflight", new Integer(this.maxInflight));
        properties.put("nextMsgID", new Integer(this.nextMsgId));
        properties.put("actualInFlight", new Integer(this.actualInFlight));
        properties.put("inFlightPubRels", new Integer(this.inFlightPubRels));
        properties.put("quiescing", Boolean.valueOf(this.quiescing));
        properties.put("pingoutstanding", new Integer(this.pingOutstanding));
        properties.put("lastOutboundActivity", new Long(this.lastOutboundActivity));
        properties.put("lastInboundActivity", new Long(this.lastInboundActivity));
        properties.put("outboundQoS2", this.outboundQoS2);
        properties.put("outboundQoS1", this.outboundQoS1);
        properties.put("inboundQoS2", this.inboundQoS2);
        properties.put("tokens", this.tokenStore);
        return properties;
    }
}
