package com.p004ti.ble.btsig;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GattInfo;
import com.ti.ble.simplelinkstarter.R;
import java.util.UUID;

/* renamed from: com.ti.ble.btsig.DeviceInformationServiceTableRow */
public class DeviceInformationServiceTableRow extends TableRow {
    public final TextView FirmwareREVLabel;
    public final TextView HardwareREVLabel;
    public final TextView ManifacturerNAMELabel;
    public final TextView ModelNRLabel;
    public final TextView SerialNRLabel;
    public final TextView SoftwareREVLabel;
    public final TextView SystemIDLabel;
    Context context;
    public final ImageView icon;
    public final Paint linePaint = new Paint() {
        {
            setStrokeWidth(1.0f);
            setARGB(255, 0, 0, 0);
        }
    };
    public final TextView title;

    public DeviceInformationServiceTableRow(Context context2) {
        super(context2);
        this.context = context2;
        addView(LayoutInflater.from(context2).inflate(R.layout.bt_sig_device_info_table_row, null, false));
        setBackgroundColor(0);
        this.icon = (ImageView) findViewById(R.id.btsig_devinfo_tr_icon);
        this.title = (TextView) findViewById(R.id.btsig_tr_devinfo_characteristic_title);
        this.SystemIDLabel = (TextView) findViewById(R.id.btsig_devinfo_tr_systemid);
        this.ModelNRLabel = (TextView) findViewById(R.id.btsig_devinfo_tr_modelnr);
        this.SerialNRLabel = (TextView) findViewById(R.id.btsig_devinfo_tr_serialnr);
        this.FirmwareREVLabel = (TextView) findViewById(R.id.btsig_devinfo_tr_firmwarerev);
        this.HardwareREVLabel = (TextView) findViewById(R.id.btsig_devinfo_tr_hardwarerev);
        this.SoftwareREVLabel = (TextView) findViewById(R.id.btsig_devinfo_tr_softwarerev);
        this.ManifacturerNAMELabel = (TextView) findViewById(R.id.btsig_devinfo_tr_manifname);
    }

    public void setIcon(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Getting MipMap for :");
        sb.append(str);
        sb.append(GattInfo.uuidToIcon(UUID.fromString(str2)));
        String str3 = "DeviceInformationServiceTableRow";
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
        String str4 = "DeviceInformationServiceTableRow";
        Log.d(str4, sb.toString());
        Resources resources = getResources();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(str3);
        int identifier = resources.getIdentifier(sb2.toString(), "mipmap", this.context.getPackageName());
        if (identifier == 0) {
            this.icon.setImageResource(R.mipmap.cc2650_launchpad_deviceinfo);
        }
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
