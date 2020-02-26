package com.p004ti.wifi;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.device_selector.DeviceTableRow;
import com.p004ti.device_selector.TopLevel;
import com.ti.ble.simplelinkstarter.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.jmdns.impl.constants.DNSConstants;

/* renamed from: com.ti.wifi.ConfigureWifiActivityStageTwo */
public class ConfigureWifiActivityStageTwo extends AppCompatActivity implements OnClickListener {
    private static final String CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_ACTION = "com.ti.WiFi.ConfigureWifiActivityStageTwo.NEW_DEVICE_ACTION";
    private static final String CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_BSSID_EXTRA = "com.ti.WiFi.ConfigureWifiActivityStageTwo.NEW_DEVICE_BSSID_EXTRA";
    private static final String CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_SSID_EXTRA = "com.ti.WiFi.ConfigureWifiActivityStageTwo.NEW_DEVICE_SSID_EXTRA";
    private static final int MAX_CONNECTION_ATTEMPTS = 2;
    private static final String TAG = "ActivityStageTwo";
    /* access modifiers changed from: private */
    public static int connectionAttemptsCounter;
    /* access modifiers changed from: private */
    public boolean conFailedDialogHasBeenShown = true;
    /* access modifiers changed from: private */
    public ArrayList<wifiDevice> deviceList;
    private Runnable deviceSetWLANRunnable = new Runnable() {
        public void run() {
            String str = ConfigureWifiActivityStageTwo.TAG;
            Log.d(str, "deviceSetWLANRunnable / called");
            ConfigureWifiActivityStageTwo.access$1008();
            StringBuilder sb = new StringBuilder();
            sb.append("deviceSetWLANRunnable / connectionAttemptsCounter: ");
            sb.append(ConfigureWifiActivityStageTwo.connectionAttemptsCounter);
            Log.d(str, sb.toString());
            ConfigureWifiActivityStageTwo.this.received = false;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("deviceSetWLANRunnable / received: ");
            sb2.append(ConfigureWifiActivityStageTwo.this.received);
            Log.d(str, sb2.toString());
            wifiDeviceActivity.deviceSetWLAN((Context) ConfigureWifiActivityStageTwo.this.mThis, ConfigureWifiActivityStageTwo.this.selectedSSID);
            Log.d(str, "deviceSetWLANRunnable / deviceSetWlan called");
            ConfigureWifiActivityStageTwo.this.f80t = new Timer();
            ConfigureWifiActivityStageTwo.this.f80t.schedule(new TimerTask() {
                public void run() {
                    String str = ConfigureWifiActivityStageTwo.TAG;
                    Log.d(str, "timerTask - connection to ST timeout / called");
                    StringBuilder sb = new StringBuilder();
                    sb.append("timerTask / received: ");
                    sb.append(ConfigureWifiActivityStageTwo.this.received);
                    Log.d(str, sb.toString());
                    if (ConfigureWifiActivityStageTwo.this.received) {
                        Log.d(str, "timerTask / connectivity changed broadcast receiver already called- return");
                        return;
                    }
                    Log.d(str, "timerTask / connectivity changed broadcast receiver was not called - continue");
                    Log.d(str, "timerTask / check current connection");
                    String currentConnectionSSID = wifiDeviceActivity.getCurrentConnectionSSID(ConfigureWifiActivityStageTwo.this.mThis);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("timerTask /connected to: ");
                    sb2.append(currentConnectionSSID);
                    Log.d(str, sb2.toString());
                    if (wifiDeviceActivity.deviceOnCorrectWLAN(ConfigureWifiActivityStageTwo.this.mThis, ConfigureWifiActivityStageTwo.this.selectedSSID)) {
                        Log.d(str, "timerTask / connected to selected SensorTag device - moving to next stage");
                        ConfigureWifiActivityStageTwo.this.runOnUiThread(ConfigureWifiActivityStageTwo.this.successfullyConnectedToSelectedST);
                    } else {
                        Log.d(str, "timerTask /  not connected to selected SensorTag device");
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("timerTask / connectionAttemptsCounter: ");
                        sb3.append(ConfigureWifiActivityStageTwo.connectionAttemptsCounter);
                        Log.d(str, sb3.toString());
                        if (ConfigureWifiActivityStageTwo.connectionAttemptsCounter >= 2) {
                            Log.d(str, "timerTask / five or more connection attempts already committed - fail");
                            ConfigureWifiActivityStageTwo.this.connectionFailedAfterFiveAttempts();
                        } else {
                            Log.d(str, "timerTask / less than five connection attempts committed - try again");
                            ConfigureWifiActivityStageTwo.this.callDeviceSetWLANOnSeparateThread();
                        }
                    }
                }
            }, 20000);
        }
    };
    /* access modifiers changed from: private */
    public TableLayout deviceTable;
    /* access modifiers changed from: private */
    public ConfigureWifiActivityStageTwo mThis = this;
    /* access modifiers changed from: private */
    public ProgressDialog progress;
    /* access modifiers changed from: private */
    public boolean received;
    private BroadcastReceiver receiver;
    private Timer refreshDevicesListTimer;
    private TimerTask refreshDevicesListTimerTask = new TimerTask() {
        public void run() {
            String str = "AGE- index: ";
            ConfigureWifiActivityStageTwo.this.updateDeviceList = new ArrayList();
            int i = 0;
            while (true) {
                int size = ConfigureWifiActivityStageTwo.this.deviceList.size();
                String str2 = ConfigureWifiActivityStageTwo.TAG;
                if (i < size) {
                    try {
                        wifiDevice wifidevice = (wifiDevice) ConfigureWifiActivityStageTwo.this.deviceList.get(i);
                        int i2 = wifidevice.age;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(i);
                        sb.append(" / old age: ");
                        sb.append(i2);
                        Log.i(str2, sb.toString());
                        if (i2 < 2) {
                            wifidevice.age = i2 + 1;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str);
                            sb2.append(i);
                            sb2.append(" / new age: ");
                            sb2.append(wifidevice.age);
                            Log.i(str2, sb2.toString());
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("AGE- Add item to refreshed list:");
                            sb3.append(wifidevice.toString());
                            Log.i(str2, sb3.toString());
                            ConfigureWifiActivityStageTwo.this.updateDeviceList.add(wifidevice);
                        } else {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("AGE- Should remove item:");
                            sb4.append(i);
                            Log.i(str2, sb4.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                } else {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("AGE- update Devices Array size:");
                    sb5.append(ConfigureWifiActivityStageTwo.this.updateDeviceList.size());
                    Log.i(str2, sb5.toString());
                    ConfigureWifiActivityStageTwo.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ConfigureWifiActivityStageTwo.this.deviceList = ConfigureWifiActivityStageTwo.this.updateDeviceList;
                            String str = ConfigureWifiActivityStageTwo.TAG;
                            Log.i(str, "AGE- update UI with refreshed devices list");
                            ConfigureWifiActivityStageTwo.this.deviceTable.removeAllViews();
                            Iterator it = ConfigureWifiActivityStageTwo.this.deviceList.iterator();
                            while (it.hasNext()) {
                                wifiDevice wifidevice = (wifiDevice) it.next();
                                StringBuilder sb = new StringBuilder();
                                sb.append("AGE- add device to UI: ");
                                sb.append(wifidevice.printData());
                                Log.i(str, sb.toString());
                                DeviceTableRow deviceTableRow = new DeviceTableRow(ConfigureWifiActivityStageTwo.this.mThis);
                                ((TextView) deviceTableRow.findViewById(R.id.device_table_row_name)).setText(wifidevice.mSSID);
                                ((TextView) deviceTableRow.findViewById(R.id.device_table_row_bt_addr)).setText(wifidevice.mBSSID);
                                ConfigureWifiActivityStageTwo.this.deviceTable.addView(deviceTableRow);
                                ((ImageView) deviceTableRow.findViewById(R.id.device_table_row_rssi_level)).setVisibility(4);
                                deviceTableRow.getChildAt(0).setOnClickListener(ConfigureWifiActivityStageTwo.this.mThis);
                            }
                        }
                    });
                    return;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Thread scannerThread;
    /* access modifiers changed from: private */
    public String selectedSSID;
    /* access modifiers changed from: private */
    public boolean shouldStopScanning = false;
    /* access modifiers changed from: private */
    public ProgressBar stageTwoProgressBar;
    /* access modifiers changed from: private */
    public Runnable successfullyConnectedToSelectedST = new Runnable() {
        public void run() {
            String str = ConfigureWifiActivityStageTwo.TAG;
            Log.d(str, "successfullyConnectedToSelectedST / called");
            Log.d(str, "successfullyConnectedToSelectedST / Connected to correct WiFi, dismissing progress dialog");
            try {
                ConfigureWifiActivityStageTwo.this.progress.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String str2 = "com.ti.ble.sensortag.ConfigureWiFiStageThree.extra.SSID";
            if (PreferenceManager.getDefaultSharedPreferences(ConfigureWifiActivityStageTwo.this.getApplicationContext()).getBoolean("pref_apmodeprovisioning", Boolean.valueOf(true).booleanValue())) {
                Log.d(str, "successfullyConnectedToSelectedST / starting ConfigureWiFiActivityStageThreeAPModeProvisioning");
                Intent intent = new Intent(ConfigureWifiActivityStageTwo.this.mThis, ConfigureWiFiActivityStageThreeAPModeProvisioning.class);
                intent.putExtra(str2, ConfigureWifiActivityStageTwo.this.selectedSSID);
                ConfigureWifiActivityStageTwo.this.startActivity(intent);
                ConfigureWifiActivityStageTwo.this.scannerThread.interrupt();
                return;
            }
            Log.d(str, "successfullyConnectedToSelectedST / starting ConfigureWiFiActivityStageThreeNoProvisioning");
            Intent intent2 = new Intent(ConfigureWifiActivityStageTwo.this.mThis, ConfigureWiFiActivityStageThreeNoProvisioning.class);
            intent2.putExtra(str2, ConfigureWifiActivityStageTwo.this.selectedSSID);
            ConfigureWifiActivityStageTwo.this.startActivity(intent2);
            ConfigureWifiActivityStageTwo.this.scannerThread.interrupt();
        }
    };
    /* access modifiers changed from: private */

    /* renamed from: t */
    public Timer f80t;
    /* access modifiers changed from: private */
    public ArrayList<wifiDevice> updateDeviceList;

    static /* synthetic */ int access$1008() {
        int i = connectionAttemptsCounter;
        connectionAttemptsCounter = i + 1;
        return i;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_configure_wifi_stage_2);
        setTitle("Found WiFi devices");
        this.deviceList = new ArrayList<>();
        this.deviceTable = (TableLayout) findViewById(R.id.acwstwo_deviceTable);
        this.stageTwoProgressBar = (ProgressBar) findViewById(R.id.stage_two_progress_bar);
        try {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.refreshDevicesListTimer = new Timer();
        this.refreshDevicesListTimer.scheduleAtFixedRate(this.refreshDevicesListTimerTask, 0, DNSConstants.CLOSE_TIMEOUT);
        ProgressBar progressBar = this.stageTwoProgressBar;
        if (progressBar != null && progressBar.getVisibility() != 0) {
            this.stageTwoProgressBar.setVisibility(0);
            this.stageTwoProgressBar.setIndeterminate(true);
        }
    }

    public void onResume() {
        super.onResume();
        this.receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String str = "receiver/onReceive - received: ";
                String str2 = ConfigureWifiActivityStageTwo.TAG;
                Log.d(str2, "receiver/onReceive - called");
                StringBuilder sb = new StringBuilder();
                sb.append("receiver/onReceive - action: ");
                sb.append(intent.getAction());
                Log.d(str2, sb.toString());
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    Log.d(str2, "receiver/onReceive - CONNECTIVITY_ACTION called");
                    try {
                        Log.d(str2, "receiver/onReceive - Connectivity changed !");
                        Log.d(str2, "receiver/onReceive - Check current connection");
                        String currentConnectionSSID = wifiDeviceActivity.getCurrentConnectionSSID(ConfigureWifiActivityStageTwo.this.mThis);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("receiver/onReceive - connected to: ");
                        sb2.append(currentConnectionSSID);
                        Log.d(str2, sb2.toString());
                        if (wifiDeviceActivity.deviceOnCorrectWLAN(ConfigureWifiActivityStageTwo.this.mThis, ConfigureWifiActivityStageTwo.this.selectedSSID)) {
                            if (ConfigureWifiActivityStageTwo.this.f80t != null) {
                                ConfigureWifiActivityStageTwo.this.f80t.cancel();
                                Log.d(str2, "receiver/onReceive - cancel timer");
                            }
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append(ConfigureWifiActivityStageTwo.this.received);
                            Log.d(str2, sb3.toString());
                            ConfigureWifiActivityStageTwo.this.received = true;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str);
                            sb4.append(ConfigureWifiActivityStageTwo.this.received);
                            Log.d(str2, sb4.toString());
                            Log.d(str2, "receiver/onReceive - Connected to selected SensorTag device");
                            Log.d(str2, "receiver/onReceive - Moving to next stage");
                            ConfigureWifiActivityStageTwo.this.runOnUiThread(ConfigureWifiActivityStageTwo.this.successfullyConnectedToSelectedST);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (intent.getAction().equals(ConfigureWifiActivityStageTwo.CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_ACTION)) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Found new device : ");
                    String str3 = ConfigureWifiActivityStageTwo.CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_SSID_EXTRA;
                    sb5.append(intent.getStringExtra(str3));
                    sb5.append("(");
                    String str4 = ConfigureWifiActivityStageTwo.CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_BSSID_EXTRA;
                    sb5.append(intent.getStringExtra(str4));
                    sb5.append(")");
                    Log.d(str2, sb5.toString());
                    boolean z = false;
                    for (int i = 0; i < ConfigureWifiActivityStageTwo.this.deviceList.size(); i++) {
                        wifiDevice wifidevice = (wifiDevice) ConfigureWifiActivityStageTwo.this.deviceList.get(i);
                        if (wifidevice.mSSID.equals(intent.getStringExtra(str3))) {
                            wifidevice.age = 0;
                            z = true;
                        }
                    }
                    if (!z) {
                        wifiDevice wifidevice2 = new wifiDevice();
                        wifidevice2.mSSID = intent.getStringExtra(str3);
                        wifidevice2.mBSSID = intent.getStringExtra(str4);
                        wifidevice2.age = 0;
                        ConfigureWifiActivityStageTwo.this.deviceList.add(wifidevice2);
                        DeviceTableRow deviceTableRow = new DeviceTableRow(ConfigureWifiActivityStageTwo.this.mThis);
                        ((TextView) deviceTableRow.findViewById(R.id.device_table_row_name)).setText(wifidevice2.mSSID);
                        ((TextView) deviceTableRow.findViewById(R.id.device_table_row_bt_addr)).setText(wifidevice2.mBSSID);
                        if (ConfigureWifiActivityStageTwo.this.stageTwoProgressBar != null && ConfigureWifiActivityStageTwo.this.stageTwoProgressBar.getVisibility() == 0) {
                            ConfigureWifiActivityStageTwo.this.stageTwoProgressBar.setVisibility(8);
                        }
                        ConfigureWifiActivityStageTwo.this.deviceTable.addView(deviceTableRow);
                        ((ImageView) deviceTableRow.findViewById(R.id.device_table_row_rssi_level)).setVisibility(4);
                        deviceTableRow.getChildAt(0).setOnClickListener(ConfigureWifiActivityStageTwo.this.mThis);
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_ACTION);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.receiver, intentFilter);
        startScanner();
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(this.receiver);
    }

    /* access modifiers changed from: private */
    public void startScanner() {
        C10182 r0 = new Runnable() {
            public void run() {
                WifiManager wifiManager = (WifiManager) ConfigureWifiActivityStageTwo.this.mThis.getApplicationContext().getSystemService("wifi");
                try {
                    Thread.sleep(1000);
                    while (!ConfigureWifiActivityStageTwo.this.shouldStopScanning) {
                        wifiManager.startScan();
                        for (ScanResult scanResult : wifiManager.getScanResults()) {
                            if (scanResult != null && (scanResult.SSID.contains(TopLevel.Sensor_Tag) || scanResult.SSID.contains(TopLevel.sensor_tag))) {
                                Intent intent = new Intent(ConfigureWifiActivityStageTwo.CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_ACTION);
                                intent.putExtra(ConfigureWifiActivityStageTwo.CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_SSID_EXTRA, scanResult.SSID);
                                intent.putExtra(ConfigureWifiActivityStageTwo.CONFIGURE_WIFI_ACTIVITY_FOUND_NEW_DEVICE_BSSID_EXTRA, scanResult.BSSID);
                                ConfigureWifiActivityStageTwo.this.mThis.sendBroadcast(intent);
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(ConfigureWifiActivityStageTwo.TAG, "Stopped WIFI Scanner !");
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        };
        Thread thread = this.scannerThread;
        if (thread == null || !thread.isAlive()) {
            this.scannerThread = new Thread(r0);
            this.scannerThread.start();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ConfigureWiFiActivity.class);
        intent.setFlags(67108864);
        startActivity(intent);
    }

    public void onClick(View view) {
        TextView textView = (TextView) ((TableRow) view).findViewById(R.id.device_table_row_name);
        this.shouldStopScanning = true;
        StringBuilder sb = new StringBuilder();
        sb.append("Cell clicked has SSID :");
        sb.append(textView.getText());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        try {
            this.selectedSSID = textView.getText().toString();
            callDeviceSetWLANOnSeparateThread();
            this.progress = new ProgressDialog(this.mThis);
            ProgressDialog progressDialog = this.progress;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Connecting to ");
            sb3.append(textView.getText());
            progressDialog.setTitle(sb3.toString());
            ProgressDialog progressDialog2 = this.progress;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Connecting to access point ");
            sb4.append(textView.getText());
            progressDialog2.setMessage(sb4.toString());
            this.progress.setIndeterminate(true);
            this.progress.setCancelable(false);
            runOnUiThread(new Runnable() {
                public void run() {
                    ConfigureWifiActivityStageTwo.this.progress.show();
                }
            });
            connectionAttemptsCounter = 0;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("onClick/ connectionAttemptsCounter: ");
            sb5.append(connectionAttemptsCounter);
            Log.d(str, sb5.toString());
            this.conFailedDialogHasBeenShown = false;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("onClick/ conFailedDialogHasBeenShown: ");
            sb6.append(this.conFailedDialogHasBeenShown);
            Log.i(str, sb6.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void callDeviceSetWLANOnSeparateThread() {
        Log.d(TAG, "callDeviceSetWLANOnSeparateThread / called");
        new Thread(this.deviceSetWLANRunnable).start();
    }

    /* access modifiers changed from: private */
    public void connectionFailedAfterFiveAttempts() {
        runOnUiThread(new Runnable() {
            public void run() {
                String str = ConfigureWifiActivityStageTwo.TAG;
                Log.d(str, "connectionFailedAfterFiveAttempts / called");
                Log.d(str, "connectionFailedAfterFiveAttempts / dismiss progress dialog");
                try {
                    ConfigureWifiActivityStageTwo.this.progress.dismiss();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Log.d(str, "connectionFailedAfterFiveAttempts / re-start scanner");
                ConfigureWifiActivityStageTwo.this.startScanner();
                Builder builder = new Builder(ConfigureWifiActivityStageTwo.this.mThis);
                builder.setTitle("Connection To SensorTag Device Failed");
                builder.setMessage("Please restart the SensorTag device,\nverify the correct device has been chosen from the list, and try again.");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ConfigureWifiActivityStageTwo.this.startActivity(new Intent(ConfigureWifiActivityStageTwo.this.mThis, ConfigureWiFiActivity.class));
                    }
                });
                AlertDialog create = builder.create();
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("connectionFailedAfterFiveAttempts / Connection failed dialog has been shown already: ");
                    sb.append(ConfigureWifiActivityStageTwo.this.conFailedDialogHasBeenShown);
                    Log.i(str, sb.toString());
                    if (!ConfigureWifiActivityStageTwo.this.conFailedDialogHasBeenShown) {
                        Log.i(str, "connectionFailedAfterFiveAttempts / Connection failed dialog has not been shown already - show");
                        create.show();
                        Log.d(str, "connectionFailedAfterFiveAttempts / showing dialog");
                        ConfigureWifiActivityStageTwo.this.conFailedDialogHasBeenShown = true;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("connectionFailedAfterFiveAttempts / conFailedDialogHasBeenShown: ");
                        sb2.append(ConfigureWifiActivityStageTwo.this.conFailedDialogHasBeenShown);
                        Log.i(str, sb2.toString());
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (this.f80t != null) {
            Log.i(TAG, "onStop / t cancel");
            this.f80t.cancel();
            this.f80t = null;
        }
        Timer timer = this.refreshDevicesListTimer;
        if (timer != null) {
            timer.cancel();
            this.refreshDevicesListTimer = null;
        }
    }
}
