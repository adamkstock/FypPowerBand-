package com.p004ti.ble.common.cloud;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.ti.ble.simplelinkstarter.R;
import java.util.Map.Entry;

/* renamed from: com.ti.ble.common.cloud.CloudProfileConfigurationDialogFragment */
public class CloudProfileConfigurationDialogFragment extends DialogFragment implements OnItemSelectedListener {
    public static final String ACTION_CLOUD_CONFIG_WAS_UPDATED = "CloudProfileConfigurationDialogFragment.UPDATE";
    public static final Integer DEF_CLOUD_CUSTOM_CLOUD_SERVICE = Integer.valueOf(3);
    public static final Integer DEF_CLOUD_DWEET_IO_SERVICE = Integer.valueOf(2);
    public static final Integer DEF_CLOUD_IBMIOTFOUNDATION_CLOUD_SERVICE = Integer.valueOf(1);
    public static final String DEF_CLOUD_IBMQUICKSTART_BROKER_ADDR = "tcp://quickstart.messaging.internetofthings.ibmcloud.com";
    public static final String DEF_CLOUD_IBMQUICKSTART_BROKER_PORT = "1883";
    public static final Boolean DEF_CLOUD_IBMQUICKSTART_CLEAN_SESSION = Boolean.valueOf(true);
    public static final Integer DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE = Integer.valueOf(0);
    public static final String DEF_CLOUD_IBMQUICKSTART_DEVICEID_PREFIX = "d:quickstart:st-app:";
    public static final String DEF_CLOUD_IBMQUICKSTART_PASSWORD = "";
    public static final String DEF_CLOUD_IBMQUICKSTART_PUBLISH_TOPIC = "iot-2/evt/status/fmt/json";
    public static final String DEF_CLOUD_IBMQUICKSTART_USERNAME = "";
    public static final Boolean DEF_CLOUD_IBMQUICKSTART_USE_SSL = Boolean.valueOf(false);
    public static final String PREF_CLOUD_BROKER_ADDR = "cloud_broker_address";
    public static final String PREF_CLOUD_BROKER_PORT = "cloud_broker_port";
    public static final String PREF_CLOUD_CLEAN_SESSION = "cloud_clean_session";
    public static final String PREF_CLOUD_DEVICE_ID = "cloud_device_id";
    public static final String PREF_CLOUD_PASSWORD = "cloud_password";
    public static final String PREF_CLOUD_PUBLISH_TOPIC = "cloud_publish_topic";
    public static final String PREF_CLOUD_SERVICE = "cloud_service";
    public static final String PREF_CLOUD_USERNAME = "cloud_username";
    public static final String PREF_CLOUD_USE_SSL = "cloud_use_ssl";
    private String deviceId = "";
    /* access modifiers changed from: private */
    public TextView dwDeviceIdLAbel;
    /* access modifiers changed from: private */
    public TextView dwKeyLabel;
    /* access modifiers changed from: private */
    public TextView dwLockLabel;
    SharedPreferences prefs = null;
    /* access modifiers changed from: private */

    /* renamed from: v */
    public View f36v;

