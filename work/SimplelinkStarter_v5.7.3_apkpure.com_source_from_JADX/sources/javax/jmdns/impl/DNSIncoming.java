package javax.jmdns.impl;

import com.p004ti.ti_oad.TIOADEoadDefinitions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSLabel;
import javax.jmdns.impl.constants.DNSOptionCode;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public final class DNSIncoming extends DNSMessage {
    public static boolean USE_DOMAIN_NAME_FORMAT_FOR_SRV_TARGET = true;
    private static final char[] _nibbleToHex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static Logger logger = Logger.getLogger(DNSIncoming.class.getName());
    private final MessageInputStream _messageInputStream;
    private final DatagramPacket _packet;
    private final long _receivedTime;
    private int _senderUDPPayload;

    /* renamed from: javax.jmdns.impl.DNSIncoming$1 */
    static /* synthetic */ class C10781 {
        static final /* synthetic */ int[] $SwitchMap$javax$jmdns$impl$constants$DNSLabel = new int[DNSLabel.values().length];
        static final /* synthetic */ int[] $SwitchMap$javax$jmdns$impl$constants$DNSOptionCode = new int[DNSOptionCode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(36:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|(2:23|24)|25|27|28|29|30|31|32|33|34|(2:35|36)|37|39|40|41|42|43|44|45|46|48) */
        /* JADX WARNING: Can't wrap try/catch for region: R(38:0|(2:1|2)|3|5|6|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|34|(2:35|36)|37|39|40|41|42|43|44|45|46|48) */
        /* JADX WARNING: Can't wrap try/catch for region: R(39:0|(2:1|2)|3|5|6|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|34|35|36|37|39|40|41|42|43|44|45|46|48) */
        /* JADX WARNING: Can't wrap try/catch for region: R(40:0|(2:1|2)|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|34|35|36|37|39|40|41|42|43|44|45|46|48) */
        /* JADX WARNING: Can't wrap try/catch for region: R(41:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|34|35|36|37|39|40|41|42|43|44|45|46|48) */
        /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0075 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x007f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x0089 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0093 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00b0 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00ba */
        /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x00c4 */
        static {
            /*
                javax.jmdns.impl.constants.DNSRecordType[] r0 = javax.jmdns.impl.constants.DNSRecordType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$javax$jmdns$impl$constants$DNSRecordType = r0
                r0 = 1
                int[] r1 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x0014 }
                javax.jmdns.impl.constants.DNSRecordType r2 = javax.jmdns.impl.constants.DNSRecordType.TYPE_A     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x001f }
                javax.jmdns.impl.constants.DNSRecordType r3 = javax.jmdns.impl.constants.DNSRecordType.TYPE_AAAA     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x002a }
                javax.jmdns.impl.constants.DNSRecordType r4 = javax.jmdns.impl.constants.DNSRecordType.TYPE_CNAME     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                r3 = 4
                int[] r4 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x0035 }
                javax.jmdns.impl.constants.DNSRecordType r5 = javax.jmdns.impl.constants.DNSRecordType.TYPE_PTR     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                r4 = 5
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x0040 }
                javax.jmdns.impl.constants.DNSRecordType r6 = javax.jmdns.impl.constants.DNSRecordType.TYPE_TXT     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r5[r6] = r4     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x004b }
                javax.jmdns.impl.constants.DNSRecordType r6 = javax.jmdns.impl.constants.DNSRecordType.TYPE_SRV     // Catch:{ NoSuchFieldError -> 0x004b }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r7 = 6
                r5[r6] = r7     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x0056 }
                javax.jmdns.impl.constants.DNSRecordType r6 = javax.jmdns.impl.constants.DNSRecordType.TYPE_HINFO     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r7 = 7
                r5[r6] = r7     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSRecordType     // Catch:{ NoSuchFieldError -> 0x0062 }
                javax.jmdns.impl.constants.DNSRecordType r6 = javax.jmdns.impl.constants.DNSRecordType.TYPE_OPT     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r7 = 8
                r5[r6] = r7     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                javax.jmdns.impl.constants.DNSOptionCode[] r5 = javax.jmdns.impl.constants.DNSOptionCode.values()
                int r5 = r5.length
                int[] r5 = new int[r5]
                $SwitchMap$javax$jmdns$impl$constants$DNSOptionCode = r5
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSOptionCode     // Catch:{ NoSuchFieldError -> 0x0075 }
                javax.jmdns.impl.constants.DNSOptionCode r6 = javax.jmdns.impl.constants.DNSOptionCode.Owner     // Catch:{ NoSuchFieldError -> 0x0075 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0075 }
                r5[r6] = r0     // Catch:{ NoSuchFieldError -> 0x0075 }
            L_0x0075:
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSOptionCode     // Catch:{ NoSuchFieldError -> 0x007f }
                javax.jmdns.impl.constants.DNSOptionCode r6 = javax.jmdns.impl.constants.DNSOptionCode.LLQ     // Catch:{ NoSuchFieldError -> 0x007f }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x007f }
                r5[r6] = r1     // Catch:{ NoSuchFieldError -> 0x007f }
            L_0x007f:
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSOptionCode     // Catch:{ NoSuchFieldError -> 0x0089 }
                javax.jmdns.impl.constants.DNSOptionCode r6 = javax.jmdns.impl.constants.DNSOptionCode.NSID     // Catch:{ NoSuchFieldError -> 0x0089 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0089 }
                r5[r6] = r2     // Catch:{ NoSuchFieldError -> 0x0089 }
            L_0x0089:
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSOptionCode     // Catch:{ NoSuchFieldError -> 0x0093 }
                javax.jmdns.impl.constants.DNSOptionCode r6 = javax.jmdns.impl.constants.DNSOptionCode.f88UL     // Catch:{ NoSuchFieldError -> 0x0093 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0093 }
                r5[r6] = r3     // Catch:{ NoSuchFieldError -> 0x0093 }
            L_0x0093:
                int[] r5 = $SwitchMap$javax$jmdns$impl$constants$DNSOptionCode     // Catch:{ NoSuchFieldError -> 0x009d }
                javax.jmdns.impl.constants.DNSOptionCode r6 = javax.jmdns.impl.constants.DNSOptionCode.Unknown     // Catch:{ NoSuchFieldError -> 0x009d }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x009d }
                r5[r6] = r4     // Catch:{ NoSuchFieldError -> 0x009d }
            L_0x009d:
                javax.jmdns.impl.constants.DNSLabel[] r4 = javax.jmdns.impl.constants.DNSLabel.values()
                int r4 = r4.length
                int[] r4 = new int[r4]
                $SwitchMap$javax$jmdns$impl$constants$DNSLabel = r4
                int[] r4 = $SwitchMap$javax$jmdns$impl$constants$DNSLabel     // Catch:{ NoSuchFieldError -> 0x00b0 }
                javax.jmdns.impl.constants.DNSLabel r5 = javax.jmdns.impl.constants.DNSLabel.Standard     // Catch:{ NoSuchFieldError -> 0x00b0 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b0 }
                r4[r5] = r0     // Catch:{ NoSuchFieldError -> 0x00b0 }
            L_0x00b0:
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSLabel     // Catch:{ NoSuchFieldError -> 0x00ba }
                javax.jmdns.impl.constants.DNSLabel r4 = javax.jmdns.impl.constants.DNSLabel.Compressed     // Catch:{ NoSuchFieldError -> 0x00ba }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ba }
                r0[r4] = r1     // Catch:{ NoSuchFieldError -> 0x00ba }
            L_0x00ba:
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSLabel     // Catch:{ NoSuchFieldError -> 0x00c4 }
                javax.jmdns.impl.constants.DNSLabel r1 = javax.jmdns.impl.constants.DNSLabel.Extended     // Catch:{ NoSuchFieldError -> 0x00c4 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c4 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00c4 }
            L_0x00c4:
                int[] r0 = $SwitchMap$javax$jmdns$impl$constants$DNSLabel     // Catch:{ NoSuchFieldError -> 0x00ce }
                javax.jmdns.impl.constants.DNSLabel r1 = javax.jmdns.impl.constants.DNSLabel.Unknown     // Catch:{ NoSuchFieldError -> 0x00ce }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ce }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x00ce }
            L_0x00ce:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.DNSIncoming.C10781.<clinit>():void");
        }
    }

    public static class MessageInputStream extends ByteArrayInputStream {
        private static Logger logger1 = Logger.getLogger(MessageInputStream.class.getName());
        final Map<Integer, String> _names;

        public MessageInputStream(byte[] bArr, int i) {
            this(bArr, 0, i);
        }

        public MessageInputStream(byte[] bArr, int i, int i2) {
            super(bArr, i, i2);
            this._names = new HashMap();
        }

        public int readByte() {
            return read();
        }

        public int readUnsignedByte() {
            return read() & 255;
        }

        public int readUnsignedShort() {
            return (readUnsignedByte() << 8) | readUnsignedByte();
        }

        public int readInt() {
            return (readUnsignedShort() << 16) | readUnsignedShort();
        }

        public byte[] readBytes(int i) {
            byte[] bArr = new byte[i];
            read(bArr, 0, i);
            return bArr;
        }

        public String readUTF(int i) {
            int i2;
            int readUnsignedByte;
            StringBuilder sb = new StringBuilder(i);
            int i3 = 0;
            while (i3 < i) {
                int readUnsignedByte2 = readUnsignedByte();
                switch (readUnsignedByte2 >> 4) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        break;
                    case 12:
                    case 13:
                        i2 = (readUnsignedByte2 & 31) << 6;
                        readUnsignedByte = readUnsignedByte() & 63;
                        break;
                    case 14:
                        readUnsignedByte2 = ((readUnsignedByte2 & 15) << 12) | ((readUnsignedByte() & 63) << 6) | (readUnsignedByte() & 63);
                        i3++;
                        break;
                    default:
                        i2 = (readUnsignedByte2 & 63) << 4;
                        readUnsignedByte = readUnsignedByte() & 15;
                        break;
                }
                readUnsignedByte2 = i2 | readUnsignedByte;
                i3++;
                sb.append((char) readUnsignedByte2);
                i3++;
            }
            return sb.toString();
        }

        /* access modifiers changed from: protected */
        public synchronized int peek() {
            return this.pos < this.count ? this.buf[this.pos] & 255 : -1;
        }

        public String readName() {
            HashMap hashMap = new HashMap();
            StringBuilder sb = new StringBuilder();
            boolean z = false;
            while (!z) {
                int readUnsignedByte = readUnsignedByte();
                if (readUnsignedByte == 0) {
                    break;
                }
                int i = C10781.$SwitchMap$javax$jmdns$impl$constants$DNSLabel[DNSLabel.labelForByte(readUnsignedByte).ordinal()];
                if (i == 1) {
                    int i2 = this.pos - 1;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(readUTF(readUnsignedByte));
                    sb2.append(".");
                    String sb3 = sb2.toString();
                    sb.append(sb3);
                    for (StringBuilder append : hashMap.values()) {
                        append.append(sb3);
                    }
                    hashMap.put(Integer.valueOf(i2), new StringBuilder(sb3));
                } else if (i == 2) {
                    int labelValue = (DNSLabel.labelValue(readUnsignedByte) << 8) | readUnsignedByte();
                    String str = (String) this._names.get(Integer.valueOf(labelValue));
                    if (str == null) {
                        Logger logger = logger1;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("bad domain name: possible circular name detected. Bad offset: 0x");
                        sb4.append(Integer.toHexString(labelValue));
                        sb4.append(" at 0x");
                        sb4.append(Integer.toHexString(this.pos - 2));
                        logger.severe(sb4.toString());
                        str = "";
                    }
                    sb.append(str);
                    for (StringBuilder append2 : hashMap.values()) {
                        append2.append(str);
                    }
                    z = true;
                } else if (i != 3) {
                    Logger logger2 = logger1;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("unsupported dns label type: '");
                    sb5.append(Integer.toHexString(readUnsignedByte & 192));
                    sb5.append("'");
                    logger2.severe(sb5.toString());
                } else {
                    logger1.severe("Extended label are not currently supported.");
                }
            }
            for (Integer num : hashMap.keySet()) {
                this._names.put(num, ((StringBuilder) hashMap.get(num)).toString());
            }
            return sb.toString();
        }

        public String readNonNameString() {
            return readUTF(readUnsignedByte());
        }
    }

    public DNSIncoming(DatagramPacket datagramPacket) throws IOException {
        super(0, 0, datagramPacket.getPort() == DNSConstants.MDNS_PORT);
        this._packet = datagramPacket;
        InetAddress address = datagramPacket.getAddress();
        this._messageInputStream = new MessageInputStream(datagramPacket.getData(), datagramPacket.getLength());
        this._receivedTime = System.currentTimeMillis();
        this._senderUDPPayload = DNSConstants.MAX_MSG_TYPICAL;
        try {
            setId(this._messageInputStream.readUnsignedShort());
            setFlags(this._messageInputStream.readUnsignedShort());
            if (getOperationCode() <= 0) {
                int readUnsignedShort = this._messageInputStream.readUnsignedShort();
                int readUnsignedShort2 = this._messageInputStream.readUnsignedShort();
                int readUnsignedShort3 = this._messageInputStream.readUnsignedShort();
                int readUnsignedShort4 = this._messageInputStream.readUnsignedShort();
                String str = " additionals:";
                String str2 = " authorities:";
                String str3 = " answers:";
                if (logger.isLoggable(Level.FINER)) {
                    Logger logger2 = logger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("DNSIncoming() questions:");
                    sb.append(readUnsignedShort);
                    sb.append(str3);
                    sb.append(readUnsignedShort2);
                    sb.append(str2);
                    sb.append(readUnsignedShort3);
                    sb.append(str);
                    sb.append(readUnsignedShort4);
                    logger2.finer(sb.toString());
                }
                if ((readUnsignedShort * 5) + ((readUnsignedShort2 + readUnsignedShort3 + readUnsignedShort4) * 11) <= datagramPacket.getLength()) {
                    if (readUnsignedShort > 0) {
                        for (int i = 0; i < readUnsignedShort; i++) {
                            this._questions.add(readQuestion());
                        }
                    }
                    if (readUnsignedShort2 > 0) {
                        for (int i2 = 0; i2 < readUnsignedShort2; i2++) {
                            DNSRecord readAnswer = readAnswer(address);
                            if (readAnswer != null) {
                                this._answers.add(readAnswer);
                            }
                        }
                    }
                    if (readUnsignedShort3 > 0) {
                        for (int i3 = 0; i3 < readUnsignedShort3; i3++) {
                            DNSRecord readAnswer2 = readAnswer(address);
                            if (readAnswer2 != null) {
                                this._authoritativeAnswers.add(readAnswer2);
                            }
                        }
                    }
                    if (readUnsignedShort4 > 0) {
                        for (int i4 = 0; i4 < readUnsignedShort4; i4++) {
                            DNSRecord readAnswer3 = readAnswer(address);
                            if (readAnswer3 != null) {
                                this._additionals.add(readAnswer3);
                            }
                        }
                    }
                    if (this._messageInputStream.available() > 0) {
                        throw new IOException("Received a message with the wrong length.");
                    }
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("questions:");
                sb2.append(readUnsignedShort);
                sb2.append(str3);
                sb2.append(readUnsignedShort2);
                sb2.append(str2);
                sb2.append(readUnsignedShort3);
                sb2.append(str);
                sb2.append(readUnsignedShort4);
                throw new IOException(sb2.toString());
            }
            throw new IOException("Received a message with a non standard operation code. Currently unsupported in the specification.");
        } catch (Exception e) {
            Logger logger3 = logger;
            Level level = Level.WARNING;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("DNSIncoming() dump ");
            sb3.append(print(true));
            sb3.append("\n exception ");
            logger3.log(level, sb3.toString(), e);
            IOException iOException = new IOException("DNSIncoming corrupted message");
            iOException.initCause(e);
            throw iOException;
        }
    }

    private DNSIncoming(int i, int i2, boolean z, DatagramPacket datagramPacket, long j) {
        super(i, i2, z);
        this._packet = datagramPacket;
        this._messageInputStream = new MessageInputStream(datagramPacket.getData(), datagramPacket.getLength());
        this._receivedTime = j;
    }

    public DNSIncoming clone() {
        DNSIncoming dNSIncoming = new DNSIncoming(getFlags(), getId(), isMulticast(), this._packet, this._receivedTime);
        dNSIncoming._senderUDPPayload = this._senderUDPPayload;
        dNSIncoming._questions.addAll(this._questions);
        dNSIncoming._answers.addAll(this._answers);
        dNSIncoming._authoritativeAnswers.addAll(this._authoritativeAnswers);
        dNSIncoming._additionals.addAll(this._additionals);
        return dNSIncoming;
    }

    private DNSQuestion readQuestion() {
        String readName = this._messageInputStream.readName();
        DNSRecordType typeForIndex = DNSRecordType.typeForIndex(this._messageInputStream.readUnsignedShort());
        if (typeForIndex == DNSRecordType.TYPE_IGNORE) {
            Logger logger2 = logger;
            Level level = Level.SEVERE;
            StringBuilder sb = new StringBuilder();
            sb.append("Could not find record type: ");
            sb.append(print(true));
            logger2.log(level, sb.toString());
        }
        int readUnsignedShort = this._messageInputStream.readUnsignedShort();
        DNSRecordClass classForIndex = DNSRecordClass.classForIndex(readUnsignedShort);
        return DNSQuestion.newQuestion(readName, typeForIndex, classForIndex, classForIndex.isUnique(readUnsignedShort));
    }

    /* JADX WARNING: type inference failed for: r1v4, types: [javax.jmdns.impl.DNSRecord] */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r2v5, types: [javax.jmdns.impl.DNSRecord$IPv4Address] */
    /* JADX WARNING: type inference failed for: r2v7, types: [javax.jmdns.impl.DNSRecord$IPv6Address] */
    /* JADX WARNING: type inference failed for: r2v10, types: [javax.jmdns.impl.DNSRecord$Pointer] */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: type inference failed for: r2v12, types: [javax.jmdns.impl.DNSRecord$Text] */
    /* JADX WARNING: type inference failed for: r2v17, types: [javax.jmdns.impl.DNSRecord$Service] */
    /* JADX WARNING: type inference failed for: r1v15 */
    /* JADX WARNING: type inference failed for: r2v26, types: [javax.jmdns.impl.DNSRecord$HostInformation] */
    /* JADX WARNING: type inference failed for: r1v18 */
    /* JADX WARNING: type inference failed for: r1v55 */
    /* JADX WARNING: type inference failed for: r2v51, types: [javax.jmdns.impl.DNSRecord$IPv4Address] */
    /* JADX WARNING: type inference failed for: r2v52, types: [javax.jmdns.impl.DNSRecord$IPv6Address] */
    /* JADX WARNING: type inference failed for: r2v53, types: [javax.jmdns.impl.DNSRecord$Text] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v11
      assigns: [?[OBJECT, ARRAY], javax.jmdns.impl.DNSRecord$IPv4Address, javax.jmdns.impl.DNSRecord$IPv6Address, javax.jmdns.impl.DNSRecord$Text]
      uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], javax.jmdns.impl.DNSRecord, javax.jmdns.impl.DNSRecord$IPv4Address, javax.jmdns.impl.DNSRecord$IPv6Address, javax.jmdns.impl.DNSRecord$Text]
      mth insns count: 355
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x00d0 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0245  */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private javax.jmdns.impl.DNSRecord readAnswer(java.net.InetAddress r19) {
        /*
            r18 = this;
            r0 = r18
            javax.jmdns.impl.DNSIncoming$MessageInputStream r1 = r0._messageInputStream
            java.lang.String r3 = r1.readName()
            javax.jmdns.impl.DNSIncoming$MessageInputStream r1 = r0._messageInputStream
            int r1 = r1.readUnsignedShort()
            javax.jmdns.impl.constants.DNSRecordType r1 = javax.jmdns.impl.constants.DNSRecordType.typeForIndex(r1)
            javax.jmdns.impl.constants.DNSRecordType r2 = javax.jmdns.impl.constants.DNSRecordType.TYPE_IGNORE
            java.lang.String r4 = "\n"
            r5 = 1
            if (r1 != r2) goto L_0x003b
            java.util.logging.Logger r2 = logger
            java.util.logging.Level r6 = java.util.logging.Level.SEVERE
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Could not find record type. domain: "
            r7.append(r8)
            r7.append(r3)
            r7.append(r4)
            java.lang.String r8 = r0.print(r5)
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            r2.log(r6, r7)
        L_0x003b:
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            int r2 = r2.readUnsignedShort()
            javax.jmdns.impl.constants.DNSRecordType r6 = javax.jmdns.impl.constants.DNSRecordType.TYPE_OPT
            if (r1 != r6) goto L_0x0048
            javax.jmdns.impl.constants.DNSRecordClass r6 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_UNKNOWN
            goto L_0x004c
        L_0x0048:
            javax.jmdns.impl.constants.DNSRecordClass r6 = javax.jmdns.impl.constants.DNSRecordClass.classForIndex(r2)
        L_0x004c:
            javax.jmdns.impl.constants.DNSRecordClass r7 = javax.jmdns.impl.constants.DNSRecordClass.CLASS_UNKNOWN
            if (r6 != r7) goto L_0x007e
            javax.jmdns.impl.constants.DNSRecordType r7 = javax.jmdns.impl.constants.DNSRecordType.TYPE_OPT
            if (r1 == r7) goto L_0x007e
            java.util.logging.Logger r7 = logger
            java.util.logging.Level r8 = java.util.logging.Level.SEVERE
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Could not find record class. domain: "
            r9.append(r10)
            r9.append(r3)
            java.lang.String r10 = " type: "
            r9.append(r10)
            r9.append(r1)
            r9.append(r4)
            java.lang.String r4 = r0.print(r5)
            r9.append(r4)
            java.lang.String r4 = r9.toString()
            r7.log(r8, r4)
        L_0x007e:
            boolean r7 = r6.isUnique(r2)
            javax.jmdns.impl.DNSIncoming$MessageInputStream r4 = r0._messageInputStream
            int r8 = r4.readInt()
            javax.jmdns.impl.DNSIncoming$MessageInputStream r4 = r0._messageInputStream
            int r4 = r4.readUnsignedShort()
            int[] r9 = javax.jmdns.impl.DNSIncoming.C10781.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType
            int r10 = r1.ordinal()
            r9 = r9[r10]
            java.lang.String r10 = ""
            r12 = 0
            switch(r9) {
                case 1: goto L_0x03a9;
                case 2: goto L_0x0398;
                case 3: goto L_0x035f;
                case 4: goto L_0x035f;
                case 5: goto L_0x034d;
                case 6: goto L_0x0319;
                case 7: goto L_0x02dd;
                case 8: goto L_0x00be;
                default: goto L_0x009c;
            }
        L_0x009c:
            java.util.logging.Logger r2 = logger
            java.util.logging.Level r3 = java.util.logging.Level.FINER
            boolean r2 = r2.isLoggable(r3)
            if (r2 == 0) goto L_0x03ba
            java.util.logging.Logger r2 = logger
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "DNSIncoming() unknown type:"
            r3.append(r5)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            r2.finer(r1)
            goto L_0x03ba
        L_0x00be:
            int r1 = r18.getFlags()
            javax.jmdns.impl.constants.DNSResultCode r1 = javax.jmdns.impl.constants.DNSResultCode.resultCodeForFlags(r1, r8)
            r3 = 16711680(0xff0000, float:2.3418052E-38)
            r3 = r3 & r8
            r4 = 16
            int r3 = r3 >> r4
            if (r3 != 0) goto L_0x02bb
            r0._senderUDPPayload = r2
        L_0x00d0:
            javax.jmdns.impl.DNSIncoming$MessageInputStream r1 = r0._messageInputStream
            int r1 = r1.available()
            if (r1 <= 0) goto L_0x03c0
            javax.jmdns.impl.DNSIncoming$MessageInputStream r1 = r0._messageInputStream
            int r1 = r1.available()
            java.lang.String r2 = "There was a problem reading the OPT record. Ignoring."
            r3 = 2
            if (r1 < r3) goto L_0x02b2
            javax.jmdns.impl.DNSIncoming$MessageInputStream r1 = r0._messageInputStream
            int r1 = r1.readUnsignedShort()
            javax.jmdns.impl.constants.DNSOptionCode r6 = javax.jmdns.impl.constants.DNSOptionCode.resultCodeForFlags(r1)
            javax.jmdns.impl.DNSIncoming$MessageInputStream r7 = r0._messageInputStream
            int r7 = r7.available()
            if (r7 < r3) goto L_0x02a9
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            int r2 = r2.readUnsignedShort()
            byte[] r7 = new byte[r12]
            javax.jmdns.impl.DNSIncoming$MessageInputStream r8 = r0._messageInputStream
            int r8 = r8.available()
            if (r8 < r2) goto L_0x010b
            javax.jmdns.impl.DNSIncoming$MessageInputStream r7 = r0._messageInputStream
            byte[] r7 = r7.readBytes(r2)
        L_0x010b:
            int[] r2 = javax.jmdns.impl.DNSIncoming.C10781.$SwitchMap$javax$jmdns$impl$constants$DNSOptionCode
            int r8 = r6.ordinal()
            r2 = r2[r8]
            r8 = 5
            r9 = 4
            r13 = 3
            if (r2 == r5) goto L_0x0174
            java.lang.String r14 = " data: "
            if (r2 == r3) goto L_0x0146
            if (r2 == r13) goto L_0x0146
            if (r2 == r9) goto L_0x0146
            if (r2 == r8) goto L_0x0123
            goto L_0x00d0
        L_0x0123:
            java.util.logging.Logger r2 = logger
            java.util.logging.Level r3 = java.util.logging.Level.WARNING
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r8 = "There was an OPT answer. Not currently handled. Option code: "
            r6.append(r8)
            r6.append(r1)
            r6.append(r14)
            java.lang.String r1 = r0._hexString(r7)
            r6.append(r1)
            java.lang.String r1 = r6.toString()
            r2.log(r3, r1)
            goto L_0x00d0
        L_0x0146:
            java.util.logging.Logger r1 = logger
            java.util.logging.Level r2 = java.util.logging.Level.FINE
            boolean r1 = r1.isLoggable(r2)
            if (r1 == 0) goto L_0x00d0
            java.util.logging.Logger r1 = logger
            java.util.logging.Level r2 = java.util.logging.Level.FINE
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r8 = "There was an OPT answer. Option code: "
            r3.append(r8)
            r3.append(r6)
            r3.append(r14)
            java.lang.String r6 = r0._hexString(r7)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            r1.log(r2, r3)
            goto L_0x00d0
        L_0x0174:
            byte r1 = r7[r12]     // Catch:{ Exception -> 0x021c }
            byte r2 = r7[r5]     // Catch:{ Exception -> 0x021d }
            r6 = 6
            byte[] r14 = new byte[r6]     // Catch:{ Exception -> 0x021e }
            byte r15 = r7[r3]     // Catch:{ Exception -> 0x021e }
            r14[r12] = r15     // Catch:{ Exception -> 0x021e }
            byte r15 = r7[r13]     // Catch:{ Exception -> 0x021e }
            r14[r5] = r15     // Catch:{ Exception -> 0x021e }
            byte r15 = r7[r9]     // Catch:{ Exception -> 0x021e }
            r14[r3] = r15     // Catch:{ Exception -> 0x021e }
            byte r15 = r7[r8]     // Catch:{ Exception -> 0x021e }
            r14[r13] = r15     // Catch:{ Exception -> 0x021e }
            byte r15 = r7[r6]     // Catch:{ Exception -> 0x021e }
            r14[r9] = r15     // Catch:{ Exception -> 0x021e }
            r15 = 7
            byte r16 = r7[r15]     // Catch:{ Exception -> 0x021e }
            r14[r8] = r16     // Catch:{ Exception -> 0x021e }
            int r11 = r7.length     // Catch:{ Exception -> 0x0219 }
            r15 = 8
            if (r11 <= r15) goto L_0x01be
            byte[] r11 = new byte[r6]     // Catch:{ Exception -> 0x0219 }
            byte r17 = r7[r15]     // Catch:{ Exception -> 0x0219 }
            r11[r12] = r17     // Catch:{ Exception -> 0x0219 }
            r17 = 9
            byte r17 = r7[r17]     // Catch:{ Exception -> 0x0219 }
            r11[r5] = r17     // Catch:{ Exception -> 0x0219 }
            r17 = 10
            byte r17 = r7[r17]     // Catch:{ Exception -> 0x0219 }
            r11[r3] = r17     // Catch:{ Exception -> 0x0219 }
            r17 = 11
            byte r17 = r7[r17]     // Catch:{ Exception -> 0x0219 }
            r11[r13] = r17     // Catch:{ Exception -> 0x0219 }
            r17 = 12
            byte r17 = r7[r17]     // Catch:{ Exception -> 0x0219 }
            r11[r9] = r17     // Catch:{ Exception -> 0x0219 }
            r17 = 13
            byte r17 = r7[r17]     // Catch:{ Exception -> 0x0219 }
            r11[r8] = r17     // Catch:{ Exception -> 0x0219 }
            goto L_0x01bf
        L_0x01be:
            r11 = r14
        L_0x01bf:
            int r6 = r7.length     // Catch:{ Exception -> 0x021a }
            r8 = 18
            if (r6 != r8) goto L_0x01dd
            byte[] r6 = new byte[r9]     // Catch:{ Exception -> 0x021a }
            r8 = 14
            byte r8 = r7[r8]     // Catch:{ Exception -> 0x021a }
            r6[r12] = r8     // Catch:{ Exception -> 0x021a }
            r8 = 15
            byte r8 = r7[r8]     // Catch:{ Exception -> 0x021a }
            r6[r5] = r8     // Catch:{ Exception -> 0x021a }
            byte r8 = r7[r4]     // Catch:{ Exception -> 0x021a }
            r6[r3] = r8     // Catch:{ Exception -> 0x021a }
            r8 = 17
            byte r8 = r7[r8]     // Catch:{ Exception -> 0x021a }
            r6[r13] = r8     // Catch:{ Exception -> 0x021a }
            goto L_0x01de
        L_0x01dd:
            r6 = 0
        L_0x01de:
            int r8 = r7.length     // Catch:{ Exception -> 0x0221 }
            r9 = 22
            if (r8 != r9) goto L_0x023b
            byte[] r8 = new byte[r15]     // Catch:{ Exception -> 0x0221 }
            r9 = 14
            byte r9 = r7[r9]     // Catch:{ Exception -> 0x0221 }
            r8[r12] = r9     // Catch:{ Exception -> 0x0221 }
            r9 = 15
            byte r9 = r7[r9]     // Catch:{ Exception -> 0x0221 }
            r8[r5] = r9     // Catch:{ Exception -> 0x0221 }
            byte r9 = r7[r4]     // Catch:{ Exception -> 0x0221 }
            r8[r3] = r9     // Catch:{ Exception -> 0x0221 }
            r3 = 17
            byte r3 = r7[r3]     // Catch:{ Exception -> 0x0221 }
            r8[r13] = r3     // Catch:{ Exception -> 0x0221 }
            r3 = 18
            byte r3 = r7[r3]     // Catch:{ Exception -> 0x0221 }
            r9 = 4
            r8[r9] = r3     // Catch:{ Exception -> 0x0221 }
            r3 = 19
            byte r3 = r7[r3]     // Catch:{ Exception -> 0x0221 }
            r9 = 5
            r8[r9] = r3     // Catch:{ Exception -> 0x0221 }
            r3 = 20
            byte r3 = r7[r3]     // Catch:{ Exception -> 0x0221 }
            r9 = 6
            r8[r9] = r3     // Catch:{ Exception -> 0x0221 }
            r3 = 21
            byte r3 = r7[r3]     // Catch:{ Exception -> 0x0221 }
            r9 = 7
            r8[r9] = r3     // Catch:{ Exception -> 0x0221 }
            r6 = r8
            goto L_0x023b
        L_0x0219:
            r11 = r14
        L_0x021a:
            r6 = 0
            goto L_0x0221
        L_0x021c:
            r1 = 0
        L_0x021d:
            r2 = 0
        L_0x021e:
            r6 = 0
            r11 = 0
            r14 = 0
        L_0x0221:
            java.util.logging.Logger r3 = logger
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "Malformed OPT answer. Option code: Owner data: "
            r8.append(r9)
            java.lang.String r7 = r0._hexString(r7)
            r8.append(r7)
            java.lang.String r7 = r8.toString()
            r3.warning(r7)
        L_0x023b:
            java.util.logging.Logger r3 = logger
            java.util.logging.Level r7 = java.util.logging.Level.FINE
            boolean r3 = r3.isLoggable(r7)
            if (r3 == 0) goto L_0x00d0
            java.util.logging.Logger r3 = logger
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Unhandled Owner OPT version: "
            r7.append(r8)
            r7.append(r1)
            java.lang.String r1 = " sequence: "
            r7.append(r1)
            r7.append(r2)
            java.lang.String r1 = " MAC address: "
            r7.append(r1)
            java.lang.String r1 = r0._hexString(r14)
            r7.append(r1)
            if (r11 == r14) goto L_0x0280
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = " wakeup MAC address: "
            r1.append(r2)
            java.lang.String r2 = r0._hexString(r11)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            goto L_0x0281
        L_0x0280:
            r1 = r10
        L_0x0281:
            r7.append(r1)
            if (r6 == 0) goto L_0x029c
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = " password: "
            r1.append(r2)
            java.lang.String r2 = r0._hexString(r6)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            goto L_0x029d
        L_0x029c:
            r1 = r10
        L_0x029d:
            r7.append(r1)
            java.lang.String r1 = r7.toString()
            r3.fine(r1)
            goto L_0x00d0
        L_0x02a9:
            java.util.logging.Logger r1 = logger
            java.util.logging.Level r3 = java.util.logging.Level.WARNING
            r1.log(r3, r2)
            goto L_0x03c0
        L_0x02b2:
            java.util.logging.Logger r1 = logger
            java.util.logging.Level r3 = java.util.logging.Level.WARNING
            r1.log(r3, r2)
            goto L_0x03c0
        L_0x02bb:
            java.util.logging.Logger r2 = logger
            java.util.logging.Level r4 = java.util.logging.Level.WARNING
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "There was an OPT answer. Wrong version number: "
            r5.append(r6)
            r5.append(r3)
            java.lang.String r3 = " result code: "
            r5.append(r3)
            r5.append(r1)
            java.lang.String r1 = r5.toString()
            r2.log(r4, r1)
            goto L_0x03c0
        L_0x02dd:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            java.lang.String r2 = r2.readUTF(r4)
            r1.append(r2)
            java.lang.String r2 = " "
            int r2 = r1.indexOf(r2)
            if (r2 <= 0) goto L_0x02f8
            java.lang.String r4 = r1.substring(r12, r2)
            goto L_0x02fc
        L_0x02f8:
            java.lang.String r4 = r1.toString()
        L_0x02fc:
            java.lang.String r9 = r4.trim()
            if (r2 <= 0) goto L_0x0307
            int r2 = r2 + r5
            java.lang.String r10 = r1.substring(r2)
        L_0x0307:
            java.lang.String r1 = r10.trim()
            javax.jmdns.impl.DNSRecord$HostInformation r10 = new javax.jmdns.impl.DNSRecord$HostInformation
            r2 = r10
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r9
            r8 = r1
            r2.<init>(r3, r4, r5, r6, r7, r8)
            r1 = r10
            goto L_0x03c1
        L_0x0319:
            javax.jmdns.impl.DNSIncoming$MessageInputStream r1 = r0._messageInputStream
            int r1 = r1.readUnsignedShort()
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            int r9 = r2.readUnsignedShort()
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            int r10 = r2.readUnsignedShort()
            boolean r2 = USE_DOMAIN_NAME_FORMAT_FOR_SRV_TARGET
            if (r2 == 0) goto L_0x0336
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            java.lang.String r2 = r2.readName()
            goto L_0x033c
        L_0x0336:
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            java.lang.String r2 = r2.readNonNameString()
        L_0x033c:
            r11 = r2
            javax.jmdns.impl.DNSRecord$Service r12 = new javax.jmdns.impl.DNSRecord$Service
            r2 = r12
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r1
            r8 = r9
            r9 = r10
            r10 = r11
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10)
            r1 = r12
            goto L_0x03c1
        L_0x034d:
            javax.jmdns.impl.DNSRecord$Text r1 = new javax.jmdns.impl.DNSRecord$Text
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            byte[] r9 = r2.readBytes(r4)
            r2 = r1
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            goto L_0x03c1
        L_0x035f:
            javax.jmdns.impl.DNSIncoming$MessageInputStream r1 = r0._messageInputStream
            java.lang.String r1 = r1.readName()
            int r2 = r1.length()
            if (r2 <= 0) goto L_0x0377
            javax.jmdns.impl.DNSRecord$Pointer r9 = new javax.jmdns.impl.DNSRecord$Pointer
            r2 = r9
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r1
            r2.<init>(r3, r4, r5, r6, r7)
            r1 = r9
            goto L_0x03c1
        L_0x0377:
            java.util.logging.Logger r1 = logger
            java.util.logging.Level r2 = java.util.logging.Level.WARNING
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "PTR record of class: "
            r4.append(r5)
            r4.append(r6)
            java.lang.String r5 = ", there was a problem reading the service name of the answer for domain:"
            r4.append(r5)
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            r1.log(r2, r3)
            goto L_0x03c0
        L_0x0398:
            javax.jmdns.impl.DNSRecord$IPv6Address r1 = new javax.jmdns.impl.DNSRecord$IPv6Address
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            byte[] r9 = r2.readBytes(r4)
            r2 = r1
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            goto L_0x03c1
        L_0x03a9:
            javax.jmdns.impl.DNSRecord$IPv4Address r1 = new javax.jmdns.impl.DNSRecord$IPv4Address
            javax.jmdns.impl.DNSIncoming$MessageInputStream r2 = r0._messageInputStream
            byte[] r9 = r2.readBytes(r4)
            r2 = r1
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            goto L_0x03c1
        L_0x03ba:
            javax.jmdns.impl.DNSIncoming$MessageInputStream r1 = r0._messageInputStream
            long r2 = (long) r4
            r1.skip(r2)
        L_0x03c0:
            r1 = 0
        L_0x03c1:
            if (r1 == 0) goto L_0x03c8
            r2 = r19
            r1.setRecordSource(r2)
        L_0x03c8:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.jmdns.impl.DNSIncoming.readAnswer(java.net.InetAddress):javax.jmdns.impl.DNSRecord");
    }

    /* access modifiers changed from: 0000 */
    public String print(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(print());
        if (z) {
            byte[] bArr = new byte[this._packet.getLength()];
            System.arraycopy(this._packet.getData(), 0, bArr, 0, bArr.length);
            sb.append(print(bArr));
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isQuery() ? "dns[query," : "dns[response,");
        if (this._packet.getAddress() != null) {
            sb.append(this._packet.getAddress().getHostAddress());
        }
        sb.append(':');
        sb.append(this._packet.getPort());
        sb.append(", length=");
        sb.append(this._packet.getLength());
        sb.append(", id=0x");
        sb.append(Integer.toHexString(getId()));
        if (getFlags() != 0) {
            sb.append(", flags=0x");
            sb.append(Integer.toHexString(getFlags()));
            if ((getFlags() & 32768) != 0) {
                sb.append(":r");
            }
            if ((getFlags() & 1024) != 0) {
                sb.append(":aa");
            }
            if ((getFlags() & 512) != 0) {
                sb.append(":tc");
            }
        }
        if (getNumberOfQuestions() > 0) {
            sb.append(", questions=");
            sb.append(getNumberOfQuestions());
        }
        if (getNumberOfAnswers() > 0) {
            sb.append(", answers=");
            sb.append(getNumberOfAnswers());
        }
        if (getNumberOfAuthorities() > 0) {
            sb.append(", authorities=");
            sb.append(getNumberOfAuthorities());
        }
        if (getNumberOfAdditionals() > 0) {
            sb.append(", additionals=");
            sb.append(getNumberOfAdditionals());
        }
        String str = "\n\t";
        if (getNumberOfQuestions() > 0) {
            sb.append("\nquestions:");
            for (DNSQuestion dNSQuestion : this._questions) {
                sb.append(str);
                sb.append(dNSQuestion);
            }
        }
        if (getNumberOfAnswers() > 0) {
            sb.append("\nanswers:");
            for (DNSRecord dNSRecord : this._answers) {
                sb.append(str);
                sb.append(dNSRecord);
            }
        }
        if (getNumberOfAuthorities() > 0) {
            sb.append("\nauthorities:");
            for (DNSRecord dNSRecord2 : this._authoritativeAnswers) {
                sb.append(str);
                sb.append(dNSRecord2);
            }
        }
        if (getNumberOfAdditionals() > 0) {
            sb.append("\nadditionals:");
            for (DNSRecord dNSRecord3 : this._additionals) {
                sb.append(str);
                sb.append(dNSRecord3);
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public void append(DNSIncoming dNSIncoming) {
        if (!isQuery() || !isTruncated() || !dNSIncoming.isQuery()) {
            throw new IllegalArgumentException();
        }
        this._questions.addAll(dNSIncoming.getQuestions());
        this._answers.addAll(dNSIncoming.getAnswers());
        this._authoritativeAnswers.addAll(dNSIncoming.getAuthorities());
        this._additionals.addAll(dNSIncoming.getAdditionals());
    }

    public int elapseSinceArrival() {
        return (int) (System.currentTimeMillis() - this._receivedTime);
    }

    public int getSenderUDPPayload() {
        return this._senderUDPPayload;
    }

    private String _hexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            byte b2 = b & 255;
            sb.append(_nibbleToHex[b2 / TIOADEoadDefinitions.TI_OAD_CONTROL_POINT_CMD_DEVICE_TYPE_CMD]);
            sb.append(_nibbleToHex[b2 % TIOADEoadDefinitions.TI_OAD_CONTROL_POINT_CMD_DEVICE_TYPE_CMD]);
        }
        return sb.toString();
    }
}
