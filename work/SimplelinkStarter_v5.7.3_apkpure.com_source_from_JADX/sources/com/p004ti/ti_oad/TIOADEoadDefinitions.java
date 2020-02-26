package com.p004ti.ti_oad;

import android.support.p000v4.view.MotionEventCompat;
import java.util.Formatter;

/* renamed from: com.ti.ti_oad.TIOADEoadDefinitions */
public class TIOADEoadDefinitions {
    public static final byte TI_OAD_CONTROL_POINT_CMD_CANCEL_OAD = 5;
    public static final byte TI_OAD_CONTROL_POINT_CMD_DEVICE_TYPE_CMD = 16;
    public static final byte TI_OAD_CONTROL_POINT_CMD_ENABLE_OAD_IMAGE = 4;
    public static final byte TI_OAD_CONTROL_POINT_CMD_GET_BLOCK_SIZE = 1;
    public static final byte TI_OAD_CONTROL_POINT_CMD_IMAGE_BLOCK_WRITE_CHAR_RESPONSE = 18;
    public static final byte TI_OAD_CONTROL_POINT_CMD_START_OAD_PROCESS = 3;
    public static int TI_OAD_EOAD_IMAGE_COPY_STATUS_IMAGE_COPIED = 252;
    public static int TI_OAD_EOAD_IMAGE_COPY_STATUS_IMAGE_TO_BE_COPIED = 254;
    public static int TI_OAD_EOAD_IMAGE_COPY_STATUS_NO_ACTION_NEEDED = 255;
    public static int TI_OAD_EOAD_IMAGE_CRC_STATUS_INVALID = 0;
    public static int TI_OAD_EOAD_IMAGE_CRC_STATUS_NOT_CALCULATED_YET = 3;
    public static int TI_OAD_EOAD_IMAGE_CRC_STATUS_VALID = 2;
    public static int TI_OAD_EOAD_IMAGE_HEADER_LEN = 44;
    public static int TI_OAD_EOAD_IMAGE_TYPE_APP = 1;
    public static int TI_OAD_EOAD_IMAGE_TYPE_APP_STACK_MERGED = 3;
    public static int TI_OAD_EOAD_IMAGE_TYPE_BIM = 6;
    public static int TI_OAD_EOAD_IMAGE_TYPE_BLE_FACTORY_IMAGE = 5;
    public static int TI_OAD_EOAD_IMAGE_TYPE_NETWORK_PROCESSOR = 4;
    public static int TI_OAD_EOAD_IMAGE_TYPE_PERSISTANT_APP = 0;
    public static int TI_OAD_EOAD_IMAGE_TYPE_STACK = 2;
    public static int TI_OAD_EOAD_SEGMENT_INFO_LEN = 8;
    public static final byte TI_OAD_EOAD_SEGMENT_TYPE_BOUNDARY_INFO = 0;
    public static final byte TI_OAD_EOAD_SEGMENT_TYPE_CONTIGUOUS_INFO = 1;
    public static int TI_OAD_EOAD_SEGMENT_TYPE_PAYLOAD_LEN_OFF = 4;
    public static int TI_OAD_EOAD_SEGMENT_TYPE_WIRELESS_STD_OFF = 1;
    public static int TI_OAD_EOAD_STATUS_CODE_ALREADY_STARTED = 4;
    public static int TI_OAD_EOAD_STATUS_CODE_AUTH_FAIL = 18;
    public static int TI_OAD_EOAD_STATUS_CODE_BUFFER_OVERFLOW = 3;
    public static int TI_OAD_EOAD_STATUS_CODE_CCCD_NOT_ENABLED = 21;
    public static int TI_OAD_EOAD_STATUS_CODE_CRC_ERR = 1;
    public static int TI_OAD_EOAD_STATUS_CODE_DL_COMPLETE = 20;
    public static int TI_OAD_EOAD_STATUS_CODE_DL_NOT_COMPLETE = 6;
    public static int TI_OAD_EOAD_STATUS_CODE_EXT_NOT_SUPPORTED = 19;
    public static int TI_OAD_EOAD_STATUS_CODE_FLASH_ERR = 2;
    public static int TI_OAD_EOAD_STATUS_CODE_IMAGE_TOO_BIG = 8;
    public static int TI_OAD_EOAD_STATUS_CODE_IMG_ID_TIMEOUT = 22;
    public static int TI_OAD_EOAD_STATUS_CODE_INCOMPATIBLE_FILE = 17;
    public static int TI_OAD_EOAD_STATUS_CODE_INCOMPATIBLE_IMAGE = 9;
    public static int TI_OAD_EOAD_STATUS_CODE_INVALID_FILE = 16;
    public static int TI_OAD_EOAD_STATUS_CODE_NOT_STARTED = 5;
    public static int TI_OAD_EOAD_STATUS_CODE_NO_RESOURCES = 7;
    public static int TI_OAD_EOAD_STATUS_CODE_SUCCESS = 0;
    public static int TI_OAD_EOAD_WIRELESS_STD_802_15_4_2_POINT_FOUR = 4;
    public static int TI_OAD_EOAD_WIRELESS_STD_802_15_4_SUB_ONE = 2;
    public static int TI_OAD_EOAD_WIRELESS_STD_BLE = 1;
    public static int TI_OAD_EOAD_WIRELESS_STD_EASY_LINK = 64;
    public static int TI_OAD_EOAD_WIRELESS_STD_RF4CE = 16;
    public static int TI_OAD_EOAD_WIRELESS_STD_THREAD = 32;
    public static int TI_OAD_EOAD_WIRELESS_STD_ZIGBEE = 8;
    public static String TI_OAD_IMAGE_BLOCK_REQUEST = "f000ffc2-0451-4000-b000-000000000000";
    public static String TI_OAD_IMAGE_CONTROL = "f000ffc5-0451-4000-b000-000000000000";
    public static String TI_OAD_IMAGE_COUNT = "f000ffc3-0451-4000-b000-000000000000";
    public static byte TI_OAD_IMAGE_IDENTIFY_PACKAGE_LEN = 22;
    public static String TI_OAD_IMAGE_NOTIFY = "f000ffc1-0451-4000-b000-000000000000";
    public static String TI_OAD_IMAGE_STATUS = "f000ffc4-0451-4000-b000-000000000000";
    public static byte[] TI_OAD_IMG_INFO_CC13XR1 = {67, 67, 49, 51, 120, 50, 82, 49};
    public static byte[] TI_OAD_IMG_INFO_CC2640R2 = {79, 65, 68, 32, 73, 77, 71, 32};
    public static byte[] TI_OAD_IMG_INFO_CC26X2R1 = {67, 67, 50, 54, 120, 50, 82, 49};
    public static String TI_OAD_SERVICE = "f000ffc0-0451-4000-b000-000000000000";

