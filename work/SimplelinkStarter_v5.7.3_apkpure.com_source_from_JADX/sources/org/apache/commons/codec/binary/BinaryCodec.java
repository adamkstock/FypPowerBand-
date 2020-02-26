package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

@Deprecated
public class BinaryCodec implements BinaryDecoder, BinaryEncoder {
    public BinaryCodec() {
        throw new RuntimeException("Stub!");
    }

    public byte[] encode(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public Object encode(Object obj) throws EncoderException {
        throw new RuntimeException("Stub!");
    }

    public Object decode(Object obj) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    public byte[] decode(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public byte[] toByteArray(String str) {
        throw new RuntimeException("Stub!");
    }

    public static byte[] fromAscii(char[] cArr) {
        throw new RuntimeException("Stub!");
    }

    public static byte[] fromAscii(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public static byte[] toAsciiBytes(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public static char[] toAsciiChars(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public static String toAsciiString(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }
}
