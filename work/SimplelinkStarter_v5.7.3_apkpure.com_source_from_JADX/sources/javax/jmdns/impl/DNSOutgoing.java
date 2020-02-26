package javax.jmdns.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.jmdns.impl.constants.DNSConstants;

public final class DNSOutgoing extends DNSMessage {
    private static final int HEADER_SIZE = 12;
    public static boolean USE_DOMAIN_NAME_COMPRESSION = true;
    private final MessageOutputStream _additionalsAnswersBytes;
    private final MessageOutputStream _answersBytes;
    private final MessageOutputStream _authoritativeAnswersBytes;
    private int _maxUDPPayload;
    Map<String, Integer> _names;
    private final MessageOutputStream _questionsBytes;

    public static class MessageOutputStream extends ByteArrayOutputStream {
        private final int _offset;
        private final DNSOutgoing _out;

        MessageOutputStream(int i, DNSOutgoing dNSOutgoing) {
            this(i, dNSOutgoing, 0);
        }

        MessageOutputStream(int i, DNSOutgoing dNSOutgoing, int i2) {
            super(i);
            this._out = dNSOutgoing;
            this._offset = i2;
        }

        /* access modifiers changed from: 0000 */
        public void writeByte(int i) {
            write(i & 255);
        }

        /* access modifiers changed from: 0000 */
        public void writeBytes(String str, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                writeByte(str.charAt(i + i3));
            }
        }

        /* access modifiers changed from: 0000 */
        public void writeBytes(byte[] bArr) {
            if (bArr != null) {
                writeBytes(bArr, 0, bArr.length);
            }
        }

