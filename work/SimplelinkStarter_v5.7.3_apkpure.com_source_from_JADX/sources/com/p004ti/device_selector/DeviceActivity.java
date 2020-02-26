package com.p004ti.device_selector;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice.BluetoothLEDeviceCB;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEManager;
import com.p004ti.ble.btsig.DeviceInformationServiceProfile;
import com.p004ti.ble.common.GattInfo;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.ble.common.RSSI.RSSIProfile;
import com.p004ti.ble.common.cloud.IBMIoTCloudProfile;
import com.p004ti.ble.common.oad.FWUpdateEOADActivity;
import com.p004ti.ble.common.oad.FWUpdateNGActivity;
import com.p004ti.ble.p005ti.profiles.TIOADProfile;
import com.p004ti.ble.sensortag.SensorTagApplicationClass;
import com.p004ti.ble.sensortag.SensorTagHumidityProfile;
import com.p004ti.ti_oad.TIOADEoadDefinitions;
import com.ti.ble.simplelinkstarter.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/* renamed from: com.ti.device_selector.DeviceActivity */
public class DeviceActivity extends AppCompatActivity {
    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
    private static final int FWUPDATE_ACT_REQ = 1;
    public static final int PREF_ACT_REQ = 0;
    /* access modifiers changed from: private */
    public static String TAG = "DeviceActivity";
    public static DeviceActivity mThis;
    BluetoothLEDeviceCB bluetoothLEDeviceCB = new BluetoothLEDeviceCB() {
        public void deviceConnectTimedOut(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceDiscoveryTimedOut(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceFailed(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceReady(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void didReadDescriptor(BluetoothLEDevice bluetoothLEDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
        }

        public void didUpdateCharacteristicIndication(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void didWriteCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void mtuValueChanged(int i) {
        }

        public void waitingForConnect(BluetoothLEDevice bluetoothLEDevice, int i, int i2) {
        }

        public void waitingForDiscovery(BluetoothLEDevice bluetoothLEDevice, int i, int i2) {
        }

        public void didUpdateCharacteristicData(BluetoothLEDevice bluetoothLEDevice, final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            for (int i = 0; i < DeviceActivity.mThis.mProfiles.size(); i++) {
                final GenericBluetoothProfile genericBluetoothProfile = (GenericBluetoothProfile) DeviceActivity.mThis.mProfiles.get(i);
                DeviceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        genericBluetoothProfile.didUpdateValueForCharacteristic(bluetoothGattCharacteristic);
                    }
                });
                Map mQTTMap = genericBluetoothProfile.getMQTTMap();
                if (mQTTMap != null) {
                    for (Entry entry : mQTTMap.entrySet()) {
                        if (DeviceActivity.this.cloudProfile != null) {
                            DeviceActivity.this.cloudProfile.addSensorValueToPendingMessage(entry);
                        }
                    }
                }
            }
        }

        public void didReadCharacteristicData(BluetoothLEDevice bluetoothLEDevice, final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String access$300 = DeviceActivity.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Read ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" Data :");
            sb.append(bluetoothGattCharacteristic.getValue().toString());
            Log.d(access$300, sb.toString());
            for (int i = 0; i < DeviceActivity.mThis.mProfiles.size(); i++) {
                final GenericBluetoothProfile genericBluetoothProfile = (GenericBluetoothProfile) DeviceActivity.mThis.mProfiles.get(i);
                DeviceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        genericBluetoothProfile.didReadValueForCharacteristic(bluetoothGattCharacteristic);
                    }
                });
            }
        }

        public void didUpdateCharacteristicNotification(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String access$300 = DeviceActivity.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Notification status updated ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            Log.d(access$300, sb.toString());
        }

        public void deviceDidDisconnect(BluetoothLEDevice bluetoothLEDevice) {
            Log.d(DeviceActivity.TAG, "Device disconnected when it was not supposed to !");
            DeviceActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (!DeviceActivity.mThis.isFinishing()) {
                        Builder builder = new Builder(DeviceActivity.mThis);
                        builder.setTitle((CharSequence) "Device disconnected !");
                        builder.setMessage((CharSequence) "Device disconnected abruptly, this could be because of poor signal, poor battery level or some other fault");
                        builder.setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DeviceActivity.mThis.finish();
                            }
                        });
                        builder.create().show();
                    }
                }
            });
        }

        public void didReadRSSI(final int i) {
            for (int i2 = 0; i2 < DeviceActivity.mThis.mProfiles.size(); i2++) {
                final GenericBluetoothProfile genericBluetoothProfile = (GenericBluetoothProfile) DeviceActivity.mThis.mProfiles.get(i2);
                DeviceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        genericBluetoothProfile.rssiUpdated(i);
                    }
                });
            }
        }
    };
    /* access modifiers changed from: private */
    public IBMIoTCloudProfile cloudProfile;
    private final BroadcastReceiver deviceActivityReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DeviceInformationServiceProfile.ACTION_FW_REV_UPDATED.equals(action)) {
                DeviceActivity.this.mFwRev = intent.getStringExtra(DeviceInformationServiceProfile.EXTRA_FW_REV_STRING);
                StringBuilder sb = new StringBuilder();
                sb.append("Got FW revision : ");
                sb.append(DeviceActivity.this.mFwRev);
                sb.append(" from DeviceInformationServiceProfile");
                Log.d("DeviceActivity", sb.toString());
                for (GenericBluetoothProfile didUpdateFirmwareRevision : DeviceActivity.this.mProfiles) {
                    didUpdateFirmwareRevision.didUpdateFirmwareRevision(DeviceActivity.this.mFwRev);
                }
            } else if (TIOADProfile.ACTION_PREPARE_FOR_OAD.equals(action)) {
                if (DeviceActivity.this.mBTLEDev.isSensorTag()) {
                    DeviceActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            new AlertDialog.Builder(DeviceActivity.mThis).setTitle("Information !").setMessage("CC254x Sensortag OAD upgrade is not supported anymore").setPositiveButton("OK", null).create().show();
                        }
                    });
                    return;
                }
                new Thread(new Runnable() {
                    public void run() {
                        DeviceActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    DeviceActivity.this.progressDialog = new ProgressDialog(DeviceActivity.this);
                                    DeviceActivity.this.progressDialog.setProgressStyle(1);
                                    DeviceActivity.this.progressDialog.setIndeterminate(false);
                                    DeviceActivity.this.progressDialog.setTitle("Starting firmware update");
                                    DeviceActivity.this.progressDialog.setMessage("");
                                    DeviceActivity.this.progressDialog.setMax(DeviceActivity.this.mProfiles.size());
                                    DeviceActivity.this.progressDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Integer valueOf = Integer.valueOf(1);
                        for (GenericBluetoothProfile genericBluetoothProfile : DeviceActivity.this.mProfiles) {
                            genericBluetoothProfile.disableService();
                            genericBluetoothProfile.deConfigureService();
                            try {
                                DeviceActivity.this.progressDialog.setProgress(valueOf.intValue());
                            } catch (NullPointerException unused) {
                            }
                            valueOf = Integer.valueOf(valueOf.intValue() + 1);
                        }
                        BluetoothGattService service = DeviceActivity.this.mBTLEDev.f28g.getService(GattInfo.OAD_SERVICE_UUID);
                        if (service != null) {
                            BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(TIOADEoadDefinitions.TI_OAD_IMAGE_CONTROL));
                            String str = DeviceActivity.EXTRA_DEVICE;
                            if (characteristic != null) {
                                DeviceActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        DeviceActivity.this.progressDialog.dismiss();
                                        DeviceActivity.mThis.finish();
                                    }
                                });
                                Intent intent = new Intent(DeviceActivity.mThis, FWUpdateEOADActivity.class);
                                intent.putExtra(str, DeviceActivity.this.mBluetoothDevice);
                                DeviceActivity.this.startActivity(intent);
                            } else if (DeviceActivity.this.mBTLEDev.isSensorTag()) {
                                DeviceActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        new AlertDialog.Builder(DeviceActivity.mThis).setTitle("Information !").setMessage("CC254x Sensortag OAD upgrade is not supported anymore").setPositiveButton("OK", null).create().show();
                                    }
                                });
                            } else {
                                DeviceActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        DeviceActivity.this.progressDialog.dismiss();
                                        DeviceActivity.mThis.finish();
                                    }
                                });
                                Intent intent2 = new Intent(DeviceActivity.mThis, FWUpdateNGActivity.class);
                                intent2.putExtra(str, DeviceActivity.this.mBluetoothDevice);
                                DeviceActivity.this.startActivity(intent2);
                            }
                        }
                    }
                }).start();
            }
        }
    };
    /* access modifiers changed from: private */
    public BluetoothLEDevice mBTLEDev;
    /* access modifiers changed from: private */
    public BluetoothDevice mBluetoothDevice = null;
    private BluetoothGattService mConnControlService = null;
    /* access modifiers changed from: private */
    public String mFwRev = new String("1.5");
    private boolean mIsCC1350LP;
    private boolean mIsCC2650RC;
    private boolean mOADFinished;
    private BluetoothGattService mOadService = null;
    /* access modifiers changed from: private */
    public List<GenericBluetoothProfile> mProfiles;
    /* access modifiers changed from: private */
    public TableLayout mTable;
    private BluetoothGattService mTestService = null;
    public ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public RSSIProfile rssiProfile;

    /* renamed from: com.ti.device_selector.DeviceActivity$firmwareUpdateStart */
    class firmwareUpdateStart extends AsyncTask<String, Integer, Void> {
        Context con;

        /* renamed from: pd */
        ProgressDialog f60pd;

        public firmwareUpdateStart(ProgressDialog progressDialog, Context context) {
            this.f60pd = progressDialog;
            this.con = context;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.f60pd = new ProgressDialog(DeviceActivity.this);
            this.f60pd.setProgressStyle(1);
            this.f60pd.setIndeterminate(false);
            this.f60pd.setTitle("Starting firmware update");
            this.f60pd.setMessage("");
            this.f60pd.setMax(DeviceActivity.this.mProfiles.size());
            this.f60pd.show();
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(String... strArr) {
            Integer valueOf = Integer.valueOf(1);
            for (GenericBluetoothProfile genericBluetoothProfile : DeviceActivity.this.mProfiles) {
                genericBluetoothProfile.disableService();
                genericBluetoothProfile.deConfigureService();
                publishProgress(new Integer[]{valueOf});
                valueOf = Integer.valueOf(valueOf.intValue() + 1);
            }
            Intent intent = new Intent(this.con, FWUpdateNGActivity.class);
            intent.putExtra(DeviceActivity.EXTRA_DEVICE, DeviceActivity.this.mBluetoothDevice);
            DeviceActivity.this.startActivity(intent);
            return null;
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Integer... numArr) {
            this.f60pd.setProgress(numArr[0].intValue());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            this.f60pd.dismiss();
            super.onPostExecute(voidR);
        }
    }

    private void fillServicesList() {
    }

    public DeviceActivity() {
        mThis = this;
    }

    public static DeviceActivity getInstance() {
        return mThis;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_device);
        this.mBluetoothDevice = (BluetoothDevice) getIntent().getParcelableExtra(EXTRA_DEVICE);
        this.mBTLEDev = BluetoothLEManager.getInstance(getApplicationContext()).deviceForBluetoothDev(this.mBluetoothDevice);
        this.mBTLEDev.myCB = this.bluetoothLEDeviceCB;
        setTitle(this.mBluetoothDevice.getName());
        try {
            this.mOadService = this.mBTLEDev.f28g.getService(GattInfo.OAD_SERVICE_UUID);
            this.mConnControlService = this.mBTLEDev.f28g.getService(GattInfo.CC_SERVICE_UUID);
        } catch (Exception e) {
            Log.d(TAG, "BluetoothGatt scan is not complete it seems !");
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                finish();
                break;
            case R.id.action_about /*2131230739*/:
                startActivity(new Intent(this, aboutActivity.class));
                break;
            case R.id.opt_prefs /*2131231035*/:
                Log.d(TAG, "Preferences clicked !");
                startPreferenceActivity();
                break;
            case R.id.service_explorer /*2131231082*/:
                Log.d(TAG, "Service Explorer clicked !");
                Intent intent = new Intent(this, ServiceExplorerServicesActivity.class);
                intent.putExtra("com.ti.ble.service.explorer.service.activity.BLUETOOTH_DEVICE", this.mBluetoothDevice);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void onBackPressed() {
        finish();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        IBMIoTCloudProfile iBMIoTCloudProfile = this.cloudProfile;
        if (iBMIoTCloudProfile != null && iBMIoTCloudProfile.isConnected) {
            this.cloudProfile.disconnect();
        }
        if (this.mBTLEDev.isConnected) {
            for (int i = 0; i < this.mProfiles.size(); i++) {
                GenericBluetoothProfile genericBluetoothProfile = (GenericBluetoothProfile) this.mProfiles.get(i);
                if (genericBluetoothProfile != null) {
                    genericBluetoothProfile.disableService();
                    genericBluetoothProfile.deConfigureService();
                    genericBluetoothProfile.onPause();
                }
            }
            this.mBTLEDev.disconnectDevice();
        } else {
            Log.d(TAG, "Cannot disconnect, device is already disconnected !");
        }
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.orientation != 1) {
            int i = configuration.orientation;
        }
        for (int i2 = 0; i2 < this.mTable.getChildCount(); i2++) {
            View childAt = this.mTable.getChildAt(i2);
            if (childAt != null) {
                childAt.invalidate();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.device_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        registerReceiver(this.deviceActivityReceiver, makeDeviceActivityIntentFilter());
        ((SensorTagApplicationClass) getApplicationContext()).currentActivity = mThis;
        new Thread(new Runnable() {
            public void run() {
                DeviceActivity.this.mProfiles = new ArrayList();
                DeviceActivity deviceActivity = DeviceActivity.this;
                deviceActivity.mTable = (TableLayout) deviceActivity.findViewById(R.id.generic_services_layout);
                DeviceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        DeviceActivity.this.mTable.removeAllViews();
                    }
                });
                while (DeviceActivity.this.mBTLEDev == null) {
                    try {
                        Thread.sleep(100, 0);
                        Log.d(DeviceActivity.TAG, "Waiting for device to become ready ! ");
                    } catch (InterruptedException unused) {
                    }
                }
                int i = 50;
                while (DeviceActivity.this.mBTLEDev.services == null) {
                    try {
                        Thread.sleep(100, 0);
                        Log.d(DeviceActivity.TAG, "Waiting for device to become ready !");
                    } catch (InterruptedException unused2) {
                    }
                    i--;
                    if (i < 0) {
                        DeviceActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                TextView textView = new TextView(((SensorTagApplicationClass) DeviceActivity.this.getApplicationContext()).currentActivity);
                                textView.setText("Device has no services, press back to go back to device scanner");
                                textView.setTextColor(R.color.colorPrimaryDark);
                                DeviceActivity.this.mTable.addView(textView);
                                DeviceActivity.this.mBTLEDev.disconnectDevice();
                            }
                        });
                        return;
                    }
                }
                DeviceActivity deviceActivity2 = DeviceActivity.this;
                deviceActivity2.cloudProfile = new IBMIoTCloudProfile(((SensorTagApplicationClass) deviceActivity2.getApplicationContext()).currentActivity, DeviceActivity.this.mBTLEDev.f27d, null);
                DeviceActivity.this.mProfiles.add(DeviceActivity.this.cloudProfile);
                DeviceActivity.this.cloudProfile.configureService();
                DeviceActivity.this.cloudProfile.enableService();
                DeviceActivity.this.cloudProfile.onResume();
                DeviceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        DeviceActivity.this.mTable.addView(DeviceActivity.this.cloudProfile.getTableRow());
                    }
                });
                DeviceActivity deviceActivity3 = DeviceActivity.this;
                deviceActivity3.rssiProfile = new RSSIProfile(((SensorTagApplicationClass) deviceActivity3.getApplicationContext()).currentActivity, DeviceActivity.this.mBTLEDev.f27d, null);
                DeviceActivity.this.mProfiles.add(DeviceActivity.this.rssiProfile);
                DeviceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        DeviceActivity.this.mTable.addView(DeviceActivity.this.rssiProfile.getTableRow());
                    }
                });
                for (BluetoothGattService bluetoothGattService : DeviceActivity.this.mBTLEDev.services) {
                    final GenericBluetoothProfile bluetoothProfileForUUID = DeviceActivity.this.mBTLEDev.getBluetoothProfileForUUID(bluetoothGattService);
                    if (SensorTagHumidityProfile.isCorrectService(bluetoothGattService)) {
                        final SensorTagHumidityProfile sensorTagHumidityProfile = new SensorTagHumidityProfile(DeviceActivity.mThis, DeviceActivity.this.mBTLEDev.f27d, bluetoothGattService);
                        DeviceActivity.this.mProfiles.add(sensorTagHumidityProfile);
                        bluetoothProfileForUUID.configureService();
                        bluetoothProfileForUUID.enableService();
                        bluetoothProfileForUUID.onResume();
                        DeviceActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                DeviceActivity.this.mTable.addView(sensorTagHumidityProfile.getTableRow());
                            }
                        });
                    } else if (bluetoothProfileForUUID != null) {
                        DeviceActivity.this.mProfiles.add(bluetoothProfileForUUID);
                        bluetoothProfileForUUID.configureService();
                        bluetoothProfileForUUID.enableService();
                        bluetoothProfileForUUID.onResume();
                        DeviceActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                DeviceActivity.this.mTable.addView(bluetoothProfileForUUID.getTableRow());
                            }
                        });
                    }
                }
            }
        }).start();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        unregisterReceiver(this.deviceActivityReceiver);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    private static IntentFilter makeDeviceActivityIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DeviceInformationServiceProfile.ACTION_FW_REV_UPDATED);
        intentFilter.addAction(TIOADProfile.ACTION_PREPARE_FOR_OAD);
        return intentFilter;
    }

    public boolean isSensorTag2() {
        return this.mBTLEDev.isSensorTag2();
    }

    public String firmwareRevision() {
        return this.mFwRev;
    }

    public BluetoothGattService getOadService() {
        return this.mOadService;
    }

    public BluetoothGattService getConnControlService() {
        return this.mConnControlService;
    }

    /* access modifiers changed from: 0000 */
    public BluetoothGattService getTestService() {
        return this.mTestService;
    }

    private void startPreferenceActivity() {
        Intent intent = new Intent(this, PreferencesActivity.class);
        intent.putExtra(":android:show_fragment", PreferencesFragment.class.getName());
        intent.putExtra(":android:no_headers", true);
        intent.putExtra(EXTRA_DEVICE, this.mBluetoothDevice);
        startActivityForResult(intent, 0);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    private void setError(String str) {
        Toast.makeText(this, str, 1).show();
    }

    private void setStatus(String str) {
        Toast.makeText(this, str, 0).show();
    }
}
