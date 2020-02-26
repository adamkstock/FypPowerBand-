package org.eclipse.paho.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.p000v4.content.LocalBroadcastManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class MqttService extends Service implements MqttTraceHandler {
    static final String TAG = "MqttService";
    /* access modifiers changed from: private */
    public volatile boolean backgroundDataEnabled = true;
    private BackgroundDataPreferenceReceiver backgroundDataPreferenceMonitor;
    private Map<String, MqttConnection> connections = new ConcurrentHashMap();
    MessageStore messageStore;
    private MqttServiceBinder mqttServiceBinder;
    private NetworkConnectionIntentReceiver networkConnectionMonitor;
    private String traceCallbackId;
    private boolean traceEnabled = false;

    private class BackgroundDataPreferenceReceiver extends BroadcastReceiver {
        private BackgroundDataPreferenceReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) MqttService.this.getSystemService("connectivity");
            MqttService.this.traceDebug("MqttService", "Reconnect since BroadcastReceiver.");
            if (!connectivityManager.getBackgroundDataSetting()) {
                MqttService.this.backgroundDataEnabled = false;
                MqttService.this.notifyClientsOffline();
            } else if (!MqttService.this.backgroundDataEnabled) {
                MqttService.this.backgroundDataEnabled = true;
                MqttService.this.reconnect();
            }
        }
    }

    private class NetworkConnectionIntentReceiver extends BroadcastReceiver {
        private NetworkConnectionIntentReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String str = "MqttService";
            MqttService.this.traceDebug(str, "Internal network status receive.");
            WakeLock newWakeLock = ((PowerManager) MqttService.this.getSystemService("power")).newWakeLock(1, "MQTT");
            newWakeLock.acquire();
            MqttService.this.traceDebug(str, "Reconnect for Network recovery.");
            if (MqttService.this.isOnline()) {
                MqttService.this.traceDebug(str, "Online,reconnect.");
                MqttService.this.reconnect();
            } else {
                MqttService.this.notifyClientsOffline();
            }
            newWakeLock.release();
        }
    }

    /* access modifiers changed from: 0000 */
    public void callbackToActivity(String str, Status status, Bundle bundle) {
        Intent intent = new Intent(MqttServiceConstants.CALLBACK_TO_ACTIVITY);
        if (str != null) {
            intent.putExtra(MqttServiceConstants.CALLBACK_CLIENT_HANDLE, str);
        }
        intent.putExtra(MqttServiceConstants.CALLBACK_STATUS, status);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public String getClient(String str, String str2, String str3, MqttClientPersistence mqttClientPersistence) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        String str4 = ":";
        sb.append(str4);
        sb.append(str2);
        sb.append(str4);
        sb.append(str3);
        String sb2 = sb.toString();
        if (!this.connections.containsKey(sb2)) {
            MqttConnection mqttConnection = new MqttConnection(this, str, str2, mqttClientPersistence, sb2);
            this.connections.put(sb2, mqttConnection);
        }
        return sb2;
    }

    public void connect(String str, MqttConnectOptions mqttConnectOptions, String str2, String str3) throws MqttSecurityException, MqttException {
        getConnection(str).connect(mqttConnectOptions, str2, str3);
    }

    /* access modifiers changed from: 0000 */
    public void reconnect() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reconnect to server, client size=");
        sb.append(this.connections.size());
        traceDebug("MqttService", sb.toString());
        for (MqttConnection mqttConnection : this.connections.values()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(mqttConnection.getClientId());
            sb2.append('/');
            sb2.append(mqttConnection.getServerURI());
            traceDebug("Reconnect Client:", sb2.toString());
            if (isOnline()) {
                mqttConnection.reconnect();
            }
        }
    }

    public void close(String str) {
        getConnection(str).close();
    }

    public void disconnect(String str, String str2, String str3) {
        getConnection(str).disconnect(str2, str3);
        this.connections.remove(str);
        stopSelf();
    }

    public void disconnect(String str, long j, String str2, String str3) {
        getConnection(str).disconnect(j, str2, str3);
        this.connections.remove(str);
        stopSelf();
    }

    public boolean isConnected(String str) {
        return getConnection(str).isConnected();
    }

    public IMqttDeliveryToken publish(String str, String str2, byte[] bArr, int i, boolean z, String str3, String str4) throws MqttPersistenceException, MqttException {
        return getConnection(str).publish(str2, bArr, i, z, str3, str4);
    }

    public IMqttDeliveryToken publish(String str, String str2, MqttMessage mqttMessage, String str3, String str4) throws MqttPersistenceException, MqttException {
        return getConnection(str).publish(str2, mqttMessage, str3, str4);
    }

    public void subscribe(String str, String str2, int i, String str3, String str4) {
        getConnection(str).subscribe(str2, i, str3, str4);
    }

    public void subscribe(String str, String[] strArr, int[] iArr, String str2, String str3) {
        getConnection(str).subscribe(strArr, iArr, str2, str3);
    }

    public void unsubscribe(String str, String str2, String str3, String str4) {
        getConnection(str).unsubscribe(str2, str3, str4);
    }

    public void unsubscribe(String str, String[] strArr, String str2, String str3) {
        getConnection(str).unsubscribe(strArr, str2, str3);
    }

    public IMqttDeliveryToken[] getPendingDeliveryTokens(String str) {
        return getConnection(str).getPendingDeliveryTokens();
    }

    private MqttConnection getConnection(String str) {
        MqttConnection mqttConnection = (MqttConnection) this.connections.get(str);
        if (mqttConnection != null) {
            return mqttConnection;
        }
        throw new IllegalArgumentException("Invalid ClientHandle");
    }

    public Status acknowledgeMessageArrival(String str, String str2) {
        if (this.messageStore.discardArrived(str, str2)) {
            return Status.OK;
        }
        return Status.ERROR;
    }

    public void onCreate() {
        super.onCreate();
        this.mqttServiceBinder = new MqttServiceBinder(this);
        this.messageStore = new DatabaseMessageStore(this, this);
    }

    public void onDestroy() {
        for (MqttConnection disconnect : this.connections.values()) {
            disconnect.disconnect(null, null);
        }
        if (this.mqttServiceBinder != null) {
            this.mqttServiceBinder = null;
        }
        unregisterBroadcastReceivers();
        MessageStore messageStore2 = this.messageStore;
        if (messageStore2 != null) {
            messageStore2.close();
        }
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        this.mqttServiceBinder.setActivityToken(intent.getStringExtra(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN));
        return this.mqttServiceBinder;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        registerBroadcastReceivers();
        return 1;
    }

    public void setTraceCallbackId(String str) {
        this.traceCallbackId = str;
    }

    public void setTraceEnabled(boolean z) {
        this.traceEnabled = z;
    }

    public boolean isTraceEnabled() {
        return this.traceEnabled;
    }

    public void traceDebug(String str, String str2) {
        traceCallback(MqttServiceConstants.TRACE_DEBUG, str, str2);
    }

    public void traceError(String str, String str2) {
        traceCallback(MqttServiceConstants.TRACE_ERROR, str, str2);
    }

    private void traceCallback(String str, String str2, String str3) {
        if (this.traceCallbackId != null && this.traceEnabled) {
            Bundle bundle = new Bundle();
            bundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.TRACE_ACTION);
            bundle.putString(MqttServiceConstants.CALLBACK_TRACE_SEVERITY, str);
            bundle.putString(MqttServiceConstants.CALLBACK_TRACE_TAG, str2);
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str3);
            callbackToActivity(this.traceCallbackId, Status.ERROR, bundle);
        }
    }

    public void traceException(String str, String str2, Exception exc) {
        if (this.traceCallbackId != null) {
            Bundle bundle = new Bundle();
            bundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.TRACE_ACTION);
            bundle.putString(MqttServiceConstants.CALLBACK_TRACE_SEVERITY, MqttServiceConstants.TRACE_EXCEPTION);
            bundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, str2);
            bundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, exc);
            bundle.putString(MqttServiceConstants.CALLBACK_TRACE_TAG, str);
            callbackToActivity(this.traceCallbackId, Status.ERROR, bundle);
        }
    }

    private void registerBroadcastReceivers() {
        if (this.networkConnectionMonitor == null) {
            this.networkConnectionMonitor = new NetworkConnectionIntentReceiver();
            registerReceiver(this.networkConnectionMonitor, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
        if (VERSION.SDK_INT < 14) {
            this.backgroundDataEnabled = ((ConnectivityManager) getSystemService("connectivity")).getBackgroundDataSetting();
            if (this.backgroundDataPreferenceMonitor == null) {
                this.backgroundDataPreferenceMonitor = new BackgroundDataPreferenceReceiver();
                registerReceiver(this.backgroundDataPreferenceMonitor, new IntentFilter("android.net.conn.BACKGROUND_DATA_SETTING_CHANGED"));
            }
        }
    }

    private void unregisterBroadcastReceivers() {
        NetworkConnectionIntentReceiver networkConnectionIntentReceiver = this.networkConnectionMonitor;
        if (networkConnectionIntentReceiver != null) {
            unregisterReceiver(networkConnectionIntentReceiver);
            this.networkConnectionMonitor = null;
        }
        if (VERSION.SDK_INT < 14) {
            BackgroundDataPreferenceReceiver backgroundDataPreferenceReceiver = this.backgroundDataPreferenceMonitor;
            if (backgroundDataPreferenceReceiver != null) {
                unregisterReceiver(backgroundDataPreferenceReceiver);
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected() && this.backgroundDataEnabled;
    }

    public void notifyClientsOffline() {
        for (MqttConnection offline : this.connections.values()) {
            offline.offline();
        }
    }
}
