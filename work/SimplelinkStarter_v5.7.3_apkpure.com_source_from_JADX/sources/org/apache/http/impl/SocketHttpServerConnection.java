package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.http.HttpInetConnection;
import org.apache.http.p007io.SessionInputBuffer;
import org.apache.http.p007io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;

@Deprecated
public class SocketHttpServerConnection extends AbstractHttpServerConnection implements HttpInetConnection {
    public SocketHttpServerConnection() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void assertNotOpen() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void assertOpen() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public SessionInputBuffer createHttpDataReceiver(Socket socket, int i, HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public SessionOutputBuffer createHttpDataTransmitter(Socket socket, int i, HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void bind(Socket socket, HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public Socket getSocket() {
        throw new RuntimeException("Stub!");
    }

    public boolean isOpen() {
        throw new RuntimeException("Stub!");
    }

    public InetAddress getLocalAddress() {
        throw new RuntimeException("Stub!");
    }

    public int getLocalPort() {
        throw new RuntimeException("Stub!");
    }

    public InetAddress getRemoteAddress() {
        throw new RuntimeException("Stub!");
    }

    public int getRemotePort() {
        throw new RuntimeException("Stub!");
    }

    public void setSocketTimeout(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getSocketTimeout() {
        throw new RuntimeException("Stub!");
    }

    public void shutdown() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
}