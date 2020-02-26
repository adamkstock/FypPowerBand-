package com.p004ti.ble.dmm;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEDevice.BluetoothLEDeviceCB;
import com.p004ti.ble.bluetooth_le_controller.BluetoothLEManager;
import com.p004ti.device_selector.TopLevel_;
import com.p004ti.util.bleUtility;
import com.ti.ble.simplelinkstarter.R;
import java.math.BigInteger;

/* renamed from: com.ti.ble.dmm.TIDMMProvisioningActivity */
public class TIDMMProvisioningActivity extends AppCompatActivity {
    public static String DMM_BLUETOOTH_DEVICE_EXTRA = "com.ti.ble.dmm.DMM_BLUETOOTH_DEVICE_EXTRA";
    public static String TAG = TIDMMProvisioningActivity.class.getSimpleName();

    /* renamed from: cb */
    BluetoothLEDeviceCB f43cb = new BluetoothLEDeviceCB() {
        public void deviceConnectTimedOut(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceDiscoveryTimedOut(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceFailed(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void deviceReady(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void didReadDescriptor(BluetoothLEDevice bluetoothLEDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
        }

        public void didReadRSSI(int i) {
        }

        public void didUpdateCharacteristicIndication(BluetoothLEDevice bluetoothLEDevice) {
        }

        public void mtuValueChanged(int i) {
        }

        public void waitingForConnect(BluetoothLEDevice bluetoothLEDevice, int i, int i2) {
        }

        public void waitingForDiscovery(BluetoothLEDevice bluetoothLEDevice, int i, int i2) {
        }

        public void didUpdateCharacteristicData(BluetoothLEDevice bluetoothLEDevice, final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String str = TIDMMProvisioningActivity.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Got Notification from ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" Value: ");
            sb.append(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
            Log.d(str, sb.toString());
            TIDMMProvisioningActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (bluetoothGattCharacteristic.equals(TIDMMProvisioningActivity.this.provState)) {
                        TIDMMProvisioningActivity.this.setProvGUIBasedOnProvCharValue(bluetoothGattCharacteristic.getValue());
                    }
                }
            });
        }

        public void didReadCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String str = TIDMMProvisioningActivity.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Did read ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" - Value: ");
            sb.append(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
            Log.d(str, sb.toString());
            if (bluetoothGattCharacteristic.equals(TIDMMProvisioningActivity.this.provState)) {
                TIDMMProvisioningActivity.this.setProvGUIBasedOnProvCharValue(bluetoothGattCharacteristic.getValue());
            }
        }

        public void didUpdateCharacteristicNotification(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String str = TIDMMProvisioningActivity.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Notification state for ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            Log.d(str, sb.toString());
        }

        public void didWriteCharacteristicData(BluetoothLEDevice bluetoothLEDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            String str = TIDMMProvisioningActivity.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Did write ");
            sb.append(bluetoothGattCharacteristic.getUuid().toString());
            sb.append(" - Value: ");
            sb.append(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
            Log.d(str, sb.toString());
        }

        public void deviceDidDisconnect(BluetoothLEDevice bluetoothLEDevice) {
            Log.d(TIDMMProvisioningActivity.TAG, "Device disconnected, must be handled !");
            TIDMMProvisioningActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Builder builder = new Builder(TIDMMProvisioningActivity.this.mThis);
                    builder.setTitle("Device disconnected !");
                    builder.setMessage("After disconnection from the network the device is reset, and disconnected from BLE");
                    builder.setPositiveButton("OK", new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TIDMMProvisioningActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(TIDMMProvisioningActivity.this.mThis, TopLevel_.class);
                                    intent.addFlags(67108864);
                                    TIDMMProvisioningActivity.this.mThis.startActivity(intent);
                                    TIDMMProvisioningActivity.this.mThis.sendBroadcast(intent);
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic channelMask;
    private TextView channelMaskDesc;
    private TextView channelMaskHighDesc;
    private TextView channelMaskHighLabel;
    private TextView channelMaskLowDesc;
    private TextView channelMaskLowLabel;
    private byte lastProvState = 0;
    /* access modifiers changed from: private */
    public boolean localProvState = false;
    private BluetoothDevice mBTDevice;
    /* access modifiers changed from: private */
    public BluetoothLEDevice mBTLEDevice;
    TIDMMProvisioningActivity mThis = this;
    private BluetoothLEDeviceCB oldCB;
    private BluetoothGattCharacteristic panID;
    private TextView panIDDesc;
    /* access modifiers changed from: private */
    public TextView panIDLabel;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic provState;
    private ImageView provisioningIcon;
    private TextView provisioningStateDesc;
    /* access modifiers changed from: private */
    public TextView provisioningStateLabel;
    private Button setChannelMask;
    private Button setPanIDButton;
    /* access modifiers changed from: private */
    public Button startProvButton;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic startProvisioning;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_dmm_provisioning);
        this.mBTDevice = (BluetoothDevice) getIntent().getParcelableExtra(DMM_BLUETOOTH_DEVICE_EXTRA);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Got bluetooth device: ");
        sb.append(this.mBTDevice.getAddress());
        Log.d(str, sb.toString());
        this.mBTLEDevice = BluetoothLEManager.getInstance(this.mThis).deviceForBluetoothDev(this.mBTDevice);
        this.oldCB = this.mBTLEDevice.myCB;
        this.mBTLEDevice.myCB = this.f43cb;
        this.channelMaskDesc = (TextView) findViewById(R.id.adp_current_network_mask_label);
        this.channelMaskHighDesc = (TextView) findViewById(R.id.adp_ch_mask_high_desc);
        this.channelMaskLowDesc = (TextView) findViewById(R.id.adp_ch_mask_low_desc);
        this.panIDDesc = (TextView) findViewById(R.id.adp_current_pan_id_desc);
        this.provisioningStateDesc = (TextView) findViewById(R.id.adp_title);
        this.panIDLabel = (TextView) findViewById(R.id.adp_current_pan_id_text);
        this.channelMaskHighLabel = (TextView) findViewById(R.id.adp_channel_mask_high_label);
        this.channelMaskLowLabel = (TextView) findViewById(R.id.adp_channel_mask_low_label);
        this.setPanIDButton = (Button) findViewById(R.id.adp_set_pan_id_button);
        this.startProvButton = (Button) findViewById(R.id.adp_start_prov_button);
        this.setChannelMask = (Button) findViewById(R.id.adp_set_network_mask_button);
        this.provisioningStateLabel = (TextView) findViewById(R.id.adp_provisioning_state_text);
        this.provisioningIcon = (ImageView) findViewById(R.id.adp_provisioning_icon);
        this.provisioningStateDesc.setText("Device is not paired, starting pairing.");
        this.provisioningStateLabel.setText("See in statusbar for pairing request !");
        this.channelMaskDesc.setVisibility(4);
        this.panIDLabel.setVisibility(4);
        this.panIDDesc.setVisibility(4);
        this.channelMaskHighLabel.setVisibility(4);
        this.channelMaskHighDesc.setVisibility(4);
        this.channelMaskLowDesc.setVisibility(4);
        this.channelMaskLowLabel.setVisibility(4);
        this.setPanIDButton.setVisibility(4);
        this.startProvButton.setVisibility(4);
        this.setChannelMask.setVisibility(4);
        if (this.mBTLEDevice.isPaired()) {
            populateGUIAfterBonding();
        } else {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TIDMMProvisioningActivity.this.mBTLEDevice.startPairing();
                    while (!TIDMMProvisioningActivity.this.mBTLEDevice.isPaired()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (TIDMMProvisioningActivity.this.mBTLEDevice.isPaired()) {
                        TIDMMProvisioningActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                TIDMMProvisioningActivity.this.provisioningStateLabel.setText("Bonded, reading provisioning values ...");
                                TIDMMProvisioningActivity.this.populateGUIAfterBonding();
                            }
                        });
                    } else {
                        TIDMMProvisioningActivity.this.provisioningStateLabel.setText("Could not pair...");
                    }
                }
            }).start();
        }
    }

    /* access modifiers changed from: private */
    public void populateGUIAfterBonding() {
        this.provisioningStateDesc.setText("Current provisioning state:");
        this.panIDLabel.setVisibility(0);
        this.panIDDesc.setVisibility(0);
        this.channelMaskHighLabel.setVisibility(0);
        this.channelMaskHighDesc.setVisibility(0);
        this.channelMaskLowDesc.setVisibility(0);
        this.channelMaskLowLabel.setVisibility(0);
        this.setPanIDButton.setVisibility(0);
        this.startProvButton.setVisibility(0);
        this.setChannelMask.setVisibility(0);
        for (BluetoothGattService bluetoothGattService : this.mBTLEDevice.services) {
            if (bluetoothGattService.getUuid().toString().equalsIgnoreCase(TIDMMProvisioningService.TIDMM_PROVISIONING_SERVICE_UUID)) {
                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
                    if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIDMMProvisioningService.TIDMM_PROVISIONING_NETWORK_PROVISIONING_STATE_CHAR)) {
                        this.provState = bluetoothGattCharacteristic;
                        this.mBTLEDevice.setCharacteristicNotificationSync(this.provState, true);
                        this.mBTLEDevice.readCharacteristicSync(this.provState);
                    } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIDMMProvisioningService.TIDMM_PROVISIONING_NETWORK_CHANNEL_MASK_CHAR)) {
                        this.channelMask = bluetoothGattCharacteristic;
                    } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIDMMProvisioningService.TIDMM_PROVISIONING_START_NETWORK_PROVISIONING_CHAR)) {
                        this.startProvisioning = bluetoothGattCharacteristic;
                    } else if (bluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(TIDMMProvisioningService.TIDMM_PROVISIONING_NETWORK_PAN_ID_CHAR)) {
                        this.panID = bluetoothGattCharacteristic;
                    }
                }
            }
        }
        this.panIDLabel.setText("0xFFFF");
        this.channelMaskHighLabel.setText("----------------");
        this.channelMaskLowLabel.setText("------------------");
        this.startProvButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                byte b;
                if (TIDMMProvisioningActivity.this.localProvState) {
                    b = -35;
                    TIDMMProvisioningActivity.this.setProvButtonBasedOnProvState();
                    Log.d(TIDMMProvisioningActivity.TAG, "Stopped provisioning");
                    TIDMMProvisioningActivity.this.localProvState = false;
                } else {
                    b = -86;
                    TIDMMProvisioningActivity.this.setProvButtonBasedOnProvState();
                    Log.d(TIDMMProvisioningActivity.TAG, "Started provisioning");
                    TIDMMProvisioningActivity.this.localProvState = true;
                }
                Log.d(TIDMMProvisioningActivity.TAG, String.format("Write provisioning characteristic status: %d", new Object[]{Integer.valueOf(TIDMMProvisioningActivity.this.mThis.mBTLEDevice.writeCharacteristicSync(TIDMMProvisioningActivity.this.mThis.startProvisioning, b))}));
            }
        });
        this.setPanIDButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(TIDMMProvisioningActivity.this.mThis);
                builder.setTitle("Please enter PAN ID");
                final EditText editText = new EditText(TIDMMProvisioningActivity.this.mThis);
                editText.setInputType(1);
                editText.setText(TIDMMProvisioningActivity.this.panIDLabel.getText());
                editText.setHint("PAN ID");
                TextInputLayout textInputLayout = new TextInputLayout(TIDMMProvisioningActivity.this.mThis);
                textInputLayout.setPadding(20, 20, 20, 20);
                textInputLayout.addView(editText);
                builder.setView(textInputLayout);
                builder.setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = TIDMMProvisioningActivity.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Text entered: ");
                        sb.append(editText.getText().toString());
                        Log.d(str, sb.toString());
                        Long decode = Long.decode(editText.getText().toString());
                        String str2 = TIDMMProvisioningActivity.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Converted to number: ");
                        sb2.append(decode.toString());
                        sb2.append(" Bytes:");
                        sb2.append(String.format("0x%02x,0x%02x", new Object[]{Byte.valueOf(bleUtility.GET_LOW_BYTE_FROM_UINT16(decode.intValue())), Byte.valueOf(bleUtility.GET_HIGH_BYTE_FROM_UINT16(decode.intValue()))}));
                        Log.d(str2, sb2.toString());
                        byte[] bArr = new byte[2];
                        bArr[1] = bleUtility.GET_LOW_BYTE_FROM_UINT16(decode.intValue());
                        bArr[0] = bleUtility.GET_HIGH_BYTE_FROM_UINT16(decode.intValue());
                        TIDMMProvisioningActivity.this.writePANIdToDevice(bArr);
                    }
                });
                builder.setNegativeButton("Cancel", new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TIDMMProvisioningActivity.TAG, "Dialog Canceled");
                    }
                });
                builder.show();
            }
        });
        this.setChannelMask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(TIDMMProvisioningActivity.this.mThis);
                builder.setTitle("Enter Channel Mask in hex");
                final EditText editText = new EditText(TIDMMProvisioningActivity.this.mThis);
                editText.setInputType(1);
                TIDMMProvisioningActivity.this.mBTLEDevice.readCharacteristicAsync(TIDMMProvisioningActivity.this.channelMask);
                BigInteger bigInteger = new BigInteger(TIDMMProvisioningActivity.this.channelMask.getValue());
                StringBuilder sb = new StringBuilder();
                sb.append("0x");
                sb.append(bigInteger.toString(16));
                editText.setText(sb.toString());
                editText.setHint("Channel Mask");
                TextInputLayout textInputLayout = new TextInputLayout(TIDMMProvisioningActivity.this.mThis);
                textInputLayout.setPadding(20, 20, 20, 20);
                textInputLayout.addView(editText);
                builder.setView(textInputLayout);
                builder.setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = TIDMMProvisioningActivity.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Text entered: ");
                        sb.append(editText.getText().toString());
                        Log.d(str, sb.toString());
                        String obj = editText.getText().toString();
                        if (obj.contains("0x") || obj.contains("0X")) {
                            obj = obj.substring(2);
                        }
                        byte[] byteArray = new BigInteger(obj, 16).toByteArray();
                        if (byteArray.length < 17) {
                            Log.d(TIDMMProvisioningActivity.TAG, "New channel mask is too short, padding it");
                        }
                        TIDMMProvisioningActivity.this.writeNewChannelMaskToDevice(byteArray);
                    }
                });
                builder.setNegativeButton("Cancel", new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TIDMMProvisioningActivity.TAG, "Dialog Canceled");
                    }
                });
                builder.show();
            }
        });
        if (this.provState.getValue() != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Got provisioning state: ");
            sb.append(this.mBTLEDevice.readCharacteristicAsync(this.provState));
            sb.append(" Prov State: ");
            sb.append(String.format("(0x%x)", new Object[]{Byte.valueOf(this.provState.getValue()[0])}));
            Log.d(str, sb.toString());
            setProvGUIBasedOnProvCharValue(this.provState.getValue());
        }
    }

    /* access modifiers changed from: private */
    public void writeNewChannelMaskToDevice(byte[] bArr) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Writing new PAN ID: ");
        sb.append(bleUtility.getStringFromByteVector(bArr));
        Log.d(str, sb.toString());
        Log.d(TAG, String.format("Write status: %d", new Object[]{Integer.valueOf(this.mBTLEDevice.writeCharacteristicAsync(this.channelMask, bArr))}));
    }

    /* access modifiers changed from: private */
    public void writePANIdToDevice(byte[] bArr) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Writing new PAN ID:");
        sb.append(bleUtility.getStringFromByteVector(bArr));
        Log.d(str, sb.toString());
        Log.d(TAG, String.format("Write status: %d", new Object[]{Integer.valueOf(this.mBTLEDevice.writeCharacteristicSync(this.panID, bArr))}));
        this.mBTLEDevice.readCharacteristicAsync(this.panID);
        if (this.panID.getValue() != null) {
            this.panIDLabel.setText(String.format("0x%04x", new Object[]{Integer.valueOf(bleUtility.BUILD_UINT16(this.panID.getValue()[0], this.panID.getValue()[1]))}));
        }
    }

    /* access modifiers changed from: private */
    public void setProvButtonBasedOnProvState() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (!TIDMMProvisioningActivity.this.localProvState) {
                    TIDMMProvisioningActivity.this.startProvButton.setText("Start provisioning (connect)");
                } else {
                    TIDMMProvisioningActivity.this.startProvButton.setText("Stop provisioning (disconnect)");
                }
            }
        });
    }

    /* JADX WARNING: type inference failed for: r0v4 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r0v3, types: [byte] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProvGUIBasedOnProvCharValue(byte[] r10) {
        /*
            r9 = this;
            android.bluetooth.BluetoothGattCharacteristic r0 = r9.provState
            byte[] r0 = r0.getValue()
            r1 = 0
            byte r0 = r0[r1]
            byte r2 = r9.lastProvState
            r3 = 1
            if (r0 == r2) goto L_0x00cc
            int r2 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_ZIGBEE_PROVISIONING_STATE_JOINING
            if (r0 == r2) goto L_0x001e
            int r2 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_ZIGBEE_PROVISIONING_STATE_ORPHANED_FROM_NETWORK
            if (r0 == r2) goto L_0x001e
            int r2 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_ZIGBEE_PROVISIONING_STATE_END_DEVICE_AUTH
            if (r0 == r2) goto L_0x001e
            int r2 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_ZIGBEE_PROVISIONING_STATE_JOINED_SEC_CURR_CHANNEL
            if (r0 != r2) goto L_0x00ca
        L_0x001e:
            com.ti.ble.bluetooth_le_controller.BluetoothLEDevice r2 = r9.mBTLEDevice
            android.bluetooth.BluetoothGattCharacteristic r4 = r9.panID
            r2.readCharacteristicAsync(r4)
            android.bluetooth.BluetoothGattCharacteristic r2 = r9.panID
            byte[] r2 = r2.getValue()
            if (r2 == 0) goto L_0x0054
            android.widget.TextView r2 = r9.panIDLabel
            java.lang.Object[] r4 = new java.lang.Object[r3]
            android.bluetooth.BluetoothGattCharacteristic r5 = r9.panID
            byte[] r5 = r5.getValue()
            byte r5 = r5[r1]
            android.bluetooth.BluetoothGattCharacteristic r6 = r9.panID
            byte[] r6 = r6.getValue()
            byte r6 = r6[r3]
            int r5 = com.p004ti.util.bleUtility.BUILD_UINT16(r5, r6)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4[r1] = r5
            java.lang.String r5 = "0x%04x"
            java.lang.String r4 = java.lang.String.format(r5, r4)
            r2.setText(r4)
        L_0x0054:
            com.ti.ble.bluetooth_le_controller.BluetoothLEDevice r2 = r9.mBTLEDevice
            android.bluetooth.BluetoothGattCharacteristic r4 = r9.channelMask
            r2.readCharacteristicAsync(r4)
            android.bluetooth.BluetoothGattCharacteristic r2 = r9.channelMask
            byte[] r2 = r2.getValue()
            if (r2 == 0) goto L_0x00ae
            java.math.BigInteger r2 = new java.math.BigInteger
            android.bluetooth.BluetoothGattCharacteristic r4 = r9.channelMask
            byte[] r4 = r4.getValue()
            r2.<init>(r4)
            android.widget.TextView r4 = r9.channelMaskLowLabel
            java.lang.Object[] r5 = new java.lang.Object[r3]
            long r6 = r2.longValue()
            java.lang.Long r6 = java.lang.Long.valueOf(r6)
            r5[r1] = r6
            java.lang.String r6 = "0x%016x"
            java.lang.String r5 = java.lang.String.format(r6, r5)
            r4.setText(r5)
            int r4 = r2.bitCount()
            r5 = 64
            if (r4 >= r5) goto L_0x0095
            android.widget.TextView r2 = r9.channelMaskHighLabel
            java.lang.String r4 = "0x00000000000000000"
            r2.setText(r4)
            goto L_0x00ae
        L_0x0095:
            java.math.BigInteger r2 = r2.shiftRight(r5)
            android.widget.TextView r4 = r9.channelMaskHighLabel
            java.lang.Object[] r5 = new java.lang.Object[r3]
            long r7 = r2.longValue()
            java.lang.Long r2 = java.lang.Long.valueOf(r7)
            r5[r1] = r2
            java.lang.String r2 = java.lang.String.format(r6, r5)
            r4.setText(r2)
        L_0x00ae:
            r9.localProvState = r3
            int r2 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_ZIGBEE_PROVISIONING_STATE_END_DEVICE_AUTH
            if (r0 == r2) goto L_0x00c2
            int r2 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_ZIGBEE_PROVISIONING_STATE_JOINED_SEC_CURR_CHANNEL
            if (r0 != r2) goto L_0x00b9
            goto L_0x00c2
        L_0x00b9:
            android.widget.ImageView r2 = r9.provisioningIcon
            r4 = 2131492898(0x7f0c0022, float:1.860926E38)
            r2.setImageResource(r4)
            goto L_0x00ca
        L_0x00c2:
            android.widget.ImageView r2 = r9.provisioningIcon
            r4 = 2131492897(0x7f0c0021, float:1.8609259E38)
            r2.setImageResource(r4)
        L_0x00ca:
            r9.lastProvState = r0
        L_0x00cc:
            byte r0 = r10[r1]
            int r2 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_PROVISIONING_STATE_STRINGS_COUNT
            if (r0 < r2) goto L_0x00d5
            int r0 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_PROVISIONING_STATE_STRINGS_COUNT
            int r0 = r0 - r3
        L_0x00d5:
            android.widget.TextView r2 = r9.provisioningStateLabel
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String[] r5 = com.p004ti.ble.dmm.TIDMMDefines.TI_DMM_PROVISIONING_STATE_STRINGS
            r0 = r5[r0]
            r4.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r3]
            byte r10 = r10[r1]
            java.lang.Byte r10 = java.lang.Byte.valueOf(r10)
            r0[r1] = r10
            java.lang.String r10 = " (0x%02x)"
            java.lang.String r10 = java.lang.String.format(r10, r0)
            r4.append(r10)
            java.lang.String r10 = r4.toString()
            r2.setText(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p004ti.ble.dmm.TIDMMProvisioningActivity.setProvGUIBasedOnProvCharValue(byte[]):void");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mBTLEDevice.myCB = this.oldCB;
    }
}
