package com.p004ti.ble.bluetooth_le_controller;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.p000v4.view.InputDeviceCompat;
import android.util.Log;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLETransaction.BluetoothLETransactionType;
import com.p004ti.ble.btsig.DeviceInformationServiceProfile;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.ble.dmm.FifteenFour.TIDMMFifteenFourRemoteDisplayService;
import com.p004ti.ble.dmm.TIDMMProvisioningService;
import com.p004ti.ble.dmm.ZigBee.TIDMMZigBeeLightSwitchService;
import com.p004ti.ble.launchpad.ProjectZeroLEDProfile;
import com.p004ti.ble.launchpad.ProjectZeroSwitchProfile;
import com.p004ti.ble.launchpad_sensor_tag.LpstkAccelerometerService;
import com.p004ti.ble.launchpad_sensor_tag.LpstkBatteryService;
import com.p004ti.ble.launchpad_sensor_tag.LpstkHallService;
import com.p004ti.ble.p005ti.profiles.TIAudioProfile;
import com.p004ti.ble.p005ti.profiles.TIConnectionControlService;
import com.p004ti.ble.p005ti.profiles.TILampControlProfile;
import com.p004ti.ble.p005ti.profiles.TIOADProfile;
import com.p004ti.ble.p005ti.profiles.TIOADResetService;
import com.p004ti.ble.p005ti.profiles.ThroughputTestService;
import com.p004ti.ble.sensortag.SensorTagAccelerometerProfile;
import com.p004ti.ble.sensortag.SensorTagAmbientTemperatureProfile;
import com.p004ti.ble.sensortag.SensorTagBarometerProfile;
import com.p004ti.ble.sensortag.SensorTagDisplayProfile;
import com.p004ti.ble.sensortag.SensorTagHumidityProfile;
import com.p004ti.ble.sensortag.SensorTagIRTemperatureProfile;
import com.p004ti.ble.sensortag.SensorTagLuxometerProfile;
import com.p004ti.ble.sensortag.SensorTagMovementProfile;
import com.p004ti.ble.sensortag.SensorTagSimpleKeysProfile;
import com.p004ti.device_selector.PreferenceWR;
import com.p004ti.device_selector.TopLevel;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jmdns.impl.constants.DNSConstants;

