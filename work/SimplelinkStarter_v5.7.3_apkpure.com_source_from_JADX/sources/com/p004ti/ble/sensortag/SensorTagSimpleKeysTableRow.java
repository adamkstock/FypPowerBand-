package com.p004ti.ble.sensortag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableRow;
import com.p004ti.util.TrippleSparkLineView;
import com.ti.ble.simplelinkstarter.R;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.ti.ble.sensortag.SensorTagSimpleKeysTableRow */
public class SensorTagSimpleKeysTableRow extends TableRow {
    protected TrippleSparkLineView buttonSL;
    protected ImageView icon;
    public byte lastKeys;
    protected ImageView leftKeyPressStateImage;
    public Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };
    protected ImageView reedStateImage;
    protected ImageView rightKeyPressStateImage;
    protected updateSparkLinesTimerTask sparkLineUpdateTask;
    protected Timer sparkLineUpdateTimer;

    /* renamed from: com.ti.ble.sensortag.SensorTagSimpleKeysTableRow$updateSparkLinesTimerTask */
    class updateSparkLinesTimerTask extends TimerTask {
        SensorTagSimpleKeysTableRow param;

        public updateSparkLinesTimerTask(SensorTagSimpleKeysTableRow sensorTagSimpleKeysTableRow) {
            this.param = sensorTagSimpleKeysTableRow;
        }

        public void run() {
            this.param.post(new Runnable() {
                public void run() {
                    if ((updateSparkLinesTimerTask.this.param.lastKeys & 1) == 1) {
                        updateSparkLinesTimerTask.this.param.buttonSL.addValue(1.0f, 0);
                    } else {
                        updateSparkLinesTimerTask.this.param.buttonSL.addValue(0.0f, 0);
                    }
                    if ((updateSparkLinesTimerTask.this.param.lastKeys & 2) == 2) {
                        updateSparkLinesTimerTask.this.param.buttonSL.addValue(1.0f, 1);
                    } else {
                        updateSparkLinesTimerTask.this.param.buttonSL.addValue(0.0f, 1);
                    }
                    if ((updateSparkLinesTimerTask.this.param.lastKeys & 4) == 4) {
                        updateSparkLinesTimerTask.this.param.buttonSL.addValue(1.0f, 2);
                    } else {
                        updateSparkLinesTimerTask.this.param.buttonSL.addValue(0.0f, 2);
                    }
                }
            });
        }
    }

    public SensorTagSimpleKeysTableRow(Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.simple_keys_table_row, null, false));
        setBackgroundColor(0);
        this.leftKeyPressStateImage = (ImageView) findViewById(R.id.sktr_left_button_image);
        this.rightKeyPressStateImage = (ImageView) findViewById(R.id.sktr_right_button_image);
        this.reedStateImage = (ImageView) findViewById(R.id.sktr_reed_image);
        this.buttonSL = (TrippleSparkLineView) findViewById(R.id.sktr_sparkline);
        this.icon = (ImageView) findViewById(R.id.sktr_icon);
        this.sparkLineUpdateTimer = new Timer();
        this.sparkLineUpdateTask = new updateSparkLinesTimerTask(this);
        this.sparkLineUpdateTimer.scheduleAtFixedRate(this.sparkLineUpdateTask, 1000, 100);
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
        int identifier = resources.getIdentifier(sb2.toString(), "mipmap", super.getContext().getPackageName());
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

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
