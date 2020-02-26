package com.p004ti.device_selector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.TableRow;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.BLEDeviceHeaderTableRow */
public class BLEDeviceHeaderTableRow extends TableRow {
    public Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 150, 150, 150);
        }
    };

    public BLEDeviceHeaderTableRow(Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.ble_device_header_table_row, null, false));
        setBackgroundColor(0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