        /* access modifiers changed from: 0000 */
        public void writeBytes(byte[] bArr, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                writeByte(bArr[i + i3]);
            }
        }

        /* access modifiers changed from: 0000 */
        public void writeShort(int i) {
            writeByte(i >> 8);
            writeByte(i);
        }

        /* access modifiers changed from: 0000 */
        public void writeInt(int i) {
            writeShort(i >> 16);
            writeShort(i);
        }

        /* access modifiers changed from: 0000 */
        public void writeUTF(String str, int i, int i2) {
            int i3 = 0;
            for (int i4 = 0; i4 < i2; i4++) {
                char charAt = str.charAt(i + i4);
                i3 = (charAt < 1 || charAt > 127) ? charAt > 2047 ? i3 + 3 : i3 + 2 : i3 + 1;
            }
            writeByte(i3);
            for (int i5 = 0; i5 < i2; i5++) {
                char charAt2 = str.charAt(i + i5);
                if (charAt2 >= 1 && charAt2 <= 127) {
                    writeByte(charAt2);
                } else if (charAt2 > 2047) {
                    writeByte(((charAt2 >> 12) & 15) | 224);
                    writeByte(((charAt2 >> 6) & 63) | 128);
                    writeByte(((charAt2 >> 0) & 63) | 128);
                } else {
                    writeByte(((charAt2 >> 6) & 31) | 192);
                    writeByte(((charAt2 >> 0) & 63) | 128);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void writeName(String str) {
            writeName(str, true);
        }

        /* access modifiers changed from: 0000 */
        public void writeName(String str, boolean z) {
            while (true) {
                int indexOf = str.indexOf(46);
                if (indexOf < 0) {
                    indexOf = str.length();
                }
                if (indexOf <= 0) {
                    writeByte(0);
                    return;
                }
                String substring = str.substring(0, indexOf);
                if (!z || !DNSOutgoing.USE_DOMAIN_NAME_COMPRESSION) {
                    writeUTF(substring, 0, substring.length());
                } else {
                    Integer num = (Integer) this._out._names.get(str);
                    if (num != null) {
                        int intValue = num.intValue();
                        writeByte((intValue >> 8) | 192);
                        writeByte(intValue & 255);
                        return;
                    }
                    this._out._names.put(str, Integer.valueOf(size() + this._offset));
                    writeUTF(substring, 0, substring.length());
                }
                str = str.substring(indexOf);
                if (str.startsWith(".")) {
                    str = str.substring(1);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void writeQuestion(DNSQuestion dNSQuestion) {
            writeName(dNSQuestion.getName());
            writeShort(dNSQuestion.getRecordType().indexValue());
            writeShort(dNSQuestion.getRecordClass().indexValue());
        }

        /* access modifiers changed from: 0000 */
        public void writeRecord(DNSRecord dNSRecord, long j) {
            writeName(dNSRecord.getName());
            writeShort(dNSRecord.getRecordType().indexValue());
            writeShort(dNSRecord.getRecordClass().indexValue() | ((!dNSRecord.isUnique() || !this._out.isMulticast()) ? 0 : 32768));
            writeInt(j == 0 ? dNSRecord.getTTL() : dNSRecord.getRemainingTTL(j));
            MessageOutputStream messageOutputStream = new MessageOutputStream(512, this._out, this._offset + size() + 2);
            dNSRecord.write(messageOutputStream);
            byte[] byteArray = messageOutputStream.toByteArray();
            writeShort(byteArray.length);
            write(byteArray, 0, byteArray.length);
        }
    }

    public DNSOutgoing(int i) {
        this(i, true, DNSConstants.MAX_MSG_TYPICAL);
    }

    public DNSOutgoing(int i, boolean z) {
        this(i, z, DNSConstants.MAX_MSG_TYPICAL);
    }

    public DNSOutgoing(int i, boolean z, int i2) {
        super(i, 0, z);
        this._names = new HashMap();
        this._maxUDPPayload = i2 > 0 ? i2 : DNSConstants.MAX_MSG_TYPICAL;
        this._questionsBytes = new MessageOutputStream(i2, this);
        this._answersBytes = new MessageOutputStream(i2, this);
        this._authoritativeAnswersBytes = new MessageOutputStream(i2, this);
        this._additionalsAnswersBytes = new MessageOutputStream(i2, this);
    }

    public int availableSpace() {
        return ((((this._maxUDPPayload - 12) - this._questionsBytes.size()) - this._answersBytes.size()) - this._authoritativeAnswersBytes.size()) - this._additionalsAnswersBytes.size();
    }

    public void addQuestion(DNSQuestion dNSQuestion) throws IOException {
        MessageOutputStream messageOutputStream = new MessageOutputStream(512, this);
        messageOutputStream.writeQuestion(dNSQuestion);
        byte[] byteArray = messageOutputStream.toByteArray();
        if (byteArray.length < availableSpace()) {
            this._questions.add(dNSQuestion);
            this._questionsBytes.write(byteArray, 0, byteArray.length);
            return;
        }
        throw new IOException("message full");
    }

    public void addAnswer(DNSIncoming dNSIncoming, DNSRecord dNSRecord) throws IOException {
        if (dNSIncoming == null || !dNSRecord.suppressedBy(dNSIncoming)) {
            addAnswer(dNSRecord, 0);
        }
    }

    public void addAnswer(DNSRecord dNSRecord, long j) throws IOException {
        if (dNSRecord == null) {
            return;
        }
        if (j == 0 || !dNSRecord.isExpired(j)) {
            MessageOutputStream messageOutputStream = new MessageOutputStream(512, this);
            messageOutputStream.writeRecord(dNSRecord, j);
            byte[] byteArray = messageOutputStream.toByteArray();
            if (byteArray.length < availableSpace()) {
                this._answers.add(dNSRecord);
                this._answersBytes.write(byteArray, 0, byteArray.length);
                return;
            }
            throw new IOException("message full");
        }
    }

    public void addAuthorativeAnswer(DNSRecord dNSRecord) throws IOException {
        MessageOutputStream messageOutputStream = new MessageOutputStream(512, this);
        messageOutputStream.writeRecord(dNSRecord, 0);
        byte[] byteArray = messageOutputStream.toByteArray();
        if (byteArray.length < availableSpace()) {
            this._authoritativeAnswers.add(dNSRecord);
            this._authoritativeAnswersBytes.write(byteArray, 0, byteArray.length);
            return;
        }
        throw new IOException("message full");
    }

    public void addAdditionalAnswer(DNSIncoming dNSIncoming, DNSRecord dNSRecord) throws IOException {
        MessageOutputStream messageOutputStream = new MessageOutputStream(512, this);
        messageOutputStream.writeRecord(dNSRecord, 0);
        byte[] byteArray = messageOutputStream.toByteArray();
        if (byteArray.length < availableSpace()) {
            this._additionals.add(dNSRecord);
            this._additionalsAnswersBytes.write(byteArray, 0, byteArray.length);
            return;
        }
        throw new IOException("message full");
    }

    public byte[] data() {
        long currentTimeMillis = System.currentTimeMillis();
        this._names.clear();
        MessageOutputStream messageOutputStream = new MessageOutputStream(this._maxUDPPayload, this);
        messageOutputStream.writeShort(this._multicast ? 0 : getId());
        messageOutputStream.writeShort(getFlags());
        messageOutputStream.writeShort(getNumberOfQuestions());
        messageOutputStream.writeShort(getNumberOfAnswers());
        messageOutputStream.writeShort(getNumberOfAuthorities());
        messageOutputStream.writeShort(getNumberOfAdditionals());
        for (DNSQuestion writeQuestion : this._questions) {
            messageOutputStream.writeQuestion(writeQuestion);
        }
        for (DNSRecord writeRecord : this._answers) {
            messageOutputStream.writeRecord(writeRecord, currentTimeMillis);
        }
        for (DNSRecord writeRecord2 : this._authoritativeAnswers) {
            messageOutputStream.writeRecord(writeRecord2, currentTimeMillis);
        }
        for (DNSRecord writeRecord3 : this._additionals) {
            messageOutputStream.writeRecord(writeRecord3, currentTimeMillis);
        }
        return messageOutputStream.toByteArray();
    }

    /* access modifiers changed from: 0000 */
    public String print(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(print());
        if (z) {
            sb.append(print(data()));
        }
        return sb.toString();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(isQuery() ? "dns[query:" : "dns[response:");
        stringBuffer.append(" id=0x");
        stringBuffer.append(Integer.toHexString(getId()));
        if (getFlags() != 0) {
            stringBuffer.append(", flags=0x");
            stringBuffer.append(Integer.toHexString(getFlags()));
            if (isResponse()) {
                stringBuffer.append(":r");
            }
            if (isAuthoritativeAnswer()) {
                stringBuffer.append(":aa");
            }
            if (isTruncated()) {
                stringBuffer.append(":tc");
            }
        }
        if (getNumberOfQuestions() > 0) {
            stringBuffer.append(", questions=");
            stringBuffer.append(getNumberOfQuestions());
        }
        if (getNumberOfAnswers() > 0) {
            stringBuffer.append(", answers=");
            stringBuffer.append(getNumberOfAnswers());
        }
        if (getNumberOfAuthorities() > 0) {
            stringBuffer.append(", authorities=");
            stringBuffer.append(getNumberOfAuthorities());
        }
        if (getNumberOfAdditionals() > 0) {
            stringBuffer.append(", additionals=");
            stringBuffer.append(getNumberOfAdditionals());
        }
        String str = "\n\t";
        if (getNumberOfQuestions() > 0) {
            stringBuffer.append("\nquestions:");
            for (DNSQuestion dNSQuestion : this._questions) {
                stringBuffer.append(str);
                stringBuffer.append(dNSQuestion);
            }
        }
        if (getNumberOfAnswers() > 0) {
            stringBuffer.append("\nanswers:");
            for (DNSRecord dNSRecord : this._answers) {
                stringBuffer.append(str);
                stringBuffer.append(dNSRecord);
            }
        }
        if (getNumberOfAuthorities() > 0) {
            stringBuffer.append("\nauthorities:");
            for (DNSRecord dNSRecord2 : this._authoritativeAnswers) {
                stringBuffer.append(str);
                stringBuffer.append(dNSRecord2);
            }
        }
        if (getNumberOfAdditionals() > 0) {
            stringBuffer.append("\nadditionals:");
            for (DNSRecord dNSRecord3 : this._additionals) {
                stringBuffer.append(str);
                stringBuffer.append(dNSRecord3);
            }
        }
        stringBuffer.append("\nnames=");
        stringBuffer.append(this._names);
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    public int getMaxUDPPayload() {
        return this._maxUDPPayload;
    }
}
