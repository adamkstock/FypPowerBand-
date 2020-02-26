package com.p004ti.ble.bluetooth_le_controller;

import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GattInfo;
import com.p004ti.util.bleUtility;
import com.ti.ble.simplelinkstarter.R;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ti.ble.bluetooth_le_controller.EddystoneBeaconDecoder */
public class EddystoneBeaconDecoder {
    public static final int MAXSUFFIXVALUE = 13;
    public static final String TAG = "EddystoneBeaconDecoder";
    public static final Map<Integer, String> eddystoneURISchemesPrefix = new HashMap();
    public static final Map<Integer, String> eddystoneURISchemesSuffix = new HashMap();
    public long beaconAdvPDUCount;
    public float beaconBatVoltage;
    public long beaconSecondCount;
    public int beaconTXPower;
    public float beaconTemperature;
    public URL beaconURL;
    public byte beaconURLSchemePrefix;
    public byte beaconVersion;

    static {
        Map<Integer, String> map = eddystoneURISchemesPrefix;
        Integer valueOf = Integer.valueOf(0);
        map.put(valueOf, "http://www.");
        Map<Integer, String> map2 = eddystoneURISchemesPrefix;
        Integer valueOf2 = Integer.valueOf(1);
        map2.put(valueOf2, "https://www.");
        Map<Integer, String> map3 = eddystoneURISchemesPrefix;
        Integer valueOf3 = Integer.valueOf(2);
        map3.put(valueOf3, "http://");
        Map<Integer, String> map4 = eddystoneURISchemesPrefix;
        Integer valueOf4 = Integer.valueOf(3);
        map4.put(valueOf4, "https://");
        eddystoneURISchemesSuffix.put(valueOf, ".com/");
        eddystoneURISchemesSuffix.put(valueOf2, ".org/");
        eddystoneURISchemesSuffix.put(valueOf3, ".edu/");
        eddystoneURISchemesSuffix.put(valueOf4, ".net/");
        eddystoneURISchemesSuffix.put(Integer.valueOf(4), ".info/");
        eddystoneURISchemesSuffix.put(Integer.valueOf(5), ".biz/");
        eddystoneURISchemesSuffix.put(Integer.valueOf(6), ".gov/");
        eddystoneURISchemesSuffix.put(Integer.valueOf(7), ".com");
        eddystoneURISchemesSuffix.put(Integer.valueOf(8), ".org");
        eddystoneURISchemesSuffix.put(Integer.valueOf(9), ".edu");
        eddystoneURISchemesSuffix.put(Integer.valueOf(10), ".net");
        eddystoneURISchemesSuffix.put(Integer.valueOf(11), ".info");
        eddystoneURISchemesSuffix.put(Integer.valueOf(12), ".biz");
        eddystoneURISchemesSuffix.put(Integer.valueOf(13), ".gov");
    }

    public static float calcFixedPointFromUINT16(byte[] bArr) {
        if (bArr.length != 2) {
            return -1.0f;
        }
        return ((float) bArr[0]) + (((float) bArr[1]) / 256.0f);
    }

