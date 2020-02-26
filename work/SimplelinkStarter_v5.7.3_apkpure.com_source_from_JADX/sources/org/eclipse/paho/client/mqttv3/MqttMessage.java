package org.eclipse.paho.client.mqttv3;

public class MqttMessage {
    private boolean dup = false;
    private boolean mutable = true;
    private byte[] payload;
    private int qos = 1;
    private boolean retained = false;

    public static void validateQos(int i) {
        if (i < 0 || i > 2) {
            throw new IllegalArgumentException();
        }
    }

    public MqttMessage() {
        setPayload(new byte[0]);
    }

    public MqttMessage(byte[] bArr) {
        setPayload(bArr);
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public void clearPayload() {
        checkMutable();
        this.payload = new byte[0];
    }

    public void setPayload(byte[] bArr) {
        checkMutable();
        if (bArr != null) {
            this.payload = bArr;
            return;
        }
        throw new NullPointerException();
    }

    public boolean isRetained() {
        return this.retained;
    }

    public void setRetained(boolean z) {
        checkMutable();
        this.retained = z;
    }

    public int getQos() {
        return this.qos;
    }

    public void setQos(int i) {
        checkMutable();
        validateQos(i);
        this.qos = i;
    }

    public String toString() {
        return new String(this.payload);
    }

    /* access modifiers changed from: protected */
    public void setMutable(boolean z) {
        this.mutable = z;
    }

    /* access modifiers changed from: protected */
    public void checkMutable() throws IllegalStateException {
        if (!this.mutable) {
            throw new IllegalStateException();
        }
    }

    /* access modifiers changed from: protected */
    public void setDuplicate(boolean z) {
        this.dup = z;
    }

    public boolean isDuplicate() {
        return this.dup;
    }
}
