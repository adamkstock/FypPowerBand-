package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;

@Deprecated
public class QuotedPrintableCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
    public QuotedPrintableCodec() {
        throw new RuntimeException("Stub!");
    }

    public QuotedPrintableCodec(String str) {
        throw new RuntimeException("Stub!");
    }

    public static final byte[] encodeQuotedPrintable(BitSet bitSet, byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public static final byte[] decodeQuotedPrintable(byte[] bArr) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    public byte[] encode(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public byte[] decode(byte[] bArr) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    public String encode(String str) throws EncoderException {
        throw new RuntimeException("Stub!");
    }

    public String decode(String str, String str2) throws DecoderException, UnsupportedEncodingException {
        throw new RuntimeException("Stub!");
    }

    public String decode(String str) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    public Object encode(Object obj) throws EncoderException {
        throw new RuntimeException("Stub!");
    }

    public Object decode(Object obj) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    public String getDefaultCharset() {
        throw new RuntimeException("Stub!");
    }

    public String encode(String str, String str2) throws UnsupportedEncodingException {
        throw new RuntimeException("Stub!");
    }
}
