package com.p004ti.wifi;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.p004ti.device_selector.DeviceActivity;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.wifi.utils.CFG_Result_Enum;
import com.p004ti.wifi.utils.Device;
import com.p004ti.wifi.utils.MDnsCallbackInterface;
import com.p004ti.wifi.utils.MDnsHelper;
import com.p004ti.wifi.utils.NetInfo;
import com.p004ti.wifi.utils.NetworkUtil;
import com.p004ti.wifi.utils.Ping;
import com.p004ti.wifi.utils.Ping.PingCallback;
import com.p004ti.wifi.utils.UdpBcastServer;
import com.ti.ble.simplelinkstarter.R;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import javax.jmdns.impl.constants.DNSConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.ti.wifi.ConfigureWiFiActivityStageThreeAPModeProvisioning */
public class ConfigureWiFiActivityStageThreeAPModeProvisioning extends AppCompatActivity {
    public static final String CONFIGURE_WIFI_ACTIVITY_STAGE_THREE_EXTRA_SSID = "com.ti.ble.sensortag.ConfigureWiFiStageThree.extra.SSID";
    private static final String IP_ADDRESS = "192.168.1.1";
    public static final String PROVISIONING_FAILED_TIMEOUT_ACTION = "com.ti.ble.sensortag.ConfigureWiFiStageThree.PROVISIONING_FAILED_TIMEOUT_ACTION";
    /* access modifiers changed from: private */
    public static String SCAN_FINISHED_BROADCAST_ACTION = "com.ti.WiFi.ConfigureWiFiActivityStageThreeAPModeProvisioning.SCAN_FINISHED";
    private static final String TAG = "StageThreeAPMode";
    /* access modifiers changed from: private */
    public int cgfTryNumber = 0;
    /* access modifiers changed from: private */
    public String chosenSTDeviceSSID;
    /* access modifiers changed from: private */
    public Timer connectToConfigSSIDTimer;
    /* access modifiers changed from: private */
    public WifiConfiguration desiredWifi;
    /* access modifiers changed from: private */
    public boolean deviceFound;
    /* access modifiers changed from: private */
    public boolean didInter;
    /* access modifiers changed from: private */
    public int encryption;
    IntentFilter filt;
    private boolean firstTime = true;
    /* access modifiers changed from: private */
    public Timer fwUTDTimer;
    /* access modifiers changed from: private */
    public TextView fwVersion;
    /* access modifiers changed from: private */
    public Timer getCFGResultViaDeviceAsAPTimeoutTimer;
    private TextView ipAddr;
    /* access modifiers changed from: private */
    public String ipAdressForExtra;
    private boolean isScanning;
    private boolean killMDNSScanWhenInBackground;
    /* access modifiers changed from: private */
    public ProgressBar loader;
    private MDnsCallbackInterface mDNSCallback;
    /* access modifiers changed from: private */
    public Device mDevice;
    /* access modifiers changed from: private */
    public MDnsHelper mDnsHelper;
    private Ping mPing;
    private PingCallback mPingCallback;
    /* access modifiers changed from: private */
    public ConfigureWiFiActivityStageThreeAPModeProvisioning mThis;
    /* access modifiers changed from: private */
    public TextView macAddr;
    /* access modifiers changed from: private */
    public String pass;
    private Thread pingThread;
    private ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public Timer provDialogTimer;
    private BroadcastReceiver receiver;
    /* access modifiers changed from: private */
    public WifiConfiguration sensorTagWifi;
    /* access modifiers changed from: private */
    public String ssidToSend;
    /* access modifiers changed from: private */
    public WifiConfiguration ssidToSendConfig;
    /* access modifiers changed from: private */
    public boolean startConVerHasBeenCalled;
    /* access modifiers changed from: private */
    public wifiStageThreeStates state = wifiStageThreeStates.START;
    /* access modifiers changed from: private */
    public TextView status;
    private UdpBcastServer udpBcastServer;
    private Thread udpBcastServerThread;
    /* access modifiers changed from: private */
    public wlanController wController;
    /* access modifiers changed from: private */
    public Timer writeNewSSIDConfigTimer;

    /* renamed from: com.ti.wifi.ConfigureWiFiActivityStageThreeAPModeProvisioning$GetCFGResult */
    class GetCFGResult extends AsyncTask<Boolean, Void, String> {
        /* access modifiers changed from: private */
        public Boolean mIsViaSTDeviceAsSTA;