/* renamed from: com.ti.ble.bluetooth_le_controller.BluetoothLEDevice */
public class BluetoothLEDevice {
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int DISCOVERY_TIMEOUT = 10000;
    public static final int MAX_RETRIES = 4;
    static final String TAG = BluetoothLEDevice.class.getSimpleName();
    BluetoothGattCallback BluetoothLEDeviceCB = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onConnectionStateChange(bluetoothGatt, i, i2);
            String str = "Device ";
            if (i2 == 2) {
                String str2 = BluetoothLEDevice.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(bluetoothGatt.getDevice().getAddress().toString());
                sb.append(" CONNECTED");
                Log.d(str2, sb.toString());
                BluetoothLEDevice bluetoothLEDevice = BluetoothLEDevice.this;
                bluetoothLEDevice.isConnected = true;
                if (bluetoothLEDevice.refreshDeviceCache()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException unused) {
                    }
                }
                bluetoothGatt.discoverServices();
                if (BluetoothLEDevice.this.mThis.f28g == null) {
                    BluetoothLEDevice.this.mThis.f28g = bluetoothGatt;
                    Log.d(BluetoothLEDevice.TAG, "Did not have BluetoothGatt Property set correctly !");
                }
            } else if (i2 == 3) {
                String str3 = BluetoothLEDevice.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(bluetoothGatt.getDevice().getAddress().toString());
                sb2.append(" DISCONNECTING");
                Log.d(str3, sb2.toString());
                BluetoothLEDevice bluetoothLEDevice2 = BluetoothLEDevice.this;
                bluetoothLEDevice2.isConnected = false;
                bluetoothLEDevice2.isDiscovered = false;
            } else if (i2 == 0) {
                BluetoothLEDevice bluetoothLEDevice3 = BluetoothLEDevice.this;
                bluetoothLEDevice3.isConnected = false;
                bluetoothLEDevice3.isDiscovered = false;
                String str4 = BluetoothLEDevice.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(bluetoothGatt.getDevice().getAddress().toString());
                sb3.append(" DISCONNECTED");
                Log.d(str4, sb3.toString());
                if (BluetoothLEDevice.this.shouldReconnect) {
                    BluetoothLEDevice.this.connectDevice();
                    BluetoothLEDevice.this.isConnected = false;
                    return;
                }
                bluetoothGatt.close();
                BluetoothLEDevice.this.mThis.myCB.deviceDidDisconnect(BluetoothLEDevice.this.mThis);
            }
        }

        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            super.onServicesDiscovered(bluetoothGatt, i);
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            String str2 = "Device ";
            sb.append(str2);
            sb.append(bluetoothGatt.getDevice().getAddress().toString());
            sb.append(" SERVICES DISCOVEREDStatus");
            sb.append(i);
            Log.d(str, sb.toString());
            if (i != 0) {
                String str3 = BluetoothLEDevice.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(bluetoothGatt.getDevice().getAddress().toString());
                sb2.append("Service Discovery FAILED !");
                Log.d(str3, sb2.toString());
                return;
            }
            BluetoothLEDevice.this.services = bluetoothGatt.getServices();
            for (BluetoothGattService characteristics : BluetoothLEDevice.this.services) {
                for (BluetoothGattCharacteristic add : characteristics.getCharacteristics()) {
                    BluetoothLEDevice.this.chars.add(add);
                }
            }
            BluetoothLEDevice.this.PrintAllServicesAndCharacteristics();
            BluetoothLEDevice bluetoothLEDevice = BluetoothLEDevice.this;
            bluetoothLEDevice.TransactionHandlerThread = new Thread(bluetoothLEDevice.deviceTransactionHandler);
            BluetoothLEDevice.this.TransactionHandlerThread.start();
            BluetoothLEDevice bluetoothLEDevice2 = BluetoothLEDevice.this;
            bluetoothLEDevice2.RSSIReadThread = new Thread(bluetoothLEDevice2.deviceReadRSSIHandler);
            BluetoothLEDevice.this.RSSIReadThread.start();
            BluetoothLEDevice bluetoothLEDevice3 = BluetoothLEDevice.this;
            bluetoothLEDevice3.isDiscovered = true;
            if (bluetoothLEDevice3.myCB != null) {
                BluetoothLEDevice.this.myCB.deviceReady(BluetoothLEDevice.this.mThis);
            }
        }

        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            super.onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic, i);
            if (!(BluetoothLEDevice.this.currentTransaction == null || BluetoothLEDevice.this.currentTransaction.transactionFinished)) {
                BluetoothLEDevice.this.currentTransaction.transactionFinished = true;
                BluetoothLEDevice.this.deviceTransactions.remove(BluetoothLEDevice.this.currentTransaction);
                BluetoothLEDevice.this.currentTransaction = null;
            }
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onCharacteristicRead: Read ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            Log.d(str, sb.toString());
            if (BluetoothLEDevice.this.myCB != null) {
                BluetoothLEDevice.this.myCB.didReadCharacteristicData(BluetoothLEDevice.this.mThis, bluetoothGattCharacteristic);
            }
        }

        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            super.onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic, i);
            if (!(BluetoothLEDevice.this.currentTransaction == null || BluetoothLEDevice.this.currentTransaction.transactionFinished)) {
                BluetoothLEDevice.this.currentTransaction.transactionFinished = true;
                BluetoothLEDevice.this.deviceTransactions.remove(BluetoothLEDevice.this.currentTransaction);
                BluetoothLEDevice.this.currentTransaction = null;
            }
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onCharacteristicWrite: Wrote to ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            Log.d(str, sb.toString());
            if (BluetoothLEDevice.this.myCB != null) {
                BluetoothLEDevice.this.myCB.didWriteCharacteristicData(BluetoothLEDevice.this.mThis, bluetoothGattCharacteristic);
            }
        }

        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
            if (BluetoothLEDevice.this.myCB != null) {
                BluetoothLEDevice.this.myCB.didUpdateCharacteristicData(BluetoothLEDevice.this.mThis, bluetoothGattCharacteristic);
            }
        }

        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            super.onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor, i);
            if (!(BluetoothLEDevice.this.currentTransaction == null || BluetoothLEDevice.this.currentTransaction.transactionFinished)) {
                BluetoothLEDevice.this.currentTransaction.transactionFinished = true;
                BluetoothLEDevice.this.deviceTransactions.remove(BluetoothLEDevice.this.currentTransaction);
                BluetoothLEDevice.this.currentTransaction = null;
            }
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onDescriptorRead: Read ");
            sb.append(bluetoothGattDescriptor.getUuid().toString());
            Log.d(str, sb.toString());
            if (BluetoothLEDevice.this.myCB != null) {
                BluetoothLEDevice.this.myCB.didReadDescriptor(BluetoothLEDevice.this.mThis, bluetoothGattDescriptor);
            }
        }

        public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            super.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor, i);
            if (!(BluetoothLEDevice.this.currentTransaction == null || BluetoothLEDevice.this.currentTransaction.transactionFinished)) {
                BluetoothLEDevice.this.currentTransaction.transactionFinished = true;
                BluetoothLEDevice.this.deviceTransactions.remove(BluetoothLEDevice.this.currentTransaction);
                BluetoothLEDevice.this.currentTransaction = null;
            }
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onDescriptorWrite: Wrote to ");
            sb.append(bluetoothGattDescriptor.getCharacteristic().getUuid().toString());
            Log.d(str, sb.toString());
            if (BluetoothLEDevice.this.myCB != null) {
                BluetoothLEDevice.this.myCB.didUpdateCharacteristicNotification(BluetoothLEDevice.this.mThis, bluetoothGattDescriptor.getCharacteristic());
            }
        }

        public void onReliableWriteCompleted(BluetoothGatt bluetoothGatt, int i) {
            super.onReliableWriteCompleted(bluetoothGatt, i);
        }

        public void onReadRemoteRssi(BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onReadRemoteRssi(bluetoothGatt, i, i2);
            BluetoothLEDevice.this.mThis.myCB.didReadRSSI(i);
        }

        public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onMtuChanged: Got new MTU setting : MTU = ");
            sb.append(i);
            sb.append("status = ");
            sb.append(i2);
            Log.d(str, sb.toString());
            super.onMtuChanged(bluetoothGatt, i, i2);
            BluetoothLEDevice.this.myCB.mtuValueChanged(i);
        }
    };
    Thread RSSIReadThread;
    Thread TransactionHandlerThread;
    public EddystoneBeaconDecoder beaconDecoder;
    public BroadcastReceiver bondingStatusReceiver;

    /* renamed from: c */
    Context f26c;
    public List<BluetoothGattCharacteristic> chars;
    int currentConnectionPriority = 0;
    volatile BluetoothLETransaction currentTransaction;

    /* renamed from: d */
    public BluetoothDevice f27d;
    BluetoothLEDeviceDebugVariables dVars;
    /* access modifiers changed from: private */
    public Runnable deviceReadRSSIHandler = new Runnable() {
        public void run() {
            while (!BluetoothLEDevice.this.stopTransactionHandler) {
                if (BluetoothLEDevice.this.isConnected) {
                    try {
                        BluetoothLEDevice.this.mThis.f28g.readRemoteRssi();
                        Thread.sleep(250, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    public Runnable deviceTransactionHandler = new Runnable() {
        public void run() {
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("deviceTransactionHandler started for device : ");
            sb.append(BluetoothLEDevice.this.mThis.f27d.getAddress().toString());
            Log.d(str, sb.toString());
            while (!BluetoothLEDevice.this.stopTransactionHandler) {
                if (BluetoothLEDevice.this.currentTransaction != null) {
                    try {
                        long time = BluetoothLEDevice.this.currentTransaction.transactionStartDate.getTime() - new Date().getTime();
                        if (Math.abs(time) > DNSConstants.CLOSE_TIMEOUT) {
                            String str2 = BluetoothLEDevice.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Transaction has used more than ");
                            sb2.append(Math.abs(time) / 1000);
                            sb2.append(" seconds to complete !");
                            Log.d(str2, sb2.toString());
                            BluetoothLEDevice.this.currentTransaction = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(BluetoothLEDevice.TAG, "currentTransaction was freed while we where using it !");
                    }
                } else if (BluetoothLEDevice.this.deviceTransactions.size() > 0) {
                    BluetoothLEDevice bluetoothLEDevice = BluetoothLEDevice.this;
                    bluetoothLEDevice.currentTransaction = (BluetoothLETransaction) bluetoothLEDevice.deviceTransactions.get(0);
                    if (BluetoothLEDevice.this.currentTransaction != null) {
                        BluetoothLEDevice.this.currentTransaction.transactionStartDate = new Date();
                        BluetoothLEDevice bluetoothLEDevice2 = BluetoothLEDevice.this;
                        if (!bluetoothLEDevice2.commitTransactionToBT(bluetoothLEDevice2.currentTransaction)) {
                            BluetoothLEDevice.this.currentTransaction = null;
                        }
                    }
                }
                try {
                    Thread.sleep(10, 0);
                } catch (InterruptedException unused) {
                    Log.d(BluetoothLEDevice.TAG, "deviceTransactionHandler: interrupted while running");
                }
            }
        }
    };
    ArrayList<BluetoothLETransaction> deviceTransactions;

    /* renamed from: g */
    public BluetoothGatt f28g;
    public boolean isConnected;
    public boolean isDiscovered;

    /* renamed from: m */
    public BluetoothManager f29m;
    BluetoothLEDevice mThis;
    public BluetoothLEDeviceCB myCB;
    public boolean needsBroadcastScreen;

    /* renamed from: sR */
    public ScanResult f30sR;
    public List<BluetoothGattService> services;
    public boolean shouldReconnect;
    boolean stopTransactionHandler = false;

    /* renamed from: com.ti.ble.bluetooth_le_controller.BluetoothLEDevice$BluetoothLEDeviceCB */
    public interface BluetoothLEDeviceCB {
        void deviceConnectTimedOut(BluetoothLEDevice bluetoothLEDevice);

        void deviceDidDisconnect(BluetoothLEDevice bluetoothLEDevice);

        void deviceDiscoveryTimedOut(BluetoothLEDevice bluetoothLEDevice);

        void deviceFailed(BluetoothLEDevice bluetoothLEDevice);

        void deviceReady(BluetoothLEDevice bluetoothLEDevice);

        void didReadCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void didReadDescriptor(BluetoothLEDevice bluetoothLEDevice, BluetoothGattDescriptor bluetoothGattDescriptor);

        void didReadRSSI(int i);

        void didUpdateCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void didUpdateCharacteristicIndication(BluetoothLEDevice bluetoothLEDevice);

        void didUpdateCharacteristicNotification(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void didWriteCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void mtuValueChanged(int i);

        void waitingForConnect(BluetoothLEDevice bluetoothLEDevice, int i, int i2);

        void waitingForDiscovery(BluetoothLEDevice bluetoothLEDevice, int i, int i2);
    }

    /* renamed from: com.ti.ble.bluetooth_le_controller.BluetoothLEDevice$BluetoothLEDeviceDebugVariables */
    private class BluetoothLEDeviceDebugVariables {
        public int connectionCalls;
        public int disconnectionCalls;
        public int reads;
        public int writes;

        private BluetoothLEDeviceDebugVariables() {
        }
    }

    public BluetoothLEDevice(BluetoothDevice bluetoothDevice, Context context) {
        this.f27d = bluetoothDevice;
        this.f26c = context;
        this.dVars = new BluetoothLEDeviceDebugVariables();
        this.beaconDecoder = new EddystoneBeaconDecoder();
        this.chars = new ArrayList();
        this.deviceTransactions = new ArrayList<>();
        this.currentTransaction = null;
        this.mThis = this;
    }

    public void connectDevice() {
        this.dVars.connectionCalls++;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Connect called from : ");
            sb.append(stackTrace[3].getClassName());
            sb.append(" ");
            sb.append(stackTrace[3].getMethodName());
            Log.d(str, sb.toString());
        }
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 5 && !BluetoothLEDevice.this.isConnected; i++) {
                    int i2 = 5000;
                    BluetoothLEDevice bluetoothLEDevice = BluetoothLEDevice.this;
                    bluetoothLEDevice.f28g = bluetoothLEDevice.f27d.connectGatt(BluetoothLEDevice.this.f26c, false, BluetoothLEDevice.this.BluetoothLEDeviceCB);
                    while (true) {
                        if (BluetoothLEDevice.this.isConnected) {
                            break;
                        }
                        try {
                            Thread.sleep(1, 0);
                        } catch (Exception unused) {
                            Log.d(BluetoothLEDevice.TAG, "Interrupted while waiting for connect");
                        }
                        i2--;
                        if (BluetoothLEDevice.this.myCB != null && i2 % DNSConstants.PROBE_WAIT_INTERVAL == 0) {
                            BluetoothLEDevice.this.myCB.waitingForConnect(BluetoothLEDevice.this.mThis, i2, i);
                        }
                        if (i2 < 0 && BluetoothLEDevice.this.f28g != null) {
                            Log.d(BluetoothLEDevice.TAG, "Timeout while connecting");
                            BluetoothLEDevice.this.f28g.disconnect();
                            if (i == 4) {
                                if (BluetoothLEDevice.this.myCB != null) {
                                    BluetoothLEDevice.this.myCB.deviceConnectTimedOut(BluetoothLEDevice.this.mThis);
                                }
                                return;
                            }
                        }
                    }
                }
                for (int i3 = 0; i3 < 5 && !BluetoothLEDevice.this.isDiscovered; i3++) {
                    int i4 = 10000;
                    while (true) {
                        if (BluetoothLEDevice.this.isDiscovered) {
                            break;
                        }
                        try {
                            Thread.sleep(1, 0);
                        } catch (Exception unused2) {
                            Log.d(BluetoothLEDevice.TAG, "Interrupted while waiting for service discovery");
                        }
                        i4--;
                        if (BluetoothLEDevice.this.myCB != null && i4 % DNSConstants.PROBE_WAIT_INTERVAL == 0) {
                            BluetoothLEDevice.this.myCB.waitingForDiscovery(BluetoothLEDevice.this.mThis, i4, i3);
                        }
                        if (i4 < 0 && BluetoothLEDevice.this.f28g != null) {
                            Log.d(BluetoothLEDevice.TAG, "Timeout while discovering services");
                            BluetoothLEDevice.this.f28g.disconnect();
                            if (i3 == 4) {
                                if (BluetoothLEDevice.this.myCB != null) {
                                    BluetoothLEDevice.this.myCB.deviceDiscoveryTimedOut(BluetoothLEDevice.this.mThis);
                                }
                                return;
                            }
                            BluetoothLEDevice bluetoothLEDevice2 = BluetoothLEDevice.this;
                            bluetoothLEDevice2.f28g = bluetoothLEDevice2.f27d.connectGatt(BluetoothLEDevice.this.f26c, false, BluetoothLEDevice.this.BluetoothLEDeviceCB);
                        }
                    }
                }
            }
        }).start();
    }

    public void disconnectDevice() {
        this.dVars.disconnectionCalls++;
        this.stopTransactionHandler = true;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Disconnect called from : ");
            sb.append(stackTrace[3].getClassName());
            sb.append(" ");
            sb.append(stackTrace[3].getMethodName());
            Log.d(str, sb.toString());
        }
        BluetoothGatt bluetoothGatt = this.f28g;
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            return;
        }
        Log.d(TAG, "FAILURE !!!! Device did not have a BluetoothGatt when isConnected = true !");
        try {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Current state is: ");
            sb2.append(this.f29m.getConnectionState(this.f27d, 7));
            Log.d(str2, sb2.toString());
        } catch (NullPointerException unused) {
            Log.d(TAG, "Not able to read state, device was already null !");
        }
    }

    public boolean requestMTUChange(int i) {
        return this.f28g.requestMtu(i);
    }

    public boolean isPaired() {
        return this.f27d.getBondState() == 12;
    }

    public boolean startPairing() {
        this.f27d.createBond();
        return true;
    }

    public int writeCharacteristicAsync(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.WRITE_SYNC, bArr);
        this.deviceTransactions.add(bluetoothLETransaction);
        int i = 100;
        while (!bluetoothLETransaction.transactionFinished) {
            i--;
            try {
                Thread.sleep(20, 0);
                continue;
            } catch (InterruptedException unused) {
            }
            if (i < 0) {
                Log.d(TAG, "writeCharacteristicAsync: Failed to write characteristic due to timeout ...");
                return InputDeviceCompat.SOURCE_KEYBOARD;
            }
        }
        return 0;
    }

    public int writeCharacteristicAsync(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte b) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.WRITE_SYNC, new byte[]{b});
        this.deviceTransactions.add(bluetoothLETransaction);
        int i = 100;
        while (!bluetoothLETransaction.transactionFinished) {
            i--;
            try {
                Thread.sleep(20, 0);
                continue;
            } catch (InterruptedException unused) {
            }
            if (i < 0) {
                Log.d(TAG, "writeCharacteristicAsync: Failed to write characteristic due to timeout ...");
                return InputDeviceCompat.SOURCE_KEYBOARD;
            }
        }
        return 0;
    }

    public int readCharacteristicAsync(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.READ_SYNC, (byte[]) null);
        this.deviceTransactions.add(bluetoothLETransaction);
        int i = 100;
        while (!bluetoothLETransaction.transactionFinished) {
            i--;
            try {
                Thread.sleep(20, 0);
                continue;
            } catch (InterruptedException unused) {
            }
            if (i < 0) {
                Log.d(TAG, "writeCharacteristicAsync: Failed to write characteristic due to timeout ...");
                return InputDeviceCompat.SOURCE_KEYBOARD;
            }
        }
        return 0;
    }

    public int readDescriptorSync(BluetoothGattDescriptor bluetoothGattDescriptor) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattDescriptor, BluetoothLETransactionType.READ_DESC_SYNC, (byte[]) null);
        this.deviceTransactions.add(bluetoothLETransaction);
        int i = 100;
        while (!bluetoothLETransaction.transactionFinished) {
            i--;
            try {
                Thread.sleep(20, 0);
                continue;
            } catch (InterruptedException unused) {
            }
            if (i < 0) {
                Log.d(TAG, "writeCharacteristicAsync: Failed to write characteristic due to timeout ...");
                return InputDeviceCompat.SOURCE_KEYBOARD;
            }
        }
        return 0;
    }

    public int setCharacteristicNotificationAsync(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        this.deviceTransactions.add(new BluetoothLETransaction(this, bluetoothGattCharacteristic, z ? BluetoothLETransactionType.ENABLE_NOTIFICATION_ASYNC : BluetoothLETransactionType.DISABLE_NOTIFICATION_ASYNC, (byte[]) null));
        return 0;
    }

    public int setCharacteristicNotificationSync(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, z ? BluetoothLETransactionType.ENABLE_NOTIFICATION_SYNC : BluetoothLETransactionType.DISABLE_NOTIFICATION_SYNC, (byte[]) null);
        this.deviceTransactions.add(bluetoothLETransaction);
        int i = 100;
        while (!bluetoothLETransaction.transactionFinished) {
            i--;
            try {
                Thread.sleep(20, 0);
                continue;
            } catch (InterruptedException unused) {
            }
            if (i < 0) {
                Log.d(TAG, "setCharacteristicNotificationSync failed due to timeout ...");
                return InputDeviceCompat.SOURCE_KEYBOARD;
            }
        }
        return 0;
    }

    public int setCharacteristicIndicationSync(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothLETransaction bluetoothLETransaction;
        if (z) {
            bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.ENABLE_INDICATION_SYNC, (byte[]) null);
        } else {
            bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.DISABLE_INDICATION_SYNC, (byte[]) null);
        }
        this.deviceTransactions.add(bluetoothLETransaction);
        while (!bluetoothLETransaction.transactionFinished) {
            try {
                Thread.sleep(20, 0);
            } catch (InterruptedException unused) {
            }
        }
        return 0;
    }

    public int writeCharacteristicSync(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.WRITE_SYNC, bArr);
        this.deviceTransactions.add(bluetoothLETransaction);
        int i = 100;
        while (!bluetoothLETransaction.transactionFinished) {
            i--;
            try {
                Thread.sleep(20, 0);
                continue;
            } catch (InterruptedException unused) {
            }
            if (i < 0) {
                Log.d(TAG, "writeCharacteristicAsync: Failed to write characteristic due to timeout ...");
                return InputDeviceCompat.SOURCE_KEYBOARD;
            }
        }
        return 0;
    }

    public int writeCharacteristicSync(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte b) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.WRITE_SYNC, new byte[]{b});
        this.deviceTransactions.add(bluetoothLETransaction);
        int i = 100;
        while (!bluetoothLETransaction.transactionFinished) {
            i--;
            try {
                Thread.sleep(20, 0);
                continue;
            } catch (InterruptedException unused) {
            }
            if (i < 0) {
                Log.d(TAG, "writeCharacteristicAsync: Failed to write characteristic due to timeout ...");
                return InputDeviceCompat.SOURCE_KEYBOARD;
            }
        }
        return 0;
    }

    public int readCharacteristicSync(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.READ_SYNC, (byte[]) null);
        this.deviceTransactions.add(bluetoothLETransaction);
        while (!bluetoothLETransaction.transactionFinished) {
            try {
                Thread.sleep(20, 0);
            } catch (InterruptedException unused) {
            }
        }
        return 0;
    }

    public void PrintAllServicesAndCharacteristics() {
        int i = 0;
        for (BluetoothGattService bluetoothGattService : this.services) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Service[");
            sb.append(i);
            String str2 = "] : ";
            sb.append(str2);
            sb.append(bluetoothGattService.getUuid().toString());
            Log.d(str, sb.toString());
            int i2 = 0;
            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("    Characteristic[");
                sb2.append(i2);
                sb2.append(str2);
                sb2.append(bluetoothGattCharacteristic.getUuid().toString());
                Log.d(str3, sb2.toString());
                i2++;
            }
            i++;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00f1, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean commitTransactionToBT(com.p004ti.ble.bluetooth_le_controller.BluetoothLETransaction r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            android.bluetooth.BluetoothGattCharacteristic r0 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            r1 = 0
            if (r0 != 0) goto L_0x000c
            android.bluetooth.BluetoothGattDescriptor r0 = r5.descriptor     // Catch:{ all -> 0x00f2 }
            if (r0 != 0) goto L_0x000c
            monitor-exit(r4)
            return r1
        L_0x000c:
            int[] r0 = com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice.C06065.f31xca9914b6     // Catch:{ all -> 0x00f2 }
            com.ti.ble.bluetooth_le_controller.BluetoothLETransaction$BluetoothLETransactionType r2 = r5.transactionType     // Catch:{ all -> 0x00f2 }
            int r2 = r2.ordinal()     // Catch:{ all -> 0x00f2 }
            r0 = r0[r2]     // Catch:{ all -> 0x00f2 }
            r2 = 1
            switch(r0) {
                case 1: goto L_0x00b4;
                case 2: goto L_0x00b4;
                case 3: goto L_0x0077;
                case 4: goto L_0x0077;
                case 5: goto L_0x005c;
                case 6: goto L_0x005c;
                case 7: goto L_0x0041;
                case 8: goto L_0x0041;
                case 9: goto L_0x0036;
                case 10: goto L_0x0036;
                case 11: goto L_0x0026;
                case 12: goto L_0x0026;
                case 13: goto L_0x001d;
                default: goto L_0x001a;
            }     // Catch:{ all -> 0x00f2 }
        L_0x001a:
            r2 = 0
            goto L_0x00f0
        L_0x001d:
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattDescriptor r5 = r5.descriptor     // Catch:{ all -> 0x00f2 }
            boolean r1 = r0.readDescriptor(r5)     // Catch:{ all -> 0x00f2 }
            goto L_0x003e
        L_0x0026:
            android.bluetooth.BluetoothGattCharacteristic r0 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            byte[] r1 = r5.dat     // Catch:{ all -> 0x00f2 }
            r0.setValue(r1)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r5 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            boolean r1 = r0.writeCharacteristic(r5)     // Catch:{ all -> 0x00f2 }
            goto L_0x003e
        L_0x0036:
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r5 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            boolean r1 = r0.readCharacteristic(r5)     // Catch:{ all -> 0x00f2 }
        L_0x003e:
            r2 = r1
            goto L_0x00f0
        L_0x0041:
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r3 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            r0.setCharacteristicNotification(r3, r1)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r5 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            java.util.UUID r0 = com.p004ti.ble.common.GattInfo.CLIENT_CHARACTERISTIC_CONFIG     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattDescriptor r5 = r5.getDescriptor(r0)     // Catch:{ all -> 0x00f2 }
            byte[] r0 = android.bluetooth.BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE     // Catch:{ all -> 0x00f2 }
            r5.setValue(r0)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            r0.writeDescriptor(r5)     // Catch:{ all -> 0x00f2 }
            goto L_0x00f0
        L_0x005c:
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r3 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            r0.setCharacteristicNotification(r3, r1)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r5 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            java.util.UUID r0 = com.p004ti.ble.common.GattInfo.CLIENT_CHARACTERISTIC_CONFIG     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattDescriptor r5 = r5.getDescriptor(r0)     // Catch:{ all -> 0x00f2 }
            byte[] r0 = android.bluetooth.BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE     // Catch:{ all -> 0x00f2 }
            r5.setValue(r0)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            r0.writeDescriptor(r5)     // Catch:{ all -> 0x00f2 }
            goto L_0x00f0
        L_0x0077:
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r1 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            r0.setCharacteristicNotification(r1, r2)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r0 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            java.util.UUID r1 = com.p004ti.ble.common.GattInfo.CLIENT_CHARACTERISTIC_CONFIG     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattDescriptor r0 = r0.getDescriptor(r1)     // Catch:{ all -> 0x00f2 }
            if (r0 != 0) goto L_0x00a9
            java.lang.String r0 = TAG     // Catch:{ all -> 0x00f2 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f2 }
            r1.<init>()     // Catch:{ all -> 0x00f2 }
            java.lang.String r3 = "Set Indication failed for :"
            r1.append(r3)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r5 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            java.util.UUID r5 = r5.getUuid()     // Catch:{ all -> 0x00f2 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00f2 }
            r1.append(r5)     // Catch:{ all -> 0x00f2 }
            java.lang.String r5 = r1.toString()     // Catch:{ all -> 0x00f2 }
            android.util.Log.d(r0, r5)     // Catch:{ all -> 0x00f2 }
            goto L_0x00f0
        L_0x00a9:
            byte[] r5 = android.bluetooth.BluetoothGattDescriptor.ENABLE_INDICATION_VALUE     // Catch:{ all -> 0x00f2 }
            r0.setValue(r5)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGatt r5 = r4.f28g     // Catch:{ all -> 0x00f2 }
            r5.writeDescriptor(r0)     // Catch:{ all -> 0x00f2 }
            goto L_0x00f0
        L_0x00b4:
            android.bluetooth.BluetoothGatt r0 = r4.f28g     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r1 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            r0.setCharacteristicNotification(r1, r2)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r0 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            java.util.UUID r1 = com.p004ti.ble.common.GattInfo.CLIENT_CHARACTERISTIC_CONFIG     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattDescriptor r0 = r0.getDescriptor(r1)     // Catch:{ all -> 0x00f2 }
            if (r0 != 0) goto L_0x00e6
            java.lang.String r0 = TAG     // Catch:{ all -> 0x00f2 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f2 }
            r1.<init>()     // Catch:{ all -> 0x00f2 }
            java.lang.String r3 = "Set Notification failed for :"
            r1.append(r3)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGattCharacteristic r5 = r5.characteristic     // Catch:{ all -> 0x00f2 }
            java.util.UUID r5 = r5.getUuid()     // Catch:{ all -> 0x00f2 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00f2 }
            r1.append(r5)     // Catch:{ all -> 0x00f2 }
            java.lang.String r5 = r1.toString()     // Catch:{ all -> 0x00f2 }
            android.util.Log.d(r0, r5)     // Catch:{ all -> 0x00f2 }
            goto L_0x00f0
        L_0x00e6:
            byte[] r5 = android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE     // Catch:{ all -> 0x00f2 }
            r0.setValue(r5)     // Catch:{ all -> 0x00f2 }
            android.bluetooth.BluetoothGatt r5 = r4.f28g     // Catch:{ all -> 0x00f2 }
            r5.writeDescriptor(r0)     // Catch:{ all -> 0x00f2 }
        L_0x00f0:
            monitor-exit(r4)
            return r2
        L_0x00f2:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice.commitTransactionToBT(com.ti.ble.bluetooth_le_controller.BluetoothLETransaction):boolean");
    }

    public int getCurrentConnectionPriority() {
        return this.currentConnectionPriority;
    }

    public boolean setCurrentConnectionPriority(int i) {
        if (!this.f28g.requestConnectionPriority(i)) {
            return false;
        }
        this.currentConnectionPriority = i;
        return true;
    }

    public GenericBluetoothProfile getBluetoothProfileForUUID(BluetoothGattService bluetoothGattService) {
        if (SensorTagAmbientTemperatureProfile.isCorrectService(bluetoothGattService)) {
            return new SensorTagAmbientTemperatureProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (SensorTagIRTemperatureProfile.isCorrectService(bluetoothGattService) && !this.f27d.getName().contains("Multi-Sensor")) {
            return new SensorTagIRTemperatureProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (SensorTagAccelerometerProfile.isCorrectService(bluetoothGattService)) {
            return new SensorTagAccelerometerProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (SensorTagBarometerProfile.isCorrectService(bluetoothGattService)) {
            return new SensorTagBarometerProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (SensorTagDisplayProfile.isCorrectService(bluetoothGattService)) {
            return new SensorTagDisplayProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (SensorTagHumidityProfile.isCorrectService(bluetoothGattService)) {
            return new SensorTagHumidityProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (SensorTagLuxometerProfile.isCorrectService(bluetoothGattService)) {
            return new SensorTagLuxometerProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (SensorTagMovementProfile.isCorrectService(bluetoothGattService)) {
            return new SensorTagMovementProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (SensorTagSimpleKeysProfile.isCorrectService(bluetoothGattService)) {
            return new SensorTagSimpleKeysProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (DeviceInformationServiceProfile.isCorrectService(bluetoothGattService)) {
            return new DeviceInformationServiceProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (TIAudioProfile.isCorrectService(bluetoothGattService)) {
            return new TIAudioProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (TIConnectionControlService.isCorrectService(bluetoothGattService)) {
            return new TIConnectionControlService(this.f26c, this.f27d, bluetoothGattService);
        }
        if (TILampControlProfile.isCorrectService(bluetoothGattService)) {
            return new TILampControlProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (TIOADProfile.isCorrectService(bluetoothGattService)) {
            return new TIOADProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (ThroughputTestService.isCorrectService(bluetoothGattService)) {
            return new ThroughputTestService(this.f26c, this.f27d, bluetoothGattService);
        }
        if (ProjectZeroLEDProfile.isCorrectService(bluetoothGattService)) {
            return new ProjectZeroLEDProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (ProjectZeroSwitchProfile.isCorrectService(bluetoothGattService)) {
            return new ProjectZeroSwitchProfile(this.f26c, this.f27d, bluetoothGattService);
        }
        if (TIOADResetService.isCorrectService(bluetoothGattService)) {
            return new TIOADResetService(this.f26c, this.f27d, bluetoothGattService);
        }
        if (TIDMMProvisioningService.isCorrectService(bluetoothGattService)) {
            return new TIDMMProvisioningService(this.f26c, this.f27d, bluetoothGattService);
        }
        if (TIDMMZigBeeLightSwitchService.isCorrectService(bluetoothGattService)) {
            return new TIDMMZigBeeLightSwitchService(this.f26c, this.f27d, bluetoothGattService);
        }
        if (TIDMMFifteenFourRemoteDisplayService.isCorrectService(bluetoothGattService)) {
            return new TIDMMFifteenFourRemoteDisplayService(this.f26c, this.f27d, bluetoothGattService);
        }
        if (LpstkHallService.isCorrectService(bluetoothGattService)) {
            return new LpstkHallService(this.f26c, this.f27d, bluetoothGattService);
        }
        if (LpstkAccelerometerService.isCorrectService(bluetoothGattService)) {
            return new LpstkAccelerometerService(this.f26c, this.f27d, bluetoothGattService);
        }
        if (LpstkBatteryService.isCorrectService(bluetoothGattService)) {
            return new LpstkBatteryService(this.f26c, this.f27d, bluetoothGattService);
        }
        return null;
    }

    public boolean isSensorTag() {
        return this.f27d.getName() != null && this.f27d.getName().equals(TopLevel.Sensor_Tag);
    }

    public boolean isSensorTag2() {
        return this.f27d.getName() != null && (this.f27d.getName().equals("SensorTag2") || this.f27d.getName().equals("CC2650 SensorTag") || this.f27d.getName().equals("CC1350 SensorTag"));
    }

    public boolean is2650Launchpad() {
        return this.f27d.getName() != null && (this.f27d.getName().equals("Launchpad") || this.f27d.getName().equals("CC2650 LaunchPad"));
    }

    public boolean is1350Launchpad() {
        return this.f27d.getName() != null && this.f27d.getName().equals("CC1350 LaunchPad");
    }

    public boolean isCC2650RC() {
        return this.f27d.getName() != null && (this.f27d.getName().equals("CC2650 RC") || this.f27d.getName().equals("HID AdvRemote"));
    }

    public boolean isCC2650Programmer() {
        return this.f27d.getName() != null && this.f27d.getName().equalsIgnoreCase("CC2650 Programmer");
    }

    public boolean refreshDeviceCache() {
        PreferenceWR preferenceWR = new PreferenceWR(this.f27d.getAddress(), this.f26c);
        String str = "refresh";
        preferenceWR.setBooleanPreference(str, true);
        if (preferenceWR.getBooleanPreference(str)) {
            Log.d(TAG, "Device needs refresh !");
            try {
                Method method = this.f28g.getClass().getMethod(str, new Class[0]);
                if (method != null) {
                    if (((Boolean) method.invoke(this.f28g, new Object[0])).booleanValue()) {
                        Log.d(TAG, "Refreshed device before scanning !");
                    } else {
                        Log.d(TAG, "Unable to refresh device");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            preferenceWR.setBooleanPreference(str, false);
            return true;
        }
        Log.d(TAG, "No device refresh neccessary");
        return false;
    }
}
