package com.p004ti.wifi;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.p004ti.device_selector.TopLevel;
import com.ti.ble.simplelinkstarter.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* renamed from: com.ti.wifi.ConfigureWiFiDialogFragment */
public class ConfigureWiFiDialogFragment extends DialogFragment implements OnItemClickListener, OnClickListener {
    public static final String CONFIG_WIFI_DIALOG_CANCELED = "com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_CANCELED";
    public static final String CONFIG_WIFI_DIALOG_ENCRYPT = "com.ti.sensortag.ConfigureWiFiDialogFragment.ENCRYPT";
    public static final String CONFIG_WIFI_DIALOG_IPADDR = "com.ti.sensortag.ConfigureWiFiDialogFragment.IPADDR";
    public static final String CONFIG_WIFI_DIALOG_OK = "com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_OK";
    public static final String CONFIG_WIFI_DIALOG_PWD = "com.ti.sensortag.ConfigureWiFiDialogFragment.PWD";
    public static final String CONFIG_WIFI_DIALOG_SSID = "com.ti.sensortag.ConfigureWiFiDialogFragment.SSID";
    private static final String TAG = "ConfigWifiDialogFrag";
    private Spinner encSpinner;
    private String ipAdr;
    private ConfigureWiFiDialogFragment mThis;
    private CheckBox pass_cb;
    /* access modifiers changed from: private */
    public EditText pwdField;
    private List<ScanResult> records;
    private String ssid;
    private List<String> ssidArray;
    private AutoCompleteTextView ssidField;

    /* renamed from: v */
    private View f79v;

    public static ConfigureWiFiDialogFragment newInstance(String str, String str2) {
        ConfigureWiFiDialogFragment configureWiFiDialogFragment = new ConfigureWiFiDialogFragment();
        configureWiFiDialogFragment.ipAdr = str;
        new Bundle();
        configureWiFiDialogFragment.mThis = configureWiFiDialogFragment;
        configureWiFiDialogFragment.ssid = str2;
        return configureWiFiDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String str = "SensorTag WiFi Setup";
        String str2 = "SAVE";
        String str3 = "CANCEL";
        Builder negativeButton = new Builder(getActivity()).setTitle(str).setPositiveButton(str2, this.mThis).setCancelable(false).setNegativeButton(str3, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent();
                intent.setAction(ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_CANCELED);
                ConfigureWiFiDialogFragment.this.getActivity().sendBroadcast(intent);
                Toast.makeText(ConfigureWiFiDialogFragment.this.getActivity(), "No values changed", 1);
            }
        });
        this.f79v = getActivity().getLayoutInflater().inflate(R.layout.wifi_configuration_dialog, null);
        negativeButton.setTitle(str);
        this.ssidField = (AutoCompleteTextView) this.f79v.findViewById(R.id.wcd_ssid);
        this.ssidField.setGravity(3);
        this.ssidField.setThreshold(1);
        this.encSpinner = (Spinner) this.f79v.findViewById(R.id.wcd_security_spinner);
        String str4 = TopLevel.initialConnectionSSID;
        String str5 = TAG;
        if (str4 != null && !TopLevel.initialConnectionSSID.equals("") && !TopLevel.initialConnectionSSID.contains(TopLevel.sensor_tag) && !TopLevel.initialConnectionSSID.contains(TopLevel.Sensor_Tag)) {
            StringBuilder sb = new StringBuilder();
            sb.append("INITIAL- dialog / in if setting text:\nInitialConnectionSSID: ");
            sb.append(TopLevel.initialConnectionSSID);
            sb.append("\nInitialConnectionSecurityType: ");
            sb.append(TopLevel.initialConnectionSecurityType);
            Log.i(str5, sb.toString());
            this.ssidField.setText(TopLevel.initialConnectionSSID);
            try {
                if (TopLevel.initialConnectionSecurityType.contains("WPA2")) {
                    this.encSpinner.setSelection(3);
                } else if (TopLevel.initialConnectionSecurityType.contains("WPA")) {
                    this.encSpinner.setSelection(2);
                } else if (TopLevel.initialConnectionSecurityType.contains("WEP")) {
                    this.encSpinner.setSelection(1);
                } else {
                    this.encSpinner.setSelection(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService("wifi");
        wifiManager.startScan();
        this.records = wifiManager.getScanResults();
        Collections.sort(this.records, new Comparator<ScanResult>() {
            public int compare(ScanResult scanResult, ScanResult scanResult2) {
                return scanResult.SSID.compareTo(scanResult2.SSID);
            }
        });
        Log.d(str5, "WiFi Scan results : ");
        this.ssidArray = new ArrayList();
        for (int i = 0; i < this.records.size(); i++) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Result ");
            sb2.append(i);
            sb2.append(" SSID : ");
            sb2.append(((ScanResult) this.records.get(i)).SSID);
            sb2.append(" Signal : ");
            sb2.append(((ScanResult) this.records.get(i)).level);
            sb2.append(" BSSID : ");
            sb2.append(((ScanResult) this.records.get(i)).BSSID);
            sb2.append(" Frequency : ");
            sb2.append(((ScanResult) this.records.get(i)).frequency);
            Log.d(str5, sb2.toString());
            if (((ScanResult) this.records.get(i)).frequency < 3000) {
                this.ssidArray.add(((ScanResult) this.records.get(i)).SSID);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Added ");
                sb3.append(((ScanResult) this.records.get(i)).SSID);
                sb3.append(" to list");
                Log.d(str5, sb3.toString());
            }
        }
        this.ssidField.setAdapter(new ArrayAdapter(getActivity(), 17367050, this.ssidArray));
        this.ssidField.setOnItemClickListener(this);
        this.pwdField = (EditText) this.f79v.findViewById(R.id.wcd_security_key);
        this.pwdField.setGravity(3);
        this.pass_cb = (CheckBox) this.f79v.findViewById(R.id.pass_check_box);
        this.pass_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    ConfigureWiFiDialogFragment.this.pwdField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ConfigureWiFiDialogFragment.this.pwdField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        negativeButton.setView(this.f79v);
        return negativeButton.create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        Intent intent = new Intent();
        intent.setAction("com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_OK");
        String obj = this.ssidField.getText().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("SSID :");
        sb.append(obj);
        Log.d("WiFiDialogFragment", sb.toString());
        intent.putExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.SSID", obj);
        intent.putExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.ENCRYPT", this.encSpinner.getSelectedItemPosition());
        intent.putExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.PWD", this.pwdField.getText().toString());
        intent.putExtra("com.ti.sensortag.ConfigureWiFiDialogFragment.IPADDR", this.ipAdr);
        getActivity().sendBroadcast(intent);
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.ssidField.getWindowToken(), 0);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        for (int i2 = 0; i2 < this.records.size(); i2++) {
            if (((String) this.ssidArray.get(i)).equals(((ScanResult) this.records.get(i2)).SSID) && ((ScanResult) this.records.get(i2)).frequency < 3000) {
                StringBuilder sb = new StringBuilder();
                sb.append("Capabilities : ");
                sb.append(((ScanResult) this.records.get(i2)).capabilities);
                Log.d(TAG, sb.toString());
                if (((ScanResult) this.records.get(i2)).capabilities.contains("WPA2")) {
                    this.encSpinner.setSelection(3);
                } else if (((ScanResult) this.records.get(i2)).capabilities.contains("WPA")) {
                    this.encSpinner.setSelection(2);
                } else if (((ScanResult) this.records.get(i2)).capabilities.contains("WEP")) {
                    this.encSpinner.setSelection(1);
                } else {
                    this.encSpinner.setSelection(0);
                }
            }
        }
    }
}
