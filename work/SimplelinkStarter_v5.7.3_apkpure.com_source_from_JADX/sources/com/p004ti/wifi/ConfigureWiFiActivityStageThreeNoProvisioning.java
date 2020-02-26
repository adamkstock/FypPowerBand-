package com.p004ti.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.p004ti.device_selector.DeviceActivity;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.wifi.utils.UdpBcastServer;
import com.ti.ble.simplelinkstarter.R;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.ti.wifi.ConfigureWiFiActivityStageThreeNoProvisioning */
public class ConfigureWiFiActivityStageThreeNoProvisioning extends AppCompatActivity {
    public static final String CONFIGURE_WIFI_ACTIVITY_STAGE_THREE_EXTRA_SSID = "com.ti.ble.sensortag.ConfigureWiFiStageThree.extra.SSID";
    private static final String IP_ADDRESS = "192.168.1.1";
    public static final String PROVISIONING_FAILED_TIMEOUT_ACTION = "com.ti.ble.sensortag.ConfigureWiFiStageThree.PROVISIONING_FAILED_TIMEOUT_ACTION";
    private static final String TAG = "StageThreeNOProv";
    /* access modifiers changed from: private */
    public Timer ConnToSTTimer;
    /* access modifiers changed from: private */
    public String STSSID;
    /* access modifiers changed from: private */
    public WifiConfiguration desiredWifi;
    /* access modifiers changed from: private */
    public boolean didInterr = false;
    /* access modifiers changed from: private */
    public Timer fwOODTimer;
    /* access modifiers changed from: private */
    public TextView fwVersion;
    private TextView ipAddr;
    /* access modifiers changed from: private */
    public ProgressBar loader;
    /* access modifiers changed from: private */
    public ConfigureWiFiActivityStageThreeNoProvisioning mThis;
    /* access modifiers changed from: private */
    public TextView macAddr;
    private BroadcastReceiver receiver;
    private WifiConfiguration sensorTagWifi;
    /* access modifiers changed from: private */
    public wifiStageThreeStates state = wifiStageThreeStates.START;
    /* access modifiers changed from: private */
    public TextView status;
    private wlanController wController;

    /* renamed from: com.ti.wifi.ConfigureWiFiActivityStageThreeNoProvisioning$wifiStageThreeStates */
    public enum wifiStageThreeStates {
        START,
        WRONG_WIFI,
        INTERROGATION,
        FW_UPGRADE_NEEDED,
        KICK_INTO_PROVISIONING,
        PROVISIONING_ASSOCIATION,
        PROVISION,
        READ_FEEDBACK,
        CANCELED,
        OK,
        READ_FEEDBACK_FAILED,
        CONNECTED_TO_ST_AS_AP
    }

