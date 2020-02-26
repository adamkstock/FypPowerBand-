package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.http.protocol.HTTP;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublish extends MqttPersistableWireMessage {
    private byte[] encodedPayload;
    private MqttMessage message;
    private String topicName;

    public boolean isMessageIdRequired() {
        return true;
    }

    public MqttPublish(String str, MqttMessage mqttMessage) {
        super(3);
        this.encodedPayload = null;
        this.topicName = str;
        this.message = mqttMessage;
    }

    public MqttPublish(byte b, byte[] bArr) throws MqttException, IOException {
        super(3);
        this.encodedPayload = null;
        this.message = new MqttReceivedMessage();
        this.message.setQos(3 & (b >> 1));
        if ((b & 1) == 1) {
            this.message.setRetained(true);
        }
        if ((b & 8) == 8) {
            ((MqttReceivedMessage) this.message).setDuplicate(true);
        }
        CountingInputStream countingInputStream = new CountingInputStream(new ByteArrayInputStream(bArr));
        DataInputStream dataInputStream = new DataInputStream(countingInputStream);
        this.topicName = decodeUTF8(dataInputStream);
        if (this.message.getQos() > 0) {
            this.msgId = dataInputStream.readUnsignedShort();
        }
        byte[] bArr2 = new byte[(bArr.length - countingInputStream.getCounter())];
        dataInputStream.readFully(bArr2);
        dataInputStream.close();
        this.message.setPayload(bArr2);
    }

    public String toString() {
        String str;
        StringBuffer stringBuffer = new StringBuffer();
        byte[] payload = this.message.getPayload();
        int min = Math.min(payload.length, 20);
        for (int i = 0; i < min; i++) {
            String hexString = Integer.toHexString(payload[i]);
            if (hexString.length() == 1) {
                StringBuffer stringBuffer2 = new StringBuffer("0");
                stringBuffer2.append(hexString);
                hexString = stringBuffer2.toString();
            }
            stringBuffer.append(hexString);
        }
        try {
            str = new String(payload, 0, min, HTTP.UTF_8);
        } catch (Exception unused) {
            str = "?";
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(super.toString());
        stringBuffer3.append(" qos:");
        stringBuffer3.append(this.message.getQos());
        if (this.message.getQos() > 0) {
            stringBuffer3.append(" msgId:");
            stringBuffer3.append(this.msgId);
        }
        stringBuffer3.append(" retained:");
        stringBuffer3.append(this.message.isRetained());
        stringBuffer3.append(" dup:");
        stringBuffer3.append(this.duplicate);
        stringBuffer3.append(" topic:\"");
        stringBuffer3.append(this.topicName);
        String str2 = "\"";
        stringBuffer3.append(str2);
        stringBuffer3.append(" payload:[hex:");
        stringBuffer3.append(stringBuffer);
        stringBuffer3.append(" utf8:\"");
        stringBuffer3.append(str);
        stringBuffer3.append(str2);
        stringBuffer3.append(" length:");
        stringBuffer3.append(payload.length);
        stringBuffer3.append("]");
        return stringBuffer3.toString();
    }

    /* access modifiers changed from: protected */
    public byte getMessageInfo() {
        byte qos = (byte) (this.message.getQos() << 1);
        if (this.message.isRetained()) {
            qos = (byte) (qos | 1);
        }
        return (this.message.isDuplicate() || this.duplicate) ? (byte) (qos | 8) : qos;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public MqttMessage getMessage() {
        return this.message;
    }

    protected static byte[] encodePayload(MqttMessage mqttMessage) {
        return mqttMessage.getPayload();
    }

    public byte[] getPayload() throws MqttException {
        if (this.encodedPayload == null) {
            this.encodedPayload = encodePayload(this.message);
        }
        return this.encodedPayload;
    }

    public int getPayloadLength() {
        try {
            return getPayload().length;
        } catch (MqttException unused) {
            return 0;
        }
    }

    public void setMessageId(int i) {
        super.setMessageId(i);
        MqttMessage mqttMessage = this.message;
        if (mqttMessage instanceof MqttReceivedMessage) {
            ((MqttReceivedMessage) mqttMessage).setMessageId(i);
        }
    }

    /* access modifiers changed from: protected */
    public byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            encodeUTF8(dataOutputStream, this.topicName);
            if (this.message.getQos() > 0) {
                dataOutputStream.writeShort(this.msgId);
            }
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MqttException((Throwable) e);
        }
    }
}
