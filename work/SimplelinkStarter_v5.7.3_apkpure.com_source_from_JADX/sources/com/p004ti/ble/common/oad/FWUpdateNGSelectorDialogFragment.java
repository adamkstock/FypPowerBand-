package com.p004ti.ble.common.oad;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import com.ti.ble.simplelinkstarter.R;
import java.util.List;

/* renamed from: com.ti.ble.common.oad.FWUpdateNGSelectorDialogFragment */
public class FWUpdateNGSelectorDialogFragment extends DialogFragment {
    public static final String ACTION_FW_WAS_SELECTED = "FWUpdateNGSelectorDialogFragment.SELECTED";
    public static final String EXTRA_SELECTED_FW_INDEX = "FWUpdateNGSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX";
    float cFW;
    String cName;
    List<FWUpdateTIFirmwareEntry> firmwares = null;
    BroadcastReceiver recv;
    TableLayout table = null;

    public static FWUpdateNGSelectorDialogFragment newInstance(List<FWUpdateTIFirmwareEntry> list, float f, String str) {
        FWUpdateNGSelectorDialogFragment fWUpdateNGSelectorDialogFragment = new FWUpdateNGSelectorDialogFragment();
        fWUpdateNGSelectorDialogFragment.firmwares = list;
        fWUpdateNGSelectorDialogFragment.cFW = f;
        fWUpdateNGSelectorDialogFragment.cName = str;
        new Bundle();
        StringBuilder sb = new StringBuilder();
        sb.append("Current firmware version : ");
        sb.append(f);
        Log.d("FWUpdateNGSelectorDialogFragment", sb.toString());
        return fWUpdateNGSelectorDialogFragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.recv = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(FWUpdateNGSelectorDialogFragment.ACTION_FW_WAS_SELECTED)) {
                    FWUpdateNGSelectorDialogFragment.this.dismiss();
                }
            }
        };
        getActivity().registerReceiver(this.recv, new IntentFilter(ACTION_FW_WAS_SELECTED));
    }

    public void dismiss() {
        if (!(this.recv == null || getActivity() == null)) {
            getActivity().unregisterReceiver(this.recv);
        }
        super.dismiss();
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Builder negativeButton = new Builder(getActivity()).setTitle("Select Factory FW").setNegativeButton("Cancel", null);
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fw_selector, null);
        this.table = (TableLayout) inflate.findViewById(R.id.fwentries_layout);
        this.table.removeAllViews();
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        this.table.setMinimumWidth(rect.width());
        if (this.firmwares != null) {
            for (int i = 0; i < this.firmwares.size(); i++) {
                FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry = (FWUpdateTIFirmwareEntry) this.firmwares.get(i);
                if (fWUpdateTIFirmwareEntry.BoardType.compareToIgnoreCase(this.cName) != 0) {
                    Log.d("FWUpdateNGSelectorDialogFragment", "Boardtype wrong !");
                    fWUpdateTIFirmwareEntry.compatible = false;
                }
                if (fWUpdateTIFirmwareEntry.RequiredVersionRev > this.cFW) {
                    fWUpdateTIFirmwareEntry.compatible = false;
                }
                if (fWUpdateTIFirmwareEntry.Version < this.cFW && fWUpdateTIFirmwareEntry.WirelessStandard.equalsIgnoreCase("BLE")) {
                    fWUpdateTIFirmwareEntry.compatible = false;
                }
                final FWUpdateBINFileEntryTableRow fWUpdateBINFileEntryTableRow = new FWUpdateBINFileEntryTableRow(getActivity(), fWUpdateTIFirmwareEntry);
                GradientDrawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-1, -3355444});
                gradientDrawable.setGradientType(0);
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{16842919, -16842913}, gradientDrawable);
                fWUpdateBINFileEntryTableRow.setBackgroundDrawable(stateListDrawable);
                fWUpdateBINFileEntryTableRow.position = i;
                fWUpdateBINFileEntryTableRow.getChildAt(0).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Log.d("FWUpdateNGSelectorDialogFragment", "Main cell show firmware info clicked");
                        FWUpdateNGFWInfoDialogFragment.newInstance(FWUpdateNGSelectorDialogFragment.this.firmwares, FWUpdateNGSelectorDialogFragment.this.cFW, FWUpdateNGSelectorDialogFragment.this.cName, fWUpdateBINFileEntryTableRow.position).show(FWUpdateNGSelectorDialogFragment.this.getActivity().getFragmentManager(), "FrimwareDescriptionFragment");
                    }
                });
                ImageView imageView = (ImageView) fWUpdateBINFileEntryTableRow.getChildAt(0).findViewById(R.id.fetr_download_button);
                imageView.setClickable(true);
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Log.d("FWUpdateNGSelectorDialogFragment", "Firmware cell clicked");
                        Intent intent = new Intent(FWUpdateNGSelectorDialogFragment.ACTION_FW_WAS_SELECTED);
                        intent.putExtra(FWUpdateNGSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX, fWUpdateBINFileEntryTableRow.position);
                        FWUpdateNGSelectorDialogFragment.this.getActivity().sendBroadcast(intent);
                    }
                });
                if (fWUpdateTIFirmwareEntry.compatible) {
                    fWUpdateBINFileEntryTableRow.setGrayedOut(Boolean.valueOf(false));
                } else {
                    fWUpdateBINFileEntryTableRow.setGrayedOut(Boolean.valueOf(true));
                }
                this.table.addView(fWUpdateBINFileEntryTableRow);
                this.table.requestLayout();
            }
        }
        negativeButton.setView(inflate);
        return negativeButton.create();
    }
}
