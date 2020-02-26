package com.p004ti.device_selector;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import com.p004ti.ble.common.GattInfo;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.ServiceExplorerCharacteristicActionDialog */
public class ServiceExplorerCharacteristicActionDialog extends DialogFragment {
    static final String TAG = ServiceExplorerCharacteristicActionDialog.class.getSimpleName();
    Context context;
    public ServiceExplorerCharacteristicAction mCB;
    ServiceExplorerCharacteristicActionDialog mThis;
    BluetoothGattCharacteristic myCharacteristic;

    public static ServiceExplorerCharacteristicActionDialog newInstance(Context context2, BluetoothGattCharacteristic bluetoothGattCharacteristic, ServiceExplorerCharacteristicAction serviceExplorerCharacteristicAction) {
        ServiceExplorerCharacteristicActionDialog serviceExplorerCharacteristicActionDialog = new ServiceExplorerCharacteristicActionDialog();
        serviceExplorerCharacteristicActionDialog.context = context2;
        serviceExplorerCharacteristicActionDialog.myCharacteristic = bluetoothGattCharacteristic;
        serviceExplorerCharacteristicActionDialog.mThis = serviceExplorerCharacteristicActionDialog;
        serviceExplorerCharacteristicActionDialog.mCB = serviceExplorerCharacteristicAction;
        return serviceExplorerCharacteristicActionDialog;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        final Builder negativeButton = new Builder(getActivity()).setTitle("Characteristic Action").setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ServiceExplorerCharacteristicActionDialog.this.dismiss();
            }
        });
        final View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_service_explorer_characteristic_action, null);
        TableRow tableRow = (TableRow) inflate.findViewById(R.id.dseca_read_cell);
        TableRow tableRow2 = (TableRow) inflate.findViewById(R.id.dseca_write_cell);
        TableRow tableRow3 = (TableRow) inflate.findViewById(R.id.dseca_notify_cell);
        TableRow tableRow4 = (TableRow) inflate.findViewById(R.id.dseca_indicate_cell);
        TextView textView = (TextView) inflate.findViewById(R.id.dseca_read_characteristic);
        TextView textView2 = (TextView) inflate.findViewById(R.id.dseca_write_characteristic);
        TextView textView3 = (TextView) inflate.findViewById(R.id.dseca_notification_setting);
        TextView textView4 = (TextView) inflate.findViewById(R.id.dseca_indication_setting);
        if ((this.myCharacteristic.getProperties() & 2) != 2) {
            tableRow.setEnabled(false);
            textView.setEnabled(false);
        } else {
            tableRow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.d(ServiceExplorerCharacteristicActionDialog.TAG, "Read touched");
                    if (ServiceExplorerCharacteristicActionDialog.this.mCB != null) {
                        ServiceExplorerCharacteristicActionDialog.this.mCB.shallReadCharacteristic();
                    }
                    ServiceExplorerCharacteristicActionDialog.this.dismiss();
                }
            });
        }
        if ((this.myCharacteristic.getProperties() & 8) != 8) {
            tableRow2.setEnabled(false);
            textView2.setEnabled(false);
        } else {
            tableRow2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String obj = ((EditText) inflate.findViewById(R.id.dseca_write_characteristic_data)).getText().toString();
                    Log.d(ServiceExplorerCharacteristicActionDialog.TAG, "Write touched");
                    if (ServiceExplorerCharacteristicActionDialog.this.mCB != null) {
                        byte[] bytesFromString = ServiceExplorerCharacteristicActionDialog.getBytesFromString(obj);
                        if (bytesFromString != null) {
                            ServiceExplorerCharacteristicActionDialog.this.mCB.shallWriteCharacteristic(bytesFromString);
                            ServiceExplorerCharacteristicActionDialog.this.dismiss();
                            return;
                        }
                        negativeButton.setMessage("Cannot parse characters to write, please enter bytes with \",\" as delimiter 0x in front is not mandatory, all values are read as hex !");
                    }
                }
            });
        }
        if ((this.myCharacteristic.getProperties() & 16) != 16) {
            tableRow3.setEnabled(false);
            textView3.setEnabled(false);
        } else {
            byte[] value = this.myCharacteristic.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG).getValue();
            if (value == null) {
                dismiss();
                return null;
            }
            if (value[0] == 1) {
                textView3.setText("Disable Notifications");
            }
            tableRow3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.d(ServiceExplorerCharacteristicActionDialog.TAG, "Notification touched");
                    if (ServiceExplorerCharacteristicActionDialog.this.mCB != null) {
                        ServiceExplorerCharacteristicActionDialog.this.mCB.shallSetNotification();
                    }
                    ServiceExplorerCharacteristicActionDialog.this.dismiss();
                }
            });
        }
        if ((this.myCharacteristic.getProperties() & 32) != 32) {
            tableRow4.setEnabled(false);
            textView4.setEnabled(false);
        } else {
            byte[] value2 = this.myCharacteristic.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG).getValue();
            if (value2 == null) {
                dismiss();
                return null;
            }
            if (value2[0] == 2) {
                textView3.setText("Disable Indications");
            }
            tableRow4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.d(ServiceExplorerCharacteristicActionDialog.TAG, "Indication touched");
                    if (ServiceExplorerCharacteristicActionDialog.this.mCB != null) {
                        ServiceExplorerCharacteristicActionDialog.this.mCB.shallSetIndication();
                    }
                    ServiceExplorerCharacteristicActionDialog.this.dismiss();
                }
            });
        }
        negativeButton.setView(inflate);
        return negativeButton.create();
    }

    static byte[] getBytesFromString(String str) {
        String[] split = str.split("\\,");
        byte[] bArr = new byte[split.length];
        try {
            int length = split.length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                int i3 = i2 + 1;
                bArr[i2] = Byte.decode(split[i]).byteValue();
                i++;
                i2 = i3;
            }
            return bArr;
        } catch (NumberFormatException unused) {
            return null;
        }
    }
}
