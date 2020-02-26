package com.p004ti.ble.sensortag;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import com.p004ti.ble.common.GenericServiceConfigurationDialogFragment;
import com.p004ti.util.GenericCharacteristicTableRow;

/* renamed from: com.ti.ble.sensortag.SensorTagBarometerTableRow */
public class SensorTagBarometerTableRow extends GenericCharacteristicTableRow {
    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    public SensorTagBarometerTableRow(Context context) {
        super(context, 1000, true);
        getChildAt(0).setOnClickListener(this);
    }

    public void onClick(View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("Row ID ");
        sb.append(this.title.getText());
        Log.d("onClick", sb.toString());
        GenericServiceConfigurationDialogFragment.newInstance(this.uuidLabel.getText().toString(), this.period, this.servOn, this.periodMinVal).show(((SensorTagApplicationClass) this.context.getApplicationContext()).currentActivity.getFragmentManager(), "ServiceConfig");
    }

    public void onAnimationEnd(Animation animation) {
        boolean z = this.config;
    }

    public void calibrationButtonTouched() {
        Intent intent = new Intent(GenericCharacteristicTableRow.ACTION_CALIBRATE);
        intent.putExtra(GenericCharacteristicTableRow.EXTRA_SERVICE_UUID, this.uuidLabel.getText());
        this.context.sendBroadcast(intent);
    }

    public void grayedOut(boolean z) {
        super.grayedOut(z);
    }
}
