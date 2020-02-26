package com.p004ti.ble.audio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.p000v4.content.LocalBroadcastManager;
import android.util.Log;
import com.p004ti.ti_codec.C0924ti_codec;
import com.p004ti.ti_oad.TIOADEoadDefinitions;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.ti.ble.audio.AdvancedRemoteBLEAudioHandler */
public class AdvancedRemoteBLEAudioHandler {
    public static final String TAG = "handler";
    ArrayList<audioFrameBuffer> aBuf;
    int aFBytesRemaining;
    byte aFId;
    int aFPacketsInLastFrame;
    int aFPacketsPerFrame;
    int aFileTotalBytes;
    int aFrameLength;
    packetParserState aState;
    BleRxTimerTask bleRxTask;
    Timer bleRxTimer;
    BufferedOutputStream bos;
    Context context;
    audioFrameBuffer curBuf;
    int decodedIndex = 0;
    String fileName;
    int foundStart = 0;
    int lock;
    SharedPreferences mPrefs;

    /* renamed from: os */
    OutputStream f25os;
    DataOutputStream outFile;
    RandomAccessFile raFile;
    int start;

    /* renamed from: com.ti.ble.audio.AdvancedRemoteBLEAudioHandler$BleRxTimerTask */
    class BleRxTimerTask extends TimerTask {
        BleRxTimerTask() {
        }

