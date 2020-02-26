package com.android.internal.http.multipart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class FilePart extends PartBase {
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    public static final String DEFAULT_TRANSFER_ENCODING = "binary";
    protected static final String FILE_NAME = "; filename=";

    public FilePart(String str, PartSource partSource, String str2, String str3) {
        String str4 = null;
        super(str4, str4, str4, str4);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, PartSource partSource) {
        String str2 = null;
        super(str2, str2, str2, str2);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, File file) throws FileNotFoundException {
        String str2 = null;
        super(str2, str2, str2, str2);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, File file, String str2, String str3) throws FileNotFoundException {
        String str4 = null;
        super(str4, str4, str4, str4);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, String str2, File file) throws FileNotFoundException {
        String str3 = null;
        super(str3, str3, str3, str3);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, String str2, File file, String str3, String str4) throws FileNotFoundException {
        String str5 = null;
        super(str5, str5, str5, str5);
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void sendDispositionHeader(OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void sendData(OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public PartSource getSource() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public long lengthOfData() {
        throw new RuntimeException("Stub!");
    }
}
