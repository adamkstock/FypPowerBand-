package com.p004ti.ble.common.oad;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ti_oad.TIOADEoadDefinitions;
import com.p004ti.ti_oad.TIOADEoadDefinitions.oadChipType;
import com.ti.ble.simplelinkstarter.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: com.ti.ble.common.oad.FWUpdateEOADNewFileSelectorStageOne */
public class FWUpdateEOADNewFileSelectorStageOne extends AppCompatActivity {
    public static final String TAG = FWUpdateEOADNewFileSelectorStageOne.class.getSimpleName();
    byte[] chipID;
    List<FWUpdateTIFirmwareEntry> fwEntries = null;
    FWUpdateEOADNewFileSelectorStageOne mThis = this;
    boolean secure = false;
    TableLayout supImages;
    TableLayout unsupImages;

    /* renamed from: com.ti.ble.common.oad.FWUpdateEOADNewFileSelectorStageOne$4 */
    static /* synthetic */ class C06544 {
        static final /* synthetic */ int[] $SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType = new int[oadChipType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                com.ti.ti_oad.TIOADEoadDefinitions$oadChipType[] r0 = com.p004ti.ti_oad.TIOADEoadDefinitions.oadChipType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType = r0
                int[] r0 = $SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.ti.ti_oad.TIOADEoadDefinitions$oadChipType r1 = com.p004ti.ti_oad.TIOADEoadDefinitions.oadChipType.tiOADChipTypeCC2640R2     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType     // Catch:{ NoSuchFieldError -> 0x001f }
                com.ti.ti_oad.TIOADEoadDefinitions$oadChipType r1 = com.p004ti.ti_oad.TIOADEoadDefinitions.oadChipType.tiOADChipTypeCC1352     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType     // Catch:{ NoSuchFieldError -> 0x002a }
                com.ti.ti_oad.TIOADEoadDefinitions$oadChipType r1 = com.p004ti.ti_oad.TIOADEoadDefinitions.oadChipType.tiOADChipTypeCC1352P     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.ti.ti_oad.TIOADEoadDefinitions$oadChipType r1 = com.p004ti.ti_oad.TIOADEoadDefinitions.oadChipType.tiOADChipTypeCC2652     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.ti.ti_oad.TIOADEoadDefinitions$oadChipType r1 = com.p004ti.ti_oad.TIOADEoadDefinitions.oadChipType.tiOADChipTypeCC2642     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.p004ti.ble.common.oad.FWUpdateEOADNewFileSelectorStageOne.C06544.<clinit>():void");
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        this.chipID = intent.getByteArrayExtra("com.ti.ble.common.oad.DEVICE_ID_EXTRA");
        this.secure = intent.getBooleanExtra("com.ti.ble.common.oad.SECURITY_EXTRA", false);
        setContentView((int) R.layout.activity_fw_ng_image_selector);
        ((TextView) findViewById(R.id.fnis_chip_id)).setText(TIOADEoadDefinitions.oadChipTypePrettyPrint(this.chipID));
        this.supImages = (TableLayout) findViewById(R.id.fnis_supported_images);
        this.unsupImages = (TableLayout) findViewById(R.id.fnis_unsupported_images);
        this.supImages.removeAllViews();
        this.unsupImages.removeAllViews();
        try {
            this.fwEntries = new FWUpdateBINFileEntriesParser().parse(getAssets().open("firmware_list_eoad.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateRowsFromEntries(false);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Intent intent2 = new Intent();
        String str = "com.ti.ble.common.oad.SELECTED_IMAGE_FILENAME_EXTRA";
        intent2.putExtra(str, intent.getStringExtra(str));
        this.mThis.setResult(-1, intent2);
        this.mThis.finish();
    }

    private ArrayList<String> getUnsupportedCircuitsFromList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry : this.fwEntries) {
            String str = fWUpdateTIFirmwareEntry.MCU;
            if (!arrayList.contains(str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    private int getNrOfSupImages() {
        int i = 0;
        for (FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry : this.fwEntries) {
            if (fWUpdateTIFirmwareEntry.MCU.equalsIgnoreCase(TIOADEoadDefinitions.oadChipTypePrettyPrint(this.chipID)) && fWUpdateTIFirmwareEntry.Security == this.secure) {
                i++;
            }
        }
        return i;
    }

    private int getNrOfUnsupImages() {
        return this.fwEntries.size() - getNrOfSupImages();
    }

    /* access modifiers changed from: private */
    public void updateRowsFromEntries(boolean z) {
        String str = " images)";
        String str2 = " (";
        if (!z) {
            if (getNrOfSupImages() > 0) {
                this.supImages.removeAllViews();
                FWUpdateEOADNewFileSelectorStageOneTableRow fWUpdateEOADNewFileSelectorStageOneTableRow = new FWUpdateEOADNewFileSelectorStageOneTableRow(this.mThis.getApplicationContext());
                fWUpdateEOADNewFileSelectorStageOneTableRow.setCircuitNameText(TIOADEoadDefinitions.oadChipTypePrettyPrint(this.chipID));
                StringBuilder sb = new StringBuilder();
                sb.append(circuitDescriptionFromChipID(this.chipID));
                sb.append(str2);
                sb.append(getNrOfSupImages());
                sb.append(str);
                fWUpdateEOADNewFileSelectorStageOneTableRow.setCircuitDescriptionText(sb.toString());
                fWUpdateEOADNewFileSelectorStageOneTableRow.getChildAt(0).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Log.d(FWUpdateEOADNewFileSelectorStageOne.TAG, "Correct Circuit clicked");
                        Intent intent = new Intent(FWUpdateEOADNewFileSelectorStageOne.this.mThis, FWUpdateEOADNewFileSelectorStageTwo.class);
                        intent.putExtra("com.ti.ble.common.oad.DEVICE_ID_EXTRA", FWUpdateEOADNewFileSelectorStageOne.this.chipID);
                        intent.putExtra("com.ti.ble.common.oad.SECURITY_EXTRA", FWUpdateEOADNewFileSelectorStageOne.this.secure);
                        FWUpdateEOADNewFileSelectorStageOne.this.mThis.startActivityForResult(intent, 0);
                    }
                });
                this.supImages.addView(fWUpdateEOADNewFileSelectorStageOneTableRow);
            }
            if (getNrOfUnsupImages() > 0) {
                TableRow tableRow = new TableRow(this.mThis);
                TextView textView = new TextView(this.mThis);
                textView.setTextColor(-7829368);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextSize(14.0f);
                textView.setText("Touch me to show unsupported factory images");
                textView.setPadding(20, 10, 0, 0);
                tableRow.setPadding(40, 0, 0, 0);
                tableRow.addView(textView);
                tableRow.getChildAt(0).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Log.d(FWUpdateEOADNewFileSelectorStageOne.TAG, "Expansion touched");
                        FWUpdateEOADNewFileSelectorStageOne.this.updateRowsFromEntries(true);
                    }
                });
                this.unsupImages.addView(tableRow);
                return;
            }
            return;
        }
        this.unsupImages.removeAllViews();
        Iterator it = getUnsupportedCircuitsFromList().iterator();
        while (it.hasNext()) {
            String str3 = (String) it.next();
            FWUpdateEOADNewFileSelectorStageOneTableRow fWUpdateEOADNewFileSelectorStageOneTableRow2 = new FWUpdateEOADNewFileSelectorStageOneTableRow(this.mThis.getApplicationContext());
            fWUpdateEOADNewFileSelectorStageOneTableRow2.setCircuitNameText(str3);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(circuitDescriptionFromStringChipName(str3));
            sb2.append(str2);
            sb2.append(0);
            sb2.append(str);
            fWUpdateEOADNewFileSelectorStageOneTableRow2.setCircuitDescriptionText(sb2.toString());
            fWUpdateEOADNewFileSelectorStageOneTableRow2.getChildAt(0).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    final AlertDialog create = new Builder(FWUpdateEOADNewFileSelectorStageOne.this.mThis).create();
                    create.setTitle("Unsupported MCU");
                    create.setMessage("This image cannot be used on the currently connected MCU");
                    create.setButton(-3, (CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    FWUpdateEOADNewFileSelectorStageOne.this.mThis.runOnUiThread(new Runnable() {
                        public void run() {
                            create.show();
                        }
                    });
                }
            });
            this.unsupImages.addView(fWUpdateEOADNewFileSelectorStageOneTableRow2);
        }
    }

    public String circuitDescriptionFromStringChipName(String str) {
        if (str.equalsIgnoreCase("CC1352")) {
            return "SimpleLink® Multi-Band MCU";
        }
        if (str.equalsIgnoreCase("CC1352P")) {
            return "SimpleLink® Multi-Band MCU With PA";
        }
        String str2 = "SimpleLink® Bluetooth Low Energy MCU";
        if (str.equalsIgnoreCase("CC2640R2")) {
            return str2;
        }
        if (str.equalsIgnoreCase("CC2652")) {
            return "SimpleLink® Multi-Standard Wireless MCU";
        }
        return str.equalsIgnoreCase("CC2642") ? str2 : "Unknown SimpleLink® MCU";
    }

    public String circuitDescriptionFromChipID(byte[] bArr) {
        int i = C06544.$SwitchMap$com$ti$ti_oad$TIOADEoadDefinitions$oadChipType[oadChipType.fromInteger(bArr[0]).ordinal()];
        String str = "SimpleLink® Bluetooth Low Energy MCU";
        if (i == 1) {
            return str;
        }
        if (i == 2) {
            return "SimpleLink® Multi-Band MCU";
        }
        if (i == 3) {
            return "SimpleLink® Multi-Band MCU With PA";
        }
        if (i != 4) {
            return i != 5 ? "" : str;
        }
        return "SimpleLink® Multi-Standard Wireless MCU";
    }
}
