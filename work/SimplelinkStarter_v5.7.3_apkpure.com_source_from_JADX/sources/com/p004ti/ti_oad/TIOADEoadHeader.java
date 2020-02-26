package com.p004ti.ti_oad;

import android.util.Log;
import java.util.ArrayList;

/* renamed from: com.ti.ti_oad.TIOADEoadHeader */
public class TIOADEoadHeader {
    public static final String TAG = TIOADEoadHeader.class.getSimpleName();
    public byte TIOADEoadBIMVersion;
    public long TIOADEoadImageCRC32;
    public long TIOADEoadImageEndAddress;
    public int TIOADEoadImageHeaderLength;
    public byte TIOADEoadImageHeaderVersion;
    public byte[] TIOADEoadImageIdentificationValue = new byte[8];
    public byte[] TIOADEoadImageInformation = new byte[4];
    public long TIOADEoadImageLength;
    public byte[] TIOADEoadImageSoftwareVersion = new byte[4];
    public long TIOADEoadImageValidation;
    public int TIOADEoadImageWirelessTechnology;
    public long TIOADEoadProgramEntryAddress;
    public int TIOADEoadReserved;
    public byte[] rawData;
    public ArrayList<TIOADEoadSegmentInformation> segments;

    /* renamed from: com.ti.ti_oad.TIOADEoadHeader$TIOADEoadBoundaryInformation */
    public static class TIOADEoadBoundaryInformation extends TIOADEoadSegmentInformation {
        public long TIOADBoundaryIcallStack0Address;
        public long TIOADBoundaryRamEndAddress;
        public long TIOADBoundaryRamStartAddress;
        public long TIOADBoundaryStackEntryAddress;

        public boolean isBoundary() {
            return true;
        }
    }

    /* renamed from: com.ti.ti_oad.TIOADEoadHeader$TIOADEoadContiguosImageInformation */
    public static class TIOADEoadContiguosImageInformation extends TIOADEoadSegmentInformation {
        public long TIOADStackEntryAddress;

        public boolean isContigous() {
            return true;
        }
    }

    /* renamed from: com.ti.ti_oad.TIOADEoadHeader$TIOADEoadSegmentInformation */
    public static class TIOADEoadSegmentInformation {
        public byte[] SegmentData;
        public long TIOADPayloadLength;
        public byte TIOADReserved;
        public byte TIOADSegmentType;
        public int TIOADWirelessTechnology;

        public boolean isBoundary() {
            return false;
        }

        public boolean isContigous() {
            return false;
        }