        public void run() {
            Log.d(AdvancedRemoteBLEAudioHandler.TAG, "RX Timed out");
            AdvancedRemoteBLEAudioHandler.this.aState = packetParserState.IDLE;
            AdvancedRemoteBLEAudioHandler.this.aFBytesRemaining = 0;
            Intent intent = new Intent("ARCBLEAudio-From-Service-Events");
            intent.putExtra("statusText", "Idle");
            LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioHandler.this.context).sendBroadcast(intent);
            try {
                AdvancedRemoteBLEAudioHandler.this.closeWaveFile(AdvancedRemoteBLEAudioHandler.this.fileName);
            } catch (IOException unused) {
            }
        }
    }

    /* renamed from: com.ti.ble.audio.AdvancedRemoteBLEAudioHandler$audioFrameBuffer */
    public class audioFrameBuffer {
        public byte[] CompressedData;
        public short[] PCMData;
        public boolean isDecoded;
        public boolean isReceived;
        public int sequenceNr;

        public audioFrameBuffer() {
        }
    }

    /* renamed from: com.ti.ble.audio.AdvancedRemoteBLEAudioHandler$packetDecoderState */
    enum packetDecoderState {
        IDLE,
        DECODING
    }

    /* renamed from: com.ti.ble.audio.AdvancedRemoteBLEAudioHandler$packetParserState */
    enum packetParserState {
        IDLE,
        RUNNING
    }

    public byte[] writeWaveHeaderPCM(int i, int i2, int i3, int i4, int i5, int i6) {
        return new byte[]{82, 73, 70, 70, (byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 24) & 255), 87, 65, 86, 69, 102, 109, 116, 32, TIOADEoadDefinitions.TI_OAD_CONTROL_POINT_CMD_DEVICE_TYPE_CMD, 0, 0, 0, 1, 0, (byte) i5, 0, (byte) (i2 & 255), (byte) ((i2 >> 8) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 24) & 255), (byte) (i3 & 255), (byte) ((i3 >> 8) & 255), (byte) ((i3 >> 16) & 255), (byte) ((i3 >> 24) & 255), 2, 0, (byte) i6, 0, 100, 97, 116, 97, (byte) (i4 & 255), (byte) ((i4 >> 8) & 255), (byte) ((i4 >> 16) & 255), (byte) ((i4 >> 24) & 255)};
    }

    public AdvancedRemoteBLEAudioHandler(int i, Context context2) {
        this.aFPacketsPerFrame = i / 20;
        this.aFPacketsInLastFrame = (i - (this.aFPacketsPerFrame * 20)) % 20;
        StringBuilder sb = new StringBuilder();
        sb.append("Expecting ");
        sb.append(this.aFPacketsPerFrame);
        sb.append(" BLE packets per frame & ");
        sb.append(this.aFPacketsInLastFrame);
        sb.append(" bytes in last frame");
        Log.d(TAG, sb.toString());
        this.aState = packetParserState.IDLE;
        this.aFrameLength = i;
        this.lock = 0;
        this.foundStart = 0;
        this.start = 1;
        this.bleRxTimer = new Timer();
        this.bleRxTask = new BleRxTimerTask();
        this.context = context2;
        this.mPrefs = context2.getSharedPreferences("voice_search", 0);
    }

    public void setFrameLen(int i) {
        this.aFPacketsPerFrame = i / 20;
        this.aFPacketsInLastFrame = (i - (this.aFPacketsPerFrame * 20)) % 20;
        StringBuilder sb = new StringBuilder();
        sb.append("setFrameLen: ");
        sb.append(this.aFPacketsPerFrame);
        sb.append(" packetsPerFrame, ");
        sb.append(this.aFPacketsInLastFrame);
        sb.append(" in last Frame");
        Log.d(TAG, sb.toString());
        this.aFrameLength = i;
    }

    public void createWaveFile(String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/BLEAudioFiles");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdir();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Writing file :");
        sb2.append(Environment.getExternalStorageDirectory().getPath());
        sb2.append(str);
        Log.d(TAG, sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(Environment.getExternalStorageDirectory().getPath());
        sb3.append(str);
        this.f25os = new FileOutputStream(new File(sb3.toString()));
        this.bos = new BufferedOutputStream(this.f25os);
        this.outFile = new DataOutputStream(this.bos);
        this.aFileTotalBytes = 0;
        this.outFile.write(writeWaveHeaderPCM(0, 16000, 32000, 0, 1, 1));
        this.aFileTotalBytes += 44;
    }

    public void closeWaveFile(String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Closed file,aFileTotalBytes :");
        sb.append(this.aFileTotalBytes);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.d(str2, sb2);
        this.outFile.close();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(Environment.getExternalStorageDirectory().getPath());
        sb3.append(str);
        this.raFile = new RandomAccessFile(sb3.toString(), "rw");
        RandomAccessFile randomAccessFile = this.raFile;
        int i = this.aFileTotalBytes;
        randomAccessFile.write(writeWaveHeaderPCM(i + 8, 16000, 32000, i - 44, 1, 16));
        this.raFile.close();
        Log.d(str2, "Closed File");
        Intent intent = new Intent("ARCBLEAudio-From-Service-Events");
        intent.putExtra("newFile", "Idle");
        LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
    }

    public void rxFrame(byte[] bArr, int i) throws IOException {
        packetParserState packetparserstate = this.aState;
        packetParserState packetparserstate2 = packetParserState.IDLE;
        String str = "ARCBLEAudio-From-Service-Events";
        String str2 = TAG;
        if (packetparserstate == packetparserstate2) {
            Log.d(str2, "Start of audio transmission");
            this.aState = packetParserState.RUNNING;
            this.aBuf = new ArrayList<>();
            this.aFBytesRemaining = this.aFrameLength;
            this.curBuf = new audioFrameBuffer();
            audioFrameBuffer audioframebuffer = this.curBuf;
            int i2 = this.aFrameLength;
            audioframebuffer.CompressedData = new byte[i2];
            audioframebuffer.PCMData = new short[((i2 - 4) * 2)];
            audioframebuffer.isDecoded = false;
            audioframebuffer.isReceived = false;
            this.decodedIndex = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
            StringBuilder sb = new StringBuilder();
            sb.append("/BLEAudioFiles/Audio");
            sb.append(simpleDateFormat.format(new Date()));
            sb.append(".wav");
            this.fileName = sb.toString();
            createWaveFile(this.fileName);
            this.foundStart = 0;
            Intent intent = new Intent(str);
            intent.putExtra("statusText", "Audio RX");
            LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
        }
        if (this.aState == packetParserState.RUNNING) {
            if (this.foundStart != 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Packet first bytes : ");
                String str3 = "%02x";
                sb2.append(String.format(str3, new Object[]{Integer.valueOf(bArr[0] & 255)}));
                String str4 = ",";
                sb2.append(str4);
                sb2.append(String.format(str3, new Object[]{Integer.valueOf(bArr[1] & 255)}));
                sb2.append(str4);
                sb2.append(String.format(str3, new Object[]{Integer.valueOf(bArr[2] & 255)}));
                sb2.append(str4);
                sb2.append(String.format(str3, new Object[]{Integer.valueOf(bArr[3] & 255)}));
                Log.d(str2, sb2.toString());
                if (bArr[0] == 9 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 0) {
                    this.foundStart = 1;
                    Log.d(str2, "Header found, continuing");
                } else {
                    return;
                }
            }
            if (i > this.aFBytesRemaining) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("To many bytes received : ");
                sb3.append(i);
                sb3.append(" but expected only ");
                sb3.append(this.aFBytesRemaining);
                Log.d(str2, sb3.toString());
            } else {
                this.bleRxTimer.cancel();
                this.bleRxTimer.purge();
                this.bleRxTimer = new Timer();
                this.bleRxTimer.schedule(new BleRxTimerTask(), 500);
                for (int i3 = 0; i3 < i; i3++) {
                    this.curBuf.CompressedData[(this.aFrameLength - this.aFBytesRemaining) + i3] = bArr[i3];
                }
                this.aFBytesRemaining -= i;
                if (this.aFBytesRemaining == 0) {
                    audioFrameBuffer audioframebuffer2 = this.curBuf;
                    audioframebuffer2.isReceived = true;
                    audioframebuffer2.sequenceNr = (byte) ((audioframebuffer2.CompressedData[0] & 255) >> 3);
                    do {
                    } while (this.lock == 1);
                    this.lock = 1;
                    this.aBuf.add(this.curBuf);
                    this.lock = 0;
                    this.curBuf = new audioFrameBuffer();
                    audioFrameBuffer audioframebuffer3 = this.curBuf;
                    int i4 = this.aFrameLength;
                    audioframebuffer3.CompressedData = new byte[i4];
                    audioframebuffer3.PCMData = new short[((i4 - 4) * 2)];
                    audioframebuffer3.isDecoded = false;
                    audioframebuffer3.isReceived = false;
                    Intent intent2 = new Intent(str);
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Frames Received : ");
                    sb4.append(this.aBuf.size());
                    intent2.putExtra("fileInfoText", sb4.toString());
                    LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent2);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Total frames in aBuf : ");
                    sb5.append(this.aBuf.size());
                    Log.d(str2, sb5.toString());
                    this.aFBytesRemaining = this.aFrameLength;
                    if (this.start == 1) {
                        this.start = 0;
                        new Thread(new Runnable() {
                            C0924ti_codec codec;
                            int lastId = 1;

                            public void run() {
                                this.codec = new C0924ti_codec();
                                while (true) {
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    while (AdvancedRemoteBLEAudioHandler.this.decodedIndex != AdvancedRemoteBLEAudioHandler.this.aBuf.size()) {
                                        do {
                                        } while (AdvancedRemoteBLEAudioHandler.this.lock == 1);
                                        AdvancedRemoteBLEAudioHandler advancedRemoteBLEAudioHandler = AdvancedRemoteBLEAudioHandler.this;
                                        advancedRemoteBLEAudioHandler.lock = 1;
                                        audioFrameBuffer audioframebuffer = (audioFrameBuffer) advancedRemoteBLEAudioHandler.aBuf.get(AdvancedRemoteBLEAudioHandler.this.decodedIndex);
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Decoding frame with ID ");
                                        String str = "%02x";
                                        sb.append(String.format(str, new Object[]{Integer.valueOf(audioframebuffer.sequenceNr)}));
                                        sb.append(" Codec Prediction : PV_Dec=");
                                        sb.append(String.format("%04x", new Object[]{Short.valueOf(this.codec.PV_Dec)}));
                                        sb.append(" SI_Dec=");
                                        sb.append(String.format(str, new Object[]{Short.valueOf(this.codec.SI_Dec)}));
                                        String sb2 = sb.toString();
                                        String str2 = AdvancedRemoteBLEAudioHandler.TAG;
                                        Log.d(str2, sb2);
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("Frame received prediction values PV_Dec = ");
                                        sb3.append(String.format("%02x%02x", new Object[]{Byte.valueOf(audioframebuffer.CompressedData[3]), Byte.valueOf(audioframebuffer.CompressedData[2])}));
                                        sb3.append(" SI_Dec = ");
                                        sb3.append(String.format(str, new Object[]{Byte.valueOf(audioframebuffer.CompressedData[1])}));
                                        Log.d(str2, sb3.toString());
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append("aBuf.size ");
                                        sb4.append(AdvancedRemoteBLEAudioHandler.this.aBuf.size());
                                        Log.d(str2, sb4.toString());
                                        int i = (audioframebuffer.sequenceNr - this.lastId) - 1;
                                        if (i < 0) {
                                            i += 32;
                                        }
                                        if (i > AdvancedRemoteBLEAudioHandler.this.aBuf.size() - 1) {
                                            Log.d(str2, "Start of audio, no missing frames");
                                            i = 0;
                                        }
                                        if (i != 0) {
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append("Missing ");
                                            sb5.append(i);
                                            sb5.append(" frames, inserting same audio");
                                            Log.d(str2, sb5.toString());
                                        }
                                        if (!(audioframebuffer.CompressedData[1] == this.codec.SI_Dec && audioframebuffer.CompressedData[3] == ((this.codec.PV_Dec >> 8) & 255) && audioframebuffer.CompressedData[2] == (this.codec.PV_Dec & 255))) {
                                            Log.d(str2, "Prediction values from package differs, using packet data in decompression");
                                            this.codec.SI_Dec = (short) (audioframebuffer.CompressedData[1] & 255);
                                            this.codec.PV_Dec = (short) ((audioframebuffer.CompressedData[3] << 8) | (audioframebuffer.CompressedData[2] & 255));
                                        }
                                        int i2 = 0;
                                        int i3 = 0;
                                        while (i2 < (audioframebuffer.CompressedData.length - 4) * 2) {
                                            byte b = audioframebuffer.CompressedData[i3 + 4];
                                            int i4 = i2 + 1;
                                            audioframebuffer.PCMData[i2] = this.codec.codec_DecodeSingle(b & 15);
                                            int i5 = i4 + 1;
                                            audioframebuffer.PCMData[i4] = this.codec.codec_DecodeSingle((byte) (((byte) (b >> 4)) & 15));
                                            i3++;
                                            i2 = i5;
                                        }
                                        audioframebuffer.isDecoded = true;
                                        for (int i6 = 0; i6 < i + 1; i6++) {
                                            int i7 = 0;
                                            for (int i8 = 0; i8 < audioframebuffer.PCMData.length; i8++) {
                                                if (i == 0 || i6 >= i) {
                                                    AdvancedRemoteBLEAudioHandler.this.outFile.writeByte(audioframebuffer.PCMData[i8] & 255);
                                                    AdvancedRemoteBLEAudioHandler.this.outFile.writeByte((audioframebuffer.PCMData[i8] >> 8) & 255);
                                                } else {
                                                    try {
                                                        AdvancedRemoteBLEAudioHandler.this.outFile.writeByte(0);
                                                        AdvancedRemoteBLEAudioHandler.this.outFile.writeByte(0);
                                                    } catch (IOException e2) {
                                                        e2.printStackTrace();
                                                    }
                                                }
                                                if (Math.abs(audioframebuffer.PCMData[i8]) > i7) {
                                                    i7 = Math.abs(audioframebuffer.PCMData[i8]);
                                                }
                                                AdvancedRemoteBLEAudioHandler.this.aFileTotalBytes += 2;
                                            }
                                            Intent intent = new Intent("ARCBLEAudio-From-Service-Events");
                                            intent.putExtra("peakVU", i7);
                                            LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioHandler.this.context).sendBroadcast(intent);
                                        }
                                        AdvancedRemoteBLEAudioHandler.this.decodedIndex++;
                                        this.lastId = audioframebuffer.sequenceNr;
                                        StringBuilder sb6 = new StringBuilder();
                                        sb6.append("Finished with ");
                                        sb6.append(AdvancedRemoteBLEAudioHandler.this.decodedIndex);
                                        sb6.append(" Left ");
                                        sb6.append(AdvancedRemoteBLEAudioHandler.this.aBuf.size() - AdvancedRemoteBLEAudioHandler.this.decodedIndex);
                                        Log.d(str2, sb6.toString());
                                        AdvancedRemoteBLEAudioHandler.this.lock = 0;
                                    }
                                }
                            }
                        }).start();
                    }
                }
            }
        }
    }

    public void printAudioPacket(short[] sArr) {
        String str = new String();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("Samples: ");
        String sb2 = sb.toString();
        for (short s : sArr) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(String.format("%04x", new Object[]{Integer.valueOf(s & 65535)}));
            String sb4 = sb3.toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append(sb4);
            sb5.append(",");
            sb2 = sb5.toString();
        }
        Log.d(TAG, sb2);
    }
}
