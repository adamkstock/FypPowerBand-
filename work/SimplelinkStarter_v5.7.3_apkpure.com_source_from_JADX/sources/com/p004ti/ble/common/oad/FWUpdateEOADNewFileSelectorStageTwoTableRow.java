package com.p004ti.ble.common.oad;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.common.oad.FWUpdateEOADNewFileSelectorStageTwoTableRow */
public class FWUpdateEOADNewFileSelectorStageTwoTableRow extends TableRow {
    TextView imageDescription;
    TextView imageTitle;
    ImageView imageWirelessSTDIcon;
    FWUpdateEOADNewFileSelectorStageTwoTableRow mThis = this;

    public FWUpdateEOADNewFileSelectorStageTwoTableRow(Context context) {
        super(context);
        View inflate = inflate(context, R.layout.table_row_fw_ng_image_selector_stage_two, null);
        this.imageTitle = (TextView) inflate.findViewById(R.id.tr_fw_ng_img_sel_two_image_name);
        this.imageDescription = (TextView) inflate.findViewById(R.id.tr_fw_ng_img_sel_two_image_description);
        this.imageWirelessSTDIcon = (ImageView) inflate.findViewById(R.id.tr_fw_ng_img_sel_two_wireless_std_icon);
        addView(inflate);
        setBackgroundColor(0);
    }

    public void setImageTitle(CharSequence charSequence) {
        this.imageTitle.setText(charSequence);
    }

    public void setImageDescription(CharSequence charSequence) {
        this.imageDescription.setText(charSequence);
    }

    public void setImageWirelessSTDIcon(int i) {
        this.imageWirelessSTDIcon.setImageResource(i);
    }
}