    public void onCreate(Bundle bundle) {
        this.mThis = this;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_configure_wifi_stage_3);
        StringBuilder sb = new StringBuilder();
        String str = "com.ti.ble.sensortag.ConfigureWiFiStageThree.extra.SSID";
        sb.append(getIntent().getStringExtra(str));
        sb.append(" (AP)");
        setTitle(sb.toString());
        this.STSSID = getIntent().getStringExtra(str);
        this.ipAddr = (TextView) findViewById(R.id.acws_three_ip_addr);
        this.fwVersion = (TextView) findViewById(R.id.acws_three_firmware);
        this.status = (TextView) findViewById(R.id.acws_three_status);
        this.macAddr = (TextView) findViewById(R.id.acws_three_mac_addr);
        this.status.setTypeface(null, 1);
        String str2 = "192.168.1.1";
        this.ipAddr.setText(str2);
        this.sensorTagWifi = new WifiConfiguration();
        WifiConfiguration wifiConfiguration = this.sensorTagWifi;
        StringBuilder sb2 = new StringBuilder();
        String str3 = "\"";
        sb2.append(str3);
        sb2.append(getIntent().getStringExtra(str));
        sb2.append(str3);
        wifiConfiguration.SSID = sb2.toString();
        this.sensorTagWifi.allowedKeyManagement.set(0);
        WifiConfiguration wifiConfiguration2 = this.sensorTagWifi;
        wifiConfiguration2.preSharedKey = "";
        this.desiredWifi = wifiConfiguration2;
        this.loader = (ProgressBar) findViewById(R.id.acw_loader);
        this.loader.setVisibility(0);
        this.loader.setIndeterminate(true);
        this.wController = new wlanController(this);
        this.wController.setDesiredWlanConfig(this.desiredWifi);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("set desired: ");
        sb3.append(this.desiredWifi);
        Log.i("wlanController", sb3.toString());
        this.wController.startWlanController();
        try {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("state: ");
        sb4.append(this.state);
        String sb5 = sb4.toString();
        String str4 = TAG;
        Log.i(str4, sb5);
        boolean deviceOnCorrectWLAN = wifiDeviceActivity.deviceOnCorrectWLAN(this.mThis, getIntent().getStringExtra(str));
        StringBuilder sb6 = new StringBuilder();
        sb6.append("Interr - we are on correct wlan: ");
        sb6.append(deviceOnCorrectWLAN);
        Log.i(str4, sb6.toString());
        if (deviceOnCorrectWLAN) {
            Log.i(str4, "Interr - Connected to SensorTag device - interrogating device");
            interrogateDevice(str2);
            this.state = wifiStageThreeStates.INTERROGATION;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("Interr - State: ");
            sb7.append(this.state);
            Log.i(str4, sb7.toString());
            this.didInterr = true;
            StringBuilder sb8 = new StringBuilder();
            sb8.append("Interr - didInterr: ");
            sb8.append(this.didInterr);
            Log.i(str4, sb8.toString());
        }
    }