    public boolean updateData(Map<ParcelUuid, byte[]> map) {
        if (!map.containsKey(new ParcelUuid(GattInfo.EDDYSTONE_SERVICE_UUID))) {
            return false;
        }
        byte[] bArr = (byte[]) map.get(new ParcelUuid(GattInfo.EDDYSTONE_SERVICE_UUID));
        if (bArr.length > 4) {
            byte b = bArr[0];
            String str = TAG;
            if (b == 16) {
                Log.d(str, "Eddystone URL found !");
                this.beaconTXPower = bArr[1];
                this.beaconURLSchemePrefix = bArr[2];
                String str2 = new String();
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append((String) eddystoneURISchemesPrefix.get(new Integer(this.beaconURLSchemePrefix)));
                String sb2 = sb.toString();
                for (int i = 3; i < bArr.length; i++) {
                    if (bArr[i] < 13) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(sb2);
                        sb3.append((String) eddystoneURISchemesSuffix.get(new Integer(bArr[i])));
                        sb2 = sb3.toString();
                    } else {
                        byte[] bArr2 = {bArr[i]};
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(sb2);
                        sb4.append(new String(bArr2));
                        sb2 = sb4.toString();
                    }
                }
                StringBuilder sb5 = new StringBuilder();
                sb5.append("TX Power : ");
                sb5.append(this.beaconTXPower);
                sb5.append("dBm");
                Log.d(str, sb5.toString());
                StringBuilder sb6 = new StringBuilder();
                sb6.append("URL : ");
                sb6.append(sb2);
                Log.d(str, sb6.toString());
                try {
                    this.beaconURL = new URL(sb2);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if (b != 32) {
                Log.d(str, "Unknown Eddystone data found !");
            } else {
                Log.d(str, "Eddystone Unencrypted TLM Frame found !");
                this.beaconBatVoltage = ((float) bleUtility.BUILD_UINT16(bArr[2], bArr[3])) / 1000.0f;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Beacon Voltage :");
                sb7.append(this.beaconBatVoltage);
                Log.d(str, sb7.toString());
                this.beaconTemperature = calcFixedPointFromUINT16(new byte[]{bArr[4], bArr[5]});
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Beacon Temperature : ");
                sb8.append(this.beaconTemperature);
                Log.d(str, sb8.toString());
                this.beaconAdvPDUCount = bleUtility.BUILD_UINT32(bArr[6], bArr[7], bArr[8], bArr[9]);
                StringBuilder sb9 = new StringBuilder();
                sb9.append("Beacon Advertisment count : ");
                sb9.append(this.beaconAdvPDUCount);
                String str3 = " (0x%08x)";
                sb9.append(String.format(str3, new Object[]{Long.valueOf(this.beaconAdvPDUCount)}));
                Log.d(str, sb9.toString());
                this.beaconSecondCount = bleUtility.BUILD_UINT32(bArr[10], bArr[11], bArr[12], bArr[13]);
                StringBuilder sb10 = new StringBuilder();
                sb10.append("Beacon Second Count : ");
                sb10.append(this.beaconSecondCount);
                sb10.append(String.format(str3, new Object[]{Long.valueOf(this.beaconSecondCount)}));
                Log.d(str, sb10.toString());
            }
        }
        return true;
    }

    public void updateTableWithParameters(TableLayout tableLayout, Context context) {
        tableLayout.removeAllViews();
        String str = "layout_inflater";
        TableRow tableRow = (TableRow) ((LayoutInflater) context.getSystemService(str)).inflate(R.layout.adv_data_table_row, null);
        ((TextView) tableRow.findViewById(R.id.advdtr_datatype)).setText("URL :");
        String str2 = "";
        ((TextView) tableRow.findViewById(R.id.advdtr_data_name)).setText(str2);
        TextView textView = (TextView) tableRow.findViewById(R.id.advdtr_data_value);
        URL url = this.beaconURL;
        if (url != null) {
            textView.setText(url.toString());
        } else {
            textView.setText(str2);
        }
        tableLayout.addView(tableRow);
        TableRow tableRow2 = (TableRow) ((LayoutInflater) context.getSystemService(str)).inflate(R.layout.adv_data_table_row, null);
        ((TextView) tableRow2.findViewById(R.id.advdtr_datatype)).setText("TX Power Level :");
        ((TextView) tableRow2.findViewById(R.id.advdtr_data_name)).setText(str2);
        TextView textView2 = (TextView) tableRow2.findViewById(R.id.advdtr_data_value);
        StringBuilder sb = new StringBuilder();
        sb.append(this.beaconTXPower);
        sb.append("dBm");
        textView2.setText(sb.toString());
        tableLayout.addView(tableRow2);
    }
}
