package com.p004ti.ble.common.oad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import com.p004ti.ti_oad.TIOADEoadDefinitions;
import com.ti.ble.simplelinkstarter.R;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.ti.ble.common.oad.FWUpdateEOADNewFileSelectorStageTwo */
public class FWUpdateEOADNewFileSelectorStageTwo extends AppCompatActivity {
    public static final String TAG = FWUpdateEOADNewFileSelectorStageTwo.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;
    byte[] chipID;
    List<FWUpdateTIFirmwareEntry> fwEntries = null;
    FWUpdateEOADNewFileSelectorStageTwo mThis = this;
    TableLayout offChipImages;
    TableLayout onChipImages;
    boolean secure;

    /* access modifiers changed from: protected */
    public void onPause() {
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 != null) {
            unregisterReceiver(broadcastReceiver2);
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, final Intent intent) {
                if (intent.getAction().equalsIgnoreCase(FWUpdateEOADSelectorDialogFragment.ACTION_FW_WAS_SELECTED)) {
                    String str = FWUpdateEOADNewFileSelectorStageTwo.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("FW Was Selected: ");
                    sb.append(intent.getStringExtra("com.ti.ble.common.oad.SELECTED_IMAGE_FILENAME_EXTRA"));
                    Log.d(str, sb.toString());
                    FWUpdateEOADNewFileSelectorStageTwo.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Intent intent = new Intent();
                            String str = "com.ti.ble.common.oad.SELECTED_IMAGE_FILENAME_EXTRA";
                            intent.putExtra(str, intent.getStringExtra(str));
                            FWUpdateEOADNewFileSelectorStageTwo.this.mThis.setResult(-1, intent);
                            FWUpdateEOADNewFileSelectorStageTwo.this.mThis.finish();
                        }
                    });
                }
            }
        };
        registerReceiver(this.broadcastReceiver, new IntentFilter(FWUpdateEOADSelectorDialogFragment.ACTION_FW_WAS_SELECTED));
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        List<FWUpdateTIFirmwareEntry> list;
        super.onCreate(bundle);
        Intent intent = getIntent();
        this.chipID = intent.getByteArrayExtra("com.ti.ble.common.oad.DEVICE_ID_EXTRA");
        this.secure = intent.getBooleanExtra("com.ti.ble.common.oad.SECURITY_EXTRA", false);
        setContentView((int) R.layout.activity_fw_ng_image_selector_stage_two);
        this.onChipImages = (TableLayout) findViewById(R.id.fnis_two_on_chip_images);
        this.offChipImages = (TableLayout) findViewById(R.id.fnis_two_off_chip_images);
        this.onChipImages.removeAllViews();
        this.offChipImages.removeAllViews();
        try {
            list = new FWUpdateBINFileEntriesParser().parse(getAssets().open("firmware_list_eoad.xml"));
        } catch (Exception e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        this.fwEntries = new ArrayList();
        for (FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry : list) {
            if (fWUpdateTIFirmwareEntry.MCU.equalsIgnoreCase(TIOADEoadDefinitions.oadChipTypePrettyPrint(this.chipID)) && fWUpdateTIFirmwareEntry.Security == this.secure) {
                this.fwEntries.add(fWUpdateTIFirmwareEntry);
            }
        }
        for (final FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry2 : this.fwEntries) {
            FWUpdateEOADNewFileSelectorStageTwoTableRow fWUpdateEOADNewFileSelectorStageTwoTableRow = new FWUpdateEOADNewFileSelectorStageTwoTableRow(this.mThis);
            if (fWUpdateTIFirmwareEntry2.WirelessStandard.equalsIgnoreCase("ble")) {
                fWUpdateEOADNewFileSelectorStageTwoTableRow.setImageWirelessSTDIcon(R.mipmap.fw_bt);
            } else if (fWUpdateTIFirmwareEntry2.WirelessStandard.equalsIgnoreCase("thread")) {
                fWUpdateEOADNewFileSelectorStageTwoTableRow.setImageWirelessSTDIcon(R.mipmap.fw_thread);
            } else if (fWUpdateTIFirmwareEntry2.WirelessStandard.equalsIgnoreCase("RF4CE")) {
                fWUpdateEOADNewFileSelectorStageTwoTableRow.setImageWirelessSTDIcon(R.mipmap.fw_ti);
            } else if (fWUpdateTIFirmwareEntry2.WirelessStandard.equalsIgnoreCase("zigbee")) {
                fWUpdateEOADNewFileSelectorStageTwoTableRow.setImageWirelessSTDIcon(R.mipmap.fw_zigbee);
            } else {
                fWUpdateEOADNewFileSelectorStageTwoTableRow.setImageWirelessSTDIcon(R.mipmap.fw_ti);
            }
            fWUpdateEOADNewFileSelectorStageTwoTableRow.setImageTitle(fWUpdateTIFirmwareEntry2.Title);
            String str = new String(Base64.decode(fWUpdateTIFirmwareEntry2.Description, 0));
            fWUpdateEOADNewFileSelectorStageTwoTableRow.setImageDescription(str.substring(str.indexOf("<h3>") + 4, str.lastIndexOf("h3>") - 2));
            fWUpdateEOADNewFileSelectorStageTwoTableRow.getChildAt(0).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Log.d(FWUpdateEOADNewFileSelectorStageTwo.TAG, "Row sent");
                    FWUpdateEOADFWInfoDialogFragment.newInstance(fWUpdateTIFirmwareEntry2, FWUpdateEOADNewFileSelectorStageTwo.this.chipID).show(FWUpdateEOADNewFileSelectorStageTwo.this.getFragmentManager(), "FWUpdateEOADFWInfoDialogFragment");
                }
            });
            if (fWUpdateTIFirmwareEntry2.onChip) {
                this.onChipImages.addView(fWUpdateEOADNewFileSelectorStageTwoTableRow);
            } else {
                this.offChipImages.addView(fWUpdateEOADNewFileSelectorStageTwoTableRow);
            }
        }
    }
}
