package com.p004ti.ble.common.oad;

import android.content.Context;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.common.oad.FWUpdateEOADNewFileSelectorStageOneTableRow */
public class FWUpdateEOADNewFileSelectorStageOneTableRow extends TableRow {
    TextView circuitDescription;
    TextView circuitName;
    FWUpdateEOADNewFileSelectorStageOneTableRow mThis = this;

    public FWUpdateEOADNewFileSelectorStageOneTableRow(Context context) {
        super(context);
        View inflate = inflate(context, R.layout.table_row_fw_ng_image_selector_cell, null);
        addView(inflate);
        this.circuitName = (TextView) inflate.findViewById(R.id.trfnis_circuit_name);
        this.circuitDescription = (TextView) inflate.findViewById(R.id.trfnis_circuit_description);
        setBackgroundColor(0);
    }

    public void setCircuitNameText(CharSequence charSequence) {
        this.circuitName.setText(charSequence);
    }

    public void setCircuitDescriptionText(CharSequence charSequence) {
        this.circuitDescription.setText(charSequence);
    }
}
