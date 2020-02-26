package com.p004ti.ti_oad;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.p004ti.ti_oad.BluetoothLEController.BluetoothLEDevice;
import com.p004ti.ti_oad.BluetoothLEController.BluetoothLEDevice.BluetoothLEDeviceCB;
import com.p004ti.ti_oad.TIOADEoadDefinitions.oadStatusEnumeration;
import com.p004ti.ti_oad.TIOADEoadDefinitions.tiOADImageType;
import org.apache.http.HttpStatus;

/* renamed from: com.ti.ti_oad.TIOADEoadClient */
public class TIOADEoadClient {
    /* access modifiers changed from: private */
    public static final String TAG = TIOADEoadClient.class.getSimpleName();
    private int TIOADEoadBlockSize = 0;
    byte[] TIOADEoadDeviceID;
    /* access modifiers changed from: private */
    public int TIOADEoadTotalBlocks = 0;
    private BluetoothGattCharacteristic blockRequestChar;

    /* renamed from: cb */
    BluetoothLEDeviceCB f69cb = new BluetoothLEDeviceCB() {
        public void deviceConnectTimedOut(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceDiscoveryTimedOut(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceFailed(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void didReadCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        public void didUpdateCharacteristicIndication(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void waitingForConnect(BluetoothLEDevice bluetoothLEDevice, int i, int i2) {
            if (TIOADEoadClient.this.progressCallback != null) {
                TIOADEoadClient.this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientDeviceConnecting);
            }
        }

        public void waitingForDiscovery(BluetoothLEDevice bluetoothLEDevice, int i, int i2) {
            if (TIOADEoadClient.this.progressCallback != null) {
                TIOADEoadClient.this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientDeviceDiscovering);
            }
        }

        public void deviceReady(BluetoothLEDevice bluetoothLEDevice) {
            Log.d(TIOADEoadClient.TAG, "Device is ready !");
            if (TIOADEoadClient.this.progressCallback != null) {
                TIOADEoadClient.this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientDeviceMTUSet);
            }
            TIOADEoadClient.this.internalState = oadStatusEnumeration.tiOADClientReady;
        }

        public void didUpdateCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String access$000 = TIOADEoadClient.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Characteristic: ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" Value: ");
            sb.append(TIOADEoadDefinitions.BytetohexString(bluetoothGattCharacteristic.getValue()));
            Log.d(access$000, sb.toString());
            if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_IMAGE_CONTROL)) {
                TIOADEoadClient.this.handleOADControlPointMessage(bluetoothGattCharacteristic.getValue());
            } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_IMAGE_NOTIFY)) {
                if (bluetoothGattCharacteristic.getValue()[0] == 0) {
                    TIOADEoadClient.this.status = oadStatusEnumeration.tiOADClientHeaderOK;
                    TIOADEoadClient.this.internalState = oadStatusEnumeration.tiOADClientHeaderOK;
                } else {
                    TIOADEoadClient.this.status = oadStatusEnumeration.tiOADClientHeaderFailed;
                    Log.d(TIOADEoadClient.TAG, "Failed when sending header, cannot continue");
                }
                if (TIOADEoadClient.this.progressCallback != null) {
                    TIOADEoadClient.this.progressCallback.oadStatusUpdate(TIOADEoadClient.this.status);
                }
            } else {
                bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_IMAGE_BLOCK_REQUEST);
            }
        }

        public void didUpdateCharacteristicNotification(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String access$000 = TIOADEoadClient.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("didUpdateCharacteristicNotification: ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            Log.d(access$000, sb.toString());
        }

        public void didWriteCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String access$000 = TIOADEoadClient.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("didWriteCharacteristic: ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" Value: ");
            sb.append(TIOADEoadDefinitions.BytetohexString(bluetoothGattCharacteristic.getValue()));
            Log.d(access$000, sb.toString());
        }

        public void deviceDidDisconnect(BluetoothLEDevice bluetoothLEDevice) {
            if (TIOADEoadClient.this.progressCallback == null) {
                return;
            }
            if (TIOADEoadClient.this.internalState != oadStatusEnumeration.tiOADClientImageTransfer) {
                TIOADEoadClient.this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientCompleteDeviceDisconnectedPositive);
            } else {
                TIOADEoadClient.this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientCompleteDeviceDisconnectedDuringProgramming);
            }
        }

        public void mtuValueChanged(int i) {
            TIOADEoadClient.this.myMTU = i;
        }
    };
    private Context context;
    public tiOADImageType detectedImageType = tiOADImageType.tiOADImageUnidentified;
    private boolean dryRun = false;
    /* access modifiers changed from: private */
    public TIOADEoadImageReader fileReader;
    private BluetoothGattCharacteristic imageControlChar;
    private BluetoothGattCharacteristic imageCountChar;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic imageNotifyChar;
    private BluetoothGattCharacteristic imageStatusChar;
    /* access modifiers changed from: private */
    public oadStatusEnumeration internalState;
    int myMTU;
    BluetoothLEDevice oadDevice;
    public Runnable oadProgramThread = new Runnable() {
        public void run() {
            new Thread(new Runnable() {
                public void run() {
                    TIOADEoadClient.this.sendHeaderToDevice();
                }
            }).start();
            while (TIOADEoadClient.this.internalState != oadStatusEnumeration.tiOADClientHeaderOK) {
                TIOADEoadClient.this.sleepWait(HttpStatus.SC_OK);
            }
            if (TIOADEoadClient.this.progressCallback != null) {
                TIOADEoadClient.this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientHeaderOK);
            }
            new Thread(new Runnable() {
                public void run() {
                    TIOADEoadClient.this.sleepWait(HttpStatus.SC_OK);
                    TIOADEoadClient.this.sendEoadStartOADProcessCmd();
                }
            }).start();
            while (TIOADEoadClient.this.internalState != oadStatusEnumeration.tiOADClientImageTransfer) {
                TIOADEoadClient.this.sleepWait(HttpStatus.SC_OK);
            }
            while (TIOADEoadClient.this.internalState != oadStatusEnumeration.tiOADClientImageTransferOK) {
                TIOADEoadClient.this.sleepWait(HttpStatus.SC_OK);
                if (TIOADEoadClient.this.internalState == oadStatusEnumeration.tiOADClientProgrammingAbortedByUser) {
                    return;
                }
            }
            if (TIOADEoadClient.this.progressCallback != null) {
                TIOADEoadClient.this.progressCallback.oadProgressUpdate(100.0f, TIOADEoadClient.this.TIOADEoadTotalBlocks);
            }
            if (TIOADEoadClient.this.progressCallback != null) {
                TIOADEoadClient.this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientEnableOADImageCommandSent);
            }
            new Thread(new Runnable() {
                public void run() {
                    TIOADEoadClient.this.sendEoadEnableImageCmd();
                }
            }).start();
            while (TIOADEoadClient.this.internalState != oadStatusEnumeration.tiOADClientCompleteFeedbackOK) {
                TIOADEoadClient.this.sleepWait(HttpStatus.SC_OK);
            }
            if (TIOADEoadClient.this.progressCallback != null) {
                TIOADEoadClient.this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientCompleteFeedbackOK);
            }
        }
    };
    public Runnable oadThread = new Runnable() {
        public void run() {
            Log.d(TIOADEoadClient.TAG, "OAD Thread started !");
            TIOADEoadClient.this.internalState = oadStatusEnumeration.tiOADClientInitializing;
            TIOADEoadClient.this.oadDevice.myCB = TIOADEoadClient.this.f69cb;
            TIOADEoadClient.this.oadDevice.connectDevice();
            while (TIOADEoadClient.this.internalState != oadStatusEnumeration.tiOADClientReady) {
                TIOADEoadClient.this.sleepWait(HttpStatus.SC_OK);
            }
            TIOADEoadClient.this.sleepWait(5000);
            TIOADEoadClient.this.oadDevice.f63g.requestMtu(255);
            TIOADEoadClient.this.sleepWait(2000);
            TIOADEoadClient.this.oadDevice.f63g.requestConnectionPriority(1);
            TIOADEoadClient.this.sleepWait(2000);
            new Thread(new Runnable() {
                public void run() {
                    if (TIOADEoadClient.this.setCharacteristicsAndCheckEOAD()) {
                        TIOADEoadClient.this.sendEoadBlockSizeRequest();
                    }
                }
            }).start();
            TIOADEoadClient.this.internalState = oadStatusEnumeration.tiOADClientBlockSizeRequestSent;
            while (TIOADEoadClient.this.internalState != oadStatusEnumeration.tiOADClientGotBlockSizeResponse) {
                TIOADEoadClient.this.sleepWait(HttpStatus.SC_OK);
            }
            new Thread(new Runnable() {
                public void run() {
                    TIOADEoadClient.this.sendEoadReadDeviceID();
                }
            }).start();
            while (TIOADEoadClient.this.internalState != oadStatusEnumeration.tiOADClientDeviceTypeRequestResponse) {
                TIOADEoadClient.this.sleepWait(HttpStatus.SC_OK);
            }
            if (TIOADEoadDefinitions.oadImageInfoFromChipType(TIOADEoadClient.this.TIOADEoadDeviceID).equals(TIOADEoadDefinitions.TI_OAD_IMG_INFO_CC2640R2)) {
                Log.d(TIOADEoadClient.TAG, "CC2640R2F detected do not start a dry run ...");
                return;
            }
            new Thread(new Runnable() {
                public void run() {
                    TIOADEoadClient.this.startDryRun();
                }
            }).start();
            while (TIOADEoadClient.this.internalState == oadStatusEnumeration.tiOADClientSecureUnsecureDetectionRunning) {
                TIOADEoadClient.this.sleepWait(2000);
            }
        }
    };
    /* access modifiers changed from: private */
    public TIOADEoadClientProgressCallback progressCallback;
    /* access modifiers changed from: private */
    public oadStatusEnumeration status;

    public TIOADEoadClient(Context context2) {
        this.context = context2;
    }

    public void sleepWait(int i) {
        try {
            Thread.sleep((long) i);
        } catch (InterruptedException unused) {
            Log.d(TAG, "Interrupted while in sleepWait !");
        }
    }

    public boolean initializeTIOADEoadProgrammingOnDevice(BluetoothDevice bluetoothDevice, TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback) {
        this.progressCallback = tIOADEoadClientProgressCallback;
        this.oadDevice = new BluetoothLEDevice(bluetoothDevice, this.context);
        new Thread(this.oadThread).start();
        return true;
    }

    public void startDryRun() {
        this.dryRun = true;
        String str = TIOADEoadDefinitions.oadImageInfoFromChipType(this.TIOADEoadDeviceID).equals(TIOADEoadDefinitions.TI_OAD_IMG_INFO_CC26X2R1) ? "cc26x2r1_fake_header.bin" : TIOADEoadDefinitions.oadImageInfoFromChipType(this.TIOADEoadDeviceID).equals(TIOADEoadDefinitions.TI_OAD_IMG_INFO_CC13XR1) ? "cc13x2r1_fake_header.bin" : TIOADEoadDefinitions.oadImageInfoFromChipType(this.TIOADEoadDeviceID).equals(TIOADEoadDefinitions.TI_OAD_IMG_INFO_CC2640R2) ? "cc2640r2_fake_header.bin" : "";
        Log.d(TAG, "Starting a dry run to get unsecure/secure status...");
        this.internalState = oadStatusEnumeration.tiOADClientSecureUnsecureDetectionRunning;
        start(str);
    }

    public void start(String str) {
        this.fileReader = new TIOADEoadImageReader(str, this.context);
        byte[] bArr = this.fileReader.imageHeader.TIOADEoadImageIdentificationValue;
        byte[] oadImageInfoFromChipType = TIOADEoadDefinitions.oadImageInfoFromChipType(this.TIOADEoadDeviceID);
        boolean z = true;
        for (int i = 0; i < 8; i++) {
            if (bArr[i] != oadImageInfoFromChipType[i]) {
                z = false;
            }
        }
        if (!z) {
            TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback = this.progressCallback;
            if (tIOADEoadClientProgressCallback != null) {
                tIOADEoadClientProgressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientFileIsNotForDevice);
            }
            return;
        }
        new Thread(this.oadProgramThread).start();
    }

    public void start(Uri uri) {
        this.fileReader = new TIOADEoadImageReader(uri, this.context);
        byte[] bArr = this.fileReader.imageHeader.TIOADEoadImageIdentificationValue;
        byte[] oadImageInfoFromChipType = TIOADEoadDefinitions.oadImageInfoFromChipType(this.TIOADEoadDeviceID);
        boolean z = true;
        for (int i = 0; i < 8; i++) {
            if (bArr[i] != oadImageInfoFromChipType[i]) {
                z = false;
            }
        }
        if (!z) {
            TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback = this.progressCallback;
            if (tIOADEoadClientProgressCallback != null) {
                tIOADEoadClientProgressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientFileIsNotForDevice);
            }
            return;
        }
        new Thread(this.oadProgramThread).start();
    }

    public void abortProgramming() {
        this.internalState = oadStatusEnumeration.tiOADClientProgrammingAbortedByUser;
    }

    public void sendHeaderToDevice() {
        new Thread(new Runnable() {
            public void run() {
                TIOADEoadClient.this.oadDevice.writeCharacteristicAsync(TIOADEoadClient.this.imageNotifyChar, TIOADEoadClient.this.fileReader.getHeaderForImageNotify());
            }
        }).start();
    }

    public void sendEoadDataBlock(long j) {
        int i = this.TIOADEoadBlockSize - 4;
        long j2 = (long) i;
        long j3 = j * j2;
        if (((long) this.fileReader.getRawImageData().length) - j3 < j2) {
            i = this.fileReader.getRawImageData().length - (((int) j) * i);
        }
        if (j == ((long) this.TIOADEoadTotalBlocks)) {
            Log.d(TAG, "Last block has been sent");
            this.internalState = oadStatusEnumeration.tiOADClientImageTransferOK;
            return;
        }
        byte[] bArr = new byte[(i + 4)];
        for (int i2 = 0; i2 < 4; i2++) {
            bArr[i2] = TIOADEoadDefinitions.GET_BYTE_FROM_UINT32(j, i2);
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Block: ");
        sb.append(j);
        sb.append(" srcPos: ");
        int i3 = (int) j3;
        sb.append(i3);
        sb.append(" dstPos: ");
        sb.append(4);
        sb.append(" readSize: ");
        sb.append(i);
        Log.d(str, sb.toString());
        System.arraycopy(this.fileReader.getRawImageData(), i3, bArr, 4, i);
        this.oadDevice.writeCharacteristicAsync(this.blockRequestChar, bArr);
        if (!this.dryRun) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Sent Block ");
            sb2.append(j);
            sb2.append(" With Data:");
            sb2.append(TIOADEoadDefinitions.BytetohexString(bArr));
            Log.d(str2, sb2.toString());
            TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback = this.progressCallback;
            if (tIOADEoadClientProgressCallback != null) {
                tIOADEoadClientProgressCallback.oadProgressUpdate((((float) j) / ((float) this.TIOADEoadTotalBlocks)) * 100.0f, (int) j);
                this.progressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientImageTransfer);
            }
        }
    }

    public void sendEoadReadDeviceID() {
        this.oadDevice.writeCharacteristicAsync(this.imageControlChar, (byte) TIOADEoadDefinitions.TI_OAD_CONTROL_POINT_CMD_DEVICE_TYPE_CMD);
    }

    public void sendEoadEnableImageCmd() {
        this.oadDevice.writeCharacteristicAsync(this.imageControlChar, 4);
    }

    public void sendEoadStopOADProcess() {
        this.oadDevice.writeCharacteristicAsync(this.imageControlChar, 5);
    }

    public void sendEoadStartOADProcessCmd() {
        this.TIOADEoadTotalBlocks = this.fileReader.getRawImageData().length / (this.TIOADEoadBlockSize - 4);
        int length = this.fileReader.getRawImageData().length;
        int i = this.TIOADEoadTotalBlocks;
        if (length - ((this.TIOADEoadBlockSize - 4) * i) > 0) {
            this.TIOADEoadTotalBlocks = i + 1;
        }
        this.oadDevice.writeCharacteristicAsync(this.imageControlChar, 3);
    }

    public void sendEoadBlockSizeRequest() {
        this.oadDevice.writeCharacteristicAsync(this.imageControlChar, 1);
    }

    public void handleEoadBlockSizeResponse(byte[] bArr) {
        if (bArr[0] == 1) {
            this.TIOADEoadBlockSize = TIOADEoadDefinitions.BUILD_UINT16(bArr[2], bArr[1]);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Block Size is : ");
            sb.append(this.TIOADEoadBlockSize);
            Log.d(str, sb.toString());
            this.internalState = oadStatusEnumeration.tiOADClientGotBlockSizeResponse;
        }
    }

    public void handleOADControlPointMessage(final byte[] bArr) {
        String str = "OAD Block Send Thread";
        int i = 0;
        if (!this.dryRun) {
            byte b = bArr[0];
            if (b == 1) {
                handleEoadBlockSizeResponse(bArr);
            } else if (b == 16) {
                this.internalState = oadStatusEnumeration.tiOADClientDeviceTypeRequestResponse;
                this.TIOADEoadDeviceID = new byte[4];
                while (i < 4) {
                    int i2 = i + 1;
                    this.TIOADEoadDeviceID[i] = bArr[i2];
                    i = i2;
                }
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Device ID: ");
                sb.append(TIOADEoadDefinitions.oadChipTypePrettyPrint(this.TIOADEoadDeviceID));
                Log.d(str2, sb.toString());
                if (TIOADEoadDefinitions.oadChipTypePrettyPrint(this.TIOADEoadDeviceID).equals("CC1352P")) {
                    TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback = this.progressCallback;
                    if (tIOADEoadClientProgressCallback != null) {
                        tIOADEoadClientProgressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientChipIsCC1352PShowWarningAboutLayouts);
                    }
                }
                TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback2 = this.progressCallback;
                if (tIOADEoadClientProgressCallback2 != null) {
                    tIOADEoadClientProgressCallback2.oadStatusUpdate(oadStatusEnumeration.tiOADClientReady);
                }
            } else if (b != 18) {
                if (b != 3) {
                    if (b != 4) {
                        String str3 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Unknown Control Point Response: ");
                        sb2.append(TIOADEoadDefinitions.BytetohexString(bArr));
                        Log.d(str3, sb2.toString());
                    } else if (bArr[1] == 0) {
                        this.internalState = oadStatusEnumeration.tiOADClientCompleteFeedbackOK;
                    }
                }
            } else if (bArr[1] == 0) {
                if (this.internalState == oadStatusEnumeration.tiOADClientProgrammingAbortedByUser) {
                    this.oadDevice.f63g.disconnect();
                    this.oadDevice.f63g.close();
                    TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback3 = this.progressCallback;
                    if (tIOADEoadClientProgressCallback3 != null) {
                        tIOADEoadClientProgressCallback3.oadStatusUpdate(oadStatusEnumeration.tiOADClientProgrammingAbortedByUser);
                    }
                    return;
                }
                this.internalState = oadStatusEnumeration.tiOADClientImageTransfer;
                new Thread(new Runnable() {
                    public void run() {
                        byte[] bArr = bArr;
                        TIOADEoadClient.this.sendEoadDataBlock(TIOADEoadDefinitions.BUILD_UINT32(bArr[5], bArr[4], bArr[3], bArr[2]));
                    }
                }, str).start();
            } else if (bArr[1] == 14) {
                this.internalState = oadStatusEnumeration.tiOADClientImageTransferOK;
            } else {
                TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback4 = this.progressCallback;
                if (tIOADEoadClientProgressCallback4 != null) {
                    tIOADEoadClientProgressCallback4.oadStatusUpdate(oadStatusEnumeration.tiOADClientImageTransferFailed);
                }
            }
        } else if (bArr[0] == 18) {
            long BUILD_UINT32 = TIOADEoadDefinitions.BUILD_UINT32(bArr[5], bArr[4], bArr[3], bArr[2]);
            if (bArr[1] == TIOADEoadDefinitions.TI_OAD_EOAD_STATUS_CODE_INCOMPATIBLE_IMAGE) {
                new Thread(new Runnable() {
                    public void run() {
                        TIOADEoadClient.this.sendEoadStopOADProcess();
                    }
                }).start();
                this.dryRun = false;
                this.internalState = oadStatusEnumeration.tiOADClientCompleteFeedbackOK;
                this.detectedImageType = tiOADImageType.tiOADImageSecure;
            } else if (bArr[1] == TIOADEoadDefinitions.TI_OAD_EOAD_STATUS_CODE_SUCCESS && BUILD_UINT32 == 0) {
                new Thread(new Runnable() {
                    public void run() {
                        byte[] bArr = bArr;
                        TIOADEoadClient.this.sendEoadDataBlock(TIOADEoadDefinitions.BUILD_UINT32(bArr[5], bArr[4], bArr[3], bArr[2]));
                    }
                }, str).start();
            } else {
                new Thread(new Runnable() {
                    public void run() {
                        TIOADEoadClient.this.sendEoadStopOADProcess();
                    }
                }).start();
                this.dryRun = false;
                this.internalState = oadStatusEnumeration.tiOADClientCompleteFeedbackOK;
                this.detectedImageType = tiOADImageType.tiOADImageUnsecure;
            }
            String str4 = this.detectedImageType == tiOADImageType.tiOADImageUnsecure ? "Unsecure" : this.detectedImageType == tiOADImageType.tiOADImageSecure ? "Secure" : "Unidentified";
            String str5 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Dry run complete, detected image is : ");
            sb3.append(str4);
            Log.d(str5, sb3.toString());
        }
    }

    public boolean setCharacteristicsAndCheckEOAD() {
        for (BluetoothGattService bluetoothGattService : this.oadDevice.f63g.getServices()) {
            if (bluetoothGattService.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_SERVICE)) {
                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
                    if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_IMAGE_BLOCK_REQUEST)) {
                        this.blockRequestChar = bluetoothGattCharacteristic;
                        this.oadDevice.setCharacteristicNotificationSync(this.blockRequestChar, true);
                    } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_IMAGE_CONTROL)) {
                        this.imageControlChar = bluetoothGattCharacteristic;
                        this.oadDevice.setCharacteristicNotificationSync(this.imageControlChar, true);
                    } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_IMAGE_NOTIFY)) {
                        this.imageNotifyChar = bluetoothGattCharacteristic;
                        this.oadDevice.setCharacteristicNotificationSync(this.imageNotifyChar, true);
                    } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_IMAGE_COUNT)) {
                        this.imageCountChar = bluetoothGattCharacteristic;
                        TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback = this.progressCallback;
                        if (tIOADEoadClientProgressCallback != null) {
                            tIOADEoadClientProgressCallback.oadStatusUpdate(oadStatusEnumeration.tiOADClientOADWrongVersion);
                        }
                        return false;
                    } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIOADEoadDefinitions.TI_OAD_IMAGE_STATUS)) {
                        this.imageStatusChar = bluetoothGattCharacteristic;
                        TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback2 = this.progressCallback;
                        if (tIOADEoadClientProgressCallback2 != null) {
                            tIOADEoadClientProgressCallback2.oadStatusUpdate(oadStatusEnumeration.tiOADClientOADWrongVersion);
                        }
                        return false;
                    }
                }
                if (!(this.blockRequestChar == null || this.imageNotifyChar == null || this.imageControlChar == null)) {
                    return true;
                }
            }
        }
        TIOADEoadClientProgressCallback tIOADEoadClientProgressCallback3 = this.progressCallback;
        if (tIOADEoadClientProgressCallback3 != null) {
            tIOADEoadClientProgressCallback3.oadStatusUpdate(oadStatusEnumeration.tiOADClientOADServiceMissingOnPeripheral);
        }
        return false;
    }

    public int getMTU() {
        return this.myMTU;
    }

    public int getTIOADEoadBlockSize() {
        return this.TIOADEoadBlockSize;
    }

    public int getTIOADEoadTotalBlocks() {
        return this.TIOADEoadTotalBlocks;
    }

    public byte[] getTIOADEoadDeviceID() {
        return this.TIOADEoadDeviceID;
    }
}
