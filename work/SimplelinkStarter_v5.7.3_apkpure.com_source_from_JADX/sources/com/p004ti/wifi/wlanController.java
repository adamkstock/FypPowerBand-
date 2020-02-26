package com.p004ti.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.util.Iterator;
import java.util.List;

/* renamed from: com.ti.wifi.wlanController */
public class wlanController {
    private static final int ON_CORRECT_WLAN_COUNT_MIN = 30;
    private static final int ON_WRONG_WLAN_COUNT_MIN = 30;
    private static final String TAG = "wlanController";
    public static final String WE_ARE_ON_DESIRED_WLAN_ACTION = "com.ti.WiFi.wlanController.WE_ARE_ON_DESIRED_WLAN_ACTION";
    private static final int delay = 250;
    public static WifiConfiguration desiredConfiguration;
    /* access modifiers changed from: private */
    public WifiInfo lastInfo;
    /* access modifiers changed from: private */
    public Context mCon;
    /* access modifiers changed from: private */
    public WifiManager man;
    /* access modifiers changed from: private */
    public boolean shouldStopThread;
    private Runnable thread;

    public wlanController(Context context) {
        this.mCon = context;
        this.man = (WifiManager) context.getSystemService("wifi");
    }

    public void stopWlanController() {
        this.shouldStopThread = true;
    }

