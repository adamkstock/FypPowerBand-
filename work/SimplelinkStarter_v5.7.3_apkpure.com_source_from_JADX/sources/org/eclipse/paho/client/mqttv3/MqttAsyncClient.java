package org.eclipse.paho.client.mqttv3;

import java.util.Hashtable;
import java.util.Properties;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;
import org.eclipse.paho.client.mqttv3.internal.ConnectActionListener;
import org.eclipse.paho.client.mqttv3.internal.ExceptionHelper;
import org.eclipse.paho.client.mqttv3.internal.LocalNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.NetworkModule;
import org.eclipse.paho.client.mqttv3.internal.SSLNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.TCPNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttDisconnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSubscribe;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttUnsubscribe;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.client.mqttv3.util.Debug;

public class MqttAsyncClient implements IMqttAsyncClient {
    private static final String CLASS_NAME;
    private static final String CLIENT_ID_PREFIX = "paho";
    private static final long DISCONNECT_TIMEOUT = 10000;
    private static final char MAX_HIGH_SURROGATE = '?';
    private static final char MIN_HIGH_SURROGATE = '?';
    private static final long QUIESCE_TIMEOUT = 30000;
    static /* synthetic */ Class class$0;
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    private String clientId;
    protected ClientComms comms;
    private MqttClientPersistence persistence;
    private String serverURI;
    private Hashtable topics;

