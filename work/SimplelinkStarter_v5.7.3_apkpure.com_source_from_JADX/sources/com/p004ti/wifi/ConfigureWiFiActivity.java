package com.p004ti.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import com.p004ti.device_selector.TopLevel;
import com.ti.ble.simplelinkstarter.R;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.ti.wifi.ConfigureWiFiActivity */
public class ConfigureWiFiActivity extends AppCompatActivity implements OnClickListener {
    private static final String CONFIGURE_WIFI_ACTIVITY_CELL_CLICKED_ACTION = "ConfigureWiFiActivity.CELL_CLICKED_ACTION";
    private static final String CONFIGURE_WIFI_ACTIVITY_CELL_SSID_EXTRA = "ConfigureWiFiActivity.CELL_SSID_EXTRA";
    private static final String TAG = "ConfigureWiFiActivity";

    /* renamed from: bR */
    private BroadcastReceiver f75bR;
    /* access modifiers changed from: private */

    /* renamed from: dF */
    public WiFiDeviceScannerDialogFragment f76dF;
    TableLayout deviceTable;
    /* access modifiers changed from: private */
    public ConfigureWiFiActivity mThis;
    private String oldSSID;
    Button openWifiButton;
    /* access modifiers changed from: private */
    public ProgressBar searchProgress;
    TimerTask searchTimeoutTimerTask = new TimerTask() {
        public void run() {
            if (ConfigureWiFiActivity.this.wifiScannerThread != null) {
                ConfigureWiFiActivity.this.wifiScannerThread.interrupt();
            }
            ConfigureWiFiActivity.this.shouldStopScanning = true;
            StringBuilder sb = new StringBuilder();
            sb.append("searchTimeoutTimerTask  - shouldStopScanning: ");
            sb.append(ConfigureWiFiActivity.this.shouldStopScanning);
            Log.d(ConfigureWiFiActivity.TAG, sb.toString());
            try {
                ConfigureWiFiActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Builder positiveButton = new Builder(ConfigureWiFiActivity.this.mThis).setCancelable(false).setTitle((CharSequence) ConfigureWiFiActivity.this.getResources().getString(R.string.scan_timeout_dialog_title)).setMessage((CharSequence) ConfigureWiFiActivity.this.getResources().getString(R.string.scan_timeout_dialog_message)).setPositiveButton((CharSequence) ConfigureWiFiActivity.this.getResources().getString(R.string.ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ConfigureWiFiActivity.this.mThis, TopLevel.class);
                                intent.setFlags(67108864);
                                ConfigureWiFiActivity.this.startActivity(intent);
                            }
                        });
                        ConfigureWiFiActivity.this.searchProgress.setVisibility(8);
                        positiveButton.create().show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean shouldStopScanning = false;
    /* access modifiers changed from: private */
    public Timer timer;
    /* access modifiers changed from: private */
    public Thread wifiScannerThread;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_configure_wifi);
        setTitle(getResources().getString(R.string.configure_a_new_wifi_device));
        this.searchProgress = (ProgressBar) findViewById(R.id.acw_search_progress);
        this.searchProgress.setVisibility(0);
        this.searchProgress.setIndeterminate(true);
        try {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.mThis = this;
        this.timer = new Timer();
    }

