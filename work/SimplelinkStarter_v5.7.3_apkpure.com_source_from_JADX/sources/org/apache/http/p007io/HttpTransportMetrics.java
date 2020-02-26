package org.apache.http.p007io;

@Deprecated
/* renamed from: org.apache.http.io.HttpTransportMetrics */
public interface HttpTransportMetrics {
    long getBytesTransferred();

    void reset();
}
