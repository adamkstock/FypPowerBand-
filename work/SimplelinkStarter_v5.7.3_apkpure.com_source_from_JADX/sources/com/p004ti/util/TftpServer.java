package com.p004ti.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.protocol.HTTP;

/* renamed from: com.ti.util.TftpServer */
public class TftpServer extends Thread {
    private static final String TAG = "TftpServer";
    public static final String TFTPSERVER_NO_PROGRESS_UPDATE_TIMEOUT = "com.ti.sensortag.TFTPSERVER_NO_PROGRESS_UPDATE_TIMEOUT";
    public static final String TFTPSERVER_PROGRESS_UPDATE_ACTION = "com.ti.sensortag.TFTPSERVER_PROGRESS_UPDATE_ACTION";
    public static final String TFTPSERVER_PROGRESS_UPDATE_PERCENTAGE = "com.ti.sensortag.TFTPSERVER_PROGRESS_UPDATE_PERCENTAGE";
    private static final int TFTP_OPCODE_ACK = 4;
    private static final int TFTP_OPCODE_DATA = 3;
    private static final int TFTP_OPCODE_ERROR = 5;
    private static final int TFTP_OPCODE_RRQ = 1;
    private static final int TFTP_OPCODE_WRQ = 2;
    /* access modifiers changed from: private */

    /* renamed from: c */
    public Context f74c;
    public DatagramSocket connection = null;
    /* access modifiers changed from: private */
    public Timer otaFailTimer = new Timer();
    private TimerTask otaFailTimerTask;
    public String ourIp;
    private int serverPort;
    public boolean shouldStop;

    public TftpServer(int i, Context context) {
        this.serverPort = i;
        this.shouldStop = false;
        this.f74c = context;
    }

