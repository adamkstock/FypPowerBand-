package com.p004ti.wifi;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;
import com.ti.ble.simplelinkstarter.R;
import java.util.List;

/* renamed from: com.ti.wifi.WiFiDeviceScannerDialogFragment */
public class WiFiDeviceScannerDialogFragment extends DialogFragment implements OnItemClickListener, OnClickListener {
    private static final String CONFIG_WIFI_DIALOG_CANCELED = "com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_CANCELED";
    public static final String CONFIG_WIFI_DIALOG_ENCRYPT = "com.ti.sensortag.ConfigureWiFiDialogFragment.ENCRYPT";
    public static final String CONFIG_WIFI_DIALOG_IPADDR = "com.ti.sensortag.ConfigureWiFiDialogFragment.IPADDR";
    public static final String CONFIG_WIFI_DIALOG_OK = "com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_OK";
    public static final String CONFIG_WIFI_DIALOG_PWD = "com.ti.sensortag.ConfigureWiFiDialogFragment.PWD";
    public static final String CONFIG_WIFI_DIALOG_SSID = "com.ti.sensortag.ConfigureWiFiDialogFragment.SSID";
    private Spinner encSpinner;
    public String ipAdr;
    private WiFiDeviceScannerDialogFragment mThis;
    public EditText pwdField;
    private List<ScanResult> records;
    String ssid;
    private List<String> ssidArray;
    public AutoCompleteTextView ssidField;

    /* renamed from: tL */
    private TableLayout f81tL;

    /* renamed from: v */
    private View f82v;

    public void onClick(DialogInterface dialogInterface, int i) {
    }

    public static WiFiDeviceScannerDialogFragment newInstance() {
        return new WiFiDeviceScannerDialogFragment();
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String str = "CANCEL";
        Builder negativeButton = new Builder(getActivity()).setTitle("WiFi Device Scanner Setup").setCancelable(false).setPositiveButton("SELECT", this.mThis).setNegativeButton(str, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent();
                intent.setAction("com.ti.sensortag.ConfigureWiFiDialogFragment.CONFIG_WIFI_DIALOG_CANCELED");
                WiFiDeviceScannerDialogFragment.this.getActivity().sendBroadcast(intent);
                Toast.makeText(WiFiDeviceScannerDialogFragment.this.getActivity(), "No values changed", 1);
            }
        });
        this.f82v = getActivity().getLayoutInflater().inflate(R.layout.wifi_device_scanner_dialog, null);
        negativeButton.setTitle("SensorTag WiFi devices found :");
        this.f81tL = (TableLayout) this.f82v.findViewById(R.id.wds_device_table);
        negativeButton.setView(this.f82v);
        return negativeButton.create();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        for (int i2 = 0; i2 < this.records.size(); i2++) {
            if (((String) this.ssidArray.get(i)).equals(((ScanResult) this.records.get(i2)).SSID) && ((ScanResult) this.records.get(i2)).frequency < 3000) {
                StringBuilder sb = new StringBuilder();
                sb.append("Capabilities : ");
                sb.append(((ScanResult) this.records.get(i2)).capabilities);
                Log.d("WiFiDialogFragment", sb.toString());
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