    /* renamed from: com.ti.ti_oad.TIOADEoadDefinitions$oadChipFamily */
    public enum oadChipFamily {
        tiOADChipFamilyCC26x0,
        tiOADChipFamilyCC13x0,
        tiOADChipFamilyCC26x1,
        tiOADChipFamilyCC26x0R2,
        tiOADChipFamilyCC13x2_CC26x2
    }

    /* renamed from: com.ti.ti_oad.TIOADEoadDefinitions$oadChipType */
    public enum oadChipType {
        tiOADChipTypeCC1310,
        tiOADChipTypeCC1350,
        tiOADChipTypeCC2620,
        tiOADChipTypeCC2630,
        tiOADChipTypeCC2640,
        tiOADChipTypeCC2650,
        tiOADChipTypeCustomOne,
        tiOADChipTypeCustomTwo,
        tiOADChipTypeCC2640R2,
        tiOADChipTypeCC2642,
        tiOADChipTypeCC2644,
        tiOADChipTypeCC2652,
        tiOADChipTypeCC1312,
        tiOADChipTypeCC1352,
        tiOADChipTypeCC1352P;

        public static oadChipType fromInteger(int i) {
            switch (i) {
                case 0:
                    return tiOADChipTypeCC1310;
                case 1:
                    return tiOADChipTypeCC1350;
                case 2:
                    return tiOADChipTypeCC2620;
                case 3:
                    return tiOADChipTypeCC2630;
                case 4:
                    return tiOADChipTypeCC2640;
                case 5:
                    return tiOADChipTypeCC2650;
                case 6:
                    return tiOADChipTypeCustomOne;
                case 7:
                    return tiOADChipTypeCustomTwo;
                case 8:
                    return tiOADChipTypeCC2640R2;
                case 9:
                    return tiOADChipTypeCC2642;
                case 10:
                    return tiOADChipTypeCC2644;
                case 11:
                    return tiOADChipTypeCC2652;
                case 12:
                    return tiOADChipTypeCC1312;
                case 13:
                    return tiOADChipTypeCC1352;
                case 14:
                    return tiOADChipTypeCC1352P;
                default:
                    return tiOADChipTypeCC2640;
            }
        }
    }

