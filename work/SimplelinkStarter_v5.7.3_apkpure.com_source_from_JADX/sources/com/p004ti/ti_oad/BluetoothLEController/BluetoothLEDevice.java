package com.p004ti.ti_oad.BluetoothLEController;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;
import com.p004ti.ti_oad.BluetoothLEController.BluetoothLETransaction.BluetoothLETransactionType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.jmdns.impl.constants.DNSConstants;

/* renamed from: com.ti.ti_oad.BluetoothLEController.BluetoothLEDevice */
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
                if (BluetoothLEDevice.this.mThis.f63g == null) {
                    BluetoothLEDevice.this.mThis.f63g = bluetoothGatt;
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
            bluetoothLEDevice.TransactionHandlerThread = new Thread(bluetoothLEDevice.deviceTransactionHandler, "BluetoothLEDevice Transaction Handler");
            BluetoothLEDevice.this.TransactionHandlerThread.start();
            String str4 = BluetoothLEDevice.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Transaction Handler Thread : ");
            sb3.append(BluetoothLEDevice.this.TransactionHandlerThread.toString());
            Log.d(str4, sb3.toString());
            BluetoothLEDevice bluetoothLEDevice2 = BluetoothLEDevice.this;
            bluetoothLEDevice2.isDiscovered = true;
            if (bluetoothLEDevice2.myCB != null) {
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

        public void onPhyUpdate(BluetoothGatt bluetoothGatt, int i, int i2, int i3) {
            super.onPhyUpdate(bluetoothGatt, i, i2, i3);
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPhyUpdate : New TX PHY: ");
            String str2 = "Coded";
            String str3 = "Unknown";
            String str4 = "1M";
            String str5 = "2M";
            String str6 = i == 2 ? str5 : i == 1 ? str4 : i == 3 ? str2 : str3;
            sb.append(str6);
            Log.d(str, sb.toString());
            String str7 = BluetoothLEDevice.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onPhyUpdate : New RX PHY: ");
            if (i2 == 2) {
                str2 = str5;
            } else if (i2 == 1) {
                str2 = str4;
            } else if (i2 != 3) {
                str2 = str3;
            }
            sb2.append(str2);
            Log.d(str7, sb2.toString());
            String str8 = BluetoothLEDevice.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onPhyRead : Status :");
            sb3.append(i3);
            Log.d(str8, sb3.toString());
        }

        public void onPhyRead(BluetoothGatt bluetoothGatt, int i, int i2, int i3) {
            super.onPhyRead(bluetoothGatt, i, i2, i3);
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPhyRead : New TX PHY: ");
            String str2 = "Coded";
            String str3 = "Unknown";
            String str4 = "1M";
            String str5 = "2M";
            String str6 = i == 2 ? str5 : i == 1 ? str4 : i == 3 ? str2 : str3;
            sb.append(str6);
            Log.d(str, sb.toString());
            String str7 = BluetoothLEDevice.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onPhyRead : New RX PHY: ");
            if (i2 == 2) {
                str2 = str5;
            } else if (i2 == 1) {
                str2 = str4;
            } else if (i2 != 3) {
                str2 = str3;
            }
            sb2.append(str2);
            Log.d(str7, sb2.toString());
            String str8 = BluetoothLEDevice.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onPhyRead : Status :");
            sb3.append(i3);
            Log.d(str8, sb3.toString());
        }
    };
    Thread TransactionHandlerThread;

    /* renamed from: c */
    Context f61c;
    public List<BluetoothGattCharacteristic> chars;
    int currentConnectionPriority = 0;
    BluetoothLETransaction currentTransaction;

    /* renamed from: d */
    public BluetoothDevice f62d;
    BluetoothLEDeviceDebugVariables dVars;
    public Runnable deviceTransactionHandler = new Runnable() {
        public void run() {
            String str = BluetoothLEDevice.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("deviceTransactionHandler started for device : ");
            sb.append(BluetoothLEDevice.this.mThis.f62d.getAddress().toString());
            Log.d(str, sb.toString());
            while (!BluetoothLEDevice.this.stopTransactionHandler) {
                if (BluetoothLEDevice.this.currentTransaction != null) {
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
                } else if (BluetoothLEDevice.this.deviceTransactions.size() > 0) {
                    BluetoothLEDevice bluetoothLEDevice = BluetoothLEDevice.this;
                    bluetoothLEDevice.currentTransaction = (BluetoothLETransaction) bluetoothLEDevice.deviceTransactions.get(0);
                    BluetoothLEDevice.this.currentTransaction.transactionStartDate = new Date();
                    BluetoothLEDevice bluetoothLEDevice2 = BluetoothLEDevice.this;
                    if (!bluetoothLEDevice2.commitTransactionToBT(bluetoothLEDevice2.currentTransaction)) {
                        BluetoothLEDevice.this.currentTransaction = null;
                    }
                }
                try {
                    Thread.sleep(100, 0);
                } catch (InterruptedException unused) {
                    Log.d(BluetoothLEDevice.TAG, "deviceTransactionHandler: interrupted while running");
                }
            }
        }
    };
    ArrayList<BluetoothLETransaction> deviceTransactions;

    /* renamed from: g */
    public BluetoothGatt f63g;
    public boolean isConnected;
    public boolean isDiscovered;

    /* renamed from: m */
    public BluetoothManager f64m;
    BluetoothLEDevice mThis;
    public BluetoothLEDeviceCB myCB;
    public boolean needsBroadcastScreen;

    /* renamed from: sR */
    public ScanResult f65sR;
    public List<BluetoothGattService> services;
    public boolean shouldReconnect;
    boolean stopTransactionHandler = false;

    /* renamed from: com.ti.ti_oad.BluetoothLEController.BluetoothLEDevice$BluetoothLEDeviceCB */
    public interface BluetoothLEDeviceCB {
        void deviceConnectTimedOut(BluetoothLEDevice bluetoothLEDevice);

        void deviceDidDisconnect(BluetoothLEDevice bluetoothLEDevice);

        void deviceDiscoveryTimedOut(BluetoothLEDevice bluetoothLEDevice);

        void deviceFailed(BluetoothLEDevice bluetoothLEDevice);

        void deviceReady(BluetoothLEDevice bluetoothLEDevice);

        void didReadCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void didUpdateCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void didUpdateCharacteristicIndication(BluetoothLEDevice bluetoothLEDevice);

        void didUpdateCharacteristicNotification(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void didWriteCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void mtuValueChanged(int i);

        void waitingForConnect(BluetoothLEDevice bluetoothLEDevice, int i, int i2);

        void waitingForDiscovery(BluetoothLEDevice bluetoothLEDevice, int i, int i2);
    }

    /* renamed from: com.ti.ti_oad.BluetoothLEController.BluetoothLEDevice$BluetoothLEDeviceDebugVariables */
    private class BluetoothLEDeviceDebugVariables {
        public int connectionCalls;
        public int disconnectionCalls;
        public int reads;
        public int writes;

        private BluetoothLEDeviceDebugVariables() {
        }
    }

    public boolean refreshDeviceCache() {
        return false;
    }

    public BluetoothLEDevice(BluetoothDevice bluetoothDevice, Context context) {
        this.f62d = bluetoothDevice;
        this.f61c = context;
        this.dVars = new BluetoothLEDeviceDebugVariables();
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
                    bluetoothLEDevice.f63g = bluetoothLEDevice.f62d.connectGatt(BluetoothLEDevice.this.f61c, false, BluetoothLEDevice.this.BluetoothLEDeviceCB);
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
                        if (i2 < 0 && BluetoothLEDevice.this.f63g != null) {
                            Log.d(BluetoothLEDevice.TAG, "Timeout while connecting");
                            BluetoothLEDevice.this.f63g.disconnect();
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
                        if (i4 < 0 && BluetoothLEDevice.this.f63g != null) {
                            Log.d(BluetoothLEDevice.TAG, "Timeout while discovering services");
                            BluetoothLEDevice.this.f63g.disconnect();
                            if (i3 == 4) {
                                if (BluetoothLEDevice.this.myCB != null) {
                                    BluetoothLEDevice.this.myCB.deviceDiscoveryTimedOut(BluetoothLEDevice.this.mThis);
                                }
                                return;
                            }
                            BluetoothLEDevice bluetoothLEDevice2 = BluetoothLEDevice.this;
                            bluetoothLEDevice2.f63g = bluetoothLEDevice2.f62d.connectGatt(BluetoothLEDevice.this.f61c, false, BluetoothLEDevice.this.BluetoothLEDeviceCB);
                        }
                    }
                }
            }
        }, "BluetoothLEDevice Connect and Discover Thread").start();
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
        BluetoothGatt bluetoothGatt = this.f63g;
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            return;
        }
        Log.d(TAG, "FAILURE !!!! Device did not have a BluetoothGatt when isConnected = true !");
        try {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Current state is: ");
            sb2.append(this.f64m.getConnectionState(this.f62d, 7));
            Log.d(str2, sb2.toString());
        } catch (NullPointerException unused) {
            Log.d(TAG, "Not able to read state, device was already null !");
        }
    }

    public boolean requestMTUChange(int i) {
        return this.f63g.requestMtu(i);
    }

    public int writeCharacteristicAsync(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.WRITE_SYNC, bArr);
        this.deviceTransactions.add(bluetoothLETransaction);
        while (!bluetoothLETransaction.transactionFinished) {
            try {
                Thread.sleep(20, 0);
            } catch (InterruptedException unused) {
            }
        }
        return 0;
    }

    public int writeCharacteristicAsync(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte b) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.WRITE_SYNC, new byte[]{b});
        this.deviceTransactions.add(bluetoothLETransaction);
        while (!bluetoothLETransaction.transactionFinished) {
            try {
                Thread.sleep(20, 0);
            } catch (InterruptedException unused) {
            }
        }
        return 0;
    }

    public int readCharacteristicAsync(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.READ_SYNC, null);
        this.deviceTransactions.add(bluetoothLETransaction);
        while (!bluetoothLETransaction.transactionFinished) {
            try {
                Thread.sleep(20, 0);
            } catch (InterruptedException unused) {
            }
        }
        return 0;
    }

    public int setCharacteristicNotificationAsync(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        this.deviceTransactions.add(new BluetoothLETransaction(this, bluetoothGattCharacteristic, z ? BluetoothLETransactionType.ENABLE_NOTIFICATION_ASYNC : BluetoothLETransactionType.DISABLE_NOTIFICATION_ASYNC, null));
        return 0;
    }

    public int setCharacteristicNotificationSync(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, z ? BluetoothLETransactionType.ENABLE_NOTIFICATION_SYNC : BluetoothLETransactionType.DISABLE_NOTIFICATION_SYNC, null);
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
        while (!bluetoothLETransaction.transactionFinished) {
            try {
                Thread.sleep(20, 0);
            } catch (InterruptedException unused) {
            }
        }
        return 0;
    }

    public int writeCharacteristicSync(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte b) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.WRITE_SYNC, new byte[]{b});
        this.deviceTransactions.add(bluetoothLETransaction);
        while (!bluetoothLETransaction.transactionFinished) {
            try {
                Thread.sleep(20, 0);
            } catch (InterruptedException unused) {
            }
        }
        return 0;
    }

    public int readCharacteristicSync(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothLETransaction bluetoothLETransaction = new BluetoothLETransaction(this, bluetoothGattCharacteristic, BluetoothLETransactionType.READ_SYNC, null);
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

    public boolean commitTransactionToBT(BluetoothLETransaction bluetoothLETransaction) {
        boolean z = false;
        if (bluetoothLETransaction.characteristic == null) {
            return false;
        }
        String str = "00002902-0000-1000-8000-00805f9b34fb";
        switch (bluetoothLETransaction.transactionType) {
            case ENABLE_NOTIFICATION_ASYNC:
            case ENABLE_NOTIFICATION_SYNC:
                this.f63g.setCharacteristicNotification(bluetoothLETransaction.characteristic, true);
                BluetoothGattDescriptor descriptor = bluetoothLETransaction.characteristic.getDescriptor(UUID.fromString(str));
                if (descriptor != null) {
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    this.mThis.f63g.writeDescriptor(descriptor);
                    break;
                } else {
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Set Notification failed for :");
                    sb.append(bluetoothLETransaction.characteristic.getUuid().toString());
                    Log.d(str2, sb.toString());
                    break;
                }
            case DISABLE_NOTIFICATION_ASYNC:
            case DISABLE_NOTIFICATION_SYNC:
                this.f63g.setCharacteristicNotification(bluetoothLETransaction.characteristic, false);
                BluetoothGattDescriptor descriptor2 = bluetoothLETransaction.characteristic.getDescriptor(UUID.fromString(str));
                descriptor2.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                this.f63g.writeDescriptor(descriptor2);
                break;
            case READ_ASYNC:
            case READ_SYNC:
                z = this.f63g.readCharacteristic(bluetoothLETransaction.characteristic);
                break;
            case WRITE_ASYNC:
            case WRITE_SYNC:
                bluetoothLETransaction.characteristic.setValue(bluetoothLETransaction.dat);
                z = this.f63g.writeCharacteristic(bluetoothLETransaction.characteristic);
                break;
        }
        z = true;
        return z;
    }

    public int getCurrentConnectionPriority() {
        return this.currentConnectionPriority;
    }

    public boolean setCurrentConnectionPriority(int i) {
        if (!this.f63g.requestConnectionPriority(i)) {
            return false;
        }
        this.currentConnectionPriority = i;
        return true;
    }
}
