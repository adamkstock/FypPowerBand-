package com.p004ti.ble.dmm.ZigBee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.dmm.ZigBee.TIDMMZigBeeLightSwitchTableRow */
public class TIDMMZigBeeLightSwitchTableRow extends TableRow {
    public ImageView icon = ((ImageView) findViewById(R.id.tr_dmm_zigbee_switch_icon));
    public Switch lightOnOff = ((Switch) findViewById(R.id.tr_dmm_zigbee_light_switch));
    private Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };
    public TextView switchBatteryLevelLabel = ((TextView) findViewById(R.id.tr_dmm_zigbee_switch_battery_level));
    public TextView targetAddressLabel = ((TextView) findViewById(R.id.tr_dmm_zigbee_target_address));
    public TextView targetAddressTypeLabel = ((TextView) findViewById(R.id.tr_dmm_zigbee_target_address_type_label));
    public TextView targetEndpoint = ((TextView) findViewById(R.id.tr_dmm_zigbee_target_endpoint));
    public TextView title = ((TextView) findViewById(R.id.tr_dmm_zigbee_switch_characteristic_title));

    public TIDMMZigBeeLightSwitchTableRow(Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.table_row_dmm_zigbee_light_switch, null, false));
        setBackgroundColor(0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
