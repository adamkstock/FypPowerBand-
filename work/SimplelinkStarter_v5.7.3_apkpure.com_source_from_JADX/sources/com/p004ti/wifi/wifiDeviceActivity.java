package com.p004ti.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.device_selector.DeviceActivity;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.util.TrippleSparkLineView;
import com.p004ti.wifi.utils.Ping.PingCallback;
import com.ti.ble.simplelinkstarter.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import javax.jmdns.impl.constants.DNSConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.ti.wifi.wifiDeviceActivity */
public class wifiDeviceActivity extends AppCompatActivity implements OnClickListener {
    private static final String BATTERY_SAVER_MODE_TIMEOUT = "120";
    private static final int MAX_CLOUD_URL_ATTEMPTS = 50;
    private static final String STAY_AWAKE_MODE_TIMEOUT = "65535";
    private static final String TAG = "wifiDeviceActivity";
    private static final String WIFI_DEVICE_ABOUT_URL = "param_about.html";
    private static final String WIFI_DEVICE_ACT_TIMEOUT = "__SL_P_S.R=setup.html&__SL_P_ACT=";
    public static final String WIFI_DEVICE_ADD_PROFILES_URL = "profiles_add.html";
    public static final String WIFI_DEVICE_ADD_PROFILE_LAST_PART = "&__SL_P_P.D=7";
    public static final String WIFI_DEVICE_ADD_PROFILE_UP_TO_ENC = "&__SL_P_P.B=";
    public static final String WIFI_DEVICE_ADD_PROFILE_UP_TO_PWD = "&__SL_P_P.C=";
    public static final String WIFI_DEVICE_ADD_PROFILE_UP_TO_SSID = "__SL_P_S.R=setup.html&__SL_P_P.A=";
    private static final String WIFI_DEVICE_CHANGE_MODE = "__SL_P_S.R=setup.html&__SL_P_REB=5000&__SL_P_S.B=sensortag&__SL_P_S.C=sensortag.net@&__SL_P_S.D=";
    private static final String WIFI_DEVICE_CONFIGURE_SENSORS = "__SL_P_UCF=";
    public static final String WIFI_DEVICE_CONFIG_URL = "param_sensortag_conf.html";
    private static final String WIFI_DEVICE_DATA_URL = "param_sensortag_poll.html";
    public static final String WIFI_DEVICE_DID_FAIL_WRITE_PROFILE = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_DID_FAIL_WRITE_PROFILE";
    public static final String WIFI_DEVICE_DID_WRITE_PROFILE = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_DID_WRITE_PROFILE";
    public static final String WIFI_DEVICE_DID_WRITE_PROFILE_RESPONSE = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_DID_WRITE_PROFILE";
    private static final String WIFI_DEVICE_FACTORY_RESET = "__SL_P_S.R=setup.html&__SL_P_REB=0&";
    public static final String WIFI_DEVICE_GET_CFG_RESULT = "param_cfg_result.txt";
    private static final String WIFI_DEVICE_GET_CONFIG = "__SL_P_UCF=0";
    private static final Float WIFI_DEVICE_LATEST_VERSION = Float.valueOf(1.003f);
    public static final String WIFI_DEVICE_MODE_AP = "AP";
    private static final String WIFI_DEVICE_MODE_CHANGE_URL = "mode_config.html";
    public static final String WIFI_DEVICE_MODE_STATION = "Station";
    public static final String WIFI_DEVICE_PARAMETER_CONFIG_URL = "param_config.html";
    private static final String WIFI_DEVICE_POLL_DATA_IP_ADDR = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_IP_ADDR";
    private static final String WIFI_DEVICE_POLL_DATA_RESPONSE = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_RESPONSE";
    public static final String WIFI_DEVICE_POLL_DATA_RESPONSE_CODE = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_RESPONSE_CODE";
    private static final String WIFI_DEVICE_POLL_DATA_RESPONSE_VALUE = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_RESPONSE_VALUE";
    private static final String WIFI_DEVICE_PROFILE_ADD_URL = "add_profile.html";
    private static final String WIFI_DEVICE_PROVISONING_MODE_START = "__SL_P_PRV=1";
    public static final String WIFI_DEVICE_REQUEST_DISCONNECTED = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_DISCONNECTED";
    public static final String WIFI_DEVICE_REQUEST_IP_ADDR = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_IP_ADDR";
    public static final String WIFI_DEVICE_REQUEST_RESPONSE = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE";
    public static final String WIFI_DEVICE_REQUEST_RESPONSE_CODE = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_CODE";
    public static final String WIFI_DEVICE_REQUEST_RESPONSE_DATA = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_DATA";
    public static final String WIFI_DEVICE_REQUEST_RESPONSE_URL = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_URL";
    private static boolean inside;
    private TextView accelerometerValue;
    private TrippleSparkLineView acceleromterSL;
    private TrippleSparkLineView ambientTempSL;
    private TextView ambientTempValue;
    private BroadcastReceiver bReceiver;
    private TrippleSparkLineView barometerSL;
    private TextView barometerValue;
    private float calibBarometer;
    /* access modifiers changed from: private */
    public TextView cloudValue;
    private String dName;
    private WifiConfiguration desiredWifiConfiguration;
    private TrippleSparkLineView gyroscopeSL;
    private TextView gyroscopeValue;
    private TrippleSparkLineView humiditySL;
    private TextView humidityValue;
    private IntentFilter iFilter;
    /* access modifiers changed from: private */
    public String ipAddress;
    private TrippleSparkLineView irTempSL;
    private TextView irTempValue;
    private ImageView leftButton;
    /* access modifiers changed from: private */
    public wifiDeviceActivity mThis;
    private TrippleSparkLineView magnetometerSL;
    private TextView magnetometerValue;
    /* access modifiers changed from: private */
    public int numOfCloudUrlAttemptsMade = 0;
    private TrippleSparkLineView optoSL;
    private TextView optoValue;
    private WiFiPeriodicDataReadThread periodicReadingThread;
    private ImageView reedButton;
    private ImageView rightButton;
    private TrippleSparkLineView simpleKeysSL;
    /* access modifiers changed from: private */
    public TextView statusText;
    private WifiConfiguration wifiConfigurationFromTL;
    private wlanController wlanCon;

    /* renamed from: com.ti.wifi.wifiDeviceActivity$WiFiPeriodicDataReadThread */
    class WiFiPeriodicDataReadThread extends Thread {
        int secToSleep;
        volatile boolean shouldStop = false;

