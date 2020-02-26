package com.p004ti.ble.common.oad;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;
import com.p004ti.ble.common.GattInfo;
import com.p004ti.ble.common.oad.FWUpdateNGBINFileHandler.fwFileLoadStatus;
import com.p004ti.ble.p005ti.profiles.TIOADProfile;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/* renamed from: com.ti.ble.common.oad.FWUpdateNGProgrammingHandler */
public class FWUpdateNGProgrammingHandler {
    private static final int HAL_FLASH_WORD_SIZE = 4;
    private static final int OAD_BLOCK_SIZE = 16;
    private static final String TAG = "FWUpdateNGProgrammingHandler";
    public boolean abort;
    /* access modifiers changed from: private */

    /* renamed from: cb */
    public oadCallBack f41cb;
    /* access modifiers changed from: private */
    public int currentBlock;
    public boolean finished;
    /* access modifiers changed from: private */
    public FWUpdateNGBINFileHandler fwFile;
    public TimerTask highSpeedNoNotify = new TimerTask() {
        public void run() {
            FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
            FWUpdateNGProgrammingHandler.this.sendOnePacket(fWUpdateNGProgrammingHandler.getPacketForBlock((short) fWUpdateNGProgrammingHandler.currentBlock = fWUpdateNGProgrammingHandler.currentBlock + 1));
            if (FWUpdateNGProgrammingHandler.this.currentBlock > (FWUpdateNGProgrammingHandler.this.fwFile.header.fileBufferPadded.length / 16) - 1) {
                FWUpdateNGProgrammingHandler.this.highSpeedProgTimer.cancel();
                Log.d(FWUpdateNGProgrammingHandler.TAG, "Finished programming !");
                FWUpdateNGProgrammingHandler.this.finished = true;
            }
        }
    };
    public TimerTask highSpeedNotify = new TimerTask() {
        private boolean highSpeedNotifyIsBusy = false;

        public void run() {
            if (!this.highSpeedNotifyIsBusy) {
                this.highSpeedNotifyIsBusy = true;
                while (FWUpdateNGProgrammingHandler.this.packetsSentToQueue < FWUpdateNGProgrammingHandler.this.oadParam.packetsPerTimer) {
                    FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
                    FWUpdateNGProgrammingHandler.this.sendOnePacket(fWUpdateNGProgrammingHandler.getPacketForBlock((short) fWUpdateNGProgrammingHandler.currentBlock = fWUpdateNGProgrammingHandler.currentBlock + 1));
                    FWUpdateNGProgrammingHandler.this.packetsSentToQueue = FWUpdateNGProgrammingHandler.this.packetsSentToQueue + 1;
                }
                this.highSpeedNotifyIsBusy = false;
            }
        }
    };
    /* access modifiers changed from: private */
    public Timer highSpeedProgTimer;
    boolean isBlocked = false;
    private long lastPacketSendTime;
    BluetoothGatt mBtGatt;
    BluetoothGattCharacteristic mTIOadImageBlockReq;
    BluetoothGattCharacteristic mTIOadImageNotifyChar;
    BluetoothGattCharacteristic mTIOadImageStatus;
    BluetoothGattService mTIOadService;
    /* access modifiers changed from: private */
    public oadParameters oadParam;
    /* access modifiers changed from: private */
    public int packetsSentToQueue;
    private int totalBlocks;

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$16 */
    static /* synthetic */ class C075016 {

        /* renamed from: $SwitchMap$com$ti$ble$common$oad$FWUpdateNGProgrammingHandler$oadParameters$oadMode */
        static final /* synthetic */ int[] f42x6df85683 = new int[oadMode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$oadParameters$oadMode[] r0 = com.p004ti.ble.common.oad.FWUpdateNGProgrammingHandler.oadParameters.oadMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f42x6df85683 = r0
                int[] r0 = f42x6df85683     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$oadParameters$oadMode r1 = com.p004ti.ble.common.oad.FWUpdateNGProgrammingHandler.oadParameters.oadMode.SAFE_MODE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = f42x6df85683     // Catch:{ NoSuchFieldError -> 0x001f }
                com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$oadParameters$oadMode r1 = com.p004ti.ble.common.oad.FWUpdateNGProgrammingHandler.oadParameters.oadMode.HIGH_SPEED     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = f42x6df85683     // Catch:{ NoSuchFieldError -> 0x002a }
                com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$oadParameters$oadMode r1 = com.p004ti.ble.common.oad.FWUpdateNGProgrammingHandler.oadParameters.oadMode.ADVANCED     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.p004ti.ble.common.oad.FWUpdateNGProgrammingHandler.C075016.<clinit>():void");
        }
    }

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$oadCallBack */
    public interface oadCallBack {
        void oadImageNotifyRejectedImage(short s, short s2, byte[] bArr);

