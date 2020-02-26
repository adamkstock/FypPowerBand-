package com.p004ti.device_selector;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.le.ScanRecord;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice.BluetoothLEDeviceCB;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEManager;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEManager.BluetoothLEManagerCB;
import com.p004ti.ble.common.BluetoothGATTDefines;
import com.p004ti.ble.common.BroadcastActivity;
import com.p004ti.device_selector.filtering.BTDeviceFilterGlobal;
import com.p004ti.device_selector.filtering.FilterConfigurationActivity;
import com.p004ti.util.bleUtility;
import com.p004ti.wifi.ConfigureWiFiDialogFragment;
import com.p004ti.wifi.ConfigureWifiActivityStageTwo;
import com.p004ti.wifi.WiFiConfigurationTableRow;
import com.p004ti.wifi.utils.DidShowMobileDataDialogSingleton;
import com.p004ti.wifi.utils.MDnsCallbackInterface;
import com.p004ti.wifi.utils.MDnsHelper;
import com.p004ti.wifi.utils.NetInfo;
import com.p004ti.wifi.utils.NetworkUtil;
import com.p004ti.wifi.utils.Ping;
import com.p004ti.wifi.utils.Ping.PingCallback;
import com.p004ti.wifi.utils.UdpBcastServer;
import com.p004ti.wifi.wifiDevice;
import com.p004ti.wifi.wifiDeviceActivity;
import com.p004ti.wifi.wifiOTAActivity;
import com.p004ti.wifi.wlanController;
import com.ti.ble.simplelinkstarter.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jmdns.impl.constants.DNSConstants;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.ti.device_selector.TopLevel */
public class TopLevel extends AppCompatActivity implements OnClickListener {
    public static final String BROADCAST_DEVICE_ADDR = "TopLevel.BROADCAST_DEVICE_ADDR";
    public static final String CONFIG_TO_CONNECT_TO_EXTRA = "configToConnectTo";
    public static final String CONNECT_TO_CONFIG_EXTRA = "connectToConfig";
    public static final String DEVICE_FOUND_BROADCAST_ACTION = "TopLevel.DEVICE_FOUND_BROADCAST_ACTION";
    public static final String DEVICE_FOUND_ON_SENSORTAG_WIFI_ACTION = "TopLevel.DEVICE_FOUND_ON_SENSORTAG_WIFI_ACTION";
    public static final String DEVICE_NAME_EXTRA = "deviceName";
    public static final String IP_ADDRESS = "192.168.1.1";
    public static final int JMDNS_CLOSE_TIME = 6000;
    public static final int MAIN_SCAN_TIME = 15000;
    private static int MOBILE = 2;
    private static int NOT_CONNECTED = 0;
    public static final String PRE_DO_NOT_SHOW_PREF = "mobile_data_dialog_do_not_show_again";
    private static final int REQUEST_CODE_ENABLE_BT_REQ = 0;
    private static final int REQ_DEVICE_ACT = 1;
    private static final int REQ_ENABLE_BT = 0;
    private static String SCAN_FINISHED_BROADCAST_ACTION = "com.ti.device_selector.TopLevel.SCAN_FINISHED";
    public static final String Sensor_Tag = "SensorTag";
    public static final String Sensor_tag = "Sensortag";
    private static final String TAG = "TopLevel";
    private static int WIFI = 1;
    public static String initialConnectionSSID = null;
    public static String initialConnectionSecurityType = null;
    public static final String sensor_tag = "sensortag";
    /* access modifiers changed from: private */
    public GoogleApiClient client;
    private boolean comingFromBluetoothRestart = false;
    /* access modifiers changed from: private */
    public ProgressDialog connectPD;
    public BluetoothLEDeviceCB deviceCB = new BluetoothLEDeviceCB() {
        public void deviceDidDisconnect(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceFailed(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void didReadCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void didReadDescriptor(BluetoothLEDevice bluetoothLEDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
        }

        public void didReadRSSI(int i) {
        }

        public void didUpdateCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void didUpdateCharacteristicIndication(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void didUpdateCharacteristicNotification(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void didWriteCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void mtuValueChanged(int i) {
        }

        public void waitingForConnect(final BluetoothLEDevice bluetoothLEDevice, final int i, final int i2) {
            TopLevel.this.runOnUiThread(new Runnable() {
                public void run() {
                    TopLevel.this.connectPD.setTitle("Connecting ... ");
                    ProgressDialog access$1200 = TopLevel.this.connectPD;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Device: ");
                    sb.append(bluetoothLEDevice.f27d.getName());
                    sb.append(" (");
                    sb.append(bluetoothLEDevice.f27d.getAddress());
                    sb.append(") \nTimeout left: ");
                    sb.append((float) (i / 1000));
                    sb.append(" sec\nRetry: ");
                    sb.append(i2);
                    sb.append(" of ");
                    sb.append(4);
                    access$1200.setMessage(sb.toString());
                }
            });
        }

        public void waitingForDiscovery(final BluetoothLEDevice bluetoothLEDevice, final int i, final int i2) {
            TopLevel.this.runOnUiThread(new Runnable() {
                public void run() {
                    TopLevel.this.connectPD.setTitle("Discovering services ... ");
                    ProgressDialog access$1200 = TopLevel.this.connectPD;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Device: ");
                    sb.append(bluetoothLEDevice.f27d.getName());
                    sb.append(" (");
                    sb.append(bluetoothLEDevice.f27d.getAddress());
                    sb.append(") \nTimeout left: ");
                    sb.append((float) (i / 1000));
                    sb.append(" sec\nRetry: ");
                    sb.append(i2);
                    sb.append(" of ");
                    sb.append(4);
                    access$1200.setMessage(sb.toString());
                }
            });
        }

        public void deviceReady(BluetoothLEDevice bluetoothLEDevice) {
            TopLevel.this.connectPD.dismiss();
            TopLevel topLevel = TopLevel.this;
            topLevel.mDeviceIntent = new Intent(topLevel.mThis, DeviceActivity.class);
            TopLevel.this.mDeviceIntent.putExtra(DeviceActivity.EXTRA_DEVICE, TopLevel.this.mBluetoothDevice);
            TopLevel topLevel2 = TopLevel.this;
            topLevel2.startActivityForResult(topLevel2.mDeviceIntent, 1);
        }

        public void deviceConnectTimedOut(BluetoothLEDevice bluetoothLEDevice) {
            TopLevel.this.runOnUiThread(new Runnable() {
                public void run() {
                    TopLevel.this.connectPD.setTitle("Connection timed out");
                    TopLevel.this.connectPD.setMessage("Tried 5 connection attempts to device, check that the device is advertising ...");
                    TopLevel.this.connectPD.setCancelable(true);
                }
            });
        }

        public void deviceDiscoveryTimedOut(BluetoothLEDevice bluetoothLEDevice) {
            TopLevel.this.runOnUiThread(new Runnable() {
                public void run() {
                    TopLevel.this.connectPD.setTitle("Service discovery timed out");
                    TopLevel.this.connectPD.setMessage("Tried 5 service discovery attempts to device, check that the device is advertising ...");
                    TopLevel.this.connectPD.setCancelable(true);
                }
            });
        }
    };
    private DidShowMobileDataDialogSingleton didShowMobileDataDialogSingleton;
    /* access modifiers changed from: private */
    public boolean doubleBackToExitPressedOnce;
    private ImageView filterIcon;
    /* access modifiers changed from: private */
    public TextView filterStatus;
    private boolean firstTime = true;
    private boolean focusFromAlertDialog = false;
    private boolean isLatestWiFiFirmware = false;
    private boolean isScanning;
    private boolean killMDNSScanWhenInBackground;
    public BluetoothLEManager leManager;
    /* access modifiers changed from: private */
    public BluetoothDevice mBluetoothDevice;
    private BluetoothAdapter mBtAdapter;
    private MDnsCallbackInterface mDNSCallback;
    /* access modifiers changed from: private */
    public Intent mDeviceIntent;
    private TableLayout mDeviceTable;
    /* access modifiers changed from: private */
    public ArrayList<BluetoothLEDevice> mDevices;
    private MDnsHelper mDnsHelper;
    private IntentFilter mFilter;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public ArrayList<BluetoothLEDevice> mPairedDevices;
    private Ping mPing;
    /* access modifiers changed from: private */
    public PingCallback mPingCallback = new PingCallback() {
        public void pingCompleted() {
            Log.i(TopLevel.TAG, "PingOrUDPBcastCallback - Completed");
        }

        public void pingDeviceFetched(JSONObject jSONObject) {
            String str = TopLevel.TAG;
            Log.i(str, "TopLevel pingDeviceFetched called");
            StringBuilder sb = new StringBuilder();
            sb.append("SensorTag Device was found via PING or UDPBcast: ");
            sb.append(jSONObject);
            Log.i(str, sb.toString());
        }

        public void pingFailed(String str) {
            Log.i(TopLevel.TAG, "PingCallback - pingFailed");
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!"android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                boolean equals = "android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction());
                String str = TopLevel.TAG;
                if (equals) {
                    Log.d(str, "Received connectivity action !");
                    WifiManager wifiManager = (WifiManager) TopLevel.this.getApplicationContext().getSystemService("wifi");
                    StringBuilder sb = new StringBuilder();
                    sb.append("SSID is now : ");
                    sb.append(wifiManager.getConnectionInfo().getSSID());
                    Log.d(str, sb.toString());
                } else {
                    String str2 = "WiFi Devices :";
                    String str3 = "0.0.0.0";
                    String str4 = "newDevice";
                    String str5 = "name";
                    String str6 = "] = ";
                    String str7 = "DuplicatesCheck- mWifiDevices[";
                    String str8 = "host";
                    if (TopLevel.DEVICE_FOUND_BROADCAST_ACTION.equals(action)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Found new device : ");
                        sb2.append(intent.getStringExtra(str4));
                        Log.d(str, sb2.toString());
                        try {
                            JSONObject jSONObject = new JSONObject(intent.getStringExtra(str4));
                            if (!jSONObject.getString(str8).equals(str3)) {
                                Log.d(str, str2);
                                int i = 0;
                                boolean z = false;
                                while (i < TopLevel.this.mWifiDevices.size()) {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(str7);
                                    sb3.append(i);
                                    sb3.append(str6);
                                    sb3.append(TopLevel.this.mWifiDevices.get(i));
                                    Log.d(str, sb3.toString());
                                    boolean z2 = z;
                                    for (int i2 = 0; i2 < TopLevel.this.mWifiDevices.size(); i2++) {
                                        wifiDevice wifidevice = (wifiDevice) TopLevel.this.mWifiDevices.get(i2);
                                        if (wifidevice.mIPAddr.equals(jSONObject.getString(str8))) {
                                            Log.i(str, "DuplicatesCheck- new device is already in devices list - same IP address");
                                            wifidevice.age = 0;
                                            z2 = true;
                                        }
                                    }
                                    i++;
                                    z = z2;
                                }
                                if (!z) {
                                    for (int i3 = 0; i3 < TopLevel.this.mWifiDevices.size(); i3++) {
                                        wifiDevice wifidevice2 = (wifiDevice) TopLevel.this.mWifiDevices.get(i3);
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(str7);
                                        sb4.append(i3);
                                        sb4.append(str6);
                                        sb4.append(wifidevice2.mName);
                                        Log.d(str, sb4.toString());
                                        if (wifidevice2.mName.equals(jSONObject.getString(str5))) {
                                            Log.i(str, "DuplicatesCheck- new device is already in devices list - same name");
                                            wifidevice2.age = 0;
                                            z = true;
                                        }
                                    }
                                }
                                if (!z) {
                                    Log.i(str, "DuplicatesCheck- new device is NOT already in devices list - create new wifiDevice Object and add to list");
                                    wifiDevice wifidevice3 = new wifiDevice();
                                    wifidevice3.mIPAddr = jSONObject.getString(str8);
                                    wifidevice3.mName = jSONObject.getString(str5);
                                    wifidevice3.age = 0;
                                    TopLevel.this.mWifiDevices.add(wifidevice3);
                                    TopLevel.this.mThis.addWifiDeviceToTable(R.mipmap.sensortagwifi, jSONObject.getString(str5), jSONObject.getString(str8), R.mipmap.wifilogo);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (TopLevel.DEVICE_FOUND_BROADCAST_ACTION.equals(action)) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Found new device : ");
                        sb5.append(intent.getStringExtra(str4));
                        Log.d(str, sb5.toString());
                        try {
                            JSONObject jSONObject2 = new JSONObject(intent.getStringExtra(str4));
                            if (!jSONObject2.getString(str8).equals(str3)) {
                                Log.d(str, str2);
                                int i4 = 0;
                                boolean z3 = false;
                                while (i4 < TopLevel.this.mWifiDevices.size()) {
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(str7);
                                    sb6.append(i4);
                                    sb6.append(str6);
                                    sb6.append(TopLevel.this.mWifiDevices.get(i4));
                                    Log.d(str, sb6.toString());
                                    boolean z4 = z3;
                                    for (int i5 = 0; i5 < TopLevel.this.mWifiDevices.size(); i5++) {
                                        wifiDevice wifidevice4 = (wifiDevice) TopLevel.this.mWifiDevices.get(i5);
                                        if (wifidevice4.mIPAddr.equals(jSONObject2.getString(str8))) {
                                            Log.i(str, "DuplicatesCheck- new device is already in devices list - same IP address");
                                            wifidevice4.age = 0;
                                            z4 = true;
                                        }
                                    }
                                    i4++;
                                    z3 = z4;
                                }
                                if (!z3) {
                                    for (int i6 = 0; i6 < TopLevel.this.mWifiDevices.size(); i6++) {
                                        wifiDevice wifidevice5 = (wifiDevice) TopLevel.this.mWifiDevices.get(i6);
                                        StringBuilder sb7 = new StringBuilder();
                                        sb7.append(str7);
                                        sb7.append(i6);
                                        sb7.append(str6);
                                        sb7.append(wifidevice5.mName);
                                        Log.d(str, sb7.toString());
                                        if (wifidevice5.mName.equals(jSONObject2.getString(str5))) {
                                            Log.i(str, "DuplicatesCheck- new device is already in devices list - same name");
                                            wifidevice5.age = 0;
                                            z3 = true;
                                        }
                                    }
                                }
                                if (!z3) {
                                    Log.i(str, "DuplicatesCheck- new device is NOT already in devices list - create new wifiDevice Object and add to list");
                                    wifiDevice wifidevice6 = new wifiDevice();
                                    wifidevice6.mIPAddr = jSONObject2.getString(str8);
                                    wifidevice6.mName = jSONObject2.getString(str5);
                                    wifidevice6.age = 0;
                                    TopLevel.this.mWifiDevices.add(wifidevice6);
                                    TopLevel.this.mThis.addWifiDeviceToTable(R.mipmap.sensortagwifi, jSONObject2.getString(str5), jSONObject2.getString(str8), R.mipmap.wifilogo);
                                }
                            }
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    } else if ("com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_OK".equals(action)) {
                        TextView textView = (TextView) TopLevel.this.findViewById(R.id.statusText);
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("Got SSID:");
                        String str9 = "com.ti.sensortag.ConfigureWiFiDialogFragment.SSID";
                        sb8.append(intent.getStringExtra(str9));
                        Log.d(str, sb8.toString());
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("Got encryption:");
                        sb9.append(intent.getIntExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.ENCRYPT", 0));
                        Log.d(str, sb9.toString());
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("Got password:");
                        sb10.append(intent.getStringExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.PWD"));
                        Log.d(str, sb10.toString());
                        String str10 = "com.ti.sensortag.ConfigureWiFiDialogFragment.IPADDR";
                        wifiDevice access$1000 = TopLevel.this.getDeviceFromIPAddress(intent.getStringExtra(str10));
                        if (access$1000 != null && access$1000.mIPAddr.equals(intent.getStringExtra(str10))) {
                            access$1000.mSSID = intent.getStringExtra(str9);
                        }
                        wifiDeviceActivity.writeNewSSIDConfig(TopLevel.this.mThis, intent.getStringExtra(str10), intent.getStringExtra(str9), intent.getIntExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.ENCRYPT", 0), intent.getStringExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.PWD"));
                    } else {
                        boolean equals2 = wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE.equals(action);
                        String str11 = wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_URL;
                        String str12 = wifiDeviceActivity.WIFI_DEVICE_REQUEST_IP_ADDR;
                        if (equals2) {
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append("Got request response for URL : ");
                            sb11.append(intent.getStringExtra(str11));
                            sb11.append("(");
                            sb11.append(intent.getIntExtra(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_CODE, HttpStatus.SC_OK));
                            sb11.append(")");
                            Log.d(str, sb11.toString());
                            String str13 = wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE_DATA;
                            if (intent.getStringExtra(str13) != null) {
                                if (intent.getStringExtra(str11).equals(wifiDeviceActivity.WIFI_DEVICE_PARAMETER_CONFIG_URL)) {
                                    StringBuilder sb12 = new StringBuilder();
                                    sb12.append("Ping found a new device ! ");
                                    sb12.append(intent.getStringExtra(str13));
                                    Log.d(str, sb12.toString());
                                    StringBuilder sb13 = new StringBuilder();
                                    sb13.append("Ping device name : ");
                                    sb13.append(wifiDeviceActivity.getName(intent.getStringExtra(str13)));
                                    Log.d(str, sb13.toString());
                                    String stringExtra = intent.getStringExtra(str12);
                                    String name = wifiDeviceActivity.getName(intent.getStringExtra(str13));
                                    StringBuilder sb14 = new StringBuilder();
                                    sb14.append("PING / FOUND IP: ");
                                    sb14.append(stringExtra);
                                    Log.i(str, sb14.toString());
                                    StringBuilder sb15 = new StringBuilder();
                                    sb15.append("PING / FOUND NAME: ");
                                    sb15.append(name);
                                    Log.i(str, sb15.toString());
                                    if (name != null && TopLevel.isSensorTagDevice(name) && !stringExtra.equals(str3)) {
                                        Log.d(str, str2);
                                        boolean z5 = false;
                                        for (int i7 = 0; i7 < TopLevel.this.mWifiDevices.size(); i7++) {
                                            wifiDevice wifidevice7 = (wifiDevice) TopLevel.this.mWifiDevices.get(i7);
                                            StringBuilder sb16 = new StringBuilder();
                                            sb16.append(str7);
                                            sb16.append(i7);
                                            sb16.append(str6);
                                            sb16.append(wifidevice7.mIPAddr);
                                            Log.d(str, sb16.toString());
                                            if (wifidevice7.mIPAddr.contains(stringExtra)) {
                                                Log.i(str, "DuplicatesCheck- new device is already in devices list - same IP Address");
                                                wifidevice7.age = 0;
                                                z5 = true;
                                            }
                                        }
                                        if (!z5) {
                                            for (int i8 = 0; i8 < TopLevel.this.mWifiDevices.size(); i8++) {
                                                wifiDevice wifidevice8 = (wifiDevice) TopLevel.this.mWifiDevices.get(i8);
                                                StringBuilder sb17 = new StringBuilder();
                                                sb17.append(str7);
                                                sb17.append(i8);
                                                sb17.append(str6);
                                                sb17.append(wifidevice8.mName);
                                                Log.d(str, sb17.toString());
                                                if (wifidevice8.mName.equals(name)) {
                                                    Log.i(str, "DuplicatesCheck- new device is already in devices list - same name");
                                                    wifidevice8.age = 0;
                                                    z5 = true;
                                                }
                                            }
                                        }
                                        if (!z5) {
                                            Log.i(str, "DuplicatesCheck- new device is NOT already in devices list - create new wifiDevice Object and add to list");
                                            wifiDevice wifidevice9 = new wifiDevice();
                                            wifidevice9.mIPAddr = stringExtra;
                                            wifidevice9.mName = name;
                                            wifidevice9.age = 0;
                                            TopLevel.this.mWifiDevices.add(wifidevice9);
                                            TopLevel.this.addWifiDeviceToTable(R.mipmap.sensortagwifi, name, stringExtra, R.mipmap.wifilogo);
                                        }
                                    }
                                } else {
                                    StringBuilder sb18 = new StringBuilder();
                                    sb18.append("Got request data :");
                                    sb18.append(intent.getStringExtra(str13));
                                    Log.d(str, sb18.toString());
                                    if (wifiDeviceActivity.getFirmwareString(intent.getStringExtra(str13)).length() > 0) {
                                        for (int i9 = 0; i9 < TopLevel.this.mWifiDevices.size(); i9++) {
                                            wifiDevice wifidevice10 = (wifiDevice) TopLevel.this.mWifiDevices.get(i9);
                                            if (wifidevice10.mIPAddr.equals(intent.getStringExtra(str12))) {
                                                wifidevice10.mMacAddr = wifiDeviceActivity.getMacString(intent.getStringExtra(str13));
                                                TopLevel.this.foundSensorTag(wifiDeviceActivity.getFirmwareString(intent.getStringExtra(str13)), intent.getStringExtra(str12));
                                                wifidevice10.printInfo();
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (wifiDeviceActivity.WIFI_DEVICE_REQUEST_DISCONNECTED.equals(action)) {
                            StringBuilder sb19 = new StringBuilder();
                            sb19.append("Request disconnect for URL : ");
                            sb19.append(intent.getStringExtra(str11));
                            sb19.append("For ipAddress : ");
                            sb19.append(intent.getStringExtra(str12));
                            Log.d(str, sb19.toString());
                        } else {
                            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                                Log.d(str, "Received connectivity action !");
                                WifiManager wifiManager2 = (WifiManager) TopLevel.this.getApplicationContext().getSystemService("wifi");
                                StringBuilder sb20 = new StringBuilder();
                                sb20.append("SSID is now : ");
                                sb20.append(wifiManager2.getConnectionInfo().getSSID());
                                Log.d(str, sb20.toString());
                            }
                        }
                    }
                }
            }
        }
    };
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            TopLevel.this.doubleBackToExitPressedOnce = false;
        }
    };
    private SwipeRefreshLayout mSwipeContainer;
    /* access modifiers changed from: private */
    public TopLevel mThis;
    /* access modifiers changed from: private */
    public Intent mWiFiDeviceIntent;
    /* access modifiers changed from: private */
    public ArrayList<wifiDevice> mWifiDevices;
    public BluetoothLEManagerCB managerCB = new BluetoothLEManagerCB() {
        public void deviceFound(BluetoothLEDevice bluetoothLEDevice) {
            String str;
            int i;
            String str2;
            Log.d(TopLevel.TAG, "Device was found");
            Iterator it = TopLevel.this.mDevices.iterator();
            boolean z = false;
            while (true) {
                str = "Man Data (";
                i = -1;
                str2 = "SERVICE DATA";
                if (!it.hasNext()) {
                    break;
                }
                BluetoothLEDevice bluetoothLEDevice2 = (BluetoothLEDevice) it.next();
                if (bluetoothLEDevice2.f27d.getAddress().equalsIgnoreCase(bluetoothLEDevice.f27d.getAddress())) {
                    ScanRecord scanRecord = bluetoothLEDevice2.f30sR.getScanRecord();
                    Map serviceData = scanRecord.getServiceData();
                    Log.d(str2, serviceData.toString());
                    boolean updateData = bluetoothLEDevice.beaconDecoder.updateData(serviceData);
                    SparseArray manufacturerSpecificData = scanRecord.getManufacturerSpecificData();
                    byte[] bArr = new byte[0];
                    int i2 = -1;
                    for (int i3 = 0; i3 < manufacturerSpecificData.size(); i3++) {
                        i2 = manufacturerSpecificData.keyAt(i3);
                        bArr = (byte[]) manufacturerSpecificData.get(i2);
                    }
                    if (updateData) {
                        Log.d("ScanCallback", "Eddystone beacon found :");
                        bluetoothLEDevice.needsBroadcastScreen = true;
                    }
                    if (bArr.length > 0) {
                        TopLevel topLevel = TopLevel.this;
                        String address = bluetoothLEDevice.f27d.getAddress();
                        int rssi = bluetoothLEDevice2.f30sR.getRssi();
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append((String) BluetoothGATTDefines.manufacturerIDStrings.get(Integer.valueOf(i2)));
                        sb.append("): \r\n");
                        sb.append(bleUtility.getStringFromByteVectorWithAutoNewLine(bArr, 10));
                        topLevel.modifyDeviceRSSIInTable(address, rssi, sb.toString());
                    } else {
                        TopLevel.this.modifyDeviceRSSIInTable(bluetoothLEDevice2.f30sR.getDevice().getAddress(), bluetoothLEDevice2.f30sR.getRssi());
                    }
                    z = true;
                }
            }
            if (!z) {
                final BTDeviceFilterGlobal instance = BTDeviceFilterGlobal.getInstance(TopLevel.this.mThis);
                if (instance.filterDevice(bluetoothLEDevice)) {
                    TopLevel.this.mDevices.add(bluetoothLEDevice);
                    ScanRecord scanRecord2 = bluetoothLEDevice.f30sR.getScanRecord();
                    Map serviceData2 = scanRecord2.getServiceData();
                    Log.d(str2, serviceData2.toString());
                    boolean updateData2 = bluetoothLEDevice.beaconDecoder.updateData(serviceData2);
                    SparseArray manufacturerSpecificData2 = scanRecord2.getManufacturerSpecificData();
                    byte[] bArr2 = new byte[0];
                    for (int i4 = 0; i4 < manufacturerSpecificData2.size(); i4++) {
                        i = manufacturerSpecificData2.keyAt(i4);
                        bArr2 = (byte[]) manufacturerSpecificData2.get(i);
                    }
                    if (updateData2) {
                        bluetoothLEDevice.needsBroadcastScreen = true;
                    }
                    if (bArr2.length > 0) {
                        TopLevel topLevel2 = TopLevel.this;
                        int access$200 = topLevel2.getDeviceIconFromDeviceName(bluetoothLEDevice.f30sR.getDevice().getName());
                        String name = bluetoothLEDevice.f30sR.getDevice().getName();
                        String address2 = bluetoothLEDevice.f30sR.getDevice().getAddress();
                        int access$300 = TopLevel.this.getRssiImageFromRssiLevel(bluetoothLEDevice.f30sR.getRssi());
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append((String) BluetoothGATTDefines.manufacturerIDStrings.get(Integer.valueOf(i)));
                        sb2.append("): ");
                        sb2.append(bleUtility.getStringFromByteVector(bArr2));
                        topLevel2.addDeviceToTable(access$200, name, address2, access$300, sb2.toString());
                    } else {
                        TopLevel topLevel3 = TopLevel.this;
                        topLevel3.addDeviceToTable(topLevel3.getDeviceIconFromDeviceName(bluetoothLEDevice.f30sR.getDevice().getName()), bluetoothLEDevice.f30sR.getDevice().getName(), bluetoothLEDevice.f30sR.getDevice().getAddress(), TopLevel.this.getRssiImageFromRssiLevel(bluetoothLEDevice.f30sR.getRssi()));
                    }
                }
                TopLevel.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (instance.globalEnable) {
                            TextView access$600 = TopLevel.this.mThis.filterStatus;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Filter On, Showing ");
                            sb.append(instance.inDeviceCount());
                            sb.append(" of ");
                            sb.append(instance.totalDeviceCount());
                            access$600.setText(sb.toString());
                            TopLevel.this.mThis.filterStatus.clearAnimation();
                            TopLevel.this.mThis.filterStatus.setAlpha(1.0f);
                            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                            alphaAnimation.setRepeatCount(-1);
                            alphaAnimation.setRepeatMode(2);
                            alphaAnimation.setDuration(500);
                            TopLevel.this.mThis.filterStatus.startAnimation(alphaAnimation);
                            return;
                        }
                        TopLevel.this.mThis.filterStatus.setText("Filter Off");
                        TopLevel.this.mThis.filterStatus.clearAnimation();
                    }
                });
            }
        }
    };
    private NetInfo net;
    private AlertDialog outOfDateFWDialog;
    private Thread pingThread;
    private AlertDialog provisioningDialog;
    private final int rssiLevel0 = -100;
    private final int rssiLevel1 = -90;
    private final int rssiLevel2 = -80;
    private final int rssiLevel3 = -60;
    private final int rssiLevel4 = -40;
    /* access modifiers changed from: private */
    public boolean scanningDisabled;
    private boolean showingWifiConfig = false;
    /* access modifiers changed from: private */
    public UdpBcastServer udpBcastServer;
    /* access modifiers changed from: private */
    public Thread udpBcastServerThread;
    /* access modifiers changed from: private */
    public ArrayList<wifiDevice> updateDevicesArray;
    /* access modifiers changed from: private */
    public WifiConfiguration wifiConfigurationToConnectToFromIntent;
    BLEDeviceHeaderTableRow wifiHeader;
    private wlanController wlanCon;

    /* access modifiers changed from: private */
    public int getRssiImageFromRssiLevel(int i) {
        return i < -100 ? R.mipmap.rssi_meter_0 : i < -90 ? R.mipmap.rssi_meter_1 : i < -80 ? R.mipmap.rssi_meter_2 : i < -60 ? R.mipmap.rssi_meter_3 : R.mipmap.rssi_meter_4;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_top_level);
        this.mThis = this;
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        this.mSwipeContainer.setColorSchemeColors(-65536);
        this.leManager = BluetoothLEManager.getInstance(getApplicationContext());
        BluetoothLEManager bluetoothLEManager = this.leManager;
        bluetoothLEManager.managerCB = this.managerCB;
        if (bluetoothLEManager.checkPermission() != 0) {
            this.leManager.prepareForScanForDevices(this.mThis);
        } else {
            this.leManager.scanForDevices(0);
        }
        this.filterIcon = (ImageView) findViewById(R.id.filter_icon);
        this.filterIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d(TopLevel.TAG, "Filter icon clicked");
                Intent intent = new Intent(TopLevel.this.mThis, FilterConfigurationActivity.class);
                TopLevel.this.mThis.startActivity(intent);
                TopLevel.this.mThis.sendBroadcast(intent);
            }
        });
        this.filterStatus = (TextView) findViewById(R.id.filter_status);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        BluetoothLEManager bluetoothLEManager = this.leManager;
        if (i == 1 && iArr.length > 0) {
            try {
                bluetoothLEManager.scanForDevices(iArr[0]);
            } catch (Exception unused) {
                Log.d(TAG, "Got exception");
            }
        }
    }

    public void refreshTable() {
        TextView textView = (TextView) findViewById(R.id.statusText);
        textView.setText("Clearing Table");
        if (this.leManager.checkPermission() != 0) {
            this.leManager.prepareForScanForDevices(this.mThis);
        } else {
            this.leManager.scanForDevices(0);
        }
        this.mDeviceTable = (TableLayout) findViewById(R.id.deviceTableLayout);
        this.mDeviceTable.removeAllViews();
        this.mDevices = new ArrayList<>();
        this.mWifiDevices = new ArrayList<>();
        this.mPairedDevices = new ArrayList<>();
        rebuildDeviceTable();
        this.mSwipeContainer.setRefreshing(false);
        textView.setText("Pull to refresh");
        scanForSensorTagDevices();
        restartUdp();
        BTDeviceFilterGlobal.getInstance(this.mThis).resetDeviceList();
        checkCurrentConnectionForDisplay();
    }

    public void rebuildDeviceTable() {
        this.mDeviceTable = (TableLayout) findViewById(R.id.deviceTableLayout);
        TableLayout tableLayout = this.mDeviceTable;
        if (tableLayout != null) {
            tableLayout.removeAllViews();
            this.mDeviceTable.addView(new BLEDeviceHeaderTableRow(this.mThis));
            this.wifiHeader = new BLEDeviceHeaderTableRow(this.mThis);
            ((TextView) this.wifiHeader.findViewById(R.id.bdhtr_section_header)).setText("WIFI DEVICES");
            this.mDeviceTable.addView(this.wifiHeader);
            this.mDeviceTable.addView(new WiFiConfigurationTableRow(this.mThis));
            Iterator it = this.mDevices.iterator();
            while (it.hasNext()) {
                BluetoothLEDevice bluetoothLEDevice = (BluetoothLEDevice) it.next();
                ScanRecord scanRecord = bluetoothLEDevice.f30sR.getScanRecord();
                if (scanRecord != null) {
                    Map serviceData = scanRecord.getServiceData();
                    Log.d("SERVICE DATA", serviceData.toString());
                    boolean updateData = bluetoothLEDevice.beaconDecoder.updateData(serviceData);
                    SparseArray manufacturerSpecificData = scanRecord.getManufacturerSpecificData();
                    byte[] bArr = new byte[0];
                    int i = -1;
                    for (int i2 = 0; i2 < manufacturerSpecificData.size(); i2++) {
                        i = manufacturerSpecificData.keyAt(i2);
                        bArr = (byte[]) manufacturerSpecificData.get(i);
                    }
                    if (updateData) {
                        Log.d("ScanCallback", "Eddystone beacon found :");
                        bluetoothLEDevice.needsBroadcastScreen = true;
                    }
                    if (bArr.length > 0) {
                        int deviceIconFromDeviceName = getDeviceIconFromDeviceName(bluetoothLEDevice.f27d.getName());
                        String name = bluetoothLEDevice.f27d.getName();
                        String address = bluetoothLEDevice.f27d.getAddress();
                        int rssiImageFromRssiLevel = getRssiImageFromRssiLevel(bluetoothLEDevice.f30sR.getRssi());
                        StringBuilder sb = new StringBuilder();
                        sb.append("Man Data (");
                        sb.append((String) BluetoothGATTDefines.manufacturerIDStrings.get(Integer.valueOf(i)));
                        sb.append("): ");
                        sb.append(bleUtility.getStringFromByteVector(bArr));
                        addDeviceToTable(deviceIconFromDeviceName, name, address, rssiImageFromRssiLevel, sb.toString());
                    } else {
                        addDeviceToTable(getDeviceIconFromDeviceName(bluetoothLEDevice.f27d.getName()), bluetoothLEDevice.f27d.getName(), bluetoothLEDevice.f27d.getAddress(), getRssiImageFromRssiLevel(bluetoothLEDevice.f30sR.getRssi()));
                    }
                } else {
                    addDeviceToTable(getDeviceIconFromDeviceName(bluetoothLEDevice.f27d.getName()), bluetoothLEDevice.f27d.getName(), bluetoothLEDevice.f27d.getAddress(), getRssiImageFromRssiLevel(bluetoothLEDevice.f30sR.getRssi()));
                }
            }
            Iterator it2 = this.mWifiDevices.iterator();
            while (it2.hasNext()) {
                wifiDevice wifidevice = (wifiDevice) it2.next();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("AGE- add device to UI: ");
                sb2.append(wifidevice.printData());
                Log.i(TAG, sb2.toString());
                addWifiDeviceToTable(R.mipmap.sensortagwifi, wifidevice.mName, wifidevice.mIPAddr, R.mipmap.wifilogo);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_level, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_about) {
            Log.d(TAG, "About menu item touched");
            startActivity(new Intent(this, aboutActivity.class));
            return true;
        }
        if (itemId == R.id.opt_prefs) {
            Log.d("DeviceActivity", "Preferences clicked !");
            Intent intent = new Intent(this, PreferencesActivity.class);
            intent.putExtra(":android:show_fragment", PreferencesFragment.class.getName());
            intent.putExtra(":android:no_headers", true);
            intent.putExtra(DeviceActivity.EXTRA_DEVICE, this.mBluetoothDevice);
            startActivityForResult(intent, 0);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onStart() {
        super.onStart();
        String str = TAG;
        Log.d(str, "onStart()");
        this.mDeviceTable = (TableLayout) findViewById(R.id.deviceTableLayout);
        this.mDevices = new ArrayList<>();
        this.mWifiDevices = new ArrayList<>();
        this.killMDNSScanWhenInBackground = false;
        this.isScanning = false;
        this.scanningDisabled = false;
        this.net = new NetInfo(this);
        this.mDnsHelper = new MDnsHelper();
        initMDNS();
        setTitle(R.string.app_name);
        this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBtAdapter == null) {
            new Builder(this).setTitle("Error !").setMessage("This Android device does not have Bluetooth or there is an error in the bluetooth setup. Application cannot start, will exit.").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).create().show();
            return;
        }
        this.client = new GoogleApiClient.Builder(this).addConnectionCallbacks(new ConnectionCallbacks() {
            public void onConnected(Bundle bundle) {
                StringBuilder sb = new StringBuilder();
                sb.append("CLIENT - onConnected: ");
                sb.append(bundle);
                Log.d(TopLevel.TAG, sb.toString());
                AppIndex.AppIndexApi.start(TopLevel.this.client, Action.newAction(Action.TYPE_VIEW, "TopLevel Page", Uri.parse("http://host/path"), Uri.parse("android-app://com.ti.ble.simplelinkstarter/http/host/path")));
            }

            public void onConnectionSuspended(int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("CLIENT - onConnectionSuspended: ");
                sb.append(i);
                Log.d(TopLevel.TAG, sb.toString());
            }
        }).addOnConnectionFailedListener(new OnConnectionFailedListener() {
            public void onConnectionFailed(ConnectionResult connectionResult) {
                StringBuilder sb = new StringBuilder();
                sb.append("CLIENT - onConnectionFailed: ");
                sb.append(connectionResult);
                Log.d(TopLevel.TAG, sb.toString());
            }
        }).addApi(AppIndex.API).build();
        this.client.connect();
        this.mFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mFilter.addAction(DEVICE_FOUND_BROADCAST_ACTION);
        this.mFilter.addAction(ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_CANCELED);
        this.mFilter.addAction("com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_OK");
        this.mFilter.addAction(wifiDeviceActivity.WIFI_DEVICE_REQUEST_RESPONSE);
        this.mFilter.addAction(wifiDeviceActivity.WIFI_DEVICE_REQUEST_DISCONNECTED);
        this.mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mSwipeContainer.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                TopLevel.this.refreshTable();
            }
        });
        Builder builder = new Builder(this.mThis);
        builder.setTitle(getResources().getString(R.string.prerequisites));
        builder.setMessage(getResources().getString(R.string.prerequisites_message));
        View inflate = View.inflate(this.mThis, R.layout.mobile_data_alert_dialog_do_not_show_again_checkbox, null);
        CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.mobile_data_alert_do_not_show_again_checkbox);
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                Editor edit = PreferenceManager.getDefaultSharedPreferences(TopLevel.this.getApplicationContext()).edit();
                edit.putBoolean(TopLevel.PRE_DO_NOT_SHOW_PREF, z);
                edit.apply();
            }
        });
        checkBox.setText(getResources().getString(R.string.do_not_show_again));
        builder.setView(inflate);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.mobile_data), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                TopLevel.this.startActivity(new Intent("android.settings.DATA_ROAMING_SETTINGS"));
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.wifi_settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                TopLevel.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
            }
        });
        builder.setNeutralButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        boolean z = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(PRE_DO_NOT_SHOW_PREF, false);
        this.didShowMobileDataDialogSingleton = DidShowMobileDataDialogSingleton.getInstance();
        boolean z2 = this.didShowMobileDataDialogSingleton.didShow;
        if (!z && !z2) {
            AlertDialog create = builder.create();
            create.show();
            create.getButton(-2).setTextColor(getResources().getColor(R.color.alertDialogButtonColor));
            create.getButton(-1).setTextColor(getResources().getColor(R.color.alertDialogButtonColor));
            create.getButton(-3).setTextColor(getResources().getColor(R.color.alertDialogButtonColor));
            this.didShowMobileDataDialogSingleton.didShow = true;
        }
        initialConnectionSSID = wifiDeviceActivity.getCurrentConnectionSSID(this.mThis);
        StringBuilder sb = new StringBuilder();
        sb.append("INITIAL- TopLevel onCreate / initialConnectionSSID: ");
        sb.append(initialConnectionSSID);
        Log.i(str, sb.toString());
        if (initialConnectionSSID != null) {
            for (ScanResult scanResult : ((WifiManager) this.mThis.getApplicationContext().getSystemService("wifi")).getScanResults()) {
                if (scanResult.SSID.equals(initialConnectionSSID)) {
                    initialConnectionSecurityType = scanResult.capabilities;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("INITIAL- TopLevel onCreate / initialConnectionSecurityType: ");
                    sb2.append(initialConnectionSecurityType);
                    Log.i(str, sb2.toString());
                }
            }
        }
        registerReceiver(this.mReceiver, this.mFilter);
    }

    public void onStop() {
        Log.d(TAG, "STOP");
        super.onStop();
        stopPing();
        this.mDnsHelper.stopDiscovery();
        unregisterReceiver(this.mReceiver);
        this.wlanCon.stopWlanController();
        AppIndex.AppIndexApi.end(this.client, Action.newAction(Action.TYPE_VIEW, "TopLevel Page", Uri.parse("http://host/path"), Uri.parse("android-app://com.ti.ble.simplelinkstarter/http/host/path")));
        this.client.disconnect();
    }

    public void onWindowFocusChanged(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Focus changed ");
        sb.append(z);
        Log.d(TAG, sb.toString());
        if (z) {
            if (this.focusFromAlertDialog) {
                if (!this.showingWifiConfig) {
                    this.focusFromAlertDialog = false;
                }
                return;
            }
            ProgressDialog progressDialog = this.connectPD;
            if (progressDialog != null && progressDialog.isShowing()) {
                this.connectPD.dismiss();
            }
            if (this.focusFromAlertDialog) {
                if (!this.showingWifiConfig) {
                    this.focusFromAlertDialog = false;
                }
                return;
            }
            TextView textView = (TextView) findViewById(R.id.statusText);
            textView.setText("Clearing Table");
            this.mDeviceTable = (TableLayout) findViewById(R.id.deviceTableLayout);
            this.mDeviceTable.removeAllViews();
            this.mDeviceTable.addView(new BLEDeviceHeaderTableRow(this.mThis));
            this.mDevices = new ArrayList<>();
            this.mSwipeContainer.setRefreshing(false);
            textView.setText("Pull to refresh");
            if (this.comingFromBluetoothRestart) {
                this.comingFromBluetoothRestart = false;
            } else {
                refreshTable();
            }
            checkCurrentConnectionForDisplay();
        }
    }

    private boolean isOnSensorTagWifi() {
        WifiInfo connectionInfo = ((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        StringBuilder sb = new StringBuilder();
        sb.append("We're on SSID : ");
        sb.append(connectionInfo.getSSID());
        Log.d("ConfigureWiFiActivity", sb.toString());
        String ssid = connectionInfo.getSSID();
        return ssid != null && isSensorTagDevice(ssid);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        String str = "Connecting to: ";
        String str2 = ". connecting to it";
        String str3 = "a WifiConfig to connect to was passed with starting intent - ";
        String str4 = "set desired: ";
        String str5 = TAG;
        Log.d(str5, "onResume()");
        super.onResume();
        this.killMDNSScanWhenInBackground = false;
        String str6 = "Checking if TopLevel was started with intent";
        Log.i(str5, str6);
        String str7 = "RECON - ";
        Log.d(str7, str6);
        Intent intent = getIntent();
        if (intent != null) {
            String str8 = "TopLevel was started with intent";
            Log.i(str5, str8);
            Log.d(str7, str8);
            String str9 = "Try to connect to passed config - if exists";
            Log.i(str5, str9);
            Log.d(str7, str9);
            try {
                this.wifiConfigurationToConnectToFromIntent = (WifiConfiguration) intent.getParcelableExtra(CONNECT_TO_CONFIG_EXTRA);
                this.wlanCon = new wlanController(this.mThis);
                this.wlanCon.setDesiredWlanConfig(this.wifiConfigurationToConnectToFromIntent);
                StringBuilder sb = new StringBuilder();
                sb.append(str4);
                sb.append(this.wifiConfigurationToConnectToFromIntent);
                Log.i("wlanController", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str4);
                sb2.append(this.wifiConfigurationToConnectToFromIntent);
                Log.d(str7, sb2.toString());
                this.wlanCon.startWlanController();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(this.wifiConfigurationToConnectToFromIntent.SSID);
                sb3.append(str2);
                Log.i(str5, sb3.toString());
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str3);
                sb4.append(this.wifiConfigurationToConnectToFromIntent.SSID);
                sb4.append(str2);
                Log.d(str7, sb4.toString());
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str);
                sb5.append(this.wifiConfigurationToConnectToFromIntent.SSID);
                Log.i(str5, sb5.toString());
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str);
                sb6.append(this.wifiConfigurationToConnectToFromIntent.SSID);
                Log.d(str7, sb6.toString());
            } catch (Exception e) {
                String str10 = "a WifiConfig to connect to was NOT passed with starting intent";
                Log.i(str5, str10);
                Log.d(str7, str10);
                e.printStackTrace();
            }
        }
        BTDeviceFilterGlobal.getInstance(this).resetDeviceList();
        BTDeviceFilterGlobal.getInstance(this).reloadFilterConfigs();
        scanForSensorTagDevices();
        checkCurrentConnectionForDisplay();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        this.killMDNSScanWhenInBackground = true;
        stopScanningForSensorTagDevices();
    }

    /* access modifiers changed from: private */
    public int getDeviceIconFromDeviceName(String str) {
        int i = R.mipmap.device_unknown;
        if (str == null) {
            return R.mipmap.device_unknown;
        }
        if (str.equals("Multi-Sensor") || str.equals("LPSTK:)")) {
            i = R.mipmap.lpstk_main_icon;
        } else if (str.equals(Sensor_Tag)) {
            i = R.mipmap.sensortag;
        } else if (str.equals("CC2650 SensorTag")) {
            i = R.mipmap.sensortag2;
        } else if (str.equals("Project Zero") || str.equals("ProjectZero")) {
            i = R.mipmap.project_zero;
        } else if (str.equals("CC2650 RC") || str.equals("HID AdvRemote")) {
            i = R.mipmap.cc2650_rc;
        } else if (str.equals("CC2650 LaunchPad") || str.equals("CC1350 LaunchPad")) {
            i = R.mipmap.cc2650_launchpad;
        }
        return i;
    }

    /* access modifiers changed from: private */
    public void addDeviceToTable(int i, String str, String str2, int i2, String str3) {
        addDeviceToTable(i, str, str2, i2);
    }

    /* access modifiers changed from: private */
    public void addDeviceToTable(int i, String str, String str2, int i2) {
        DeviceTableRow deviceTableRow = new DeviceTableRow(this);
        ((ImageView) deviceTableRow.findViewById(R.id.device_table_row_icon)).setImageResource(i);
        ((TextView) deviceTableRow.findViewById(R.id.device_table_row_name)).setText(str);
        ((TextView) deviceTableRow.findViewById(R.id.device_table_row_bt_addr)).setText(str2);
        ((ImageView) deviceTableRow.findViewById(R.id.device_table_row_rssi_level)).setImageResource(i2);
        deviceTableRow.getChildAt(0).setOnClickListener(this);
        try {
            this.mDeviceTable.addView(deviceTableRow, this.mDeviceTable.indexOfChild(this.wifiHeader));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void addWifiDeviceToTable(int i, String str, String str2, int i2) {
        DeviceTableRow deviceTableRow = new DeviceTableRow(this);
        ((ImageView) deviceTableRow.findViewById(R.id.device_table_row_icon)).setImageResource(i);
        ((TextView) deviceTableRow.findViewById(R.id.device_table_row_name)).setText(str);
        ((TextView) deviceTableRow.findViewById(R.id.device_table_row_bt_addr)).setText(str2);
        ((ImageView) deviceTableRow.findViewById(R.id.device_table_row_rssi_level)).setImageResource(i2);
        deviceTableRow.getChildAt(0).setOnClickListener(this);
        this.mDeviceTable.addView(deviceTableRow);
        wifiDeviceActivity.configureDevice(this, str2, 127);
        Log.d(TAG, "Added new WiFi row");
    }

    public void onClick(View view) {
        TableRow tableRow = (TableRow) view;
        final TextView textView = (TextView) tableRow.findViewById(R.id.device_table_row_bt_addr);
        final TextView textView2 = (TextView) tableRow.findViewById(R.id.device_table_row_name);
        StringBuilder sb = new StringBuilder();
        sb.append("Clicked device cell with Bluetooth address : ");
        sb.append(textView.getText());
        Log.d(TAG, sb.toString());
        final CharSequence text = textView.getText();
        runOnUiThread(new Runnable() {
            public void run() {
                TopLevel topLevel = TopLevel.this;
                topLevel.connectPD = new ProgressDialog(topLevel.mThis);
                TopLevel.this.connectPD.setTitle("Connecting ...");
                TopLevel.this.connectPD.setIndeterminate(true);
                ProgressDialog access$1200 = TopLevel.this.connectPD;
                StringBuilder sb = new StringBuilder();
                sb.append("Connecting to peripheral (");
                sb.append(text);
                sb.append(") ");
                access$1200.setMessage(sb.toString());
                TopLevel.this.connectPD.show();
            }
        });
        new Thread(new Runnable() {
            public void run() {
                boolean z = false;
                int i = 0;
                while (true) {
                    int size = TopLevel.this.mDevices.size();
                    String str = "Found device in device list";
                    String str2 = TopLevel.TAG;
                    if (i < size) {
                        BluetoothLEDevice bluetoothLEDevice = (BluetoothLEDevice) TopLevel.this.mDevices.get(i);
                        bluetoothLEDevice.myCB = TopLevel.this.deviceCB;
                        if (!bluetoothLEDevice.f27d.getAddress().equals(text)) {
                            i++;
                        } else if (((BluetoothLEDevice) TopLevel.this.mDevices.get(i)).needsBroadcastScreen) {
                            Intent intent = new Intent(TopLevel.this.mThis, BroadcastActivity.class);
                            intent.putExtra(TopLevel.BROADCAST_DEVICE_ADDR, bluetoothLEDevice.f27d.getAddress());
                            TopLevel.this.startActivity(intent);
                            return;
                        } else {
                            Log.d(str2, str);
                            TopLevel.this.mBluetoothDevice = bluetoothLEDevice.f27d;
                            bluetoothLEDevice.connectDevice();
                            TopLevel.this.mThis.leManager.stopScan();
                            return;
                        }
                    } else {
                        if (TopLevel.this.mPairedDevices != null) {
                            for (int i2 = 0; i2 < TopLevel.this.mPairedDevices.size(); i2++) {
                                BluetoothLEDevice bluetoothLEDevice2 = (BluetoothLEDevice) TopLevel.this.mPairedDevices.get(i2);
                                bluetoothLEDevice2.myCB = TopLevel.this.deviceCB;
                                if (bluetoothLEDevice2.f27d.getAddress().equals(text)) {
                                    Log.d(str2, str);
                                    TopLevel.this.mBluetoothDevice = bluetoothLEDevice2.f27d;
                                    bluetoothLEDevice2.connectDevice();
                                    return;
                                }
                            }
                        }
                        TopLevel topLevel = TopLevel.this;
                        topLevel.mWiFiDeviceIntent = new Intent(topLevel.mThis, wifiDeviceActivity.class);
                        TopLevel.this.mWiFiDeviceIntent.putExtra(DeviceActivity.EXTRA_DEVICE, textView.getText());
                        TopLevel.this.mWiFiDeviceIntent.putExtra(TopLevel.DEVICE_NAME_EXTRA, textView2.getText());
                        StringBuilder sb = new StringBuilder();
                        sb.append("Passing chosen device data-\nIP: ");
                        sb.append(textView.getText());
                        sb.append(" / Name: ");
                        sb.append(textView2.getText());
                        Log.i(str2, sb.toString());
                        WifiConfiguration access$1700 = TopLevel.this.wifiConfigurationToConnectToFromIntent;
                        String str3 = TopLevel.CONFIG_TO_CONNECT_TO_EXTRA;
                        if (access$1700 != null) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Got WifiConfiguration from StageThree: ");
                            sb2.append(TopLevel.this.wifiConfigurationToConnectToFromIntent.SSID);
                            sb2.append(" - pass it to wifiDeviceActivity");
                            Log.i(str2, sb2.toString());
                            TopLevel.this.mWiFiDeviceIntent.putExtra(str3, TopLevel.this.wifiConfigurationToConnectToFromIntent);
                        } else {
                            Log.i(str2, "Do not have WifiConfiguration from StageThree - find current WiFi connection configuration and pass it to wifiDeviceActivity");
                            String currentConnectionSSID = wifiDeviceActivity.getCurrentConnectionSSID(TopLevel.this.mThis);
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Connected to: ");
                            sb3.append(currentConnectionSSID);
                            Log.i(str2, sb3.toString());
                            Log.i(str2, "Looking for connection configuration");
                            List<WifiConfiguration> configuredNetworks = ((WifiManager) TopLevel.this.mThis.getApplicationContext().getSystemService("wifi")).getConfiguredNetworks();
                            WifiConfiguration wifiConfiguration = null;
                            StringBuilder sb4 = new StringBuilder();
                            String str4 = "\"";
                            sb4.append(str4);
                            sb4.append(currentConnectionSSID);
                            sb4.append(str4);
                            String sb5 = sb4.toString();
                            for (WifiConfiguration wifiConfiguration2 : configuredNetworks) {
                                if (!z && wifiConfiguration2.SSID != null && wifiConfiguration2.SSID.equals(sb5)) {
                                    wifiConfiguration = wifiConfiguration2;
                                    z = true;
                                }
                            }
                            if (z) {
                                Log.i(str2, "Found network configuration - passing it to wifiDeviceActivity");
                                TopLevel.this.mWiFiDeviceIntent.putExtra(str3, wifiConfiguration);
                            } else {
                                Log.i(str2, "Did not find network configuration - passing nothing");
                            }
                        }
                        TopLevel topLevel2 = TopLevel.this;
                        topLevel2.startActivityForResult(topLevel2.mWiFiDeviceIntent, 1);
                        return;
                    }
                }
            }
        }).start();
    }

    public void modifyDeviceRSSIInTable(String str, int i) {
        for (int i2 = 0; i2 < this.mDevices.size(); i2++) {
            TableRow tableRow = (TableRow) this.mDeviceTable.getChildAt(i2);
            if (tableRow != null) {
                TextView textView = (TextView) tableRow.findViewById(R.id.device_table_row_bt_addr);
                if (textView != null && textView.getText().equals(str)) {
                    ((ImageView) tableRow.findViewById(R.id.device_table_row_rssi_level)).setImageResource(getRssiImageFromRssiLevel(i));
                }
            }
        }
    }

    public void modifyDeviceRSSIInTable(String str, int i, String str2) {
        for (int i2 = 0; i2 < this.mDevices.size(); i2++) {
            TableRow tableRow = (TableRow) this.mDeviceTable.getChildAt(i2);
            if (tableRow != null) {
                TextView textView = (TextView) tableRow.findViewById(R.id.device_table_row_bt_addr);
                if (textView != null && textView.getText().equals(str)) {
                    ((ImageView) tableRow.findViewById(R.id.device_table_row_rssi_level)).setImageResource(getRssiImageFromRssiLevel(i));
                    ((TextView) tableRow.findViewById(R.id.device_table_row_additional_info)).setText(str2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void foundSensorTag(String str, final String str2) {
        for (int i = 0; i < this.mWifiDevices.size(); i++) {
            wifiDevice wifidevice = (wifiDevice) this.mWifiDevices.get(i);
            if (wifidevice.mIPAddr.equals(str2)) {
                this.isLatestWiFiFirmware = wifiDeviceActivity.isLatestFirmware(str);
                String str3 = "\" ";
                String str4 = "\"";
                if (!this.isLatestWiFiFirmware) {
                    this.focusFromAlertDialog = true;
                    AlertDialog alertDialog = this.outOfDateFWDialog;
                    if (alertDialog == null || !alertDialog.isShowing()) {
                        Builder title = new Builder(this).setTitle(getResources().getString(R.string.out_of_date_firmware_found));
                        StringBuilder sb = new StringBuilder();
                        sb.append(str4);
                        sb.append(wifidevice.mName);
                        sb.append(str3);
                        sb.append(getResources().getString(R.string.out_of_date_firmware_found_message));
                        this.outOfDateFWDialog = title.setMessage(sb.toString()).setCancelable(false).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(TopLevel.this.mThis, wifiOTAActivity.class);
                                intent.putExtra(DeviceActivity.EXTRA_DEVICE, str2);
                                TopLevel.this.startActivityForResult(intent, 1);
                            }
                        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                        this.outOfDateFWDialog.show();
                        this.outOfDateFWDialog.getButton(-1).setTextColor(getResources().getColor(R.color.alertDialogButtonColor));
                        this.outOfDateFWDialog.getButton(-2).setTextColor(getResources().getColor(R.color.alertDialogButtonColor));
                    }
                } else if (isOnSensorTagWifi()) {
                    this.focusFromAlertDialog = true;
                    AlertDialog alertDialog2 = this.provisioningDialog;
                    if (alertDialog2 == null || !alertDialog2.isShowing()) {
                        Builder title2 = new Builder(this).setTitle(getResources().getString(R.string.access_point_mode_found));
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str4);
                        sb2.append(wifidevice.mName);
                        sb2.append(str3);
                        sb2.append(getResources().getString(R.string.access_point_mode_found_message));
                        this.provisioningDialog = title2.setMessage(sb2.toString()).setCancelable(false).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TopLevel.this.startActivity(new Intent(TopLevel.this.mThis, ConfigureWifiActivityStageTwo.class));
                            }
                        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                        this.provisioningDialog.show();
                        this.provisioningDialog.getButton(-1).setTextColor(getResources().getColor(R.color.alertDialogButtonColor));
                        this.provisioningDialog.getButton(-2).setTextColor(getResources().getColor(R.color.alertDialogButtonColor));
                    }
                }
            }
        }
    }

    private boolean initPing() {
        String str;
        stopPing();
        if (NetworkUtil.getConnectionStatus(this) == NetworkUtil.WIFI) {
            try {
                this.net.getWifiInfo();
                while (true) {
                    boolean equalsIgnoreCase = this.net.gatewayIp.equalsIgnoreCase("0.0.0.0");
                    str = TAG;
                    if (!equalsIgnoreCase) {
                        break;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("initPing/ in gateway ip while - network ip: ");
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
                sb2.append("initPing/ after while network ip: ");
                sb2.append(this.net.gatewayIp);
                Log.i(str, sb2.toString());
                String[] split = this.net.gatewayIp.split("\\.");
                String str2 = "";
                String str3 = str2;
                for (int i = 0; i < 3; i++) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str3);
                    sb3.append(split[i]);
                    sb3.append(".");
                    str3 = sb3.toString();
                }
                this.mPing = new Ping(this.mThis, this.mPingCallback, this.net.gatewayIp);
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

    private void startPing() {
        String str = TAG;
        if (initPing()) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("startPing: Ping Thread : ");
                sb.append(this.pingThread);
                sb.append(" State : ");
                sb.append(this.pingThread.getState());
                sb.append(" isAlive: ");
                sb.append(this.pingThread.isAlive());
                Log.d(str, sb.toString());
            } catch (NullPointerException unused) {
                Log.d(str, "startPing: Could not read thread status on pingThred = null !");
            }
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

    private void stopPing() {
        String str = TAG;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("stopPing: Ping Thread : ");
            sb.append(this.pingThread);
            sb.append(" State : ");
            sb.append(this.pingThread.getState());
            sb.append(" isAlive: ");
            sb.append(this.pingThread.isAlive());
            Log.d(str, sb.toString());
        } catch (NullPointerException unused) {
            Log.d(str, "stopPing: Could not read thread status on pingThred = null !");
        }
        Ping ping = this.mPing;
        if (ping != null && ping.working) {
            this.mPing.stopPing();
            Log.i(str, "Ping - stopped");
        }
        this.mPing = null;
    }

    private void restartUdp() {
        new Thread(new Runnable() {
            public void run() {
                if (TopLevel.this.udpBcastServer != null) {
                    TopLevel.this.udpBcastServer.stopUDPBcastServer();
                    TopLevel.this.udpBcastServerThread.interrupt();
                    TopLevel.this.udpBcastServer = null;
                }
                TopLevel topLevel = TopLevel.this;
                topLevel.udpBcastServer = new UdpBcastServer(topLevel.mThis, TopLevel.this.mPingCallback);
                TopLevel topLevel2 = TopLevel.this;
                topLevel2.udpBcastServerThread = new Thread(topLevel2.udpBcastServer.udpBcastServerRunnable);
                TopLevel.this.udpBcastServerThread.start();
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public wifiDevice getDeviceFromIPAddress(String str) {
        for (int i = 0; i < this.mWifiDevices.size(); i++) {
            wifiDevice wifidevice = (wifiDevice) this.mWifiDevices.get(i);
            if (wifidevice.mIPAddr.equals(str)) {
                return wifidevice;
            }
        }
        return null;
    }

    private void initMDNS() {
        this.mDNSCallback = new MDnsCallbackInterface() {
            public void onDeviceResolved(JSONObject jSONObject) {
                StringBuilder sb = new StringBuilder();
                sb.append("Found new device via mDNS: ");
                sb.append(jSONObject);
                String sb2 = sb.toString();
                String str = TopLevel.TAG;
                Log.d(str, sb2);
                if (TopLevel.this.scanningDisabled) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("New device was found via mDNS: ");
                    sb3.append(jSONObject);
                    sb3.append(",\nbut scanning is disabled so we shall ignore it");
                    Log.i(str, sb3.toString());
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("newDevice", jSONObject.toString());
                intent.setAction(TopLevel.DEVICE_FOUND_BROADCAST_ACTION);
                TopLevel.this.sendBroadcast(intent);
            }
        };
        this.mDnsHelper.init(this, this.mDNSCallback);
    }

    public void scanForSensorTagDevices() {
        Intent intent;
        String str = "AGE- index: ";
        String str2 = "Stopping mDNS discovery/scanForSensorTagDevices";
        if (!this.killMDNSScanWhenInBackground) {
            startPing();
            boolean z = this.isScanning;
            String str3 = TAG;
            if (z || this.scanningDisabled) {
                Log.i(str3, "An mDNS discovery is already in progress/scanForSensorTagDevices");
                return;
            }
            this.isScanning = true;
            this.updateDevicesArray = new ArrayList<>();
            for (int i = 0; i < this.mWifiDevices.size(); i++) {
                try {
                    wifiDevice wifidevice = (wifiDevice) this.mWifiDevices.get(i);
                    int i2 = wifidevice.age;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(i);
                    sb.append(" / old age: ");
                    sb.append(i2);
                    Log.i(str3, sb.toString());
                    if (i2 < 1) {
                        wifidevice.age = i2 + 1;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(i);
                        sb2.append(" / new age: ");
                        sb2.append(wifidevice.age);
                        Log.i(str3, sb2.toString());
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("AGE- Add item to refreshed list:");
                        sb3.append(wifidevice.toString());
                        Log.i(str3, sb3.toString());
                        this.updateDevicesArray.add(wifidevice);
                    } else {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("AGE- Should remove item:");
                        sb4.append(i);
                        Log.i(str3, sb4.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            StringBuilder sb5 = new StringBuilder();
            sb5.append("AGE- update Devices Array size:");
            sb5.append(this.updateDevicesArray.size());
            Log.i(str3, sb5.toString());
            runOnUiThread(new Runnable() {
                public void run() {
                    TopLevel topLevel = TopLevel.this;
                    topLevel.mWifiDevices = topLevel.updateDevicesArray;
                    Log.i(TopLevel.TAG, "AGE- update UI with refreshed devices list");
                    TopLevel.this.rebuildDeviceTable();
                }
            });
            try {
                Log.i(str3, "Starting mDNS discovery/scanForSensorTagDevices");
                if (this.firstTime) {
                    this.firstTime = false;
                    this.mDnsHelper.startDiscovery();
                } else {
                    this.mDnsHelper.restartDiscovery();
                }
                Thread.sleep(15000);
                if (this.isScanning && !this.scanningDisabled) {
                    Log.i(str3, str2);
                    intent = new Intent();
                    intent.setAction(SCAN_FINISHED_BROADCAST_ACTION);
                    sendBroadcast(intent);
                    this.isScanning = false;
                    scanForSensorTagDevices();
                }
            } catch (InterruptedException e2) {
                Log.e(str3, "Failed to sleep during mDNS discovery/scanForSensorTagDevices");
                e2.printStackTrace();
                if (this.isScanning && !this.scanningDisabled) {
                    Log.i(str3, str2);
                    intent = new Intent();
                }
            } catch (Throwable th) {
                if (this.isScanning && !this.scanningDisabled) {
                    Log.i(str3, str2);
                    Intent intent2 = new Intent();
                    intent2.setAction(SCAN_FINISHED_BROADCAST_ACTION);
                    sendBroadcast(intent2);
                    this.isScanning = false;
                    scanForSensorTagDevices();
                }
                throw th;
            }
        }
    }

    public void stopScanningForSensorTagDevices() {
        Intent intent;
        String str = "Stopping mDNS scan notification/stopScanningForSensorTagDevices";
        String str2 = TAG;
        stopPing();
        try {
            Log.i(str2, "Stopping mDNS discovery/stopScanningForSensorTagDevices");
            this.firstTime = true;
            this.mDnsHelper.stopDiscovery();
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
            throw th;
        }
        intent.setAction(SCAN_FINISHED_BROADCAST_ACTION);
        sendBroadcast(intent);
        this.isScanning = false;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        stopPing();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mRunnable);
        }
    }

    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.please_tap_again_to_exit), 0).show();
        this.mHandler.postDelayed(this.mRunnable, 2000);
    }

    public void checkCurrentConnectionForDisplay() {
        String str;
        String str2 = TAG;
        Log.i(str2, "checkCurrentConnectionForDisplay / Check if we are connected to an ST device as AP");
        String currentConnectionSSID = wifiDeviceActivity.getCurrentConnectionSSID(this.mThis);
        if (currentConnectionSSID == null) {
            return;
        }
        if (isSensorTagDevice(currentConnectionSSID)) {
            Log.i(str2, "checkCurrentConnectionForDisplay / We are connected to an ST device as AP");
            Log.i(str2, "checkCurrentConnectionForDisplay / Check if it is already in devices list");
            int i = 0;
            boolean z = false;
            while (true) {
                int size = this.mWifiDevices.size();
                str = IP_ADDRESS;
                if (i >= size) {
                    break;
                }
                wifiDevice wifidevice = (wifiDevice) this.mWifiDevices.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("checkCurrentConnectionForDisplay / mWifiDevices[");
                sb.append(i);
                sb.append("] = ");
                sb.append(wifidevice.mIPAddr);
                Log.d(str2, sb.toString());
                if (wifidevice.mIPAddr.contains(str)) {
                    Log.i(str2, "checkCurrentConnectionForDisplay / ST device already in devices list");
                    z = true;
                }
                i++;
            }
            if (!z) {
                Log.i(str2, "checkCurrentConnectionForDisplay / ST device not in devices list - add");
                wifiDevice wifidevice2 = new wifiDevice();
                wifidevice2.mIPAddr = str;
                wifidevice2.mName = currentConnectionSSID;
                this.mWifiDevices.add(wifidevice2);
                addWifiDeviceToTable(R.mipmap.sensortagwifi, currentConnectionSSID, str, R.mipmap.wifilogo);
                return;
            }
            return;
        }
        Log.i(str2, "checkCurrentConnectionForDisplay / We are NOT connected to an ST device as AP");
    }

    public static boolean isSensorTagDevice(String str) {
        return str.contains(Sensor_Tag) || str.contains(Sensor_tag) || str.contains(sensor_tag);
    }
}
