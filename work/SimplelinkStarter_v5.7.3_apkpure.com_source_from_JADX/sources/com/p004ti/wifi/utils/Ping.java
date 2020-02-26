package com.p004ti.wifi.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

/* renamed from: com.ti.wifi.utils.Ping */
public class Ping {
    private static final int BUF = 512;
    private final String CMD = "/system/bin/ping -n -b ";
    private final String IP_ADDRESS_PATTERN;
    private final String PTN = "\\b[0-9]+.[0-9]+.[0-9]+.[0-9]\\b";
    private final int REACH_TIMEOUT = 5000;
    private final String TAG = "Ping";
    private Handler handler_;
    private String indicator;
    public String ipToPing;
    private String line;
    private Context mContext;
    private String mGatewayIp;
    private Pattern mPattern;
    private PingCallback mPingCallback;
    public Runnable pingRunnable;
    private Process proc;
    private int rate;
    public String receivedIP;
    public boolean working;

    /* renamed from: com.ti.wifi.utils.Ping$PingCallback */
    public interface PingCallback {
        void pingCompleted();

        void pingDeviceFetched(JSONObject jSONObject);

        void pingFailed(String str);
    }

    public Ping(Context context, PingCallback pingCallback, String str) {
        String str2 = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        this.IP_ADDRESS_PATTERN = str2;
        this.proc = null;
        this.indicator = null;
        this.rate = 800;
        this.working = false;
        String str3 = "";
        this.ipToPing = str3;
        this.mGatewayIp = str3;
        this.handler_ = new Handler(Looper.getMainLooper());
        this.pingRunnable = new Runnable() {
            public void run() {
                if (!Thread.interrupted()) {
                    String str = "pingRunnable";
                    Log.d(str, "started");
                    Ping.this.before();
                    if (!Thread.interrupted()) {
                        Ping.this.working = true;
                        Log.i("Ping", "Ping Runnable started");
                        Ping ping = Ping.this;
                        ping.ping(ping.ipToPing);
                        if (!Thread.interrupted()) {
                            Ping.this.after();
                            Log.d(str, "finished");
                        }
                    }
                }
            }
        };
        this.mContext = context;
        this.mPingCallback = pingCallback;
        this.mGatewayIp = str;
        this.mPattern = Pattern.compile(str2);
    }

    /* access modifiers changed from: private */
    public void before() {
        this.working = true;
    }

    /* access modifiers changed from: private */
    public void after() {
        this.working = false;
        this.mPingCallback.pingCompleted();
    }

    public void adaptRate() {
        int avgResponseTime = getAvgResponseTime(this.indicator);
        if (avgResponseTime > 0) {
            if (avgResponseTime > 100) {
                this.rate = avgResponseTime * 5;
            } else {
                this.rate = avgResponseTime * 10;
            }
            if (this.rate > 5000) {
                this.rate = 5000;
            }
        }
    }

