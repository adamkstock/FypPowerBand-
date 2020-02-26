package com.p004ti.ble.sensortag;

import com.p004ti.ble.audio.AdvancedRemoteBLEAudioDefines;
import com.p004ti.ble.btsig.DeviceInformationServiceProfile;
import com.p004ti.ble.launchpad_sensor_tag.LpstkHallService;
import java.util.UUID;

/* renamed from: com.ti.ble.sensortag.SensorTagGatt */
public class SensorTagGatt {
    public static final UUID UUID_ACC_CONF = UUID.fromString("f000aa12-0451-4000-b000-000000000000");
    public static final UUID UUID_ACC_DATA = UUID.fromString("f000aa11-0451-4000-b000-000000000000");
    public static final UUID UUID_ACC_PERI = UUID.fromString("f000aa13-0451-4000-b000-000000000000");
    public static final UUID UUID_ACC_SERV = UUID.fromString("f000aa10-0451-4000-b000-000000000000");
    public static final UUID UUID_AUDIO_AUDIO = UUID.fromString(AdvancedRemoteBLEAudioDefines.AudioStreamUUID);
    public static final UUID UUID_AUDIO_SERV = UUID.fromString(AdvancedRemoteBLEAudioDefines.AudioServiceUUID);
    public static final UUID UUID_AUDIO_STARTSTOP = UUID.fromString(AdvancedRemoteBLEAudioDefines.AudioControlUUID);
    public static final UUID UUID_BAR_CALI = UUID.fromString("f000aa43-0451-4000-b000-000000000000");
    public static final UUID UUID_BAR_CONF = UUID.fromString("f000aa42-0451-4000-b000-000000000000");
    public static final UUID UUID_BAR_DATA = UUID.fromString("f000aa41-0451-4000-b000-000000000000");
    public static final UUID UUID_BAR_PERI = UUID.fromString("f000aa44-0451-4000-b000-000000000000");
    public static final UUID UUID_BAR_SERV = UUID.fromString("f000aa40-0451-4000-b000-000000000000");
    public static final UUID UUID_DEVINFO_FWREV = UUID.fromString("00002A26-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_DEVINFO_SERV = UUID.fromString(DeviceInformationServiceProfile.dISService_UUID);
    public static final UUID UUID_GYR_CONF = UUID.fromString("f000aa52-0451-4000-b000-000000000000");
    public static final UUID UUID_GYR_DATA = UUID.fromString("f000aa51-0451-4000-b000-000000000000");
    public static final UUID UUID_GYR_PERI = UUID.fromString("f000aa53-0451-4000-b000-000000000000");
    public static final UUID UUID_GYR_SERV = UUID.fromString("f000aa50-0451-4000-b000-000000000000");
    public static final UUID UUID_HUM_CONF = UUID.fromString("f000aa22-0451-4000-b000-000000000000");
    public static final UUID UUID_HUM_DATA = UUID.fromString("f000aa21-0451-4000-b000-000000000000");
    public static final UUID UUID_HUM_PERI = UUID.fromString("f000aa23-0451-4000-b000-000000000000");
    public static final UUID UUID_HUM_SERV = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    public static final UUID UUID_IRT_CONF = UUID.fromString("f000aa02-0451-4000-b000-000000000000");
    public static final UUID UUID_IRT_DATA = UUID.fromString("f000aa01-0451-4000-b000-000000000000");
    public static final UUID UUID_IRT_PERI = UUID.fromString("f000aa03-0451-4000-b000-000000000000");
    public static final UUID UUID_IRT_SERV = UUID.fromString("f000aa00-0451-4000-b000-000000000000");
    public static final UUID UUID_KEY_DATA = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_KEY_SERV = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_MAG_CONF = UUID.fromString(LpstkHallService.LPSTK_HALL_CONFIG_CHARACTERISTIC_UUID);
    public static final UUID UUID_MAG_DATA = UUID.fromString(LpstkHallService.LPSTK_HALL_DATA_CHARACTERISTIC_UUID);
    public static final UUID UUID_MAG_PERI = UUID.fromString(LpstkHallService.LPSTK_HALL_PERIOD_CHARACTERISTIC_UUID);
    public static final UUID UUID_MAG_SERV = UUID.fromString(LpstkHallService.LPSTK_HALL_SERVICE_UUID);
    public static final UUID UUID_MOV_CONF = UUID.fromString("f000aa82-0451-4000-b000-000000000000");
    public static final UUID UUID_MOV_DATA = UUID.fromString("f000aa81-0451-4000-b000-000000000000");
    public static final UUID UUID_MOV_PERI = UUID.fromString("f000aa83-0451-4000-b000-000000000000");
    public static final UUID UUID_MOV_SERV = UUID.fromString("f000aa80-0451-4000-b000-000000000000");
    public static final UUID UUID_OPT_CONF = UUID.fromString("f000aa72-0451-4000-b000-000000000000");
    public static final UUID UUID_OPT_DATA = UUID.fromString("f000aa71-0451-4000-b000-000000000000");
    public static final UUID UUID_OPT_PERI = UUID.fromString("f000aa73-0451-4000-b000-000000000000");
    public static final UUID UUID_OPT_SERV = UUID.fromString("f000aa70-0451-4000-b000-000000000000");
    public static final UUID UUID_TST_DATA = UUID.fromString("f000aa65-0451-4000-b000-000000000000");
    public static final UUID UUID_TST_SERV = UUID.fromString("f000aa64-0451-4000-b000-000000000000");
}
