package com.p004ti.wifi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* renamed from: com.ti.wifi.utils.NetInfo */
public class NetInfo {
    private static final int BUF = 8192;
    private static final String CMD_IP = " -f inet addr show %s";
    private static final String NOIF = "0";
    private static final String NOIP = "0.0.0.0";
    private static final String NOMAC = "00:00:00:00:00:00";
    private static final String NOMASK = "255.255.255.255";
    private static final String PTN_IF = "^%s: ip [0-9\\.]+ mask ([0-9\\.]+) flags.*";
    private static final String PTN_IP1 = "\\s*inet [0-9\\.]+\\/([0-9]+) brd [0-9\\.]+ scope global %s$";
    private static final String PTN_IP2 = "\\s*inet [0-9\\.]+ peer [0-9\\.]+\\/([0-9]+) scope global %s$";
    private final String TAG = "NetInfo";
    private String bssid;
    private String carrier;
    private int cidr;
    private Context ctxt;
    public String gatewayIp;
    private WifiInfo info;
    private String intf = "eth0";

    /* renamed from: ip */
    private String f86ip;
    private String macAddress;
    private String netmaskIp;
    private SharedPreferences prefs;
    private int speed;
    private String ssid;

    public NetInfo(Context context) {
        String str = NOIP;
        this.f86ip = str;
        this.cidr = 24;
        this.speed = 0;
        this.ssid = null;
        this.bssid = null;
        this.carrier = null;
        this.macAddress = NOMAC;
        this.netmaskIp = NOMASK;
        this.gatewayIp = str;
        this.ctxt = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        getIp();
        getWifiInfo();
    }

    private void getIp() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                this.intf = networkInterface.getName();
                this.f86ip = getInterfaceFirstIp(networkInterface);
                if (!Objects.equals(this.f86ip, NOIP)) {
                    break;
                }
            }
        } catch (SocketException e) {
            Log.e("NetInfo", e.getMessage());
        }
        getCidr();
    }

    private String getInterfaceFirstIp(NetworkInterface networkInterface) {
        if (networkInterface != null) {
            Enumeration inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    if (!(inetAddress instanceof Inet6Address)) {
                        return inetAddress.getHostAddress();
                    }
                    Log.i("NetInfo", "IPv6 detected and not supported yet!");
                }
            }
        }
        return NOIP;
    }

    private void getCidr() {
        String str = "NetInfo";
        String str2 = CMD_IP;
        String str3 = "/system/xbin/ip";
        if (!Objects.equals(this.netmaskIp, NOMASK)) {
            this.cidr = IpToCidr(this.netmaskIp);
            return;
        }
        try {
            String runCommand = runCommand(str3, String.format(str2, new Object[]{this.intf}), String.format(PTN_IP1, new Object[]{this.intf}));
            if (runCommand != null) {
                this.cidr = Integer.parseInt(runCommand);
                return;
            }
            String runCommand2 = runCommand(str3, String.format(str2, new Object[]{this.intf}), String.format(PTN_IP2, new Object[]{this.intf}));
            if (runCommand2 != null) {
                this.cidr = Integer.parseInt(runCommand2);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            sb.append(this.intf);
            String runCommand3 = runCommand("/system/bin/ifconfig", sb.toString(), String.format(PTN_IF, new Object[]{this.intf}));
            if (runCommand3 != null) {
                this.cidr = IpToCidr(runCommand3);
            } else {
                Log.i(str, "cannot find cidr, using default /24");
            }
        } catch (NumberFormatException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(e.getMessage());
            sb2.append(" -> cannot find cidr, using default /24");
            Log.i(str, sb2.toString());
        }
    }

    private String runCommand(String str, String str2, String str3) {
        Matcher matcher;
        try {
            if (new File(str).exists()) {
                Pattern compile = Pattern.compile(str3);
                Runtime runtime = Runtime.getRuntime();
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(str2);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runtime.exec(sb.toString()).getInputStream()), 8192);
                do {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        matcher = compile.matcher(readLine);
                    }
                } while (!matcher.matches());
                return matcher.group(1);
            }
            return null;
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't use native command: ");
            sb2.append(e.getMessage());
            Log.e("NetInfo", sb2.toString());
            return null;
        }
    }

    public boolean getMobileInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) this.ctxt.getSystemService("phone");
        if (telephonyManager != null) {
            this.carrier = telephonyManager.getNetworkOperatorName();
        }
        return false;
    }

    public boolean getWifiInfo() {
        WifiManager wifiManager = (WifiManager) this.ctxt.getSystemService("wifi");
        if (wifiManager == null) {
            return false;
        }
        this.info = wifiManager.getConnectionInfo();
        this.speed = this.info.getLinkSpeed();
        this.ssid = this.info.getSSID();
        this.bssid = this.info.getBSSID();
        this.macAddress = this.info.getMacAddress();
        this.gatewayIp = getIpFromIntSigned(wifiManager.getDhcpInfo().gateway);
        this.netmaskIp = getIpFromIntSigned(wifiManager.getDhcpInfo().netmask);
        return true;
    }

    public String getNetIp() {
        int i = 32 - this.cidr;
        return getIpFromLongUnsigned((long) ((((int) getUnsignedLongFromIp(this.f86ip)) >> i) << i));
    }

    public SupplicantState getSupplicantState() {
        return this.info.getSupplicantState();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    private static long getUnsignedLongFromIp(String str) {
        String[] split = str.split("\\.");
        return (long) ((Integer.parseInt(split[0]) * 16777216) + (Integer.parseInt(split[1]) * 65536) + (Integer.parseInt(split[2]) * 256) + Integer.parseInt(split[3]));
    }

    private static String getIpFromIntSigned(int i) {
        String str = "";
        for (int i2 = 0; i2 < 4; i2++) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append((i >> (i2 * 8)) & 255);
            sb.append(".");
            str = sb.toString();
        }
        return str.substring(0, str.length() - 1);
    }

    private static String getIpFromLongUnsigned(long j) {
        String str = "";
        for (int i = 3; i > -1; i--) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append((j >> (i * 8)) & 255);
            sb.append(".");
            str = sb.toString();
        }
        return str.substring(0, str.length() - 1);
    }

    private int IpToCidr(String str) {
        double d = -2.0d;
        for (String parseDouble : str.split("\\.")) {
            d += 256.0d - Double.parseDouble(parseDouble);
        }
        return 32 - ((int) (Math.log(d) / Math.log(2.0d)));
    }
}