    /* renamed from: com.ti.ti_oad.TIOADEoadDefinitions$oadStatusEnumeration */
    public enum oadStatusEnumeration {
        tiOADClientDeviceConnecting,
        tiOADClientDeviceDiscovering,
        tiOADClientConnectionParametersChanged,
        tiOADClientDeviceMTUSet,
        tiOADClientInitializing,
        tiOADClientPeripheralConnected,
        tiOADClientOADServiceMissingOnPeripheral,
        tiOADClientOADCharacteristicMissingOnPeripheral,
        tiOADClientOADWrongVersion,
        tiOADClientReady,
        tiOADClientFileIsNotForDevice,
        tiOADClientDeviceTypeRequestResponse,
        tiOADClientBlockSizeRequestSent,
        tiOADClientGotBlockSizeResponse,
        tiOADClientHeaderSent,
        tiOADClientHeaderOK,
        tiOADClientHeaderFailed,
        tiOADClientOADProcessStartCommandSent,
        tiOADClientImageTransfer,
        tiOADClientImageTransferFailed,
        tiOADClientImageTransferOK,
        tiOADClientEnableOADImageCommandSent,
        tiOADClientCompleteFeedbackOK,
        tiOADClientCompleteFeedbackFailed,
        tiOADClientCompleteDeviceDisconnectedPositive,
        tiOADClientCompleteDeviceDisconnectedDuringProgramming,
        tiOADClientProgrammingAbortedByUser,
        tiOADClientChipIsCC1352PShowWarningAboutLayouts,
        tiOADClientSecureUnsecureDetectionRunning
    }

    /* renamed from: com.ti.ti_oad.TIOADEoadDefinitions$tiOADImageType */
    public enum tiOADImageType {
        tiOADImageUnidentified,
        tiOADImageUnsecure,
        tiOADImageSecure
    }

    public static int BUILD_UINT16(byte b, byte b2) {
        return (((b & 255) << 8) & 65280) | (b2 & 255);
    }

    public static long BUILD_UINT32(byte b, byte b2, byte b3, byte b4) {
        return (((((long) (b2 & 255)) << 16) & 16711680) | ((((long) (b & 255)) << 24) & -16777216) | ((((long) (b3 & 255)) << 8) & 65280) | (((long) b4) & 255 & 255)) & -1;
    }

    public static byte GET_BYTE_FROM_UINT32(long j, int i) {
        char c;
        if (i != 0) {
            if (i == 1) {
                c = 8;
            } else if (i == 2) {
                c = 16;
            } else if (i != 3) {
                return 0;
            } else {
                c = 24;
            }
            j >>= c;
        }
        return (byte) ((int) (j & 255));
    }

