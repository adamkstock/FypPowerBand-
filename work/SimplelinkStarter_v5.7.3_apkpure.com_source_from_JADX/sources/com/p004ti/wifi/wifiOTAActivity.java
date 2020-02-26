package com.p004ti.wifi;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.p000v4.internal.view.SupportMenu;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.p004ti.device_selector.DeviceActivity;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.util.TftpServer;
import com.p004ti.wifi.utils.OTAFBTimerTask;
import com.ti.ble.simplelinkstarter.R;
import java.util.Timer;
import java.util.TimerTask;
import javax.jmdns.impl.constants.DNSConstants;

/* renamed from: com.ti.wifi.wifiOTAActivity */
public class wifiOTAActivity extends AppCompatActivity implements OnClickListener {
    public static final int BATTERY_SAVER_MODE = 0;
    private static final String LATEST_FW = "1.003 Feb 22 2017 10:55:45";
    public static final int STAY_AWAKE_MODE = 1;
    private static final String TAG = "wifiOTAActivity";
    public static OTAStates state = OTAStates.ONGOING;
    private AlertDialog alertDialog;
    private BroadcastReceiver bReceiver;
    private Button batterySaverModeBtn;
    /* access modifiers changed from: private */
    public Timer connIfFailedTimer;
    /* access modifiers changed from: private */
    public TextView curFirmware;
    private Button factoryDefaultBtn;
    /* access modifiers changed from: private */
    public String firmwareRev;
    private IntentFilter iFilter;
    /* access modifiers changed from: private */
    public String ipAddress;
    private TextView label;
    private TextView latestFirmware;
    /* access modifiers changed from: private */
    public wifiOTAActivity mThis;
    private TextView message;
    /* access modifiers changed from: private */
    public ProgressDialog otaFailProgDialog;

    /* renamed from: pB */
    private ProgressBar f87pB;
    private RelativeLayout progIndicLayout;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;
    /* access modifiers changed from: private */
    public TextView progressText;
    /* access modifiers changed from: private */
    public WifiConfiguration sensorTagWifi;
    /* access modifiers changed from: private */
    public TftpServer server;
    private Button startOTAButton;
    private String startingActivity;
    private Button stayAwakeModeBtn;
    private Timer timeoutTimer;
    /* access modifiers changed from: private */
    public Timer timer;
    /* access modifiers changed from: private */
    public wlanController wController;

    /* renamed from: com.ti.wifi.wifiOTAActivity$OTAStates */
    public enum OTAStates {
        FAIL,
        ONGOING,
        CONN_FOR_FACTORY_DEFAULT_AFTER_FAILED_OTA,
        FINISHED
    }