    public void setDesiredWlanConfig(WifiConfiguration wifiConfiguration) {
        desiredConfiguration = wifiConfiguration;
        StringBuilder sb = new StringBuilder();
        sb.append("set desired: ");
        sb.append(desiredConfiguration);
        String sb2 = sb.toString();
        String str = TAG;
        Log.i(str, sb2);
        StringBuilder sb3 = new StringBuilder();
        String str2 = "Desired WLAN changed to : ";
        sb3.append(str2);
        sb3.append(wifiConfiguration.toString());
        Log.i(str, sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append(wifiConfiguration.toString());
        Log.d("RECON - ", sb4.toString());
    }

    public void startWlanController() {
        this.shouldStopThread = false;
        this.thread = new Runnable() {
            public void run() {
                String str;
                String str2;
                String str3;
                String str4;
                String str5 = "/ Desired SSID: ";
                String str6 = wlanController.TAG;
                Log.i(str6, "Started wlanController thread");
                boolean z = false;
                while (true) {
                    int i = 0;
                    int i2 = 0;
                    while (!wlanController.this.shouldStopThread) {
                        WifiInfo connectionInfo = wlanController.this.man.getConnectionInfo();
                        String currentConnectionSSID = wifiDeviceActivity.getCurrentConnectionSSID(wlanController.this.mCon);
                        StringBuilder sb = new StringBuilder();
                        String str7 = "\"";
                        sb.append(str7);
                        sb.append(currentConnectionSSID);
                        sb.append(str7);
                        String sb2 = sb.toString();
                        try {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("WifiInfo - Current SSID: ");
                            sb3.append(connectionInfo.getSSID());
                            sb3.append(str5);
                            sb3.append(wlanController.desiredConfiguration.SSID);
                            Log.i(str6, sb3.toString());
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("ConnectivityManager - Current SSID: ");
                            sb4.append(sb2);
                            sb4.append(str5);
                            sb4.append(wlanController.desiredConfiguration.SSID);
                            Log.i(str6, sb4.toString());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        if (wlanController.desiredConfiguration == null) {
                            Log.i(str6, "Desired configuration is null, doing nothing ...");
                        } else {
                            if (sb2 == null || !sb2.equals(wlanController.desiredConfiguration.SSID)) {
                                Log.i(str6, "We are NOT connected to desired SSID");
                                int i3 = i2 + 1;
                                if (i3 > 30) {
                                    Log.i(str6, "Attempt to connect to desired SSID");
                                    List<WifiConfiguration> configuredNetworks = wlanController.this.man.getConfiguredNetworks();
                                    if (configuredNetworks == null) {
                                        Log.d(str6, "WifiManager returned a null list of configured networks - there is an error");
                                    } else {
                                        Log.i(str6, "Search for desired wifiConfiguration in wifiManager's configured networks list");
                                        Iterator it = configuredNetworks.iterator();
                                        boolean z2 = false;
                                        while (true) {
                                            str = "foundNetwork: ";
                                            str2 = "connect to desired configuration";
                                            str3 = "enable desired wifiConfiguration";
                                            str4 = "disconnect from current network";
                                            if (!it.hasNext()) {
                                                break;
                                            }
                                            WifiConfiguration wifiConfiguration = (WifiConfiguration) it.next();
                                            if (!z2 && wifiConfiguration != null && wifiConfiguration.SSID != null && wifiConfiguration.SSID.equals(wlanController.desiredConfiguration.SSID)) {
                                                Log.i(str6, "FOUND desired wifiConfiguration in wifiManger's configured networks list");
                                                StringBuilder sb5 = new StringBuilder();
                                                sb5.append("desired wifiConfiguration networkId: ");
                                                sb5.append(wifiConfiguration.networkId);
                                                Log.i(str6, sb5.toString());
                                                Log.i(str6, "Found network in list, connecting ...");
                                                wlanController.this.man.disconnect();
                                                Log.i(str6, str4);
                                                wlanController.this.man.enableNetwork(wifiConfiguration.networkId, true);
                                                Log.i(str6, str3);
                                                wlanController.this.man.reconnect();
                                                Log.i(str6, str2);
                                                StringBuilder sb6 = new StringBuilder();
                                                sb6.append(str);
                                                sb6.append(true);
                                                Log.i(str6, sb6.toString());
                                                z2 = true;
                                            }
                                        }
                                        if (!z2) {
                                            Log.i(str6, "Did not find desired wifiConfiguration in wifiManger's configured networks list");
                                            Log.i(str6, "Network not found in list, adding new and connecting ...");
                                            wlanController.this.man.addNetwork(wlanController.desiredConfiguration);
                                            Log.i(str6, "add desired wifiConfiguration to list");
                                            wlanController.this.man.saveConfiguration();
                                            Log.i(str6, "save added configuration");
                                            Log.i(str6, "Search for added wifiConfiguration in wifiManager's configured networks list");
                                            for (WifiConfiguration wifiConfiguration2 : configuredNetworks) {
                                                if (!z2 && wifiConfiguration2.SSID != null && wifiConfiguration2.SSID.equals(wlanController.desiredConfiguration.SSID)) {
                                                    Log.i(str6, "FOUND added wifiConfiguration in wifiManger's configured networks list");
                                                    StringBuilder sb7 = new StringBuilder();
                                                    sb7.append("Added wifiConfiguration network id: ");
                                                    sb7.append(wifiConfiguration2.networkId);
                                                    Log.i(str6, sb7.toString());
                                                    Log.i(str6, "Found added network in list, connecting ...");
                                                    wlanController.this.man.disconnect();
                                                    Log.i(str6, str4);
                                                    wlanController.this.man.enableNetwork(wifiConfiguration2.networkId, true);
                                                    Log.i(str6, str3);
                                                    wlanController.this.man.reconnect();
                                                    Log.i(str6, str2);
                                                    StringBuilder sb8 = new StringBuilder();
                                                    sb8.append(str);
                                                    sb8.append(true);
                                                    Log.d(str6, sb8.toString());
                                                    z2 = true;
                                                }
                                            }
                                        }
                                        z = false;
                                        i = 0;
                                        i2 = 0;
                                    }
                                }
                                i2 = i3;
                                z = false;
                                i = 0;
                            } else {
                                Log.e(str6, "***************************** We ARE already connected to desired SSID");
                                i++;
                                if (!z && i > 30) {
                                    Log.e(str6, "**************** Broadcasting WE_ARE_ON_DESIRED_WLAN_ACTION");
                                    Intent intent = new Intent(wlanController.WE_ARE_ON_DESIRED_WLAN_ACTION);
                                    intent.putExtra("wlan", connectionInfo.getSSID());
                                    wlanController.this.mCon.sendBroadcast(intent);
                                    z = true;
                                }
                            }
                            wlanController.this.lastInfo = connectionInfo;
                            try {
                                Thread.sleep(250);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                    String str8 = "Thread Stopped !";
                    Log.d(str6, str8);
                    Log.d("RECON - ", str8);
                    return;
                }
            }
        };
        new Thread(this.thread).start();
    }
}
