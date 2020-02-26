package org.apache.http.impl.p006io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.params.HttpParams;

@Deprecated
/* renamed from: org.apache.http.impl.io.SocketInputBuffer */
public class SocketInputBuffer extends AbstractSessionInputBuffer {
    public SocketInputBuffer(Socket socket, int i, HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public boolean isDataAvailable(int i) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public boolean isStale() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
