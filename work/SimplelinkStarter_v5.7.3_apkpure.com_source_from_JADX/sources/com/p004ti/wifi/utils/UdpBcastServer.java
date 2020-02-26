package com.p004ti.wifi.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.wifi.utils.Ping.PingCallback;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.ti.wifi.utils.UdpBcastServer */
public class UdpBcastServer {
    private static final String UDP_BCAST_SERVER_DEVICE_AGE_EXTRA = "com.ti.WiFi.utils.UDP_BCAST_SERVER_DEVICE_AGE_EXTRA";
    private static final String UDP_BCAST_SERVER_DEVICE_IP_EXTRA = "com.ti.WiFi.utils.UDP_BCAST_SERVER_DEVICE_IP_EXTRA";
    private static final String UDP_BCAST_SERVER_DEVICE_NAME_EXTRA = "com.ti.WiFi.utils.UDP_BCAST_SERVER_DEVICE_NAME_EXTRA";
    public static final String UDP_BCAST_SERVER_FOUND_DEVICE_ACTION = "com.ti.WiFi.utils.UDP_BCAST_SERVER_FOUND_DEVICE_ACTION";
    private final String TAG = "UdpBcastServer";
    private Context context;
    private PingCallback mPingCallback;
    private int portNumber1 = 1501;
    private byte[] receiveData = new byte[1024];
    /* access modifiers changed from: private */
    public DatagramSocket serverSocket1;
    public Runnable udpBcastServerRunnable = new Runnable() {
        public void run() {
            if (!Thread.interrupted()) {
                UdpBcastServer.this.before();
                if (!Thread.interrupted()) {
                    UdpBcastServer.this.working = true;
                    String str = "UdpBcastServer";
                    Log.i(str, "udpBcastRunnable started");
                    UdpBcastServer.this.UdpReceive();
                    Log.i(str, "UDP -do in background");
                    if (!Thread.interrupted()) {
                        UdpBcastServer.this.after();
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean working = false;

    public UdpBcastServer(Context context2, PingCallback pingCallback) {
        this.mPingCallback = pingCallback;
        this.context = context2;
    }

    /* access modifiers changed from: private */
    public void before() {
        String str = "UdpBcastServer";
        try {
            if (this.serverSocket1 == null) {
                Log.i(str, "UDP - serverSocket1 was null");
                this.serverSocket1 = new DatagramSocket(null);
                this.serverSocket1.setReuseAddress(true);
                this.serverSocket1.setBroadcast(true);
                this.serverSocket1.bind(new InetSocketAddress(this.portNumber1));
            } else {
                Log.i(str, "UDP - serverSocket1 NOT null");
            }
        } catch (SocketException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Socket error/onPreExecute ");
            sb.append(e.getMessage());
            Log.i(str, sb.toString());
        }
        this.working = true;
    }

    /* access modifiers changed from: private */
    public void after() {
        Log.i("UdpBcastServer", "UDP - serverSocket1 finished");
        new Thread(new Runnable() {
            public void run() {
                UdpBcastServer.this.working = false;
                if (UdpBcastServer.this.serverSocket1 != null) {
                    UdpBcastServer.this.serverSocket1.disconnect();
                    UdpBcastServer.this.serverSocket1.close();
                }
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void UdpReceive() {
        while (this.working) {
            String str = "UdpBcastServer";
            Log.i(str, "UDP - UdpReceive");
            if (this.serverSocket1 != null) {
                byte[] bArr = this.receiveData;
                DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
                try {
                    this.serverSocket1.receive(datagramPacket);
                    String str2 = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    StringBuilder sb = new StringBuilder();
                    sb.append("data string");
                    sb.append(str2);
                    String str3 = "";
                    Log.i(sb.toString(), str3);
                    if (str2.length() != 0) {
                        String str4 = ",";
                        String str5 = !str2.equalsIgnoreCase(str3) ? str2.split(str4)[1] : str2;
                        if (!str2.equalsIgnoreCase(str3)) {
                            str2 = str2.split(str4)[0];
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Received name: ");
                        sb2.append(str5);
                        sb2.append(" Received address: ");
                        sb2.append(str2);
                        Log.i(str, sb2.toString());
                        InetAddress address = datagramPacket.getAddress();
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Received from ");
                        sb3.append(address.toString().split(MqttTopic.TOPIC_LEVEL_SEPARATOR)[1]);
                        Log.i(str, sb3.toString());
                        if (!str5.equalsIgnoreCase(str3) && !str2.equalsIgnoreCase(str3)) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("UDPServer/ FOUND IP: ");
                            sb4.append(str2);
                            Log.i(str, sb4.toString());
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("UDPServer/ FOUND NAME: ");
                            sb5.append(str5);
                            Log.i(str, sb5.toString());
                            if (str5 != null && TopLevel.isSensorTagDevice(str5)) {
                                JSONObject jSONObject = new JSONObject();
                                try {
                                    jSONObject.put("name", str5);
                                    jSONObject.put("host", str2);
                                    jSONObject.put("age", 0);
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("UDPBcast Server publishing device found to application,\nName: ");
                                    sb6.append(str5);
                                    sb6.append("\nHost: ");
                                    sb6.append(str2);
                                    sb6.append("\n/UdpReceive");
                                    Log.i(str, sb6.toString());
                                    this.mPingCallback.pingDeviceFetched(jSONObject);
                                    Intent intent = new Intent(UDP_BCAST_SERVER_FOUND_DEVICE_ACTION);
                                    intent.putExtra(UDP_BCAST_SERVER_DEVICE_NAME_EXTRA, str5);
                                    intent.putExtra(UDP_BCAST_SERVER_DEVICE_IP_EXTRA, str2);
                                    intent.putExtra(UDP_BCAST_SERVER_DEVICE_AGE_EXTRA, 0);
                                    Intent intent2 = new Intent();
                                    intent2.putExtra("newDevice", jSONObject.toString());
                                    intent2.setAction(TopLevel.DEVICE_FOUND_BROADCAST_ACTION);
                                    this.context.sendBroadcast(intent);
                                    this.context.sendBroadcast(intent2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    Log.e(str, "IO error- Closed in the middle of the process");
                    this.working = false;
                }
            }
        }
    }

    public void stopUDPBcastServer() {
        after();
    }
}
