package com.p004ti.ble.common.oad;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.p004ti.ti_oad.TIOADEoadDefinitions;
import com.p004ti.ti_oad.TIOADEoadHeader;
import com.p004ti.ti_oad.TIOADEoadImageReader;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.common.oad.FWUpdateEOADFWInfoDialogFragment */
public class FWUpdateEOADFWInfoDialogFragment extends DialogFragment {
    static final String TAG = FWUpdateEOADFWInfoDialogFragment.class.getSimpleName();
    byte[] chipType;
    public FWUpdateTIFirmwareEntry firmware;

    public static FWUpdateEOADFWInfoDialogFragment newInstance(FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry, byte[] bArr) {
        FWUpdateEOADFWInfoDialogFragment fWUpdateEOADFWInfoDialogFragment = new FWUpdateEOADFWInfoDialogFragment();
        fWUpdateEOADFWInfoDialogFragment.firmware = fWUpdateTIFirmwareEntry;
        fWUpdateEOADFWInfoDialogFragment.chipType = bArr;
        new Bundle();
        return fWUpdateEOADFWInfoDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String str = "Download";
        Builder positiveButton = new Builder(getActivity()).setTitle("").setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FWUpdateEOADFWInfoDialogFragment.this.dismiss();
            }
        }).setPositiveButton(str, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(FWUpdateEOADSelectorDialogFragment.ACTION_FW_WAS_SELECTED);
                intent.putExtra("com.ti.ble.common.oad.SELECTED_IMAGE_FILENAME_EXTRA", FWUpdateEOADFWInfoDialogFragment.this.firmware.Filename);
                FWUpdateEOADFWInfoDialogFragment.this.getActivity().sendBroadcast(intent);
                FWUpdateEOADFWInfoDialogFragment.this.dismiss();
            }
        });
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.firmware_description, null);
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Loading file : ");
        sb.append(this.firmware.Filename);
        Log.d(str2, sb.toString());
        TIOADEoadImageReader tIOADEoadImageReader = new TIOADEoadImageReader(this.firmware.Filename, (Context) getActivity());
        ImageView imageView = (ImageView) inflate.findViewById(R.id.fd_firmware_icon);
        if ((tIOADEoadImageReader.imageHeader.TIOADEoadImageWirelessTechnology & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_BLE) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_BLE) {
            imageView.setImageResource(R.mipmap.fw_bt);
        } else if ((tIOADEoadImageReader.imageHeader.TIOADEoadImageWirelessTechnology & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_THREAD) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_THREAD) {
            imageView.setImageResource(R.mipmap.fw_thread);
        } else if ((tIOADEoadImageReader.imageHeader.TIOADEoadImageWirelessTechnology & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_ZIGBEE) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_ZIGBEE) {
            imageView.setImageResource(R.mipmap.fw_zigbee);
        } else if ((tIOADEoadImageReader.imageHeader.TIOADEoadImageWirelessTechnology & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_802_15_4_SUB_ONE) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_802_15_4_SUB_ONE) {
            imageView.setImageResource(R.mipmap.fw_ti);
        }
        FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry = this.firmware;
        TextView textView = (TextView) inflate.findViewById(R.id.fd_firmware_name);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(fWUpdateTIFirmwareEntry.DevPack);
        sb2.append(" ");
        textView.setText(String.format("%s %s(%s)", new Object[]{fWUpdateTIFirmwareEntry.BoardType, sb2.toString(), fWUpdateTIFirmwareEntry.WirelessStandard}));
        ((TextView) inflate.findViewById(R.id.fd_firmware_version)).setText(String.format("Version %1.2f", new Object[]{Float.valueOf(fWUpdateTIFirmwareEntry.Version)}));
        ((TextView) inflate.findViewById(R.id.fd_wireless_standard)).setText(TIOADEoadHeader.WirelessStdToString(tIOADEoadImageReader.imageHeader.TIOADEoadImageWirelessTechnology));
        ((TextView) inflate.findViewById(R.id.fd_mcu)).setText(fWUpdateTIFirmwareEntry.MCU);
        ((TextView) inflate.findViewById(R.id.fd_board_type)).setText(fWUpdateTIFirmwareEntry.BoardType);
        ((TextView) inflate.findViewById(R.id.fd_image_type)).setText(fWUpdateTIFirmwareEntry.Type);
        ((TextView) inflate.findViewById(R.id.fd_size)).setText(String.format("%.1fKb", new Object[]{Float.valueOf(((float) tIOADEoadImageReader.imageHeader.TIOADEoadImageLength) / 1024.0f)}));
        TextView textView2 = (TextView) inflate.findViewById(R.id.fd_description_html);
        textView2.setText(Html.fromHtml(new String(Base64.decode(fWUpdateTIFirmwareEntry.Description, 0))));
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        positiveButton.setView(inflate);
        return positiveButton.create();
    }
}
