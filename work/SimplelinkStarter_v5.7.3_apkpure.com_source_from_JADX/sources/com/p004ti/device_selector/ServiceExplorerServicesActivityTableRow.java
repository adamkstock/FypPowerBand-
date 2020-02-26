package com.p004ti.device_selector;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GattInfo;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.ServiceExplorerServicesActivityTableRow */
public class ServiceExplorerServicesActivityTableRow extends TableRow {
    public Paint linePaint;
    public BluetoothGattCharacteristic myCharacteristic;
    public BluetoothGattService myService;

    public ServiceExplorerServicesActivityTableRow(Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.table_row_service_explorer_service, null, false));
        this.linePaint = new Paint() {
            {
                setStrokeWidth(1.0f);
                setARGB(255, 0, 0, 0);
            }
        };
        setBackgroundColor(0);
    }

    public ServiceExplorerServicesActivityTableRow(Context context, BluetoothGattService bluetoothGattService, int i) {
        this(context);
        this.myService = bluetoothGattService;
        new GattInfo(getResources().getXml(R.xml.gatt_uuid));
        TextView textView = (TextView) findViewById(R.id.trses_service_title);
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(" - ");
        sb.append(GattInfo.getTitle(bluetoothGattService.getUuid()));
        textView.setText(sb.toString());
        TextView textView2 = (TextView) findViewById(R.id.trses_characteristics);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(bluetoothGattService.getCharacteristics().size());
        sb2.append(" Characteristics");
        textView2.setText(sb2.toString());
        TextView textView3 = (TextView) findViewById(R.id.trses_uuid_title);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("UUID: ");
        sb3.append(bluetoothGattService.getUuid().toString());
        textView3.setText(sb3.toString());
    }

    public ServiceExplorerServicesActivityTableRow(Context context, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        this(context);
        this.myCharacteristic = bluetoothGattCharacteristic;
        new GattInfo(getResources().getXml(R.xml.gatt_uuid));
        TextView textView = (TextView) findViewById(R.id.trses_service_title);
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(" - ");
        sb.append(GattInfo.getTitle(bluetoothGattCharacteristic.getUuid()));
        textView.setText(sb.toString());
        TextView textView2 = (TextView) findViewById(R.id.trses_uuid_title);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("UUID: ");
        sb2.append(bluetoothGattCharacteristic.getUuid().toString());
        textView2.setText(sb2.toString());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), (float) canvas.getWidth(), ((float) canvas.getHeight()) - this.linePaint.getStrokeWidth(), this.linePaint);
    }
}
