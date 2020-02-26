package com.google.android.gms.internal;

import com.p004ti.ti_oad.TIOADEoadDefinitions;
import java.io.IOException;
import org.apache.http.protocol.HTTP;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

public final class zzrw {
    private final byte[] buffer;
    private int zzbia;
    private int zzbib;
    private int zzbic;
    private int zzbid;
    private int zzbie;
    private int zzbif = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    private int zzbig;
    private int zzbih = 64;
    private int zzbii = 67108864;

    private zzrw(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzbia = i;
        this.zzbib = i2 + i;
        this.zzbid = i;
    }

    public static zzrw zzB(byte[] bArr) {
        return zza(bArr, 0, bArr.length);
    }

    private void zzFz() {
        this.zzbib += this.zzbic;
        int i = this.zzbib;
        int i2 = this.zzbif;
        if (i > i2) {
            this.zzbic = i - i2;
            this.zzbib = i - this.zzbic;
            return;
        }
        this.zzbic = 0;
    }

    public static long zzX(long j) {
        return (-(j & 1)) ^ (j >>> 1);
    }

    public static zzrw zza(byte[] bArr, int i, int i2) {
        return new zzrw(bArr, i, i2);
    }

    public static int zzlB(int i) {
        return (-(i & 1)) ^ (i >>> 1);
    }

    public int getPosition() {
        return this.zzbid - this.zzbia;
    }

    public byte[] readBytes() throws IOException {
        int zzFv = zzFv();
        int i = this.zzbib;
        int i2 = this.zzbid;
        if (zzFv > i - i2 || zzFv <= 0) {
            return zzFv == 0 ? zzsh.zzbiE : zzlF(zzFv);
        }
        byte[] bArr = new byte[zzFv];
        System.arraycopy(this.buffer, i2, bArr, 0, zzFv);
        this.zzbid += zzFv;
        return bArr;
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(zzFy());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(zzFx());
    }

    public String readString() throws IOException {
        int zzFv = zzFv();
        int i = this.zzbib;
        int i2 = this.zzbid;
        int i3 = i - i2;
        String str = HTTP.UTF_8;
        if (zzFv > i3 || zzFv <= 0) {
            return new String(zzlF(zzFv), str);
        }
        String str2 = new String(this.buffer, i2, zzFv, str);
        this.zzbid += zzFv;
        return str2;
    }

    public int zzFA() {
        int i = this.zzbif;
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return i - this.zzbid;
    }

    public boolean zzFB() {
        return this.zzbid == this.zzbib;
    }

    public byte zzFC() throws IOException {
        int i = this.zzbid;
        if (i != this.zzbib) {
            byte[] bArr = this.buffer;
            this.zzbid = i + 1;
            return bArr[i];
        }
        throw zzsd.zzFJ();
    }

    public int zzFo() throws IOException {
        if (zzFB()) {
            this.zzbie = 0;
            return 0;
        }
        this.zzbie = zzFv();
        int i = this.zzbie;
        if (i != 0) {
            return i;
        }
        throw zzsd.zzFM();
    }

    public void zzFp() throws IOException {
        int zzFo;
        do {
            zzFo = zzFo();
            if (zzFo == 0) {
                return;
            }
        } while (zzlA(zzFo));
    }

    public long zzFq() throws IOException {
        return zzFw();
    }

    public int zzFr() throws IOException {
        return zzFv();
    }

    public boolean zzFs() throws IOException {
        return zzFv() != 0;
    }

    public int zzFt() throws IOException {
        return zzlB(zzFv());
    }

    public long zzFu() throws IOException {
        return zzX(zzFw());
    }

    public int zzFv() throws IOException {
        byte b;
        int i;
        byte zzFC = zzFC();
        if (zzFC >= 0) {
            return zzFC;
        }
        byte b2 = zzFC & Byte.MAX_VALUE;
        byte zzFC2 = zzFC();
        if (zzFC2 >= 0) {
            i = zzFC2 << 7;
        } else {
            b2 |= (zzFC2 & Byte.MAX_VALUE) << 7;
            byte zzFC3 = zzFC();
            if (zzFC3 >= 0) {
                i = zzFC3 << MqttWireMessage.MESSAGE_TYPE_DISCONNECT;
            } else {
                b2 |= (zzFC3 & Byte.MAX_VALUE) << MqttWireMessage.MESSAGE_TYPE_DISCONNECT;
                byte zzFC4 = zzFC();
                if (zzFC4 >= 0) {
                    i = zzFC4 << 21;
                } else {
                    byte b3 = b2 | ((zzFC4 & Byte.MAX_VALUE) << 21);
                    byte zzFC5 = zzFC();
                    b = b3 | (zzFC5 << 28);
                    if (zzFC5 < 0) {
                        for (int i2 = 0; i2 < 5; i2++) {
                            if (zzFC() >= 0) {
                                return b;
                            }
                        }
                        throw zzsd.zzFL();
                    }
                    return b;
                }
            }
        }
        b = b2 | i;
        return b;
    }

