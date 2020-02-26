package org.eclipse.paho.client.mqttv3.internal.wire;

public abstract class MqttAck extends MqttWireMessage {
    /* access modifiers changed from: protected */
    public byte getMessageInfo() {
        return 0;
    }

    public MqttAck(byte b) {
        super(b);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(super.toString()));
        stringBuffer.append(" msgId ");
        stringBuffer.append(this.msgId);
        return stringBuffer.toString();
    }
}
