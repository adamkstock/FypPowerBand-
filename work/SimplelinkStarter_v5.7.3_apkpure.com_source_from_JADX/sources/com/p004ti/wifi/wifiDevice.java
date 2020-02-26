package com.p004ti.wifi;

import android.util.Log;

/* renamed from: com.ti.wifi.wifiDevice */
public class wifiDevice {
    public int age;
    private boolean isProvisioning;
    public String mBSSID;
    public String mIPAddr;
    public String mMacAddr;
    public String mName;
    public String mSSID;
    public String param_sensortag_conf_response;

    public void printInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wifi Device (");
        sb.append(this.mMacAddr);
        sb.append("), ipAddr = ");
        sb.append(this.mIPAddr);
        sb.append(", mName = ");
        sb.append(this.mName);
        sb.append(", isProvisioning = ");
        sb.append(this.isProvisioning);
        sb.append(", age= ");
        sb.append(this.age);
        Log.d("wifiDevice", sb.toString());
    }

    public String printData() {
        StringBuilder sb = new StringBuilder();
        sb.append("AGE- Wifi Device (");
        sb.append(this.mMacAddr);
        sb.append("), ipAddr = ");
        sb.append(this.mIPAddr);
        sb.append(", mName = ");
        sb.append(this.mName);
        sb.append(", isProvisioning = ");
        sb.append(this.isProvisioning);
        sb.append(", age= ");
        sb.append(this.age);
        return sb.toString();
    }
}
