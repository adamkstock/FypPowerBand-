package com.p004ti.ble.common.cloud;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GattInfo;
import com.ti.ble.simplelinkstarter.R;
import java.util.UUID;

/* renamed from: com.ti.ble.common.cloud.IBMIoTCloudTableRow */
public class IBMIoTCloudTableRow extends TableRow {
    ImageView cloudConnectionStatus;
    TextView cloudURL;
    Button configureCloud;
    Context context;
    ImageView icon;
    Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };
    Switch pushToCloud;
    TextView pushToCloudCaption;
    TextView title;
    TextView value;

    public IBMIoTCloudTableRow(Context context2) {
        super(context2);
        this.context = context2;
        addView(LayoutInflater.from(context2).inflate(R.layout.cloud_configuration_table_row, null, false));
        setBackgroundColor(0);
        this.icon = (ImageView) findViewById(R.id.cloud_config_tr_icon);
        this.title = (TextView) findViewById(R.id.cloud_config_tr_characteristic_title);
        this.value = (TextView) findViewById(R.id.cloud_config_tr_value);
        this.configureCloud = (Button) findViewById(R.id.cloud_config_tr_advanced_button);
        this.cloudConnectionStatus = (ImageView) findViewById(R.id.cloud_config_tr_cloud_status_image);
        this.cloudURL = (TextView) findViewById(R.id.cloud_config_tr_cloud_url);
        this.pushToCloud = (Switch) findViewById(R.id.cloud_config_tr_push_to_cloud_sw);
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

    public void setCloudConnectionStatusImage(int i) {
        this.cloudConnectionStatus.setImageResource(i);
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

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