        public void printSegmentInformation(TIOADEoadSegmentInformation tIOADEoadSegmentInformation) {
            Log.d(TIOADEoadHeader.TAG, "Segment information :");
            String str = TIOADEoadHeader.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Segment Type: ");
            sb.append(tIOADEoadSegmentInformation.TIOADSegmentType);
            sb.append(" (");
            byte b = tIOADEoadSegmentInformation.TIOADSegmentType;
            String str2 = b == 0 ? "Boundary Info" : b == 1 ? "Contiguous Info" : "Unknown Type";
            sb.append(str2);
            sb.append(") ");
            Log.d(str, sb.toString());
            String str3 = TIOADEoadHeader.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Segment Wireless Standard: ");
            sb2.append(TIOADEoadHeader.WirelessStdToString(tIOADEoadSegmentInformation.TIOADWirelessTechnology));
            Log.d(str3, sb2.toString());
            String str4 = "Stack Entry Address (32-bit): ";
            if (tIOADEoadSegmentInformation.isBoundary()) {
                TIOADEoadBoundaryInformation tIOADEoadBoundaryInformation = (TIOADEoadBoundaryInformation) tIOADEoadSegmentInformation;
                String str5 = TIOADEoadHeader.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str4);
                String str6 = "0x%08x";
                sb3.append(String.format(str6, new Object[]{Long.valueOf(tIOADEoadBoundaryInformation.TIOADBoundaryStackEntryAddress)}));
                Log.d(str5, sb3.toString());
                String str7 = TIOADEoadHeader.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("ICall Stack 0 Address (32-bit): ");
                sb4.append(String.format(str6, new Object[]{Long.valueOf(tIOADEoadBoundaryInformation.TIOADBoundaryIcallStack0Address)}));
                Log.d(str7, sb4.toString());
                String str8 = TIOADEoadHeader.TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("RAM Start Address (32-bit): ");
                sb5.append(String.format(str6, new Object[]{Long.valueOf(tIOADEoadBoundaryInformation.TIOADBoundaryRamStartAddress)}));
                Log.d(str8, sb5.toString());
                String str9 = TIOADEoadHeader.TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("RAM End Address (32-bit): ");
                sb6.append(String.format(str6, new Object[]{Long.valueOf(tIOADEoadBoundaryInformation.TIOADBoundaryRamEndAddress)}));
                Log.d(str9, sb6.toString());
            } else if (tIOADEoadSegmentInformation.isContigous()) {
                TIOADEoadContiguosImageInformation tIOADEoadContiguosImageInformation = (TIOADEoadContiguosImageInformation) tIOADEoadSegmentInformation;
                Log.d(TIOADEoadHeader.TAG, "Contiguous image information :");
                String str10 = TIOADEoadHeader.TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str4);
                sb7.append(String.format("0x%8x", new Object[]{Long.valueOf(tIOADEoadContiguosImageInformation.TIOADStackEntryAddress)}));
                Log.d(str10, sb7.toString());
            }
        }
    }

    public TIOADEoadHeader(byte[] bArr) {
        this.rawData = bArr;
    }

    public boolean validateImage() {
        byte[] bArr = this.rawData;
        if (bArr == null) {
            return false;
        }
        byte[] bArr2 = this.TIOADEoadImageIdentificationValue;
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        byte[] bArr3 = this.rawData;
        this.TIOADEoadImageCRC32 = TIOADEoadDefinitions.BUILD_UINT32(bArr3[11], bArr3[10], bArr3[9], bArr3[8]);
        byte[] bArr4 = this.rawData;
        this.TIOADEoadBIMVersion = bArr4[12];
        this.TIOADEoadImageHeaderVersion = bArr4[13];
        this.TIOADEoadImageWirelessTechnology = TIOADEoadDefinitions.BUILD_UINT16(bArr4[15], bArr4[14]);
        byte[] bArr5 = this.rawData;
        byte[] bArr6 = this.TIOADEoadImageInformation;
        System.arraycopy(bArr5, 16, bArr6, 0, bArr6.length);
        byte[] bArr7 = this.rawData;
        this.TIOADEoadImageValidation = TIOADEoadDefinitions.BUILD_UINT32(bArr7[23], bArr7[22], bArr7[21], bArr7[20]);
        byte[] bArr8 = this.rawData;
        this.TIOADEoadImageLength = TIOADEoadDefinitions.BUILD_UINT32(bArr8[27], bArr8[26], bArr8[25], bArr8[24]);
        byte[] bArr9 = this.rawData;
        this.TIOADEoadProgramEntryAddress = TIOADEoadDefinitions.BUILD_UINT32(bArr9[31], bArr9[30], bArr9[29], bArr9[28]);
        byte[] bArr10 = this.rawData;
        byte[] bArr11 = this.TIOADEoadImageSoftwareVersion;
        System.arraycopy(bArr10, 32, bArr11, 0, bArr11.length);
        byte[] bArr12 = this.rawData;
        this.TIOADEoadImageEndAddress = TIOADEoadDefinitions.BUILD_UINT32(bArr12[39], bArr12[38], bArr12[37], bArr12[36]);
        byte[] bArr13 = this.rawData;
        this.TIOADEoadImageHeaderLength = TIOADEoadDefinitions.BUILD_UINT16(bArr13[41], bArr13[40]);
        byte[] bArr14 = this.rawData;
        this.TIOADEoadReserved = TIOADEoadDefinitions.BUILD_UINT16(bArr14[43], bArr14[42]);
        this.segments = new ArrayList<>();
        TIOADEoadSegmentInformation tIOADEoadSegmentInformation = new TIOADEoadSegmentInformation();
        byte[] bArr15 = this.rawData;
        tIOADEoadSegmentInformation.TIOADSegmentType = bArr15[44];
        tIOADEoadSegmentInformation.TIOADWirelessTechnology = TIOADEoadDefinitions.BUILD_UINT16(bArr15[46], bArr15[45]);
        byte[] bArr16 = this.rawData;
        tIOADEoadSegmentInformation.TIOADReserved = bArr16[47];
        tIOADEoadSegmentInformation.TIOADPayloadLength = TIOADEoadDefinitions.BUILD_UINT32(bArr16[51], bArr16[50], bArr16[49], bArr16[48]);
        byte b = tIOADEoadSegmentInformation.TIOADSegmentType;
        if (b == 0) {
            TIOADEoadBoundaryInformation tIOADEoadBoundaryInformation = new TIOADEoadBoundaryInformation();
            tIOADEoadBoundaryInformation.TIOADSegmentType = tIOADEoadSegmentInformation.TIOADSegmentType;
            tIOADEoadBoundaryInformation.TIOADWirelessTechnology = tIOADEoadSegmentInformation.TIOADWirelessTechnology;
            tIOADEoadBoundaryInformation.TIOADReserved = tIOADEoadSegmentInformation.TIOADReserved;
            tIOADEoadBoundaryInformation.TIOADPayloadLength = tIOADEoadSegmentInformation.TIOADPayloadLength;
            addBoundaryInformation(tIOADEoadBoundaryInformation, this.rawData, 52);
        } else if (b == 1) {
            TIOADEoadContiguosImageInformation tIOADEoadContiguosImageInformation = new TIOADEoadContiguosImageInformation();
            tIOADEoadContiguosImageInformation.TIOADSegmentType = tIOADEoadSegmentInformation.TIOADSegmentType;
            tIOADEoadContiguosImageInformation.TIOADWirelessTechnology = tIOADEoadSegmentInformation.TIOADWirelessTechnology;
            tIOADEoadContiguosImageInformation.TIOADReserved = tIOADEoadSegmentInformation.TIOADReserved;
            tIOADEoadContiguosImageInformation.TIOADPayloadLength = tIOADEoadSegmentInformation.TIOADPayloadLength;
            addContigousInformation(tIOADEoadContiguosImageInformation, this.rawData, 52);
        }
        this.segments.add(tIOADEoadSegmentInformation);
        return true;
    }

    public void printHeader(TIOADEoadHeader tIOADEoadHeader) {
        TIOADEoadHeader tIOADEoadHeader2 = tIOADEoadHeader;
        Log.d(TAG, "Enhanced OAD Header");
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        String str2 = "Image Information : ";
        sb.append(str2);
        sb.append(String.format("%c,%c,%c,%c,%c,%c,%c,%c", new Object[]{Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[0]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[1]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[2]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[3]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[4]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[5]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[6]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[7])}));
        Log.d(str, sb.toString());
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Image CRC32 : ");
        String str4 = "0x%08X";
        sb2.append(String.format(str4, new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadImageCRC32)}));
        Log.d(str3, sb2.toString());
        String str5 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Image BIM version : ");
        sb3.append(tIOADEoadHeader2.TIOADEoadBIMVersion);
        Log.d(str5, sb3.toString());
        String str6 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Image Image Header Version : ");
        sb4.append(tIOADEoadHeader2.TIOADEoadImageHeaderVersion);
        Log.d(str6, sb4.toString());
        String str7 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Image Wireless Standard : ");
        sb5.append(WirelessStdToString(tIOADEoadHeader2.TIOADEoadImageWirelessTechnology));
        Log.d(str7, sb5.toString());
        String str8 = TAG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(str2);
        sb6.append(String.format("%d(0x%02x),%d(0x%02x),%d(0x%02x),%d(0x%02x)", new Object[]{Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[0]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[0]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[1]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[1]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[2]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[2]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[3]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[3])}));
        Log.d(str8, sb6.toString());
        String str9 = TAG;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("Image Validation : ");
        sb7.append(String.format("%d(0x%08X)", new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadImageValidation), Long.valueOf(tIOADEoadHeader2.TIOADEoadImageValidation)}));
        Log.d(str9, sb7.toString());
        String str10 = TAG;
        StringBuilder sb8 = new StringBuilder();
        sb8.append("Image Length : ");
        String str11 = "%d(0x%08X) Bytes";
        sb8.append(String.format(str11, new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadImageLength), Long.valueOf(tIOADEoadHeader2.TIOADEoadImageLength)}));
        Log.d(str10, sb8.toString());
        String str12 = TAG;
        StringBuilder sb9 = new StringBuilder();
        sb9.append("Program Entry Address : ");
        sb9.append(String.format(str4, new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadProgramEntryAddress)}));
        Log.d(str12, sb9.toString());
        String str13 = TAG;
        StringBuilder sb10 = new StringBuilder();
        sb10.append("Image Software Version : ");
        sb10.append(String.format("%c(0x%02X),%c(0x%02X),%c(0x%02X),%c(0x%02X)", new Object[]{Byte.valueOf(this.TIOADEoadImageSoftwareVersion[0]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[0]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[1]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[1]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[2]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[2]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[3]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[3])}));
        Log.d(str13, sb10.toString());
        String str14 = TAG;
        StringBuilder sb11 = new StringBuilder();
        sb11.append("Image End Address : ");
        sb11.append(String.format(str4, new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadImageEndAddress)}));
        Log.d(str14, sb11.toString());
        String str15 = TAG;
        StringBuilder sb12 = new StringBuilder();
        sb12.append("Image Header Length : ");
        sb12.append(String.format(str11, new Object[]{Integer.valueOf(tIOADEoadHeader2.TIOADEoadImageHeaderLength), Integer.valueOf(tIOADEoadHeader2.TIOADEoadImageHeaderLength)}));
        Log.d(str15, sb12.toString());
        String str16 = TAG;
        StringBuilder sb13 = new StringBuilder();
        sb13.append("Image Reserved : ");
        sb13.append(String.format("%d(0x%04X)", new Object[]{Integer.valueOf(tIOADEoadHeader2.TIOADEoadReserved), Integer.valueOf(tIOADEoadHeader2.TIOADEoadReserved)}));
        Log.d(str16, sb13.toString());
    }

    public String getHeaderInfo(TIOADEoadHeader tIOADEoadHeader) {
        TIOADEoadHeader tIOADEoadHeader2 = tIOADEoadHeader;
        StringBuilder sb = new StringBuilder();
        sb.append("Enhanced OAD Header\r\n");
        String str = "Image Information : ";
        sb.append(str);
        sb.append(String.format("%c,%c,%c,%c,%c,%c,%c,%c", new Object[]{Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[0]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[1]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[2]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[3]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[4]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[5]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[6]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageIdentificationValue[7])}));
        String str2 = "\r\n";
        sb.append(str2);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("Image CRC32 : ");
        String str3 = "0x%08X";
        sb3.append(String.format(str3, new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadImageCRC32)}));
        sb3.append(str2);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        sb5.append("Image BIM version : ");
        sb5.append(tIOADEoadHeader2.TIOADEoadBIMVersion);
        sb5.append(str2);
        String sb6 = sb5.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(sb6);
        sb7.append("Image Image Header Version : ");
        sb7.append(tIOADEoadHeader2.TIOADEoadImageHeaderVersion);
        sb7.append(str2);
        String sb8 = sb7.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(sb8);
        sb9.append("Image Wireless Standard : ");
        sb9.append(WirelessStdToString(tIOADEoadHeader2.TIOADEoadImageWirelessTechnology));
        sb9.append(str2);
        String sb10 = sb9.toString();
        StringBuilder sb11 = new StringBuilder();
        sb11.append(sb10);
        sb11.append(str);
        sb11.append(String.format("%d(0x%02x),%d(0x%02x),%d(0x%02x),%d(0x%02x)", new Object[]{Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[0]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[0]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[1]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[1]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[2]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[2]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[3]), Byte.valueOf(tIOADEoadHeader2.TIOADEoadImageInformation[3])}));
        sb11.append(str2);
        String sb12 = sb11.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(sb12);
        sb13.append("Image Validation : ");
        sb13.append(String.format("%d(0x%08X)", new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadImageValidation), Long.valueOf(tIOADEoadHeader2.TIOADEoadImageValidation)}));
        sb13.append(str2);
        String sb14 = sb13.toString();
        StringBuilder sb15 = new StringBuilder();
        sb15.append(sb14);
        sb15.append("Image Length : ");
        String str4 = "%d(0x%08X) Bytes";
        sb15.append(String.format(str4, new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadImageLength), Long.valueOf(tIOADEoadHeader2.TIOADEoadImageLength)}));
        sb15.append(str2);
        String sb16 = sb15.toString();
        StringBuilder sb17 = new StringBuilder();
        sb17.append(sb16);
        sb17.append("Program Entry Address : ");
        String str5 = str4;
        sb17.append(String.format(str3, new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadProgramEntryAddress)}));
        sb17.append(str2);
        String sb18 = sb17.toString();
        StringBuilder sb19 = new StringBuilder();
        sb19.append(sb18);
        sb19.append("Image Software Version : ");
        sb19.append(String.format("%c(0x%02X),%c(0x%02X),%c(0x%02X),%c(0x%02X)", new Object[]{Byte.valueOf(this.TIOADEoadImageSoftwareVersion[0]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[0]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[1]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[1]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[2]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[2]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[3]), Byte.valueOf(this.TIOADEoadImageSoftwareVersion[3])}));
        sb19.append(str2);
        String sb20 = sb19.toString();
        StringBuilder sb21 = new StringBuilder();
        sb21.append(sb20);
        sb21.append("Image End Address : ");
        sb21.append(String.format(str3, new Object[]{Long.valueOf(tIOADEoadHeader2.TIOADEoadImageEndAddress)}));
        sb21.append(str2);
        String sb22 = sb21.toString();
        StringBuilder sb23 = new StringBuilder();
        sb23.append(sb22);
        sb23.append("Image Header Length : ");
        sb23.append(String.format(str5, new Object[]{Integer.valueOf(tIOADEoadHeader2.TIOADEoadImageHeaderLength), Integer.valueOf(tIOADEoadHeader2.TIOADEoadImageHeaderLength)}));
        sb23.append(str2);
        String sb24 = sb23.toString();
        StringBuilder sb25 = new StringBuilder();
        sb25.append(sb24);
        sb25.append("Image Reserved : ");
        sb25.append(String.format("%d(0x%04X)", new Object[]{Integer.valueOf(tIOADEoadHeader2.TIOADEoadReserved), Integer.valueOf(tIOADEoadHeader2.TIOADEoadReserved)}));
        sb25.append(str2);
        return sb25.toString();
    }

    /* access modifiers changed from: 0000 */
    public boolean addBoundaryInformation(TIOADEoadBoundaryInformation tIOADEoadBoundaryInformation, byte[] bArr, int i) {
        tIOADEoadBoundaryInformation.TIOADBoundaryStackEntryAddress = TIOADEoadDefinitions.BUILD_UINT32(bArr[i + 3], bArr[i + 2], bArr[i + 1], bArr[i]);
        int i2 = i + 4;
        tIOADEoadBoundaryInformation.TIOADBoundaryIcallStack0Address = TIOADEoadDefinitions.BUILD_UINT32(bArr[i2 + 3], bArr[i2 + 2], bArr[i2 + 1], bArr[i2]);
        int i3 = i2 + 4;
        tIOADEoadBoundaryInformation.TIOADBoundaryRamStartAddress = TIOADEoadDefinitions.BUILD_UINT32(bArr[i3 + 3], bArr[i3 + 2], bArr[i3 + 1], bArr[i3]);
        int i4 = i3 + 4;
        tIOADEoadBoundaryInformation.TIOADBoundaryRamEndAddress = TIOADEoadDefinitions.BUILD_UINT32(bArr[i4 + 3], bArr[i4 + 2], bArr[i4 + 1], bArr[i4]);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean addContigousInformation(TIOADEoadContiguosImageInformation tIOADEoadContiguosImageInformation, byte[] bArr, int i) {
        tIOADEoadContiguosImageInformation.TIOADStackEntryAddress = TIOADEoadDefinitions.BUILD_UINT32(bArr[i + 3], bArr[i + 2], bArr[i + 1], bArr[i]);
        return true;
    }

    public static String WirelessStdToString(int i) {
        StringBuilder sb = new StringBuilder();
        String str = "";
        sb.append(str);
        sb.append((TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_BLE & i) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_BLE ? " BLE " : str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append((TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_RF4CE & i) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_RF4CE ? "RF4CE" : str);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        sb5.append((TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_802_15_4_2_POINT_FOUR & i) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_802_15_4_2_POINT_FOUR ? "802.15.4 (2.4GHz)" : str);
        String sb6 = sb5.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(sb6);
        sb7.append((TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_802_15_4_SUB_ONE & i) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_802_15_4_SUB_ONE ? "802.15.4 (Sub-One)" : str);
        String sb8 = sb7.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(sb8);
        sb9.append((TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_EASY_LINK & i) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_EASY_LINK ? "Easy Link" : str);
        String sb10 = sb9.toString();
        StringBuilder sb11 = new StringBuilder();
        sb11.append(sb10);
        sb11.append((TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_THREAD & i) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_THREAD ? "Thread" : str);
        String sb12 = sb11.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(sb12);
        if ((i & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_ZIGBEE) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_ZIGBEE) {
            str = "ZigBee";
        }
        sb13.append(str);
        return sb13.toString();
    }
}
