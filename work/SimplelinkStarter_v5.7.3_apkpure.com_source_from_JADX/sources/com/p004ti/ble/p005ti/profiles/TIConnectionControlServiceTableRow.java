package com.p004ti.ble.p005ti.profiles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.p000v4.internal.view.SupportMenu;
import android.support.p000v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.ti.profiles.TIConnectionControlServiceTableRow */
public class TIConnectionControlServiceTableRow extends TableRow {
    Context context;
    protected ImageView icon = ((ImageView) findViewById(R.id.cctr_icon));
    public Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };

    TIConnectionControlServiceTableRow(Context context2) {
        super(context2);
        this.context = context2;
        addView(LayoutInflater.from(context2).inflate(R.layout.connection_control_table_row, null, false));
        setBackgroundColor(0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }

    public void setIcon(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append("Getting MipMap for :");
        sb.append(str);
        sb.append(str3);
        String str4 = "GenericCharacteristicTableRow";
        Log.d(str4, sb.toString());
        Resources resources = getResources();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(str3);
        int identifier = resources.getIdentifier(sb2.toString(), "mipmap", this.context.getPackageName());
        if (identifier != -1) {
            this.icon.setImageResource(identifier);
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Icon for : ");
        sb3.append(str);
        sb3.append(str3);
        sb3.append(" Not found !");
        Log.d(str4, sb3.toString());
    }

    public void invalidateConnectionParameters() {
        TextView textView = (TextView) findViewById(R.id.cctr_current_slave_latency);
        TextView textView2 = (TextView) findViewById(R.id.cctr_current_supervision_timeout);
        TextView textView3 = (TextView) findViewById(R.id.cctr_current_android_connection_priority);
        ((TextView) findViewById(R.id.cctr_current_connection_interval)).setTextColor(SupportMenu.CATEGORY_MASK);
        textView.setTextColor(SupportMenu.CATEGORY_MASK);
        textView2.setTextColor(SupportMenu.CATEGORY_MASK);
        textView3.setTextColor(SupportMenu.CATEGORY_MASK);
    }

    public void setConIntText(int i) {
        TextView textView = (TextView) findViewById(R.id.cctr_current_connection_interval);
        StringBuilder sb = new StringBuilder();
        sb.append(((float) i) * 1.25f);
        sb.append(" ms");
        textView.setText(sb.toString());
    }

    public void setConnectionParameters(int i, int i2, int i3, int i4) {
        TextView textView = (TextView) findViewById(R.id.cctr_current_connection_interval);
        TextView textView2 = (TextView) findViewById(R.id.cctr_current_slave_latency);
        TextView textView3 = (TextView) findViewById(R.id.cctr_current_supervision_timeout);
        TextView textView4 = (TextView) findViewById(R.id.cctr_current_android_connection_priority);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.cctr_high_priority);
        ToggleButton toggleButton2 = (ToggleButton) findViewById(R.id.cctr_balanced_priority);
        ToggleButton toggleButton3 = (ToggleButton) findViewById(R.id.cctr_low_pwr_priority);
        SeekBar seekBar = (SeekBar) findViewById(R.id.cctr_set_connection_interval_seekbar);
        textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        textView3.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        textView4.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        float f = ((float) i) * 1.25f;
        seekBar.setProgress((int) (f - 8.0f));
        StringBuilder sb = new StringBuilder();
        sb.append(f);
        String str = " ms";
        sb.append(str);
        textView.setText(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(i2);
        sb2.append(" ");
        textView2.setText(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(((float) i3) * 1.25f);
        sb3.append(str);
        textView3.setText(sb3.toString());
        String str2 = i4 == 0 ? "Balanced" : i4 == 1 ? "High" : i4 == 2 ? "Low Power" : "Unknown";
        textView4.setText(str2);
        if (i4 == 1) {
            toggleButton.setChecked(true);
            toggleButton2.setChecked(false);
            toggleButton3.setChecked(false);
        } else if (i4 == 2) {
            toggleButton.setChecked(false);
            toggleButton2.setChecked(false);
            toggleButton3.setChecked(true);
        } else {
            toggleButton.setChecked(false);
            toggleButton2.setChecked(true);
            toggleButton3.setChecked(false);
        }
    }
}
