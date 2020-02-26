package org.eclipse.paho.client.mqttv3;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.net.SocketFactory;
import org.eclipse.paho.client.mqttv3.util.Debug;

public class MqttConnectOptions {
    public static final boolean CLEAN_SESSION_DEFAULT = true;
    public static final int CONNECTION_TIMEOUT_DEFAULT = 30;
    public static final int KEEP_ALIVE_INTERVAL_DEFAULT = 60;
    public static final int MQTT_VERSION_3_1 = 3;
    public static final int MQTT_VERSION_3_1_1 = 4;
    public static final int MQTT_VERSION_DEFAULT = 0;
    protected static final int URI_TYPE_LOCAL = 2;
    protected static final int URI_TYPE_SSL = 1;
    protected static final int URI_TYPE_TCP = 0;
    private int MqttVersion = 0;
    private boolean cleanSession = true;
    private int connectionTimeout = 30;
    private int keepAliveInterval = 60;
    private char[] password;
    private String[] serverURIs = null;
    private SocketFactory socketFactory;
    private Properties sslClientProps = null;
    private String userName;
    private String willDestination = null;
    private MqttMessage willMessage = null;

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] cArr) {
        this.password = cArr;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String str) {
        if (str == null || !str.trim().equals("")) {
            this.userName = str;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setWill(MqttTopic mqttTopic, byte[] bArr, int i, boolean z) {
        String name = mqttTopic.getName();
        validateWill(name, bArr);
        setWill(name, new MqttMessage(bArr), i, z);
    }

    public void setWill(String str, byte[] bArr, int i, boolean z) {
        validateWill(str, bArr);
        setWill(str, new MqttMessage(bArr), i, z);
    }

    private void validateWill(String str, Object obj) {
        if (str == null || obj == null) {
            throw new IllegalArgumentException();
        }
        MqttTopic.validate(str, false);
    }

    /* access modifiers changed from: protected */
    public void setWill(String str, MqttMessage mqttMessage, int i, boolean z) {
        this.willDestination = str;
        this.willMessage = mqttMessage;
        this.willMessage.setQos(i);
        this.willMessage.setRetained(z);
        this.willMessage.setMutable(false);
    }

    public int getKeepAliveInterval() {
        return this.keepAliveInterval;
    }

    public int getMqttVersion() {
        return this.MqttVersion;
    }

    public void setKeepAliveInterval(int i) throws IllegalArgumentException {
        if (i >= 0) {
            this.keepAliveInterval = i;
            return;
        }
        throw new IllegalArgumentException();
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int i) {
        if (i >= 0) {
            this.connectionTimeout = i;
            return;
        }
        throw new IllegalArgumentException();
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public void setSocketFactory(SocketFactory socketFactory2) {
        this.socketFactory = socketFactory2;
    }

    public String getWillDestination() {
        return this.willDestination;
    }

    public MqttMessage getWillMessage() {
        return this.willMessage;
    }

    public Properties getSSLProperties() {
        return this.sslClientProps;
    }

    public void setSSLProperties(Properties properties) {
        this.sslClientProps = properties;
    }

    public boolean isCleanSession() {
        return this.cleanSession;
    }

    public void setCleanSession(boolean z) {
        this.cleanSession = z;
    }

    public String[] getServerURIs() {
        return this.serverURIs;
    }

    public void setServerURIs(String[] strArr) {
        for (String validateURI : strArr) {
            validateURI(validateURI);
        }
        this.serverURIs = strArr;
    }

    protected static int validateURI(String str) {
        try {
            URI uri = new URI(str);
            if (!uri.getPath().equals("")) {
                throw new IllegalArgumentException(str);
            } else if (uri.getScheme().equals("tcp")) {
                return 0;
            } else {
                if (uri.getScheme().equals("ssl")) {
                    return 1;
                }
                if (uri.getScheme().equals("local")) {
                    return 2;
                }
                throw new IllegalArgumentException(str);
            }
        } catch (URISyntaxException unused) {
            throw new IllegalArgumentException(str);
        }
    }

    public void setMqttVersion(int i) throws IllegalArgumentException {
        if (i == 0 || i == 3 || i == 4) {
            this.MqttVersion = i;
            return;
        }
        throw new IllegalArgumentException();
    }

    public Properties getDebug() {
        Properties properties = new Properties();
        properties.put("MqttVersion", new Integer(getMqttVersion()));
        properties.put("CleanSession", Boolean.valueOf(isCleanSession()));
        properties.put("ConTimeout", new Integer(getConnectionTimeout()));
        properties.put("KeepAliveInterval", new Integer(getKeepAliveInterval()));
        String str = "null";
        properties.put("UserName", getUserName() == null ? str : getUserName());
        properties.put("WillDestination", getWillDestination() == null ? str : getWillDestination());
        String str2 = "SocketFactory";
        if (getSocketFactory() == null) {
            properties.put(str2, str);
        } else {
            properties.put(str2, getSocketFactory());
        }
        String str3 = "SSLProperties";
        if (getSSLProperties() == null) {
            properties.put(str3, str);
        } else {
            properties.put(str3, getSSLProperties());
        }
        return properties;
    }

    public String toString() {
        return Debug.dumpProperties(getDebug(), "Connection options");
    }
}