        void oadImageStatusWasUpdated(oadImageStatus oadimagestatus);

        void oadProgrammingFininshed();

        void oadProgressWasUpdated(int i, int i2, int i3, int i4);

        void oadStartedWithParameters(oadMode oadmode, boolean z, int i, int i2);

        void oadTimedoutWhileProgramming();
    }

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$oadImageStatus */
    public enum oadImageStatus {
        OAD_SUCCESS,
        OAD_CRC_ERR,
        OAD_FLASH_ERR,
        OAD_BUFFER_OFL
    }

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$oadParameters */
    public static class oadParameters {
        public oadMode mode;
        public int packetsPerTimer;
        public int timerDelay;
        public boolean useNotifications;

        /* renamed from: com.ti.ble.common.oad.FWUpdateNGProgrammingHandler$oadParameters$oadMode */
        public enum oadMode {
            SAFE_MODE,
            HIGH_SPEED,
            ADVANCED
        }

        public oadParameters(oadMode oadmode, float f, boolean z, int i, int i2) {
            int i3 = C075016.f42x6df85683[oadmode.ordinal()];
            if (i3 == 1) {
                this.timerDelay = ((int) f) + 10;
                this.useNotifications = true;
                this.packetsPerTimer = 1;
                this.mode = oadmode;
            } else if (i3 != 2) {
                if (i3 == 3) {
                    this.timerDelay = i;
                    this.useNotifications = z;
                    this.packetsPerTimer = i2;
                    this.mode = oadmode;
                }
            } else if (f < 25.0f) {
                this.timerDelay = 30;
                this.useNotifications = true;
                this.packetsPerTimer = 4;
                this.mode = oadmode;
            } else {
                this.timerDelay = 15;
                this.useNotifications = true;
                this.packetsPerTimer = 4;
                this.mode = oadmode;
            }
        }

        public void setAdvancedParameters(boolean z, int i, int i2) {
            this.useNotifications = z;
            this.packetsPerTimer = i;
            this.timerDelay = i2;
        }
    }

    public FWUpdateNGProgrammingHandler(FWUpdateNGBINFileHandler fWUpdateNGBINFileHandler, BluetoothGatt bluetoothGatt, oadCallBack oadcallback) {
        this.fwFile = fWUpdateNGBINFileHandler;
        this.f41cb = oadcallback;
        this.totalBlocks = fWUpdateNGBINFileHandler.header.fileBufferPadded.length / 16;
        this.mBtGatt = bluetoothGatt;
        this.mTIOadService = bluetoothGatt.getService(GattInfo.OAD_SERVICE_UUID);
        BluetoothGattService bluetoothGattService = this.mTIOadService;
        if (bluetoothGattService != null) {
            this.mTIOadImageNotifyChar = bluetoothGattService.getCharacteristic(UUID.fromString(TIOADProfile.oadImageNotify_UUID));
            this.mTIOadImageBlockReq = this.mTIOadService.getCharacteristic(UUID.fromString(TIOADProfile.oadBlockRequest_UUID));
            this.mTIOadImageStatus = this.mTIOadService.getCharacteristic(UUID.fromString(TIOADProfile.oadImageStatus_UUID));
        }
    }

    public boolean setupOAD(oadMode oadmode, float f, boolean z, int i, int i2) {
        oadParameters oadparameters = new oadParameters(oadmode, f, z, i, i2);
        this.oadParam = oadparameters;
        enableNotifications();
        return true;
    }

