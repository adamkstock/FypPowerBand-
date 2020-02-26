package org.eclipse.paho.client.mqttv3.internal;

public abstract class MessageCatalog {
    private static MessageCatalog INSTANCE;

    /* access modifiers changed from: protected */
    public abstract String getLocalizedMessage(int i);

    public static final String getMessage(int i) {
        if (INSTANCE == null) {
            String str = "";
            if (ExceptionHelper.isClassAvailable("java.util.ResourceBundle")) {
                try {
                    INSTANCE = (MessageCatalog) Class.forName("org.eclipse.paho.client.mqttv3.internal.ResourceBundleCatalog").newInstance();
                } catch (Exception unused) {
                    return str;
                }
            } else {
                String str2 = "org.eclipse.paho.client.mqttv3.internal.MIDPCatalog";
                if (ExceptionHelper.isClassAvailable(str2)) {
                    try {
                        INSTANCE = (MessageCatalog) Class.forName(str2).newInstance();
                    } catch (Exception unused2) {
                        return str;
                    }
                }
            }
        }
        return INSTANCE.getLocalizedMessage(i);
    }
}
