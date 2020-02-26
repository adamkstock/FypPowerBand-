package org.apache.http.impl.p006io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.params.HttpParams;

@Deprecated
/* renamed from: org.apache.http.impl.io.SocketOutputBuffer */
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {
    public SocketOutputBuffer(Socket socket, int i, HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
