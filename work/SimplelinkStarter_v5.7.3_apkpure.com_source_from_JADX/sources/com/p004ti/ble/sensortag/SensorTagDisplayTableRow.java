package com.p004ti.ble.sensortag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.sensortag.SensorTagDisplayTableRow */
public class SensorTagDisplayTableRow extends TableRow {
    Context context;
    ImageView displayAccessory;
    CheckBox displayClock;
    ImageView displayIcon;
    CheckBox displayInvert;
    TextView displaySubTitle;
    EditText displayText;
    TextView displayTitle;
    public Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };

    SensorTagDisplayTableRow(Context context2) {
        super(context2);
        this.context = context2;
        View inflate = LayoutInflater.from(context2).inflate(R.layout.display_table_row, null, false);
        addView(inflate);
        this.displayTitle = (TextView) inflate.findViewById(R.id.dtr_characteristic_title);
        this.displaySubTitle = (TextView) inflate.findViewById(R.id.dtr_value);
        this.displayIcon = (ImageView) inflate.findViewById(R.id.dtr_icon);
        this.displayAccessory = (ImageView) inflate.findViewById(R.id.dtr_disclosure_indicator);
        this.displayText = (EditText) inflate.findViewById(R.id.dtr_display_text);
        this.displayInvert = (CheckBox) inflate.findViewById(R.id.dtr_invert_chkbox);
        this.displayClock = (CheckBox) inflate.findViewById(R.id.dtr_clock_chkbox);
        this.displayTitle.setText("Display control");
        this.displaySubTitle.setText("");
        this.displayIcon.setImageResource(R.mipmap.sensortag2_display_service);
        this.displayAccessory.setVisibility(4);
        this.displayText.setMaxLines(1);
        this.displayText.setInputType(1);
        this.displayText.setImeOptions(6);
        this.displayText.setFilters(new InputFilter[]{new LengthFilter(16)});
        setBackgroundColor(0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
