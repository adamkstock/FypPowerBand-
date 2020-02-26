package com.p004ti.wifi.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;
import com.p004ti.device_selector.TopLevel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.constants.DNSConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.ti.wifi.utils.MDnsHelper */
public class MDnsHelper implements ServiceListener, ServiceTypeListener {
    private static final String SERVICE_TYPE = "_http._tcp.local.";
    private static final String SMARTCONFIG_IDENTIFIER = "srcvers=1D90645";
    private static final String TAG = "MDnsHelper";
    private MDnsCallbackInterface callback;
    private Context context;
    /* access modifiers changed from: private */
    public boolean isDiscovering;
    /* access modifiers changed from: private */
    public JmDNSImpl jmdns;
    /* access modifiers changed from: private */
    public MulticastLock multicastLock;

    /* renamed from: wm */
    private WifiManager f85wm;

    public void init(Context context2, MDnsCallbackInterface mDnsCallbackInterface) {
        this.context = context2;
        this.callback = mDnsCallbackInterface;
        this.isDiscovering = false;
        this.f85wm = (WifiManager) context2.getSystemService("wifi");
        this.multicastLock = this.f85wm.createMulticastLock(getClass().getName());
        this.multicastLock.setReferenceCounted(true);
    }

    public void startDiscovery() {
        String str;
        String str2;
        synchronized (this) {
            if (!this.isDiscovering) {
                InetAddress deviceIpAddress = getDeviceIpAddress(this.f85wm);
                if (!this.multicastLock.isHeld()) {
                    this.multicastLock.acquire();
                } else {
                    Log.i(TAG, "Multicast lock already held");
                }
                try {
                    if (this.jmdns == null) {
                        this.jmdns = new JmDNSImpl(deviceIpAddress, "SmartConfig");
                        this.jmdns.addServiceTypeListener(this);
                    }
                    Log.w(TAG, "Starting mDNS discovery");
                    if (this.jmdns != null) {
                        this.isDiscovering = true;
                        str = TAG;
                        str2 = "discovering services";
                        Log.i(str, str2);
                    }
                } catch (IOException e) {
                    try {
                        Log.e(TAG, e.getMessage());
                        if (this.jmdns != null) {
                            this.isDiscovering = true;
                            str = TAG;
                            str2 = "discovering services";
                        }
                    } catch (Throwable th) {
                        if (this.jmdns != null) {
                            this.isDiscovering = true;
                            Log.i(TAG, "discovering services");
                        }
                        throw th;
                    }
                }
            }
        }
    }

    public void stopDiscovery() {
        synchronized (this) {
            new Thread(new Runnable() {
                public void run() {
                    if (MDnsHelper.this.isDiscovering && MDnsHelper.this.jmdns != null) {
                        try {
                            boolean isHeld = MDnsHelper.this.multicastLock.isHeld();
                            String str = MDnsHelper.TAG;
                            if (isHeld) {
                                MDnsHelper.this.multicastLock.release();
                            } else {
                                Log.i(str, "Multicast lock already released");
                            }
                            MDnsHelper.this.jmdns.closeState();
                            MDnsHelper.this.jmdns.cancelState();
                            MDnsHelper.this.jmdns.unregisterAllServices();
                            MDnsHelper.this.jmdns.cancelTimer();
                            MDnsHelper.this.jmdns.cancelStateTimer();
                            MDnsHelper.this.jmdns.purgeStateTimer();
                            MDnsHelper.this.jmdns.purgeTimer();
                            MDnsHelper.this.jmdns.recover();
                            MDnsHelper.this.jmdns.close();
                            MDnsHelper.this.jmdns = null;
                            try {
                                Thread.sleep(DNSConstants.SERVICE_INFO_TIMEOUT);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            MDnsHelper.this.isDiscovering = false;
                            Log.w(str, "mDNS discovery stopped");
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    public void restartDiscovery() {
        synchronized (this) {
            Log.w(TAG, "restartDiscovery");
            try {
                if (this.jmdns != null) {
                    this.jmdns.closeState();
                    this.jmdns.cancelState();
                    this.jmdns.unregisterAllServices();
                    this.jmdns.cancelTimer();
                    this.jmdns.closeState();
                    this.jmdns.cancelStateTimer();
                    this.jmdns.purgeStateTimer();
                    this.jmdns.purgeTimer();
                    this.jmdns.recover();
                    this.jmdns.close();
                    Thread.sleep(DNSConstants.SERVICE_INFO_TIMEOUT);
                    this.jmdns = null;
                    this.jmdns = new JmDNSImpl(getDeviceIpAddress(this.f85wm), "SmartConfig");
                    this.jmdns.addServiceTypeListener(this);
                    Log.w(TAG, "reStarting mDNS discovery");
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void serviceAdded(ServiceEvent serviceEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("serviceAdded: ");
        sb.append(serviceEvent);
        sb.append(" Nice String:");
        sb.append(serviceEvent.getInfo().getNiceTextString());
        Log.i(TAG, sb.toString());
    }

    public void serviceRemoved(ServiceEvent serviceEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("serviceRemoved: ");
        sb.append(serviceEvent);
        Log.i(TAG, sb.toString());
    }

    public void serviceResolved(ServiceEvent serviceEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("serviceResolved ");
        sb.append(serviceEvent);
        String sb2 = sb.toString();
        String str = TAG;
        Log.i(str, sb2);
        if (serviceEvent.getInfo().getNiceTextString().contains(SMARTCONFIG_IDENTIFIER)) {
            String name = serviceEvent.getName();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("mDNS / FOUND IP: ");
            sb3.append(serviceEvent.getInfo().getHostAddresses()[0]);
            Log.i(str, sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("mDNS / FOUND NAME: ");
            sb4.append(name);
            Log.i(str, sb4.toString());
            if (name != null && TopLevel.isSensorTagDevice(name)) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("name", serviceEvent.getName());
                    jSONObject.put("host", serviceEvent.getInfo().getHostAddresses()[0]);
                    jSONObject.put("age", 0);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Publishing device found to application,  name: ");
                    sb5.append(serviceEvent.getName());
                    Log.i(str, sb5.toString());
                    this.callback.onDeviceResolved(jSONObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private InetAddress getDeviceIpAddress(WifiManager wifiManager) {
        try {
            InetAddress byName = InetAddress.getByName("10.0.0.2");
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            return InetAddress.getByAddress(new byte[]{(byte) (ipAddress & 255), (byte) ((ipAddress >> 8) & 255), (byte) ((ipAddress >> 16) & 255), (byte) ((ipAddress >> 24) & 255)});
        } catch (UnknownHostException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("getDeviceIpAddress Error: ");
            sb.append(e.getMessage());
            Log.e(TAG, sb.toString());
            return null;
        }
    }

    public void serviceTypeAdded(ServiceEvent serviceEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("serviceTypeAdded: ");
        sb.append(serviceEvent);
        Log.i(TAG, sb.toString());
        if (serviceEvent.getType().contains(SERVICE_TYPE)) {
            this.jmdns.addServiceListener(serviceEvent.getType(), this);
        }
    }

    public void subTypeForServiceTypeAdded(ServiceEvent serviceEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("subTypeForServiceTypeAdded: ");
        sb.append(serviceEvent);
        Log.i(TAG, sb.toString());
    }
}
