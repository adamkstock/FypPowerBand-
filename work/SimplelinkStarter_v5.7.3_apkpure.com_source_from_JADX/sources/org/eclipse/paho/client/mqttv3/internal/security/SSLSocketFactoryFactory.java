package org.eclipse.paho.client.mqttv3.internal.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class SSLSocketFactoryFactory {
    public static final String CIPHERSUITES = "com.ibm.ssl.enabledCipherSuites";
    private static final String CLASS_NAME = "org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory";
    public static final String CLIENTAUTH = "com.ibm.ssl.clientAuthentication";
    public static final String DEFAULT_PROTOCOL = "TLS";
    public static final String JSSEPROVIDER = "com.ibm.ssl.contextProvider";
    public static final String KEYSTORE = "com.ibm.ssl.keyStore";
    public static final String KEYSTOREMGR = "com.ibm.ssl.keyManager";
    public static final String KEYSTOREPROVIDER = "com.ibm.ssl.keyStoreProvider";
    public static final String KEYSTOREPWD = "com.ibm.ssl.keyStorePassword";
    public static final String KEYSTORETYPE = "com.ibm.ssl.keyStoreType";
    public static final String SSLPROTOCOL = "com.ibm.ssl.protocol";
    public static final String SYSKEYMGRALGO = "ssl.KeyManagerFactory.algorithm";
    public static final String SYSKEYSTORE = "javax.net.ssl.keyStore";
    public static final String SYSKEYSTOREPWD = "javax.net.ssl.keyStorePassword";
    public static final String SYSKEYSTORETYPE = "javax.net.ssl.keyStoreType";
    public static final String SYSTRUSTMGRALGO = "ssl.TrustManagerFactory.algorithm";
    public static final String SYSTRUSTSTORE = "javax.net.ssl.trustStore";
    public static final String SYSTRUSTSTOREPWD = "javax.net.ssl.trustStorePassword";
    public static final String SYSTRUSTSTORETYPE = "javax.net.ssl.trustStoreType";
    public static final String TRUSTSTORE = "com.ibm.ssl.trustStore";
    public static final String TRUSTSTOREMGR = "com.ibm.ssl.trustManager";
    public static final String TRUSTSTOREPROVIDER = "com.ibm.ssl.trustStoreProvider";
    public static final String TRUSTSTOREPWD = "com.ibm.ssl.trustStorePassword";
    public static final String TRUSTSTORETYPE = "com.ibm.ssl.trustStoreType";
    private static final byte[] key = {-99, -89, -39, Byte.MIN_VALUE, 5, -72, -119, -100};
    private static final String[] propertyKeys = {SSLPROTOCOL, JSSEPROVIDER, KEYSTORE, KEYSTOREPWD, KEYSTORETYPE, KEYSTOREPROVIDER, KEYSTOREMGR, TRUSTSTORE, TRUSTSTOREPWD, TRUSTSTORETYPE, TRUSTSTOREPROVIDER, TRUSTSTOREMGR, CIPHERSUITES, CLIENTAUTH};
    private static final String xorTag = "{xor}";
    private Hashtable configs;
    private Properties defaultProperties;
    private Logger logger;

    public static boolean isSupportedOnJVM() throws LinkageError, ExceptionInInitializerError {
        try {
            Class.forName("javax.net.ssl.SSLServerSocketFactory");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    public SSLSocketFactoryFactory() {
        this.logger = null;
        this.configs = new Hashtable();
    }

    public SSLSocketFactoryFactory(Logger logger2) {
        this();
        this.logger = logger2;
    }

    private boolean keyValid(String str) {
        int i = 0;
        while (true) {
            String[] strArr = propertyKeys;
            if (i < strArr.length && !strArr[i].equals(str)) {
                i++;
            }
        }
        if (i < propertyKeys.length) {
            return true;
        }
        return false;
    }

    private void checkPropertyKeys(Properties properties) throws IllegalArgumentException {
        for (String str : properties.keySet()) {
            if (!keyValid(str)) {
                StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
                stringBuffer.append(" is not a valid IBM SSL property key.");
                throw new IllegalArgumentException(stringBuffer.toString());
            }
        }
    }

    public static char[] toChar(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        char[] cArr = new char[(bArr.length / 2)];
        int i = 0;
        int i2 = 0;
        while (i < bArr.length) {
            int i3 = i2 + 1;
            int i4 = i + 1;
            int i5 = i4 + 1;
            cArr[i2] = (char) ((bArr[i] & 255) + ((bArr[i4] & 255) << 8));
            i2 = i3;
            i = i5;
        }
        return cArr;
    }

    public static byte[] toByte(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        byte[] bArr = new byte[(cArr.length * 2)];
        int i = 0;
        int i2 = 0;
        while (i < cArr.length) {
            int i3 = i2 + 1;
            bArr[i2] = (byte) (cArr[i] & 255);
            i2 = i3 + 1;
            int i4 = i + 1;
            bArr[i3] = (byte) ((cArr[i] >> 8) & 255);
            i = i4;
        }
        return bArr;
    }

    public static String obfuscate(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        byte[] bArr = toByte(cArr);
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i];
            byte[] bArr2 = key;
            bArr[i] = (byte) ((b ^ bArr2[i % bArr2.length]) & 255);
        }
        StringBuffer stringBuffer = new StringBuffer(xorTag);
        stringBuffer.append(new String(SimpleBase64Encoder.encode(bArr)));
        return stringBuffer.toString();
    }

    public static char[] deObfuscate(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] decode = SimpleBase64Encoder.decode(str.substring(5));
            for (int i = 0; i < decode.length; i++) {
                byte b = decode[i];
                byte[] bArr = key;
                decode[i] = (byte) ((b ^ bArr[i % bArr.length]) & 255);
            }
            return toChar(decode);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String packCipherSuites(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strArr.length; i++) {
            stringBuffer.append(strArr[i]);
            if (i < strArr.length - 1) {
                stringBuffer.append(',');
            }
        }
        return stringBuffer.toString();
    }

    public static String[] unpackCipherSuites(String str) {
        if (str == null) {
            return null;
        }
        Vector vector = new Vector();
        int indexOf = str.indexOf(44);
        int i = 0;
        while (indexOf > -1) {
            vector.add(str.substring(i, indexOf));
            i = indexOf + 1;
            indexOf = str.indexOf(44, i);
        }
        vector.add(str.substring(i));
        String[] strArr = new String[vector.size()];
        vector.toArray(strArr);
        return strArr;
    }

    private void convertPassword(Properties properties) {
        String str = KEYSTOREPWD;
        String property = properties.getProperty(str);
        String str2 = xorTag;
        if (property != null && !property.startsWith(str2)) {
            properties.put(str, obfuscate(property.toCharArray()));
        }
        String str3 = TRUSTSTOREPWD;
        String property2 = properties.getProperty(str3);
        if (property2 != null && !property2.startsWith(str2)) {
            properties.put(str3, obfuscate(property2.toCharArray()));
        }
    }

    public void initialize(Properties properties, String str) throws IllegalArgumentException {
        checkPropertyKeys(properties);
        Properties properties2 = new Properties();
        properties2.putAll(properties);
        convertPassword(properties2);
        if (str != null) {
            this.configs.put(str, properties2);
        } else {
            this.defaultProperties = properties2;
        }
    }

    public void merge(Properties properties, String str) throws IllegalArgumentException {
        checkPropertyKeys(properties);
        Properties properties2 = this.defaultProperties;
        if (str != null) {
            properties2 = (Properties) this.configs.get(str);
        }
        if (properties2 == null) {
            properties2 = new Properties();
        }
        convertPassword(properties);
        properties2.putAll(properties);
        if (str != null) {
            this.configs.put(str, properties2);
        } else {
            this.defaultProperties = properties2;
        }
    }

    public boolean remove(String str) {
        if (str != null) {
            if (this.configs.remove(str) != null) {
                return true;
            }
        } else if (this.defaultProperties != null) {
            this.defaultProperties = null;
            return true;
        }
        return false;
    }

    public Properties getConfiguration(String str) {
        Object obj;
        if (str == null) {
            obj = this.defaultProperties;
        } else {
            obj = this.configs.get(str);
        }
        return (Properties) obj;
    }

    private String getProperty(String str, String str2, String str3) {
        String propertyFromConfig = getPropertyFromConfig(str, str2);
        if (propertyFromConfig != null) {
            return propertyFromConfig;
        }
        if (str3 != null) {
            propertyFromConfig = System.getProperty(str3);
        }
        return propertyFromConfig;
    }

    private String getPropertyFromConfig(String str, String str2) {
        String str3 = null;
        Properties properties = str != null ? (Properties) this.configs.get(str) : null;
        if (properties != null) {
            str3 = properties.getProperty(str2);
            if (str3 != null) {
                return str3;
            }
        }
        Properties properties2 = this.defaultProperties;
        if (properties2 != null) {
            str3 = properties2.getProperty(str2);
            if (str3 != null) {
            }
        }
        return str3;
    }

    public String getSSLProtocol(String str) {
        return getProperty(str, SSLPROTOCOL, null);
    }

    public String getJSSEProvider(String str) {
        return getProperty(str, JSSEPROVIDER, null);
    }

    public String getKeyStore(String str) {
        String propertyFromConfig = getPropertyFromConfig(str, KEYSTORE);
        return propertyFromConfig != null ? propertyFromConfig : System.getProperty(SYSKEYSTORE);
    }

    public char[] getKeyStorePassword(String str) {
        String property = getProperty(str, KEYSTOREPWD, SYSKEYSTOREPWD);
        if (property == null) {
            return null;
        }
        if (property.startsWith(xorTag)) {
            return deObfuscate(property);
        }
        return property.toCharArray();
    }

    public String getKeyStoreType(String str) {
        return getProperty(str, KEYSTORETYPE, SYSKEYSTORETYPE);
    }

    public String getKeyStoreProvider(String str) {
        return getProperty(str, KEYSTOREPROVIDER, null);
    }

    public String getKeyManager(String str) {
        return getProperty(str, KEYSTOREMGR, SYSKEYMGRALGO);
    }

    public String getTrustStore(String str) {
        return getProperty(str, TRUSTSTORE, SYSTRUSTSTORE);
    }

    public char[] getTrustStorePassword(String str) {
        String property = getProperty(str, TRUSTSTOREPWD, SYSTRUSTSTOREPWD);
        if (property == null) {
            return null;
        }
        if (property.startsWith(xorTag)) {
            return deObfuscate(property);
        }
        return property.toCharArray();
    }

    public String getTrustStoreType(String str) {
        return getProperty(str, TRUSTSTORETYPE, null);
    }

    public String getTrustStoreProvider(String str) {
        return getProperty(str, TRUSTSTOREPROVIDER, null);
    }

    public String getTrustManager(String str) {
        return getProperty(str, TRUSTSTOREMGR, SYSTRUSTMGRALGO);
    }

    public String[] getEnabledCipherSuites(String str) {
        return unpackCipherSuites(getProperty(str, CIPHERSUITES, null));
    }

    public boolean getClientAuthentication(String str) {
        String property = getProperty(str, CLIENTAUTH, null);
        if (property != null) {
            return Boolean.valueOf(property).booleanValue();
        }
        return false;
    }

    private SSLContext getSSLContext(String str) throws MqttSecurityException {
        SSLContext sSLContext;
        KeyManager[] keyManagerArr;
        TrustManager[] trustManagerArr;
        TrustManagerFactory trustManagerFactory;
        KeyManagerFactory keyManagerFactory;
        String str2 = str;
        String str3 = KEYSTORE;
        String sSLProtocol = getSSLProtocol(str);
        if (sSLProtocol == null) {
            sSLProtocol = DEFAULT_PROTOCOL;
        }
        Logger logger2 = this.logger;
        String str4 = "null (broker defaults)";
        String str5 = "getSSLContext";
        String str6 = CLASS_NAME;
        if (logger2 != null) {
            Object[] objArr = new Object[2];
            objArr[0] = str2 != null ? str2 : str4;
            objArr[1] = sSLProtocol;
            logger2.fine(str6, str5, "12000", objArr);
        }
        String jSSEProvider = getJSSEProvider(str);
        if (jSSEProvider == null) {
            try {
                sSLContext = SSLContext.getInstance(sSLProtocol);
            } catch (KeyStoreException e) {
                throw new MqttSecurityException((Throwable) e);
            } catch (CertificateException e2) {
                throw new MqttSecurityException((Throwable) e2);
            } catch (FileNotFoundException e3) {
                throw new MqttSecurityException((Throwable) e3);
            } catch (IOException e4) {
                throw new MqttSecurityException((Throwable) e4);
            } catch (KeyStoreException e5) {
                throw new MqttSecurityException((Throwable) e5);
            } catch (CertificateException e6) {
                throw new MqttSecurityException((Throwable) e6);
            } catch (FileNotFoundException e7) {
                throw new MqttSecurityException((Throwable) e7);
            } catch (IOException e8) {
                throw new MqttSecurityException((Throwable) e8);
            } catch (UnrecoverableKeyException e9) {
                throw new MqttSecurityException((Throwable) e9);
            } catch (NoSuchAlgorithmException e10) {
                throw new MqttSecurityException((Throwable) e10);
            } catch (NoSuchProviderException e11) {
                throw new MqttSecurityException((Throwable) e11);
            } catch (KeyManagementException e12) {
                throw new MqttSecurityException((Throwable) e12);
            }
        } else {
            sSLContext = SSLContext.getInstance(sSLProtocol, jSSEProvider);
        }
        if (this.logger != null) {
            Logger logger3 = this.logger;
            String str7 = "12001";
            Object[] objArr2 = new Object[2];
            objArr2[0] = str2 != null ? str2 : str4;
            objArr2[1] = sSLContext.getProvider().getName();
            logger3.fine(str6, str5, str7, objArr2);
        }
        String property = getProperty(str2, str3, null);
        if (property == null) {
            property = getProperty(str2, str3, SYSKEYSTORE);
        }
        String str8 = "null";
        if (this.logger != null) {
            Logger logger4 = this.logger;
            String str9 = "12004";
            Object[] objArr3 = new Object[2];
            objArr3[0] = str2 != null ? str2 : str4;
            objArr3[1] = property != null ? property : str8;
            logger4.fine(str6, str5, str9, objArr3);
        }
        char[] keyStorePassword = getKeyStorePassword(str);
        if (this.logger != null) {
            Logger logger5 = this.logger;
            String str10 = "12005";
            Object[] objArr4 = new Object[2];
            objArr4[0] = str2 != null ? str2 : str4;
            objArr4[1] = keyStorePassword != null ? obfuscate(keyStorePassword) : str8;
            logger5.fine(str6, str5, str10, objArr4);
        }
        String keyStoreType = getKeyStoreType(str);
        if (keyStoreType == null) {
            keyStoreType = KeyStore.getDefaultType();
        }
        if (this.logger != null) {
            Logger logger6 = this.logger;
            String str11 = "12006";
            Object[] objArr5 = new Object[2];
            objArr5[0] = str2 != null ? str2 : str4;
            objArr5[1] = keyStoreType != null ? keyStoreType : str8;
            logger6.fine(str6, str5, str11, objArr5);
        }
        String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        String keyStoreProvider = getKeyStoreProvider(str);
        String keyManager = getKeyManager(str);
        if (keyManager == null) {
            keyManager = defaultAlgorithm;
        }
        if (property == null || keyStoreType == null || keyManager == null) {
            keyManagerArr = null;
        } else {
            KeyStore instance = KeyStore.getInstance(keyStoreType);
            instance.load(new FileInputStream(property), keyStorePassword);
            if (keyStoreProvider != null) {
                keyManagerFactory = KeyManagerFactory.getInstance(keyManager, keyStoreProvider);
            } else {
                keyManagerFactory = KeyManagerFactory.getInstance(keyManager);
            }
            if (this.logger != null) {
                Logger logger7 = this.logger;
                String str12 = "12010";
                Object[] objArr6 = new Object[2];
                objArr6[0] = str2 != null ? str2 : str4;
                if (keyManager == null) {
                    keyManager = str8;
                }
                objArr6[1] = keyManager;
                logger7.fine(str6, str5, str12, objArr6);
                Logger logger8 = this.logger;
                String str13 = "12009";
                Object[] objArr7 = new Object[2];
                objArr7[0] = str2 != null ? str2 : str4;
                objArr7[1] = keyManagerFactory.getProvider().getName();
                logger8.fine(str6, str5, str13, objArr7);
            }
            keyManagerFactory.init(instance, keyStorePassword);
            keyManagerArr = keyManagerFactory.getKeyManagers();
        }
        String trustStore = getTrustStore(str);
        if (this.logger != null) {
            Logger logger9 = this.logger;
            String str14 = "12011";
            Object[] objArr8 = new Object[2];
            objArr8[0] = str2 != null ? str2 : str4;
            objArr8[1] = trustStore != null ? trustStore : str8;
            logger9.fine(str6, str5, str14, objArr8);
        }
        char[] trustStorePassword = getTrustStorePassword(str);
        if (this.logger != null) {
            Logger logger10 = this.logger;
            String str15 = "12012";
            Object[] objArr9 = new Object[2];
            objArr9[0] = str2 != null ? str2 : str4;
            objArr9[1] = trustStorePassword != null ? obfuscate(trustStorePassword) : str8;
            logger10.fine(str6, str5, str15, objArr9);
        }
        String trustStoreType = getTrustStoreType(str);
        if (trustStoreType == null) {
            trustStoreType = KeyStore.getDefaultType();
        }
        if (this.logger != null) {
            Logger logger11 = this.logger;
            String str16 = "12013";
            Object[] objArr10 = new Object[2];
            objArr10[0] = str2 != null ? str2 : str4;
            objArr10[1] = trustStoreType != null ? trustStoreType : str8;
            logger11.fine(str6, str5, str16, objArr10);
        }
        String defaultAlgorithm2 = TrustManagerFactory.getDefaultAlgorithm();
        String trustStoreProvider = getTrustStoreProvider(str);
        String trustManager = getTrustManager(str);
        if (trustManager == null) {
            trustManager = defaultAlgorithm2;
        }
        if (trustStore == null || trustStoreType == null || trustManager == null) {
            trustManagerArr = null;
        } else {
            KeyStore instance2 = KeyStore.getInstance(trustStoreType);
            instance2.load(new FileInputStream(trustStore), trustStorePassword);
            if (trustStoreProvider != null) {
                trustManagerFactory = TrustManagerFactory.getInstance(trustManager, trustStoreProvider);
            } else {
                trustManagerFactory = TrustManagerFactory.getInstance(trustManager);
            }
            if (this.logger != null) {
                Logger logger12 = this.logger;
                String str17 = "12017";
                Object[] objArr11 = new Object[2];
                objArr11[0] = str2 != null ? str2 : str4;
                if (trustManager == null) {
                    trustManager = str8;
                }
                objArr11[1] = trustManager;
                logger12.fine(str6, str5, str17, objArr11);
                Logger logger13 = this.logger;
                String str18 = "12016";
                Object[] objArr12 = new Object[2];
                if (str2 == null) {
                    str2 = str4;
                }
                objArr12[0] = str2;
                objArr12[1] = trustManagerFactory.getProvider().getName();
                logger13.fine(str6, str5, str18, objArr12);
            }
            trustManagerFactory.init(instance2);
            trustManagerArr = trustManagerFactory.getTrustManagers();
        }
        sSLContext.init(keyManagerArr, trustManagerArr, null);
        return sSLContext;
    }

    public SSLSocketFactory createSocketFactory(String str) throws MqttSecurityException {
        SSLContext sSLContext = getSSLContext(str);
        Logger logger2 = this.logger;
        if (logger2 != null) {
            Object[] objArr = new Object[2];
            objArr[0] = str != null ? str : "null (broker defaults)";
            objArr[1] = getEnabledCipherSuites(str) != null ? getProperty(str, CIPHERSUITES, null) : "null (using platform-enabled cipher suites)";
            logger2.fine(CLASS_NAME, "createSocketFactory", "12020", objArr);
        }
        return sSLContext.getSocketFactory();
    }
}
