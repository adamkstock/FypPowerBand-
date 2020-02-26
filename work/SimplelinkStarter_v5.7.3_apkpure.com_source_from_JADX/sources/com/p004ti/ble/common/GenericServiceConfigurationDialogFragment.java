package com.p004ti.ble.common;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.p004ti.util.GenericCharacteristicTableRow;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.common.GenericServiceConfigurationDialogFragment */
public class GenericServiceConfigurationDialogFragment extends DialogFragment implements OnSeekBarChangeListener {
    public static final String ACTION_GENERIC_SERVICE_CONFIGURATION_WAS_UPDATED = "GenericServiceConfigurationDialogFragment.UPDATE";
    public static final String VALUE_GENERIC_SERVICE_CONFIGURATION_PERIOD = "com.ti.ble.common.GenericServiceConfigurationDialog.PERIOD";
    public static final String VALUE_GENERIC_SERVICE_CONFIGURATION_SENSOR_STATE = "com.ti.ble.common.GenericServiceConfigurationDialog.SENSOR_STATE";
    /* access modifiers changed from: private */
    public String UUID;
    public SeekBar currentPeriodSeekBar;
    public TextView currentPeriodText;
    public Switch currentSensorState;
    public int lastPeriod;
    public boolean lastServiceOn;
    public int minPeriod;
    public TextView minimumPeriodText;
    /* access modifiers changed from: private */

    /* renamed from: v */
    public View f34v;

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public static GenericServiceConfigurationDialogFragment newInstance(String str, int i, boolean z, int i2) {
        GenericServiceConfigurationDialogFragment genericServiceConfigurationDialogFragment = new GenericServiceConfigurationDialogFragment();
        new Bundle();
        genericServiceConfigurationDialogFragment.UUID = str;
        genericServiceConfigurationDialogFragment.lastPeriod = i;
        genericServiceConfigurationDialogFragment.lastServiceOn = z;
        genericServiceConfigurationDialogFragment.minPeriod = i2;
        return genericServiceConfigurationDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String str = "Cancel";
        Builder negativeButton = new Builder(getActivity()).setTitle("Cloud configuration").setPositiveButton("Save", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView textView = (TextView) GenericServiceConfigurationDialogFragment.this.f34v.findViewById(R.id.gscd_current_period_text);
                Switch switchR = (Switch) GenericServiceConfigurationDialogFragment.this.f34v.findViewById(R.id.gscd_sensor_state_switch);
                Intent intent = new Intent(GenericServiceConfigurationDialogFragment.ACTION_GENERIC_SERVICE_CONFIGURATION_WAS_UPDATED);
                intent.putExtra(GenericCharacteristicTableRow.EXTRA_SERVICE_UUID, GenericServiceConfigurationDialogFragment.this.UUID);
                intent.putExtra(GenericServiceConfigurationDialogFragment.VALUE_GENERIC_SERVICE_CONFIGURATION_PERIOD, textView.getText().toString());
                intent.putExtra(GenericServiceConfigurationDialogFragment.VALUE_GENERIC_SERVICE_CONFIGURATION_SENSOR_STATE, switchR.isChecked());
                GenericServiceConfigurationDialogFragment.this.getActivity().sendBroadcast(intent);
            }
        }).setNegativeButton(str, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(GenericServiceConfigurationDialogFragment.this.getActivity(), "No values changed", 1);
            }
        });
        this.f34v = getActivity().getLayoutInflater().inflate(R.layout.generic_service_configuration_dialog, null);
        negativeButton.setTitle("Service configuration");
        negativeButton.setView(this.f34v);
        this.currentPeriodSeekBar = (SeekBar) this.f34v.findViewById(R.id.gscd_sensor_period_seekbar);
        this.currentPeriodSeekBar.setMax(2500);
        this.currentPeriodText = (TextView) this.f34v.findViewById(R.id.gscd_current_period_text);
        this.currentSensorState = (Switch) this.f34v.findViewById(R.id.gscd_sensor_state_switch);
        this.currentSensorState.setChecked(this.lastServiceOn);
        this.currentPeriodSeekBar.setProgress(this.lastPeriod);
        TextView textView = this.currentPeriodText;
        StringBuilder sb = new StringBuilder();
        sb.append(this.lastPeriod);
        String str2 = "ms";
        sb.append(str2);
        textView.setText(sb.toString());
        this.currentPeriodSeekBar.setOnSeekBarChangeListener(this);
        this.minimumPeriodText = (TextView) this.f34v.findViewById(R.id.gscd_sensor_minimum_period_text);
        TextView textView2 = this.minimumPeriodText;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.minPeriod);
        sb2.append(str2);
        textView2.setText(sb2.toString());
        return negativeButton.create();
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        this.currentPeriodText = (TextView) this.f34v.findViewById(R.id.gscd_current_period_text);
        int i2 = this.minPeriod;
        if (i >= i2) {
            i2 = i;
        }
        int i3 = i2 % 10;
        if (i3 != 0) {
            i2 -= i3;
        }
        TextView textView = this.currentPeriodText;
        StringBuilder sb = new StringBuilder();
        sb.append(i2);
        sb.append("ms");
        textView.setText(sb.toString());
    }
}
