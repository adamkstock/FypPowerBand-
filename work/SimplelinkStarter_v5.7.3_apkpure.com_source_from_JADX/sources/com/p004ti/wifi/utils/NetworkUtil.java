package com.p004ti.wifi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/* renamed from: com.ti.wifi.utils.NetworkUtil */
public class NetworkUtil {
    private static int MOBILE = 2;
    private static int NOT_CONNECTED = 0;
    private static final String TAG = "NetworkUtil";
    public static int WIFI = 1;

    /* renamed from: com.ti.wifi.utils.NetworkUtil$2 */
    static /* synthetic */ class C10352 {
        static final /* synthetic */ int[] $SwitchMap$com$ti$wifi$utils$DeviceVersion = new int[DeviceVersion.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$ti$wifi$utils$SecurityType = new int[SecurityType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(33:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|(2:23|24)|25|27|28|29|30|31|32|33|35|36|37|38|39|40|41|42|(3:43|44|46)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(37:0|(2:1|2)|3|5|6|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|35|36|37|38|39|40|41|42|43|44|46) */
        /* JADX WARNING: Can't wrap try/catch for region: R(38:0|(2:1|2)|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|35|36|37|38|39|40|41|42|43|44|46) */
        /* JADX WARNING: Can't wrap try/catch for region: R(39:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|35|36|37|38|39|40|41|42|43|44|46) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0075 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x007f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x009c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00a6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00b0 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00ba */
        static {
            /*
                com.ti.wifi.utils.CFG_Result_Enum[] r0 = com.p004ti.wifi.utils.CFG_Result_Enum.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum = r0
                r0 = 1
                int[] r1 = $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.ti.wifi.utils.CFG_Result_Enum r2 = com.p004ti.wifi.utils.CFG_Result_Enum.Failure     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum     // Catch:{ NoSuchFieldError -> 0x001f }
                com.ti.wifi.utils.CFG_Result_Enum r3 = com.p004ti.wifi.utils.CFG_Result_Enum.Success     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum     // Catch:{ NoSuchFieldError -> 0x002a }
                com.ti.wifi.utils.CFG_Result_Enum r4 = com.p004ti.wifi.utils.CFG_Result_Enum.Time_Out     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                r3 = 4
                int[] r4 = $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.ti.wifi.utils.CFG_Result_Enum r5 = com.p004ti.wifi.utils.CFG_Result_Enum.Unknown_Token     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                r4 = 5
                int[] r5 = $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.ti.wifi.utils.CFG_Result_Enum r6 = com.p004ti.wifi.utils.CFG_Result_Enum.Not_Started     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r5[r6] = r4     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r5 = $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum     // Catch:{ NoSuchFieldError -> 0x004b }
                com.ti.wifi.utils.CFG_Result_Enum r6 = com.p004ti.wifi.utils.CFG_Result_Enum.Ap_Not_Found     // Catch:{ NoSuchFieldError -> 0x004b }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r7 = 6
                r5[r6] = r7     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r5 = $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum     // Catch:{ NoSuchFieldError -> 0x0056 }
                com.ti.wifi.utils.CFG_Result_Enum r6 = com.p004ti.wifi.utils.CFG_Result_Enum.Ip_Add_Fail     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r7 = 7
                r5[r6] = r7     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                int[] r5 = $SwitchMap$com$ti$wifi$utils$CFG_Result_Enum     // Catch:{ NoSuchFieldError -> 0x0062 }
                com.ti.wifi.utils.CFG_Result_Enum r6 = com.p004ti.wifi.utils.CFG_Result_Enum.Wrong_Password     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r7 = 8
                r5[r6] = r7     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                com.ti.wifi.utils.DeviceVersion[] r5 = com.p004ti.wifi.utils.DeviceVersion.values()
                int r5 = r5.length
                int[] r5 = new int[r5]
                $SwitchMap$com$ti$wifi$utils$DeviceVersion = r5
                int[] r5 = $SwitchMap$com$ti$wifi$utils$DeviceVersion     // Catch:{ NoSuchFieldError -> 0x0075 }
                com.ti.wifi.utils.DeviceVersion r6 = com.p004ti.wifi.utils.DeviceVersion.R1     // Catch:{ NoSuchFieldError -> 0x0075 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0075 }
                r5[r6] = r0     // Catch:{ NoSuchFieldError -> 0x0075 }
            L_0x0075:
                int[] r5 = $SwitchMap$com$ti$wifi$utils$DeviceVersion     // Catch:{ NoSuchFieldError -> 0x007f }
                com.ti.wifi.utils.DeviceVersion r6 = com.p004ti.wifi.utils.DeviceVersion.R2     // Catch:{ NoSuchFieldError -> 0x007f }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x007f }
                r5[r6] = r1     // Catch:{ NoSuchFieldError -> 0x007f }
            L_0x007f:
                int[] r5 = $SwitchMap$com$ti$wifi$utils$DeviceVersion     // Catch:{ NoSuchFieldError -> 0x0089 }
                com.ti.wifi.utils.DeviceVersion r6 = com.p004ti.wifi.utils.DeviceVersion.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x0089 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0089 }
                r5[r6] = r2     // Catch:{ NoSuchFieldError -> 0x0089 }
            L_0x0089:
                com.ti.wifi.utils.SecurityType[] r5 = com.p004ti.wifi.utils.SecurityType.values()
                int r5 = r5.length
                int[] r5 = new int[r5]
                $SwitchMap$com$ti$wifi$utils$SecurityType = r5
                int[] r5 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x009c }
                com.ti.wifi.utils.SecurityType r6 = com.p004ti.wifi.utils.SecurityType.OPEN     // Catch:{ NoSuchFieldError -> 0x009c }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r5[r6] = r0     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x00a6 }
                com.ti.wifi.utils.SecurityType r5 = com.p004ti.wifi.utils.SecurityType.WEP     // Catch:{ NoSuchFieldError -> 0x00a6 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a6 }
                r0[r5] = r1     // Catch:{ NoSuchFieldError -> 0x00a6 }
            L_0x00a6:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x00b0 }
                com.ti.wifi.utils.SecurityType r1 = com.p004ti.wifi.utils.SecurityType.WPA1     // Catch:{ NoSuchFieldError -> 0x00b0 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b0 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00b0 }
            L_0x00b0:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x00ba }
                com.ti.wifi.utils.SecurityType r1 = com.p004ti.wifi.utils.SecurityType.WPA2     // Catch:{ NoSuchFieldError -> 0x00ba }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ba }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x00ba }
            L_0x00ba:
                int[] r0 = $SwitchMap$com$ti$wifi$utils$SecurityType     // Catch:{ NoSuchFieldError -> 0x00c4 }
                com.ti.wifi.utils.SecurityType r1 = com.p004ti.wifi.utils.SecurityType.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x00c4 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c4 }
                r0[r1] = r4     // Catch:{ NoSuchFieldError -> 0x00c4 }
            L_0x00c4:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.utils.NetworkUtil.C10352.<clinit>():void");
        }
    }

    public static int getConnectionStatus(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == 1) {
                return WIFI;
            }
            if (activeNetworkInfo.getType() == 0) {
                return MOBILE;
            }
        }
        return NOT_CONNECTED;
    }

    public static String getConnectedSSID(Context context) {
        String str;
        if (context == null) {
            return null;
        }
        int connectionStatus = getConnectionStatus(context);
        StringBuilder sb = new StringBuilder();
        sb.append("Network State:");
        sb.append(connectionStatus);
        Log.i(TAG, sb.toString());
        String str2 = "";
        if (connectionStatus == WIFI) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null) {
                    str = connectionInfo.getSSID().replaceAll("\"", str2);
                    if (str == null || str.equals("<unknown ssid>") || str.equals("0x") || str.equals(str2)) {
                        str = null;
                    }
                    return str;
                }
            }
        }
        str = null;
        str = null;
        return str;
    }

    public static String getConnectionStatusString(Context context) {
        int connectionStatus = getConnectionStatus(context);
        if (connectionStatus == WIFI) {
            return "Connected to Wifi";
        }
        return connectionStatus == MOBILE ? "Connected to Mobile Data" : "No internet connection";
    }

    public static List<ScanResult> getWifiScanResults(Boolean bool, Context context) {
        List<ScanResult> scanResults = getWifiManager(context).getScanResults();
        ArrayList arrayList = new ArrayList();
        for (ScanResult scanResult : scanResults) {
            if (!scanResult.SSID.equals("")) {
                arrayList.add(scanResult);
            }
        }
        scanResults.clear();
        scanResults.addAll(arrayList);
        if (!bool.booleanValue()) {
            return scanResults;
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (ScanResult scanResult2 : scanResults) {
            if (scanResult2.SSID.contains(Constants.DEVICE_PREFIX)) {
                arrayList2.add(scanResult2);
            } else {
                arrayList3.add(scanResult2);
            }
        }
        ArrayList removeMultipleSSIDsWithRSSI = removeMultipleSSIDsWithRSSI(arrayList2);
        removeMultipleSSIDsWithRSSI.addAll(removeMultipleSSIDsWithRSSI(arrayList3));
        return removeMultipleSSIDsWithRSSI;
    }

    private static ArrayList<ScanResult> removeMultipleSSIDsWithRSSI(ArrayList<ScanResult> arrayList) {
        ArrayList<ScanResult> arrayList2 = new ArrayList<>();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ScanResult scanResult = (ScanResult) it.next();
            boolean z = false;
            Iterator it2 = arrayList2.iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (((ScanResult) it2.next()).SSID.equals(scanResult.SSID)) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z) {
                arrayList2.add(scanResult);
            }
        }
        Collections.sort(arrayList2, new Comparator<ScanResult>() {
            public int compare(ScanResult scanResult, ScanResult scanResult2) {
                if (scanResult.level < scanResult2.level) {
                    return 1;
                }
                return scanResult.level == scanResult2.level ? 0 : -1;
            }
        });
        return arrayList2;
    }

    public static String getWifiName(Context context) {
        String ssid = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getSSID();
        String str = "";
        if (ssid == null || ssid.contains("unknown ssid") || ssid.length() <= 2) {
            return str;
        }
        String str2 = "\"";
        if (ssid.startsWith(str2) && ssid.endsWith(str2)) {
            ssid = ssid.subSequence(1, ssid.length() - 1).toString();
        }
        return ssid;
    }

    public static String getGateway(Context context) {
        return intToIp(((WifiManager) context.getSystemService("wifi")).getDhcpInfo().gateway);
    }

    private static String intToIp(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append((i >> 24) & 255);
        String str = ".";
        sb.append(str);
        sb.append((i >> 16) & 255);
        sb.append(str);
        sb.append((i >> 8) & 255);
        sb.append(str);
        sb.append(i & 255);
        return sb.toString();
    }

    public static void startScan(Context context) {
        ((WifiManager) context.getSystemService("wifi")).startScan();
    }

    private static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getSystemService("wifi");
    }

    public static void connectToKnownWifi(Context context, String str) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        for (WifiConfiguration wifiConfiguration : wifiManager.getConfiguredNetworks()) {
            if (wifiConfiguration.SSID != null) {
                String str2 = wifiConfiguration.SSID;
                StringBuilder sb = new StringBuilder();
                String str3 = "\"";
                sb.append(str3);
                sb.append(str);
                sb.append(str3);
                if (str2.equals(sb.toString())) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                    wifiManager.reconnect();
                }
            }
        }
    }

    public static Boolean connectToWifiAfterDisconnecting(Context context, String str) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        new WifiConfiguration();
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        StringBuilder sb = new StringBuilder();
        String str2 = "\"";
        sb.append(str2);
        sb.append(str);
        sb.append(str2);
        wifiConfiguration.SSID = sb.toString();
        wifiConfiguration.allowedKeyManagement.set(0);
        wifiManager.addNetwork(wifiConfiguration);
        for (WifiConfiguration wifiConfiguration2 : wifiManager.getConfiguredNetworks()) {
            if (wifiConfiguration2.SSID != null) {
                String str3 = wifiConfiguration2.SSID;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(str);
                sb2.append(str2);
                if (str3.equals(sb2.toString())) {
                    wifiManager.enableNetwork(wifiConfiguration2.networkId, true);
                    return Boolean.valueOf(wifiManager.reconnect());
                }
            }
        }
        return Boolean.valueOf(false);
    }

    public static void removeSSIDFromConfiguredNetwork(Context context, String str) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        List configuredNetworks = wifiManager.getConfiguredNetworks();
        for (int i = 0; i < configuredNetworks.size(); i++) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) configuredNetworks.get(i);
            if (wifiConfiguration.SSID != null) {
                String str2 = wifiConfiguration.SSID;
                StringBuilder sb = new StringBuilder();
                String str3 = "\"";
                sb.append(str3);
                sb.append(str);
                sb.append(str3);
                if (str2.equals(sb.toString())) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Removing network: ");
                    sb2.append(wifiConfiguration.SSID);
                    Log.w(TAG, sb2.toString());
                    wifiManager.removeNetwork(wifiConfiguration.networkId);
                    return;
                }
            }
        }
    }

    public static WifiConfiguration getWifiConfigurationWithInfo(Context context, String str, SecurityType securityType, String str2) {
        WifiManager wifiManager = getWifiManager(context);
        List configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks == null) {
            return null;
        }
        Iterator it = configuredNetworks.iterator();
        while (true) {
            boolean hasNext = it.hasNext();
            String str3 = "Wifi configuration for ";
            String str4 = TAG;
            String str5 = "\"";
            if (hasNext) {
                WifiConfiguration wifiConfiguration = (WifiConfiguration) it.next();
                if (wifiConfiguration.SSID != null) {
                    String str6 = wifiConfiguration.SSID;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str5);
                    sb.append(str);
                    sb.append(str5);
                    if (str6.equals(sb.toString())) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str3);
                        sb2.append(str);
                        sb2.append(" already exist, so we will use it");
                        Log.i(str4, sb2.toString());
                        return wifiConfiguration;
                    }
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(str);
                sb3.append(" doesn't exist, so we will create new one");
                Log.i(str4, sb3.toString());
                StringBuilder sb4 = new StringBuilder();
                sb4.append("SSID: ");
                sb4.append(str);
                Log.i(str4, sb4.toString());
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Security: ");
                sb5.append(securityType);
                Log.i(str4, sb5.toString());
                WifiConfiguration wifiConfiguration2 = new WifiConfiguration();
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str5);
                sb6.append(str);
                sb6.append(str5);
                wifiConfiguration2.SSID = sb6.toString();
                wifiConfiguration2.status = 2;
                wifiConfiguration2.hiddenSSID = false;
                int i = C10352.$SwitchMap$com$ti$wifi$utils$SecurityType[securityType.ordinal()];
                if (i == 1) {
                    wifiConfiguration2.allowedKeyManagement.set(0);
                } else if (i == 2) {
                    wifiConfiguration2.allowedKeyManagement.set(1);
                    wifiConfiguration2.allowedAuthAlgorithms.set(1);
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str5);
                    sb7.append(str2);
                    sb7.append(str5);
                    wifiConfiguration2.preSharedKey = sb7.toString();
                } else if (i == 3) {
                    wifiConfiguration2.allowedKeyManagement.set(1);
                    wifiConfiguration2.allowedProtocols.set(0);
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str5);
                    sb8.append(str2);
                    sb8.append(str5);
                    wifiConfiguration2.preSharedKey = sb8.toString();
                } else if (i == 4) {
                    wifiConfiguration2.allowedKeyManagement.set(1);
                    wifiConfiguration2.allowedProtocols.set(1);
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str5);
                    sb9.append(str2);
                    sb9.append(str5);
                    wifiConfiguration2.preSharedKey = sb9.toString();
                } else if (i == 5) {
                    if (str2 == null) {
                        wifiConfiguration2.allowedKeyManagement.set(0);
                    } else {
                        wifiConfiguration2.allowedKeyManagement.set(1);
                        wifiConfiguration2.allowedProtocols.set(0);
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(str5);
                        sb10.append(str2);
                        sb10.append(str5);
                        wifiConfiguration2.preSharedKey = sb10.toString();
                    }
                }
                StringBuilder sb11 = new StringBuilder();
                sb11.append("New wifi configuration with id ");
                sb11.append(wifiManager.addNetwork(wifiConfiguration2));
                Log.i(str4, sb11.toString());
                StringBuilder sb12 = new StringBuilder();
                sb12.append("Saving configuration ");
                sb12.append(wifiManager.saveConfiguration());
                Log.i(str4, sb12.toString());
                StringBuilder sb13 = new StringBuilder();
                sb13.append("wc.networkId ");
                sb13.append(wifiConfiguration2.networkId);
                Log.i(str4, sb13.toString());
                for (WifiConfiguration wifiConfiguration3 : wifiManager.getConfiguredNetworks()) {
                    if (wifiConfiguration3.SSID != null) {
                        String str7 = wifiConfiguration3.SSID;
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append(str5);
                        sb14.append(str);
                        sb14.append(str5);
                        if (str7.equals(sb14.toString())) {
                            StringBuilder sb15 = new StringBuilder();
                            sb15.append("Returning wifiConfiguration with id ");
                            sb15.append(wifiConfiguration3.networkId);
                            Log.i(str4, sb15.toString());
                            return wifiConfiguration3;
                        }
                    }
                }
                return null;
            }
        }
    }

    public static void connectToWifiWithInfo(Context context, String str, SecurityType securityType, String str2) {
        String str3;
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        List configuredNetworks = wifiManager.getConfiguredNetworks();
        int i = 0;
        int i2 = 0;
        while (true) {
            str3 = "\"";
            if (i >= configuredNetworks.size()) {
                break;
            }
            WifiConfiguration wifiConfiguration = (WifiConfiguration) configuredNetworks.get(i);
            if (wifiConfiguration.SSID != null) {
                String str4 = wifiConfiguration.SSID;
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(str);
                sb.append(str3);
                if (str4.equals(sb.toString())) {
                    i2++;
                }
            }
            i++;
        }
        PrintStream printStream = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Done checking doubles: ");
        sb2.append(i2);
        printStream.println(sb2.toString());
        Iterator it = configuredNetworks.iterator();
        while (true) {
            boolean hasNext = it.hasNext();
            String str5 = TAG;
            if (hasNext) {
                WifiConfiguration wifiConfiguration2 = (WifiConfiguration) it.next();
                if (wifiConfiguration2.SSID != null) {
                    String str6 = wifiConfiguration2.SSID;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str3);
                    sb3.append(str);
                    sb3.append(str3);
                    if (str6.equals(sb3.toString())) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Trying to disconnect (success = ");
                        sb4.append(wifiManager.disconnect());
                        String str7 = ")";
                        sb4.append(str7);
                        Log.i(str5, sb4.toString());
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Trying to connect to ");
                        sb5.append(wifiConfiguration2.SSID);
                        sb5.append(" (success = ");
                        sb5.append(wifiManager.enableNetwork(wifiConfiguration2.networkId, true));
                        sb5.append(str7);
                        Log.i(str5, sb5.toString());
                        return;
                    }
                }
            } else {
                WifiConfiguration wifiConfiguration3 = new WifiConfiguration();
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str3);
                sb6.append(str);
                sb6.append(str3);
                wifiConfiguration3.SSID = sb6.toString();
                wifiConfiguration3.status = 2;
                wifiConfiguration3.hiddenSSID = false;
                int i3 = C10352.$SwitchMap$com$ti$wifi$utils$SecurityType[securityType.ordinal()];
                if (i3 == 1) {
                    wifiConfiguration3.allowedKeyManagement.set(0);
                } else if (i3 == 2) {
                    wifiConfiguration3.allowedKeyManagement.set(1);
                    wifiConfiguration3.allowedAuthAlgorithms.set(1);
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str3);
                    sb7.append(str2);
                    sb7.append(str3);
                    wifiConfiguration3.preSharedKey = sb7.toString();
                } else if (i3 == 3) {
                    wifiConfiguration3.allowedKeyManagement.set(1);
                    wifiConfiguration3.allowedProtocols.set(0);
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str3);
                    sb8.append(str2);
                    sb8.append(str3);
                    wifiConfiguration3.preSharedKey = sb8.toString();
                } else if (i3 == 4) {
                    wifiConfiguration3.allowedKeyManagement.set(1);
                    wifiConfiguration3.allowedProtocols.set(1);
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str3);
                    sb9.append(str2);
                    sb9.append(str3);
                    wifiConfiguration3.preSharedKey = sb9.toString();
                } else if (i3 == 5) {
                    if (str2 == null) {
                        wifiConfiguration3.allowedKeyManagement.set(0);
                    } else {
                        wifiConfiguration3.allowedKeyManagement.set(1);
                        wifiConfiguration3.allowedProtocols.set(0);
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(str3);
                        sb10.append(str2);
                        sb10.append(str3);
                        wifiConfiguration3.preSharedKey = sb10.toString();
                    }
                }
                int addNetwork = wifiManager.addNetwork(wifiConfiguration3);
                StringBuilder sb11 = new StringBuilder();
                sb11.append("addnetwork :");
                sb11.append(addNetwork);
                Log.i(str5, sb11.toString());
                wifiManager.disconnect();
                wifiManager.enableNetwork(addNetwork, true);
                wifiManager.saveConfiguration();
                return;
            }
        }
    }

    public static Boolean isLollipopAndUp() {
        return Boolean.valueOf(VERSION.SDK_INT >= 21);
    }

    public static SecurityType getScanResultSecurity(ScanResult scanResult) {
        String str = scanResult != null ? scanResult.capabilities : "";
        SecurityType securityType = scanResult != null ? SecurityType.OPEN : SecurityType.UNKNOWN;
        if (str.contains("WEP")) {
            return SecurityType.WEP;
        }
        if (str.contains("WPA2")) {
            return SecurityType.WPA2;
        }
        return str.contains("WPA") ? SecurityType.WPA1 : securityType;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:18|19|20|21|22|23|24|30) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0072, code lost:
        if (addProfile(r10, com.p004ti.wifi.utils.SecurityType.WPA1, r12, r13, r14, r15).booleanValue() != false) goto L_0x0076;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x00cf */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Boolean addProfile(java.lang.String r10, com.p004ti.wifi.utils.SecurityType r11, java.lang.String r12, java.lang.String r13, java.lang.String r14, com.p004ti.wifi.utils.DeviceVersion r15) {
        /*
            java.lang.String r0 = "__SL_P_P.D"
            int[] r1 = com.p004ti.wifi.utils.NetworkUtil.C10352.$SwitchMap$com$ti$wifi$utils$DeviceVersion
            int r2 = r15.ordinal()
            r1 = r1[r2]
            r2 = 1
            if (r1 == r2) goto L_0x0024
            r3 = 2
            if (r1 == r3) goto L_0x0012
            r1 = r10
            goto L_0x0035
        L_0x0012:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r10)
            java.lang.String r3 = "/api/1/wlan/profile_add"
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            goto L_0x0035
        L_0x0024:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r10)
            java.lang.String r3 = "/profiles_add.html"
            r1.append(r3)
            java.lang.String r1 = r1.toString()
        L_0x0035:
            r3 = 0
            java.lang.Boolean.valueOf(r3)
            com.ti.wifi.utils.SecurityType r4 = com.p004ti.wifi.utils.SecurityType.UNKNOWN
            if (r11 != r4) goto L_0x007b
            java.lang.String r11 = ""
            boolean r11 = r13.matches(r11)
            if (r11 == 0) goto L_0x0052
            com.ti.wifi.utils.SecurityType r5 = com.p004ti.wifi.utils.SecurityType.OPEN
            r4 = r10
            r6 = r12
            r7 = r13
            r8 = r14
            r9 = r15
            java.lang.Boolean r10 = addProfile(r4, r5, r6, r7, r8, r9)
            goto L_0x00f3
        L_0x0052:
            com.ti.wifi.utils.SecurityType r5 = com.p004ti.wifi.utils.SecurityType.WEP
            r4 = r10
            r6 = r12
            r7 = r13
            r8 = r14
            r9 = r15
            java.lang.Boolean r11 = addProfile(r4, r5, r6, r7, r8, r9)
            boolean r11 = r11.booleanValue()
            if (r11 == 0) goto L_0x0075
            com.ti.wifi.utils.SecurityType r5 = com.p004ti.wifi.utils.SecurityType.WPA1
            r4 = r10
            r6 = r12
            r7 = r13
            r8 = r14
            r9 = r15
            java.lang.Boolean r10 = addProfile(r4, r5, r6, r7, r8, r9)
            boolean r10 = r10.booleanValue()
            if (r10 == 0) goto L_0x0075
            goto L_0x0076
        L_0x0075:
            r2 = 0
        L_0x0076:
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r2)
            goto L_0x00f3
        L_0x007b:
            org.apache.http.impl.client.DefaultHttpClient r10 = new org.apache.http.impl.client.DefaultHttpClient     // Catch:{ Exception -> 0x00eb }
            r10.<init>()     // Catch:{ Exception -> 0x00eb }
            org.apache.http.client.methods.HttpPost r15 = new org.apache.http.client.methods.HttpPost     // Catch:{ Exception -> 0x00eb }
            r15.<init>(r1)     // Catch:{ Exception -> 0x00eb }
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ Exception -> 0x00eb }
            r4 = 4
            r1.<init>(r4)     // Catch:{ Exception -> 0x00eb }
            java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x00eb }
            java.lang.String r5 = "UTF-8"
            byte[] r12 = r12.getBytes(r5)     // Catch:{ Exception -> 0x00eb }
            java.lang.String r5 = "ISO-8859-1"
            r4.<init>(r12, r5)     // Catch:{ Exception -> 0x00eb }
            org.apache.http.message.BasicNameValuePair r12 = new org.apache.http.message.BasicNameValuePair     // Catch:{ Exception -> 0x00eb }
            java.lang.String r5 = "__SL_P_P.A"
            r12.<init>(r5, r4)     // Catch:{ Exception -> 0x00eb }
            r1.add(r12)     // Catch:{ Exception -> 0x00eb }
            org.apache.http.message.BasicNameValuePair r12 = new org.apache.http.message.BasicNameValuePair     // Catch:{ Exception -> 0x00eb }
            java.lang.String r4 = "__SL_P_P.B"
            int r11 = com.p004ti.wifi.utils.SecurityType.getIntValue(r11)     // Catch:{ Exception -> 0x00eb }
            java.lang.String r11 = java.lang.String.valueOf(r11)     // Catch:{ Exception -> 0x00eb }
            r12.<init>(r4, r11)     // Catch:{ Exception -> 0x00eb }
            r1.add(r12)     // Catch:{ Exception -> 0x00eb }
            org.apache.http.message.BasicNameValuePair r11 = new org.apache.http.message.BasicNameValuePair     // Catch:{ Exception -> 0x00eb }
            java.lang.String r12 = "__SL_P_P.C"
            r11.<init>(r12, r13)     // Catch:{ Exception -> 0x00eb }
            r1.add(r11)     // Catch:{ Exception -> 0x00eb }
            int r11 = java.lang.Integer.parseInt(r14)     // Catch:{ NumberFormatException -> 0x00cf }
            org.apache.http.message.BasicNameValuePair r12 = new org.apache.http.message.BasicNameValuePair     // Catch:{ NumberFormatException -> 0x00cf }
            java.lang.String r11 = java.lang.String.valueOf(r11)     // Catch:{ NumberFormatException -> 0x00cf }
            r12.<init>(r0, r11)     // Catch:{ NumberFormatException -> 0x00cf }
            r1.add(r12)     // Catch:{ NumberFormatException -> 0x00cf }
            goto L_0x00db
        L_0x00cf:
            org.apache.http.message.BasicNameValuePair r11 = new org.apache.http.message.BasicNameValuePair     // Catch:{ Exception -> 0x00eb }
            java.lang.String r12 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x00eb }
            r11.<init>(r0, r12)     // Catch:{ Exception -> 0x00eb }
            r1.add(r11)     // Catch:{ Exception -> 0x00eb }
        L_0x00db:
            org.apache.http.client.entity.UrlEncodedFormEntity r11 = new org.apache.http.client.entity.UrlEncodedFormEntity     // Catch:{ Exception -> 0x00eb }
            r11.<init>(r1)     // Catch:{ Exception -> 0x00eb }
            r15.setEntity(r11)     // Catch:{ Exception -> 0x00eb }
            r10.execute(r15)     // Catch:{ Exception -> 0x00eb }
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x00eb }
            goto L_0x00f3
        L_0x00eb:
            r10 = move-exception
            r10.printStackTrace()
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r3)
        L_0x00f3:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.wifi.utils.NetworkUtil.addProfile(java.lang.String, com.ti.wifi.utils.SecurityType, java.lang.String, java.lang.String, java.lang.String, com.ti.wifi.utils.DeviceVersion):java.lang.Boolean");
    }

    public static Boolean moveStateMachineAfterProfileAddition(String str, String str2, DeviceVersion deviceVersion) {
        int i = C10352.$SwitchMap$com$ti$wifi$utils$DeviceVersion[deviceVersion.ordinal()];
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("/add_profile.html");
            str = sb.toString();
        } else if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("/api/1/wlan/confirm_req");
            str = sb2.toString();
        }
        Boolean.valueOf(false);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(str);
            if (C10352.$SwitchMap$com$ti$wifi$utils$DeviceVersion[deviceVersion.ordinal()] == 1) {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(new BasicNameValuePair("__SL_P_UAN", str2));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
            }
            defaultHttpClient.execute(httpPost);
            return Boolean.valueOf(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    public static DeviceVersion getSLVersion(String str) {
        String str2 = "2.0";
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/param_product_version.txt");
        String sb2 = sb.toString();
        DeviceVersion deviceVersion = DeviceVersion.UNKNOWN;
        try {
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 3000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 5000);
            String entityUtils = EntityUtils.toString(new DefaultHttpClient(basicHttpParams).execute(new HttpGet(sb2)).getEntity());
            if (!entityUtils.equals("R1.0")) {
                if (!entityUtils.contains("1.0")) {
                    if (entityUtils.equals("R2.0") || entityUtils.equals(str2) || entityUtils.contains(str2)) {
                        return DeviceVersion.R2;
                    }
                    return deviceVersion;
                }
            }
            return DeviceVersion.R1;
        } catch (Exception e) {
            e.printStackTrace();
            return deviceVersion;
        }
    }

    public static String getDeviceName(String str, DeviceVersion deviceVersion) {
        int i = C10352.$SwitchMap$com$ti$wifi$utils$DeviceVersion[deviceVersion.ordinal()];
        String str2 = "/param_device_name.txt";
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(str2);
            str = sb.toString();
        } else if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(str2);
            str = sb2.toString();
        }
        try {
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 3000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 5000);
            return EntityUtils.toString(new DefaultHttpClient(basicHttpParams).execute(new HttpGet(str)).getEntity());
        } catch (Exception unused) {
            Log.e(TAG, "Failed to fetch device name from board");
            return "";
        }
    }

    public static ArrayList<String> getSSIDListFromDevice(String str, DeviceVersion deviceVersion) {
        String[] split;
        if (C10352.$SwitchMap$com$ti$wifi$utils$DeviceVersion[deviceVersion.ordinal()] != 1) {
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/netlist.txt");
        String sb2 = sb.toString();
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Getting list from url: ");
        sb3.append(sb2);
        String sb4 = sb3.toString();
        String str2 = TAG;
        Log.d(str2, sb4);
        try {
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 3000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 5000);
            String entityUtils = EntityUtils.toString(new DefaultHttpClient(basicHttpParams).execute(new HttpGet(sb2)).getEntity());
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Got netlist with results: ");
            sb5.append(entityUtils);
            Log.d(str2, sb5.toString());
            for (String str3 : entityUtils.split(";")) {
                if (!str3.equals("X")) {
                    arrayList.add(str3);
                }
            }
            return arrayList;
        } catch (Exception e) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Failed to get netlist: ");
            sb6.append(e.getMessage());
            Log.d(str2, sb6.toString());
            return null;
        }
    }

    public static Boolean rescanNetworksOnDevice(String str, DeviceVersion deviceVersion) {
        Boolean valueOf = Boolean.valueOf(false);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        StringBuilder sb = new StringBuilder();
        sb.append("Rescaning with url:");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.d(str2, sb2);
        int i = C10352.$SwitchMap$com$ti$wifi$utils$DeviceVersion[deviceVersion.ordinal()];
        if (i == 1) {
            ArrayList arrayList = new ArrayList(1);
            try {
                HttpPost httpPost = new HttpPost(str);
                arrayList.add(new BasicNameValuePair("__SL_P_UFS", "just empty information"));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
                defaultHttpClient.execute(httpPost);
                return Boolean.valueOf(true);
            } catch (Exception e) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Failed to perform rescan: ");
                sb3.append(e.getMessage());
                Log.d(str2, sb3.toString());
                return valueOf;
            }
        } else if (i != 2) {
            return valueOf;
        } else {
            ArrayList arrayList2 = new ArrayList(2);
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append("/api/1/wlan/en_ap_scan");
            try {
                HttpPost httpPost2 = new HttpPost(sb4.toString());
                arrayList2.add(new BasicNameValuePair("__SL_P_SC1", "10"));
                arrayList2.add(new BasicNameValuePair("__SL_P_SC2", "1"));
                httpPost2.setEntity(new UrlEncodedFormEntity(arrayList2));
                defaultHttpClient.execute(httpPost2);
                return Boolean.valueOf(true);
            } catch (Exception e2) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Failed to perform rescan (R2): ");
                sb5.append(e2.getMessage());
                Log.d(str2, sb5.toString());
                return valueOf;
            }
        }
    }

    public static CFG_Result_Enum cfgEnumForResponse(String str) {
        if (str.contains("5") || str.contains("4")) {
            return CFG_Result_Enum.Success;
        }
        if (str.contains(Constants.DEVICE_UNKNOWN_TOKEN_STRING)) {
            return CFG_Result_Enum.Unknown_Token;
        }
        if (str.contains("Timeout")) {
            return CFG_Result_Enum.Time_Out;
        }
        if (str.contains("0")) {
            return CFG_Result_Enum.Not_Started;
        }
        if (str.contains("1")) {
            return CFG_Result_Enum.Ap_Not_Found;
        }
        if (str.contains("2")) {
            return CFG_Result_Enum.Wrong_Password;
        }
        if (str.contains("3")) {
            return CFG_Result_Enum.Ip_Add_Fail;
        }
        return CFG_Result_Enum.Failure;
    }

    public static Boolean setNewDeviceName(String str, String str2, DeviceVersion deviceVersion) {
        int i = C10352.$SwitchMap$com$ti$wifi$utils$DeviceVersion[deviceVersion.ordinal()];
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("/mode_config");
            str2 = sb.toString();
        } else if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append("/api/1/netapp/set_urn");
            str2 = sb2.toString();
        }
        Boolean.valueOf(false);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(str2);
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(new BasicNameValuePair("__SL_P_S.B", new String(str.getBytes(HTTP.UTF_8), "ISO-8859-1")));
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
            defaultHttpClient.execute(httpPost);
            return Boolean.valueOf(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    public static Boolean setIotUuid(String str, String str2) {
        Boolean bool;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("/api/1/iotlink/uuid");
        String sb2 = sb.toString();
        boolean equals = str.equals("");
        Boolean valueOf = Boolean.valueOf(false);
        if (equals) {
            return valueOf;
        }
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 1000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 1000);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        try {
            HttpPost httpPost = new HttpPost(sb2);
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(new BasicNameValuePair("uuid", new String(str.getBytes(HTTP.UTF_8), "ISO-8859-1")));
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
            bool = defaultHttpClient.execute(httpPost).getStatusLine().getStatusCode() == 200 ? Boolean.valueOf(true) : valueOf;
            defaultHttpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            defaultHttpClient.getConnectionManager().shutdown();
            bool = valueOf;
        }
        return bool;
    }

    public static String getCGFResultFromDevice(String str) {
        String str2 = "****CFG result returned: ";
        String str3 = "CFG result returned empty!";
        String str4 = "Timeout";
        String str5 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/param_cfg_result.txt");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("getCGFResultFromDevice/getCFGResult url: ");
        sb3.append(sb2);
        String str6 = "StageThreeAPMode";
        Log.d(str6, sb3.toString());
        try {
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 5000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 5000);
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
            HttpGet httpGet = new HttpGet(sb2);
            Log.d(str5, "******* PRE EXECUTE *******");
            HttpResponse execute = defaultHttpClient.execute(httpGet);
            Log.d(str5, "******* POST EXECUTE *******");
            String entityUtils = EntityUtils.toString(execute.getEntity());
            if (entityUtils.equals("")) {
                Log.w(str5, str3);
                Log.w(str6, str3);
                Log.w(str6, "getCGFResultFromDevice/CFG result returned empty!");
                return str4;
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(entityUtils);
            Log.i(str5, sb4.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str2);
            sb5.append(entityUtils);
            Log.i(str6, sb5.toString());
            return entityUtils;
        } catch (Exception e) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Failed to get CFG result: ");
            sb6.append(e.getMessage());
            Log.e(str5, sb6.toString());
            StringBuilder sb7 = new StringBuilder();
            sb7.append("getCGFResultFromDevice/Failed to get CFG result: ");
            sb7.append(e.getMessage());
            Log.e(str6, sb7.toString());
            StringBuilder sb8 = new StringBuilder();
            sb8.append("ex: ");
            sb8.append(e.getMessage());
            Log.e(str6, sb8.toString());
            e.printStackTrace();
            return str4;
        }
    }

    public static String getErrorMsgForCFGResult(CFG_Result_Enum cFG_Result_Enum) {
        switch (cFG_Result_Enum) {
            case Failure:
                return CFG_Result_Enum.FAILURE_STRING;
            case Success:
                return CFG_Result_Enum.SUCCESS_STRING;
            case Time_Out:
                System.out.println("CFG_Result_Enum: Time_Out");
                break;
            case Unknown_Token:
                return CFG_Result_Enum.UNKNOWN_STRING;
            case Not_Started:
                return CFG_Result_Enum.NOT_STARTED_STRING;
            case Ap_Not_Found:
                return CFG_Result_Enum.AP_NOT_FOUND_STRING;
            case Ip_Add_Fail:
                return CFG_Result_Enum.IP_ADD_FAIL_STRING;
            case Wrong_Password:
                return CFG_Result_Enum.WRONG_PASSWORD_STRING;
        }
        return null;
    }

    public static CFG_Result_Enum getResultTypeCFGString(String str) {
        CFG_Result_Enum cFG_Result_Enum = CFG_Result_Enum.Unknown_Token;
        if (str.equals(CFG_Result_Enum.WRONG_PASSWORD_STRING)) {
            return CFG_Result_Enum.Wrong_Password;
        }
        if (str.equals(CFG_Result_Enum.IP_ADD_FAIL_STRING)) {
            return CFG_Result_Enum.Ip_Add_Fail;
        }
        if (str.equals(CFG_Result_Enum.AP_NOT_FOUND_STRING)) {
            return CFG_Result_Enum.Ap_Not_Found;
        }
        if (str.equals(CFG_Result_Enum.NOT_STARTED_STRING)) {
            return CFG_Result_Enum.Not_Started;
        }
        if (str.equals(CFG_Result_Enum.SUCCESS_STRING)) {
            return CFG_Result_Enum.Success;
        }
        return str.equals(CFG_Result_Enum.FAILURE_STRING) ? CFG_Result_Enum.Failure : cFG_Result_Enum;
    }

    public static String getDeviceNameToVerifyType(String str) {
        String str2 = TAG;
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
                    sb4.append("getDeviceNameToVerifyType/ IP of found device: ");
                    sb4.append(str);
                    Log.i(str2, sb4.toString());
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("getDeviceNameToVerifyType/ Name retrieved from found device: ");
                    sb5.append(str4);
                    Log.i(str2, sb5.toString());
                    return str4;
                }
            }
        } catch (Exception e) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("getDeviceNameToVerifyType/ Exception: ");
            sb6.append(e.toString());
            Log.e(str2, sb6.toString());
            e.printStackTrace();
            return str3;
        }
    }
}