    public void onResume() {
        super.onResume();
        this.f75bR = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String str = "\"";
                String stringExtra = intent.getStringExtra(ConfigureWiFiActivity.CONFIGURE_WIFI_ACTIVITY_CELL_SSID_EXTRA);
                WifiManager wifiManager = (WifiManager) ConfigureWiFiActivity.this.mThis.getApplicationContext().getSystemService("wifi");
                try {
                    WifiConfiguration wifiConfiguration = new WifiConfiguration();
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(stringExtra);
                    sb.append(str);
                    wifiConfiguration.SSID = sb.toString();
                    wifiConfiguration.allowedKeyManagement.set(0);
                    int addNetwork = wifiManager.addNetwork(wifiConfiguration);
                    wifiManager.saveConfiguration();
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(addNetwork, true);
                    wifiManager.reconnect();
                    if (ConfigureWiFiActivity.this.wifiScannerThread != null) {
                        ConfigureWiFiActivity.this.wifiScannerThread.interrupt();
                    }
                    ConfigureWiFiActivity.this.f76dF.dismiss();
                    ConfigureWiFiActivity.this.mThis.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(this.f75bR, new IntentFilter(CONFIGURE_WIFI_ACTIVITY_CELL_CLICKED_ACTION));
    }

    public void onPause() {
        super.onPause();
        Thread thread = this.wifiScannerThread;
        if (thread != null) {
            thread.interrupt();
        }
        unregisterReceiver(this.f75bR);
    }

    public void onDestroy() {
        super.onDestroy();
        this.shouldStopScanning = true;
        StringBuilder sb = new StringBuilder();
        sb.append("searchTimeoutTimerTask / onDestroy - shouldStopScanning: ");
        sb.append(this.shouldStopScanning);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        this.timer.cancel();
        Log.d(str, "searchTimeoutTimerTask / onDestroy - Stopped WiFi Scanner- cancel timeout timer");
    }

    public void onClick(View view) {
        startActivity(new Intent("android.settings.WIFI_SETTINGS"));
    }

    public void onWindowFocusChanged(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Focus changed ");
        sb.append(z);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (z) {
            WifiInfo connectionInfo = ((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("We're on SSID : ");
            sb3.append(connectionInfo.getSSID());
            Log.d(str, sb3.toString());
            String ssid = connectionInfo.getSSID();
            if (ssid == null) {
                this.oldSSID = ssid;
            } else if (TopLevel.isSensorTagDevice(ssid)) {
                Log.d(str, "Continue process, we are on sensortag wifi");
            }
            startScanner();
            Log.d(str, "Started WiFi scanner");
            try {
                this.timer.schedule(this.searchTimeoutTimerTask, 20000);
                Log.d(str, "searchTimeoutTimerTask / Started WiFi scanner - schedule timeout timer");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startScanner() {
        C09732 r0 = new Runnable() {
            public void run() {
                String str;
                ScanResult scanResult;
                WifiManager wifiManager = (WifiManager) ConfigureWiFiActivity.this.mThis.getApplicationContext().getSystemService("wifi");
                try {
                    Thread.sleep(1000);
                    wifiManager.startScan();
                    loop0:
                    while (true) {
                        boolean access$300 = ConfigureWiFiActivity.this.shouldStopScanning;
                        str = ConfigureWiFiActivity.TAG;
                        if (!access$300) {
                            Iterator it = wifiManager.getScanResults().iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    scanResult = (ScanResult) it.next();
                                    if (scanResult == null || (!scanResult.SSID.contains(TopLevel.Sensor_Tag) && !scanResult.SSID.contains(TopLevel.sensor_tag))) {
                                    }
                                }
                            }
                        } else {
                            Log.d(str, "Stopped WiFi Scanner !");
                            ConfigureWiFiActivity.this.timer.cancel();
                            Log.d(str, "searchTimeoutTimerTask / startScanner -  Stopped WiFi Scanner- cancel timeout timer");
                            return;
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Found Sensortag wifi : ");
                    sb.append(scanResult.SSID);
                    Log.d(str, sb.toString());
                    ConfigureWiFiActivity.this.timer.cancel();
                    Log.d(str, "searchTimeoutTimerTask / Found Sensortag wifi - cancel timeout timer");
                    ConfigureWiFiActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ConfigureWiFiActivity.this.startActivity(new Intent(ConfigureWiFiActivity.this.mThis, ConfigureWifiActivityStageTwo.class));
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = this.wifiScannerThread;
        if (thread == null || !thread.isAlive()) {
            this.wifiScannerThread = new Thread(r0);
            this.wifiScannerThread.start();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.timer.cancel();
        Log.d(TAG, "searchTimeoutTimerTask / onBackPressed - cancel timeout timer");
        Intent intent = new Intent(this, TopLevel.class);
        intent.setFlags(67108864);
        startActivity(intent);
    }
}
