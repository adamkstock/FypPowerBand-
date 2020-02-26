package org.eclipse.paho.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.p000v4.app.NotificationCompat;
import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;

class AlarmPingSender implements MqttPingSender {
    static final String TAG = "AlarmPingSender";
    private BroadcastReceiver alarmReceiver;
    /* access modifiers changed from: private */
    public ClientComms comms;
    private volatile boolean hasStarted = false;
    private PendingIntent pendingIntent;
    /* access modifiers changed from: private */
    public MqttService service;
    /* access modifiers changed from: private */
    public AlarmPingSender that;

    class AlarmReceiver extends BroadcastReceiver {
        /* access modifiers changed from: private */
        public String wakeLockTag;
        /* access modifiers changed from: private */
        public WakeLock wakelock;

        AlarmReceiver() {
            StringBuilder sb = new StringBuilder();
            sb.append(MqttServiceConstants.PING_WAKELOCK);
            sb.append(AlarmPingSender.this.that.comms.getClient().getClientId());
            this.wakeLockTag = sb.toString();
        }

        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.intent.extra.ALARM_COUNT", -1);
            StringBuilder sb = new StringBuilder();
            sb.append("Ping ");
            sb.append(intExtra);
            sb.append(" times.");
            String sb2 = sb.toString();
            String str = AlarmPingSender.TAG;
            Log.d(str, sb2);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Check time :");
            sb3.append(System.currentTimeMillis());
            Log.d(str, sb3.toString());
            MqttToken checkForActivity = AlarmPingSender.this.comms.checkForActivity();
            if (checkForActivity != null) {
                if (this.wakelock == null) {
                    this.wakelock = ((PowerManager) AlarmPingSender.this.service.getSystemService("power")).newWakeLock(1, this.wakeLockTag);
                }
                this.wakelock.acquire();
                checkForActivity.setActionCallback(new IMqttActionListener() {
                    public void onSuccess(IMqttToken iMqttToken) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Success. Release lock(");
                        sb.append(AlarmReceiver.this.wakeLockTag);
                        sb.append("):");
                        sb.append(System.currentTimeMillis());
                        Log.d(AlarmPingSender.TAG, sb.toString());
                        if (AlarmReceiver.this.wakelock != null && AlarmReceiver.this.wakelock.isHeld()) {
                            AlarmReceiver.this.wakelock.release();
                        }
                    }

                    public void onFailure(IMqttToken iMqttToken, Throwable th) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failure. Release lock(");
                        sb.append(AlarmReceiver.this.wakeLockTag);
                        sb.append("):");
                        sb.append(System.currentTimeMillis());
                        Log.d(AlarmPingSender.TAG, sb.toString());
                        if (AlarmReceiver.this.wakelock != null && AlarmReceiver.this.wakelock.isHeld()) {
                            AlarmReceiver.this.wakelock.release();
                        }
                    }
                });
            }
        }
    }

    public AlarmPingSender(MqttService mqttService) {
        if (mqttService != null) {
            this.service = mqttService;
            this.that = this;
            return;
        }
        throw new IllegalArgumentException("Neither service nor client can be null.");
    }

    public void init(ClientComms clientComms) {
        this.comms = clientComms;
        this.alarmReceiver = new AlarmReceiver();
    }

    public void start() {
        StringBuilder sb = new StringBuilder();
        sb.append(MqttServiceConstants.PING_SENDER);
        sb.append(this.comms.getClient().getClientId());
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Register alarmreceiver to MqttService");
        sb3.append(sb2);
        Log.d(TAG, sb3.toString());
        this.service.registerReceiver(this.alarmReceiver, new IntentFilter(sb2));
        this.pendingIntent = PendingIntent.getBroadcast(this.service, 0, new Intent(sb2), 134217728);
        schedule(this.comms.getKeepAlive());
        this.hasStarted = true;
    }

    public void stop() {
        ((AlarmManager) this.service.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(this.pendingIntent);
        StringBuilder sb = new StringBuilder();
        sb.append("Unregister alarmreceiver to MqttService");
        sb.append(this.comms.getClient().getClientId());
        Log.d(TAG, sb.toString());
        if (this.hasStarted) {
            this.hasStarted = false;
            try {
                this.service.unregisterReceiver(this.alarmReceiver);
            } catch (IllegalArgumentException unused) {
            }
        }
    }

    public void schedule(long j) {
        long currentTimeMillis = System.currentTimeMillis() + j;
        StringBuilder sb = new StringBuilder();
        sb.append("Schedule next alarm at ");
        sb.append(currentTimeMillis);
        Log.d(TAG, sb.toString());
        ((AlarmManager) this.service.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(0, currentTimeMillis, this.pendingIntent);
    }
}
