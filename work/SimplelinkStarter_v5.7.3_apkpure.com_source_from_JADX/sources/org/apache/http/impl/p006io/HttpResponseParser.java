package org.apache.http.impl.p006io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponseFactory;
import org.apache.http.ParseException;
import org.apache.http.message.LineParser;
import org.apache.http.p007io.SessionInputBuffer;
import org.apache.http.params.HttpParams;

@Deprecated
/* renamed from: org.apache.http.impl.io.HttpResponseParser */
public class HttpResponseParser extends AbstractMessageParser {
    public HttpResponseParser(SessionInputBuffer sessionInputBuffer, LineParser lineParser, HttpResponseFactory httpResponseFactory, HttpParams httpParams) {
        super(null, null, null);
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public HttpMessage parseHead(SessionInputBuffer sessionInputBuffer) throws IOException, HttpException, ParseException {
        throw new RuntimeException("Stub!");
    }
}