        public WiFiPeriodicDataReadThread(int i) {
            this.secToSleep = i;
        }

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Regions count limit reached
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:89)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeEndlessLoop(RegionMaker.java:368)
            	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:172)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:106)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:30)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
            	at jadx.core.ProcessClass.process(ProcessClass.java:35)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
            */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x008c  */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x012d  */
        public void run() {
            /*
                r12 = this;
                java.lang.String r0 = "param_sensortag_poll.html"
                java.lang.String r1 = "/"
                java.lang.String r2 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_IP_ADDR"
                java.lang.String r3 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_RESPONSE_VALUE"
                java.lang.String r4 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_RESPONSE"
                java.lang.String r5 = "wifiDeviceActivity"
                java.lang.String r6 = "WiFi Periodic read thread started !"
                android.util.Log.d(r5, r6)
            L_0x0011:
                java.net.URL r6 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0179 }
                java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ MalformedURLException -> 0x0179 }
                r7.<init>()     // Catch:{ MalformedURLException -> 0x0179 }
                java.lang.String r8 = "http://"     // Catch:{ MalformedURLException -> 0x0179 }
                r7.append(r8)     // Catch:{ MalformedURLException -> 0x0179 }
                com.ti.wifi.wifiDeviceActivity r8 = com.p004ti.wifi.wifiDeviceActivity.this     // Catch:{ MalformedURLException -> 0x0179 }
                java.lang.String r8 = r8.ipAddress     // Catch:{ MalformedURLException -> 0x0179 }
                r7.append(r8)     // Catch:{ MalformedURLException -> 0x0179 }
                r7.append(r1)     // Catch:{ MalformedURLException -> 0x0179 }
                r7.append(r0)     // Catch:{ MalformedURLException -> 0x0179 }
                java.lang.String r7 = r7.toString()     // Catch:{ MalformedURLException -> 0x0179 }
                r6.<init>(r7)     // Catch:{ MalformedURLException -> 0x0179 }
                r7 = 0
                java.lang.String r8 = ""
                java.net.URLConnection r6 = r6.openConnection()     // Catch:{ IOException -> 0x00f2, Exception -> 0x00bc, all -> 0x00b8 }
                java.net.HttpURLConnection r6 = (java.net.HttpURLConnection) r6     // Catch:{ IOException -> 0x00f2, Exception -> 0x00bc, all -> 0x00b8 }
                com.ti.wifi.wifiDeviceActivity r7 = com.p004ti.wifi.wifiDeviceActivity.this     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                com.ti.wifi.wifiDeviceActivity r7 = r7.mThis     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                com.ti.wifi.wifiDeviceActivity$WiFiPeriodicDataReadThread$1 r9 = new com.ti.wifi.wifiDeviceActivity$WiFiPeriodicDataReadThread$1     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r9.<init>()     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r7.runOnUiThread(r9)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.lang.String r7 = "GET"     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r6.setRequestMethod(r7)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.lang.String r7 = "Connection"     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.lang.String r9 = "close"     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r6.setRequestProperty(r7, r9)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r7 = 1     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r6.setDoInput(r7)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.io.InputStream r7 = r6.getInputStream()     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.lang.String r8 = com.p004ti.wifi.wifiDeviceActivity.readStream(r7)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                int r7 = r6.getResponseCode()     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r9.<init>()     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.lang.String r10 = "HTTP Response : "     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r9.append(r10)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r9.append(r7)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.lang.String r7 = " Length : "     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r9.append(r7)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                int r7 = r8.length()     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                r9.append(r7)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                java.lang.String r7 = r9.toString()     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                android.util.Log.d(r5, r7)     // Catch:{ IOException -> 0x00b6, Exception -> 0x00b4 }
                int r7 = r8.length()
                if (r7 <= 0) goto L_0x0127
                android.content.Intent r7 = new android.content.Intent
                r7.<init>()
                r7.setAction(r4)
                r7.putExtra(r3, r8)
                com.ti.wifi.wifiDeviceActivity r8 = com.p004ti.wifi.wifiDeviceActivity.this
                java.lang.String r8 = r8.ipAddress
                r7.putExtra(r2, r8)
                com.ti.wifi.wifiDeviceActivity r8 = com.p004ti.wifi.wifiDeviceActivity.this
                com.ti.wifi.wifiDeviceActivity r8 = r8.mThis
                r8.sendBroadcast(r7)
                r6.disconnect()     // Catch:{ NullPointerException -> 0x00ae }
                goto L_0x0127
            L_0x00ae:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0127
            L_0x00b4:
                r7 = move-exception
                goto L_0x00c0
            L_0x00b6:
                r7 = move-exception
                goto L_0x00f6
            L_0x00b8:
                r0 = move-exception
                r6 = r7
                goto L_0x014d
            L_0x00bc:
                r6 = move-exception
                r11 = r7
                r7 = r6
                r6 = r11
            L_0x00c0:
                r7.printStackTrace()     // Catch:{ all -> 0x014c }
                r6.disconnect()     // Catch:{ NullPointerException -> 0x00c7 }
                goto L_0x00cb
            L_0x00c7:
                r7 = move-exception
                r7.printStackTrace()     // Catch:{ all -> 0x014c }
            L_0x00cb:
                int r7 = r8.length()
                if (r7 <= 0) goto L_0x0127
                android.content.Intent r7 = new android.content.Intent
                r7.<init>()
                r7.setAction(r4)
                r7.putExtra(r3, r8)
                com.ti.wifi.wifiDeviceActivity r8 = com.p004ti.wifi.wifiDeviceActivity.this
                java.lang.String r8 = r8.ipAddress
                r7.putExtra(r2, r8)
                com.ti.wifi.wifiDeviceActivity r8 = com.p004ti.wifi.wifiDeviceActivity.this
                com.ti.wifi.wifiDeviceActivity r8 = r8.mThis
                r8.sendBroadcast(r7)
                r6.disconnect()     // Catch:{ NullPointerException -> 0x00ae }
                goto L_0x0127
            L_0x00f2:
                r6 = move-exception
                r11 = r7
                r7 = r6
                r6 = r11
            L_0x00f6:
                r7.printStackTrace()     // Catch:{ all -> 0x014c }
                r6.disconnect()     // Catch:{ NullPointerException -> 0x00fd }
                goto L_0x0101
            L_0x00fd:
                r7 = move-exception
                r7.printStackTrace()     // Catch:{ all -> 0x014c }
            L_0x0101:
                int r7 = r8.length()
                if (r7 <= 0) goto L_0x0127
                android.content.Intent r7 = new android.content.Intent
                r7.<init>()
                r7.setAction(r4)
                r7.putExtra(r3, r8)
                com.ti.wifi.wifiDeviceActivity r8 = com.p004ti.wifi.wifiDeviceActivity.this
                java.lang.String r8 = r8.ipAddress
                r7.putExtra(r2, r8)
                com.ti.wifi.wifiDeviceActivity r8 = com.p004ti.wifi.wifiDeviceActivity.this
                com.ti.wifi.wifiDeviceActivity r8 = r8.mThis
                r8.sendBroadcast(r7)
                r6.disconnect()     // Catch:{ NullPointerException -> 0x00ae }
            L_0x0127:
                r6 = 0
                r7 = 0
            L_0x0129:
                int r8 = r12.secToSleep
                if (r7 >= r8) goto L_0x0011
                r8 = 0
            L_0x012e:
                r9 = 10
                if (r8 >= r9) goto L_0x0149
                r9 = 100
                java.lang.Thread.sleep(r9, r6)     // Catch:{ InterruptedException -> 0x0138 }
                goto L_0x013c
            L_0x0138:
                r9 = move-exception
                r9.printStackTrace()
            L_0x013c:
                boolean r9 = r12.shouldStop
                if (r9 == 0) goto L_0x0146
                java.lang.String r0 = "WiFi Periodic read thread stopped !"
                android.util.Log.d(r5, r0)
                return
            L_0x0146:
                int r8 = r8 + 1
                goto L_0x012e
            L_0x0149:
                int r7 = r7 + 1
                goto L_0x0129
            L_0x014c:
                r0 = move-exception
            L_0x014d:
                int r1 = r8.length()
                if (r1 <= 0) goto L_0x0178
                android.content.Intent r1 = new android.content.Intent
                r1.<init>()
                r1.setAction(r4)
                r1.putExtra(r3, r8)
                com.ti.wifi.wifiDeviceActivity r3 = com.p004ti.wifi.wifiDeviceActivity.this
                java.lang.String r3 = r3.ipAddress
                r1.putExtra(r2, r3)
                com.ti.wifi.wifiDeviceActivity r2 = com.p004ti.wifi.wifiDeviceActivity.this
                com.ti.wifi.wifiDeviceActivity r2 = r2.mThis
                r2.sendBroadcast(r1)
                r6.disconnect()     // Catch:{ NullPointerException -> 0x0174 }
                goto L_0x0178
            L_0x0174:
                r1 = move-exception
                r1.printStackTrace()
            L_0x0178:
                throw r0
            L_0x0179:
                r2 = move-exception
                r2.printStackTrace()
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "URL \"http://"
                r2.append(r3)
                com.ti.wifi.wifiDeviceActivity r3 = com.p004ti.wifi.wifiDeviceActivity.this
                java.lang.String r3 = r3.ipAddress
                r2.append(r3)
                r2.append(r1)
                r2.append(r0)
                java.lang.String r0 = "\" is malformed !"
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                android.util.Log.d(r5, r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.wifiDeviceActivity.WiFiPeriodicDataReadThread.run():void");
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_device_wifi);
        this.mThis = this;
        Intent intent = getIntent();
        this.ipAddress = intent.getStringExtra(DeviceActivity.EXTRA_DEVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("ST device's IP address: ");
        sb.append(this.ipAddress);
        String sb2 = sb.toString();
        String str = TAG;
        Log.i(str, sb2);
        try {
            this.wifiConfigurationFromTL = (WifiConfiguration) intent.getParcelableExtra(TopLevel.CONFIG_TO_CONNECT_TO_EXTRA);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("wifiConfigurationFromTL: ");
            sb3.append(this.wifiConfigurationFromTL);
            Log.i(str, sb3.toString());
        } catch (Exception e) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(" ex: ");
            sb4.append(e);
            Log.i(str, sb4.toString());
            e.printStackTrace();
        }
        this.dName = intent.getStringExtra(TopLevel.DEVICE_NAME_EXTRA);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("device name: ");
        sb5.append(this.dName);
        Log.i(str, sb5.toString());
        this.wlanCon = new wlanController(this.mThis);
        if (this.ipAddress.equals(TopLevel.IP_ADDRESS)) {
            Log.i(str, "ST device is AP - stay connected to it");
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            StringBuilder sb6 = new StringBuilder();
            String str2 = "\"";
            sb6.append(str2);
            sb6.append(this.dName);
            sb6.append(str2);
            wifiConfiguration.SSID = sb6.toString();
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.preSharedKey = "";
            this.desiredWifiConfiguration = wifiConfiguration;
        } else {
            Log.i(str, "ST device is STA - stay connected to the wLAN it is in");
            WifiConfiguration wifiConfiguration2 = this.wifiConfigurationFromTL;
            if (wifiConfiguration2 != null) {
                this.desiredWifiConfiguration = wifiConfiguration2;
            }
        }
        this.wlanCon.setDesiredWlanConfig(this.desiredWifiConfiguration);
        StringBuilder sb7 = new StringBuilder();
        sb7.append("set desired: ");
        sb7.append(this.desiredWifiConfiguration);
        Log.i(str, sb7.toString());
        this.wlanCon.startWlanController();
        this.iFilter = new IntentFilter();
        this.iFilter.addAction(WIFI_DEVICE_REQUEST_RESPONSE);
        this.iFilter.addAction(WIFI_DEVICE_POLL_DATA_RESPONSE);
        try {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } catch (NullPointerException e2) {
            e2.printStackTrace();
        }
        this.bReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                StringBuilder sb = new StringBuilder();
                sb.append("Received intent");
                sb.append(intent);
                String sb2 = sb.toString();
                String str = wifiDeviceActivity.TAG;
                Log.d(str, sb2);
                if (wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE.equals(intent.getAction())) {
                    wifiDeviceActivity.this.statusText.setText(wifiDeviceActivity.this.getResources().getString(R.string.rx_request));
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Request response on ");
                    String str2 = wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_URL;
                    sb3.append(intent.getStringExtra(str2));
                    Log.d(str, sb3.toString());
                    if (intent.getStringExtra(str2).equals(wifiDeviceActivity.WIFI_DEVICE_CONFIG_URL)) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Got data");
                        String str3 = wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_DATA;
                        sb4.append(intent.getStringExtra(str3));
                        Log.d(str, sb4.toString());
                        String access$100 = wifiDeviceActivity.getURLString(intent.getStringExtra(str3));
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("cloudUrl: ");
                        sb5.append(access$100);
                        Log.d(str, sb5.toString());
                        if (access$100 != null && !access$100.equals("Unknown TokenUnknown Token")) {
                            if (access$100.endsWith(Constants.DEVICE_UNKNOWN_TOKEN_STRING)) {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("cloudUrl not resolved - check connected IP: ");
                                sb6.append(wifiDeviceActivity.this.ipAddress);
                                Log.d(str, sb6.toString());
                                if (wifiDeviceActivity.this.ipAddress.equals(TopLevel.IP_ADDRESS)) {
                                    Log.d(str, "cloudUrl not resolved - we are connected to ST device as AP - no cloudUrl");
                                } else {
                                    Log.d(str, "cloudUrl not resolved - another configureDevice for cloudUrl required");
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append("cloudUrl not resolved - cloudUrl attempts made: ");
                                    sb7.append(wifiDeviceActivity.this.numOfCloudUrlAttemptsMade);
                                    sb7.append(" max is: ");
                                    sb7.append(50);
                                    Log.d(str, sb7.toString());
                                    if (wifiDeviceActivity.this.numOfCloudUrlAttemptsMade < 50) {
                                        Log.d(str, "cloudUrl not resolved - sending another configureDevice for cloudUrl");
                                        wifiDeviceActivity.this.numOfCloudUrlAttemptsMade = wifiDeviceActivity.this.numOfCloudUrlAttemptsMade + 1;
                                        wifiDeviceActivity.configureDevice(wifiDeviceActivity.this.mThis, wifiDeviceActivity.this.ipAddress, 127);
                                    } else {
                                        Log.d(str, "cloudUrl not resolved - max number of cloudUrl attempts committed, no cloudUrl");
                                    }
                                }
                            } else {
                                Log.d(str, "setting cloudUrl");
                                wifiDeviceActivity.this.numOfCloudUrlAttemptsMade = 0;
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append("cloudUrl number of attempts: ");
                                sb8.append(wifiDeviceActivity.this.numOfCloudUrlAttemptsMade);
                                Log.d(str, sb8.toString());
                                wifiDeviceActivity.this.cloudValue.setText(access$100);
                                Linkify.addLinks(wifiDeviceActivity.this.cloudValue, 1);
                            }
                        }
                    }
                } else {
                    if (wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_RESPONSE.equals(intent.getAction())) {
                        wifiDeviceActivity.this.statusText.setText(wifiDeviceActivity.this.getResources().getString(R.string.rx_data));
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("Got poll data from WiFi device ");
                        sb9.append(intent.getStringExtra(wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_IP_ADDR));
                        Log.d(str, sb9.toString());
                        String stringExtra = intent.getStringExtra(wifiDeviceActivity.WIFI_DEVICE_POLL_DATA_RESPONSE_VALUE);
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("Data :");
                        sb10.append(stringExtra);
                        Log.d(str, sb10.toString());
                        if (stringExtra.length() >= 20) {
                            String access$600 = wifiDeviceActivity.parsePollDataToJSON(stringExtra);
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append("Length :");
                            sb11.append(access$600.length());
                            Log.d(str, sb11.toString());
                            StringBuilder sb12 = new StringBuilder();
                            sb12.append("JSON: ");
                            sb12.append(access$600);
                            Log.d(str, sb12.toString());
                            try {
                                wifiDeviceActivity.this.mThis.updateGUI(new JSONObject(access$600));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
    }

    public void onStart() {
        super.onStart();
        this.statusText = (TextView) findViewById(R.id.adw_status_text);
        this.statusText.setText(getResources().getString(R.string.start));
        registerReceiver(this.bReceiver, this.iFilter);
        configureDevice(this, this.ipAddress, 127);
        String str = "";
        setupCell(R.id.adw_oad_cell, getResources().getString(R.string.Firmware_upgrade), R.mipmap.wifisensortag_ota, str);
        TableRow tableRow = (TableRow) findViewById(R.id.adw_oad_cell);
        TrippleSparkLineView trippleSparkLineView = (TrippleSparkLineView) tableRow.findViewById(R.id.gctr_sparkline1);
        trippleSparkLineView.setColor(0, 1, 1, 1, 0);
        trippleSparkLineView.setColor(0, 1, 1, 1, 1);
        trippleSparkLineView.setColor(0, 1, 1, 1, 2);
        ((ImageView) tableRow.findViewById(R.id.gctr_disclosure_indicator)).setVisibility(0);
        tableRow.setOnClickListener(this);
        String str2 = "-.-°C";
        setupCell(R.id.cell1, getResources().getString(R.string.ambient_temperature), R.mipmap.wifisensortag_temp, str2);
        this.ambientTempValue = getValueFromCell(R.id.cell1);
        this.ambientTempSL = getSparkLineViewFromCell(R.id.cell1);
        TrippleSparkLineView trippleSparkLineView2 = this.ambientTempSL;
        trippleSparkLineView2.autoScale = true;
        trippleSparkLineView2.autoScaleBounceBack = true;
        trippleSparkLineView2.setColor(0, 1, 0, 0, 1);
        this.ambientTempSL.setColor(0, 1, 0, 0, 2);
        setupCell(R.id.cell2, getResources().getString(R.string.ir_temperature), R.mipmap.wifisensortag_irtemp, str2);
        this.irTempValue = getValueFromCell(R.id.cell2);
        this.irTempSL = getSparkLineViewFromCell(R.id.cell2);
        TrippleSparkLineView trippleSparkLineView3 = this.irTempSL;
        trippleSparkLineView3.autoScale = true;
        trippleSparkLineView3.autoScaleBounceBack = true;
        trippleSparkLineView3.setColor(0, 1, 0, 0, 1);
        this.irTempSL.setColor(0, 1, 0, 0, 2);
        setupCell(R.id.cell3, getResources().getString(R.string.humidity), R.mipmap.wifisensortag_humidity, "--.-%rH");
        this.humidityValue = getValueFromCell(R.id.cell3);
        this.humiditySL = getSparkLineViewFromCell(R.id.cell3);
        TrippleSparkLineView trippleSparkLineView4 = this.humiditySL;
        trippleSparkLineView4.autoScale = true;
        trippleSparkLineView4.autoScaleBounceBack = true;
        trippleSparkLineView4.setColor(0, 1, 0, 0, 1);
        this.humiditySL.setColor(0, 1, 0, 0, 2);
        setupCell(R.id.cell4, getResources().getString(R.string.barometer), R.mipmap.wifisensortag_barometer, "---.- mBar");
        this.barometerValue = getValueFromCell(R.id.cell4);
        this.barometerSL = getSparkLineViewFromCell(R.id.cell4);
        TrippleSparkLineView trippleSparkLineView5 = this.barometerSL;
        trippleSparkLineView5.autoScale = true;
        trippleSparkLineView5.autoScaleBounceBack = true;
        trippleSparkLineView5.setColor(0, 1, 0, 0, 1);
        this.barometerSL.setColor(0, 1, 0, 0, 2);
        TableRow tableRow2 = (TableRow) findViewById(R.id.cell5);
        try {
            ((ImageView) findViewById(R.id.smdtr_icon)).setImageResource(R.mipmap.wifisensortag_movement);
            ((ImageView) findViewById(R.id.smdtr_disclose_icon)).setVisibility(4);
            this.accelerometerValue = (TextView) tableRow2.findViewById(R.id.smdtr_value);
            this.acceleromterSL = (TrippleSparkLineView) tableRow2.findViewById(R.id.smdtr_sparkline1);
            this.acceleromterSL.autoScale = true;
            this.acceleromterSL.autoScaleBounceBack = true;
            this.gyroscopeValue = (TextView) tableRow2.findViewById(R.id.smdtr_value2);
            this.gyroscopeSL = (TrippleSparkLineView) tableRow2.findViewById(R.id.smdtr_sparkline2);
            this.gyroscopeSL.autoScale = true;
            this.gyroscopeSL.autoScaleBounceBack = true;
            this.magnetometerValue = (TextView) tableRow2.findViewById(R.id.smdtr_value3);
            this.magnetometerSL = (TrippleSparkLineView) tableRow2.findViewById(R.id.smdtr_sparkline3);
            this.magnetometerSL.autoScale = true;
            this.magnetometerSL.autoScaleBounceBack = true;
            ((TextView) tableRow2.findViewById(R.id.smdtr_characteristic_title)).setText(getResources().getString(R.string.movement));
            ((Switch) tableRow2.findViewById(R.id.smdtr_wos)).setVisibility(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TableRow tableRow3 = (TableRow) findViewById(R.id.cell6);
        try {
            this.leftButton = (ImageView) tableRow3.findViewById(R.id.sktr_left_button_image);
            this.rightButton = (ImageView) tableRow3.findViewById(R.id.sktr_right_button_image);
            this.reedButton = (ImageView) tableRow3.findViewById(R.id.sktr_reed_image);
            this.simpleKeysSL = (TrippleSparkLineView) tableRow3.findViewById(R.id.sktr_sparkline);
            ((ImageView) tableRow3.findViewById(R.id.sktr_disclosure_indicator)).setVisibility(4);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        setupCell(R.id.cell7, getResources().getString(R.string.light), R.mipmap.wifisensortag_light, "0.0 Lux");
        TableRow tableRow4 = (TableRow) findViewById(R.id.cell7);
        try {
            this.optoValue = (TextView) tableRow4.findViewById(R.id.gctr_value);
            this.optoSL = (TrippleSparkLineView) tableRow4.findViewById(R.id.gctr_sparkline1);
            this.optoSL.autoScale = true;
            this.optoSL.autoScaleBounceBack = true;
            this.optoSL.setColor(0, 1, 1, 1, 1);
            this.optoSL.setColor(0, 1, 1, 1, 2);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            this.cloudValue = (TextView) ((TableRow) findViewById(R.id.adw_cloud_cell)).findViewById(R.id.wcutr_cloud_url);
            this.cloudValue.setText(str);
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        this.periodicReadingThread = new WiFiPeriodicDataReadThread(1);
        this.periodicReadingThread.start();
    }

    private void setupCell(int i, String str, int i2, String str2) {
        TableRow tableRow = (TableRow) findViewById(i);
        ((ImageView) tableRow.findViewById(R.id.gctr_disclosure_indicator)).setVisibility(4);
        if (tableRow.findViewById(R.id.gctr_characteristic_title) != null) {
            ((TextView) tableRow.findViewById(R.id.gctr_characteristic_title)).setText(str);
            ((ImageView) tableRow.findViewById(R.id.gctr_icon)).setImageResource(i2);
            ((TextView) tableRow.findViewById(R.id.gctr_value)).setText(str2);
        }
    }

    private TextView getValueFromCell(int i) {
        TableRow tableRow = (TableRow) findViewById(i);
        if (tableRow.findViewById(R.id.gctr_characteristic_title) != null) {
            return (TextView) tableRow.findViewById(R.id.gctr_value);
        }
        return null;
    }

    private TrippleSparkLineView getSparkLineViewFromCell(int i) {
        TableRow tableRow = (TableRow) findViewById(i);
        if (tableRow.findViewById(R.id.gctr_characteristic_title) != null) {
            return (TrippleSparkLineView) tableRow.findViewById(R.id.gctr_sparkline1);
        }
        return null;
    }

    public void onStop() {
        super.onStop();
        unregisterReceiver(this.bReceiver);
        WiFiPeriodicDataReadThread wiFiPeriodicDataReadThread = this.periodicReadingThread;
        if (wiFiPeriodicDataReadThread != null) {
            wiFiPeriodicDataReadThread.shouldStop = true;
        }
        wlanController wlancontroller = this.wlanCon;
        if (wlancontroller != null) {
            wlancontroller.stopWlanController();
            Log.d("RECON - ", "WDA/onStop stopped wlanCon");
            this.wlanCon = null;
        }
    }

    /* access modifiers changed from: private */
    public void updateGUI(JSONObject jSONObject) {
        String str = "gyr";
        String str2 = "mag";
        String str3 = "acc";
        try {
            float valueFromString = getValueFromString((String) jSONObject.get("tmp"), 3);
            if (this.ambientTempValue != null) {
                this.ambientTempValue.setText(String.format(Locale.ENGLISH, "%.1f°C", new Object[]{Float.valueOf(valueFromString)}));
            }
            if (this.ambientTempSL != null) {
                this.ambientTempSL.addValue(valueFromString, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            float valueFromString2 = getValueFromString((String) jSONObject.get("tmp"), 2);
            if (this.irTempValue != null) {
                this.irTempValue.setText(String.format(Locale.ENGLISH, "%.1f°C", new Object[]{Float.valueOf(valueFromString2)}));
            }
            if (this.irTempSL != null) {
                this.irTempSL.addValue(valueFromString2, 0);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        try {
            float valueFromString3 = getValueFromString((String) jSONObject.get("hum"), 2);
            if (this.humidityValue != null) {
                this.humidityValue.setText(String.format(Locale.ENGLISH, "%.1f%%rH", new Object[]{Float.valueOf(valueFromString3)}));
            }
            if (this.humiditySL != null) {
                this.humiditySL.addValue(valueFromString3, 0);
            }
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
        try {
            float valueFromString4 = getValueFromString((String) jSONObject.get("bar"), 3);
            if (this.barometerValue != null) {
                this.barometerValue.setText(String.format(Locale.ENGLISH, "%.1fmBar", new Object[]{Float.valueOf(valueFromString4)}));
            }
            if (this.barometerSL != null) {
                this.barometerSL.addValue(valueFromString4, 0);
            }
        } catch (JSONException e4) {
            e4.printStackTrace();
        }
        try {
            float valueFromString5 = getValueFromString((String) jSONObject.get(str3), 3);
            if (!(this.accelerometerValue == null || this.acceleromterSL == null)) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format(Locale.ENGLISH, "X:%.2fG,", new Object[]{Float.valueOf(valueFromString5)}));
                this.acceleromterSL.addValue(valueFromString5, 0);
                float valueFromString6 = getValueFromString((String) jSONObject.get(str3), 4);
                sb.append(String.format(Locale.ENGLISH, "Y:%.2fG, ", new Object[]{Float.valueOf(valueFromString6)}));
                this.acceleromterSL.addValue(valueFromString6, 1);
                float valueFromString7 = getValueFromString((String) jSONObject.get(str3), 5);
                sb.append(String.format(Locale.ENGLISH, "Z:%.2fG", new Object[]{Float.valueOf(valueFromString7)}));
                this.acceleromterSL.addValue(valueFromString7, 2);
                this.accelerometerValue.setText(sb.toString());
            }
            float valueFromString8 = getValueFromString((String) jSONObject.get(str2), 3);
            if (!(this.magnetometerValue == null || this.magnetometerSL == null)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(String.format(Locale.ENGLISH, "X:%.2fuT,", new Object[]{Float.valueOf(valueFromString8)}));
                this.magnetometerSL.addValue(valueFromString8, 0);
                float valueFromString9 = getValueFromString((String) jSONObject.get(str2), 4);
                sb2.append(String.format(Locale.ENGLISH, "Y:%.2fuT, ", new Object[]{Float.valueOf(valueFromString9)}));
                this.magnetometerSL.addValue(valueFromString9, 1);
                float valueFromString10 = getValueFromString((String) jSONObject.get(str2), 5);
                sb2.append(String.format(Locale.ENGLISH, "Z:%.2fuT", new Object[]{Float.valueOf(valueFromString10)}));
                this.magnetometerSL.addValue(valueFromString10, 2);
                this.magnetometerValue.setText(sb2.toString());
            }
            float valueFromString11 = getValueFromString((String) jSONObject.get(str), 3);
            if (!(this.gyroscopeValue == null || this.gyroscopeSL == null)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(String.format(Locale.ENGLISH, "X:%.2f°/s,", new Object[]{Float.valueOf(valueFromString11)}));
                this.gyroscopeSL.addValue(valueFromString11, 0);
                float valueFromString12 = getValueFromString((String) jSONObject.get(str), 4);
                sb3.append(String.format(Locale.ENGLISH, "Y:%.2f°/s, ", new Object[]{Float.valueOf(valueFromString12)}));
                this.gyroscopeSL.addValue(valueFromString12, 1);
                float valueFromString13 = getValueFromString((String) jSONObject.get(str), 5);
                sb3.append(String.format(Locale.ENGLISH, "Z:%.2f°/s", new Object[]{Float.valueOf(valueFromString13)}));
                this.gyroscopeSL.addValue(valueFromString13, 2);
                this.gyroscopeValue.setText(sb3.toString());
            }
        } catch (JSONException e5) {
            e5.printStackTrace();
        }
        try {
            switch ((int) getValueFromString((String) jSONObject.get("key"), 0)) {
                case 1:
                    this.leftButton.setImageResource(R.mipmap.left_key_off);
                    this.rightButton.setImageResource(R.mipmap.right_key_on);
                    this.reedButton.setImageResource(R.mipmap.reed_relay_off);
                    this.simpleKeysSL.addValue(1.0f, 0);
                    this.simpleKeysSL.addValue(0.0f, 1);
                    this.simpleKeysSL.addValue(0.0f, 2);
                    break;
                case 2:
                    this.leftButton.setImageResource(R.mipmap.left_key_on);
                    this.rightButton.setImageResource(R.mipmap.right_key_off);
                    this.reedButton.setImageResource(R.mipmap.reed_relay_off);
                    this.simpleKeysSL.addValue(0.0f, 0);
                    this.simpleKeysSL.addValue(1.0f, 1);
                    this.simpleKeysSL.addValue(0.0f, 2);
                    break;
                case 3:
                    this.leftButton.setImageResource(R.mipmap.left_key_on);
                    this.rightButton.setImageResource(R.mipmap.right_key_on);
                    this.reedButton.setImageResource(R.mipmap.reed_relay_off);
                    this.simpleKeysSL.addValue(1.0f, 0);
                    this.simpleKeysSL.addValue(1.0f, 1);
                    this.simpleKeysSL.addValue(0.0f, 2);
                    break;
                case 4:
                    this.leftButton.setImageResource(R.mipmap.left_key_off);
                    this.rightButton.setImageResource(R.mipmap.right_key_off);
                    this.reedButton.setImageResource(R.mipmap.reed_relay_on);
                    this.simpleKeysSL.addValue(0.0f, 0);
                    this.simpleKeysSL.addValue(0.0f, 1);
                    this.simpleKeysSL.addValue(1.0f, 2);
                    break;
                case 5:
                    this.leftButton.setImageResource(R.mipmap.left_key_off);
                    this.rightButton.setImageResource(R.mipmap.right_key_on);
                    this.reedButton.setImageResource(R.mipmap.reed_relay_off);
                    this.simpleKeysSL.addValue(1.0f, 0);
                    this.simpleKeysSL.addValue(0.0f, 1);
                    this.simpleKeysSL.addValue(1.0f, 2);
                    break;
                case 6:
                    this.leftButton.setImageResource(R.mipmap.left_key_on);
                    this.rightButton.setImageResource(R.mipmap.right_key_off);
                    this.reedButton.setImageResource(R.mipmap.reed_relay_off);
                    this.simpleKeysSL.addValue(0.0f, 0);
                    this.simpleKeysSL.addValue(1.0f, 1);
                    this.simpleKeysSL.addValue(1.0f, 2);
                    break;
                case 7:
                    this.leftButton.setImageResource(R.mipmap.left_key_on);
                    this.rightButton.setImageResource(R.mipmap.right_key_on);
                    this.reedButton.setImageResource(R.mipmap.reed_relay_on);
                    this.simpleKeysSL.addValue(1.0f, 0);
                    this.simpleKeysSL.addValue(1.0f, 1);
                    this.simpleKeysSL.addValue(1.0f, 2);
                    break;
                default:
                    this.leftButton.setImageResource(R.mipmap.left_key_off);
                    this.rightButton.setImageResource(R.mipmap.right_key_off);
                    this.reedButton.setImageResource(R.mipmap.reed_relay_off);
                    this.simpleKeysSL.addValue(0.0f, 0);
                    this.simpleKeysSL.addValue(0.0f, 1);
                    this.simpleKeysSL.addValue(0.0f, 2);
                    break;
            }
        } catch (JSONException e6) {
            e6.printStackTrace();
        }
        try {
            float valueFromString14 = getValueFromString((String) jSONObject.get("opt"), 1);
            this.optoValue.setText(String.format(Locale.ENGLISH, "%.1f Lux", new Object[]{Float.valueOf(valueFromString14)}));
            this.optoSL.addValue(valueFromString14, 0);
        } catch (JSONException e7) {
            e7.printStackTrace();
        }
    }

    private float getValueFromString(String str, int i) {
        int i2 = 0;
        try {
            Scanner useDelimiter = new Scanner(str).useDelimiter("\\s* \\s*");
            while (useDelimiter.hasNext()) {
                int i3 = i2 + 1;
                if (i2 == i) {
                    return Float.parseFloat(useDelimiter.next());
                }
                useDelimiter.next();
                i2 = i3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public void onClick(View view) {
        Log.d(TAG, "Firmware clicked");
        Intent intent = new Intent(this.mThis, wifiOTAActivity.class);
        intent.putExtra(DeviceActivity.EXTRA_DEVICE, this.ipAddress);
        startActivityForResult(intent, 0);
    }

    public static void writeNewSSIDConfig(Context context, String str, String str2, int i, String str3) {
        final String str4 = str2;
        final String str5 = str3;
        final int i2 = i;
        final Context context2 = context;
        final String str6 = str;
        C10472 r0 = new Runnable() {
            public void run() {
                Log.i(wifiDeviceActivity.TAG, "inside writeNewSSIDConfig");
                StringBuilder sb = new StringBuilder();
                sb.append(wifiDeviceActivity.WIFI_DEVICE_ADD_PROFILE_UP_TO_SSID);
                sb.append(str4);
                sb.append(wifiDeviceActivity.WIFI_DEVICE_ADD_PROFILE_UP_TO_PWD);
                sb.append(str5);
                sb.append(wifiDeviceActivity.WIFI_DEVICE_ADD_PROFILE_UP_TO_ENC);
                sb.append(i2);
                sb.append(wifiDeviceActivity.WIFI_DEVICE_ADD_PROFILE_LAST_PART);
                wifiDeviceActivity.postToWiFiDevice(context2, str6, wifiDeviceActivity.WIFI_DEVICE_ADD_PROFILES_URL, sb.toString(), true);
            }
        };
        new Thread(r0).start();
    }

    public static void writeModeChange(final Context context, final String str, final String str2) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(DNSConstants.CLOSE_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(wifiDeviceActivity.WIFI_DEVICE_CHANGE_MODE);
                sb.append(str2);
                wifiDeviceActivity.postToWiFiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_MODE_CHANGE_URL, sb.toString(), true);
            }
        }).start();
    }

    public static void enterProvMode(final Context context, final String str) {
        new Thread(new Runnable() {
            public void run() {
                Log.i(wifiDeviceActivity.TAG, "inside enterProvMode");
                wifiDeviceActivity.postToWiFiDevice(context, str, "", wifiDeviceActivity.WIFI_DEVICE_PROVISONING_MODE_START, false);
            }
        }).start();
    }

    public static void writeProvEnd(final Context context, final String str, final String str2) {
        new Thread(new Runnable() {
            public void run() {
                Log.i(wifiDeviceActivity.TAG, "inside writeProvEnd");
                StringBuilder sb = new StringBuilder();
                sb.append("__SL_P_S.R=setup.html&__SL_P_UAN=");
                sb.append(str2);
                wifiDeviceActivity.postToWiFiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_PROFILE_ADD_URL, sb.toString(), true);
            }
        }).start();
    }

    public static void readConfigResult(final Context context, final String str) {
        new Thread(new Runnable() {
            public void run() {
                wifiDeviceActivity.getFromWifiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_GET_CFG_RESULT, false, null);
            }
        }).start();
    }

    public static void startOTAUpgrade(final Context context, final String str, final String str2) {
        new Thread(new Runnable() {
            public void run() {
                Log.i(wifiDeviceActivity.TAG, "inside startOTAUpgrade");
                StringBuilder sb = new StringBuilder();
                sb.append("__SL_P_OTS=");
                sb.append(str2);
                wifiDeviceActivity.postToWiFiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_ADD_PROFILES_URL, sb.toString(), true);
            }
        }).start();
    }

    public static void interrogateDevice(final Context context, final String str) {
        new Thread(new Runnable() {
            public void run() {
                wifiDeviceActivity.getFromWifiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_ABOUT_URL, false, null);
            }
        }).start();
    }

    public static void configureDevice(final Context context, final String str, final int i) {
        new Thread(new Runnable() {
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append(wifiDeviceActivity.WIFI_DEVICE_CONFIGURE_SENSORS);
                sb.append(i);
                wifiDeviceActivity.postToWiFiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_CONFIG_URL, sb.toString(), true);
            }
        }).start();
    }

    public static void getDeviceConfig(final Context context, final String str) {
        new Thread(new Runnable() {
            public void run() {
                wifiDeviceActivity.postToWiFiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_CONFIG_URL, wifiDeviceActivity.WIFI_DEVICE_GET_CONFIG, true);
            }
        }).start();
    }

    public static void getParamDeviceConfig(final Context context, final String str, final PingCallback pingCallback) {
        new Thread(new Runnable() {
            public void run() {
                wifiDeviceActivity.getFromWifiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_PARAMETER_CONFIG_URL, true, pingCallback);
            }
        }).start();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0172  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x01af  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:21:0x00b3=Splitter:B:21:0x00b3, B:84:0x01a5=Splitter:B:84:0x01a5, B:50:0x010d=Splitter:B:50:0x010d, B:67:0x014f=Splitter:B:67:0x014f} */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:36:0x00d1=Splitter:B:36:0x00d1, B:53:0x0113=Splitter:B:53:0x0113, B:70:0x0154=Splitter:B:70:0x0154} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void getFromWifiDevice(android.content.Context r16, java.lang.String r17, java.lang.String r18, boolean r19, com.p004ti.wifi.utils.Ping.PingCallback r20) {
        /*
            r1 = r16
            r2 = r17
            r3 = r18
            r4 = r20
            java.lang.String r5 = "/"
            java.lang.String r6 = "wifiDeviceActivity"
            java.lang.String r7 = "host"
            java.lang.String r8 = "name"
            java.lang.String r9 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_IP_ADDR"
            java.lang.String r10 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_URL"
            java.lang.String r11 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_DATA"
            java.lang.String r12 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE"
            java.net.URL r0 = new java.net.URL     // Catch:{ MalformedURLException -> 0x01ec }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ MalformedURLException -> 0x01ec }
            r13.<init>()     // Catch:{ MalformedURLException -> 0x01ec }
            java.lang.String r14 = "http://"
            r13.append(r14)     // Catch:{ MalformedURLException -> 0x01ec }
            r13.append(r2)     // Catch:{ MalformedURLException -> 0x01ec }
            r13.append(r5)     // Catch:{ MalformedURLException -> 0x01ec }
            r13.append(r3)     // Catch:{ MalformedURLException -> 0x01ec }
            java.lang.String r13 = r13.toString()     // Catch:{ MalformedURLException -> 0x01ec }
            r0.<init>(r13)     // Catch:{ MalformedURLException -> 0x01ec }
            r5 = 0
            java.lang.String r13 = ""
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ ConnectException -> 0x0153, IOException -> 0x0112, Exception -> 0x00d0 }
            r14 = r0
            java.net.HttpURLConnection r14 = (java.net.HttpURLConnection) r14     // Catch:{ ConnectException -> 0x0153, IOException -> 0x0112, Exception -> 0x00d0 }
            java.lang.String r0 = "GET"
            r14.setRequestMethod(r0)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            java.lang.String r0 = "Connection"
            java.lang.String r5 = "close"
            r14.setRequestProperty(r0, r5)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            r0 = 1
            r14.setDoInput(r0)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            java.io.InputStream r0 = r14.getInputStream()     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            java.lang.String r13 = readStream(r0)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            int r0 = r14.getResponseCode()     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            r5.<init>()     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            java.lang.String r15 = "HTTP Response : "
            r5.append(r15)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            r5.append(r0)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            java.lang.String r0 = " Length : "
            r5.append(r0)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            int r0 = r13.length()     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            r5.append(r0)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            java.lang.String r0 = r5.toString()     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            android.util.Log.d(r6, r0)     // Catch:{ ConnectException -> 0x00c7, IOException -> 0x00c4, Exception -> 0x00c1, all -> 0x00bf }
            int r0 = r13.length()
            if (r0 <= 0) goto L_0x01a8
            android.content.Intent r5 = new android.content.Intent
            r5.<init>()
            r5.setAction(r12)
            r5.putExtra(r11, r13)
            r5.putExtra(r10, r3)
            r5.putExtra(r9, r2)
            if (r19 == 0) goto L_0x00aa
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x00a6 }
            r0.<init>()     // Catch:{ Exception -> 0x00a6 }
            java.lang.String r3 = getName(r13)     // Catch:{ Exception -> 0x00a6 }
            r0.put(r8, r3)     // Catch:{ Exception -> 0x00a6 }
            r0.put(r7, r2)     // Catch:{ Exception -> 0x00a6 }
            r4.pingDeviceFetched(r0)     // Catch:{ Exception -> 0x00a6 }
            goto L_0x00aa
        L_0x00a6:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00aa:
            r1.sendBroadcast(r5)     // Catch:{ Exception -> 0x00ae }
            goto L_0x00b3
        L_0x00ae:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x00b3:
            r14.disconnect()     // Catch:{ NullPointerException -> 0x00b8 }
            goto L_0x01a8
        L_0x00b8:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
            goto L_0x01a8
        L_0x00bf:
            r0 = move-exception
            goto L_0x00cd
        L_0x00c1:
            r0 = move-exception
            r5 = r14
            goto L_0x00d1
        L_0x00c4:
            r0 = move-exception
            r5 = r14
            goto L_0x0113
        L_0x00c7:
            r0 = move-exception
            r5 = r14
            goto L_0x0154
        L_0x00cb:
            r0 = move-exception
            r14 = r5
        L_0x00cd:
            r5 = r0
            goto L_0x01a9
        L_0x00d0:
            r0 = move-exception
        L_0x00d1:
            r0.printStackTrace()     // Catch:{ all -> 0x00cb }
            int r0 = r13.length()
            if (r0 <= 0) goto L_0x01a8
            android.content.Intent r6 = new android.content.Intent
            r6.<init>()
            r6.setAction(r12)
            r6.putExtra(r11, r13)
            r6.putExtra(r10, r3)
            r6.putExtra(r9, r2)
            if (r19 == 0) goto L_0x0104
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x0100 }
            r0.<init>()     // Catch:{ Exception -> 0x0100 }
            java.lang.String r3 = getName(r13)     // Catch:{ Exception -> 0x0100 }
            r0.put(r8, r3)     // Catch:{ Exception -> 0x0100 }
            r0.put(r7, r2)     // Catch:{ Exception -> 0x0100 }
            r4.pingDeviceFetched(r0)     // Catch:{ Exception -> 0x0100 }
            goto L_0x0104
        L_0x0100:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0104:
            r1.sendBroadcast(r6)     // Catch:{ Exception -> 0x0108 }
            goto L_0x010d
        L_0x0108:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x010d:
            r5.disconnect()     // Catch:{ NullPointerException -> 0x00b8 }
            goto L_0x01a8
        L_0x0112:
            r0 = move-exception
        L_0x0113:
            r0.printStackTrace()     // Catch:{ all -> 0x00cb }
            int r0 = r13.length()
            if (r0 <= 0) goto L_0x01a8
            android.content.Intent r6 = new android.content.Intent
            r6.<init>()
            r6.setAction(r12)
            r6.putExtra(r11, r13)
            r6.putExtra(r10, r3)
            r6.putExtra(r9, r2)
            if (r19 == 0) goto L_0x0146
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x0142 }
            r0.<init>()     // Catch:{ Exception -> 0x0142 }
            java.lang.String r3 = getName(r13)     // Catch:{ Exception -> 0x0142 }
            r0.put(r8, r3)     // Catch:{ Exception -> 0x0142 }
            r0.put(r7, r2)     // Catch:{ Exception -> 0x0142 }
            r4.pingDeviceFetched(r0)     // Catch:{ Exception -> 0x0142 }
            goto L_0x0146
        L_0x0142:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0146:
            r1.sendBroadcast(r6)     // Catch:{ Exception -> 0x014a }
            goto L_0x014f
        L_0x014a:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x014f:
            r5.disconnect()     // Catch:{ NullPointerException -> 0x00b8 }
            goto L_0x01a8
        L_0x0153:
            r0 = move-exception
        L_0x0154:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ all -> 0x00cb }
            r14.<init>()     // Catch:{ all -> 0x00cb }
            java.lang.String r15 = "Connect failed with message: "
            r14.append(r15)     // Catch:{ all -> 0x00cb }
            java.lang.String r0 = r0.getLocalizedMessage()     // Catch:{ all -> 0x00cb }
            r14.append(r0)     // Catch:{ all -> 0x00cb }
            java.lang.String r0 = r14.toString()     // Catch:{ all -> 0x00cb }
            android.util.Log.d(r6, r0)     // Catch:{ all -> 0x00cb }
            int r0 = r13.length()
            if (r0 <= 0) goto L_0x01a8
            android.content.Intent r6 = new android.content.Intent
            r6.<init>()
            r6.setAction(r12)
            r6.putExtra(r11, r13)
            r6.putExtra(r10, r3)
            r6.putExtra(r9, r2)
            if (r19 == 0) goto L_0x019c
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x0198 }
            r0.<init>()     // Catch:{ Exception -> 0x0198 }
            java.lang.String r3 = getName(r13)     // Catch:{ Exception -> 0x0198 }
            r0.put(r8, r3)     // Catch:{ Exception -> 0x0198 }
            r0.put(r7, r2)     // Catch:{ Exception -> 0x0198 }
            r4.pingDeviceFetched(r0)     // Catch:{ Exception -> 0x0198 }
            goto L_0x019c
        L_0x0198:
            r0 = move-exception
            r0.printStackTrace()
        L_0x019c:
            r1.sendBroadcast(r6)     // Catch:{ Exception -> 0x01a0 }
            goto L_0x01a5
        L_0x01a0:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x01a5:
            r5.disconnect()     // Catch:{ NullPointerException -> 0x00b8 }
        L_0x01a8:
            return
        L_0x01a9:
            int r0 = r13.length()
            if (r0 <= 0) goto L_0x01eb
            android.content.Intent r6 = new android.content.Intent
            r6.<init>()
            r6.setAction(r12)
            r6.putExtra(r11, r13)
            r6.putExtra(r10, r3)
            r6.putExtra(r9, r2)
            if (r19 == 0) goto L_0x01d9
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x01d5 }
            r0.<init>()     // Catch:{ Exception -> 0x01d5 }
            java.lang.String r3 = getName(r13)     // Catch:{ Exception -> 0x01d5 }
            r0.put(r8, r3)     // Catch:{ Exception -> 0x01d5 }
            r0.put(r7, r2)     // Catch:{ Exception -> 0x01d5 }
            r4.pingDeviceFetched(r0)     // Catch:{ Exception -> 0x01d5 }
            goto L_0x01d9
        L_0x01d5:
            r0 = move-exception
            r0.printStackTrace()
        L_0x01d9:
            r1.sendBroadcast(r6)     // Catch:{ Exception -> 0x01dd }
            goto L_0x01e2
        L_0x01dd:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x01e2:
            r14.disconnect()     // Catch:{ NullPointerException -> 0x01e6 }
            goto L_0x01eb
        L_0x01e6:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x01eb:
            throw r5
        L_0x01ec:
            r0 = move-exception
            r0.printStackTrace()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "URL \"http://"
            r0.append(r1)
            r0.append(r2)
            r0.append(r5)
            r0.append(r3)
            java.lang.String r1 = "\" is malformed !"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r6, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.wifiDeviceActivity.getFromWifiDevice(android.content.Context, java.lang.String, java.lang.String, boolean, com.ti.wifi.utils.Ping$PingCallback):void");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x01e0 A[Catch:{ Exception -> 0x0268 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0201 A[Catch:{ Exception -> 0x0268 }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0244 A[Catch:{ Exception -> 0x0268 }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0264 A[Catch:{ Exception -> 0x0268 }] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0280 A[Catch:{ Exception -> 0x0284 }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:70:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:30:0x01c5=Splitter:B:30:0x01c5, B:37:0x01e6=Splitter:B:37:0x01e6, B:44:0x0206=Splitter:B:44:0x0206, B:51:0x0249=Splitter:B:51:0x0249} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void postToWiFiDevice(android.content.Context r9, java.lang.String r10, java.lang.String r11, java.lang.String r12, boolean r13) {
        /*
            java.lang.String r0 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_DISCONNECTED"
            java.lang.String r1 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_IP_ADDR"
            java.lang.String r2 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_URL"
            java.lang.String r3 = "wifiDeviceActivity"
            java.lang.String r4 = "inside postToWiFiDevice"
            android.util.Log.i(r3, r4)
            java.lang.String r4 = "MODE - inside postToWiFiDevice"
            android.util.Log.i(r3, r4)
            java.lang.String r4 = "StageThree - Interr - inside postToWiFiDevice"
            android.util.Log.i(r3, r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "post data - "
            r4.append(r5)
            r4.append(r12)
            java.lang.String r4 = r4.toString()
            android.util.Log.i(r3, r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "MODE - post data - "
            r4.append(r5)
            r4.append(r12)
            java.lang.String r4 = r4.toString()
            android.util.Log.i(r3, r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "StageThree - Interr - post data - "
            r4.append(r5)
            r4.append(r12)
            java.lang.String r4 = r4.toString()
            android.util.Log.i(r3, r4)
            r4 = 0
            r5 = 0
            java.net.URL r6 = new java.net.URL     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.<init>()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = "http://"
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.append(r10)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = "/"
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.append(r11)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r7 = r7.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r6.<init>(r7)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.<init>()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = "inside postToWiFiDevice - post to: "
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = r6.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r7 = r7.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            android.util.Log.i(r3, r7)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.<init>()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = "MODE - inside postToWiFiDevice - post to: "
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = r6.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r7 = r7.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            android.util.Log.i(r3, r7)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.<init>()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = "FACTORY DEFAULT - inside postToWiFiDevice - post to: "
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = r6.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r7 = r7.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            android.util.Log.i(r3, r7)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.<init>()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = "StageThree - Interr - inside postToWiFiDevice - post to: "
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r8 = r6.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r7 = r7.toString()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            android.util.Log.i(r3, r7)     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.net.URLConnection r6 = r6.openConnection()     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.net.HttpURLConnection r6 = (java.net.HttpURLConnection) r6     // Catch:{ ProtocolException -> 0x0248, FileNotFoundException -> 0x0205, IOException -> 0x01e5, Exception -> 0x01c4 }
            java.lang.String r4 = "Content-type"
            java.lang.String r7 = "application/x-www-form-urlencoded"
            r6.setRequestProperty(r4, r7)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r4 = "User-Agent"
            java.lang.String r7 = "Mozilla/5.0 ( compatible ) "
            r6.setRequestProperty(r4, r7)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r4 = "Accept"
            java.lang.String r7 = "*/*"
            r6.setRequestProperty(r4, r7)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r4 = "Connection"
            java.lang.String r7 = "close"
            r6.setRequestProperty(r4, r7)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r4 = 1
            if (r13 == 0) goto L_0x0100
            r6.setDoInput(r4)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r7 = 5000(0x1388, float:7.006E-42)
            r6.setReadTimeout(r7)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
        L_0x0100:
            android.content.Intent r7 = new android.content.Intent     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r7.<init>()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r8 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE"
            r7.setAction(r8)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r7.putExtra(r2, r11)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r7.putExtra(r1, r10)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            int r8 = r12.length()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            if (r8 <= 0) goto L_0x0128
            r6.setDoOutput(r4)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.io.OutputStreamWriter r4 = new java.io.OutputStreamWriter     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.io.OutputStream r8 = r6.getOutputStream()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r4.<init>(r8)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r4.write(r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r4.close()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
        L_0x0128:
            if (r13 == 0) goto L_0x015f
            java.io.InputStream r12 = r6.getInputStream()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r12 = readStream(r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r13 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_DATA"
            r7.putExtra(r13, r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r13.<init>()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r4 = "Response data:"
            r13.append(r4)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r13.append(r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r13 = r13.toString()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            android.util.Log.d(r3, r13)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r13.<init>()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r4 = "StageThree - Interr - Response data:"
            r13.append(r4)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r13.append(r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r12 = r13.toString()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            android.util.Log.d(r3, r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
        L_0x015f:
            int r5 = r6.getResponseCode()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r12.<init>()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r13 = "Response code:"
            r12.append(r13)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r12.append(r5)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r12 = r12.toString()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            android.util.Log.d(r3, r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r12.<init>()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r13 = "StageThree - Interr - Response code:"
            r12.append(r13)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r12.append(r5)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r12 = r12.toString()     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            android.util.Log.d(r3, r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r12 = "com.ti.sensortag.wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_CODE"
            r7.putExtra(r12, r5)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            r9.sendBroadcast(r7)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            java.lang.String r12 = "StageThree - Interr - BR SENT"
            android.util.Log.d(r3, r12)     // Catch:{ ProtocolException -> 0x01bd, FileNotFoundException -> 0x01ba, IOException -> 0x01b7, Exception -> 0x01b4, all -> 0x01b0 }
            android.content.Intent r12 = new android.content.Intent     // Catch:{ Exception -> 0x0268 }
            r12.<init>()     // Catch:{ Exception -> 0x0268 }
            r12.setAction(r0)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r2, r11)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r1, r10)     // Catch:{ Exception -> 0x0268 }
            r9.sendBroadcast(r12)     // Catch:{ Exception -> 0x0268 }
            if (r6 == 0) goto L_0x026c
            r6.disconnect()     // Catch:{ Exception -> 0x0268 }
            goto L_0x026c
        L_0x01b0:
            r12 = move-exception
            r4 = r6
            goto L_0x026d
        L_0x01b4:
            r12 = move-exception
            r4 = r6
            goto L_0x01c5
        L_0x01b7:
            r12 = move-exception
            r4 = r6
            goto L_0x01e6
        L_0x01ba:
            r12 = move-exception
            r4 = r6
            goto L_0x0206
        L_0x01bd:
            r12 = move-exception
            r4 = r6
            goto L_0x0249
        L_0x01c1:
            r12 = move-exception
            goto L_0x026d
        L_0x01c4:
            r12 = move-exception
        L_0x01c5:
            r12.printStackTrace()     // Catch:{ all -> 0x01c1 }
            java.lang.String r12 = "StageThree - Interr - Exception"
            android.util.Log.d(r3, r12)     // Catch:{ all -> 0x01c1 }
            android.content.Intent r12 = new android.content.Intent     // Catch:{ Exception -> 0x0268 }
            r12.<init>()     // Catch:{ Exception -> 0x0268 }
            r12.setAction(r0)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r2, r11)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r1, r10)     // Catch:{ Exception -> 0x0268 }
            r9.sendBroadcast(r12)     // Catch:{ Exception -> 0x0268 }
            if (r4 == 0) goto L_0x026c
            r4.disconnect()     // Catch:{ Exception -> 0x0268 }
            goto L_0x026c
        L_0x01e5:
            r12 = move-exception
        L_0x01e6:
            r12.printStackTrace()     // Catch:{ all -> 0x01c1 }
            java.lang.String r12 = "StageThree - Interr - IOException"
            android.util.Log.d(r3, r12)     // Catch:{ all -> 0x01c1 }
            android.content.Intent r12 = new android.content.Intent     // Catch:{ Exception -> 0x0268 }
            r12.<init>()     // Catch:{ Exception -> 0x0268 }
            r12.setAction(r0)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r2, r11)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r1, r10)     // Catch:{ Exception -> 0x0268 }
            r9.sendBroadcast(r12)     // Catch:{ Exception -> 0x0268 }
            if (r4 == 0) goto L_0x026c
            r4.disconnect()     // Catch:{ Exception -> 0x0268 }
            goto L_0x026c
        L_0x0205:
            r12 = move-exception
        L_0x0206:
            r12.printStackTrace()     // Catch:{ all -> 0x01c1 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c1 }
            r12.<init>()     // Catch:{ all -> 0x01c1 }
            java.lang.String r13 = "FileNotFoundExceptionGot / response "
            r12.append(r13)     // Catch:{ all -> 0x01c1 }
            r12.append(r5)     // Catch:{ all -> 0x01c1 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x01c1 }
            android.util.Log.d(r3, r12)     // Catch:{ all -> 0x01c1 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c1 }
            r12.<init>()     // Catch:{ all -> 0x01c1 }
            java.lang.String r13 = "FileNotFoundException / StageThree - Interr - Got response "
            r12.append(r13)     // Catch:{ all -> 0x01c1 }
            r12.append(r5)     // Catch:{ all -> 0x01c1 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x01c1 }
            android.util.Log.d(r3, r12)     // Catch:{ all -> 0x01c1 }
            android.content.Intent r12 = new android.content.Intent     // Catch:{ Exception -> 0x0268 }
            r12.<init>()     // Catch:{ Exception -> 0x0268 }
            r12.setAction(r0)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r2, r11)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r1, r10)     // Catch:{ Exception -> 0x0268 }
            r9.sendBroadcast(r12)     // Catch:{ Exception -> 0x0268 }
            if (r4 == 0) goto L_0x026c
            r4.disconnect()     // Catch:{ Exception -> 0x0268 }
            goto L_0x026c
        L_0x0248:
            r12 = move-exception
        L_0x0249:
            r12.printStackTrace()     // Catch:{ all -> 0x01c1 }
            java.lang.String r12 = "StageThree - Interr - ProtocolException"
            android.util.Log.d(r3, r12)     // Catch:{ all -> 0x01c1 }
            android.content.Intent r12 = new android.content.Intent     // Catch:{ Exception -> 0x0268 }
            r12.<init>()     // Catch:{ Exception -> 0x0268 }
            r12.setAction(r0)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r2, r11)     // Catch:{ Exception -> 0x0268 }
            r12.putExtra(r1, r10)     // Catch:{ Exception -> 0x0268 }
            r9.sendBroadcast(r12)     // Catch:{ Exception -> 0x0268 }
            if (r4 == 0) goto L_0x026c
            r4.disconnect()     // Catch:{ Exception -> 0x0268 }
            goto L_0x026c
        L_0x0268:
            r9 = move-exception
            r9.printStackTrace()
        L_0x026c:
            return
        L_0x026d:
            android.content.Intent r13 = new android.content.Intent     // Catch:{ Exception -> 0x0284 }
            r13.<init>()     // Catch:{ Exception -> 0x0284 }
            r13.setAction(r0)     // Catch:{ Exception -> 0x0284 }
            r13.putExtra(r2, r11)     // Catch:{ Exception -> 0x0284 }
            r13.putExtra(r1, r10)     // Catch:{ Exception -> 0x0284 }
            r9.sendBroadcast(r13)     // Catch:{ Exception -> 0x0284 }
            if (r4 == 0) goto L_0x0288
            r4.disconnect()     // Catch:{ Exception -> 0x0284 }
            goto L_0x0288
        L_0x0284:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0288:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.wifiDeviceActivity.postToWiFiDevice(android.content.Context, java.lang.String, java.lang.String, java.lang.String, boolean):void");
    }

    /* access modifiers changed from: private */
    public static String readStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static String parsePollDataToJSON(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\r\n");
        if (str.length() < 20) {
            return "";
        }
        try {
            Scanner useDelimiter = new Scanner(str).useDelimiter("\\s*<p id=\"\\s*");
            Log.d(TAG, useDelimiter.next());
            while (useDelimiter.hasNext()) {
                String next = useDelimiter.next();
                String substring = next.substring(0, 3);
                String substring2 = next.substring(5, next.indexOf("</p>"));
                sb.append("\"");
                sb.append(substring);
                sb.append("\":\"");
                sb.append(substring2);
                sb.append("\",\r\n");
            }
            sb.delete(sb.length() - 3, sb.length() - 1);
            sb.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getFirmwareString(String str) {
        String str2 = "";
        try {
            String substring = str.substring(str.indexOf("fwr\">") + 5);
            return substring.substring(0, substring.indexOf("<"));
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static String getMacString(String str) {
        String str2 = "";
        try {
            String substring = str.substring(str.indexOf("mac\">") + 5);
            return substring.substring(0, substring.indexOf("<"));
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    /* access modifiers changed from: private */
    public static String getURLString(String str) {
        String str2 = "";
        try {
            String substring = str.substring(str.indexOf("url\">") + 5);
            return substring.substring(0, substring.indexOf("<"));
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static String getName(String str) {
        String str2 = "";
        try {
            String substring = str.substring(str.indexOf("<p id=\"Device_Name_URN\">") + 24);
            return substring.substring(0, substring.indexOf("<"));
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static boolean isLatestFirmware(String str) {
        String str2 = TAG;
        try {
            Log.i(str2, "inside isLatestFirmware");
            String substring = str.substring(0, str.indexOf(" "));
            StringBuilder sb = new StringBuilder();
            sb.append("isLatestFirmware / firmware rev: ");
            sb.append(substring);
            Log.i(str2, sb.toString());
            if (str.equals(Constants.DEVICE_UNKNOWN_TOKEN_STRING)) {
                Log.i(str2, "isLatestFirmware / unknown token - return true");
                return true;
            }
            float parseFloat = Float.parseFloat(substring);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("isLatestFirmware / version: ");
            sb2.append(parseFloat);
            Log.i(str2, sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("isLatestFirmware / compare to: ");
            sb3.append(WIFI_DEVICE_LATEST_VERSION);
            Log.i(str2, sb3.toString());
            if (parseFloat != WIFI_DEVICE_LATEST_VERSION.floatValue()) {
                Log.i(str2, "isLatestFirmware / out-of-date firmware");
                return false;
            }
            Log.i(str2, "isLatestFirmware / firmware up-to-date");
            return true;
        } catch (Exception e) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("isLatestFirmware / ex: ");
            sb4.append(e);
            Log.i(str2, sb4.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deviceOnCorrectWLAN(Context context, String str) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.getType() == 1) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                String str2 = TAG;
                Log.d(str2, "Checking for correct WiFi");
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null) {
                    String str3 = "\"";
                    String replaceAll = connectionInfo.getSSID().replaceAll(str3, "");
                    if (replaceAll.startsWith(str3) && replaceAll.endsWith(str3)) {
                        replaceAll = replaceAll.substring(1, replaceAll.length() - 1);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Connected to : ");
                    sb.append(replaceAll);
                    sb.append(" / Looking for ");
                    sb.append(str);
                    Log.d(str2, sb.toString());
                    if (replaceAll.equals(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getCurrentConnectionSSID(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.getType() == 1) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                String str = TAG;
                Log.d(str, "Checking for correct WiFi");
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null) {
                    String str2 = "\"";
                    String replaceAll = connectionInfo.getSSID().replaceAll(str2, "");
                    if (replaceAll.startsWith(str2) && replaceAll.endsWith(str2)) {
                        replaceAll = replaceAll.substring(1, replaceAll.length() - 1);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Connected to : ");
                    sb.append(replaceAll);
                    Log.d(str, sb.toString());
                    return replaceAll;
                }
            }
        }
        return null;
    }

    public static void deviceSetWLAN(Context context, String str) {
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7 = str;
        String str8 = TAG;
        if (str7 == null || str7.equals("null") || str7.equals("")) {
            Log.i(str8, "deviceSetWLAN / SSID was null - do not try to connect");
        } else {
            Log.i(str8, "deviceSetWLAN - started");
            inside = true;
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            WifiConfiguration wifiConfiguration = null;
            List<WifiConfiguration> configuredNetworks = wifiManager != null ? wifiManager.getConfiguredNetworks() : null;
            if (configuredNetworks != null) {
                Iterator it = configuredNetworks.iterator();
                boolean z = false;
                while (true) {
                    str2 = "deviceSetWLAN / connect to desired configuration";
                    str3 = "deviceSetWLAN / enable desired wifiConfiguration";
                    str4 = "deviceSetWLAN / disconnect from current network";
                    str5 = "deviceSetWLAN / Wifi configuration for ";
                    str6 = "\"";
                    if (!it.hasNext()) {
                        break;
                    }
                    WifiConfiguration wifiConfiguration2 = (WifiConfiguration) it.next();
                    if (!z && wifiConfiguration2.SSID != null) {
                        String str9 = wifiConfiguration2.SSID;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str6);
                        sb.append(str7);
                        sb.append(str6);
                        if (str9.equals(sb.toString())) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str5);
                            sb2.append(str7);
                            sb2.append(" already exists - use it");
                            Log.i(str8, sb2.toString());
                            int i = wifiConfiguration2.networkId;
                            wifiManager.disconnect();
                            Log.i(str8, str4);
                            wifiManager.enableNetwork(i, true);
                            Log.i(str8, str3);
                            wifiManager.reconnect();
                            Log.i(str8, str2);
                            wifiConfiguration = wifiConfiguration2;
                            z = true;
                        }
                    }
                }
                if (wifiConfiguration == null) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str5);
                    sb3.append(str7);
                    sb3.append(" does not exists - create one");
                    Log.i(str8, sb3.toString());
                    WifiConfiguration wifiConfiguration3 = new WifiConfiguration();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str6);
                    sb4.append(str7);
                    sb4.append(str6);
                    wifiConfiguration3.SSID = sb4.toString();
                    wifiConfiguration3.hiddenSSID = false;
                    wifiConfiguration3.allowedKeyManagement.set(0);
                    wifiManager.addNetwork(wifiConfiguration3);
                    Log.i(str8, "deviceSetWLAN / added config");
                    wifiManager.saveConfiguration();
                    Log.i("deviceSetWLAN", "deviceSetWLAN / saved config");
                    Log.i(str8, "deviceSetWLAN / search for added config in configured networks list");
                    boolean z2 = false;
                    for (WifiConfiguration wifiConfiguration4 : configuredNetworks) {
                        if (!z2 && wifiConfiguration4.SSID != null && wifiConfiguration4.SSID.equals(wifiConfiguration3.SSID)) {
                            Log.i(str8, "deviceSetWLAN / FOUND added wifiConfiguration in wifiManger's configured networks list");
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("deviceSetWLAN / Added wifiConfiguration network id: ");
                            sb5.append(wifiConfiguration4.networkId);
                            Log.i(str8, sb5.toString());
                            Log.i(str8, "deviceSetWLAN / Found added network in list, connecting ...");
                            wifiManager.disconnect();
                            Log.i(str8, str4);
                            wifiManager.enableNetwork(wifiConfiguration4.networkId, true);
                            Log.i(str8, str3);
                            wifiManager.reconnect();
                            Log.i(str8, str2);
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("deviceSetWLAN / foundNetwork: ");
                            sb6.append(true);
                            Log.d(str8, sb6.toString());
                            z2 = true;
                        }
                    }
                }
                inside = false;
                Log.i(str8, "deviceSetWLAN / deviceSetWLAN - finished");
            }
        }
    }

    public static void deviceSetWLAN(Context context, WifiConfiguration wifiConfiguration) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        int addNetwork = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.saveConfiguration();
        wifiManager.disconnect();
        wifiManager.enableNetwork(addNetwork, true);
        wifiManager.reconnect();
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getStringFromConfigResult(java.lang.String r1) {
        /*
            int r0 = r1.hashCode()
            switch(r0) {
                case 48: goto L_0x0044;
                case 49: goto L_0x003a;
                case 50: goto L_0x0030;
                case 51: goto L_0x0026;
                case 52: goto L_0x001c;
                case 53: goto L_0x0012;
                case 54: goto L_0x0008;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x004e
        L_0x0008:
            java.lang.String r0 = "6"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x004e
            r1 = 6
            goto L_0x004f
        L_0x0012:
            java.lang.String r0 = "5"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x004e
            r1 = 5
            goto L_0x004f
        L_0x001c:
            java.lang.String r0 = "4"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x004e
            r1 = 4
            goto L_0x004f
        L_0x0026:
            java.lang.String r0 = "3"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x004e
            r1 = 3
            goto L_0x004f
        L_0x0030:
            java.lang.String r0 = "2"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x004e
            r1 = 2
            goto L_0x004f
        L_0x003a:
            java.lang.String r0 = "1"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x004e
            r1 = 1
            goto L_0x004f
        L_0x0044:
            java.lang.String r0 = "0"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x004e
            r1 = 0
            goto L_0x004f
        L_0x004e:
            r1 = -1
        L_0x004f:
            switch(r1) {
                case 0: goto L_0x0067;
                case 1: goto L_0x0064;
                case 2: goto L_0x0061;
                case 3: goto L_0x005e;
                case 4: goto L_0x005b;
                case 5: goto L_0x0058;
                case 6: goto L_0x0055;
                default: goto L_0x0052;
            }
        L_0x0052:
            java.lang.String r1 = "Unknown Error"
            return r1
        L_0x0055:
            java.lang.String r1 = "General Error"
            return r1
        L_0x0058:
            java.lang.String r1 = "Success ! Running Station mode"
            return r1
        L_0x005b:
            java.lang.String r1 = "Success ! Running AP mode"
            return r1
        L_0x005e:
            java.lang.String r1 = "IP not received via DHCP"
            return r1
        L_0x0061:
            java.lang.String r1 = "Connection To AP Failed"
            return r1
        L_0x0064:
            java.lang.String r1 = "Network not found"
            return r1
        L_0x0067:
            java.lang.String r1 = "Not Started"
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.wifiDeviceActivity.getStringFromConfigResult(java.lang.String):java.lang.String");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        try {
            if (this.wlanCon == null) {
                this.wlanCon = new wlanController(this.mThis);
                this.wlanCon.setDesiredWlanConfig(this.desiredWifiConfiguration);
                StringBuilder sb = new StringBuilder();
                sb.append("set desired: ");
                sb.append(this.desiredWifiConfiguration);
                Log.i("wlanController", sb.toString());
                this.wlanCon.startWlanController();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFactoryDefault(final Context context, final String str) {
        new Thread(new Runnable() {
            public void run() {
                wifiDeviceActivity.postToWiFiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_MODE_CHANGE_URL, wifiDeviceActivity.WIFI_DEVICE_FACTORY_RESET, true);
            }
        }).start();
    }

    public static void writeAwakeMode(final Context context, final String str, final int i) {
        new Thread(new Runnable() {
            public void run() {
                int i = i;
                String str = wifiDeviceActivity.WIFI_DEVICE_ACT_TIMEOUT;
                if (i == 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(wifiDeviceActivity.BATTERY_SAVER_MODE_TIMEOUT);
                    str = sb.toString();
                } else if (i == 1) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(wifiDeviceActivity.STAY_AWAKE_MODE_TIMEOUT);
                    str = sb2.toString();
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("MODE - str: ");
                sb3.append(str);
                Log.i(wifiDeviceActivity.TAG, sb3.toString());
                wifiDeviceActivity.postToWiFiDevice(context, str, wifiDeviceActivity.WIFI_DEVICE_MODE_CHANGE_URL, str, true);
            }
        }).start();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this.mThis, TopLevel.class);
        intent.setFlags(67108864);
        startActivity(intent);
    }
}
