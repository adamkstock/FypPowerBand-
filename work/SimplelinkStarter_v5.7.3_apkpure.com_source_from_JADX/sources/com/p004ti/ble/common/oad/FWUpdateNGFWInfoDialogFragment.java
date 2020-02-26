package com.p004ti.ble.common.oad;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;
import java.util.List;

/* renamed from: com.ti.ble.common.oad.FWUpdateNGFWInfoDialogFragment */
public class FWUpdateNGFWInfoDialogFragment extends DialogFragment {
    public float cFW;
    public String cName;
    public List<FWUpdateTIFirmwareEntry> firmwares;
    public int fwPosition;

    public static FWUpdateNGFWInfoDialogFragment newInstance(List<FWUpdateTIFirmwareEntry> list, float f, String str, int i) {
        FWUpdateNGFWInfoDialogFragment fWUpdateNGFWInfoDialogFragment = new FWUpdateNGFWInfoDialogFragment();
        fWUpdateNGFWInfoDialogFragment.firmwares = list;
        fWUpdateNGFWInfoDialogFragment.cFW = f;
        fWUpdateNGFWInfoDialogFragment.cName = str;
        fWUpdateNGFWInfoDialogFragment.fwPosition = i;
        new Bundle();
        StringBuilder sb = new StringBuilder();
        sb.append("Current firmware version : ");
        sb.append(f);
        Log.d("FWUpdateNGSelectorDialogFragment", sb.toString());
        return fWUpdateNGFWInfoDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String str = "Download";
        Builder positiveButton = new Builder(getActivity()).setTitle("").setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FWUpdateNGFWInfoDialogFragment.this.dismiss();
            }
        }).setPositiveButton(str, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(FWUpdateNGSelectorDialogFragment.ACTION_FW_WAS_SELECTED);
                intent.putExtra(FWUpdateNGSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX, FWUpdateNGFWInfoDialogFragment.this.fwPosition);
                FWUpdateNGFWInfoDialogFragment.this.getActivity().sendBroadcast(intent);
                FWUpdateNGFWInfoDialogFragment.this.dismiss();
            }
        });
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.firmware_description, null);
        FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry = (FWUpdateTIFirmwareEntry) this.firmwares.get(this.fwPosition);
        TextView textView = (TextView) inflate.findViewById(R.id.fd_firmware_name);
        StringBuilder sb = new StringBuilder();
        sb.append(fWUpdateTIFirmwareEntry.DevPack);
        sb.append(" ");
        textView.setText(String.format("%s %s(%s)", new Object[]{fWUpdateTIFirmwareEntry.BoardType, sb.toString(), fWUpdateTIFirmwareEntry.WirelessStandard}));
        ((TextView) inflate.findViewById(R.id.fd_firmware_version)).setText(String.format("Version %1.2f", new Object[]{Float.valueOf(fWUpdateTIFirmwareEntry.Version)}));
        TextView textView2 = (TextView) inflate.findViewById(R.id.fd_wireless_standard);
        if (fWUpdateTIFirmwareEntry.WirelessStandard.equals("BLE")) {
            textView2.setText("Bluetooth Smart");
        } else {
            String str2 = "RF4CE";
            if (fWUpdateTIFirmwareEntry.WirelessStandard.equals(str2)) {
                textView2.setText(str2);
            } else if (fWUpdateTIFirmwareEntry.WirelessStandard.equals("Sub-One")) {
                textView2.setText("Sub-1GHz");
            } else {
                String str3 = "ZigBee";
                if (fWUpdateTIFirmwareEntry.WirelessStandard.equals(str3)) {
                    textView2.setText(str3);
                } else {
                    textView2.setText("Unknown");
                }
            }
        }
        ((TextView) inflate.findViewById(R.id.fd_mcu)).setText(fWUpdateTIFirmwareEntry.MCU);
        ((TextView) inflate.findViewById(R.id.fd_board_type)).setText(fWUpdateTIFirmwareEntry.BoardType);
        ((TextView) inflate.findViewById(R.id.fd_image_type)).setText(fWUpdateTIFirmwareEntry.Type);
        TextView textView3 = (TextView) inflate.findViewById(R.id.fd_size);
        TextView textView4 = (TextView) inflate.findViewById(R.id.fd_description_html);
        textView4.setText(Html.fromHtml(new String(Base64.decode(fWUpdateTIFirmwareEntry.Description, 0))));
        textView4.setMovementMethod(LinkMovementMethod.getInstance());
        positiveButton.setView(inflate);
        return positiveButton.create();
    }
}