    public void onResume() {
        super.onResume();
        this.loader.setVisibility(0);
        if (this.wController == null) {
            this.wController = new wlanController(this.mThis);
            this.wController.setDesiredWlanConfig(this.desiredWifi);
            StringBuilder sb = new StringBuilder();
            sb.append("set desired: ");
            sb.append(this.desiredWifi);
            Log.i("wlanController", sb.toString());
            this.wController.startWlanController();
        }
        IntentFilter intentFilter = new IntentFilter(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE);
        intentFilter.addAction(wifiDeviceActivity.WIFI_DEVICE_REQUEST_DISCONNECTED);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_OK");
        intentFilter.addAction(ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_CANCELED);
        intentFilter.addAction(UdpBcastServer.UDP_BCAST_SERVER_FOUND_DEVICE_ACTION);
        intentFilter.addAction("com.ti.ble.sensortag.ConfigureWiFiStageThree.PROVISIONING_FAILED_TIMEOUT_ACTION");
        intentFilter.addAction(wlanController.WE_ARE_ON_DESIRED_WLAN_ACTION);
        this.receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean equals = intent.getAction().equals(wlanController.WE_ARE_ON_DESIRED_WLAN_ACTION);
                String str = ConfigureWiFiActivityStageThreeNoProvisioning.TAG;
                if (equals) {
                    Log.i(str, "Interr - WE_ARE_ON_DESIRED_WLAN_ACTION");
                    StringBuilder sb = new StringBuilder();
                    sb.append("We are on wlan : ");
                    sb.append(ConfigureWiFiActivityStageThreeNoProvisioning.this.desiredWifi.SSID);
                    Log.d(str, sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("state : ");
                    sb2.append(ConfigureWiFiActivityStageThreeNoProvisioning.this.state);
                    Log.d(str, sb2.toString());
                    if (ConfigureWiFiActivityStageThreeNoProvisioning.this.state == wifiStageThreeStates.START) {
                        Log.i(str, "Interr - WE_ARE_ON_DESIRED_WLAN_ACTION/START");
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Interr - didInterr: ");
                        sb3.append(ConfigureWiFiActivityStageThreeNoProvisioning.this.didInterr);
                        Log.i(str, sb3.toString());
                        if (!ConfigureWiFiActivityStageThreeNoProvisioning.this.didInterr) {
                            Log.i(str, "Interr - did not already interrogate device - interrogate now");
                            Log.i(str, "Connected to SensorTag device - interrogating device");
                            ConfigureWiFiActivityStageThreeNoProvisioning.this.interrogateDevice("192.168.1.1");
                            ConfigureWiFiActivityStageThreeNoProvisioning.this.state = wifiStageThreeStates.INTERROGATION;
                        }
                    }
                }
                if (intent.getAction().equals(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE)) {
                    String stringExtra = intent.getStringExtra(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_DATA);
                    String stringExtra2 = intent.getStringExtra(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_URL);
                    intent.getStringExtra(wifiDeviceActivity.WIFI_DEVICE_REQUEST_IP_ADDR);
                    if (stringExtra2.equals(wifiDeviceActivity.WIFI_DEVICE_CONFIG_URL)) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Received data : ");
                        sb4.append(stringExtra);
                        Log.i(str, sb4.toString());
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("state: ");
                        sb5.append(ConfigureWiFiActivityStageThreeNoProvisioning.this.state);
                        Log.i(str, sb5.toString());
                        if (ConfigureWiFiActivityStageThreeNoProvisioning.this.state == wifiStageThreeStates.INTERROGATION) {
                            Log.i(str, "Interr - got interrogation results");
                            ConfigureWiFiActivityStageThreeNoProvisioning.this.loader.setVisibility(8);
                            ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis.fwVersion.setText(wifiDeviceActivity.getFirmwareString(stringExtra));
                            ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis.macAddr.setText(wifiDeviceActivity.getMacString(stringExtra));
                            ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis.status.setText(R.string.Idle);
                            Log.i(str, "Checking firmware version is up-to-date");
                            if (wifiDeviceActivity.isLatestFirmware((String) ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis.fwVersion.getText())) {
                                ConfigureWiFiActivityStageThreeNoProvisioning.this.ConnToSTTimer = new Timer();
                                ConfigureWiFiActivityStageThreeNoProvisioning.this.ConnToSTTimer.schedule(new TimerTask() {
                                    public void run() {
                                        ConfigureWiFiActivityStageThreeNoProvisioning.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Log.i(ConfigureWiFiActivityStageThreeNoProvisioning.TAG, "Firmware is up-to-date - move to connecting");
                                                ConfigureWiFiActivityStageThreeNoProvisioning.this.state = wifiStageThreeStates.CONNECTED_TO_ST_AS_AP;
                                                ConfigureWiFiActivityStageThreeNoProvisioning.this.status.setText(R.string.connected_to_sensortag_ap);
                                                Builder builder = new Builder(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis);
                                                builder.setTitle((CharSequence) "Connected To SensorTag");
                                                builder.setMessage((CharSequence) "Connection to SensorTag device as AP was established successfully.\n\nIf you would like to provision the SensorTag device as a station in another wLAN, click \"PROVISION\".\n\nTo view the sensors data click \"SENSORS\".");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton((CharSequence) "PROVISION", (OnClickListener) new OnClickListener() {
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        Intent intent = new Intent(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis, ConfigureWiFiActivityStageThreeAPModeProvisioning.class);
                                                        intent.putExtra("com.ti.ble.sensortag.ConfigureWiFiStageThree.extra.SSID", ConfigureWiFiActivityStageThreeNoProvisioning.this.STSSID);
                                                        ConfigureWiFiActivityStageThreeNoProvisioning.this.startActivity(intent);
                                                    }
                                                });
                                                builder.setNegativeButton((CharSequence) "SENSORS", (OnClickListener) new OnClickListener() {
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        Intent intent = new Intent(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis, wifiDeviceActivity.class);
                                                        intent.putExtra(DeviceActivity.EXTRA_DEVICE, "192.168.1.1");
                                                        intent.putExtra(TopLevel.DEVICE_NAME_EXTRA, ConfigureWiFiActivityStageThreeNoProvisioning.this.STSSID);
                                                        intent.setFlags(67108864);
                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append("connectedAsAP / extras for wifiDeviceActivity:\nEXTRA_DEVICE: 192.168.1.1\nDEVICE_NAME_EXTRA: ");
                                                        sb.append(ConfigureWiFiActivityStageThreeNoProvisioning.this.STSSID);
                                                        sb.append("\nconfigToConnectTo: none");
                                                        Log.d(ConfigureWiFiActivityStageThreeNoProvisioning.TAG, sb.toString());
                                                        ConfigureWiFiActivityStageThreeNoProvisioning.this.startActivity(intent);
                                                    }
                                                });
                                                builder.create().show();
                                            }
                                        });
                                    }
                                }, 2000);
                                return;
                            }
                            Log.i(str, "Firmware is not up-to-date - requires upgrade");
                            ConfigureWiFiActivityStageThreeNoProvisioning.this.fwOODTimer = new Timer();
                            ConfigureWiFiActivityStageThreeNoProvisioning.this.fwOODTimer.schedule(new TimerTask() {
                                public void run() {
                                    ConfigureWiFiActivityStageThreeNoProvisioning.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Builder builder = new Builder(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis);
                                            builder.setTitle((CharSequence) "Out-of-Date Firmware");
                                            builder.setMessage((CharSequence) "SensorTag firmware is out-of-date. To upgrade your SensorTag device to the latest firmware, click \"UPGRADE\".\n\nOtherwise click \"CANCEL\".");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton((CharSequence) "UPGRADE", (OnClickListener) new OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    ConfigureWiFiActivityStageThreeNoProvisioning.this.state = wifiStageThreeStates.FW_UPGRADE_NEEDED;
                                                    Intent intent = new Intent(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis, wifiOTAActivity.class);
                                                    intent.putExtra(DeviceActivity.EXTRA_DEVICE, "192.168.1.1");
                                                    ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis.startActivity(intent);
                                                }
                                            });
                                            builder.setNegativeButton((CharSequence) "CANCEL", (OnClickListener) new OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    Builder builder = new Builder(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis);
                                                    builder.setTitle((CharSequence) "Connected To SensorTag");
                                                    builder.setMessage((CharSequence) "Connection to SensorTag device as AP was established successfully.\n\nIf you would like to provision the SensorTag device as a station in another wLAN, click \"PROVISION\".\n\nTo go to the main page click \"MAIN\",\n\nOtherwise click \"OK\".");
                                                    builder.setCancelable(false);
                                                    builder.setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    builder.setNegativeButton((CharSequence) "PROVISION", (OnClickListener) new OnClickListener() {
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                            Intent intent = new Intent(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis, ConfigureWiFiActivityStageThreeAPModeProvisioning.class);
                                                            intent.putExtra("com.ti.ble.sensortag.ConfigureWiFiStageThree.extra.SSID", ConfigureWiFiActivityStageThreeNoProvisioning.this.STSSID);
                                                            ConfigureWiFiActivityStageThreeNoProvisioning.this.startActivity(intent);
                                                        }
                                                    });
                                                    builder.setNeutralButton((CharSequence) "MAIN", (OnClickListener) new OnClickListener() {
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis, TopLevel.class);
                                                            intent.setFlags(67108864);
                                                            ConfigureWiFiActivityStageThreeNoProvisioning.this.startActivity(intent);
                                                        }
                                                    });
                                                    ConfigureWiFiActivityStageThreeNoProvisioning.this.loader.setVisibility(8);
                                                    builder.create().show();
                                                }
                                            });
                                            builder.create().show();
                                        }
                                    });
                                }
                            }, 2000);
                        }
                    }
                }
            }
        };
        registerReceiver(this.receiver, intentFilter);
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(this.receiver);
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
                String str = ConfigureWiFiActivityStageThreeNoProvisioning.TAG;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Interrogating device @ ");
                    sb.append(str);
                    Log.d(str, sb.toString());
                    wifiDeviceActivity.getDeviceConfig(ConfigureWiFiActivityStageThreeNoProvisioning.this.mThis, str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        Timer timer = this.ConnToSTTimer;
        if (timer != null) {
            timer.cancel();
            this.ConnToSTTimer = null;
        }
        Timer timer2 = this.fwOODTimer;
        if (timer2 != null) {
            timer2.cancel();
            this.fwOODTimer = null;
        }
        wlanController wlancontroller = this.wController;
        if (wlancontroller != null) {
            wlancontroller.stopWlanController();
            Log.d("RECON - ", "StageThreeNoProv/onStop stopped wlanCon");
            this.wController = null;
        }
    }
}
