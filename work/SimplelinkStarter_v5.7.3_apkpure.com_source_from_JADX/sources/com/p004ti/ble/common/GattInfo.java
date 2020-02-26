package com.p004ti.ble.common;

import android.content.res.XmlResourceParser;
import com.p004ti.ble.p005ti.profiles.TIOADProfile;
import com.p004ti.ble.p005ti.profiles.ThroughputTestService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: com.ti.ble.common.GattInfo */
public class GattInfo {
    public static final UUID CC_SERVICE_UUID = UUID.fromString("f000ccc0-0451-4000-b000-000000000000");
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID EDDYSTONE_SERVICE_UUID = UUID.fromString("0000feaa-0000-1000-8000-00805f9b34fb");
    public static final UUID OAD_SERVICE_UUID = UUID.fromString(TIOADProfile.oadService_UUID);
    public static final UUID THROUGHPUT_TEST_UUID = UUID.fromString(ThroughputTestService.throughputService_UUID);
    private static Map<String, String> mDescrMap = new HashMap();
    private static Map<String, String> mIconMap = new HashMap();
    private static Map<String, String> mNameMap = new HashMap();
    private static Map<String, String> mTitleMap = new HashMap();
    private static final String uuidBtSigBase = "0000****-0000-1000-8000-00805f9b34fb";
    private static final String uuidTiBase = "f000****-0451-4000-b000-000000000000";

    public GattInfo(XmlResourceParser xmlResourceParser) {
        try {
            readUuidData(xmlResourceParser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static String uuidToName(UUID uuid) {
        return uuidToName(toShortUuidStr(uuid).toUpperCase(Locale.ENGLISH));
    }

    public static String uuidToIconLong(UUID uuid) {
        return uuidToIconLong(uuid.toString().toLowerCase(Locale.ENGLISH));
    }

    public static String uuidToIcon(UUID uuid) {
        return uuidToIcon(toShortUuidStr(uuid).toUpperCase(Locale.ENGLISH));
    }

    public static String getDescription(UUID uuid) {
        return (String) mDescrMap.get(toShortUuidStr(uuid).toUpperCase(Locale.ENGLISH));
    }

    public static String getTitle(UUID uuid) {
        return (String) mTitleMap.get(uuid.toString());
    }

    public static boolean isTiUuid(UUID uuid) {
        return uuid.toString().replace(toShortUuidStr(uuid), "****").equals(uuidTiBase);
    }

    public static boolean isBtSigUuid(UUID uuid) {
        return uuid.toString().replace(toShortUuidStr(uuid), "****").equals(uuidBtSigBase);
    }

    public static String uuidToString(UUID uuid) {
        String str;
        if (isBtSigUuid(uuid)) {
            str = toShortUuidStr(uuid);
        } else {
            str = uuid.toString();
        }
        return str.toUpperCase(Locale.ENGLISH);
    }

    private static String toShortUuidStr(UUID uuid) {
        return uuid.toString().substring(4, 8);
    }

    private static String uuidToName(String str) {
        return (String) mNameMap.get(str);
    }

    public String descriptionOfUUID(String str) {
        return (String) mDescrMap.get(str);
    }

    private static String uuidToIcon(String str) {
        return (String) mIconMap.get(str);
    }

    private static String uuidToIconLong(String str) {
        return (String) mIconMap.get(str);
    }

    private void readUuidData(XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException {
        xmlResourceParser.next();
        int eventType = xmlResourceParser.getEventType();
        String str = null;
        String str2 = null;
        Object obj = null;
        Object obj2 = null;
        while (eventType != 1) {
            if (eventType != 0) {
                if (eventType == 2) {
                    String name = xmlResourceParser.getName();
                    String attributeValue = xmlResourceParser.getAttributeValue(null, "uuid");
                    String attributeValue2 = xmlResourceParser.getAttributeValue(null, "descr");
                    obj2 = xmlResourceParser.getAttributeValue(null, "icon");
                    obj = attributeValue2;
                    str2 = attributeValue;
                    str = name;
                } else if (eventType != 3 && eventType == 4 && str.equalsIgnoreCase("item")) {
                    String text = xmlResourceParser.getText();
                    if (!str2.isEmpty()) {
                        str2 = str2.replace("0x", "");
                        mNameMap.put(str2, xmlResourceParser.getText());
                        mDescrMap.put(str2, obj);
                        mIconMap.put(str2, obj2);
                        mTitleMap.put(str2, text);
                    }
                }
            }
            eventType = xmlResourceParser.next();
        }
    }
}