    public static CloudProfileConfigurationDialogFragment newInstance(String str) {
        CloudProfileConfigurationDialogFragment cloudProfileConfigurationDialogFragment = new CloudProfileConfigurationDialogFragment();
        cloudProfileConfigurationDialogFragment.deviceId = str;
        new Bundle();
        return cloudProfileConfigurationDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String str = "Cancel";
        Builder negativeButton = new Builder(getActivity()).setTitle("Cloud configuration").setPositiveButton("Save", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Integer valueOf = Integer.valueOf(((Spinner) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_spinner)).getSelectedItemPosition());
                CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_SERVICE, valueOf.toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
                CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_USERNAME, ((EditText) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_username)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
                CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_PASSWORD, ((EditText) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_password)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
                Integer num = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE;
                String str = CloudProfileConfigurationDialogFragment.PREF_CLOUD_DEVICE_ID;
                if (valueOf == num) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_DEVICEID_PREFIX);
                    sb.append(((EditText) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_deviceid)).getText().toString());
                    CloudProfileConfigurationDialogFragment.setCloudPref(str, sb.toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
                } else {
                    CloudProfileConfigurationDialogFragment.setCloudPref(str, ((EditText) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_deviceid)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
                }
                CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_BROKER_ADDR, ((EditText) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_broker_address)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
                CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_BROKER_PORT, ((EditText) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_broker_port)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
                CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_PUBLISH_TOPIC, ((EditText) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_publish_topic)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
                String str2 = "true";
                String str3 = "false";
                CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_CLEAN_SESSION, ((CheckBox) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_clean_session_checkbox)).isChecked() ? str2 : str3, CloudProfileConfigurationDialogFragment.this.getActivity());
                if (!((CheckBox) CloudProfileConfigurationDialogFragment.this.f36v.findViewById(R.id.cloud_use_ssl_checkbox)).isChecked()) {
                    str2 = str3;
                }
                CloudProfileConfigurationDialogFragment.setCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_USE_SSL, str2, CloudProfileConfigurationDialogFragment.this.getActivity());
                CloudProfileConfigurationDialogFragment cloudProfileConfigurationDialogFragment = CloudProfileConfigurationDialogFragment.this;
                cloudProfileConfigurationDialogFragment.prefs = PreferenceManager.getDefaultSharedPreferences(cloudProfileConfigurationDialogFragment.getActivity());
                for (Entry entry : CloudProfileConfigurationDialogFragment.this.prefs.getAll().entrySet()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append((String) entry.getKey());
                    sb2.append(":");
                    sb2.append(entry.getValue().toString());
                    Log.d("CloudProfileConfigurationDialogFragment", sb2.toString());
                }
                CloudProfileConfigurationDialogFragment.this.getActivity().sendBroadcast(new Intent(CloudProfileConfigurationDialogFragment.ACTION_CLOUD_CONFIG_WAS_UPDATED));
            }
        }).setNegativeButton(str, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(CloudProfileConfigurationDialogFragment.this.getActivity(), "No values changed", 1);
            }
        });
        this.f36v = getActivity().getLayoutInflater().inflate(R.layout.cloud_config_dialog, null);
        negativeButton.setTitle("Cloud Setup");
        Spinner spinner = (Spinner) this.f36v.findViewById(R.id.cloud_spinner);
        ArrayAdapter createFromResource = ArrayAdapter.createFromResource(getActivity(), R.array.cloud_config_dialog_cloud_services_array, 17367048);
        createFromResource.setDropDownViewResource(17367049);
        spinner.setAdapter(createFromResource);
        createFromResource.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(this);
        try {
            spinner.setSelection(Integer.valueOf(Integer.parseInt(retrieveCloudPref(PREF_CLOUD_SERVICE, getActivity()), 10)).intValue());
        } catch (Exception unused) {
        }
        negativeButton.setView(this.f36v);
        return negativeButton.create();
    }

    public void enDisUsername(boolean z, String str) {
        TextView textView = (TextView) this.f36v.findViewById(R.id.cloud_username_label);
        EditText editText = (EditText) this.f36v.findViewById(R.id.cloud_username);
        editText.setEnabled(z);
        editText.setText(str);
        if (z) {
            textView.setAlpha(1.0f);
            editText.setAlpha(1.0f);
            return;
        }
        textView.setAlpha(0.4f);
        editText.setAlpha(0.4f);
    }

    public void enDisUsername(boolean z, String str, String str2) {
        TextView textView = (TextView) this.f36v.findViewById(R.id.cloud_username_label);
        EditText editText = (EditText) this.f36v.findViewById(R.id.cloud_username);
        editText.setEnabled(z);
        editText.setText(str);
        editText.setHint(str2);
        if (z) {
            textView.setAlpha(1.0f);
            editText.setAlpha(1.0f);
            return;
        }
        textView.setAlpha(0.4f);
        editText.setAlpha(0.4f);
    }

    public void enDisPassword(boolean z, String str) {
        TextView textView = (TextView) this.f36v.findViewById(R.id.cloud_password_label);
        EditText editText = (EditText) this.f36v.findViewById(R.id.cloud_password);
        editText.setEnabled(z);
        editText.setText(str);
        if (z) {
            textView.setAlpha(1.0f);
            editText.setAlpha(1.0f);
            return;
        }
        textView.setAlpha(0.4f);
        editText.setAlpha(0.4f);
    }

    public void enDisPassword(boolean z, String str, String str2) {
        TextView textView = (TextView) this.f36v.findViewById(R.id.cloud_password_label);
        EditText editText = (EditText) this.f36v.findViewById(R.id.cloud_password);
        editText.setEnabled(z);
        editText.setText(str);
        editText.setHint(str2);
        if (z) {
            textView.setAlpha(1.0f);
            editText.setAlpha(1.0f);
            return;
        }
        textView.setAlpha(0.4f);
        editText.setAlpha(0.4f);
    }

    public void setDeviceId(String str) {
        ((EditText) this.f36v.findViewById(R.id.cloud_deviceid)).setText(str);
    }

    public void enDisBrokerAddressPort(boolean z, String str, String str2) {
        TextView textView = (TextView) this.f36v.findViewById(R.id.cloud_broker_address_label);
        EditText editText = (EditText) this.f36v.findViewById(R.id.cloud_broker_address);
        TextView textView2 = (TextView) this.f36v.findViewById(R.id.cloud_broker_port_label);
        EditText editText2 = (EditText) this.f36v.findViewById(R.id.cloud_broker_port);
        editText.setEnabled(z);
        editText2.setEnabled(z);
        editText.setText(str);
        editText2.setText(str2);
        if (z) {
            textView.setAlpha(1.0f);
            editText.setAlpha(1.0f);
            textView2.setAlpha(1.0f);
            editText2.setAlpha(1.0f);
            return;
        }
        textView.setAlpha(0.4f);
        textView2.setAlpha(0.4f);
        editText.setAlpha(0.4f);
        editText2.setAlpha(0.4f);
    }

    public void enDisTopic(boolean z, String str) {
        TextView textView = (TextView) this.f36v.findViewById(R.id.cloud_publish_topic_label);
        EditText editText = (EditText) this.f36v.findViewById(R.id.cloud_publish_topic);
        editText.setEnabled(z);
        editText.setText(str);
        if (z) {
            textView.setAlpha(1.0f);
            editText.setAlpha(1.0f);
            return;
        }
        textView.setAlpha(0.4f);
        editText.setAlpha(0.4f);
    }

    public void enDisCleanSession(boolean z, boolean z2) {
        CheckBox checkBox = (CheckBox) this.f36v.findViewById(R.id.cloud_clean_session_checkbox);
        checkBox.setEnabled(z);
        checkBox.setChecked(z2);
    }

    public void enDisUseSSL(boolean z, boolean z2) {
        CheckBox checkBox = (CheckBox) this.f36v.findViewById(R.id.cloud_use_ssl_checkbox);
        checkBox.setEnabled(z);
        checkBox.setChecked(z2);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        int i2 = i;
        StringBuilder sb = new StringBuilder();
        sb.append("onItemSelected :");
        sb.append(i2);
        Log.d("CloudProfileConfigurationDialogFragment", sb.toString());
        String str = "Password :";
        String str2 = "Username :";
        String str3 = "Device ID :";
        String str4 = "";
        if (i2 != 0) {
            String str5 = PREF_CLOUD_PUBLISH_TOPIC;
            String str6 = PREF_CLOUD_BROKER_PORT;
            String str7 = PREF_CLOUD_BROKER_ADDR;
            String str8 = PREF_CLOUD_DEVICE_ID;
            String str9 = PREF_CLOUD_PASSWORD;
            String str10 = PREF_CLOUD_USERNAME;
            if (i2 == 1) {
                this.dwDeviceIdLAbel = (TextView) this.f36v.findViewById(R.id.cloud_deviceid_label);
                this.dwDeviceIdLAbel.setText(str3);
                this.dwKeyLabel = (TextView) this.f36v.findViewById(R.id.cloud_username_label);
                this.dwKeyLabel.setText(str2);
                this.dwLockLabel = (TextView) this.f36v.findViewById(R.id.cloud_password_label);
                this.dwLockLabel.setText(str);
                enDisUsername(true, retrieveCloudPref(str10, getActivity()), str4);
                enDisPassword(true, retrieveCloudPref(str9, getActivity()), str4);
                setDeviceId(retrieveCloudPref(str8, getActivity()));
                enDisBrokerAddressPort(true, retrieveCloudPref(str7, getActivity()), retrieveCloudPref(str6, getActivity()));
                enDisTopic(true, retrieveCloudPref(str5, getActivity()));
                try {
                    z = Boolean.parseBoolean(retrieveCloudPref(PREF_CLOUD_CLEAN_SESSION, getActivity()));
                    z2 = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    z2 = true;
                    z = false;
                }
                enDisCleanSession(z2, z);
                try {
                    z3 = Boolean.parseBoolean(retrieveCloudPref(PREF_CLOUD_USE_SSL, getActivity()));
                    z4 = true;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    z4 = true;
                    z3 = false;
                }
                enDisUseSSL(z4, z3);
            } else if (i2 == 2) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CloudProfileConfigurationDialogFragment cloudProfileConfigurationDialogFragment = CloudProfileConfigurationDialogFragment.this;
                        cloudProfileConfigurationDialogFragment.dwDeviceIdLAbel = (TextView) cloudProfileConfigurationDialogFragment.f36v.findViewById(R.id.cloud_deviceid_label);
                        CloudProfileConfigurationDialogFragment.this.dwDeviceIdLAbel.setText("Thing :");
                        CloudProfileConfigurationDialogFragment cloudProfileConfigurationDialogFragment2 = CloudProfileConfigurationDialogFragment.this;
                        cloudProfileConfigurationDialogFragment2.dwKeyLabel = (TextView) cloudProfileConfigurationDialogFragment2.f36v.findViewById(R.id.cloud_username_label);
                        CloudProfileConfigurationDialogFragment.this.dwKeyLabel.setText("Key");
                        CloudProfileConfigurationDialogFragment cloudProfileConfigurationDialogFragment3 = CloudProfileConfigurationDialogFragment.this;
                        cloudProfileConfigurationDialogFragment3.dwLockLabel = (TextView) cloudProfileConfigurationDialogFragment3.f36v.findViewById(R.id.cloud_password_label);
                        CloudProfileConfigurationDialogFragment.this.dwLockLabel.setText("Lock");
                        CloudProfileConfigurationDialogFragment cloudProfileConfigurationDialogFragment4 = CloudProfileConfigurationDialogFragment.this;
                        cloudProfileConfigurationDialogFragment4.enDisUsername(true, CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_USERNAME, cloudProfileConfigurationDialogFragment4.getActivity()), "Enter Key here, or none for non-locked mode");
                        CloudProfileConfigurationDialogFragment cloudProfileConfigurationDialogFragment5 = CloudProfileConfigurationDialogFragment.this;
                        cloudProfileConfigurationDialogFragment5.enDisPassword(true, CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_PASSWORD, cloudProfileConfigurationDialogFragment5.getActivity()), "Enter Lock here, or none for non-locked mode");
                        CloudProfileConfigurationDialogFragment cloudProfileConfigurationDialogFragment6 = CloudProfileConfigurationDialogFragment.this;
                        cloudProfileConfigurationDialogFragment6.setDeviceId(CloudProfileConfigurationDialogFragment.retrieveCloudPref(CloudProfileConfigurationDialogFragment.PREF_CLOUD_DEVICE_ID, cloudProfileConfigurationDialogFragment6.getActivity()));
                        CloudProfileConfigurationDialogFragment.this.enDisUseSSL(false, false);
                        CloudProfileConfigurationDialogFragment.this.enDisCleanSession(false, false);
                        CloudProfileConfigurationDialogFragment.this.enDisBrokerAddressPort(false, "https://dweet.io:443/dweet/for", "0");
                        CloudProfileConfigurationDialogFragment.this.enDisTopic(false, "");
                    }
                });
            } else if (i2 != 3) {
                this.dwDeviceIdLAbel = (TextView) this.f36v.findViewById(R.id.cloud_deviceid_label);
                this.dwDeviceIdLAbel.setText(str3);
                this.dwKeyLabel = (TextView) this.f36v.findViewById(R.id.cloud_username_label);
                this.dwKeyLabel.setText(str2);
                this.dwLockLabel = (TextView) this.f36v.findViewById(R.id.cloud_password_label);
                this.dwLockLabel.setText(str);
                enDisUsername(true, retrieveCloudPref(str10, getActivity()), str4);
                enDisPassword(true, retrieveCloudPref(str9, getActivity()), str4);
                setDeviceId(retrieveCloudPref(str8, getActivity()));
                enDisBrokerAddressPort(true, retrieveCloudPref(str7, getActivity()), retrieveCloudPref(str6, getActivity()));
                enDisTopic(true, retrieveCloudPref(str5, getActivity()));
                try {
                    z9 = Boolean.parseBoolean(retrieveCloudPref(PREF_CLOUD_CLEAN_SESSION, getActivity()));
                    z10 = true;
                } catch (Exception e3) {
                    e3.printStackTrace();
                    z10 = true;
                    z9 = false;
                }
                enDisCleanSession(z10, z9);
                try {
                    z11 = Boolean.parseBoolean(retrieveCloudPref(PREF_CLOUD_USE_SSL, getActivity()));
                    z12 = true;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    z12 = true;
                    z11 = false;
                }
                enDisUseSSL(z12, z11);
            } else {
                this.dwDeviceIdLAbel = (TextView) this.f36v.findViewById(R.id.cloud_deviceid_label);
                this.dwDeviceIdLAbel.setText("DeviceID :");
                this.dwKeyLabel = (TextView) this.f36v.findViewById(R.id.cloud_username_label);
                this.dwKeyLabel.setText("Username:");
                this.dwLockLabel = (TextView) this.f36v.findViewById(R.id.cloud_password_label);
                this.dwLockLabel.setText("Password:");
                enDisUsername(true, retrieveCloudPref(str10, getActivity()), str4);
                enDisPassword(true, retrieveCloudPref(str9, getActivity()), str4);
                setDeviceId(retrieveCloudPref(str8, getActivity()));
                enDisBrokerAddressPort(true, retrieveCloudPref(str7, getActivity()), retrieveCloudPref(str6, getActivity()));
                enDisTopic(true, retrieveCloudPref(str5, getActivity()));
                try {
                    z5 = Boolean.parseBoolean(retrieveCloudPref(PREF_CLOUD_CLEAN_SESSION, getActivity()));
                    z6 = true;
                } catch (Exception e5) {
                    e5.printStackTrace();
                    z6 = true;
                    z5 = false;
                }
                enDisCleanSession(z6, z5);
                try {
                    z7 = Boolean.parseBoolean(retrieveCloudPref(PREF_CLOUD_USE_SSL, getActivity()));
                    z8 = true;
                } catch (Exception e6) {
                    e6.printStackTrace();
                    z8 = true;
                    z7 = false;
                }
                enDisUseSSL(z8, z7);
            }
        } else {
            this.dwDeviceIdLAbel = (TextView) this.f36v.findViewById(R.id.cloud_deviceid_label);
            this.dwDeviceIdLAbel.setText(str3);
            this.dwKeyLabel = (TextView) this.f36v.findViewById(R.id.cloud_username_label);
            this.dwKeyLabel.setText(str2);
            this.dwLockLabel = (TextView) this.f36v.findViewById(R.id.cloud_password_label);
            this.dwLockLabel.setText(str);
            enDisUsername(false, str4, str4);
            enDisPassword(false, str4, str4);
            setDeviceId(this.deviceId);
            enDisBrokerAddressPort(false, DEF_CLOUD_IBMQUICKSTART_BROKER_ADDR, DEF_CLOUD_IBMQUICKSTART_BROKER_PORT);
            enDisTopic(false, DEF_CLOUD_IBMQUICKSTART_PUBLISH_TOPIC);
            enDisCleanSession(false, DEF_CLOUD_IBMQUICKSTART_CLEAN_SESSION.booleanValue());
            enDisUseSSL(false, DEF_CLOUD_IBMQUICKSTART_USE_SSL.booleanValue());
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        StringBuilder sb = new StringBuilder();
        sb.append("onNothingSelected");
        sb.append(adapterView);
        Log.d("CloudProfileConfigurationDialogFragment", sb.toString());
    }

    public static String retrieveCloudPref(String str, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("pref_cloud_config_");
        sb.append(str);
        return PreferenceManager.getDefaultSharedPreferences(context).getString(sb.toString(), "NS");
    }

    public static boolean setCloudPref(String str, String str2, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("pref_cloud_config_");
        sb.append(str);
        String sb2 = sb.toString();
        Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(sb2, str2);
        return edit.commit();
    }
}