    public void cancelPreviousAndStartNewTimer() {
        String str = TAG;
        Log.i(str, "OTA task failed dialog - cancelPreviousAndStartNewTimer/TftpServer");
        try {
            if (this.otaFailTimer != null) {
                this.otaFailTimer.cancel();
                this.otaFailTimer.purge();
                this.otaFailTimer = null;
            }
            this.otaFailTimer = new Timer();
            this.otaFailTimerTask = new TimerTask() {
                public void run() {
                    TftpServer.this.f74c.sendBroadcast(new Intent(TftpServer.TFTPSERVER_NO_PROGRESS_UPDATE_TIMEOUT));
                    TftpServer.this.otaFailTimer.cancel();
                    TftpServer.this.otaFailTimer.purge();
                    TftpServer.this.otaFailTimer = null;
                }
            };
            this.otaFailTimer.schedule(this.otaFailTimerTask, 25000);
            Log.i(str, "OTA task failed dialog - timer scheduled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopTimer() {
        Log.i(TAG, "OTA task failed dialog - process finished - stop timer");
        Timer timer = this.otaFailTimer;
        if (timer != null) {
            timer.cancel();
            this.otaFailTimer.purge();
            this.otaFailTimer = null;
        }
    }

    public void run() {
        String str = "File transferred correctly !";
        String str2 = "\" MODE : ";
        String str3 = "ACK: ";
        String str4 = "RRQ: \"";
        String str5 = "Received";
        this.ourIp = Formatter.formatIpAddress(((WifiManager) this.f74c.getSystemService("wifi")).getConnectionInfo().getIpAddress());
        StringBuilder sb = new StringBuilder();
        String str6 = "Started TFTPServer @ port ";
        sb.append(str6);
        sb.append(this.serverPort);
        String str7 = " My ip : ";
        sb.append(str7);
        sb.append(this.ourIp);
        String sb2 = sb.toString();
        String str8 = TAG;
        Log.d(str8, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str6);
        sb3.append(this.serverPort);
        sb3.append(str7);
        sb3.append(this.ourIp);
        String str9 = "OTA";
        Log.d(str9, sb3.toString());
        byte[] bArr = null;
        while (true) {
            byte[] bArr2 = null;
            while (!this.shouldStop) {
                try {
                    if (this.connection == null) {
                        this.connection = new DatagramSocket(this.serverPort);
                    }
                    if (bArr == null) {
                        bArr = new byte[256];
                    }
                    DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
                    this.connection.receive(datagramPacket);
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str5);
                    sb4.append(Arrays.toString(bArr));
                    Log.d(str8, sb4.toString());
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str5);
                    sb5.append(Arrays.toString(bArr));
                    Log.d(str9, sb5.toString());
                    Log.i(str8, "OTA task failed dialog - received - cancel OTA fail timer");
                    cancelPreviousAndStartNewTimer();
                    byte b = bArr[1];
                    if (b == 1) {
                        String fileNameFromCommand = getFileNameFromCommand(bArr);
                        String modeFromCommand = getModeFromCommand(bArr);
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str4);
                        sb6.append(fileNameFromCommand);
                        sb6.append(str2);
                        sb6.append(modeFromCommand);
                        Log.d(str8, sb6.toString());
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(str4);
                        sb7.append(fileNameFromCommand);
                        sb7.append(str2);
                        sb7.append(modeFromCommand);
                        Log.d(str9, sb7.toString());
                        AssetManager assets = this.f74c.getAssets();
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("22.02.2017/");
                        sb8.append(fileNameFromCommand);
                        InputStream open = assets.open(sb8.toString());
                        if (open.available() > 0) {
                            bArr2 = new byte[open.available()];
                            open.read(bArr2);
                            sendDataBlock(1, this.connection, bArr2, datagramPacket.getAddress(), datagramPacket.getPort());
                        }
                    } else if (b == 3) {
                        continue;
                    } else if (b == 4) {
                        byte b2 = (bArr[3] & 255) | ((bArr[2] & 255) << 8);
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append(str3);
                        sb9.append(b2);
                        Log.d(str8, sb9.toString());
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(str3);
                        sb10.append(b2);
                        Log.d(str9, sb10.toString());
                        if (bArr2 == null) {
                            continue;
                        } else if (b2 * 512 > bArr2.length) {
                            Log.d(str8, str);
                            Log.d(str9, str);
                        } else {
                            sendDataBlock(b2 + 1, this.connection, bArr2, datagramPacket.getAddress(), datagramPacket.getPort());
                            Intent intent = new Intent(TFTPSERVER_PROGRESS_UPDATE_ACTION);
                            intent.putExtra(TFTPSERVER_PROGRESS_UPDATE_PERCENTAGE, (b2 * 100) / (bArr2.length / 512));
                            this.f74c.sendBroadcast(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
    }

    private static void sendDataBlock(int i, DatagramSocket datagramSocket, byte[] bArr, InetAddress inetAddress, int i2) {
        int i3 = 512;
        int i4 = (i - 1) * 512;
        byte[] bArr2 = new byte[516];
        bArr2[0] = 0;
        bArr2[1] = 3;
        bArr2[2] = (byte) (i >> 8);
        bArr2[3] = (byte) (i & 255);
        String str = "OTA";
        if (i4 + 512 > bArr.length) {
            i3 = bArr.length - i4;
            StringBuilder sb = new StringBuilder();
            String str2 = "End of file reached ";
            sb.append(str2);
            sb.append(i3);
            String str3 = " bytes left";
            sb.append(str3);
            Log.d(TAG, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(i3);
            sb2.append(str3);
            Log.d(str, sb2.toString());
        }
        System.arraycopy(bArr, i4 + 0, bArr2, 4, i3);
        DatagramPacket datagramPacket = new DatagramPacket(bArr2, i3 + 4);
        datagramPacket.setAddress(inetAddress);
        datagramPacket.setPort(i2);
        try {
            datagramSocket.send(datagramPacket);
            String substring = Arrays.toString(bArr2).substring(0, 84);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("sending");
            sb3.append(substring);
            Log.d(str, sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("sending to ST - datagram packet: ");
            sb4.append(datagramPacket.toString());
            sb4.append("\nAddress: ");
            sb4.append(inetAddress);
            sb4.append("\nPort: ");
            sb4.append(i2);
            sb4.append("\nlength: ");
            sb4.append(i3);
            Log.i(str, sb4.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileNameFromCommand(byte[] bArr) {
        int i = 2;
        while (true) {
            if (i >= bArr.length) {
                i = 2;
                break;
            } else if (bArr[i] != 0) {
                i++;
            }
        }
        try {
            return new String(Arrays.copyOfRange(bArr, 2, i), HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getModeFromCommand(byte[] bArr) {
        int i = 2;
        int i2 = 2;
        while (true) {
            if (i2 >= bArr.length) {
                break;
            } else if (bArr[i2] == 0) {
                i = i2;
                break;
            } else {
                i2++;
            }
        }
        int i3 = i + 1;
        int i4 = i3;
        while (true) {
            if (i4 < bArr.length) {
                if (bArr[i4] == 0) {
                    i = i4;
                    break;
                }
                i4++;
            }
        }
        try {
            return new String(Arrays.copyOfRange(bArr, i3, i), HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String readStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        new StringBuilder();
        try {
            return bufferedReader.readLine();
        } catch (IOException unused) {
            return "";
        }
    }
}
