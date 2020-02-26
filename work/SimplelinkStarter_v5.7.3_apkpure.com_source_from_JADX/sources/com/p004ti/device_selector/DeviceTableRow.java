package com.p004ti.device_selector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.DeviceTableRow */
public class DeviceTableRow extends TableRow {
    public ImageView RSSIIcon;
    public TextView detailLabel;
    public ImageView icon;
    public Paint linePaint;
    public TextView titleLabel;

    public DeviceTableRow(Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.device_table_row, null, false));
        this.linePaint = new Paint() {
            {
                setStrokeWidth(1.0f);
                setARGB(255, 150, 150, 150);
            }
        };
        setBackgroundColor(0);
    }

    public DeviceTableRow(Context context, AttributeSet attributeSet) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.device_table_row, null, false));
        this.linePaint = new Paint() {
            {
                setStrokeWidth(1.0f);
                setARGB(255, 150, 150, 150);
            }
        };
        setBackgroundColor(0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
