package com.p004ti.wifi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.p004ti.wifi.utils.MDnsCallbackInterface;
import com.p004ti.wifi.utils.MDnsHelper;
import com.p004ti.wifi.utils.NetInfo;
import com.p004ti.wifi.utils.NetworkUtil;
import com.p004ti.wifi.utils.Ping;
import com.p004ti.wifi.utils.Ping.PingCallback;
import com.p004ti.wifi.utils.UdpBcastServer;
import java.util.ArrayList;
import javax.jmdns.impl.constants.DNSConstants;
import org.json.JSONObject;

/* renamed from: com.ti.wifi.CC32XXWifiDetectionLibrary */
class CC32XXWifiDetectionLibrary {
    private static final String DETECTED_DEVICE_IP_ADDR_EXTRA = "com.ti.WiFi.utils.CC32XXWifiDetectionLibrary.DETECTED_DEVICE_IP_ADDR_EXTRA";
    public static final String DETECTED_DEVICE_NAME = "com.ti.WiFi.utils.CC32XXWifiDetectionLibrary.DETECTED_DEVICE_NAME";
    private static final String DID_DETECT_DEVICE_ACTION = "com.ti.WiFi.utils.CC32XXWifiDetecitonLibrary.DID_DETECT_DEVICE_ACTION";
    public static final String MDNS_DETECTED_DEVICE_JSON = "com.ti.WiFi.utils.CC32XXWifiDetectionLibrary.MDNS_DETECTED_DEVICE_JSON";
    public static final String MDNS_DID_DETECT_DEVICE_ACTION = "com.ti.WiFi.utils.CC32XXWifiDetectionLibrary.MDNS_DID_DETECT_DEVICE_ACTION";
    private static final String TAG = "CC32XXWifiDetectionLib";
    private Context mContext;
    /* access modifiers changed from: private */
    public ArrayList<String> mDeviceList = new ArrayList<>();
    private MDnsHelper mDnsHelper;
    private Ping mPing;
    private PingCallback mPingCallback = new PingCallback() {
        public void pingCompleted() {
            Log.i(CC32XXWifiDetectionLibrary.TAG, "PingOrBcastCallback - Completed");
        }

        public void pingDeviceFetched(JSONObject jSONObject) {
            StringBuilder sb = new StringBuilder();
            sb.append("Device was found via PING or Bcast : ");
            sb.append(jSONObject);
            String sb2 = sb.toString();
            String str = CC32XXWifiDetectionLibrary.TAG;
            Log.i(str, sb2);
            try {
                String obj = jSONObject.get("host").toString();
                if (obj.length() > 0) {
                    boolean z = false;
                    for (int i = 0; i < CC32XXWifiDetectionLibrary.this.mDeviceList.size(); i++) {
                        if (obj.equals((String) CC32XXWifiDetectionLibrary.this.mDeviceList.get(i))) {
                            z = true;
                        }
                    }
                    if (!z) {
                        CC32XXWifiDetectionLibrary.this.mDeviceList.add(obj);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("PING: Found new device : ");
                        sb3.append(obj);
                        Log.d(str, sb3.toString());
                        CC32XXWifiDetectionLibrary.this.sendBroadCast(obj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void pingFailed(String str) {
            Log.i(CC32XXWifiDetectionLibrary.TAG, "PingCallback - pingFailed");
        }
    };
    private CC32XXWifiDetectionLibrary mThis;
    private NetInfo net;
    /* access modifiers changed from: private */
    public boolean shouldStop;
    private UdpBcastServer udpBcastServer;
    private Thread udpBcastServerThread;

    public void stopDetection() {
    }

    public CC32XXWifiDetectionLibrary(Context context) {
        this.mContext = context;
        this.net = new NetInfo(context);
        this.mThis = this;
        this.shouldStop = false;
    }

    public void startDetection() {
        new Thread(new Runnable() {
            public void run() {
                CC32XXWifiDetectionLibrary.this.initPing();
                CC32XXWifiDetectionLibrary.this.startPing();
                CC32XXWifiDetectionLibrary.this.restartUdp();
                CC32XXWifiDetectionLibrary.this.startMDNS();
                while (!CC32XXWifiDetectionLibrary.this.shouldStop) {
                    try {
                        Thread.sleep(DNSConstants.CLOSE_TIMEOUT);
                        Log.d(CC32XXWifiDetectionLibrary.TAG, "Running detection ...");
                        CC32XXWifiDetectionLibrary.this.startPing();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public boolean initPing() {
        String str;
        stopPing();
        if (NetworkUtil.getConnectionStatus(this.mContext) != NetworkUtil.WIFI) {
            return false;
        }
        this.net.getWifiInfo();
        while (true) {
            boolean equalsIgnoreCase = this.net.gatewayIp.equalsIgnoreCase("0.0.0.0");
            str = TAG;
            if (!equalsIgnoreCase) {
                break;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("in while - network ip: ");
            sb.append(this.net.gatewayIp);
            Log.i(str, sb.toString());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.net.getWifiInfo();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("after while network ip: ");
        sb2.append(this.net.gatewayIp);
        Log.i(str, sb2.toString());
        String[] split = this.net.gatewayIp.split("\\.");
        String str2 = "";
        for (int i = 0; i < 3; i++) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(split[i]);
            sb3.append(".");
            str2 = sb3.toString();
        }
        this.mPing = new Ping(this.mContext, this.mPingCallback, this.net.gatewayIp);
        Ping ping = this.mPing;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append("255");
        ping.ipToPing = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Will ping ip: ");
        sb5.append(this.mPing.ipToPing);
        Log.i(str, sb5.toString());
        return true;
    }

    /* access modifiers changed from: private */
    public void startPing() {
        Ping ping = this.mPing;
        if ((ping == null || !ping.working) && initPing()) {
            try {
                new Thread(this.mPing.pingRunnable).start();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopPing() {
        Ping ping = this.mPing;
        if (ping != null && ping.working) {
            this.mPing.stopPing();
        }
        this.mPing = null;
    }

    /* access modifiers changed from: private */
    public void sendBroadCast(String str) {
        Intent intent = new Intent(DID_DETECT_DEVICE_ACTION);
        intent.putExtra(DETECTED_DEVICE_IP_ADDR_EXTRA, str);
        this.mContext.sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    public void restartUdp() {
        UdpBcastServer udpBcastServer2 = this.udpBcastServer;
        if (udpBcastServer2 != null) {
            udpBcastServer2.stopUDPBcastServer();
            this.udpBcastServerThread.interrupt();
            this.udpBcastServer = null;
        }
        this.udpBcastServer = new UdpBcastServer(this.mContext, this.mPingCallback);
        this.udpBcastServerThread = new Thread(this.udpBcastServer.udpBcastServerRunnable);
        this.udpBcastServerThread.start();
    }

    /* access modifiers changed from: private */
    public void startMDNS() {
        this.mDnsHelper = new MDnsHelper();
        this.mDnsHelper.init(this.mContext, new MDnsCallbackInterface() {
            public void onDeviceResolved(JSONObject jSONObject) {
                try {
                    String obj = jSONObject.get("host").toString();
                    if (obj.length() > 0) {
                        boolean z = false;
                        for (int i = 0; i < CC32XXWifiDetectionLibrary.this.mDeviceList.size(); i++) {
                            if (obj.equals((String) CC32XXWifiDetectionLibrary.this.mDeviceList.get(i))) {
                                z = true;
                            }
                        }
                        if (!z) {
                            CC32XXWifiDetectionLibrary.this.mDeviceList.add(obj);
                            String str = CC32XXWifiDetectionLibrary.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("mDNS: Found new device : ");
                            sb.append(obj);
                            Log.d(str, sb.toString());
                            CC32XXWifiDetectionLibrary.this.sendBroadCast(obj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mDnsHelper.startDiscovery();
    }
}
