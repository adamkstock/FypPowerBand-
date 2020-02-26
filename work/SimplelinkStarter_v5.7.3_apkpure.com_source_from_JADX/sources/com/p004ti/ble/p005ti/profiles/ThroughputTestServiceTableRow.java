package com.p004ti.ble.p005ti.profiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.TableRow;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.ti.profiles.ThroughputTestServiceTableRow */
public class ThroughputTestServiceTableRow extends TableRow {
    Context context;
    public Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };

    ThroughputTestServiceTableRow(Context context2) {
        super(context2);
        this.context = context2;
        addView(LayoutInflater.from(context2).inflate(R.layout.throughput_test_table_row, null, false));
        setBackgroundColor(0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
