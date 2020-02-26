package com.p004ti.util;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.p004ti.ble.common.GattInfo;
import com.p004ti.ble.common.GenericServiceConfigurationDialogFragment;
import com.p004ti.ble.sensortag.SensorTagApplicationClass;
import com.ti.ble.simplelinkstarter.R;
import java.util.UUID;

/* renamed from: com.ti.util.GenericCharacteristicTableRow */
public class GenericCharacteristicTableRow extends TableRow implements OnClickListener, AnimationListener {
    public static final String ACTION_CALIBRATE = "com.ti.util.ACTION_CALIBRATE";
    public static final String ACTION_ONOFF_UPDATE = "com.ti.util.ACTION_ONOFF_UPDATE";
    public static final String ACTION_PERIOD_UPDATE = "com.ti.util.ACTION_PERIOD_UPDATE";
    public static final String EXTRA_ONOFF = "com.ti.util.EXTRA_ONOFF";
    public static final String EXTRA_PERIOD = "com.ti.util.EXTRA_PERIOD";
    public static final String EXTRA_SERVICE_UUID = "com.ti.util.EXTRA_SERVICE_UUID";
    public boolean config;
    protected final Context context;
    public final ImageView icon;
    public int iconSize = 100;
    private final Paint linePaint;
    public int period;
    public int periodMinVal;
    protected final RelativeLayout rowLayout;
    public boolean servOn;
    public final TrippleSparkLineView sl1;
    public final TextView title;
    public final TextView uuidLabel;
    public final TextView value;

    public static boolean isCorrectService(String str) {
        return true;
    }

    public void onAnimationEnd(Animation animation) {
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public GenericCharacteristicTableRow(Context context2, int i, boolean z) {
        super(context2);
        this.context = context2;
        this.config = false;
        setLayoutParams(new LayoutParams(1));
        setBackgroundColor(0);
        this.periodMinVal = 100;
        this.period = i;
        this.servOn = z;
        new GattInfo(getResources().getXml(R.xml.gatt_uuid));
        this.rowLayout = new RelativeLayout(this.context);
        this.linePaint = new Paint() {
            {
                setStrokeWidth(1.0f);
                setARGB(255, 150, 150, 150);
            }
        };
        addView(LayoutInflater.from(this.context).inflate(R.layout.generic_characteristic_table_row, null, false));
        this.icon = (ImageView) findViewById(R.id.gctr_icon);
        this.title = (TextView) findViewById(R.id.gctr_characteristic_title);
        this.value = (TextView) findViewById(R.id.gctr_value);
        this.sl1 = (TrippleSparkLineView) findViewById(R.id.gctr_sparkline1);
        getChildAt(0).setOnClickListener(this);
        this.uuidLabel = new TextView(context2) {
            {
                setTextSize(3, 8.0f);
                setId(R.id.gctr_uuid_field);
                setVisibility(4);
            }
        };
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
        sb2.append(GattInfo.uuidToIconLong(UUID.fromString(str2)));
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

    public void onClick(View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("Row ID ");
        sb.append(this.title.getText());
        Log.d("onClick", sb.toString());
        GenericServiceConfigurationDialogFragment.newInstance(this.uuidLabel.getText().toString(), this.period, this.servOn, this.periodMinVal).show(((SensorTagApplicationClass) this.context.getApplicationContext()).currentActivity.getFragmentManager(), "ServiceConfig");
    }

    public void grayedOut(boolean z) {
        if (z) {
            this.value.setAlpha(0.4f);
            this.title.setAlpha(0.4f);
            this.icon.setAlpha(0.4f);
            this.sl1.setAlpha(0.4f);
            return;
        }
        this.value.setAlpha(1.0f);
        this.title.setAlpha(1.0f);
        this.icon.setAlpha(1.0f);
        this.sl1.setAlpha(1.0f);
    }
}