    public static byte GET_HIGH_BYTE_FROM_UINT16(int i) {
        return (byte) ((i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
    }

    public static byte GET_LOW_BYTE_FROM_UINT16(int i) {
        return (byte) (i & 255);
    }

    public static String BytetohexString(byte[] bArr) {
        if (bArr == null) {
            return "NULL";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 3);
        Formatter formatter = new Formatter(sb);
        for (int i = 0; i < bArr.length; i++) {
            if (i < bArr.length - 1) {
                formatter.format("%02X:", new Object[]{Byte.valueOf(bArr[i])});
            } else {
                formatter.format("%02X", new Object[]{Byte.valueOf(bArr[i])});
            }
        }
        formatter.close();
        return sb.toString();
    }

    public static String oadImageIdentificationPrettyPrint(byte[] bArr) {
        boolean z = true;
        for (int i = 0; i < 8; i++) {
            if (bArr[i] != TI_OAD_IMG_INFO_CC2640R2[i]) {
                z = false;
            }
        }
        if (z) {
            return "CC2640R2";
        }
        boolean z2 = true;
        for (int i2 = 0; i2 < 8; i2++) {
            if (bArr[i2] != TI_OAD_IMG_INFO_CC26X2R1[i2]) {
                z2 = false;
            }
        }
        if (z2) {
            return "CC26X2R";
        }
        boolean z3 = true;
        for (int i3 = 0; i3 < 8; i3++) {
            if (bArr[i3] != TI_OAD_IMG_INFO_CC13XR1[i3]) {
                z3 = false;
            }
        }
        return z3 ? "CC13X2R" : "UNKNOWN";
    }

    public static byte[] oadImageInfoFromChipType(byte[] bArr) {
        int i = C09521.$SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType[oadChipType.values()[bArr[0]].ordinal()];
        if (i == 1) {
            return TI_OAD_IMG_INFO_CC2640R2;
        }
        if (i == 2 || i == 3) {
            return TI_OAD_IMG_INFO_CC26X2R1;
        }
        if (i == 4 || i == 5) {
            return TI_OAD_IMG_INFO_CC13XR1;
        }
        return new byte[8];
    }

    public static String oadChipTypePrettyPrint(byte[] bArr) {
        switch (oadChipType.values()[bArr[0]]) {
            case tiOADChipTypeCC2640R2:
                return "CC2640R2";
            case tiOADChipTypeCC2642:
                return "CC2642";
            case tiOADChipTypeCC2652:
                return "CC2652";
            case tiOADChipTypeCC1352:
                return "CC1352";
            case tiOADChipTypeCC1352P:
                return "CC1352P";
            case tiOADChipTypeCC1310:
                return "CC1310";
            case tiOADChipTypeCC1312:
                return "CC1312";
            case tiOADChipTypeCC1350:
                return "CC1350";
            case tiOADChipTypeCC2620:
                return "CC2620";
            case tiOADChipTypeCC2630:
                return "CC2630";
            case tiOADChipTypeCC2640:
                return "CC2640";
            case tiOADChipTypeCC2644:
                return "CC2644";
            case tiOADChipTypeCC2650:
                return "CC2650";
            case tiOADChipTypeCustomOne:
            case tiOADChipTypeCustomTwo:
                return "Custom";
            default:
                return "Unknown";
        }
    }

    public static String oadStatusEnumerationGetDescriptiveString(oadStatusEnumeration oadstatusenumeration) {
        switch (oadstatusenumeration) {
            case tiOADClientDeviceConnecting:
                return "TI EOAD Client is connecting !";
            case tiOADClientDeviceDiscovering:
                return "TI EOAD Client is discovering services !";
            case tiOADClientConnectionParametersChanged:
                return "TI EOAD Client waiting for connection parameter change";
            case tiOADClientDeviceMTUSet:
                return "TI EOAD Client waiting for MTU Update";
            case tiOADClientInitializing:
                return "TI EOAD Client is initializing !";
            case tiOADClientPeripheralConnected:
                return "Connected to peripheral";
            case tiOADClientOADServiceMissingOnPeripheral:
                return "EOAD service is missing on peripheral, cannot continue !";
            case tiOADClientOADCharacteristicMissingOnPeripheral:
                return "Found EOAD service, but it`s missing some characteristics !";
            case tiOADClientOADWrongVersion:
                return "OAD on peripheral has the wrong version !";
            case tiOADClientReady:
                return "EOAD Client is ready for programming";
            case tiOADClientBlockSizeRequestSent:
                return "EOAD Client sent block size request to peripheral";
            case tiOADClientGotBlockSizeResponse:
                return "EOAD Client received block size response from peripheral";
            case tiOADClientHeaderSent:
                return "EOAD Client sent image header to peripheral";
            case tiOADClientHeaderOK:
                return "EOAD Client header was accepted by peripheral";
            case tiOADClientHeaderFailed:
                return "EOAD Client header was rejected by peripheral, cannot continue !";
            case tiOADClientOADProcessStartCommandSent:
                return "Sent start command to peripheral";
            case tiOADClientImageTransfer:
                return "EOAD Image is transfering";
            case tiOADClientImageTransferFailed:
                return "EOAD Image transfer failed, cannot continue !";
            case tiOADClientImageTransferOK:
                return "EOAD Image transfer completed OK";
            case tiOADClientEnableOADImageCommandSent:
                return "EOAD Image Enable command sent";
            case tiOADClientCompleteFeedbackOK:
                return "EOAD Image Enable OK, device is rebooting on new image !";
            case tiOADClientCompleteFeedbackFailed:
                return "EOAD Image Enable FAILED, device continuing on old image !";
            case tiOADClientFileIsNotForDevice:
                return "EOAD Image is not for this device, cannot continue";
            case tiOADClientDeviceTypeRequestResponse:
                return "EOAD Image device type response received";
            case tiOADClientCompleteDeviceDisconnectedPositive:
                return "TI EOAD Client disconnected after successfull programming !";
            case tiOADClientCompleteDeviceDisconnectedDuringProgramming:
                return "TI EOAD Client disconnected during image transfer, please move closer and try again !";
            case tiOADClientProgrammingAbortedByUser:
                return "Programming aborted by user !";
            default:
                return "Unknown states";
        }
    }
}