    protected static boolean Character_isHighSurrogate(char c) {
        return c >= 55296 && c <= 56319;
    }

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.MqttAsyncClient");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
    }

    public MqttAsyncClient(String str, String str2) throws MqttException {
        this(str, str2, new MqttDefaultFilePersistence());
    }

    public MqttAsyncClient(String str, String str2, MqttClientPersistence mqttClientPersistence) throws MqttException {
        this(str, str2, mqttClientPersistence, new TimerPingSender());
    }

    public MqttAsyncClient(String str, String str2, MqttClientPersistence mqttClientPersistence, MqttPingSender mqttPingSender) throws MqttException {
        log.setResourceName(str2);
        if (str2 != null) {
            int i = 0;
            int i2 = 0;
            while (i < str2.length() - 1) {
                if (Character_isHighSurrogate(str2.charAt(i))) {
                    i++;
                }
                i2++;
                i++;
            }
            if (i2 <= 65535) {
                MqttConnectOptions.validateURI(str);
                this.serverURI = str;
                this.clientId = str2;
                this.persistence = mqttClientPersistence;
                if (this.persistence == null) {
                    this.persistence = new MemoryPersistence();
                }
                log.fine(CLASS_NAME, "MqttAsyncClient", "101", new Object[]{str2, str, mqttClientPersistence});
                this.persistence.open(str2, str);
                this.comms = new ClientComms(this, this.persistence, mqttPingSender);
                this.persistence.close();
                this.topics = new Hashtable();
                return;
            }
            throw new IllegalArgumentException("ClientId longer than 65535 characters");
        }
        throw new IllegalArgumentException("Null clientId");
    }

    /* access modifiers changed from: protected */
    public NetworkModule[] createNetworkModules(String str, MqttConnectOptions mqttConnectOptions) throws MqttException, MqttSecurityException {
        String str2 = "createNetworkModules";
        log.fine(CLASS_NAME, str2, "116", new Object[]{str});
        String[] serverURIs = mqttConnectOptions.getServerURIs();
        if (serverURIs == null) {
            serverURIs = new String[]{str};
        } else if (serverURIs.length == 0) {
            serverURIs = new String[]{str};
        }
        NetworkModule[] networkModuleArr = new NetworkModule[serverURIs.length];
        for (int i = 0; i < serverURIs.length; i++) {
            networkModuleArr[i] = createNetworkModule(serverURIs[i], mqttConnectOptions);
        }
        log.fine(CLASS_NAME, str2, "108");
        return networkModuleArr;
    }

    private NetworkModule createNetworkModule(String str, MqttConnectOptions mqttConnectOptions) throws MqttException, MqttSecurityException {
        SSLSocketFactoryFactory sSLSocketFactoryFactory;
        log.fine(CLASS_NAME, "createNetworkModule", "115", new Object[]{str});
        SocketFactory socketFactory = mqttConnectOptions.getSocketFactory();
        int validateURI = MqttConnectOptions.validateURI(str);
        if (validateURI == 0) {
            String substring = str.substring(6);
            String hostName = getHostName(substring);
            int port = getPort(substring, 1883);
            if (socketFactory == null) {
                socketFactory = SocketFactory.getDefault();
            } else if (socketFactory instanceof SSLSocketFactory) {
                throw ExceptionHelper.createMqttException(32105);
            }
            TCPNetworkModule tCPNetworkModule = new TCPNetworkModule(socketFactory, hostName, port, this.clientId);
            tCPNetworkModule.setConnectTimeout(mqttConnectOptions.getConnectionTimeout());
            return tCPNetworkModule;
        } else if (validateURI == 1) {
            String substring2 = str.substring(6);
            String hostName2 = getHostName(substring2);
            int port2 = getPort(substring2, 8883);
            if (socketFactory == null) {
                SSLSocketFactoryFactory sSLSocketFactoryFactory2 = new SSLSocketFactoryFactory();
                Properties sSLProperties = mqttConnectOptions.getSSLProperties();
                if (sSLProperties != null) {
                    sSLSocketFactoryFactory2.initialize(sSLProperties, null);
                }
                sSLSocketFactoryFactory = sSLSocketFactoryFactory2;
                socketFactory = sSLSocketFactoryFactory2.createSocketFactory(null);
            } else if (socketFactory instanceof SSLSocketFactory) {
                sSLSocketFactoryFactory = null;
            } else {
                throw ExceptionHelper.createMqttException(32105);
            }
            SSLNetworkModule sSLNetworkModule = new SSLNetworkModule((SSLSocketFactory) socketFactory, hostName2, port2, this.clientId);
            SSLNetworkModule sSLNetworkModule2 = sSLNetworkModule;
            sSLNetworkModule2.setSSLhandshakeTimeout(mqttConnectOptions.getConnectionTimeout());
            if (sSLSocketFactoryFactory != null) {
                String[] enabledCipherSuites = sSLSocketFactoryFactory.getEnabledCipherSuites(null);
                if (enabledCipherSuites != null) {
                    sSLNetworkModule2.setEnabledCiphers(enabledCipherSuites);
                }
            }
            return sSLNetworkModule;
        } else if (validateURI != 2) {
            return null;
        } else {
            return new LocalNetworkModule(str.substring(8));
        }
    }

    private int getPort(String str, int i) {
        int lastIndexOf = str.lastIndexOf(58);
        return lastIndexOf == -1 ? i : Integer.parseInt(str.substring(lastIndexOf + 1));
    }

    private String getHostName(String str) {
        int lastIndexOf = str.lastIndexOf(47);
        int lastIndexOf2 = str.lastIndexOf(58);
        if (lastIndexOf2 == -1) {
            lastIndexOf2 = str.length();
        }
        return str.substring(lastIndexOf + 1, lastIndexOf2);
    }

    public IMqttToken connect(Object obj, IMqttActionListener iMqttActionListener) throws MqttException, MqttSecurityException {
        return connect(new MqttConnectOptions(), obj, iMqttActionListener);
    }

    public IMqttToken connect() throws MqttException, MqttSecurityException {
        return connect(null, null);
    }

    public IMqttToken connect(MqttConnectOptions mqttConnectOptions) throws MqttException, MqttSecurityException {
        return connect(mqttConnectOptions, null, null);
    }

    public IMqttToken connect(MqttConnectOptions mqttConnectOptions, Object obj, IMqttActionListener iMqttActionListener) throws MqttException, MqttSecurityException {
        if (this.comms.isConnected()) {
            throw ExceptionHelper.createMqttException(32100);
        } else if (this.comms.isConnecting()) {
            throw new MqttException(32110);
        } else if (this.comms.isDisconnecting()) {
            throw new MqttException(32102);
        } else if (!this.comms.isClosed()) {
            Logger logger = log;
            String str = CLASS_NAME;
            Object[] objArr = new Object[8];
            objArr[0] = Boolean.valueOf(mqttConnectOptions.isCleanSession());
            objArr[1] = new Integer(mqttConnectOptions.getConnectionTimeout());
            objArr[2] = new Integer(mqttConnectOptions.getKeepAliveInterval());
            objArr[3] = mqttConnectOptions.getUserName();
            String str2 = "[null]";
            String str3 = "[notnull]";
            objArr[4] = mqttConnectOptions.getPassword() == null ? str2 : str3;
            if (mqttConnectOptions.getWillMessage() != null) {
                str2 = str3;
            }
            objArr[5] = str2;
            objArr[6] = obj;
            objArr[7] = iMqttActionListener;
            logger.fine(str, MqttServiceConstants.CONNECT_ACTION, "103", objArr);
            this.comms.setNetworkModules(createNetworkModules(this.serverURI, mqttConnectOptions));
            MqttToken mqttToken = new MqttToken(getClientId());
            ConnectActionListener connectActionListener = new ConnectActionListener(this, this.persistence, this.comms, mqttConnectOptions, mqttToken, obj, iMqttActionListener);
            mqttToken.setActionCallback(connectActionListener);
            mqttToken.setUserContext(this);
            this.comms.setNetworkModuleIndex(0);
            connectActionListener.connect();
            return mqttToken;
        } else {
            throw new MqttException(32111);
        }
    }

    public IMqttToken disconnect(Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        return disconnect(QUIESCE_TIMEOUT, obj, iMqttActionListener);
    }

    public IMqttToken disconnect() throws MqttException {
        return disconnect(null, null);
    }

    public IMqttToken disconnect(long j) throws MqttException {
        return disconnect(j, null, null);
    }

    public IMqttToken disconnect(long j, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        Logger logger = log;
        String str = CLASS_NAME;
        Object[] objArr = {new Long(j), obj, iMqttActionListener};
        String str2 = MqttServiceConstants.DISCONNECT_ACTION;
        logger.fine(str, str2, "104", objArr);
        MqttToken mqttToken = new MqttToken(getClientId());
        mqttToken.setActionCallback(iMqttActionListener);
        mqttToken.setUserContext(obj);
        try {
            this.comms.disconnect(new MqttDisconnect(), j, mqttToken);
            log.fine(CLASS_NAME, str2, "108");
            return mqttToken;
        } catch (MqttException e) {
            log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "105", null, e);
            throw e;
        }
    }

    public void disconnectForcibly() throws MqttException {
        disconnectForcibly(QUIESCE_TIMEOUT, DISCONNECT_TIMEOUT);
    }

    public void disconnectForcibly(long j) throws MqttException {
        disconnectForcibly(QUIESCE_TIMEOUT, j);
    }

    public void disconnectForcibly(long j, long j2) throws MqttException {
        this.comms.disconnectForcibly(j, j2);
    }

    public boolean isConnected() {
        return this.comms.isConnected();
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getServerURI() {
        return this.serverURI;
    }

    /* access modifiers changed from: protected */
    public MqttTopic getTopic(String str) {
        MqttTopic.validate(str, false);
        MqttTopic mqttTopic = (MqttTopic) this.topics.get(str);
        if (mqttTopic != null) {
            return mqttTopic;
        }
        MqttTopic mqttTopic2 = new MqttTopic(str, this.comms);
        this.topics.put(str, mqttTopic2);
        return mqttTopic2;
    }

    public IMqttToken checkPing(Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        String str = "ping";
        log.fine(CLASS_NAME, str, "117");
        MqttToken checkForActivity = this.comms.checkForActivity();
        log.fine(CLASS_NAME, str, "118");
        return checkForActivity;
    }

    public IMqttToken subscribe(String str, int i, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        return subscribe(new String[]{str}, new int[]{i}, obj, iMqttActionListener);
    }

    public IMqttToken subscribe(String str, int i) throws MqttException {
        return subscribe(new String[]{str}, new int[]{i}, (Object) null, (IMqttActionListener) null);
    }

    public IMqttToken subscribe(String[] strArr, int[] iArr) throws MqttException {
        return subscribe(strArr, iArr, (Object) null, (IMqttActionListener) null);
    }

    public IMqttToken subscribe(String[] strArr, int[] iArr, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        if (strArr.length == iArr.length) {
            String str = "";
            for (int i = 0; i < strArr.length; i++) {
                if (i > 0) {
                    StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
                    stringBuffer.append(", ");
                    str = stringBuffer.toString();
                }
                StringBuffer stringBuffer2 = new StringBuffer(String.valueOf(str));
                stringBuffer2.append("topic=");
                stringBuffer2.append(strArr[i]);
                stringBuffer2.append(" qos=");
                stringBuffer2.append(iArr[i]);
                str = stringBuffer2.toString();
                MqttTopic.validate(strArr[i], true);
            }
            Logger logger = log;
            String str2 = CLASS_NAME;
            Object[] objArr = {str, obj, iMqttActionListener};
            String str3 = MqttServiceConstants.SUBSCRIBE_ACTION;
            logger.fine(str2, str3, "106", objArr);
            MqttToken mqttToken = new MqttToken(getClientId());
            mqttToken.setActionCallback(iMqttActionListener);
            mqttToken.setUserContext(obj);
            mqttToken.internalTok.setTopics(strArr);
            this.comms.sendNoWait(new MqttSubscribe(strArr, iArr), mqttToken);
            log.fine(CLASS_NAME, str3, "109");
            return mqttToken;
        }
        throw new IllegalArgumentException();
    }

    public IMqttToken unsubscribe(String str, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        return unsubscribe(new String[]{str}, obj, iMqttActionListener);
    }

    public IMqttToken unsubscribe(String str) throws MqttException {
        return unsubscribe(new String[]{str}, (Object) null, (IMqttActionListener) null);
    }

    public IMqttToken unsubscribe(String[] strArr) throws MqttException {
        return unsubscribe(strArr, (Object) null, (IMqttActionListener) null);
    }

    public IMqttToken unsubscribe(String[] strArr, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        String str = "";
        for (int i = 0; i < strArr.length; i++) {
            if (i > 0) {
                StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
                stringBuffer.append(", ");
                str = stringBuffer.toString();
            }
            StringBuffer stringBuffer2 = new StringBuffer(String.valueOf(str));
            stringBuffer2.append(strArr[i]);
            str = stringBuffer2.toString();
            MqttTopic.validate(strArr[i], true);
        }
        Logger logger = log;
        String str2 = CLASS_NAME;
        Object[] objArr = {str, obj, iMqttActionListener};
        String str3 = MqttServiceConstants.UNSUBSCRIBE_ACTION;
        logger.fine(str2, str3, "107", objArr);
        MqttToken mqttToken = new MqttToken(getClientId());
        mqttToken.setActionCallback(iMqttActionListener);
        mqttToken.setUserContext(obj);
        mqttToken.internalTok.setTopics(strArr);
        this.comms.sendNoWait(new MqttUnsubscribe(strArr), mqttToken);
        log.fine(CLASS_NAME, str3, "110");
        return mqttToken;
    }

    public void setCallback(MqttCallback mqttCallback) {
        this.comms.setCallback(mqttCallback);
    }

    public static String generateClientId() {
        StringBuffer stringBuffer = new StringBuffer(CLIENT_ID_PREFIX);
        stringBuffer.append(System.nanoTime());
        return stringBuffer.toString();
    }

    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.comms.getPendingDeliveryTokens();
    }

    public IMqttDeliveryToken publish(String str, byte[] bArr, int i, boolean z, Object obj, IMqttActionListener iMqttActionListener) throws MqttException, MqttPersistenceException {
        MqttMessage mqttMessage = new MqttMessage(bArr);
        mqttMessage.setQos(i);
        mqttMessage.setRetained(z);
        return publish(str, mqttMessage, obj, iMqttActionListener);
    }

    public IMqttDeliveryToken publish(String str, byte[] bArr, int i, boolean z) throws MqttException, MqttPersistenceException {
        return publish(str, bArr, i, z, null, null);
    }

    public IMqttDeliveryToken publish(String str, MqttMessage mqttMessage) throws MqttException, MqttPersistenceException {
        return publish(str, mqttMessage, (Object) null, (IMqttActionListener) null);
    }

    public IMqttDeliveryToken publish(String str, MqttMessage mqttMessage, Object obj, IMqttActionListener iMqttActionListener) throws MqttException, MqttPersistenceException {
        String str2 = "publish";
        log.fine(CLASS_NAME, str2, "111", new Object[]{str, obj, iMqttActionListener});
        MqttTopic.validate(str, false);
        MqttDeliveryToken mqttDeliveryToken = new MqttDeliveryToken(getClientId());
        mqttDeliveryToken.setActionCallback(iMqttActionListener);
        mqttDeliveryToken.setUserContext(obj);
        mqttDeliveryToken.setMessage(mqttMessage);
        mqttDeliveryToken.internalTok.setTopics(new String[]{str});
        this.comms.sendNoWait(new MqttPublish(str, mqttMessage), mqttDeliveryToken);
        log.fine(CLASS_NAME, str2, "112");
        return mqttDeliveryToken;
    }

    public void close() throws MqttException {
        String str = "close";
        log.fine(CLASS_NAME, str, "113");
        this.comms.close();
        log.fine(CLASS_NAME, str, "114");
    }

    public Debug getDebug() {
        return new Debug(this.clientId, this.comms);
    }
}