    public void onCreate(Bundle bundle) {
        this.mThis = this;
        this.ipAddress = getIntent().getStringExtra(DeviceActivity.EXTRA_DEVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("Received IP address: ");
        sb.append(this.ipAddress);
        String sb2 = sb.toString();
        String str = TAG;
        Log.i(str, sb2);
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_wifi_ota);
        this.startOTAButton = (Button) findViewById(R.id.awo_start_ota_button);
        this.startOTAButton.setOnClickListener(this);
        this.iFilter = new IntentFilter(TftpServer.TFTPSERVER_PROGRESS_UPDATE_ACTION);
        this.iFilter.addAction(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE);
        this.iFilter.addAction(TftpServer.TFTPSERVER_NO_PROGRESS_UPDATE_TIMEOUT);
        this.iFilter.addAction(wlanController.WE_ARE_ON_DESIRED_WLAN_ACTION);
        this.progressBar = (ProgressBar) findViewById(R.id.awo_progress_bar);
        this.progressText = (TextView) findViewById(R.id.awo_progress_text);
        this.progressBar.setProgressTintList(ColorStateList.valueOf(SupportMenu.CATEGORY_MASK));
        this.curFirmware = (TextView) findViewById(R.id.awo_cur_firmware);
        this.latestFirmware = (TextView) findViewById(R.id.awo_newest_firmware);
        this.progIndicLayout = (RelativeLayout) findViewById(R.id.progress_indicator_layout);
        this.f87pB = (ProgressBar) this.progIndicLayout.findViewById(R.id.progress_indicator_layout_progress_bar);
        this.label = (TextView) this.progIndicLayout.findViewById(R.id.progress_indicator_layout_label);
        this.message = (TextView) this.progIndicLayout.findViewById(R.id.progress_indicator_layout_message);
        this.factoryDefaultBtn = (Button) findViewById(R.id.wifi_ota_activity_factory_default_btn);
        this.factoryDefaultBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(wifiOTAActivity.this.mThis);
                builder.setCancelable(false);
                builder.setTitle((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.return_to_factory_default));
                builder.setMessage((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.are_you_sure_factory_default));
                builder.setPositiveButton((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.yes), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        wifiDeviceActivity.writeFactoryDefault(wifiOTAActivity.this.mThis, wifiOTAActivity.this.ipAddress);
                        StringBuilder sb = new StringBuilder();
                        sb.append("FACTORY DEFAULT- writing factory default with IP address: ");
                        sb.append(wifiOTAActivity.this.ipAddress);
                        Log.i(wifiOTAActivity.TAG, sb.toString());
                        final ProgressDialog progressDialog = new ProgressDialog(wifiOTAActivity.this.mThis);
                        progressDialog.setTitle(wifiOTAActivity.this.getResources().getString(R.string.returning_to_factory_default));
                        progressDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.returning_to_factory_default_message));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new Timer().schedule(new TimerTask() {
                            public void run() {
                                wifiOTAActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });
                                Intent intent = new Intent(wifiOTAActivity.this.mThis, TopLevel.class);
                                intent.setFlags(67108864);
                                wifiOTAActivity.this.startActivity(intent);
                            }
                        }, DNSConstants.SERVICE_INFO_TIMEOUT);
                    }
                });
                builder.setNeutralButton((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.no), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        this.stayAwakeModeBtn = (Button) findViewById(R.id.wifi_ota_activity_stay_awake_btn);
        this.stayAwakeModeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(wifiOTAActivity.this.mThis);
                builder.setCancelable(false);
                builder.setTitle((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.stay_awake_dialog_title));
                builder.setMessage((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.stay_awake_dialog_message));
                builder.setPositiveButton((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.yes), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = wifiOTAActivity.TAG;
                        Log.i(str, "MODE - stayAwakeModeDialog yes clicked");
                        dialogInterface.dismiss();
                        wifiDeviceActivity.writeAwakeMode(wifiOTAActivity.this.mThis, wifiOTAActivity.this.ipAddress, 1);
                        StringBuilder sb = new StringBuilder();
                        sb.append("MODE - STAY AWAKE- writing stay awake timeout with IP address: ");
                        sb.append(wifiOTAActivity.this.ipAddress);
                        Log.i(str, sb.toString());
                        Toast.makeText(wifiOTAActivity.this.mThis, "Stay Awake mode written to ST device", 0).show();
                    }
                });
                builder.setNeutralButton((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.no), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(wifiOTAActivity.TAG, "MODE - stayAwakeModeDialog no clicked");
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        this.batterySaverModeBtn = (Button) findViewById(R.id.wifi_ota_activity_battery_saver_btn);
        this.batterySaverModeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(wifiOTAActivity.this.mThis);
                builder.setCancelable(false);
                builder.setTitle((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.battery_saver_dialog_title));
                builder.setMessage((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.battery_saver_dialog_message));
                builder.setPositiveButton((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.yes), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = wifiOTAActivity.TAG;
                        Log.i(str, "MODE - batterySaverModeDialogBuilder yes clicked");
                        dialogInterface.dismiss();
                        wifiDeviceActivity.writeAwakeMode(wifiOTAActivity.this.mThis, wifiOTAActivity.this.ipAddress, 0);
                        StringBuilder sb = new StringBuilder();
                        sb.append("MODE - BATTERY SAVER- writing battery saver timeout with IP address: ");
                        sb.append(wifiOTAActivity.this.ipAddress);
                        Log.i(str, sb.toString());
                        Toast.makeText(wifiOTAActivity.this.mThis, "Battery Saver mode written to ST device", 0).show();
                    }
                });
                builder.setNeutralButton((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.no), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(wifiOTAActivity.TAG, "MODE - batterySaverModeDialogBuilder no clicked");
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        try {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.latestFirmware.setText(LATEST_FW);
        Log.i(str, "latest fw: 1.003 Feb 22 2017 10:55:45");
        WifiInfo connectionInfo = ((WifiManager) this.mThis.getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Current SSID: ");
        sb3.append(connectionInfo.getSSID());
        Log.i(str, sb3.toString());
        this.sensorTagWifi = new WifiConfiguration();
        this.sensorTagWifi.SSID = connectionInfo.getSSID();
        this.sensorTagWifi.allowedKeyManagement.set(0);
        this.sensorTagWifi.preSharedKey = "";
        this.wController = new wlanController(this.mThis);
        this.wController.setDesiredWlanConfig(this.sensorTagWifi);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("set desired: ");
        sb4.append(this.sensorTagWifi);
        Log.i("wlanController", sb4.toString());
        this.wController.startWlanController();
        this.bReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean equals = intent.getAction().equals(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE);
                String str = wifiOTAActivity.TAG;
                if (equals) {
                    Log.i(str, "WIFI_DEVICE_REQUEST_RESPONSE");
                    String str2 = wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_DATA;
                    String stringExtra = intent.getStringExtra(str2);
                    Log.d(str, stringExtra);
                    if (stringExtra.contains("fwr")) {
                        wifiOTAActivity.this.firmwareRev = wifiDeviceActivity.getFirmwareString(intent.getStringExtra(str2));
                        StringBuilder sb = new StringBuilder();
                        sb.append("current fw: ");
                        sb.append(wifiOTAActivity.this.firmwareRev);
                        Log.d(str, sb.toString());
                        wifiOTAActivity.this.curFirmware.setText(wifiOTAActivity.this.firmwareRev);
                    }
                } else if (intent.getAction().equals(TftpServer.TFTPSERVER_PROGRESS_UPDATE_ACTION)) {
                    Log.i(str, "TFTPSERVER_PROGRESS_UPDATE_ACTION");
                    ProgressBar access$400 = wifiOTAActivity.this.progressBar;
                    String str3 = TftpServer.TFTPSERVER_PROGRESS_UPDATE_PERCENTAGE;
                    access$400.setProgress(intent.getIntExtra(str3, 0));
                    TextView access$500 = wifiOTAActivity.this.progressText;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(intent.getIntExtra(str3, 0));
                    sb2.append(" %");
                    access$500.setText(sb2.toString());
                    if (intent.getIntExtra(str3, 0) == 100) {
                        try {
                            wifiOTAActivity.this.server.stopTimer();
                            wifiOTAActivity.state = OTAStates.FINISHED;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("OTA task failed dialog - FTPSERVER_PROGRESS_UPDATE_ACTION/100 - state: ");
                            sb3.append(wifiOTAActivity.state);
                            Log.i(str, sb3.toString());
                            final ProgressDialog progressDialog = new ProgressDialog(wifiOTAActivity.this.mThis);
                            progressDialog.setTitle(wifiOTAActivity.this.getResources().getString(R.string.waiting_for_st_device_to_reboot));
                            progressDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.waiting_for_st_device_to_reboot_message));
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            C10681 r8 = new TimerTask() {
                                public void run() {
                                    wifiOTAActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog.setTitle("ST Device Restarting");
                                        }
                                    });
                                }
                            };
                            C10702 r0 = new TimerTask() {
                                public void run() {
                                    wifiOTAActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog.setTitle("ST Device Test Booting");
                                        }
                                    });
                                }
                            };
                            C10723 r1 = new TimerTask() {
                                public void run() {
                                    wifiOTAActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(wifiOTAActivity.this.mThis, ConfigureWiFiActivity.class);
                                            intent.setFlags(67108864);
                                            wifiOTAActivity.this.mThis.startActivity(intent);
                                        }
                                    });
                                }
                            };
                            wifiOTAActivity.this.timer = new Timer();
                            wifiOTAActivity.this.timer.schedule(r8, DNSConstants.CLOSE_TIMEOUT);
                            wifiOTAActivity.this.timer.schedule(r0, 13000);
                            wifiOTAActivity.this.timer.schedule(r1, 25000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (intent.getAction().equals(TftpServer.TFTPSERVER_NO_PROGRESS_UPDATE_TIMEOUT)) {
                    Log.i(str, "OTA task failed dialog - TFTPSERVER_NO_PROGRESS_UPDATE_TIMEOUT");
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("OTA task failed dialog - TFTPSERVER_NO_PROGRESS_UPDATE_TIMEOUT / state: ");
                    sb4.append(wifiOTAActivity.state);
                    Log.i(str, sb4.toString());
                    if (wifiOTAActivity.state != OTAStates.FINISHED) {
                        Log.i(str, "OTA task failed dialog - show dialog");
                        Builder builder = new Builder(wifiOTAActivity.this.mThis);
                        builder.setCancelable(false);
                        builder.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String str = wifiOTAActivity.TAG;
                                Log.i(str, "OTA task failed dialog - OK clicked");
                                dialogInterface.dismiss();
                                wifiOTAActivity.this.otaFailProgDialog = new ProgressDialog(wifiOTAActivity.this.mThis);
                                wifiOTAActivity.this.otaFailProgDialog.setTitle(wifiOTAActivity.this.getResources().getString(R.string.ota_failed_progress_dialog_title));
                                wifiOTAActivity.this.otaFailProgDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.ota_failed_progress_dialog_message_1));
                                wifiOTAActivity.this.otaFailProgDialog.setIndeterminate(true);
                                wifiOTAActivity.this.otaFailProgDialog.setCancelable(false);
                                wifiOTAActivity.this.otaFailProgDialog.show();
                                Log.i(str, "OTA task failed dialog - Check if we are connected to ST device");
                                String currentConnectionSSID = wifiDeviceActivity.getCurrentConnectionSSID(wifiOTAActivity.this.mThis);
                                if (currentConnectionSSID == null) {
                                    currentConnectionSSID = "";
                                }
                                String str2 = "OTA task failed dialog - state: ";
                                if (TopLevel.isSensorTagDevice(currentConnectionSSID)) {
                                    wifiOTAActivity.this.otaFailProgDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.ota_failed_progress_dialog_message_2));
                                    Log.i(str, "OTA task failed dialog - We are connected to ST device - commit factory default to start a new OTA process");
                                    wifiDeviceActivity.writeFactoryDefault(wifiOTAActivity.this.mThis, wifiOTAActivity.this.ipAddress);
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("OTA task failed dialog - FACTORY DEFAULT- writing factory default with IP address: ");
                                    sb.append(wifiOTAActivity.this.ipAddress);
                                    Log.i(str, sb.toString());
                                    Log.i(str, "OTA task failed dialog - Factory default has been committed - re connect to ST device after reboot to continue OTA process");
                                    wifiOTAActivity.this.wController.stopWlanController();
                                    Log.d("RECON - ", "OTA/BR stopped wlanCon");
                                    wifiOTAActivity.this.wController = null;
                                    wifiOTAActivity.this.wController = new wlanController(wifiOTAActivity.this.mThis);
                                    wifiOTAActivity.this.wController.setDesiredWlanConfig(wifiOTAActivity.this.sensorTagWifi);
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("set desired: ");
                                    sb2.append(wifiOTAActivity.this.sensorTagWifi);
                                    Log.i("wlanController", sb2.toString());
                                    wifiOTAActivity.this.wController.startWlanController();
                                    wifiOTAActivity.this.connIfFailedTimer = new Timer();
                                    wifiOTAActivity.this.connIfFailedTimer.schedule(new OTAFBTimerTask(wifiOTAActivity.this.mThis, wifiOTAActivity.this.otaFailProgDialog), 60000);
                                    Log.i(str, "OTA task failed dialog - We are initially connected to ST device / connIfFailedTimer scheduled");
                                    wifiOTAActivity.state = OTAStates.FAIL;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(str2);
                                    sb3.append(wifiOTAActivity.state);
                                    Log.i(str, sb3.toString());
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("OTA task failed dialog - restart wlanController with ST configuration: ");
                                    sb4.append(wifiOTAActivity.this.sensorTagWifi);
                                    Log.i(str, sb4.toString());
                                    Log.i(str, "OTA task failed dialog - waiting for wlanController BR");
                                    wifiOTAActivity.this.otaFailProgDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.ota_failed_progress_dialog_message_4));
                                    return;
                                }
                                wifiOTAActivity.this.otaFailProgDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.ota_failed_progress_dialog_message_3));
                                Log.i(str, "OTA task failed dialog - We are NOT connected to ST device");
                                Log.i(str, "OTA task failed dialog - NOT connected - Connect to ST device and then commit factory default to start a new OTA process");
                                wifiOTAActivity.this.wController.stopWlanController();
                                Log.d(str, "OTA task failed dialog - NOT connected - OTA/BR stopped wlanCon");
                                wifiOTAActivity.this.wController = null;
                                wifiOTAActivity.this.wController = new wlanController(wifiOTAActivity.this.mThis);
                                wifiOTAActivity.this.wController.setDesiredWlanConfig(wifiOTAActivity.this.sensorTagWifi);
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("OTA task failed dialog - NOT connected - set desired: ");
                                sb5.append(wifiOTAActivity.this.sensorTagWifi);
                                Log.i(str, sb5.toString());
                                wifiOTAActivity.this.wController.startWlanController();
                                wifiOTAActivity.this.connIfFailedTimer = new Timer();
                                wifiOTAActivity.this.connIfFailedTimer.schedule(new OTAFBTimerTask(wifiOTAActivity.this.mThis, wifiOTAActivity.this.otaFailProgDialog), 60000);
                                Log.i(str, "OTA task failed dialog - We are NOT initially connected to ST device / connIfFailedTimer scheduled");
                                wifiOTAActivity.state = OTAStates.CONN_FOR_FACTORY_DEFAULT_AFTER_FAILED_OTA;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append(str2);
                                sb6.append(wifiOTAActivity.state);
                                Log.i(str, sb6.toString());
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append("OTA task failed dialog - NOT connected - restart wlanController with ST configuration: ");
                                sb7.append(wifiOTAActivity.this.sensorTagWifi);
                                Log.i(str, sb7.toString());
                                Log.i(str, "OTA task failed dialog - NOT connected - waiting for wlanController BR");
                            }
                        });
                        builder.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String str = wifiOTAActivity.TAG;
                                Log.i(str, "OTA task failed dialog - CANCEL clicked");
                                dialogInterface.dismiss();
                                wifiDeviceActivity.writeFactoryDefault(wifiOTAActivity.this.mThis, wifiOTAActivity.this.ipAddress);
                                StringBuilder sb = new StringBuilder();
                                sb.append("OTA task failed dialog - FACTORY DEFAULT- writing factory default with IP address: ");
                                sb.append(wifiOTAActivity.this.ipAddress);
                                Log.i(str, sb.toString());
                                Log.i(str, "OTA task failed dialog - go to TopLevel_");
                                Intent intent = new Intent(wifiOTAActivity.this.mThis, TopLevel.class);
                                intent.setFlags(67108864);
                                wifiOTAActivity.this.mThis.startActivity(intent);
                            }
                        });
                        builder.setTitle((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.ota_failed_dialog_title));
                        builder.setMessage((CharSequence) wifiOTAActivity.this.getResources().getString(R.string.ota_failed_dialog_message));
                        builder.create().show();
                    }
                } else {
                    String action = intent.getAction();
                    String str4 = wlanController.WE_ARE_ON_DESIRED_WLAN_ACTION;
                    String str5 = "OTA task failed dialog - state: ";
                    String str6 = "OTA task failed dialog - WE_ARE_ON_DESIRED_WLAN_ACTION / state :";
                    if (action.equals(str4) && wifiOTAActivity.state == OTAStates.FAIL) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str6);
                        sb5.append(wifiOTAActivity.state);
                        Log.i(str, sb5.toString());
                        if (wifiOTAActivity.this.connIfFailedTimer != null) {
                            wifiOTAActivity.this.connIfFailedTimer.cancel();
                            wifiOTAActivity.this.connIfFailedTimer.purge();
                            wifiOTAActivity.this.connIfFailedTimer = null;
                            Log.i(str, "OTA task failed dialog - final connection - restart OTA process / connIfFailedTimer cancelled");
                        }
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str5);
                        sb6.append(wifiOTAActivity.state);
                        Log.i(str, sb6.toString());
                        wifiOTAActivity.state = OTAStates.ONGOING;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(str5);
                        sb7.append(wifiOTAActivity.state);
                        Log.i(str, sb7.toString());
                        wifiOTAActivity.this.otaFailProgDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.ota_failed_progress_dialog_message_5));
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str6);
                        sb8.append(wifiOTAActivity.state);
                        Log.i(str, sb8.toString());
                        Log.i(str, "OTA task failed dialog - WE_ARE_ON_DESIRED_WLAN_ACTION / run startOTAUpgrade again");
                        wifiOTAActivity.this.server.cancelPreviousAndStartNewTimer();
                        Log.i(str, "OTA task failed dialog - WE_ARE_ON_DESIRED_WLAN_ACTION / cancelPreviousAndStartNewTimer/onClick");
                        wifiOTAActivity.this.otaFailProgDialog.dismiss();
                        wifiDeviceActivity.startOTAUpgrade(wifiOTAActivity.this.mThis, wifiOTAActivity.this.ipAddress, wifiOTAActivity.this.server.ourIp);
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("start OTA upgrade with IP address: ");
                        sb9.append(wifiOTAActivity.this.ipAddress);
                        Log.i(str, sb9.toString());
                    } else if (intent.getAction().equals(str4) && wifiOTAActivity.state == OTAStates.CONN_FOR_FACTORY_DEFAULT_AFTER_FAILED_OTA) {
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(str6);
                        sb10.append(wifiOTAActivity.state);
                        Log.i(str, sb10.toString());
                        if (wifiOTAActivity.this.connIfFailedTimer != null) {
                            wifiOTAActivity.this.connIfFailedTimer.cancel();
                            wifiOTAActivity.this.connIfFailedTimer.purge();
                            wifiOTAActivity.this.connIfFailedTimer = null;
                            Log.i(str, "OTA task failed dialog - made initial conn - write factory default / connIfFailedTimer cancelled");
                        }
                        wifiOTAActivity.this.otaFailProgDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.ota_failed_progress_dialog_message_2));
                        wifiDeviceActivity.writeFactoryDefault(wifiOTAActivity.this.mThis, wifiOTAActivity.this.ipAddress);
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("OTA task failed dialog - WE_ARE_ON_DESIRED_WLAN_ACTION / FACTORY DEFAULT- writing factory default with IP address: ");
                        sb11.append(wifiOTAActivity.this.ipAddress);
                        Log.i(str, sb11.toString());
                        Log.i(str, "OTA task failed dialog - WE_ARE_ON_DESIRED_WLAN_ACTION / Factory default has been committed - re connect to ST device after reboot to start a new OTA process");
                        wifiOTAActivity.this.wController.stopWlanController();
                        Log.d(str, "WE_ARE_ON_DESIRED_WLAN_ACTION / OTA/BR stopped wlanCon");
                        wifiOTAActivity.this.wController = null;
                        wifiOTAActivity wifiotaactivity = wifiOTAActivity.this;
                        wifiotaactivity.wController = new wlanController(wifiotaactivity.mThis);
                        wifiOTAActivity.this.wController.setDesiredWlanConfig(wifiOTAActivity.this.sensorTagWifi);
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("set desired: ");
                        sb12.append(wifiOTAActivity.this.sensorTagWifi);
                        Log.i(str, sb12.toString());
                        wifiOTAActivity.this.wController.startWlanController();
                        wifiOTAActivity.this.connIfFailedTimer = new Timer();
                        wifiOTAActivity.this.connIfFailedTimer.schedule(new OTAFBTimerTask(wifiOTAActivity.this.mThis, wifiOTAActivity.this.otaFailProgDialog), 60000);
                        Log.i(str, "OTA task failed dialog - reconn after factory default after no initial conn / connIfFailedTimer scheduled");
                        wifiOTAActivity.state = OTAStates.FAIL;
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append(str5);
                        sb13.append(wifiOTAActivity.state);
                        Log.i(str, sb13.toString());
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append("OTA task failed dialog - WE_ARE_ON_DESIRED_WLAN_ACTION / restart wlanController with ST configuration: ");
                        sb14.append(wifiOTAActivity.this.sensorTagWifi);
                        Log.i(str, sb14.toString());
                        Log.i(str, "OTA task failed dialog - WE_ARE_ON_DESIRED_WLAN_ACTION / waiting for wlanController BR");
                        wifiOTAActivity.this.otaFailProgDialog.setMessage(wifiOTAActivity.this.getResources().getString(R.string.ota_failed_progress_dialog_message_4));
                    }
                }
            }
        };
    }

    public void onStart() {
        super.onStart();
        this.server = new TftpServer(6969, this);
        this.server.start();
        registerReceiver(this.bReceiver, this.iFilter);
        wifiDeviceActivity.getDeviceConfig(this.mThis, this.ipAddress);
        StringBuilder sb = new StringBuilder();
        sb.append("Get device config with IP address: ");
        sb.append(this.ipAddress);
        Log.i(TAG, sb.toString());
    }

    public void onStop() {
        super.onStop();
        TftpServer tftpServer = this.server;
        tftpServer.shouldStop = true;
        tftpServer.connection.close();
        unregisterReceiver(this.bReceiver);
        this.wController.stopWlanController();
        Log.d("RECON - ", "OTA/onStop stopped wlanCon");
        Timer timer2 = this.timer;
        if (timer2 != null) {
            timer2.cancel();
            this.timer = null;
        }
        Timer timer3 = this.connIfFailedTimer;
        String str = TAG;
        if (timer3 != null) {
            timer3.cancel();
            this.connIfFailedTimer = null;
            Log.i(str, "OTA task failed dialog - onStop / connIfFailedTimer cancelled");
        }
        state = OTAStates.ONGOING;
        StringBuilder sb = new StringBuilder();
        sb.append("state: ");
        sb.append(state);
        Log.i(str, sb.toString());
    }

    public void onClick(View view) {
        String str = TAG;
        Log.i(str, "FWCOMPARE - in onC");
        StringBuilder sb = new StringBuilder();
        String str2 = "FWCOMPARE - firmwareRev: ";
        sb.append(str2);
        sb.append(this.firmwareRev);
        Log.i(str, sb.toString());
        String str3 = "start OTA upgrade with IP address: ";
        String str4 = "FWCOMPARE - OTA task failed dialog - cancelPreviousAndStartNewTimer/onClick";
        if (this.firmwareRev != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(this.firmwareRev);
            Log.i(str, sb2.toString());
            Log.i(str, "FWCOMPARE - LATEST_FW: 1.003 Feb 22 2017 10:55:45");
            String substring = this.firmwareRev.substring(0, 6);
            String str5 = "1.003 ";
            StringBuilder sb3 = new StringBuilder();
            sb3.append("FWCOMPARE - currFWRev: ");
            sb3.append(substring);
            Log.i(str, sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("FWCOMPARE - latFWRev: ");
            sb4.append(str5);
            Log.i(str, sb4.toString());
            if (substring.equals(str5)) {
                Builder builder = new Builder(this.mThis);
                builder.setTitle((CharSequence) "Firmware is already up-to-date");
                builder.setMessage((CharSequence) "SensorTag firmware is already up-to-date, no upgrade required.");
                builder.setCancelable(false);
                builder.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(wifiOTAActivity.this.mThis, TopLevel.class);
                        intent.setFlags(67108864);
                        wifiOTAActivity.this.startActivity(intent);
                    }
                });
                builder.create().show();
                return;
            }
            this.server.cancelPreviousAndStartNewTimer();
            Log.i(str, str4);
            wifiDeviceActivity.startOTAUpgrade(this, this.ipAddress, this.server.ourIp);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str3);
            sb5.append(this.ipAddress);
            Log.i(str, sb5.toString());
            return;
        }
        this.server.cancelPreviousAndStartNewTimer();
        Log.i(str, "OTA task failed dialog - cancelPreviousAndStartNewTimer/onClick");
        Log.i(str, str4);
        wifiDeviceActivity.startOTAUpgrade(this, this.ipAddress, this.server.ourIp);
        StringBuilder sb6 = new StringBuilder();
        sb6.append(str3);
        sb6.append(this.ipAddress);
        Log.i(str, sb6.toString());
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TopLevel.class);
        intent.setFlags(67108864);
        startActivity(intent);
    }
}
