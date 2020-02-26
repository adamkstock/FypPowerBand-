package org.eclipse.paho.client.mqttv3.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.eclipse.paho.client.mqttv3.MqttException;

public class LocalNetworkModule implements NetworkModule {
    static /* synthetic */ Class class$0;
    private String brokerName;
    private Object localAdapter;
    private Class localListener;

    public LocalNetworkModule(String str) {
        this.brokerName = str;
    }

    public void start() throws IOException, MqttException {
        String str = "com.ibm.mqttdirect.modules.local.bindings.localListener";
        if (ExceptionHelper.isClassAvailable(str)) {
            try {
                this.localListener = Class.forName(str);
                Class cls = this.localListener;
                String str2 = MqttServiceConstants.CONNECT_ACTION;
                Class[] clsArr = new Class[1];
                Class cls2 = class$0;
                if (cls2 == null) {
                    cls2 = Class.forName("java.lang.String");
                    class$0 = cls2;
                }
                clsArr[0] = cls2;
                this.localAdapter = cls.getMethod(str2, clsArr).invoke(null, new Object[]{this.brokerName});
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            } catch (Exception unused) {
            }
            if (this.localAdapter == null) {
                throw ExceptionHelper.createMqttException(32103);
            }
            return;
        }
        throw ExceptionHelper.createMqttException(32103);
    }

    public InputStream getInputStream() throws IOException {
        try {
            return (InputStream) this.localListener.getMethod("getClientInputStream", new Class[0]).invoke(this.localAdapter, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    public OutputStream getOutputStream() throws IOException {
        try {
            return (OutputStream) this.localListener.getMethod("getClientOutputStream", new Class[0]).invoke(this.localAdapter, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    public void stop() throws IOException {
        if (this.localAdapter != null) {
            try {
                this.localListener.getMethod("close", new Class[0]).invoke(this.localAdapter, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }
}
