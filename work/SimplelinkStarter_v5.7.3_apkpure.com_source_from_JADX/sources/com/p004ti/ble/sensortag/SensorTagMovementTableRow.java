package com.p004ti.ble.sensortag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GattInfo;
import com.p004ti.ble.common.GenericServiceConfigurationDialogFragment;
import com.p004ti.util.TrippleSparkLineView;
import com.ti.ble.simplelinkstarter.R;
import java.util.UUID;

/* renamed from: com.ti.ble.sensortag.SensorTagMovementTableRow */
public class SensorTagMovementTableRow extends TableRow implements OnClickListener, AnimationListener {
    public Switch WOS = ((Switch) findViewById(R.id.smdtr_wos));
    public final TextView accValue = ((TextView) findViewById(R.id.smdtr_value));
    public boolean config;
    public Context context;
    public final TextView gyroValue = ((TextView) findViewById(R.id.smdtr_value2));
    public final ImageView icon = ((ImageView) findViewById(R.id.smdtr_icon));
    public Paint linePaint;
    public final TextView magValue = ((TextView) findViewById(R.id.smdtr_value3));
    public int period;
    public boolean servOn;
    public final TrippleSparkLineView sl1 = ((TrippleSparkLineView) findViewById(R.id.smdtr_sparkline1));
    public final TrippleSparkLineView sl2 = ((TrippleSparkLineView) findViewById(R.id.smdtr_sparkline2));
    public final TrippleSparkLineView sl3 = ((TrippleSparkLineView) findViewById(R.id.smdtr_sparkline3));
    public TableRow tRow;
    public final TextView title = ((TextView) findViewById(R.id.smdtr_characteristic_title));
    public TextView uuidLabel;

    public void onAnimationEnd(Animation animation) {
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    public SensorTagMovementTableRow(Context context2) {
        super(context2);
        this.context = context2;
        addView(LayoutInflater.from(context2).inflate(R.layout.sensortag_motion_data_table_row, null, false));
        this.WOS.setVisibility(4);
        this.uuidLabel = new TextView(this.context);
        setBackgroundColor(0);
        this.linePaint = new Paint() {
            {
                setStrokeWidth(1.0f);
                setARGB(255, 0, 0, 0);
            }
        };
        getChildAt(0).setOnClickListener(this);
        this.period = 1000;
    }

    public void onClick(View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("Row ID ");
        sb.append(this.title.getText());
        Log.d("onClick", sb.toString());
        GenericServiceConfigurationDialogFragment.newInstance(this.uuidLabel.getText().toString(), this.period, this.servOn, 100).show(((SensorTagApplicationClass) this.context.getApplicationContext()).currentActivity.getFragmentManager(), "ServiceConfig");
    }

    public void setIcon(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Getting MipMap for :");
        sb.append(str);
        sb.append(GattInfo.uuidToIcon(UUID.fromString(str2)));
        String str3 = "GenericCharacteristicTableRow";
        Log.d(str3, sb.toString());
        Resources resources = getResources();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(GattInfo.uuidToIcon(UUID.fromString(str2)));
        int identifier = resources.getIdentifier(sb2.toString(), "mipmap", this.context.getPackageName());
        if (identifier != -1) {
            this.icon.setImageResource(identifier);
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Icon for : ");
        sb3.append(str);
        sb3.append(GattInfo.uuidToIcon(UUID.fromString(str2)));
        sb3.append(" Not found !");
        Log.d(str3, sb3.toString());
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

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
