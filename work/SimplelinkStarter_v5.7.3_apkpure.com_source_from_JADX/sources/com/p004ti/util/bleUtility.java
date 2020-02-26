package com.p004ti.util;

import android.bluetooth.BluetoothDevice;
import android.support.p000v4.view.MotionEventCompat;
import com.p004ti.device_selector.TopLevel;

/* renamed from: com.ti.util.bleUtility */
public class bleUtility {

    /* renamed from: com.ti.util.bleUtility$boardType */
    public enum boardType {
        unknown,
        sensorTagOne,
        CC2650SensorTag,
        CC2650FlashProgrammer,
        CC2650RC,
        CC2650LaunchPad,
        CC1350LaunchPad
    }

    public static int BUILD_INT16(byte b, byte b2) {
        byte b3 = (((b & 255) << 8) & 65280) | (b2 & 255);
        return (b3 & 32768) == 32768 ? b3 | -65536 : b3;
    }

    public static int BUILD_UINT16(byte b, byte b2) {
        return (((b & 255) << 8) & 65280) | (b2 & 255);
    }

    public static long BUILD_UINT32(byte b, byte b2, byte b3, byte b4) {
        return (((((long) (b2 & 255)) << 16) & 16711680) | ((((long) (b & 255)) << 24) & -16777216) | ((((long) (b3 & 255)) << 8) & 65280) | (((long) b4) & 255 & 255)) & -1;
    }

    public static byte GET_HIGH_BYTE_FROM_UINT16(int i) {
        return (byte) ((i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
    }

    public static byte GET_LOW_BYTE_FROM_UINT16(int i) {
        return (byte) (i & 255);
    }

    public static boardType boardTypeFromBluetoothDevice(BluetoothDevice bluetoothDevice) {
        String str;
        String str2 = "CC2650 Programmer";
        try {
            str = bluetoothDevice.getName();
        } catch (Exception e) {
            e.printStackTrace();
            str = str2;
        }
        if (str == null) {
            str = str2;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1820147360:
                if (str.equals(TopLevel.Sensor_Tag)) {
                    c = 5;
                    break;
                }
                break;
            case -1712830785:
                if (str.equals("CC2650 SensorTag")) {
                    c = 0;
                    break;
                }
                break;
            case -1473029123:
                if (str.equals("CC1350 LaunchPad")) {
                    c = 3;
                    break;
                }
                break;
            case -1309261390:
                if (str.equals("CC2650 RC")) {
                    c = 4;
                    break;
                }
                break;
            case 1057934199:
                if (str.equals(str2)) {
                    c = 1;
                    break;
                }
                break;
            case 1436170527:
                if (str.equals("CC2650 LaunchPad")) {
                    c = 2;
                    break;
                }
                break;
        }
        if (c == 0) {
            return boardType.CC2650SensorTag;
        }
        if (c == 1) {
            return boardType.CC2650FlashProgrammer;
        }
        if (c == 2) {
            return boardType.CC2650LaunchPad;
        }
        if (c == 3) {
            return boardType.CC1350LaunchPad;
        }
        if (c == 4) {
            return boardType.CC2650RC;
        }
        if (c != 5) {
            return boardType.unknown;
        }
        return boardType.sensorTagOne;
    }

    public static String getStringFromByteVector(byte[] bArr) {
        String str = "";
        for (int i = 0; i < bArr.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(String.format("0x%x", new Object[]{Byte.valueOf(bArr[i])}));
            String sb2 = sb.toString();
            if (i < bArr.length - 1) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append(",");
                str = sb3.toString();
            } else {
                str = sb2;
            }
        }
        return str;
    }

    public static String getStringFromByteVectorWithAutoNewLine(byte[] bArr, int i) {
        String str = "";
        for (int i2 = 0; i2 < bArr.length; i2++) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(String.format("0x%x", new Object[]{Byte.valueOf(bArr[i2])}));
            String sb2 = sb.toString();
            if (i2 < bArr.length - 1) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append(",");
                sb2 = sb3.toString();
            }
            if (i2 % i != 0 || i2 == 0) {
                str = sb2;
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(sb2);
                sb4.append("\r\n");
                str = sb4.toString();
            }
        }
        return str;
    }

    public static String ServiceStateStringFromStatus(int i) {
        String str = ")";
        if (i == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("GATT_SUCCESS (");
            sb.append(i);
            sb.append(str);
            return sb.toString();
        } else if (i != 257) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("UNKNOWN (");
            sb2.append(i);
            sb2.append(str);
            return sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("GATT_FAILURE (");
            sb3.append(i);
            sb3.append(str);
            return sb3.toString();
        }
    }

    public static String ConnectionStateStringFromState(int i) {
        StringBuilder sb = new StringBuilder();
        String str = i == 2 ? "Connected" : i == 1 ? "Connecting" : i == 3 ? "Disconnecting" : "Disconnected";
        sb.append(str);
        sb.append(" (");
        sb.append(i);
        sb.append(")");
        return sb.toString();
    }
}
