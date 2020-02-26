package org.eclipse.paho.client.mqttv3.internal;

import java.util.Enumeration;
import java.util.Properties;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnack;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttDisconnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class ClientComms {
    public static String BUILD_LEVEL = "L${build.level}";
    /* access modifiers changed from: private */
    public static final String CLASS_NAME;
    private static final byte CLOSED = 4;
    private static final byte CONNECTED = 0;
    private static final byte CONNECTING = 1;
    private static final byte DISCONNECTED = 3;
    private static final byte DISCONNECTING = 2;
    public static String VERSION = "${project.version}";
    static /* synthetic */ Class class$0;
    /* access modifiers changed from: private */
    public static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    /* access modifiers changed from: private */
    public CommsCallback callback;
    private IMqttAsyncClient client;
    /* access modifiers changed from: private */
    public ClientState clientState;
    private boolean closePending;
    private Object conLock;
    private MqttConnectOptions conOptions;
    private byte conState;
    /* access modifiers changed from: private */
    public int networkModuleIndex;
    /* access modifiers changed from: private */
    public NetworkModule[] networkModules;
    private MqttClientPersistence persistence;
    private MqttPingSender pingSender;
    /* access modifiers changed from: private */
    public CommsReceiver receiver;
    /* access modifiers changed from: private */
    public CommsSender sender;
    private boolean stoppingComms;
    /* access modifiers changed from: private */
    public CommsTokenStore tokenStore;

    private class ConnectBG implements Runnable {
        Thread cBg = null;
        ClientComms clientComms = null;
        MqttConnect conPacket;
        MqttToken conToken;

        ConnectBG(ClientComms clientComms2, MqttToken mqttToken, MqttConnect mqttConnect) {
            this.clientComms = clientComms2;
            this.conToken = mqttToken;
            this.conPacket = mqttConnect;
            StringBuffer stringBuffer = new StringBuffer("MQTT Con: ");
            stringBuffer.append(ClientComms.this.getClient().getClientId());
            this.cBg = new Thread(this, stringBuffer.toString());
        }

        /* access modifiers changed from: 0000 */
        public void start() {
            this.cBg.start();
        }

        public void run() {
            ClientComms.log.fine(ClientComms.CLASS_NAME, "connectBG:run", "220");
            MqttException e = null;
            try {
                MqttDeliveryToken[] outstandingDelTokens = ClientComms.this.tokenStore.getOutstandingDelTokens();
                for (MqttDeliveryToken mqttDeliveryToken : outstandingDelTokens) {
                    mqttDeliveryToken.internalTok.setException(null);
                }
                ClientComms.this.tokenStore.saveToken(this.conToken, (MqttWireMessage) this.conPacket);
                NetworkModule networkModule = ClientComms.this.networkModules[ClientComms.this.networkModuleIndex];
                networkModule.start();
                ClientComms.this.receiver = new CommsReceiver(this.clientComms, ClientComms.this.clientState, ClientComms.this.tokenStore, networkModule.getInputStream());
                CommsReceiver access$7 = ClientComms.this.receiver;
                StringBuffer stringBuffer = new StringBuffer("MQTT Rec: ");
                stringBuffer.append(ClientComms.this.getClient().getClientId());
                access$7.start(stringBuffer.toString());
                ClientComms.this.sender = new CommsSender(this.clientComms, ClientComms.this.clientState, ClientComms.this.tokenStore, networkModule.getOutputStream());
                CommsSender access$9 = ClientComms.this.sender;
                StringBuffer stringBuffer2 = new StringBuffer("MQTT Snd: ");
                stringBuffer2.append(ClientComms.this.getClient().getClientId());
                access$9.start(stringBuffer2.toString());
                CommsCallback access$10 = ClientComms.this.callback;
                StringBuffer stringBuffer3 = new StringBuffer("MQTT Call: ");
                stringBuffer3.append(ClientComms.this.getClient().getClientId());
                access$10.start(stringBuffer3.toString());
                ClientComms.this.internalSend(this.conPacket, this.conToken);
            } catch (MqttException e2) {
                e = e2;
                ClientComms.log.fine(ClientComms.CLASS_NAME, "connectBG:run", "212", null, e);
            } catch (Exception e3) {
                ClientComms.log.fine(ClientComms.CLASS_NAME, "connectBG:run", "209", null, e3);
                e = ExceptionHelper.createMqttException((Throwable) e3);
            }
            if (e != null) {
                ClientComms.this.shutdownConnection(this.conToken, e);
            }
        }
    }

    private class DisconnectBG implements Runnable {
        Thread dBg = null;
        MqttDisconnect disconnect;
        long quiesceTimeout;
        MqttToken token;

        DisconnectBG(MqttDisconnect mqttDisconnect, long j, MqttToken mqttToken) {
            this.disconnect = mqttDisconnect;
            this.quiesceTimeout = j;
            this.token = mqttToken;
        }

        /* access modifiers changed from: 0000 */
        public void start() {
            StringBuffer stringBuffer = new StringBuffer("MQTT Disc: ");
            stringBuffer.append(ClientComms.this.getClient().getClientId());
            this.dBg = new Thread(this, stringBuffer.toString());
            this.dBg.start();
        }

        public void run() {
            ClientComms.log.fine(ClientComms.CLASS_NAME, "disconnectBG:run", "221");
            ClientComms.this.clientState.quiesce(this.quiesceTimeout);
            try {
                ClientComms.this.internalSend(this.disconnect, this.token);
                this.token.internalTok.waitUntilSent();
            } catch (MqttException unused) {
            } catch (Throwable th) {
                this.token.internalTok.markComplete(null, null);
                ClientComms.this.shutdownConnection(this.token, null);
                throw th;
            }
            this.token.internalTok.markComplete(null, null);
            ClientComms.this.shutdownConnection(this.token, null);
        }
    }

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.ClientComms");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
    }

    public ClientComms(IMqttAsyncClient iMqttAsyncClient, MqttClientPersistence mqttClientPersistence, MqttPingSender mqttPingSender) throws MqttException {
        this.stoppingComms = false;
        this.conState = 3;
        this.conLock = new Object();
        this.closePending = false;
        this.conState = 3;
        this.client = iMqttAsyncClient;
        this.persistence = mqttClientPersistence;
        this.pingSender = mqttPingSender;
        this.pingSender.init(this);
        this.tokenStore = new CommsTokenStore(getClient().getClientId());
        this.callback = new CommsCallback(this);
        ClientState clientState2 = new ClientState(mqttClientPersistence, this.tokenStore, this.callback, this, mqttPingSender);
        this.clientState = clientState2;
        this.callback.setClientState(this.clientState);
        log.setResourceName(getClient().getClientId());
    }

    /* access modifiers changed from: 0000 */
    public CommsReceiver getReceiver() {
        return this.receiver;
    }

    /* access modifiers changed from: 0000 */
    public void internalSend(MqttWireMessage mqttWireMessage, MqttToken mqttToken) throws MqttException {
        String str = "internalSend";
        log.fine(CLASS_NAME, str, "200", new Object[]{mqttWireMessage.getKey(), mqttWireMessage, mqttToken});
        if (mqttToken.getClient() == null) {
            mqttToken.internalTok.setClient(getClient());
            try {
                this.clientState.send(mqttWireMessage, mqttToken);
            } catch (MqttException e) {
                if (mqttWireMessage instanceof MqttPublish) {
                    this.clientState.undo((MqttPublish) mqttWireMessage);
                }
                throw e;
            }
        } else {
            log.fine(CLASS_NAME, str, "213", new Object[]{mqttWireMessage.getKey(), mqttWireMessage, mqttToken});
            throw new MqttException(32201);
        }
    }

    public void sendNoWait(MqttWireMessage mqttWireMessage, MqttToken mqttToken) throws MqttException {
        if (isConnected() || ((!isConnected() && (mqttWireMessage instanceof MqttConnect)) || (isDisconnecting() && (mqttWireMessage instanceof MqttDisconnect)))) {
            internalSend(mqttWireMessage, mqttToken);
        } else {
            log.fine(CLASS_NAME, "sendNoWait", "208");
            throw ExceptionHelper.createMqttException(32104);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws org.eclipse.paho.client.mqttv3.MqttException {
        /*
            r5 = this;
            java.lang.Object r0 = r5.conLock
            monitor-enter(r0)
            boolean r1 = r5.isClosed()     // Catch:{ all -> 0x005d }
            if (r1 != 0) goto L_0x005b
            boolean r1 = r5.isDisconnected()     // Catch:{ all -> 0x005d }
            if (r1 != 0) goto L_0x0040
            org.eclipse.paho.client.mqttv3.logging.Logger r1 = log     // Catch:{ all -> 0x005d }
            java.lang.String r2 = CLASS_NAME     // Catch:{ all -> 0x005d }
            java.lang.String r3 = "close"
            java.lang.String r4 = "224"
            r1.fine(r2, r3, r4)     // Catch:{ all -> 0x005d }
            boolean r1 = r5.isConnecting()     // Catch:{ all -> 0x005d }
            if (r1 != 0) goto L_0x0038
            boolean r1 = r5.isConnected()     // Catch:{ all -> 0x005d }
            if (r1 != 0) goto L_0x0031
            boolean r1 = r5.isDisconnecting()     // Catch:{ all -> 0x005d }
            if (r1 == 0) goto L_0x0040
            r1 = 1
            r5.closePending = r1     // Catch:{ all -> 0x005d }
            monitor-exit(r0)     // Catch:{ all -> 0x005d }
            return
        L_0x0031:
            r1 = 32100(0x7d64, float:4.4982E-41)
            org.eclipse.paho.client.mqttv3.MqttException r1 = org.eclipse.paho.client.mqttv3.internal.ExceptionHelper.createMqttException(r1)     // Catch:{ all -> 0x005d }
            throw r1     // Catch:{ all -> 0x005d }
        L_0x0038:
            org.eclipse.paho.client.mqttv3.MqttException r1 = new org.eclipse.paho.client.mqttv3.MqttException     // Catch:{ all -> 0x005d }
            r2 = 32110(0x7d6e, float:4.4996E-41)
            r1.<init>(r2)     // Catch:{ all -> 0x005d }
            throw r1     // Catch:{ all -> 0x005d }
        L_0x0040:
            r1 = 4
            r5.conState = r1     // Catch:{ all -> 0x005d }
            org.eclipse.paho.client.mqttv3.internal.ClientState r1 = r5.clientState     // Catch:{ all -> 0x005d }
            r1.close()     // Catch:{ all -> 0x005d }
            r1 = 0
            r5.clientState = r1     // Catch:{ all -> 0x005d }
            r5.callback = r1     // Catch:{ all -> 0x005d }
            r5.persistence = r1     // Catch:{ all -> 0x005d }
            r5.sender = r1     // Catch:{ all -> 0x005d }
            r5.pingSender = r1     // Catch:{ all -> 0x005d }
            r5.receiver = r1     // Catch:{ all -> 0x005d }
            r5.networkModules = r1     // Catch:{ all -> 0x005d }
            r5.conOptions = r1     // Catch:{ all -> 0x005d }
            r5.tokenStore = r1     // Catch:{ all -> 0x005d }
        L_0x005b:
            monitor-exit(r0)     // Catch:{ all -> 0x005d }
            return
        L_0x005d:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x005d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.ClientComms.close():void");
    }

    public void connect(MqttConnectOptions mqttConnectOptions, MqttToken mqttToken) throws MqttException {
        synchronized (this.conLock) {
            if (!isDisconnected() || this.closePending) {
                log.fine(CLASS_NAME, MqttServiceConstants.CONNECT_ACTION, "207", new Object[]{new Byte(this.conState)});
                if (isClosed() || this.closePending) {
                    throw new MqttException(32111);
                } else if (isConnecting()) {
                    throw new MqttException(32110);
                } else if (isDisconnecting()) {
                    throw new MqttException(32102);
                } else {
                    throw ExceptionHelper.createMqttException(32100);
                }
            } else {
                log.fine(CLASS_NAME, MqttServiceConstants.CONNECT_ACTION, "214");
                this.conState = 1;
                this.conOptions = mqttConnectOptions;
                MqttConnect mqttConnect = new MqttConnect(this.client.getClientId(), mqttConnectOptions.getMqttVersion(), mqttConnectOptions.isCleanSession(), mqttConnectOptions.getKeepAliveInterval(), mqttConnectOptions.getUserName(), mqttConnectOptions.getPassword(), mqttConnectOptions.getWillMessage(), mqttConnectOptions.getWillDestination());
                this.clientState.setKeepAliveSecs((long) mqttConnectOptions.getKeepAliveInterval());
                this.clientState.setCleanSession(mqttConnectOptions.isCleanSession());
                this.tokenStore.open();
                new ConnectBG(this, mqttToken, mqttConnect).start();
            }
        }
    }

    public void connectComplete(MqttConnack mqttConnack, MqttException mqttException) throws MqttException {
        int returnCode = mqttConnack.getReturnCode();
        synchronized (this.conLock) {
            if (returnCode == 0) {
                log.fine(CLASS_NAME, "connectComplete", "215");
                this.conState = 0;
                return;
            }
            log.fine(CLASS_NAME, "connectComplete", "204", new Object[]{new Integer(returnCode)});
            throw mqttException;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:68|69|(2:71|72)|73|74) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        if (r9 == null) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0035, code lost:
        if (r9.isComplete() != false) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0037, code lost:
        r9.internalTok.setException(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        r0 = r8.callback;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r0 == null) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        r0.stop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0045, code lost:
        if (r8.networkModules == null) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0047, code lost:
        r0 = r8.networkModules[r8.networkModuleIndex];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004d, code lost:
        if (r0 == null) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004f, code lost:
        r0.stop();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:73:0x00c3 */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0084 A[Catch:{ Exception -> 0x0089 }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x008c A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00bc A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void shutdownConnection(org.eclipse.paho.client.mqttv3.MqttToken r9, org.eclipse.paho.client.mqttv3.MqttException r10) {
        /*
            r8 = this;
            java.lang.Object r0 = r8.conLock
            monitor-enter(r0)
            boolean r1 = r8.stoppingComms     // Catch:{ all -> 0x00cd }
            if (r1 != 0) goto L_0x00cb
            boolean r1 = r8.closePending     // Catch:{ all -> 0x00cd }
            if (r1 == 0) goto L_0x000d
            goto L_0x00cb
        L_0x000d:
            r1 = 1
            r8.stoppingComms = r1     // Catch:{ all -> 0x00cd }
            org.eclipse.paho.client.mqttv3.logging.Logger r2 = log     // Catch:{ all -> 0x00cd }
            java.lang.String r3 = CLASS_NAME     // Catch:{ all -> 0x00cd }
            java.lang.String r4 = "shutdownConnection"
            java.lang.String r5 = "216"
            r2.fine(r3, r4, r5)     // Catch:{ all -> 0x00cd }
            boolean r2 = r8.isConnected()     // Catch:{ all -> 0x00cd }
            r3 = 0
            if (r2 != 0) goto L_0x002a
            boolean r2 = r8.isDisconnecting()     // Catch:{ all -> 0x00cd }
            if (r2 != 0) goto L_0x002a
            r2 = 0
            goto L_0x002b
        L_0x002a:
            r2 = 1
        L_0x002b:
            r4 = 2
            r8.conState = r4     // Catch:{ all -> 0x00cd }
            monitor-exit(r0)     // Catch:{ all -> 0x00cd }
            if (r9 == 0) goto L_0x003c
            boolean r0 = r9.isComplete()
            if (r0 != 0) goto L_0x003c
            org.eclipse.paho.client.mqttv3.internal.Token r0 = r9.internalTok
            r0.setException(r10)
        L_0x003c:
            org.eclipse.paho.client.mqttv3.internal.CommsCallback r0 = r8.callback
            if (r0 == 0) goto L_0x0043
            r0.stop()
        L_0x0043:
            org.eclipse.paho.client.mqttv3.internal.NetworkModule[] r0 = r8.networkModules     // Catch:{ Exception -> 0x0053 }
            if (r0 == 0) goto L_0x0054
            org.eclipse.paho.client.mqttv3.internal.NetworkModule[] r0 = r8.networkModules     // Catch:{ Exception -> 0x0053 }
            int r4 = r8.networkModuleIndex     // Catch:{ Exception -> 0x0053 }
            r0 = r0[r4]     // Catch:{ Exception -> 0x0053 }
            if (r0 == 0) goto L_0x0054
            r0.stop()     // Catch:{ Exception -> 0x0053 }
            goto L_0x0054
        L_0x0053:
        L_0x0054:
            org.eclipse.paho.client.mqttv3.internal.CommsReceiver r0 = r8.receiver
            if (r0 == 0) goto L_0x005b
            r0.stop()
        L_0x005b:
            org.eclipse.paho.client.mqttv3.internal.CommsTokenStore r0 = r8.tokenStore
            org.eclipse.paho.client.mqttv3.MqttException r4 = new org.eclipse.paho.client.mqttv3.MqttException
            r5 = 32102(0x7d66, float:4.4984E-41)
            r4.<init>(r5)
            r0.quiesce(r4)
            org.eclipse.paho.client.mqttv3.MqttToken r9 = r8.handleOldTokens(r9, r10)
            org.eclipse.paho.client.mqttv3.internal.ClientState r0 = r8.clientState     // Catch:{ Exception -> 0x0071 }
            r0.disconnected(r10)     // Catch:{ Exception -> 0x0071 }
            goto L_0x0072
        L_0x0071:
        L_0x0072:
            org.eclipse.paho.client.mqttv3.internal.CommsSender r0 = r8.sender
            if (r0 == 0) goto L_0x0079
            r0.stop()
        L_0x0079:
            org.eclipse.paho.client.mqttv3.MqttPingSender r0 = r8.pingSender
            if (r0 == 0) goto L_0x0080
            r0.stop()
        L_0x0080:
            org.eclipse.paho.client.mqttv3.MqttClientPersistence r0 = r8.persistence     // Catch:{ Exception -> 0x0089 }
            if (r0 == 0) goto L_0x0089
            org.eclipse.paho.client.mqttv3.MqttClientPersistence r0 = r8.persistence     // Catch:{ Exception -> 0x0089 }
            r0.close()     // Catch:{ Exception -> 0x0089 }
        L_0x0089:
            java.lang.Object r4 = r8.conLock
            monitor-enter(r4)
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = log     // Catch:{ all -> 0x00c8 }
            java.lang.String r5 = CLASS_NAME     // Catch:{ all -> 0x00c8 }
            java.lang.String r6 = "shutdownConnection"
            java.lang.String r7 = "217"
            r0.fine(r5, r6, r7)     // Catch:{ all -> 0x00c8 }
            r0 = 3
            r8.conState = r0     // Catch:{ all -> 0x00c8 }
            r8.stoppingComms = r3     // Catch:{ all -> 0x00c8 }
            monitor-exit(r4)     // Catch:{ all -> 0x00c8 }
            if (r9 == 0) goto L_0x00a1
            r0 = 1
            goto L_0x00a2
        L_0x00a1:
            r0 = 0
        L_0x00a2:
            org.eclipse.paho.client.mqttv3.internal.CommsCallback r4 = r8.callback
            if (r4 == 0) goto L_0x00a7
            goto L_0x00a8
        L_0x00a7:
            r1 = 0
        L_0x00a8:
            r0 = r0 & r1
            if (r0 == 0) goto L_0x00b0
            org.eclipse.paho.client.mqttv3.internal.CommsCallback r0 = r8.callback
            r0.asyncOperationComplete(r9)
        L_0x00b0:
            if (r2 == 0) goto L_0x00b9
            org.eclipse.paho.client.mqttv3.internal.CommsCallback r9 = r8.callback
            if (r9 == 0) goto L_0x00b9
            r9.connectionLost(r10)
        L_0x00b9:
            java.lang.Object r9 = r8.conLock
            monitor-enter(r9)
            boolean r10 = r8.closePending     // Catch:{ all -> 0x00c5 }
            if (r10 == 0) goto L_0x00c3
            r8.close()     // Catch:{ Exception -> 0x00c3 }
        L_0x00c3:
            monitor-exit(r9)     // Catch:{ all -> 0x00c5 }
            return
        L_0x00c5:
            r10 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x00c5 }
            throw r10
        L_0x00c8:
            r9 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x00c8 }
            throw r9
        L_0x00cb:
            monitor-exit(r0)     // Catch:{ all -> 0x00cd }
            return
        L_0x00cd:
            r9 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00cd }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.ClientComms.shutdownConnection(org.eclipse.paho.client.mqttv3.MqttToken, org.eclipse.paho.client.mqttv3.MqttException):void");
    }

    private MqttToken handleOldTokens(MqttToken mqttToken, MqttException mqttException) {
        log.fine(CLASS_NAME, "handleOldTokens", "222");
        MqttToken mqttToken2 = null;
        if (mqttToken != null) {
            try {
                if (this.tokenStore.getToken(mqttToken.internalTok.getKey()) == null) {
                    this.tokenStore.saveToken(mqttToken, mqttToken.internalTok.getKey());
                }
            } catch (Exception unused) {
            }
        }
        Enumeration elements = this.clientState.resolveOldTokens(mqttException).elements();
        while (elements.hasMoreElements()) {
            MqttToken mqttToken3 = (MqttToken) elements.nextElement();
            if (!mqttToken3.internalTok.getKey().equals(MqttDisconnect.KEY)) {
                if (!mqttToken3.internalTok.getKey().equals("Con")) {
                    this.callback.asyncOperationComplete(mqttToken3);
                }
            }
            mqttToken2 = mqttToken3;
        }
        return mqttToken2;
    }

    public void disconnect(MqttDisconnect mqttDisconnect, long j, MqttToken mqttToken) throws MqttException {
        synchronized (this.conLock) {
            if (isClosed()) {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "223");
                throw ExceptionHelper.createMqttException(32111);
            } else if (isDisconnected()) {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "211");
                throw ExceptionHelper.createMqttException(32101);
            } else if (isDisconnecting()) {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "219");
                throw ExceptionHelper.createMqttException(32102);
            } else if (Thread.currentThread() != this.callback.getThread()) {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "218");
                this.conState = 2;
                DisconnectBG disconnectBG = new DisconnectBG(mqttDisconnect, j, mqttToken);
                disconnectBG.start();
            } else {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "210");
                throw ExceptionHelper.createMqttException(32107);
            }
        }
    }

    public void disconnectForcibly(long j, long j2) throws MqttException {
        this.clientState.quiesce(j);
        MqttToken mqttToken = new MqttToken(this.client.getClientId());
        try {
            internalSend(new MqttDisconnect(), mqttToken);
            mqttToken.waitForCompletion(j2);
        } catch (Exception unused) {
        } catch (Throwable th) {
            mqttToken.internalTok.markComplete(null, null);
            shutdownConnection(mqttToken, null);
            throw th;
        }
        mqttToken.internalTok.markComplete(null, null);
        shutdownConnection(mqttToken, null);
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.conLock) {
            z = this.conState == 0;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.conLock) {
            z = true;
            if (this.conState != 1) {
                z = false;
            }
        }
        return z;
    }

    public boolean isDisconnected() {
        boolean z;
        synchronized (this.conLock) {
            z = this.conState == 3;
        }
        return z;
    }

    public boolean isDisconnecting() {
        boolean z;
        synchronized (this.conLock) {
            z = this.conState == 2;
        }
        return z;
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this.conLock) {
            z = this.conState == 4;
        }
        return z;
    }

    public void setCallback(MqttCallback mqttCallback) {
        this.callback.setCallback(mqttCallback);
    }

    /* access modifiers changed from: protected */
    public MqttTopic getTopic(String str) {
        return new MqttTopic(str, this);
    }

    public void setNetworkModuleIndex(int i) {
        this.networkModuleIndex = i;
    }

    public int getNetworkModuleIndex() {
        return this.networkModuleIndex;
    }

    public NetworkModule[] getNetworkModules() {
        return this.networkModules;
    }

    public void setNetworkModules(NetworkModule[] networkModuleArr) {
        this.networkModules = networkModuleArr;
    }

    public MqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.tokenStore.getOutstandingDelTokens();
    }

    /* access modifiers changed from: protected */
    public void deliveryComplete(MqttPublish mqttPublish) throws MqttPersistenceException {
        this.clientState.deliveryComplete(mqttPublish);
    }

    public IMqttAsyncClient getClient() {
        return this.client;
    }

    public long getKeepAlive() {
        return this.clientState.getKeepAlive();
    }

    public ClientState getClientState() {
        return this.clientState;
    }

    public MqttConnectOptions getConOptions() {
        return this.conOptions;
    }

    public Properties getDebug() {
        Properties properties = new Properties();
        properties.put("conState", new Integer(this.conState));
        properties.put("serverURI", getClient().getServerURI());
        properties.put("callback", this.callback);
        properties.put("stoppingComms", new Boolean(this.stoppingComms));
        return properties;
    }

    public MqttToken checkForActivity() {
        try {
            return this.clientState.checkForActivity();
        } catch (MqttException e) {
            handleRunException(e);
            return null;
        } catch (Exception e2) {
            handleRunException(e2);
            return null;
        }
    }

    private void handleRunException(Exception exc) {
        MqttException mqttException;
        log.fine(CLASS_NAME, "handleRunException", "804", null, exc);
        if (!(exc instanceof MqttException)) {
            mqttException = new MqttException(32109, exc);
        } else {
            mqttException = (MqttException) exc;
        }
        shutdownConnection(null, mqttException);
    }
}
