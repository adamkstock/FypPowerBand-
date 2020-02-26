package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttConnack extends MqttAck {
    public static final String KEY = "Con";
    private int returnCode;
    private boolean sessionPresent;

    public String getKey() {
        return "Con";
    }

    /* access modifiers changed from: protected */
    public byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    public boolean isMessageIdRequired() {
        return false;
    }

    public MqttConnack(byte b, byte[] bArr) throws IOException {
        super(2);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        boolean z = true;
        if ((dataInputStream.readUnsignedByte() & 1) != 1) {
            z = false;
        }
        this.sessionPresent = z;
        this.returnCode = dataInputStream.readUnsignedByte();
        dataInputStream.close();
    }

    public int getReturnCode() {
        return this.returnCode;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(super.toString()));
        stringBuffer.append(" session present:");
        stringBuffer.append(this.sessionPresent);
        stringBuffer.append(" return code: ");
        stringBuffer.append(this.returnCode);
        return stringBuffer.toString();
    }

    public boolean getSessionPresent() {
        return this.sessionPresent;
    }
}