    public long zzFw() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte zzFC = zzFC();
            j |= ((long) (zzFC & Byte.MAX_VALUE)) << i;
            if ((zzFC & 128) == 0) {
                return j;
            }
        }
        throw zzsd.zzFL();
    }

    public int zzFx() throws IOException {
        return (zzFC() & 255) | ((zzFC() & 255) << 8) | ((zzFC() & 255) << TIOADEoadDefinitions.TI_OAD_CONTROL_POINT_CMD_DEVICE_TYPE_CMD) | ((zzFC() & 255) << 24);
    }

    public long zzFy() throws IOException {
        byte zzFC = zzFC();
        byte zzFC2 = zzFC();
        return ((((long) zzFC2) & 255) << 8) | (((long) zzFC) & 255) | ((((long) zzFC()) & 255) << 16) | ((((long) zzFC()) & 255) << 24) | ((((long) zzFC()) & 255) << 32) | ((((long) zzFC()) & 255) << 40) | ((((long) zzFC()) & 255) << 48) | ((((long) zzFC()) & 255) << 56);
    }

    public void zza(zzse zzse) throws IOException {
        int zzFv = zzFv();
        if (this.zzbig < this.zzbih) {
            int zzlC = zzlC(zzFv);
            this.zzbig++;
            zzse.zzb(this);
            zzlz(0);
            this.zzbig--;
            zzlD(zzlC);
            return;
        }
        throw zzsd.zzFP();
    }

    public void zza(zzse zzse, int i) throws IOException {
        int i2 = this.zzbig;
        if (i2 < this.zzbih) {
            this.zzbig = i2 + 1;
            zzse.zzb(this);
            zzlz(zzsh.zzD(i, 4));
            this.zzbig--;
            return;
        }
        throw zzsd.zzFP();
    }

    public boolean zzlA(int i) throws IOException {
        int zzlU = zzsh.zzlU(i);
        if (zzlU == 0) {
            zzFr();
            return true;
        } else if (zzlU == 1) {
            zzFy();
            return true;
        } else if (zzlU == 2) {
            zzlG(zzFv());
            return true;
        } else if (zzlU == 3) {
            zzFp();
            zzlz(zzsh.zzD(zzsh.zzlV(i), 4));
            return true;
        } else if (zzlU == 4) {
            return false;
        } else {
            if (zzlU == 5) {
                zzFx();
                return true;
            }
            throw zzsd.zzFO();
        }
    }

    public int zzlC(int i) throws zzsd {
        if (i >= 0) {
            int i2 = i + this.zzbid;
            int i3 = this.zzbif;
            if (i2 <= i3) {
                this.zzbif = i2;
                zzFz();
                return i3;
            }
            throw zzsd.zzFJ();
        }
        throw zzsd.zzFK();
    }

    public void zzlD(int i) {
        this.zzbif = i;
        zzFz();
    }

    public void zzlE(int i) {
        int i2 = this.zzbid;
        int i3 = this.zzbia;
        if (i > i2 - i3) {
            StringBuilder sb = new StringBuilder();
            sb.append("Position ");
            sb.append(i);
            sb.append(" is beyond current ");
            sb.append(this.zzbid - this.zzbia);
            throw new IllegalArgumentException(sb.toString());
        } else if (i >= 0) {
            this.zzbid = i3 + i;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Bad position ");
            sb2.append(i);
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public byte[] zzlF(int i) throws IOException {
        if (i >= 0) {
            int i2 = this.zzbid;
            int i3 = i2 + i;
            int i4 = this.zzbif;
            if (i3 > i4) {
                zzlG(i4 - i2);
                throw zzsd.zzFJ();
            } else if (i <= this.zzbib - i2) {
                byte[] bArr = new byte[i];
                System.arraycopy(this.buffer, i2, bArr, 0, i);
                this.zzbid += i;
                return bArr;
            } else {
                throw zzsd.zzFJ();
            }
        } else {
            throw zzsd.zzFK();
        }
    }

    public void zzlG(int i) throws IOException {
        if (i >= 0) {
            int i2 = this.zzbid;
            int i3 = i2 + i;
            int i4 = this.zzbif;
            if (i3 > i4) {
                zzlG(i4 - i2);
                throw zzsd.zzFJ();
            } else if (i <= this.zzbib - i2) {
                this.zzbid = i2 + i;
            } else {
                throw zzsd.zzFJ();
            }
        } else {
            throw zzsd.zzFK();
        }
    }

    public void zzlz(int i) throws zzsd {
        if (this.zzbie != i) {
            throw zzsd.zzFN();
        }
    }

    public byte[] zzx(int i, int i2) {
        if (i2 == 0) {
            return zzsh.zzbiE;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buffer, this.zzbia + i, bArr, 0, i2);
        return bArr;
    }
}
