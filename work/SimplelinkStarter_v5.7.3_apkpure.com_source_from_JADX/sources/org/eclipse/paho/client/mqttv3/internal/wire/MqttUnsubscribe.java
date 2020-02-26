package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttUnsubscribe extends MqttWireMessage {
    private int count;
    private String[] names;

    public boolean isRetryable() {
        return true;
    }

    public MqttUnsubscribe(String[] strArr) {
        super(10);
        this.names = strArr;
    }

    public MqttUnsubscribe(byte b, byte[] bArr) throws IOException {
        super(10);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.msgId = dataInputStream.readUnsignedShort();
        boolean z = false;
        this.count = 0;
        this.names = new String[10];
        while (!z) {
            try {
                this.names[this.count] = decodeUTF8(dataInputStream);
            } catch (Exception unused) {
                z = true;
            }
        }
        dataInputStream.close();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(super.toString());
        stringBuffer.append(" names:[");
        for (int i = 0; i < this.count; i++) {
            if (i > 0) {
                stringBuffer.append(", ");
            }
            String str = "\"";
            StringBuffer stringBuffer2 = new StringBuffer(str);
            stringBuffer2.append(this.names[i]);
            stringBuffer2.append(str);
            stringBuffer.append(stringBuffer2.toString());
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    public byte getMessageInfo() {
        return (byte) ((this.duplicate ? 8 : 0) | 2);
    }

    /* access modifiers changed from: protected */
    public byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeShort(this.msgId);
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MqttException((Throwable) e);
        }
    }

    public byte[] getPayload() throws MqttException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        int i = 0;
        while (true) {
            String[] strArr = this.names;
            if (i >= strArr.length) {
                return byteArrayOutputStream.toByteArray();
            }
            encodeUTF8(dataOutputStream, strArr[i]);
            i++;
        }
    }
}
