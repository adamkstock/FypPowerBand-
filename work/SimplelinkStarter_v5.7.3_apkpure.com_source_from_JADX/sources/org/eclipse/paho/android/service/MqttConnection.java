package org.eclipse.paho.android.service;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.paho.android.service.MessageStore.StoredMessage;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

class MqttConnection implements MqttCallback {
    private static final String NOT_CONNECTED = "not connected";
    private static final String TAG = "MqttConnection";
    private boolean cleanSession = true;
    /* access modifiers changed from: private */
    public String clientHandle;
    private String clientId;
    private MqttConnectOptions connectOptions;
    private volatile boolean disconnected = true;
    private volatile boolean isConnecting = false;
    private MqttAsyncClient myClient = null;
    private MqttClientPersistence persistence = null;
    private String reconnectActivityToken = null;
    private Map<IMqttDeliveryToken, String> savedActivityTokens = new HashMap();
    private Map<IMqttDeliveryToken, String> savedInvocationContexts = new HashMap();
    private Map<IMqttDeliveryToken, MqttMessage> savedSentMessages = new HashMap();
    private Map<IMqttDeliveryToken, String> savedTopics = new HashMap();
    private String serverURI;
    /* access modifiers changed from: private */
    public MqttService service = null;
    private String wakeLockTag = null;
    private WakeLock wakelock = null;

    private class MqttConnectionListener implements IMqttActionListener {
        private final Bundle resultBundle;

        private MqttConnectionListener(Bundle bundle) {
            this.resultBundle = bundle;
        }

        public void onSuccess(IMqttToken iMqttToken) {
            MqttConnection.this.service.callbackToActivity(MqttConnection.this.clientHandle, Status.OK, this.resultBundle);
        }

