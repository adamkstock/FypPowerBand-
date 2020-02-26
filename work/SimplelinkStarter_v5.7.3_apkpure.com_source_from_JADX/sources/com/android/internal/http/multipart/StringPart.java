package com.android.internal.http.multipart;

import java.io.IOException;
import java.io.OutputStream;

public class StringPart extends PartBase {
    public static final String DEFAULT_CHARSET = "US-ASCII";
    public static final String DEFAULT_CONTENT_TYPE = "text/plain";
    public static final String DEFAULT_TRANSFER_ENCODING = "8bit";

    public StringPart(String str, String str2, String str3) {
        String str4 = null;
        super(str4, str4, str4, str4);
        throw new RuntimeException("Stub!");
    }

    public StringPart(String str, String str2) {
        String str3 = null;
        super(str3, str3, str3, str3);
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void sendData(OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public long lengthOfData() {
        throw new RuntimeException("Stub!");
    }

    public void setCharSet(String str) {
        throw new RuntimeException("Stub!");
    }
}
