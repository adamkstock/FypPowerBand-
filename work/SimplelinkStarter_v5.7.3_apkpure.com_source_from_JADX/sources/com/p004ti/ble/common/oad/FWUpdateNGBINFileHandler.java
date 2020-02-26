package com.p004ti.ble.common.oad;

import android.content.Context;
import android.util.Log;
import com.p004ti.util.bleUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* renamed from: com.ti.ble.common.oad.FWUpdateNGBINFileHandler */
public class FWUpdateNGBINFileHandler {
    public static String TAG = "FWUpdateNGBINFileHandler";
    public binFileHeader header;
    private byte[] mFileBuffer;

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGBINFileHandler$binFileHeader */
    public static class binFileHeader {
        public int BIN_FILE_HEADER_FLASH_PAGE_SIZE = 4096;
        public int BIN_FILE_HEADER_FLASH_WIDTH = 4;
        public int BIN_FILE_HEADER_SIZE = 16;
        public int EFL_OAD_IMG_TYPE_APP = 1;
        public int EFL_OAD_IMG_TYPE_NP = 3;
        public int EFL_OAD_IMG_TYPE_STACK = 2;
        public String TAG = "binFileHeader";
        int addr;
        short crc1;
        short crc2;
        byte[] fileBufferPadded;
        byte imgType;
        int len;
        fwFileLoadStatus loadStatus = fwFileLoadStatus.UNKNOWN;
        byte status;
        byte[] uid = new byte[4];
        int ver;

        /* access modifiers changed from: 0000 */
        public short crc16(short s, byte b) {
            byte b2 = b;
            short s2 = s;
            byte b3 = 0;
            while (b3 < 8) {
                boolean z = (s2 & 32768) == 32768;
                s2 = (short) (s2 << 1);
                if ((b2 & 128) == 128) {
                    s2 = (short) (s2 | 1);
                }
                if (z) {
                    s2 = (short) (s2 ^ 4129);
                }
                b3 = (byte) (b3 + 1);
                b2 = (byte) (b2 << 1);
            }
            return s2;
        }

