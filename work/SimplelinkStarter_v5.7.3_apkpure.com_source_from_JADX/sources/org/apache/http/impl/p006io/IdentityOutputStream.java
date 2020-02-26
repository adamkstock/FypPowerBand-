package org.apache.http.impl.p006io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.p007io.SessionOutputBuffer;

@Deprecated
/* renamed from: org.apache.http.impl.io.IdentityOutputStream */
public class IdentityOutputStream extends OutputStream {
    public IdentityOutputStream(SessionOutputBuffer sessionOutputBuffer) {
        throw new RuntimeException("Stub!");
    }

    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void flush() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] bArr) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(int i) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
