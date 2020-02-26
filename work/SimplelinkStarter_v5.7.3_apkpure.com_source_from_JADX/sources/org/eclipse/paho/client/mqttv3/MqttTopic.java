package org.eclipse.paho.client.mqttv3;

import java.io.UnsupportedEncodingException;
import org.apache.http.protocol.HTTP;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.util.Strings;

public class MqttTopic {
    private static final int MAX_TOPIC_LEN = 65535;
    private static final int MIN_TOPIC_LEN = 1;
    public static final String MULTI_LEVEL_WILDCARD = "#";
    public static final String MULTI_LEVEL_WILDCARD_PATTERN = "/#";
    private static final char NUL = '\u0000';
    public static final String SINGLE_LEVEL_WILDCARD = "+";
    public static final String TOPIC_LEVEL_SEPARATOR = "/";
    public static final String TOPIC_WILDCARDS = "#+";
    private ClientComms comms;
    private String name;

    public MqttTopic(String str, ClientComms clientComms) {
        this.comms = clientComms;
        this.name = str;
    }

    public MqttDeliveryToken publish(byte[] bArr, int i, boolean z) throws MqttException, MqttPersistenceException {
        MqttMessage mqttMessage = new MqttMessage(bArr);
        mqttMessage.setQos(i);
        mqttMessage.setRetained(z);
        return publish(mqttMessage);
    }

    public MqttDeliveryToken publish(MqttMessage mqttMessage) throws MqttException, MqttPersistenceException {
        MqttDeliveryToken mqttDeliveryToken = new MqttDeliveryToken(this.comms.getClient().getClientId());
        mqttDeliveryToken.setMessage(mqttMessage);
        this.comms.sendNoWait(createPublish(mqttMessage), mqttDeliveryToken);
        mqttDeliveryToken.internalTok.waitUntilSent();
        return mqttDeliveryToken;
    }

    public String getName() {
        return this.name;
    }

    private MqttPublish createPublish(MqttMessage mqttMessage) {
        return new MqttPublish(getName(), mqttMessage);
    }

    public String toString() {
        return getName();
    }

    public static void validate(String str, boolean z) {
        try {
            int length = str.getBytes(HTTP.UTF_8).length;
            if (length < 1 || length > 65535) {
                throw new IllegalArgumentException(String.format("Invalid topic length, should be in range[%d, %d]!", new Object[]{new Integer(1), new Integer(65535)}));
            } else if (z) {
                String str2 = MULTI_LEVEL_WILDCARD;
                if (!Strings.equalsAny(str, new String[]{str2, SINGLE_LEVEL_WILDCARD})) {
                    if (Strings.countMatches(str, str2) > 1 || (str.contains(str2) && !str.endsWith(MULTI_LEVEL_WILDCARD_PATTERN))) {
                        StringBuffer stringBuffer = new StringBuffer("Invalid usage of multi-level wildcard in topic string: ");
                        stringBuffer.append(str);
                        throw new IllegalArgumentException(stringBuffer.toString());
                    }
                    validateSingleLevelWildcard(str);
                }
            } else if (Strings.containsAny((CharSequence) str, (CharSequence) TOPIC_WILDCARDS)) {
                throw new IllegalArgumentException("The topic name MUST NOT contain any wildcard characters (#+)");
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void validateSingleLevelWildcard(String str) {
        char charAt = SINGLE_LEVEL_WILDCARD.charAt(0);
        char charAt2 = TOPIC_LEVEL_SEPARATOR.charAt(0);
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        int i = 0;
        while (i < length) {
            int i2 = i - 1;
            char c = i2 >= 0 ? charArray[i2] : NUL;
            int i3 = i + 1;
            char c2 = i3 < length ? charArray[i3] : NUL;
            if (charArray[i] != charAt || ((c == charAt2 || c == 0) && (c2 == charAt2 || c2 == 0))) {
                i = i3;
            } else {
                throw new IllegalArgumentException(String.format("Invalid usage of single-level wildcard in topic string '%s'!", new Object[]{str}));
            }
        }
    }
}
