package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

@Deprecated
public class Hex implements BinaryEncoder, BinaryDecoder {
    public Hex() {
        throw new RuntimeException("Stub!");
    }

    public static byte[] decodeHex(char[] cArr) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    protected static int toDigit(char c, int i) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    public static char[] encodeHex(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public byte[] decode(byte[] bArr) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    public Object decode(Object obj) throws DecoderException {
        throw new RuntimeException("Stub!");
    }

    public byte[] encode(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public Object encode(Object obj) throws EncoderException {
        throw new RuntimeException("Stub!");
    }
}
