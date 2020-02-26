package com.p004ti.ble.p005ti.profiles;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.p004ti.util.GenericCharacteristicTableRow;

/* renamed from: com.ti.ble.ti.profiles.TIOADProfileTableRow */
public class TIOADProfileTableRow extends GenericCharacteristicTableRow {
    public static final String ACTION_VIEW_CLICKED = "TIOADProfileTableRow.ACTION_VIEW_CLICKED";

    public TIOADProfileTableRow(Context context) {
        super(context, 1000, true);
    }

    public void onClick(View view) {
        this.context.sendBroadcast(new Intent(ACTION_VIEW_CLICKED));
    }
}
