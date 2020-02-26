package com.p004ti.ble.audio;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.p000v4.content.LocalBroadcastManager;
import android.util.Log;
import com.p004ti.ble.btsig.DeviceInformationServiceProfile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/* renamed from: com.ti.ble.audio.AdvancedRemoteBLEAudioService */
public class AdvancedRemoteBLEAudioService extends Service {
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    protected static final UUID CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    private static final String TAG = "service";
    private BroadcastReceiver AdvancedRemoteBLEAudioServiceMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("message", -1);
            int intExtra2 = intent.getIntExtra("device", -1);
            int intExtra3 = intent.getIntExtra("paired", -1);
            String stringExtra = intent.getStringExtra("btaddr");
            if (intExtra >= 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("Got Message ID : ");
                sb.append(intExtra);
                Log.d("info", sb.toString());
                if (intExtra == 1) {
                    AdvancedRemoteBLEAudioService.this.scanForBLEDevices();
                } else if (intExtra == 2) {
                } else {
                    if (intExtra == 3) {
                        AdvancedRemoteBLEAudioService.this.killCommunications();
                    } else if (intExtra != 4) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Received unknown message id : ");
                        sb2.append(intExtra);
                        Log.d(MqttServiceConstants.TRACE_ERROR, sb2.toString());
                    } else {
                        AdvancedRemoteBLEAudioService.this.enableAudio();
                    }
                }
            } else {
                String str = "]";
                String str2 = " [";
                String str3 = ") - ";
                String str4 = "Connecting To (";
                String str5 = "service";
                if (intExtra2 >= 0) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Got deviceID : ");
                    sb3.append(intExtra2);
                    Log.d(str5, sb3.toString());
                    BluetoothDevice bluetoothDevice = (BluetoothDevice) AdvancedRemoteBLEAudioService.this.mBluetoothDevices.get(intExtra2);
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str4);
                    sb4.append(intExtra2);
                    sb4.append(str3);
                    sb4.append(bluetoothDevice.getName());
                    sb4.append(str2);
                    sb4.append(bluetoothDevice.getAddress());
                    sb4.append(str);
                    Log.d(str5, sb4.toString());
                    AdvancedRemoteBLEAudioService.this.initiateRemoteCommunication(bluetoothDevice);
                } else if (intExtra3 >= 0) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Got PairedId : ");
                    sb5.append(intExtra3);
                    Log.d(str5, sb5.toString());
                    BluetoothDevice bluetoothDevice2 = (BluetoothDevice) AdvancedRemoteBLEAudioService.this.mPairedDevices.get(intExtra3);
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str4);
                    sb6.append(intExtra2);
                    sb6.append(str3);
                    sb6.append(bluetoothDevice2.getName());
                    sb6.append(str2);
                    sb6.append(bluetoothDevice2.getAddress());
                    sb6.append(str);
                    Log.d(str5, sb6.toString());
                    AdvancedRemoteBLEAudioService.this.initiateRemoteCommunication(bluetoothDevice2);
                } else if (stringExtra != null) {
                    AdvancedRemoteBLEAudioService.this.mBtAddr = stringExtra;
                    AdvancedRemoteBLEAudioService.this.scanForBLEDevices();
                    if (AdvancedRemoteBLEAudioService.this.mPairedDevices != null) {
                        for (int i = 0; i < AdvancedRemoteBLEAudioService.this.mPairedDevices.size(); i++) {
                            BluetoothDevice bluetoothDevice3 = (BluetoothDevice) AdvancedRemoteBLEAudioService.this.mPairedDevices.get(i);
                            if (bluetoothDevice3.getAddress().equals(AdvancedRemoteBLEAudioService.this.mBtAddr)) {
                                Log.d(str5, "Device found in paired devices, connecting");
                                AdvancedRemoteBLEAudioService.this.initiateRemoteCommunication(bluetoothDevice3);
                            }
                        }
                    }
                }
            }
        }
    };
    public BluetoothGattCharacteristic audioConfigChar;
    public BluetoothGattCharacteristic audioCtrlChar;
    public BluetoothGattService audioService;
    public BluetoothGattCharacteristic audioStreamChar;
    /* access modifiers changed from: private */
    public AdvancedRemoteBLEAudioHandler mAudioHandler;
    private BluetoothAdapter mBluetoothAdapter;
    /* access modifiers changed from: private */
    public ArrayList<BluetoothDevice> mBluetoothDevices;
    /* access modifiers changed from: private */
    public BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    /* access modifiers changed from: private */
    public String mBtAddr;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic mConControlCurParam;
    /* access modifiers changed from: private */
    public BluetoothGattService mConControlService;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic mConControlSetParam;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            String str = "statusText";
            String str2 = "ARCBLEAudio-From-Service-Events";
            String str3 = "service";
            if (i2 == 2) {
                AdvancedRemoteBLEAudioService.this.broadcastUpdate(AdvancedRemoteBLEAudioService.ACTION_GATT_CONNECTED);
                StringBuilder sb = new StringBuilder();
                sb.append("Connected to GATT server. Status : ");
                sb.append(i);
                Log.i(str3, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Attempting to start service discovery:");
                sb2.append(AdvancedRemoteBLEAudioService.this.mBluetoothGatt.discoverServices());
                Log.i(str3, sb2.toString());
                Intent intent = new Intent(str2);
                intent.putExtra(str, "Connected");
                LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(intent);
                if (VERSION.SDK_INT >= 21) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Requested connection priority HIGH, result : ");
                    sb3.append(AdvancedRemoteBLEAudioService.this.mBluetoothGatt.requestConnectionPriority(1));
                    Log.d("AdvancedRemoteBLEAudioService", sb3.toString());
                }
            } else if (i2 == 0) {
                Intent intent2 = new Intent(str2);
                intent2.putExtra(str, "Disconnected");
                LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(intent2);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Disconnected from GATT server. Status : ");
                sb4.append(i);
                Log.i(str3, sb4.toString());
                AdvancedRemoteBLEAudioService.this.broadcastUpdate(AdvancedRemoteBLEAudioService.ACTION_GATT_DISCONNECTED);
                bluetoothGatt.connect();
            }
        }

        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            String str = "service";
            if (i == 0) {
                AdvancedRemoteBLEAudioService.this.broadcastUpdate(AdvancedRemoteBLEAudioService.ACTION_GATT_SERVICES_DISCOVERED);
                AdvancedRemoteBLEAudioService advancedRemoteBLEAudioService = AdvancedRemoteBLEAudioService.this;
                advancedRemoteBLEAudioService.mConControlService = advancedRemoteBLEAudioService.mBluetoothGatt.getService(UUID.fromString("f000ccc0-0451-4000-b000-000000000000"));
                if (AdvancedRemoteBLEAudioService.this.mConControlService != null) {
                    Log.d(str, "Found device with Connection control service, using it !");
                    AdvancedRemoteBLEAudioService advancedRemoteBLEAudioService2 = AdvancedRemoteBLEAudioService.this;
                    advancedRemoteBLEAudioService2.mConControlCurParam = advancedRemoteBLEAudioService2.mConControlService.getCharacteristic(UUID.fromString("f000ccc1-0451-4000-b000-000000000000"));
                    AdvancedRemoteBLEAudioService advancedRemoteBLEAudioService3 = AdvancedRemoteBLEAudioService.this;
                    advancedRemoteBLEAudioService3.mConControlSetParam = advancedRemoteBLEAudioService3.mConControlService.getCharacteristic(UUID.fromString("f000ccc2-0451-4000-b000-000000000000"));
                }
                Log.d(str, "Audio notification enabled !");
                AdvancedRemoteBLEAudioService.this.enableAudio();
                Intent intent = new Intent("ARCBLEAudio-From-Service-Events");
                intent.putExtra("statusText", "Enabling audio");
                LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(intent);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("onServicesDiscovered received: ");
            sb.append(i);
            Log.w(str, sb.toString());
        }

        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            if (bluetoothGattCharacteristic == AdvancedRemoteBLEAudioService.this.audioStreamChar) {
                byte[] value = bluetoothGattCharacteristic.getValue();
                try {
                    AdvancedRemoteBLEAudioService.this.mAudioHandler.rxFrame(value, value.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String str = "service";
                if (bluetoothGattCharacteristic == AdvancedRemoteBLEAudioService.this.audioCtrlChar) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Audio control characteristic updated : ");
                    sb.append(bluetoothGattCharacteristic.getValue().toString());
                    Log.d(str, sb.toString());
                } else if (bluetoothGattCharacteristic == AdvancedRemoteBLEAudioService.this.mConControlCurParam) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Connection control, received current parameters :");
                    sb2.append(AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue());
                    Log.d(str, sb2.toString());
                    byte[] value2 = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue();
                    String str2 = "ARCBLEAudio-From-Service-Events";
                    Intent intent = new Intent(str2);
                    intent.putExtra("conInterval", value2[0]);
                    LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(intent);
                    byte[] value3 = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue();
                    if (((double) (value3[0] | (value3[1] << 8))) <= 8.0d) {
                        return;
                    }
                    if (!AdvancedRemoteBLEAudioService.this.triedLoweringConnectionInterval) {
                        AdvancedRemoteBLEAudioService.this.lowerConnectionInterval();
                        return;
                    }
                    Intent intent2 = new Intent(str2);
                    intent2.putExtra("statusText", "Failed to set connection interval");
                    LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(intent2);
                }
            }
        }

        public void printAudioPacket(byte[] bArr) {
            String str = new String();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("Pkt: ");
            String sb2 = sb.toString();
            for (byte b : bArr) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append(String.format("%02x", new Object[]{Integer.valueOf(b & 255)}));
                String sb4 = sb3.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(sb4);
                sb5.append(",");
                sb2 = sb5.toString();
            }
            Log.d("service", sb2);
        }

        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            StringBuilder sb = new StringBuilder();
            sb.append("r");
            sb.append(bluetoothGattCharacteristic.getStringValue(0));
            sb.append(" uuid: ");
            sb.append(bluetoothGattCharacteristic.getUuid());
            String str = "service";
            Log.d(str, sb.toString());
            if (i == 0 && bluetoothGattCharacteristic == AdvancedRemoteBLEAudioService.this.mConControlCurParam) {
                byte[] value = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Connection control, received current parameters :");
                sb2.append(value[0]);
                String str2 = " ";
                sb2.append(str2);
                sb2.append(value[1]);
                sb2.append(str2);
                sb2.append(value[2]);
                sb2.append(str2);
                sb2.append(value[3]);
                sb2.append(str2);
                sb2.append(value[4]);
                sb2.append(str2);
                sb2.append(value[5]);
                Log.d(str, sb2.toString());
                String str3 = "ARCBLEAudio-From-Service-Events";
                Intent intent = new Intent(str3);
                intent.putExtra("conInterval", value[0]);
                LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(intent);
                byte[] value2 = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue();
                if (((double) (value2[0] | (value2[1] << 8))) <= 8.0d) {
                    return;
                }
                if (!AdvancedRemoteBLEAudioService.this.triedLoweringConnectionInterval) {
                    AdvancedRemoteBLEAudioService.this.lowerConnectionInterval();
                    return;
                }
                Intent intent2 = new Intent(str3);
                intent2.putExtra("statusText", "Failed to set connection interval");
                LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(intent2);
            }
        }

        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            Log.d("service", "Did Read Descriptor");
        }

        public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            StringBuilder sb = new StringBuilder();
            sb.append("Did Write Descriptor for characteristic ");
            sb.append(bluetoothGattDescriptor.getCharacteristic().getUuid());
            Log.d("service", sb.toString());
            if (bluetoothGattDescriptor.getCharacteristic().getUuid().toString().equals(AdvancedRemoteBLEAudioDefines.AudioStreamUUID)) {
                if (AdvancedRemoteBLEAudioService.this.mConControlCurParam != null) {
                    new Runnable() {
                        public void run() {
                            Log.d("service", "Reading Connection parameters from device");
                            AdvancedRemoteBLEAudioService.this.mBluetoothGatt.setCharacteristicNotification(AdvancedRemoteBLEAudioService.this.mConControlCurParam, true);
                            BluetoothGattDescriptor descriptor = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getDescriptor(AdvancedRemoteBLEAudioService.CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            AdvancedRemoteBLEAudioService.this.mBluetoothGatt.writeDescriptor(descriptor);
                        }
                    }.run();
                }
            } else if (bluetoothGattDescriptor.getCharacteristic().getUuid().toString().equals("f000ccc1-0451-4000-b000-000000000000")) {
                AdvancedRemoteBLEAudioService.this.mBluetoothGatt.setCharacteristicNotification(AdvancedRemoteBLEAudioService.this.audioCtrlChar, true);
                BluetoothGattDescriptor descriptor = AdvancedRemoteBLEAudioService.this.audioCtrlChar.getDescriptor(AdvancedRemoteBLEAudioService.CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                AdvancedRemoteBLEAudioService.this.mBluetoothGatt.writeDescriptor(descriptor);
            } else if (bluetoothGattDescriptor.getCharacteristic().getUuid().toString().equals(AdvancedRemoteBLEAudioDefines.AudioControlUUID)) {
                AdvancedRemoteBLEAudioService.this.mBluetoothGatt.readCharacteristic(AdvancedRemoteBLEAudioService.this.mConControlCurParam);
            }
        }

        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            StringBuilder sb = new StringBuilder();
            sb.append("Wrote Characteristic : ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" Status : ");
            sb.append(i);
            Log.d("service", sb.toString());
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<BluetoothDevice> mPairedDevices;
    private ScanCallback mScanCallback = new ScanCallback() {
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            StringBuilder sb = new StringBuilder();
            sb.append("onLeScan() -> dev:");
            sb.append(scanResult.getDevice().getAddress());
            sb.append(" name:");
            sb.append(scanResult.getDevice().getName());
            Log.d("service", sb.toString());
            if (AdvancedRemoteBLEAudioService.this.mBluetoothDevices == null) {
                AdvancedRemoteBLEAudioService.this.mBluetoothDevices = new ArrayList();
            }
            if (AdvancedRemoteBLEAudioService.this.mBluetoothDevices.indexOf(scanResult.getDevice()) < 0) {
                AdvancedRemoteBLEAudioService.this.mBluetoothDevices.add(scanResult.getDevice());
                AdvancedRemoteBLEAudioService.this.sendDevice(scanResult.getDevice());
            }
            if (AdvancedRemoteBLEAudioService.this.mBtAddr.equals(scanResult.getDevice().getAddress())) {
                Log.d("AdvancedRemoteBLEAudioService", "Found correct device, connecting ...");
                AdvancedRemoteBLEAudioService.this.initiateRemoteCommunication(scanResult.getDevice());
            }
        }
    };
    public boolean triedLoweringConnectionInterval;

    public byte[] buildConnectionParameters(int i, int i2, int i3, int i4) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) (i2 & 255), (byte) ((i2 >> 8) & 255), (byte) (i3 & 255), (byte) ((i3 >> 8) & 255), (byte) (i4 & 255), (byte) ((i4 >> 8) & 255)};
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendMsg(int i) {
        Intent intent = new Intent("ARCBLEAudio-From-Service-Events");
        intent.putExtra("message", i);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void sendDevice(BluetoothDevice bluetoothDevice) {
        Intent intent = new Intent("ARCBLEAudio-From-Service-Events");
        intent.putExtra("device", bluetoothDevice);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void sendPaired(BluetoothDevice bluetoothDevice) {
        Intent intent = new Intent("ARCBLEAudio-From-Service-Events");
        intent.putExtra("paired", bluetoothDevice);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void onCreate() {
        killCommunications();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.AdvancedRemoteBLEAudioServiceMessageReceiver, new IntentFilter("ARCBLEAudio-To-Service-Events"));
        Log.d("service", "Advanced Remote BLE Audio Service Created !");
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
        }
        if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        }
        fillPairedDeviceList();
        this.mAudioHandler = new AdvancedRemoteBLEAudioHandler(100, this);
    }

    public void fillPairedDeviceList() {
        Set<BluetoothDevice> bondedDevices = this.mBluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : bondedDevices) {
                if (bluetoothDevice.getType() == 2) {
                    if (this.mPairedDevices == null) {
                        this.mPairedDevices = new ArrayList<>();
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("fillPairedDeviceList() -> dev:");
                    sb.append(bluetoothDevice.getAddress());
                    sb.append(" name:");
                    sb.append(bluetoothDevice.getName());
                    Log.d("service", sb.toString());
                    if (this.mPairedDevices.indexOf(bluetoothDevice) < 0) {
                        this.mPairedDevices.add(bluetoothDevice);
                        sendPaired(bluetoothDevice);
                    }
                }
            }
        }
    }

    public void initiateRemoteCommunication(BluetoothDevice bluetoothDevice) {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
        }
        if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        }
        this.mBluetoothAdapter.getBluetoothLeScanner().stopScan(this.mScanCallback);
        this.mBluetoothGatt = bluetoothDevice.connectGatt(this, true, this.mGattCallback);
    }

    public void killCommunications() {
        Log.d("info", "Killing communications");
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
            this.mBluetoothAdapter.getBluetoothLeScanner().stopScan(this.mScanCallback);
            this.mBluetoothAdapter = null;
            this.mBluetoothManager = null;
        }
    }

    public void scanForBLEDevices() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
        }
        if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        }
        this.mBluetoothAdapter.getBluetoothLeScanner().startScan(this.mScanCallback);
        sendMsg(3);
        fillPairedDeviceList();
    }

    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.AdvancedRemoteBLEAudioServiceMessageReceiver);
        super.onDestroy();
    }

    private void readDeviceInformationService() {
        this.mBluetoothGatt.readCharacteristic(this.mBluetoothGatt.getService(UUID.fromString(DeviceInformationServiceProfile.dISService_UUID)).getCharacteristic(UUID.fromString(DeviceInformationServiceProfile.dISManifacturerNAME_UUID)));
    }

    /* access modifiers changed from: private */
    public void enableAudio() {
        this.audioService = this.mBluetoothGatt.getService(UUID.fromString(AdvancedRemoteBLEAudioDefines.AudioServiceUUID));
        this.audioCtrlChar = this.audioService.getCharacteristic(UUID.fromString(AdvancedRemoteBLEAudioDefines.AudioControlUUID));
        this.audioStreamChar = this.audioService.getCharacteristic(UUID.fromString(AdvancedRemoteBLEAudioDefines.AudioStreamUUID));
        if (this.audioStreamChar != null) {
            new Runnable() {
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Enabled audioStrmChar : ");
                    sb.append(AdvancedRemoteBLEAudioService.this.mBluetoothGatt.setCharacteristicNotification(AdvancedRemoteBLEAudioService.this.audioStreamChar, true));
                    String str = "service";
                    Log.d(str, sb.toString());
                    BluetoothGattDescriptor descriptor = AdvancedRemoteBLEAudioService.this.audioStreamChar.getDescriptor(AdvancedRemoteBLEAudioService.CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Enabled descriptor : ");
                    sb2.append(descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
                    Log.d(str, sb2.toString());
                    AdvancedRemoteBLEAudioService.this.mBluetoothGatt.writeDescriptor(descriptor);
                    Log.d(str, descriptor.getUuid().toString());
                }
            }.run();
        }
        readDeviceInformationService();
    }

    /* access modifiers changed from: private */
    public void broadcastUpdate(String str) {
        sendBroadcast(new Intent(str));
    }

    public void lowerConnectionInterval() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("service", "Current connection interval to high, trying to set lower");
                AdvancedRemoteBLEAudioService.this.mConControlSetParam.setValue(AdvancedRemoteBLEAudioService.this.buildConnectionParameters(8, 8, 1, 50));
                AdvancedRemoteBLEAudioService.this.mBluetoothGatt.writeCharacteristic(AdvancedRemoteBLEAudioService.this.mConControlSetParam);
                AdvancedRemoteBLEAudioService.this.triedLoweringConnectionInterval = true;
            }
        }).start();
    }
}
