package com.p004ti.ble.common.oad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.p000v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.common.oad.FWUpdateBINFileEntryTableRow */
public class FWUpdateBINFileEntryTableRow extends TableRow {
    Context context;
    ImageView download;
    FWUpdateTIFirmwareEntry ent;
    private final Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 60, 60, 60);
        }
    };
    public int position;
    TextView subTitleView;

    /* renamed from: tV */
    TextView f38tV = ((TextView) findViewById(R.id.fetr_board_name_and_version));

    public FWUpdateBINFileEntryTableRow(Context context2, FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry) {
        super(context2);
        this.context = context2;
        addView(LayoutInflater.from(context2).inflate(R.layout.firmware_entry_table_row, null, false));
        TextView textView = this.f38tV;
        if (textView != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(fWUpdateTIFirmwareEntry.DevPack);
            sb.append(" ");
            textView.setText(String.format("%s %1.2f %s(%s)", new Object[]{fWUpdateTIFirmwareEntry.BoardType, Float.valueOf(fWUpdateTIFirmwareEntry.Version), sb.toString(), fWUpdateTIFirmwareEntry.WirelessStandard}));
        }
        this.subTitleView = (TextView) findViewById(R.id.fetr_short_description);
        if (this.subTitleView == null) {
            return;
        }
        if (fWUpdateTIFirmwareEntry.compatible) {
            this.subTitleView.setText(String.format("%s", new Object[]{"Compatible"}));
            return;
        }
        this.subTitleView.setText("Not compatible");
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }

    public void setGrayedOut(Boolean bool) {
        if (bool.booleanValue()) {
            this.f38tV.setTextColor(-3355444);
            this.subTitleView.setTextColor(-3355444);
            return;
        }
        this.f38tV.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.subTitleView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
    }
}
