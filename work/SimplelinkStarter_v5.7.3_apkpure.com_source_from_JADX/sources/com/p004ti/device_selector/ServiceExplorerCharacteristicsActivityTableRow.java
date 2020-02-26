package com.p004ti.device_selector;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GattInfo;
import com.p004ti.ti_oad.TIOADEoadDefinitions;
import com.ti.ble.simplelinkstarter.R;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/* renamed from: com.ti.device_selector.ServiceExplorerCharacteristicsActivityTableRow */
public class ServiceExplorerCharacteristicsActivityTableRow extends TableRow {
    static final String TAG = ServiceExplorerCharacteristicsActivityTableRow.class.getSimpleName();
    public Paint linePaint;
    public BluetoothGattCharacteristic myCharacteristic;
    public BluetoothGattService myService;
    public BluetoothGattDescriptor myUserDescription;

    public ServiceExplorerCharacteristicsActivityTableRow(Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.table_row_service_explorer_characteristic, null, false));
        this.linePaint = new Paint() {
            {
                setStrokeWidth(1.0f);
                setARGB(255, 0, 0, 0);
            }
        };
        setBackgroundColor(0);
    }

    public void refreshValue() {
        String str = "Value (UTF8): <binary data>";
        TextView textView = (TextView) findViewById(R.id.trsec_value);
        StringBuilder sb = new StringBuilder();
        sb.append("Value: ");
        sb.append(TIOADEoadDefinitions.BytetohexString(this.myCharacteristic.getValue()));
        textView.setText(sb.toString());
        TextView textView2 = (TextView) findViewById(R.id.trsec_value_string);
        try {
            if (looksLikeUTF8(this.myCharacteristic.getValue())) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Value (UTF8): ");
                sb2.append(new String(this.myCharacteristic.getValue()));
                textView2.setText(sb2.toString());
            } else {
                textView2.setText(str);
            }
        } catch (UnsupportedEncodingException unused) {
            textView2.setText(str);
        }
        textView.setEnabled(true);
        textView2.setEnabled(true);
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Refreshed Value for ");
        sb3.append(this.myCharacteristic.getUuid().toString());
        Log.d(str2, sb3.toString());
    }

    public void refreshDescription() {
        TextView textView = (TextView) findViewById(R.id.trsec_user_description);
        if (this.myUserDescription != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("User Description: ");
            sb.append(new String(this.myUserDescription.getValue()));
            textView.setText(sb.toString());
            return;
        }
        textView.setText("User Description: <not present>");
    }

    public ServiceExplorerCharacteristicsActivityTableRow(Context context, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
        this(context);
        this.myCharacteristic = bluetoothGattCharacteristic;
        this.myUserDescription = bluetoothGattDescriptor;
        new GattInfo(getResources().getXml(R.xml.gatt_uuid));
        TextView textView = (TextView) findViewById(R.id.trsec_service_title);
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(" - ");
        sb.append(GattInfo.getTitle(bluetoothGattCharacteristic.getUuid()));
        textView.setText(sb.toString());
        TextView textView2 = (TextView) findViewById(R.id.trsec_uuid_title);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("UUID: ");
        sb2.append(bluetoothGattCharacteristic.getUuid().toString());
        textView2.setText(sb2.toString());
        TextView textView3 = (TextView) findViewById(R.id.trsec_user_description);
        if (bluetoothGattDescriptor != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("User Description: ");
            sb3.append(bluetoothGattDescriptor.toString());
            textView3.setText(sb3.toString());
        } else {
            textView3.setText("User Description: Not present");
        }
        ImageView imageView = (ImageView) findViewById(R.id.trsec_read_prop);
        ImageView imageView2 = (ImageView) findViewById(R.id.trsec_write_prop);
        ImageView imageView3 = (ImageView) findViewById(R.id.trsec_notification_prop);
        ImageView imageView4 = (ImageView) findViewById(R.id.trsec_indicate_prop);
        if ((bluetoothGattCharacteristic.getProperties() & 2) == 2) {
            imageView.setImageResource(R.mipmap.read);
        } else {
            TextView textView4 = (TextView) findViewById(R.id.trsec_value_string);
            ((TextView) findViewById(R.id.trsec_value)).setEnabled(false);
            textView4.setEnabled(false);
        }
        if ((bluetoothGattCharacteristic.getProperties() & 8) == 8) {
            imageView2.setImageResource(R.mipmap.write);
        }
        if ((bluetoothGattCharacteristic.getProperties() & 16) == 16) {
            imageView3.setImageResource(R.mipmap.notification);
        }
        if ((bluetoothGattCharacteristic.getProperties() & 32) == 32) {
            imageView4.setImageResource(R.mipmap.indicate);
        }
    }

    static boolean looksLikeUTF8(byte[] bArr) throws UnsupportedEncodingException {
        if (bArr == null) {
            return false;
        }
        return Pattern.compile("\\A(\n  [\\x09\\x0A\\x0D\\x20-\\x7E]             # ASCII\\n| [\\xC2-\\xDF][\\x80-\\xBF]               # non-overlong 2-byte\n|  \\xE0[\\xA0-\\xBF][\\x80-\\xBF]         # excluding overlongs\n| [\\xE1-\\xEC\\xEE\\xEF][\\x80-\\xBF]{2}  # straight 3-byte\n|  \\xED[\\x80-\\x9F][\\x80-\\xBF]         # excluding surrogates\n|  \\xF0[\\x90-\\xBF][\\x80-\\xBF]{2}      # planes 1-3\n| [\\xF1-\\xF3][\\x80-\\xBF]{3}            # planes 4-15\n|  \\xF4[\\x80-\\x8F][\\x80-\\xBF]{2}      # plane 16\n)*\\z", 4).matcher(new String(bArr, "ISO-8859-1")).matches();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
