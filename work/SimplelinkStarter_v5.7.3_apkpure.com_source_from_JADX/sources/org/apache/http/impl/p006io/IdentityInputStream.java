package org.apache.http.impl.p006io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.p007io.SessionInputBuffer;

@Deprecated
/* renamed from: org.apache.http.impl.io.IdentityInputStream */
public class IdentityInputStream extends InputStream {
    public IdentityInputStream(SessionInputBuffer sessionInputBuffer) {
        throw new RuntimeException("Stub!");
    }

    public int available() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int read() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