    private String getDeviceName(String str) {
        String str2 = "Ping";
        String str3 = "";
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(str);
        sb.append("/param_device_name.txt");
        String sb2 = sb.toString();
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 3000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 5000);
        try {
            HttpResponse execute = new DefaultHttpClient(basicHttpParams).execute(new HttpGet(sb2));
            if (execute.getStatusLine().getStatusCode() == 404) {
                return str3;
            }
            if (execute.getStatusLine().getStatusCode() == 503) {
                return str3;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(execute.getEntity().getContent()));
            String str4 = str3;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str4);
                    sb3.append(readLine);
                    str4 = sb3.toString();
                } else {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("name from file:");
                    sb4.append(str4);
                    Log.i(str2, sb4.toString());
                    return str4;
                }
            }
        } catch (HttpHostConnectException e) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Connection exception: ");
            sb5.append(e.toString());
            Log.e(str2, sb5.toString());
            return str3;
        } catch (Exception e2) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("exception: ");
            sb6.append(e2.toString());
            Log.e(str2, sb6.toString());
            e2.printStackTrace();
            return str3;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0167 A[SYNTHETIC, Splitter:B:48:0x0167] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x016e A[SYNTHETIC, Splitter:B:53:0x016e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void ping(java.lang.String r7) {
        /*
            r6 = this;
            java.lang.String r0 = "Ping"
            java.lang.String r1 = ""
            r6.receivedIP = r1
            r1 = 0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            r2.<init>()     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.lang.String r3 = "ping "
            r2.append(r3)     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            r2.append(r7)     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.lang.Process r2 = r6.proc     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            if (r2 != 0) goto L_0x013a
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            r3.<init>()     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.lang.String r4 = "/system/bin/ping -n -b "
            r3.append(r4)     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            r3.append(r7)     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.lang.String r7 = r3.toString()     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.lang.Process r7 = r2.exec(r7)     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            r6.proc = r7     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.lang.Process r3 = r6.proc     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            java.io.InputStream r3 = r3.getInputStream()     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
            r3 = 512(0x200, float:7.175E-43)
            r7.<init>(r2, r3)     // Catch:{ Exception -> 0x0149, all -> 0x0146 }
        L_0x004c:
            java.lang.String r1 = r7.readLine()     // Catch:{ Exception -> 0x0138 }
            r6.line = r1     // Catch:{ Exception -> 0x0138 }
            if (r1 == 0) goto L_0x0134
            boolean r1 = r6.working     // Catch:{ Exception -> 0x0138 }
            if (r1 != 0) goto L_0x006a
            java.lang.Process r1 = r6.proc     // Catch:{ Exception -> 0x0138 }
            if (r1 == 0) goto L_0x0061
            java.lang.Process r1 = r6.proc     // Catch:{ Exception -> 0x0138 }
            r1.destroy()     // Catch:{ Exception -> 0x0138 }
        L_0x0061:
            r7.close()     // Catch:{ IOException -> 0x0065 }
            goto L_0x0069
        L_0x0065:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0069:
            return
        L_0x006a:
            java.util.regex.Pattern r1 = r6.mPattern     // Catch:{ Exception -> 0x0138 }
            java.lang.String r2 = r6.line     // Catch:{ Exception -> 0x0138 }
            java.util.regex.Matcher r1 = r1.matcher(r2)     // Catch:{ Exception -> 0x0138 }
            boolean r2 = r1.find()     // Catch:{ Exception -> 0x0138 }
            if (r2 == 0) goto L_0x004c
            java.lang.String r2 = r6.mGatewayIp     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = r1.group()     // Catch:{ Exception -> 0x0138 }
            boolean r2 = r2.equalsIgnoreCase(r3)     // Catch:{ Exception -> 0x0138 }
            if (r2 == 0) goto L_0x008a
            java.lang.String r1 = "gateway answer - skip"
            android.util.Log.i(r0, r1)     // Catch:{ Exception -> 0x0138 }
            goto L_0x004c
        L_0x008a:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0138 }
            r2.<init>()     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = "Ping - got an answer: "
            r2.append(r3)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = r1.group()     // Catch:{ Exception -> 0x0138 }
            r2.append(r3)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0138 }
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x0138 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0138 }
            r2.<init>()     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = "previously received ip: "
            r2.append(r3)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = r6.receivedIP     // Catch:{ Exception -> 0x0138 }
            r2.append(r3)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0138 }
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x0138 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0138 }
            r2.<init>()     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = "currently received ip: "
            r2.append(r3)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = r1.group()     // Catch:{ Exception -> 0x0138 }
            r2.append(r3)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0138 }
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r2 = r6.receivedIP     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = r1.group()     // Catch:{ Exception -> 0x0138 }
            boolean r2 = r2.equals(r3)     // Catch:{ Exception -> 0x0138 }
            if (r2 == 0) goto L_0x00e3
            java.lang.String r1 = "already checked currently received ip - continue without checking it"
            android.util.Log.i(r0, r1)     // Catch:{ Exception -> 0x0138 }
            goto L_0x004c
        L_0x00e3:
            java.lang.String r2 = "Have not checked currently received ip yet - checking"
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r2 = r1.group()     // Catch:{ Exception -> 0x0138 }
            r6.receivedIP = r2     // Catch:{ Exception -> 0x0138 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0138 }
            r2.<init>()     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = "New received ip: "
            r2.append(r3)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r3 = r6.receivedIP     // Catch:{ Exception -> 0x0138 }
            r2.append(r3)     // Catch:{ Exception -> 0x0138 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0138 }
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x0138 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x012e }
            r2.<init>()     // Catch:{ Exception -> 0x012e }
            java.lang.String r3 = "Ping - checking if: "
            r2.append(r3)     // Catch:{ Exception -> 0x012e }
            java.lang.String r3 = r1.group()     // Catch:{ Exception -> 0x012e }
            r2.append(r3)     // Catch:{ Exception -> 0x012e }
            java.lang.String r3 = " is SensorTag device"
            r2.append(r3)     // Catch:{ Exception -> 0x012e }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x012e }
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x012e }
            android.content.Context r2 = r6.mContext     // Catch:{ Exception -> 0x012e }
            java.lang.String r1 = r1.group()     // Catch:{ Exception -> 0x012e }
            com.ti.wifi.utils.Ping$PingCallback r3 = r6.mPingCallback     // Catch:{ Exception -> 0x012e }
            com.p004ti.wifi.wifiDeviceActivity.getParamDeviceConfig(r2, r1, r3)     // Catch:{ Exception -> 0x012e }
            goto L_0x004c
        L_0x012e:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ Exception -> 0x0138 }
            goto L_0x004c
        L_0x0134:
            r7.close()     // Catch:{ Exception -> 0x0138 }
            goto L_0x013b
        L_0x0138:
            r1 = move-exception
            goto L_0x014d
        L_0x013a:
            r7 = r1
        L_0x013b:
            if (r7 == 0) goto L_0x016a
            r7.close()     // Catch:{ IOException -> 0x0141 }
            goto L_0x016a
        L_0x0141:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x016a
        L_0x0146:
            r0 = move-exception
            r7 = r1
            goto L_0x016c
        L_0x0149:
            r7 = move-exception
            r5 = r1
            r1 = r7
            r7 = r5
        L_0x014d:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x016b }
            r2.<init>()     // Catch:{ all -> 0x016b }
            java.lang.String r3 = "ping/ Can't use native ping: "
            r2.append(r3)     // Catch:{ all -> 0x016b }
            java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x016b }
            r2.append(r1)     // Catch:{ all -> 0x016b }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x016b }
            android.util.Log.e(r0, r1)     // Catch:{ all -> 0x016b }
            if (r7 == 0) goto L_0x016a
            r7.close()     // Catch:{ IOException -> 0x0141 }
        L_0x016a:
            return
        L_0x016b:
            r0 = move-exception
        L_0x016c:
            if (r7 == 0) goto L_0x0176
            r7.close()     // Catch:{ IOException -> 0x0172 }
            goto L_0x0176
        L_0x0172:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0176:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.utils.Ping.ping(java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00c0 A[SYNTHETIC, Splitter:B:28:0x00c0] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00cd A[SYNTHETIC, Splitter:B:35:0x00cd] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getAvgResponseTime(java.lang.String r6) {
        /*
            r5 = this;
            java.lang.String r0 = "Ping"
            r1 = 0
            java.lang.String r2 = "getAvgResponseTime"
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x00b5 }
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x00b5 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b5 }
            r3.<init>()     // Catch:{ Exception -> 0x00b5 }
            java.lang.String r4 = "/system/bin/ping -n -b "
            r3.append(r4)     // Catch:{ Exception -> 0x00b5 }
            r3.append(r6)     // Catch:{ Exception -> 0x00b5 }
            java.lang.String r6 = r3.toString()     // Catch:{ Exception -> 0x00b5 }
            java.lang.Process r6 = r2.exec(r6)     // Catch:{ Exception -> 0x00b5 }
            r6.waitFor()     // Catch:{ Exception -> 0x00b5 }
            int r2 = r6.exitValue()     // Catch:{ Exception -> 0x00b5 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b5 }
            r3.<init>()     // Catch:{ Exception -> 0x00b5 }
            java.lang.String r4 = "exit value = "
            r3.append(r4)     // Catch:{ Exception -> 0x00b5 }
            r3.append(r2)     // Catch:{ Exception -> 0x00b5 }
            java.lang.String r2 = r3.toString()     // Catch:{ Exception -> 0x00b5 }
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x00b5 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00b5 }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00b5 }
            java.io.InputStream r6 = r6.getInputStream()     // Catch:{ Exception -> 0x00b5 }
            r3.<init>(r6)     // Catch:{ Exception -> 0x00b5 }
            r6 = 512(0x200, float:7.175E-43)
            r2.<init>(r3, r6)     // Catch:{ Exception -> 0x00b5 }
        L_0x004c:
            java.lang.String r6 = r2.readLine()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r5.line = r6     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            if (r6 == 0) goto L_0x00a6
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r6.<init>()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r1 = "Answer from ping"
            r6.append(r1)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r1 = r5.line     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r6.append(r1)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            android.util.Log.i(r0, r6)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.util.regex.Pattern r6 = r5.mPattern     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r1 = r5.line     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.util.regex.Matcher r6 = r6.matcher(r1)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            boolean r1 = r6.matches()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            if (r1 == 0) goto L_0x004c
            r2.close()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r1.<init>()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r3 = "found device at:"
            r1.append(r3)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r3 = 1
            java.lang.String r4 = r6.group(r3)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r1.append(r4)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            android.util.Log.i(r0, r1)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r6 = r6.group(r3)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            float r6 = java.lang.Float.parseFloat(r6)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            int r6 = (int) r6
            r2.close()     // Catch:{ IOException -> 0x00a1 }
            goto L_0x00a5
        L_0x00a1:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00a5:
            return r6
        L_0x00a6:
            r2.close()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r2.close()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00c8
        L_0x00ad:
            r6 = move-exception
            r1 = r2
            goto L_0x00cb
        L_0x00b0:
            r6 = move-exception
            r1 = r2
            goto L_0x00b6
        L_0x00b3:
            r6 = move-exception
            goto L_0x00cb
        L_0x00b5:
            r6 = move-exception
        L_0x00b6:
            r6.printStackTrace()     // Catch:{ all -> 0x00b3 }
            java.lang.String r6 = "Can't use native ping"
            android.util.Log.e(r0, r6)     // Catch:{ all -> 0x00b3 }
            if (r1 == 0) goto L_0x00c8
            r1.close()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00c8
        L_0x00c4:
            r6 = move-exception
            r6.printStackTrace()
        L_0x00c8:
            int r6 = r5.rate
            return r6
        L_0x00cb:
            if (r1 == 0) goto L_0x00d5
            r1.close()     // Catch:{ IOException -> 0x00d1 }
            goto L_0x00d5
        L_0x00d1:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00d5:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.utils.Ping.getAvgResponseTime(java.lang.String):int");
    }

    public void stopPing() {
        Log.i("Ping", "stopPing");
        Process process = this.proc;
        if (process != null) {
            process.destroy();
        }
        this.working = false;
    }
}