        GetCFGResult() {
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            String str2 = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
            Log.i(str2, "***GetCFGResult/onPost");
            StringBuilder sb = new StringBuilder();
            sb.append("***Cfg result from ST: ");
            sb.append(str);
            Log.i(str2, sb.toString());
            if (str != null) {
                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.PROV_FINISHED;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("State: ");
                sb2.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                Log.i(str2, sb2.toString());
                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.startConVerHasBeenCalled = false;
                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.loader.setVisibility(8);
                if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getCFGResultViaDeviceAsAPTimeoutTimer != null) {
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getCFGResultViaDeviceAsAPTimeoutTimer.cancel();
                    Log.i(str2, "cancel connection to SensorTag device for cfg verification via device as AP timeout - got result");
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getCFGResultViaDeviceAsAPTimeoutTimer = null;
                }
                if (NetworkUtil.getResultTypeCFGString(str) == CFG_Result_Enum.Success) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("***Provisioning Successful + result:\n");
                    sb3.append(str);
                    Log.i(str2, sb3.toString());
                    ConfigureWiFiActivityStageThreeAPModeProvisioning configureWiFiActivityStageThreeAPModeProvisioning = ConfigureWiFiActivityStageThreeAPModeProvisioning.this;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Provisioning finished with result :\n");
                    sb4.append(str);
                    configureWiFiActivityStageThreeAPModeProvisioning.showProvDialog("Provisioning Successful", sb4.toString(), true);
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.PROV_FINISHED;
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.finishAPProvProcess(Boolean.valueOf(false));
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound = false;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("1GetCFGResult/deviceFound: ");
                    sb5.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound);
                    Log.i(str2, sb5.toString());
                } else {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("***Provisioning Failed + result:\n");
                    sb6.append(str);
                    Log.i(str2, sb6.toString());
                    ConfigureWiFiActivityStageThreeAPModeProvisioning configureWiFiActivityStageThreeAPModeProvisioning2 = ConfigureWiFiActivityStageThreeAPModeProvisioning.this;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("Provisioning failed with result :\n");
                    sb7.append(str);
                    configureWiFiActivityStageThreeAPModeProvisioning2.showProvDialog("Provisioning Failed", sb7.toString(), false);
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.PROV_FINISHED;
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.finishAPProvProcess(Boolean.valueOf(true));
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound = false;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("2GetCFGResult/deviceFound: ");
                    sb8.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound);
                    Log.i(str2, sb8.toString());
                }
            } else {
                Log.i(str2, "***Result is null - make another cfg verification retrieval attempt");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.cgfTryNumber = ConfigureWiFiActivityStageThreeAPModeProvisioning.this.cgfTryNumber + 1;
                        StringBuilder sb = new StringBuilder();
                        sb.append("cgfTryNumber: ");
                        sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.cgfTryNumber);
                        Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, sb.toString());
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getCFG(GetCFGResult.this.mIsViaSTDeviceAsSTA);
                    }
                }, 3000);
            }
            super.onPostExecute(str);
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Boolean... boolArr) {
            String str;
            String str2 = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
            Log.i(str2, "*** GetCFGResult called");
            Log.i(str2, "*** GetCFGResult/doInBackground");
            this.mIsViaSTDeviceAsSTA = boolArr[0];
            StringBuilder sb = new StringBuilder();
            sb.append("mIsViaSTDeviceAsSTA: ");
            sb.append(this.mIsViaSTDeviceAsSTA);
            Log.i(str2, sb.toString());
            if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mDevice == null || ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mDevice.host.equals("")) {
                str = "http://192.168.1.1";
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("http://");
                sb2.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mDevice.host);
                str = sb2.toString();
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("*** getCFGResult url: ");
            sb3.append(str);
            Log.d(str2, sb3.toString());
            String cGFResultFromDevice = NetworkUtil.getCGFResultFromDevice(str);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("*** Getting cfg result from ST:\n");
            sb4.append(cGFResultFromDevice);
            Log.i(str2, sb4.toString());
            CFG_Result_Enum cfgEnumForResponse = NetworkUtil.cfgEnumForResponse(cGFResultFromDevice);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("*** result_Enum: ");
            sb5.append(cfgEnumForResponse);
            Log.i(str2, sb5.toString());
            String errorMsgForCFGResult = NetworkUtil.getErrorMsgForCFGResult(cfgEnumForResponse);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("*** result: ");
            sb6.append(errorMsgForCFGResult);
            Log.i(str2, sb6.toString());
            return errorMsgForCFGResult;
        }
    }

    /* renamed from: com.ti.wifi.ConfigureWiFiActivityStageThreeAPModeProvisioning$wifiStageThreeStates */
    public enum wifiStageThreeStates {
        START,
        WRONG_WIFI,
        INTERROGATION,
        FW_UPGRADE_NEEDED,
        CONFIGURE,
        CONFIG_WRITTEN,
        KICK_INTO_PROVISIONING,
        PROVISIONING_ASSOCIATION,
        PROVISION,
        READ_FEEDBACK,
        CANCELED,
        OK,
        READ_FEEDBACK_FAILED,
        PROV_FINISHED,
        SCAN_FOR_ST_DEVICE,
        CONNECT_TO_CONFIG_SSID,
        RECONNECT_TO_ST_DEVICE_AFTER_REBOOT
    }

    public ConfigureWiFiActivityStageThreeAPModeProvisioning() {
        String str = "";
        this.mDevice = new Device(str, str);
        this.deviceFound = false;
        this.ssidToSend = str;
        this.startConVerHasBeenCalled = false;
        this.ssidToSendConfig = null;
        this.getCFGResultViaDeviceAsAPTimeoutTimer = null;
        this.didInter = false;
        this.mPingCallback = new PingCallback() {
            public void pingCompleted() {
                Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, "PingOrUDPBcastCallback - Completed");
            }

            public void pingDeviceFetched(JSONObject jSONObject) {
                String str = "host";
                String str2 = "name";
                String str3 = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
                Log.i(str3, "StageThreeAPMode pingDeviceFetched called");
                StringBuilder sb = new StringBuilder();
                sb.append("1pingDeviceFetched/deviceFound: ");
                sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound);
                Log.i(str3, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("pingDeviceFetched/state: ");
                sb2.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                Log.i(str3, sb2.toString());
                if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound || ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state != wifiStageThreeStates.SCAN_FOR_ST_DEVICE) {
                    Log.i(str3, "Did not enter pingDeviceFetched - did not meet if conditions");
                    return;
                }
                Log.i(str3, "Running pingDeviceFetched - inside if");
                StringBuilder sb3 = new StringBuilder();
                sb3.append("SensorTag Device was found via PING or UDPBcast: ");
                sb3.append(jSONObject);
                Log.i(str3, sb3.toString());
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Ping/UDP found a new device ! ");
                sb4.append(jSONObject.toString());
                Log.d(str3, sb4.toString());
                try {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Ping/UDP device name : ");
                    sb5.append(jSONObject.getString(str2));
                    Log.d(str3, sb5.toString());
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Pin/UDP device host: ");
                    sb6.append(jSONObject.getString(str));
                    Log.d(str3, sb6.toString());
                    String string = jSONObject.getString(str);
                    String string2 = jSONObject.getString(str2);
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("PING/UDP / FOUND IP: ");
                    sb7.append(string);
                    Log.i(str3, sb7.toString());
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("PING/UDP / FOUND NAME: ");
                    sb8.append(string2);
                    Log.i(str3, sb8.toString());
                    if (string2 != null) {
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("NAME - pingDeviceFetched / chosenSTDeviceSSID: ");
                        sb9.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.chosenSTDeviceSSID);
                        Log.i(str3, sb9.toString());
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("NAME - pingDeviceFetched / found device's name: ");
                        sb10.append(string2);
                        Log.i(str3, sb10.toString());
                        if (string2.equals(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.chosenSTDeviceSSID)) {
                            Log.i(str3, "NAME - pingDeviceFetched / found ST device's name and chosenSTDeviceSSID are EQUAL - found device IS the requested device - move to configuration verification");
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound = true;
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append("2pingDeviceFetched/deviceFound: ");
                            sb11.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound);
                            Log.i(str3, sb11.toString());
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.READ_FEEDBACK;
                            StringBuilder sb12 = new StringBuilder();
                            sb12.append("pingDeviceFetched / state: ");
                            sb12.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                            Log.i(str3, sb12.toString());
                            Log.i(str3, "Correct SensorTag device has been found via Ping/UDP - Stop discovery");
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.stopPing();
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mDnsHelper.stopDiscovery();
                            StringBuilder sb13 = new StringBuilder();
                            sb13.append("startConVerHasBeenCalled: ");
                            sb13.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.startConVerHasBeenCalled);
                            Log.i(str3, sb13.toString());
                            if (!ConfigureWiFiActivityStageThreeAPModeProvisioning.this.startConVerHasBeenCalled) {
                                Log.i(str3, "Starting configuration verification process - Call startConfigVerification");
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.startConfigVerification("", string);
                                return;
                            }
                            return;
                        }
                        Log.i(str3, "NAME - pingDeviceFetched / found ST device's name and chosenSTDeviceSSID are NOT equal - found device is not the requested device - do not move to configuration verification");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void pingFailed(String str) {
                Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, "PingCallback - pingFailed");
            }
        };
    }

    public void onCreate(Bundle bundle) {
        this.mThis = this;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_configure_wifi_stage_3);
        this.chosenSTDeviceSSID = getIntent().getStringExtra("com.ti.ble.sensortag.ConfigureWiFiStageThree.extra.SSID");
        StringBuilder sb = new StringBuilder();
        sb.append(this.chosenSTDeviceSSID);
        sb.append(" (Prov.)");
        setTitle(sb.toString());
        this.ipAddr = (TextView) findViewById(R.id.acws_three_ip_addr);
        this.fwVersion = (TextView) findViewById(R.id.acws_three_firmware);
        this.status = (TextView) findViewById(R.id.acws_three_status);
        this.status.setTypeface(null, 1);
        this.macAddr = (TextView) findViewById(R.id.acws_three_mac_addr);
        this.loader = (ProgressBar) findViewById(R.id.acw_loader);
        this.loader.setVisibility(0);
        this.loader.setIndeterminate(true);
        String str = "192.168.1.1";
        this.ipAddr.setText(str);
        this.ipAdressForExtra = str;
        this.sensorTagWifi = new WifiConfiguration();
        WifiConfiguration wifiConfiguration = this.sensorTagWifi;
        StringBuilder sb2 = new StringBuilder();
        String str2 = "\"";
        sb2.append(str2);
        sb2.append(this.chosenSTDeviceSSID);
        sb2.append(str2);
        wifiConfiguration.SSID = sb2.toString();
        this.sensorTagWifi.allowedKeyManagement.set(0);
        WifiConfiguration wifiConfiguration2 = this.sensorTagWifi;
        wifiConfiguration2.preSharedKey = "";
        this.desiredWifi = wifiConfiguration2;
        this.wController = new wlanController(this);
        this.wController.setDesiredWlanConfig(this.desiredWifi);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("set desired: ");
        sb3.append(this.desiredWifi);
        Log.e("wlanController", sb3.toString());
        this.wController.startWlanController();
        this.filt = new IntentFilter(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE);
        this.filt.addAction(wifiDeviceActivity.WIFI_DEVICE_REQUEST_DISCONNECTED);
        this.filt.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.filt.addAction("com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_OK");
        this.filt.addAction(ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_CANCELED);
        this.filt.addAction(UdpBcastServer.UDP_BCAST_SERVER_FOUND_DEVICE_ACTION);
        this.filt.addAction("com.ti.ble.sensortag.ConfigureWiFiStageThree.PROVISIONING_FAILED_TIMEOUT_ACTION");
        this.filt.addAction(wlanController.WE_ARE_ON_DESIRED_WLAN_ACTION);
        this.filt.addAction(SCAN_FINISHED_BROADCAST_ACTION);
        this.receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Intent intent2 = intent;
                boolean equals = intent.getAction().equals(wlanController.WE_ARE_ON_DESIRED_WLAN_ACTION);
                String str = "";
                String str2 = "192.168.1.1";
                String str3 = "\"";
                String str4 = "State: ";
                String str5 = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
                if (equals) {
                    Log.i(str5, "WE_ARE_ON_DESIRED_WLAN_ACTION");
                    StringBuilder sb = new StringBuilder();
                    sb.append("We are on wlan : ");
                    sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.SSID);
                    Log.d(str5, sb.toString());
                    if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.START) {
                        Log.i(str5, "Interr - WE_ARE_ON_DESIRED_WLAN_ACTION/START");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Interr - didInter: ");
                        sb2.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.didInter);
                        Log.i(str5, sb2.toString());
                        if (!ConfigureWiFiActivityStageThreeAPModeProvisioning.this.didInter) {
                            Log.i(str5, "Interr - did not already interrogate device - interrogate now");
                            Log.i(str5, "Connected to SensorTag device - interrogating device");
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.interrogateDevice(str2);
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.INTERROGATION;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str4);
                            sb3.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                            Log.i(str5, sb3.toString());
                        }
                    } else if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.KICK_INTO_PROVISIONING) {
                        Log.i(str5, "WE_ARE_ON_DESIRED_WLAN_ACTION/KICK_INTO_PROVISIONING");
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.status.setText(R.string.provisioning_ready);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.PROVISIONING_ASSOCIATION;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str4);
                        sb4.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                        Log.i(str5, sb4.toString());
                        Log.i(str5, "Showing provisioning dialog");
                        ConfigureWiFiDialogFragment.newInstance(str2, str).show(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.getFragmentManager(), "WifiConfig");
                    } else if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state != wifiStageThreeStates.PROVISION) {
                        if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.CONNECT_TO_CONFIG_SSID) {
                            Log.i(str5, "WE_ARE_ON_DESIRED_WLAN_ACTION/CONNECT_TO_CONFIG_SSID");
                            Log.i(str5, "Check if we are connected to config SSID");
                            String stringExtra = intent2.getStringExtra("wlan");
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(str3);
                            sb5.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSend);
                            sb5.append(str3);
                            if (stringExtra.equals(sb5.toString())) {
                                Log.i(str5, "We are connected to config SSID - Start scanning for SensorTag device as STA in wlan");
                                if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.connectToConfigSSIDTimer != null) {
                                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.connectToConfigSSIDTimer.cancel();
                                    Log.i(str5, "Cancel connection to config SSID timeout - we are connected");
                                }
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.SCAN_FOR_ST_DEVICE;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("BR - WE_ARE_ON_DESIRED_WLAN_ACTION / state: ");
                                sb6.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                                Log.i(str5, sb6.toString());
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.restartUdp();
                                Log.i(str5, "*** RESTART UDP SERVER ***");
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.scanForSensorTagDevices();
                                Log.i(str5, "*** CALL SCAN FOR SENSORTAG DEVICES ***");
                            }
                        } else if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.READ_FEEDBACK_FAILED) {
                            Log.i(str5, "WE_ARE_ON_DESIRED_WLAN_ACTION/READ_FEEDBACK_FAILED");
                            if (intent2.getStringExtra("wlan").equals(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.sensorTagWifi.SSID)) {
                                Log.i(str5, "we are connected to ST device");
                                new Timer().schedule(new TimerTask() {
                                    public void run() {
                                        Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, "Retrieve cfg verification from ST device as AP");
                                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getCFG(Boolean.valueOf(false));
                                    }
                                }, DNSConstants.CLOSE_TIMEOUT);
                            }
                        }
                    }
                }
                boolean equals2 = intent.getAction().equals(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE);
                String str6 = wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_URL;
                if (equals2) {
                    Log.i(str5, "WIFI_DEVICE_REQUEST_RESPONSE");
                    String stringExtra2 = intent2.getStringExtra(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_DATA);
                    String stringExtra3 = intent2.getStringExtra(str6);
                    intent2.getStringExtra(wifiDeviceActivity.WIFI_DEVICE_REQUEST_IP_ADDR);
                    if (stringExtra3.equals(wifiDeviceActivity.WIFI_DEVICE_CONFIG_URL)) {
                        Log.i(str5, "WIFI_DEVICE_REQUEST_RESPONSE/WIFI_DEVICE_CONFIG_URL");
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("Received interrogation data : ");
                        sb7.append(stringExtra2);
                        Log.d(str5, sb7.toString());
                        if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.INTERROGATION) {
                            Log.i(str5, "WIFI_DEVICE_REQUEST_RESPONSE/WIFI_DEVICE_CONFIG_URL/INTERROGATION");
                            Log.i(str5, "Interr - got interrogation results");
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.loader.setVisibility(4);
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.fwVersion.setText(wifiDeviceActivity.getFirmwareString(stringExtra2));
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.macAddr.setText(wifiDeviceActivity.getMacString(stringExtra2));
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.status.setText(R.string.Idle);
                            Log.i(str5, "Checking firmware version");
                            if (wifiDeviceActivity.isLatestFirmware((String) ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.fwVersion.getText())) {
                                Log.i(str5, "Firmware up-to-date - Show provisioning dialog");
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.provDialogTimer = new Timer();
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.provDialogTimer.schedule(new TimerTask() {
                                    public void run() {
                                        ConfigureWiFiDialogFragment newInstance = ConfigureWiFiDialogFragment.newInstance("192.168.1.1", "");
                                        ConfigureWiFiActivityStageThreeAPModeProvisioning access$400 = ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis;
                                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.CONFIGURE;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("State: ");
                                        sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                                        Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, sb.toString());
                                        try {
                                            newInstance.show(access$400.getFragmentManager(), "WifiConfig");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 2000);
                            } else {
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.fwUTDTimer = new Timer();
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.fwUTDTimer.schedule(new TimerTask() {
                                    public void run() {
                                        Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, "Firmware is not up-to-date - Upgrade firmware");
                                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Builder builder = new Builder(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis);
                                                builder.setTitle((CharSequence) ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getResources().getString(R.string.out_of_date_firmware_title));
                                                builder.setMessage((CharSequence) "Your SensorTag device's firmware is out-of-date. To upgrade your SensorTag device to the latest firmware click \"UPGRADE\".\n\nTo continue the provisioning process without upgrading the firmware, click \"PROVISION\".");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton((CharSequence) "UPGRADE", (OnClickListener) new OnClickListener() {
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.FW_UPGRADE_NEEDED;
                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append("State: ");
                                                        sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                                                        String sb2 = sb.toString();
                                                        String str = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
                                                        Log.i(str, sb2);
                                                        Log.i(str, "Move to wifiOTAActivity");
                                                        Intent intent = new Intent(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis, wifiOTAActivity.class);
                                                        intent.putExtra(DeviceActivity.EXTRA_DEVICE, "192.168.1.1");
                                                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.startActivity(intent);
                                                    }
                                                });
                                                builder.setNegativeButton((CharSequence) "PROVISION", (OnClickListener) new OnClickListener() {
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        ConfigureWiFiDialogFragment newInstance = ConfigureWiFiDialogFragment.newInstance("192.168.1.1", "");
                                                        ConfigureWiFiActivityStageThreeAPModeProvisioning access$400 = ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis;
                                                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.CONFIGURE;
                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append("State: ");
                                                        sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                                                        Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, sb.toString());
                                                        newInstance.show(access$400.getFragmentManager(), "WifiConfig");
                                                    }
                                                });
                                                try {
                                                    builder.create().show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }, 2000);
                            }
                        }
                    }
                    if (stringExtra3.equals(wifiDeviceActivity.WIFI_DEVICE_GET_CFG_RESULT)) {
                        Log.i(str5, "WIFI_DEVICE_REQUEST_RESPONSE/WIFI_DEVICE_GET_CFG_RESULT");
                        String str7 = "OK";
                        if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.READ_FEEDBACK) {
                            Log.i(str5, "WIFI_DEVICE_REQUEST_RESPONSE/WIFI_DEVICE_GET_CFG_RESULT/READ_FEEDBACK");
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("Configuration response was : ");
                            sb8.append(stringExtra2);
                            Log.d(str5, sb8.toString());
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.status.setText(String.format(Locale.ENGLISH, "%1$s %2$s", new Object[]{Integer.valueOf(R.string.provisioning_finished), stringExtra2}));
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.OK;
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append(str4);
                            sb9.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                            Log.i(str5, sb9.toString());
                            Builder builder = new Builder(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis);
                            builder.setPositiveButton((CharSequence) str7, (OnClickListener) new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            Intent intent = new Intent(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis, TopLevel.class);
                                            intent.setFlags(67108864);
                                            intent.putExtra(TopLevel.CONNECT_TO_CONFIG_EXTRA, ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSendConfig);
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("ssidToSendConfig: ");
                                            sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSendConfig);
                                            sb.append(" extra to TL");
                                            Log.d("RECON - ", sb.toString());
                                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.startActivity(intent);
                                        }
                                    }).run();
                                }
                            });
                            builder.setTitle((CharSequence) "Provisioning finished !");
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append("Provisioning finished with result :\n");
                            sb10.append(wifiDeviceActivity.getStringFromConfigResult(stringExtra2));
                            builder.setMessage((CharSequence) sb10.toString());
                            builder.setCancelable(false);
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.loader.setVisibility(8);
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.PROV_FINISHED;
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.finishAPProvProcess(Boolean.valueOf(false));
                            builder.create().show();
                        } else if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.READ_FEEDBACK_FAILED) {
                            Log.i(str5, "WIFI_DEVICE_REQUEST_RESPONSE/WIFI_DEVICE_GET_CFG_RESULT/READ_FEEDBACK_FAILED");
                            Builder builder2 = new Builder(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis);
                            builder2.setPositiveButton((CharSequence) str7, (OnClickListener) new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            Intent intent = new Intent(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis, TopLevel.class);
                                            intent.putExtra(TopLevel.CONNECT_TO_CONFIG_EXTRA, ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSendConfig);
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("ssidToSendConfig: ");
                                            sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSendConfig);
                                            sb.append(" extra to TL");
                                            Log.d("RECON - ", sb.toString());
                                            intent.setFlags(67108864);
                                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.startActivity(intent);
                                        }
                                    }).run();
                                }
                            });
                            builder2.setTitle((CharSequence) "Provisioning failed !");
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append("Provisioning failed with result :\n");
                            sb11.append(wifiDeviceActivity.getStringFromConfigResult(stringExtra2));
                            builder2.setMessage((CharSequence) sb11.toString());
                            builder2.setCancelable(false);
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.loader.setVisibility(8);
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.PROV_FINISHED;
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.finishAPProvProcess(Boolean.valueOf(true));
                            builder2.create().show();
                        }
                    }
                }
                if (intent.getAction().equals(wifiDeviceActivity.WIFI_DEVICE_REQUEST_DISCONNECTED)) {
                    Log.i(str5, "WIFI_DEVICE_REQUEST_DISCONNECTED");
                    if (intent2.getStringExtra(str6).equals(wifiDeviceActivity.WIFI_DEVICE_ADD_PROFILES_URL)) {
                        Log.i(str5, "WIFI_DEVICE_REQUEST_DISCONNECTED/WIFI_DEVICE_ADD_PROFILES_URL");
                        if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.PROVISION) {
                            Log.i(str5, "WIFI_DEVICE_REQUEST_DISCONNECTED/WIFI_DEVICE_ADD_PROFILES_URL/PROVISION");
                            Log.i(str5, "Profile has been added to SensorTag device");
                            if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.writeNewSSIDConfigTimer != null) {
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.writeNewSSIDConfigTimer.cancel();
                                Log.i(str5, "cancel writeNewSSIDConfigTimer - profile has been written to SensorTag device");
                            }
                            wifiDeviceActivity.writeProvEnd(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis, str2, TopLevel.Sensor_Tag);
                            Log.i(str5, "writeProvEnd has been called - restart device");
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.CONNECT_TO_CONFIG_SSID;
                            StringBuilder sb12 = new StringBuilder();
                            sb12.append(str4);
                            sb12.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                            Log.i(str5, sb12.toString());
                            if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.wController == null) {
                                ConfigureWiFiActivityStageThreeAPModeProvisioning configureWiFiActivityStageThreeAPModeProvisioning = ConfigureWiFiActivityStageThreeAPModeProvisioning.this;
                                configureWiFiActivityStageThreeAPModeProvisioning.wController = new wlanController(configureWiFiActivityStageThreeAPModeProvisioning.mThis);
                                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.wController.startWlanController();
                            }
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.wController.setDesiredWlanConfig(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi);
                            StringBuilder sb13 = new StringBuilder();
                            sb13.append("set desired: ");
                            sb13.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi);
                            Log.i("wlanController", sb13.toString());
                            StringBuilder sb14 = new StringBuilder();
                            sb14.append("Connect to config SSID: ");
                            sb14.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.toString());
                            Log.i(str5, sb14.toString());
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.connectToConfigSSIDTimer = new Timer();
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.connectToConfigSSIDTimer.schedule(new TimerTask() {
                                public void run() {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Could not Connect to config SSID: ");
                                    sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.toString());
                                    String sb2 = sb.toString();
                                    String str = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
                                    Log.i(str, sb2);
                                    Log.i(str, "SensorTag device will not be discovered as STA in wlan");
                                    Log.i(str, "Moving to retrieving cfg verification via SensorTag device as AP");
                                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getCFGResultViaDeviceAsAP();
                                }
                            }, 80000);
                        }
                    }
                }
                if (intent.getAction().equals(ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_CANCELED)) {
                    Log.i(str5, "CONFIG_WIFI_DIALOG_CANCELED");
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.state = wifiStageThreeStates.CANCELED;
                    StringBuilder sb15 = new StringBuilder();
                    sb15.append(str4);
                    sb15.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                    Log.i(str5, sb15.toString());
                }
                if (intent.getAction().equals("com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_OK")) {
                    Log.i(str5, "CONFIG_WIFI_DIALOG_OK");
                    Log.i(str5, "Provisioning dialog \"Save\" button clicked -\n**** Starting AP provisioning process ****");
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.loader.setVisibility(0);
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.loader.setIndeterminate(true);
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.PROVISION;
                    StringBuilder sb16 = new StringBuilder();
                    sb16.append(str4);
                    sb16.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                    Log.i(str5, sb16.toString());
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.status.setText(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getResources().getString(R.string.provisioning));
                    StringBuilder sb17 = new StringBuilder();
                    sb17.append("Got SSID:");
                    String str8 = "com.ti.sensortag.ConfigureWiFiDialogFragment.SSID";
                    sb17.append(intent2.getStringExtra(str8));
                    Log.d(str5, sb17.toString());
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSend = intent2.getStringExtra(str8);
                    StringBuilder sb18 = new StringBuilder();
                    sb18.append("Got encryption:");
                    String str9 = "com.ti.sensortag.ConfigureWiFiDialogFragment.ENCRYPT";
                    sb18.append(intent2.getIntExtra(str9, 0));
                    Log.d(str5, sb18.toString());
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.encryption = intent2.getIntExtra(str9, 0);
                    StringBuilder sb19 = new StringBuilder();
                    sb19.append("Got password:");
                    String str10 = "com.ti.sensortag.ConfigureWiFiDialogFragment.PWD";
                    sb19.append(intent2.getStringExtra(str10));
                    Log.d(str5, sb19.toString());
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.pass = intent2.getStringExtra(str10);
                    Log.d(str5, "Create WifiConfiguration to connect to after adding a profile to ST device, and search for ST device as STA in");
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi = new WifiConfiguration();
                    WifiConfiguration access$000 = ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi;
                    StringBuilder sb20 = new StringBuilder();
                    sb20.append(str3);
                    sb20.append(intent2.getStringExtra(str8));
                    sb20.append(str3);
                    access$000.SSID = sb20.toString();
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.status = 2;
                    ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.hiddenSSID = false;
                    StringBuilder sb21 = new StringBuilder();
                    sb21.append(str3);
                    sb21.append(intent2.getStringExtra(str10));
                    sb21.append(str3);
                    String sb22 = sb21.toString();
                    int intExtra = intent2.getIntExtra(str9, 0);
                    if (intExtra == 0) {
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedKeyManagement.set(0);
                    } else if (intExtra == 1) {
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedKeyManagement.set(1);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedAuthAlgorithms.set(1);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.preSharedKey = sb22;
                    } else if (intExtra == 2) {
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedKeyManagement.set(1);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedProtocols.set(0);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.preSharedKey = sb22;
                    } else if (intExtra == 3) {
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedKeyManagement.set(1);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedProtocols.set(1);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.preSharedKey = sb22;
                    } else if (sb22 == null || sb22.contains(str)) {
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedKeyManagement.set(0);
                    } else {
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedKeyManagement.set(1);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.allowedProtocols.set(0);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.preSharedKey = sb22;
                    }
                    ConfigureWiFiActivityStageThreeAPModeProvisioning configureWiFiActivityStageThreeAPModeProvisioning2 = ConfigureWiFiActivityStageThreeAPModeProvisioning.this;
                    configureWiFiActivityStageThreeAPModeProvisioning2.ssidToSendConfig = configureWiFiActivityStageThreeAPModeProvisioning2.desiredWifi;
                    StringBuilder sb23 = new StringBuilder();
                    sb23.append("WiFi configuration sent to SensorTag device:\n");
                    sb23.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.desiredWifi.toString());
                    Log.i(str5, sb23.toString());
                    wifiDeviceActivity.writeNewSSIDConfig(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis, intent2.getStringExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.IPADDR"), intent2.getStringExtra(str8), intent2.getIntExtra(str9, 0), intent2.getStringExtra(str10));
                    Log.i(str5, "writeNewSSIDConfig has been called - add profile");
                }
                if (intent.getAction().equals("com.ti.ble.sensortag.ConfigureWiFiStageThree.PROVISIONING_FAILED_TIMEOUT_ACTION")) {
                    Log.i(str5, "PROVISIONING_FAILED_TIMEOUT_ACTION");
                }
                if (intent.getAction().equals(ConfigureWiFiActivityStageThreeAPModeProvisioning.SCAN_FINISHED_BROADCAST_ACTION)) {
                    Log.i(str5, "SCAN_FINISHED_BROADCAST_ACTION");
                    StringBuilder sb24 = new StringBuilder();
                    sb24.append("SCAN_FINISHED_BROADCAST_ACTION/deviceFound: ");
                    sb24.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound);
                    Log.i(str5, sb24.toString());
                    StringBuilder sb25 = new StringBuilder();
                    sb25.append("SCAN_FINISHED_BROADCAST_ACTION/state: ");
                    sb25.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                    Log.i(str5, sb25.toString());
                    if (!ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound && ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state == wifiStageThreeStates.SCAN_FOR_ST_DEVICE) {
                        Log.i(str5, "Scan finished ST device was not found - Move to retrieving cfg verification via ST device as AP");
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getCFGResultViaDeviceAsAP();
                    }
                }
            }
        };
        registerReceiver(this.receiver, this.filt);
        String str3 = TAG;
        Log.i(str3, "onCreate / receiver registered!!");
        try {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.killMDNSScanWhenInBackground = false;
        this.isScanning = false;
        this.cgfTryNumber = 0;
        boolean deviceOnCorrectWLAN = wifiDeviceActivity.deviceOnCorrectWLAN(this.mThis, this.chosenSTDeviceSSID);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Interr - we are on correct wlan: ");
        sb4.append(deviceOnCorrectWLAN);
        Log.i(str3, sb4.toString());
        if (deviceOnCorrectWLAN) {
            Log.i(str3, "Interr - Connected to SensorTag device - interrogating device");
            interrogateDevice(str);
            this.state = wifiStageThreeStates.INTERROGATION;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Interr - State: ");
            sb5.append(this.state);
            Log.i(str3, sb5.toString());
            this.didInter = true;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Interr - didInter: ");
            sb6.append(this.didInter);
            Log.i(str3, sb6.toString());
        }
    }

    public void onResume() {
        String str = TAG;
        super.onResume();
        try {
            registerReceiver(this.receiver, this.filt);
            Log.i(str, "onResume / receiver registered!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mDnsHelper = new MDnsHelper();
        initMDNS();
        StringBuilder sb = new StringBuilder();
        sb.append("onResume / state: ");
        sb.append(this.state);
        Log.i(str, sb.toString());
        this.killMDNSScanWhenInBackground = false;
        if (this.wController == null) {
            this.wController = new wlanController(this.mThis);
            this.wController.setDesiredWlanConfig(this.desiredWifi);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("set desired: ");
            sb2.append(this.desiredWifi);
            Log.i("wlanController", sb2.toString());
            this.wController.startWlanController();
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ConfigureWifiActivityStageTwo.class);
        intent.setFlags(67108864);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void interrogateDevice(final String str) {
        new Thread(new Runnable() {
            public void run() {
                String str = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Interrogating device @ ");
                    sb.append(str);
                    Log.d(str, sb.toString());
                    wifiDeviceActivity.getDeviceConfig(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis, str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void restartUdp() {
        UdpBcastServer udpBcastServer2 = this.udpBcastServer;
        if (udpBcastServer2 != null) {
            udpBcastServer2.stopUDPBcastServer();
            this.udpBcastServerThread.interrupt();
            this.udpBcastServer = null;
        }
        this.udpBcastServer = new UdpBcastServer(this.mThis, this.mPingCallback);
        this.udpBcastServerThread = new Thread(this.udpBcastServer.udpBcastServerRunnable);
        this.udpBcastServerThread.start();
    }

    private void initMDNS() {
        this.mDNSCallback = new MDnsCallbackInterface() {
            public void onDeviceResolved(JSONObject jSONObject) {
                String str = "host";
                String str2 = "name";
                StringBuilder sb = new StringBuilder();
                sb.append("1onDeviceResolved/deviceFound: ");
                sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound);
                String sb2 = sb.toString();
                String str3 = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
                Log.i(str3, sb2);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onDeviceResolved/state: ");
                sb3.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                Log.i(str3, sb3.toString());
                if (ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound || ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state != wifiStageThreeStates.SCAN_FOR_ST_DEVICE) {
                    Log.i(str3, "Did not enter onDeviceResolved - did not meet if conditions");
                    return;
                }
                Log.i(str3, "Running onDeviceResolved - inside if");
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Found new device via mDNS: ");
                sb4.append(jSONObject);
                Log.d(str3, sb4.toString());
                StringBuilder sb5 = new StringBuilder();
                sb5.append("mDNS found a new device ! ");
                sb5.append(jSONObject.toString());
                Log.d(str3, sb5.toString());
                try {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("mDNS device name : ");
                    sb6.append(jSONObject.getString(str2));
                    Log.d(str3, sb6.toString());
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("mDNS device host : ");
                    sb7.append(jSONObject.getString(str));
                    Log.d(str3, sb7.toString());
                    String string = jSONObject.getString(str);
                    String string2 = jSONObject.getString(str2);
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("mDNS / FOUND IP: ");
                    sb8.append(string);
                    Log.i(str3, sb8.toString());
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("mDNS / FOUND NAME: ");
                    sb9.append(string2);
                    Log.i(str3, sb9.toString());
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("NAME - onDeviceResolved / chosenSTDeviceSSID: ");
                    sb10.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.chosenSTDeviceSSID);
                    Log.i(str3, sb10.toString());
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append("NAME - onDeviceResolved / found ST device's name: ");
                    sb11.append(string2);
                    Log.i(str3, sb11.toString());
                    if (string2.equals(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.chosenSTDeviceSSID)) {
                        Log.i(str3, "NAME - onDeviceResolved / found ST device's name and chosenSTDeviceSSID are EQUAL - found device IS the requested device - move to configuration verification");
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound = true;
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("2onDeviceResolved/deviceFound: ");
                        sb12.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.deviceFound);
                        Log.i(str3, sb12.toString());
                        Log.i(str3, "Correct SensorTag device has been found via mDNS discovery - Stop discovery");
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.READ_FEEDBACK;
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append("onDeviceResolved / state: ");
                        sb13.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state);
                        Log.i(str3, sb13.toString());
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.stopPing();
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mDnsHelper.stopDiscovery();
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append("startConVerHasBeenCalled: ");
                        sb14.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.startConVerHasBeenCalled);
                        Log.i(str3, sb14.toString());
                        if (!ConfigureWiFiActivityStageThreeAPModeProvisioning.this.startConVerHasBeenCalled) {
                            Log.i(str3, "Starting configuration verification process - Call startConfigVerification");
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.startConfigVerification(string2, string);
                            return;
                        }
                        return;
                    }
                    Log.i(str3, "NAME - onDeviceResolved / found ST device's name and chosenSTDeviceSSID are NOT equal - found device is not the requested device - do not move to configuration verification");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        this.mDnsHelper.init(this, this.mDNSCallback);
    }

    public void scanForSensorTagDevices() {
        Intent intent;
        String str = "Stopping mDNS discovery/scanForSensorTagDevices";
        StringBuilder sb = new StringBuilder();
        sb.append("scanForSensorTagDevices/deviceFound: ");
        sb.append(this.deviceFound);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.i(str2, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("scanForSensorTagDevices/state: ");
        sb3.append(this.state);
        Log.i(str2, sb3.toString());
        if (!this.deviceFound && this.state == wifiStageThreeStates.SCAN_FOR_ST_DEVICE) {
            Log.i(str2, "Running scanForSensorTagDevices - inside if");
            if (this.killMDNSScanWhenInBackground) {
                return;
            }
            if (!this.isScanning) {
                this.isScanning = true;
                startPing();
                Log.i(str2, "*** START PING BCAST ***");
                try {
                    Log.i(str2, "Starting mDNS discovery/scanForSensorTagDevices");
                    if (this.firstTime) {
                        this.firstTime = false;
                        this.mDnsHelper.startDiscovery();
                    } else {
                        this.mDnsHelper.restartDiscovery();
                    }
                    Log.i(str2, "*** START/RESTART mDNS DISCOVERY ***");
                    Thread.sleep(15000);
                    Log.i(str2, str);
                    intent = new Intent();
                } catch (InterruptedException e) {
                    Log.e(str2, "Failed to sleep during mDNS discovery/scanForSensorTagDevices");
                    e.printStackTrace();
                    Log.i(str2, str);
                    intent = new Intent();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Log.i(str2, str);
                    intent = new Intent();
                } catch (Throwable th) {
                    Log.i(str2, str);
                    Intent intent2 = new Intent();
                    intent2.setAction(SCAN_FINISHED_BROADCAST_ACTION);
                    sendBroadcast(intent2);
                    this.isScanning = false;
                    scanForSensorTagDevices();
                    throw th;
                }
                intent.setAction(SCAN_FINISHED_BROADCAST_ACTION);
                sendBroadcast(intent);
                this.isScanning = false;
                scanForSensorTagDevices();
                return;
            }
            Log.i(str2, "An mDNS discovery is already in progress/scanForSensorTagDevices");
        }
    }

    private void startPing() {
        if (initPing()) {
            Thread thread = this.pingThread;
            if (thread != null && thread.isAlive()) {
                this.pingThread.interrupt();
            }
            try {
                this.pingThread = new Thread(this.mPing.pingRunnable);
                this.pingThread.start();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean initPing() {
        String str;
        stopPing();
        if (NetworkUtil.getConnectionStatus(this) == NetworkUtil.WIFI) {
            try {
                NetInfo netInfo = new NetInfo(this);
                netInfo.getWifiInfo();
                while (true) {
                    boolean equalsIgnoreCase = netInfo.gatewayIp.equalsIgnoreCase("0.0.0.0");
                    str = TAG;
                    if (!equalsIgnoreCase) {
                        break;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("initPing/ in gateway ip while - network ip: ");
                    sb.append(netInfo.gatewayIp);
                    Log.i(str, sb.toString());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    netInfo.getWifiInfo();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("initPing/ after while network ip: ");
                sb2.append(netInfo.gatewayIp);
                Log.i(str, sb2.toString());
                String[] split = netInfo.gatewayIp.split("\\.");
                String str2 = "";
                String str3 = str2;
                for (int i = 0; i < 3; i++) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str3);
                    sb3.append(split[i]);
                    sb3.append(".");
                    str3 = sb3.toString();
                }
                this.mPing = new Ping(this.mThis, this.mPingCallback, netInfo.gatewayIp);
                Ping ping = this.mPing;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str3);
                sb4.append("255");
                ping.ipToPing = sb4.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append("initPing/ Will ping ip: ");
                sb5.append(this.mPing.ipToPing);
                Log.i(str, sb5.toString());
                this.mPing.receivedIP = str2;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("initPing/ receivedIP: ");
                sb6.append(this.mPing.receivedIP);
                Log.i(str, sb6.toString());
                return true;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void stopPing() {
        Ping ping = this.mPing;
        if (ping != null && ping.working) {
            this.mPing.stopPing();
            Log.i(TAG, "Ping - stopped");
        }
        this.mPing = null;
    }

    public void stopScanningForSensorTagDevices() {
        Intent intent;
        String str = "Stopping mDNS scan notification/stopScanningForSensorTagDevices";
        String str2 = TAG;
        stopPing();
        try {
            Log.i(str2, "Stopping mDNS discovery/stopScanningForSensorTagDevices");
            this.firstTime = true;
            if (this.mDnsHelper != null) {
                this.mDnsHelper.stopDiscovery();
            }
            Thread.sleep(DNSConstants.SERVICE_INFO_TIMEOUT);
            Log.i(str2, str);
            intent = new Intent();
        } catch (InterruptedException e) {
            Log.e(str2, "Failed to sleep during mDNS discovery stop/stopScanningForSensorTagDevices");
            e.printStackTrace();
            Log.i(str2, str);
            intent = new Intent();
        } catch (Throwable th) {
            Log.i(str2, str);
            Intent intent2 = new Intent();
            intent2.setAction(SCAN_FINISHED_BROADCAST_ACTION);
            sendBroadcast(intent2);
            this.isScanning = false;
            scanForDevices();
            throw th;
        }
        intent.setAction(SCAN_FINISHED_BROADCAST_ACTION);
        sendBroadcast(intent);
        this.isScanning = false;
        scanForDevices();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        stopPing();
        Timer timer = this.provDialogTimer;
        if (timer != null) {
            timer.cancel();
            this.provDialogTimer = null;
        }
        Timer timer2 = this.fwUTDTimer;
        if (timer2 != null) {
            timer2.cancel();
            this.fwUTDTimer = null;
        }
        MDnsHelper mDnsHelper2 = this.mDnsHelper;
        if (mDnsHelper2 != null) {
            mDnsHelper2.stopDiscovery();
        }
        wlanController wlancontroller = this.wController;
        if (wlancontroller != null) {
            wlancontroller.stopWlanController();
            Log.d("RECON - ", "StageThreeAPProv/onStop stopped wlanCon");
            this.wController = null;
        }
        this.killMDNSScanWhenInBackground = true;
        unregisterReceiver(this.receiver);
    }

    /* access modifiers changed from: private */
    public void getCFG(Boolean bool) {
        String str = TAG;
        Log.i(str, "getCFG called");
        if (bool.booleanValue()) {
            StringBuilder sb = new StringBuilder();
            sb.append("*AP* Number of attempts previously made to retrieve cfg verification from ST device as STA: ");
            sb.append(this.cgfTryNumber);
            Log.i(str, sb.toString());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("*AP* Number of attempts previously made to retrieve cfg verification from ST device as AP: ");
            sb2.append(this.cgfTryNumber);
            Log.i(str, sb2.toString());
        }
        int i = this.cgfTryNumber;
        if (i < 5) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("*AP* Executing cfg verification attempt no.: ");
            sb3.append(i + 1);
            Log.i(str, sb3.toString());
            if (VERSION.SDK_INT >= 11) {
                new GetCFGResult().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Boolean[]{bool});
                return;
            }
            new GetCFGResult().execute(new Boolean[]{bool});
        } else if (bool.booleanValue()) {
            Log.i(str, "**** *AP* Max attempts at cfg verification via ST device as STA reached - starting attempts at verification via ST device as AP ****");
            getCFGResultViaDeviceAsAP();
        } else {
            Log.i(str, "**** Max attempts at cfg verification via ST device as AP reached, process failed ****");
            this.loader.setVisibility(8);
            showProvDialog("Provisioning Failed", "Could not retrieve configuration verification from SensorTag device", false);
            this.state = wifiStageThreeStates.PROV_FINISHED;
            Timer timer = this.getCFGResultViaDeviceAsAPTimeoutTimer;
            if (timer != null) {
                timer.cancel();
                Log.i(str, "cancel connection to SensorTag device for cfg verification via device as AP timeout - 5 attempts already failed");
                this.getCFGResultViaDeviceAsAPTimeoutTimer = null;
            }
            finishAPProvProcess(Boolean.valueOf(true));
        }
    }

    private void scanForDevices() {
        scanForSensorTagDevices();
    }

    /* access modifiers changed from: private */
    public void startConfigVerification(String str, String str2) {
        this.startConVerHasBeenCalled = true;
        StringBuilder sb = new StringBuilder();
        sb.append("startConVerHasBeenCalled: ");
        sb.append(this.startConVerHasBeenCalled);
        String sb2 = sb.toString();
        String str3 = TAG;
        Log.i(str3, sb2);
        this.ipAdressForExtra = str2;
        this.mDevice = new Device(str, str2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("mDevice: ");
        sb3.append(this.mDevice.name);
        sb3.append(",");
        sb3.append(this.mDevice.host);
        Log.i(str3, sb3.toString());
        new Timer().schedule(new TimerTask() {
            public void run() {
                Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, "SensorTag device data retrieved - get configuration verification from device");
                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.getCFG(Boolean.valueOf(true));
            }
        }, DNSConstants.CLOSE_TIMEOUT);
    }

    /* access modifiers changed from: private */
    public void showProvDialog(String str, String str2, final boolean z) {
        Builder builder = new Builder(this.mThis);
        builder.setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        StringBuilder sb = new StringBuilder();
                        String str = "showProvDialog / Prov was successful: ";
                        sb.append(str);
                        sb.append(z);
                        String str2 = "\nIP Address: ";
                        sb.append(str2);
                        sb.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ipAdressForExtra);
                        String sb2 = sb.toString();
                        String str3 = ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG;
                        Log.d(str3, sb2);
                        if (!z || ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ipAdressForExtra.equals("192.168.1.1")) {
                            Log.d(str3, "showProvDialog / Either prov was unsuccessful or IP address was AP IP - move to TopLevel");
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append(z);
                            sb3.append(str2);
                            sb3.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ipAdressForExtra);
                            Log.d(str3, sb3.toString());
                            Intent intent = new Intent(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis, TopLevel.class);
                            intent.setFlags(67108864);
                            intent.putExtra(TopLevel.CONNECT_TO_CONFIG_EXTRA, ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSendConfig);
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("showProvDialog / ssidToSendConfig: ");
                            sb4.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSendConfig);
                            sb4.append(" extra to TL");
                            Log.d("RECON - ", sb4.toString());
                            ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis.startActivity(intent);
                            return;
                        }
                        Log.d(str3, "showProvDialog / Prov was successful and we have STA IP - move to wifiDeviceActivity");
                        Intent intent2 = new Intent(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.mThis, wifiDeviceActivity.class);
                        intent2.putExtra(DeviceActivity.EXTRA_DEVICE, ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ipAdressForExtra);
                        intent2.putExtra(TopLevel.DEVICE_NAME_EXTRA, ConfigureWiFiActivityStageThreeAPModeProvisioning.this.chosenSTDeviceSSID);
                        intent2.putExtra(TopLevel.CONFIG_TO_CONNECT_TO_EXTRA, ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSendConfig);
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("showProvDialog / extras for wifiDeviceActivity:\nEXTRA_DEVICE: ");
                        sb5.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ipAdressForExtra);
                        sb5.append("\nDEVICE_NAME_EXTRA: ");
                        sb5.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.chosenSTDeviceSSID);
                        sb5.append("\nCONFIG_TO_CONNECT_TO_EXTRA: ");
                        sb5.append(ConfigureWiFiActivityStageThreeAPModeProvisioning.this.ssidToSendConfig);
                        Log.d(str3, sb5.toString());
                        intent2.setFlags(67108864);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.startActivity(intent2);
                    }
                }).run();
            }
        });
        builder.setTitle((CharSequence) str);
        builder.setMessage((CharSequence) str2);
        builder.setCancelable(false);
        try {
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void finishAPProvProcess(Boolean bool) {
        String str = "finishAPProvProcess / *AP* *** ST device AP mode provisioning process complete ***";
        String str2 = TAG;
        Log.i(str2, str);
        String str3 = "RECON - ";
        Log.d(str3, str);
        StringBuilder sb = new StringBuilder();
        sb.append("finishAPProvProcess / diConFromST: ");
        sb.append(bool);
        Log.d(str3, sb.toString());
        if (bool.booleanValue()) {
            Log.d(str3, "finishAPProvProcess / prov. failed - disconnect from St if connected to it");
            String currentConnectionSSID = wifiDeviceActivity.getCurrentConnectionSSID(this.mThis);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("finishAPProvProcess / currConn: ");
            sb2.append(currentConnectionSSID);
            Log.d(str3, sb2.toString());
            if (currentConnectionSSID == null) {
                Log.d(str3, "finishAPProvProcess / no current connection");
            } else if (TopLevel.isSensorTagDevice(currentConnectionSSID)) {
                Log.d(str3, "finishAPProvProcess / currently connected to ST device - disconnect");
                ((WifiManager) this.mThis.getApplicationContext().getSystemService("wifi")).disconnect();
                Log.d(str3, "finishAPProvProcess / wifiManger disconnect called");
            } else {
                Log.d(str3, "finishAPProvProcess / not currently connected to ST device - no need to disconnect");
            }
        } else {
            Log.d(str3, "finishAPProvProcess / prov.did not fail no need to disconnect from ST device");
        }
        StringBuilder sb3 = new StringBuilder();
        String str4 = "finishAPProvProcess / *AP* Connecting to config network: \"";
        sb3.append(str4);
        sb3.append(this.ssidToSend);
        String str5 = "\"";
        sb3.append(str5);
        Log.i(str2, sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str4);
        sb4.append(this.ssidToSend);
        sb4.append(str5);
        Log.d(str3, sb4.toString());
        if (this.ssidToSend == null) {
            String str6 = "finishAPProvProcess / config network is null - will not attempt to connect";
            Log.i(str2, str6);
            Log.d(str3, str6);
        } else if (this.ssidToSendConfig != null) {
            if (this.wController == null) {
                this.wController = new wlanController(this.mThis);
                this.wController.startWlanController();
            }
            this.desiredWifi = this.ssidToSendConfig;
            this.wController.setDesiredWlanConfig(this.desiredWifi);
            StringBuilder sb5 = new StringBuilder();
            String str7 = "finishAPProvProcess / set desired: ";
            sb5.append(str7);
            sb5.append(this.desiredWifi);
            Log.i("wlanController", sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append(str7);
            sb6.append(this.desiredWifi);
            Log.i(str2, sb6.toString());
            StringBuilder sb7 = new StringBuilder();
            sb7.append(str7);
            sb7.append(this.desiredWifi);
            Log.d(str3, sb7.toString());
        }
    }

    /* access modifiers changed from: private */
    public void getCFGResultViaDeviceAsAP() {
        String str = TAG;
        Log.i(str, "Moving to retrieving cfg verification via ST device as AP - getCFGResultViaDeviceAsAP");
        this.state = wifiStageThreeStates.READ_FEEDBACK_FAILED;
        StringBuilder sb = new StringBuilder();
        sb.append("State: ");
        sb.append(this.state);
        Log.i(str, sb.toString());
        this.desiredWifi = this.sensorTagWifi;
        if (this.wController == null) {
            this.wController = new wlanController(this.mThis);
            this.wController.startWlanController();
        }
        this.wController.setDesiredWlanConfig(this.desiredWifi);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("set desired: ");
        sb2.append(this.desiredWifi);
        Log.i("wlanController", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Connect to ST device: ");
        sb3.append(this.desiredWifi.toString());
        Log.i(str, sb3.toString());
        this.getCFGResultViaDeviceAsAPTimeoutTimer = new Timer();
        this.getCFGResultViaDeviceAsAPTimeoutTimer.schedule(new TimerTask() {
            public void run() {
                ConfigureWiFiActivityStageThreeAPModeProvisioning.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.i(ConfigureWiFiActivityStageThreeAPModeProvisioning.TAG, "connection to ST device for cfg verification via ST device as AP timed out - Provisioning process failed");
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.loader.setVisibility(8);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.showProvDialog("Provisioning Failed", "Could not connect to SensorTag device in order to retrieve configuration verification", false);
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.state = wifiStageThreeStates.PROV_FINISHED;
                        ConfigureWiFiActivityStageThreeAPModeProvisioning.this.finishAPProvProcess(Boolean.valueOf(true));
                    }
                });
            }
        }, 70000);
        this.mDevice.host = "192.168.1.1";
        StringBuilder sb4 = new StringBuilder();
        sb4.append("IP address for cfg verification - ST device's IP address when in AP mode - mDevice.host: ");
        sb4.append(this.mDevice.host);
        Log.i(str, sb4.toString());
        this.cgfTryNumber = 0;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("cfgTryNumber: ");
        sb5.append(this.cgfTryNumber);
        Log.i(str, sb5.toString());
    }
}