        public void onFailure(IMqttToken iMqttToken, Throwable th) {
            this.resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, th.getLocalizedMessage());
            this.resultBundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, th);
            MqttConnection.this.service.callbackToActivity(MqttConnection.this.clientHandle, Status.ERROR, this.resultBundle);
        }
    }

    public String getServerURI() {
        return this.serverURI;
    }

    public void setServerURI(String str) {
        this.serverURI = str;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String str) {
        this.clientId = str;
    }

    public MqttConnectOptions getConnectOptions() {
        return this.connectOptions;
    }

    public void setConnectOptions(MqttConnectOptions mqttConnectOptions) {
        this.connectOptions = mqttConnectOptions;
    }

    public String getClientHandle() {
        return this.clientHandle;
    }

    public void setClientHandle(String str) {
        this.clientHandle = str;
    }

    MqttConnection(MqttService mqttService, String str, String str2, MqttClientPersistence mqttClientPersistence, String str3) {
        this.serverURI = str.toString();
        this.service = mqttService;
        this.clientId = str2;
        this.persistence = mqttClientPersistence;
        this.clientHandle = str3;
        StringBuffer stringBuffer = new StringBuffer(getClass().getCanonicalName());
        String str4 = " ";
        stringBuffer.append(str4);
        stringBuffer.append(str2);
        stringBuffer.append(str4);
        stringBuffer.append("on host ");
        stringBuffer.append(str);
        this.wakeLockTag = stringBuffer.toString();
    }

    public void connect(MqttConnectOptions mqttConnectOptions, String str, String str2) {
        this.connectOptions = mqttConnectOptions;
        this.reconnectActivityToken = str2;
        if (mqttConnectOptions != null) {
            this.cleanSession = mqttConnectOptions.isCleanSession();
        }
        if (this.connectOptions.isCleanSession()) {
            this.service.messageStore.clearArrivedMessages(this.clientHandle);
        }
        MqttService mqttService = this.service;
        StringBuilder sb = new StringBuilder();
        sb.append("Connecting {");
        sb.append(this.serverURI);
        sb.append("} as {");
        sb.append(this.clientId);
        sb.append("}");
        String sb2 = sb.toString();
        String str3 = TAG;
        mqttService.traceDebug(str3, sb2);
        final Bundle bundle = new Bundle();
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str2);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str);
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.CONNECT_ACTION);
        try {
            if (this.persistence == null) {
                File externalFilesDir = this.service.getExternalFilesDir(str3);
                if (externalFilesDir == null) {
                    externalFilesDir = this.service.getDir(str3, 0);
                    if (externalFilesDir == null) {
                        bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, "Error! No external and internal storage available");
                        bundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, new MqttPersistenceException());
                        this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
                        return;
                    }
                }
                this.persistence = new MqttDefaultFilePersistence(externalFilesDir.getAbsolutePath());
            }
            C11061 r12 = new MqttConnectionListener(bundle) {
                public void onSuccess(IMqttToken iMqttToken) {
                    MqttConnection.this.doAfterConnectSuccess(bundle);
                    MqttConnection.this.service.traceDebug(MqttConnection.TAG, "connect success!");
                }

                public void onFailure(IMqttToken iMqttToken, Throwable th) {
                    bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, th.getLocalizedMessage());
                    bundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, th);
                    MqttService access$200 = MqttConnection.this.service;
                    StringBuilder sb = new StringBuilder();
                    sb.append("connect fail, call connect to reconnect.reason:");
                    sb.append(th.getMessage());
                    access$200.traceError(MqttConnection.TAG, sb.toString());
                    MqttConnection.this.doAfterConnectFail(bundle);
                }
            };
            String str4 = "Do Real connect!";
            if (this.myClient == null) {
                this.myClient = new MqttAsyncClient(this.serverURI, this.clientId, this.persistence, new AlarmPingSender(this.service));
                this.myClient.setCallback(this);
                this.service.traceDebug(str3, str4);
                setConnectingState(true);
                this.myClient.connect(this.connectOptions, str, r12);
            } else if (this.isConnecting) {
                this.service.traceDebug(str3, "myClient != null and the client is connecting. Connect return directly.");
                MqttService mqttService2 = this.service;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Connect return:isConnecting:");
                sb3.append(this.isConnecting);
                sb3.append(".disconnected:");
                sb3.append(this.disconnected);
                mqttService2.traceDebug(str3, sb3.toString());
            } else if (!this.disconnected) {
                this.service.traceDebug(str3, "myClient != null and the client is connected and notify!");
                doAfterConnectSuccess(bundle);
            } else {
                this.service.traceDebug(str3, "myClient != null and the client is not connected");
                this.service.traceDebug(str3, str4);
                setConnectingState(true);
                this.myClient.connect(this.connectOptions, str, r12);
            }
        } catch (Exception e) {
            handleException(bundle, e);
        }
    }

    /* access modifiers changed from: private */
    public void doAfterConnectSuccess(Bundle bundle) {
        acquireWakeLock();
        this.service.callbackToActivity(this.clientHandle, Status.OK, bundle);
        deliverBacklog();
        setConnectingState(false);
        this.disconnected = false;
        releaseWakeLock();
    }

    /* access modifiers changed from: private */
    public void doAfterConnectFail(Bundle bundle) {
        acquireWakeLock();
        this.disconnected = true;
        setConnectingState(false);
        this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
        releaseWakeLock();
    }

    private void handleException(Bundle bundle, Exception exc) {
        bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, exc.getLocalizedMessage());
        bundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, exc);
        this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
    }

    private void deliverBacklog() {
        Iterator allArrivedMessages = this.service.messageStore.getAllArrivedMessages(this.clientHandle);
        while (allArrivedMessages.hasNext()) {
            StoredMessage storedMessage = (StoredMessage) allArrivedMessages.next();
            Bundle messageToBundle = messageToBundle(storedMessage.getMessageId(), storedMessage.getTopic(), storedMessage.getMessage());
            messageToBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.MESSAGE_ARRIVED_ACTION);
            this.service.callbackToActivity(this.clientHandle, Status.OK, messageToBundle);
        }
    }

    private Bundle messageToBundle(String str, String str2, MqttMessage mqttMessage) {
        Bundle bundle = new Bundle();
        bundle.putString(MqttServiceConstants.CALLBACK_MESSAGE_ID, str);
        bundle.putString(MqttServiceConstants.CALLBACK_DESTINATION_NAME, str2);
        bundle.putParcelable(MqttServiceConstants.CALLBACK_MESSAGE_PARCEL, new ParcelableMqttMessage(mqttMessage));
        return bundle;
    }

    /* access modifiers changed from: 0000 */
    public void close() {
        this.service.traceDebug(TAG, "close()");
        try {
            if (this.myClient != null) {
                this.myClient.close();
            }
        } catch (MqttException e) {
            handleException(new Bundle(), e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void disconnect(long j, String str, String str2) {
        this.service.traceDebug(TAG, "disconnect()");
        this.disconnected = true;
        Bundle bundle = new Bundle();
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str2);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str);
        String str3 = MqttServiceConstants.DISCONNECT_ACTION;
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, str3);
        MqttAsyncClient mqttAsyncClient = this.myClient;
        if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
            String str4 = NOT_CONNECTED;
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str4);
            this.service.traceError(str3, str4);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
        } else {
            try {
                this.myClient.disconnect(j, str, new MqttConnectionListener(bundle));
            } catch (Exception e) {
                handleException(bundle, e);
            }
        }
        if (this.connectOptions.isCleanSession()) {
            this.service.messageStore.clearArrivedMessages(this.clientHandle);
        }
        releaseWakeLock();
    }

    /* access modifiers changed from: 0000 */
    public void disconnect(String str, String str2) {
        this.service.traceDebug(TAG, "disconnect()");
        this.disconnected = true;
        Bundle bundle = new Bundle();
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str2);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str);
        String str3 = MqttServiceConstants.DISCONNECT_ACTION;
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, str3);
        MqttAsyncClient mqttAsyncClient = this.myClient;
        if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
            String str4 = NOT_CONNECTED;
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str4);
            this.service.traceError(str3, str4);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
        } else {
            try {
                this.myClient.disconnect(str, new MqttConnectionListener(bundle));
            } catch (Exception e) {
                handleException(bundle, e);
            }
        }
        if (this.connectOptions.isCleanSession()) {
            this.service.messageStore.clearArrivedMessages(this.clientHandle);
        }
        releaseWakeLock();
    }

    public boolean isConnected() {
        MqttAsyncClient mqttAsyncClient = this.myClient;
        if (mqttAsyncClient != null) {
            return mqttAsyncClient.isConnected();
        }
        return false;
    }

    public IMqttDeliveryToken publish(String str, byte[] bArr, int i, boolean z, String str2, String str3) {
        Bundle bundle = new Bundle();
        String str4 = MqttServiceConstants.SEND_ACTION;
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, str4);
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str3);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str2);
        MqttAsyncClient mqttAsyncClient = this.myClient;
        IMqttDeliveryToken iMqttDeliveryToken = null;
        if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
            String str5 = NOT_CONNECTED;
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str5);
            this.service.traceError(str4, str5);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
            return null;
        }
        MqttConnectionListener mqttConnectionListener = new MqttConnectionListener(bundle);
        try {
            MqttMessage mqttMessage = new MqttMessage(bArr);
            mqttMessage.setQos(i);
            mqttMessage.setRetained(z);
            IMqttDeliveryToken publish = this.myClient.publish(str, bArr, i, z, str2, mqttConnectionListener);
            try {
                storeSendDetails(str, mqttMessage, publish, str2, str3);
                return publish;
            } catch (Exception e) {
                e = e;
                iMqttDeliveryToken = publish;
                handleException(bundle, e);
                return iMqttDeliveryToken;
            }
        } catch (Exception e2) {
            e = e2;
            handleException(bundle, e);
            return iMqttDeliveryToken;
        }
    }

    public IMqttDeliveryToken publish(String str, MqttMessage mqttMessage, String str2, String str3) {
        Bundle bundle = new Bundle();
        String str4 = MqttServiceConstants.SEND_ACTION;
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, str4);
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str3);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str2);
        MqttAsyncClient mqttAsyncClient = this.myClient;
        IMqttDeliveryToken iMqttDeliveryToken = null;
        if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
            String str5 = NOT_CONNECTED;
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str5);
            this.service.traceError(str4, str5);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
            return null;
        }
        try {
            IMqttDeliveryToken publish = this.myClient.publish(str, mqttMessage, (Object) str2, (IMqttActionListener) new MqttConnectionListener(bundle));
            try {
                storeSendDetails(str, mqttMessage, publish, str2, str3);
                return publish;
            } catch (Exception e) {
                e = e;
                iMqttDeliveryToken = publish;
                handleException(bundle, e);
                return iMqttDeliveryToken;
            }
        } catch (Exception e2) {
            e = e2;
            handleException(bundle, e);
            return iMqttDeliveryToken;
        }
    }

    public void subscribe(String str, int i, String str2, String str3) {
        MqttService mqttService = this.service;
        StringBuilder sb = new StringBuilder();
        sb.append("subscribe({");
        sb.append(str);
        sb.append("},");
        sb.append(i);
        sb.append(",{");
        sb.append(str2);
        sb.append("}, {");
        sb.append(str3);
        sb.append("}");
        mqttService.traceDebug(TAG, sb.toString());
        Bundle bundle = new Bundle();
        String str4 = MqttServiceConstants.SUBSCRIBE_ACTION;
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, str4);
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str3);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str2);
        MqttAsyncClient mqttAsyncClient = this.myClient;
        if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
            String str5 = NOT_CONNECTED;
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str5);
            this.service.traceError(str4, str5);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
            return;
        }
        try {
            this.myClient.subscribe(str, i, (Object) str2, (IMqttActionListener) new MqttConnectionListener(bundle));
        } catch (Exception e) {
            handleException(bundle, e);
        }
    }

    public void subscribe(String[] strArr, int[] iArr, String str, String str2) {
        MqttService mqttService = this.service;
        StringBuilder sb = new StringBuilder();
        sb.append("subscribe({");
        sb.append(strArr);
        sb.append("},");
        sb.append(iArr);
        sb.append(",{");
        sb.append(str);
        sb.append("}, {");
        sb.append(str2);
        sb.append("}");
        mqttService.traceDebug(TAG, sb.toString());
        Bundle bundle = new Bundle();
        String str3 = MqttServiceConstants.SUBSCRIBE_ACTION;
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, str3);
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str2);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str);
        MqttAsyncClient mqttAsyncClient = this.myClient;
        if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
            String str4 = NOT_CONNECTED;
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str4);
            this.service.traceError(str3, str4);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
            return;
        }
        try {
            this.myClient.subscribe(strArr, iArr, (Object) str, (IMqttActionListener) new MqttConnectionListener(bundle));
        } catch (Exception e) {
            handleException(bundle, e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void unsubscribe(String str, String str2, String str3) {
        MqttService mqttService = this.service;
        StringBuilder sb = new StringBuilder();
        sb.append("unsubscribe({");
        sb.append(str);
        sb.append("},{");
        sb.append(str2);
        sb.append("}, {");
        sb.append(str3);
        sb.append("})");
        mqttService.traceDebug(TAG, sb.toString());
        Bundle bundle = new Bundle();
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.UNSUBSCRIBE_ACTION);
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str3);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str2);
        MqttAsyncClient mqttAsyncClient = this.myClient;
        if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
            String str4 = NOT_CONNECTED;
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str4);
            this.service.traceError(MqttServiceConstants.SUBSCRIBE_ACTION, str4);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
            return;
        }
        try {
            this.myClient.unsubscribe(str, (Object) str2, (IMqttActionListener) new MqttConnectionListener(bundle));
        } catch (Exception e) {
            handleException(bundle, e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void unsubscribe(String[] strArr, String str, String str2) {
        MqttService mqttService = this.service;
        StringBuilder sb = new StringBuilder();
        sb.append("unsubscribe({");
        sb.append(strArr);
        sb.append("},{");
        sb.append(str);
        sb.append("}, {");
        sb.append(str2);
        sb.append("})");
        mqttService.traceDebug(TAG, sb.toString());
        Bundle bundle = new Bundle();
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.UNSUBSCRIBE_ACTION);
        bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str2);
        bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str);
        MqttAsyncClient mqttAsyncClient = this.myClient;
        if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
            String str3 = NOT_CONNECTED;
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str3);
            this.service.traceError(MqttServiceConstants.SUBSCRIBE_ACTION, str3);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, bundle);
            return;
        }
        try {
            this.myClient.unsubscribe(strArr, (Object) str, (IMqttActionListener) new MqttConnectionListener(bundle));
        } catch (Exception e) {
            handleException(bundle, e);
        }
    }

    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.myClient.getPendingDeliveryTokens();
    }

    public void connectionLost(Throwable th) {
        MqttService mqttService = this.service;
        StringBuilder sb = new StringBuilder();
        sb.append("connectionLost(");
        sb.append(th.getMessage());
        sb.append(")");
        mqttService.traceDebug(TAG, sb.toString());
        this.disconnected = true;
        try {
            this.myClient.disconnect(null, new IMqttActionListener() {
                public void onFailure(IMqttToken iMqttToken, Throwable th) {
                }

                public void onSuccess(IMqttToken iMqttToken) {
                }
            });
        } catch (Exception unused) {
        }
        Bundle bundle = new Bundle();
        bundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.ON_CONNECTION_LOST_ACTION);
        if (th != null) {
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, th.getMessage());
            if (th instanceof MqttException) {
                bundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, th);
            }
            bundle.putString(MqttServiceConstants.CALLBACK_EXCEPTION_STACK, Log.getStackTraceString(th));
        }
        this.service.callbackToActivity(this.clientHandle, Status.OK, bundle);
        releaseWakeLock();
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        MqttService mqttService = this.service;
        StringBuilder sb = new StringBuilder();
        sb.append("deliveryComplete(");
        sb.append(iMqttDeliveryToken);
        sb.append(")");
        mqttService.traceDebug(TAG, sb.toString());
        MqttMessage mqttMessage = (MqttMessage) this.savedSentMessages.remove(iMqttDeliveryToken);
        if (mqttMessage != null) {
            String str = (String) this.savedTopics.remove(iMqttDeliveryToken);
            String str2 = (String) this.savedActivityTokens.remove(iMqttDeliveryToken);
            String str3 = (String) this.savedInvocationContexts.remove(iMqttDeliveryToken);
            Bundle messageToBundle = messageToBundle(null, str, mqttMessage);
            String str4 = MqttServiceConstants.CALLBACK_ACTION;
            if (str2 != null) {
                messageToBundle.putString(str4, MqttServiceConstants.SEND_ACTION);
                messageToBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, str2);
                messageToBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, str3);
                this.service.callbackToActivity(this.clientHandle, Status.OK, messageToBundle);
            }
            messageToBundle.putString(str4, MqttServiceConstants.MESSAGE_DELIVERED_ACTION);
            this.service.callbackToActivity(this.clientHandle, Status.OK, messageToBundle);
        }
    }

    public void messageArrived(String str, MqttMessage mqttMessage) throws Exception {
        MqttService mqttService = this.service;
        StringBuilder sb = new StringBuilder();
        sb.append("messageArrived(");
        sb.append(str);
        sb.append(",{");
        sb.append(mqttMessage.toString());
        sb.append("})");
        mqttService.traceDebug(TAG, sb.toString());
        String storeArrived = this.service.messageStore.storeArrived(this.clientHandle, str, mqttMessage);
        Bundle messageToBundle = messageToBundle(storeArrived, str, mqttMessage);
        messageToBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.MESSAGE_ARRIVED_ACTION);
        messageToBundle.putString(MqttServiceConstants.CALLBACK_MESSAGE_ID, storeArrived);
        this.service.callbackToActivity(this.clientHandle, Status.OK, messageToBundle);
    }

    private void storeSendDetails(String str, MqttMessage mqttMessage, IMqttDeliveryToken iMqttDeliveryToken, String str2, String str3) {
        this.savedTopics.put(iMqttDeliveryToken, str);
        this.savedSentMessages.put(iMqttDeliveryToken, mqttMessage);
        this.savedActivityTokens.put(iMqttDeliveryToken, str3);
        this.savedInvocationContexts.put(iMqttDeliveryToken, str2);
    }

    private void acquireWakeLock() {
        if (this.wakelock == null) {
            this.wakelock = ((PowerManager) this.service.getSystemService("power")).newWakeLock(1, this.wakeLockTag);
        }
        this.wakelock.acquire();
    }

    private void releaseWakeLock() {
        WakeLock wakeLock = this.wakelock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.wakelock.release();
        }
    }

    /* access modifiers changed from: 0000 */
    public void offline() {
        if (!this.disconnected && !this.cleanSession) {
            connectionLost(new Exception("Android offline"));
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void reconnect() {
        if (this.isConnecting) {
            this.service.traceDebug(TAG, "The client is connecting. Reconnect return directly.");
            return;
        } else if (!this.service.isOnline()) {
            this.service.traceDebug(TAG, "The network is not reachable. Will not do reconnect");
            return;
        } else if (this.disconnected && !this.cleanSession) {
            this.service.traceDebug(TAG, "Do Real Reconnect!");
            final Bundle bundle = new Bundle();
            bundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, this.reconnectActivityToken);
            bundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, null);
            bundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.CONNECT_ACTION);
            try {
                this.myClient.connect(this.connectOptions, null, new MqttConnectionListener(bundle) {
                    public void onSuccess(IMqttToken iMqttToken) {
                        MqttService access$200 = MqttConnection.this.service;
                        String str = MqttConnection.TAG;
                        access$200.traceDebug(str, "Reconnect Success!");
                        MqttConnection.this.service.traceDebug(str, "DeliverBacklog when reconnect.");
                        MqttConnection.this.doAfterConnectSuccess(bundle);
                    }

                    public void onFailure(IMqttToken iMqttToken, Throwable th) {
                        bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, th.getLocalizedMessage());
                        bundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, th);
                        MqttConnection.this.service.callbackToActivity(MqttConnection.this.clientHandle, Status.ERROR, bundle);
                        MqttConnection.this.doAfterConnectFail(bundle);
                    }
                });
                setConnectingState(true);
            } catch (MqttException e) {
                MqttService mqttService = this.service;
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot reconnect to remote server.");
                sb.append(e.getMessage());
                mqttService.traceError(str, sb.toString());
                setConnectingState(false);
                handleException(bundle, e);
            }
        }
        return;
    }

    /* access modifiers changed from: 0000 */
    public synchronized void setConnectingState(boolean z) {
        this.isConnecting = z;
    }
}
