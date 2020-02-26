package com.p004ti.ble.dmm.FifteenFour;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.dmm.FifteenFour.TIDMMFifteenFourRemoteDisplayTableRow */
public class TIDMMFifteenFourRemoteDisplayTableRow extends TableRow {
    public ImageView icon = ((ImageView) findViewById(R.id.tr_dmm_fifteen_four_switch_icon));
    private Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };
    public TextView nodeDataLabel = ((TextView) findViewById(R.id.tr_dmm_fifteen_four_last_node_data_label));
    public TextView nodeReportingIntervalLabel = ((TextView) findViewById(R.id.tr_dmm_fifteen_four_node_reporting_interval_label));
    public TextView title = ((TextView) findViewById(R.id.tr_dmm_fifteen_four_switch_characteristic_title));
    public Button toggleCollectorLEDButton = ((Button) findViewById(R.id.tr_dmm_fifteen_four_toggle_coll_led_button));

    public TIDMMFifteenFourRemoteDisplayTableRow(Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.table_row_dmm_fifteen_four_remote_display, null, false));
        setBackgroundColor(0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
