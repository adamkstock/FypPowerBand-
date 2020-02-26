package org.eclipse.paho.client.mqttv3.logging;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LoggerFactory {
    private static final String CLASS_NAME;
    public static final String MQTT_CLIENT_MSG_CAT = "org.eclipse.paho.client.mqttv3.internal.nls.logcat";
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    private static String jsr47LoggerClassName = "org.eclipse.paho.client.mqttv3.logging.JSR47Logger";
    private static String overrideloggerClassName = null;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.logging.LoggerFactory");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
    }

    public static Logger getLogger(String str, String str2) {
        String str3 = overrideloggerClassName;
        if (str3 == null) {
            str3 = jsr47LoggerClassName;
        }
        Logger logger = getLogger(str3, ResourceBundle.getBundle(str), str2, null);
        if (logger != null) {
            return logger;
        }
        throw new MissingResourceException("Error locating the logging class", CLASS_NAME, str2);
    }

    private static Logger getLogger(String str, ResourceBundle resourceBundle, String str2, String str3) {
        Logger logger;
        try {
            Class cls = Class.forName(str);
            if (cls != null) {
                try {
                    logger = (Logger) cls.newInstance();
                    logger.initialise(resourceBundle, str2, str3);
                } catch (ExceptionInInitializerError | IllegalAccessException | InstantiationException | SecurityException unused) {
                    return null;
                }
            } else {
                logger = null;
            }
            return logger;
        } catch (ClassNotFoundException | NoClassDefFoundError unused2) {
            return null;
        }
    }

    public static String getLoggingProperty(String str) {
        try {
            Class cls = Class.forName("java.util.logging.LogManager");
            Object invoke = cls.getMethod("getLogManager", new Class[0]).invoke(null, null);
            String str2 = "getProperty";
            Class[] clsArr = new Class[1];
            Class cls2 = class$1;
            if (cls2 == null) {
                cls2 = Class.forName("java.lang.String");
                class$1 = cls2;
            }
            clsArr[0] = cls2;
            return (String) cls.getMethod(str2, clsArr).invoke(invoke, new Object[]{str});
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        } catch (Exception unused) {
            return null;
        }
    }

    public static void setLogger(String str) {
        overrideloggerClassName = str;
    }
}
