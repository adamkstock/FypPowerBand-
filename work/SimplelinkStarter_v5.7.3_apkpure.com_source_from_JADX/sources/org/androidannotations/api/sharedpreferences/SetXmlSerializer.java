package org.androidannotations.api.sharedpreferences;

import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class SetXmlSerializer {
    private static final String NAMESPACE = "";
    private static final String SET_TAG = "AA_set";
    private static final String STRING_TAG = "AA_string";

    private SetXmlSerializer() {
    }

    public static String serialize(Set<String> set) {
        String str = STRING_TAG;
        String str2 = SET_TAG;
        String str3 = "";
        if (set == null) {
            set = Collections.emptySet();
        }
        StringWriter stringWriter = new StringWriter();
        XmlSerializer newSerializer = Xml.newSerializer();
        try {
            newSerializer.setOutput(stringWriter);
            newSerializer.startTag(str3, str2);
            for (String text : set) {
                newSerializer.startTag(str3, str).text(text).endTag(str3, str);
            }
            newSerializer.endTag(str3, str2).endDocument();
        } catch (IOException | IllegalArgumentException | IllegalStateException unused) {
        }
        return stringWriter.toString();
    }

    public static Set<String> deserialize(String str) {
        String str2 = STRING_TAG;
        String str3 = "";
        String str4 = "getStringSet";
        TreeSet treeSet = new TreeSet();
        XmlPullParser newPullParser = Xml.newPullParser();
        try {
            newPullParser.setInput(new StringReader(str));
            newPullParser.next();
            newPullParser.require(2, str3, SET_TAG);
            while (newPullParser.next() != 3) {
                newPullParser.require(2, str3, str2);
                newPullParser.next();
                newPullParser.require(4, null, null);
                treeSet.add(newPullParser.getText());
                newPullParser.next();
                newPullParser.require(3, null, str2);
            }
            return treeSet;
        } catch (XmlPullParserException e) {
            Log.w(str4, e);
            return null;
        } catch (IOException e2) {
            Log.w(str4, e2);
            return null;
        }
    }
}
