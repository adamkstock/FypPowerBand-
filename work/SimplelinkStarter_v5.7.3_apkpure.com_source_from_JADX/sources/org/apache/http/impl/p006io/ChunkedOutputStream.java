package org.apache.http.impl.p006io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.p007io.SessionOutputBuffer;

@Deprecated
/* renamed from: org.apache.http.impl.io.ChunkedOutputStream */
public class ChunkedOutputStream extends OutputStream {
    public ChunkedOutputStream(SessionOutputBuffer sessionOutputBuffer, int i) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public ChunkedOutputStream(SessionOutputBuffer sessionOutputBuffer) throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void flushCache() throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void flushCacheWithAppend(byte[] bArr, int i, int i2) throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void writeClosingChunk() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void finish() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(int i) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] bArr) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void flush() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