        public boolean initHeader(byte[] bArr) {
            int i;
            short s;
            if (bArr.length < this.BIN_FILE_HEADER_SIZE) {
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Image too short to be a firmware file : ");
                sb.append(bArr.length);
                Log.d(str, sb.toString());
                return false;
            }
            this.crc1 = (short) ((bArr[1] & 255) << 8);
            this.crc1 = (short) (this.crc1 | ((short) (bArr[0] & 255)));
            this.crc2 = (short) ((bArr[3] & 255) << 8);
            this.crc2 = (short) (this.crc2 | ((short) (bArr[2] & 255)));
            this.ver = (bArr[5] & 255) << 8;
            this.ver |= bArr[4] & 255;
            this.len = (bArr[7] & 255) << 8;
            this.len |= bArr[6] & 255;
            for (int i2 = 0; i2 < 4; i2++) {
                this.uid[i2] = bArr[i2 + 8];
            }
            this.addr = (bArr[13] & 255) << 8;
            this.addr |= bArr[12] & 255;
            this.imgType = bArr[14];
            this.status = bArr[15];
            int i3 = this.len;
            if (i3 == 0) {
                String str2 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Length from header : ");
                sb2.append(this.len);
                sb2.append(" bytes, this image does not contain a header !");
                Log.d(str2, sb2.toString());
                this.loadStatus = fwFileLoadStatus.HEADERLESS_IMG;
                this.fileBufferPadded = new byte[bArr.length];
                System.arraycopy(bArr, 0, this.fileBufferPadded, 0, bArr.length);
                return false;
            }
            int i4 = this.BIN_FILE_HEADER_FLASH_WIDTH;
            this.fileBufferPadded = new byte[(i3 * i4)];
            System.arraycopy(bArr, 0, this.fileBufferPadded, 0, i3 * i4);
            if (bArr.length > this.len * this.BIN_FILE_HEADER_FLASH_WIDTH) {
                int length = bArr.length;
                i = 0;
                while (length % 16 != 0) {
                    length++;
                    i++;
                }
                String str3 = this.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Padding file with ");
                sb3.append(i);
                sb3.append(" bytes of value = 0xff !");
                Log.d(str3, sb3.toString());
                this.fileBufferPadded = new byte[length];
                System.arraycopy(bArr, 0, this.fileBufferPadded, 0, bArr.length);
                int length2 = bArr.length;
                while (true) {
                    byte[] bArr2 = this.fileBufferPadded;
                    if (length2 >= bArr2.length) {
                        break;
                    }
                    bArr2[length2] = -1;
                    length2++;
                }
            } else {
                i = 0;
            }
            String str4 = " Header crc1 ";
            String str5 = "Calculated CRC : ";
            String str6 = "0x%04x";
            if (this.status == -1) {
                s = calcImageCRC(0, this.fileBufferPadded);
                String str7 = this.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str5);
                sb4.append(String.format(str6, new Object[]{Short.valueOf(s)}));
                sb4.append(str4);
                sb4.append(String.format(str6, new Object[]{Short.valueOf(this.crc1)}));
                Log.d(str7, sb4.toString());
            } else if (i != 0) {
                byte[] bArr3 = this.fileBufferPadded;
                byte[] bArr4 = new byte[(bArr3.length - 16)];
                System.arraycopy(bArr3, 16, bArr4, 0, bArr3.length - 16);
                s = calcImageCRC(0, bArr4);
                String str8 = this.TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str5);
                sb5.append(String.format(str6, new Object[]{Short.valueOf(s)}));
                sb5.append(str4);
                sb5.append(String.format(str6, new Object[]{Short.valueOf(this.crc1)}));
                Log.d(str8, sb5.toString());
            } else {
                byte[] bArr5 = new byte[bArr.length];
                System.arraycopy(bArr, 16, bArr5, 0, bArr.length - 16);
                s = calcImageCRC(0, bArr5);
                String str9 = this.TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str5);
                sb6.append(String.format(str6, new Object[]{Short.valueOf(s)}));
                sb6.append(str4);
                sb6.append(String.format(str6, new Object[]{Short.valueOf(this.crc1)}));
                Log.d(str9, sb6.toString());
            }
            if (s == this.crc1) {
                this.loadStatus = fwFileLoadStatus.LOAD_OK;
                return true;
            }
            this.loadStatus = fwFileLoadStatus.CRC_CHECK_FAILED;
            return false;
        }

        public void printHeaderInfo() {
            Log.d(this.TAG, "BIN File Header Info");
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("CRC1 : ");
            String str2 = "0x%04x";
            sb.append(String.format(str2, new Object[]{Short.valueOf(this.crc1)}));
            Log.d(str, sb.toString());
            String str3 = this.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("CRC2 : ");
            sb2.append(String.format(str2, new Object[]{Short.valueOf(this.crc2)}));
            Log.d(str3, sb2.toString());
            String str4 = this.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Length : ");
            sb3.append(this.len);
            String str5 = " (0x%04x)";
            sb3.append(String.format(str5, new Object[]{Integer.valueOf(this.len)}));
            Log.d(str4, sb3.toString());
            String str6 = this.TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("UID : ");
            sb4.append(bleUtility.getStringFromByteVector(this.uid));
            Log.d(str6, sb4.toString());
            String str7 = this.TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Address : ");
            sb5.append(String.format(str5, new Object[]{Integer.valueOf(this.addr)}));
            Log.d(str7, sb5.toString());
            String str8 = this.TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Image Type : ");
            sb6.append(this.imgType);
            sb6.append("(");
            Object[] objArr = new Object[1];
            byte b = this.imgType;
            String str9 = b == this.EFL_OAD_IMG_TYPE_APP ? "Application" : b == this.EFL_OAD_IMG_TYPE_STACK ? "Stack" : b == this.EFL_OAD_IMG_TYPE_NP ? "NP" : "Unknown";
            objArr[0] = str9;
            sb6.append(String.format("%s", objArr));
            sb6.append(")");
            Log.d(str8, sb6.toString());
            String str10 = this.TAG;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("Status : ");
            sb7.append(String.format("0x%02x", new Object[]{Byte.valueOf(this.status)}));
            Log.d(str10, sb7.toString());
        }

        /* access modifiers changed from: 0000 */
        public short calcImageCRC(int i, byte[] bArr) {
            long j = (long) (i * 4096);
            byte b = (byte) i;
            int i2 = this.len;
            byte b2 = (byte) (i2 / 1024);
            int i3 = (i2 - (b2 * 1024)) * 4;
            byte b3 = (byte) (b2 + b);
            short s = 0;
            while (true) {
                short s2 = s;
                int i4 = 0;
                while (i4 < 4096) {
                    if (i == b && i4 == 0) {
                        i4 += 3;
                    } else if (i == b3 && i4 == i3) {
                        short crc16 = crc16(crc16(s2, 0), 0);
                        String str = this.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("addr + oset: ");
                        sb.append((int) (j + ((long) i4)));
                        sb.append(" pageEnd ");
                        sb.append(b3);
                        sb.append(" osetEnd ");
                        sb.append(i3);
                        Log.d(str, sb.toString());
                        return crc16;
                    } else {
                        s2 = crc16(s2, bArr[(int) (((long) i4) + j)]);
                    }
                    i4++;
                }
                i++;
                j = (long) (i * 4096);
                s = s2;
            }
        }
    }

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGBINFileHandler$fwFileLoadStatus */
    public enum fwFileLoadStatus {
        LOAD_OK,
        HEADERLESS_IMG,
        CRC_CHECK_FAILED,
        UNKNOWN
    }

    public FWUpdateNGBINFileHandler(String str, boolean z, Context context) {
        InputStream inputStream;
        if (z) {
            try {
                inputStream = context.getAssets().open(str);
            } catch (IOException unused) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("File open failed for path ");
                sb.append(str);
                Log.d(str2, sb.toString());
            }
        } else {
            inputStream = new FileInputStream(new File(str));
        }
        this.mFileBuffer = new byte[inputStream.available()];
        inputStream.read(this.mFileBuffer, 0, this.mFileBuffer.length);
        inputStream.close();
        if (checkFile()) {
            Log.d(TAG, "Found header and checked CRC OK !");
        } else {
            Log.d(TAG, "CRC failed, or header invalid, image can still be programmed, but be carefull");
        }
    }

    public boolean checkFile() {
        this.header = new binFileHeader();
        if (!this.header.initHeader(this.mFileBuffer)) {
            return false;
        }
        this.header.printHeaderInfo();
        return true;
    }
}
