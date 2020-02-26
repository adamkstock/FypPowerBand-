package javax.jmdns.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.jmdns.impl.constants.DNSConstants;
import org.apache.http.HttpStatus;

public abstract class DNSMessage {
    public static final boolean MULTICAST = true;
    public static final boolean UNICAST = false;
    protected final List<DNSRecord> _additionals = Collections.synchronizedList(new LinkedList());
    protected final List<DNSRecord> _answers = Collections.synchronizedList(new LinkedList());
    protected final List<DNSRecord> _authoritativeAnswers = Collections.synchronizedList(new LinkedList());
    private int _flags;
    private int _id;
    boolean _multicast;
    protected final List<DNSQuestion> _questions = Collections.synchronizedList(new LinkedList());

    protected DNSMessage(int i, int i2, boolean z) {
        this._flags = i;
        this._id = i2;
        this._multicast = z;
    }

    public int getId() {
        if (this._multicast) {
            return 0;
        }
        return this._id;
    }

    public void setId(int i) {
        this._id = i;
    }

    public int getFlags() {
        return this._flags;
    }

    public void setFlags(int i) {
        this._flags = i;
    }

    public boolean isMulticast() {
        return this._multicast;
    }

    public Collection<? extends DNSQuestion> getQuestions() {
        return this._questions;
    }

    public int getNumberOfQuestions() {
        return getQuestions().size();
    }

    public Collection<? extends DNSRecord> getAllAnswers() {
        ArrayList arrayList = new ArrayList(this._answers.size() + this._authoritativeAnswers.size() + this._additionals.size());
        arrayList.addAll(this._answers);
        arrayList.addAll(this._authoritativeAnswers);
        arrayList.addAll(this._additionals);
        return arrayList;
    }

    public Collection<? extends DNSRecord> getAnswers() {
        return this._answers;
    }

    public int getNumberOfAnswers() {
        return getAnswers().size();
    }

    public Collection<? extends DNSRecord> getAuthorities() {
        return this._authoritativeAnswers;
    }

    public int getNumberOfAuthorities() {
        return getAuthorities().size();
    }

    public Collection<? extends DNSRecord> getAdditionals() {
        return this._additionals;
    }

    public int getNumberOfAdditionals() {
        return getAdditionals().size();
    }

    public boolean isValidResponseCode() {
        return (this._flags & 15) == 0;
    }

    public int getOperationCode() {
        return (this._flags & DNSConstants.FLAGS_OPCODE) >> 11;
    }

    public boolean isTruncated() {
        return (this._flags & 512) != 0;
    }

    public boolean isAuthoritativeAnswer() {
        return (this._flags & 1024) != 0;
    }

    public boolean isQuery() {
        return (this._flags & 32768) == 0;
    }

    public boolean isResponse() {
        return (this._flags & 32768) == 32768;
    }

    public boolean isEmpty() {
        return ((getNumberOfQuestions() + getNumberOfAnswers()) + getNumberOfAuthorities()) + getNumberOfAdditionals() == 0;
    }

    /* access modifiers changed from: 0000 */
    public String print() {
        StringBuffer stringBuffer = new StringBuffer(HttpStatus.SC_OK);
        stringBuffer.append(toString());
        String str = "\n";
        stringBuffer.append(str);
        for (DNSQuestion dNSQuestion : this._questions) {
            stringBuffer.append("\tquestion:      ");
            stringBuffer.append(dNSQuestion);
            stringBuffer.append(str);
        }
        for (DNSRecord dNSRecord : this._answers) {
            stringBuffer.append("\tanswer:        ");
            stringBuffer.append(dNSRecord);
            stringBuffer.append(str);
        }
        for (DNSRecord dNSRecord2 : this._authoritativeAnswers) {
            stringBuffer.append("\tauthoritative: ");
            stringBuffer.append(dNSRecord2);
            stringBuffer.append(str);
        }
        for (DNSRecord dNSRecord3 : this._additionals) {
            stringBuffer.append("\tadditional:    ");
            stringBuffer.append(dNSRecord3);
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    public String print(byte[] bArr) {
        StringBuilder sb = new StringBuilder(4000);
        int length = bArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            int min = Math.min(32, length - i);
            if (i < 16) {
                sb.append(' ');
            }
            if (i < 256) {
                sb.append(' ');
            }
            if (i < 4096) {
                sb.append(' ');
            }
            sb.append(Integer.toHexString(i));
            sb.append(':');
            int i2 = 0;
            while (i2 < min) {
                if (i2 % 8 == 0) {
                    sb.append(' ');
                }
                int i3 = i + i2;
                sb.append(Integer.toHexString((bArr[i3] & 240) >> 4));
                sb.append(Integer.toHexString((bArr[i3] & 15) >> 0));
                i2++;
            }
            if (i2 < 32) {
                while (i2 < 32) {
                    if (i2 % 8 == 0) {
                        sb.append(' ');
                    }
                    sb.append("  ");
                    i2++;
                }
            }
            sb.append("    ");
            for (int i4 = 0; i4 < min; i4++) {
                if (i4 % 8 == 0) {
                    sb.append(' ');
                }
                byte b = bArr[i + i4] & 255;
                sb.append((b <= 32 || b >= Byte.MAX_VALUE) ? '.' : (char) b);
            }
            sb.append("\n");
            i += 32;
            if (i >= 2048) {
                sb.append("....\n");
                break;
            }
        }
        return sb.toString();
    }
}
