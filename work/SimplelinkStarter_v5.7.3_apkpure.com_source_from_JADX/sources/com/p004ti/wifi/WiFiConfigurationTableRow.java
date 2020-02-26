package com.p004ti.wifi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import com.p004ti.device_selector.TopLevel;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.wifi.WiFiConfigurationTableRow */
public class WiFiConfigurationTableRow extends TableRow implements OnClickListener {
    private static final String TAG = "WiFiConfigurationTR";
    private String lastSSID;
    private Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };
    private Intent showWifiIntent;
    private TopLevel topLevel;

    public WiFiConfigurationTableRow(Context context) {
        super(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.wifi_configuration_table_row, null, false);
        this.topLevel = (TopLevel) context;
        addView(inflate);
        setBackgroundColor(0);
        getChildAt(0).setOnClickListener(this);
        this.lastSSID = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getSSID();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }

    public void onClick(View view) {
        Log.d(TAG, "Configure new WiFi device clicked !");
        this.showWifiIntent = new Intent(this.topLevel, ConfigureWiFiActivity.class);
        this.topLevel.startActivity(this.showWifiIntent);
    }
}
