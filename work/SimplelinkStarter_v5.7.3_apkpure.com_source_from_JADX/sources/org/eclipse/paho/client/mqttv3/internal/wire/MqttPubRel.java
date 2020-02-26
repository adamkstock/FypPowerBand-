package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttPubRel extends MqttPersistableWireMessage {
    public MqttPubRel(MqttPubRec mqttPubRec) {
        super(6);
        setMessageId(mqttPubRec.getMessageId());
    }

    public MqttPubRel(byte b, byte[] bArr) throws IOException {
        super(6);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.msgId = dataInputStream.readUnsignedShort();
        dataInputStream.close();
    }

    /* access modifiers changed from: protected */
    public byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }

    /* access modifiers changed from: protected */
    public byte getMessageInfo() {
        return (byte) ((this.duplicate ? 8 : 0) | 2);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(super.toString()));
        stringBuffer.append(" msgId ");
        stringBuffer.append(this.msgId);
        return stringBuffer.toString();
    }
}
