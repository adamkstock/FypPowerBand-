package com.p004ti.ble.common.cloud;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.ti.ble.simplelinkstarter.R;
import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/* renamed from: com.ti.ble.common.cloud.IBMIoTCloudProfile */
public class IBMIoTCloudProfile extends GenericBluetoothProfile {
    static IBMIoTCloudProfile mThis;
    final String addrShort;
    MqttAndroidClient client;
    BroadcastReceiver cloudConfigUpdateReceiver;
    cloudConfig config;
    public boolean isConnected;
    MemoryPersistence memPer;
    IBMIoTCloudTableRow myRow;
    Timer publishTimer;
    public boolean ready;
    final String startString = "{\n \"d\":{\n";
    final String stopString = "\n}\n}";
    Map<String, String> valueMap = new HashMap();
    private WakeLock wakeLock;

    /* renamed from: com.ti.ble.common.cloud.IBMIoTCloudProfile$MQTTTimerTask */
    class MQTTTimerTask extends TimerTask {
        MQTTTimerTask() {
        }

        public void run() {
            try {
                if (IBMIoTCloudProfile.this.ready) {
                    Activity activity = (Activity) IBMIoTCloudProfile.this.context;
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(R.mipmap.cloud_connected_tx);
                        }
                    });
                    String str = "";
                    HashMap hashMap = new HashMap();
                    hashMap.putAll(IBMIoTCloudProfile.this.valueMap);
                    for (Entry entry : hashMap.entrySet()) {
                        String str2 = (String) entry.getKey();
                        String str3 = (String) entry.getValue();
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append("\"");
                        sb.append(str2);
                        sb.append("\":\"");
                        sb.append(str3);
                        sb.append("\",\n");
                        str = sb.toString();
                    }
                    if (str.length() > 0) {
                        IBMIoTCloudProfile.this.client.publish(IBMIoTCloudProfile.this.config.publishTopic, IBMIoTCloudProfile.this.jsonEncode(str.substring(0, str.length() - 2)).getBytes(), 0, false);
                        try {
                            Thread.sleep(60);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(R.mipmap.cloud_connected);
                        }
                    });
                    return;
                }
                Log.d("IBMIoTCloudProfile", "MQTTTimerTask ran, but MQTT not ready");
            } catch (MqttException e2) {
                e2.printStackTrace();
            }
        }
    }

    /* renamed from: com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig */
    class cloudConfig {
        public String brokerAddress;
        public int brokerPort;
        public boolean cleanSession;
        public String deviceId;
        public String password;
        public String publishTopic;
        public Integer service;
        public boolean useSSL;
        public String username;

        cloudConfig() {
        }

        public String toString() {
            new String();
            StringBuilder sb = new StringBuilder();
            sb.append("Cloud configuration :\r\n");
            sb.append("Service : ");
            sb.append(this.service);
            String str = "\r\n";
            sb.append(str);
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append("Username : ");
            sb3.append(this.username);
            sb3.append(str);
            String sb4 = sb3.toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append(sb4);
            sb5.append("Password : ");
            sb5.append(this.password);
            sb5.append(str);
            String sb6 = sb5.toString();
            StringBuilder sb7 = new StringBuilder();
            sb7.append(sb6);
            sb7.append("Device ID : ");
            sb7.append(this.deviceId);
            sb7.append(str);
            String sb8 = sb7.toString();
            StringBuilder sb9 = new StringBuilder();
            sb9.append(sb8);
            sb9.append("Broker Address : ");
            sb9.append(this.brokerAddress);
            sb9.append(str);
            String sb10 = sb9.toString();
            StringBuilder sb11 = new StringBuilder();
            sb11.append(sb10);
            sb11.append("Proker Port : ");
            sb11.append(this.brokerPort);
            sb11.append(str);
            String sb12 = sb11.toString();
            StringBuilder sb13 = new StringBuilder();
            sb13.append(sb12);
            sb13.append("Publish Topic : ");
            sb13.append(this.publishTopic);
            sb13.append(str);
            String sb14 = sb13.toString();
            StringBuilder sb15 = new StringBuilder();
            sb15.append(sb14);
            sb15.append("Clean Session : ");
            sb15.append(this.cleanSession);
            sb15.append(str);
            String sb16 = sb15.toString();
            StringBuilder sb17 = new StringBuilder();
            sb17.append(sb16);
            sb17.append("Use SSL : ");
            sb17.append(this.useSSL);
            sb17.append(str);
            return sb17.toString();
        }
    }

    /* renamed from: com.ti.ble.common.cloud.IBMIoTCloudProfile$dweetIOTimerTask */
    class dweetIOTimerTask extends TimerTask {
        dweetIOTimerTask() {
        }

        public void run() {
            HttpURLConnection httpURLConnection;
            String str = "Reply Text : ";
            String str2 = "Reply from WEB Server : ";
            String str3 = "dweetIOIoT";
            try {
                Activity activity = (Activity) IBMIoTCloudProfile.this.context;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(R.mipmap.cloud_connected_tx);
                    }
                });
                String str4 = "";
                HashMap hashMap = new HashMap();
                hashMap.putAll(IBMIoTCloudProfile.this.valueMap);
                for (Entry entry : hashMap.entrySet()) {
                    String str5 = (String) entry.getKey();
                    String str6 = (String) entry.getValue();
                    StringBuilder sb = new StringBuilder();
                    sb.append(str4);
                    sb.append("\"");
                    sb.append(str5);
                    sb.append("\":\"");
                    sb.append(str6);
                    sb.append("\",\n");
                    str4 = sb.toString();
                }
                if (str4.length() > 0) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("{\n");
                    sb2.append(str4.substring(0, str4.length() - 2));
                    sb2.append("\n}");
                    String sb3 = sb2.toString();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(IBMIoTCloudProfile.this.config.brokerAddress);
                    sb4.append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
                    sb4.append(IBMIoTCloudProfile.this.config.deviceId);
                    httpURLConnection = (HttpURLConnection) new URL(sb4.toString()).openConnection();
                    httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
                    httpURLConnection.setRequestProperty(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setChunkedStreamingMode(0);
                    PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                    printWriter.println(sb3.toString());
                    printWriter.flush();
                    printWriter.close();
                    byte[] bArr = new byte[1024];
                    new BufferedInputStream(httpURLConnection.getInputStream()).read(bArr, 0, 1024);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Response body : ");
                    sb5.append(new String(bArr));
                    Log.d(str3, sb5.toString());
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str2);
                    sb6.append(httpURLConnection.getResponseCode());
                    Log.d(str3, sb6.toString());
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str);
                    sb7.append(httpURLConnection.getResponseMessage());
                    Log.d(str3, sb7.toString());
                    httpURLConnection.disconnect();
                }
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(R.mipmap.cloud_connected);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str2);
                sb8.append(httpURLConnection.getResponseCode());
                Log.d(str3, sb8.toString());
                StringBuilder sb9 = new StringBuilder();
                sb9.append(str);
                sb9.append(httpURLConnection.getResponseMessage());
                Log.d(str3, sb9.toString());
                httpURLConnection.disconnect();
                throw th;
            }
        }
    }

    public void configureService() {
    }

    public void deConfigureService() {
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    public void disableService() {
    }

    public void enableService() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x0234  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x024a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public IBMIoTCloudProfile(android.content.Context r8, android.bluetooth.BluetoothDevice r9, android.bluetooth.BluetoothGattService r10) {
        /*
            r7 = this;
            java.lang.String r0 = "sensortag2_cloudservice"
            java.lang.String r1 = ""
            r7.<init>(r8, r9, r10)
            java.lang.String r9 = "{\n \"d\":{\n"
            r7.startString = r9
            java.lang.String r9 = "\n}\n}"
            r7.stopString = r9
            java.util.HashMap r9 = new java.util.HashMap
            r9.<init>()
            r7.valueMap = r9
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r9 = new com.ti.ble.common.cloud.IBMIoTCloudTableRow
            r9.<init>(r8)
            r7.myRow = r9
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow
            r9 = 0
            r8.setOnClickListener(r9)
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r8 = r7.readCloudConfigFromPrefs()
            r7.config = r8
            r8 = 0
            r7.isConnected = r8
            android.bluetooth.BluetoothDevice r9 = r7.mBTDevice
            java.lang.String r9 = r9.getAddress()
            java.lang.String r10 = ":"
            java.lang.String[] r10 = r9.split(r10)
            r2 = 6
            int[] r3 = new int[r2]
            r4 = 0
        L_0x003c:
            if (r4 >= r2) goto L_0x004b
            r5 = r10[r4]
            r6 = 16
            int r5 = java.lang.Integer.parseInt(r5, r6)
            r3[r4] = r5
            int r4 = r4 + 1
            goto L_0x003c
        L_0x004b:
            r7.ready = r8
            java.lang.Object[] r10 = new java.lang.Object[r2]
            r2 = r3[r8]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r10[r8] = r2
            r8 = 1
            r2 = r3[r8]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r10[r8] = r2
            r8 = 2
            r2 = r3[r8]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r10[r8] = r2
            r8 = 3
            r2 = r3[r8]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r10[r8] = r2
            r8 = 4
            r2 = r3[r8]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r10[r8] = r2
            r8 = 5
            r2 = r3[r8]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r10[r8] = r2
            java.lang.String r8 = "%02x%02x%02x%02x%02x%02x"
            java.lang.String r8 = java.lang.String.format(r8, r10)
            r7.addrShort = r8
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r8 = r7.config
            java.lang.String r10 = "IBMIoTCloudProfile"
            if (r8 == 0) goto L_0x00ad
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r2 = "Stored cloud configuration\r\n"
            r8.append(r2)
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config
            java.lang.String r2 = r2.toString()
            r8.append(r2)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r10, r8)
            goto L_0x00cd
        L_0x00ad:
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r8 = r7.initPrefsWithIBMQuickStart()
            r7.config = r8
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r2 = "Stored cloud configuration was corrupt, starting new based on IBM IoT Quickstart variables"
            r8.append(r2)
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config
            java.lang.String r2 = r2.toString()
            r8.append(r2)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r10, r8)
        L_0x00cd:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r2 = "Device ID : "
            r8.append(r2)
            java.lang.String r3 = r7.addrShort
            r8.append(r3)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r10, r8)
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow
            android.widget.TextView r8 = r8.title
            java.lang.String r10 = "Cloud View"
            r8.setText(r10)
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r10 = "CC2650 SensorTag"
            boolean r8 = r8.equals(r10)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 != 0) goto L_0x01f5
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r10 = "CC1350 SensorTag"
            boolean r8 = r8.equals(r10)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 == 0) goto L_0x010a
            goto L_0x01f5
        L_0x010a:
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r10 = "Multi-Sensor"
            boolean r8 = r8.contains(r10)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 != 0) goto L_0x01ea
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r10 = "LPSTK:)"
            boolean r8 = r8.contains(r10)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 == 0) goto L_0x0128
            goto L_0x01ea
        L_0x0128:
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r10 = "SensorTag"
            boolean r8 = r8.equals(r10)     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r10 = "sensortag_cloudservice"
            if (r8 == 0) goto L_0x013f
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            r8.setIcon(r10, r1, r1)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x01fa
        L_0x013f:
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r3 = "CC2650 RC"
            boolean r8 = r8.equals(r3)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 != 0) goto L_0x01e2
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r3 = "HID AdvRemote"
            boolean r8 = r8.equals(r3)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 == 0) goto L_0x015d
            goto L_0x01e2
        L_0x015d:
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r3 = "CC2650 LaunchPad"
            boolean r8 = r8.equals(r3)     // Catch:{ NullPointerException -> 0x0211 }
            r3 = 2131492867(0x7f0c0003, float:1.8609198E38)
            if (r8 != 0) goto L_0x01da
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r4 = "CC1350 LaunchPad"
            boolean r8 = r8.equals(r4)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 == 0) goto L_0x017d
            goto L_0x01da
        L_0x017d:
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r4 = "Throughput Periph"
            boolean r8 = r8.equals(r4)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 == 0) goto L_0x0193
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            android.widget.ImageView r8 = r8.icon     // Catch:{ NullPointerException -> 0x0211 }
            r8.setImageResource(r3)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x01fa
        L_0x0193:
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r3 = "ProjectZero"
            boolean r8 = r8.equalsIgnoreCase(r3)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 != 0) goto L_0x01cf
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r3 = "Project Zero"
            boolean r8 = r8.equalsIgnoreCase(r3)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 == 0) goto L_0x01b0
            goto L_0x01cf
        L_0x01b0:
            android.bluetooth.BluetoothDevice r8 = r7.mBTDevice     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r8 = r8.getName()     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r3 = "DMM"
            boolean r8 = r8.contains(r3)     // Catch:{ NullPointerException -> 0x0211 }
            if (r8 == 0) goto L_0x01c9
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            android.widget.ImageView r8 = r8.icon     // Catch:{ NullPointerException -> 0x0211 }
            r10 = 2131492890(0x7f0c001a, float:1.8609245E38)
            r8.setImageResource(r10)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x01fa
        L_0x01c9:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            r8.setIcon(r10, r1, r1)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x01fa
        L_0x01cf:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            android.widget.ImageView r8 = r8.icon     // Catch:{ NullPointerException -> 0x0211 }
            r10 = 2131492939(0x7f0c004b, float:1.8609344E38)
            r8.setImageResource(r10)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x01fa
        L_0x01da:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            android.widget.ImageView r8 = r8.icon     // Catch:{ NullPointerException -> 0x0211 }
            r8.setImageResource(r3)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x01fa
        L_0x01e2:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r10 = "cc2650_rc_cloudservice"
            r8.setIcon(r10, r1, r1)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x01fa
        L_0x01ea:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            android.widget.ImageView r8 = r8.icon     // Catch:{ NullPointerException -> 0x0211 }
            r10 = 2131492920(0x7f0c0038, float:1.8609306E38)
            r8.setImageResource(r10)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x01fa
        L_0x01f5:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            r8.setIcon(r0, r1, r1)     // Catch:{ NullPointerException -> 0x0211 }
        L_0x01fa:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow     // Catch:{ NullPointerException -> 0x0211 }
            android.widget.TextView r8 = r8.value     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ NullPointerException -> 0x0211 }
            r10.<init>()     // Catch:{ NullPointerException -> 0x0211 }
            r10.append(r2)     // Catch:{ NullPointerException -> 0x0211 }
            r10.append(r9)     // Catch:{ NullPointerException -> 0x0211 }
            java.lang.String r9 = r10.toString()     // Catch:{ NullPointerException -> 0x0211 }
            r8.setText(r9)     // Catch:{ NullPointerException -> 0x0211 }
            goto L_0x0216
        L_0x0211:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow
            r8.setIcon(r0, r1, r1)
        L_0x0216:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow
            android.widget.Switch r9 = r8.pushToCloud
            com.ti.ble.common.cloud.IBMIoTCloudProfile$1 r10 = new com.ti.ble.common.cloud.IBMIoTCloudProfile$1
            r10.<init>()
            r9.setOnCheckedChangeListener(r10)
            android.widget.Button r8 = r8.configureCloud
            com.ti.ble.common.cloud.IBMIoTCloudProfile$2 r9 = new com.ti.ble.common.cloud.IBMIoTCloudProfile$2
            r9.<init>()
            r8.setOnClickListener(r9)
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r8 = r7.config
            java.lang.Integer r8 = r8.service
            java.lang.Integer r9 = com.p004ti.ble.common.cloud.CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE
            if (r8 != r9) goto L_0x024a
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow
            android.widget.TextView r8 = r8.cloudURL
            java.lang.String r9 = "Open in browser"
            r8.setText(r9)
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow
            android.widget.TextView r8 = r8.cloudURL
            com.ti.ble.common.cloud.IBMIoTCloudProfile$3 r9 = new com.ti.ble.common.cloud.IBMIoTCloudProfile$3
            r9.<init>()
            r8.setOnClickListener(r9)
            goto L_0x025b
        L_0x024a:
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow
            android.widget.TextView r8 = r8.cloudURL
            r8.setText(r1)
            com.ti.ble.common.cloud.IBMIoTCloudTableRow r8 = r7.myRow
            android.widget.TextView r8 = r8.cloudURL
            r9 = 1036831949(0x3dcccccd, float:0.1)
            r8.setAlpha(r9)
        L_0x025b:
            mThis = r7
            com.ti.ble.common.cloud.IBMIoTCloudProfile$4 r8 = new com.ti.ble.common.cloud.IBMIoTCloudProfile$4
            r8.<init>()
            r7.cloudConfigUpdateReceiver = r8
            android.content.Context r8 = r7.context
            android.content.BroadcastReceiver r9 = r7.cloudConfigUpdateReceiver
            android.content.IntentFilter r10 = makeCloudConfigUpdateFilter()
            r8.registerReceiver(r9, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.ble.common.cloud.IBMIoTCloudProfile.<init>(android.content.Context, android.bluetooth.BluetoothDevice, android.bluetooth.BluetoothGattService):void");
    }

    public boolean disconnect() {
        try {
            this.myRow.setCloudConnectionStatusImage(R.mipmap.cloud_disconnected);
            this.ready = false;
            if (this.publishTimer != null) {
                this.publishTimer.cancel();
            }
            if (this.client != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Disconnecting from cloud : ");
                sb.append(this.client.getServerURI());
                sb.append(",");
                sb.append(this.client.getClientId());
                Log.d("IBMIoTCloudProfile", sb.toString());
                if (this.client.isConnected()) {
                    this.client.disconnect();
                }
                this.client.unregisterResources();
                this.client = null;
                this.memPer = null;
            }
            this.isConnected = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x00e0 A[Catch:{ MqttException -> 0x0123 }] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00eb A[Catch:{ MqttException -> 0x0123 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0145  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connect() {
        /*
            r7 = this;
            java.lang.String r0 = "tcp://"
            java.lang.String r1 = "IBMIoTCloudProfile"
            org.eclipse.paho.client.mqttv3.persist.MemoryPersistence r2 = new org.eclipse.paho.client.mqttv3.persist.MemoryPersistence     // Catch:{ MqttException -> 0x0123 }
            r2.<init>()     // Catch:{ MqttException -> 0x0123 }
            r7.memPer = r2     // Catch:{ MqttException -> 0x0123 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ MqttException -> 0x0123 }
            r2.<init>()     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r3 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r3 = r3.brokerAddress     // Catch:{ MqttException -> 0x0123 }
            r2.append(r3)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r3 = ":"
            r2.append(r3)     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r3 = r7.config     // Catch:{ MqttException -> 0x0123 }
            int r3 = r3.brokerPort     // Catch:{ MqttException -> 0x0123 }
            r2.append(r3)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r2.toString()     // Catch:{ MqttException -> 0x0123 }
            boolean r3 = r2.contains(r0)     // Catch:{ MqttException -> 0x0123 }
            if (r3 != 0) goto L_0x003c
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ MqttException -> 0x0123 }
            r3.<init>()     // Catch:{ MqttException -> 0x0123 }
            r3.append(r0)     // Catch:{ MqttException -> 0x0123 }
            r3.append(r2)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r3.toString()     // Catch:{ MqttException -> 0x0123 }
        L_0x003c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ MqttException -> 0x0123 }
            r0.<init>()     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r3 = "Cloud Broker URL : "
            r0.append(r3)     // Catch:{ MqttException -> 0x0123 }
            r0.append(r2)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r0 = r0.toString()     // Catch:{ MqttException -> 0x0123 }
            android.util.Log.d(r1, r0)     // Catch:{ MqttException -> 0x0123 }
            org.eclipse.paho.android.service.MqttAndroidClient r0 = new org.eclipse.paho.android.service.MqttAndroidClient     // Catch:{ MqttException -> 0x0123 }
            android.content.Context r3 = r7.context     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r4 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r4 = r4.deviceId     // Catch:{ MqttException -> 0x0123 }
            r0.<init>(r3, r2, r4)     // Catch:{ MqttException -> 0x0123 }
            r7.client = r0     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r0 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.Integer r0 = r0.service     // Catch:{ MqttException -> 0x0123 }
            java.lang.Integer r2 = com.p004ti.ble.common.cloud.CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMIOTFOUNDATION_CLOUD_SERVICE     // Catch:{ MqttException -> 0x0123 }
            r3 = 0
            if (r0 == r2) goto L_0x0071
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r0 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.Integer r0 = r0.service     // Catch:{ MqttException -> 0x0123 }
            java.lang.Integer r2 = com.p004ti.ble.common.cloud.CloudProfileConfigurationDialogFragment.DEF_CLOUD_CUSTOM_CLOUD_SERVICE     // Catch:{ MqttException -> 0x0123 }
            if (r0 != r2) goto L_0x006f
            goto L_0x0071
        L_0x006f:
            r0 = r3
            goto L_0x00d8
        L_0x0071:
            org.eclipse.paho.client.mqttv3.MqttConnectOptions r0 = new org.eclipse.paho.client.mqttv3.MqttConnectOptions     // Catch:{ MqttException -> 0x0123 }
            r0.<init>()     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            boolean r2 = r2.cleanSession     // Catch:{ MqttException -> 0x0123 }
            r0.setCleanSession(r2)     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r2.username     // Catch:{ MqttException -> 0x0123 }
            int r2 = r2.length()     // Catch:{ MqttException -> 0x0123 }
            if (r2 <= 0) goto L_0x008e
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r2.username     // Catch:{ MqttException -> 0x0123 }
            r0.setUserName(r2)     // Catch:{ MqttException -> 0x0123 }
        L_0x008e:
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r2.password     // Catch:{ MqttException -> 0x0123 }
            int r2 = r2.length()     // Catch:{ MqttException -> 0x0123 }
            if (r2 <= 0) goto L_0x00a3
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r2.password     // Catch:{ MqttException -> 0x0123 }
            char[] r2 = r2.toCharArray()     // Catch:{ MqttException -> 0x0123 }
            r0.setPassword(r2)     // Catch:{ MqttException -> 0x0123 }
        L_0x00a3:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ MqttException -> 0x0123 }
            r2.<init>()     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r4 = "Adding Options : Clean Session : "
            r2.append(r4)     // Catch:{ MqttException -> 0x0123 }
            boolean r4 = r0.isCleanSession()     // Catch:{ MqttException -> 0x0123 }
            r2.append(r4)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r4 = ", Username : "
            r2.append(r4)     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r4 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r4 = r4.username     // Catch:{ MqttException -> 0x0123 }
            r2.append(r4)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r4 = ", Password : \""
            r2.append(r4)     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r4 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r4 = r4.password     // Catch:{ MqttException -> 0x0123 }
            r2.append(r4)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r4 = "\""
            r2.append(r4)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r2.toString()     // Catch:{ MqttException -> 0x0123 }
            android.util.Log.d(r1, r2)     // Catch:{ MqttException -> 0x0123 }
        L_0x00d8:
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.Integer r2 = r2.service     // Catch:{ MqttException -> 0x0123 }
            java.lang.Integer r4 = com.p004ti.ble.common.cloud.CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMIOTFOUNDATION_CLOUD_SERVICE     // Catch:{ MqttException -> 0x0123 }
            if (r2 != r4) goto L_0x00eb
            org.eclipse.paho.android.service.MqttAndroidClient r1 = r7.client     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$5 r2 = new com.ti.ble.common.cloud.IBMIoTCloudProfile$5     // Catch:{ MqttException -> 0x0123 }
            r2.<init>()     // Catch:{ MqttException -> 0x0123 }
            r1.connect(r0, r3, r2)     // Catch:{ MqttException -> 0x0123 }
            goto L_0x0127
        L_0x00eb:
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.Integer r2 = r2.service     // Catch:{ MqttException -> 0x0123 }
            java.lang.Integer r3 = com.p004ti.ble.common.cloud.CloudProfileConfigurationDialogFragment.DEF_CLOUD_DWEET_IO_SERVICE     // Catch:{ MqttException -> 0x0123 }
            if (r2 != r3) goto L_0x0118
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ MqttException -> 0x0123 }
            r0.<init>()     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = "Connecting to cloud : "
            r0.append(r2)     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r2.brokerAddress     // Catch:{ MqttException -> 0x0123 }
            r0.append(r2)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = ","
            r0.append(r2)     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r2 = r7.config     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r2 = r2.deviceId     // Catch:{ MqttException -> 0x0123 }
            r0.append(r2)     // Catch:{ MqttException -> 0x0123 }
            java.lang.String r0 = r0.toString()     // Catch:{ MqttException -> 0x0123 }
            android.util.Log.d(r1, r0)     // Catch:{ MqttException -> 0x0123 }
            goto L_0x0127
        L_0x0118:
            org.eclipse.paho.android.service.MqttAndroidClient r1 = r7.client     // Catch:{ MqttException -> 0x0123 }
            com.ti.ble.common.cloud.IBMIoTCloudProfile$6 r2 = new com.ti.ble.common.cloud.IBMIoTCloudProfile$6     // Catch:{ MqttException -> 0x0123 }
            r2.<init>()     // Catch:{ MqttException -> 0x0123 }
            r1.connect(r0, r2)     // Catch:{ MqttException -> 0x0123 }
            goto L_0x0127
        L_0x0123:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0127:
            com.ti.ble.common.cloud.IBMIoTCloudProfile$cloudConfig r0 = r7.config
            java.lang.Integer r0 = r0.service
            java.lang.Integer r1 = com.p004ti.ble.common.cloud.CloudProfileConfigurationDialogFragment.DEF_CLOUD_DWEET_IO_SERVICE
            if (r0 != r1) goto L_0x0145
            java.util.Timer r0 = new java.util.Timer
            r0.<init>()
            r7.publishTimer = r0
            com.ti.ble.common.cloud.IBMIoTCloudProfile$dweetIOTimerTask r2 = new com.ti.ble.common.cloud.IBMIoTCloudProfile$dweetIOTimerTask
            r2.<init>()
            java.util.Timer r1 = r7.publishTimer
            r3 = 1100(0x44c, double:5.435E-321)
            r5 = 1100(0x44c, double:5.435E-321)
            r1.schedule(r2, r3, r5)
            goto L_0x015a
        L_0x0145:
            java.util.Timer r0 = new java.util.Timer
            r0.<init>()
            r7.publishTimer = r0
            com.ti.ble.common.cloud.IBMIoTCloudProfile$MQTTTimerTask r2 = new com.ti.ble.common.cloud.IBMIoTCloudProfile$MQTTTimerTask
            r2.<init>()
            java.util.Timer r1 = r7.publishTimer
            r3 = 1000(0x3e8, double:4.94E-321)
            r5 = 1000(0x3e8, double:4.94E-321)
            r1.schedule(r2, r3, r5)
        L_0x015a:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.ble.common.cloud.IBMIoTCloudProfile.connect():boolean");
    }

    public String jsonEncode(String str, String str2) {
        String str3 = new String();
        StringBuilder sb = new StringBuilder();
        sb.append(str3);
        sb.append("{\n \"d\":{\n");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        String str4 = "\"";
        sb3.append(str4);
        sb3.append(str);
        sb3.append("\":\"");
        sb3.append(str2);
        sb3.append(str4);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        sb5.append("\n}\n}");
        return sb5.toString();
    }

    public String jsonEncode(String str) {
        String str2 = new String();
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("{\n \"d\":{\n");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(str);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        sb5.append("\n}\n}");
        return sb5.toString();
    }

    public void publishString(String str) {
        new MqttMessage();
        try {
            this.client.publish(this.config.publishTopic, jsonEncode("Test", "123").getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void addSensorValueToPendingMessage(String str, String str2) {
        this.valueMap.put(str, str2);
    }

    public void addSensorValueToPendingMessage(Entry<String, String> entry) {
        this.valueMap.put(entry.getKey(), entry.getValue());
    }

    public void onPause() {
        super.onPause();
        this.context.unregisterReceiver(this.cloudConfigUpdateReceiver);
    }

    public void onResume() {
        super.onResume();
        this.context.registerReceiver(this.cloudConfigUpdateReceiver, makeCloudConfigUpdateFilter());
    }

    public static IBMIoTCloudProfile getInstance() {
        return mThis;
    }

    private static IntentFilter makeCloudConfigUpdateFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CloudProfileConfigurationDialogFragment.ACTION_CLOUD_CONFIG_WAS_UPDATED);
        return intentFilter;
    }

    public cloudConfig readCloudConfigFromPrefs() {
        cloudConfig cloudconfig = new cloudConfig();
        try {
            cloudconfig.service = Integer.valueOf(Integer.parseInt(CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_SERVICE, this.context), 10));
            cloudconfig.username = CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_USERNAME, this.context);
            cloudconfig.password = CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_PASSWORD, this.context);
            cloudconfig.deviceId = CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_DEVICE_ID, this.context);
            cloudconfig.brokerAddress = CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_BROKER_ADDR, this.context);
            cloudconfig.brokerPort = Integer.parseInt(CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_BROKER_PORT, this.context), 10);
            cloudconfig.publishTopic = CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_PUBLISH_TOPIC, this.context);
            cloudconfig.cleanSession = Boolean.parseBoolean(CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_CLEAN_SESSION, this.context));
            cloudconfig.useSSL = Boolean.parseBoolean(CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_USE_SSL, this.context));
            if (cloudconfig.service == CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE) {
                this.myRow.cloudURL.setText("Open in browser");
                this.myRow.cloudURL.setAlpha(1.0f);
                this.myRow.cloudURL.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Context context = view.getContext();
                        StringBuilder sb = new StringBuilder();
                        sb.append("https://quickstart.internetofthings.ibmcloud.com/#/device/");
                        sb.append(IBMIoTCloudProfile.this.addrShort);
                        sb.append("/sensor/");
                        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                    }
                });
            } else {
                this.myRow.cloudURL.setText("");
                this.myRow.cloudURL.setAlpha(0.1f);
            }
            return cloudconfig;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public cloudConfig initPrefsWithIBMQuickStart() {
        cloudConfig cloudconfig = new cloudConfig();
        cloudconfig.service = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE;
        String str = "";
        cloudconfig.username = str;
        cloudconfig.password = str;
        StringBuilder sb = new StringBuilder();
        sb.append(CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_DEVICEID_PREFIX);
        sb.append(this.addrShort);
        cloudconfig.deviceId = sb.toString();
        cloudconfig.brokerAddress = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_BROKER_ADDR;
        try {
            cloudconfig.brokerPort = Integer.parseInt(CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_BROKER_PORT);
        } catch (Exception unused) {
            cloudconfig.brokerPort = 1883;
        }
        cloudconfig.publishTopic = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_PUBLISH_TOPIC;
        cloudconfig.cleanSession = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLEAN_SESSION.booleanValue();
        cloudconfig.useSSL = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_USE_SSL.booleanValue();
        return cloudconfig;
    }

    public void writeCloudConfigToPrefs(cloudConfig cloudconfig) {
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_SERVICE, cloudconfig.service.toString(), this.context);
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_USERNAME, cloudconfig.username, this.context);
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_PASSWORD, cloudconfig.password, this.context);
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_DEVICE_ID, cloudconfig.deviceId, this.context);
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_BROKER_ADDR, cloudconfig.brokerAddress, this.context);
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_BROKER_PORT, Integer.valueOf(cloudconfig.brokerPort).toString(), this.context);
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_PUBLISH_TOPIC, cloudconfig.publishTopic, this.context);
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_CLEAN_SESSION, Boolean.valueOf(cloudconfig.cleanSession).toString(), this.context);
        CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_USE_SSL, Boolean.valueOf(cloudconfig.useSSL).toString(), this.context);
    }

    public TableRow getTableRow() {
        return this.myRow;
    }
}
