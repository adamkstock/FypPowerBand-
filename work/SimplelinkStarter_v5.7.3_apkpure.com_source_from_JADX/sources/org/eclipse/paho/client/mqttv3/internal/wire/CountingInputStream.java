package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends InputStream {
    private int counter = 0;

    /* renamed from: in */
    private InputStream f99in;

    public CountingInputStream(InputStream inputStream) {
        this.f99in = inputStream;
    }

    public int read() throws IOException {
        int read = this.f99in.read();
        if (read != -1) {
            this.counter++;
        }
        return read;
    }

    public int getCounter() {
        return this.counter;
    }

    public void resetCounter() {
        this.counter = 0;
    }
}
