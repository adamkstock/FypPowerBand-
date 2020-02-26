package com.p004ti.ble.common.oad;

import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: com.ti.ble.common.oad.FWUpdateBINFileEntriesParser */
public class FWUpdateBINFileEntriesParser {

    /* renamed from: ns */
    private static final String f37ns = null;

    /* JADX INFO: finally extract failed */
    public List parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser newPullParser = Xml.newPullParser();
            newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            newPullParser.setInput(inputStream, HTTP.UTF_8);
            newPullParser.nextTag();
            List readFw = readFw(newPullParser);
            inputStream.close();
            return readFw;
        } catch (Exception e) {
            e.printStackTrace();
            inputStream.close();
            return null;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    private List readFw(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        ArrayList arrayList = new ArrayList();
        xmlPullParser.require(2, f37ns, "FirmwareEntries");
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("FirmwareEntry")) {
                    arrayList.add(readEntry(xmlPullParser));
                } else {
                    skip(xmlPullParser);
                }
            }
        }
        return arrayList;
    }

    public FWUpdateTIFirmwareEntry readEntry(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        int i = 2;
        xmlPullParser2.require(2, f37ns, "FirmwareEntry");
        Boolean valueOf = Boolean.valueOf(false);
        Boolean bool = valueOf;
        Boolean bool2 = bool;
        String str = "";
        String str2 = str;
        String str3 = str2;
        String str4 = str3;
        String str5 = str4;
        String str6 = str5;
        String str7 = str6;
        String str8 = str7;
        int i2 = 0;
        float f = 0.0f;
        float f2 = 0.0f;
        Boolean bool3 = bool2;
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == i) {
                String name = xmlPullParser.getName();
                String simpleName = getClass().getSimpleName();
                StringBuilder sb = new StringBuilder();
                sb.append("Name:");
                sb.append(name);
                Log.d(simpleName, sb.toString());
                String str9 = "Custom";
                if (name.equals(str9)) {
                    valueOf = Boolean.valueOf(readBoolean(xmlPullParser2, str9));
                } else {
                    String str10 = "WirelessStandard";
                    if (name.equals(str10)) {
                        str2 = readTag(xmlPullParser2, str10);
                    } else {
                        String str11 = "Type";
                        if (name.equals(str11)) {
                            str3 = readTag(xmlPullParser2, str11);
                        } else {
                            String str12 = "OADAlgo";
                            if (name.equals(str12)) {
                                i2 = readInt(xmlPullParser2, str12);
                            } else {
                                String str13 = "BoardType";
                                if (name.equals(str13)) {
                                    str4 = readTag(xmlPullParser2, str13);
                                } else {
                                    String str14 = "RequiredVersionRev";
                                    if (name.equals(str14)) {
                                        f = readFloat(xmlPullParser2, str14);
                                    } else {
                                        String str15 = "SafeMode";
                                        if (name.equals(str15)) {
                                            bool = Boolean.valueOf(readBoolean(xmlPullParser2, str15));
                                        } else {
                                            String str16 = "Version";
                                            if (name.equals(str16)) {
                                                f2 = readFloat(xmlPullParser2, str16);
                                            } else {
                                                String str17 = "Filename";
                                                if (name.equals(str17)) {
                                                    str = readTag(xmlPullParser2, str17);
                                                } else {
                                                    String str18 = "DevPack";
                                                    if (name.equals(str18)) {
                                                        str5 = readTag(xmlPullParser2, str18);
                                                    } else {
                                                        String str19 = "Description";
                                                        if (name.equals(str19)) {
                                                            str6 = readTag(xmlPullParser2, str19);
                                                        } else if (name.equals("MCU")) {
                                                            str7 = readTag(xmlPullParser2, "MCU");
                                                        } else if (name.equals("OnChip")) {
                                                            bool2 = Boolean.valueOf(readTag(xmlPullParser2, "OnChip").equalsIgnoreCase("true"));
                                                        } else if (name.equals("Title")) {
                                                            str8 = readTag(xmlPullParser2, "Title");
                                                        } else if (name.equals("Security")) {
                                                            bool3 = Boolean.valueOf(readTag(xmlPullParser2, "Security").equalsIgnoreCase(ClientCookie.SECURE_ATTR));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                i = 2;
            }
        }
        FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry = new FWUpdateTIFirmwareEntry(str, valueOf.booleanValue(), str2, str3, i2, str4, f, bool.booleanValue(), f2, str5, str6, str7, bool2.booleanValue(), str8, bool3.booleanValue());
        return fWUpdateTIFirmwareEntry;
    }

    private void skip(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() == 2) {
            int i = 1;
            while (i != 0) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    i++;
                } else if (next == 3) {
                    i--;
                }
            }
            return;
        }
        throw new IllegalStateException();
    }

    private String readTag(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        String str2;
        xmlPullParser.require(2, f37ns, str);
        if (xmlPullParser.next() == 4) {
            str2 = xmlPullParser.getText();
            xmlPullParser.nextTag();
        } else {
            str2 = "";
        }
        xmlPullParser.require(3, f37ns, str);
        return str2;
    }

    private boolean readBoolean(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        boolean z;
        xmlPullParser.require(2, f37ns, str);
        if (xmlPullParser.next() == 4) {
            z = Boolean.getBoolean(xmlPullParser.getText());
            xmlPullParser.nextTag();
        } else {
            z = false;
        }
        xmlPullParser.require(3, f37ns, str);
        return z;
    }

    private float readFloat(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        float f;
        xmlPullParser.require(2, f37ns, str);
        if (xmlPullParser.next() == 4) {
            f = Float.parseFloat(xmlPullParser.getText());
            xmlPullParser.nextTag();
        } else {
            f = 0.0f;
        }
        xmlPullParser.require(3, f37ns, str);
        return f;
    }

    private int readInt(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        int i;
        xmlPullParser.require(2, f37ns, str);
        if (xmlPullParser.next() == 4) {
            i = Integer.parseInt(xmlPullParser.getText());
            xmlPullParser.nextTag();
        } else {
            i = 0;
        }
        xmlPullParser.require(3, f37ns, str);
        return i;
    }
}
