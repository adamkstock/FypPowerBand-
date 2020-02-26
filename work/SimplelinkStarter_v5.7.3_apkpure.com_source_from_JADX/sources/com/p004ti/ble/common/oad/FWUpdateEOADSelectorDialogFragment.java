package com.p004ti.ble.common.oad;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import com.p004ti.ti_oad.TIOADEoadDefinitions;
import com.ti.ble.simplelinkstarter.R;
import java.util.List;

/* renamed from: com.ti.ble.common.oad.FWUpdateEOADSelectorDialogFragment */
public class FWUpdateEOADSelectorDialogFragment extends DialogFragment {
    public static final String ACTION_FW_WAS_SELECTED = "FWUpdateEOADSelectorDialogFragment.SELECTED";
    public static final String EXTRA_SELECTED_FW_INDEX = "FWUpdateEOADSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX";
    static final String TAG = FWUpdateEOADSelectorDialogFragment.class.getSimpleName();
    byte[] chipID;
    List<FWUpdateTIFirmwareEntry> firmwares;
    BroadcastReceiver recv;
    TableLayout table;

    public static FWUpdateEOADSelectorDialogFragment newInstance(List<FWUpdateTIFirmwareEntry> list, byte[] bArr) {
        FWUpdateEOADSelectorDialogFragment fWUpdateEOADSelectorDialogFragment = new FWUpdateEOADSelectorDialogFragment();
        fWUpdateEOADSelectorDialogFragment.firmwares = list;
        fWUpdateEOADSelectorDialogFragment.chipID = bArr;
        new Bundle();
        return fWUpdateEOADSelectorDialogFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.recv = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(FWUpdateEOADSelectorDialogFragment.ACTION_FW_WAS_SELECTED)) {
                    FWUpdateEOADSelectorDialogFragment.this.dismiss();
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
        Builder negativeButton = new Builder(getActivity()).setTitle("Select FW").setNegativeButton("Cancel", null);
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fw_selector, null);
        this.table = (TableLayout) inflate.findViewById(R.id.fwentries_layout);
        this.table.removeAllViews();
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        this.table.setMinimumWidth(rect.width());
        if (this.firmwares != null) {
            for (int i = 0; i < this.firmwares.size(); i++) {
                FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry = (FWUpdateTIFirmwareEntry) this.firmwares.get(i);
                if (!fWUpdateTIFirmwareEntry.MCU.equalsIgnoreCase(TIOADEoadDefinitions.oadChipTypePrettyPrint(this.chipID))) {
                    fWUpdateTIFirmwareEntry.compatible = false;
                }
                final FWUpdateBINFileEntryTableRow fWUpdateBINFileEntryTableRow = new FWUpdateBINFileEntryTableRow(getActivity(), fWUpdateTIFirmwareEntry);
                GradientDrawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-1, -3355444});
                gradientDrawable.setGradientType(0);
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{16842919, -16842913}, gradientDrawable);
                fWUpdateBINFileEntryTableRow.setBackground(stateListDrawable);
                fWUpdateBINFileEntryTableRow.position = i;
                fWUpdateBINFileEntryTableRow.getChildAt(0).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                    }
                });
                ImageView imageView = (ImageView) fWUpdateBINFileEntryTableRow.getChildAt(0).findViewById(R.id.fetr_download_button);
                imageView.setClickable(true);
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(FWUpdateEOADSelectorDialogFragment.ACTION_FW_WAS_SELECTED);
                        intent.putExtra(FWUpdateEOADSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX, fWUpdateBINFileEntryTableRow.position);
                        FWUpdateEOADSelectorDialogFragment.this.getActivity().sendBroadcast(intent);
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