    public void enableNotifications() {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mTIOadImageNotifyChar;
        String str = TAG;
        if (bluetoothGattCharacteristic != null) {
            this.mBtGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
            new Thread(new Runnable() {
                public void run() {
                    FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
                    fWUpdateNGProgrammingHandler.isBlocked = true;
                    BluetoothGattDescriptor descriptor = fWUpdateNGProgrammingHandler.mTIOadImageNotifyChar.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
                    if (descriptor != null) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        FWUpdateNGProgrammingHandler.this.mBtGatt.writeDescriptor(descriptor);
                    }
                }
            }).start();
            while (this.isBlocked) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(str, "Enabled notifications for OAD Image Notify Characteristic");
        }
        if (!this.oadParam.useNotifications) {
            Log.d(str, "Not using notifications !");
        } else if (this.mTIOadImageBlockReq != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            this.mBtGatt.setCharacteristicNotification(this.mTIOadImageBlockReq, true);
            new Thread(new Runnable() {
                public void run() {
                    FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
                    fWUpdateNGProgrammingHandler.isBlocked = true;
                    BluetoothGattDescriptor descriptor = fWUpdateNGProgrammingHandler.mTIOadImageBlockReq.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
                    if (descriptor != null) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        int i = 100;
                        while (!FWUpdateNGProgrammingHandler.this.mBtGatt.writeDescriptor(descriptor)) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String str = FWUpdateNGProgrammingHandler.TAG;
                            Log.d(str, "writeDescriptor failed, retrying ...");
                            i--;
                            if (i < 0) {
                                Log.d(str, "writeDescriptor failed, giving up !");
                                FWUpdateNGProgrammingHandler.this.isBlocked = false;
                                return;
                            }
                        }
                    }
                }
            }).start();
            while (this.isBlocked) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
            Log.d(str, "Enabled notifications for OAD Image Block Request Characteristic");
        }
        if (this.mTIOadImageStatus != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e4) {
                e4.printStackTrace();
            }
            this.mBtGatt.setCharacteristicNotification(this.mTIOadImageStatus, true);
            new Thread(new Runnable() {
                public void run() {
                    FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
                    fWUpdateNGProgrammingHandler.isBlocked = true;
                    BluetoothGattDescriptor descriptor = fWUpdateNGProgrammingHandler.mTIOadImageStatus.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
                    if (descriptor != null) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        FWUpdateNGProgrammingHandler.this.mBtGatt.writeDescriptor(descriptor);
                    }
                }
            }).start();
            while (this.isBlocked) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e5) {
                    e5.printStackTrace();
                }
            }
            Log.d(str, "Enabled notifications for OAD Image Status Characteristic");
        }
    }

    public void disableNotifications() {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mTIOadImageNotifyChar;
        String str = TAG;
        if (bluetoothGattCharacteristic != null) {
            this.mBtGatt.setCharacteristicNotification(bluetoothGattCharacteristic, false);
            new Thread(new Runnable() {
                public void run() {
                    FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
                    fWUpdateNGProgrammingHandler.isBlocked = true;
                    BluetoothGattDescriptor descriptor = fWUpdateNGProgrammingHandler.mTIOadImageNotifyChar.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
                    if (descriptor != null) {
                        descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                        FWUpdateNGProgrammingHandler.this.mBtGatt.writeDescriptor(descriptor);
                    }
                }
            }).start();
            while (this.isBlocked) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(str, "Enabled notifications for OAD Image Notify Characteristic");
        }
        if (!this.oadParam.useNotifications) {
            Log.d(str, "Not using notifications !");
        } else if (this.mTIOadImageBlockReq != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            this.mBtGatt.setCharacteristicNotification(this.mTIOadImageBlockReq, false);
            new Thread(new Runnable() {
                public void run() {
                    FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
                    fWUpdateNGProgrammingHandler.isBlocked = true;
                    BluetoothGattDescriptor descriptor = fWUpdateNGProgrammingHandler.mTIOadImageBlockReq.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
                    if (descriptor != null) {
                        descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                        while (!FWUpdateNGProgrammingHandler.this.mBtGatt.writeDescriptor(descriptor)) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d(FWUpdateNGProgrammingHandler.TAG, "writeDescriptor failed, retrying ...");
                        }
                    }
                }
            }).start();
            while (this.isBlocked) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
            Log.d(str, "Enabled notifications for OAD Image Block Request Characteristic");
        }
        if (this.mTIOadImageStatus != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e4) {
                e4.printStackTrace();
            }
            this.mBtGatt.setCharacteristicNotification(this.mTIOadImageStatus, false);
            new Thread(new Runnable() {
                public void run() {
                    FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
                    fWUpdateNGProgrammingHandler.isBlocked = true;
                    BluetoothGattDescriptor descriptor = fWUpdateNGProgrammingHandler.mTIOadImageStatus.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
                    if (descriptor != null) {
                        descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                        FWUpdateNGProgrammingHandler.this.mBtGatt.writeDescriptor(descriptor);
                    }
                }
            }).start();
            while (this.isBlocked) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e5) {
                    e5.printStackTrace();
                }
            }
            Log.d(str, "Disbled notifications for OAD Image Status Characteristic");
        }
    }

    public boolean startOADProcess() {
        this.abort = false;
        final byte[] packetForOADImageIdentify = getPacketForOADImageIdentify();
        if (packetForOADImageIdentify != null) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler = FWUpdateNGProgrammingHandler.this;
                    fWUpdateNGProgrammingHandler.finished = false;
                    if (fWUpdateNGProgrammingHandler.oadParam.mode != oadMode.ADVANCED || FWUpdateNGProgrammingHandler.this.oadParam.useNotifications) {
                        FWUpdateNGProgrammingHandler fWUpdateNGProgrammingHandler2 = FWUpdateNGProgrammingHandler.this;
                        fWUpdateNGProgrammingHandler2.writeCharacteristic(fWUpdateNGProgrammingHandler2.mTIOadImageNotifyChar, packetForOADImageIdentify);
                        FWUpdateNGProgrammingHandler.this.packetsSentToQueue = 0;
                        FWUpdateNGProgrammingHandler.this.f41cb.oadStartedWithParameters(FWUpdateNGProgrammingHandler.this.oadParam.mode, FWUpdateNGProgrammingHandler.this.oadParam.useNotifications, FWUpdateNGProgrammingHandler.this.oadParam.timerDelay, FWUpdateNGProgrammingHandler.this.oadParam.packetsPerTimer);
                        StringBuilder sb = new StringBuilder();
                        sb.append("Wrote image header to image notify characteristic: ");
                        sb.append(FWUpdateNGProgrammingHandler.getStringFromByteVector(packetForOADImageIdentify));
                        Log.d(FWUpdateNGProgrammingHandler.TAG, sb.toString());
                        return;
                    }
                    FWUpdateNGProgrammingHandler.this.highSpeedProgTimer = new Timer("oadHighSpeedTimer");
                    FWUpdateNGProgrammingHandler.this.highSpeedProgTimer.scheduleAtFixedRate(FWUpdateNGProgrammingHandler.this.highSpeedNoNotify, 500, (long) FWUpdateNGProgrammingHandler.this.oadParam.timerDelay);
                }
            }).start();
        }
        return true;
    }

    public byte[] getPacketForBlock(short s) {
        int i = (this.fwFile.header.status & 255) == 254 ? (s + 1) * 16 : s * 16;
        byte[] bArr = new byte[18];
        bArr[1] = (byte) ((s >> 8) & 255);
        int i2 = 0;
        bArr[0] = (byte) (s & 255);
        while (true) {
            if (i2 >= 16) {
                break;
            }
            int i3 = i + i2;
            try {
                if (i3 >= this.fwFile.header.fileBufferPadded.length) {
                    break;
                }
                bArr[i2 + 2] = this.fwFile.header.fileBufferPadded[i3];
                i2++;
            } catch (Exception e) {
                e.printStackTrace();
                StringBuilder sb = new StringBuilder();
                sb.append("Something went wrong when making packet for block");
                sb.append(s);
                Log.d(TAG, sb.toString());
                return null;
            }
        }
        this.f41cb.oadProgressWasUpdated(s * 16, s, this.fwFile.header.fileBufferPadded.length, this.fwFile.header.fileBufferPadded.length / 16);
        return bArr;
    }

    public void sendOnePacket(byte[] bArr) {
        if (!this.abort) {
            int i = C075016.f42x6df85683[this.oadParam.mode.ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i == 3 && bArr != null) {
                        writeCharacteristicNoResponse(this.mTIOadImageBlockReq, bArr);
                    }
                } else if (bArr != null) {
                    writeCharacteristicNoResponse(this.mTIOadImageBlockReq, bArr);
                }
            } else if (bArr != null) {
                writeCharacteristicNoResponse(this.mTIOadImageBlockReq, bArr);
            }
        }
    }

    public byte[] getPacketForOADImageIdentify() {
        if (this.fwFile.header.loadStatus == fwFileLoadStatus.LOAD_OK) {
            byte[] bArr = new byte[16];
            bArr[0] = (byte) (this.fwFile.header.crc1 & 255);
            bArr[1] = (byte) ((this.fwFile.header.crc1 >> 8) & 255);
            bArr[2] = (byte) (this.fwFile.header.crc2 & 255);
            bArr[3] = (byte) ((this.fwFile.header.crc2 >> 8) & 255);
            bArr[4] = (byte) (this.fwFile.header.ver & 255);
            bArr[5] = (byte) ((this.fwFile.header.ver >> 8) & 255);
            bArr[6] = (byte) (this.fwFile.header.len & 255);
            bArr[7] = (byte) ((this.fwFile.header.len >> 8) & 255);
            for (int i = 0; i < 4; i++) {
                bArr[i + 8] = this.fwFile.header.uid[i];
            }
            bArr[12] = (byte) (this.fwFile.header.addr & 255);
            bArr[13] = (byte) ((this.fwFile.header.addr >> 8) & 255);
            bArr[14] = this.fwFile.header.imgType;
            bArr[15] = this.fwFile.header.status;
            return bArr;
        } else if (this.fwFile.header.loadStatus == fwFileLoadStatus.CRC_CHECK_FAILED) {
            Log.d(TAG, "Cannot program a file with broken CRC, that would brick the device !!!");
            return null;
        } else {
            byte[] bArr2 = new byte[16];
            bArr2[2] = -1;
            bArr2[3] = -1;
            bArr2[4] = 0;
            bArr2[5] = 0;
            int length = this.fwFile.header.fileBufferPadded.length / 4;
            bArr2[6] = (byte) (length & 255);
            bArr2[7] = (byte) ((length >> 8) & 255);
            bArr2[11] = 69;
            bArr2[10] = 69;
            bArr2[9] = 69;
            bArr2[8] = 69;
            bArr2[13] = 0;
            bArr2[12] = 0;
            bArr2[14] = 0;
            bArr2[15] = -2;
            return bArr2;
        }
    }

    public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        Log.d(TAG, "onCharacteristicRead");
        this.isBlocked = false;
    }

    public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.isBlocked = false;
        boolean equals = bluetoothGattCharacteristic.getUuid().equals(this.mTIOadImageBlockReq.getUuid());
        String str = TAG;
        if (equals) {
            byte[] value = this.mTIOadImageBlockReq.getValue();
            byte b = (value[0] & 255) | (65280 & (value[1] << 8));
            StringBuilder sb = new StringBuilder();
            sb.append("Block Request : ");
            sb.append(b);
            Log.d(str, sb.toString());
            if (this.oadParam.mode == oadMode.SAFE_MODE) {
                sendOnePacket(getPacketForBlock((short) b));
                this.currentBlock = b;
                if ((this.fwFile.header.len * 4) / 16 == ((short) (b + 1))) {
                    this.f41cb.oadProgrammingFininshed();
                }
            } else {
                String str2 = "Finished programming !";
                String str3 = "oadHighSpeedTimer";
                String str4 = " CurrentBlock:";
                String str5 = "packetsSentToQueue: ";
                if (this.oadParam.mode == oadMode.HIGH_SPEED) {
                    if (b == 0) {
                        Timer timer = this.highSpeedProgTimer;
                        if (timer != null) {
                            timer.cancel();
                            Log.d(str, str2);
                            new Thread(new Runnable() {
                                public void run() {
                                    FWUpdateNGProgrammingHandler.this.highSpeedProgTimer.cancel();
                                    FWUpdateNGProgrammingHandler.this.disableNotifications();
                                    FWUpdateNGProgrammingHandler.this.f41cb.oadProgrammingFininshed();
                                }
                            }).start();
                            this.finished = true;
                            return;
                        }
                        this.currentBlock = 0;
                        this.highSpeedProgTimer = new Timer(str3);
                        this.highSpeedProgTimer.scheduleAtFixedRate(this.highSpeedNotify, 250, (long) this.oadParam.timerDelay);
                    }
                    this.packetsSentToQueue--;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str5);
                    sb2.append(this.packetsSentToQueue);
                    sb2.append(str4);
                    sb2.append(this.currentBlock);
                    Log.d(str, sb2.toString());
                } else if (this.oadParam.mode == oadMode.ADVANCED) {
                    if (b == 0) {
                        Timer timer2 = this.highSpeedProgTimer;
                        if (timer2 != null) {
                            timer2.cancel();
                            Log.d(str, str2);
                            new Thread(new Runnable() {
                                public void run() {
                                    FWUpdateNGProgrammingHandler.this.highSpeedProgTimer.cancel();
                                    FWUpdateNGProgrammingHandler.this.disableNotifications();
                                    FWUpdateNGProgrammingHandler.this.f41cb.oadProgrammingFininshed();
                                }
                            }).start();
                            this.finished = true;
                            return;
                        }
                        this.currentBlock = 0;
                        this.highSpeedProgTimer = new Timer(str3);
                        this.highSpeedProgTimer.scheduleAtFixedRate(this.highSpeedNotify, 250, (long) this.oadParam.timerDelay);
                    }
                    this.packetsSentToQueue--;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str5);
                    sb3.append(this.packetsSentToQueue);
                    sb3.append(str4);
                    sb3.append(this.currentBlock);
                    Log.d(str, sb3.toString());
                }
            }
        } else if (bluetoothGattCharacteristic.getUuid().equals(this.mTIOadImageNotifyChar.getUuid())) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Image notify received: ");
            sb4.append(getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
            Log.d(str, sb4.toString());
            byte[] value2 = bluetoothGattCharacteristic.getValue();
            byte b2 = ((value2[1] << 8) & 65280) | (value2[0] & 255);
            byte b3 = (65280 & (value2[3] << 8)) | (value2[2] & 255);
            byte[] bArr = new byte[4];
            for (int i = 0; i < bArr.length; i++) {
                bArr[i] = value2[i + 4];
            }
            this.f41cb.oadImageNotifyRejectedImage((short) b2, (short) b3, bArr);
            this.abort = true;
        } else if (bluetoothGattCharacteristic.getUuid().equals(this.mTIOadImageStatus.getUuid())) {
            byte b4 = bluetoothGattCharacteristic.getValue()[0];
            this.f41cb.oadImageStatusWasUpdated(oadImageStatus.values()[b4]);
            this.abort = true;
            if (oadImageStatus.values()[b4] == oadImageStatus.OAD_SUCCESS) {
                this.finished = true;
            } else {
                this.finished = false;
            }
        }
    }

    public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        String str = TAG;
        if (i != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("onCharacteristicWrite failed to ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" with status :");
            sb.append(i);
            Log.d(str, sb.toString());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onCharacteristicWrite : GATT_SUCCESS ( Block: ");
            sb2.append(this.currentBlock);
            sb2.append(" of ");
            sb2.append((this.fwFile.header.len * 4) / 16);
            sb2.append(")");
            Log.d(str, sb2.toString());
            if (bluetoothGattCharacteristic.getValue().length < 5) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Value :");
                sb3.append(getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
                Log.d(str, sb3.toString());
            }
        }
        if (bluetoothGattCharacteristic.getUuid().equals(this.mTIOadImageBlockReq.getUuid())) {
            String str2 = "Finished programming !";
            if (this.oadParam.mode == oadMode.HIGH_SPEED) {
                if (this.currentBlock > (this.fwFile.header.fileBufferPadded.length / 16) - 1) {
                    this.highSpeedProgTimer.cancel();
                    Log.d(str, str2);
                    new Thread(new Runnable() {
                        public void run() {
                            FWUpdateNGProgrammingHandler.this.highSpeedProgTimer.cancel();
                            FWUpdateNGProgrammingHandler.this.disableNotifications();
                            FWUpdateNGProgrammingHandler.this.f41cb.oadProgrammingFininshed();
                        }
                    }).start();
                    this.finished = true;
                    return;
                }
            } else if (this.oadParam.mode == oadMode.ADVANCED && this.currentBlock > (this.fwFile.header.fileBufferPadded.length / 16) - 1) {
                this.highSpeedProgTimer.cancel();
                Log.d(str, str2);
                new Thread(new Runnable() {
                    public void run() {
                        FWUpdateNGProgrammingHandler.this.highSpeedProgTimer.cancel();
                        FWUpdateNGProgrammingHandler.this.disableNotifications();
                        FWUpdateNGProgrammingHandler.this.f41cb.oadProgrammingFininshed();
                    }
                }).start();
                this.finished = true;
                return;
            }
        }
        this.isBlocked = false;
    }

    public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
        Log.d(TAG, "onDescriptorWrite");
        this.isBlocked = false;
    }

    public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
        Log.d(TAG, "onDescriptorRead");
        this.isBlocked = false;
    }

    public boolean readCharacteristic(final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic == null || this.isBlocked) {
            return false;
        }
        this.isBlocked = true;
        new Thread(new Runnable() {
            public void run() {
                FWUpdateNGProgrammingHandler.this.mBtGatt.readCharacteristic(bluetoothGattCharacteristic);
            }
        }).start();
        while (this.isBlocked) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean writeCharacteristic(final BluetoothGattCharacteristic bluetoothGattCharacteristic, final byte[] bArr) {
        if (bluetoothGattCharacteristic == null || this.isBlocked) {
            return false;
        }
        this.isBlocked = true;
        new Thread(new Runnable() {
            public void run() {
                bluetoothGattCharacteristic.setWriteType(2);
                bluetoothGattCharacteristic.setValue(bArr);
                FWUpdateNGProgrammingHandler.this.mBtGatt.writeCharacteristic(bluetoothGattCharacteristic);
            }
        }).start();
        while (this.isBlocked) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean writeCharacteristicNoResponse(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[i];
        }
        bluetoothGattCharacteristic.setWriteType(1);
        bluetoothGattCharacteristic.setValue(bArr2);
        this.lastPacketSendTime = System.currentTimeMillis();
        int i2 = 0;
        while (!this.mBtGatt.writeCharacteristic(bluetoothGattCharacteristic)) {
            String str = TAG;
            Log.d(str, "WRITE CHARACTERISTIC FAILED !");
            i2++;
            if (i2 > 30) {
                Timer timer = this.highSpeedProgTimer;
                if (timer != null) {
                    timer.cancel();
                }
                this.f41cb.oadTimedoutWhileProgramming();
                return false;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bluetoothGattCharacteristic.setWriteType(1);
            byte[] bArr3 = new byte[bArr.length];
            for (int i3 = 0; i3 < bArr.length; i3++) {
                bArr3[i3] = bArr[i3];
            }
            bluetoothGattCharacteristic.setValue(bArr3);
            if (bArr3.length < 5) {
                StringBuilder sb = new StringBuilder();
                sb.append("Value :");
                sb.append(getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
                Log.d(str, sb.toString());
            }
        }
        return true;
    }

    public static String getStringFromByteVector(byte[] bArr) {
        String str = "";
        for (int i = 0; i < bArr.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(String.format("0x%x", new Object[]{Byte.valueOf(bArr[i])}));
            String sb2 = sb.toString();
            if (i < bArr.length - 1) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append(",");
                str = sb3.toString();
            } else {
                str = sb2;
            }
        }
        return str;
    }
}
